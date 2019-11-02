package com.yixin.garage.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @ClassName: RepayPlanRepayStatusEnum
 * @Description 还款计划支付情况枚举 
 * @author  YixinCapital -- lizhongxin	   
 * @date  2019年1月22日 上午11:13:54
 *
 */
public enum RepayPlanRepayStatusEnum {
	NOT_PAID("NOT PAID" ,"未支付"),
	PAID("PAID","已支付");
	
	private String code;
	private String name;
	
	private RepayPlanRepayStatusEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
	
	public static String getCode(String name) {
        if (StringUtils.isNotBlank(name)) {
            for (RepayPlanRepayStatusEnum typeEnum : RepayPlanRepayStatusEnum.values()) {
                if (typeEnum.getName().equals(name)) {
                    return typeEnum.getCode();
                }
            }
        }
        return null;
    }
	
	public static String getName(String code) {
        if (StringUtils.isNotBlank(code)) {
            for (RepayPlanRepayStatusEnum typeEnum : RepayPlanRepayStatusEnum.values()) {
                if (typeEnum.getCode().equals(code)) {
                    return typeEnum.getName();
                }
            }
        }
        return null;
    }
}

