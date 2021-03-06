package com.yixin.garage.service.api.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.JsonObject;
import com.yixin.common.exception.BzException;
import com.yixin.common.utils.DateUitls;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.auth.CurrentUser;
import com.yixin.garage.dao.garage.SgGarageInfoMapper;
import com.yixin.garage.dao.order.SgGarageDetailMapper;
import com.yixin.garage.dao.order.SgGarageOrderMapper;
import com.yixin.garage.dao.vehicle.SgVehicleInfoMapper;
import com.yixin.garage.dto.api.forLoan.SgGarageForLoanGarageInDTO;
import com.yixin.garage.dto.api.forLoan.SgGarageForLoanGarageOutDTO;
import com.yixin.garage.dto.api.forLoan.SgQueryVehicleIfoStatDTO;
import com.yixin.garage.dto.sys.VehicleInoutRecordDTO;
import com.yixin.garage.entity.garage.SgGarageInfo;
import com.yixin.garage.entity.order.SgGarageDetail;
import com.yixin.garage.entity.order.SgGarageOrder;
import com.yixin.garage.entity.order.SgGarageOrderLog;
import com.yixin.garage.entity.vehicle.SgVehicleInfo;
import com.yixin.garage.entity.vehicle.SgVehicleLog;
import com.yixin.garage.entity.zhongtai.SgSendInfoToZhongtai;
import com.yixin.garage.enums.VehicleTypeEnum;
import com.yixin.garage.enums.garage.*;
import com.yixin.garage.enums.garage.zhongtai.BillStatusEnum;
import com.yixin.garage.enums.garage.zhongtai.SendTypeEnum;
import com.yixin.garage.service.api.SgGarageForLoanService;
import com.yixin.garage.service.order.ISgGarageOrderService;
import com.yixin.garage.util.JacksonUtil;
import com.yixin.garage.util.RestUtil;
import com.yixin.garage.util.ResultUtil;
import com.yixin.garage.util.TaskNumUtil;
import com.yixin.garage.util.excel.inptVO.DataCreateImportVO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @description:
 * @author: YixinCapital -- libochen
 * @create: 2019-08-13 17:02
 **/
@Service
@Transactional
public class SgGarageForLoanServiceImpl implements SgGarageForLoanService {


    private Logger logger = LoggerFactory.getLogger(SgGarageForLoanServiceImpl.class);

    @Autowired
    SgGarageInfoMapper sgGarageInfoMapper;

    @Autowired
    SgVehicleInfoMapper sgVehicleInfoMapper;

    @Autowired
    SgGarageDetailMapper sgGarageDetailMapper;

    @Autowired
    SgGarageOrderMapper sgGarageOrderMapper;


//    @Autowired
//    SgVehicleInfoMapper sgVehicleInfoMapper;

    @Autowired
    ISgGarageOrderService iSgGarageOrderService;

    @Value("${rentCarUrl}")
    private String rentCarUrl;

