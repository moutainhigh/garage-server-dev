package com.yixin.garage.entity.errorlog;

import com.yixin.garage.core.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * <p>
 * 
 * </p>
 *
 * @author zhouhang
 * @since 2019-08-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SgErrorInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String batchNum;

    private Integer businessType;

    private String errorCode;

    private String interfaceName;

    private String remark;

    private Integer solveStat;

    private Integer systemOfType;

    private Integer type;

    private String uniqueCode;

}
