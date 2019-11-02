package com.yixin.garage.enums.garage;

import java.util.ArrayList;
import java.util.List;

public enum TenancySignEnum {
	
	/**
	 * 未推送：0
	 */
	NOT_PUSH(0, "未推送"),
	
	/**
	 * 已推送：1
	 */
	HAS_PUSH(1, "已推送");
	
	private int value;

    private String name;
    
    private boolean selected;
    
    private TenancySignEnum() {
    }

    private TenancySignEnum(int value, String name) {
        this.name = name;
        this.value = value;
    }
    
    private TenancySignEnum(int value, String name, boolean selected) {
        this.name = name;
        this.value = value;
        this.selected = selected;
    }
    
    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public static String getDisplayNameByIndex(int value) {
        for (TenancySignEnum status : TenancySignEnum.values()) {
            if (value == status.getValue()) {
                return status.getName();
            }
        }
        return "";
    }

    private static List<TenancySignEnum> list;

    static {
        list = new ArrayList<TenancySignEnum>();
        TenancySignEnum[] garageSigns = TenancySignEnum.values();
        for (TenancySignEnum garageSign : garageSigns) {
            list.add(garageSign);
        }
    }

    public static List<TenancySignEnum> getDataList() {
        return list;
    }

	public boolean isSelected() {
		return selected;
	}

}
