package com.yixin.garage.service.order.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.common.exception.BzException;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.auth.CurrentUser;
import com.yixin.garage.core.base.BaseAssembler;
import com.yixin.garage.core.common.factory.PageFactory;
import com.yixin.garage.dao.AttachSourceMapper;
import com.yixin.garage.dao.garage.SgContactToGarageTempMapper;
import com.yixin.garage.dao.garage.SgGarageInfoMapper;
import com.yixin.garage.dao.order.SgGarageOrderLogMapper;
import com.yixin.garage.dao.order.SgGarageOrderMapper;
import com.yixin.garage.dao.vehicle.SgVehicleInfoMapper;
import com.yixin.garage.dto.api.forLoan.SgQueryVehicleIfoStatDTO;
import com.yixin.garage.dto.order.SgGarageDetailDTO;
import com.yixin.garage.dto.order.SgGarageOrderDTO;
import com.yixin.garage.dto.order.SgGarageOrderLogDTO;
import com.yixin.garage.dto.sys.AttachSourceDTO;
import com.yixin.garage.dto.sys.CatTypeDTO;
import com.yixin.garage.dto.sys.VehicleInoutRecordDTO;
import com.yixin.garage.dto.vehicle.SgVehicleInfoDTO;
import com.yixin.garage.entity.garage.SgContactToGarageTemp;
import com.yixin.garage.entity.garage.SgGarageInfo;
import com.yixin.garage.entity.order.SgGarageDetail;
import com.yixin.garage.dao.order.SgGarageDetailMapper;
import com.yixin.garage.entity.order.SgGarageOrder;
import com.yixin.garage.entity.order.SgGarageOrderLog;
import com.yixin.garage.entity.vehicle.SgVehicleInfo;
import com.yixin.garage.entity.vehicle.SgVehicleLog;
import com.yixin.garage.entity.zhongtai.SgSendInfoToZhongtai;
import com.yixin.garage.enums.AttachTypeEnum;
import com.yixin.garage.enums.RcAttachmentTypeEnum;
import com.yixin.garage.enums.RoleEnum;
import com.yixin.garage.enums.garage.*;
import com.yixin.garage.enums.garage.zhongtai.BillStatusEnum;
import com.yixin.garage.enums.garage.zhongtai.SendTypeEnum;
import com.yixin.garage.service.order.ISgGarageDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yixin.garage.service.order.ISgGarageOrderService;
import com.yixin.garage.util.RestUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liyaqing
 * @since 2019-08-19
 */
@Service
public class SgGarageDetailServiceImpl extends ServiceImpl<SgGarageDetailMapper, SgGarageDetail> implements ISgGarageDetailService {

    private final static Logger logger = LoggerFactory.getLogger(SgGarageDetailServiceImpl.class);

    @Autowired
    SgGarageDetailMapper sgGarageDetailMapper;

    @Autowired
    SgVehicleInfoMapper sgVehicleInfoMapper;

    @Autowired
    SgGarageInfoMapper sgGarageInfoMapper;

    @Autowired
    SgGarageOrderMapper sgGarageOrderMapper;

    @Autowired
    SgGarageOrderLogMapper sgGarageOrderLogMapper;

    @Autowired
    AttachSourceMapper attachSourceMapper;

    @Autowired
    ISgGarageOrderService iSgGarageOrderService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private SgContactToGarageTempMapper SgContactToGarageTempMapper;

    @Value("${rentCarUrl}")
    private String rentCarUrl;


