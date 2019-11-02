package com.yixin.garage.enums.garage.inventory;

import java.util.ArrayList;
import java.util.List;

/**
 * 盘点审批主页盘点状态枚举类
 *
 * 2019/1/11 15:36
 * @author: YixinCapital -- libochen
*/
public enum ResourceInventoryChannelApprovalStateEnum {

	/**
	 * 0：待审批
	 */
	PENDING_APPROVAL(0, "待审批"),

	/**
	 * 1：分公司审批
	 */
	BRANCH_OFFICE_APPROVAL(1, "分公司审批"),

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

	private ResourceInventoryChannelApprovalStateEnum() {
	}

	private ResourceInventoryChannelApprovalStateEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}

	private ResourceInventoryChannelApprovalStateEnum(int value, String name, boolean selected) {
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
        for (ResourceInventoryChannelApprovalStateEnum status : ResourceInventoryChannelApprovalStateEnum.values()) {
            if (value == status.getValue()) {
                return status.getName();
            }
        }
        return "";
    }
	
	 private static List<ResourceInventoryChannelApprovalStateEnum> list;
    
	 static {
	        list = new ArrayList<ResourceInventoryChannelApprovalStateEnum>();
	        ResourceInventoryChannelApprovalStateEnum[] stats= ResourceInventoryChannelApprovalStateEnum.values();
	        for (ResourceInventoryChannelApprovalStateEnum stat : stats) {
	            list.add(stat);
	        }
	    }

	    public static List<ResourceInventoryChannelApprovalStateEnum> getDataList() {
	        return list;
	    }
	    
	    
}
