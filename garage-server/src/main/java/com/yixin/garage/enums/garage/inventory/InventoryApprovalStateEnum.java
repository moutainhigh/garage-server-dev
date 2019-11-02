package com.yixin.garage.enums.garage.inventory;

import java.util.ArrayList;
import java.util.List;

/**
 * 盘点审批主页盘点状态枚举类
 *
 * 2019/10/11 10:36
 * @author: YixinCapital -- libochen
*/
public enum InventoryApprovalStateEnum {

	/**
	 * 0：未审核
	 */
	UN_APPROVAL(0, "未审核"),

	/**
	 * 1：合格
	 */
	QUALIFIED(1, "合格"),

	/**
	 * 2:不合格
	 */
	UN_QUALIFIED(2,"不合格");

	private int value;

    private String name;

    private boolean selected;

	private InventoryApprovalStateEnum() {
	}

	private InventoryApprovalStateEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}

	private InventoryApprovalStateEnum(int value, String name, boolean selected) {
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
        for (InventoryApprovalStateEnum status : InventoryApprovalStateEnum.values()) {
            if (value == status.getValue()) {
                return status.getName();
            }
        }
        return "";
    }
	
	 private static List<InventoryApprovalStateEnum> list;
    
	 static {
	        list = new ArrayList<InventoryApprovalStateEnum>();
	        InventoryApprovalStateEnum[] stats= InventoryApprovalStateEnum.values();
	        for (InventoryApprovalStateEnum stat : stats) {
	            list.add(stat);
	        }
	    }

	    public static List<InventoryApprovalStateEnum> getDataList() {
	        return list;
	    }
	    
	    
}
