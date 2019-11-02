package com.yixin.garage.enums.garage.zhongtai;

import java.util.ArrayList;
import java.util.List;

/**
 * 向erp系统发送数据的接口名称字段对应的常量值
 *
 * @author YixinCapital -- zhouhang
 *		   2019年8月29日 上午10:11:14
 *
 */
public enum SendTypeEnum {

    PSM_RESTOCK_INTERFACE("PSM_RESTOCK_INTERFACE", "安全车库入库",false);

    private String value;

    private String name;
    
    private boolean selected;

    private SendTypeEnum() {
    }

    private SendTypeEnum(String value, String name) {
        this.name = name;
        this.value = value;
    }

    private SendTypeEnum(String value, String name, boolean selected) {
        this.name = name;
        this.value = value;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public static String getDisplayNameByIndex(String value) {
        for (SendTypeEnum status : SendTypeEnum.values()) {
            if (value.equals(status.getValue())) {
                return status.getName();
            }
        }
        return "";
    }

    public static SendTypeEnum getBillTypeByIndex(String value) {
        for (SendTypeEnum status : SendTypeEnum.values()) {
            if (value.equals(status.getValue())) {
                return status;
            }
        }
        return null;
    }

    private static List<SendTypeEnum> list;

    static {
        list = new ArrayList<SendTypeEnum>();
        SendTypeEnum[] approvalTypes = SendTypeEnum.values();
        for (SendTypeEnum approvalType : approvalTypes) {
            list.add(approvalType);
        }
    }

    public static List<SendTypeEnum> getDataList() {
        return list;
    }

    public static List<SendTypeEnum> getList() {
        return list;
    }
    public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
