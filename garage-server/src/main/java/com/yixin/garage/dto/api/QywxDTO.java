package com.yixin.garage.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class QywxDTO extends QywxBaseDTO  {
	
	private static final long serialVersionUID = 413796382779349707L;
	
	/**
	 * 查询类型：0：待审批；1：已审批
	 */
	private int queryType;
	/**
	 * 业务编号（订单处理编号、订单编号等）
	 */
	private String bussNo;
	/**
	 * 业务状态
	 */
	private String bussStatus;
	/**
	 * 审批结果。1: 审批通过;2: 驳回;10:退回;
	 */
	private Integer auditResult;
	/**
	 * 审批备注 
	 */
    private String auditRemark;
    /**
     * 业务类型： 查询日志时候见LogTypeEnum; 查询列表的时候见AuditBussTypeEnum
     */
    private String bussType;
    
	public int getQueryType() {
        return queryType;
    }
    public void setQueryType(int queryType) {
        this.queryType = queryType;
    }
    public String getBussNo() {
		return bussNo;
	}
	public void setBussNo(String bussNo) {
		this.bussNo = bussNo;
	}
	
    public Integer getAuditResult() {
		return auditResult;
	}
	public void setAuditResult(Integer auditResult) {
		this.auditResult = auditResult;
	}
	public String getAuditRemark() {
		return auditRemark;
	}
	public void setAuditRemark(String auditRemark) {
		this.auditRemark = auditRemark;
	}
	public String getBussType() {
		return bussType;
	}
	public void setBussType(String bussType) {
		this.bussType = bussType;
	}

	public String getBussStatus() {
        return bussStatus;
    }
    public void setBussStatus(String bussStatus) {
        this.bussStatus = bussStatus;
    }
    @Override
	public String toString() {
		return "LoanQywxDTO{" +
				", queryType=" + queryType +
				", bussNo='" + bussNo + '\'' +
				", auditResult=" + auditResult +
				", auditRemark='" + auditRemark + '\'' +
				", bussType='" + bussType + '\'' +
				'}';
	}
}

