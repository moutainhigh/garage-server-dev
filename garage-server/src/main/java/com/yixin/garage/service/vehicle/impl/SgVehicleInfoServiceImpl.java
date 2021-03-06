package com.yixin.garage.service.vehicle.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yixin.common.exception.BzException;
import com.yixin.common.utils.HttpRequestUtils;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.auth.CurrentUser;
import com.yixin.garage.core.base.BaseAssembler;
import com.yixin.garage.core.common.factory.PageFactory;
import com.yixin.garage.dao.AttachSourceMapper;
import com.yixin.garage.dao.garage.SgContactToGarageTempMapper;
import com.yixin.garage.dao.garage.SgGarageInfoMapper;
import com.yixin.garage.dao.order.SgGarageDetailMapper;
import com.yixin.garage.dao.order.SgGarageOrderMapper;
import com.yixin.garage.dao.vehicle.SgVehicleInfoMapper;
import com.yixin.garage.dao.vehicle.SgVehicleLogMapper;
import com.yixin.garage.dto.api.forLoan.SgQueryVehicleIfoStatDTO;
import com.yixin.garage.dto.api.rentcar.SgVehicleByFreeDTO;
import com.yixin.garage.dto.sys.AttachSourceDTO;
import com.yixin.garage.dto.sys.CatTypeDTO;
import com.yixin.garage.dto.sys.SysUserDTO;
import com.yixin.garage.dto.sys.UserByPhoneDTO;
import com.yixin.garage.dto.vehicle.SgVehicleInfoDTO;
import com.yixin.garage.entity.AttachSource;
import com.yixin.garage.entity.garage.SgContactToGarageTemp;
import com.yixin.garage.entity.garage.SgGarageInfo;
import com.yixin.garage.entity.order.SgGarageDetail;
import com.yixin.garage.entity.order.SgGarageOrder;
import com.yixin.garage.entity.order.SgGarageOrderLog;
import com.yixin.garage.entity.vehicle.SgVehicleInfo;
import com.yixin.garage.entity.vehicle.SgVehicleLog;
import com.yixin.garage.entity.zhongtai.SgSendInfoToZhongtai;
import com.yixin.garage.enums.RcAttachmentTypeEnum;
import com.yixin.garage.enums.RoleEnum;
import com.yixin.garage.enums.garage.*;
import com.yixin.garage.enums.garage.zhongtai.BillStatusEnum;
import com.yixin.garage.enums.garage.zhongtai.SendTypeEnum;
import com.yixin.garage.service.vehicle.ISgVehicleInfoService;
import com.yixin.garage.util.DateUtil;
import com.yixin.garage.util.RestUtil;
import com.yixin.garage.util.ResultUtil;
import com.yixin.garage.util.TaskNumUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 *  ???????????????
 * </p>
 *
 * @author liyaqing
 * @since 2019-08-16
 */
@Transactional
@Service
public class SgVehicleInfoServiceImpl extends ServiceImpl<SgVehicleInfoMapper, SgVehicleInfo> implements ISgVehicleInfoService {

    private final static Logger logger = LoggerFactory.getLogger(SgVehicleInfoServiceImpl.class);

    @Autowired
    private SgVehicleInfoMapper sgVehicleInfoMapper;

    @Autowired
    private SgGarageOrderMapper sgGarageOrderMapper;

    @Autowired
    private SgGarageInfoMapper sgGarageInfoMapper;

    @Autowired
    private AttachSourceMapper attachSourceMapper;

    @Autowired
    private SgVehicleLogMapper sgVehicleLogMapper;
    @Autowired
    private SgGarageDetailMapper sgGarageDetailMapper;

    @Autowired
    private SgContactToGarageTempMapper SgContactToGarageTempMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${carStyleUrl}")
    private String carStyleUrl;



    private static final String CREATE = "????????????";
    private static final String UPDATE = "????????????";
    private static final String TEMP_IN = "????????????";

    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

