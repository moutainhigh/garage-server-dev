package com.yixin.garage.service.inventory.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yixin.common.exception.BzException;
import com.yixin.common.utils.DateUitls;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.auth.CurrentUser;
import com.yixin.garage.core.base.BaseAssembler;
import com.yixin.garage.dao.garage.SgGarageInfoMapper;
import com.yixin.garage.dao.inventory.SgInventoryApprovalBillMapper;
import com.yixin.garage.dao.inventory.SgInventoryApprovalDetailMapper;
import com.yixin.garage.dao.inventory.SgInventoryHomeMapper;
import com.yixin.garage.dto.inventory.*;
import com.yixin.garage.dto.sys.SysUserDTO;
import com.yixin.garage.entity.garage.SgGarageInfo;
import com.yixin.garage.entity.inventory.SgInventoryApprovalBill;
import com.yixin.garage.entity.inventory.SgInventoryApprovalDetail;
import com.yixin.garage.entity.inventory.SgInventoryHome;
import com.yixin.garage.entity.inventory.SgInventoryLog;
import com.yixin.garage.enums.RoleEnum;
import com.yixin.garage.enums.garage.inventory.InventoryApprovalStateEnum;
import com.yixin.garage.enums.garage.inventory.InventoryBillStateEnum;
import com.yixin.garage.enums.garage.inventory.InventoryHomeStateEnum;
import com.yixin.garage.enums.garage.inventory.InventoryResultTypeEnum;
import com.yixin.garage.service.inventory.ISgInventoryApprovalBillService;
import com.yixin.garage.service.inventory.ISgInventoryLogService;
import com.yixin.garage.service.sys.IUserService;
import com.yixin.garage.util.BeanUtil;
import com.yixin.garage.util.MessageUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author libochen
 * @since 2019-10-11
 */
@Service
@Transactional
public class SgInventoryApprovalBillServiceImpl extends ServiceImpl<SgInventoryApprovalBillMapper, SgInventoryApprovalBill> implements ISgInventoryApprovalBillService {

    private Logger logger = LoggerFactory.getLogger(SgInventoryApprovalBillServiceImpl.class);

    @Autowired
    SgInventoryApprovalDetailMapper sgInventoryDetailMapper;

    @Autowired
    SgInventoryHomeMapper sgInventoryHomeMapper;

    @Autowired
    SgGarageInfoMapper sgGarageInfoMapper;

    @Autowired
    private IUserService userService;

    @Autowired
    private ISgInventoryLogService inventoryLogService;

    @Value("${shiro.casService}")
    private String casService;

    @Value("${mailTo}")
    private String mailTo;

    @Override
    public List<SgInventoryBillDTO> pageQueryBill(SgInventoryBillDTO dto) throws BzException {
        logger.info("分页查询主单任务pageQueryBill入参：{}", JSONObject.toJSON(dto));
        Boolean hasRoles = false;
        String userName = null;
        BeanUtil.setEmptyStrFields2Null(dto);
        if (dto.getUserName() != null && !dto.getUserName().equals("")) {
            //app调用接口
            List<SysUserDTO> manageNameList = userService.getUserInfoByRoleName(RoleEnum.GARAGE_MANAGE_ROLE.getRoleName());
            if(CollectionUtils.isNotEmpty(manageNameList)){
                userName = dto.getUserName();
                for(SysUserDTO usetDto : manageNameList){
                    if(usetDto.getDomainName().equals(dto.getUserName())){
                        hasRoles = true;
                        break;
                    }
                }
            }
        }else{
            //判断当前登录人是否有库管权限
            hasRoles = CurrentUser.hasRole(RoleEnum.GARAGE_MANAGE_ROLE);
            userName = CurrentUser.getUsername();
            logger.info("当前登录人为：" + CurrentUser.getCnName()+"是否存在库管权限："+ hasRoles);
        }
        List<SgInventoryBillDTO> pageProject = this.baseMapper.pageQueryBill(dto,hasRoles,userName);
        return pageProject;
    }


