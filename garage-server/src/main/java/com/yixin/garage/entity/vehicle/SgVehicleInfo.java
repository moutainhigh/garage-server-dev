package com.yixin.garage.entity.vehicle;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.yixin.garage.core.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author liyaqing
 * @since 2019-08-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SgVehicleInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Date actualStorageTime;

    private String alixNum;

    private String brand;

    private String brandModel;

    @TableField("brand_modelStr")
    private String brandModelStr;

    @TableField("brandStr")
    private String brandStr;

    private String businessType;

    private String color;

    @TableField("damageDesc")
    private String damageDesc;

    private String disposeCerNum;

    @TableField("dispose_Handover")
    private String disposeHandover;

    private String isBatteryWork;

    private String isDragGarage;

    private String isDrivingLicense;

    private String isKey;

    private String isNewKey;

    private String isNormalStart;

    @TableField("is_Spare")
    private String isSpare;

    private String isTyreModel;

    @TableField("is_licNum_Car")
    private String isLicnumCar;

    private String leftFrontNum;

    private String leftRearNum;

    private String lessee;

    private String licNum;

    private BigDecimal mileage;

    private String model;

    @TableField("modelStr")
    private String modelStr;

    private String oilQuantity;

    private String propertyRightType;

    private String remark;

    @TableField("remove_GPS")
    private String removeGps;

    private String rightFrontNum;

    private String rightRearNum;

    @TableField("sgGarageInfo_Id")
    private String sgGarageInfoId;

    private Integer stat;

    private String vehicleClass;

    @TableField("vehicle_classStr")
    private String vehicleClassStr;

    private String vehicleCross;

    private String vehicleSource;

    private String videoNumber;

    private String vin;

    /**
     * 车型四级是否更新
     */
    private Boolean isUpdate;

    /**
     * 租赁属性
     */
    private String leaseProperty;

}
