package com.yixin.garage.service.zhongtai.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.common.exception.BzException;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.core.common.factory.PageFactory;
import com.yixin.garage.dao.order.SgGarageDetailMapper;
import com.yixin.garage.dao.zhongtai.SgSendLogToZhongtaiMapper;
import com.yixin.garage.dto.zhongtai.SgGarageSendInfoDTO;
import com.yixin.garage.entity.errorlog.SgErrorInfo;
import com.yixin.garage.entity.zhongtai.SgSendInfoToZhongtai;
import com.yixin.garage.dao.zhongtai.SgSendInfoToZhongtaiMapper;
import com.yixin.garage.entity.zhongtai.SgSendLogToZhongtai;
import com.yixin.garage.enums.garage.BusinessTypeEnum;
import com.yixin.garage.enums.garage.SystemOfTypeEnum;
import com.yixin.garage.enums.garage.VehicleTypeEnum;
import com.yixin.garage.enums.garage.zhongtai.BillStatusEnum;
import com.yixin.garage.enums.garage.zhongtai.SendTypeEnum;
import com.yixin.garage.service.zhongtai.ISgSendInfoToZhongtaiService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yixin.garage.util.RestUtil;
import com.yixin.garage.util.TaskNumUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhouhang
 * @since 2019-08-29
 */
@Service
public class SgSendInfoToZhongtaiServiceImpl extends ServiceImpl<SgSendInfoToZhongtaiMapper, SgSendInfoToZhongtai> implements ISgSendInfoToZhongtaiService {

    private final static Logger logger = LoggerFactory.getLogger(SgSendInfoToZhongtaiServiceImpl.class);

    @Autowired
    private SgSendInfoToZhongtaiMapper sgSendInfoToZhongtaiMapper;

    @Autowired
    private SgSendLogToZhongtaiMapper sgSendLogToZhongtaiMapper;

    @Autowired
    private SgGarageDetailMapper sgGarageDetailMapper;

    @Value("${sgToErpUrl}")
    private String sgToErpUrl;

    private static final String PSM_SYSTEM = "PSM_SYSTEM";

    @Override
    public IPage<SgGarageSendInfoDTO> pageQuery(SgGarageSendInfoDTO sgGarageSendInfoDTO) throws BzException {
        logger.info("分页查询中台信息列表入参：{}", JSONObject.toJSON(sgGarageSendInfoDTO));
        Page<SgGarageSendInfoDTO> dtoPage = null;
        try {
            Page<SgSendInfoToZhongtai> page = new PageFactory<SgSendInfoToZhongtai>().defaultPage(sgGarageSendInfoDTO);
            IPage<SgGarageSendInfoDTO> pageList = sgSendInfoToZhongtaiMapper.pageQuery(page, sgGarageSendInfoDTO);

            dtoPage = new Page<SgGarageSendInfoDTO>(pageList.getCurrent(), pageList.getSize(), pageList.getTotal()).setRecords(pageList.getRecords());
            logger.info("分页查询中台信息列表结束 pageQuery() end :============ ");

        }catch (Exception e){
            logger.info("SgSendInfoToZhongtaiServiceImpl pageQuery() failed：", e.getMessage());
            throw new BzException(e.getMessage());
        }
        return dtoPage;
    }

    @Override
    public IPage<SgGarageSendInfoDTO> pageQueryLog(SgGarageSendInfoDTO sgGarageSendInfoDTO) throws BzException {
        logger.info("分页查询推送中台日志信息列表入参：{}", JSONObject.toJSON(sgGarageSendInfoDTO));
        Page<SgGarageSendInfoDTO> dtoPage = null;
        try {
            Page<SgSendLogToZhongtai> page = new PageFactory<SgSendLogToZhongtai>().defaultPage(sgGarageSendInfoDTO);
            IPage<SgGarageSendInfoDTO> pageList = sgSendLogToZhongtaiMapper.pageQueryLog(page, sgGarageSendInfoDTO);

            dtoPage = new Page<SgGarageSendInfoDTO>(pageList.getCurrent(), pageList.getSize(), pageList.getTotal()).setRecords(pageList.getRecords());
            logger.info("分页查询推送中台日志信息列表结束 pageQueryLog() end :============ ");

        }catch (Exception e){
            logger.info("SgSendInfoToZhongtaiServiceImpl pageQueryLog() failed：", e.getMessage());
            throw new BzException(e.getMessage());
        }
        return dtoPage;
    }

