package com.yixin.garage.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yixin.garage.core.base.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author libochen
 * @since 2019-01-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sg_attach_source")
public class AttachSource extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 原附件存储路径
     */
    private String attchPath;

    /**
     * 图片类型
     */
    private String attchType;

    /**
     * 所属业务id
     */
    private String bussId;

    /**
     * 压缩附件存储路径
     */
    private String compressAttchPath;

    /**
     * 原附件名称
     */
    private String sourceAttchName;

    /**
     * 所属业务类型
     */
    private String bussType;

    /**
     * 附件访问路径
     */
    private String viewUrl;

}
