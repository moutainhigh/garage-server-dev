package com.yixin.garage.dto.api.forLoan;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;


@JsonIgnoreProperties(ignoreUnknown = true)
public class SgQueryVehicleIfoStatDTO {

	private static final long serialVersionUID = 413796382779349707L;


	/**
	 * *车架号
	 */
	private String vin;

	/**
	 * *alix申请编号
	 */
	private String applyNo;

	/**
	 * 车辆状态
	 */
	private Integer state;

	/**
	 * 所在车库名称
	 */
	@NotNull
	private String garageName;

	/**
	 * 所在车库id
	 */
	@NotNull
	private String garageId;

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getApplyNo() {
		return applyNo;
	}

	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getGarageName() {
		return garageName;
	}

	public void setGarageName(String garageName) {
		this.garageName = garageName;
	}

	public String getGarageId() {
		return garageId;
	}

	public void setGarageId(String garageId) {
		this.garageId = garageId;
	}
}

