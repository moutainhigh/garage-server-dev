package com.yixin.garage.config.property;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 应用名称属性spring.application.name
 */
@Component
@ConfigurationProperties(prefix = "role")
public class SysRoleProperties {

    private Map<String,String> financingRole = new HashMap<>();


    public Map<String, String> getFinancingRole() {
        return financingRole;
    }

    public void setFinancingRole(Map<String, String> financingRole) {
        this.financingRole = financingRole;
    }
}
