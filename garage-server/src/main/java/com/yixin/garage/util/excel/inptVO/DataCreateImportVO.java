package com.yixin.garage.util.excel.inptVO;


import com.yixin.garage.annotation.ExcelField;

import java.util.Date;

/**
 * @author YixinCapital -- libochen
 * @description: TODO
 * @date 2019/09/18 09:47
 */
public class DataCreateImportVO {

    /**
     * 车库名称
     */
    @ExcelField(title = "车库名称")
    private String garageName;

    /**
     * 库ID
     */
    @ExcelField(title = "库ID")
    private String garageId;

    /**
     * 库管员
     */
    @ExcelField(title = "库管员")
    private String contact;

    /**
     * 库管ID
     */
    @ExcelField(title = "库管ID")
    private String userId;

    /**
     * alix申请编号
     */
    @ExcelField(title = "申请编号")
    private String applyNo;

    /**
     * 车架号
     */
    @ExcelField(title = "车架号")
    private String vin;

    /**
     * 客户名字
     */
    @ExcelField(title = "客户名字")
    private String clientName;

    /**
     * 车牌号
     */
    @ExcelField(title = "车牌号")
    private String licNum;

    /**
     * 品牌车型
     */
    @ExcelField(title = "品牌车型")
    private String model;

    /**
     * 车辆颜色
     */
    @ExcelField(title = "车辆颜色")
    private String color;

    /**
     * 入库公里数
     */
    @ExcelField(title = "入库公里数")
    private String mileage;

    /**
     * 入库日期
     */
    @ExcelField(title = "入库日期")
    private Date actualStorageTime;

    /**
     * 业务类型
     */
    @ExcelField(title = "业务类型")
    private String businessType;

    /**
     * 是否有钥匙
     */
    @ExcelField(title = "是否有钥匙")
    private String isKey;


    /**
     * 车辆钥匙数
     */
    @ExcelField(title = "车辆钥匙数")
    private String keyNum;

    /**
     * 是否有行驶证
     */
    @ExcelField(title = "是否有行驶证")
    private String isDrivinglicense;

    /**
     * 是否能正常行驶
     */
    @ExcelField(title = "是否能正常行驶")
    private String isNormalStart;

    /**
     * 是否拖车入库
     */
    @ExcelField(title = "是否拖车入库")
    private String isDragGarage;

    /**
     * 是否新配钥匙
     */
    @ExcelField(title = "是否新配钥匙")
    private String hasNewKey;

    /**
     * 车牌是否在车上
     */
    @ExcelField(title = "车牌是否在车上")
    private String hasLicense;

    /**
     * 电瓶是否正常工作
     */
    @ExcelField(title = "电瓶是否正常工作")
    private String hasBattery;

    /**
     * 是否有备胎
     */
    @ExcelField(title = "是否有备胎")
    private String hasSpareTire;

    /**
     * 轮胎型号是否一致
     */
    @ExcelField(title = "轮胎型号是否一致")
    private String tyreUniformity;

    /**
     * 轮胎编码(左前)
     */
    @ExcelField(title = "轮胎编码(左前)")
    private String tyreCodingLeftFront;

    /**
     * 轮胎编码(左后)
     */
    @ExcelField(title = "轮胎编码(左后)")
    private String tyreCodingLeftBack;

    /**
     * 轮胎编码(右前)
     */
    @ExcelField(title = "轮胎编码(右前)")
    private String tyreCodingRightFront;

    /**
     * 轮胎编码(右后)
     */
    @ExcelField(title = "轮胎编码(右后)")
    private String tyreCodingRightBack;


    /**
     * 邮箱油量
     */
    @ExcelField(title = "邮箱油量")
    private String oilQuantity;

    /**
     * 车内物品
     */
    @ExcelField(title = "车内物品")
    private String incarArticles;

    /**
     * 损坏描述
     */
    @ExcelField(title = "损坏描述")
    private String damageDescript;

    /**
     * Gps是否删除
     */
    @ExcelField(title = "Gps是否删除")
    private String gpsDelete;

    /**
     * 承租人姓名
     */
    @ExcelField(title = "承租人姓名")
    private String lesseeName;

    /**
     * 处置商交接人员
     */
    @ExcelField(title = "处置商交接人员")
    private String disposalHandoverPersonnel;

    /**
     * 处置商身份证号
     */
    @ExcelField(title = "处置商身份证号")
    private String disposalHandoverPersonnelNum;

    /**
     * 租赁属性
     */
    @ExcelField(title = "业务属性")
    private String leaseProperty;

    /**
     * 入库类型
     */
    @ExcelField(title = "入库类型")
    private String tbCarType;

    /**
     * 收车人员
     */
    @ExcelField(title = "收车人员")
    private String colectPerson;

    /**
     * 收车人员证件号
     */
    @ExcelField(title = "收车人员证件号")
    private String colectPersonNum;


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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getApplyNo() {
        return applyNo;
    }

    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getLicNum() {
        return licNum;
    }

    public void setLicNum(String licNum) {
        this.licNum = licNum;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public Date getActualStorageTime() {
        return actualStorageTime;
    }

    public void setActualStorageTime(Date actualStorageTime) {
        this.actualStorageTime = actualStorageTime;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
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
}
