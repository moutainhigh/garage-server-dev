package com.yixin.garage.service.order.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yixin.common.exception.BzException;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.auth.CurrentUser;
import com.yixin.garage.core.base.BaseAssembler;
import com.yixin.garage.core.common.factory.PageFactory;
import com.yixin.garage.dao.AttachSourceMapper;
import com.yixin.garage.dao.garage.SgContactToGarageTempMapper;
import com.yixin.garage.dao.garage.SgGarageInfoMapper;
import com.yixin.garage.dao.order.SgGarageDetailMapper;
import com.yixin.garage.dao.order.SgGarageOrderLogMapper;
import com.yixin.garage.dao.order.SgGarageOrderMapper;
import com.yixin.garage.dao.vehicle.SgVehicleInfoMapper;
import com.yixin.garage.dao.vehicle.SgVehicleLogMapper;
import com.yixin.garage.dto.api.forLoan.SgQueryVehicleIfoStatDTO;
import com.yixin.garage.dto.order.SgGarageDetailDTO;
import com.yixin.garage.dto.order.SgGarageOrderDTO;
import com.yixin.garage.dto.sys.AttachSourceDTO;
import com.yixin.garage.dto.sys.VehicleInoutRecordDTO;
import com.yixin.garage.dto.vehicle.SgVehicleInfoDTO;
import com.yixin.garage.entity.AttachSource;
import com.yixin.garage.entity.garage.SgContactToGarageTemp;
import com.yixin.garage.entity.garage.SgGarageInfo;
import com.yixin.garage.entity.order.SgGarageDetail;
import com.yixin.garage.entity.order.SgGarageOrder;
import com.yixin.garage.entity.order.SgGarageOrderLog;
import com.yixin.garage.entity.vehicle.SgVehicleInfo;
import com.yixin.garage.entity.vehicle.SgVehicleLog;
import com.yixin.garage.enums.RcAttachmentTypeEnum;
import com.yixin.garage.enums.RoleEnum;
import com.yixin.garage.enums.garage.*;
import com.yixin.garage.service.order.ISgGarageOrderService;
import com.yixin.garage.util.CommonFileUtil;
import com.yixin.garage.util.RestUtil;
import com.yixin.garage.util.ResultUtil;
import com.yixin.garage.util.TaskNumUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.yixin.garage.util.TaskNumUtil.createTaskNum;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liyaqing
 * @since 2019-08-19
 */
@Transactional
@Service
public class SgGarageOrderServiceImpl extends ServiceImpl<SgGarageOrderMapper, SgGarageOrder> implements ISgGarageOrderService {

    private final static Logger logger = LoggerFactory.getLogger(SgGarageOrderServiceImpl.class);

    @Autowired
    private SgGarageDetailMapper sgGarageDetailMapper;

    @Autowired
    private AttachSourceMapper attachSourceMapper;

    @Autowired
    private SgGarageOrderLogMapper sgGarageOrderLogMapper;

    @Autowired
    private SgGarageOrderMapper sgGarageOrderMapper;

    @Autowired
    private SgGarageInfoMapper sgGarageInfoMapper;

    @Autowired
    private SgVehicleInfoMapper sgVehicleInfoMapper;

    @Autowired
    private SgVehicleLogMapper sgVehicleLogMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private SgContactToGarageTempMapper SgContactToGarageTempMapper;

    @Value("${rentCarUrl}")
    private String rentCarUrl;

    private static final String GARAGEOUT_BRANCH = "临时出库-处理";

    private static final String APPROVE_GARAGEOUT = "出库-审批";

    private static final String REJECT = "驳回";

    private static final String CREATE_TEMP = "新增临时出库";

    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");