    @Override
    public SgInventoryBillReturnDTO getBillView(SgInventoryDetailsDTO dto) throws BzException {
        logger.info("查询主单任务详情getBillView入参：{}", JSONObject.toJSON(dto));
        SgInventoryBillReturnDTO result = new SgInventoryBillReturnDTO();
        List<SgInventoryDetailsDTO> inGarageVehicles = new ArrayList<>();
        List<SgInventoryDetailsDTO> othersInVehicles = new ArrayList<>();
        QueryWrapper<SgInventoryApprovalDetail> detailInQueryWrapper = new QueryWrapper();
        QueryWrapper<SgInventoryApprovalDetail> otherdetailInQueryWrapper = new QueryWrapper();
        try {
            //根据任务主单获取数据
            SgInventoryApprovalBill bill = this.baseMapper.selectById(dto.getSgBillId());
            BeanUtil.setEmptyStrFields2Null(dto);
            //审批意见
            result.setApprovalOpinion(bill.getApprovalOpinion());
            result.setBillState(bill.getApprovalState());

            //判断查询条件
            if (StringUtils.isNotEmpty(dto.getVin())) {
                detailInQueryWrapper.like("vin", dto.getVin());
                otherdetailInQueryWrapper.like("vin", dto.getVin());
            }
            if(null!=dto.getInventory()){
                detailInQueryWrapper.eq("inventory", dto.getInventory());
            }

            if (null != dto.getApprovalStatus() && !dto.getApprovalStatus().equals("")) {
                detailInQueryWrapper.eq("approval_status", dto.getApprovalStatus());
            }

            //系统在库车辆
            detailInQueryWrapper.eq("IS_DELETED", false);
            detailInQueryWrapper.eq("inventory_approval_bill_id", dto.getSgBillId());
            detailInQueryWrapper.eq("manual_add", false);//是否为手动添加
            List<SgInventoryApprovalDetail> inDetails = sgInventoryDetailMapper.selectList(detailInQueryWrapper);
            //将数据放入dto
            for(SgInventoryApprovalDetail in : inDetails){
                SgInventoryDetailsDTO inDTO = new SgInventoryDetailsDTO();
                BaseAssembler.mapObjWithoutNull(in, inDTO);
                inGarageVehicles.add(inDTO);
            }
            result.setInGarageVehicles(inGarageVehicles);

            //系统手动新增车辆
            otherdetailInQueryWrapper.eq("IS_DELETED", false);
            otherdetailInQueryWrapper.eq("inventory_approval_bill_id", dto.getSgBillId());
            otherdetailInQueryWrapper.eq("manual_add", true);//是否为手动添加
            List<SgInventoryApprovalDetail> otherInDetails = sgInventoryDetailMapper.selectList(otherdetailInQueryWrapper);
            //将数据放入dto
            for(SgInventoryApprovalDetail other: otherInDetails){
                SgInventoryDetailsDTO otherDTO = new SgInventoryDetailsDTO();
                BaseAssembler.mapObjWithoutNull(other, otherDTO);
                othersInVehicles.add(otherDTO);
            }
            result.setOthersInVehicles(othersInVehicles);

            //查询清单数量
            //待修改
            SgInventoryNumDTO inventoryNumDto = new SgInventoryNumDTO();


            //清单数量、实际盘点数量
            QueryWrapper<SgInventoryApprovalDetail> allNumQueryWrapper = new QueryWrapper();
            allNumQueryWrapper.eq("IS_DELETED", "false");
            allNumQueryWrapper.eq("inventory_approval_bill_id", bill.getId());//当前任务
            allNumQueryWrapper.eq("manual_add", false);//当前任务
            Integer allNumCount = sgInventoryDetailMapper.selectCount(allNumQueryWrapper);

            QueryWrapper<SgInventoryApprovalDetail> pandianNumQueryWrapper = new QueryWrapper();
            pandianNumQueryWrapper.eq("IS_DELETED", "false");
            pandianNumQueryWrapper.eq("inventory_approval_bill_id", bill.getId());//当前任务
            pandianNumQueryWrapper.eq("inventory", true);//是否盘点
            Integer pandianNumCount = sgInventoryDetailMapper.selectCount(pandianNumQueryWrapper);
            inventoryNumDto.setActualNum(Integer.toString(pandianNumCount));
            inventoryNumDto.setListNum(Integer.toString(allNumCount));
            result.setInventoryNum(inventoryNumDto);
        } catch (BzException e) {
            logger.error("createGarage() failed：", e);
            throw new BzException(e.getMessage());
        }
        return result;
    }

