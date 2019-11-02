package com.yixin.garage.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 部门表
 * </p>
 *
 * @author lizhongxin
 * @since 2019-01-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_dept_area")
public class DeptArea extends Model<DeptArea> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Integer id;

    /**
     * 排序
     */
    private Integer num;

    /**
     * 父部门id
     */
    private Integer pid;

    /**
     * 大区id
     */
    private String areaId;

    /**
     * 部门领导用户id
     */
    private Integer deptManagerId;

    /**
     * 简称
     */
    private String simplename;

    /**
     * 全称
     */
    private String fullname;

    /**
     * 版本（乐观锁保留字段）
     */
    @Version
    private Integer version;
    /**
     * 部门业务类型
     */
    private String busiType;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
