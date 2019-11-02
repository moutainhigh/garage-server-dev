package com.yixin.garage.dto.sys;

import com.yixin.garage.entity.DeptArea;

/**
 * Created by lianghaoguan on 2018/5/22.
 * 上传附件DTO
 */
public class UserDeptsDTO {

    private DeptArea[] depts;

    private String domainAccount;
    
    public String getDomainAccount() {
        return domainAccount;
    }

    public void setDomainAccount(String domainAccount) {
        this.domainAccount = domainAccount;
    }

    private String userId;

    public DeptArea[] getDepts() {
        return depts;
    }

    public void setDepts(DeptArea[] depts) {
        this.depts = depts;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
