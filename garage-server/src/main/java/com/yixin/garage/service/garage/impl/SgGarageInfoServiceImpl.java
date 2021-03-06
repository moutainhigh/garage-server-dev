package com.yixin.garage.service.garage.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import com.yixin.garage.dto.api.forLoan.SgGarageInfoMqDTO;
import com.yixin.garage.dto.api.forLoan.SgPersonChangeDTO;
import com.yixin.garage.dto.api.forLoan.SgPersonDTO;
import com.yixin.garage.dto.garage.SgContactToGarageTempDTO;
import com.yixin.garage.dto.garage.SgGarageInfoDTO;
import com.yixin.garage.dto.sys.AttachSourceDTO;
import com.yixin.garage.dto.sys.UserByPhoneDTO;
import com.yixin.garage.entity.AttachSource;
import com.yixin.garage.entity.garage.SgContactToGarageTemp;
import com.yixin.garage.entity.garage.SgGarageInfo;
import com.yixin.garage.enums.RcAttachmentTypeEnum;
import com.yixin.garage.enums.RoleEnum;
import com.yixin.garage.service.garage.ISgGarageInfoService;
import com.yixin.garage.service.impl.sys.AttachSourceServiceImpl;
import com.yixin.garage.service.impl.sys.UserServiceImpl;
import com.yixin.garage.util.BeanUtil;
import com.yixin.garage.util.DateUtil;
import com.yixin.garage.util.TaskNumUtil;
import com.yixin.garage.util.ValidatorUtil;

/**
 * <p>
 *  ???????????????
 * </p>
 *
 * @author libochen
 * @since 2019-08-01
 */
@Service
@Transactional
public class SgGarageInfoServiceImpl extends ServiceImpl<SgGarageInfoMapper, SgGarageInfo> implements ISgGarageInfoService {


    private final static Logger logger = LoggerFactory.getLogger(SgGarageInfoServiceImpl.class);

    /**
     * ?????????????????????
     */
    @Value("${fileServiceURL}")
    private String fileServiceURL;

    /**
     * ??????????????????????????????????????????
     */
    @Value("${fileServerUrl}")
    private String fileServerUrl;

    /**
     * ???????????????????????? ?????????
     */
    @Autowired
    private SgGarageInfoLogServiceImpl sgGarageInfoLogService;

    /**
     * ????????????????????? ?????????
     */
    @Autowired
    private AttachSourceServiceImpl attachSourceService;

    @Autowired
    private AttachSourceMapper attachSourceMapper;

    /**
     * ???????????? ????????????????????????????????????
     */
    @Autowired
    private SgContactToGarageTempServiceImpl sgContactToGarageTempService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private SgContactToGarageTempMapper sgContactToGarageTempMapper;

    @Autowired
    private SgGarageInfoMapper sgGarageInfoMapper;


    @Autowired
    UserServiceImpl userService;

    @Override
    public Page<SgGarageInfoDTO> pageQuery(SgGarageInfoDTO dto) throws BzException {
        logger.info("???????????????????????????{}", JSONObject.toJSON(dto));
        Page<SgGarageInfo> pageParam = new PageFactory<SgGarageInfo>().defaultPage(dto);
        //????????????
        //?????????????????????????????????????????????????????????????????????
        Boolean hasRoles = CurrentUser.hasRole(RoleEnum.GARAGE_MANAGE_ROLE);
        logger.info("?????????????????????" + CurrentUser.getCnName()+"???????????????????????????"+ hasRoles);
//        Boolean hasRoles = false;
        //?????????????????????????????? ???????????????????????????????????????????????????
        if (hasRoles) {
            dto.setCreatorName(CurrentUser.getCnName());
            logger.info("?????????????????????" + CurrentUser.getCnName());
        }
        IPage<SgGarageInfoDTO> pageProject = this.baseMapper.pageQuery(pageParam, dto,hasRoles);
        //List<SecurityGarageInfoDTO> resultList = pageProject.getRecords();
        return new Page<SgGarageInfoDTO>(pageProject.getCurrent(),pageProject.getSize(),pageProject.getTotal()).setRecords(pageProject.getRecords());
    }


