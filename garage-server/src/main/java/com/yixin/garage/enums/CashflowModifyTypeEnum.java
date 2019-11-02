package com.yixin.garage.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @ClassName: LogTypeEnum
 * @Description 日志业务类型枚举
 * @author  YixinCapital -- lizhongxin	   
 * @date  2018年8月2日 下午4:16:36
 *
 */
public enum CashflowModifyTypeEnum {
    A_VAl("aVal","实际"),
    F_VAl("fVal","预测"),
	;
	
	private CashflowModifyTypeEnum(String typeCode, String typeName) {
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
	
	public static String getName(String value) {
        if (StringUtils.isNotBlank(value)) {
            for (CashflowModifyTypeEnum num : CashflowModifyTypeEnum.values()) {
                if (num.getTypeCode().equals(value)) {
                    return num.getTypeName();
                }
            }
        }
        return null;
    }
}

