package com.yixin.garage.enums.garage;
import java.util.ArrayList;
import java.util.List;

/**
 * @Title: 车辆类型 
 * @Description: TODO 
 * @param:
 * @author YixinCapital -- liyaqing
 * @date 2019/8/5 14:13
 */
public enum VehicleTypeEnum {
	
	/**
	 * 033000000:资源车
	 */
	RESOURCE_CAR("033000000", "资源车",true),

    /**
     * 033000011:非资源车
     */
    NOT_RESOURCE_CAR("033000011", "非资源车",true);

    private String value;

    private String name;

    private boolean selected; 
    
    private VehicleTypeEnum() {
    }

    private VehicleTypeEnum(String value, String name) {
        this.name = name;
        this.value = value;
    }
    
    private VehicleTypeEnum(String value, String name, boolean selected) {
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
        for (VehicleTypeEnum status : VehicleTypeEnum.values()) {
            if (value.equals(status.getValue())) {
                return status.getName();
            }
        }
        return "";
    }

    private static List<VehicleTypeEnum> list;

    static {
        list = new ArrayList<VehicleTypeEnum>();
        VehicleTypeEnum[] approvalTypes = VehicleTypeEnum.values();
        for (VehicleTypeEnum approvalType : approvalTypes) {
            list.add(approvalType);
        }
    }

    public static List<VehicleTypeEnum> getDataList() {
        return list;
    }

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
