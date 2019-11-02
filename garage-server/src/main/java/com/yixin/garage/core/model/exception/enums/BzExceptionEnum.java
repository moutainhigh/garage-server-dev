package com.yixin.garage.core.model.exception.enums;

import com.yixin.garage.core.model.exception.AbstractBaseExceptionEnum;

/**
 * 所有业务异常的枚举
 *
 */
public enum BzExceptionEnum implements AbstractBaseExceptionEnum {

    /**
     * 其他
     */
    INVLIDE_DATE_STRING(400, "输入日期格式不对"),

    /**
     * 初始化数据库的异常
     */
    NO_CURRENT_USER(700, "当前没有登录的用户"),
    INIT_TABLE_EMPTY_PARAMS(701, "初始化数据库，存在为空的字段"),
    
    /**
     * 数据库字段与实体字段不一致
     */
    FIELD_VALIDATE_ERROR(702, "数据库字段与实体字段不一致!"),

    /**
     * 其他
     */
    WRITE_ERROR(500, "渲染界面错误"),
    ENCRYPT_ERROR(600, "加解密错误"),


    /**
     * 错误的请求
     */
    PAGE_NULL(404, "请求页面不存在"),
    IO_ERROR(500, "流读取异常"),
    SERVICE_ERROR(500, "服务器异常"),
    REMOTE_SERVICE_NULL(404, "远程服务不存在"),
    ASYNC_ERROR(5000, "数据在被别人修改，请稍后重试"),
	
	
	
	/**
     * 字典
     */
    DICT_EXISTED(400, "字典已经存在"),
    ERROR_CREATE_DICT(500, "创建字典失败"),
    ERROR_WRAPPER_FIELD(500, "包装字典属性失败"),
    ERROR_CODE_EMPTY(500, "字典类型不能为空"),

    /**
     * 文件上传
     */
    FILE_READING_ERROR(400, "FILE_READING_ERROR!"),
    FILE_NOT_FOUND(400, "FILE_NOT_FOUND!"),
    UPLOAD_ERROR(500, "上传图片出错"),

    /**
     * 权限和数据问题
     */
    DB_RESOURCE_NULL(400, "数据库中没有该资源"),
    NO_PERMITION(405, "权限异常"),
    REQUEST_INVALIDATE(400, "请求数据格式不正确"),
    INVALID_KAPTCHA(400, "验证码不正确"),
    CANT_DELETE_ADMIN(600, "不能删除超级管理员"),
    CANT_FREEZE_ADMIN(600, "不能冻结超级管理员"),
    CANT_CHANGE_ADMIN(600, "不能修改超级管理员角色"),

    /**
     * 账户问题
     */
    AUTH_REQUEST_ERROR(400, "账号密码错误"),
    USER_ALREADY_REG(401, "该用户已经注册"),
    NO_THIS_USER(400, "没有此用户"),
    USER_NOT_EXISTED(400, "没有此用户"),
    ACCOUNT_FREEZED(401, "账号被冻结"),
    OLD_PWD_NOT_RIGHT(402, "原密码不正确"),
    TWO_PWD_NOT_MATCH(405, "两次输入密码不一致"),

    /**
     * 错误的请求
     */
    MENU_PCODE_COINCIDENCE(400, "菜单编号和副编号不能一致"),
    EXISTED_THE_MENU(400, "菜单编号重复，不能添加"),
    DICT_MUST_BE_NUMBER(400, "字典的值必须为数字"),
    REQUEST_NULL(400, "请求有错误"),
    SESSION_TIMEOUT(400, "会话超时"),
    SERVER_ERROR(500, "服务器异常"),

    /**
     * token异常
     */
    TOKEN_EXPIRED(700, "token过期"),
    TOKEN_ERROR(700, "token验证失败"),

    /**
     * 签名异常
     */
    SIGN_ERROR(700, "签名验证失败"),
    
    /**
     * 业务查询异常
     */
    ID_EMPTY(800,"入参Id为空"),
    NOT_FIND(801,"未查询到记录"),

    PARAMETER_INCOMPLETE(900,"必填项不全，请核实")
    ;

    

    BzExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;

    private String message;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