    @Override
    public InvokeResult<String> submitBill(SgInventoryBillDTO dto) throws BzException {
        logger.info("盘点单个任务提交入参：{}", JSONObject.toJSON(dto));
        InvokeResult<String> result = new InvokeResult<String>();
        SgInventoryApprovalBill bill = this.baseMapper.selectById(dto.getId());
        if(bill.getApprovalState() == InventoryBillStateEnum.UN_COMMIT.getValue()
                ||bill.getApprovalState() == InventoryBillStateEnum.REJECT.getValue()){

            QueryWrapper<SgInventoryApprovalDetail> detailQueryWrapper = new QueryWrapper();
            detailQueryWrapper.eq("IS_DELETED", false);
            detailQueryWrapper.eq("inventory_approval_bill_id", dto.getId());

            List<SgInventoryApprovalDetail> details = sgInventoryDetailMapper.selectList(detailQueryWrapper);
            for (SgInventoryApprovalDetail detail : details) {
                if(!detail.getInventory()){
                    result.failure("存在未盘点数据，请盘点后提交！");
                    return result;
                }
            }
            bill.setApprovalState(InventoryBillStateEnum.PENDING_APPROVAL.getValue());
            if(null!= dto.getUserName() && !dto.getUserName().equals("")){
                bill.setSubmitterName(dto.getUserName());
            }else{
                bill.setSubmitterName(CurrentUser.getCnName());
            }
            Date date = new Date();
            bill.setFinalizedTime(date);
            bill.insertOrUpdate();
            //创建生成盘点日志
            inventoryLogService.createInventoryLog(bill, dto, "盘点提交", null, InventoryBillStateEnum.PENDING_APPROVAL.getName());
        }else{
            result.failure("单据已提交，提交失败！");
            return result;
        }
        return result;
    }


    @Override
    public InvokeResult<String> submitApproveBill(SgInventoryBillDTO dto) throws BzException {
        logger.info("审批单个任务提交入参：{}", JSONObject.toJSON(dto));
        InvokeResult<String> result = new InvokeResult<String>();
        SgInventoryApprovalBill bill = this.baseMapper.selectById(dto.getId());

        QueryWrapper<SgInventoryApprovalDetail> detailQueryWrapper = new QueryWrapper();
        detailQueryWrapper.eq("IS_DELETED", false);
        detailQueryWrapper.eq("inventory_approval_bill_id", dto.getId());
        List<SgInventoryApprovalDetail> details = sgInventoryDetailMapper.selectList(detailQueryWrapper);
        for (SgInventoryApprovalDetail detail : details) {
            if (detail.getApprovalStatus().equals(InventoryApprovalStateEnum.UN_APPROVAL.getValue())) {
                result.failure("存在未审核数据，请审核后提交！");
                return result;
            }
            if (detail.getApprovalStatus().equals(InventoryApprovalStateEnum.UN_QUALIFIED.getValue())) {
                result.failure("存在不合格数据，不合格数据不能选择通过按钮！");
                return result;
            }
        }

        bill.setApprovalOpinion(dto.getApprovalOpinion());//审批意见
        bill.setApprovalState(InventoryBillStateEnum.FINISH.getValue());
        bill.insertOrUpdate();


        //当前任务下所有数据是否审批完毕，如果是的话修改主单状态
        QueryWrapper<SgInventoryApprovalBill> billQueryWrapper = new QueryWrapper();
        Object[] parm = new Object[3];
        parm[0] = InventoryBillStateEnum.PENDING_APPROVAL.getValue();
        parm[1] = InventoryBillStateEnum.UN_COMMIT.getValue();
        parm[2] = InventoryBillStateEnum.REJECT.getValue();
        billQueryWrapper.eq("IS_DELETED", false);
        billQueryWrapper.eq("inventory_home_id", bill.getInventoryHomeId());
        billQueryWrapper.in("approval_state", parm);
        List<SgInventoryApprovalBill> bills = this.baseMapper.selectList(billQueryWrapper);
        if (CollectionUtils.isEmpty(bills)) {
            SgInventoryHome home = sgInventoryHomeMapper.selectById(bill.getInventoryHomeId());
            home.setState(InventoryHomeStateEnum.END.getValue());
            home.insertOrUpdate();
        }

        //创建生成盘点日志
        inventoryLogService.createInventoryLog(bill, dto, "盘点审批通过", dto.getApprovalOpinion(),InventoryBillStateEnum.FINISH.getName());
        return result;
    }


