package com.yixin.garage.dto.garage;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yixin.common.utils.BaseDTO;
import com.yixin.common.utils.serializer.JsonDateDeserializer;
import com.yixin.common.utils.serializer.JsonDateSerializer;
import com.yixin.garage.dto.api.forLoan.SgPersonDTO;
import com.yixin.garage.dto.sys.AttachSourceDTO;
import com.yixin.garage.enums.garage.GarageStatusEnum;
import com.yixin.garage.enums.garage.InvoiceTypeEnum;
import com.yixin.garage.enums.garage.SgOperateAttrEnum;
import com.yixin.garage.enums.garage.TenancySignEnum;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 车库信息DTO Package : com.yixin.rentcar.dto.inventory
 * 
 * @author YixinCapital -- qinyunfei 2016年5月26日 下午7:25:06
 *
 */
public class SgGarageInfoDTO extends BaseDTO {

	/**
	 * 经营属性 ( 0-自营,1-加盟商 )
	 */
	@NotNull(message = "经营属性不能为空")
	private Integer operateAttr;

	private String operateAttrStr;

	/**
	 * 车库状态 ( 枚举类GarageStatus：0-正常，1-盘库中，2-停用 )
	 */
	@NotNull(message = "车库状态不能为空")
	private Integer garageStatus;

	private String garageStatusStr;

	/**
	 * 车库编号
	 */
	private String garageNum;

	/**
	 * 车库名称
	 */
	@NotNull(message = "车库名称不能为空")
	private String garageName;

	/**
	 * 车位状态 (枚举：0-未满，1-已满)
	 */
	private Integer parkingStatus = 0;

	/**
	 * 所在省份
	 */
	@NotNull(message = "所在省份不能为空")
	private String province;

	/**
	 * 所在省份名称
	 */
	private String provinceStr;

	/**
	 * 所在城市
	 */
	@NotNull(message = "所在城市不能为空")
	private String city;

	/**
	 * 所在城市名称
	 */
	private String cityStr;

	/**
	 * 所在区县
	 */
	private String country;

	/**
	 * 所在区县名称
	 */
	private String countryStr;

	/**
	 * 车库地址
	 */
	@NotNull(message = "具体地址不能为空")
	private String garageAddresss;

	/**
	 * 总车位数
	 */
	@NotNull(message = "总车位数不能为空")
	private Integer parkingNum;

	/**
	 * 已使用车位数
	 */
	private Integer parkedNum = 0;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 附件id
	 */
	private String attachmentId;

	/**
	 * 附件url
	 */
	private String attachmentUrl;

	/**
	 * 附件名称
	 */
	private String attachmentName;

	/**
	 * 租期开始日期
	 */
	@NotNull(message = "租期开始日期不能为空")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date tenancyBeginDate;

	/**
	 * 租期结束日期
	 */
	@NotNull(message = "租期结束日期不能为空")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date tenancyEndDate;

	/**
	 * 联系人
	 */
	private String contact;

	/**
	 * 联系人电话
	 */
	private String contactTele;

	/***
	 * 当前页码
	 */
//	private int current = 1;

	/**
	 * 车辆数量
	 */
	private int carNum;
	

	/**
	 * 接车人身份证ID
	 */
	private String contactCardID;

	/**
	 * 租期剩余一个月标记
	 */
	private Integer tenancySign;
	private String tenancySignStr;


	/**
	 * 停车费-小时
	 */
	@NotNull(message = "停车费-小时不能为空")
	private BigDecimal parkingCostHour;

	/**
	 * 停车费-天
	 */
	@NotNull(message = "停车费-天不能为空")
	private BigDecimal parkingCostDay;

	/**
	 * 停车费-月
	 */
	@NotNull(message = "停车费-月不能为空")
	private BigDecimal parkingCostMonth;

	/**
	 * 发票类型
	 */
	@NotNull(message = "发票类型不能为空")
	private Integer invoiceType;

	private String invoiceTypeStr;

	/**
	 * *库管信息
	 */
	private List<SgPersonDTO> personList;

	/**
	 * *附件列表
	 */
	private List<AttachSourceDTO> garageAttList;


	public Integer getTenancySign() {
		return tenancySign;
	}

	public void setTenancySign(Integer tenancySign) {
		this.tenancySign = tenancySign;
	}

	public String getTenancySignStr() {
		if (this.tenancySign != null) {
			return TenancySignEnum.getDisplayNameByIndex(this.tenancySign);
		} else {
			return "";
		}
	}

	public void setTenancySignStr(String tenancySignStr) {
		this.tenancySignStr = tenancySignStr;
	}



	public String getOperateAttrStr() {
		if (this.operateAttr != null) {
			return SgOperateAttrEnum.getDisplayNameByIndex(this.operateAttr);
		} else {
			return "";
		}
	}