    /**
     * @Title: create
     * @Description: ????????????
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/16 18:07
     */
    @Override
    public InvokeResult<String> create(SgVehicleInfoDTO sgVehicleInfoDTO) throws BzException {
        try {
            logger.info("??????????????????create() sgVehicleInfoDTO=[{}]", JSONObject.toJSONString(sgVehicleInfoDTO));

            //????????????????????????????????????
            QueryWrapper<SgVehicleInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("stat",SgVehicleStatusEnum.FREE.getValue())
                    .eq("vin",sgVehicleInfoDTO.getVin())
                    .eq("IS_DELETED","0");
            List<SgVehicleInfo> list = list(queryWrapper);
            if (list.size() > 0) {
                logger.error("????????????????????????????????????????????????????????????????????????");
                throw new BzException("????????????????????????????????????????????????????????????????????????");
            }
            SgVehicleInfo sgVehicleInfo = new SgVehicleInfo();
            BaseAssembler.mapObjWithoutNull(sgVehicleInfoDTO, sgVehicleInfo);
            sgVehicleInfo.setActualStorageTime(new Date());
            sgVehicleInfo.setStat(SgVehicleStatusEnum.FREE.getValue());

            SgGarageInfo garageInfo = sgGarageInfoMapper.selectById(sgVehicleInfoDTO.getSgGarageInfoId());
            if (garageInfo.getParkingNum() - garageInfo.getParkedNum() > 0) {
                garageInfo.setParkedNum(garageInfo.getParkedNum() + 1);
                sgGarageInfoMapper.updateById(garageInfo);
                logger.info("===========???????????? create():?????????????????????=========");
            }else{
                logger.error("??????["+garageInfo.getGarageName()+"]??????????????????????????????");
                throw new BzException("??????["+garageInfo.getGarageName()+"]??????????????????????????????");
            }
            sgVehicleInfo.setIsUpdate(true);
            sgVehicleInfoMapper.insert(sgVehicleInfo);
            logger.info("============???????????? create():??????????????????=========");

            //??????-?????????
            this.saveAtt(sgVehicleInfoDTO,sgVehicleInfo);
            logger.info("==========???????????? create(): ??????????????????]=========");


            //????????????
            List<String> delIds = sgVehicleInfoDTO.getDelAttIds();
            if(delIds != null && !delIds.isEmpty()){
                for(String attId : delIds){
                    AttachSource att = attachSourceMapper.selectById(attId);
                    if(att != null){
                        att.deleteById();
                    }
                }

            }
            logger.info("==========???????????? create():??????????????????=========");
//            //?????????????????????????????????
//            if(sgVehicleInfo.getStat() == SgVehicleStatusEnum.FREE.getValue()){
//                //?????????????????????
//                SgGarageDetail sgGarageDetail = new SgGarageDetail();
//                sgGarageDetail.setOrderType(SgOrderInTypeEnum.TEMPORARY_IN.getValue());//??????????????? 1??????  2:??????
//                sgGarageDetail.setAlixNum(sgVehicleInfo.getAlixNum());//alix????????????
//                sgGarageDetail.setVin(sgVehicleInfo.getVin());
//                sgGarageDetail.setGarageSign(RCAllocateGarageSignEnum.GARAGEIN_SIGN.getValue());
//                sgGarageDetail.setPushSource(PushSourceEnum.TEMP_GARAGE.getValue());
//                sgGarageDetail.setBillStatus(SgAllocateTaskStatusEnum.INOFSTORE.getValue());
//                String maxGarageDetailNum = sgGarageDetailMapper.createTaskNum(formatter.format(new Date()), "1");
//                sgGarageDetail.setTaskNum(TaskNumUtil.createTaskNum("RK",maxGarageDetailNum));
//                sgGarageDetail.setRemark(sgVehicleInfoDTO.getRemark());
//                sgGarageDetail.setCreateTime(new Date());// ????????????
//                sgGarageDetail.setCreatorName(CurrentUser.getCnName());// ?????????
//                sgGarageDetail.setCreatorId(CurrentUser.getUserId());
//                if(StringUtils.isNotBlank(CurrentUser.getDepartmentId())){
//                    sgGarageDetail.setCreatorDepartmentId(Long.parseLong(CurrentUser.getDepartmentId()));
//                }
//                sgGarageDetail.setCreatorDepartmentName(CurrentUser.getDepartmentName());
//                sgGarageDetailMapper.insert(sgGarageDetail);
//                logger.info("==========????????????create(): sgGarageDetail????????????=========");
//
//                //??????????????????
//                SgGarageOrder sgGarageOrder = new SgGarageOrder();
//                sgGarageOrder.setSgGarageInfoToId(sgVehicleInfo.getSgGarageInfoId());
//                sgGarageOrder.setSgVehicleId(sgVehicleInfo.getId());
//                String maxGarageOrderNum = sgGarageOrderMapper.createGarageOrderTaskNum(formatter.format(new Date()));
//                sgGarageOrder.setTaskNum(TaskNumUtil.createGarageOrderTaskNum(maxGarageOrderNum));
//                sgGarageOrder.setOrderStatus(SgAllocateTaskStatusEnum.INOFSTORE.getValue());
//                sgGarageOrder.setRemark(sgVehicleInfoDTO.getRemark());
//                sgGarageOrder.setSgGarageRKDatailId(sgGarageDetail.getId());
//                sgGarageOrder.setCreateTime(new Date());// ????????????
//                sgGarageOrder.setCreatorName(CurrentUser.getCnName());// ?????????
//                sgGarageOrder.setCreatorId(CurrentUser.getUserId());
//                if(StringUtils.isNotBlank(CurrentUser.getDepartmentId())){
//                    sgGarageOrder.setCreatorDepartmentId(Long.parseLong(CurrentUser.getDepartmentId()));
//                }
//                sgGarageOrder.setCreatorDepartmentName(CurrentUser.getDepartmentName());
//                sgGarageOrderMapper.insert(sgGarageOrder);
//                logger.info("==========????????????create(): order????????????=========");
//                sgGarageDetail.setSgGaragOrderId(sgGarageOrder.getId());
//                sgGarageDetailMapper.updateById(sgGarageDetail);
//
//                //??????order??????
//                SgGarageOrderLog sgGarageOrderLog = new SgGarageOrderLog();
//                sgGarageOrderLog.setEvent(TEMP_IN);
//                sgGarageOrderLog.setRemark(sgVehicleInfo.getRemark());
//                sgGarageOrderLog.setCreateTime(new Date());// ????????????
//                sgGarageOrderLog.setCreatorName(CurrentUser.getCnName());// ?????????
//                sgGarageOrderLog.setCreatorId(CurrentUser.getUserId());
//                if (CurrentUser.getDepartmentId() != null) {
//                    sgGarageOrderLog.setCreatorDepartmentId(Long.parseLong(CurrentUser.getDepartmentId()));
//                }
//                sgGarageOrderLog.setCreatorDepartmentName(CurrentUser.getDepartmentName());
//                sgGarageOrderLog.setSgGarageOrderId(sgGarageOrder.getId());
//                sgGarageOrderLog.insert();
//                logger.info("==========????????????create(): sgGarageOrderLog????????????=========");
//
//
//
//            }
            //????????????????????????
            logger.info("SgVehicleInfoServiceImpl create()??????:???????????????????????????????????????????????? =========");
            SgSendInfoToZhongtai rcSendInfoToZhongtai = new SgSendInfoToZhongtai();
            rcSendInfoToZhongtai.setBillNum(sgVehicleInfo.getAlixNum());//??????
            rcSendInfoToZhongtai.setInterfaceName(SendTypeEnum.PSM_RESTOCK_INTERFACE.getValue());//????????????(??????)
            rcSendInfoToZhongtai.setSendCount(0);//????????????
            rcSendInfoToZhongtai.setType(BillStatusEnum.PSM_RESTOCK_INTERFACE.getValue());//????????????
            rcSendInfoToZhongtai.setStat(0);//????????????
            rcSendInfoToZhongtai.setCreatorName("????????????");
            rcSendInfoToZhongtai.insert();

            //??????mq??????
            SgQueryVehicleIfoStatDTO statDTO = new SgQueryVehicleIfoStatDTO();
            statDTO.setApplyNo(sgVehicleInfo.getAlixNum());
            statDTO.setVin(sgVehicleInfo.getVin());
            statDTO.setState(1); //????????????????????? 0-???????????????1-?????????2-??????
            logger.info("????????????mq??????DTO=[{}]", JSONObject.toJSONString(statDTO));
            rabbitTemplate.convertAndSend("sg.loan.garage", "loanGetVehicleStatRoutingKey",JSON.toJSONString(statDTO));
            logger.info("==========????????????create(): ????????????mq????????????=========");


            //????????????  ????????????
            this.createVehicleLog(sgVehicleInfo, CREATE);
            logger.info("????????????????????????create() ?????????=[{}]", JSONObject.toJSONString(sgVehicleInfo));
        } catch (Exception e) {
            logger.error("create() failed???", e.getMessage());
            throw new BzException(e.getMessage());
        }
        return ResultUtil.success("????????????");
    }

