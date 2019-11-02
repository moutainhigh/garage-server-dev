package com.yixin.garage.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yixin.garage.auth.CurrentUser;

@Controller
public class RedirectController {
    private static Logger logger = LoggerFactory.getLogger(RedirectController.class);

    /*@RequestMapping("/vue")
    public String path(@RequestParam("url") String url) {
        logger.info("---------------url:" + url + "---------------");
        return "redirect:/garage-vue/" + url;
    }*/
    
    @RequestMapping("/vue")
    public String path(@RequestParam(required=false,value="yunPortalUserName") String yunPortalUserName,
            @RequestParam("url") String url) {
        
        logger.info("-------传入url--------{}---------------yunPortalUserName:{}",url,yunPortalUserName);
        String destination = "redirect:/garage-vue/" + url;
        try {
            if (StringUtils.isNotBlank(yunPortalUserName) && !"yunPortalUserNameInfo".equals(yunPortalUserName)){
                String username = CurrentUser.getUsername();
                logger.info("-------传入url--------" + url + "---------------yunPortalUserName:{}----------------username:{}",yunPortalUserName,username);
                if (StringUtils.isNotBlank(username)){
                    if (!username.equals(yunPortalUserName)){
                        SecurityUtils.getSubject().logout();
                        destination = "redirect:/vue?yunPortalUserName=" + yunPortalUserName +"&url=" + url;
                    }
                }
            }
        }catch (Exception e){
            logger.info("--------重定向-------比较当前登录人异常--------------",e);
        }


        logger.info("--------重定向-------" + destination + "---------------");
        return destination;
    }
}
