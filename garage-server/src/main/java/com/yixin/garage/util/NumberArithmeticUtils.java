package com.yixin.garage.util;

import java.math.BigDecimal;
import java.util.List;

import com.google.common.base.Optional;
/**
 * @Description 数据算数计算工具类
 */
public class NumberArithmeticUtils {
	
	private NumberArithmeticUtils() {
	    throw new IllegalAccessError("Utility class");
	  }
	/**
	 * 一千
	 */
	public static final BigDecimal ONE_THOUSAND = BigDecimal.valueOf(1000);
	/**
	 * 一万
	 */
	public static final BigDecimal TEN_THOUSAND = BigDecimal.valueOf(10000);
	/**
	 * 一百万
	 */
	public static final BigDecimal ONE_MILLION = BigDecimal.valueOf(1000000);
	/**
	 * BigDecimal的加法运算封装
	 * @param b1
	 * @param bn
	 * @return Integer
	 */
   public static BigDecimal safeAdd(BigDecimal b1, BigDecimal... bn) {
       if (null == b1) {
           b1 = BigDecimal.ZERO;
       }
       if (null != bn) {
           for (BigDecimal b : bn) {
               b1 = b1.add(null == b ? BigDecimal.ZERO : b);
           }
       }
       return b1;
   }
 
   /**
    * Integer加法运算的封装
    * @param b1   第一个数
    * @param bn   需要加的加法数组
    * @注 ： Optional  是属于com.google.common.base.Optional<T> 下面的class
    * @return Integer
    */
   public static Integer safeAdd(Integer b1, Integer... bn) {
       if (null == b1) {
           b1 = 0;
       }
       Integer r = b1;
       if (null != bn) {
           for (Integer b : bn) {
               r += Optional.fromNullable(b).or(0);
           }
       }
       return r > 0 ? r : 0;
   }
 
   /**
    * 计算金额方法
    * @param b1
    * @param bn
    * @return BigDecimal
    */
   public static BigDecimal safeSubtract(BigDecimal b1, BigDecimal... bn) {
       return safeSubtract(true, b1, bn);
   }
 
