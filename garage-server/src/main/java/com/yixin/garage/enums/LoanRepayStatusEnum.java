package com.yixin.garage.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

/**
 * 
 * @ClassName: LoanRepayStatusEnum
 * @Description 老流程(作废):还款状态：新添加一条记录就是编辑中，点提交复核以后为待复核。复核通过即生效。只复核一次，以后修改走审批
 *                     修改任何信息提交以后变成待部门审批，通过则进入待资管审批，驳回回到生效；
 *                                 然后是待资管领导审批，通过进入资管领导审批，驳回回到待部门审批，且部门审批人可以修改，修改后重新待资管审批；
 *                                 然后是资管领导审批，通过即生效，驳回返回上一节点（类似上衣状态步骤）
 *              新流程:新建(修改)--大区总监审批--资金管理复核--资金管理总监审批----资金管理负责人审批--生效;
 *                   任何节点驳回都回到最初新建编辑中或上一次生效,此次审批结束;
 * @author  YixinCapital -- yangfei02	   
 * @date  2018年12月29日 下午4:08:36
 *
 */
public enum LoanRepayStatusEnum {

    BIANJI("0","编辑中"),
    /*TOFUHE("1","复核"),
    BUMENSHEN("2","融资大区总监审批"), //融资部门领导
    ZIGUANSHEN("3","资金管理部审批"), //资管人员,与复合是一个角色
    JINGLISHEN("4","资金管理部总监审批"), //资管经理
    LINGDAOSHEN("5","资金管理部负责人审批"),//资管总监
*/  
    DAQUZONGJIAN("1","大区总监审批"),//融资大区总监
    ZIJINGUANLI("2","资金管理复核"),//资管操作员
    ZIJINGUANLIZONGJIAN("3","资金管理总监审批"),//资管总监
    ZIJINGUANLIFUZEREN("4","资金管理负责人审批"),//资管负责人
    SHENGXIAO("9","生效"),
    
    BOHUI("10","驳回");
	
	LoanRepayStatusEnum(String code,String name){
		this.statusCode = code;
		this.statusName = name;
	}
	
	private String statusCode;
	private String statusName;
	private static List<Map<String,String>> statusList;
	
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
	public static LoanRepayStatusEnum getEnumByCode(String code){

        if(StringUtils.isEmpty(code)){
            return null;
        }
        LoanRepayStatusEnum[] enums = LoanRepayStatusEnum.values();
        for (LoanRepayStatusEnum temp : enums) {
            if(code.equals(temp.getStatusCode())){
                return temp;
            }
        }
        return null;
	}
	
	public static String getNameByCode(String code){

        if(StringUtils.isEmpty(code)){
            return null;
        }
        LoanRepayStatusEnum[] enums = LoanRepayStatusEnum.values();
        for (LoanRepayStatusEnum temp : enums) {
            if(code.equals(temp.getStatusCode())){
                return temp.getStatusName();
            }
        }
        return null;
    }
	
	public static List<Map<String,String>> statusMapList(){
        if(statusList == null ){
            statusList = new ArrayList<>();
            for(LoanRepayStatusEnum statusEnum: LoanRepayStatusEnum.values()){
                Map<String,String> map = new HashMap<>();
                map.put("code", statusEnum.getStatusCode());
                map.put("name", statusEnum.getStatusName());
                statusList.add(map);
            }
        }
        return statusList;
    }
}

