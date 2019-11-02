package com.yixin.garage.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yixin.common.exception.BzException;
import com.yixin.garage.core.util.SpringContextHolder;

import cn.hutool.core.date.format.FastDateFormat;

/**
 * Created by lianghaoguan on 2017/10/20.
 */
public class DateUtil {

    private final static Logger logger = LoggerFactory.getLogger(DateUtil.class);

//    private static ITestDateCfgService testDateCfgService;
    
    public static final String YYYYMMDD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_WITH_SECOND = "yyyy-MM-dd HH:mm:ss";
    /**
     * " 00:00:00"
     */
    public static final String begin = " 00:00:00";
    /**
     * " 23:59:59"
     */
    public static final String end = " 23:59:59";
    /**
     * " 24:00:00"
     */
    public static final String end1 = " 24:00:00";
    private static final int FREE_DAYS = 40;//从预计接车时间当天开始FREE_DAYS天内可免息销售

	/** 标准日期格式：yyyy年MM月dd日 */
	public final static String CHINESE_DATE_PATTERN = "yyyy年MM月dd日";
	/** 标准日期格式：yyyyMMdd */
	public final static String PURE_DATE_PATTERN = "yyyyMMdd";
	
	/** 标准日期格式 {@link FastDateFormat}：yyyy年MM月dd日 */
	public final static FastDateFormat CHINESE_DATE_FORMAT = FastDateFormat.getInstance(CHINESE_DATE_PATTERN);
	
	//-------------------------------------------------------------------------------------------------------------------------------- Pure
	/** 标准日期格式 {@link FastDateFormat}：yyyyMMdd */
	public final static FastDateFormat PURE_DATE_FORMAT = FastDateFormat.getInstance(PURE_DATE_PATTERN);
	
    private DateUtil() {
	    throw new IllegalAccessError("Utility class");
	  }

