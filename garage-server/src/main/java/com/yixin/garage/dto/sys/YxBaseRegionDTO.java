package com.yixin.garage.dto.sys;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 基础数据-行政地区 Package : com.yixin.rentcar.dto.basic
 *
 * @author YixinCapital -- liutao1 2016年6月4日 下午3:11:49
 *
 */
public class YxBaseRegionDTO {

	/**
	 * 源ID 
	 */
	@JsonProperty("SID")
	private Long sid; 

	/**
	 * ID 
	 */
	@JsonProperty("ID")
	private String ID; 

	/**
	 * 源类型
	 */
	private int stype;

	/**
	 * 父节点ID
	 */
	private String parentId; 

	/**
	 * 地区名称
	 */
	@JsonProperty("Name")
	private String name; 
	
	/**
	 * 地区拼音
	 */
	@JsonProperty("Spell")
	private String spell; 
	
	/**
	 * 地区全名
	 */
	@JsonProperty("FullName")
	private String fullName; 

	/**
	 * 地区级别（1：省，2：市）
	 */
	@JsonProperty("Level")
	private Integer level; 

	/**
	 * 排序号
	 */
	@JsonProperty("OrderNumber")
	private Integer orderNumber; 

	/**
	 * 行政区号
	 */
	@JsonProperty("RegionId")
	private Integer regionId; 

	public Long getSid() {
		return sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}

	public int getStype() {
		return stype;
	}

	public void setStype(int stype) {
		this.stype = stype;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpell() {
		return spell;
	}

	public void setSpell(String spell) {
		this.spell = spell;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

}
