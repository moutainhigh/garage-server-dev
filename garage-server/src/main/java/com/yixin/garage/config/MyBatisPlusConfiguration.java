package com.yixin.garage.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.yixin.garage.config.property.DruidProperties;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * mybatis-plus配置
 *
 */
@EnableTransactionManagement
@Configuration
@MapperScan("com.yixin.garage.dao")
public class MyBatisPlusConfiguration {

 
    /**
     * mybatis-plus分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
    	 PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
    	 paginationInterceptor.setDialectType(DbType.MYSQL.getDb());
    	return paginationInterceptor;
    }

    /**
     * 数据范围mybatis插件
     */
  /*  @Bean
    public DataScopeInterceptor dataScopeInterceptor() {
        return new DataScopeInterceptor();
    }*/

    /**
     * 乐观锁mybatis插件
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }
    /**
     * sql注入器  逻辑删除插件
     */
    @Bean
    public ISqlInjector sqlInjector() {
        return new LogicSqlInjector();
    }
    
    /**
     * SQL执行效率插件
     */
    @Bean
    @ConditionalOnProperty(value = "garage.sql-performance-enable")// 设置 dev test 环境开启
    public PerformanceInterceptor performanceInterceptor() {
    	PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
    	performanceInterceptor.setMaxTime(100);
    	performanceInterceptor.setFormat(true);
        return new PerformanceInterceptor();
    }

}