    /**
     * @Title: garageOutBranch
     * @Description: 出库处理审批
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/19 23:36
     */
    @Override
    public InvokeResult<String> garageOutBranch(SgGarageOrderDTO sgGarageOrderDTO) throws BzException {
        try {
            logger.info("处理出库代办garageOutBranch() sgGarageOrderDTO=[{}]", JSONObject.toJSONString(sgGarageOrderDTO));

            SgGarageOrder sgGarageOrder = sgGarageOrderMapper.selectById(sgGarageOrderDTO.getId());
            if(sgGarageOrder == null){
                logger.error("根据id["+sgGarageOrderDTO.getId()+"]未查到对应的数据，请刷新页面重新操作！");
                throw new BzException("根据id["+sgGarageOrderDTO.getId()+"]未查到对应的数据，请刷新页面重新操作！");
            }

            //满足条件 出库+临时出库(pushSource)+ 待处理或审批驳回
            SgGarageDetail sgGarageDetail = sgGarageDetailMapper.queryTempOut(sgGarageOrder.getId());
            if(sgGarageDetail == null){
                logger.error("该车辆临时出库处理条件不符合，操作禁止");
                throw new BzException("该车辆临时出库处理条件不符合，操作禁止");
            }

            sgGarageDetail.setBillStatus(SgAllocateTaskStatusEnum.OUT_GARAGING.getValue()); //出库中
            sgGarageDetail.setRemark(sgGarageOrderDTO.getRemark());
            sgGarageDetailMapper.updateById(sgGarageDetail);
            logger.info("===========garageOutBranch(): 出库单更新成功===========");

            BaseAssembler.mapObjWithoutNullAndBaseColumn(sgGarageOrderDTO,sgGarageOrder);
            sgGarageOrder.setOrderStatus(SgAllocateTaskStatusEnum.OUT_GARAGING.getValue()); //出库中
            sgGarageOrderMapper.updateById(sgGarageOrder);
            logger.info("===========garageOutBranch(): 订单表更新成功===========");

            //更新车辆里程
            SgVehicleInfo sgVehicleInfo = sgVehicleInfoMapper.selectById(sgGarageOrder.getSgVehicleId());
            sgVehicleInfo.setMileage(sgGarageOrderDTO.getVehicleInfoDTOList().get(0).getMileage());
            sgVehicleInfoMapper.updateById(sgVehicleInfo);
            logger.info("===========garageOutBranch(): 车辆里程更新成功===========");

            //删除附件
            this.delAtt(sgGarageOrderDTO);
            logger.info("===========garageOutBranch(): 附件删除成功]===========");

            //附件-出库单
            List<String> attIds = sgGarageOrderDTO.getAttIds();
            if(CollectionUtils.isNotEmpty(attIds)){
                for (String attId : attIds) {
                    AttachSource att = attachSourceMapper.selectById(attId);
                    att = CommonFileUtil.saveAtts(att, sgGarageDetail.getId(), RcAttachmentTypeEnum.SG_OUT_IN_GARAGE.getValue());
                    attachSourceMapper.updateById(att);
                }
            }
            logger.info("===========garageOutBranch(): 出库附件上传成功]===========");

            //附件-其他
            List<String> otherAttIds = sgGarageOrderDTO.getOtherAttIds();
            if(CollectionUtils.isNotEmpty(otherAttIds)) {
                for (String attId : otherAttIds) {
                    AttachSource att = attachSourceMapper.selectById(attId);
                    att = CommonFileUtil.saveAtts(att, sgGarageDetail.getId(), RcAttachmentTypeEnum.SG_OTHERS_OF_GARAGE.getValue());
                    attachSourceMapper.updateById(att);
                }
            }
            logger.info("===========garageOutBranch(): 其他附件上传成功]===========");
            //创建日志
            SgGarageOrderLog sgGarageOrderLog = this.createOrderLog(sgGarageOrder, GARAGEOUT_BRANCH);
            sgGarageOrderLogMapper.insert(sgGarageOrderLog);
            logger.info("===========garageOutBranch(): 审批日志添加成功===========");

        } catch (Exception e) {
            logger.error("garageOutBranch() failed：", e.getMessage());
            throw new BzException(e.getMessage());
        }
        return ResultUtil.success("处理成功");
    }

