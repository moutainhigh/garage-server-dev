package com.yixin.garage.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;



/**
 * 
 * @ClassName: FcastStatusEnum
 * @Description cashflow审批状态
 * @author  YixinCapital -- yangfei02	   
 * @date  2019年3月7日 下午3:59:43
 *
 */
public enum FcastStatusEnum {
    ZIJIGUANLIZONGJIAN("0","待资金管理组副总审核"),
    SHENGXIAO("9","生效");
	
	FcastStatusEnum(String code,String name){
		this.statusCode = code;
		this.statusName = name;
	}
	
	private String statusCode;
	private String statusName;
	private static List<Map<String,String>> fundStatusList;
	
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
	public static FcastStatusEnum getEnumByCode(String code){

        if(StringUtils.isEmpty(code)){
            return null;
        }
        FcastStatusEnum[] enums = FcastStatusEnum.values();
        for (FcastStatusEnum temp : enums) {
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
            for (FcastStatusEnum fundStatusEnum : FcastStatusEnum.values()) {
                if (fundStatusEnum.getStatusCode().equals(value)) {
                    return fundStatusEnum.getStatusName();
                }
            }
        }
        return null;
    }
    
    /**
     * 
     * @Description: 查询资金计划状态列表 
     * @return List<Map<String,String>>
     * @throws 
     * @author YixinCapital -- mjj
     *	       2019年1月9日 上午11:33:34
     */
    public static List<Map<String,String>> fundStatusMapList(){
        if(fundStatusList == null ){
            fundStatusList = new ArrayList<>();
            for(FcastStatusEnum fundStatus: FcastStatusEnum.values()){
                Map<String,String> map = new HashMap<>();
                map.put("code", fundStatus.getStatusCode());
                map.put("name", fundStatus.getStatusName());
                fundStatusList.add(map);
            }
        }
        return fundStatusList;
    }
}

