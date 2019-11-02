package com.yixin.garage.util.redis;

public interface RedisPrefix {
	/**
	 * root层级的前缀不可单独使用
	 */
	String BASE_PREFIX = "garage:";
	/**
	 * 资金日预测手动刷新控制key
	 */
	String FUND_CASHFLOW_MANUAL_UPDATE = BASE_PREFIX+"fund:cashFlow:manualUpdate";
  
}
