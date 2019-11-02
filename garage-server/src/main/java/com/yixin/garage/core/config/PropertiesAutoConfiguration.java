package com.yixin.garage.core.config;

import com.yixin.garage.core.config.properties.AppNameProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 应用属性配置
 *
 */
@Configuration
//@PropertySource("classpath:config.properties") 
public class PropertiesAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.application")
    public AppNameProperties appNameProperties() {
        return new AppNameProperties();
    }
}
