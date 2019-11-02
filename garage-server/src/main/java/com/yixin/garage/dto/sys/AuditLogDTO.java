package com.yixin.garage.dto.sys;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yixin.common.utils.BaseDTO;
import com.yixin.common.utils.serializer.JsonDateDeserializer;
import com.yixin.common.utils.serializer.JsonDateTimeSerializer;

/**
 * 
 * @ClassName: OperateLogDTO
 * @Description 审批、操作类日志
 * @author YixinCapital -- yangfei02
 * @date 2018年5月29日 下午5:48:01
 *
 */
public class AuditLogDTO extends BaseDTO {

	private static final long serialVersionUID = 9033228357703342386L;

	/** 业务类型 */
	private String logType;
	
	private String logTypeName;
	/**
	 * 业务记录id
	 */
	private String bussId;
	/**
	 * 业务状态code
	 */
	private String bussStatusCode;
	/**
	 * 业务状态名称
	 */
	private String bussStatusName;
	/** 操作动作名 */
	private String oprName;
	/** 处理人域账号 */
	private String dealUserAccount;
	/** 处理人名字 */
	private String dealUserName;
	/** 处理时间 */
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date dealTime;
	/** 处理备注 */
	private String dealRemark;
	
	
	public String getLogType() {
		return logType;
	}
	public void setLogType(String logType) {
		this.logType = logType;
	}
	public String getLogTypeName() {
		return logTypeName;
	}
	public void setLogTypeName(String logTypeName) {
		this.logTypeName = logTypeName;
	}
	public String getBussId() {
		return bussId;
	}
	public void setBussId(String bussId) {
		this.bussId = bussId;
	}
	public String getBussStatusCode() {
		return bussStatusCode;
	}
	public void setBussStatusCode(String bussStatusCode) {
		this.bussStatusCode = bussStatusCode;
	}
	public String getBussStatusName() {
		return bussStatusName;
	}
	public void setBussStatusName(String bussStatusName) {
		this.bussStatusName = bussStatusName;
	}
	public String getOprName() {
		return oprName;
	}
	public void setOprName(String oprName) {
		this.oprName = oprName;
	}
	public String getDealUserAccount() {
		return dealUserAccount;
	}
	public void setDealUserAccount(String dealUserAccount) {
		this.dealUserAccount = dealUserAccount;
	}
	public String getDealUserName() {
		return dealUserName;
	}
	public void setDealUserName(String dealUserName) {
		this.dealUserName = dealUserName;
	}
	public Date getDealTime() {
		return dealTime;
	}
	public void setDealTime(Date dealTime) {
		this.dealTime = dealTime;
	}
	public String getDealRemark() {
		return dealRemark;
	}
	public void setDealRemark(String dealRemark) {
		this.dealRemark = dealRemark;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