    /**
     * createGarage(????????????)
     * @param dto
     * @return
     * com.yixin.common.utils.InvokeResult<java.lang.String>
     * @author: YixinCapital -- libochen
     * 2019/8/5 15:14
     */
    @Transactional
    @Override
    public InvokeResult<String> createGarage(SgGarageInfoDTO dto) throws BzException{
        InvokeResult<String> result = new InvokeResult<String>();
        SgGarageInfo sgGarageInfo = new SgGarageInfo();
//        SgGarageManageerMqDTO garageManageermqDTO = new SgGarageManageerMqDTO();
        List<SgPersonDTO> manageerMqDTO = new ArrayList<>();
        try {
            BeanUtil.setEmptyStrFields2Null(dto);
            logger.info("createGarage????????????{}", JSONObject.toJSON(dto));
            InvokeResult validate = ValidatorUtil.validate(dto);
            if(validate.isSuccess()){
                BaseAssembler.mapObjWithoutNull(dto, sgGarageInfo);
            }
            sgGarageInfo.setGarageStatus(dto.getGarageStatus());
            sgGarageInfo.setGarageNum(this.createGarageNum());
            sgGarageInfo.insert();

            if (dto.getAttachmentId() != null && !dto.getAttachmentId().equals("")) {
                AttachSource attachSource = new AttachSource();
                attachSource.selectById(dto.getAttachmentId());
                if (attachSource != null) {
                    attachSource.setBussId(sgGarageInfo.getId());
                    attachSource.setAttchType(RcAttachmentTypeEnum.SG_AGREEMENT.getValue());
                    attachSource.insertOrUpdate();
                }
                sgGarageInfo.setYxAttachment(attachSource.getId());
            }
            List<String> contact = new ArrayList<>();

            //?????????????????????????????????
            if (dto.getPersonList()!=null) {
                InvokeResult<Map<String,String>> personResult = checkPersonList(dto.getPersonList());
                if(personResult.isHasErrors()){
                    result.failure(personResult.getErrorMessage());
                    throw new BzException(personResult.getErrorMessage());
                }

                logger.info("createGarage()PersonList????????????{}",JSONObject.toJSONString(dto.getPersonList()));
                for (int i = 0; i < dto.getPersonList().size(); i++) {
                    SgPersonDTO tempMqDto = new SgPersonDTO();
                    SgPersonDTO personDTO = dto.getPersonList().get(i);
                    contact.add(personDTO.getName());
                    //?????????????????????????????????????????????????????????userId???????????????
                    List<SgContactToGarageTempDTO> garageTempDTOs =
                            sgContactToGarageTempMapper.queryTemp(personDTO.getName(), personDTO.getPhone());

                    SgContactToGarageTemp temp = new SgContactToGarageTemp();
                    temp.setGarageInfoId(sgGarageInfo.getId());
                    temp.setCreatorName(CurrentUser.getCnName());
                    temp.setCreatorId(CurrentUser.getUserId());
//                    temp.setContact(personDTO.getName());
                    temp.setContactTel(personDTO.getPhone());
                    //???????????????
                    if (garageTempDTOs != null && garageTempDTOs.size() > 0) {
                        String userId = garageTempDTOs.get(0).getUserId();
                        String userName = garageTempDTOs.get(0).getUserName();
                        temp.setUserId(userId);
                        temp.setUserName(userName);
                        temp.setContact(garageTempDTOs.get(0).getContact());
                        tempMqDto.setOperationExplain("2"); //1=?????????2=?????????3=????????????
                    }else{
                        String uuid = UUID.randomUUID().toString();//??????UUID????????????String??????
                        uuid = uuid.replace("-", "");//??????UUID?????????32???????????????????????????-?????????????????????????????????
                        temp.setUserId(uuid);
                        //?????????????????????????????????
                        UserByPhoneDTO sysUserDTOS = userService.getUserByPhoneNO(personDTO.getPhone());
                        logger.info("getUserByPhoneNO??????????????????{}", JSONObject.toJSON(sysUserDTOS));

                        if (null == sysUserDTOS) {
                            result.failure("??????????????????:" + personDTO.getName() + "????????????????????????????????????????????????");
                            throw new BzException("??????????????????:" + personDTO.getName() + "????????????????????????????????????????????????");
                        }

                        //????????????????????????????????????????????????
                        if(!sysUserDTOS.getCnName().equals(personDTO.getName())){
                            result.failure("????????????"+ personDTO.getPhone()+"??????????????????????????????"+ sysUserDTOS.getCnName()+",???????????????");
                            throw new BzException("????????????"+ personDTO.getPhone()+"??????????????????????????????"+ sysUserDTOS.getCnName()+",???????????????");
                        }
                        temp.setUserName(sysUserDTOS.getUsername());
                        temp.setContact(sysUserDTOS.getCnName());
                        tempMqDto.setOperationExplain("1"); //1=?????????2=?????????3=????????????
                    }
                    temp.insert();
                    //mq??????
                    tempMqDto.setPersonId(temp.getUserId());
                    tempMqDto.setName(temp.getContact());
                    tempMqDto.setPhone(temp.getContactTel());
                    tempMqDto.setUserName(temp.getUserName());
                    manageerMqDTO.add(tempMqDto);
                }
            }


            //?????????????????????????????????
            sgGarageInfo.setContact(StringUtils.strip(contact.toString(), "[]"));
            this.updateById(sgGarageInfo);
            //????????????????????????
            sgGarageInfoLogService.createGarageLog(sgGarageInfo, dto, "????????????");
            result.setData("?????????????????????");
            result.success();

            //??????????????????
            SgGarageInfoMqDTO garageInfomqDTO = new SgGarageInfoMqDTO();
            garageInfomqDTO.setId(sgGarageInfo.getId());
            garageInfomqDTO.setGarageName(sgGarageInfo.getGarageName());
            garageInfomqDTO.setGarageStatus(sgGarageInfo.getGarageStatus().toString());
            garageInfomqDTO.setManageerMqDTO(manageerMqDTO);
            logger.info("??????????????????(??????????????????)???{}", JSONObject.toJSON(garageInfomqDTO));
//            logger.info("?????????????????????????????????{}", JSONObject.toJSON(garageManageermqDTO));
            rabbitTemplate.convertAndSend("sg.loan.garage", "loanGetGarageInfoRoutingKey",JSON.toJSONString(garageInfomqDTO));

            //??????????????????????????????
//            garageManageermqDTO.setId(sgGarageInfo.getId());
//            garageManageermqDTO.setPersonList(manageerMqDTO);
//            rabbitTemplate.convertAndSend("sg.loan.garage", "loanGetGarageAdministratorRoutingKey",JSON.toJSONString(garageManageermqDTO));
//            logger.info("?????????????????????????????????{}", JSONObject.toJSON(garageManageermqDTO));

        } catch (BzException e) {
            logger.error("createGarage() failed???", e);
            throw new BzException(e.getMessage());
        }
        return result;
    }


