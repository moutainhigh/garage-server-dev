package com.yixin.garage.dto.api.forLoan;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;


@JsonIgnoreProperties(ignoreUnknown = true)
public class SgGarageForLoanGarageOutDTO {

	private static final long serialVersionUID = 413796382779349707L;


	/**
	 * *车架号
	 */
	@NotNull
	private String vin;

	/**
	 * *alix申请编号
	 */
	@NotNull
	private String applyNo;

	/**
	 * 出库原因
	 */
	@NotNull
	private String outReason;


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

	public String getOutReason() {
		return outReason;
	}

	public void setOutReason(String outReason) {
		this.outReason = outReason;
	}

	public static void main(String[] args) {
		SgGarageForLoanGarageOutDTO dto = new SgGarageForLoanGarageOutDTO();
		dto.setVin("LSVAT4NGXGN059088");
		dto.setApplyNo("1234556");
//		dto.setReason("出库原因");
		System.out.println(JSON.toJSONString(dto));

	}

}

