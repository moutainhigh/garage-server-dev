package com.yixin.garage.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;



/**
 * 
 * @ClassName: CurrentEnum
 * @Description 币种枚举
 * @author  YixinCapital -- yangfei02	   
 * @date  2018年12月29日 下午4:08:36
 *
 */
public enum CurrentEnum {
    CNY("CNY","人民币"),
    USD("USD","美元"),
    HKD("HKD","港元");
	
	CurrentEnum(String code,String name){
		this.statusCode = code;
		this.statusName = name;
	}
	
	private String statusCode;
	private String statusName;
	private static List<Map<String,String>> list;
	
	public String getStatusCode() {
		return statusCode;
	}
	public String getStatusName() {
		return statusName;
	}
    
    /**
	 * 
	 * @Description: 根据当前状态获取对应的枚举
	 * @param code
	 * @return LoanRepayStatusEnum
	 * @throws 
	 * @author YixinCapital -- yangfei02
	 *	       2019年1月2日 上午11:41:19
	 */
	public static CurrentEnum getEnumByCode(String code){

        if(StringUtils.isEmpty(code)){
            return null;
        }
        CurrentEnum[] enums = CurrentEnum.values();
        for (CurrentEnum temp : enums) {
            if(code.equals(temp.getStatusCode())){
                return temp;
            }
        }
        return null;
	}
	
	/**
	 * 
	 * @Description: 根据code查询资金计划名称
	 * @param value
	 * @return String
	 * @throws 
	 * @author YixinCapital -- mjj
	 *	       2019年1月8日 下午3:32:36
	 */
    public static String getName(String value) {
        if (StringUtils.isNotBlank(value)) {
            for (CurrentEnum fundStatusEnum : CurrentEnum.values()) {
                if (fundStatusEnum.getStatusCode().equals(value)) {
                    return fundStatusEnum.getStatusName();
                }
            }
        }
        return null;
    }
    
    /**
     * 
     * @Description: 查询列表 
     * @return List<Map<String,String>>
     * @throws 
     * @author YixinCapital -- mjj
     *	       2019年1月9日 上午11:33:34
     */
    public static List<Map<String,String>> mapList(){
        if(list == null ){
            list = new ArrayList<>();
            for(CurrentEnum fundStatus: CurrentEnum.values()){
                Map<String,String> map = new HashMap<>();
                map.put("code", fundStatus.getStatusCode());
                map.put("name", fundStatus.getStatusName());
                list.add(map);
            }
        }
        return list;
    }
}