    /**
     * @Title: approveGarageOut
     * @Description: 临时出库-审批
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/20 10:45
     */
    @Override
    public InvokeResult<String> approveGarageOut(SgGarageOrderDTO sgGarageOrderDTO) throws BzException {
        try {
            logger.info("临时出库-审批 approveGarageOut() sgGarageOrderDTO=[{}]", JSONObject.toJSONString(sgGarageOrderDTO));
            SgGarageOrder sgGarageOrder = sgGarageOrderMapper.selectById(sgGarageOrderDTO.getId());
            if(sgGarageOrder == null){
                logger.error("根据id["+sgGarageOrderDTO.getId()+"]未查到对应的数据，请刷新页面重新操作！");
                throw new BzException("根据id["+sgGarageOrderDTO.getId()+"]未查到对应的数据，请刷新页面重新操作！");
            }
            if(sgGarageOrderDTO.getPushSource() == null){
                logger.error("推送来源不允许为空");
                throw new BzException("推送来源不允许为空");
            }
            //更新出库单状态 出库完成
            SgGarageDetail sgGarageDetail = sgGarageDetailMapper.queryOutDeatil(sgGarageOrderDTO);
            SgVehicleInfo sgVehicleInfo = sgVehicleInfoMapper.selectById(sgGarageOrder.getSgVehicleId());
            if(sgVehicleInfo == null){
                logger.error("车辆信息获取失败");
                throw new BzException("车辆信息获取失败");
            }
            Integer oldStat = sgVehicleInfo.getStat();
            SgGarageInfo sgGarageInfo = null;
            //融后推送的车辆，审批完成后，状态为[出库-审批完成]，临时出库的车辆，审批完成为[临时出库],
            if(sgGarageOrderDTO.getPushSource() == 0){
                sgGarageDetail.setBillStatus(SgAllocateTaskStatusEnum.OUTOFSTORE.getValue());
                sgGarageOrder.setOrderStatus(SgAllocateTaskStatusEnum.OUTOFSTORE.getValue());
                sgVehicleInfo.setStat(SgVehicleStatusEnum.DISPOSE_OUT.getValue());  //处置出库
                //更新出库车库
                sgGarageInfo = sgGarageInfoMapper.selectById(sgGarageOrder.getSgGarageInfoFromId());
            }else if(sgGarageOrderDTO.getPushSource() == 1){
                sgGarageDetail.setBillStatus(SgAllocateTaskStatusEnum.TEMP_GARAGE_OUT.getValue());
                sgGarageOrder.setOrderStatus(SgAllocateTaskStatusEnum.TEMP_GARAGE_OUT.getValue());
                sgVehicleInfo.setStat(SgVehicleStatusEnum.TEMP_GARAGE_OUT.getValue());  //临时出库
                //更新出库车库
                sgGarageInfo = sgGarageInfoMapper.selectById(sgVehicleInfo.getSgGarageInfoId());

            }
            sgVehicleInfoMapper.updateById(sgVehicleInfo);
            logger.info("===========approveGarageOut(): 车辆状态更新成功===========");

            if(sgGarageInfo == null){
                logger.error("车库信息获取失败");
                throw new BzException("车库信息获取失败");
            }
            sgGarageInfo.setParkedNum(sgGarageInfo.getParkedNum() - 1);
            sgGarageInfoMapper.updateById(sgGarageInfo);
            logger.info("=============approveGarageOut(): 出库车库车位数更新成功===========");

            sgGarageDetail.setRemark(sgGarageOrderDTO.getRemark());
            sgGarageDetailMapper.updateById(sgGarageDetail);
            logger.info("==========approveGarageOut(): garageDetail更新完成===========");

            //更新order
            sgGarageOrder.setSgGarageInfoToId(sgVehicleInfo.getSgGarageInfoId());
            sgGarageOrderMapper.updateById(sgGarageOrder);
            logger.info("===============approveGarageOut(): order更新完成============");

            if(sgGarageDetail.getPushSource() == PushSourceEnum.TEMP_GARAGE.getValue()){
                //创建临时出入入库单
                SgGarageDetail sgGarageRKDetail = new SgGarageDetail();
                BaseAssembler.mapObjWithoutBaseColumn(sgGarageDetail,sgGarageRKDetail);
                sgGarageRKDetail.setGarageSign(RCAllocateGarageSignEnum.GARAGEIN_SIGN.getValue());
                sgGarageRKDetail.setBillStatus(SgAllocateTaskStatusEnum.IN_GARAGING.getValue());
                String maxGarageOrderNum = sgGarageDetailMapper.createTaskNum(formatter.format(new Date()), "1");
                sgGarageRKDetail.setTaskNum(TaskNumUtil.createTaskNum("RK",maxGarageOrderNum));
                sgGarageRKDetail.setPushSource(PushSourceEnum.TEMP_GARAGE.getValue());
                sgGarageRKDetail.setTempPredictTime(new Date());
                sgGarageRKDetail.setSgGaragOrderId(sgGarageOrder.getId());
                sgGarageRKDetail.setId(null);
                sgGarageRKDetail.setCreateTime(new Date());// 操作时间
                sgGarageRKDetail.setCreatorName(CurrentUser.getCnName());// 操作人
                sgGarageRKDetail.setCreatorId(CurrentUser.getUserId());
                if (CurrentUser.getDepartmentId() != null) {
                    sgGarageRKDetail.setCreatorDepartmentId(Long.parseLong(CurrentUser.getDepartmentId()));
                }
                sgGarageRKDetail.setCreatorDepartmentName(CurrentUser.getDepartmentName());
                sgGarageDetailMapper.insert(sgGarageRKDetail);
                logger.info("===========approveGarageOut(): 创建临时出入入库单成功===========");
            }
            //添加车辆日志
            SgVehicleLog sgVehicleLog = this.tempVehLog(sgGarageOrder.getSgVehicleId(), APPROVE_GARAGEOUT);
            sgVehicleLog.setRemark("车辆状态从[" + SgVehicleStatusEnum.getDisplayNameByIndex(oldStat) + "]更新为[" + SgVehicleStatusEnum.getDisplayNameByIndex(sgVehicleInfo.getStat()) + "]");
            sgVehicleLogMapper.insert(sgVehicleLog);
            logger.info("===========approveGarageOut(): 车辆日志添加完成===========");

            //只有融后出库的车辆发送MQ消息
            //只有融后出库的车辆同步进销存数据
            if(sgGarageDetail.getPushSource() == PushSourceEnum.LOAN_PUSH.getValue()){
                //发送mq消息
                SgQueryVehicleIfoStatDTO statDTO = new SgQueryVehicleIfoStatDTO();
                statDTO.setApplyNo(sgVehicleInfo.getAlixNum());
                statDTO.setVin(sgVehicleInfo.getVin());
                statDTO.setState(2);
                logger.info("融后发送mq消息DTO=[{}]", JSONObject.toJSONString(statDTO));
                rabbitTemplate.convertAndSend("sg.loan.garage", "loanGetVehicleStatRoutingKey",JSON.toJSONString(statDTO));
                logger.info("==========approveGarageOut(): 融后发送mq消息成功=========");

                try{
                    VehicleInoutRecordDTO msgdto = new VehicleInoutRecordDTO();
                    msgdto.setApplyNum(sgVehicleInfo.getAlixNum());
                    msgdto.setBusinessTypeName(BusinessTypeEnum.getDisplayNameByIndex(sgVehicleInfo.getBusinessType()));
                    msgdto.setDataCreateDate(sgGarageDetail.getCreateTime());
                    msgdto.setGarageId(sgVehicleInfo.getSgGarageInfoId());
                    msgdto.setVin(sgVehicleInfo.getVin());
                    msgdto.setGarageName(sgGarageInfoMapper.selectById(sgVehicleInfo.getSgGarageInfoId()).getGarageName());
                    msgdto.setLicNum(sgVehicleInfo.getLicNum());
                    msgdto.setType(2);
                    msgdto.setInoutDate(sgGarageDetail.getCreateTime());
                    msgdto.setRemark(APPROVE_GARAGEOUT);
                    if(sgVehicleInfo.getLeaseProperty().equals(LeasePropertyEnum.LEASEBACK_CAR.getValue())){
                        msgdto.setRentType(2);
                    }
                    if (sgVehicleInfo.getLeaseProperty().equals(LeasePropertyEnum.RESOURCE_CAR.getValue())) {
                        msgdto.setRentType(1);
                    }
                    logger.info("出库订单同步进销存,入参:{}", JSONObject.toJSON(msgdto));
                    String c = RestUtil.sendRequest(rentCarUrl + "/api/rcSGService/saveInoutRecord", msgdto);
                    logger.info("接口返回数据为:{}", JSONObject.toJSON(c));
                    InvokeResult<String> resultMsg = JSON.parseObject(c, new TypeReference<InvokeResult<String>>(){});
                    if (!resultMsg.isSuccess()) {
                        logger.error("出库订单同步进销存失败，" + resultMsg.getData());
                    }
                }catch (Exception e){
                    logger.info("调用进销存接口入库失败！！！", e);
                }
            }

            //创建日志
            SgGarageOrderLog sgGarageOrderLog = this.createOrderLog(sgGarageOrder, APPROVE_GARAGEOUT);
            sgGarageOrderLogMapper.insert(sgGarageOrderLog);
            logger.info("===========approveGarageOut(): 审批日志添加成功===========");
        } catch (Exception e) {
            logger.error("approveGarageOut() failed：", e.getMessage());
            throw new BzException(e.getMessage());
        }
        return ResultUtil.success("审批通过");
    }

