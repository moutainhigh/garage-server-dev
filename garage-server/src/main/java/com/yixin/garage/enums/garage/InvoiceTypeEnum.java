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
public enum InvoiceTypeEnum {

	/**
	 * 增值税票：0
	 */
    VAT_TICKET(0, "增值税票"),


    /**
     * 收据：1
     */
    RECEIPT(1,"收据");

    private int value;

    private String name;

    private boolean selected;

    private InvoiceTypeEnum() {
    }

    private InvoiceTypeEnum(int value, String name) {
        this.name = name;
        this.value = value;
    }

    private InvoiceTypeEnum(int value, String name, boolean selected) {
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
        for (InvoiceTypeEnum status : InvoiceTypeEnum.values()) {
            if (value == status.getValue()) {
                return status.getName();
            }
        }
        return "";
    }

    private static List<InvoiceTypeEnum> list;

    static {
        list = new ArrayList<InvoiceTypeEnum>();
        InvoiceTypeEnum[] garageStatuss = InvoiceTypeEnum.values();
        for (InvoiceTypeEnum garageStatus : garageStatuss) {
            list.add(garageStatus);
        }
    }

    public static List<InvoiceTypeEnum> getDataList() {
        return list;
    }

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}

