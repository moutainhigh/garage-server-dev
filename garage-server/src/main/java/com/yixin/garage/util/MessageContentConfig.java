package com.yixin.garage.util;

/**
 * 
 * @ClassName: MessageContentConfig
 * @Description 发送邮件或短信等消息通知的内容和标题等配置
 * @author  YixinCapital -- yangfei02	   
 * @date  2019年1月7日 下午1:56:16
 *
 */
public final class MessageContentConfig {
    /*
     * 提醒回填邮件内容
     */
	public static final String NOTIFY_LOAN_CONTENT = "您有项目已成功提款，请前往流动管理系统填写还款计划表，谢谢！";
	
	/*
     * 提醒回填邮件标题
     */
	public static final String NOTIFY_LOAN_TITLE = "回填还款计划提醒";
	
	/*
     * 流动性系统公共邮箱
     */
	public static final String garage_EMAIL = "garage@yxqiche.com";
	
	/*
     * 提醒回填邮件出错内容
     */
    public static final String NOTIFY_LOAN_ERROR_CONTENT = "发送邮件提醒【%s(%s)】出错，出错信息【%s】";
    
    /*
     * 提醒回填邮件出错标题
     */
    public static final String NOTIFY_LOAN_ERROR_TITLE = "邮件提醒回填出错";
    
    /*
     * 提醒回填邮件未找到收件人内容
     */
    public static final String NOTIFY_LOAN_NOTFOUND_CONTENT = "发送邮件提醒回填还款计划有查询不到收件人的记录，查询时间为【%s】";
    
    /*
     * 提醒回填邮件未找到收件人标题
     */
    public static final String NOTIFY_LOAN_NOTFOUND_TITLE = "邮件提醒回填查询不到收件人";
    
    /*
     * 资金日预测修改发送邮件内容
     */
    public static final String NOTIFY_FUNDDAY_FCAST_CONTENT = "领导好：CashFlow有修改，请及时登录查看，谢谢！";
    
    /*
     * 资金日预测修改发送邮件标题
     */
    public static final String NOTIFY_FUNDDAY_FCAST_TITLE = "资金日预测修改提醒";
    
    /*
     * 资金日预测修改邮件发送者
     
    public static final String NOTIFY_FUNDDAY_FCAST_FROM = "garage@yxqiche.com";*/
    
    /*
     * 资金日预测修改发送邮件出错内容
     */
    public static final String NOTIFY_FUNDDAY_FCAST_ERROR_CONTENT = "发送邮件提醒【%s(%s)】出错，出错信息【%s】";
    
    /*
     * 资金日预测修改发送邮件出错标题
     */
    public static final String NOTIFY_FUNDDAY_FCAST_ERROR_TITLE = "邮件提醒回填出错";
}
