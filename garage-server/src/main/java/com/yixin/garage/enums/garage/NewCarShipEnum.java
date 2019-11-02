package com.yixin.garage.enums.garage;

import java.util.ArrayList;
import java.util.List;

/**
 * 新车发运枚举：0-是，1-否
 * Package : com.yixin.rentcar.enumpackage
 * 
 * @author YixinCapital -- zhouhang 
 * 2018年4月11日上午10:39:18
 */
public enum NewCarShipEnum {

	/**
	 * 是：0
	 */
	YES(0, "是"),

	/**
	 * 否：1
	 */
	NO(1, "否");

    private int value;

    private String name;

    private boolean selected;

    private NewCarShipEnum() {
    }

    private NewCarShipEnum(int value, String name) {
        this.name = name;
        this.value = value;
    }

    private NewCarShipEnum(int value, String name, boolean selected) {
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
        for (NewCarShipEnum status : NewCarShipEnum.values()) {
            if (value == status.getValue()) {
                return status.getName();
            }
        }
        return "";
    }

    private static List<NewCarShipEnum> list;

    static {
        list = new ArrayList<NewCarShipEnum>();
        NewCarShipEnum[] garageStatuss = NewCarShipEnum.values();
        for (NewCarShipEnum garageStatus : garageStatuss) {
            list.add(garageStatus);
        }
    }

    public static List<NewCarShipEnum> getDataList() {
        return list;
    }

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}

