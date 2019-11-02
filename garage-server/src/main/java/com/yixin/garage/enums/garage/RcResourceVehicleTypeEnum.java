package com.yixin.garage.enums.garage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YixinCapital -- liyaqing
 * @description: TODO
 * @date 2019/8/513:54
 */
public enum RcResourceVehicleTypeEnum {

    /**
     * 035900000:新车
     */
    NEW_CAR("035900000", "新车"),

    /**
     * 035900001:二手车
     */
    SENCOND_CAR("035900001", "二手车");

    private String value;

    private String name;

    private boolean selected;

    private RcResourceVehicleTypeEnum() {
    }

    private RcResourceVehicleTypeEnum(String value, String name) {
        this.name = name;
        this.value = value;
    }

    private RcResourceVehicleTypeEnum(String value, String name,boolean selected) {
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
        for (RcResourceVehicleTypeEnum status : RcResourceVehicleTypeEnum.values()) {
            if (value.equals(status.getValue())) {
                return status.getName();
            }
        }
        return "";
    }

    private static List<RcResourceVehicleTypeEnum> list;

    static {
        list = new ArrayList<RcResourceVehicleTypeEnum>();
        RcResourceVehicleTypeEnum[] approvalTypes = RcResourceVehicleTypeEnum.values();
        for (RcResourceVehicleTypeEnum approvalType : approvalTypes) {
            list.add(approvalType);
        }
    }

    public static List<RcResourceVehicleTypeEnum> getDataList() {
        return list;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