    /**
     * @Title: reject
     * @Description: 拒绝
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/20 14:07
     */
    @Override
    public InvokeResult<String> reject(SgGarageOrderDTO sgGarageOrderDTO) throws BzException {
        try {
            logger.info("拒绝 reject() sgGarageOrderDTO=[{}]", JSONObject.toJSONString(sgGarageOrderDTO));

            SgGarageOrder sgGarageOrder = sgGarageOrderMapper.selectById(sgGarageOrderDTO.getId());
            if(sgGarageOrder == null){
                logger.error("根据id["+sgGarageOrderDTO.getId()+"]未查到对应的数据，请刷新页面重新操作！");
                throw new BzException("根据id["+sgGarageOrderDTO.getId()+"]未查到对应的数据，请刷新页面重新操作！");
            }
            Integer orderStatus = sgGarageOrder.getOrderStatus();
            if(orderStatus!= SgAllocateTaskStatusEnum.OUT_GARAGING.getValue()){
                logger.error("非出库中或入库中的车不允许进行审批操作");
                throw new BzException("非出库中或入库中的车不允许进行审批操作");
            }

            //查询出库单
            SgGarageDetail sgGarageDetail = sgGarageDetailMapper.queryOutDeatil(sgGarageOrderDTO);
            sgGarageDetail.setBillStatus(SgAllocateTaskStatusEnum.OUT_REJECT.getValue());
            sgGarageDetail.setRemark(sgGarageOrderDTO.getRemark());
            sgGarageDetailMapper.updateById(sgGarageDetail);
            logger.info("===========出库审批驳回完成============");
            sgGarageOrder.setOrderStatus(SgAllocateTaskStatusEnum.OUT_REJECT.getValue());

            sgGarageOrder.setRemark(sgGarageOrderDTO.getRemark());
            sgGarageOrderMapper.updateById(sgGarageOrder);
            //创建日志
            SgGarageOrderLog sgGarageOrderLog = this.createOrderLog(sgGarageOrder, REJECT);
            sgGarageOrderLogMapper.insert(sgGarageOrderLog);
            logger.info("===========审批日志添加成功===========");
        } catch (Exception e) {
            logger.error("reject() failed：", e.getMessage());
            throw new BzException(e.getMessage());
        }
        return ResultUtil.success("审批通过");
    }

