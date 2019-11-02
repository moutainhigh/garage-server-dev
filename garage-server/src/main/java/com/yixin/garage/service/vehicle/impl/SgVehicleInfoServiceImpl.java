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
 *  服务实现类
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



    private static final String CREATE = "新增车辆";
    private static final String UPDATE = "车辆编辑";
    private static final String TEMP_IN = "临时入库";

    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

    /**
     * @Title: create
     * @Description: 新增车辆
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/16 18:07
     */
    @Override
    public InvokeResult<String> create(SgVehicleInfoDTO sgVehicleInfoDTO) throws BzException {
        try {
            logger.info("创建车辆信息create() sgVehicleInfoDTO=[{}]", JSONObject.toJSONString(sgVehicleInfoDTO));

            //判断车辆是否符合入库条件
            QueryWrapper<SgVehicleInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("stat",SgVehicleStatusEnum.FREE.getValue())
                    .eq("vin",sgVehicleInfoDTO.getVin())
                    .eq("IS_DELETED","0");
            List<SgVehicleInfo> list = list(queryWrapper);
            if (list.size() > 0) {
                logger.error("车库中已存在相同车架号的在库车辆，不允许重复添加");
                throw new BzException("车库中已存在相同车架号的在库车辆，不允许重复添加");
            }
            SgVehicleInfo sgVehicleInfo = new SgVehicleInfo();
            BaseAssembler.mapObjWithoutNull(sgVehicleInfoDTO, sgVehicleInfo);
            sgVehicleInfo.setActualStorageTime(new Date());
            sgVehicleInfo.setStat(SgVehicleStatusEnum.FREE.getValue());

            SgGarageInfo garageInfo = sgGarageInfoMapper.selectById(sgVehicleInfoDTO.getSgGarageInfoId());
            if (garageInfo.getParkingNum() - garageInfo.getParkedNum() > 0) {
                garageInfo.setParkedNum(garageInfo.getParkedNum() + 1);
                sgGarageInfoMapper.updateById(garageInfo);
                logger.info("===========新增车辆 create():车位数更新完成=========");
            }else{
                logger.error("车库["+garageInfo.getGarageName()+"]车位不足，请更换车库");
                throw new BzException("车库["+garageInfo.getGarageName()+"]车位不足，请更换车库");
            }
            sgVehicleInfo.setIsUpdate(true);
            sgVehicleInfoMapper.insert(sgVehicleInfo);
            logger.info("============新增车辆 create():车辆添加完成=========");

            //附件-入库单
            this.saveAtt(sgVehicleInfoDTO,sgVehicleInfo);
            logger.info("==========新增车辆 create(): 附件添加完成]=========");


            //删除附件
            List<String> delIds = sgVehicleInfoDTO.getDelAttIds();
            if(delIds != null && !delIds.isEmpty()){
                for(String attId : delIds){
                    AttachSource att = attachSourceMapper.selectById(attId);
                    if(att != null){
                        att.deleteById();
                    }
                }

            }
            logger.info("==========新增车辆 create():附件删除完成=========");
//            //在库状态下生成入库信息
//            if(sgVehicleInfo.getStat() == SgVehicleStatusEnum.FREE.getValue()){
//                //创建入库单信息
//                SgGarageDetail sgGarageDetail = new SgGarageDetail();
//                sgGarageDetail.setOrderType(SgOrderInTypeEnum.TEMPORARY_IN.getValue());//入库类型： 1收车  2:退车
//                sgGarageDetail.setAlixNum(sgVehicleInfo.getAlixNum());//alix申请编号
//                sgGarageDetail.setVin(sgVehicleInfo.getVin());
//                sgGarageDetail.setGarageSign(RCAllocateGarageSignEnum.GARAGEIN_SIGN.getValue());
//                sgGarageDetail.setPushSource(PushSourceEnum.TEMP_GARAGE.getValue());
//                sgGarageDetail.setBillStatus(SgAllocateTaskStatusEnum.INOFSTORE.getValue());
//                String maxGarageDetailNum = sgGarageDetailMapper.createTaskNum(formatter.format(new Date()), "1");
//                sgGarageDetail.setTaskNum(TaskNumUtil.createTaskNum("RK",maxGarageDetailNum));
//                sgGarageDetail.setRemark(sgVehicleInfoDTO.getRemark());
//                sgGarageDetail.setCreateTime(new Date());// 操作时间
//                sgGarageDetail.setCreatorName(CurrentUser.getCnName());// 操作人
//                sgGarageDetail.setCreatorId(CurrentUser.getUserId());
//                if(StringUtils.isNotBlank(CurrentUser.getDepartmentId())){
//                    sgGarageDetail.setCreatorDepartmentId(Long.parseLong(CurrentUser.getDepartmentId()));
//                }
//                sgGarageDetail.setCreatorDepartmentName(CurrentUser.getDepartmentName());
//                sgGarageDetailMapper.insert(sgGarageDetail);
//                logger.info("==========新增车辆create(): sgGarageDetail创建成功=========");
//
//                //创建订单信息
//                SgGarageOrder sgGarageOrder = new SgGarageOrder();
//                sgGarageOrder.setSgGarageInfoToId(sgVehicleInfo.getSgGarageInfoId());
//                sgGarageOrder.setSgVehicleId(sgVehicleInfo.getId());
//                String maxGarageOrderNum = sgGarageOrderMapper.createGarageOrderTaskNum(formatter.format(new Date()));
//                sgGarageOrder.setTaskNum(TaskNumUtil.createGarageOrderTaskNum(maxGarageOrderNum));
//                sgGarageOrder.setOrderStatus(SgAllocateTaskStatusEnum.INOFSTORE.getValue());
//                sgGarageOrder.setRemark(sgVehicleInfoDTO.getRemark());
//                sgGarageOrder.setSgGarageRKDatailId(sgGarageDetail.getId());
//                sgGarageOrder.setCreateTime(new Date());// 操作时间
//                sgGarageOrder.setCreatorName(CurrentUser.getCnName());// 操作人
//                sgGarageOrder.setCreatorId(CurrentUser.getUserId());
//                if(StringUtils.isNotBlank(CurrentUser.getDepartmentId())){
//                    sgGarageOrder.setCreatorDepartmentId(Long.parseLong(CurrentUser.getDepartmentId()));
//                }
//                sgGarageOrder.setCreatorDepartmentName(CurrentUser.getDepartmentName());
//                sgGarageOrderMapper.insert(sgGarageOrder);
//                logger.info("==========新增车辆create(): order创建成功=========");
//                sgGarageDetail.setSgGaragOrderId(sgGarageOrder.getId());
//                sgGarageDetailMapper.updateById(sgGarageDetail);
//
//                //添加order日志
//                SgGarageOrderLog sgGarageOrderLog = new SgGarageOrderLog();
//                sgGarageOrderLog.setEvent(TEMP_IN);
//                sgGarageOrderLog.setRemark(sgVehicleInfo.getRemark());
//                sgGarageOrderLog.setCreateTime(new Date());// 操作时间
//                sgGarageOrderLog.setCreatorName(CurrentUser.getCnName());// 操作人
//                sgGarageOrderLog.setCreatorId(CurrentUser.getUserId());
//                if (CurrentUser.getDepartmentId() != null) {
//                    sgGarageOrderLog.setCreatorDepartmentId(Long.parseLong(CurrentUser.getDepartmentId()));
//                }
//                sgGarageOrderLog.setCreatorDepartmentName(CurrentUser.getDepartmentName());
//                sgGarageOrderLog.setSgGarageOrderId(sgGarageOrder.getId());
//                sgGarageOrderLog.insert();
//                logger.info("==========新增车辆create(): sgGarageOrderLog创建成功=========");
//
//
//
//            }
            //推送中台信息保存
            logger.info("SgVehicleInfoServiceImpl create()入库:保存推送信息，供定时推送中台使用 =========");
            SgSendInfoToZhongtai rcSendInfoToZhongtai = new SgSendInfoToZhongtai();
            rcSendInfoToZhongtai.setBillNum(sgVehicleInfo.getAlixNum());//单号
            rcSendInfoToZhongtai.setInterfaceName(SendTypeEnum.PSM_RESTOCK_INTERFACE.getValue());//接口名称(码值)
            rcSendInfoToZhongtai.setSendCount(0);//发送次数
            rcSendInfoToZhongtai.setType(BillStatusEnum.PSM_RESTOCK_INTERFACE.getValue());//单子类型
            rcSendInfoToZhongtai.setStat(0);//发送状态
            rcSendInfoToZhongtai.setCreatorName("临时入库");
            rcSendInfoToZhongtai.insert();

            //发送mq消息
            SgQueryVehicleIfoStatDTO statDTO = new SgQueryVehicleIfoStatDTO();
            statDTO.setApplyNo(sgVehicleInfo.getAlixNum());
            statDTO.setVin(sgVehicleInfo.getVin());
            statDTO.setState(1); //给融后推送消息 0-临时出库、1-在库、2-出库
            logger.info("融后发送mq消息DTO=[{}]", JSONObject.toJSONString(statDTO));
            rabbitTemplate.convertAndSend("sg.loan.garage", "loanGetVehicleStatRoutingKey",JSON.toJSONString(statDTO));
            logger.info("==========新增车辆create(): 融后发送mq消息成功=========");


            //车辆创建  增加日志
            this.createVehicleLog(sgVehicleInfo, CREATE);
            logger.info("创建车辆信息成功create() 返回值=[{}]", JSONObject.toJSONString(sgVehicleInfo));
        } catch (Exception e) {
            logger.error("create() failed：", e.getMessage());
            throw new BzException(e.getMessage());
        }
        return ResultUtil.success("新增成功");
    }

    /**
     * @Title: update
     * @Description: 编辑车辆信息
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/19 13:59
     */
    @Override
    public InvokeResult<String> update(SgVehicleInfoDTO sgVehicleInfoDTO) throws BzException {
        try {
            logger.info("编辑车辆信息update() sgVehicleInfoDTO=[{}]", JSONObject.toJSONString(sgVehicleInfoDTO));
            SgVehicleInfo sgVehicleInfo = sgVehicleInfoMapper.selectById(sgVehicleInfoDTO.getId());
            if(sgVehicleInfo == null){
                logger.error("根据id["+sgVehicleInfoDTO.getId()+"]未查到对应的数据，请刷新页面重新编辑！");
                throw new BzException("根据id["+sgVehicleInfoDTO.getId()+"]未查到对应的数据，请刷新页面重新编辑！");
            }
            if(sgVehicleInfo.getStat() != SgVehicleStatusEnum.FREE.getValue()){
                logger.error("非在库状态的车辆不能编辑");
                throw new BzException("非在库状态的车辆不能编辑");
            }

            logger.info("编辑前的车辆信息为:=[{}]", JSONObject.toJSONString(sgVehicleInfo));
            BaseAssembler.mapObjWithoutNullAndBaseColumn(sgVehicleInfoDTO,sgVehicleInfo);
            this.updateById(sgVehicleInfo);

            //附件-入库单
            this.saveAtt(sgVehicleInfoDTO,sgVehicleInfo);

            logger.info("编辑完成:=[{}]", JSONObject.toJSONString(sgVehicleInfo));

            // 添加车辆日志
            this.createVehicleLog(sgVehicleInfo,  UPDATE);

        } catch (Exception e) {
            logger.error("update() failed：", e.getMessage());
            throw new BzException(e.getMessage());
        }
        return ResultUtil.success("修改成功");
    }

    /**
     * @Title: getBill
     * @Description: 查看详情
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/19 14:14
     */
    @Override
    public InvokeResult<SgVehicleInfoDTO> getBill(String vehicleId) throws BzException {
        logger.info("查看车辆信息ID为：{}" + vehicleId);
        SgVehicleInfoDTO vehicleInfoDTO = new SgVehicleInfoDTO();
        try {
            SgVehicleInfo sgVehicleInfo = sgVehicleInfoMapper.selectById(vehicleId);
            if (sgVehicleInfo == null) {
                logger.error("根据id[" + vehicleId + "]未查到对应的数据，请刷新页面重新操作！");
                throw new BzException("根据id[" + vehicleId + "]未查到对应的数据，请刷新页面重新操作！");
            }
            BaseAssembler.mapObjWithoutNull(sgVehicleInfo, vehicleInfoDTO);
            logger.info("===============车辆详情 getBill():车辆信息转换成功=============");

            SgGarageInfo sgGarageInfo = sgGarageInfoMapper.selectById(sgVehicleInfo.getSgGarageInfoId());
            if (sgGarageInfo != null) {
                vehicleInfoDTO.setSgGarageName(sgGarageInfo.getGarageName());
            }
            logger.info("===============车辆详情 getBill():车库名称查询成功=============");
            List<AttachSourceDTO> sourceDTOList = attachSourceMapper.queryAtts(vehicleId,RcAttachmentTypeEnum.SG_INTO_GARAGE.getValue());
            if (sourceDTOList != null) {
                vehicleInfoDTO.setVehcleAttList(sourceDTOList);
            }
            logger.info("======车辆详情 getBill():附件查询成功");
        }catch (Exception e){
            logger.error("getBill() failed：", e.getMessage());
            throw new BzException(e.getMessage());
        }
        return ResultUtil.success(vehicleInfoDTO);
    }


    /**
     * @Title: pageQuery
     * @Description: 分页查询
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/19 15:03
     */
    @Override
    public IPage<SgVehicleInfoDTO> pageQuery(SgVehicleInfoDTO sgVehicleInfoDTO) throws BzException {
        logger.info("分页查询车辆入参：{}", JSONObject.toJSON(sgVehicleInfoDTO));
        Page<SgVehicleInfoDTO> dtoPage = null;
        try{
            //车辆信息 - 0，临时出库 - 1
            String sign = sgVehicleInfoDTO.getSign();
            if(StringUtils.isBlank(sign)){
                logger.error("sign为空");
                throw new BzException("sign为空");
            }

            Page<SgVehicleInfo> page = new PageFactory<SgVehicleInfo>().defaultPage(sgVehicleInfoDTO);
            List<String> garageAdmins = new ArrayList<>();

            //权限控制
            //获取当前登录人的角色集合，判断是否拥有库管权限
            Boolean hasRoles = CurrentUser.hasRole(RoleEnum.GARAGE_MANAGE_ROLE);
            //如果当前登录人角色为 车库管理员，则只能看到对应车库的车
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

            logger.info("=====车辆列表pageQuery(): 登录人权限查询完毕");
            IPage<SgVehicleInfoDTO> pageList = sgVehicleInfoMapper.pageQuery(page,sgVehicleInfoDTO,garageAdmins);
            logger.info("=====车辆列表pageQuery(): 查询完成");
            List<SgVehicleInfoDTO> sgVehicleInfoDTOS = pageList.getRecords();
            if(CollectionUtils.isNotEmpty(sgVehicleInfoDTOS)){
                //计算累计在库天数
                this.getActualStorageTimeInt(sgVehicleInfoDTOS);
            }
            logger.info("=====车辆列表pageQuery(): 共查询[" + pageList.getTotal() + "]条数据");
            dtoPage = new Page<SgVehicleInfoDTO>(pageList.getCurrent(), pageList.getSize(), pageList.getTotal()).setRecords(pageList.getRecords());
        }catch (Exception e){
            logger.error("pageQuery() failed：", e.getMessage());
            throw new BzException(e.getMessage());
        }
        return dtoPage;
    }

    /**
     * @Title: orderPageQuery
     * @Description: 订单列表分页查询
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/21 14:01
     */
    @Override
    public IPage<SgVehicleInfoDTO> orderPageQuery(SgVehicleInfoDTO sgVehicleInfoDTO) throws BzException {
        logger.info("orderPageQuery() 分页查询入参：{}", JSONObject.toJSON(sgVehicleInfoDTO));
        Page<SgVehicleInfoDTO> dtoPage = null;
        try{
            Page<SgVehicleInfo> page = new PageFactory<SgVehicleInfo>().defaultPage(sgVehicleInfoDTO);
            IPage<SgVehicleInfoDTO> pageList = sgVehicleInfoMapper.orderPageQuery(page, sgVehicleInfoDTO);
            List<SgVehicleInfoDTO> sgVehicleInfoDTOS = pageList.getRecords();
            //计算累计在库天数
            this.getActualStorageTimeInt(sgVehicleInfoDTOS);
            logger.info("=====订单列表 orderPageQuery():共查询[" + pageList.getTotal() + "]条数据");
            dtoPage = new Page<SgVehicleInfoDTO>(pageList.getCurrent(), pageList.getSize(), pageList.getTotal()).setRecords(pageList.getRecords());
        }catch (Exception e){
            logger.error("orderPageQuery() failed：", e.getMessage());
            throw new BzException(e.getMessage());
        }
        return dtoPage;
    }

    /**
     * @Title: checkOrderBill
     * @Description: 车辆-订单综合查新
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/21 20:09
     */
    @Override
    public InvokeResult<SgVehicleInfoDTO> checkOrderBill(String vehicleId) throws BzException {
        logger.info("车辆-订单综合查新 ID为：{}" + vehicleId);
        SgVehicleInfoDTO vehicleInfoDTO = new SgVehicleInfoDTO();
        try {
            //查询订单信息
            List<SgGarageOrder> sgGarageOrderList = sgGarageOrderMapper.selectList(new QueryWrapper<SgGarageOrder>().eq("sgVehicle_id", vehicleId).eq("IS_DELETED", "0"));
            if (sgGarageOrderList.isEmpty()) {
                logger.error("未获取到车辆订单信息，查询失败");
                throw new BzException("未获取到车辆订单信息，查询失败");
            }

            SgGarageOrder sgGarageOrder = sgGarageOrderList.get(0);
            vehicleInfoDTO.setTaskNum(sgGarageOrder.getTaskNum());
            logger.info("===============订单编号查询成功=============");

            SgVehicleInfo sgVehicleInfo = sgVehicleInfoMapper.selectById(vehicleId);
            if (sgVehicleInfo == null) {
                logger.error("根据id[" + vehicleId + "]未查到对应的数据，请刷新页面重新操作！");
                throw new BzException("根据id[" + vehicleId + "]未查到对应的数据，请刷新页面重新操作！");
            }
            BaseAssembler.mapObjWithoutNull(sgVehicleInfo, vehicleInfoDTO);
            logger.info("===============订单详情 checkOrderBill(): 车辆信息转换成功=============");

            SgGarageInfo sgGarageInfo = sgGarageInfoMapper.selectById(sgVehicleInfo.getSgGarageInfoId());
            if (sgGarageInfo == null) {
                logger.error("车库信息查询失败");
                throw new BzException("车库信息查询失败");
            }
            vehicleInfoDTO.setSgGarageName(sgGarageInfo.getGarageName());
            logger.info("===============订单详情 checkOrderBill(): 车库名称查询成功=============");

            List<AttachSourceDTO> sourceDTOList = attachSourceMapper.queryAtts(vehicleId,null);
            if (sourceDTOList != null) {
                vehicleInfoDTO.setVehcleAttList(sourceDTOList);
            }
            logger.info("======订单详情 checkOrderBill(): 车辆附件查询成功");
        }catch (Exception e){
            logger.error("checkOrderBill() failed：", e.getMessage());
            throw new BzException(e.getMessage());
        }
        return ResultUtil.success(vehicleInfoDTO);
    }

    /**
     * @Title: queryFreeVeh
     * @Description: 查询在库车辆
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/9/16 16:08
     */
    @Override
    public List<SgVehicleByFreeDTO> queryFreeVeh() throws BzException {
        logger.info("查询在库车辆开始");
        List<SgVehicleByFreeDTO> result = new ArrayList<SgVehicleByFreeDTO>();
        try {
            //查询在库车辆信息
            QueryWrapper<SgVehicleInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("stat",SgVehicleStatusEnum.FREE.getValue())
                    .eq("IS_DELETED","0");
            List<SgVehicleInfo> list = list(queryWrapper);
            if(list != null && !list.isEmpty()){
                for (SgVehicleInfo sgVehicleInfo : list) {
                    SgVehicleByFreeDTO vehicleByFreeDTO = new SgVehicleByFreeDTO();
                    //组装车辆信息
                    BaseAssembler.mapObjWithoutNull(sgVehicleInfo, vehicleByFreeDTO);
                    //组装车库名称、车库地址
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
            logger.info("===============查询在库车辆：queryFreeVeh() 完成=============");
        }catch (Exception e){
            logger.error("queryFreeVeh() failed：", e.getMessage());
            throw new BzException(e.getMessage());
        }
        return result;
    }

    /**
     * @Title: getActualStorageTimeInt
     * @Description: 计算累计在库天数
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
            logger.error("累计在库日期计算有误",e.getMessage());
            throw new BzException(e.getMessage());
        }

    }

    /**
     * @Title: createVehicleLog
     * @Description: 新增日志
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/19 9:57
     */
    private void createVehicleLog(SgVehicleInfo sgVehicleInfo, String event) {
        SgVehicleLog sgVehicleLog = new SgVehicleLog();
        sgVehicleLog.setEvent(event);
        sgVehicleLog.setRemark(sgVehicleInfo.getRemark());
        sgVehicleLog.setCreateTime(new Date());// 操作时间
        sgVehicleLog.setCreatorName(CurrentUser.getCnName());// 操作人
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
     * @Description: 附件上传
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/26 17:29
     */
    private void saveAtt(SgVehicleInfoDTO sgVehicleInfoDTO,SgVehicleInfo sgVehicleInfo) throws BzException{
        try{
            //附件-入库单
            List<String> attIds = sgVehicleInfoDTO.getAttIds();
            if(!attIds.isEmpty()){
                for (String attId : attIds){
                    AttachSource att = attachSourceMapper.selectById(attId);
                    if(att != null){
                        att.setAttchType(RcAttachmentTypeEnum.SG_INTO_GARAGE.getValue());// 附件类型-安全车库入库单
                        att.setBussId(sgVehicleInfo.getId());
                        att.setCreatorName(CurrentUser.getCnName());// 操作人
                        att.setCreatorId(CurrentUser.getUserId());
                        if(StringUtils.isNotBlank(CurrentUser.getDepartmentId())){
                            att.setCreatorDepartmentId(Long.parseLong(CurrentUser.getDepartmentId()));
                        }
                        att.setCreatorDepartmentName(CurrentUser.getDepartmentName());
                        attachSourceMapper.updateById(att);
                    }
                }
            }
            logger.info("==========附件添加完成=========");
        }catch (Exception e){
            logger.error("新增附件失败",e.getMessage());
            throw new BzException(e.getMessage());
        }
    }





    /**
    * updateVehicleFour(定时刷新车辆四级码值)
    * @param
    * @return
    * void
    * @author: YixinCapital -- libochen
    * 2019/9/17 9:49
    */
    public String updateVehicleFour() throws BzException{
        logger.info("==========定时刷新车辆四级码值开始=========");
        String errMsg = "true";
        try {
            QueryWrapper queryVehicleWrapper = new QueryWrapper();
            queryVehicleWrapper.eq("IS_DELETED", false);
            queryVehicleWrapper.eq("is_update", false);
            List<SgVehicleInfo> sgVehicleInfos =  sgVehicleInfoMapper.selectList(queryVehicleWrapper);
            logger.info("==========updateVehicleFour车辆数据为：{}",JSONObject.toJSON(sgVehicleInfos));
            logger.info("==========updateVehicleFour-----条数为：{}", JSONObject.toJSON(sgVehicleInfos.size()));
            if (CollectionUtils.isNotEmpty(sgVehicleInfos)) {
                for (int i = 0; i < sgVehicleInfos.size(); i++) {
                    SgVehicleInfo ve = sgVehicleInfos.get(i);
                    String model = ve.getModel();
                    Map<String, String> map = new HashMap<>();
                    map.put("styleId", model);
                    logger.info("第四级数据详情getVehicle4DetailInfoForTB,入参:{}", JSONObject.toJSON(map));
                    String c = RestUtil.sendRequest(carStyleUrl + "/carInfo/getVehicle4DetailInfoForTB", map);
                    logger.info("接口返回数据为:{}", JSONObject.toJSON(c));
                    InvokeResult<CatTypeDTO> result = JSON.parseObject(c, new TypeReference<InvokeResult<CatTypeDTO>>(){});
                    if (!result.isSuccess()) {
                        logger.error("从车型库查询车辆四级失败，" + result.getErrorMessage());
                    }
                    CatTypeDTO catTypeDTO = (CatTypeDTO) result.getData();
                    ve.setBrand(catTypeDTO.getMasterBrandId());//主品牌
                    ve.setBrandStr(catTypeDTO.getMasterBrandName());
                    ve.setBrandModel(catTypeDTO.getMakeId());//子品牌
                    ve.setBrandModelStr(catTypeDTO.getMakeName());
                    ve.setVehicleClass(catTypeDTO.getModelId());//车型
                    ve.setVehicleClassStr(catTypeDTO.getModelName());
                    ve.setModelStr(catTypeDTO.getName() + " " + catTypeDTO.getYear() + "款");
                    ve.setIsUpdate(true);
                    sgVehicleInfoMapper.updateById(ve);
                }
            }

        }catch (Exception e) {
            logger.info("调用第4级数据详情失败:",e.getMessage());
        }
        return errMsg;
    }

}
