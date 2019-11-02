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
        logger.info("接收融后入库指令- SgGarageForLoanServiceImpl.inGarage() start!");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        InvokeResult<String> result = new InvokeResult<String>();
        try{
            //判断是否存在相同的任务
            QueryWrapper querySameWrapper = new QueryWrapper();
            querySameWrapper.eq("alix_num", dto.getApplyNo());
            querySameWrapper.eq("vin", dto.getVin());
            querySameWrapper.eq("garage_sign", RCAllocateGarageSignEnum.GARAGEIN_SIGN.getValue());
            querySameWrapper.eq("bill_status", SgAllocateTaskStatusEnum.IN_GARAGING.getValue());
            List<SgGarageDetail> sgGarageDetails =  sgGarageDetailMapper.selectList(querySameWrapper);
            if (sgGarageDetails.size() > 0) {
                logger.info("已存在相同的任务推送，请检查！");
                result.failure("已存在相同的任务推送，请检查！");
                return result;
            }

            //车库不存在，不能接受入库！
            QueryWrapper garageWrapper = new QueryWrapper();
            garageWrapper.eq("id", dto.getGarageId());
            garageWrapper.eq("IS_DELETED", 0);
            garageWrapper.eq("garage_status", GarageStatusEnum.NORMAL.getValue());
            List<SgGarageInfo> sgGarageInfos = sgGarageInfoMapper.selectList(garageWrapper);
            logger.info("==========车库信息为：{}",JSONObject.toJSON(sgGarageInfos));
            if (CollectionUtils.isEmpty(sgGarageInfos)) {
                logger.info("车库不存在或者车库已经停用，不能接受入库！");
                result.failure("车库不存在或者车库已经停用，不能接受入库！");
                return result;
            }
            SgGarageInfo sgGarageInfo = sgGarageInfos.get(0);


            QueryWrapper vehicleStateWrapper = new QueryWrapper();
//            vehicleWrapper.eq("garage_name", dto.getGarageIn());

            vehicleStateWrapper.eq("IS_DELETED", 0);
            vehicleStateWrapper.eq("vin", dto.getVin());
            List<SgVehicleInfo> sgVehicleStateInfos = sgVehicleInfoMapper.selectList(vehicleStateWrapper);
            logger.info("==========车辆信息为：{}",JSONObject.toJSON(sgVehicleStateInfos));
            if (null != sgVehicleStateInfos && !sgVehicleStateInfos.isEmpty()) {
                int state = sgVehicleStateInfos.get(0).getStat();
                //判断此车辆状态
                if (state != SgVehicleStatusEnum.DISPOSE_OUT.getValue()) {
                    logger.info("当前车辆状态为：" + SgVehicleStatusEnum.getDisplayNameByIndex(state) + "不允许出库！");
                    result.failure("当前车辆状态为：" + SgVehicleStatusEnum.getDisplayNameByIndex(state) + "不允许出库！");
                    return result;
                }
            }

            //订单如果为取消状态
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

                    //创建生成新的单子
                    SgGarageOrder sgGarageOrder = new SgGarageOrder();
                    SgGarageOrderLog sgGarageOrderLog = new SgGarageOrderLog();
                    String maxGarageOrderNum = sgGarageOrderMapper.createGarageOrderTaskNum(formatter.format(new Date()));
                    sgGarageOrder.setTaskNum(TaskNumUtil.createGarageOrderTaskNum(maxGarageOrderNum));
                    //创建订单信息
                    sgGarageOrder.setConsignmentFee(dto.getConsignmentFee().toString());
                    sgGarageOrder.setSgGarageInfoToId(dto.getGarageId());
                    sgGarageOrder.setSgVehicleId(ve.getId());
                    sgGarageOrder.setOrderStatus(SgAllocateTaskStatusEnum.INOFSTORE.getValue());
                    sgGarageOrder.setSgGarageInfoToId(sgGarageInfo.getId());
                    sgGarageOrder.insert();

                    //创建入库单
                    SgGarageDetail rkDetail = new SgGarageDetail();
                    //创建入库单信息
                    rkDetail.setOrderType(dto.getTbCarType());//入库类型： 1收车  2:退车
                    String maxGarageOrderRkNum = sgGarageDetailMapper.createTaskNum(formatter.format(new Date()), "1");
                    rkDetail.setTaskNum(TaskNumUtil.createTaskNum("RK",maxGarageOrderRkNum));
                    rkDetail.setContractNo(dto.getContractNo());
                    rkDetail.setColectPerson(dto.getColectPerson());//收车人员
                    rkDetail.setColectPersonNum(dto.getColectPersonNum());//收车人员证件号
                    rkDetail.setAlixNum(dto.getApplyNo());//alix申请编号
                    rkDetail.setVin(dto.getVin());
                    rkDetail.setSgGaragOrderId(sgGarageOrder.getId());
                    rkDetail.setGarageSign(RCAllocateGarageSignEnum.GARAGEIN_SIGN.getValue());
                    rkDetail.setPushSource(PushSourceEnum.LOAN_PUSH.getValue());
                    rkDetail.setBillStatus(SgAllocateTaskStatusEnum.INOFSTORE.getValue());
                    rkDetail.insert();
                    sgGarageOrder.setSgGarageRKDatailId(rkDetail.getId());
                    sgGarageOrder.insertOrUpdate();
                    sgGarageOrderLog.setEvent("融后推送入库");
                    sgGarageOrderLog.setSgGarageInfoId(dto.getGarageId());
                    sgGarageOrderLog.setCreatorName("系统推送");
                    sgGarageOrderLog.setSgGarageOrderId(sgGarageOrder.getId());
                    sgGarageOrderLog.insert();

                    sendToRentcar(ve, rkDetail);
                }else{
                    SgVehicleInfo ve = sgVehicleInfos.get(0);
                    int oldStat = ve.getStat();
                    ve.setStat(SgVehicleStatusEnum.FREE.getValue());
                    sgVehicleInfoMapper.updateById(ve);

                    //查询入库单
                    QueryWrapper detailWrapper = new QueryWrapper();
                    detailWrapper.eq("IS_DELETED", 0);
                    detailWrapper.eq("vin", dto.getVin());
                    detailWrapper.eq("alix_num", dto.getApplyNo());
                    detailWrapper.eq("garage_sign", RCAllocateGarageSignEnum.GARAGEIN_SIGN.getValue());
                    detailWrapper.eq("push_source", PushSourceEnum.LOAN_PUSH.getValue());
                    List<SgGarageDetail> details = sgGarageDetailMapper.selectList(detailWrapper);
                    if(CollectionUtils.isEmpty(details)){
                        logger.info("没有历史数据，不能取消");
                        result.failure("没有历史数据，不能取消");
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

                    //创建生成新的单子
                    SgGarageOrder sgGarageOrder = new SgGarageOrder();
                    SgGarageOrderLog sgGarageOrderLog = new SgGarageOrderLog();
                    String maxGarageOrderNum = sgGarageOrderMapper.createGarageOrderTaskNum(formatter.format(new Date()));
                    sgGarageOrder.setTaskNum(TaskNumUtil.createGarageOrderTaskNum(maxGarageOrderNum));
                    //创建订单信息
                    sgGarageOrder.setConsignmentFee(dto.getConsignmentFee().toString());
                    sgGarageOrder.setSgGarageInfoToId(dto.getGarageId());
                    sgGarageOrder.setSgVehicleId(ve.getId());
                    sgGarageOrder.setOrderStatus(SgAllocateTaskStatusEnum.INOFSTORE.getValue());
                    sgGarageOrder.setSgGarageInfoToId(sgGarageInfo.getId());
                    sgGarageOrder.insert();

                    //创建入库单
                    SgGarageDetail rkDetail = new SgGarageDetail();
                    //创建入库单信息
                    rkDetail.setOrderType(dto.getTbCarType());//入库类型： 1收车  2:退车
                    String maxGarageOrderRkNum = sgGarageDetailMapper.createTaskNum(formatter.format(new Date()), "1");
                    rkDetail.setTaskNum(TaskNumUtil.createTaskNum("RK",maxGarageOrderRkNum));
                    rkDetail.setContractNo(dto.getContractNo());
                    rkDetail.setColectPerson(dto.getColectPerson());//收车人员
                    rkDetail.setColectPersonNum(dto.getColectPersonNum());//收车人员证件号
                    rkDetail.setAlixNum(dto.getApplyNo());//alix申请编号
                    rkDetail.setVin(dto.getVin());
                    rkDetail.setSgGaragOrderId(sgGarageOrder.getId());
                    rkDetail.setGarageSign(RCAllocateGarageSignEnum.GARAGEIN_SIGN.getValue());
                    rkDetail.setPushSource(PushSourceEnum.LOAN_PUSH.getValue());
                    rkDetail.setBillStatus(SgAllocateTaskStatusEnum.INOFSTORE.getValue());
                    rkDetail.insert();
                    sgGarageOrder.setSgGarageRKDatailId(rkDetail.getId());
                    sgGarageOrder.insertOrUpdate();
                    sgGarageOrderLog.setEvent("融后推送入库");
                    sgGarageOrderLog.setSgGarageInfoId(dto.getGarageId());
                    sgGarageOrderLog.setCreatorName("系统推送");
                    sgGarageOrderLog.setSgGarageOrderId(sgGarageOrder.getId());
                    sgGarageOrderLog.insert();
                    sendToRentcar(ve, rkDetail);

                    //增加车辆日志
                    SgVehicleLog sgVehicleLog = new SgVehicleLog();
                    sgVehicleLog.setEvent("推送入库");
                    sgVehicleLog.setCreateTime(new Date());// 操作时间
                    sgVehicleLog.setCreatorName("贷后系统");// 操作人
                    sgVehicleLog.setCreatorDepartmentName("贷后系统");
                    sgVehicleLog.setVehicleId(ve.getId());
                    sgVehicleLog.setRemark("车辆状态从[" + SgVehicleStatusEnum.getDisplayNameByIndex(oldStat) + "]更新为[" + SgVehicleStatusEnum.getDisplayNameByIndex(ve.getStat()) + "]");
                    sgVehicleLog.insert();

                }
            }else{
                //订单生效状态
                QueryWrapper vehicleWrapper = new QueryWrapper();
                vehicleWrapper.eq("IS_DELETED", 0);
                vehicleWrapper.eq("vin", dto.getVin());
                vehicleWrapper.eq("alix_num", dto.getApplyNo());
                List<SgVehicleInfo> sgVehicleInfos = sgVehicleInfoMapper.selectList(vehicleWrapper);
                if (CollectionUtils.isEmpty(sgVehicleInfos)){
                    //创建车辆
                    SgVehicleInfo ve = new SgVehicleInfo();
                    SgVehicleLog veLog = new SgVehicleLog();
                    createVehicleInfo(sgGarageInfo,ve,veLog,dto);

                    //创建生成新的单子
                    SgGarageOrder sgGarageOrder = new SgGarageOrder();
                    SgGarageOrderLog sgGarageOrderLog = new SgGarageOrderLog();
                    String maxGarageOrderNum = sgGarageOrderMapper.createGarageOrderTaskNum(formatter.format(new Date()));
                    sgGarageOrder.setTaskNum(TaskNumUtil.createGarageOrderTaskNum(maxGarageOrderNum));

                    //创建订单信息
                    sgGarageOrder.setConsignmentFee(dto.getConsignmentFee().toString());
                    sgGarageOrder.setSgGarageInfoToId(dto.getGarageId());
                    sgGarageOrder.setSgVehicleId(ve.getId());
                    sgGarageOrder.setOrderStatus(SgAllocateTaskStatusEnum.INOFSTORE.getValue());
                    sgGarageOrder.setSgGarageInfoToId(sgGarageInfo.getId());
                    sgGarageOrder.insert();

                    //创建入库单
                    SgGarageDetail rkDetail = new SgGarageDetail();
                    //创建入库单信息
                    rkDetail.setOrderType(dto.getTbCarType());//入库类型： 1收车  2:退车
                    String maxGarageOrderRkNum = sgGarageDetailMapper.createTaskNum(formatter.format(new Date()), "1");
                    rkDetail.setTaskNum(TaskNumUtil.createTaskNum("RK",maxGarageOrderRkNum));
                    rkDetail.setContractNo(dto.getContractNo());
                    rkDetail.setColectPerson(dto.getColectPerson());//收车人员
                    rkDetail.setColectPersonNum(dto.getColectPersonNum());//收车人员证件号
                    rkDetail.setAlixNum(dto.getApplyNo());//alix申请编号
                    rkDetail.setVin(dto.getVin());
                    rkDetail.setSgGaragOrderId(sgGarageOrder.getId());
                    rkDetail.setGarageSign(RCAllocateGarageSignEnum.GARAGEIN_SIGN.getValue());
                    rkDetail.setPushSource(PushSourceEnum.LOAN_PUSH.getValue());
                    rkDetail.setBillStatus(SgAllocateTaskStatusEnum.INOFSTORE.getValue());
                    rkDetail.insert();
                    sgGarageOrder.setSgGarageRKDatailId(rkDetail.getId());
                    sgGarageOrder.insertOrUpdate();
                    sgGarageOrderLog.setEvent("融后推送入库");
                    sgGarageOrderLog.setSgGarageInfoId(dto.getGarageId());
                    sgGarageOrderLog.setCreatorName("系统推送");
                    sgGarageOrderLog.setSgGarageOrderId(sgGarageOrder.getId());
                    sgGarageOrderLog.insert();
                    sendToRentcar(ve, rkDetail);
                }else{
                    SgVehicleInfo ve = sgVehicleInfos.get(0);
                    int oldStat = ve.getStat();
                    ve.setStat(SgVehicleStatusEnum.FREE.getValue());
                    ve.insertOrUpdate();

                    //创建生成新的单子
                    SgGarageOrder sgGarageOrder = new SgGarageOrder();
                    SgGarageOrderLog sgGarageOrderLog = new SgGarageOrderLog();
                    String maxGarageOrderNum = sgGarageOrderMapper.createGarageOrderTaskNum(formatter.format(new Date()));
                    sgGarageOrder.setTaskNum(TaskNumUtil.createGarageOrderTaskNum(maxGarageOrderNum));

                    //创建订单信息
                    sgGarageOrder.setConsignmentFee(dto.getConsignmentFee().toString());
                    sgGarageOrder.setSgGarageInfoToId(dto.getGarageId());
                    sgGarageOrder.setSgVehicleId(ve.getId());
                    sgGarageOrder.setOrderStatus(SgAllocateTaskStatusEnum.INOFSTORE.getValue());
                    sgGarageOrder.setSgGarageInfoToId(sgGarageInfo.getId());
                    sgGarageOrder.insert();

                    //创建入库单
                    SgGarageDetail rkDetail = new SgGarageDetail();
                    //SgGarageDetail ckDetail = new SgGarageDetail();
                    //创建入库单信息
                    rkDetail.setOrderType(dto.getTbCarType());//入库类型： 1收车  2:退车
                    String maxGarageOrderRkNum = sgGarageDetailMapper.createTaskNum(formatter.format(new Date()), "1");
                    rkDetail.setTaskNum(TaskNumUtil.createTaskNum("RK",maxGarageOrderRkNum));
                    rkDetail.setContractNo(dto.getContractNo());
                    rkDetail.setColectPerson(dto.getColectPerson());//收车人员
                    rkDetail.setColectPersonNum(dto.getColectPersonNum());//收车人员证件号
                    rkDetail.setAlixNum(dto.getApplyNo());//alix申请编号
                    rkDetail.setVin(dto.getVin());
                    rkDetail.setSgGaragOrderId(sgGarageOrder.getId());
                    rkDetail.setGarageSign(RCAllocateGarageSignEnum.GARAGEIN_SIGN.getValue());
                    rkDetail.setPushSource(PushSourceEnum.LOAN_PUSH.getValue());
                    rkDetail.setBillStatus(SgAllocateTaskStatusEnum.INOFSTORE.getValue());
                    rkDetail.insert();
                    sgGarageOrder.setSgGarageRKDatailId(rkDetail.getId());
                    sgGarageOrder.insertOrUpdate();
                    sgGarageOrderLog.setEvent("融后推送入库");
                    sgGarageOrderLog.setSgGarageInfoId(dto.getGarageId());
                    sgGarageOrderLog.setCreatorName("系统推送");
                    sgGarageOrderLog.setSgGarageOrderId(sgGarageOrder.getId());
                    sgGarageOrderLog.insert();

                    SgVehicleLog vehicleLog = new SgVehicleLog();
                    vehicleLog.setEvent("融后推送车辆");
                    vehicleLog.setRemark("车辆状态从[" + SgVehicleStatusEnum.getDisplayNameByIndex(oldStat) + "]更新为[" + SgVehicleStatusEnum.getDisplayNameByIndex(ve.getStat()) + "]");
                    vehicleLog.setGarageId(sgGarageInfo.getId());
                    vehicleLog.setGarageName(sgGarageInfo.getGarageName());
                    vehicleLog.setVehicleId(ve.getId());
                    vehicleLog.insert();
                    sendToRentcar(ve, rkDetail);
                }
            }

            sgGarageInfo.setParkedNum(sgGarageInfo.getParkedNum() + 1);
            sgGarageInfo.insertOrUpdate();

            //推送中台信息保存
            logger.info("SgGarageForLoanServiceImpl inGarage()融后推送入库:保存推送信息，供定时推送中台使用 =========");
            SgSendInfoToZhongtai rcSendInfoToZhongtai = new SgSendInfoToZhongtai();
            rcSendInfoToZhongtai.setBillNum(dto.getApplyNo());//单号
            rcSendInfoToZhongtai.setInterfaceName(SendTypeEnum.PSM_RESTOCK_INTERFACE.getValue());//接口名称(码值)
            rcSendInfoToZhongtai.setSendCount(0);//发送次数
            rcSendInfoToZhongtai.setType(BillStatusEnum.PSM_RESTOCK_INTERFACE.getValue());//单子类型
            rcSendInfoToZhongtai.setStat(0);//发送状态
            rcSendInfoToZhongtai.setCreatorName("融后推送入库");
            rcSendInfoToZhongtai.insert();

        }catch (Exception e) {
            logger.error("inGarage() failed");
            throw new BzException("inGarage() failed", e);
        }
        return ResultUtil.success("接收入库完成");
    }


    @Transactional
    @Override
    public InvokeResult<String> outGarage(SgGarageForLoanGarageOutDTO dto) throws BzException {
        logger.info("接收融后出库指令- SgGarageForLoanServiceImpl.inGarage() start!");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        InvokeResult<String> result = new InvokeResult<String>();
        try{
            //根据入参查询出库单
            QueryWrapper queryCkWrapper = new QueryWrapper();
            queryCkWrapper.eq("IS_DELETED", 0);
            queryCkWrapper.ne("bill_status", SgAllocateTaskStatusEnum.CANCEL.getValue());//订单不为已取消状态的数据
            queryCkWrapper.eq("push_source", PushSourceEnum.LOAN_PUSH.getValue());
            queryCkWrapper.eq("vin", dto.getVin());
            queryCkWrapper.eq("garage_sign", RCAllocateGarageSignEnum.GARAGEOUT_SIGN.getValue());
            queryCkWrapper.eq("alix_num", dto.getApplyNo());
            List<SgGarageDetail> sgGarageCKDetails = sgGarageDetailMapper.selectList(queryCkWrapper);
            if (CollUtil.isNotEmpty(sgGarageCKDetails)) {
                logger.info("已存在出库单调用失败，请检查！");
                result.failure("已存在出库单调用失败，请检查！");
                return result;
            }

            //根据入参查询入库单
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("IS_DELETED", 0);
            queryCkWrapper.ne("bill_status", SgAllocateTaskStatusEnum.CANCEL.getValue());//订单不为已取消状态的数据
            queryWrapper.eq("vin", dto.getVin());
            queryWrapper.eq("push_source", PushSourceEnum.LOAN_PUSH.getValue());
            queryWrapper.eq("garage_sign", RCAllocateGarageSignEnum.GARAGEIN_SIGN.getValue());
            queryWrapper.eq("alix_num", dto.getApplyNo());
            List<SgGarageDetail> sgGarageDetails = sgGarageDetailMapper.selectList(queryWrapper);

            SgGarageDetail ckDetail = new SgGarageDetail();
            if(CollectionUtils.isNotEmpty(sgGarageDetails)){
                SgGarageOrder sgGarageOrder = sgGarageOrderMapper.selectById(sgGarageDetails.get(0).getSgGaragOrderId());
                SgGarageDetail rkDetail = sgGarageDetails.get(0);
                //生成出库单
                String maxGarageOrderCkNum = sgGarageDetailMapper.createTaskNum(formatter.format(new Date()), "0");
                ckDetail.setTaskNum(TaskNumUtil.createTaskNum("CK",maxGarageOrderCkNum));
                ckDetail.setContractNo(ckDetail.getContractNo());//合同编号
                ckDetail.setOrderType(rkDetail.getOrderType());//入库类型： 1收车  2:退车
                ckDetail.setColectPerson(rkDetail.getColectPerson());//收车人员
                ckDetail.setColectPersonNum(rkDetail.getColectPersonNum());//
                ckDetail.setAlixNum(rkDetail.getAlixNum());//alix申请编号
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

                //更新车辆状态
                QueryWrapper vehicleWrapper = new QueryWrapper();
                vehicleWrapper.eq("IS_DELETED", 0);
                vehicleWrapper.eq("alix_num", dto.getApplyNo());
                vehicleWrapper.eq("vin", dto.getVin());
                List<SgVehicleInfo> sgVehicleInfos = sgVehicleInfoMapper.selectList(vehicleWrapper);
                if(CollectionUtils.isNotEmpty(sgVehicleInfos)){
                    SgVehicleInfo ve = sgVehicleInfos.get(0);
                    int oldStat = ve.getStat();
                    if (ve.getStat() != SgVehicleStatusEnum.FREE.getValue()) {
                        logger.info("车辆状态为：" + SgVehicleStatusEnum.getDisplayNameByIndex(ve.getStat()) + "，不允许出库操作");
                        result.failure("车辆状态为：" + SgVehicleStatusEnum.getDisplayNameByIndex(ve.getStat()) + "，不允许出库操作");
                        return result;
                    }
                    ve.setStat(SgVehicleStatusEnum.DISPOSE_OUT.getValue());
                    ve.insertOrUpdate();
                    //修改车位数
                    SgGarageInfo garageInfo = sgGarageInfoMapper.selectById(ve.getSgGarageInfoId());
                    garageInfo.setParkedNum(garageInfo.getParkedNum() - 1);
                    garageInfo.insertOrUpdate();
                    sgGarageOrder.setSgGarageInfoFromId(ve.getSgGarageInfoId());
                    //增加车辆日志
                    SgVehicleLog sgVehicleLog = new SgVehicleLog();
                    sgVehicleLog.setEvent("推送出库");
                    sgVehicleLog.setCreateTime(new Date());// 操作时间
                    sgVehicleLog.setCreatorName("贷后系统");// 操作人
                    sgVehicleLog.setCreatorDepartmentName("贷后系统");
                    sgVehicleLog.setVehicleId(ve.getId());
                    sgVehicleLog.setRemark("车辆状态从[" + SgVehicleStatusEnum.getDisplayNameByIndex(oldStat) + "]更新为[" + SgVehicleStatusEnum.getDisplayNameByIndex(ve.getStat()) + "]");
                    sgVehicleLog.insert();

                }
                sgGarageOrder.setOrderStatus(SgAllocateTaskStatusEnum.OUTOFSTORE.getValue());
                ckDetail.insert();
                sgGarageOrder.setSgGarageCKDatailId(ckDetail.getId());
                sgGarageOrder.insertOrUpdate();

                SgGarageOrderLog sgGarageOrderLog = new SgGarageOrderLog();
                sgGarageOrderLog.setEvent("融后推送出库");
                sgGarageOrderLog.setSgGarageOrderId(ckDetail.getSgGaragOrderId());
                sgGarageOrderLog.setCreatorName("系统推送");
                sgGarageOrderLog.insert();

            }else{
                logger.error("没有对应入库单，请检查！");
                result.failure("没有对应入库单，请检查！");
                return result;
            }

        }catch (Exception e) {
            logger.error("outGarage() failed");
            throw new BzException("outGarage() failed", e);
        }
        logger.error("接收入库完成");
        return ResultUtil.success("接收入库完成");
    }



    @Override
    public InvokeResult<SgQueryVehicleIfoStatDTO> queryVehicleStat(SgQueryVehicleIfoStatDTO dto) throws BzException {
        logger.info("接收融后出库指令- SgGarageForLoanServiceImpl.inGarage() start!");
        InvokeResult<SgQueryVehicleIfoStatDTO> result = new InvokeResult<SgQueryVehicleIfoStatDTO>();
        try{
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("IS_DELETED", 0);
            queryWrapper.eq("alix_num", dto.getApplyNo());
            queryWrapper.eq("vin", dto.getVin());
            List<SgVehicleInfo> sgVehicleInfos = sgVehicleInfoMapper.selectList(queryWrapper);
            if(sgVehicleInfos.size()<=0){
                result.failure("车辆不存在，请检查！");
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
        logger.info("初始化数据接口- SgGarageForLoanServiceImpl.dateCreate() start!");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        InvokeResult<String> result = new InvokeResult<String>();
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        try{
            //判断车库是否能接受入库
            QueryWrapper garageWrapper = new QueryWrapper();
            garageWrapper.eq("id", dto.getGarageId());
            garageWrapper.eq("IS_DELETED", 0);
            garageWrapper.eq("garage_status", GarageStatusEnum.NORMAL.getValue());
            List<SgGarageInfo> sgGarageInfos = sgGarageInfoMapper.selectList(garageWrapper);
            logger.info("==========车库信息为：{}",JSONObject.toJSON(sgGarageInfos));

            if (sgGarageInfos == null || sgGarageInfos.size() == 0) {
                logger.info("车库不存在或已经停用，不能接受入库！");
                result.failure("车库不存在或已经停用，不能接受入库！");
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
                //如果数据库中没有所传车辆，新增车辆
                vehicleInfo.setStat(SgVehicleStatusEnum.FREE.getValue());
                //融后提供资源车类型数据
//                vehicleInfo.setPropertyRightType(dto.getPropertyRightType());//资源车类型
                vehicleInfo.setVin(dto.getVin());
                vehicleInfo.setAlixNum(dto.getApplyNo());

                if(dto.getLeaseProperty().equals("回租")){
                    vehicleInfo.setLeaseProperty(LeasePropertyEnum.LEASEBACK_CAR.getValue());
                }
                if(dto.getLeaseProperty().equals("正租")){
                    vehicleInfo.setLeaseProperty(LeasePropertyEnum.RESOURCE_CAR.getValue());
                }

                if(dto.getBusinessType().equals("消费融")){
                    vehicleInfo.setBusinessType(BusinessTypeEnum.CONSUMER_LOAN.getValue());
                }
                if(dto.getBusinessType().equals("经营性租赁")){
                    vehicleInfo.setBusinessType(BusinessTypeEnum.OPERATE.getValue());
                }
                if(dto.getBusinessType().equals("车主融")){
                    vehicleInfo.setBusinessType(BusinessTypeEnum.MORTGAGE_CAR.getValue());
                }
                if(dto.getBusinessType().equals("联合融")){
                    vehicleInfo.setBusinessType(BusinessTypeEnum.JOINT_LOAN.getValue());
                }

                vehicleInfo.setModel(dto.getModel());
                vehicleInfo.setColor(dto.getColor());

                //是整数返回true,否则返回false
                if(pattern.matcher(dto.getMileage()).matches()){
                    vehicleInfo.setMileage(new BigDecimal(dto.getMileage()));
                }else{
                    vehicleInfo.setMileage(new BigDecimal(0));
                }

                vehicleInfo.setLicNum(dto.getLicNum());

                if (dto.getIsKey() != null && !dto.getIsKey().equals("")) {
                    if (dto.getIsKey().equals("否")) {
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
                vehicleInfo.setIsSpare(dto.getHasSpareTire());//是否有备胎
                vehicleInfo.setIsTyreModel(dto.getTyreUniformity());//轮胎型号是否一致
                vehicleInfo.setLeftFrontNum(dto.getTyreCodingLeftFront());
                vehicleInfo.setLeftRearNum(dto.getTyreCodingLeftBack());
                vehicleInfo.setRightFrontNum(dto.getTyreCodingRightFront());
                vehicleInfo.setRightRearNum(dto.getTyreCodingRightBack());
                vehicleInfo.setOilQuantity(dto.getOilQuantity());//油箱油量
                vehicleInfo.setVehicleCross(dto.getIncarArticles());//车内物品
                vehicleInfo.setDamageDesc(dto.getDamageDescript());
                vehicleInfo.setRemoveGps(dto.getGpsDelete());//gps是否移除
                vehicleInfo.setLessee(dto.getLesseeName());
                vehicleInfo.setDisposeHandover(dto.getDisposalHandoverPersonnel());//处置商交接人员
                vehicleInfo.setDisposeCerNum(dto.getDisposalHandoverPersonnelNum());//处置商身份证号
                vehicleInfo.setSgGarageInfoId(dto.getGarageId());
//                vehicleInfo.setActualStorageTime(DateUitls.strToDate(dto.getActualStorageTime()));
                vehicleInfo.setVehicleSource(SgVehicleSourceEnum.DHSC.getValue());
                //车辆四级是否更新，初始化数据统一为未更新
                vehicleInfo.setIsUpdate(false);
                vehicleInfo.insert();
            }

            //生成出入库单   ------初始化车辆数据补全
            SgGarageOrder sgGarageOrder = new SgGarageOrder();
            SgGarageOrderLog sgGarageOrderLog = new SgGarageOrderLog();
            String maxGarageOrderNum = sgGarageOrderMapper.createGarageOrderTaskNum(formatter.format(new Date()));
            sgGarageOrder.setTaskNum(TaskNumUtil.createGarageOrderTaskNum(maxGarageOrderNum));
            //创建订单信息
            sgGarageOrder.setConsignmentFee(null);
            sgGarageOrder.setSgGarageInfoToId(dto.getGarageId());
            sgGarageOrder.setSgVehicleId(vehicleInfo.getId());
            sgGarageOrder.setOrderStatus(SgAllocateTaskStatusEnum.INOFSTORE.getValue());
            sgGarageOrder.setSgGarageInfoToId(sgGarageInfo.getId());
            sgGarageOrder.insert();


            SgGarageDetail rkDetail = new SgGarageDetail();
            //SgGarageDetail ckDetail = new SgGarageDetail();


            //创建入库单信息
            rkDetail.setOrderType(dto.getTbCarType());//入库类型： 1收车  2:退车
            String maxGarageOrderRkNum = sgGarageDetailMapper.createTaskNum(formatter.format(new Date()), "1");
            rkDetail.setTaskNum(TaskNumUtil.createTaskNum("RK",maxGarageOrderRkNum));
//            rkDetail.setContractNo(dto.getContractNo()); //合同编号
            rkDetail.setColectPerson(dto.getColectPerson());//收车人员
            rkDetail.setColectPersonNum(dto.getColectPersonNum());//收车人员证件号
            rkDetail.setAlixNum(dto.getApplyNo());//alix申请编号
            rkDetail.setVin(dto.getVin());
            rkDetail.setSgGaragOrderId(sgGarageOrder.getId());
            rkDetail.setGarageSign(RCAllocateGarageSignEnum.GARAGEIN_SIGN.getValue());
            rkDetail.setPushSource(PushSourceEnum.LOAN_PUSH.getValue());
            rkDetail.setBillStatus(SgAllocateTaskStatusEnum.INOFSTORE.getValue());
            rkDetail.insert();
            sgGarageOrder.setSgGarageRKDatailId(rkDetail.getId());
            sgGarageOrder.insertOrUpdate();

            sgGarageOrderLog.setEvent("初始化数据");
            sgGarageOrderLog.setSgGarageInfoId(dto.getGarageId());
            sgGarageOrderLog.setCreatorName("系统推送");
            sgGarageOrderLog.setSgGarageOrderId(sgGarageOrder.getId());
            sgGarageOrderLog.insert();

            vehicleLog.setEvent("初始化数据");
            vehicleLog.setGarageId(sgGarageInfo.getId());
            vehicleLog.setGarageName(sgGarageInfo.getGarageName());
            vehicleLog.insert();

        }catch (Exception e) {
            logger.error("dateCreate() failed");
            throw new BzException("dateCreate() failed", e);
        }
        return ResultUtil.success("初始化数据完成");
    }



    public void createVehicleInfo (SgGarageInfo sgGarageInfo,SgVehicleInfo vehicleInfo,SgVehicleLog vehicleLog,SgGarageForLoanGarageInDTO dto) throws BzException {
        //如果数据库中没有所传车辆，新增车辆
        vehicleInfo.setStat(SgVehicleStatusEnum.FREE.getValue());
        vehicleInfo.setPropertyRightType(dto.getPropertyRightType());
        vehicleInfo.setVin(dto.getVin());
        vehicleInfo.setAlixNum(dto.getApplyNo());
        //租赁属性
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
        vehicleInfo.setIsSpare(dto.getHasSpareTire());//是否有备胎
        vehicleInfo.setIsTyreModel(dto.getTyreUniformity());//轮胎型号是否一致
        vehicleInfo.setLeftFrontNum(dto.getTyreCodingLeftFront());
        vehicleInfo.setLeftRearNum(dto.getTyreCodingLeftBack());
        vehicleInfo.setRightFrontNum(dto.getTyreCodingRightFront());
        vehicleInfo.setRightRearNum(dto.getTyreCodingRightBack());
        vehicleInfo.setOilQuantity(dto.getOilQuantity());//油箱油量
        vehicleInfo.setVehicleCross(dto.getIncarArticles());//车内物品
        vehicleInfo.setDamageDesc(dto.getDamageDescript());
        vehicleInfo.setRemoveGps(dto.getGpsDelete());//gps是否移除
        vehicleInfo.setLessee(dto.getLesseeName());//承租人姓名
        vehicleInfo.setDisposeHandover(dto.getDisposalHandoverPersonnel());//处置商交接人员
        vehicleInfo.setDisposeCerNum(dto.getDisposalHandoverPersonnelNum());//处置商身份证号
        vehicleInfo.setSgGarageInfoId(dto.getGarageId());
        vehicleInfo.setActualStorageTime(new Date());//实际入库时间
        vehicleInfo.setVehicleSource(SgVehicleSourceEnum.DHSC.getValue());
        //车辆四级是否更新，初始化数据统一为未更新
        vehicleInfo.setIsUpdate(false);
        vehicleInfo.insert();
        vehicleLog.setEvent("系统推送");
        vehicleLog.setCreatorName("贷后系统");
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
            logger.info("入库订单同步进销存,入参:{}", JSONObject.toJSON(msgdto));
            String c = RestUtil.sendRequest(rentCarUrl + "/api/rcSGService/saveInoutRecord", msgdto);
            logger.info("接口返回数据为:{}", JSONObject.toJSON(c));
            InvokeResult<String> resultMsg = JSON.parseObject(c, new TypeReference<InvokeResult<String>>() {
            });
            if (!resultMsg.isSuccess()) {
                logger.error("入库订单同步进销存失败，" + resultMsg.getData());
            }
        } catch (Exception e) {
            logger.info("调用进销存接口入库失败！！！", e);
        }
    }





}
