package com.yixin.garage.enums.garage;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: 出库原因
 * @Description: TODO 
 * @param:
 * @author YixinCapital -- liyaqing
 * @date 2019/8/5 14:13
 */
public enum OutReasonEnum {

    /**
     * 0:清洗
     */
    CLEAR(0, "清洗", false),

    /**
     * 1:保养
     */
    MAINTAIN(1, "保养", false),

    /**
     * 2:维修
     */
    REPAIR(2, "维修", false),

    /**
     * 3:处置出库
     */
    DISPOSEOUT(3, "处置出库", false),

    /**
     * 4:赎回出库
     */
    REDEEMOUT(4, "赎回出库", false),

    /**
     * 5:部分债权转让出库
     */
    TRANSFEROUT(5, "部分债权转让出库", false);


    private int value;

    private String name;

    private boolean selected;

    private OutReasonEnum() {
    }

    private OutReasonEnum(int value, String name) {
        this.name = name;
        this.value = value;
    }
    private OutReasonEnum(int value, String name, boolean selected) {
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
        for (OutReasonEnum status : OutReasonEnum.values()) {
            if (value == status.getValue()) {
                return status.getName();
            }
        }
        return "";
    }
    
    private static List<OutReasonEnum> list;

    static {
        list = new ArrayList<OutReasonEnum>();
        OutReasonEnum[] handleStatuss = OutReasonEnum.values();
        for (OutReasonEnum handleStatus : handleStatuss) {
            list.add(handleStatus);
        }
    }

    public static List<OutReasonEnum> getDataList() {
        return list;
    }

}

