package com.yixin.garage.enums.garage;
import java.util.ArrayList;
import java.util.List;

/**
 * 车辆类型
 * Package : com.yixin.rentcar.enumpackage
 * 
 * @author YixinCapital -- liutao1
 *		   2016年6月8日 上午11:26:15
 *
 */
public enum CerTypeCodeEnum {
	
	/**
	 * 0:中华人民共和国居民身份证
	 */
	IDCARD("0", "中华人民共和国居民身份证",true),

    /**
     * 1:护照
     */
	PASSPORT("1", "护照",true),
	
	/**
	 * 2:中华人民解放军军人身份证
	 */
	LIBERATION_ARMY_CARD("2", "中华人民解放军军人身份证",true),
	
	/**
	 * 3:港澳居民来往内地通行证
	 */
	HOME_RETURN_PERMIT("3", "港澳居民来往内地通行证",true),
	
	/**
	 * 4:警官证
	 */
	POLICE_ID_CARD("4", "警官证",true),
	
	/**
	 * 5:士兵证
	 */
	SOLDIER_CARD("5", "士兵证",true),
	
	/**
	 * 6:香港/澳门身份证
	 */
	HONG_KONG_MACAU_CARD("6", "香港/澳门身份证",true),
	
	/**
	 * 7:工商营业执政号码
	 */
   BUSINESS_LICENSE("7", "工商营业执政号码",true);

    private String value;

    private String name;

    private boolean selected; 
    
    private CerTypeCodeEnum() {
    }

    private CerTypeCodeEnum(String value, String name) {
        this.name = name;
        this.value = value;
    }
    
    private CerTypeCodeEnum(String value, String name, boolean selected) {
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
        for (CerTypeCodeEnum status : CerTypeCodeEnum.values()) {
            if (value.equals(status.getValue())) {
                return status.getName();
            }
        }
        return "";
    }

    private static List<CerTypeCodeEnum> list;

    static {
        list = new ArrayList<CerTypeCodeEnum>();
        CerTypeCodeEnum[] approvalTypes = CerTypeCodeEnum.values();
        for (CerTypeCodeEnum approvalType : approvalTypes) {
            list.add(approvalType);
        }
    }

    public static List<CerTypeCodeEnum> getDataList() {
        return list;
    }

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
