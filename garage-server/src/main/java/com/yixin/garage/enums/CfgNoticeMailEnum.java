package com.yixin.garage.enums;

public enum CfgNoticeMailEnum {
    FUND_DEPT_LEADER("0","资金部门领导");
 
    private String code;
    private String name;
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    CfgNoticeMailEnum(String code,String name){
        this.code=code;
        this.name=name;
    }
}

