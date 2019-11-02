package com.yixin.garage.xss;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/** 
 * 拦截防止xss注入
 * 通过Jsoup过滤请求参数内的特定字符
 * @author yangwk 
 */  
public class XssFilter implements Filter {
    private List<Pattern> excludes = null;
    @Override
    public void init(FilterConfig config) throws ServletException {
        String excludesStr = config.getInitParameter("excludes");
        if(StringUtils.isBlank(excludesStr)){
            return;
        }
        String[] arr = excludesStr.split(",");
        int len = arr.length;
        excludes = new ArrayList<>(len);
        for (int i = 0; i <len ; i++) {
            Pattern p = Pattern.compile("^" + arr[i]);
            excludes.add(p);
        }

    }
 
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if(handleExcludeURL(request,response)){
            chain.doFilter(request, response);
            return;
        }
        XssHttpServletRequestWrapper xssHttpServletRequestWrapper = new XssHttpServletRequestWrapper((HttpServletRequest)request);
        chain.doFilter(xssHttpServletRequestWrapper, response);
    }
 
    @Override
    public void destroy() {}

    private boolean handleExcludeURL(ServletRequest req, ServletResponse res) {
        HttpServletRequest request = (HttpServletRequest) req;
        if (excludes == null || excludes.isEmpty()) {
            return false;
        }
        String url = request.getServletPath();
        //yxlogger.info("handleExcludeURL:url={}",url);
        for (Pattern p : excludes) {
            Matcher m = p.matcher(url);
            if (m.find()) {
                return true;
            }
        }

        return false;
    }
    
} 

