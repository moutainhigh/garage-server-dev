//package com.yixin.garage.entity;
//
//import java.io.Serializable;
//
//import com.baomidou.mybatisplus.annotation.TableName;
//import com.baomidou.mybatisplus.extension.activerecord.Model;
//
//import com.yixin.garage.core.base.BaseEntity;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import lombok.experimental.Accessors;
//
///**
// * <p>
// * 用户部门关系表,多对多
// * </p>
// *
// * @author lizhongxin
// * @since 2019-01-02
// */
//@Data
//@EqualsAndHashCode(callSuper = false)
//@Accessors(chain = true)
//@TableName("sys_user_dept")
//public class UserDept extends BaseEntity {
//
//    private static final long serialVersionUID = 1L;
//
//    /**
//     * 用户id
//     */
//    private Integer userId;
//
//    /**
//     * 域账号
//     */
//    private String domainAccount;
//
//    /**
//     * 部门id
//     */
//    private Integer deptId;
//
//
//    @Override
//    protected Serializable pkVal() {
//        return this.userId;
//    }
//
//}
