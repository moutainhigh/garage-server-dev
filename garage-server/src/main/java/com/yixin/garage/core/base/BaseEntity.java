package com.yixin.garage.core.base;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;


public abstract class BaseEntity extends Model<BaseEntity> {

	private static final long serialVersionUID = 1L;

	@TableId(value = "ID", type = IdType.UUID)
	private String id;

	/***
	 * 创建时间
	 */
	@TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
	private Date createTime;

	/***
	 * 创建人ID
	 */
	@TableField(value = "CREATOR_ID", fill = FieldFill.INSERT)
	private String creatorId;
	/**
	 * 创建人姓名
	 */
	@TableField(value = "CREATOR_NAME", fill = FieldFill.INSERT)
	private String creatorName;

	/**
	 * 创建人所属部门id
	 */
	@TableField(value = "CREATOR_DEPARTMENT_ID", fill = FieldFill.INSERT)
	private Long creatorDepartmentId;
	/**
	 * 创建人所属部门名称
	 */
	@TableField(value = "CREATOR_DEPARTMENT_NAME", fill = FieldFill.INSERT)
	private String creatorDepartmentName;

	/***
	 * 修改时间
	 */
	@TableField(value = "UPDATE_TIME", fill = FieldFill.UPDATE)
	private Date updateTime;
	/***
	 * 修改人域账号
	 */
	@TableField(value = "UPDATOR_ID", fill = FieldFill.UPDATE)
	private String updatorId;
	/**
	 * 修改人姓名
	 */
	@TableField(value = "UPDATOR_NAME", fill = FieldFill.UPDATE)
	private String updatorName;
	/**
	 * 修改人所属部门id
	 */
	@TableField(value = "UPDATOR_DEPARTMENT_ID", fill = FieldFill.UPDATE)
	private Long updatorDepartmentId;
	/**
	 * 修改人所属部门名称
	 */
	@TableField(value = "UPDATOR_DEPARTMENT_NAME", fill = FieldFill.UPDATE)
	private String updatorDepartmentName;

	/**
	 * 版本号
	 */
	@TableField("VERSION")
	@Version
	private Integer version = 0;
	/***
	 * 是否删除状态，true为已删除，false为未删除
	 */
	@TableField("IS_DELETED")
	@TableLogic
	private Boolean deleted = false;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Long getCreatorDepartmentId() {
		return creatorDepartmentId;
	}

	public void setCreatorDepartmentId(Long creatorDepartmentId) {
		this.creatorDepartmentId = creatorDepartmentId;
	}

	public String getCreatorDepartmentName() {
		return creatorDepartmentName;
	}

	public void setCreatorDepartmentName(String creatorDepartmentName) {
		this.creatorDepartmentName = creatorDepartmentName;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdatorId() {
		return updatorId;
	}

	public void setUpdatorId(String updatorId) {
		this.updatorId = updatorId;
	}

	public String getUpdatorName() {
		return updatorName;
	}

	public void setUpdatorName(String updatorName) {
		this.updatorName = updatorName;
	}

	public Long getUpdatorDepartmentId() {
		return updatorDepartmentId;
	}

	public void setUpdatorDepartmentId(Long updatorDepartmentId) {
		this.updatorDepartmentId = updatorDepartmentId;
	}

	public String getUpdatorDepartmentName() {
		return updatorDepartmentName;
	}

	public void setUpdatorDepartmentName(String updatorDepartmentName) {
		this.updatorDepartmentName = updatorDepartmentName;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

}
