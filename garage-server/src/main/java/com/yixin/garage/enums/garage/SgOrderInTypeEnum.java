package com.yixin.garage.enums.garage;

import java.util.ArrayList;
import java.util.List;

/**
 * 经营属性枚举：0-自营，1-经销商
 * Package : com.yixin.rentcar.enumpackage
 * 
 * @author YixinCapital -- tangzilong
 *		   2016年9月19日 上午9:31:10
 *
 */

public enum SgOrderInTypeEnum {

	/**
	 * 临时入库：0
	 */
    TEMPORARY_IN("0", "临时入库"),

    /**
     * 收车: 1
     */
    BACK_CAR("1","收车"),

    /**
     * 退车：2
     */
    RETURN_CAR("2","退车");

    private String value;

    private String name;

    private boolean selected;

    private SgOrderInTypeEnum() {
    }

    private SgOrderInTypeEnum(String value, String name) {
        this.name = name;
        this.value = value;
    }

    private SgOrderInTypeEnum(String value, String name, boolean selected) {
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
        for (SgOrderInTypeEnum status : SgOrderInTypeEnum.values()) {
            if (value.equals(status.getValue())) {
                return status.getName();
            }
        }
        return "";
    }

    private static List<SgOrderInTypeEnum> list;

    static {
        list = new ArrayList<SgOrderInTypeEnum>();
        SgOrderInTypeEnum[] garageStatuss = SgOrderInTypeEnum.values();
        for (SgOrderInTypeEnum garageStatus : garageStatuss) {
            list.add(garageStatus);
        }
    }

    public static List<SgOrderInTypeEnum> getDataList() {
        return list;
    }

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}

