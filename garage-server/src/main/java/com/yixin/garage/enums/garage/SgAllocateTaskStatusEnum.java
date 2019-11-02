package com.yixin.garage.enums.garage;

import java.util.ArrayList;
import java.util.List;

/**
 * 调配单车辆状态
 * @Title: RCAllocateBillStatusEnum.java  
 * @Package com.yixin.rentcar.enumpackage  
 * @Description: TODO  
 * @author YixinCapital -- liyaqing 
 * @date 2018年8月8日
 */
public enum SgAllocateTaskStatusEnum {

	/**
	 * 0：待处理
	 */
	PENDING(0, "待处理"),

	/**
	 * 1：出库驳回
	 */
	OUT_REJECT(1, "出库驳回"),

	/**
	 * 6：入库驳回
	 */
	IN_REJECT(6, "入库驳回"),

	/**
	 * 2：出库-审批完成
	 */
	OUTOFSTORE(2, "出库-审批完成"),

	/**
	 * 3:入库中
	 */
	IN_GARAGING(3,"入库中"),

	/**
	 * 4：已入库
	 */
	INOFSTORE(4, "已入库"),

	/**
	 * 5:出库中
	 */
	OUT_GARAGING(5,"出库中"),

	/**
	 * 7:临时出库
	 */
	TEMP_GARAGE_OUT(7,"临时出库"),

	/**
	 * 8:取消
	 */
	CANCEL(8,"取消"),

	/**
	 * 9:预临时出库
	 */
	TEMP_READY(9,"预临时出库");

	private int value;

	private String name;

	private boolean selected;

	private SgAllocateTaskStatusEnum() {
	}

	private SgAllocateTaskStatusEnum(int value, String name) {
		this.name = name;
		this.value = value;
	}

	private SgAllocateTaskStatusEnum(int value, String name, boolean selected) {
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
		for (SgAllocateTaskStatusEnum status : SgAllocateTaskStatusEnum.values()) {
			if (value == status.getValue()) {
				return status.getName();
			}
		}
		return "";
	}

	private static List<SgAllocateTaskStatusEnum> list;

	static {
		list = new ArrayList<SgAllocateTaskStatusEnum>();
		SgAllocateTaskStatusEnum[] garageStatuss = SgAllocateTaskStatusEnum.values();
		for (SgAllocateTaskStatusEnum garageStatus : garageStatuss) {
			list.add(garageStatus);
		}
	}

	public static List<SgAllocateTaskStatusEnum> getDataList() {
		return list;
	}

}
