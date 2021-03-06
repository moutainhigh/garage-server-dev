package com.yixin.garage.service.inventory.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yixin.common.exception.BzException;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.core.base.BaseAssembler;
import com.yixin.garage.core.common.factory.PageFactory;
import com.yixin.garage.dao.garage.SgContactToGarageTempMapper;
import com.yixin.garage.dao.garage.SgGarageInfoMapper;
import com.yixin.garage.dao.inventory.SgInventoryHomeMapper;
import com.yixin.garage.dao.vehicle.SgVehicleInfoMapper;
import com.yixin.garage.dto.inventory.SgInventoryGrageIdsDTO;
import com.yixin.garage.dto.inventory.SgInventoryHomeDTO;
import com.yixin.garage.dto.sys.SysUserDTO;
import com.yixin.garage.entity.garage.SgContactToGarageTemp;
import com.yixin.garage.entity.garage.SgGarageInfo;
import com.yixin.garage.entity.inventory.SgInventoryApprovalBill;
import com.yixin.garage.entity.inventory.SgInventoryApprovalDetail;
import com.yixin.garage.entity.inventory.SgInventoryHome;
import com.yixin.garage.entity.vehicle.SgVehicleInfo;
import com.yixin.garage.enums.RoleEnum;
import com.yixin.garage.enums.garage.GarageStatusEnum;
import com.yixin.garage.enums.garage.SgVehicleStatusEnum;
import com.yixin.garage.enums.garage.inventory.InventoryApprovalStateEnum;
import com.yixin.garage.enums.garage.inventory.InventoryBillStateEnum;
import com.yixin.garage.enums.garage.inventory.InventoryHomeStateEnum;
import com.yixin.garage.service.inventory.ISgInventoryHomeService;
import com.yixin.garage.service.sys.IUserService;
import com.yixin.garage.util.BeanUtil;
import com.yixin.garage.util.DateUtil;
import com.yixin.garage.util.TaskNumUtil;
import com.yixin.garage.util.ValidatorUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 *  ???????????????
 * </p>
 *
 * @author libochen
 * @since 2019-10-11
 */
@Service
@Transactional
public class SgInventoryHomeServiceImpl extends ServiceImpl<SgInventoryHomeMapper, SgInventoryHome> implements ISgInventoryHomeService {

    private Logger logger = LoggerFactory.getLogger(SgInventoryHomeServiceImpl.class);

    @Autowired
    private SgVehicleInfoMapper sgVehicleInfoMapper;

    @Autowired
    private SgGarageInfoMapper sgGarageInfoMapper;

    @Autowired
    private IUserService userService;

    @Autowired
    private SgContactToGarageTempMapper sgContactToGarageTempMapper;

    /**
    * createInventory(??????????????????)
    * @param dto
    * @return
    * java.lang.String
    * @author: YixinCapital -- libochen
    * 2019/10/11 16:09
    */
    @Override
    public String createInventory(SgInventoryHomeDTO dto) {
        String errMsg = null;
        logger.info("????????????????????????start?????????------------");
        logger.info("SgInventoryHomeServiceImpl.createInventory???????????????:{}",JSONObject.toJSONString(dto));
        try {
            SgInventoryHome sgInventoryHome = new SgInventoryHome();
            BeanUtil.setEmptyStrFields2Null(dto);
            logger.info("createInventory????????????{}", JSONObject.toJSON(dto));
            InvokeResult validate = ValidatorUtil.validate(dto);
            if(validate.isSuccess()){
                BaseAssembler.mapObjWithoutNull(dto, sgInventoryHome);
            }
            sgInventoryHome.setBillNum(this.createBillNum());
            sgInventoryHome.setState(InventoryHomeStateEnum.PENDING.getValue());
            sgInventoryHome.setAppoint(dto.getAppoint());
            sgInventoryHome.setId(null);
            if (CollectionUtils.isNotEmpty(dto.getGarages())) {
                sgInventoryHome.setGarageIds(JSONObject.toJSONString(dto.getGarages()));
            }
            sgInventoryHome.insert();
        } catch (Exception e) {
            logger.error("createInventory() failed");
            throw new BzException(e.getMessage(), e);
        }
        return errMsg;
    }


