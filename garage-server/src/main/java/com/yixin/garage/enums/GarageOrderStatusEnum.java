package com.yixin.garage.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 出库入库订单状态
 * Package : com.yixin.rentcar.enumpackage.inventory
 * 
 * @author YixinCapital -- qinyunfei
 *		   2016年6月15日 上午9:52:53
 *
 */
public enum GarageOrderStatusEnum {

	/**
	 * 运输中：0
	 */
	UN_DONE(0, "运输中"),
	
	/**
	 * 已入库：1
	 */
	DONE(1, "已入库"),
	
	/**
	 * 待处理：2
	 */
	PENDING(2,"待处理"),
	
	/**
	 * 已取消：4
	 */
	CANCEL(3,"已取消"),
	
	/**
	 * 出库撤回：4
	 */
	retract(4, "出库撤回");

    private int value;

    private String name;

    private boolean selected; 
    
    private GarageOrderStatusEnum() {
    }

    private GarageOrderStatusEnum(int value, String name) {
        this.name = name;
        this.value = value;
    }
    private GarageOrderStatusEnum(int value, String name, boolean selected) {
        this.name = name;
        this.value = value;
        this.selected = selected;
    }
    
    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
    
    public boolean isSelected() {
		return selected;
	}

    public static String getDisplayNameByIndex(int value) {
        for (GarageOrderStatusEnum status : GarageOrderStatusEnum.values()) {
            if (value == status.getValue()) {
                return status.getName();
            }
        }
        return "";
    }

    private static List<GarageOrderStatusEnum> list;

    static {
        list = new ArrayList<GarageOrderStatusEnum>();
        GarageOrderStatusEnum[] garageStatuss = GarageOrderStatusEnum.values();
        for (GarageOrderStatusEnum garageStatus : garageStatuss) {
            list.add(garageStatus);
        }
    }

    public static List<GarageOrderStatusEnum> getDataList() {
        return list;
    }

}

