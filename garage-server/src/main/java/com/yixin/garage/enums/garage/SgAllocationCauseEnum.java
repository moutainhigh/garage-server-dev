package com.yixin.garage.enums.garage;

import java.util.ArrayList;
import java.util.List;

/**
 * 调配原因
 * Package : com.yixin.rentcar.enumpackage
 * 
 * @author YixinCapital -- libochen 
 * 2018年7月30日下午2:03:11
 */

public enum SgAllocationCauseEnum {

	/**
	 * 0:清洗
	 */
	CLEAR("0", "清洗", false),

    /**
     * 1:保养
     */
    MAINTAIN("1", "保养", false),

    /**
     * 2:维修
     */
    REPAIR("2", "维修", false);

    private String value;

    private String name;

    private boolean selected;

    private SgAllocationCauseEnum() {
    }

    private SgAllocationCauseEnum(String value, String name) {
        this.name = name;
        this.value = value;
    }

    private SgAllocationCauseEnum(String value, String name, boolean selected) {
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

    public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static String getDisplayNameByIndex(String value) {
        for (SgAllocationCauseEnum status : SgAllocationCauseEnum.values()) {
            if (value.equals(status.getValue())) {
                return status.getName();
            }
        }
        return value;
    }

    private static List<SgAllocationCauseEnum> list;

    static {
        list = new ArrayList<SgAllocationCauseEnum>();
        SgAllocationCauseEnum[] approvalTypes = SgAllocationCauseEnum.values();
        for (SgAllocationCauseEnum approvalType : approvalTypes) {
            list.add(approvalType);
        }
    }

    public static List<SgAllocationCauseEnum> getDataList() {
        return list;
    }

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