    @Override
    public void sendInfoToZT() throws BzException {

        //1,安全车库入库
        logger.info("sgSendInfoForErpJob 开始定时任务给中台推【安全车库入库】信息");
        List<SgSendInfoToZhongtai> sgRkInfos = getNeedSendInfo(BillStatusEnum.PSM_RESTOCK_INTERFACE.getValue());
        if(!sgRkInfos.isEmpty()){
            try {
                sendByType(BillStatusEnum.PSM_RESTOCK_INTERFACE.getValue());
            } catch (Exception e) {
                logger.error("sgSendInfoForErpJob 开始定时任务给中台推【安全车库入库】信息失败"+ e.getMessage());
            }
        }
    }

    @Override
    public InvokeResult<String> sendInfoToZT(SgGarageSendInfoDTO sgGarageSendInfoDTO) throws BzException {
        InvokeResult<String> result = new InvokeResult<>();
        String billNum = sgGarageSendInfoDTO.getBillNum();
        String id = sgGarageSendInfoDTO.getId();
        int type = sgGarageSendInfoDTO.getType();
        //1,安全车库入库
        logger.info("SgSendInfoToZhongtaiServiceImpl 手动给中台推【安全车库入库】信息");
        List<Map<String,Object>> list = new ArrayList<>();
        list = getSgRkInfo(billNum);
        if(list.isEmpty()){
            return result;
        }
        if(BillStatusEnum.PSM_RESTOCK_INTERFACE.getValue() == type){//开票数据
            //将即将推送给中台的数据落地数据库
            SgSendLogToZhongtai sgSendLogToZhongtai = new SgSendLogToZhongtai();
            sgSendLogToZhongtai.setBatchNum((String)list.get(0).get("batchNum"));//批次号(唯一标识)
            sgSendLogToZhongtai.setInterfaceName(SendTypeEnum.PSM_RESTOCK_INTERFACE.getValue());//接口名称(码值)
            sgSendLogToZhongtai.setSendInfo(JSON.toJSONString(list));//发送内容
            sgSendLogToZhongtai.setLineCount(1);//发送行数
            sgSendLogToZhongtai.setCreatorName("手动推送信息");
            sgSendLogToZhongtai.setPushMode("正推");
            sgSendLogToZhongtai.insert();
        }

        logger.info("SgSendInfoToZhongtaiServiceImpl sendInfoToZT() 手动调用erp接口 参数：{}",JSONObject.toJSONString(list));
        String erpUrl = sgToErpUrl+"/api/public/impdata";
        String c = RestUtil.sendRequest(erpUrl,list);
        List<Map<String,Object>> list1 = JSON.parseObject(c, new TypeReference<List<Map<String,Object>>>(){});
        logger.info("SgSendInfoToZhongtaiServiceImpl sendInfoToZT() 手动调用erp接口结束：返回参数：---{}",c);

        //对返回结果做校验,如果错误数据将保存在异常表中
        for(int i = 0;i<list1.size();i++){
            Map<String,Object> map1 = list1.get(i);
            Object syncStatus = map1.get("syncStatus");
            if("E".equals(syncStatus.toString())){
                //保存在异常表
                createErrorForErp(map1);
                updateStatOfIfsendSuccess(id,2);
                result.failure("发送中台失败,错误码："+map1.get("errorCode")+" 错误信息："+map1.get("errorMessage"));
                return result;
            }
        }
        //发送银企成功后将单子改为已发送
        updateStatOfIfsendSuccess(id,1);
        return result;
    }

    private List<SgSendInfoToZhongtai> getNeedSendInfo(int value) {
        List<SgSendInfoToZhongtai> list = new ArrayList<>();
        list = sgSendInfoToZhongtaiMapper.pageQueryList(value);
        logger.error("sgSendInfoForErpJob 查询需要发送的数据的条数是 ====="+list.size() );
        return list;
    }

