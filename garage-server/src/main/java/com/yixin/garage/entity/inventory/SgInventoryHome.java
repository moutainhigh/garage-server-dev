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
public class SgInventoryHome extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 任务单号
     */
    private String billNum;

    /**
     * 任务结束时间
     */
    private Date endTime;

    /**
     * 任务开始时间
     */
    private Date startTime;

    /**
     * 发布状态
     */
    private Integer state;

    /**
     * 指定车库类型
     */
    private String appoint;


    private String garageIds;

}
