package com.yixin.garage.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.yixin.garage.core.base.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author lizhongxin
 * @since 2019-01-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_audit_log")
public class AuditLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 业务记录id
     */
    private String bussId;

    /**
     * 业务状态编号
     */
    private String bussStatusCode;

    /**
     * 业务状态名称
     */
    private String bussStatusName;

    /**
     * 处理备注
     */
    private String dealRemark;

    /**
     * 处理时间 
     */
    private Date dealTime;

    /**
     * 处理人域账号
     */
    private String dealUserAccount;

    /**
     * 处理人姓名
     */
    private String dealUserName;

    /**
     * 日志业务类型
     */
    private String logType;

    /**
     * 操作动作名
     */
    private String oprName;

}
