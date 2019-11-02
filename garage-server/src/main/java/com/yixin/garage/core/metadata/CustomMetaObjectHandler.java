package com.yixin.garage.core.metadata;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.yixin.garage.auth.CurrentUser;


/**
 * 自定义sql字段填充器,
 */
@Component
public class CustomMetaObjectHandler implements MetaObjectHandler {
	/**
	 * 创建时间字段的名称（非数据库中字段名称）
	 */
	public static final String CREATE_TIME_FIELD_NAME = "createTime";
	/**
	 * 创建用户账号字段名称
	 */
	public static final String CREATOR_ID_FIELD_NAME = "creatorId";
	/**
	 * 创建用户姓名字段名称
	 */
	public static final String CREATOR_NAME_FIELD_NAME = "creatorName";
	/**
	 * 更新时间字段的名称
	 */
	public static final String UPDATE_TIME_FIELD_NAME = "updateTime";
	/**
	 * 更新用户账号字段名称
	 */
	public static final String UPDATOR_ID_FIELD_NAME = "updatorId";
	
	public static final String UPDATOR_NAME_FIELD_NAME = "updatorName";
	/**
	 * 逻辑删除字段的名称
	 */
	public static final String IS_DELETED_FIELD_NAME = "deleted";
	
	
//	private final Logger logger = LoggerFactory.getLogger(CustomMetaObjectHandler.class);

    @Override
    public void insertFill(MetaObject metaObject) {
        final Object delFlag = getFieldValByName(IS_DELETED_FIELD_NAME, metaObject);
        
        if (delFlag == null) {
            setFieldValByName(IS_DELETED_FIELD_NAME, getDefaultDelFlagValue(), metaObject);
        }

        final Object createTime = getFieldValByName(CREATE_TIME_FIELD_NAME, metaObject);
        if (createTime == null) {
            setFieldValByName(CREATE_TIME_FIELD_NAME, new Date(), metaObject);
        }
        if (CurrentUser.isSMAvailable() && getFieldValByName(CREATOR_ID_FIELD_NAME, metaObject) == null ) {
            //获取当前登录用户
            setFieldValByName(CREATOR_ID_FIELD_NAME, CurrentUser.getUsername(), metaObject);
            setFieldValByName(CREATOR_NAME_FIELD_NAME, CurrentUser.getCnName(), metaObject);
            setFieldValByName("creatorDepartmentId", Long.valueOf(CurrentUser.getDepartmentId()==null?"0":CurrentUser.getDepartmentId()), metaObject);
            setFieldValByName("creatorDepartmentName", CurrentUser.getDepartmentName(), metaObject);
        }
        
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldValByName(UPDATE_TIME_FIELD_NAME, new Date(), metaObject);
        if (CurrentUser.isSMAvailable() && StringUtils.isNotBlank(CurrentUser.getUsername())) { //web请求且当前用户不为空
            
            setFieldValByName(UPDATOR_ID_FIELD_NAME, CurrentUser.getUsername(), metaObject);
            setFieldValByName(UPDATOR_NAME_FIELD_NAME, CurrentUser.getCnName(), metaObject);
            setFieldValByName("updatorDepartmentId", Long.valueOf(CurrentUser.getDepartmentId()==null?"0":CurrentUser.getDepartmentId()), metaObject);
            setFieldValByName("updatorDepartmentName", CurrentUser.getDepartmentName(), metaObject);
        }
    }


    /**
     * 获取逻辑删除字段的默认值
     */
    protected Object getDefaultDelFlagValue() {
        return Boolean.FALSE;
    }

}