    public String createGarageNum() {
        logger.info("??????????????????");
        //??????????????????
        SgGarageInfoDTO securityGarageInfoDTO = new SgGarageInfoDTO();
//        sgGarageInfo.setCreateTime(new Date());

        String dateString = DateUtil.dateToString(new Date(), "yyyyMMdd");
        String custNumString = "%06"+ dateString+"%";
        securityGarageInfoDTO.setGarageNum(custNumString);
        String maxGarageNum = this.baseMapper.createGarageNum(securityGarageInfoDTO);
        String nextString = TaskNumUtil.createTaskNum(null,maxGarageNum);

//        String maxContractNum = dateString + "001";
//        if (null != maxGarageNum && !"".equals(maxGarageNum)) {
//            maxContractNum = maxGarageNum;
//        }
//        String lastFiveString = StringUtils.substringAfter(maxContractNum, dateString);
//        String nextString = custNumString + String.format("%08d", NumberUtils.toInt(lastFiveString) + 1);

        return nextString;
    }





    @Override
    public InvokeResult<SgGarageInfoDTO> garageDetail(String id) {

        logger.info("garageDetail()?????????:[{}]",id);
        InvokeResult<SgGarageInfoDTO> result=new InvokeResult<SgGarageInfoDTO>();
        try{
//
//          SgGarageInfo sgGarageInfo = new SgGarageInfo();
//          sgGarageInfo.selectById(id);

            SgGarageInfo sgGarageInfo = this.getById(id);
            SgGarageInfoDTO securityGarageInfoDTO = new SgGarageInfoDTO();

            BaseAssembler.mapObj(sgGarageInfo,securityGarageInfoDTO);
            if (sgGarageInfo != null) {
                if (sgGarageInfo.getYxAttachment() != null && !"".equals(sgGarageInfo.getYxAttachment())) {

//                    AttachSource attachSource = attachSourceService.getById(sgGarageInfo.getYxAttachment());
//                    //??????id
//                    securityGarageInfoDTO.setAttachmentId(attachSource.getId());
//                    //????????????
//                    securityGarageInfoDTO.setAttachmentName(attachSource.getSourceAttchName());
                    //??????????????????
                    List<AttachSourceDTO> sourceDTOList = attachSourceMapper.queryAtts(sgGarageInfo.getId(), RcAttachmentTypeEnum.SG_AGREEMENT.getValue());

                    if (sourceDTOList != null) {
                        securityGarageInfoDTO.setGarageAttList(sourceDTOList);
                    }
                    securityGarageInfoDTO.setAttachmentId(sourceDTOList.get(0).getId());
                }
                securityGarageInfoDTO.setPersonList(sgContactToGarageTempService.selectContact(id));
            }
            result.setData(securityGarageInfoDTO);
            result.success();

        }catch (Exception e) {
            logger.error("garageDetail() failed???", e);
            result.failure("garageDetail() failed");
        }
        return result;
    }


