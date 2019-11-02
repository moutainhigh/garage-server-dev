package com.yixin.garage.dto.api;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.models.auth.In;

import javax.validation.constraints.NotNull;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class SgGarageForLoanQueryVehicleStateDTO {

	private static final long serialVersionUID = 413796382779349707L;

	/**
	 * 工单号（中瑞）
	 */
	private String jobNumber;

	/**
	 * *车架号
	 */
	@NotNull
	private String vin;

	/**
	 * *alix申请编号
	 */
	@NotNull
	private String alixNum;


	/**
	 * 车辆状态
	 */
	private Integer state;

	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getAlixNum() {
		return alixNum;
	}

	public void setAlixNum(String alixNum) {
		this.alixNum = alixNum;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public static void main(String[] args) {
		SgGarageForLoanQueryVehicleStateDTO dto = new SgGarageForLoanQueryVehicleStateDTO();
		dto.setJobNumber("12311");
		dto.setVin("LSVAT4NGXGN059088");
		dto.setAlixNum("1234556");
		dto.setState(1);
		System.out.println(JSON.toJSONString(dto));
	}

}

