package com.yixin.garage.service.sys;

import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.dto.sys.CityInfoDTO;
import com.yixin.garage.dto.sys.RegionInfoDTO;

import java.util.List;

/**
 * 基础数据查询接口
 */
public interface BaseDataService {
    InvokeResult<List<RegionInfoDTO>> listProvinces();

    InvokeResult<List<RegionInfoDTO>> listCities(String provinceId);

    void validateCityInfo(List<CityInfoDTO> cityInfoList);

    /**
     * 刷新基础数据
     * @return
     */
//    InvokeResult<String> refreshRegionInfo();
}
