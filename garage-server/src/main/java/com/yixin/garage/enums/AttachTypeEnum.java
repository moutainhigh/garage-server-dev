package com.yixin.garage.enums;

import org.springframework.util.StringUtils;

/**
 * 
 * @ClassName: LoanRepayStatusEnum
 * @Description 还款状态：新添加一条记录就是编辑中，点提交复核以后为待复核。复核通过即生效。只复核一次，以后修改走审批
 *                     修改任何信息提交以后变成待部门审批，通过则进入待资管审批，驳回回到生效；
 *                                 然后是待资管领导审批，通过进入资管领导审批，驳回回到待部门审批，且部门审批人可以修改，修改后重新待资管审批；
 *                                 然后是资管领导审批，通过即生效，驳回返回上一节点（类似上衣状态步骤）
 * @author  YixinCapital -- yangfei02	   
 * @date  2018年12月29日 下午4:08:36
 *
 */
public enum AttachTypeEnum {

    SUBMIT_FUNDPLAN("submitfundplan","shenpi","已提交"),
    ;
	
	AttachTypeEnum(String bussType,String attachType,String desc){
		this.bussType = bussType;
		this.attachType = attachType;
		this.desc = desc;
	}
	
	private String bussType;
	private String attachType;
	private String desc;
    public String getBussType() {
        return bussType;
    }
    public String getAttachType() {
        return attachType;
    }
    public String getDesc() {
        return desc;
    }
}

