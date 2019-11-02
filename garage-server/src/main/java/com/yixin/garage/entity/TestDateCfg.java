package com.yixin.garage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 测试环境系统日期设置
 * </p>
 *
 * @author lizhongxin
 * @since 2019-02-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TestDateCfg extends Model<TestDateCfg> {

    private static final long serialVersionUID = 1L;

    /**
     * 环境类型:dev/test/uat
     */
    @TableId(value = "environment", type = IdType.ID_WORKER)
    private String environment;

    /**
     * 设定的系统时间
     */
    private Date garageDate;

    /**
     * 备用字段
     */
    private String type;


    @Override
    protected Serializable pkVal() {
        return this.environment;
    }

    public Date getgarageDate() {
        return new Date();
    }
}
