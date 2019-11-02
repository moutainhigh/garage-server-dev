package com.yixin.garage.auth_local;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.cas.CasSubjectFactory;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.yixin.GarageApplication;
import com.yixin.garage.auth.logout.NewSingleSignOutFilter;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by guowanfeng on 2017/8/7.
 */
@Configuration
@ConditionalOnProperty(name="env",havingValue="dev")
//@Profile("dev")
@Slf4j
public class ShiroConfigurationLocal {
	private final static Logger logger = LoggerFactory.getLogger(ShiroConfigurationLocal.class);

    @Value("${shiro.casServerUrlPrefix}")
    private String casServerUrlPrefix;

    @Value("${shiro.casService}")
    private String casService;

    @Value("${shiro.casServerList}")
    private String casUrlList;

    @Bean(name = "myShiroCasRealm")
    public MyShiroCasRealm myShiroCasRealm() {
    	logger.info("Using MyShiroCasRealm !!!");
        MyShiroCasRealm realm = new MyShiroCasRealm();
        return realm;
    }

    /**
     * 注册单点登出listener
     * @return
     */
    @Bean
    public ServletListenerRegistrationBean singleSignOutHttpSessionListener(){
        ServletListenerRegistrationBean bean = new ServletListenerRegistrationBean();
        bean.setListener(new SingleSignOutHttpSessionListener());
        //bean.setName(""); //默认为bean name
        bean.setEnabled(true);
        //bean.setOrder(Ordered.HIGHEST_PRECEDENCE); //设置优先级
        return bean;
    }



    @Bean
    public NewSingleSignOutFilter singleSignOutFilter() {
        NewSingleSignOutFilter ssof = new NewSingleSignOutFilter(casUrlList);
        ssof.setLogoutParameterName(casServerUrlPrefix);
        return ssof;
    }
    
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}


    /**
     * 注册单点登出filter
     * @return
     */
    @Bean
    public FilterRegistrationBean logOutFilter(NewSingleSignOutFilter singleSignOutFilter){
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(singleSignOutFilter); // 该值缺省为false,表示生命周期由SpringApplicationContext管理,设置为true则表示由ServletContainer管理
        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
        filterRegistration.setEnabled(true);
        filterRegistration.setOrder(1); // 注意，此处需小于 shiroFilter的顺序值 eg:值越小，filter执行顺序越靠前
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.addServletNames("default");
        return filterRegistration;
    }



    /**
     * 注册DelegatingFilterProxy（Shiro）
     *
     * @return
     * @author guowanfeng
     * @create  2016年1月13日
     */
    @Bean
    public FilterRegistrationBean delegatingFilterProxy() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        //  该值缺省为false,表示生命周期由SpringApplicationContext管理,设置为true则表示由ServletContainer管理
        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
        filterRegistration.setEnabled(true);
        filterRegistration.setOrder(2);
        filterRegistration.addUrlPatterns("/*");
        return filterRegistration;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(MyShiroCasRealm myShiroCasRealm) {
        DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
        dwsm.setRealm(myShiroCasRealm);
        // 指定 SubjectFactory
        dwsm.setSubjectFactory(new CasSubjectFactory());
        return dwsm;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(securityManager);
        return aasa;
    }


    /**
     * CAS过滤器
     *
     * @return
     * @author SHANHY
     * @create  2016年1月17日
     */