    /**
     * @Title: update
     * @Description: ??????????????????
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/19 13:59
     */
    @Override
    public InvokeResult<String> update(SgVehicleInfoDTO sgVehicleInfoDTO) throws BzException {
        try {
            logger.info("??????????????????update() sgVehicleInfoDTO=[{}]", JSONObject.toJSONString(sgVehicleInfoDTO));
            SgVehicleInfo sgVehicleInfo = sgVehicleInfoMapper.selectById(sgVehicleInfoDTO.getId());
            if(sgVehicleInfo == null){
                logger.error("??????id["+sgVehicleInfoDTO.getId()+"]?????????????????????????????????????????????????????????");
                throw new BzException("??????id["+sgVehicleInfoDTO.getId()+"]?????????????????????????????????????????????????????????");
            }
            if(sgVehicleInfo.getStat() != SgVehicleStatusEnum.FREE.getValue()){
                logger.error("????????????????????????????????????");
                throw new BzException("????????????????????????????????????");
            }

            logger.info("???????????????????????????:=[{}]", JSONObject.toJSONString(sgVehicleInfo));
            BaseAssembler.mapObjWithoutNullAndBaseColumn(sgVehicleInfoDTO,sgVehicleInfo);
            this.updateById(sgVehicleInfo);

            //??????-?????????
            this.saveAtt(sgVehicleInfoDTO,sgVehicleInfo);

            logger.info("????????????:=[{}]", JSONObject.toJSONString(sgVehicleInfo));

            // ??????????????????
            this.createVehicleLog(sgVehicleInfo,  UPDATE);

        } catch (Exception e) {
            logger.error("update() failed???", e.getMessage());
            throw new BzException(e.getMessage());
        }
        return ResultUtil.success("????????????");
    }