	public void setOperateAttrStr(String operateAttrStr) {
		this.operateAttrStr = operateAttrStr;
	}

	public String getGarageStatusStr() {
		if (this.garageStatus != null) {
			return GarageStatusEnum.getDisplayNameByIndex(this.garageStatus);
		} else {
			return "";
		}
	}

	public void setGarageStatusStr(String garageStatusStr) {
		this.garageStatusStr = garageStatusStr;
	}

	public Integer getOperateAttr() {
		return operateAttr;
	}

	public void setOperateAttr(Integer operateAttr) {
		this.operateAttr = operateAttr;
	}

	public Integer getGarageStatus() {
		return garageStatus;
	}

	public void setGarageStatus(Integer garageStatus) {
		this.garageStatus = garageStatus;
	}

	public String getGarageNum() {
		return garageNum;
	}

	public void setGarageNum(String garageNum) {
		this.garageNum = garageNum;
	}

	public String getGarageName() {
		return garageName;
	}

	public void setGarageName(String garageName) {
		this.garageName = garageName;
	}

	public Integer getParkingStatus() {
		return parkingStatus;
	}

	public void setParkingStatus(Integer parkingStatus) {
		this.parkingStatus = parkingStatus;
	}

	public String getGarageAddresss() {
		return garageAddresss;
	}

	public void setGarageAddresss(String garageAddresss) {
		this.garageAddresss = garageAddresss;
	}

	public Integer getParkingNum() {
		return parkingNum;
	}

	public void setParkingNum(Integer parkingNum) {
		this.parkingNum = parkingNum;
	}

	public Integer getParkedNum() {
		return parkedNum;
	}

	public void setParkedNum(Integer parkedNum) {
		this.parkedNum = parkedNum;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


//	public int getCurrent() {
//		return current;
//	}

//	public void setCurrent(int current) {
//		this.current = current;
//	}

	public Date getTenancyBeginDate() {
		return tenancyBeginDate;
	}

	public void setTenancyBeginDate(Date tenancyBeginDate) {
		this.tenancyBeginDate = tenancyBeginDate;
	}

	public Date getTenancyEndDate() {
		return tenancyEndDate;
	}

	public void setTenancyEndDate(Date tenancyEndDate) {
		this.tenancyEndDate = tenancyEndDate;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getContactTele() {
		return contactTele;
	}

	public void setContactTele(String contactTele) {
		this.contactTele = contactTele;
	}

	public String getProvinceStr() {
		return provinceStr;
	}

	public void setProvinceStr(String provinceStr) {
		this.provinceStr = provinceStr;
	}

	public String getCityStr() {
		return cityStr;
	}

	public void setCityStr(String cityStr) {
		this.cityStr = cityStr;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountryStr() {
		return countryStr;
	}

	public void setCountryStr(String countryStr) {
		this.countryStr = countryStr;
	}

	public int getCarNum() {
		return carNum;
	}

	public void setCarNum(int carNum) {
		this.carNum = carNum;
	}


	public String getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}

	public String getAttachmentUrl() {
		return attachmentUrl;
	}

	public void setAttachmentUrl(String attachmentUrl) {
		this.attachmentUrl = attachmentUrl;
	}

	public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	public List<SgPersonDTO> getPersonList() {
		return personList;
	}

	public void setPersonList(List<SgPersonDTO> personList) {
		this.personList = personList;
	}

	public BigDecimal getParkingCostHour() {
		return parkingCostHour;
	}

	public void setParkingCostHour(BigDecimal parkingCostHour) {
		this.parkingCostHour = parkingCostHour;
	}

	public BigDecimal getParkingCostDay() {
		return parkingCostDay;
	}

	public void setParkingCostDay(BigDecimal parkingCostDay) {
		this.parkingCostDay = parkingCostDay;
	}

	public BigDecimal getParkingCostMonth() {
		return parkingCostMonth;
	}

	public void setParkingCostMonth(BigDecimal parkingCostMonth) {
		this.parkingCostMonth = parkingCostMonth;
	}

	public Integer getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(Integer invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getInvoiceTypeStr() {
		if (this.invoiceType != null) {
			return InvoiceTypeEnum.getDisplayNameByIndex(this.invoiceType);
		} else {
			return "";
		}
	}

	public List<AttachSourceDTO> getGarageAttList() {
		return garageAttList;
	}

	public void setGarageAttList(List<AttachSourceDTO> garageAttList) {
		this.garageAttList = garageAttList;
	}

	public void setInvoiceTypeStr(String invoiceTypeStr) {
		this.invoiceTypeStr = invoiceTypeStr;
	}

}