    /**
     * getGarageInfoList(??????????????????????????????)
     * @return
     * com.yixin.common.utils.InvokeResult<java.util.List<com.yixin.garage.dto.garage.SecurityGarageInfoDTO>>
     * @author: YixinCapital -- libochen
     * 2019/8/7 15:06
     */
    @Override
    public InvokeResult<List<SgGarageInfoDTO>> getGarageInfoList() {
        InvokeResult<List<SgGarageInfoDTO>> result = new InvokeResult<List<SgGarageInfoDTO>>();
        try {
            List<String> listQuery = new ArrayList<>();
            SgGarageInfoDTO dto = new SgGarageInfoDTO();
            if(CurrentUser.hasRole(RoleEnum.GARAGE_MANAGE_ROLE)){
                listQuery = sgContactToGarageTempService.selectGarageId(CurrentUser.getCnName());
            }

            List<SgGarageInfoDTO> resultList = this.baseMapper.getGarageInfoList(listQuery,dto,false);

            result.setData(resultList);
            result.success();
        }catch (Exception e) {
            logger.error("getGarageInfoList() failed???", e);
            result.failure("getGarageInfoList() failed");
        }
        return result;
    }

    @Override
    public InvokeResult<List<SgGarageInfoDTO>> getGarageInfoAllList(SgGarageInfoDTO dto) {
        InvokeResult<List<SgGarageInfoDTO>> result = new InvokeResult<List<SgGarageInfoDTO>>();
        logger.info("garageDetail()?????????:[{}]",dto);
        try {
            List<String> listQuery = new ArrayList<>();
            List<SgGarageInfoDTO> resultList = this.baseMapper.getGarageInfoList(listQuery,dto,true);
            result.setData(resultList);
            result.success();
        }catch (Exception e) {
            logger.error("getGarageInfoList() failed???", e);
            result.failure("getGarageInfoList() failed");
        }
        return result;
    }

