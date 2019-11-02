package com.yixin.garage.enums.garage;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: 车辆业务类型
 * @Description: TODO 
 * @param:
 * @author YixinCapital -- liyaqing
 * @date 2019/8/5 14:13
 */
public enum BusinessTypeEnum {

    /**
     * 013000000:消费融
     */
    CONSUMER_LOAN("013000000","消费融", false),

	/**
	 * 0130000500000:经营性租赁
	 */
    OPERATE("0130000500000", "经营性租赁", false),

    /**
     * 0130000300000:联合融
     */
    JOINT_LOAN("0130000300000", "联合融", false),
    /**
     * 0130000100000:车主融
     */
    MORTGAGE_CAR("0130000100000", "车主融", false);


    private String value;

    private String name;

    private boolean selected;

    private BusinessTypeEnum() {
    }

    private BusinessTypeEnum(String value, String name) {
        this.name = name;
        this.value = value;
    }
    private BusinessTypeEnum(String value, String name, boolean selected) {
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
        for (BusinessTypeEnum status : BusinessTypeEnum.values()) {
            if (value.equals(status.getValue())) {
                return status.getName();
            }
        }
        return "";
    }
    
    private static List<BusinessTypeEnum> list;

    static {
        list = new ArrayList<BusinessTypeEnum>();
        BusinessTypeEnum[] handleStatuss = BusinessTypeEnum.values();
        for (BusinessTypeEnum handleStatus : handleStatuss) {
            list.add(handleStatus);
        }
    }

    public static List<BusinessTypeEnum> getDataList() {
        return list;
    }

}