    /**
     * @Title: pageQuery
     * @Description: 出入库列表分页查询
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/20 17:46
     */
    @Override
    public IPage<SgGarageDetailDTO> pageQuery(SgGarageDetailDTO sgGarageDetailDTO) throws BzException {
        logger.info("分页查询入参：{}", JSONObject.toJSON(sgGarageDetailDTO));
        Page<SgGarageDetailDTO> dtoPage = null;
        try{
            Page<SgGarageDetail> page = new PageFactory<SgGarageDetail>().defaultPage(sgGarageDetailDTO);
            //权限控制
            //获取当前登录人的角色集合，判断是否拥有库管权限
            List<String> garageAdmins = new ArrayList<>();
            Boolean hasRoles = CurrentUser.hasRole(RoleEnum.GARAGE_MANAGE_ROLE);
            //如果当前登录人角色为 车库管理员，则只能看到对应车库的车
            if(hasRoles){
                logger.info("当前有无权限：{}", JSONObject.toJSON(hasRoles));
                Map<String,Object> map = new HashMap<>();
                map.put("contact", CurrentUser.getCnName());
                map.put("IS_DELETED", 0);
                List<SgContactToGarageTemp> contactList = SgContactToGarageTempMapper.selectByMap(map);
                logger.info("当前联系人的信息为：{}", JSONObject.toJSON(contactList));
                if(CollectionUtils.isNotEmpty(contactList)){
                    for (SgContactToGarageTemp contact : contactList){
                        garageAdmins.add(contact.getGarageInfoId());
                    }
                }
            }
            logger.info("当前联系人有权限的车库id为：{}", JSONObject.toJSON(garageAdmins));

            IPage<SgGarageDetailDTO> pageList = sgGarageDetailMapper.pageQuery(page, sgGarageDetailDTO, RCAllocateGarageSignEnum.GARAGEOUT_SIGN.getValue(), garageAdmins);
            logger.info("=====共查询[" + pageList.getTotal() + "]条数据");
            dtoPage = new Page<SgGarageDetailDTO>(pageList.getCurrent(), pageList.getSize(), pageList.getTotal()).setRecords(pageList.getRecords());
        }catch (Exception e){
            logger.error("pageQuery() failed：", e.getMessage());
            throw new BzException(e.getMessage());
        }
        return dtoPage;
    }

