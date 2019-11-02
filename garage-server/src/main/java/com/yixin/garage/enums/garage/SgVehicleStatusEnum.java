package com.yixin.garage.enums.garage;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: 车辆状态
 * @Description: TODO 
 * @param:
 * @author YixinCapital -- liyaqing
 * @date 2019/8/5 14:13
 */
public enum SgVehicleStatusEnum {

	/**
	 * 0:在库
	 */
    FREE(0, "在库", false),

    /**
     * 1:出库
     */
    DISPOSE_OUT(1, "出库", false),

    /**
     * 2:临时出库
     */
    TEMP_GARAGE_OUT(2, "临时出库", false),

    /**
     * 3:新建调配
     */
    ALLOCATE_TASK(11, "新建调配", false),

    /**
     * 4:临时出库审批中
     */
    INTRANSIT(4, "临时出库审批中", false);

    private int value;

    private String name;

    private boolean selected;

    private SgVehicleStatusEnum() {
    }

    private SgVehicleStatusEnum(int value, String name) {
        this.name = name;
        this.value = value;
    }

    private SgVehicleStatusEnum(int value, String name, boolean selected) {
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
        for (SgVehicleStatusEnum status : SgVehicleStatusEnum.values()) {
            if (value == status.getValue()) {
                return status.getName();
            }
        }
        return "";
    }
    
    public static String getDisplayNameByIndex1(Integer value) {
    	if(value == null){
    		return "未知状态";
    	}
        for (SgVehicleStatusEnum status : SgVehicleStatusEnum.values()) {
            if (value == status.getValue()) {
                return status.getName();
            }
        }
        return "";
    }

    private static List<SgVehicleStatusEnum> list;

    static {
        list = new ArrayList<SgVehicleStatusEnum>();
        SgVehicleStatusEnum[] approvalTypes = SgVehicleStatusEnum.values();
        for (SgVehicleStatusEnum approvalType : approvalTypes) {
            list.add(approvalType);
        }
    }

    public static List<SgVehicleStatusEnum> getDataList() {
        return list;
    }

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
