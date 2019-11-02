package com.yixin.garage.core.common.constant.state;

/**
 * 业务是否成功的日志记录
 */
public enum LogSucceed {

    SUCCESS("成功"),
    FAIL("失败");

    String message;

    LogSucceed(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
