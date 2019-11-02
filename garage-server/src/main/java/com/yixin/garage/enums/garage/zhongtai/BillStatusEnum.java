package com.yixin.garage.enums.garage.zhongtai;
import java.util.ArrayList;
import java.util.List;

/**
 * 对接中台(单子类型枚举)
 * Package : com.yixin.rentcar.enumpackage
 * contracting
 * @author YixinCapital -- zhouhang
 *		   2019年8月29日
 *
 */
public enum BillStatusEnum {
	
    /**
     * 20:安全车库入库
     */
    PSM_RESTOCK_INTERFACE(20, "安全车库入库",false),
    ;

    private int value;

    private String name;

    private boolean selected; 
    
    private BillStatusEnum() {
    }

    private BillStatusEnum(int value, String name) {
        this.name = name;
        this.value = value;
    }
    
    private BillStatusEnum(int value, String name, boolean selected) {
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
        for (BillStatusEnum status : BillStatusEnum.values()) {
            if (value == status.getValue()) {
                return status.getName();
            }
        }
        return "";
    }

    private static List<BillStatusEnum> list;

    static {
        list = new ArrayList<BillStatusEnum>();
        BillStatusEnum[] approvalTypes = BillStatusEnum.values();
        for (BillStatusEnum approvalType : approvalTypes) {
            list.add(approvalType);
        }
    }

    public static List<BillStatusEnum> getDataList() {
        return list;
    }

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
