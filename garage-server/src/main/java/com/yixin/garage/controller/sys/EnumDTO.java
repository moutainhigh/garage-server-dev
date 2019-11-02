package com.yixin.garage.controller.sys;

/**
 * 枚举工具DTO类
 * Package : com.yixin.rentcar.controller.common
 * 
 * @author YixinCapital -- tangzilong
 *		   2017年5月2日 上午10:54:40
 *
 */
public class EnumDTO {

    public EnumDTO() {
    }

    /**
     * 枚举名称
     */
    private Object name;
    
    /**
     * 枚举值
     */
    private Object value;
    private Object selected;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public EnumDTO(Object name, Object value) {
        super();
        this.name = name;
        this.value = value;
    }

    public EnumDTO(Object name, Object value, Object selected) {
        super();
        this.name = name;
        this.value = value;
        this.selected = selected;
    }

    public Object getSelected() {
        return selected;
    }

    public void setSelected(Object selected) {
        this.selected = selected;
    }

}
