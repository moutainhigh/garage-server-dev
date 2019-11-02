package com.yixin.garage.dto.sys;

import lombok.Data;

/**
 * 城市数据转换DTO
 * 此类主要用于输入 省市名 获取省市code码，并判断省市是否匹配
 */
@Data
public class CityInfoDTO extends BaseRes{
    private String provinceName;
    private String provinceCode;
    private String cityName;
    private String cityCode;

    public CityInfoDTO(int idx, String provinceName, String cityName) {
        super();
        super.setIdx(idx);
        this.provinceName = provinceName;
        this.cityName = cityName;
    }

    public CityInfoDTO() {
    }

}
