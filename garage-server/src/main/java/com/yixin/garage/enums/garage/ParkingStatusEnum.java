package com.yixin.garage.enums.garage;

import java.util.ArrayList;
import java.util.List;

/**
 * 车位状态枚举：0--车库停车位未满，1-车库停车位已满
 * Package : com.yixin.rentcar.enumpackage.inventory
 * 
 * @author YixinCapital -- qinyunfei
 *		   2016年6月2日 上午9:52:53
 *
 */
public enum ParkingStatusEnum {

	/**
	 * 未满：0
	 */
	NOT_FULL(0, "未满"),
	
	/**
	 * 已满：1
	 */
	FULL(1, "已满");

    private int value;

    private String name;
    
    private boolean selected;
    
    private ParkingStatusEnum() {
    }

    private ParkingStatusEnum(int value, String name) {
        this.name = name;
        this.value = value;
    }
    
    private ParkingStatusEnum(int value, String name, boolean selected) {
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
        for (ParkingStatusEnum status : ParkingStatusEnum.values()) {
            if (value == status.getValue()) {
                return status.getName();
            }
        }
        return "";
    }

    private static List<ParkingStatusEnum> list;

    static {
        list = new ArrayList<ParkingStatusEnum>();
        ParkingStatusEnum[] garageStatuss = ParkingStatusEnum.values();
        for (ParkingStatusEnum garageStatus : garageStatuss) {
            list.add(garageStatus);
        }
    }

    public static List<ParkingStatusEnum> getDataList() {
        return list;
    }

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}