    @Override
    public Page<SgGarageDetailDTO> pageQueryRKOrder(SgGarageDetailDTO dto) {
        logger.info("分页查询入参：{}", JSONObject.toJSON(dto));
        Page<SgGarageDetailDTO> dtoPage = null;
        try{
            Page<SgGarageDetail> page = new PageFactory<SgGarageDetail>().defaultPage(dto);

            //权限控制
            //获取当前登录人的角色集合，判断是否拥有库管权限
            List<String> garageAdmins = new ArrayList<>();
            Boolean hasRoles = CurrentUser.hasRole(RoleEnum.GARAGE_MANAGE_ROLE);
            //如果当前登录人角色为 车库管理员，则只能看到对应车库的车
            if(hasRoles){
                logger.info("当前有无权限：{}", JSONObject.toJSON(hasRoles));
                dto.setCreatorName(CurrentUser.getCnName());
                Map<String,Object> map = new HashMap<>();
                map.put("contact", CurrentUser.getCnName());
                map.put("IS_DELETED", 0);
                List<SgContactToGarageTemp> contactList = SgContactToGarageTempMapper.selectByMap(map);
                logger.info("当前有权限的车库为：{}", JSONObject.toJSON(contactList));
                if(CollectionUtils.isNotEmpty(contactList)){
                    for (SgContactToGarageTemp contact : contactList){
                        garageAdmins.add(contact.getGarageInfoId());
                    }
                }
            }
            logger.info("当前联系人有权限的车库id为：{}", JSONObject.toJSON(garageAdmins));
            IPage<SgGarageDetailDTO> pageList = sgGarageDetailMapper.pageQuery(page, dto, RCAllocateGarageSignEnum.GARAGEIN_SIGN.getValue(),garageAdmins);
            logger.info("=====共查询[" + pageList.getSize() + "]条数据");
            dtoPage = new Page<SgGarageDetailDTO>(pageList.getCurrent(), pageList.getSize(), pageList.getTotal()).setRecords(pageList.getRecords());
        }catch (Exception e){
            logger.error("pageQuery() failed：", e);
            throw new BzException("pageQuery() failed:",e);
        }
        return dtoPage;
    }


