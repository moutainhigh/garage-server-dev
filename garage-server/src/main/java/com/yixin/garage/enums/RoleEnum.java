package com.yixin.garage.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.yixin.garage.util.ConfigUtil;

/**
 * 系统角色枚举
 */
public enum RoleEnum {

	//安全车库
	GARAGE_MANAGE_ROLE("garageRole.garageManageRoleId","车库管理员","车库管理员"),
	//安全车库
	DISPOSE_COMMISSIONER_ROLE("garageRole.disposeCommissionerRoleId","自建库处置专员","自建库处置专员"),

	//安全车库
	ASSETS_COMMISSIONER_ROLE("garageRole.assetsCommissionerRoleId","资产经理","资产经理"),

	//安全车库 - 车库一览
	GARAGE_CHECKLIST_ROLE("garageRole.garageChecklistRoleId","车库一览","车库一览"),

    ;

    RoleEnum(String roleName,String jobName) {
    	this.roleName = roleName;
        this.jobName = jobName;
    }

	RoleEnum(String roleName, String jobName, String roleShortName) {
		this.roleName = roleName;
		this.jobName = jobName;
		this.roleShortName = roleShortName;
	}

	private String roleName;
    private String jobName;
	private String roleShortName;

    private static Map<String,RoleEnum> roleNameMap;
	public String getJobName() {
		return jobName;
	}
	public String getRoleName() {
		return roleName;
	}
	public String getRoleShortName(){
		return roleShortName;
	}
	static {
		buildMap();
	}
	private static void buildMap(){
		roleNameMap = new HashMap<>();
		for(RoleEnum one: RoleEnum.values()){
			roleNameMap.put(one.getRoleName(), one);
		}
	}
	
	public static boolean contains(String roleName) {
		if(roleNameMap==null||roleNameMap.isEmpty()){
			buildMap();
		}
		return roleNameMap.containsKey(roleName);
	}
	public static RoleEnum getEnumByRoleName(String roleName) {
		return roleNameMap.get(roleName);
	}
    
	public boolean equals(RoleEnum userRole){
		if(userRole == null)return false;
		return this.roleName.equals(userRole.getRoleName());
	}
    private final String ROLE_PREFIX = "garage.role.";

    public String getRoleId(){
        String roleId = ConfigUtil.getValue(this.getRoleName(),"");
        if(StringUtils.isBlank(roleId)){
            roleId = ConfigUtil.getValue(ROLE_PREFIX+this.getRoleName(),"");
        }
        return roleId;
    }

	public static List<RoleEnum> getRoleListByBusiType(String roelBusiType) {
		List<RoleEnum> roleList = new ArrayList<>();
		for(RoleEnum roleEnum : RoleEnum.values()){
			if(roleEnum.getRoleName().contains(roelBusiType)){
				roleList.add(roleEnum);
			}
		}
		return roleList;
	}


}
