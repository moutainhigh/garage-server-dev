package com.yixin.garage.controller.garage;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.dto.garage.SgGarageInfoDTO;
import com.yixin.garage.dto.sys.RegionInfoDTO;
import com.yixin.garage.dto.sys.YxBaseRegionDTO;
import com.yixin.garage.service.garage.ISgGarageInfoService;
import com.yixin.garage.service.impl.sys.BaseDataServiceImpl;
import com.yixin.garage.service.sys.BaseDataService;
import com.yixin.garage.util.JacksonUtil;
import com.yixin.garage.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author libochen
 * @since 2019-08-01
 */
@RestController
@RequestMapping("/sgGarageInfo")
public class SgGarageInfoController {

    private final static Logger logger=LoggerFactory.getLogger(SgGarageInfoController.class);

    @Autowired
    ISgGarageInfoService iSgGarageInfoService;

    @Autowired
    BaseDataService baseDataService;


    @RequestMapping("/pageQuery")
    @ResponseBody
    public InvokeResult<Page<SgGarageInfoDTO>> pageQuery(@RequestBody SgGarageInfoDTO dto) {
        logger.info("车库列表查询，查询参数为：[{}]",JacksonUtil.fromObjectToJson(dto));
        try {
            Page<SgGarageInfoDTO> page =  iSgGarageInfoService.pageQuery(dto);
            return ResultUtil.success(page);
        }catch (Exception e){
            logger.error("车库列表查询异常：[{}]", e.getMessage(), e);
            return ResultUtil.error(e.getMessage());
        }

    }

    @RequestMapping("/createGarage")
    @ResponseBody
    public InvokeResult<String> createGarage(@RequestBody SgGarageInfoDTO dto) {
        InvokeResult<String> result = new InvokeResult<String>();
        try {
            result = iSgGarageInfoService.createGarage(dto);
        } catch (Exception e) {
            logger.error("createGarage() failed :", e);
            result.failure(e.getMessage());
        }
        return result;
    }
    /**

    * garageDetail(车库信息详情)
    * @param dto
    * @return
    * com.yixin.common.utils.InvokeResult<com.yixin.garage.dto.garage.SecurityGarageInfoDTO>
    * @author: YixinCapital -- libochen
    * 2019/8/6 18:32
    */
    @RequestMapping("/garageDetail")
    @ResponseBody
    public InvokeResult<SgGarageInfoDTO> garageDetail(@RequestBody SgGarageInfoDTO dto) {
        InvokeResult<SgGarageInfoDTO> result = new InvokeResult<SgGarageInfoDTO>();
        try {
            result=iSgGarageInfoService.garageDetail(dto.getId());
        } catch (Exception e) {
            logger.error("garageDetail() failed :", e);
            result.failure("error");
        }
        return result;
    }

    /**
    * getListByFranId(获取当前登录人所能看见的车库信息)
    * @param
    * @return
    * com.yixin.common.utils.InvokeResult<java.util.List<com.yixin.garage.dto.garage.SecurityGarageInfoDTO>>
    * @author: YixinCapital -- libochen
    * 2019/8/7 15:01
    */
    @RequestMapping("/getGarageInfoList")
    @ResponseBody
    public InvokeResult<List<SgGarageInfoDTO>> getGarageInfoList() {
        InvokeResult<List<SgGarageInfoDTO>> result = new InvokeResult<List<SgGarageInfoDTO>>();
        try {
            result = iSgGarageInfoService.getGarageInfoList();
        } catch (Exception e) {
            logger.error("getGarageInfoList() failed：", e);
            result.failure("error");
        }
        return result;
    }



    /**
    * updateGarage(修改更新车库数据)
    * @param dto
    * @return
    * com.yixin.common.utils.InvokeResult<com.yixin.garage.dto.garage.SecurityGarageInfoDTO>
    * @author: YixinCapital -- libochen
    * 2019/8/7 20:06
    */
    @RequestMapping("/updateGarage")
    @ResponseBody
    public InvokeResult<String> updateGarage(@RequestBody SgGarageInfoDTO dto) {
        InvokeResult<String> result = new InvokeResult<String>();
        try {
            result =  iSgGarageInfoService.updateGarage(dto);
        } catch (Exception e) {
            logger.error("updateGarage() failed :", e);
            result.failure(e.getMessage());
        }
        return result;
    }



    /**
    * getProvinces(获取省份列表)
    * @param
    * @return
    * @author: YixinCapital -- libochen
    * 2019/8/27 14:24
    */
    @RequestMapping("/getProvinces")
    @ResponseBody
    public InvokeResult<List<RegionInfoDTO>> getProvinces() {
        InvokeResult<List<RegionInfoDTO>> result = new InvokeResult<List<RegionInfoDTO>>();
        try {
            result = baseDataService.listProvinces();
        } catch (Exception e) {
            logger.error("getProvinces() failed :", e);
            result.failure(e.getMessage());
        }
        return result;
    }

    /**
    * getCities(获取省份id下城市列表)
    * @param provinceId
    * @return
    * @author: YixinCapital -- libochen
    * 2019/8/27 14:25
    */
    @RequestMapping("/getCities")
    @ResponseBody
    public InvokeResult<List<RegionInfoDTO>> getCities(@RequestParam("provinceId") String provinceId) {
        InvokeResult<List<RegionInfoDTO>> result = new InvokeResult<List<RegionInfoDTO>>();
        try {
            result = baseDataService.listCities(provinceId);
        } catch (Exception e) {
            logger.error("getCities() failed :", e);
            result.failure(e.getMessage());
        }
        return result;
    }




}
