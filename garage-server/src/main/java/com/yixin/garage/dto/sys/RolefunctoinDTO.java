package com.yixin.garage.dto.sys;

import com.yixin.common.utils.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 融资项目
 * </p>
 *
 * @author lizhongxin
 * @since 2018-12-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RolefunctoinDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    private String roleid;
    
    /**
     * 系统中配置的key值
     */
    private String roleKey;

    /**
     * 业务：还款、资金、融资
     */
    private String bussType;

    /**
     * 操作权限:audit、update
     */
    private String oprPower;
}
