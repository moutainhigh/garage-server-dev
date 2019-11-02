package com.yixin.garage.dto.api.forLoan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class SgGarageManageerMqDTO {

	private static final long serialVersionUID = 413796382779349707L;


	/**
	 * *车库id
	 */
	private String id;

	/**
	 * *库管信息
	 */
	private List<SgPersonDTO> personList;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<SgPersonDTO> getPersonList() {
		return personList;
	}

	public void setPersonList(List<SgPersonDTO> personList) {
		this.personList = personList;
	}
}

