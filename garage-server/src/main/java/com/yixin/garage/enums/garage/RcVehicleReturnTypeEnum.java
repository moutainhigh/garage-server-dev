package com.yixin.garage.enums.garage;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: 车辆还车类型
 * @Description: TODO 
 * @param:
 * @author YixinCapital -- liyaqing
 * @date 2019/8/5 14:13
 */
public enum RcVehicleReturnTypeEnum {

	/**
     * 提前再入库:0
     */
	ADVANCE(0, "提前再入库"),
    
    /**
     * 逾期再入库:1
     */
	OVERDUE(1, "逾期再入库"),
	
	/**
     * 正常再入库:2
     */
	NORMAL(2, "正常再入库"),
	
	/**
     * 强制再入库:3
     */
	FORCE(3, "强制再入库"),
    
    /**
     * 虚拟再入库:4
     */
    VIRTUAL(4, "虚拟再入库"),
    
    /**
     * 展期再入库:5
     */
    LEASE2(5, "展期再入库");
    
	private int value;

    private String name;
    
    private boolean selected;

	private RcVehicleReturnTypeEnum() {
	}

	private RcVehicleReturnTypeEnum(int value) {
		this.value = value;
	}

	private RcVehicleReturnTypeEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}

	private RcVehicleReturnTypeEnum(int value, String name, boolean selected) {
		this.value = value;
		this.name = name;
		this.selected = selected;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
    
	public static String getDisplayNameByIndex(int value) {
        for (RcVehicleReturnTypeEnum status : RcVehicleReturnTypeEnum.values()) {
            if (value == status.getValue()) {
                return status.getName();
            }
        }
        return "";
    }

    private static List<RcVehicleReturnTypeEnum> list;

    static {
        list = new ArrayList<RcVehicleReturnTypeEnum>();
        RcVehicleReturnTypeEnum[] statuss = RcVehicleReturnTypeEnum.values();
        for (RcVehicleReturnTypeEnum status : statuss) {
            list.add(status);
        }
    }

    public static List<RcVehicleReturnTypeEnum> getDataList() {
        return list;
    }
}
