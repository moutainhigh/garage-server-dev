package com.yixin.garage.enums.garage;

import java.util.ArrayList;
import java.util.List;

/**
 * 产生错误日志相应的系统
 * Package : com.yixin.rentcar.enumpackage
 * 
 * @author YixinCapital -- zhouhang
 *		   2019年8月30日 上午10:11:14
 *
 */
public enum SystemOfTypeEnum {

	/**
	 * "":0
	 */
    RENTCAR_SYSTEM(0, "进销存系统"),

    /**
     * "":0
     */
    FNS_SYSTEM(1, "FNS系统"),

    /**
     * "":0
     */
    ERP_SYSTEM(2, "erp系统");

    private int value;

    private String name;

    private boolean selected; 
    
    private SystemOfTypeEnum() {
    }

    private SystemOfTypeEnum(int value, String name) {
        this.name = name;
        this.value = value;
    }
    
    private SystemOfTypeEnum(int value, String name, boolean selected) {
        this.name = name;
        this.value = value;
        this.selected = selected;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static String getDisplayNameByIndex(int value) {
        for (SystemOfTypeEnum status : SystemOfTypeEnum.values()) {
            if (value == status.getValue()) {
                return status.getName();
            }
        }
        return "";
    }

    private static List<SystemOfTypeEnum> list;

    static {
        list = new ArrayList<SystemOfTypeEnum>();
        SystemOfTypeEnum[] approvalTypes = SystemOfTypeEnum.values();
        for (SystemOfTypeEnum systemOfTypeEnum : approvalTypes) {
        	list.add(systemOfTypeEnum);
		}
    }

    public static List<SystemOfTypeEnum> getDataList() {
        return list;
    }

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
