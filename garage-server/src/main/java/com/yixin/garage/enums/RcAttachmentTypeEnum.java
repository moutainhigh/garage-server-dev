package com.yixin.garage.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 附件类型
 * Package : com.yixin.rentcar.enumpackage
 * 
 * @author YixinCapital -- tangzilong
 *		   2018年8月6日 下午1:57:27
 *
 */
public enum RcAttachmentTypeEnum {

	/**
	 * 安全车库-协议附件 01
	 */
    SG_AGREEMENT("01", "安全车库-协议附件"),

    /**
     * 安全车库-出入库附件 02
     */
    SG_OUT_IN_GARAGE("02", "安全车库-出库附件"),


    /**
     * 安全车库 - 其他(出入库)：03
     */
    SG_OTHERS_OF_GARAGE("03", "安全车库-其他"),

    /**
     * 安全车库-入库附件：04
     */
//    SG_IN_GARAGE("04", "安全车库-入库附件"),

    /**
     * 新增车辆入库附件：05
     */
    SG_INTO_GARAGE("05", "新增车辆入库附件"),

    /**
     * 安全车库-交接视频：06
     */
    SG_HAND_OVER("06", "安全车库-交接视频"),

	/**
	 * 安全车库-车架号照片：07
	 */
	INVENTORY_VIN_PHOTO("07", "安全车库-车架号照片"),

	/**
	 * 安全车库-车头正面照片：08
	 */
	INVENTORY_CARHEAD_PHOTO("08", "安全车库-车头正面照片");

	private String value;

    private String name;
    
    private boolean selected;

	private RcAttachmentTypeEnum() {
	}

	private RcAttachmentTypeEnum(String value) {
		this.value = value;
	}

	private RcAttachmentTypeEnum(String value, String name) {
		this.value = value;
		this.name = name;
	}

	private RcAttachmentTypeEnum(String value, String name, boolean selected) {
		this.value = value;
		this.name = name;
		this.selected = selected;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
    
	public static String getDisplayNameByIndex(String value) {
        for (RcAttachmentTypeEnum status : RcAttachmentTypeEnum.values()) {
            if (value == status.getValue()) {
                return status.getName();
            }
        }
        return "";
    }

    private static List<RcAttachmentTypeEnum> list;

    static {
        list = new ArrayList<RcAttachmentTypeEnum>();
        RcAttachmentTypeEnum[] statuss = RcAttachmentTypeEnum.values();
        for (RcAttachmentTypeEnum status : statuss) {
            list.add(status);
        }
    }

    public static List<RcAttachmentTypeEnum> getDataList() {
        return list;
    }
}