    /**
     * @Title: getBill
     * @Description: 查看详情
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/20 18:18
     */
    @Override
    public SgGarageOrderDTO getBill(String sgGarageDetailId) throws BzException {
        logger.info("查看详情 getBill() sgGarageDetailId=[{}]", JSONObject.toJSONString(sgGarageDetailId));
        SgGarageOrderDTO sgGarageOrderDTO = new SgGarageOrderDTO();
        try {
            SgGarageDetail sgGarageDetail = sgGarageDetailMapper.selectById(sgGarageDetailId);
            SgGarageOrder sgGarageOrder = sgGarageOrderMapper.selectById(sgGarageDetail.getSgGaragOrderId());
            if(sgGarageOrder == null){
                logger.error("根据id["+sgGarageDetail.getId()+"]未查到对应的数据，请刷新页面重新操作！");
                throw new BzException("根据id["+sgGarageDetail.getId()+"]未查到对应的数据，请刷新页面重新操作！");
            }
            //获取Order
            BaseAssembler.mapObjWithoutBaseColumn(sgGarageOrder,sgGarageOrderDTO);
            sgGarageOrderDTO.setId(sgGarageDetail.getSgGaragOrderId());
            //开始组装车辆信息
            SgVehicleInfo sgVehicleInfo = sgVehicleInfoMapper.selectById(sgGarageOrder.getSgVehicleId());
            if(sgVehicleInfo == null){
                logger.error("根据id["+sgGarageDetail.getId()+"]未查到对应的车辆信息，请查证！");
                throw new BzException("根据id["+sgGarageDetail.getId()+"]未查到对应的车辆信息，请查证！");
            }

            sgGarageOrderDTO.setPushSource(sgGarageDetail.getPushSource()); //推送来源
            sgGarageOrderDTO.setDetailTaskNum(sgGarageDetail.getTaskNum()); //出库库单号
            //临时出库车辆，出库车库、入库车库都为车辆目前所在库、出入库时间为临时出入库时间
            String garageName = "";
            if(sgGarageDetail.getPushSource() == PushSourceEnum.TEMP_GARAGE.getValue()){
                SgGarageInfo garageInfoFrom = sgGarageInfoMapper.selectById(sgVehicleInfo.getSgGarageInfoId()); //出库车库
                sgGarageOrderDTO.setGarageOutName(garageInfoFrom.getGarageName());
                sgGarageOrderDTO.setGarageInName(garageInfoFrom.getGarageName());
                garageName = garageInfoFrom.getGarageName();
                sgGarageOrderDTO.setActualStartTime(sgGarageDetail.getTempActualTime());
                sgGarageOrderDTO.setPredictEndTime(sgGarageDetail.getTempPredictTime());

                //融后推送的单子，按照推送车库判断
            }else if(sgGarageDetail.getPushSource() == PushSourceEnum.LOAN_PUSH.getValue()){
                SgGarageInfo sgGarageInfoFrom = sgGarageInfoMapper.selectById(sgGarageOrder.getSgGarageInfoFromId());
                if(sgGarageInfoFrom != null){
                    sgGarageOrderDTO.setGarageOutName(sgGarageInfoFrom.getGarageName()); //出库车库
                }

                SgGarageInfo sgGarageInfoTo = sgGarageInfoMapper.selectById(sgGarageOrder.getSgGarageInfoToId());
                if(sgGarageInfoTo != null){
                    sgGarageOrderDTO.setGarageOutName(sgGarageInfoTo.getGarageName()); //入库库车库
                }
            }else {
                logger.error("推送来源为空，请查证");
                throw new BzException("推送来源为空，请查证");
            }
            logger.info("==============出库详情getBill(): order组装完成============");

            SgVehicleInfoDTO sgVehicleInfoDTO = new SgVehicleInfoDTO();
            BaseAssembler.mapObjWithoutBaseColumn(sgVehicleInfo,sgVehicleInfoDTO);
            sgVehicleInfoDTO.setId(sgVehicleInfo.getId());
            sgVehicleInfoDTO.setSgGarageName(garageName); //在库车库名称(即目前入库车库)
            List<SgVehicleInfoDTO> vehList = new ArrayList<>();
            vehList.add(sgVehicleInfoDTO);
            sgGarageOrderDTO.setVehicleInfoDTOList(vehList);
            logger.info("==============出库详情getBill(): 车辆信息组装完成============");

            //开始查询附件信息
            List<AttachSourceDTO> attachSourceDTOS = attachSourceMapper.queryAtts(sgGarageDetailId,RcAttachmentTypeEnum.SG_OUT_IN_GARAGE.getValue());
            sgGarageOrderDTO.setAttachSourceDTOList(attachSourceDTOS);
            logger.info("==============出库详情getBill(): 出库单附件组装完成============");
            List<AttachSourceDTO> otherAttDTOLists = attachSourceMapper.queryAtts(sgGarageDetailId,RcAttachmentTypeEnum.SG_OTHERS_OF_GARAGE.getValue());
            sgGarageOrderDTO.setOtherAttDTOList(otherAttDTOLists);
            logger.info("==============出库详情getBill(): 其他附件组装完成============");
        } catch (Exception e) {
            logger.error("getBill() failed：", e.getMessage());
            throw new BzException(e.getMessage());
        }
        return sgGarageOrderDTO;
    }

