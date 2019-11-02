package com.yixin.garage.entity.inventory;

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
 * @since 2019-10-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SgInventoryApprovalDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 审核状态
     */
    private Integer approvalStatus;

    /**
     * 不合格原因
     */
    private String unqualifiedReasons;

    /**
     * 实际颜色
     */
    private String actualColor;

    /**
     * 实际具体停放地址
     */
    private String actualParkAddress;

    /**
     * 实际具体停放车库
     */
    private String actualGarageName;

    /**
     * 实际入库日期
     */
    private Date actualStorageTime;

    private String color;

    private String garageAddresss;

    /**
     * 外观是否损坏
     */
    private String isAppearanceDamage;

    /**
     * 外观是否损坏说明
     */
    private String appearanceDamageDescription;

    /**
     * 是否盘点
     */
    private Boolean inventory;

    /**
     * 是否为手动增加
     */
    private Boolean manualAdd;

    /**
     * 牌照号
     */
    private String licNum;

    /**
     * 盘点日期
     */
    private Date pandianDate;

    /**
     * 盘点人姓名
     */
    private String pandianName;

    /**
     * 主单id
     */
    private String inventoryApprovalBillId;

    /**
     * 盘点结果类型
     */
    private Integer resultType;

    /**
     * 盘点结果类型具体情况说明
     */
    private String resultTypeDescription;

    /**
     * 车型名称
     */
    @TableField("vehicle_classStr")
    private String vehicleClassstr;

    /**
     * 品牌(手动新增车辆使用）
     */
    private String brandStr;

    /**
     * 车辆id
     */
    private String vehicleInfoId;

    /**
     * 车架号
     */
    private String vin;

    /**
     * 车架号
     */
    private String alixNum;

}
