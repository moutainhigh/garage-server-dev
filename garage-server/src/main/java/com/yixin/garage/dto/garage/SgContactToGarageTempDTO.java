package com.yixin.garage.dto.garage;


import com.yixin.common.utils.BaseDTO;

public class SgContactToGarageTempDTO extends BaseDTO {


	private static final long serialVersionUID = 1L;

	private String contact;

	private String contactTel;

	private String garageInfoId;

	private String userName;

	private String userId;


	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getContactTel() {
		return contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	public String getGarageInfoId() {
		return garageInfoId;
	}

	public void setGarageInfoId(String garageInfoId) {
		this.garageInfoId = garageInfoId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}