    @Override
    public InvokeResult<String> inGarage(SgGarageForLoanGarageInDTO dto) throws BzException {
        logger.info("????????????????????????- SgGarageForLoanServiceImpl.inGarage() start!");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        InvokeResult<String> result = new InvokeResult<String>();
        try{
            //?????????????????????????????????
            QueryWrapper querySameWrapper = new QueryWrapper();
            querySameWrapper.eq("alix_num", dto.getApplyNo());
            querySameWrapper.eq("vin", dto.getVin());
            querySameWrapper.eq("garage_sign", RCAllocateGarageSignEnum.GARAGEIN_SIGN.getValue());
            querySameWrapper.eq("bill_status", SgAllocateTaskStatusEnum.IN_GARAGING.getValue());
            List<SgGarageDetail> sgGarageDetails =  sgGarageDetailMapper.selectList(querySameWrapper);
            if (sgGarageDetails.size() > 0) {
                logger.info("?????????????????????????????????????????????");
                result.failure("?????????????????????????????????????????????");
                return result;
            }

            //???????????????????????????????????????
            QueryWrapper garageWrapper = new QueryWrapper();
            garageWrapper.eq("id", dto.getGarageId());
            garageWrapper.eq("IS_DELETED", 0);
            garageWrapper.eq("garage_status", GarageStatusEnum.NORMAL.getValue());
            List<SgGarageInfo> sgGarageInfos = sgGarageInfoMapper.selectList(garageWrapper);
            logger.info("==========??????????????????{}",JSONObject.toJSON(sgGarageInfos));
            if (CollectionUtils.isEmpty(sgGarageInfos)) {
                logger.info("???????????????????????????????????????????????????????????????");
                result.failure("???????????????????????????????????????????????????????????????");
                return result;
            }
            SgGarageInfo sgGarageInfo = sgGarageInfos.get(0);


            QueryWrapper vehicleStateWrapper = new QueryWrapper();
//            vehicleWrapper.eq("garage_name", dto.getGarageIn());

            vehicleStateWrapper.eq("IS_DELETED", 0);
            vehicleStateWrapper.eq("vin", dto.getVin());
            List<SgVehicleInfo> sgVehicleStateInfos = sgVehicleInfoMapper.selectList(vehicleStateWrapper);
            logger.info("==========??????????????????{}",JSONObject.toJSON(sgVehicleStateInfos));
            if (null != sgVehicleStateInfos && !sgVehicleStateInfos.isEmpty()) {
                int state = sgVehicleStateInfos.get(0).getStat();
                //?????????????????????
                if (state != SgVehicleStatusEnum.DISPOSE_OUT.getValue()) {
                    logger.info("????????????????????????" + SgVehicleStatusEnum.getDisplayNameByIndex(state) + "??????????????????");
                    result.failure("????????????????????????" + SgVehicleStatusEnum.getDisplayNameByIndex(state) + "??????????????????");
                    return result;
                }
            }

            //???????????????????????????
            if("1".equals(dto.getIsCancel())){
                QueryWrapper vehicleWrapper = new QueryWrapper();
                vehicleWrapper.eq("IS_DELETED", 0);
                vehicleWrapper.eq("vin", dto.getVin());
                vehicleWrapper.eq("alix_num", dto.getApplyNo());
                List<SgVehicleInfo> sgVehicleInfos = sgVehicleInfoMapper.selectList(vehicleWrapper);

                if(CollectionUtils.isEmpty(sgVehicleInfos)){
                    SgVehicleInfo ve = new SgVehicleInfo();
                    SgVehicleLog veLog = new SgVehicleLog();
                    createVehicleInfo(sgGarageInfo,ve,veLog,dto);

                    //????????????????????????
                    SgGarageOrder sgGarageOrder = new SgGarageOrder();
                    SgGarageOrderLog sgGarageOrderLog = new SgGarageOrderLog();
                    String maxGarageOrderNum = sgGarageOrderMapper.createGarageOrderTaskNum(formatter.format(new Date()));
                    sgGarageOrder.setTaskNum(TaskNumUtil.createGarageOrderTaskNum(maxGarageOrderNum));
                    //??????????????????
                    sgGarageOrder.setConsignmentFee(dto.getConsignmentFee().toString());
                    sgGarageOrder.setSgGarageInfoToId(dto.getGarageId());
                    sgGarageOrder.setSgVehicleId(ve.getId());
                    sgGarageOrder.setOrderStatus(SgAllocateTaskStatusEnum.INOFSTORE.getValue());
                    sgGarageOrder.setSgGarageInfoToId(sgGarageInfo.getId());
                    sgGarageOrder.insert();

                    //???????????????
                    SgGarageDetail rkDetail = new SgGarageDetail();
                    //?????????????????????
                    rkDetail.setOrderType(dto.getTbCarType());//??????????????? 1??????  2:??????
                    String maxGarageOrderRkNum = sgGarageDetailMapper.createTaskNum(formatter.format(new Date()), "1");
                    rkDetail.setTaskNum(TaskNumUtil.createTaskNum("RK",maxGarageOrderRkNum));
                    rkDetail.setContractNo(dto.getContractNo());
                    rkDetail.setColectPerson(dto.getColectPerson());//????????????
                    rkDetail.setColectPersonNum(dto.getColectPersonNum());//?????????????????????
                    rkDetail.setAlixNum(dto.getApplyNo());//alix????????????
                    rkDetail.setVin(dto.getVin());
                    rkDetail.setSgGaragOrderId(sgGarageOrder.getId());
                    rkDetail.setGarageSign(RCAllocateGarageSignEnum.GARAGEIN_SIGN.getValue());
                    rkDetail.setPushSource(PushSourceEnum.LOAN_PUSH.getValue());
                    rkDetail.setBillStatus(SgAllocateTaskStatusEnum.INOFSTORE.getValue());
                    rkDetail.insert();
                    sgGarageOrder.setSgGarageRKDatailId(rkDetail.getId());
                    sgGarageOrder.insertOrUpdate();
                    sgGarageOrderLog.setEvent("??????????????????");
                    sgGarageOrderLog.setSgGarageInfoId(dto.getGarageId());
                    sgGarageOrderLog.setCreatorName("????????????");
                    sgGarageOrderLog.setSgGarageOrderId(sgGarageOrder.getId());
                    sgGarageOrderLog.insert();

                    sendToRentcar(ve, rkDetail);
                }else{
                    SgVehicleInfo ve = sgVehicleInfos.get(0);
                    int oldStat = ve.getStat();
                    ve.setStat(SgVehicleStatusEnum.FREE.getValue());
                    sgVehicleInfoMapper.updateById(ve);

                    //???????????????
                    QueryWrapper detailWrapper = new QueryWrapper();
                    detailWrapper.eq("IS_DELETED", 0);
                    detailWrapper.eq("vin", dto.getVin());
                    detailWrapper.eq("alix_num", dto.getApplyNo());
                    detailWrapper.eq("garage_sign", RCAllocateGarageSignEnum.GARAGEIN_SIGN.getValue());
                    detailWrapper.eq("push_source", PushSourceEnum.LOAN_PUSH.getValue());
                    List<SgGarageDetail> details = sgGarageDetailMapper.selectList(detailWrapper);
                    if(CollectionUtils.isEmpty(details)){
                        logger.info("?????????????????????????????????");
                        result.failure("?????????????????????????????????");
                        return result;
                    }
                    for(SgGarageDetail detail  : details){
                        detail.setBillStatus(SgAllocateTaskStatusEnum.CANCEL.getValue());
                        detail.insertOrUpdate();
                        SgGarageOrder order = sgGarageOrderMapper.selectById(detail.getSgGaragOrderId());
                        order.setOrderStatus(SgAllocateTaskStatusEnum.CANCEL.getValue());
                        order.insertOrUpdate();
                        SgGarageDetail ckdetail = sgGarageDetailMapper.selectById(order.getSgGarageCKDatailId());
                        ckdetail.setBillStatus(SgAllocateTaskStatusEnum.CANCEL.getValue());
                        ckdetail.insertOrUpdate();
                    }

                    //????????????????????????
                    SgGarageOrder sgGarageOrder = new SgGarageOrder();
                    SgGarageOrderLog sgGarageOrderLog = new SgGarageOrderLog();
                    String maxGarageOrderNum = sgGarageOrderMapper.createGarageOrderTaskNum(formatter.format(new Date()));
                    sgGarageOrder.setTaskNum(TaskNumUtil.createGarageOrderTaskNum(maxGarageOrderNum));
                    //??????????????????
                    sgGarageOrder.setConsignmentFee(dto.getConsignmentFee().toString());
                    sgGarageOrder.setSgGarageInfoToId(dto.getGarageId());
                    sgGarageOrder.setSgVehicleId(ve.getId());
                    sgGarageOrder.setOrderStatus(SgAllocateTaskStatusEnum.INOFSTORE.getValue());
                    sgGarageOrder.setSgGarageInfoToId(sgGarageInfo.getId());
                    sgGarageOrder.insert();

                    //???????????????
                    SgGarageDetail rkDetail = new SgGarageDetail();
                    //?????????????????????
                    rkDetail.setOrderType(dto.getTbCarType());//??????????????? 1??????  2:??????
                    String maxGarageOrderRkNum = sgGarageDetailMapper.createTaskNum(formatter.format(new Date()), "1");
                    rkDetail.setTaskNum(TaskNumUtil.createTaskNum("RK",maxGarageOrderRkNum));
                    rkDetail.setContractNo(dto.getContractNo());
                    rkDetail.setColectPerson(dto.getColectPerson());//????????????
                    rkDetail.setColectPersonNum(dto.getColectPersonNum());//?????????????????????
                    rkDetail.setAlixNum(dto.getApplyNo());//alix????????????
                    rkDetail.setVin(dto.getVin());
                    rkDetail.setSgGaragOrderId(sgGarageOrder.getId());
                    rkDetail.setGarageSign(RCAllocateGarageSignEnum.GARAGEIN_SIGN.getValue());
                    rkDetail.setPushSource(PushSourceEnum.LOAN_PUSH.getValue());
                    rkDetail.setBillStatus(SgAllocateTaskStatusEnum.INOFSTORE.getValue());
                    rkDetail.insert();
                    sgGarageOrder.setSgGarageRKDatailId(rkDetail.getId());
                    sgGarageOrder.insertOrUpdate();
                    sgGarageOrderLog.setEvent("??????????????????");
                    sgGarageOrderLog.setSgGarageInfoId(dto.getGarageId());
                    sgGarageOrderLog.setCreatorName("????????????");
                    sgGarageOrderLog.setSgGarageOrderId(sgGarageOrder.getId());
                    sgGarageOrderLog.insert();
                    sendToRentcar(ve, rkDetail);

                    //??????????????????
                    SgVehicleLog sgVehicleLog = new SgVehicleLog();
                    sgVehicleLog.setEvent("????????????");
                    sgVehicleLog.setCreateTime(new Date());// ????????????
                    sgVehicleLog.setCreatorName("????????????");// ?????????
                    sgVehicleLog.setCreatorDepartmentName("????????????");
                    sgVehicleLog.setVehicleId(ve.getId());
                    sgVehicleLog.setRemark("???????????????[" + SgVehicleStatusEnum.getDisplayNameByIndex(oldStat) + "]?????????[" + SgVehicleStatusEnum.getDisplayNameByIndex(ve.getStat()) + "]");
                    sgVehicleLog.insert();

                }
            }else{
                //??????????????????
                QueryWrapper vehicleWrapper = new QueryWrapper();
                vehicleWrapper.eq("IS_DELETED", 0);
                vehicleWrapper.eq("vin", dto.getVin());
                vehicleWrapper.eq("alix_num", dto.getApplyNo());
                List<SgVehicleInfo> sgVehicleInfos = sgVehicleInfoMapper.selectList(vehicleWrapper);
                if (CollectionUtils.isEmpty(sgVehicleInfos)){
                    //????????????
                    SgVehicleInfo ve = new SgVehicleInfo();
                    SgVehicleLog veLog = new SgVehicleLog();
                    createVehicleInfo(sgGarageInfo,ve,veLog,dto);

                    //????????????????????????
                    SgGarageOrder sgGarageOrder = new SgGarageOrder();
                    SgGarageOrderLog sgGarageOrderLog = new SgGarageOrderLog();
                    String maxGarageOrderNum = sgGarageOrderMapper.createGarageOrderTaskNum(formatter.format(new Date()));
                    sgGarageOrder.setTaskNum(TaskNumUtil.createGarageOrderTaskNum(maxGarageOrderNum));

                    //??????????????????
                    sgGarageOrder.setConsignmentFee(dto.getConsignmentFee().toString());
                    sgGarageOrder.setSgGarageInfoToId(dto.getGarageId());
                    sgGarageOrder.setSgVehicleId(ve.getId());
                    sgGarageOrder.setOrderStatus(SgAllocateTaskStatusEnum.INOFSTORE.getValue());
                    sgGarageOrder.setSgGarageInfoToId(sgGarageInfo.getId());
                    sgGarageOrder.insert();

                    //???????????????
                    SgGarageDetail rkDetail = new SgGarageDetail();
                    //?????????????????????
                    rkDetail.setOrderType(dto.getTbCarType());//??????????????? 1??????  2:??????
                    String maxGarageOrderRkNum = sgGarageDetailMapper.createTaskNum(formatter.format(new Date()), "1");
                    rkDetail.setTaskNum(TaskNumUtil.createTaskNum("RK",maxGarageOrderRkNum));
                    rkDetail.setContractNo(dto.getContractNo());
                    rkDetail.setColectPerson(dto.getColectPerson());//????????????
                    rkDetail.setColectPersonNum(dto.getColectPersonNum());//?????????????????????
                    rkDetail.setAlixNum(dto.getApplyNo());//alix????????????
                    rkDetail.setVin(dto.getVin());
                    rkDetail.setSgGaragOrderId(sgGarageOrder.getId());
                    rkDetail.setGarageSign(RCAllocateGarageSignEnum.GARAGEIN_SIGN.getValue());
                    rkDetail.setPushSource(PushSourceEnum.LOAN_PUSH.getValue());
                    rkDetail.setBillStatus(SgAllocateTaskStatusEnum.INOFSTORE.getValue());
                    rkDetail.insert();
                    sgGarageOrder.setSgGarageRKDatailId(rkDetail.getId());
                    sgGarageOrder.insertOrUpdate();
                    sgGarageOrderLog.setEvent("??????????????????");
                    sgGarageOrderLog.setSgGarageInfoId(dto.getGarageId());
                    sgGarageOrderLog.setCreatorName("????????????");
                    sgGarageOrderLog.setSgGarageOrderId(sgGarageOrder.getId());
                    sgGarageOrderLog.insert();
                    sendToRentcar(ve, rkDetail);
                }else{
                    SgVehicleInfo ve = sgVehicleInfos.get(0);
                    int oldStat = ve.getStat();
                    ve.setStat(SgVehicleStatusEnum.FREE.getValue());
                    ve.insertOrUpdate();

                    //????????????????????????
                    SgGarageOrder sgGarageOrder = new SgGarageOrder();
                    SgGarageOrderLog sgGarageOrderLog = new SgGarageOrderLog();
                    String maxGarageOrderNum = sgGarageOrderMapper.createGarageOrderTaskNum(formatter.format(new Date()));
                    sgGarageOrder.setTaskNum(TaskNumUtil.createGarageOrderTaskNum(maxGarageOrderNum));

                    //??????????????????
                    sgGarageOrder.setConsignmentFee(dto.getConsignmentFee().toString());
                    sgGarageOrder.setSgGarageInfoToId(dto.getGarageId());
                    sgGarageOrder.setSgVehicleId(ve.getId());
                    sgGarageOrder.setOrderStatus(SgAllocateTaskStatusEnum.INOFSTORE.getValue());
                    sgGarageOrder.setSgGarageInfoToId(sgGarageInfo.getId());
                    sgGarageOrder.insert();

                    //???????????????
                    SgGarageDetail rkDetail = new SgGarageDetail();
                    //SgGarageDetail ckDetail = new SgGarageDetail();
                    //?????????????????????
                    rkDetail.setOrderType(dto.getTbCarType());//??????????????? 1??????  2:??????
                    String maxGarageOrderRkNum = sgGarageDetailMapper.createTaskNum(formatter.format(new Date()), "1");
                    rkDetail.setTaskNum(TaskNumUtil.createTaskNum("RK",maxGarageOrderRkNum));
                    rkDetail.setContractNo(dto.getContractNo());
                    rkDetail.setColectPerson(dto.getColectPerson());//????????????
                    rkDetail.setColectPersonNum(dto.getColectPersonNum());//?????????????????????
                    rkDetail.setAlixNum(dto.getApplyNo());//alix????????????
                    rkDetail.setVin(dto.getVin());
                    rkDetail.setSgGaragOrderId(sgGarageOrder.getId());
                    rkDetail.setGarageSign(RCAllocateGarageSignEnum.GARAGEIN_SIGN.getValue());
                    rkDetail.setPushSource(PushSourceEnum.LOAN_PUSH.getValue());
                    rkDetail.setBillStatus(SgAllocateTaskStatusEnum.INOFSTORE.getValue());
                    rkDetail.insert();
                    sgGarageOrder.setSgGarageRKDatailId(rkDetail.getId());
                    sgGarageOrder.insertOrUpdate();
                    sgGarageOrderLog.setEvent("??????????????????");
                    sgGarageOrderLog.setSgGarageInfoId(dto.getGarageId());
                    sgGarageOrderLog.setCreatorName("????????????");
                    sgGarageOrderLog.setSgGarageOrderId(sgGarageOrder.getId());
                    sgGarageOrderLog.insert();

                    SgVehicleLog vehicleLog = new SgVehicleLog();
                    vehicleLog.setEvent("??????????????????");
                    vehicleLog.setRemark("???????????????[" + SgVehicleStatusEnum.getDisplayNameByIndex(oldStat) + "]?????????[" + SgVehicleStatusEnum.getDisplayNameByIndex(ve.getStat()) + "]");
                    vehicleLog.setGarageId(sgGarageInfo.getId());
                    vehicleLog.setGarageName(sgGarageInfo.getGarageName());
                    vehicleLog.setVehicleId(ve.getId());
                    vehicleLog.insert();
                    sendToRentcar(ve, rkDetail);
                }
            }

            sgGarageInfo.setParkedNum(sgGarageInfo.getParkedNum() + 1);
            sgGarageInfo.insertOrUpdate();

            //????????????????????????
            logger.info("SgGarageForLoanServiceImpl inGarage()??????????????????:???????????????????????????????????????????????? =========");
            SgSendInfoToZhongtai rcSendInfoToZhongtai = new SgSendInfoToZhongtai();
            rcSendInfoToZhongtai.setBillNum(dto.getApplyNo());//??????
            rcSendInfoToZhongtai.setInterfaceName(SendTypeEnum.PSM_RESTOCK_INTERFACE.getValue());//????????????(??????)
            rcSendInfoToZhongtai.setSendCount(0);//????????????
            rcSendInfoToZhongtai.setType(BillStatusEnum.PSM_RESTOCK_INTERFACE.getValue());//????????????
            rcSendInfoToZhongtai.setStat(0);//????????????
            rcSendInfoToZhongtai.setCreatorName("??????????????????");
            rcSendInfoToZhongtai.insert();

        }catch (Exception e) {
            logger.error("inGarage() failed");
            throw new BzException("inGarage() failed", e);
        }
        return ResultUtil.success("??????????????????");
    }