    /**
     * updateGarage(??????????????????)
     * @param dto
     * @return
     * com.yixin.common.utils.InvokeResult<com.yixin.garage.dto.garage.SecurityGarageInfoDTO>
     * @author: YixinCapital -- libochen
     * 2019/8/8 15:11
     */

    @Override
    public InvokeResult<String> updateGarage(SgGarageInfoDTO dto) {
        BeanUtil.setEmptyStrFields2Null(dto);
        logger.info("updateGarage:{}",JSONObject.toJSONString(dto));
        InvokeResult<String> result=new InvokeResult<String>() ;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

//        SgGarageManageerMqDTO garageManageermqDTO = new SgGarageManageerMqDTO();
        List<SgPersonDTO> manageerMqDTO = new ArrayList<>();
        try{
            logger.info("securityGarageInfoDTO.updateGarage:{}",JSONObject.toJSONString(dto));
            //??????
            SgGarageInfo sgGarageInfo = this.getById(dto.getId());
            if(sgGarageInfo == null ){
                result.failure("???????????????????????????????????????");
                return result;
            }
            logger.info("?????????????????????????????????{}",JSONObject.toJSONString(sgGarageInfo));
            //??????????????????
            sgGarageInfo.setParkingNum(dto.getParkingNum());//?????????










            //??????????????????
            List<String> contact = new ArrayList<>();
            logger.info("updateGarage()PersonList????????????{}",JSONObject.toJSONString(dto.getPersonList()));
            //????????????????????????????????????
            InvokeResult<Map<String,String>> personResult = checkPersonList(dto.getPersonList());
            if(personResult.isHasErrors()){
                result.failure(personResult.getErrorMessage());
                return result;
            }

            //?????????????????????????????????
            //???????????????????????????????????????????????????
            SgPersonChangeDTO changeDTO = handelPersonList(dto.getPersonList(), sgGarageInfo);
            Map<String, String> diffMap = changeDTO.getDiffMap();
            if (null!=diffMap && !diffMap.isEmpty()) {
                for(Map.Entry<String, String> entry : diffMap.entrySet()){
                    String name = entry.getKey();
                    String phone = entry.getValue();
                    QueryWrapper needDeleteWrapper = new QueryWrapper();
                    needDeleteWrapper.eq("IS_DELETED", 0);
                    needDeleteWrapper.eq("contact", name);
                    needDeleteWrapper.eq("contact_tel", phone);
                    needDeleteWrapper.eq("garage_info_id", dto.getId());
                    List<SgContactToGarageTemp> sgContactToGarageTemps =
                            sgContactToGarageTempMapper.selectList(needDeleteWrapper);
                    if (null != sgContactToGarageTemps && sgContactToGarageTemps.size() > 0) {
                        SgContactToGarageTemp temp = sgContactToGarageTemps.get(0);
                        temp.setDeleted(true);
                        temp.setGarageInfoId(null);
                        sgContactToGarageTempMapper.updateById(temp);
                    }else{
                        result.failure("???????????????????????????????????????????????????????????????");
                        return result;
                    }
                }
            }

            for (int i = 0; i < dto.getPersonList().size(); i++) {
                SgPersonDTO tempMqDto = new SgPersonDTO();
                SgPersonDTO personDTO = dto.getPersonList().get(i);
                //???????????????????????????
                contact.add(personDTO.getName());
                //?????????????????????????????????????????????????????????userId???????????????
                List<SgContactToGarageTempDTO> garageTempDTOs =
                        sgContactToGarageTempMapper.queryTemp(personDTO.getName(), personDTO.getPhone());
                //???????????????,???????????????????????????userid ???????????????id  ???????????? ??????id
                if (CollectionUtils.isNotEmpty(garageTempDTOs)) {
                    boolean flag = false;
                    String userId = garageTempDTOs.get(0).getUserId();
                    //????????? ?????????????????????
                    for (int j = 0; j < garageTempDTOs.size(); j++) {
                        if (null == garageTempDTOs.get(j).getGarageInfoId()) {
                            continue;
                        }
                        //????????????????????????????????????????????????????????????
                        if (garageTempDTOs.get(j).getGarageInfoId().equals(sgGarageInfo.getId())) {
                            flag = true;
                        }
                    }
                    //?????????????????????????????????
                    //?????????????????????????????????
                    if(!flag){
                        SgContactToGarageTemp temp = new SgContactToGarageTemp();
//                            temp.setContact(personDTO.getName());
                        temp.setContactTel(personDTO.getPhone());
                        temp.setGarageInfoId(sgGarageInfo.getId());
                        temp.setCreatorName(CurrentUser.getCnName());
                        temp.setCreatorId(CurrentUser.getUserId());
                        temp.setUserId(userId);
                        temp.setContact(garageTempDTOs.get(0).getContact());
                        temp.setUserName(garageTempDTOs.get(0).getUserName());
                        temp.insert();
                    }
                    tempMqDto.setPersonId(userId);
                    tempMqDto.setName(dto.getPersonList().get(i).getName());
                    tempMqDto.setPhone(dto.getPersonList().get(i).getPhone());
                    tempMqDto.setUserName(garageTempDTOs.get(0).getUserName());
//                    tempMqDto.setOperationExplain("2"); //1=?????????2=?????????3=????????????
                }
                else{
                    //???????????????????????? ?????? userid username ????????????id ??????????????????
                    SgContactToGarageTemp temp = new SgContactToGarageTemp();
                    temp.setContactTel(personDTO.getPhone());
                    temp.setGarageInfoId(sgGarageInfo.getId());
                    temp.setCreatorName(CurrentUser.getCnName());
                    temp.setCreatorId(CurrentUser.getUserId());
                    //??????????????????uuid
                    String uuid = UUID.randomUUID().toString();//??????UUID????????????String??????
                    uuid = uuid.replace("-", "");//??????UUID?????????32???????????????????????????-?????????????????????????????????
                    temp.setUserId(uuid);
                    //?????????????????????????????????
                    UserByPhoneDTO sysUserDTOS = userService.getUserByPhoneNO(personDTO.getPhone());
                    if (null == sysUserDTOS) {
                        result.failure("??????????????????:" + personDTO.getName() + "????????????????????????????????????????????????");
                        return result;
                    }

                    //????????????????????????????????????????????????
                    if(!sysUserDTOS.getCnName().equals(personDTO.getName())){
                        result.failure("????????????"+ personDTO.getPhone()+"??????????????????????????????"+ sysUserDTOS.getCnName()+",???????????????");
                        return result;
                    }
                    temp.setUserName(sysUserDTOS.getUsername());
                    temp.setContact(sysUserDTOS.getCnName());
                    temp.insert();
                    //mq??????
                    tempMqDto.setPersonId(temp.getUserId());
                    tempMqDto.setName(temp.getContact());
                    tempMqDto.setPhone(temp.getContactTel());
                    tempMqDto.setUserName(sysUserDTOS.getUsername());
//                    tempMqDto.setOperationExplain("1"); //1=?????????2=?????????3=????????????
                }
                manageerMqDTO.add(tempMqDto);
            }

            //????????????
            saveUpdateGarage(sgGarageInfo, dto);
            //??????????????????
            handelAtt(sgGarageInfo, dto);

            //?????????????????????????????????
            sgGarageInfo.setContact(StringUtils.strip(contact.toString(), "[]"));
            sgGarageInfoMapper.updateById(sgGarageInfo);

            //???????????????????????????????????????????????????
            SgGarageInfoMqDTO garageInfomqDTO = new SgGarageInfoMqDTO();
//            garageManageermqDTO.setId(sgGarageInfo.getId());
//            garageManageermqDTO.setPersonList(manageerMqDTO);
            garageInfomqDTO.setId(sgGarageInfo.getId());
            garageInfomqDTO.setGarageName(sgGarageInfo.getGarageName());
            garageInfomqDTO.setGarageStatus(sgGarageInfo.getGarageStatus().toString());
            garageInfomqDTO.setManageerMqDTO(manageerMqDTO);
            logger.info("updateGarage:??????????????????(??????????????????){}",JSONObject.toJSONString(garageInfomqDTO));
            rabbitTemplate.convertAndSend("sg.loan.garage", "loanGetGarageInfoRoutingKey",JSON.toJSONString(garageInfomqDTO));


//            if(changeDTO.getFlag()){
//                //??????????????????????????????
//                garageManageermqDTO.setId(sgGarageInfo.getId());
//                garageManageermqDTO.setPersonList(manageerMqDTO);
//                logger.info("updateGarage:??????????????????????????????{}",JSONObject.toJSONString(garageManageermqDTO));
//                rabbitTemplate.convertAndSend("sg.loan.garage", "loanGetGarageAdministratorRoutingKey",JSON.toJSONString(garageManageermqDTO));
//            }
//
            // ?????? ???????????? - ????????????
            sgGarageInfoLogService.createGarageLog(sgGarageInfo, dto,"????????????");
            this.baseMapper.updateById(sgGarageInfo);
            result.setData("???????????????");
            result.success();
        } catch (Exception e) {
            logger.error("updateGarage() failed???", e);
            throw new BzException(e.getMessage());
        }
        return result;
    }