    /**
     * @Title: createTempOut
     * @Description: 新增临时出库
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/21 10:47
     */
    @Override
    public InvokeResult<String> createTempOut(SgGarageDetailDTO sgGarageDetailDTO) throws BzException {
        try {
            logger.info("新增临时出库 createTempOut() sgGarageOrderDTO=[{}]", JSONObject.toJSONString(sgGarageDetailDTO));
            SgVehicleInfo sgVehicleInfo = sgVehicleInfoMapper.selectById(sgGarageDetailDTO.getSgVehicleId());
            if(sgVehicleInfo.getStat()!= SgVehicleStatusEnum.FREE.getValue()){
                logger.error("非在库车辆不允许做临时出库");
                throw new BzException("非在库车辆不允许做临时出库");
            }
            if(sgGarageDetailDTO.getOutReason() != OutReasonEnum.CLEAR.getValue() && sgGarageDetailDTO.getOutReason() != OutReasonEnum.MAINTAIN.getValue()
                    && sgGarageDetailDTO.getOutReason() != OutReasonEnum.REPAIR.getValue()){
                logger.error("临时出库原因只能为[清洗、保养、维修]");
                throw new BzException("临时出库原因只能为[清洗、保养、维修]");
            }
            //更改车辆状态
            Integer oldStat = sgVehicleInfo.getStat(); //更改之前的车辆状态
            sgVehicleInfo.setStat(SgVehicleStatusEnum.INTRANSIT.getValue());
            sgVehicleInfoMapper.updateById(sgVehicleInfo);
            logger.info("=============createTempOut(): 车辆状态更新完成=============");

            //融后推送-- 更改order，新增出库单  手动创建 -- 新增order、出库
            SgGarageDetail sgGarageDetail = sgGarageDetailMapper.getGarageInExis(sgGarageDetailDTO);
            if(sgGarageDetail == null){
                logger.error("入库单查询失败，vin[" + sgGarageDetailDTO.getVin() + "],alixNum[" + sgGarageDetailDTO.getAlixNum() + "]");
                throw new BzException("入库单查询失败，vin[" + sgGarageDetailDTO.getVin() + "],alixNum[" + sgGarageDetailDTO.getAlixNum() + "]");
            }

            //融后推送
            SgGarageOrder sgGarageOrder = sgGarageOrderMapper.selectById(sgGarageDetail.getSgGaragOrderId());
            sgGarageOrder.setOrderStatus(SgAllocateTaskStatusEnum.TEMP_READY.getValue()); //预临时出库
            sgGarageOrder.setSgGarageInfoToId(sgVehicleInfo.getSgGarageInfoId());
            sgGarageOrderMapper.updateById(sgGarageOrder);
            logger.info("==========createTempOut(): order更新完成===========");

//            SgGarageOrder sgGarageOrder = null;
//            //融后推送
//            if(sgGarageDetail != null){
//                //更新出入库时间
//                sgGarageOrder = sgGarageOrderMapper.selectById(sgGarageDetail.getSgGaragOrderId());
//                sgGarageOrder.setOrderStatus(SgAllocateTaskStatusEnum.TEMP_READY.getValue()); //预临时出库
//                sgGarageOrder.setSgGarageInfoToId(sgVehicleInfo.getSgGarageInfoId());
//                sgGarageOrderMapper.updateById(sgGarageOrder);
//                logger.info("==========createTempOut(): order更新完成===========");
//            }else{
//                //手动新增
//                //1、新增order
//                sgGarageOrder = new SgGarageOrder();
//                sgGarageOrder.setOrderStatus(SgAllocateTaskStatusEnum.TEMP_READY.getValue()); //预临时出库
//                sgGarageOrder.setSgVehicleId(sgVehicleInfo.getId());
//                String maxGarageOrderNum = sgGarageOrderMapper.createGarageOrderTaskNum(formatter.format(new Date()));
//                sgGarageOrder.setTaskNum(TaskNumUtil.createGarageOrderTaskNum(maxGarageOrderNum));
//                sgGarageOrder.setCreateTime(new Date());// 操作时间
//                sgGarageOrder.setCreatorName(CurrentUser.getCnName());// 操作人
//                sgGarageOrder.setCreatorId(CurrentUser.getUserId());
//                if (CurrentUser.getDepartmentId() != null) {
//                    sgGarageOrder.setCreatorDepartmentId(Long.parseLong(CurrentUser.getDepartmentId()));
//                }
//                sgGarageOrder.setCreatorDepartmentName(CurrentUser.getDepartmentName());
//                sgGarageOrderMapper.insert(sgGarageOrder);
//                logger.info("==========createTempOut(): order新增完成===========");
//            }

            //2、生成出库单
            SgGarageDetail CKGarageDetail = new SgGarageDetail();
            BaseAssembler.mapObjWithoutBaseColumn(sgGarageDetailDTO,CKGarageDetail);
            CKGarageDetail.setSgGaragOrderId(sgGarageOrder.getId());
            CKGarageDetail.setGarageSign(RCAllocateGarageSignEnum.GARAGEOUT_SIGN.getValue());
            CKGarageDetail.setBillStatus(SgAllocateTaskStatusEnum.PENDING.getValue());
            CKGarageDetail.setPushSource(PushSourceEnum.TEMP_GARAGE.getValue());
            CKGarageDetail.setOutReason(sgGarageDetailDTO.getOutReason());
            //防止前端点击重置按钮
            CKGarageDetail.setVin(sgVehicleInfo.getVin());
            CKGarageDetail.setAlixNum(sgVehicleInfo.getAlixNum());
            String dateString = formatter.format(new Date());
            String maxTaskNum = sgGarageDetailMapper.createTaskNum(dateString, "0");
            CKGarageDetail.setTaskNum(TaskNumUtil.createTaskNum("CK",maxTaskNum));
            CKGarageDetail.setId(null);
            CKGarageDetail.setCreateTime(new Date());// 操作时间
            CKGarageDetail.setCreatorName(CurrentUser.getCnName());// 操作人
            CKGarageDetail.setCreatorId(CurrentUser.getUserId());
            if (CurrentUser.getDepartmentId() != null) {
                CKGarageDetail.setCreatorDepartmentId(Long.parseLong(CurrentUser.getDepartmentId()));
            }
            CKGarageDetail.setCreatorDepartmentName(CurrentUser.getDepartmentName());
            sgGarageDetailMapper.insert(CKGarageDetail);
            logger.info("==========createTempOut(): 出库单创建成功===========");



            //车辆创建  增加日志
            SgVehicleLog sgVehicleLog = this.tempVehLog(sgGarageOrder.getSgVehicleId(), CREATE_TEMP);
            sgVehicleLog.setRemark("车辆状态从[" + SgVehicleStatusEnum.getDisplayNameByIndex(oldStat) + "]更新为[" + SgVehicleStatusEnum.getDisplayNameByIndex(sgVehicleInfo.getStat()) + "]");
            sgVehicleLogMapper.insert(sgVehicleLog);
            logger.info("=============createTempOut(): 车辆日志添加成功=============");

            //添加order日志
            SgGarageOrderLog sgGarageOrderLog = this.createOrderLog(sgGarageOrder, CREATE_TEMP);
            sgGarageOrderLogMapper.insert(sgGarageOrderLog);
            logger.info("=============createTempOut(): 订单日志添加成功=============");

            logger.info("新增临时出库成功 createTempOut() 返回值=[{}]", JSONObject.toJSONString(sgGarageDetailDTO));
        } catch (Exception e) {
            logger.error("createTempOut() failed：", e.getMessage());
            throw new BzException(e.getMessage());
        }
        return ResultUtil.success("新增临时出库成功");
    }