    @Transactional
    @Override
    public InvokeResult<String> approveIn(SgGarageOrderDTO dto) {
        logger.info("入库审批-sgGarageOrderDTO：{}", JSONObject.toJSONString(dto));

        InvokeResult<String> result = new InvokeResult<String>();
        try{

            //入库审批第一步，状态
            SgGarageDetail sgGarageDetail = sgGarageDetailMapper.selectById(dto.getId());

            SgGarageOrder sgGarageOrder = sgGarageOrderMapper.selectById(sgGarageDetail.getSgGaragOrderId());


            SgVehicleInfo sgVehicleInfo = sgVehicleInfoMapper.selectById(sgGarageOrder.getSgVehicleId());
            int oldStat = sgVehicleInfo.getStat();

            sgVehicleInfo.setStat(SgVehicleStatusEnum.FREE.getValue());
            SgGarageOrderLog log = new SgGarageOrderLog();//记录审批日志
            String event = "";
            Integer billStatus = sgGarageDetail.getBillStatus();

            //第一步审批判断逻辑
            if(billStatus == SgAllocateTaskStatusEnum.PENDING.getValue() || billStatus == SgAllocateTaskStatusEnum.IN_REJECT.getValue()){
                sgGarageOrder.setOrderStatus(SgAllocateTaskStatusEnum.IN_GARAGING.getValue()); //更新单子状态
                sgGarageDetail.setBillStatus(SgAllocateTaskStatusEnum.IN_GARAGING.getValue());
                event ="入库第一步审批";

                //更新车辆里程
//                sgVehicleInfo.setMileage(dto.getMileage());
            }
            if(billStatus == SgAllocateTaskStatusEnum.IN_GARAGING.getValue()){
                sgGarageOrder.setOrderStatus(SgAllocateTaskStatusEnum.INOFSTORE.getValue());
                sgGarageDetail.setBillStatus(SgAllocateTaskStatusEnum.INOFSTORE.getValue());
                event ="入库确认审批";
            }

            if(sgGarageDetail.getPushSource().equals(PushSourceEnum.LOAN_PUSH.getValue())){
                try{
                    VehicleInoutRecordDTO msgdto = new VehicleInoutRecordDTO();
                    msgdto.setApplyNum(sgVehicleInfo.getAlixNum());
                    msgdto.setBusinessTypeName(BusinessTypeEnum.getDisplayNameByIndex(sgVehicleInfo.getBusinessType()));
                    msgdto.setDataCreateDate(sgGarageDetail.getCreateTime());
                    msgdto.setGarageId(sgVehicleInfo.getSgGarageInfoId());
                    msgdto.setVin(sgVehicleInfo.getVin());
                    msgdto.setGarageName(sgGarageInfoMapper.selectById(sgVehicleInfo.getSgGarageInfoId()).getGarageName());
                    msgdto.setLicNum(sgVehicleInfo.getLicNum());
                    msgdto.setType(1);
                    msgdto.setInoutDate(sgVehicleInfo.getActualStorageTime());
                    msgdto.setRemark(event);
                    if(sgVehicleInfo.getLeaseProperty().equals(LeasePropertyEnum.LEASEBACK_CAR.getValue())){
                        msgdto.setRentType(2);
                    }
                    if (sgVehicleInfo.getLeaseProperty().equals(LeasePropertyEnum.RESOURCE_CAR.getValue())) {
                        msgdto.setRentType(1);
                    }
                    logger.info("入库订单同步进销存,入参:{}", JSONObject.toJSON(msgdto));
                    String c = RestUtil.sendRequest(rentCarUrl + "/api/rcSGService/saveInoutRecord", msgdto);
                    logger.info("接口返回数据为:{}", JSONObject.toJSON(c));
                    InvokeResult<String> resultMsg = JSON.parseObject(c, new TypeReference<InvokeResult<String>>(){});
                    if (!resultMsg.isSuccess()) {
                        logger.error("入库订单同步进销存失败，" + resultMsg.getData());
                    }
                }catch (Exception e){
                    logger.info("调用进销存接口入库失败！！！", e);
                }
            }

            log.setEvent(event);
            log.setSgGarageOrderId(sgGarageOrder.getId());
            log.setCreatorName(CurrentUser.getCnName());
            log.setCreateTime(new Date()); //操作时间
            if (null != dto.getRemark() && !dto.getRemark().equals("")) {
                log.setRemark(dto.getRemark());
            }
            log.insert();

            SgVehicleLog vehicleLog = new SgVehicleLog();
            vehicleLog.setVehicleId(sgVehicleInfo.getId());
            vehicleLog.setEvent("入库审批");
            vehicleLog.setRemark("车辆状态从[" + SgVehicleStatusEnum.getDisplayNameByIndex(oldStat) + "]更新为[" + SgVehicleStatusEnum.getDisplayNameByIndex(sgVehicleInfo.getStat()) + "]");
            vehicleLog.insert();


            //修改车位数
            SgGarageInfo sgGarageInfo = sgGarageInfoMapper.selectById(sgGarageOrder.getSgGarageInfoToId());
            sgGarageInfo.setParkedNum(sgGarageInfo.getParkedNum() + 1);
            sgGarageInfoMapper.updateById(sgGarageInfo);


            sgVehicleInfo.insertOrUpdate();
            sgGarageOrder.insertOrUpdate();
            sgGarageDetailMapper.updateById(sgGarageDetail);
            sgGarageDetail.insertOrUpdate();
            result.setData("入库成功！");


            if (sgGarageDetail.getPushSource() != null && sgGarageDetail.getPushSource() == PushSourceEnum.LOAN_PUSH.getValue()) {
                logger.info("===========融后发送mq消息开始===========");
                SgQueryVehicleIfoStatDTO mqDto = new SgQueryVehicleIfoStatDTO();
                mqDto.setApplyNo(sgVehicleInfo.getAlixNum());
                mqDto.setVin(sgVehicleInfo.getVin());
                mqDto.setState(1);//在库/1；出库/2；临时出库/0
                //推送车辆状态变更mq
                rabbitTemplate.convertAndSend("sg.loan.garage", "loanGetVehicleStatRoutingKey",JSON.toJSONString(mqDto));
                logger.info("===========融后发送mq消息结束,mqDto：{}===========" + JSONObject.toJSONString(mqDto));
            }


            //推送中台数据
            if (sgGarageDetail.getPushSource() == PushSourceEnum.LOAN_PUSH.getValue()) {
                //推送中台信息保存
                logger.info("SgGarageForLoanServiceImpl inGarage()融后推送入库:保存推送信息，供定时推送中台使用 =========");
                SgSendInfoToZhongtai rcSendInfoToZhongtai = new SgSendInfoToZhongtai();
                rcSendInfoToZhongtai.setBillNum(sgGarageDetail.getAlixNum());//单号
                rcSendInfoToZhongtai.setInterfaceName(SendTypeEnum.PSM_RESTOCK_INTERFACE.getValue());//接口名称(码值)
                rcSendInfoToZhongtai.setSendCount(0);//发送次数
                rcSendInfoToZhongtai.setType(BillStatusEnum.PSM_RESTOCK_INTERFACE.getValue());//单子类型
                rcSendInfoToZhongtai.setStat(0);//发送状态
                rcSendInfoToZhongtai.setCreatorName("融后推送入库");
                rcSendInfoToZhongtai.insert();
            }
        }catch (Exception e){
            logger.error("approveIn() failed：", e.getMessage());
            throw new BzException(e.getMessage());
        }
        return result;
    }


