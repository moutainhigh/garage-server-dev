package com.yixin.garage.enums.dict;

/**
 * 
 * @Description 部门业务类型枚举
 * @author  YixinCapital -- lizhongxin	   
 * @date  2018年12月27日 下午3:42:00
 *
 */
public enum DeptBusiTypeEnum {
	FINANCING("financing","融资业务"),
	FUND("fund","资金业务")

	;
	
	
	DeptBusiTypeEnum(String code,String name){
		this.typeCode = code;
		this.typeName = name;
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

