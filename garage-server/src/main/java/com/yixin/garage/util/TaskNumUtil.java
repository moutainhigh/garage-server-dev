package com.yixin.garage.util;

import com.yixin.common.exception.BzException;
import com.yixin.garage.dao.order.SgGarageDetailMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author YixinCapital -- liyaqing
 * @description: TODO
 * @date 2019/8/2817:49
 */
@Component
public class TaskNumUtil {

    private final static Logger logger = LoggerFactory.getLogger(TaskNumUtil.class);

    @Autowired
    private SgGarageDetailMapper sgGarageDetailMapper;

    /**
     * 生成出库库调配任务单号
     *
     * @return
     * @author YixinCapital -- qinyunfei
     *	       2016年6月29日 下午3:09:04
     */
    public static String createTaskNum(String sign,String maxGarageOrderNum) throws BzException{
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String dateString = formatter.format(new Date());
        String createPassWord = createPassWord();
        String custNumString = "";
        if(null == sign){
            custNumString = "06" + dateString;
        }else{
            if("CK".equals(sign)){
                custNumString = "CK" + createPassWord + dateString;
            }else if("RK".equals(sign)){
                custNumString = "RK" + createPassWord + dateString;
            }
        }

        String maxContractNum = dateString + "001";
        if (null != maxGarageOrderNum && !"".equals(maxGarageOrderNum)) {
            maxContractNum = maxGarageOrderNum;
        }
        String lastFiveString = StringUtils.substringAfter(maxContractNum, dateString);
        String nextString = custNumString + String.format("%03d", NumberUtils.toInt(lastFiveString) + 1);
        return nextString;
    }

    /**
     * 生成三位随机码
     * @Title: createPassWord
     * @Description: TODO
     * @return String
     * @author YixinCapital -- liyaqing
    2018年1月6日下午4:02:56
     */
    public static String createPassWord(){
        //得到0.0到1.0之间的数字，并扩大100000倍
        double temp = Math.random()*100000;
        //如果数据等于100000，则减少1
        if(temp>=100000){
            temp = 99999;
        }

        int tempint = (int)Math.ceil(temp);
        Random rd = new Random(tempint);
        final int  maxNum = 62;
        StringBuffer sb = new StringBuffer();
        int rdGet;//取得随机数
        char[] str = { 'A','B','C','D','E','F','G','H','I','J','K',
                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y' ,'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

        int count=0;
        while(count < 3){
            rdGet = Math.abs(rd.nextInt(maxNum));//生成的数最大为62-1
            if (rdGet >= 0 && rdGet < str.length) {
                sb.append(str[rdGet]);
                count ++;
            }
        }
        return sb.toString();
    }




    /**
     * @Title: createGarageOrderTaskNum
     * @Description: 随机创建order taskNum
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/28 18:31
     */
    public static String createGarageOrderTaskNum(String maxGarageOrderNum) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String dateString = formatter.format(new Date());
        String createPassWord = createPassWord();
        String custNumString = "";
        custNumString = "DP" + createPassWord + dateString;
        String maxContractNum = dateString + "001";
        if (null != maxGarageOrderNum && !"".equals(maxGarageOrderNum)) {
            maxContractNum = maxGarageOrderNum;
        }
        String lastFiveString = StringUtils.substringAfter(maxContractNum, dateString);
        String nextString = custNumString + String.format("%03d", NumberUtils.toInt(lastFiveString) + 1);
        return nextString;
    }


    public static String createInventoryBillNum(String maxGarageOrderNum) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String dateString = formatter.format(new Date());
        String createPassWord = createPassWord();
        String custNumString = "";
        custNumString = "PD" + createPassWord + dateString;
        String maxContractNum = dateString + "001";
        if (null != maxGarageOrderNum && !"".equals(maxGarageOrderNum)) {
            maxContractNum = maxGarageOrderNum;
        }
        String lastFiveString = StringUtils.substringAfter(maxContractNum, dateString);
        String nextString = custNumString + String.format("%03d", NumberUtils.toInt(lastFiveString) + 1);
        return nextString;








    }



    public static String createBatchNum() {
        String dateString = formatDate(new Date(), "yyyyMMddHHmmss");
        String custNumString = "PSM" + dateString + createPassWord();
        return custNumString;
    }

    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat value = new SimpleDateFormat(pattern);
        sdfThread.set(value);
        SimpleDateFormat format = sdfThread.get();
        return format.format(date);
    }

    private static ThreadLocal<SimpleDateFormat> sdfThread = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }

        @Override
        public SimpleDateFormat get() {
            // TODO Auto-generated method stub
            return super.get();
        }

        @Override
        public void set(SimpleDateFormat value) {
            super.set(value);
        }

        @Override
        public void remove() {
            // TODO Auto-generated method stub
            super.remove();
        }
    };
}
