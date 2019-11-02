package com.yixin.garage.enums.garage.inventory;

import java.util.ArrayList;
import java.util.List;

/**
 * 盘点任务状态枚举类
 *
 * 2019/10/11 10:36
 * @author: YixinCapital -- libochen
 */


public enum InventoryHomeStateEnum {

	/**
	 * 0：待发布
	 */
	PENDING(0, "待发布"),

	/**
	 * 1：发布成功
	 */
	SUCCESS(1, "发布成功"),

	/**
	 * 2:盘点结束
	 */
	END(2,"盘点结束");

	private int value;

    private String name;

    private boolean selected;

	private InventoryHomeStateEnum() {
	}

	private InventoryHomeStateEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}

	private InventoryHomeStateEnum(int value, String name, boolean selected) {
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
        for (InventoryHomeStateEnum status : InventoryHomeStateEnum.values()) {
            if (value == status.getValue()) {
                return status.getName();
            }
        }
        return "";
    }
	
	 private static List<InventoryHomeStateEnum> list;
    
	 static {
	        list = new ArrayList<InventoryHomeStateEnum>();
	        InventoryHomeStateEnum[] stats= InventoryHomeStateEnum.values();
	        for (InventoryHomeStateEnum stat : stats) {
	            list.add(stat);
	        }
	    }

	    public static List<InventoryHomeStateEnum> getDataList() {
	        return list;
	    }
	    
	    
}
