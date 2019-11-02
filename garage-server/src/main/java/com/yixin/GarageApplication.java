package com.yixin;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;


//@ImportResource(locations = {"classpath:rabbitmq-context.xml"})
@SpringBootApplication
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800)
@EnableAsync
@EnableTransactionManagement(proxyTargetClass = true)
@MapperScan("mapper")
public class GarageApplication {

	private final static Logger logger = LoggerFactory.getLogger(GarageApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(GarageApplication.class, args);
		logger.info("GarageApplication is success!");
	}

}
