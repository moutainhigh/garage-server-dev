package com.yixin.garage.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by lishuaifeng on 2018/7/16.
 */
public class NumberUtils {
    /**
     * bigDecimal 类型数据转化为字符串并保留指定位数小数
     * @return
     */
    public static String bigDecimal2String(BigDecimal num ,int dotLenth){
        if(num==null){
            return "0.00";
        }
        num.setScale(dotLenth, RoundingMode.HALF_UP);
        return  String.format("%.2f ",num);
    }
    /**
     * bigDecimal 类型数据转化为字符串并保留两位小数
     * @return
     */
    public static String bigDecimal2String(BigDecimal num){
        if(num==null){
            return "0.00";
        }
        num.setScale(2, RoundingMode.HALF_UP);
        return  String.format("%.2f ",num);
    }
    
    /**
     * 
     * @Title: isNumeric   
     * @Description: 判断字符串是否为数值,
     * @param str
     * @return boolean 是否为数值串     
     * @author YixinCapital -- lizhongxin
     *	       2019年3月11日 下午4:25:24
     */
	public static boolean isNumeric(String str) {
		if(StringUtils.isBlank(str)) return false;
		Pattern pattern = Pattern.compile("(-|\\+)?[0-9]+\\.*[0-9]*");
		Matcher isNum = pattern.matcher(str);
		return isNum.matches();
	}
	
	
	
	
	public static void main(String[] args) {
		System.out.println(Pattern.matches("(-|\\+)?[0-9]+\\.*[0-9]*", "032"));
		BigDecimal d = new BigDecimal("+0034.2");
		System.out.println(d);
		
	}
}