//    @Bean(name = "casFilter")
//    public CasFilter getCasFilter() {
//        CasFilter casFilter = new CasFilter();
//        casFilter.setName("casFilter");
//        casFilter.setEnabled(true);
//        // 登录失败后跳转的URL，也就是 Shiro 执行 CasRealm 的 doGetAuthenticationInfo 方法向CasServer验证tiket
//        casFilter.setFailureUrl("/local/loginView");// 我们选择认证失败后再打开登录页面
//        return casFilter;
//    }

    /**
     * ShiroFilter<br/>
     * 注意这里参数中的 StudentService 和 IScoreDao 只是一个例子，因为我们在这里可以用这样的方式获取到相关访问数据库的对象，
     * 然后读取数据库相关配置，配置到 shiroFilterFactoryBean 的访问规则中。实际项目中，请使用自己的Service来处理业务逻辑。
     *
     * @param securityManager
     * @return
     * @author SHANHY
     * @create  2016年1月14日
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("http://localhost:8888/#!/login");
        //shiroFilterFactoryBean.setLoginUrl("/local/loginView");
        // 登录成功后要跳转的连接
        //shiroFilterFactoryBean.setSuccessUrl("/index");
        shiroFilterFactoryBean.setUnauthorizedUrl("/pages/errors/denied.jsp");
        // 添加casFilter到shiroFilter中
        Map<String, Filter> filters = new HashMap<>();
        //filters.put("casFilter", casFilter);
        // filters.put("logout",logoutFilter());
        shiroFilterFactoryBean.setFilters(filters);

        loadShiroFilterChain(shiroFilterFactoryBean);
        return shiroFilterFactoryBean;
    }

    /**
     * 加载shiroFilter权限控制规则（从数据库读取然后配置）,角色/权限信息由MyShiroCasRealm对象提供doGetAuthorizationInfo实现获取来的
     *
     * @author SHANHY
     * @create  2016年1月14日
     */
    private void loadShiroFilterChain(ShiroFilterFactoryBean shiroFilterFactoryBean){
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

        // authc：该过滤器下的页面必须登录后才能访问，它是Shiro内置的一个拦截器org.apache.shiro.web.filter.authc.FormAuthenticationFilter
        // anon: 可以理解为不拦截
        // user: 登录了就不拦截
        // roles["admin"] 用户拥有admin角色
        // perms["permission1"] 用户拥有permission1权限
        // filter顺序按照定义顺序匹配，匹配到就验证，验证完毕结束。
        // url匹配通配符支持：? * **,分别表示匹配1个，匹配0-n个（不含子路径），匹配下级所有路径

        //shiro集成cas后，首先添加该规则
        //filterChainDefinitionMap.put("/shiro-cas", "casFilter");

        //不拦截的请求
        //swagger 配置
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/swagger-resources", "anon");
        filterChainDefinitionMap.put("/v2/api-docs", "anon");
        filterChainDefinitionMap.put("/configuration/ui", "anon");
        filterChainDefinitionMap.put("/configuration/security", "anon");
        filterChainDefinitionMap.put("/webjars/springfox-swagger-ui/**", "anon");

        filterChainDefinitionMap.put("/api/**", "anon");
		filterChainDefinitionMap.put("/cardemo/**", "anon");
		filterChainDefinitionMap.put("/health/**", "anon");
		filterChainDefinitionMap.put("/baffle/**", "anon");
		filterChainDefinitionMap.put("/basicPlatformNeedHandle/**", "anon");
		filterChainDefinitionMap.put("/carInfo/**", "anon");
		filterChainDefinitionMap.put("/ecsqlexecutor_@$/**", "anon");
		filterChainDefinitionMap.put("/cache/**", "anon");
        filterChainDefinitionMap.put("/orderTracking/**", "anon");
        filterChainDefinitionMap.put("/local/login/**", "anon");
        filterChainDefinitionMap.put("/local/getlogin/**", "anon");
        filterChainDefinitionMap.put("/financingProject/**", "anon");
        filterChainDefinitionMap.put("/cfg/**", "anon");
        filterChainDefinitionMap.put("/finProject/**", "anon");
        filterChainDefinitionMap.put("/dict/**", "anon");//字典
        filterChainDefinitionMap.put("/fundMonthPlan/**", "anon");//资金计划
        filterChainDefinitionMap.put("/fundMonthPlanDetail/**", "anon");//资金计划
        filterChainDefinitionMap.put("/fundDayFcast/**", "anon");//资金日预测
        filterChainDefinitionMap.put("/cfgDayFcastItem/**", "anon");//资金日预测配置
        filterChainDefinitionMap.put("/fundMonthOutcome/**", "anon");//资金月查询
        filterChainDefinitionMap.put("/fundDayOutcome/**", "anon");//资金日查询
        filterChainDefinitionMap.put("/cfgWorkHoliday/**", "anon");//日历表
        filterChainDefinitionMap.put("/garagesqlexecutor_@$/update", "anon");//sql更新接口
        filterChainDefinitionMap.put("/auditLog/getOperateLogs", "anon");//查询日志接口
        filterChainDefinitionMap.put("/fundDayFcastRemark/**", "anon");//日预测备注查询
        filterChainDefinitionMap.put("/log/**", "anon");//日预测备注查询
        filterChainDefinitionMap.put("/fundRate/**", "anon");//资金利率配置
        //filterChainDefinitionMap.put("/garageInfo/**", "anon");//车库列表查询
        filterChainDefinitionMap.put("/sgGarageInfo/**", "anon");//车库列表查询
        filterChainDefinitionMap.put("/sgGarageInfoLog/**", "anon");//车库日志
        filterChainDefinitionMap.put("/sgVehicleInfo/**", "anon");//车辆列表查询
        filterChainDefinitionMap.put("/sgVehicleLog/**", "anon");//车辆日志查询
        filterChainDefinitionMap.put("/sgGarageOrder/**", "anon");//订单查询
        filterChainDefinitionMap.put("/sgGarageOrderLog/**", "anon");//订单日志查询
        filterChainDefinitionMap.put("/sgGarageDetail/**", "anon");//入库
        filterChainDefinitionMap.put("/carDict/**", "anon");//车辆四级
        filterChainDefinitionMap.put("/attachSource/**", "anon");//附件上传
        filterChainDefinitionMap.put("/hasRole/**", "anon");//附件上传
        filterChainDefinitionMap.put("/sgSendInfoToZhongtai/**", "anon");//中台信息

        filterChainDefinitionMap.put("/sgInventoryHome/**", "anon");//盘点主页
        filterChainDefinitionMap.put("/sgInventoryBill/**", "anon");//盘点任务主单
        filterChainDefinitionMap.put("/sgInventoryDetail/**", "anon");//盘点任务主详单
        filterChainDefinitionMap.put("/sgInventoryLog/**", "anon");//盘点任务日志

//        filterChainDefinitionMap.put("/dataCreateController/**", "anon");//初始化数据
        filterChainDefinitionMap.put("/enum/**", "anon");//枚举
        //登录过的不拦截
		filterChainDefinitionMap.put("/**", "user");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
    }

}
