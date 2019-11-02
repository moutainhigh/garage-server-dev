package com.yixin.garage.entity.order;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import java.sql.Blob;
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
 * @since 2019-08-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SgGarageOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Date actualStartTime;

    private String assignReason;

    private String cerNum;

    private String cerType;

    private String consignmentFee;

    private String contactTel;

    private String driverName;

    private String logiCompany;

    @TableField("logi_licNum")
    private String logiLicNum;

    private Integer orderStatus;

    private Date predictEndTime;

    @TableField("sgGarageCKDatail_id")
    private String sgGarageCKDatailId;

    @TableField("sgGarageRKDatail_id")
    private String sgGarageRKDatailId;


    /**
     * 出库车库
     */
    @TableField("sgGarageInfoFrom_id")
    private String sgGarageInfoFromId;

    /**
     * 入库车库
     */
    @TableField("sgGarageInfoTo_id")
    private String sgGarageInfoToId;

    @TableField("sgVehicle_id")
    private String sgVehicleId;

    private String shippingType;

    private String taskNum;

    private String remark;


}
