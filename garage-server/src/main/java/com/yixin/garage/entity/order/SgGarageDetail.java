package com.yixin.garage.entity.order;

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
 * @since 2019-08-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SgGarageDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String alixNum;

    private Integer billStatus;

    private String colectPerson;

    private String colectPersonNum;

    @TableField("contract_No")
    private String contractNo;

    private Integer garageSign;

    private String isCancel;

    private String orderType;

    private Integer outReason;

    private Integer pushSource;

    private String remark;

    @TableField("sgGaragOrder_id")
    private String sgGaragOrderId;

    @TableField("sgVehicle_id")
    private String sgVehicleId;

    private String taskNum;

    private String vin;

    private Date tempActualTime;

    private Date tempPredictTime;


}