    /**
     * @Title: getBill
     * @Description: ????????????
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/19 14:14
     */
    @Override
    public InvokeResult<SgVehicleInfoDTO> getBill(String vehicleId) throws BzException {
        logger.info("??????????????????ID??????{}" + vehicleId);
        SgVehicleInfoDTO vehicleInfoDTO = new SgVehicleInfoDTO();
        try {
            SgVehicleInfo sgVehicleInfo = sgVehicleInfoMapper.selectById(vehicleId);
            if (sgVehicleInfo == null) {
                logger.error("??????id[" + vehicleId + "]?????????????????????????????????????????????????????????");
                throw new BzException("??????id[" + vehicleId + "]?????????????????????????????????????????????????????????");
            }
            BaseAssembler.mapObjWithoutNull(sgVehicleInfo, vehicleInfoDTO);
            logger.info("===============???????????? getBill():????????????????????????=============");

            SgGarageInfo sgGarageInfo = sgGarageInfoMapper.selectById(sgVehicleInfo.getSgGarageInfoId());
            if (sgGarageInfo != null) {
                vehicleInfoDTO.setSgGarageName(sgGarageInfo.getGarageName());
            }
            logger.info("===============???????????? getBill():????????????????????????=============");
            List<AttachSourceDTO> sourceDTOList = attachSourceMapper.queryAtts(vehicleId,RcAttachmentTypeEnum.SG_INTO_GARAGE.getValue());
            if (sourceDTOList != null) {
                vehicleInfoDTO.setVehcleAttList(sourceDTOList);
            }
            logger.info("======???????????? getBill():??????????????????");
        }catch (Exception e){
            logger.error("getBill() failed???", e.getMessage());
            throw new BzException(e.getMessage());
        }
        return ResultUtil.success(vehicleInfoDTO);
    }


