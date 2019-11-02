package com.yixin.garage.config;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2的装配Bean
 * @description 
 * 当前是默认的配置，可以自行修改hard code部分。
 * 
 * 页面访问地址：http://localhost:8080/{contextPath}/swagger-ui.html
 * 
 * @author Spring Cao
 * @date 2017年6月17日 下午3:38:57
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(prefix = "garage", name = "swagger-open", havingValue = "true")
@ComponentScan(basePackages = {"com.yixin.garage.controller"})  //Swagger只扫描Controller类的注解
public class Swagger2Config {
    
	@Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
        		.apiInfo(apiInfo())
                .groupName("易鑫集团")  //组织名称
                .genericModelSubstitutes(DeferredResult.class)
                .forCodeGeneration(false)
                .useDefaultResponseMessages(false)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))                         //这里采用包含注解的方式来确定要显示的接口
                //.apis(RequestHandlerSelectors.basePackage("cn.stylefeng.guns.modular.system.controller"))    //这里采用包扫描的方式来确定要显示的接口
                //  .genericModelSubstitutes(ResponseEntity.class)
                //.paths(PathSelectors.regex("/comm.*"))//过滤的接口
                //.paths(PathSelectors.any())
                .build()
                ;
    }

    /**
     * 
     * @title 用于API总体信息展示
     *
     * @description
     * 
     * @return
     */
    private ApiInfo apiInfo() {
        Contact contact = new Contact("易鑫集团——智能数据中心", "http://www.daikuan.com", "manager@yxqiche.com");
       /* ApiInfo apiInfo = new ApiInfo("全工程API接口",//大标题
                "REST风格API",//小标题
                "0.1",//版本
                "www.daikuan.com",
                contact,//作者
                "主页",//链接显示文字
                "http://www.daikuan.com"//网站链接
        );*/
//        return apiInfo;
        return new ApiInfoBuilder()
                .title("工程API接口 Doc")
                .description("REST风格 Api文档")
                .version("2.0")
                .termsOfServiceUrl("www.daikuan.com")
                .contact(contact)
                .build();
    }
}