    private void handelAtt(SgGarageInfo sgGarageInfo, SgGarageInfoDTO dto) {

        //??????????????????????????????????????????????????????
        if(sgGarageInfo.getYxAttachment()==null){
            //??????????????????????????????
            if(dto.getAttachmentId() != null && !"".equals(dto.getAttachmentId())){
                sgGarageInfo.setYxAttachment(dto.getAttachmentId());
                //??????????????????
                AttachSource attachSource = attachSourceService.getById(dto.getAttachmentId());
                attachSource.setBussId(sgGarageInfo.getId());
                attachSource.setAttchType(RcAttachmentTypeEnum.SG_AGREEMENT.getValue());
                attachSource.setBussType(RcAttachmentTypeEnum.SG_AGREEMENT.getName());
                attachSourceMapper.updateById(attachSource);
            }
        }else{
            //???????????????id?????????
            //????????????????????????id????????????????????????????????????
            if(dto.getAttachmentId() != null && !"".equals(dto.getAttachmentId())) {
                AttachSource oldAttachSource = attachSourceService.getById(sgGarageInfo.getYxAttachment());
                oldAttachSource.setBussId(null);
                attachSourceMapper.updateById(oldAttachSource);
                sgGarageInfo.setYxAttachment(dto.getAttachmentId());
                //??????????????????
                AttachSource attachSource = attachSourceService.getById(dto.getAttachmentId());
                attachSource.setBussId(sgGarageInfo.getId());
                attachSource.setAttchType(RcAttachmentTypeEnum.SG_AGREEMENT.getValue());
                attachSource.setBussType(RcAttachmentTypeEnum.SG_AGREEMENT.getName());
                attachSourceMapper.updateById(attachSource);
            }else if (dto.getAttachmentId() == null || "".equals(dto.getAttachmentId())){
                String attId = sgGarageInfo.getYxAttachment();
                sgGarageInfo.setYxAttachment(null);
                //??????????????????
                AttachSource attachSource = attachSourceService.getById(attId);
                attachSource.setBussId(null);
                attachSource.setAttchType(null);
                attachSource.setBussType(null);
                attachSourceMapper.updateById(attachSource);
            }
        }
    }

