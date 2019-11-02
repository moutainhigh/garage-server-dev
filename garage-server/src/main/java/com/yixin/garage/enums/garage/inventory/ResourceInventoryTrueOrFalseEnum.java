package com.yixin.garage.enums.garage.inventory;

import java.util.ArrayList;
import java.util.List;

/**
 * 资源车盘点盘点结果类型枚举类
 *
 * 2019/1/14 15:36
 * @author: YixinCapital -- libochen
*/
public enum ResourceInventoryTrueOrFalseEnum {

	/**
	 * 0：false
	 */
	FALSE(0,"否"),

	/**
	 * 1：true
	 */
	TRUE(1, "是");

	private int value;

    private String name;

    private boolean selected;

	private ResourceInventoryTrueOrFalseEnum() {
	}

	private ResourceInventoryTrueOrFalseEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}

	private ResourceInventoryTrueOrFalseEnum(int value, String name, boolean selected) {
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
        for (ResourceInventoryTrueOrFalseEnum status : ResourceInventoryTrueOrFalseEnum.values()) {
            if (value == status.getValue()) {
                return status.getName();
            }
        }
        return "";
    }
	
	 private static List<ResourceInventoryTrueOrFalseEnum> list;
    
	 static {
	        list = new ArrayList<ResourceInventoryTrueOrFalseEnum>();
	        ResourceInventoryTrueOrFalseEnum[] stats= ResourceInventoryTrueOrFalseEnum.values();
	        for (ResourceInventoryTrueOrFalseEnum stat : stats) {
	            list.add(stat);
	        }
	    }

	    public static List<ResourceInventoryTrueOrFalseEnum> getDataList() {
	        return list;
	    }
	    
	    
}
