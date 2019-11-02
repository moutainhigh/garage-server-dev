package com.yixin.garage.enums.garage;

import java.util.ArrayList;
import java.util.List;

/**
 * 是否购买运输险枚举
 * Package : com.yixin.rentcar.enumpackage
 * 
 * @author YixinCapital -- qinyunfei
 *		   2016年10月21日 下午3:58:13
 *
 */
public enum IsBuyTranInsuranceEnum {
	
	/**
	 * 是：0
	 */
	YES(0, "是"),
	
	/**
	 * 否：1
	 */
	NO(1, "否");

    private int value;

    private String name;
    
    private boolean selected;
    
    private IsBuyTranInsuranceEnum() {
    }

    private IsBuyTranInsuranceEnum(int value, String name) {
        this.name = name;
        this.value = value;
    }
    
    private IsBuyTranInsuranceEnum(int value, String name, boolean selected) {
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
        for (IsBuyTranInsuranceEnum isBuyTranInsurance : IsBuyTranInsuranceEnum.values()) {
            if (value == isBuyTranInsurance.getValue()) {
                return isBuyTranInsurance.getName();
            }
        }
        return "";
    }

    private static List<IsBuyTranInsuranceEnum> list;

    static {
        list = new ArrayList<IsBuyTranInsuranceEnum>();
        IsBuyTranInsuranceEnum[] isBuyTranInsurances = IsBuyTranInsuranceEnum.values();
        for (IsBuyTranInsuranceEnum isBuyTranInsurance : isBuyTranInsurances) {
            list.add(isBuyTranInsurance);
        }
    }

    public static List<IsBuyTranInsuranceEnum> getDataList() {
        return list;
    }

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}

