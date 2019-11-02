package com.yixin.garage.auth_local;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.auth.CurrentUser;
import com.yixin.garage.util.ConfigUtil;
import com.yixin.garage.util.ResultUtil;

/**
 * 本地開發環境登陸
 * 生产环境屏蔽本地登录接口
 * @author lishuaifeng
 *
 */
@Controller
@RequestMapping("/local/")
public class LocalLoginController {
	Logger logger = LoggerFactory.getLogger(getClass());
	@RequestMapping("login")
	@ResponseBody
	public InvokeResult<String> login(@RequestBody LocalUser user,HttpServletRequest req){
	   if(StringUtils.isBlank(user.getUserName()) || StringUtils.isBlank(user.getPassword())){
		   logger.info("账号或密码不能为空!"); 
		   return ResultUtil.error("账号或密码不能为空！");
       }
       if("pro".equals(ConfigUtil.getValue("env", "pro"))){
            //生产环境屏蔽本地登录页面的免密登录接口
           logger.error("有用户非法登录，ip:{}，操作账号：{}",req.getRemoteAddr(),user.getUserName());
           return ResultUtil.error("非法登录，操作已记录！ip："+req.getRemoteAddr());
       }
       // 获取主体
       Subject subject = SecurityUtils.getSubject();
       try{
           // 调用安全认证框架的登录方法
           subject.login(new UsernamePasswordToken(user.getUserName(), user.getPassword()));
       }catch(AuthenticationException ex){
           System.out.println("登陆失败: " + ex.getMessage());
           return ResultUtil.error("登录过程出错！");
       }
       logger.info("=======登陸成功========");
       return ResultUtil.success("登录成功！");
	} 
	
	@GetMapping("getlogin")
	@ResponseBody
	public InvokeResult<String> getlogin(String username){
	   if(StringUtils.isBlank(username)){
		   logger.info("账号不能为空!"); 
		   return ResultUtil.error("账号或密码不能为空！");
       }
       // 获取主体
       Subject subject = SecurityUtils.getSubject();
       try{
           // 调用安全认证框架的登录方法
           subject.login(new UsernamePasswordToken(username, "1111"));
       }catch(AuthenticationException ex){
           System.out.println("登陆失败: " + ex.getMessage());
           return ResultUtil.error("登录过程出错！");
       }
       logger.info("=======登陸成功========");
       return ResultUtil.success(CurrentUser.getCnName() + "登录成功！");
	} 
	@RequestMapping("loginView")
	@ResponseBody
	public InvokeResult<String> loginView(){
		logger.info("=========请重新登录=========");
		return ResultUtil.error("请重新登录！");
	} 
	
	@RequestMapping("loginOut")
	@ResponseBody
	public InvokeResult<String> loginOut(){
		SecurityUtils.getSubject().logout();
		logger.info("=======退出成功========");
	   return ResultUtil.success("退出成功！");
	} 
	
	/**
	 * 查询当前登陆人及渠道信息。
	 * @return
	 * @author lishuaifeng
	 */
	@GetMapping(value = "queryCurrentUserChannelInfo")
	@ResponseBody
	InvokeResult<Map<String, String>> queryCurrentUserChannelInfo(){
		Map<String, String> channelInfo = new HashMap<>();
		logger.info("查询当前登陆人渠道信息接口,当前登陆人域账号：{}，名字：{},角色id:{}"
				,CurrentUser.getUsername(),CurrentUser.getCnName(),CurrentUser.getUserRoles());
		channelInfo.put("userAccount", CurrentUser.getUsername());
		channelInfo.put("userCnName", CurrentUser.getCnName());
		if(org.apache.commons.lang3.StringUtils.isBlank(CurrentUser.getUsername())){
			ResultUtil.error("请登录之后再进行操作！");
		}
		return ResultUtil.success(channelInfo);
	}
}