    @Override
    public InvokeResult<String> rejectBill(SgInventoryBillDTO dto) throws BzException {
        logger.info("驳回单个任务提交入参：{}", JSONObject.toJSON(dto));
        InvokeResult<String> result = new InvokeResult<String>();
        SgInventoryApprovalBill bill = this.baseMapper.selectById(dto.getId());
        QueryWrapper<SgInventoryApprovalDetail> detailQueryWrapper = new QueryWrapper();

        detailQueryWrapper.eq("IS_DELETED", false);
        detailQueryWrapper.eq("inventory_approval_bill_id", dto.getId());
        detailQueryWrapper.eq("approval_status", InventoryApprovalStateEnum.UN_APPROVAL.getValue());
        List<SgInventoryApprovalDetail> details = sgInventoryDetailMapper.selectList(detailQueryWrapper);
        if(CollectionUtils.isNotEmpty(details)){
            result.failure("存在未审核数据，请审核后驳回！");
            return result;
        }
        bill.setApprovalOpinion(dto.getApprovalOpinion());//审批意见
        bill.setApprovalState(InventoryBillStateEnum.REJECT.getValue());

        bill.insertOrUpdate();

        //创建生成盘点日志
        inventoryLogService.createInventoryLog(bill, dto, "盘点审批驳回", dto.getApprovalOpinion(), InventoryBillStateEnum.REJECT.getName());
        return result;
    }