    @Transactional
    @Override
    public InvokeResult<String> outGarage(SgGarageForLoanGarageOutDTO dto) throws BzException {
        logger.info("????????????????????????- SgGarageForLoanServiceImpl.inGarage() start!");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        InvokeResult<String> result = new InvokeResult<String>();
        try{
            //???????????????????????????
            QueryWrapper queryCkWrapper = new QueryWrapper();
            queryCkWrapper.eq("IS_DELETED", 0);
            queryCkWrapper.ne("bill_status", SgAllocateTaskStatusEnum.CANCEL.getValue());//????????????????????????????????????
            queryCkWrapper.eq("push_source", PushSourceEnum.LOAN_PUSH.getValue());
            queryCkWrapper.eq("vin", dto.getVin());
            queryCkWrapper.eq("garage_sign", RCAllocateGarageSignEnum.GARAGEOUT_SIGN.getValue());
            queryCkWrapper.eq("alix_num", dto.getApplyNo());
            List<SgGarageDetail> sgGarageCKDetails = sgGarageDetailMapper.selectList(queryCkWrapper);
            if (CollUtil.isNotEmpty(sgGarageCKDetails)) {
                logger.info("?????????????????????????????????????????????");
                result.failure("?????????????????????????????????????????????");
                return result;
            }

            //???????????????????????????
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("IS_DELETED", 0);
            queryCkWrapper.ne("bill_status", SgAllocateTaskStatusEnum.CANCEL.getValue());//????????????????????????????????????
            queryWrapper.eq("vin", dto.getVin());
            queryWrapper.eq("push_source", PushSourceEnum.LOAN_PUSH.getValue());
            queryWrapper.eq("garage_sign", RCAllocateGarageSignEnum.GARAGEIN_SIGN.getValue());
            queryWrapper.eq("alix_num", dto.getApplyNo());
            List<SgGarageDetail> sgGarageDetails = sgGarageDetailMapper.selectList(queryWrapper);

            SgGarageDetail ckDetail = new SgGarageDetail();
            if(CollectionUtils.isNotEmpty(sgGarageDetails)){
                SgGarageOrder sgGarageOrder = sgGarageOrderMapper.selectById(sgGarageDetails.get(0).getSgGaragOrderId());
                SgGarageDetail rkDetail = sgGarageDetails.get(0);
                //???????????????
                String maxGarageOrderCkNum = sgGarageDetailMapper.createTaskNum(formatter.format(new Date()), "0");
                ckDetail.setTaskNum(TaskNumUtil.createTaskNum("CK",maxGarageOrderCkNum));
                ckDetail.setContractNo(ckDetail.getContractNo());//????????????
                ckDetail.setOrderType(rkDetail.getOrderType());//??????????????? 1??????  2:??????
                ckDetail.setColectPerson(rkDetail.getColectPerson());//????????????
                ckDetail.setColectPersonNum(rkDetail.getColectPersonNum());//
                ckDetail.setAlixNum(rkDetail.getAlixNum());//alix????????????
                ckDetail.setVin(rkDetail.getVin());
                ckDetail.setGarageSign(RCAllocateGarageSignEnum.GARAGEOUT_SIGN.getValue());
                ckDetail.setPushSource(PushSourceEnum.LOAN_PUSH.getValue());
                ckDetail.setSgGaragOrderId(sgGarageOrder.getId());
                ckDetail.setBillStatus(SgAllocateTaskStatusEnum.OUTOFSTORE.getValue());
                ckDetail.setPushSource(PushSourceEnum.LOAN_PUSH.getValue());

                if(dto.getOutReason().equals("01")){
                    ckDetail.setOutReason(OutReasonEnum.DISPOSEOUT.getValue());
                }
                if(dto.getOutReason().equals("12")){
                    ckDetail.setOutReason(OutReasonEnum.REDEEMOUT.getValue());
                }
                if(dto.getOutReason().equals("14")){
                    ckDetail.setOutReason(OutReasonEnum.TRANSFEROUT.getValue());
                }

                sgGarageOrder.setActualStartTime(new Date());

                //??????????????????
                QueryWrapper vehicleWrapper = new QueryWrapper();
                vehicleWrapper.eq("IS_DELETED", 0);
                vehicleWrapper.eq("alix_num", dto.getApplyNo());
                vehicleWrapper.eq("vin", dto.getVin());
                List<SgVehicleInfo> sgVehicleInfos = sgVehicleInfoMapper.selectList(vehicleWrapper);
                if(CollectionUtils.isNotEmpty(sgVehicleInfos)){
                    SgVehicleInfo ve = sgVehicleInfos.get(0);
                    int oldStat = ve.getStat();
                    if (ve.getStat() != SgVehicleStatusEnum.FREE.getValue()) {
                        logger.info("??????????????????" + SgVehicleStatusEnum.getDisplayNameByIndex(ve.getStat()) + "????????????????????????");
                        result.failure("??????????????????" + SgVehicleStatusEnum.getDisplayNameByIndex(ve.getStat()) + "????????????????????????");
                        return result;
                    }
                    ve.setStat(SgVehicleStatusEnum.DISPOSE_OUT.getValue());
                    ve.insertOrUpdate();
                    //???????????????
                    SgGarageInfo garageInfo = sgGarageInfoMapper.selectById(ve.getSgGarageInfoId());
                    garageInfo.setParkedNum(garageInfo.getParkedNum() - 1);
                    garageInfo.insertOrUpdate();
                    sgGarageOrder.setSgGarageInfoFromId(ve.getSgGarageInfoId());
                    //??????????????????
                    SgVehicleLog sgVehicleLog = new SgVehicleLog();
                    sgVehicleLog.setEvent("????????????");
                    sgVehicleLog.setCreateTime(new Date());// ????????????
                    sgVehicleLog.setCreatorName("????????????");// ?????????
                    sgVehicleLog.setCreatorDepartmentName("????????????");
                    sgVehicleLog.setVehicleId(ve.getId());
                    sgVehicleLog.setRemark("???????????????[" + SgVehicleStatusEnum.getDisplayNameByIndex(oldStat) + "]?????????[" + SgVehicleStatusEnum.getDisplayNameByIndex(ve.getStat()) + "]");
                    sgVehicleLog.insert();

                }
                sgGarageOrder.setOrderStatus(SgAllocateTaskStatusEnum.OUTOFSTORE.getValue());
                ckDetail.insert();
                sgGarageOrder.setSgGarageCKDatailId(ckDetail.getId());
                sgGarageOrder.insertOrUpdate();

                SgGarageOrderLog sgGarageOrderLog = new SgGarageOrderLog();
                sgGarageOrderLog.setEvent("??????????????????");
                sgGarageOrderLog.setSgGarageOrderId(ckDetail.getSgGaragOrderId());
                sgGarageOrderLog.setCreatorName("????????????");
                sgGarageOrderLog.insert();

            }else{
                logger.error("????????????????????????????????????");
                result.failure("????????????????????????????????????");
                return result;
            }

        }catch (Exception e) {
            logger.error("outGarage() failed");
            throw new BzException("outGarage() failed", e);
        }
        logger.error("??????????????????");
        return ResultUtil.success("??????????????????");
    }



