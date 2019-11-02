package com.yixin.garage.dto.message;

/**
 * Created by lianghaoguan on 2018/3/3.
 */
public class MessageDTO<T> {

    private int type;

    private Object data;

    private String ext;

    public MessageDTO(T dto) {
        this.data = dto;
    }
    public MessageDTO() {
    }
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(T dto) {
        this.data = dto;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