    @Override
    public InvokeResult<String> submitSendMail(SgInventoryBillDTO dto) throws BzException {
        logger.info("submitSendMail()通过并上报经理：{}", JSONObject.toJSON(dto));
        InvokeResult<String> result = new InvokeResult<String>();

        QueryWrapper<SgInventoryApprovalDetail> detailQueryWrapper = new QueryWrapper();

        detailQueryWrapper.eq("IS_DELETED", false);
        detailQueryWrapper.eq("inventory_approval_bill_id", dto.getId());
        List<SgInventoryApprovalDetail> details = sgInventoryDetailMapper.selectList(detailQueryWrapper);
        for (SgInventoryApprovalDetail detail : details) {
            if (detail.getApprovalStatus().equals(InventoryApprovalStateEnum.UN_APPROVAL.getValue())) {
                result.failure("存在未审核数据，请审核后提交！");
                return result;
            }
            if (detail.getApprovalStatus().equals(InventoryApprovalStateEnum.UN_QUALIFIED.getValue())) {
                result.failure("存在不合格数据，上报经理失败！");
                return result;
            }
        }

        SgInventoryApprovalBill bill = this.baseMapper.selectById(dto.getId());
        bill.setApprovalState(InventoryBillStateEnum.FINISH.getValue());
        bill.insertOrUpdate();

        SgInventoryHome home = sgInventoryHomeMapper.selectById(bill.getInventoryHomeId());
        //当前任务下所有数据是否审批完毕，如果是的话修改主单状态
        QueryWrapper<SgInventoryApprovalBill> billQueryWrapper = new QueryWrapper();
        Object[] parm = new Object[3];
        parm[0] = InventoryBillStateEnum.PENDING_APPROVAL.getValue();
        parm[1] = InventoryBillStateEnum.UN_COMMIT.getValue();
        parm[2] = InventoryBillStateEnum.REJECT.getValue();
        billQueryWrapper.eq("IS_DELETED", false);
        billQueryWrapper.eq("inventory_home_id", bill.getInventoryHomeId());
        billQueryWrapper.in("approval_state", parm);
        List<SgInventoryApprovalBill> bills = this.baseMapper.selectList(billQueryWrapper);
        if (CollectionUtils.isEmpty(bills)) {
            home.setState(InventoryHomeStateEnum.END.getValue());
            home.insertOrUpdate();
        }

        SgGarageInfo garageInfo = sgGarageInfoMapper.selectById(bill.getGarageInfoId());

        String billNum = home.getBillNum();
        dto.setInventoryHomeId(home.getId());
        dto.setGarageInfoId(bill.getGarageInfoId());
        logger.info("发送邮件：{}", JSONObject.toJSON(dto));
        StringBuilder content = new StringBuilder();
        //style='color:red'
        content.append("<span size =\"10\">");
        content.append("盘点期数：").append(billNum).append("<br/>");
        //拼接查看地址
        String casServiceUrl = casService.substring(0, casService.length() - 10);//前缀
        casServiceUrl = casServiceUrl + "/garage-vue/#/disk_warehouse_approvalDetails?sgBillId=" +bill.getId()+"&type=view";
        content.append("详情请查看：").append(casServiceUrl);
        content.append("</span><br/>").append("<br/>");
        //&nbsp;
//        content.append("&nbsp;&nbsp;&nbsp;&nbsp;<span size =\"10\" face=\"宋体\"> ————————————————————————————————").append("</span><br/>");

        content.append("<span size =\"10\" face=\"宋体\">");
        content.append(garageInfo.getGarageName()).append("<br/>");

        String stockQuantity = sgInventoryDetailMapper.resultStatistics(dto, null,false);
        content.append("&nbsp;&nbsp;&nbsp;&nbsp;").append("存量在库车辆：【").append(stockQuantity).append("】").append("<br/>");


        String zaikuCount = sgInventoryDetailMapper.resultStatistics(dto, InventoryResultTypeEnum.FREE.getValue(),false);
        content.append("&nbsp;&nbsp;&nbsp;&nbsp;").append("在库：【").append(zaikuCount).append("】").append("<br/>");

        String linshiCount = sgInventoryDetailMapper.resultStatistics(dto, InventoryResultTypeEnum.TEMP_GARAGE_OUT.getValue(),false);
        content.append("&nbsp;&nbsp;&nbsp;&nbsp;").append("临时出库：【").append(linshiCount).append("】").append("<br/>");

        String weizaikuCount = sgInventoryDetailMapper.resultStatistics(dto, InventoryResultTypeEnum.NOT_FOUND.getValue(),false);
        content.append("&nbsp;&nbsp;&nbsp;&nbsp;")
                .append("<span size =\"10\" style='color:red' face=\"宋体\">")
                .append("未在库：【").append(weizaikuCount).append("】")
                .append("</span><br/>");

        String xubutuiCount = sgInventoryDetailMapper.resultStatistics(dto, null,true);
        content.append("&nbsp;&nbsp;&nbsp;&nbsp;")
                .append("<span size =\"10\" style='color:red' face=\"宋体\">")
                .append("需补推入库：【").append(xubutuiCount).append("】")
                .append("</span><br/>");

        content.append("</span><br/>");

        String title = "'安全车库-盘库异常提醒'";
        MessageUtil.sendMail(content.toString(), title, mailTo);

        StringBuilder contentRemake = new StringBuilder();
        contentRemake.append("盘点期数：").append(billNum)
                .append("，存量在库车辆：").append(stockQuantity)
                .append("，在库：").append(zaikuCount)
                .append("，临时出库：").append(linshiCount)
                .append("，未在库：").append(weizaikuCount)
                .append("，需补推入库：").append(xubutuiCount);

        //创建生成盘点日志
        inventoryLogService.createInventoryLog(bill, dto, "盘点通过并上报总经理", contentRemake.toString(), InventoryBillStateEnum.FINISH.getName());
        return result;
    }


}


