package com.yixin.garage.enums.garage;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: 车辆租赁属性
 * @Description:
 * @param:
 * @author YixinCapital -- liyaqing
 * @date 2019/8/5 14:13
 */
public enum LeasePropertyEnum {

    /**
     * 0130000000000:正租
     */
    RESOURCE_CAR("0130000000000", "正租", false),
    /**
     * 0130000000001:回租
     */
    LEASEBACK_CAR("0130000000001", "回租", false);

    private String value;

    private String name;

    private boolean selected;

    private LeasePropertyEnum() {
    }

    private LeasePropertyEnum(String value, String name) {
        this.name = name;
        this.value = value;
    }
    private LeasePropertyEnum(String value, String name, boolean selected) {
        this.name = name;
        this.value = value;
        this.selected = selected;
    }
    
    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
    
    public boolean isSelected() {
		return selected;
	}


    public static String getDisplayNameByIndex(String value) {
        for (LeasePropertyEnum status : LeasePropertyEnum.values()) {
            if (value.equals(status.getValue())) {
                return status.getName();
            }
        }
        return "";
    }
    
    private static List<LeasePropertyEnum> list;

    static {
        list = new ArrayList<LeasePropertyEnum>();
        LeasePropertyEnum[] handleStatuss = LeasePropertyEnum.values();
        for (LeasePropertyEnum handleStatus : handleStatuss) {
            list.add(handleStatus);
        }
    }

    public static List<LeasePropertyEnum> getDataList() {
        return list;
    }

}

