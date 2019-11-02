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
public enum OperateAttrEnum {

	/**
	 * 资源车大库：0
	 */
	SELF(0, "资源车大库"),
	
	/**
	 * 体验店：1
	 */
	FRAN(1, "体验店"),
	
	/**
	 * 代理商：2
	 */
	AGENT(2, "代理商"),
	
	/**
	 * 其他：3
	 */
	OTHER(3, "其他"),

    /**
     * 临时库：4
     */
    TEMPORARY_GARAGE(4, "临时库"),

    /**
     * 强制收回车库：5
     */
    FORCEBACK_GARAGE(5, "强制收回车库"),

    /**
     * 停放4S店：6
     */
    PARKED_4S_GARAGE(6, "停放4S店"),
	
    /**
     * 综合店：7
     */
	COMPREHENSIVE_SHOP(7,"综合店"),
	
	/**
	 * 4s店:8
	 */
	GARAGE_4S(8,"4s店"),

    /**
     * 虚拟车库：9
     */
    VIRTUAL_GARAGE(9,"虚拟车库");

    private int value;

    private String name;
    
    private boolean selected;
    
    private OperateAttrEnum() {
    }

    private OperateAttrEnum(int value, String name) {
        this.name = name;
        this.value = value;
    }
    
    private OperateAttrEnum(int value, String name, boolean selected) {
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
        for (OperateAttrEnum status : OperateAttrEnum.values()) {
            if (value == status.getValue()) {
                return status.getName();
            }
        }
        return "";
    }

    private static List<OperateAttrEnum> list;

    static {
        list = new ArrayList<OperateAttrEnum>();
        OperateAttrEnum[] garageStatuss = OperateAttrEnum.values();
        for (OperateAttrEnum garageStatus : garageStatuss) {
            list.add(garageStatus);
        }
    }

    public static List<OperateAttrEnum> getDataList() {
        return list;
    }

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}

