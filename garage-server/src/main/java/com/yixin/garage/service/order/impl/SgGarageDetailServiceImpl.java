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
 * ???????????????
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
        logger.info("?????????????????????{}", JSONObject.toJSON(dto));
        Page<SgGarageDetailDTO> dtoPage = null;
        try{
            Page<SgGarageDetail> page = new PageFactory<SgGarageDetail>().defaultPage(dto);

            //????????????
            //?????????????????????????????????????????????????????????????????????
            List<String> garageAdmins = new ArrayList<>();
            Boolean hasRoles = CurrentUser.hasRole(RoleEnum.GARAGE_MANAGE_ROLE);
            //?????????????????????????????? ???????????????????????????????????????????????????
            if(hasRoles){
                logger.info("?????????????????????{}", JSONObject.toJSON(hasRoles));
                dto.setCreatorName(CurrentUser.getCnName());
                Map<String,Object> map = new HashMap<>();
                map.put("contact", CurrentUser.getCnName());
                map.put("IS_DELETED", 0);
                List<SgContactToGarageTemp> contactList = SgContactToGarageTempMapper.selectByMap(map);
                logger.info("??????????????????????????????{}", JSONObject.toJSON(contactList));
                if(CollectionUtils.isNotEmpty(contactList)){
                    for (SgContactToGarageTemp contact : contactList){
                        garageAdmins.add(contact.getGarageInfoId());
                    }
                }
            }
            logger.info("?????????????????????????????????id??????{}", JSONObject.toJSON(garageAdmins));
            IPage<SgGarageDetailDTO> pageList = sgGarageDetailMapper.pageQuery(page, dto, RCAllocateGarageSignEnum.GARAGEIN_SIGN.getValue(),garageAdmins);
            logger.info("=====?????????[" + pageList.getSize() + "]?????????");
            dtoPage = new Page<SgGarageDetailDTO>(pageList.getCurrent(), pageList.getSize(), pageList.getTotal()).setRecords(pageList.getRecords());
        }catch (Exception e){
            logger.error("pageQuery() failed???", e);
            throw new BzException("pageQuery() failed:",e);
        }
        return dtoPage;
    }


    @Transactional
    @Override
    public InvokeResult<String> approveIn(SgGarageOrderDTO dto) {
        logger.info("????????????-sgGarageOrderDTO???{}", JSONObject.toJSONString(dto));

        InvokeResult<String> result = new InvokeResult<String>();
        try{

            //??????????????????????????????
            SgGarageDetail sgGarageDetail = sgGarageDetailMapper.selectById(dto.getId());

            SgGarageOrder sgGarageOrder = sgGarageOrderMapper.selectById(sgGarageDetail.getSgGaragOrderId());


            SgVehicleInfo sgVehicleInfo = sgVehicleInfoMapper.selectById(sgGarageOrder.getSgVehicleId());
            int oldStat = sgVehicleInfo.getStat();

            sgVehicleInfo.setStat(SgVehicleStatusEnum.FREE.getValue());
            SgGarageOrderLog log = new SgGarageOrderLog();//??????????????????
            String event = "";
            Integer billStatus = sgGarageDetail.getBillStatus();

            //???????????????????????????
            if(billStatus == SgAllocateTaskStatusEnum.PENDING.getValue() || billStatus == SgAllocateTaskStatusEnum.IN_REJECT.getValue()){
                sgGarageOrder.setOrderStatus(SgAllocateTaskStatusEnum.IN_GARAGING.getValue()); //??????????????????
                sgGarageDetail.setBillStatus(SgAllocateTaskStatusEnum.IN_GARAGING.getValue());
                event ="?????????????????????";

                //??????????????????
//                sgVehicleInfo.setMileage(dto.getMileage());
            }
            if(billStatus == SgAllocateTaskStatusEnum.IN_GARAGING.getValue()){
                sgGarageOrder.setOrderStatus(SgAllocateTaskStatusEnum.INOFSTORE.getValue());
                sgGarageDetail.setBillStatus(SgAllocateTaskStatusEnum.INOFSTORE.getValue());
                event ="??????????????????";
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
                    logger.info("???????????????????????????,??????:{}", JSONObject.toJSON(msgdto));
                    String c = RestUtil.sendRequest(rentCarUrl + "/api/rcSGService/saveInoutRecord", msgdto);
                    logger.info("?????????????????????:{}", JSONObject.toJSON(c));
                    InvokeResult<String> resultMsg = JSON.parseObject(c, new TypeReference<InvokeResult<String>>(){});
                    if (!resultMsg.isSuccess()) {
                        logger.error("????????????????????????????????????" + resultMsg.getData());
                    }
                }catch (Exception e){
                    logger.info("??????????????????????????????????????????", e);
                }
            }

            log.setEvent(event);
            log.setSgGarageOrderId(sgGarageOrder.getId());
            log.setCreatorName(CurrentUser.getCnName());
            log.setCreateTime(new Date()); //????????????
            if (null != dto.getRemark() && !dto.getRemark().equals("")) {
                log.setRemark(dto.getRemark());
            }
            log.insert();

            SgVehicleLog vehicleLog = new SgVehicleLog();
            vehicleLog.setVehicleId(sgVehicleInfo.getId());
            vehicleLog.setEvent("????????????");
            vehicleLog.setRemark("???????????????[" + SgVehicleStatusEnum.getDisplayNameByIndex(oldStat) + "]?????????[" + SgVehicleStatusEnum.getDisplayNameByIndex(sgVehicleInfo.getStat()) + "]");
            vehicleLog.insert();


            //???????????????
            SgGarageInfo sgGarageInfo = sgGarageInfoMapper.selectById(sgGarageOrder.getSgGarageInfoToId());
            sgGarageInfo.setParkedNum(sgGarageInfo.getParkedNum() + 1);
            sgGarageInfoMapper.updateById(sgGarageInfo);


            sgVehicleInfo.insertOrUpdate();
            sgGarageOrder.insertOrUpdate();
            sgGarageDetailMapper.updateById(sgGarageDetail);
            sgGarageDetail.insertOrUpdate();
            result.setData("???????????????");


            if (sgGarageDetail.getPushSource() != null && sgGarageDetail.getPushSource() == PushSourceEnum.LOAN_PUSH.getValue()) {
                logger.info("===========????????????mq????????????===========");
                SgQueryVehicleIfoStatDTO mqDto = new SgQueryVehicleIfoStatDTO();
                mqDto.setApplyNo(sgVehicleInfo.getAlixNum());
                mqDto.setVin(sgVehicleInfo.getVin());
                mqDto.setState(1);//??????/1?????????/2???????????????/0
                //????????????????????????mq
                rabbitTemplate.convertAndSend("sg.loan.garage", "loanGetVehicleStatRoutingKey",JSON.toJSONString(mqDto));
                logger.info("===========????????????mq????????????,mqDto???{}===========" + JSONObject.toJSONString(mqDto));
            }


            //??????????????????
            if (sgGarageDetail.getPushSource() == PushSourceEnum.LOAN_PUSH.getValue()) {
                //????????????????????????
                logger.info("SgGarageForLoanServiceImpl inGarage()??????????????????:???????????????????????????????????????????????? =========");
                SgSendInfoToZhongtai rcSendInfoToZhongtai = new SgSendInfoToZhongtai();
                rcSendInfoToZhongtai.setBillNum(sgGarageDetail.getAlixNum());//??????
                rcSendInfoToZhongtai.setInterfaceName(SendTypeEnum.PSM_RESTOCK_INTERFACE.getValue());//????????????(??????)
                rcSendInfoToZhongtai.setSendCount(0);//????????????
                rcSendInfoToZhongtai.setType(BillStatusEnum.PSM_RESTOCK_INTERFACE.getValue());//????????????
                rcSendInfoToZhongtai.setStat(0);//????????????
                rcSendInfoToZhongtai.setCreatorName("??????????????????");
                rcSendInfoToZhongtai.insert();
            }
        }catch (Exception e){
            logger.error("approveIn() failed???", e.getMessage());
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
            //???????????????????????????????????????
            SgGarageDetail ckDetail = sgGarageDetailMapper.selectById(sgGarageOrder.getSgGarageCKDatailId());
            if(ckDetail.getBillStatus()!=SgAllocateTaskStatusEnum.OUTOFSTORE.getValue()){
                result.failure("???????????????????????????????????????");
                return result;
            }

            SgGarageDetail sgGarageDetail = sgGarageDetailMapper.selectById(dto.getSgGarageRKDatailId());
            SgGarageOrderLog sgGarageOrderLog = new SgGarageOrderLog();

            //?????????????????????????????????
            Integer billStatus = sgGarageDetail.getBillStatus();
            //?????????????????????
            if(billStatus != SgAllocateTaskStatusEnum.IN_GARAGING.getValue()){
                result.failure("???????????????????????????");
                return result;
            }
            sgGarageOrderLog.setEvent("????????????????????????");
            sgGarageDetail.setBillStatus(SgAllocateTaskStatusEnum.IN_REJECT.getValue());
            sgGarageOrder.setOrderStatus(SgAllocateTaskStatusEnum.IN_REJECT.getValue());



            //??????????????????
            sgGarageOrderLog.setSgGarageOrderId(sgGarageOrder.getId()); //??????ID
            sgGarageOrderLog.setCreateTime(new Date()); //????????????
            sgGarageOrderLog.setRemark(dto.getRemark()); //??????
            sgGarageOrderLog.setCreatorName(CurrentUser.getCnName());
            sgGarageOrderLog.setCreatorId(CurrentUser.getUserId());
            sgGarageOrderLog.insert();

            sgGarageOrder.insertOrUpdate();
            sgGarageDetail.insertOrUpdate();
            result.setData("????????????");
            return result;
        }catch (Exception e){
            logger.error("inReject() failed???{}",e.getMessage(), e);
            throw new BzException("inReject() failed", e);
        }
    }



    @Override
    public SgGarageOrderDTO getBill(String sgGarageOrderId) {
        logger.info("???????????? getBill() sgGarageOrderDTO=[{}]", JSONObject.toJSONString(sgGarageOrderId));
        SgGarageOrderDTO sgGarageOrderDTO = new SgGarageOrderDTO();
        try {
            SgGarageOrder sgGarageOrder = sgGarageOrderMapper.selectById(sgGarageOrderId);
            //??????Order
            BaseAssembler.mapObjWithoutBaseColumn(sgGarageOrder,sgGarageOrderDTO);
            sgGarageOrderDTO.setId(sgGarageOrderId);
            //????????????????????????
            SgVehicleInfo sgVehicleInfo = sgVehicleInfoMapper.selectById(sgGarageOrder.getSgVehicleId());
            if(sgVehicleInfo == null){
                logger.error("??????id["+sgGarageOrderId+"]?????????????????????????????????????????????");
                throw new BzException("??????id["+sgGarageOrderId+"]?????????????????????????????????????????????");
            }
            SgVehicleInfoDTO sgVehicleInfoDTO = new SgVehicleInfoDTO();
            BaseAssembler.mapObjWithoutBaseColumn(sgVehicleInfo,sgVehicleInfoDTO);
            sgVehicleInfoDTO.setId(sgVehicleInfo.getId());

            SgGarageInfo sgGarageInfo = sgGarageInfoMapper.selectById(sgGarageOrder.getSgGarageInfoToId()); //????????????
            if(sgGarageInfo != null){
                sgVehicleInfoDTO.setSgGarageName(sgGarageInfo.getGarageName());
                sgGarageOrderDTO.setGarageInName(sgGarageInfo.getGarageName());
            }

            List<SgVehicleInfoDTO> vehList = new ArrayList<>();
            vehList.add(sgVehicleInfoDTO);
            sgGarageOrderDTO.setVehicleInfoDTOList(vehList);
            logger.info("==============????????????????????????============");


            //????????????????????????
            List<AttachSourceDTO> attachSourceDTOS = attachSourceMapper.queryAtts(sgGarageOrder.getSgGarageCKDatailId(), RcAttachmentTypeEnum.SG_OUT_IN_GARAGE.getValue());
            sgGarageOrderDTO.setAttachSourceDTOList(attachSourceDTOS);
            logger.info("==============????????????getBill(): ???????????????????????????============");
            List<AttachSourceDTO> otherAttDTOLists = attachSourceMapper.queryAtts(sgGarageOrder.getSgGarageCKDatailId(),RcAttachmentTypeEnum.SG_OTHERS_OF_GARAGE.getValue());
            sgGarageOrderDTO.setOtherAttDTOList(otherAttDTOLists);
            logger.info("==============????????????getBill(): ????????????????????????============");


            QueryWrapper detailWrapper = new QueryWrapper();
            detailWrapper.eq("sgGaragOrder_id", sgGarageOrderId);
            detailWrapper.eq("IS_DELETED", 0);
            detailWrapper.eq("garage_sign", RCAllocateGarageSignEnum.GARAGEIN_SIGN.getValue());
            List<SgGarageDetail> sgGarageDetails = sgGarageDetailMapper.selectList(detailWrapper);
            SgGarageDetail sgGarageDetail = sgGarageDetails.get(0);
//            SgGarageDetail sgGarageDetail = sgGarageDetailMapper.selectById(sgGarageOrder.getSgGarageRKDatailId());
            sgGarageOrderDTO.setPushSource(sgGarageDetail.getPushSource()); //????????????
            sgGarageOrderDTO.setDetailTaskNum(sgGarageDetail.getTaskNum()); //????????????
            sgGarageOrderDTO.setPredictEndTime(sgGarageDetail.getTempPredictTime());//????????????
            logger.info("==============order????????????============");
        } catch (Exception e) {
            logger.error("getBill() failed???", e.getMessage());
            throw new BzException(e.getMessage());
        }
        return sgGarageOrderDTO;
    }

    @Override
    public Page<SgGarageOrderLogDTO> pageQueryLog(SgGarageOrderLogDTO dto) {
        logger.info("???????????????????????????????????????{}", JSONObject.toJSON(dto));
        Page<SgGarageOrderLogDTO> dtoPage = null;
        try{
            Page<SgGarageOrderLog> page = new PageFactory<SgGarageOrderLog>().defaultPage(dto);
            IPage<SgGarageOrderLogDTO> pageList = sgGarageOrderLogMapper.pageQueryLog(page, dto);
            logger.info("=====?????????[" + pageList.getSize() + "]?????????");
            dtoPage = new Page<SgGarageOrderLogDTO>(pageList.getCurrent(), pageList.getSize(), pageList.getTotal()).setRecords(pageList.getRecords());
        }catch (Exception e){
            logger.error("pageQuery() failed???", e);
            throw new BzException("pageQuery() failed:",e);
        }
        return dtoPage;
    }


}
