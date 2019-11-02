package com.yixin.garage.enums.garage;

import java.util.ArrayList;
import java.util.List;

/**
 * 总部审批主单状态
 * @Title: RCZBInBillStatusEnum.java  
 * @Package com.yixin.rentcar.enumpackage  
 * @Description: TODO  
 * @author YixinCapital -- liyaqing 
 * @date 2018年8月8日
 */
public enum RCZBInBillStatusEnum {
	
	/**
	 * 0：待处理
	 */
	PENDING(0, "待处理"),

	/**
	 * 1：出库驳回
	 */
	OUT_REJECT(1, "出库驳回"),

	/**
	 * 2：已入库
	 */
	INOFSTORE(2, "已入库"),
	
	/**
	 * 3:出库中
	 */
	OUT_GARAGING(3,"出库中"),
	
	/**
	 * 4：已出库
	 */
	OUTOFSTORE(4, "已出库"),
	
	/**
	 * 5:入库中
	 */
	IN_GARAGING(5,"入库中"),
	
	/**
	 * 6：入库驳回
	 */
	IN_REJECT(6, "入库驳回"),
	
	/**
	 * 7:调配取消
	 */
	CANCEL(7,"调配取消");

	private int value;

	private String name;

	private boolean selected;

	private RCZBInBillStatusEnum() {
	}

	private RCZBInBillStatusEnum(int value, String name) {
		this.name = name;
		this.value = value;
	}

	private RCZBInBillStatusEnum(int value, String name, boolean selected) {
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
		for (RCZBInBillStatusEnum status : RCZBInBillStatusEnum.values()) {
			if (value == status.getValue()) {
				return status.getName();
			}
		}
		return "";
	}

	private static List<RCZBInBillStatusEnum> list;

	static {
		list = new ArrayList<RCZBInBillStatusEnum>();
		RCZBInBillStatusEnum[] garageStatuss = RCZBInBillStatusEnum.values();
		for (RCZBInBillStatusEnum garageStatus : garageStatuss) {
			list.add(garageStatus);
		}
	}

	public static List<RCZBInBillStatusEnum> getDataList() {
		return list;
	}

}
