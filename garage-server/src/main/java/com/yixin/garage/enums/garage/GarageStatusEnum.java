package com.yixin.garage.enums.garage;

import java.util.ArrayList;
import java.util.List;

/**
 * 车库状态枚举：0-正常，1-盘库中，2-停用
 * Package : com.yixin.rentcar.enumpackage
 * 
 * @author YixinCapital -- tangzilong
 *		   2016年9月18日 下午3:58:13
 *
 */
public enum GarageStatusEnum {
	
	/**
	 * 正常：0
	 */
	NORMAL(0, "正常"),
	
	/**
	 * 停用：1
	 */
	DISABLE(1, "停用");

    private int value;

    private String name;
    
    private boolean selected;
    
    private GarageStatusEnum() {
    }

    private GarageStatusEnum(int value, String name) {
        this.name = name;
        this.value = value;
    }
    
    private GarageStatusEnum(int value, String name, boolean selected) {
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
        for (GarageStatusEnum status : GarageStatusEnum.values()) {
            if (value == status.getValue()) {
                return status.getName();
            }
        }
        return "";
    }

    private static List<GarageStatusEnum> list;

    static {
        list = new ArrayList<GarageStatusEnum>();
        GarageStatusEnum[] garageStatuss = GarageStatusEnum.values();
        for (GarageStatusEnum garageStatus : garageStatuss) {
            list.add(garageStatus);
        }
    }

    public static List<GarageStatusEnum> getDataList() {
        return list;
    }

	public boolean isSelected() {
		return selected;
	}
}