    /**
     * 获取当前日期所在月份的第一天 
     * @return
     */
    public static Date getCurMonthFirstDay(){
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),1);
        cal.add(Calendar.MONTH,0);
        cal.set(Calendar.DAY_OF_MONTH,1);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        return cal.getTime();
    }
    /**
     * 获取指定月份下第一天的日期
     * @param year
     * @param month
     * @return
     */
    public static Date getMonthFirstDay(String year,String month){
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(year),Integer.parseInt(month)-1,1);
        cal.add(Calendar.MONTH,0);
        cal.set(Calendar.DAY_OF_MONTH,1);
        return cal.getTime();
    }

    /**
     * 获取指定月份下的最后一天的日期
     * @param year
     * @param month
     * @return
     */
    public static Date getMonthLastDay(String year,String month){
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(year),Integer.parseInt(month)-1,1);
        cal.add(Calendar.MONTH,1);
        cal.set(Calendar.DAY_OF_MONTH,0);
        return cal.getTime();
    }
    /**
     * 把传入的yyyy-MM-dd时间转化为  yyyy-MM-dd 00:00:00
     * @param dateStr
     * @return
     */
    public static Date StringToBeginDate(String dateStr,String pattern) {
		DateFormat sdf = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			dateStr = dateStr.trim();
			date = sdf.parse(dateStr+begin);
		} catch (Exception e) {
			 logger.error(e.getMessage(), e);
		}
		return date;
	}
    /**
     * 把传入的yyyy-MM-dd时间转化为  yyyy-MM-dd 23:59:59
     * @param dateStr
     * @return
     */
    public static Date StringToEndDate(String dateStr,String pattern) {
		DateFormat sdf = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			dateStr = dateStr.trim();
			date = sdf.parse(dateStr+end);
		} catch (Exception e) {
			 logger.error(e.getMessage(), e);
		}
		return date;
	}
    /**
     * 传入开始时间，结束时间，最大时间间隔范围，获取符合条件的开始时间。
     * 
     * @param beginDate 开始时间  时间格式 yyyy-MM-dd
     * @param endDate	结束时间  时间格式 yyyy-MM-dd
     * @param rangeDays 开始时间与结束时间最大时间范围
     * @return 符合结束时间-开始时间<=rangeDays 的开始时间
     * @author YixinCapital -- lishuaifeng
     *	       2018年3月30日 下午3:11:04
     */
    public static Date beginDTByMaxDaysRange(String beginDate,String endDate,int rangeDays) {
		//开始时间不为空
    	if(StringUtils.isNotBlank(beginDate)){
    		return StringToBeginDate(beginDate,DATE_FORMAT_WITH_SECOND);
    	}
    	//开始时间为空的情况
    	if(StringUtils.isBlank(endDate)){//结束时间为空
			return addDate(new Date(),-rangeDays);
		}else{//结束时间不为空
			return addDate(StringToEndDate(endDate,DATE_FORMAT_WITH_SECOND),-rangeDays);
		}
		
	}
    /**
     * 传入开始时间，结束时间，最大时间间隔范围，获取符合条件的结束时间。
     * 
     * @param beginDate 开始时间  时间格式 yyyy-MM-dd
     * @param endDate	结束时间  时间格式 yyyy-MM-dd
     * @param rangeDays 开始时间与结束时间最大时间范围
     * @return 符合结束时间-开始时间<=rangeDays 的结束时间
     * @author YixinCapital -- lishuaifeng
     *	       2018年3月30日 下午3:11:04
     */
    public static Date endDTByMaxDaysRange(String beginDate,String endDate,int rangeDays) {
    	//结束时间不为空
    	if(StringUtils.isNotBlank(endDate)){
    		return StringToBeginDate(endDate,DATE_FORMAT_WITH_SECOND);
    	}
    	//结束时间为空的情况
    	if(StringUtils.isBlank(beginDate)){//开始时间为空
			return new Date();
		}else{//开始时间不为空
			return addDate(StringToEndDate(beginDate,DATE_FORMAT_WITH_SECOND),rangeDays);
		}
	}
    /**
     * 传入预计接车时间得到剩余免息天数
     * @param preDate  预计接车时间 不可为null
     * @return 剩余免息天数
     */
    public static int getRemainFreeDays(Date preDate){
    	return daysBetween(new Date(), preDate)+FREE_DAYS;
    }
    
	public static int compare_date(Date date, Date startDate, Date endDate) {
	    if(date==null || startDate==null || endDate==null ){
	        return 0;
	    }
		if (startDate.getTime() < date.getTime() && endDate.getTime() > date.getTime()) {
			return 1;
		} else {
			return 0;
		}
	}
	/**
	 * 第二个参数比第一个参数日期大则返回1，否则返回0.
	 * @param date
	 * @param endDate
	 * @return
	 */
	public static int compare_dates(Date date, Date endDate) {
		if ( endDate.getTime() > date.getTime()) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * 返回 bdate 大于 sdate
	 * @param sdate
	 * @param bdate
	 * @return
	 */
	public static boolean compareSimpleDate(Date sdate, Date bdate) {
		long time1 = getStartOfDay(sdate).getTime();
		long time2 = getStartOfDay(bdate).getTime();
		return  time2 - time1 > 0 ;
	}
	
	public static boolean isDateAsc(Date d1, Date d2) {
		long time1 = getStartOfDay(d1).getTime();
		long time2 = getStartOfDay(d2).getTime();
		return  (time2 - time1 >= 0);
		
	}

	/**
	 * 返回date1 是否到达 date2
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean date1ReachDate2(Date date1, Date date2){
		long time1 = getStartOfDay(date1).getTime();
		long time2 = getStartOfDay(date2).getTime();
		return  time1 - time2 >= 0;
	}
    /**
	 * 返回smdate 与 bdate 之间天数
	 * 
	 * @param smdate
	 * @param bdate
	 * @return 
	 * @author YixinCapital -- dujiangying
	 *	       2016年11月3日 上午11:36:26
	 */
	public static int daysBetween(String smdate, String bdate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		int num = 0;
		try {
			cal.setTime(sdf.parse(smdate));
			long time1 = cal.getTimeInMillis();
			cal.setTime(sdf.parse(bdate));
			long time2 = cal.getTimeInMillis();
			long between_days = (time2 - time1) / (1000 * 3600 * 24);
			num = Integer.parseInt(String.valueOf(between_days));
		} catch (ParseException e) {
			 logger.error(e.getMessage(), e);
		}
		return num;
	}
	
	/**
	 * 返回date1 与 date2 之间天数
	 * 
	 * @param date1
	 * @param date2
	 * @return 
	 * @author YixinCapital -- qinyunfei
	 *	       2017年11月10日 下午10:43:50
	 */
	public static int daysBetween(Date date1,Date date2){
		int result = 0;
		try{
		    Date da1 = getDateWithOutHour(date1);
		    Date da2 = getDateWithOutHour(date2);
		    Calendar cal = Calendar.getInstance();
		    cal.setTime(da1);
		    long time1 = cal.getTimeInMillis();
		    cal.setTime(da2);
		    long time2 = cal.getTimeInMillis();
		    long between_days=(time2-time1)/(1000*3600*24);
		    result = Integer.parseInt(String.valueOf(between_days));
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		}
	    return result;
    }

	/**
	 * 返回date1 与 date2 之间天数
	 *
	 * @param date1
	 * @param date2
	 * @return
	 * @author YixinCapital -- qinyunfei
	 *	       2017年11月10日 下午10:43:50
	 */
	public static int hoursBetween(Date date1,Date date2){
		long between = 0;
		between = (date2.getTime() - date1.getTime());// 得到两者的毫秒数
		long hour = between / (60 * 60 * 1000) ;
		return Integer.parseInt(String.valueOf(hour));
	}

	/**
	 * 获取Date日期yyyy-MM-dd
	 * 
	 * @param date
	 * @return 
	 * @author YixinCapital -- qinyunfei
	 *	       2017年11月10日 下午10:43:50
	 */
	public static Date getDateWithOutHour(Date date) throws ParseException{
	    SimpleDateFormat sdf=new SimpleDateFormat(YYYYMMDD);
	    String s=sdf.format(date);
	    return sdf.parse(s) ;  
    }

	/**
	 * 返回固定时间 加上 固定天数后的时间 (忽略时分秒)
	 * @param date
	 * @param day
	 * @return
	 * @author lianghaoguan
	 * @throws Exception 
 	 */
 	public static Date addDate(Date date, int day)  {
		Calendar cal = Calendar.getInstance();
		Date da1;
		try {
			da1 = getDateWithOutHour(date);
			cal.setTime(da1);
			cal.add(5, day);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return cal.getTime();
	}
 	
 	/**
	 * 返回固定时间 加上 固定天数后的时间
	 * @param date
	 * @param day
	 * @return Date
 	 */
 	public static Date addDateWithSecond(Date date, int day)  {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(5, day);
		return cal.getTime();
	}
 	/** 
     * 将Date类型时间转换为字符串 ，默认yyyy-MM-dd
     * @param date 
     * @return 
     */  
    public static String dateToString(Date date) {  
    	 return dateToString(date, YYYYMMDD);
    }

    /**
     * 
     * @Description: 将Date类型时间转换为字符串
     * @param date
     * @param formatStr
     * @return 
     * @return: String
     * @throws 
     * @author YixinCapital -- lizhongxin
     *	       2018年8月7日 下午4:54:26
     */
	public static String dateToString(Date date, String formatStr) {
		if(date == null) {
			return "";
		}
		DateFormat sdf = new SimpleDateFormat(formatStr);
		return sdf.format(date);
	}
	
	public static Date StringToDate(String dateStr, String formatStr) {
		DateFormat sdf = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (Exception e) {
			 logger.error(e.getMessage(), e);
		}
		return date;
	}
	/**
	 * 将yyyy-MM-dd 转为 Date,并抛异常
	 * 
	 * @param dateStr
	 * @return 
	 * @author YixinCapital -- lizhongxin
	 *	       2018年3月20日 上午10:10:26
	 */
	public static Date StringToDate(String dateStr) {
		DateFormat sdf = new SimpleDateFormat(YYYYMMDD);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (Exception e) {
			 logger.error(e.getMessage(), e);
			 throw new BzException(e);
		}
		return date;
	}
	
	/**
	 * 返回yyyy-MM
	 * 
	 * @param date
	 * @return 
	 * @author YixinCapital -- lizhongxin
	 *	       2018年3月6日 下午10:51:26
	 */
	public static String getYearMonthString(Date date){
		if(date == null){
			return null;
		}
		DateFormat sdf = new SimpleDateFormat(YYYYMMDD);
		String dateStr =sdf.format(date);
		return dateStr.substring(0, 7);
	}
	
	// 获得某天最大时间 2017-10-15 23:59:59
	public static Date getEndOfDay(Date date) {
		LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());;
		LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
		return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	// 获得某天最小时间 2017-10-15 00:00:00
	public static Date getStartOfDay(Date date) {
		LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
		LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
		return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
	}
	//判断时间大小，返回一个时间大于第二个时间
	public static  boolean firstBigerThanSecond(Date first,Date second){
		return first.getTime() > second.getTime();
	}

	/**
	 * 
	 * @Description: 两个日期之间相差的天数
	 * @param begin
	 * @param end
	 * @return long
	 * @throws 
	 * @author YixinCapital -- mjj
	 *	       2019年1月5日 上午10:31:37
	 */
    public static long getDaysBetween(Date begin, Date end) {

        Instant instant1 = begin.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant1, zone);
        LocalDate localDateBegin = localDateTime.toLocalDate();

        Instant instant2 = end.toInstant();
        LocalDateTime localDateTimeEnd = LocalDateTime.ofInstant(instant2, zone);
        LocalDate localDateEnd = localDateTimeEnd.toLocalDate();

        return localDateEnd.toEpochDay() - localDateBegin.toEpochDay();
    }

    /**
     * 
     * @Description: 根据期数（yyyyMM格式）获取当月最大几号（例如2月就是28号或29号）
     * @param fcastPeriod
     * @return int
     * @throws 
     * @author YixinCapital -- yangfei02
     *	       2019年1月16日 上午10:59:24
     */
    public static int getMonthLastDayNum(String fcastPeriod){
        Calendar cal = Calendar.getInstance();
        //获取上一个月最后一天
        cal.clear();
        cal.set(Calendar.YEAR, Integer.parseInt(fcastPeriod.substring(0, 4)));             //设置年份
        cal.set(Calendar.MONTH, Integer.parseInt(fcastPeriod.substring(4)) - 1);   
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);    //获取当前时间下，该月的最大日期的数字
    }
    
    /**
     * 
     * @Description: 获取date是当月第几个自然天
     * @param date
     * @return int 
     * @throws 
     * @author YixinCapital -- yangfei02
     *	       2019年1月17日 上午11:36:24
     */
    public static int getDayNumInMonth(Date date){
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }
    
    /**
     * 
     * @Description: 获取指定时间是一年中的几月份
     * @param date
     * @return int
     * @throws 
     * @author YixinCapital -- yangfei02
     *	       2019年3月9日 下午9:04:40
     */
    public static int getMonthNum(Date date){
        Calendar cal = Calendar.getInstance();
        //获取上一个月最后一天
        cal.clear();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }
    
    //获取当个月最后一个工作日是几号
