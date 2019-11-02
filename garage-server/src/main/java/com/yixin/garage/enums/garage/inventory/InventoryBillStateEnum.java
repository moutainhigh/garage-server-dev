package com.yixin.garage.enums.garage.inventory;

import java.util.ArrayList;
import java.util.List;

/**
 * 盘点审批主页盘点状态枚举类
 *
 * 2019/10/11 10:36
 * @author: YixinCapital -- libochen
*/
public enum InventoryBillStateEnum {

	/**
	 * 0：待审批
	 */
	PENDING_APPROVAL(0, "待审批"),

	/**
	 * 1：待提交
	 */
	UN_COMMIT(1, "待提交"),

	/**
	 * 2:已驳回
	 */
	REJECT(2,"已驳回"),


	/**
	 * 3:已完成
	 */
	FINISH(3,"已完成");

	private int value;

    private String name;

    private boolean selected;

	private InventoryBillStateEnum() {
	}

	private InventoryBillStateEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}

	private InventoryBillStateEnum(int value, String name, boolean selected) {
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
        for (InventoryBillStateEnum status : InventoryBillStateEnum.values()) {
            if (value == status.getValue()) {
                return status.getName();
            }
        }
        return "";
    }
	
	 private static List<InventoryBillStateEnum> list;
    
	 static {
	        list = new ArrayList<InventoryBillStateEnum>();
	        InventoryBillStateEnum[] stats= InventoryBillStateEnum.values();
	        for (InventoryBillStateEnum stat : stats) {
	            list.add(stat);
	        }
	    }

	    public static List<InventoryBillStateEnum> getDataList() {
	        return list;
	    }
	    
	    
}
