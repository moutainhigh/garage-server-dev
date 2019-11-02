package com.yixin.garage.dto.vehicle;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yixin.common.utils.BaseDTO;
import com.yixin.common.utils.serializer.JsonDateDeserializer;
import com.yixin.common.utils.serializer.JsonDateSerializer;
import com.yixin.garage.dto.garage.SgGarageInfoDTO;
import com.yixin.garage.dto.sys.AttachSourceDTO;
import com.yixin.garage.enums.garage.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author YixinCapital -- liyaqing
 * @description: 安全车库-车辆信息
 * @date 2019/8/511:20
 */
@Data
public class SgVehicleInfoDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 车架号
     */
    private String vin;

    /**
     * 车身颜色
     */
    private String color;


    /**
     * 行驶里程
     */
    private BigDecimal mileage;

    /**
     * 车辆状态
     */
    private Integer stat;
    private String statStr;

    /**
     * 业务类型
     */
    private String businessType;
    private String businessTypeStr;

    /**
     * 车辆类型
     */
    private String propertyRightType;
    private String propertyRightTypeStr;

    /**
     * 牌照号
     */
    private String licNum;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 子品牌
     */
    private String brandModel;

    /**
     * 车型
     */
    private String vehicleClass;

    /**
     * 款型
     */
    private String model;

    /**
     * 品牌名称
     */
    private String brandStr;

    /**
     * 子品牌名称
     */
    private String brandModelStr;

    /**
     * 车型名称
     */
    private String vehicleClassStr;

    /**
     * 款型名称
     */
    private String modelStr;

    /**
     * 车库id
     */
    private String sgGarageInfoId;
    /**
     * 车辆所在车库名称
     */
    private String sgGarageName;

    /**
     * 车辆所在地址
     */
    private String sgGarageAddresss;

    private SgGarageInfoDTO sgGarageInfoDTO;// 停靠车库

    /**
     * 累计在库日期
     */
    private int actualStorageTimeInt;

    /**
     * 车辆来源
     */
    private String vehicleSource;
    private String vehicleSourceStr;

    /**
     * 是否有钥匙
     */
    private String isKey;

    /**
     * 是否正常启动
     */
    private String isNormalStart;

    /**
     * 是拖车入库
     */
    private String isDragGarage;

    /**
     * 损坏描述
     */
    private String damageDesc;

    /**
     * 处置商身份证号
     */
    private String disposeCerNum;

    /**
     * 处置商交接人员
     */
    private String disposeHandover;

    /**
     * 电瓶是否正常工作
     */
    private String isBatteryWork;

    /**
     * 是否存在行驶证
     */
    private String isDrivingLicense;

    /**
     * 是否配有新钥匙
     */
    private String isNewKey;

    /**
     * 是否有备胎
     */
    private String isSpare;

    /**
     * 轮胎型号是否一致
     */
    private String isTyreModel;

    /**
     * 车牌照是否在车上
     */
    private String isLicnumCar;

    /**
     * 左前轮胎编码
     */
    private String leftFrontNum;

    /**
     * 左后轮胎编码
     */
    private String leftRearNum;

    /**
     * 右前轮胎编码
     */
    private String rightFrontNum;

    /**
     * 右后轮胎编码
     */
    private String rightRearNum;

    /**
     * 承租人姓名
     */
    private String lessee;

    /**
     * 视频编码
     */
    private String videoNumber;

    /**
     * 首次入库日期
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date actualStorageTime;

    /**
     * 备注
     */
    private String remark;


    /**
     * alix申请编号
     */
    private String alixNum;

    /**
     * 油箱油量
     */
    private String oilQuantity;

    /**
     * GPS是否拆除
     */
    private String removeGps;

    /**
     * 车内物品
     */
    private String vehicleCross;

    /**
     * 车辆附件ID集合
     */
    private List<String> attIds;

    /**
     * 删除附件集合
     */
    private List<String> delAttIds;

    /**
     * 车辆附件信息集合
     */
    private List<AttachSourceDTO> vehcleAttList;

    /**
     * 车库管理员集合
     */
    private List<String> garageAdmins;

    /**
     * 订单单号
     */
    private String taskNum;

    /**
     * 实际出库日期
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date actualStartTime;

    /**
     * 判断是车辆信息查询还是临时出库查询 0-车辆信息 1-临时出库
     */
    private String sign;


    /**
     * 车型四级是否更新
     */
    private String isUpdate;

    /**
     * 车型四级是否更新
     */
    private String leaseProperty;

    private String leasePropertyStr;

    //订单查询页面时间区间查询
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date createTimeBegin;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date createTimeEnd;

    public String getLeasePropertyStr() {
        if (this.leaseProperty != null) {
            return LeasePropertyEnum.getDisplayNameByIndex(this.leaseProperty);
        } else {
            return "";
        }
    }

    public void setLeasePropertyStr(String leasePropertyStr) {
        this.leasePropertyStr = leasePropertyStr;
    }

    public String getBusinessTypeStr() {
        if (this.businessType != null) {
            return BusinessTypeEnum.getDisplayNameByIndex(this.businessType);
        } else {
            return "";
        }
    }

    public void setBusinessTypeStr() {
        this.businessTypeStr = businessTypeStr;
    }

    public String getPropertyRightTypeStr() {
        if (this.propertyRightType != null) {
            return VehicleTypeEnum.getDisplayNameByIndex(this.propertyRightType);
        } else {
            return "";
        }
    }

    public void setPropertyRightTypeStr(String propertyRightTypeStr) {
        this.propertyRightTypeStr = propertyRightTypeStr;
    }

    public String getStatStr() {
        if (this.stat != null) {
            return SgVehicleStatusEnum.getDisplayNameByIndex(this.stat);
        } else {
            return "";
        }
    }

    public void setStatStr(String statStr) {
        this.statStr = statStr;
    }

    public String getVehicleSourceStr() {
        if (this.vehicleSource != null) {
            return SgVehicleSourceEnum.getDisplayNameByIndex(this.vehicleSource);
        } else {
            return "";
        }
    }

    public void setVehicleSourceStr(String vehicleSourceStr) {
        this.vehicleSourceStr = vehicleSourceStr;
    }



}
