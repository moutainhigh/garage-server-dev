package com.yixin.garage.dto.api.forLoan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class SgPersonDTO {

	private static final long serialVersionUID = 413796382779349707L;


	/**
	 * *库管id
	 */
	private String personId;

	/**
	 * *姓名
	 */
	private String name;

	/**
	 * 联系方式
	 */
	private String phone;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 操作说明 1=新增，2=更新，3=逻辑删除
	 */
	private String operationExplain;


	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOperationExplain() {
		return operationExplain;
	}

	public void setOperationExplain(String operationExplain) {
		this.operationExplain = operationExplain;
	}
}

