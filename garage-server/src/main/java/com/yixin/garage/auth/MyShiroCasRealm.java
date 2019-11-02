package com.yixin.garage.auth;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.cas.CasToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.yixin.common.exception.BzException;
import com.yixin.common.utils.InvokeResult;
import com.yixin.oa.web.dto.UserDto;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by guowanfeng on 2017/8/7.
 */
@Slf4j
public class MyShiroCasRealm extends CasRealm {


    @Autowired
    private RestTemplate restTemplate;

    @Value("${usc.getAdjunctByDomainAccountAndSystemId}")
    private String getAdjunctByDomainAccountAndSystemId;

    @Value("${usc.findResourceByUser}")
    private String findResourceByUser;


    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof CasToken || token instanceof UsernamePasswordToken;
    }

    /**
     * 查询用户信息
     *
     * @author YixinCapital--wujt 2017/6/28 16:40
     */
    private UserDto getUserDto(String username) {
        long bg = System.currentTimeMillis();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<String>("", headers);
        String url = findResourceByUser;
        url = url + "?username=" + username;
        ResponseEntity<UserDto> responseEntity = restTemplate.postForEntity(url, formEntity,
                UserDto.class);
        long ed = System.currentTimeMillis();
        log.info("getUserDto方法,查询用户信息数据耗时：{}秒", (ed - bg) / 1000.0);
        return responseEntity.getBody();
    }


    /**
     * 获取兼职行政部门以及归属行政部门id的递归串
     *
     * @param username
     * @return
     */
    private Object getAdjunctInfo(String username) {
        long bg = System.currentTimeMillis();
        // 获取兼职行政部门以及归属行政部门id的递归串
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<String>("", headers);
        String url = getAdjunctByDomainAccountAndSystemId + "?domainAccount=" + username + "&systemId=10006";
        ResponseEntity<InvokeResult> responseEntity = restTemplate.postForEntity(url, formEntity,
                InvokeResult.class);
        InvokeResult<String> invokeResult = responseEntity.getBody();
        long ed = System.currentTimeMillis();
        log.info("getAdjunctInfo,查询兼职部门数据耗时：{}秒", (ed - bg) / 1000.0);
        return invokeResult.getData();
    }

    /**
     * 权限认证，为当前登录的Subject授予角色和权限
     *
     * @see ：本例中该方法的调用时机为需授权资源被访问时
     * @see ：并且每次访问需授权资源时都会执行该方法中的逻辑，这表明本例中默认并未启用AuthorizationCache
     * @see ：如果连续访问同一个URL（比如刷新），该方法不会被重复调用，Shiro有一个时间间隔（也就是cache时间，在ehcache-shiro.xml中配置），超过这个时间间隔再刷新页面，该方法会被执行
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
    	log.info("##################执行Shiro权限认证##################");
        //获取当前登录输入的用户名，等价于(String) principalCollection.fromRealm(getName()).iterator().next();
        String username = (String) super.getAvailablePrincipal(principalCollection);
        //String username = (String) principals.getPrimaryPrincipal();// 只有一个realm的时候，暂时不考虑有多个realm的realm链
        Session session = SecurityUtils.getSubject().getSession();
        SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
        AuthorizationInfo authorizationInfo = null;
        if (session.getAttribute("userInfo_shiro") == null) {
            UserDto userDto = getUserDto(username);
            authorizationInfo = userDto.getSimpleAuthorizationInfo();
            session.setAttribute("userInfo_shiro", authorizationInfo);
        } else {
            authorizationInfo = (AuthorizationInfo) session.getAttribute("userInfo_shiro");
        }
        // 缓存直接加在service的实现层
        return authorizationInfo;
    }

    /**
     * 用casRealm的认证方法进行验证用户的登录信息
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        AuthenticationInfo doGetAuthenticationInfo;
        if (token instanceof CasToken) {
            doGetAuthenticationInfo = super.doGetAuthenticationInfo(token);
        } else if (token instanceof UsernamePasswordToken) {
            doGetAuthenticationInfo = new SimpleAuthenticationInfo(token.getPrincipal()
                    , token.getCredentials(), this.getName());
        } else {
            throw new BzException("非法请求");
        }

        PrincipalCollection principals = doGetAuthenticationInfo.getPrincipals();
        Object primaryPrincipal = principals.getPrimaryPrincipal();
        String username = String.valueOf(primaryPrincipal);
        Session session = SecurityUtils.getSubject().getSession();
        String username_session = (String) session.getAttribute("username_shiro");// session里域账号
        if (session.getAttribute("authenticationInfo_shiro") == null
                || !(username.equalsIgnoreCase(username_session))) {
            UserDto userDto = getUserDto(username);
            session.setAttribute("authenticationInfo_shiro", doGetAuthenticationInfo);
            session.setAttribute("username_shiro", userDto.getUsername());// 域账号
            session.setAttribute("departmentId_shiro", userDto.getDepartmentId());// 部门id
            session.setAttribute("departmentName_shiro", userDto.getDepartmentName());// 部门名称
            session.setAttribute("cnName_shiro", userDto.getCnName());// 中文名称
            session.setAttribute("userId_shiro", userDto.getId());// 用户id
            session.setAttribute("employeeNumber_shiro", userDto.getEmployeesNumber());// 用户编号
            // 部门和渠道信息
            session.setAttribute("business_department_id", userDto.getBusiness_department_id());// 业务部门id
            session.setAttribute("business_department_name", userDto.getBusiness_department_name());// 业务部门name
            session.setAttribute("clue_inner_channel_id", userDto.getClue_inner_channel_id());// 线索内部渠道id
            session.setAttribute("clue_outer_channel_id", userDto.getClue_outer_channel_id());// 线索外部渠道id
            session.setAttribute("decision_inner_channel_id",
                    userDto.getDecision_inner_channel_id());// 决策内部渠道id
            session.setAttribute("decision_outer_channel_id",
                    userDto.getDecision_outer_channel_id());// 决策外部渠道id
            session.setAttribute("clue_inner_channel_name", userDto.getClue_inner_channel_name());// 线索内部渠道name
            session.setAttribute("clue_outer_channel_name", userDto.getClue_outer_channel_name());// 线索外部渠道name
            session.setAttribute("decision_inner_channel_name",
                    userDto.getDecision_inner_channel_name());// 决策内部渠道name
            session.setAttribute("decision_outer_channel_name",
                    userDto.getDecision_outer_channel_name());// 决策外部渠道name
            session.setAttribute("next_all_Level_department_ids",
                    userDto.getNext_all_Level_department_ids());// 当前人行政部门以及下属行政部门递归id串，以逗号分隔

            session.setAttribute("manager_department_ids", userDto.getManager_department_ids());// 当前人管理的行政部门id串，以逗号分隔
            session.setAttribute("manager_business_department_ids",
                    userDto.getManager_business_department_ids());// 当前人管理的业务部门id串，以逗号分隔
            session.setAttribute("adjunct_department_ids", getAdjunctInfo(username));// 当前人兼职的行政部门以及归属行政部门递归id串，以逗号分隔

            AuthorizationInfo authorizationInfo;
            if (session.getAttribute("userInfo_shiro") == null) {
                authorizationInfo = userDto.getSimpleAuthorizationInfo();
                session.setAttribute("userInfo_shiro", authorizationInfo);
                log.info("将用户角色信息放入到session中：{}", JSONObject.toJSONString(authorizationInfo));
            }
        } else {
            doGetAuthenticationInfo = (AuthenticationInfo) session
                    .getAttribute("authenticationInfo_shiro");
            log.info("用户登录过-当前用户:{}", CurrentUser.getUsername());
        }
        return doGetAuthenticationInfo;
    }
}