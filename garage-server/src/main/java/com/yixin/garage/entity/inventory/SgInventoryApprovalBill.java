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
public class SgInventoryApprovalBill extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 审批意见
     */
    private String approvalOpinion;

    /**
     * 审批状态
     */
    private Integer approvalState;

    /**
     * 最终完成时间
     */
    private Date finalizedTime;

    /**
     * 车库地址
     */
    private String garageAddresss;

    /**
     * 盘点home表id（外键）
     */
    private String inventoryHomeId;

    /**
     * 提交人姓名
     */
    private String submitterName;

    /**
     * 车库id
     */
    private String garageInfoId;

}