    @Override
    public String updateInventory(SgInventoryHomeDTO dto) {
        String errMsg = null;
        logger.info("????????????????????????start?????????------------");
        logger.info("SgInventoryHomeServiceImpl.updateInventory???????????????:{}",JSONObject.toJSONString(dto));
        try {
            SgInventoryHome sgInventoryHome = this.baseMapper.selectById(dto.getId());
            logger.info("SgInventoryHomeServiceImpl.updateInventory????????????:{}",JSONObject.toJSONString(sgInventoryHome));
            BeanUtil.setEmptyStrFields2Null(dto);
            logger.info("updateInventory????????????{}", JSONObject.toJSON(dto));
            InvokeResult validate = ValidatorUtil.validate(dto);
            if(validate.isSuccess()){
                sgInventoryHome.setAppoint(dto.getAppoint());
                if(dto.getAppoint().equals("1")){
                    sgInventoryHome.setGarageIds(JSONObject.toJSONString(dto.getGarages()));
                }else{
                    sgInventoryHome.setGarageIds(null);
                }
                sgInventoryHome.setStartTime(dto.getStartTime());
                sgInventoryHome.setEndTime(dto.getEndTime());
            }
            sgInventoryHome.insertOrUpdate();
        } catch (Exception e) {
            logger.error("updateInventory() failed");
            throw new BzException(e.getMessage(), e);
        }
        return errMsg;
    }


    @Override
    public Page<SgInventoryHomeDTO> pageQuery(SgInventoryHomeDTO dto) throws BzException {
        logger.info("???????????????????????????????????????{}", JSONObject.toJSON(dto));
        Page<SgInventoryHome> pageParam = new PageFactory<SgInventoryHome>().defaultPage(dto);
        IPage<SgInventoryHomeDTO> pageProject = this.baseMapper.pageQuery(pageParam, dto);
        return new Page<SgInventoryHomeDTO>(pageProject.getCurrent(),pageProject.getSize(),pageProject.getTotal()).setRecords(pageProject.getRecords());
    }

    @Override
    public SgInventoryHomeDTO getHomeView(SgInventoryHomeDTO dto) throws BzException {
        SgInventoryHomeDTO resultDto = new SgInventoryHomeDTO();
        logger.info("???????????????????????????{}", JSONObject.toJSON(dto));
        SgInventoryHome home = this.baseMapper.selectById(dto.getId());
        BaseAssembler.mapObjWithoutNull(home, resultDto);
        List<SgInventoryGrageIdsDTO> garages = JSONObject.parseObject(resultDto.getGarageIds() , ArrayList.class);
        resultDto.setGarages(garages);
        return resultDto;
    }

    @Override
    public String checkData(SgInventoryHomeDTO dto) {
        String errMsg = null;
        try {
            SgInventoryHome sgInventoryHome = this.getById(dto.getId());
            //??????????????????????????????????????????
            if (sgInventoryHome.getState() == InventoryHomeStateEnum.PENDING.getValue()) {
                QueryWrapper<SgInventoryHome> queryWrapper = new QueryWrapper();
                queryWrapper.eq("state", InventoryHomeStateEnum.SUCCESS.getValue());
                queryWrapper.eq("IS_DELETED", false);
                List<SgInventoryHome> sgInventoryHomes = this.baseMapper.selectList(queryWrapper);
                //???????????????????????????
                if (CollectionUtils.isNotEmpty(sgInventoryHomes)) {
                    errMsg = "?????????????????????????????????????????????????????????????????????";
                }
            }else {
                errMsg = "????????????????????????????????????????????????????????????";
            }
        }catch (Exception e) {
            logger.error("checkData() failed", e);
            throw new BzException(e.getMessage());
        }
        return errMsg;
    }