    public void sendByType(int type){
        String batchNum = TaskNumUtil.createBatchNum();
        List<Map<String, Object>> finalMap = new ArrayList<Map<String, Object>>();
        List<String> sendInfoIds = new ArrayList<String>();
        List<Map<String, Object>> lists = new ArrayList<>();

        List<SgSendInfoToZhongtai> infos = getNeedSendInfo(type);
        logger.info("sendByType() 定时任务拼装中台信息开始，类型==="+BillStatusEnum.getDisplayNameByIndex(type));
        for(SgSendInfoToZhongtai sgSendInfoToZhongtai : infos){
            if(type == BillStatusEnum.PSM_RESTOCK_INTERFACE.getValue()){//如果是集采付款
                lists = getSgRkInfo(sgSendInfoToZhongtai.getBillNum());
            }
            if(lists.isEmpty()){
                continue;
            }
            sendInfoIds.add(sgSendInfoToZhongtai.getId());
            finalMap.addAll(lists);
        }
        logger.info("sendByType() 定时任务拼装中台信息结束，类型==="+BillStatusEnum.getDisplayNameByIndex(type));
        //修改一下最终Map集合里的每一个行数和批次号
        for(Map<String, Object> map : finalMap){
            map.put("lineCount",finalMap.size()+"");
            map.put("batchNum",batchNum);
            map.put("value200",batchNum);
        }

        logger.info("SgSendInfoToZhongtaiServiceImpl sendInfoToZT() 本次发送的单号billNums："+sendInfoIds);
        logger.info("SgSendInfoToZhongtaiServiceImpl sendInfoToZT()"+BillStatusEnum.getDisplayNameByIndex(type)+" 调用erp接口 参数：{}",JSONObject.toJSONString(finalMap));
        String erpUrl = sgToErpUrl+"/api/public/impdata";
        String c = RestUtil.sendRequest(erpUrl,finalMap);
        List<Map<String,Object>> list1 = JSON.parseObject(c, new TypeReference<List<Map<String,Object>>>(){});
        logger.info("SgSendInfoToZhongtaiServiceImpl sendInfoToZT()"+BillStatusEnum.getDisplayNameByIndex(type)+" 调用erp接口结束：返回参数：---{}",c);

        //对返回结果做校验,如果错误数据将保存在异常表中
        boolean b = true;
        for(int i = 0;i<list1.size();i++){
            Map<String,Object> map1 = list1.get(i);
            Object syncStatus = map1.get("syncStatus");
            if("E".equals(syncStatus.toString())){
                //保存在异常表
                createErrorForErp(map1);
                for(String sendInfoId : sendInfoIds){
                    updateStatOfIfsendSuccess(sendInfoId,2);
                }
                b = false;
            }
        }
        //发送银企成功后将单子改为已发送
        if(b){
            for(String sendInfoId : sendInfoIds){
                updateStatOfIfsendSuccess(sendInfoId,1);
            }
            //创建发送日志
            SgSendLogToZhongtai sgSendLogToZhongtai = new SgSendLogToZhongtai();
            sgSendLogToZhongtai.setBatchNum(batchNum);//批次号(唯一标识)
            if(type == BillStatusEnum.PSM_RESTOCK_INTERFACE.getValue()){//如果是集采付款
                sgSendLogToZhongtai.setInterfaceName(SendTypeEnum.PSM_RESTOCK_INTERFACE.getValue());//接口名称(码值)
            }
            sgSendLogToZhongtai.setSendInfo(JSON.toJSONString(finalMap));//发送内容
            sgSendLogToZhongtai.setLineCount(finalMap.size());//发送行数
            sgSendLogToZhongtai.setCreatorName("定时任务调用");
            sgSendLogToZhongtai.setPushMode("正推");
            sgSendLogToZhongtaiMapper.insert(sgSendLogToZhongtai);
        }
    }

