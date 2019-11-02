package com.yixin.garage.util;

import com.yixin.common.utils.InvokeResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * 返回结果工具类
 * Package : com.yixin.garage.util
 * 
 * @author YixinCapital -- lizhongxin
 *		   2017年10月18日 下午1:41:05
 *
 */
public class  ResultUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(ResultUtil.class);
	/**
	 * 处理成功返回
	 * 
	 * @param object
	 * @return InvokeResult
	 * @author YixinCapital -- lizhongxin
	 *	       2017年10月18日 下午2:46:38
	 */
	public static <T> InvokeResult<T> success(T object) {
		InvokeResult<T> result = new InvokeResult<>();
        result.setData(object);
        return result;
    }
	/**
	 * 处理成功返回
	 * @return InvokeResult
	 * @author YixinCapital -- lizhongxin
	 *	       2017年10月18日 下午2:47:09
	 */
    public static InvokeResult success() {
        return success(null);
    }
    /**
     * 处理失败返回
     * @param errorMessage
     * @return InvokeResult
     * @author YixinCapital -- lizhongxin
     *	       2017年10月18日 下午2:47:26
     */
    public static InvokeResult error(String errorMessage) {
    	InvokeResult result = new InvokeResult();
        result.setHasErrors(true);
        result.setErrorMessage(errorMessage);
        return result;
    }

    /**
     * 该方法处理失败返回并且进行--事务回滚【前提1.操作处于事务范围内，2.事务还未进行提交或者回滚】。<br/>
     * 可用该方法取代抛异常进行回滚数据的方式。
     * @param errorMessage
     * @return
     */
    public static InvokeResult errorAndRollback(String errorMessage){
		InvokeResult result = new InvokeResult();
		result.setHasErrors(true);
		result.setErrorMessage(errorMessage);
		try {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }catch (NoTransactionException e){
            LOGGER.error("当前操作不在事务管理中无法进行数据回滚，异常信息：{}",e);
        }catch (Exception e){
		    LOGGER.error("数据回滚异常，错误信息为：{}",e);
        }
		return result;
	}
}