    /**
    * issue(??????????????????)
    * @param dto
    * @return
    * java.lang.String
    * @author: YixinCapital -- libochen
    * 2019/10/12 10:10
    */
    @Override
    public String issue(SgInventoryHomeDTO dto) {
        String errMsg = null;
        try {
            SgInventoryHome sgInventoryHome = this.getById(dto.getId());
            //???????????????????????????????????????
            sgInventoryHome.setState(InventoryHomeStateEnum.SUCCESS.getValue());

            Object[] parm = new Object[3];
            parm[0] = SgVehicleStatusEnum.FREE.getValue();
            parm[1] = SgVehicleStatusEnum.TEMP_GARAGE_OUT.getValue();
            parm[2] = SgVehicleStatusEnum.INTRANSIT.getValue();
            //?????????????????????????????????
            if(sgInventoryHome.getAppoint().equals("1")){
                String garageInfo = sgInventoryHome.getGarageIds();
                List<SgInventoryGrageIdsDTO> garageInfoList = new ArrayList<>();
                garageInfoList = JSON.parseObject(garageInfo, new TypeReference<List<SgInventoryGrageIdsDTO>>() {
                });
                //?????????????????????
                for (int i = 0; i < garageInfoList.size(); i++) {
                    QueryWrapper vehicleQueryWrapper = new QueryWrapper();
                    vehicleQueryWrapper.eq("IS_DELETED", 0);
                    vehicleQueryWrapper.eq("sgGarageInfo_Id", garageInfoList.get(i).getId());
                    vehicleQueryWrapper.in("stat", parm);
                    List<SgVehicleInfo> vehicleInfoList = sgVehicleInfoMapper.selectList(vehicleQueryWrapper);
                    SgInventoryApprovalBill bill = new SgInventoryApprovalBill();
                    bill.setApprovalState(InventoryBillStateEnum.UN_COMMIT.getValue());
                    bill.setGarageInfoId(garageInfoList.get(i).getId());
                    bill.setGarageAddresss(garageInfoList.get(i).getName());
                    bill.setInventoryHomeId(dto.getId());
                    bill.insert();
                    //????????????
                    SgGarageInfo sgGarageInfo = sgGarageInfoMapper.selectById(garageInfoList.get(i).getId());
                    //????????????
                    for (int j = 0; j < vehicleInfoList.size(); j++) {
                        SgInventoryApprovalDetail detail = new SgInventoryApprovalDetail();
                        SgVehicleInfo ve = vehicleInfoList.get(j);
                        detail.setActualColor(ve.getColor());
                        detail.setVin(ve.getVin());
                        detail.setAlixNum(ve.getAlixNum());
                        detail.setVehicleInfoId(ve.getId());
                        detail.setVehicleClassstr(ve.getVehicleClassStr());
                        detail.setInventoryApprovalBillId(bill.getId());
                        detail.setLicNum(ve.getLicNum());
                        detail.setManualAdd(false);//?????????????????????
                        detail.setInventory(false);
                        detail.setGarageAddresss(sgGarageInfo.getGarageAddresss());
                        detail.setActualParkAddress(sgGarageInfo.getGarageAddresss());
                        detail.setActualGarageName(sgGarageInfo.getGarageName());
                        detail.setColor(ve.getColor());
                        detail.setActualColor(ve.getColor());
                        detail.setActualStorageTime(ve.getActualStorageTime());
                        detail.setBrandStr(ve.getBrandStr());
                        detail.setApprovalStatus(InventoryApprovalStateEnum.UN_APPROVAL.getValue());
                        detail.insert();
                    }
                }
            }else{
                QueryWrapper<SgGarageInfo> garageInfoQueryWrapper = new QueryWrapper();
                garageInfoQueryWrapper.eq("IS_DELETED", false);
                garageInfoQueryWrapper.eq("garage_status", GarageStatusEnum.NORMAL.getValue());
                List<SgGarageInfo> sgGarageInfos = sgGarageInfoMapper.selectList(garageInfoQueryWrapper);

                //?????????????????????
                for (int i = 0; i < sgGarageInfos.size(); i++) {
                    QueryWrapper vehicleQueryWrapper = new QueryWrapper();
                    vehicleQueryWrapper.eq("IS_DELETED", 0);
                    vehicleQueryWrapper.eq("sgGarageInfo_Id", sgGarageInfos.get(i).getId());
                    vehicleQueryWrapper.in("stat", parm);

                    List<SgVehicleInfo> vehicleInfoList = sgVehicleInfoMapper.selectList(vehicleQueryWrapper);
                    SgInventoryApprovalBill bill = new SgInventoryApprovalBill();
                    bill.setApprovalState(InventoryBillStateEnum.UN_COMMIT.getValue());
                    bill.setGarageInfoId(sgGarageInfos.get(i).getId());
                    bill.setGarageAddresss(sgGarageInfos.get(i).getGarageName());
                    bill.setInventoryHomeId(dto.getId());
                    bill.insert();
                    //????????????
                    SgGarageInfo sgGarageInfo = sgGarageInfoMapper.selectById(sgGarageInfos.get(i).getId());
                    //????????????
                    for (int j = 0; j < vehicleInfoList.size(); j++) {
                        SgInventoryApprovalDetail detail = new SgInventoryApprovalDetail();
                        SgVehicleInfo ve = vehicleInfoList.get(j);
                        detail.setActualColor(ve.getColor());
                        detail.setVin(ve.getVin());
                        detail.setAlixNum(ve.getAlixNum());
                        detail.setVehicleInfoId(ve.getId());
                        detail.setVehicleClassstr(ve.getVehicleClassStr());
                        detail.setInventoryApprovalBillId(bill.getId());
                        detail.setLicNum(ve.getLicNum());
                        detail.setManualAdd(false);//?????????????????????
                        detail.setInventory(false);
                        detail.setGarageAddresss(sgGarageInfo.getGarageAddresss());
                        detail.setActualParkAddress(sgGarageInfo.getGarageAddresss());
                        detail.setActualGarageName(sgGarageInfo.getGarageName());
                        detail.setColor(ve.getColor());
                        detail.setActualColor(ve.getColor());
                        detail.setActualStorageTime(ve.getActualStorageTime());
                        detail.setBrandStr(ve.getBrandStr());
                        detail.setApprovalStatus(InventoryApprovalStateEnum.UN_APPROVAL.getValue());
                        detail.insert();
                    }
                }
            }

            sgInventoryHome.insertOrUpdate();
        } catch (Exception e) {
            logger.error("issue() failed", e);
            throw new BzException(e.getMessage());
        }
        return errMsg;
    }