    /**
     * @Title: delAtt
     * @Description: 删除附件内部方法
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/20 10:31
     */
    private void delAtt(SgGarageOrderDTO sgGarageOrderDTO){
        List<String> delAttIds = sgGarageOrderDTO.getDelAttIds();
        if(CollectionUtils.isNotEmpty(delAttIds)){
            for(String attId : delAttIds){
                AttachSource att = attachSourceMapper.selectById(attId);
                if(att != null){
                    att.deleteById();
                }
            }

        }
    }

    /**
     * @Title: createVehicleLog
     * @Description: 添加日志
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/19 23:34
     */
    public SgGarageOrderLog createOrderLog(SgGarageOrder sgGarageOrder, String event) throws BzException {
        SgGarageOrderLog sgGarageOrderLog = new SgGarageOrderLog();
        try {
            sgGarageOrderLog.setEvent(event);
            sgGarageOrderLog.setRemark(sgGarageOrder.getRemark());
            sgGarageOrderLog.setCreateTime(new Date());// 操作时间
            sgGarageOrderLog.setCreatorName(CurrentUser.getCnName());// 操作人
            sgGarageOrderLog.setCreatorId(CurrentUser.getUserId());
            if (CurrentUser.getDepartmentId() != null) {
                sgGarageOrderLog.setCreatorDepartmentId(Long.parseLong(CurrentUser.getDepartmentId()));
            }
            sgGarageOrderLog.setCreatorDepartmentName(CurrentUser.getDepartmentName());
            sgGarageOrderLog.setSgGarageOrderId(sgGarageOrder.getId());
            //sgGarageOrderLogMapper.insert(sgGarageOrderLog);
        }catch (Exception e) {
            logger.error("createOrderLog() failed：", e.getMessage());
            throw new BzException(e.getMessage());
        }
        return sgGarageOrderLog;
    }

    /**
     * @Title: creatTempVehLog
     * @Description: 新增车辆临时出库
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/21 11:59
     */
    private SgVehicleLog tempVehLog(String sgVehicleId,String event) throws BzException{
        SgVehicleLog sgVehicleLog = new SgVehicleLog();
        try {
            sgVehicleLog.setEvent(event);
            sgVehicleLog.setCreateTime(new Date());// 操作时间
            sgVehicleLog.setCreatorName(CurrentUser.getCnName());// 操作人
            sgVehicleLog.setCreatorId(CurrentUser.getUserId());
            if (CurrentUser.getDepartmentId() != null) {
                sgVehicleLog.setCreatorDepartmentId(Long.parseLong(CurrentUser.getDepartmentId()));
            }
            sgVehicleLog.setCreatorDepartmentName(CurrentUser.getDepartmentName());
            sgVehicleLog.setVehicleId(sgVehicleId);
//        sgVehicleLogMapper.insert(sgVehicleLog);
        }catch (Exception e) {
            logger.error("tempVehLog() failed：", e.getMessage());
            throw new BzException(e.getMessage());
        }
        return sgVehicleLog;
    }


}
