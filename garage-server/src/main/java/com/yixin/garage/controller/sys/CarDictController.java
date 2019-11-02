package com.yixin.garage.controller.sys;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.dto.vehicle.SgVehicleInfoDTO;
import com.yixin.garage.util.RestUtil;
import com.yixin.garage.util.ResultUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/carDict")
public class CarDictController {

//	@Value("${basedataURL}")
//	private String basedataURL;

    @Value("${carStyleUrl}")
    private String carStyleUrl;
    private static final String PARAM_PREFIX = "?checkAllData=1&entityId=vtl_180510004";
    String addIsEnableLabel(String content){
        return addIsEnableLabel(content,false);
    }

    /**
     *
     * @param content
     * @param isStyle true 车款数据处理，需要拼上年款 false 不需要做年款处理
     * @return
     */
    String addIsEnableLabel(String content,boolean isStyle){
        if(StringUtils.isBlank(content)){
            return null;
        }
        JSONObject jsonObj = JSONObject.parseObject(content);
        JSONArray jsonArr = null;
        if(jsonObj!=null){
            jsonArr = jsonObj.getJSONArray("data");
        }
        if(CollectionUtils.isNotEmpty(jsonArr)){
            String resStr = null;
            JSONArray arr = new JSONArray();
            for(Object one:jsonArr){
                if(one==null){
                    continue;
                }
                JSONObject tmp = (JSONObject)one;
                String year = tmp.getString("year");
                String name = tmp.getString("name");
                if(isStyle&&StringUtils.isNotBlank(year)){
                    name+=" "+year+"款";
                }
                if(tmp.getIntValue("isEnabled")==0){
                    name+="(停用)";
                }
                tmp.put("name",name);
                arr.add(tmp);
            }
            jsonObj.put("data",arr);
        }
        return jsonObj.toJSONString();
    }
	
	/**
	 * 查询主品牌列表   --- 查询资源车车子主品牌 主要是【目录发布功能】使用。
	 * 
	 * @return 
	 * @author YixinCapital -- lizhongxin
	 *	       2017年7月27日 下午5:05:50
	 */
	@ResponseBody
	@RequestMapping(value = "/listMasterBrand", method = RequestMethod.GET)
	public String listMasterBrand() {
		return addIsEnableLabel(RestUtil.sendRequestForGet(carStyleUrl+"/vehicleTypeFilter/allMasterBrandByFilter"+PARAM_PREFIX));
        //return RestUtil.sendRequestForGet(basedataURL+"/car/listMasterBrand");
	}
	@Deprecated
	@ResponseBody
	@RequestMapping(value = "/listResourceVehicleMasterBrand", method = RequestMethod.GET)
	public InvokeResult listResourceVehicleMasterBrand() {
		JSONObject jo = new JSONObject();
		jo.put("id", "900000000007");
		jo.put("name", "资源车车型库");
		jo.put("logoUrl", "");
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(jo);
		return ResultUtil.success(jsonArray);
//		return  RestUtil.sendRequestForGet(basedataURL+"/car/listYxMasterBrandForResourceCar");
	}
	
	/**
	 * 根据资源车主品牌查询品牌列表
	 * 
	 * @param
	 * @return 
	 * @author YixinCapital -- lizhongxin
	 *	       2017年7月27日 下午5:06:13
	 */
	@ResponseBody
	@RequestMapping(value = "/listResourceBrand", method = RequestMethod.GET)
	public String listBrand() {
        return addIsEnableLabel(RestUtil.sendRequestForGet(carStyleUrl+"/libraryComponent/allMakeForResourceCar"));
        //return RestUtil.sendRequestForGet(carStyleUrl+"/allBrand?masterId=900000000007");
        //return RestUtil.sendRequestForGet(basedataURL+"/car/listBrand/900000000007");
	}
	
	/**
	 * 根据主品牌查询品牌列表   ---查询资源车车子子品牌
	 * @param masterId
	 * @return 
	 * @author YixinCapital -- lizhongxin
	 *	       2017年7月27日 下午5:06:13
	 */
	@ResponseBody
	@RequestMapping(value = "/listBrand/{masterId}", method = RequestMethod.GET)
	public String listBrand(@PathVariable(value = "masterId") Long masterId) {
        String param = PARAM_PREFIX+"&masterBrandId="+masterId;
        return addIsEnableLabel(RestUtil.sendRequestForGet(carStyleUrl+"/vehicleTypeFilter/allMakeByFilter"+param));
		//return RestUtil.sendRequestForGet(basedataURL+"/car/listBrand/"+ masterId);
	}
    /**
     * 根据品牌查询车型列表   ---查询资源车车型
     * @param brandId 品牌ID
     *
     */
    @ResponseBody
    @RequestMapping(value = "/listModel/{brandId}", method = RequestMethod.GET)
    public String listModel(@PathVariable(value = "brandId") Long brandId) {
        String param = PARAM_PREFIX+"&makeId="+brandId;
        return addIsEnableLabel(RestUtil.sendRequestForGet(carStyleUrl+"/vehicleTypeFilter/allModelByFilter"+param));
        //return RestUtil.sendRequestForGet(basedataURL + "/car/listModel/" + brandId);
    }
    /**
     * 根据车型查询车系列表   ---查询资源车车款
     * @param modelId  车型ID
     */
    @ResponseBody
    @RequestMapping(value = "/listStyle/{modelId}", method = RequestMethod.GET)
    public String listStyle(@PathVariable(value = "modelId") Long modelId) {
        String param = PARAM_PREFIX+"&modelId="+modelId;
        return addIsEnableLabel(RestUtil.sendRequestForGet(carStyleUrl+"/vehicleTypeFilter/allStyleByFilter"+param),true);
        //String param = "?modelId=" + modelId + "&standard=false" ;
        //return RestUtil.sendRequestForGet(basedataURL + "/car/style" + param);
    }

