package com.yixin.garage.auth.logout;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.cas.CasToken;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.jasig.cas.client.session.SingleSignOutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @version 1.0
 * @Description:
 * @author:guowanfeng
 * @Date:Created in 14:36 2017/11/16
 */
//@Slf4j
public class NewCasFilter extends AuthenticatingFilter{
	
	private final static Logger log = LoggerFactory.getLogger(AuthenticatingFilter.class);
	
    private static final SingleSignOutHandler HANDLER = new SingleSignOutHandler();

    // the name of the parameter service ticket in url
    private static final String TICKET_PARAMETER = "ticket";

    // the url where the application is redirected if the CAS service ticket validation failed (example : /mycontextpatch/cas_error.jsp)
    private String failureUrl;

    /**
     * The token created for this authentication is a CasToken containing the CAS service ticket received on the CAS service url (on which
     * the filter must be configured).
     *
     * @param request the incoming request
     * @param response the outgoing response
     * @throws Exception if there is an error processing the request.
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String ticket = httpRequest.getParameter(TICKET_PARAMETER);
        return new CasToken(ticket);
    }

    /**
     * Execute login by creating {@link #createToken(ServletRequest, ServletResponse) token} and logging subject
     * with this token.
     *
     * @param request the incoming request
     * @param response the outgoing response
     * @throws Exception if there is an error processing the request.
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        return executeLogin(request, response);
    }

    /**
     * Returns <code>false</code> to always force authentication (user is never considered authenticated by this filter).
     *
     * @param request the incoming request
     * @param response the outgoing response
     * @param mappedValue the filter-specific config value mapped to this filter in the URL rules mappings.
     * @return <code>false</code>
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return false;
    }

    /**
     * If login has been successful, redirect user to the original protected url.
     *
     * @param token the token representing the current authentication
     * @param subject the current authenticated subjet
     * @param request the incoming request
     * @param response the outgoing response
     * @throws Exception if there is an error processing the request.
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
                                     ServletResponse response) throws Exception {
        issueSuccessRedirect(request, response);
        return false;
    }

    /**
     * If login has failed, redirect user to the CAS error page (no ticket or ticket validation failed) except if the user is already
     * authenticated, in which case redirect to the default success url.
     *
     * @param token the token representing the current authentication
     * @param ae the current authentication exception
     * @param request the incoming request
     * @param response the outgoing response
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException ae, ServletRequest request,
                                     ServletResponse response) {
        // is user authenticated or in remember me mode ?
        Subject subject = getSubject(request, response);
        //******************************************新增清除shiro信息开始***********************************
        HttpServletRequest req = (HttpServletRequest)request;
        if (HANDLER.isLogoutRequest(req)) {
            String logoutc=request.getParameter("logoutc");
            String message= request.getParameter("logoutRequest");
            log.error("**********************CAS当前登录消息[{}]*************************************",message);
            if (logoutc!=null) {
                try {
                    subject.logout();
                    log.error("**********************单点登出清除shiro信息完成*************************************");
                } catch (SessionException ise) {
                    log.error("**********************单点登出清除shiro信息失败*************************************");
                }
            }
            return false;
        }
        //******************************************新增清除shiro信息结束***********************************
        if (subject.isAuthenticated() || subject.isRemembered()) {
            try {
                issueSuccessRedirect(request, response);
            } catch (Exception e) {
                log.error("Cannot redirect to the default success url", e);
            }
        } else {
            try {
                WebUtils.issueRedirect(request, response, failureUrl);
            } catch (IOException e) {
                log.error("Cannot redirect to failure url : {}", failureUrl, e);
            }
        }
        return false;
    }

    public void setFailureUrl(String failureUrl) {
        this.failureUrl = failureUrl;
    }
}
