package com.yixin.garage.dto.sys;

import lombok.Data;

@Data
public class BaseRes {
    /**
     * 序号
     */
    private int idx;
    /**
     * 错误信息
     */
    private String errMsg;
    /**
     * 是否成功
     */
    private boolean success = true;
}
