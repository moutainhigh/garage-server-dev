package com.yixin.garage.dto.garage;


import com.yixin.common.utils.BaseDTO;

/**
 * 车库日志
 * @Title: YxGarageLogDTO.java  
 * @Package com.yixin.rentcar.dto.inventory  
 * @Description: TODO  
 * @author YixinCapital -- liyaqing 
 * @date 2018年7月24日
 */
public class YxGarageInfoLogDTO extends BaseDTO {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 事件
	 */
	private String event;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 车库ID
	 */
	private String garageId;



	/**
	 * 车库id
	 */
	private String sgGarageInfoId;

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getGarageId() {
		return garageId;
	}

	public void setGarageId(String garageId) {
		this.garageId = garageId;
	}

	public String getSgGarageInfoId() {
		return sgGarageInfoId;
	}

	public void setSgGarageInfoId(String sgGarageInfoId) {
		this.sgGarageInfoId = sgGarageInfoId;
	}
}