    @Override
    public InvokeResult<SgQueryVehicleIfoStatDTO> queryVehicleStat(SgQueryVehicleIfoStatDTO dto) throws BzException {
        logger.info("????????????????????????- SgGarageForLoanServiceImpl.inGarage() start!");
        InvokeResult<SgQueryVehicleIfoStatDTO> result = new InvokeResult<SgQueryVehicleIfoStatDTO>();
        try{
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("IS_DELETED", 0);
            queryWrapper.eq("alix_num", dto.getApplyNo());
            queryWrapper.eq("vin", dto.getVin());
            List<SgVehicleInfo> sgVehicleInfos = sgVehicleInfoMapper.selectList(queryWrapper);
            if(sgVehicleInfos.size()<=0){
                result.failure("??????????????????????????????");
                return result;
            }
            SgVehicleInfo ve = sgVehicleInfos.get(0);
            SgGarageInfo garageInfo = sgGarageInfoMapper.selectById(ve.getSgGarageInfoId());

            SgQueryVehicleIfoStatDTO resultDto = new SgQueryVehicleIfoStatDTO();
            resultDto.setApplyNo(ve.getAlixNum());
            resultDto.setState(ve.getStat());
            resultDto.setVin(ve.getVin());
            resultDto.setGarageId(ve.getSgGarageInfoId());
            resultDto.setGarageName(garageInfo.getGarageName());
            result.setData(resultDto);

        }catch(Exception e){
            logger.error("queryVehicleStat() failed");
            throw new BzException("queryVehicleStat() failed", e);
        }
        return result;
    }