   /**
    * BigDecimal的安全减法运算
    * @param isZero  减法结果为负数时是否返回0，true是返回0（金额计算时使用），false是返回负数结果
    * @param b1		   被减数
    * @param bn        需要减的减数数组
    * @return BigDecimal
    */
   public static BigDecimal safeSubtract(Boolean isZero, BigDecimal b1, BigDecimal... bn) {
       if (null == b1) {
           b1 = BigDecimal.ZERO;
       }
       BigDecimal r = b1;
       if (null != bn) {
           for (BigDecimal b : bn) {
               r = r.subtract((null == b ? BigDecimal.ZERO : b));
           }
       }
       return isZero ? (r.compareTo(BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : r) : r;
   }
 
   /**
    * 整型的减法运算，小于0时返回0
    * @param b1
    * @param bn
    * @return BigDecimal
    */
   public static Integer safeSubtract(Integer b1, Integer... bn) {
       if (null == b1) {
           b1 = 0;
       }
       Integer r = b1;
       if (null != bn) {
           for (Integer b : bn) {
               r -= Optional.fromNullable(b).or(0);
           }
       }
       return null != r && r > 0 ? r : 0;
   }
 
   /**
    * 金额除法计算，返回2位小数（具体的返回多少位大家自己看着改吧）
    * @param b1
    * @param b2
    * @return BigDecimal
    */
   public static <T extends Number> BigDecimal safeDivide(T b1, T b2){
       return safeDivide(b1, b2, BigDecimal.ZERO);
   }
   /**
    * 金额除法计算，返回scale位小数
    * @param b1
    * @param b2
    * @param scale
    * @return BigDecimal
    */
   public static <T extends Number> BigDecimal safeDivide(T b1, T b2,int scale){
       return safeDivide(b1, b2, BigDecimal.ZERO,scale);
   }
 
   /**
    * BigDecimal的除法运算封装，如果除数或者被除数为0，返回默认值
    * 默认返回小数位后2位，用于金额计算
    * @param b1
    * @param b2
    * @param defaultValue
    * @return BigDecimal
    */
   public static <T extends Number> BigDecimal safeDivide(T b1, T b2, BigDecimal defaultValue) {
       if (null == b1 || null == b2) {
           return defaultValue;
       }
       try {
           return BigDecimal.valueOf(b1.doubleValue()).divide(BigDecimal.valueOf(b2.doubleValue()), 2, BigDecimal.ROUND_HALF_UP);
       } catch (Exception e) {
           return defaultValue;
       }
   }
   
   /**
    * BigDecimal的除法运算封装，如果除数或者被除数为0，返回默认值
    * 默认返回小数位后scale位，用于金额计算
    * @param b1
    * @param b2
    * @param defaultValue
    * @param scale 
    * @return BigDecimal
    */
   public static <T extends Number> BigDecimal safeDivide(T b1, T b2, BigDecimal defaultValue ,int scale) {
       if (null == b1 || null == b2) {
           return defaultValue;
       }
       try {
           return BigDecimal.valueOf(b1.doubleValue()).divide(BigDecimal.valueOf(b2.doubleValue()), scale, BigDecimal.ROUND_HALF_UP);
       } catch (Exception e) {
           return defaultValue;
       }
   }
 
   /**
    * BigDecimal的乘法运算封装
    * @param b1
    * @param b2
    * @return BigDecimal
    */
   public static <T extends Number> BigDecimal safeMultiply(T b1, T b2) {
       if (null == b1 || null == b2) {
           return BigDecimal.ZERO;
       }
       return BigDecimal.valueOf(b1.doubleValue()).multiply(BigDecimal.valueOf(b2.doubleValue())).setScale(2, BigDecimal.ROUND_HALF_UP);
   }
   /**
    * 
    * @Title: divideByThousand   
    * @Description:返回 除以一万后的结果,结果四舍五入保留两位小数
    * @param b1
    * @return  BigDecimal    
    * @author YixinCapital -- lizhongxin
    *	       2019年1月29日 下午2:19:37
    */
   public static <T extends Number> BigDecimal divideByTenThousand(T b1) {
       if (null == b1) {
           return BigDecimal.ZERO;
       }
       try {
           return BigDecimal.valueOf(b1.doubleValue()).divide(TEN_THOUSAND,2, BigDecimal.ROUND_HALF_UP);
       } catch (Exception e) {
           return BigDecimal.ZERO;
       }
   }
   
   /**
    * 
    * @Description: 返回 除以一千后的结果,结果四舍五入保留两位小数
    * @param b1
    * @return BigDecimal
    * @throws 
    * @author YixinCapital -- mjj
    *	       2019年2月26日 上午11:36:21
    */
   public static <T extends Number> BigDecimal divideByBaiThousand(T b1) {
       if (null == b1) {
           return BigDecimal.ZERO;
       }
       try {
           return BigDecimal.valueOf(b1.doubleValue()).divide(ONE_THOUSAND,2, BigDecimal.ROUND_HALF_UP);
       } catch (Exception e) {
           return BigDecimal.ZERO;
       }
   }
   /**
    * 
    * @Title: safeDivided   
    * @Description: list中每个数值除以指定值
    * @param divideNum
    * @param lists      
    * void
    * @author YixinCapital -- lizhongxin
    *	       2019年4月2日 下午2:32:31
    */
   @SafeVarargs
   public static  void safeDivided(BigDecimal divideNum, List<BigDecimal> ... lists){
	   if(divideNum==null || BigDecimal.ZERO.compareTo(divideNum)==0 
			   || BigDecimal.ONE.compareTo(divideNum)==0 )
		   return;
	   if(lists!=null){
		   for(List<BigDecimal> list: lists){
			   for(int i=0;i<list.size();i++){
				   list.set(i, safeDivide(list.get(i), divideNum, 2));
			   }
		   }
	   }
   }
  
   
   
}