    @Override
    public InvokeResult<String> inReject(SgGarageOrderDTO dto) {
        logger.info("inReject() sgGarageOrderDTO : {}",JSONObject.toJSONString(dto));
        InvokeResult<String> result = new InvokeResult<String>();
        try {
            SgGarageOrder sgGarageOrder = sgGarageOrderMapper.selectById(dto.getId());
            //首先判断出库单是否审批完成
            SgGarageDetail ckDetail = sgGarageDetailMapper.selectById(sgGarageOrder.getSgGarageCKDatailId());
            if(ckDetail.getBillStatus()!=SgAllocateTaskStatusEnum.OUTOFSTORE.getValue()){
                result.failure("出库审批没有完成，请检查！");
                return result;
            }

            SgGarageDetail sgGarageDetail = sgGarageDetailMapper.selectById(dto.getSgGarageRKDatailId());
            SgGarageOrderLog sgGarageOrderLog = new SgGarageOrderLog();

            //判断审批是哪个节点驳回
            Integer billStatus = sgGarageDetail.getBillStatus();
            //第一步审批驳回
            if(billStatus != SgAllocateTaskStatusEnum.IN_GARAGING.getValue()){
                result.failure("审批状态不能驳回！");
                return result;
            }
            sgGarageOrderLog.setEvent("资产经理入库驳回");
            sgGarageDetail.setBillStatus(SgAllocateTaskStatusEnum.IN_REJECT.getValue());
            sgGarageOrder.setOrderStatus(SgAllocateTaskStatusEnum.IN_REJECT.getValue());



            //生成驳回日志
            sgGarageOrderLog.setSgGarageOrderId(sgGarageOrder.getId()); //调配ID
            sgGarageOrderLog.setCreateTime(new Date()); //操作时间
            sgGarageOrderLog.setRemark(dto.getRemark()); //备注
            sgGarageOrderLog.setCreatorName(CurrentUser.getCnName());
            sgGarageOrderLog.setCreatorId(CurrentUser.getUserId());
            sgGarageOrderLog.insert();

            sgGarageOrder.insertOrUpdate();
            sgGarageDetail.insertOrUpdate();
            result.setData("审批完成");
            return result;
        }catch (Exception e){
            logger.error("inReject() failed：{}",e.getMessage(), e);
            throw new BzException("inReject() failed", e);
        }
    }



