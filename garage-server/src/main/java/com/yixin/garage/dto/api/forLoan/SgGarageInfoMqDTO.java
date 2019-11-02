package com.yixin.garage.dto.api.forLoan;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.JsonObject;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class SgGarageInfoMqDTO {

	private static final long serialVersionUID = 413796382779349707L;


	/**
	 * *车库id
	 */
	private String id;

	/**
	 * *车库名称
	 */
	private String garageName;

	/**
	 * 车库状态
	 */
	private String garageStatus;

	private List<SgPersonDTO> manageerMqDTO;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGarageName() {
		return garageName;
	}

	public void setGarageName(String garageName) {
		this.garageName = garageName;
	}

	public String getGarageStatus() {
		return garageStatus;
	}

	public void setGarageStatus(String garageStatus) {
		this.garageStatus = garageStatus;
	}

	public List<SgPersonDTO> getManageerMqDTO() {
		return manageerMqDTO;
	}

	public void setManageerMqDTO(List<SgPersonDTO> manageerMqDTO) {
		this.manageerMqDTO = manageerMqDTO;
	}


//	public static void main(String[] args) {
//
//		SgGarageInfoMqDTO dto = new SgGarageInfoMqDTO();
//
//		List<SgPersonDTO> list = new ArrayList<>();
//		for (int i = 1; i <3 ; i++) {
//			SgPersonDTO personDTO = new SgPersonDTO();
//			personDTO.setName("库管姓名" + i);
//			personDTO.setPhone("库管手机号" + i);
//			personDTO.setUserName("库管userName" + i);
//			personDTO.setPersonId("库管id" + i);
//			list.add(personDTO);
//		}
//		dto.setId("车库id");
//		dto.setGarageName("车库名称");
//		dto.setGarageStatus("车库状态：启用、停用");
//		dto.setManageerMqDTO(list);
//		System.out.println(JSONObject.toJSON(dto));
//
//	}


}