    private void saveUpdateGarage(SgGarageInfo sgGarageInfo, SgGarageInfoDTO dto) {
        sgGarageInfo.setGarageStatus(dto.getGarageStatus());
        sgGarageInfo.setRemark(dto.getRemark());
        sgGarageInfo.setGarageName(dto.getGarageName());
        sgGarageInfo.setProvince(dto.getProvince());
        sgGarageInfo.setProvinceStr(dto.getProvinceStr());
        sgGarageInfo.setGarageStatus(dto.getGarageStatus());
        sgGarageInfo.setCity(dto.getCity());
        sgGarageInfo.setCityStr(dto.getCityStr());
        sgGarageInfo.setCountry(dto.getCountry());
        sgGarageInfo.setCountryStr(dto.getCountryStr());
        sgGarageInfo.setGarageAddresss(dto.getGarageAddresss());
        //?????????????????????????????????
        sgGarageInfo.setParkingcostDay(dto.getParkingCostDay());
        sgGarageInfo.setParkingcostHour(dto.getParkingCostHour());
        sgGarageInfo.setParkingcostMonth(dto.getParkingCostMonth());
        sgGarageInfo.setInvoiceType(dto.getInvoiceType());
        sgGarageInfo.setTenancyBeginDate(dto.getTenancyBeginDate());
        sgGarageInfo.setTenancyEndDate(dto.getTenancyEndDate());
    }

