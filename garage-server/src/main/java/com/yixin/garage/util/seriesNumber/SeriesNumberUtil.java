package com.yixin.garage.util.seriesNumber;

import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yixin.common.exception.BzException;
import com.yixin.garage.util.RedisClientUtil;
//@Component
public class SeriesNumberUtil {
	private static Logger logger = LoggerFactory.getLogger(SeriesNumberUtil.class);
	private static String PREFIX = "garage:";
	private static String DATE_PATTERN = "yyyyMMdd";
	private static String NUM_PATTERN_MIN = "%04d";
	private static String NUM_PATTERN_MAX = "%015d";
	private static int DEFAULT_SERIES_LEN = 4;
	
	private static String getDateStr(){//修复时区不对导致的序列号日期跟当前不对应的问题。
        return DateFormatUtils.format(new Date(),DATE_PATTERN, Locale.CHINA);
        //return DateUtils.formatDate(new Date(), DATE_PATTERN);

	}
	
	private static RedisClientUtil redisClientUtil;
	@Autowired
	public void setRedisClientUtil(RedisClientUtil redis){
		redisClientUtil = redis;
	}
	/**
	 * 生成4位序列号
	 * @param type
	 * @return
	 */
	public static String getSeriesNo(String type){
		return getSeriesNo(type,DEFAULT_SERIES_LEN);
	}
	/**
	 * 按传入长度参数生成对应位数的序列号
	 * @param type
	 * @param seriesLen  传入数值小于4位按4位处理  大于15按15位处理
	 * @return
	 */
	public static String getSeriesNo(String type,int seriesLen) {
		try {
			if(StringUtils.isBlank(type)){
				throw new BzException("序列号类型不能为空！");
			}
			String dateStr = getDateStr();
			String key = PREFIX+type+":"+dateStr;
			String seriesPreFix = type.toUpperCase()+dateStr;
			long num = redisClientUtil.incr(key);
			String num_pattern = "%04d";
			if(seriesLen<=4){
				num_pattern = NUM_PATTERN_MIN;
			}else if(seriesLen<15){
				num_pattern = "%0"+seriesLen+"d";
			}else{
				num_pattern = NUM_PATTERN_MAX;
			}
			return seriesPreFix+=String.format(num_pattern, num);
		} catch (Exception e) {
			logger.warn("获取序列号出错，信息为：{}",e);
			throw new BzException("生成序列号出错，请稍后再试！");
		}
	}

	/**
	 * 传入指定日期生成序列号。
	 * @param type
	 * @param seriesLen
	 * @param dateStr	yyyyMMdd 形式
	 * @return
	 */
	public static String getSeriesNo(String type,int seriesLen,String dateStr) {
		try {
			if(StringUtils.isBlank(type)){
				throw new BzException("序列号类型不能为空！");
			}
			String key = PREFIX+type+":"+dateStr;
			String seriesPreFix = type.toUpperCase()+dateStr;
			long num = redisClientUtil.incr(key);
			String num_pattern = "%04d";
			if(seriesLen<=4){
				num_pattern = NUM_PATTERN_MIN;
			}else if(seriesLen<15){
				num_pattern = "%0"+seriesLen+"d";
			}else{
				num_pattern = NUM_PATTERN_MAX;
			}
			return seriesPreFix+=String.format(num_pattern, num);
		} catch (Exception e) {
			logger.warn("获取序列号出错，信息为：{}",e);
			throw new BzException("生成序列号出错，请稍后再试！");
		}
	}

}
