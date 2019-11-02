package com.yixin.garage.dto.sys;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 省份或者城市数据
 */
@Data
@Accessors(chain = true)
public class RegionInfoDTO {
    /**
     * if("1".equals(map.get("level").toString())) {
     * 	    		DepositProvincesSubRegions d = new DepositProvincesSubRegions();
     * 		    	d.setCode(map.get("regionId").toString());
     * 		    	d.setName(map.get("name").toString());
     * 		    	d.setSid(map.get("sid").toString());
     * 		    	listDitchNameDTO.add(d);
     *                        }
     */

    private String level;//层级 1-省 2-市
    private String regionId;//code
    private String fullName;
    private String name;//名字
    private String sid;//唯一id
    private String parentId;//父节点sid
    private int orderNumber;

    public RegionInfoDTO(String level, String regionId, String name, String sid) {
        this.level = level;
        this.regionId = regionId;
        this.name = name;
        this.sid = sid;
    }

    public RegionInfoDTO() {
    }
}
