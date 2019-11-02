package com.yixin.garage.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * spring session配置
 */
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800)  //session过期时间  如果部署多机环境,需要打开注释
@ConditionalOnProperty(prefix = "garage", name = "spring-session-open", havingValue = "true")
public class SpringSessionConfig {

}
