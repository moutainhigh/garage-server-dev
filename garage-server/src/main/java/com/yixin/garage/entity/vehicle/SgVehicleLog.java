package com.yixin.garage.entity.vehicle;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.yixin.garage.core.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author YixinCapital -- liyaqing
 * @description: 安全车库 - 车辆信息日志
 * @date 2019/8/510:41
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SgVehicleLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Date actualStorageTime;

    private String businessId;

    private String event;

    private Integer eventcode;

    private String garageId;

    private String garageName;

    private String remark;

    @TableField("taskBill_id")
    private String taskbillId;

    private String vehicleId;

    private String garageOrderId;


}
