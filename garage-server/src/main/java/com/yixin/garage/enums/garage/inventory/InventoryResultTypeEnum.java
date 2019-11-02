package com.yixin.garage.enums.garage.inventory;

import java.util.ArrayList;
import java.util.List;

/**
 * 盘点结果类型枚举类
 *
 * 2019/10/11 10:36
 * @author: YixinCapital -- libochen
*/
public enum InventoryResultTypeEnum {

	/**
	 * 0：在库
	 */
	FREE(0, "在库"),

	/**
	 * 1：临时出库
	 */
	TEMP_GARAGE_OUT(1, "临时出库"),

	/**
	 * 2：处置出库
	 */
	DISPOSEOUT(2, "处置出库"),

	/**
	 * 3：未在库
	 */
	NOT_FOUND(3, "未在库");

	private int value;

    private String name;

    private boolean selected;

	private InventoryResultTypeEnum() {
	}

	private InventoryResultTypeEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}

	private InventoryResultTypeEnum(int value, String name, boolean selected) {
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
        for (InventoryResultTypeEnum status : InventoryResultTypeEnum.values()) {
            if (value == status.getValue()) {
                return status.getName();
            }
        }
        return "";
    }
	
	 private static List<InventoryResultTypeEnum> list;
    
	 static {
	        list = new ArrayList<InventoryResultTypeEnum>();
	        InventoryResultTypeEnum[] stats= InventoryResultTypeEnum.values();
	        for (InventoryResultTypeEnum stat : stats) {
	            list.add(stat);
	        }
	    }

	    public static List<InventoryResultTypeEnum> getDataList() {
	        return list;
	    }
	    
	    
}
