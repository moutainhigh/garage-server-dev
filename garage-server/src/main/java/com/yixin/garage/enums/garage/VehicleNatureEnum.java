package com.yixin.garage.enums.garage;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用中车辆：车辆性质
 * @Title: VehicleNatureEnum.java  
 * @Package com.yixin.rentcar.enumpackage  
 * @Description: TODO  
 * @author YixinCapital -- liyaqing 
 * @date 2018年10月22日
 */
public enum VehicleNatureEnum {
	
	/**
	 * 营运：0
	 */
	OPERATION(0, "营运"),
	
	/**
	 * 非营运：1
	 */
	NON_OPERATION(1, "非营运"),

    /**
     * 租赁：2
     */
    LEASE(2, "租赁"),

    /**
     * 预约出租客运：3
     */
    RESERVATION_LEASE(3, "预约出租客运");

    private int value;

    private String name;
    
    private boolean selected;
    
    private VehicleNatureEnum() {
    }

    private VehicleNatureEnum(int value, String name) {
        this.name = name;
        this.value = value;
    }
    
    private VehicleNatureEnum(int value, String name, boolean selected) {
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
        for (VehicleNatureEnum status : VehicleNatureEnum.values()) {
            if (value == status.getValue()) {
                return status.getName();
            }
        }
        return "";
    }

    private static List<VehicleNatureEnum> list;

    static {
        list = new ArrayList<VehicleNatureEnum>();
        VehicleNatureEnum[] statuss = VehicleNatureEnum.values();
        for (VehicleNatureEnum status : statuss) {
            list.add(status);
        }
    }

    public static List<VehicleNatureEnum> getDataList() {
        return list;
    }

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
