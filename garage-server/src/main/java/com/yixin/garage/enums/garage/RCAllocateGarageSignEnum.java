package com.yixin.garage.enums.garage;

import java.util.ArrayList;
import java.util.List;

/**
 * 出入库标识
 * @Title: RCAllocategarageSignEnum.java  
 * @Package com.yixin.rentcar.enumpackage  
 * @Description: TODO  
 * @author YixinCapital -- liyaqing 
 * @date 2018年8月6日
 */
public enum RCAllocateGarageSignEnum {
	/**
	 * 0 - 调配出库单
	 */
	GARAGEOUT_SIGN(0,"调配出库单"),
	
	/**
	 * 1 - 调配入库单
	 */
	GARAGEIN_SIGN(1,"调配入库单"),
	
	/**
	 * 2 - 分公司入库申请
	 */
	BRANCE_INAPPROVE(2,"分公司入库申请");
	
	
	private int value;

	private String name;

	private boolean selected;

	private RCAllocateGarageSignEnum() {
	}

	private RCAllocateGarageSignEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}

	private RCAllocateGarageSignEnum(int value, String name, boolean selected) {
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
		for (RCAllocateGarageSignEnum status : RCAllocateGarageSignEnum.values()) {
			if (value == status.getValue()) {
				return status.getName();
			}
		}
		return "";
	}

	private static List<RCAllocateGarageSignEnum> list;

	static {
		list = new ArrayList<RCAllocateGarageSignEnum>();
		RCAllocateGarageSignEnum[] garageStatuss = RCAllocateGarageSignEnum.values();
		for (RCAllocateGarageSignEnum garageStatus : garageStatuss) {
			list.add(garageStatus);
		}
	}

	public static List<RCAllocateGarageSignEnum> getDataList() {
		return list;
	}

}