    @Transactional
    @Override
    public InvokeResult<String> dateCreate (DataCreateImportVO dto) throws BzException {
        logger.info("?????????????????????- SgGarageForLoanServiceImpl.dateCreate() start!");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        InvokeResult<String> result = new InvokeResult<String>();
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        try{
            //?????????????????????????????????
            QueryWrapper garageWrapper = new QueryWrapper();
            garageWrapper.eq("id", dto.getGarageId());
            garageWrapper.eq("IS_DELETED", 0);
            garageWrapper.eq("garage_status", GarageStatusEnum.NORMAL.getValue());
            List<SgGarageInfo> sgGarageInfos = sgGarageInfoMapper.selectList(garageWrapper);
            logger.info("==========??????????????????{}",JSONObject.toJSON(sgGarageInfos));

            if (sgGarageInfos == null || sgGarageInfos.size() == 0) {
                logger.info("??????????????????????????????????????????????????????");
                result.failure("??????????????????????????????????????????????????????");
                return result;
            }

            SgGarageInfo sgGarageInfo = sgGarageInfos.get(0);
            QueryWrapper vehicleWrapper = new QueryWrapper();
//            vehicleWrapper.eq("garage_name", dto.getGarageIn());
            vehicleWrapper.eq("IS_DELETED", 0);
            vehicleWrapper.eq("vin", dto.getVin());
            vehicleWrapper.eq("alix_num", dto.getApplyNo());
            vehicleWrapper.eq("stat", SgVehicleStatusEnum.FREE.getValue());
            List<SgVehicleInfo> sgVehicleInfos = sgVehicleInfoMapper.selectList(vehicleWrapper);
            SgVehicleInfo vehicleInfo = new SgVehicleInfo();
            SgVehicleLog vehicleLog = new SgVehicleLog();
            if (null == sgVehicleInfos || sgVehicleInfos.size() == 0) {
                //???????????????????????????????????????????????????
                vehicleInfo.setStat(SgVehicleStatusEnum.FREE.getValue());
                //?????????????????????????????????
//                vehicleInfo.setPropertyRightType(dto.getPropertyRightType());//???????????????
                vehicleInfo.setVin(dto.getVin());
                vehicleInfo.setAlixNum(dto.getApplyNo());

                if(dto.getLeaseProperty().equals("??????")){
                    vehicleInfo.setLeaseProperty(LeasePropertyEnum.LEASEBACK_CAR.getValue());
                }
                if(dto.getLeaseProperty().equals("??????")){
                    vehicleInfo.setLeaseProperty(LeasePropertyEnum.RESOURCE_CAR.getValue());
                }

                if(dto.getBusinessType().equals("?????????")){
                    vehicleInfo.setBusinessType(BusinessTypeEnum.CONSUMER_LOAN.getValue());
                }
                if(dto.getBusinessType().equals("???????????????")){
                    vehicleInfo.setBusinessType(BusinessTypeEnum.OPERATE.getValue());
                }
                if(dto.getBusinessType().equals("?????????")){
                    vehicleInfo.setBusinessType(BusinessTypeEnum.MORTGAGE_CAR.getValue());
                }
                if(dto.getBusinessType().equals("?????????")){
                    vehicleInfo.setBusinessType(BusinessTypeEnum.JOINT_LOAN.getValue());
                }

                vehicleInfo.setModel(dto.getModel());
                vehicleInfo.setColor(dto.getColor());

                //???????????????true,????????????false
                if(pattern.matcher(dto.getMileage()).matches()){
                    vehicleInfo.setMileage(new BigDecimal(dto.getMileage()));
                }else{
                    vehicleInfo.setMileage(new BigDecimal(0));
                }

                vehicleInfo.setLicNum(dto.getLicNum());

                if (dto.getIsKey() != null && !dto.getIsKey().equals("")) {
                    if (dto.getIsKey().equals("???")) {
                        vehicleInfo.setIsKey("0");
                    } else {
                        vehicleInfo.setIsKey(dto.getKeyNum());
                    }
                }
                vehicleInfo.setIsDrivingLicense(dto.getIsDrivinglicense());
                vehicleInfo.setIsNormalStart(dto.getIsNormalStart());
                vehicleInfo.setIsDragGarage(dto.getIsDragGarage());
                vehicleInfo.setIsNewKey(dto.getHasNewKey());
                vehicleInfo.setIsLicnumCar(dto.getHasLicense());
                vehicleInfo.setIsBatteryWork(dto.getHasBattery());
                vehicleInfo.setIsSpare(dto.getHasSpareTire());//???????????????
                vehicleInfo.setIsTyreModel(dto.getTyreUniformity());//????????????????????????
                vehicleInfo.setLeftFrontNum(dto.getTyreCodingLeftFront());
                vehicleInfo.setLeftRearNum(dto.getTyreCodingLeftBack());
                vehicleInfo.setRightFrontNum(dto.getTyreCodingRightFront());
                vehicleInfo.setRightRearNum(dto.getTyreCodingRightBack());
                vehicleInfo.setOilQuantity(dto.getOilQuantity());//????????????
                vehicleInfo.setVehicleCross(dto.getIncarArticles());//????????????
                vehicleInfo.setDamageDesc(dto.getDamageDescript());
                vehicleInfo.setRemoveGps(dto.getGpsDelete());//gps????????????
                vehicleInfo.setLessee(dto.getLesseeName());
                vehicleInfo.setDisposeHandover(dto.getDisposalHandoverPersonnel());//?????????????????????
                vehicleInfo.setDisposeCerNum(dto.getDisposalHandoverPersonnelNum());//?????????????????????
                vehicleInfo.setSgGarageInfoId(dto.getGarageId());
//                vehicleInfo.setActualStorageTime(DateUitls.strToDate(dto.getActualStorageTime()));
                vehicleInfo.setVehicleSource(SgVehicleSourceEnum.DHSC.getValue());
                //????????????????????????????????????????????????????????????
                vehicleInfo.setIsUpdate(false);
                vehicleInfo.insert();
            }

            //??????????????????   ------???????????????????????????
            SgGarageOrder sgGarageOrder = new SgGarageOrder();
            SgGarageOrderLog sgGarageOrderLog = new SgGarageOrderLog();
            String maxGarageOrderNum = sgGarageOrderMapper.createGarageOrderTaskNum(formatter.format(new Date()));
            sgGarageOrder.setTaskNum(TaskNumUtil.createGarageOrderTaskNum(maxGarageOrderNum));
            //??????????????????
            sgGarageOrder.setConsignmentFee(null);
            sgGarageOrder.setSgGarageInfoToId(dto.getGarageId());
            sgGarageOrder.setSgVehicleId(vehicleInfo.getId());
            sgGarageOrder.setOrderStatus(SgAllocateTaskStatusEnum.INOFSTORE.getValue());
            sgGarageOrder.setSgGarageInfoToId(sgGarageInfo.getId());
            sgGarageOrder.insert();


            SgGarageDetail rkDetail = new SgGarageDetail();
            //SgGarageDetail ckDetail = new SgGarageDetail();


            //?????????????????????
            rkDetail.setOrderType(dto.getTbCarType());//??????????????? 1??????  2:??????
            String maxGarageOrderRkNum = sgGarageDetailMapper.createTaskNum(formatter.format(new Date()), "1");
            rkDetail.setTaskNum(TaskNumUtil.createTaskNum("RK",maxGarageOrderRkNum));
//            rkDetail.setContractNo(dto.getContractNo()); //????????????
            rkDetail.setColectPerson(dto.getColectPerson());//????????????
            rkDetail.setColectPersonNum(dto.getColectPersonNum());//?????????????????????
            rkDetail.setAlixNum(dto.getApplyNo());//alix????????????
            rkDetail.setVin(dto.getVin());
            rkDetail.setSgGaragOrderId(sgGarageOrder.getId());
            rkDetail.setGarageSign(RCAllocateGarageSignEnum.GARAGEIN_SIGN.getValue());
            rkDetail.setPushSource(PushSourceEnum.LOAN_PUSH.getValue());
            rkDetail.setBillStatus(SgAllocateTaskStatusEnum.INOFSTORE.getValue());
            rkDetail.insert();
            sgGarageOrder.setSgGarageRKDatailId(rkDetail.getId());
            sgGarageOrder.insertOrUpdate();

            sgGarageOrderLog.setEvent("???????????????");
            sgGarageOrderLog.setSgGarageInfoId(dto.getGarageId());
            sgGarageOrderLog.setCreatorName("????????????");
            sgGarageOrderLog.setSgGarageOrderId(sgGarageOrder.getId());
            sgGarageOrderLog.insert();

            vehicleLog.setEvent("???????????????");
            vehicleLog.setGarageId(sgGarageInfo.getId());
            vehicleLog.setGarageName(sgGarageInfo.getGarageName());
            vehicleLog.insert();

        }catch (Exception e) {
            logger.error("dateCreate() failed");
            throw new BzException("dateCreate() failed", e);
        }
        return ResultUtil.success("?????????????????????");
    }