//    public static Date parseDate(String year, String  month){
//    	
//    	Calendar cal = Calendar.getInstance();
//    	cal.set(Calendar.YEAR, Integer.valueOf(year));
//    	cal.set(Calendar.MONTH, Integer.valueOf(month));
//    	cal.set(field, value);
//    }
    
	/**
     * 
     * @Description: 获取当期最后一天
     * @param fcastPeriod
     * @return Date
     * @throws 
     * @author YixinCapital -- yangfei02
     *	       2019年1月16日 下午3:53:15
     */
    public static  Date getLastPeriodDay(String fcastPeriod){
        Calendar cal = Calendar.getInstance();
        //获取上一个月最后一天
        cal.clear();
        cal.set(Calendar.YEAR, Integer.parseInt(fcastPeriod.substring(0, 4)));             //设置年份
        cal.set(Calendar.MONTH, Integer.parseInt(fcastPeriod.substring(4)) - 1);       //设置月份，因为月份从0开始，所以用month - 1
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);    //获取当前时间下，该月的最大日期的数字
        cal.set(Calendar.DAY_OF_MONTH, lastDay);  //将获取的最大日期数设置为Calendar实例的日期数
        return cal.getTime();
    }
    
    /**
     * 
     * @Description: 获取上个月的期数
     * @param fcastPeriod yyyyMM
     * @return String
     * @throws 
     * @author YixinCapital -- yangfei02
     *	       2019年1月15日 下午6:23:18
     */
    public static String getLastPlanPeriod(String fcastPeriod){
    	
    	int year = Integer.parseInt(fcastPeriod.substring(0, 4));
    	int month = Integer.parseInt(fcastPeriod.substring(4));
    	if(month == 1){
    		return String.format("%d%d", year-1,12);
    	}else{
    		return String.format("%d%02d",year,month-1);
    	}
    	
    }
    
    /**
     * 
     * @Description: 获取当天是星期几
     * @param day
     * @return String
     * @throws 
     * @author YixinCapital -- yangfei02
     *	       2019年1月31日 下午5:09:20
     */
    public static String getWeekInfo(Date day){
//        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        cal.clear();
        cal.setTime(day);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
    
//    public static Date getgarageDate(){
//        if(testDateCfgService == null){
//            testDateCfgService = (ITestDateCfgService)SpringContextHolder.getApplicationContext().getBean("testDateCfgService");
//        }
//        return testDateCfgService.getgarageDate();
//    }

	/**
	 * @Title: getNowStr
	 * @Description: 返回当前时间字符串
	 * @return String
	 */
    public static String getNowStr(){
    	return dateToString(new Date(), DATE_FORMAT_WITH_SECOND);
    }
    public static void main(String[] args) {
        DateFormat sdf = new SimpleDateFormat("dd");
        System.out.println(sdf.format(new Date()));
	}
  
}
