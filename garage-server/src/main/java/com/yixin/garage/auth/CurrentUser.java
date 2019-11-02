package com.yixin.garage.auth;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yixin.garage.enums.RoleEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.yixin.common.utils.YxCurrentUserDTO;
import com.yixin.garage.util.ConfigUtil;
import com.yixin.garage.util.RestUtil;

import org.apache.shiro.util.ThreadContext;

/**
 * 能够直接获取当前用户 当前用户的角色， 当前用户的权限， 当前用户的部门id、部门名称 当前用户的中文名称 当前用户的域账号 当前用户的用户编号
 *
 * @author zhangyiqing 2016年4月15日11:01:42
 */
public final class CurrentUser {
    /**
     * 判断是否可以获取到Session数据
     * @return
     */
    public static boolean isSMAvailable(){
        if(ThreadContext.getSecurityManager()!=null){
            return true;
        }
        return false;
    }

    /**
     * 检查当前会话是否为已登录状态
     * <br/>（已登录状态但是用户数据不一定是当前用户的有可能还是上个用户的登录状态）
     * @return true:已登录 false:未登录
     */
    public static boolean isLogin(){
        if(!isSMAvailable()||StringUtils.isBlank(getUsername())){
            return false;
        }
        return true;
    }

    /**
     * 检查当前会话是否为指定用户已登录环境
     * @param userName
     * @return
     */
    public static boolean isLogin(String userName){
        if(!isSMAvailable()||StringUtils.isBlank(getUsername())||!Objects.equals(userName,getUsername())){
            return false;
        }
        return true;
    }

	/**
	 * 获取用户域账号
	 * 
	 * @return
	 */
	public static String getUsername() {
		Session session = getSession();
		return (String) session.getAttribute("username_shiro");
	}

	/**
	 * 获取用户中文名称
	 * 
	 * @return
	 */
	public static String getCnName() {
		Session session = getSession();
		return (String) session.getAttribute("cnName_shiro");
	}

	/**
	 * 获取用户id
	 * 
	 * @return
	 */
	public static String getUserId() {
		Session session = getSession();
		return "" + session.getAttribute("userId_shiro");
	}

	/**
	 * 获取部门id
	 * 
	 * @return
	 */
	public static String getDepartmentId() {
		Session session = getSession();
		return (String) session.getAttribute("departmentId_shiro");
	}

	/**
	 * 获取部门名称
	 * 
	 * @return
	 */
	public static String getDepartmentName() {
		Session session = getSession();
		return (String) session.getAttribute("departmentName_shiro");
	}

	/**
	 * 获取员工编号
	 * 
	 * @return
	 */
	public static String getEmployeeNumber() {
		Session session = getSession();
		return (String) session.getAttribute("employeeNumber_shiro");
	}

	/**
	 * 获取权限列表
	 * 
	 * @return
	 */
	public static Iterable<String> getPermissions() {
		return getInfo() != null ? getInfo().getStringPermissions() : null;
	}

	/**
	 * 获取角色列表
	 * 
	 * @return
	 */
	public static Iterable<String> getRoles() {
		return getInfo() != null ? getInfo().getRoles() : null;
	}

	private static AuthorizationInfo getInfo() {
		Session session = getSession();
		return (AuthorizationInfo) session.getAttribute("userInfo_shiro");
	}

	// 获取session
	private static Session getSession() {
		return SecurityUtils.getSubject().getSession();
	}

	/**
	 * 获取业务部门id
	 * 
	 * @return
	 */
	public static String getBusiness_department_id() {
		Session session = getSession();
		return (String) session.getAttribute("business_department_id");
	}

	/**
	 * 获取业务部门name
	 * 
	 * @return
	 */
	public static String getBusiness_department_name() {
		Session session = getSession();
		return (String) session.getAttribute("business_department_name");
	}

	/**
	 * 获取决策内部渠道name
	 * 
	 * @return
	 */
	public static String getDecision_inner_channel_name() {
		Session session = getSession();
		return (String) session.getAttribute("decision_inner_channel_name");
	}

	/**
	 * 获取决策外部渠道name
	 * 
	 * @return
	 */
	public static String getDecision_outer_channel_name() {
		Session session = getSession();
		return (String) session.getAttribute("decision_outer_channel_name");
	}

	/**
	 * 获取索引内部渠道name
	 * 
	 * @return
	 */
	public static String getClue_inner_channel_name() {
		Session session = getSession();
		return (String) session.getAttribute("clue_inner_channel_name");
	}

	/**
	 * 获取索引外部渠道name
	 * 
	 * @return
	 */
	public static String getClue_outer_channel_name() {
		Session session = getSession();
		return (String) session.getAttribute("clue_outer_channel_name");
	}