    public void createVehicleInfo (SgGarageInfo sgGarageInfo,SgVehicleInfo vehicleInfo,SgVehicleLog vehicleLog,SgGarageForLoanGarageInDTO dto) throws BzException {
        //???????????????????????????????????????????????????
        vehicleInfo.setStat(SgVehicleStatusEnum.FREE.getValue());
        vehicleInfo.setPropertyRightType(dto.getPropertyRightType());
        vehicleInfo.setVin(dto.getVin());
        vehicleInfo.setAlixNum(dto.getApplyNo());
        //????????????
        vehicleInfo.setLeaseProperty(dto.getLeaseProperty());
        vehicleInfo.setBusinessType(dto.getBusinessType());
        vehicleInfo.setBrand(dto.getBrand());
        vehicleInfo.setBrandStr(dto.getBrandStr());
        vehicleInfo.setBrandModel(dto.getBrandModel());
        vehicleInfo.setBrandModelStr(dto.getBrandModelStr());
        vehicleInfo.setVehicleClass(dto.getVehicleClass());
        vehicleInfo.setVehicleClassStr(dto.getVehicleClassStr());
        vehicleInfo.setModel(dto.getModel());
        vehicleInfo.setModelStr(dto.getModelStr());
        vehicleInfo.setColor(dto.getColor());
        vehicleInfo.setMileage(dto.getMileage());
        vehicleInfo.setLicNum(dto.getLicNum());
        if (dto.getIsKey() != null && !dto.getIsKey().equals("")) {
            if (dto.getIsKey().equals("0")) {
                vehicleInfo.setIsKey("0");
            } else {
                vehicleInfo.setIsKey(dto.getKeyNum());
            }
        }
        vehicleInfo.setIsDrivingLicense(dto.getIsDrivinglicense());
        vehicleInfo.setIsNormalStart(dto.getIsNormalStart());
        vehicleInfo.setIsDragGarage(dto.getIsDragGarage());
        vehicleInfo.setIsNewKey(dto.getHasNewKey());
        vehicleInfo.setIsLicnumCar(dto.getHasLicense());
        vehicleInfo.setIsBatteryWork(dto.getHasBattery());
        vehicleInfo.setIsSpare(dto.getHasSpareTire());//???????????????
        vehicleInfo.setIsTyreModel(dto.getTyreUniformity());//????????????????????????
        vehicleInfo.setLeftFrontNum(dto.getTyreCodingLeftFront());
        vehicleInfo.setLeftRearNum(dto.getTyreCodingLeftBack());
        vehicleInfo.setRightFrontNum(dto.getTyreCodingRightFront());
        vehicleInfo.setRightRearNum(dto.getTyreCodingRightBack());
        vehicleInfo.setOilQuantity(dto.getOilQuantity());//????????????
        vehicleInfo.setVehicleCross(dto.getIncarArticles());//????????????
        vehicleInfo.setDamageDesc(dto.getDamageDescript());
        vehicleInfo.setRemoveGps(dto.getGpsDelete());//gps????????????
        vehicleInfo.setLessee(dto.getLesseeName());//???????????????
        vehicleInfo.setDisposeHandover(dto.getDisposalHandoverPersonnel());//?????????????????????
        vehicleInfo.setDisposeCerNum(dto.getDisposalHandoverPersonnelNum());//?????????????????????
        vehicleInfo.setSgGarageInfoId(dto.getGarageId());
        vehicleInfo.setActualStorageTime(new Date());//??????????????????
        vehicleInfo.setVehicleSource(SgVehicleSourceEnum.DHSC.getValue());
        //????????????????????????????????????????????????????????????
        vehicleInfo.setIsUpdate(false);
        vehicleInfo.insert();
        vehicleLog.setEvent("????????????");
        vehicleLog.setCreatorName("????????????");
        vehicleLog.setVehicleId(vehicleInfo.getId());
        vehicleLog.setGarageId(sgGarageInfo.getId());
        vehicleLog.setGarageName(sgGarageInfo.getGarageName());
        vehicleLog.insert();
    }


    public void sendToRentcar (SgVehicleInfo sgVehicleInfo,SgGarageDetail sgGarageDetail) throws BzException {
        try {
            String event = "";
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
            if (sgVehicleInfo.getLeaseProperty().equals(LeasePropertyEnum.LEASEBACK_CAR.getValue())) {
                msgdto.setRentType(2);
            }
            if (sgVehicleInfo.getLeaseProperty().equals(LeasePropertyEnum.RESOURCE_CAR.getValue())) {
                msgdto.setRentType(1);
            }
            logger.info("???????????????????????????,??????:{}", JSONObject.toJSON(msgdto));
            String c = RestUtil.sendRequest(rentCarUrl + "/api/rcSGService/saveInoutRecord", msgdto);
            logger.info("?????????????????????:{}", JSONObject.toJSON(c));
            InvokeResult<String> resultMsg = JSON.parseObject(c, new TypeReference<InvokeResult<String>>() {
            });
            if (!resultMsg.isSuccess()) {
                logger.error("????????????????????????????????????" + resultMsg.getData());
            }
        } catch (Exception e) {
            logger.info("??????????????????????????????????????????", e);
        }
    }





}
