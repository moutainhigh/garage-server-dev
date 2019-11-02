//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yixin.garage.auth.logout;

import org.apache.shiro.SecurityUtils;
import org.jasig.cas.client.session.SessionMappingStorage;
import org.jasig.cas.client.session.SingleSignOutHandler;
import org.jasig.cas.client.util.AbstractConfigurationFilter;
import org.jasig.cas.client.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class NewSingleSignOutFilter extends AbstractConfigurationFilter {
    private static final SingleSignOutHandler handler = new SingleSignOutHandler();

    private static final HttpClient httpClient=new SimpleHttpClient();

    private static List<String> stringList=new ArrayList<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(NewSingleSignOutFilter.class);


    public NewSingleSignOutFilter() {
    }

    public NewSingleSignOutFilter(String casUrlList) {
        String [] casServerArr=casUrlList.split(",");
        stringList=Arrays.asList(casServerArr);
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        if(!this.isIgnoreInitConfiguration()) {
            handler.setArtifactParameterName(this.getPropertyFromInitParams(filterConfig, "artifactParameterName", "ticket"));
            handler.setLogoutParameterName(this.getPropertyFromInitParams(filterConfig, "logoutParameterName", "logoutRequest"));
        }
        LOGGER.info("cas logout url init done!");
        handler.init();
    }

    public void setArtifactParameterName(String name) {
        handler.setArtifactParameterName(name);
    }

    public void setLogoutParameterName(String name) {
        handler.setLogoutParameterName(name);
    }

    public void setSessionMappingStorage(SessionMappingStorage storage) {
        handler.setSessionMappingStorage(storage);
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        if(handler.isTokenRequest(request)) {
            handler.recordSession(request);
        } else {
            if(handler.isLogoutRequest(request)) {
                String logoutMessage = CommonUtils.safeGetParameter(request, "logoutRequest");
                LOGGER.info("登出请求路径uri：{}，入参信息logoutRequest：{}",request.getServletPath(),logoutMessage);
                String message= request.getParameter("logoutRequest");
                String logoutc=request.getParameter("logoutc");
                if (logoutc==null){
                    for (String str:stringList ) {
                        httpClient.sendMessageToEndPoint(str,message,true);
                        LOGGER.info("Service {} logout!",str);
                    }
                }else {
                    handler.destroySession(request);
                    LOGGER.info("**********单点登出**********清除CAS信息成功!************************************");
                }
                
                SecurityUtils.getSubject().logout();
                LOGGER.info("#################session登出#######################");
            }

            this.log.trace("Ignoring URI " + request.getRequestURI());
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }


    public void destroy() {
    }

    protected static SingleSignOutHandler getSingleSignOutHandler() {
        return handler;
    }
}
