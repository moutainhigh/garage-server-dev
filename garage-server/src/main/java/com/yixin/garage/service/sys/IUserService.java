package com.yixin.garage.service.sys;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yixin.garage.dto.sys.SysUserDTO;
import com.yixin.garage.dto.sys.UserByPhoneDTO;
import com.yixin.garage.entity.User;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author lizhongxin
 * @since 2019-01-02
 */
public interface IUserService {

    /*List<User> queryUserByRoleKey(String roleKey);*/
    /**
     * 
     * @Description: 根据角色获取用户列表
     * @param roleName
     * @return List<SysUserDTO>
     * @throws 
     * @author YixinCapital -- yangfei02
     *	       2019年1月18日 下午3:17:13
     */
    List<SysUserDTO> getUserInfoByRoleName(String roleName);


    /**
     *
     * @Description: 根据手机号获取用户信息
     * @param phoneNo
     * @return List<SysUserDTO>
     * @throws
     * @author YixinCapital -- libochen
     *	       2019年08月26日 下午3:17:13
     */
    UserByPhoneDTO getUserByPhoneNO(String phoneNo);

    /**
     * 根据域账号查询SysUser用户信息
     * @param domainAccount
     * @return
     */
//    User getUserByDomainAccount(String domainAccount);
//
//    /**
//     *
//     * @Description: 更具配置的角色名查找拥有该角色的用户名字符串（主要给查找下一处理人用）
//     * @param roleName
//     * @return String
//     * @throws
//     * @author YixinCapital -- yangfei02
//     *	       2019年1月10日 上午11:52:36
//     */
//    String getUserNameByRoleName(String roleName);
//
//    /**
//     *
//     * @Description: 根据域账号获取有权限（所属部门或者管辖部门）的部门列表
//     * @param domainAccount
//     * @return List<String>
//     * @throws
//     * @author YixinCapital -- yangfei02
//     *	       2019年1月18日 下午3:16:25
//     */
//    List<Integer> getDeptIdByDomainAccount(String domainAccount);
}