    private InvokeResult<Map<String,String>> checkPersonList(List<SgPersonDTO> personList) {
        InvokeResult<Map<String, String>> result = new InvokeResult<Map<String, String>>();
        Map<String, String> map = new HashMap<>();
        for (SgPersonDTO dto : personList) {
            map.put(dto.getName(), dto.getPhone());
        }
        if(map.size() < personList.size()){
            result.failure("????????????????????????????????????");
            return result;
        }
        result.setData(map);
        result.success();
        return result;
    }


    private SgPersonChangeDTO handelPersonList(List<SgPersonDTO> newList, SgGarageInfo sgGarageInfo) {

        SgPersonChangeDTO result = new SgPersonChangeDTO();
        result.setFlag(false);
        //???????????????????????????????????????????????????????????????????????????
        //?????????????????????????????????
        List<SgPersonDTO> oldList = sgContactToGarageTempService.selectContact(sgGarageInfo.getId());

        List<String> newPersons = new ArrayList<>();
        Map<String, String> newMaps = new HashMap<>();

        Map<String, String> oldMaps = new HashMap<>();
        List<String> oldPersons = new ArrayList<>();

        Map<String, String> diffMap = new HashMap<>();

        for (int i = 0; i < newList.size(); i++) {
            newMaps.put(newList.get(i).getName(), newList.get(i).getPhone());
            newPersons.add(newList.get(i).getName());
        }
        for (int j = 0; j < oldList.size(); j++) {
            oldMaps.put(oldList.get(j).getName(), oldList.get(j).getPhone());
            oldPersons.add(oldList.get(j).getName());
        }
        List<String> diff = new ArrayList<String>();
        diff = this.getDiffrentList(oldPersons, newPersons);
        if (diff.size() > 0) {
            for (int i = 0; i < diff.size(); i++) {
                if (null != oldMaps.get(diff.get(i))) {
                    diffMap.put(diff.get(i), oldMaps.get(diff.get(i)));
                }
                result.setDiffMap(diffMap);
            }
            result.setFlag(true);
        }
        return result;
    }




    private  List<String> getDiffrentList(List<String> list1, List<String> list2) {
        long st = System.nanoTime();
        List<String> diff = new ArrayList<String>();
        List<String> maxList = list1;
        List<String> minList = list2;
        if(list2.size()>list1.size())
        {
            maxList = list2;
            minList = list1;
        }
        Map<String,Integer> map = new HashMap<String,Integer>(maxList.size());
        for (String string : maxList) {
            map.put(string, 1);
        }
        for (String string : minList) {
            if(map.get(string)!=null)
            {
                map.put(string, 2);
                continue;
            }
            diff.add(string);
        }
        for(Map.Entry<String, Integer> entry:map.entrySet())
        {
            if(entry.getValue()==1)
            {
                diff.add(entry.getKey());
            }
        }
        return diff;
    }



}
