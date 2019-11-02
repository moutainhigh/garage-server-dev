package com.yixin.garage.dto.inventory;


import com.yixin.common.utils.BaseDTO;

public class SgInventoryLogDTO extends BaseDTO {
	
	private static final long serialVersionUID = 1L;
	
    /**
     * 操作节点
     */
	private String operationNode;
	
	 /**
     * 状态
     */
    private String stat;


	/**
	 * 备注信息
	 */
	private String remark;

	public String getOperationNode() {
		return operationNode;
	}

	public void setOperationNode(String operationNode) {
		this.operationNode = operationNode;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