    /**
     * @Title: pageQuery
     * @Description: ????????????
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/19 15:03
     */
    @Override
    public IPage<SgVehicleInfoDTO> pageQuery(SgVehicleInfoDTO sgVehicleInfoDTO) throws BzException {
        logger.info("???????????????????????????{}", JSONObject.toJSON(sgVehicleInfoDTO));
        Page<SgVehicleInfoDTO> dtoPage = null;
        try{
            //???????????? - 0??????????????? - 1
            String sign = sgVehicleInfoDTO.getSign();
            if(StringUtils.isBlank(sign)){
                logger.error("sign??????");
                throw new BzException("sign??????");
            }

            Page<SgVehicleInfo> page = new PageFactory<SgVehicleInfo>().defaultPage(sgVehicleInfoDTO);
            List<String> garageAdmins = new ArrayList<>();

            //????????????
            //?????????????????????????????????????????????????????????????????????
            Boolean hasRoles = CurrentUser.hasRole(RoleEnum.GARAGE_MANAGE_ROLE);
            //?????????????????????????????? ???????????????????????????????????????????????????
            if(hasRoles){
                sgVehicleInfoDTO.setCreatorName(CurrentUser.getCnName());
                Map<String,Object> map = new HashMap<>();
                map.put("contact", CurrentUser.getCnName());
                map.put("IS_DELETED", 0);
                List<SgContactToGarageTemp> contactList = SgContactToGarageTempMapper.selectByMap(map);
                if(CollectionUtils.isNotEmpty(contactList)){
                    for (SgContactToGarageTemp contact : contactList){
                        garageAdmins.add(contact.getGarageInfoId());
                    }
                }
            }

            logger.info("=====????????????pageQuery(): ???????????????????????????");
            IPage<SgVehicleInfoDTO> pageList = sgVehicleInfoMapper.pageQuery(page,sgVehicleInfoDTO,garageAdmins);
            logger.info("=====????????????pageQuery(): ????????????");
            List<SgVehicleInfoDTO> sgVehicleInfoDTOS = pageList.getRecords();
            if(CollectionUtils.isNotEmpty(sgVehicleInfoDTOS)){
                //????????????????????????
                this.getActualStorageTimeInt(sgVehicleInfoDTOS);
            }
            logger.info("=====????????????pageQuery(): ?????????[" + pageList.getTotal() + "]?????????");
            dtoPage = new Page<SgVehicleInfoDTO>(pageList.getCurrent(), pageList.getSize(), pageList.getTotal()).setRecords(pageList.getRecords());
        }catch (Exception e){
            logger.error("pageQuery() failed???", e.getMessage());
            throw new BzException(e.getMessage());
        }
        return dtoPage;
    }

