package com.yixin.garage.enums.garage;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: 推送来源
 * @Description: TODO 
 * @param:
 * @author YixinCapital -- liyaqing
 * @date 2019/8/5 14:13
 */
public enum PushSourceEnum {

	/**
	 * 0:融后推送
	 */
	LOAN_PUSH(0, "融后推送", false),

	/**
	 * 1:手动创建
	 */
    TEMP_GARAGE(1, "手动创建", false);

    private int value;

    private String name;

    private boolean selected;

    private PushSourceEnum() {
    }

    private PushSourceEnum(int value, String name) {
        this.name = name;
        this.value = value;
    }
    private PushSourceEnum(int value, String name, boolean selected) {
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
        for (PushSourceEnum status : PushSourceEnum.values()) {
            if (value == status.getValue()) {
                return status.getName();
            }
        }
        return "";
    }
    
    private static List<PushSourceEnum> list;

    static {
        list = new ArrayList<PushSourceEnum>();
        PushSourceEnum[] handleStatuss = PushSourceEnum.values();
        for (PushSourceEnum handleStatus : handleStatuss) {
            list.add(handleStatus);
        }
    }

    public static List<PushSourceEnum> getDataList() {
        return list;
    }

}

