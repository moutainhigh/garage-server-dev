package com.yixin.garage.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @ClassName: PeriodTypeEnum
 * @Description 还款周期标识
 * @author  YixinCapital -- yangfei02	   
 * @date  2019年1月5日 上午11:41:06
 *
 */
public enum PeriodTypeEnum {
    DAY("0","天"),
    MONTH("1","月"),
    QUARTER("3","季度"),
    //HALF("6","半年"),
	;
    
    private static List<Map<String,String>> typeList;
    
    /**
     * 
     * @Title: finTypeMapList   
     * @Description: 查询融资类型列表 
     */
    public static List<Map<String,String>> typeMapList(){
        if(typeList == null ){
            typeList = new ArrayList<>();
            for(PeriodTypeEnum pType: PeriodTypeEnum.values()){
                Map<String,String> map = new HashMap<>();
                map.put("code", pType.getTypeCode());
                map.put("name", pType.getTypeName());
                typeList.add(map);
            }
        }
        return typeList;
    }
	
    public static String getName(String value) {
        if (StringUtils.isNotBlank(value)) {
            for (PeriodTypeEnum typeEnum : PeriodTypeEnum.values()) {
                if (typeEnum.getTypeCode().equals(value)) {
                    return typeEnum.getTypeName();
                }
            }
        }
        return null;
    }
    
    public static String getCode(String name) {
        if (StringUtils.isNotBlank(name)) {
            for (PeriodTypeEnum typeEnum : PeriodTypeEnum.values()) {
                if (typeEnum.getTypeName().equals(name)) {
                    return typeEnum.getTypeCode();
                }
            }
        }
        return null;
    }
    
	private PeriodTypeEnum(String typeCode, String typeName) {
		this.typeCode = typeCode;
		this.typeName = typeName;
	}
	private String typeCode;
	private String typeName;
	
	public String getTypeCode() {
		return typeCode;
	}
	public String getTypeName() {
		return typeName;
	}
	
	
}