    @Override
    public InvokeResult<List<String>> getInventoryList() {
        InvokeResult<List<String>> result = new InvokeResult<List<String>>();
        try {

            Object[] parm = new Object[2];
            parm[0] = InventoryHomeStateEnum.SUCCESS.getValue();
            parm[1] = InventoryHomeStateEnum.END.getValue();
            List<SgInventoryHome> listQuery = new ArrayList<>();
            List<String> resultBillNum = new ArrayList<>();
            QueryWrapper<SgInventoryHome> invemtoryHome = new QueryWrapper();
            invemtoryHome.eq("IS_DELETED", false);
            invemtoryHome.groupBy("id").orderByDesc("CREATE_TIME");
            invemtoryHome.in("state", parm);
            listQuery = this.baseMapper.selectList(invemtoryHome);

            if (CollectionUtils.isNotEmpty(listQuery)) {
                for (int i = 0; i < listQuery.size(); i++) {
                    resultBillNum.add(listQuery.get(i).getBillNum());
                }
            }
            result.setData(resultBillNum);
            result.success();
        }catch (Exception e) {
            logger.error("getInventoryList() failed???", e);
            result.failure("getInventoryList() failed");
        }
        return result;
    }

    public String createBillNum() {
        logger.info("??????????????????");
        //??????????????????
        SgInventoryHomeDTO sgInventoryHomeDTO = new SgInventoryHomeDTO();
        String dateString = DateUtil.dateToString(new Date(), "yyyyMMdd");
        String maxGarageNum = this.baseMapper.createBillNum(dateString);
        String nextString = TaskNumUtil.createInventoryBillNum(maxGarageNum);
        return nextString;
    }


    @Override
    public InvokeResult<List<SgInventoryGrageIdsDTO>> getGarageInfoListForApp(SgInventoryHomeDTO dto) {
        logger.info("???????????????????????????????????????{}", JSONObject.toJSON(dto));
        InvokeResult<List<SgInventoryGrageIdsDTO>> reuslt = new InvokeResult<List<SgInventoryGrageIdsDTO>>();
        List<SgInventoryGrageIdsDTO> resultDto = new ArrayList<>();
        String userName = null;
        Boolean hasRoles = false;
        Boolean isAll = false;
        //??????????????????????????????
        if (dto.getUserName() != null) {
            //app????????????
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
            logger.info("getGarageInfoListForApp()??????username??????????????????{}", JSONObject.toJSON(hasRoles));
        }

        //????????????????????????????????????????????????
        QueryWrapper<SgInventoryHome> homeQueryWrapper = new QueryWrapper<>();
        homeQueryWrapper.eq("bill_num", dto.getBillNum());
        homeQueryWrapper.eq("IS_DELETED", false);
        List<SgInventoryHome> sgInventoryHomes = this.baseMapper.selectList(homeQueryWrapper);
        String appoint =  sgInventoryHomes.get(0).getAppoint();
        if(appoint.equals("1")){
            isAll = true;
        }
        resultDto = this.baseMapper.getGarageInfoListForApp(dto, hasRoles, dto.getUserName(), isAll);

        reuslt.setData(resultDto);

        return reuslt;
    }



}
