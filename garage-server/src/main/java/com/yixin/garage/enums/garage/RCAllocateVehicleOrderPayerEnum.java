package com.yixin.garage.enums.garage;

import java.util.ArrayList;
import java.util.List;

/**
 * 调配任务：费用付款方
 * Package : com.yixin.rentcar.enumpackage
 * 
 * @author YixinCapital -- tangzilong
 *		   2016年6月26日 下午4:16:30
 *
 */
public enum RCAllocateVehicleOrderPayerEnum {

	/**
	 * 1：易鑫
	 */
	YI_XIN(1, "易鑫"),

	/**
	 * 2：渠道
	 */
	QU_DAO(2, "渠道");

	private int value;

	private String name;

	private boolean selected;

	private RCAllocateVehicleOrderPayerEnum() {
	}

	private RCAllocateVehicleOrderPayerEnum(int value, String name) {
		this.name = name;
		this.value = value;
	}

	private RCAllocateVehicleOrderPayerEnum(int value, String name, boolean selected) {
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

	public boolean isSelected() {
		return selected;
	}

	public static String getDisplayNameByIndex(int value) {
		for (RCAllocateVehicleOrderPayerEnum status : RCAllocateVehicleOrderPayerEnum.values()) {
			if (value == status.getValue()) {
				return status.getName();
			}
		}
		return "";
	}

	private static List<RCAllocateVehicleOrderPayerEnum> list;

	static {
		list = new ArrayList<RCAllocateVehicleOrderPayerEnum>();
		RCAllocateVehicleOrderPayerEnum[] garageStatuss = RCAllocateVehicleOrderPayerEnum.values();
		for (RCAllocateVehicleOrderPayerEnum garageStatus : garageStatuss) {
			list.add(garageStatus);
		}
	}

	public static List<RCAllocateVehicleOrderPayerEnum> getDataList() {
		return list;
	}

}
