package com.yixin.garage.dto.sys;


import com.yixin.common.utils.BaseDTO;

/**
 * 上传文件dto Package : com.yixin.rentcar.dto.common
 * 
 * @author YixinCapital -- qinyunfei 2016年6月24日 下午4:42:57
 *
 */
public class UploadFileDTO extends BaseDTO {

	private static final long serialVersionUID = 6867434054821572135L;

	/**
	 * 业务对象类型
	 */
	private String yxObjType;
	
	/**
	 * 文件大小
	 */
	private String fileSize; 
	
	/**
	 * 附件地址
	 */
	private String yxAttachmentUrl;
	
	private String token;

	/**
	 * 文件流
	 */
	private byte[] stream;

	/**
	 * 文件名称
	 */
	private String fileName;

	/**
	 * 业务类型
	 */
	private String fileType;

	/**
	 * 客户id
	 */
	private String custId;

	/**
	 * “调配任务”ID
	 */
	private String yxGarageOrder_ID;
	
	/**
	 * 上牌主单ID
	 */
	private String yxLicenseCardId;
	
	// pangguowei add 2017-5-27 导入分配  start
	/**
	 * 分配管理-导入分配-采购编号
	 */
	private String billNum;
	
	/**
	 * 分配管理-导入分配-车款ID
	 */
	private String model;
	
	/**
	 * 分配管理-导入分配-车辆来源
	 */
	private String vehicleSource;
	
	/**
	 * 分配管理-导入分配-车辆颜色
	 */
	private String color;
	
	/**
	 * 在库车辆管理-上传附件
	 */
	private String yxVehicleInfoId;
	// pangguowei add 2017-5-27 导入分配  end
	
	/**
	 * 采购单主单id
	 */
	private String rcDcVehiclePurcheseBillId;
	
	/**
	 * 采购详单id
	 */
	private String rcDcVehiclePurcheseDetailId;
	/**
	 * 付款申请主单yx_payment_purchase_bill表id
	 */
	private String yxPaymentPurchaseBillId;
	
	/**
	 * 车辆ID
	 */
	private String vehicleId;
	
	/**
	 * 业务ID
	 */
	private String businessId;
	
	public String getYxPaymentPurchaseBillId() {
		return yxPaymentPurchaseBillId;
	}

	public void setYxPaymentPurchaseBillId(String yxPaymentPurchaseBillId) {
		this.yxPaymentPurchaseBillId = yxPaymentPurchaseBillId;
	}

	public String getRcDcVehiclePurcheseBillId() {
		return rcDcVehiclePurcheseBillId;
	}

	public void setRcDcVehiclePurcheseBillId(String rcDcVehiclePurcheseBillId) {
		this.rcDcVehiclePurcheseBillId = rcDcVehiclePurcheseBillId;
	}

	public String getRcDcVehiclePurcheseDetailId() {
		return rcDcVehiclePurcheseDetailId;
	}

	public void setRcDcVehiclePurcheseDetailId(String rcDcVehiclePurcheseDetailId) {
		this.rcDcVehiclePurcheseDetailId = rcDcVehiclePurcheseDetailId;
	}

	public String getYxLicenseCardId() {
		return yxLicenseCardId;
	}

	public void setYxLicenseCardId(String yxLicenseCardId) {
		this.yxLicenseCardId = yxLicenseCardId;
	}

	public String getYxObjType() {
		return yxObjType;
	}

	public void setYxObjType(String yxObjType) {
		this.yxObjType = yxObjType;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getYxAttachmentUrl() {
		return yxAttachmentUrl;
	}

	public void setYxAttachmentUrl(String yxAttachmentUrl) {
		this.yxAttachmentUrl = yxAttachmentUrl;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getFileName() {
		return fileName;
	}

	public byte[] getStream() {
		return stream;
	}

	public void setStream(byte[] stream) {
		this.stream = stream;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getYxGarageOrder_ID() {
		return yxGarageOrder_ID;
	}

	public void setYxGarageOrder_ID(String yxGarageOrder_ID) {
		this.yxGarageOrder_ID = yxGarageOrder_ID;
	}

	/**
	 * @return the billNum
	 */
	public String getBillNum() {
		return billNum;
	}

	/**
	 * @param billNum the billNum to set
	 */
	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}

	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * @return the vehicleSource
	 */
	public String getVehicleSource() {
		return vehicleSource;
	}

	/**
	 * @param vehicleSource the vehicleSource to set
	 */
	public void setVehicleSource(String vehicleSource) {
		this.vehicleSource = vehicleSource;
	}

	/**
	 * @return the yxVehicleInfoId
	 */
	public String getYxVehicleInfoId() {
		return yxVehicleInfoId;
	}

	/**
	 * @param yxVehicleInfoId the yxVehicleInfoId to set
	 */
	public void setYxVehicleInfoId(String yxVehicleInfoId) {
		this.yxVehicleInfoId = yxVehicleInfoId;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

}