    /**
     * @Title: orderPageQuery
     * @Description: ????????????????????????
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/21 14:01
     */
    @Override
    public IPage<SgVehicleInfoDTO> orderPageQuery(SgVehicleInfoDTO sgVehicleInfoDTO) throws BzException {
        logger.info("orderPageQuery() ?????????????????????{}", JSONObject.toJSON(sgVehicleInfoDTO));
        Page<SgVehicleInfoDTO> dtoPage = null;
        try{
            Page<SgVehicleInfo> page = new PageFactory<SgVehicleInfo>().defaultPage(sgVehicleInfoDTO);
            IPage<SgVehicleInfoDTO> pageList = sgVehicleInfoMapper.orderPageQuery(page, sgVehicleInfoDTO);
            List<SgVehicleInfoDTO> sgVehicleInfoDTOS = pageList.getRecords();
            //????????????????????????
            this.getActualStorageTimeInt(sgVehicleInfoDTOS);
            logger.info("=====???????????? orderPageQuery():?????????[" + pageList.getTotal() + "]?????????");
            dtoPage = new Page<SgVehicleInfoDTO>(pageList.getCurrent(), pageList.getSize(), pageList.getTotal()).setRecords(pageList.getRecords());
        }catch (Exception e){
            logger.error("orderPageQuery() failed???", e.getMessage());
            throw new BzException(e.getMessage());
        }
        return dtoPage;
    }

    /**
     * @Title: checkOrderBill
     * @Description: ??????-??????????????????
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/21 20:09
     */
    @Override
    public InvokeResult<SgVehicleInfoDTO> checkOrderBill(String vehicleId) throws BzException {
        logger.info("??????-?????????????????? ID??????{}" + vehicleId);
        SgVehicleInfoDTO vehicleInfoDTO = new SgVehicleInfoDTO();
        try {
            //??????????????????
            List<SgGarageOrder> sgGarageOrderList = sgGarageOrderMapper.selectList(new QueryWrapper<SgGarageOrder>().eq("sgVehicle_id", vehicleId).eq("IS_DELETED", "0"));
            if (sgGarageOrderList.isEmpty()) {
                logger.error("?????????????????????????????????????????????");
                throw new BzException("?????????????????????????????????????????????");
            }

            SgGarageOrder sgGarageOrder = sgGarageOrderList.get(0);
            vehicleInfoDTO.setTaskNum(sgGarageOrder.getTaskNum());
            logger.info("===============????????????????????????=============");

            SgVehicleInfo sgVehicleInfo = sgVehicleInfoMapper.selectById(vehicleId);
            if (sgVehicleInfo == null) {
                logger.error("??????id[" + vehicleId + "]?????????????????????????????????????????????????????????");
                throw new BzException("??????id[" + vehicleId + "]?????????????????????????????????????????????????????????");
            }
            BaseAssembler.mapObjWithoutNull(sgVehicleInfo, vehicleInfoDTO);
            logger.info("===============???????????? checkOrderBill(): ????????????????????????=============");

            SgGarageInfo sgGarageInfo = sgGarageInfoMapper.selectById(sgVehicleInfo.getSgGarageInfoId());
            if (sgGarageInfo == null) {
                logger.error("????????????????????????");
                throw new BzException("????????????????????????");
            }
            vehicleInfoDTO.setSgGarageName(sgGarageInfo.getGarageName());
            logger.info("===============???????????? checkOrderBill(): ????????????????????????=============");

            List<AttachSourceDTO> sourceDTOList = attachSourceMapper.queryAtts(vehicleId,null);
            if (sourceDTOList != null) {
                vehicleInfoDTO.setVehcleAttList(sourceDTOList);
            }
            logger.info("======???????????? checkOrderBill(): ????????????????????????");
        }catch (Exception e){
            logger.error("checkOrderBill() failed???", e.getMessage());
            throw new BzException(e.getMessage());
        }
        return ResultUtil.success(vehicleInfoDTO);
    }

