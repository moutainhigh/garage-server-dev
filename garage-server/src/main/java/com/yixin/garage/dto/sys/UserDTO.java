package com.yixin.garage.dto.sys;

import java.util.List;

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
public class UserDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Integer userid;

    /**
     * 域账号
     */
    private String domainAccount;
    
    /**
     * 域账号
     */
    private String userName;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 电话
     */
    private String phone;

    /**
     * 角色id
     */
    private String roleid;

    /**
     * 部门id
     */
    private Integer deptid;

    /**
     * 状态(1：启用  2：冻结  3：删除）
     */
    private Integer status;
    
    /**
     * 用户权限列表
     */
    List<RolefunctoinDTO> roleList;
}
