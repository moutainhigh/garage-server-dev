package com.yixin.garage.util.excel.formatter;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 对于BigDecimal 类型数据保留两位小数并处理为包含两位小数的字符串。
 * Created by lishuaifeng on 2018/4/10.
 */
public class BigDecimalFormatter implements ValueFormatter{
    private BigDecimalFormatter(){}
    public static BigDecimalFormatter getInstance(){
        return Nested.instance;
    }
    static class Nested{
        private static BigDecimalFormatter instance = new BigDecimalFormatter();
    }
    @Override
    public Object format(Object oriValue, Object rowData) {
        if(oriValue==null){
            return null;
        }
        if(!(oriValue instanceof BigDecimal)){
            return BigDecimal.ZERO;
        }
        return  ((BigDecimal) oriValue).setScale(2, RoundingMode.HALF_UP);
//        ((BigDecimal) oriValue).setScale(2, RoundingMode.HALF_UP);
//        return  String.format("%.2f ",oriValue);

    }

}
