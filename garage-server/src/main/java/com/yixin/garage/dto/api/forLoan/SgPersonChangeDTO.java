package com.yixin.garage.dto.api.forLoan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;


@JsonIgnoreProperties(ignoreUnknown = true)
public class SgPersonChangeDTO {

	private static final long serialVersionUID = 413796382779349707L;


	/**
	 * *新增或删除数据
	 */
	private Map<String, String> diffMap;

	/**
	 * *是否变更
	 */
	private Boolean flag;


	public Map<String, String> getDiffMap() {
		return diffMap;
	}

	public void setDiffMap(Map<String, String> diffMap) {
		this.diffMap = diffMap;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}
}