    /**
     * @Title: queryFreeVeh
     * @Description: ??????????????????
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/9/16 16:08
     */
    @Override
    public List<SgVehicleByFreeDTO> queryFreeVeh() throws BzException {
        logger.info("????????????????????????");
        List<SgVehicleByFreeDTO> result = new ArrayList<SgVehicleByFreeDTO>();
        try {
            //????????????????????????
            QueryWrapper<SgVehicleInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("stat",SgVehicleStatusEnum.FREE.getValue())
                    .eq("IS_DELETED","0");
            List<SgVehicleInfo> list = list(queryWrapper);
            if(list != null && !list.isEmpty()){
                for (SgVehicleInfo sgVehicleInfo : list) {
                    SgVehicleByFreeDTO vehicleByFreeDTO = new SgVehicleByFreeDTO();
                    //??????????????????
                    BaseAssembler.mapObjWithoutNull(sgVehicleInfo, vehicleByFreeDTO);
                    //?????????????????????????????????
                    SgGarageInfo sgGarageInfo = sgGarageInfoMapper.selectById(sgVehicleInfo.getSgGarageInfoId());
                    if (sgGarageInfo != null) {
                        vehicleByFreeDTO.setGarageName(sgGarageInfo.getGarageName());
                        vehicleByFreeDTO.setGarageAddresss(sgGarageInfo.getGarageAddresss());
                        vehicleByFreeDTO.setProvince(sgGarageInfo.getProvince());
                        vehicleByFreeDTO.setProvinceStr(sgGarageInfo.getProvinceStr());
                        vehicleByFreeDTO.setCity(sgGarageInfo.getCity());
                        vehicleByFreeDTO.setCityStr(sgGarageInfo.getCityStr());
                        vehicleByFreeDTO.setGarageInfoId(sgGarageInfo.getId());
                    }
                    vehicleByFreeDTO.setLeaseProperty(LeasePropertyEnum.getDisplayNameByIndex(sgVehicleInfo.getLeaseProperty()));
                    result.add(vehicleByFreeDTO);
                }
            }
            logger.info("===============?????????????????????queryFreeVeh() ??????=============");
        }catch (Exception e){
            logger.error("queryFreeVeh() failed???", e.getMessage());
            throw new BzException(e.getMessage());
        }
        return result;
    }

    /**
     * @Title: getActualStorageTimeInt
     * @Description: ????????????????????????
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/21 19:53
     */
    private void getActualStorageTimeInt(List<SgVehicleInfoDTO> list) throws BzException {
        try{
            if(list != null){
                for(SgVehicleInfoDTO dto : list){
                    SgGarageInfo sgGarageInfo = sgGarageInfoMapper.selectById(dto.getSgGarageInfoId());
                    if(sgGarageInfo != null){
                        dto.setSgGarageName(sgGarageInfo.getGarageName());
                        dto.setSgGarageAddresss(sgGarageInfo.getGarageAddresss());
                    }
                    Date storageTime = dto.getActualStorageTime();
                    if(storageTime != null){
                        int daysBetween = DateUtil.daysBetween(storageTime, new Date());
                        dto.setActualStorageTimeInt(daysBetween);
                    }

                }
            }
        }catch (Exception e){
            logger.error("??????????????????????????????",e.getMessage());
            throw new BzException(e.getMessage());
        }

    }

    /**
     * @Title: createVehicleLog
     * @Description: ????????????
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/19 9:57
     */
    private void createVehicleLog(SgVehicleInfo sgVehicleInfo, String event) {
        SgVehicleLog sgVehicleLog = new SgVehicleLog();
        sgVehicleLog.setEvent(event);
        sgVehicleLog.setRemark(sgVehicleInfo.getRemark());
        sgVehicleLog.setCreateTime(new Date());// ????????????
        sgVehicleLog.setCreatorName(CurrentUser.getCnName());// ?????????
        sgVehicleLog.setCreatorId(CurrentUser.getUserId());
        if(StringUtils.isNotBlank(CurrentUser.getDepartmentId())){
            sgVehicleLog.setCreatorDepartmentId(Long.parseLong(CurrentUser.getDepartmentId()));
        }
        sgVehicleLog.setCreatorDepartmentName(CurrentUser.getDepartmentName());
        sgVehicleLog.setVehicleId(sgVehicleInfo.getId());
        sgVehicleLogMapper.insert(sgVehicleLog);
    }


