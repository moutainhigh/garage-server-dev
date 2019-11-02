package com.yixin.garage.dto.api.forLoan;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.models.auth.In;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class SgGarageForLoanGarageInDTO {

	private static final long serialVersionUID = 413796382779349707L;

	/**
	 * 入库车库
	 */
	@NotNull(message="入库车库不能为空")
	private String garageName;

	@NotNull(message="入库车库id不能为空")
	private String garageId;

	/**
	 * 车辆类型
	 */
	@NotNull(message="车辆类型不能为空")
	private String propertyRightType;

	/**
	 * 托运费
	 */
	private BigDecimal consignmentFee;

	/**
	 * *车架号
	 */
	@NotNull(message="车架号不能为空")
	private String vin;

	/**
	 * *alix申请编号
	 */
	@NotNull(message="alix申请编号不能为空")
	private String applyNo;

	/**
	 * 合同编号
	 */
	private String contractNo;

	/**
	 * 业务类型
	 */
	private String businessType;

	/**
	 * 入库时订单是否取消
	 */
	private String isCancel;


	/**
	 * 品牌
	 */
	private String brand;

	/**
	 * 品牌名称
	 */
//	@NotNull(message = "品牌名称不能为空")
	private String brandStr;

	/**
	 * 子品牌
	 */
	private String brandModel;

	/**
	 * 子品牌名称
	 */
//	@NotNull(message = "子品牌名称不能为空")
	private String brandModelStr;

	/**
	 * 车型
	 */
	private String vehicleClass;

	/**
	 * 车型名称
	 */
//	@NotNull(message = "车型名称不能为空")
	private String vehicleClassStr;

	/**
	 * 车款
	 */
	private String model;

	/**
	 * 车款名称
	 */
//	@NotNull(message = "车款名称不能为空")
	private String modelStr;

	/**
	 * 车牌号
	 */
	private String licNum;


	/**
	 * 颜色
	 */
//	@NotNull(message = "颜色不能为空")
	private String color;

	/**
	 * 里程
	 */
	@NotNull(message = "里程不能为空")
	private BigDecimal mileage;

	private String isKey;
	private String keyNum;
	private String isDrivinglicense;
	private String isNormalStart;
	private String isDragGarage;
	private String hasNewKey;
	private String hasLicense;
	private String hasBattery;
	private String hasSpareTire;
	private String tyreUniformity;
	private String tyreCodingLeftFront;
	private String tyreCodingLeftBack;
	private String tyreCodingRightFront;
	private String tyreCodingRightBack;
	private String oilQuantity;
	private String incarArticles;
	private String damageDescript;
	private String gpsDelete;
	private String lesseeName;
	private String disposalHandoverPersonnel;
	private String disposalHandoverPersonnelNum;
	private String tbCarType; //入库类型
	private String colectPerson;//收车人员
	private String colectPersonNum;//收车人员证件号
	/**
	 * 租赁属性
	 */
	private String leaseProperty;


	public String getGarageName() {
		return garageName;
	}

	public void setGarageName(String garageName) {
		this.garageName = garageName;
	}

	public String getGarageId() {
		return garageId;
	}

	public void setGarageId(String garageId) {
		this.garageId = garageId;
	}

	public String getPropertyRightType() {
		return propertyRightType;
	}

	public void setPropertyRightType(String propertyRightType) {
		this.propertyRightType = propertyRightType;
	}

	public BigDecimal getConsignmentFee() {
		return consignmentFee;
	}

	public void setConsignmentFee(BigDecimal consignmentFee) {
		this.consignmentFee = consignmentFee;
	}

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

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(String isCancel) {
		this.isCancel = isCancel;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getBrandStr() {
		return brandStr;
	}

	public void setBrandStr(String brandStr) {
		this.brandStr = brandStr;
	}

	public String getBrandModel() {
		return brandModel;
	}

	public void setBrandModel(String brandModel) {
		this.brandModel = brandModel;
	}

	public String getBrandModelStr() {
		return brandModelStr;
	}

	public void setBrandModelStr(String brandModelStr) {
		this.brandModelStr = brandModelStr;
	}

	public String getVehicleClass() {
		return vehicleClass;
	}

	public void setVehicleClass(String vehicleClass) {
		this.vehicleClass = vehicleClass;
	}

	public String getVehicleClassStr() {
		return vehicleClassStr;
	}

	public void setVehicleClassStr(String vehicleClassStr) {
		this.vehicleClassStr = vehicleClassStr;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getModelStr() {
		return modelStr;
	}

	public void setModelStr(String modelStr) {
		this.modelStr = modelStr;
	}

	public String getLicNum() {
		return licNum;
	}

	public void setLicNum(String licNum) {
		this.licNum = licNum;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public BigDecimal getMileage() {
		return mileage;
	}

	public void setMileage(BigDecimal mileage) {
		this.mileage = mileage;
	}

	public String getIsKey() {
		return isKey;
	}

	public void setIsKey(String isKey) {
		this.isKey = isKey;
	}

	public String getKeyNum() {
		return keyNum;
	}

	public void setKeyNum(String keyNum) {
		this.keyNum = keyNum;
	}

	public String getIsDrivinglicense() {
		return isDrivinglicense;
	}

	public void setIsDrivinglicense(String isDrivinglicense) {
		this.isDrivinglicense = isDrivinglicense;
	}

	public String getIsNormalStart() {
		return isNormalStart;
	}

	public void setIsNormalStart(String isNormalStart) {
		this.isNormalStart = isNormalStart;
	}

	public String getIsDragGarage() {
		return isDragGarage;
	}

	public void setIsDragGarage(String isDragGarage) {
		this.isDragGarage = isDragGarage;
	}

	public String getHasNewKey() {
		return hasNewKey;
	}

	public void setHasNewKey(String hasNewKey) {
		this.hasNewKey = hasNewKey;
	}

	public String getHasLicense() {
		return hasLicense;
	}

	public void setHasLicense(String hasLicense) {
		this.hasLicense = hasLicense;
	}

	public String getHasBattery() {
		return hasBattery;
	}

	public void setHasBattery(String hasBattery) {
		this.hasBattery = hasBattery;
	}

	public String getHasSpareTire() {
		return hasSpareTire;
	}

	public void setHasSpareTire(String hasSpareTire) {
		this.hasSpareTire = hasSpareTire;
	}

	public String getTyreUniformity() {
		return tyreUniformity;
	}

	public void setTyreUniformity(String tyreUniformity) {
		this.tyreUniformity = tyreUniformity;
	}

	public String getTyreCodingLeftFront() {
		return tyreCodingLeftFront;
	}

	public void setTyreCodingLeftFront(String tyreCodingLeftFront) {
		this.tyreCodingLeftFront = tyreCodingLeftFront;
	}

	public String getTyreCodingLeftBack() {
		return tyreCodingLeftBack;
	}

	public void setTyreCodingLeftBack(String tyreCodingLeftBack) {
		this.tyreCodingLeftBack = tyreCodingLeftBack;
	}

	public String getTyreCodingRightFront() {
		return tyreCodingRightFront;
	}

	public void setTyreCodingRightFront(String tyreCodingRightFront) {
		this.tyreCodingRightFront = tyreCodingRightFront;
	}

	public String getTyreCodingRightBack() {
		return tyreCodingRightBack;
	}

	public void setTyreCodingRightBack(String tyreCodingRightBack) {
		this.tyreCodingRightBack = tyreCodingRightBack;
	}

	public String getOilQuantity() {
		return oilQuantity;
	}

	public void setOilQuantity(String oilQuantity) {
		this.oilQuantity = oilQuantity;
	}

	public String getIncarArticles() {
		return incarArticles;
	}

	public void setIncarArticles(String incarArticles) {
		this.incarArticles = incarArticles;
	}

	public String getDamageDescript() {
		return damageDescript;
	}

	public void setDamageDescript(String damageDescript) {
		this.damageDescript = damageDescript;
	}

	public String getGpsDelete() {
		return gpsDelete;
	}

	public void setGpsDelete(String gpsDelete) {
		this.gpsDelete = gpsDelete;
	}

	public String getLesseeName() {
		return lesseeName;
	}

	public void setLesseeName(String lesseeName) {
		this.lesseeName = lesseeName;
	}

	public String getDisposalHandoverPersonnel() {
		return disposalHandoverPersonnel;
	}

	public void setDisposalHandoverPersonnel(String disposalHandoverPersonnel) {
		this.disposalHandoverPersonnel = disposalHandoverPersonnel;
	}

	public String getDisposalHandoverPersonnelNum() {
		return disposalHandoverPersonnelNum;
	}

	public void setDisposalHandoverPersonnelNum(String disposalHandoverPersonnelNum) {
		this.disposalHandoverPersonnelNum = disposalHandoverPersonnelNum;
	}

	public String getTbCarType() {
		return tbCarType;
	}

	public void setTbCarType(String tbCarType) {
		this.tbCarType = tbCarType;
	}

	public String getColectPerson() {
		return colectPerson;
	}

	public void setColectPerson(String colectPerson) {
		this.colectPerson = colectPerson;
	}

	public String getColectPersonNum() {
		return colectPersonNum;
	}

	public void setColectPersonNum(String colectPersonNum) {
		this.colectPersonNum = colectPersonNum;
	}

	public String getLeaseProperty() {
		return leaseProperty;
	}

	public void setLeaseProperty(String leaseProperty) {
		this.leaseProperty = leaseProperty;
	}

	//	public static void main(String[] args) {
//		SgGarageForLoanGarageInDTO dto = new SgGarageForLoanGarageInDTO();
//		dto.setGarageIn("鹤岗车库");
//		dto.setPropertyRightType("资源车");
//		dto.setConsignmentFee(new BigDecimal(123.00));
//		dto.setVin("LSVAT4NGXGN059088");
//		dto.setAlixNum("1234556");
//		dto.setContractNo("5431221");
//		dto.setBusinessType(1);
//		dto.setBrand("84001");
//		dto.setBrandStr("奧迪");
//		dto.setBrandModel("85012");
//		dto.setBrandModelStr("一汽大众-奥迪");
//		dto.setVehicleClass("3999");
//		dto.setVehicleClassStr("奥迪A3");
//		dto.setModel("126313");
//		dto.setModelStr("1.4T 双离合 30周年纪念版 Sportback 35TFSI 进取型 2018款");
//		dto.setLicNum("车牌号");
//		dto.setColor("颜色");
//		dto.setMileage(new BigDecimal(123123));
//		System.out.println(JSON.toJSONString(dto));
//
//	}










//	@Override
//	public String toString() {
//		return "LoanQywxDTO{" +
//				", queryType=" + queryType +
//				", bussNo='" + bussNo + '\'' +
//				", auditResult=" + auditResult +
//				", auditRemark='" + auditRemark + '\'' +
//				", bussType='" + bussType + '\'' +
//				'}';
//	}
}

