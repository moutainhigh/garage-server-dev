package com.yixin.garage.util.constant;
/**
 * 常量接口
 * @author lishuaifeng
 *
 */
public interface Constants {
	/**
	 * 前端入参
	 */
	String undefined = "undefined";
	
	 /**
     * 资金管理部大区编码
     */
    public static final String FUND_AREA_CODE="100105"; 
    
    public static final String OTHER_AREA = "other";
    /**
     * 时间限制业务类型：部门业务资金计划提报
     */
    String DEPT_SUBMISSION = "fundPlan:deptSubmission"; 
    
    String YEAR ="year";
    
    String MONTH = "month";
    
	/**
	 * 邮件发送者
	 */
	String EMAIL_FROM = "garage@yxqiche.com";
	/**
	 * 邮件接收人，多个用','隔开
	 */
	String MAIL_TO_PERSON="lizhongxin@yxqiche.com";
    
	/**
	 * Mq 消息处理异常邮件内容
	 */
	String RABBITMQ_RECEIVE_EXCEPTION_MAIL_TITLE = "Environment:%s RabbitMQ接收处理异常,请核查。";
	/**
	 * Mq 消息处理异常邮件内容
	 */
	String RABBITMQ_RECEIVE_EXCEPTION_MAIL_TEXT = "RabbitMQ接收处理异常。发生时间：【%s】，异常信息：【%s】，消息内容：【%s】，消息属性：【%s】。";
	/**
	 * MQ处理异常邮件接收人，多个用','隔开
	 */
	String RABBITMQ_RECEIVE_EXCEPTION_MAIL_ADDRESS="lizhongxin@yxqiche.com,qinyunfei@yxqiche.com,lishuaifeng@yxqiche.com,yangfei@yxqiche.com";
}
