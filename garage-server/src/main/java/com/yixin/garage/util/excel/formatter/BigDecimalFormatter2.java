package com.yixin.garage.util.excel.formatter;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 对于BigDecimal 类型数据保留两位小数。
 * 对于不是BigDecimal的直接返回原值原类型
 * Created by lishuaifeng on 2018/4/10.
 */
public class BigDecimalFormatter2 implements ValueFormatter{
    private BigDecimalFormatter2(){}
    public static BigDecimalFormatter2 getInstance(){
        return Nested.instance;
    }
    static class Nested{
        private static BigDecimalFormatter2 instance = new BigDecimalFormatter2();
    }
    @Override
    public Object format(Object oriValue, Object rowData) {
        if(oriValue==null){
            return null;
        }
        if(!(oriValue instanceof BigDecimal)){
            return oriValue;
        }
        return  ((BigDecimal) oriValue).setScale(2, RoundingMode.HALF_UP);
//        ((BigDecimal) oriValue).setScale(2, RoundingMode.HALF_UP);
//        return  String.format("%.2f ",oriValue);

    }

}