    public List<Map<String,Object>> getSgRkInfo (String billNum){
        logger.info("SgSendInfoToZhongtaiServiceImpl getSgRkInfo() 开始参数："+billNum);
        List<Map<String,Object>> results = new ArrayList<>();
        String batchNum = TaskNumUtil.createBatchNum();
        //通过alix申请编号查询入库单信息，组装推送中台参数
        Map<String,Object> map1 = sgGarageDetailMapper.getInfoListByBillNum(billNum);
        if(map1 == null || map1.isEmpty()){
            return results;
        }
        String contractNo = sgGarageDetailMapper.getContractNoByBillNum(billNum);

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("sourceSystem",PSM_SYSTEM);
        map.put("batchNum",batchNum);
        map.put("lineCount",1+"");
        map.put("interfaceName",SendTypeEnum.PSM_RESTOCK_INTERFACE.getValue());
        map.put("value1","SG_RK"+map1.get("alix_num"));
        map.put("value2",billNum);
        map.put("value3",contractNo == null ? "": contractNo);
        map.put("value4",map1.get("vin"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String value5 = map1.get("actual_storage_time") == null ? "" : sdf.format(map1.get("actual_storage_time"));
        map.put("value5",value5);
        map.put("value6",map1.get("garage_name"));
        map.put("value7",map1.get("property_right_type") == null ? "" : VehicleTypeEnum.getDisplayNameByIndex(map1.get("property_right_type").toString()));
        map.put("value8",map1.get("business_type") == null ? "" : BusinessTypeEnum.getDisplayNameByIndex(map1.get("business_type").toString()));
        map.put("value9",map1.get("brandStr"));
        map.put("value10",map1.get("brand_modelStr"));
        map.put("value11",map1.get("vehicle_classStr"));
        map.put("value12",map1.get("modelStr"));
        map.put("value13",map1.get("lic_num"));
        map.put("value14",map1.get("color"));
        map.put("value15",map1.get("mileage") == null ? "0" : map1.get("mileage")+"");
        map.put("value200",batchNum);
        results.add(map);
        logger.info("SgSendInfoToZhongtaiServiceImpl getSgRkInfo() 查询结果：{} ",JSONObject.toJSONString(results));
        return results;
    }

    private void createErrorForErp(Map<String,Object> map1) {
        logger.info("SgSendInfoToZhongtaiServiceImpl createErrorForErp() 开始将erp返回的错误每条数据保存至错误信息表中："+map1.get("uniqueCode").toString());
        //保存在异常表
        SgErrorInfo sgErrorInfo = new SgErrorInfo();
        sgErrorInfo.setSystemOfType(SystemOfTypeEnum.ERP_SYSTEM.getValue());
        sgErrorInfo.setInterfaceName(map1.get("interfaceName") == null ? "" : map1.get("interfaceName").toString());
        sgErrorInfo.setBatchNum(map1.get("batchNum") == null ? "" :map1.get("batchNum").toString());
        sgErrorInfo.setUniqueCode(map1.get("uniqueCode") == null ? "" : map1.get("uniqueCode").toString());
        sgErrorInfo.setErrorCode(map1.get("errorCode") == null ? "" : map1.get("errorCode").toString());
        sgErrorInfo.setRemark(map1.get("errorMessage") == null ? "" : map1.get("errorMessage").toString());
        sgErrorInfo.setSolveStat(0);
        sgErrorInfo.insert();
    }

    /**
     * 修改给中台发送单子信息的状态
     * @param id
     * @param stat 0：未发送  1：已发送  2：发送失败
     */
    void updateStatOfIfsendSuccess(String id,int stat){//stat 0：未发送  1：已发送  2：发送失败
        if(id != null){
            Map<String,Object> map1 = new HashMap<>();
            SgSendInfoToZhongtai sgSendInfoToZhongtai = sgSendInfoToZhongtaiMapper.selectById(id);
            sgSendInfoToZhongtai.setSendCount(sgSendInfoToZhongtai.getSendCount()+1);
            sgSendInfoToZhongtai.setStat(stat);
            sgSendInfoToZhongtai.updateById();
        }
    }
}