    /**
     * 查询主品牌列表   ---用于车子全量查询（包含资源车与非资源车）  【主要是目录外预测功能使用】
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listMasterBrand2", method = RequestMethod.GET)
    public String listMasterBrand2() {
        return addIsEnableLabel(RestUtil.sendRequestForGet(carStyleUrl+"/libraryComponent/allMasterBrand"));
        //return RestUtil.sendRequestForGet(basedataURL+"/car/listMasterBrand");
    }

    /**
     * 根据主品牌查询品牌列表   ---用于车子全量查询（包含资源车与非资源车）  【主要是目录外预测功能使用】
     * @param masterId
     * @return
     * @author YixinCapital -- lizhongxin
     *	       2017年7月27日 下午5:06:13
     */
    @ResponseBody
    @RequestMapping(value = "/listBrand2/{masterId}", method = RequestMethod.GET)
    public String listBrand2(@PathVariable(value = "masterId") Long masterId) {
        return addIsEnableLabel(RestUtil.sendRequestForGet(carStyleUrl+"/libraryComponent/allBrand?masterId="+masterId));
        //return RestUtil.sendRequestForGet(basedataURL+"/car/listBrand/"+ masterId);
    }
	/**
     * 根据品牌查询车型列表   ---用于车子全量查询（包含资源车与非资源车） 【主要是目录外预测功能使用】
     * @param brandId 品牌ID
     *
     */
	@ResponseBody
	@RequestMapping(value = "/listModel2/{brandId}", method = RequestMethod.GET)
	public String listModel2(@PathVariable(value = "brandId") Long brandId) {
        return addIsEnableLabel(RestUtil.sendRequestForGet(carStyleUrl+"/libraryComponent/allModel?brandId="+brandId));
		//return RestUtil.sendRequestForGet(basedataURL + "/car/listModel/" + brandId);
	}
	/**
	 * 根据车型查询车系列表   ---用于车子全量查询（包含资源车与非资源车） 【主要是目录外预测功能使用】
     * @param modelId  车型ID
	 */
	@ResponseBody
	@RequestMapping(value = "/listStyle2/{modelId}", method = RequestMethod.GET)
	public String listStyle2(@PathVariable(value = "modelId") Long modelId) {
        return addIsEnableLabel(RestUtil.sendRequestForGet(carStyleUrl+"/libraryComponent/allStyle?modelId="+modelId),true);
		//String param = "?modelId=" + modelId + "&standard=false" ;
		//return RestUtil.sendRequestForGet(basedataURL + "/car/style" + param);
	}
	/**
     * 查询主品牌列表   ---用于车子全量查询（包含资源车与非资源车）  【主要是发布车源使用 品牌不带首字母】
     * @return
     * @author YixinCapital -- liaojunwei
     */
    @ResponseBody
    @RequestMapping(value = "/listMasterBrand3", method = RequestMethod.POST)
    public String listMasterBrand3(@RequestBody SgVehicleInfoDTO vehicleInfoDTO) {
        return addIsEnableLabel(RestUtil.sendRequest(carStyleUrl+"/libraryComponent/listVehicle1",vehicleInfoDTO));
        //return RestUtil.sendRequestForGet(basedataURL+"/car/listMasterBrand");
    }
	
	/**
     * 根据主品牌查询品牌列表   ---用于车子全量查询（包含资源车与非资源车）  【主要是发布车源使用 品牌不带首字母】
     * @param
     * @return
     * @author YixinCapital -- liaojunwei
     *	       2017年7月27日 下午5:06:13
     */
    @ResponseBody
    @RequestMapping(value = "/listBrand3", method = RequestMethod.POST)
    public String listBrand3(@RequestBody SgVehicleInfoDTO vehicleInfoDTO) {
        return addIsEnableLabel(RestUtil.sendRequest(carStyleUrl+"/libraryComponent/listVehicle2",vehicleInfoDTO));
        //return RestUtil.sendRequestForGet(basedataURL+"/car/listBrand/"+ masterId);
    }
	/**
     * 根据品牌查询车型列表   ---用于车子全量查询（包含资源车与非资源车） 【主要是发布车源使用 车型不带首字母】
     * @param
     *@author YixinCapital -- liaojunwei
     */
	@ResponseBody
	@RequestMapping(value = "/listModel3", method = RequestMethod.POST)
	public String listModel3(@RequestBody SgVehicleInfoDTO vehicleInfoDTO) {
        return addIsEnableLabel(RestUtil.sendRequest(carStyleUrl+"/libraryComponent/listVehicle3",vehicleInfoDTO));
		//return RestUtil.sendRequestForGet(basedataURL + "/car/listModel/" + brandId);
	}
	/**
	 * 根据车型查询车系列表   ---用于车子全量查询（包含资源车与非资源车） 【主要是发布车源使用 品牌不带首字母】
     * @param
     * @author YixinCapital -- liaojunwei
	 */
	@ResponseBody
	@RequestMapping(value = "/listStyle3", method = RequestMethod.POST)
	public String listStyle3(@RequestBody SgVehicleInfoDTO vehicleInfoDTO) {
        return addIsEnableLabel(RestUtil.sendRequest(carStyleUrl+"/libraryComponent/listVehicle4",vehicleInfoDTO),true);
		//String param = "?modelId=" + modelId + "&standard=false" ;
		//return RestUtil.sendRequestForGet(basedataURL + "/car/style" + param);
	}
	
	
}

