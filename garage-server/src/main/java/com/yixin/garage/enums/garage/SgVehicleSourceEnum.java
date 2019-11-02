package com.yixin.garage.enums.garage;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: 车辆来源
 * @Description: TODO 
 * @param:
 * @author YixinCapital -- liyaqing
 * @date 2019/8/5 14:13
 */
public enum SgVehicleSourceEnum {

    /**
     * 036100002:融后收车
     */
    DHSC("036100002", "融后收车"),

    /**
     * 036100003:还车
     */
    RETURN("036100003", "还车");

	private String value;

    private String name;

    private boolean selected;

    private SgVehicleSourceEnum() {
    }

    private SgVehicleSourceEnum(String value, String name) {
        this.name = name;
        this.value = value;
    }

    private SgVehicleSourceEnum(String value, String name, boolean selected) {
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

    public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static String getDisplayNameByIndex(String value) {
        for (SgVehicleSourceEnum status : SgVehicleSourceEnum.values()) {
            if (value.equals(status.getValue())) {
                return status.getName();
            }
        }
        return "";
    }

    private static List<SgVehicleSourceEnum> list;

    static {
        list = new ArrayList<SgVehicleSourceEnum>();
        SgVehicleSourceEnum[] approvalTypes = SgVehicleSourceEnum.values();
        for (SgVehicleSourceEnum approvalType : approvalTypes) {
            list.add(approvalType);
        }
    }

    public static List<SgVehicleSourceEnum> getDataList() {
        return list;
    }

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
