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

public enum ShippingTypeEnum {

	/**
	 * 0:自提
	 */
	SELF_LIFTING("0", "自提", false),
	
	/**
	 * 
	 * 1:送货上门
	 */
	TRAILER("1", "送货上门", false);
	
    private String value;

    private String name;

    private boolean selected; 
    
    private ShippingTypeEnum() {
    }

    private ShippingTypeEnum(String value, String name) {
        this.name = name;
        this.value = value;
    }
    
    private ShippingTypeEnum(String value, String name, boolean selected) {
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
        for (ShippingTypeEnum status : ShippingTypeEnum.values()) {
            if (value == status.getValue()) {
                return status.getName();
            }
        }
        return "";
    }

    private static List<ShippingTypeEnum> list;

    static {
        list = new ArrayList<ShippingTypeEnum>();
        ShippingTypeEnum[] approvalTypes = ShippingTypeEnum.values();
        for (ShippingTypeEnum approvalType : approvalTypes) {
            list.add(approvalType);
        }
    }

    public static List<ShippingTypeEnum> getDataList() {
        return list;
    }

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
