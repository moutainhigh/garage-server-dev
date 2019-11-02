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
public enum SgOperateAttrEnum {

	/**
	 * 安全车库（自建）：0
	 */
    BUILD_OWN(0, "安全车库（自建）"),

    /**
     * 安全车库（非自建）: 1
     */
    BUILD_OTHERS(1,"安全车库（非自建）"),

    /**
     * 虚拟车库：2
     */
    VIRTUAL(2,"虚拟车库");

    private int value;

    private String name;

    private boolean selected;

    private SgOperateAttrEnum() {
    }

    private SgOperateAttrEnum(int value, String name) {
        this.name = name;
        this.value = value;
    }

    private SgOperateAttrEnum(int value, String name, boolean selected) {
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
        for (SgOperateAttrEnum status : SgOperateAttrEnum.values()) {
            if (value == status.getValue()) {
                return status.getName();
            }
        }
        return "";
    }

    private static List<SgOperateAttrEnum> list;

    static {
        list = new ArrayList<SgOperateAttrEnum>();
        SgOperateAttrEnum[] garageStatuss = SgOperateAttrEnum.values();
        for (SgOperateAttrEnum garageStatus : garageStatuss) {
            list.add(garageStatus);
        }
    }

    public static List<SgOperateAttrEnum> getDataList() {
        return list;
    }

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}

