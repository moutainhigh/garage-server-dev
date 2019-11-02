package com.yixin.garage.core.handler;

import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yixin.common.exception.BzException;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.core.model.exception.ServiceException;
import com.yixin.garage.util.ResultUtil;

/**
 * 
 * Package : com.yixin.garage.core.handler
 * 
 * @author YixinCapital -- lizhongxin 2017年10月18日 下午1:47:19
 *
 */
@ControllerAdvice
public class BzExceptionHandler {
	private final static Logger logger = LoggerFactory.getLogger(BzExceptionHandler.class);

	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public InvokeResult handle(Exception e) {
		if (e instanceof BzException) {
			BzException bzException = (BzException) e;
			logger.error("发生业务异常,异常信息:{}",bzException.getMessage(), bzException);
			return ResultUtil.error(bzException.getMessage());
		}else if (e instanceof ServiceException) {
			ServiceException exception = (ServiceException) e;
			logger.error("服务调用发生错误,错误号：{},失败信息:{}",exception.getCode(),exception.getMessage(), exception);
			return ResultUtil.error(exception.getMessage());
		}else if(e instanceof UnauthorizedException){
            logger.error("【无权限】{}", e.getMessage(),e);
            return ResultUtil.error("您没有当前操作的权限。请先退出然后重新登录，或清理浏览器缓存。如果仍然没有权限请联系管理员！");
        } else {
			logger.error("【系统异常】{}", e.getMessage(),e);
			return ResultUtil.error("系统异常");
		}
	}
}
