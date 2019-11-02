package com.yixin.garage.dto.api.rentcar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yixin.common.utils.serializer.JsonDateDeserializer;
import com.yixin.common.utils.serializer.JsonDateSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author YixinCapital -- liyaqing
 * @description: TODO
 * @date 2019/9/1615:58
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SgVehicleByFreeDTO {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 车架号
     */
    private String vin;

    /**
     * alix申请编号
     */
    private String alixNum;

    /**
     * 行驶里程
     */
    private BigDecimal mileage;


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
    private String garageInfoId;
    /**
     * 车辆所在车库名称
     */
    private String garageName;

    /**
     * 车辆所在地址
     */
    private String garageAddresss;

    /**
     * 首次入库日期
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date actualStorageTime;

    /**
     * 所在省份
     */
    private String province;

    /**
     * 所在省份名称
     */
    private String provinceStr;

    /**
     * 所在城市
     */
    private String city;

    /**
     * 所在城市名称
     */
    private String cityStr;

    /**
     * 租赁属性(存汉字)
     */
    private String leaseProperty;


}