	/**
	 * 获取决策内部渠道id
	 * 
	 * @return
	 */
	public static String getDecision_inner_channel_id() {
		Session session = getSession();
		return (String) session.getAttribute("decision_inner_channel_id");
	}

	/**
	 * 获取决策外部渠道id
	 * 
	 * @return
	 */
	public static String getDecision_outer_channel_id() {
		Session session = getSession();
		return (String) session.getAttribute("decision_outer_channel_id");
	}

	/**
	 * 获取索引内部渠道id
	 * 
	 * @return
	 */
	public static String getClue_inner_channel_id() {
		Session session = getSession();
		return (String) session.getAttribute("clue_inner_channel_id");
	}

	/**
	 * 获取索引外部渠道id
	 * 
	 * @return
	 */
	public static String getClue_outer_channel_id() {
		Session session = getSession();
		return (String) session.getAttribute("clue_outer_channel_id");
	}

	/**
	 * //当前人管理的行政部门id串，以逗号分隔
	 * 
	 * @return
	 */
	public static String getManager_department_ids() {
		Session session = getSession();
		return (String) session.getAttribute("manager_department_ids");
	}

	/**
	 * //当前人管理的业务部门id串，以逗号分隔
	 * 
	 * @return
	 */
	public static String getManager_business_department_ids() {
		Session session = getSession();
		return (String) session.getAttribute("manager_business_department_ids");
	}
	
	 /**
     * 获取当前 Subject
     *
     * @return Subject
     */
    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }
	/**
     * 认证通过或已记住的用户。
     *
     * @return 用户：true，否则 false
     */
    public static boolean isUser() {
        return getSubject() != null && getSubject().getPrincipal() != null;
    }


	/**
	 * 获取用户角色信息
	 *
	 * @return
	 */
	public static Set<String> getUserRoles(){
		Session session = getSession();
		AuthorizationInfo authorizationInfo = (AuthorizationInfo) session.getAttribute("userInfo_shiro");
		if(authorizationInfo!=null){
			return (Set<String>) authorizationInfo.getRoles();
		}else {
			return null;
		}
	}

	public static YxCurrentUserDTO getCurrentUserDTO() {
		YxCurrentUserDTO yxCurrentUserDTO = new YxCurrentUserDTO();
		yxCurrentUserDTO.setId(getUserId());
		yxCurrentUserDTO.setUserName(getCnName());
		yxCurrentUserDTO.setUserAccount(getUsername());
		yxCurrentUserDTO.setDepartmentId(getDepartmentId());
		yxCurrentUserDTO.setDepartmentName(getDepartmentName());
		yxCurrentUserDTO.setPermissions((Set<String>) getPermissions());
		yxCurrentUserDTO.setRoles((Set<String>) getRoles());
		return yxCurrentUserDTO;
	}
	/**
	 * 
	 * @Title: checkIfHasPermissions   
	 * @Description: 检查用户是否具有当前系统的指定的权限控制   
	 * @param premissionName
	 * @return   Boolean    
	 * @author YixinCapital -- lizhongxin
	 *	       2019年1月22日 下午8:44:09
	 * @return 
	 */
	public static Boolean checkIfHasPermissions(String permissionName){
		HashSet<String> permissions = (HashSet<String>) CurrentUser.getPermissions();
    	if(permissions == null || permissions.isEmpty())
    		return false;
    	return permissions.contains(permissionName);
	}
	
	/**
     *
     * @Description: 判断当前登录人是否有某角色，只要满足其中一个角色即可
     * @param roleKeys
     * @return boolean
     * @throws 
     * @author YixinCapital -- yangfei02
     *         2019年1月4日 下午2:28:09
     */
    public static boolean checkUserPowerOR(String... roleKeys){
        //登陆人的角色列表
        Set<String> roles = CurrentUser.getUserRoles();
        if(roles==null) {
            return false;
        }
        boolean hasPower = false;
        for(int i=0; i<roleKeys.length; i++){
            String roleId = ConfigUtil.getValue(roleKeys[i]);
            hasPower = roles.contains(roleId) || hasPower;
            if(hasPower) return hasPower;
        }
        return hasPower;
    }

	/**
	 * @Title: hasRole
	 * @Description: 判断当前用户是否具有指定的用户角色
	 * @param userRoles
	 * @return boolean
	 * @author YixinCapital -- lizhongxin
	 *        2019年6月26日 下午5:29:01
	 */
	public static boolean hasRole(RoleEnum... userRoles ){
		//登陆人的角色列表
		Set<String> roles = CurrentUser.getUserRoles();
		if(roles==null) return false;
		for(int i=0; i<userRoles.length; i++){
			String roleId = ConfigUtil.getValue(userRoles[i].getRoleName());
			if(roles.contains(roleId)) return true;
		}
		return false;
	}


}