    /**
     * @Title: saveAtt
     * @Description: ????????????
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/26 17:29
     */
    private void saveAtt(SgVehicleInfoDTO sgVehicleInfoDTO,SgVehicleInfo sgVehicleInfo) throws BzException{
        try{
            //??????-?????????
            List<String> attIds = sgVehicleInfoDTO.getAttIds();
            if(!attIds.isEmpty()){
                for (String attId : attIds){
                    AttachSource att = attachSourceMapper.selectById(attId);
                    if(att != null){
                        att.setAttchType(RcAttachmentTypeEnum.SG_INTO_GARAGE.getValue());// ????????????-?????????????????????
                        att.setBussId(sgVehicleInfo.getId());
                        att.setCreatorName(CurrentUser.getCnName());// ?????????
                        att.setCreatorId(CurrentUser.getUserId());
                        if(StringUtils.isNotBlank(CurrentUser.getDepartmentId())){
                            att.setCreatorDepartmentId(Long.parseLong(CurrentUser.getDepartmentId()));
                        }
                        att.setCreatorDepartmentName(CurrentUser.getDepartmentName());
                        attachSourceMapper.updateById(att);
                    }
                }
            }
            logger.info("==========??????????????????=========");
        }catch (Exception e){
            logger.error("??????????????????",e.getMessage());
            throw new BzException(e.getMessage());
        }
    }





    /**
    * updateVehicleFour(??????????????????????????????)
    * @param
    * @return
    * void
    * @author: YixinCapital -- libochen
    * 2019/9/17 9:49
    */
    public String updateVehicleFour() throws BzException{
        logger.info("==========????????????????????????????????????=========");
        String errMsg = "true";
        try {
            QueryWrapper queryVehicleWrapper = new QueryWrapper();
            queryVehicleWrapper.eq("IS_DELETED", false);
            queryVehicleWrapper.eq("is_update", false);
            List<SgVehicleInfo> sgVehicleInfos =  sgVehicleInfoMapper.selectList(queryVehicleWrapper);
            logger.info("==========updateVehicleFour??????????????????{}",JSONObject.toJSON(sgVehicleInfos));
            logger.info("==========updateVehicleFour-----????????????{}", JSONObject.toJSON(sgVehicleInfos.size()));
            if (CollectionUtils.isNotEmpty(sgVehicleInfos)) {
                for (int i = 0; i < sgVehicleInfos.size(); i++) {
                    SgVehicleInfo ve = sgVehicleInfos.get(i);
                    String model = ve.getModel();
                    Map<String, String> map = new HashMap<>();
                    map.put("styleId", model);
                    logger.info("?????????????????????getVehicle4DetailInfoForTB,??????:{}", JSONObject.toJSON(map));
                    String c = RestUtil.sendRequest(carStyleUrl + "/carInfo/getVehicle4DetailInfoForTB", map);
                    logger.info("?????????????????????:{}", JSONObject.toJSON(c));
                    InvokeResult<CatTypeDTO> result = JSON.parseObject(c, new TypeReference<InvokeResult<CatTypeDTO>>(){});
                    if (!result.isSuccess()) {
                        logger.error("???????????????????????????????????????" + result.getErrorMessage());
                    }
                    CatTypeDTO catTypeDTO = (CatTypeDTO) result.getData();
                    ve.setBrand(catTypeDTO.getMasterBrandId());//?????????
                    ve.setBrandStr(catTypeDTO.getMasterBrandName());
                    ve.setBrandModel(catTypeDTO.getMakeId());//?????????
                    ve.setBrandModelStr(catTypeDTO.getMakeName());
                    ve.setVehicleClass(catTypeDTO.getModelId());//??????
                    ve.setVehicleClassStr(catTypeDTO.getModelName());
                    ve.setModelStr(catTypeDTO.getName() + " " + catTypeDTO.getYear() + "???");
                    ve.setIsUpdate(true);
                    sgVehicleInfoMapper.updateById(ve);
                }
            }

        }catch (Exception e) {
            logger.info("?????????4?????????????????????:",e.getMessage());
        }
        return errMsg;
    }

}
