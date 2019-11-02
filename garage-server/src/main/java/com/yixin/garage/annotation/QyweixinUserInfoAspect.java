package com.yixin.garage.annotation;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.yixin.common.exception.BzException;
import com.yixin.garage.auth.CurrentUser;
import com.yixin.garage.dto.api.QywxBaseDTO;

@Aspect
@Component
public class QyweixinUserInfoAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QyweixinUserInfoAspect.class);

	@Pointcut("@annotation(com.yixin.garage.annotation.QyweixinUserInfoAnn)")
	public void qywxCurrentUserAspect(){
	}
	/**
	 * @Title: doBefore   
	 * @Description: 请求方法前模拟本地登陆   
	 * @param point      
	 * @author YixinCapital -- lizhongxin
	 *	       2018年10月17日 下午7:02:11
	 */
	@Before("qywxCurrentUserAspect()")
	public void doBefore(JoinPoint point){
		LOGGER.info("doBefore 方法开始，设置企业微信请求用户信息。 ");
		long bg = System.currentTimeMillis();
		Object[] args = point.getArgs();
		if (args == null) return;
		if (args[0] instanceof QywxBaseDTO) {
			QywxBaseDTO dto = (QywxBaseDTO) args[0];
			if (StringUtils.isNotBlank(dto.getDomainAccount())) {
				// 获取主体
				Subject subject = SecurityUtils.getSubject();
				try {
					// 调用安全认证框架的登录方法
					subject.login(new UsernamePasswordToken(dto.getDomainAccount(), "qyweixin"));
				} catch (AuthenticationException ex) {
					LOGGER.error("登陆失败,失败信息: {}", ex.getMessage(), ex);
					throw new BzException("用户模拟登陆失败");
				}
				long ed = System.currentTimeMillis();
				LOGGER.info("doBefore 方法结束，用户：{},，登录耗时：{}秒",dto.getDomainAccount(),(ed-bg)/1000.0);
				LOGGER.info("用户:{} =======登陸成功========", dto.getDomainAccount());
			}else{
				LOGGER.error("模拟登陆用户域账号为空");
			}
		}
	}
	/**
	 * @Title: doAfter   
	 * @Description: 方法结束用户本地登出
	 * @param joinPoint      
	 * @author YixinCapital -- lizhongxin
	 *	       2018年10月17日 下午7:35:23
	 */
	 @AfterReturning("qywxCurrentUserAspect()")
	 public void doAfter(JoinPoint joinPoint) {
		 long bg = System.currentTimeMillis();
		 if(StringUtils.isNotBlank(CurrentUser.getUsername())){
			 LOGGER.info("调用方法完成用户{}退出本地登陆。",CurrentUser.getCnName());
			 SecurityUtils.getSubject().logout();
			 LOGGER.info("=======退出成功========。");
		 }else{
			 LOGGER.info("当前用户为空不用登出。");
		 }
		 long ed = System.currentTimeMillis();
		 LOGGER.info("用户登出耗时{}秒",(ed-bg)/1000.0);
	 }
	 
	 @AfterThrowing("qywxCurrentUserAspect()")
	 public void doAfterThrow(JoinPoint joinPoint) {
		 doAfter(joinPoint);
	 }
}

