package com.yixin.garage.enums.garage;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用中车辆：审批状态
 * @Title: InUseApproveStatusEnum.java  
 * @Package com.yixin.rentcar.enumpackage  
 * @Description: TODO  
 * @author YixinCapital -- liyaqing 
 * @date 2018年10月22日
 */
public enum InUseApproveStatusEnum {
	
	/**
	 * 待处理:0
	 */
	HANDLING(0,"待处理"),
	
	/**
	 * 待审批：1
	 */
	PENDING(1, "待审批"),
	
	/**
	 * 审批驳回：2
	 */
	REJECT(2, "审批驳回"),
	
	/**
	 * 已完成：3
	 */
	APPROVAL(3, "已完成");
	

	private int value;

    private String name;
    
    private boolean selected;
    
    private InUseApproveStatusEnum() {
    }

    private InUseApproveStatusEnum(int value, String name) {
        this.name = name;
        this.value = value;
    }
    
    private InUseApproveStatusEnum(int value, String name, boolean selected) {
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

    public static String getDisplayNameByIndex(int value) {
        for (InUseApproveStatusEnum status : InUseApproveStatusEnum.values()) {
            if (value == status.getValue()) {
                return status.getName();
            }
        }
        return "";
    }

    private static List<InUseApproveStatusEnum> list;

    static {
        list = new ArrayList<InUseApproveStatusEnum>();
        InUseApproveStatusEnum[] statuss = InUseApproveStatusEnum.values();
        for (InUseApproveStatusEnum status : statuss) {
            list.add(status);
        }
    }

    public static List<InUseApproveStatusEnum> getDataList() {
        return list;
    }

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
