package com.yixin.garage.service.impl.sys;

import com.alibaba.fastjson.JSONObject;
import com.yixin.common.exception.BzException;
import com.yixin.common.utils.HttpRequestUtils;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.dto.sys.CityInfoDTO;
import com.yixin.garage.dto.sys.RegionInfoDTO;
import com.yixin.garage.service.sys.BaseDataService;
import com.yixin.garage.util.ResultUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BaseDataServiceImpl implements BaseDataService {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    //省级行政区下无城市数据的区域。港，澳，台
    private List<String> SPECIAL_AREA = Arrays.asList("香港","澳门","台湾");

    @Value("${basedata.basedataURL}")
    private String basedataURL;

    @Value("${basedata.getProvincesUrl}")
    private String GET_PROVINCES_URL;

    @Value("${basedata.getSubRegionsUrl}")
    private String GET_SUBREGIONS_URL;


//    @Value("${basedataURL:http://192.168.145.99:8081/basedata/api}")
//    private String basedataURL;
//    /**
//     * getProvincesUrl=/basicRegion/getProvinces
//     * getSubRegionsUrl=/basicRegion/getSubRegions
//     */
//    private final String GET_PROVINCES_URL = "/basicRegion/getProvinces";
//    private final String GET_SUBREGIONS_URL = "/basicRegion/getSubRegions";

//    @Autowired
//    IBaseRegionInfoService regionInfoService;

    @Override
    public InvokeResult<List<RegionInfoDTO>> listProvinces() {
        //循环组装前台数据
        List<RegionInfoDTO> resList;
        String json_result = null;
        logger.info("调用获取所有省份接口getProvinces");
        JSONObject result = null;
        try {
            json_result = HttpRequestUtils.httpPost(basedataURL+GET_PROVINCES_URL,null);
            result = JSONObject.parseObject(json_result);
            if(!result.getBoolean("success")){
                return ResultUtil.error("从基础平台查询省份数据失败，"+result.getString("errorMessage"));
            }
        } catch (Exception e) {
            logger.info("调用获取所有省份接口getProvinces失败:",e.getMessage());
        }
        if(StringUtils.isEmpty(json_result)) {
            return ResultUtil.success(Collections.EMPTY_LIST);
        }

        resList = JSONObject.parseArray(result.get("data").toString(),RegionInfoDTO.class);
        if(CollectionUtils.isEmpty(resList)){
            logger.warn("调基础接口查询省份数据为空====");
            return ResultUtil.success(Collections.EMPTY_LIST);
        }
        resList = resList.stream().filter(one->"1".equals(one.getLevel())).collect(Collectors.toList());
        return ResultUtil.success(resList);
    }

    @Override
    public InvokeResult<List<RegionInfoDTO>> listCities(String provinceId) {
        String json_result = null;
        logger.info("调用获取所有省份下的城市接口getProvinces开始入参：{}",provinceId);
        JSONObject result = null;
        try {
            json_result = HttpRequestUtils.httpPost(basedataURL+GET_SUBREGIONS_URL,provinceId);
            result = JSONObject.parseObject(json_result);
            if(!result.getBoolean("success")){
                return ResultUtil.error("从基础平台查询省份数据失败，"+result.getString("errorMessage"));
            }
        } catch (Exception e) {
            logger.info("调用获取所有省份下的城市接口getSubRegions失败:",e.getMessage());
        }

        //循环组装前台数据
        List<RegionInfoDTO> resList = JSONObject.parseArray(result.get("data").toString(),RegionInfoDTO.class);
        if(CollectionUtils.isEmpty(resList)){
            logger.warn("调基础接口查询省份数据为空====");
            return ResultUtil.success(Collections.EMPTY_LIST);
        }
        logger.info("调用获取所有省份下的城市接口getSubRegions完成：{}条数据内容",resList.size());
        return ResultUtil.success(resList);
    }

    /**
     * 根据省市名称获取code并且验证省份城市是否匹配
     * @param cityInfoList
     */
    @Override
    public void validateCityInfo(List<CityInfoDTO> cityInfoList) {
        InvokeResult<List<RegionInfoDTO>> proRes = listProvinces();
        List<RegionInfoDTO> proList = (List<RegionInfoDTO>) proRes.getData();
        if(CollectionUtils.isEmpty(proList)){
            throw new BzException("validateCityInfo 从基础平台查询省份数据异常！");
        }
        Map<String,String> proNameCodeMap = new HashMap<>();
        Set<String> cachedProvince = new HashSet<>();
        for(RegionInfoDTO one:proList){
            proNameCodeMap.put(one.getName(),one.getRegionId());
        }
        Map<String,String> cityNameCodeMap = new HashMap<>(500);
        Map<String,String> cityProMap = new HashMap<>(500);
        for(CityInfoDTO one:cityInfoList){
            String proName = one.getProvinceName();
            String proCode = proNameCodeMap.get(proName);
            if(proCode==null){
                logger.warn("validateCityInfo》》》当前省份:{}与基础平台数据不匹配",proName);
                one.setSuccess(false);
                one.setErrMsg("省份数据与基础平台数据不匹配");
                continue;
            }
            if(!cachedProvince.contains(proName)){
                InvokeResult<List<RegionInfoDTO>> cityRes = listCities(proCode);
                List<RegionInfoDTO> cityList = (List<RegionInfoDTO>) cityRes.getData();
                if(CollectionUtils.isEmpty(cityList)){
                    //如果是港澳台则只设置省份code 之后直接返回 不再处理城市字段
                    if(SPECIAL_AREA.contains(proName)){
                        one.setProvinceCode(proCode);
                        continue;
                    }
                    throw new BzException("当前省份["+proName+"]code:"+proCode+"未查到对应的城市数据列表");
                }
                cachedProvince.add(proName);
                for(RegionInfoDTO city:cityList){
                    cityNameCodeMap.put(city.getName(),city.getRegionId());
                    cityProMap.put(city.getName(),proName);
                }
            }
            String cityName = one.getCityName();
            String tmpPro = cityProMap.get(cityName);
            if(!proName.equals(tmpPro)){//校验城市是否在当前省份下存在
                logger.warn("validateCityInfo》》》当前城市：{}在省份：{}下不存在！",cityName,proName);
                one.setSuccess(false);
                one.setErrMsg("城市与省份数据不匹配");
                continue;
            }
            one.setCityCode(cityNameCodeMap.get(cityName));
            one.setProvinceCode(proCode);
        }

    }

    /**
     * 刷新基础数据
     * @return
     */
//    @Override
//    @Transactional
//    public InvokeResult<String> refreshRegionInfo() {
//        long t1 = System.currentTimeMillis();
//        try {
//            regionInfoService.removeAll();
//            InvokeResult<List<RegionInfoDTO>> proRes = listProvinces();
//            if(proRes.isHasErrors()){
//                return ResultUtil.error(proRes.getErrorMessage());
//            }
//            List<RegionInfoDTO> proList = (List<RegionInfoDTO>) proRes.getData();
//            List<BaseRegionInfo> baseList = new ArrayList<>(500);
//            for(RegionInfoDTO one:proList){
//                String proCode = one.getRegionId();
//                InvokeResult<List<RegionInfoDTO>> cityRes = listCities(proCode);
//                if(cityRes.isHasErrors()){
//                    return ResultUtil.error(cityRes.getErrorMessage());
//                }
//                BaseRegionInfo proData = new BaseRegionInfo();
//                BaseAssembler.mapObj(one,proData);
//                baseList.add(proData);
//                List<RegionInfoDTO> cityList = (List<RegionInfoDTO>) cityRes.getData();
//                for(RegionInfoDTO city:cityList){
//                    BaseRegionInfo cityData = new BaseRegionInfo();
//                    BaseAssembler.mapObj(city,cityData);
//                    baseList.add(cityData);
//                }
//            }
//            regionInfoService.saveBatch(baseList);
//        }finally {
//            long t2 = System.currentTimeMillis();
//            logger.info("刷新基础数据耗时:{}毫秒",t2-t1);
//        }
//        return ResultUtil.success("刷新数据成功");
//    }
}