    @Override
    public SgGarageOrderDTO getBill(String sgGarageOrderId) {
        logger.info("查看详情 getBill() sgGarageOrderDTO=[{}]", JSONObject.toJSONString(sgGarageOrderId));
        SgGarageOrderDTO sgGarageOrderDTO = new SgGarageOrderDTO();
        try {
            SgGarageOrder sgGarageOrder = sgGarageOrderMapper.selectById(sgGarageOrderId);
            //获取Order
            BaseAssembler.mapObjWithoutBaseColumn(sgGarageOrder,sgGarageOrderDTO);
            sgGarageOrderDTO.setId(sgGarageOrderId);
            //开始组装车辆信息
            SgVehicleInfo sgVehicleInfo = sgVehicleInfoMapper.selectById(sgGarageOrder.getSgVehicleId());
            if(sgVehicleInfo == null){
                logger.error("根据id["+sgGarageOrderId+"]未查到对应的车辆信息，请查证！");
                throw new BzException("根据id["+sgGarageOrderId+"]未查到对应的车辆信息，请查证！");
            }
            SgVehicleInfoDTO sgVehicleInfoDTO = new SgVehicleInfoDTO();
            BaseAssembler.mapObjWithoutBaseColumn(sgVehicleInfo,sgVehicleInfoDTO);
            sgVehicleInfoDTO.setId(sgVehicleInfo.getId());

            SgGarageInfo sgGarageInfo = sgGarageInfoMapper.selectById(sgGarageOrder.getSgGarageInfoToId()); //入库车库
            if(sgGarageInfo != null){
                sgVehicleInfoDTO.setSgGarageName(sgGarageInfo.getGarageName());
                sgGarageOrderDTO.setGarageInName(sgGarageInfo.getGarageName());
            }

            List<SgVehicleInfoDTO> vehList = new ArrayList<>();
            vehList.add(sgVehicleInfoDTO);
            sgGarageOrderDTO.setVehicleInfoDTOList(vehList);
            logger.info("==============车辆信息组装完成============");


            //开始查询附件信息
            List<AttachSourceDTO> attachSourceDTOS = attachSourceMapper.queryAtts(sgGarageOrder.getSgGarageCKDatailId(), RcAttachmentTypeEnum.SG_OUT_IN_GARAGE.getValue());
            sgGarageOrderDTO.setAttachSourceDTOList(attachSourceDTOS);
            logger.info("==============出库详情getBill(): 出库单附件组装完成============");
            List<AttachSourceDTO> otherAttDTOLists = attachSourceMapper.queryAtts(sgGarageOrder.getSgGarageCKDatailId(),RcAttachmentTypeEnum.SG_OTHERS_OF_GARAGE.getValue());
            sgGarageOrderDTO.setOtherAttDTOList(otherAttDTOLists);
            logger.info("==============出库详情getBill(): 其他附件组装完成============");


            QueryWrapper detailWrapper = new QueryWrapper();
            detailWrapper.eq("sgGaragOrder_id", sgGarageOrderId);
            detailWrapper.eq("IS_DELETED", 0);
            detailWrapper.eq("garage_sign", RCAllocateGarageSignEnum.GARAGEIN_SIGN.getValue());
            List<SgGarageDetail> sgGarageDetails = sgGarageDetailMapper.selectList(detailWrapper);
            SgGarageDetail sgGarageDetail = sgGarageDetails.get(0);
//            SgGarageDetail sgGarageDetail = sgGarageDetailMapper.selectById(sgGarageOrder.getSgGarageRKDatailId());
            sgGarageOrderDTO.setPushSource(sgGarageDetail.getPushSource()); //推送来源
            sgGarageOrderDTO.setDetailTaskNum(sgGarageDetail.getTaskNum()); //入库单号
            sgGarageOrderDTO.setPredictEndTime(sgGarageDetail.getTempPredictTime());//入库时间
            logger.info("==============order组装完成============");
        } catch (Exception e) {
            logger.error("getBill() failed：", e.getMessage());
            throw new BzException(e.getMessage());
        }
        return sgGarageOrderDTO;
    }

    @Override
    public Page<SgGarageOrderLogDTO> pageQueryLog(SgGarageOrderLogDTO dto) {
        logger.info("分页查询订单审批日志入参：{}", JSONObject.toJSON(dto));
        Page<SgGarageOrderLogDTO> dtoPage = null;
        try{
            Page<SgGarageOrderLog> page = new PageFactory<SgGarageOrderLog>().defaultPage(dto);
            IPage<SgGarageOrderLogDTO> pageList = sgGarageOrderLogMapper.pageQueryLog(page, dto);
            logger.info("=====共查询[" + pageList.getSize() + "]条数据");
            dtoPage = new Page<SgGarageOrderLogDTO>(pageList.getCurrent(), pageList.getSize(), pageList.getTotal()).setRecords(pageList.getRecords());
        }catch (Exception e){
            logger.error("pageQuery() failed：", e);
            throw new BzException("pageQuery() failed:",e);
        }
        return dtoPage;
    }


}
