package com.yixin.garage.rabbitMQ;

import com.alibaba.fastjson.JSONObject;
import com.yixin.garage.mq.RentCarVehicleImportConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @description:
 * @author: YixinCapital -- libochen
 * @create: 2019-08-09 13:03
 **/
@Component
public class test {

    public static final Logger logger = LoggerFactory.getLogger(test.class);

    public static void main(String[] args) {

        for (int i = 0; i <10 ; i++) {
            String uuid = UUID.randomUUID().toString();//获取UUID并转化为String对象
            uuid = uuid.replace("-", "");//因为UUID本身为32位只是生成时多了“-”，所以将它们去点就可
            System.out.println(uuid);

        }

    }

//    private static List<String> getDiffrentList(List<String> list1, List<String> list2) {
//        long st = System.nanoTime();
//        List<String> diff = new ArrayList<String>();
//        List<String> maxList = list1;
//        List<String> minList = list2;
//        if(list2.size()>list1.size())
//        {
//            maxList = list2;
//            minList = list1;
//        }
//        Map<String,Integer> map = new HashMap<String,Integer>(maxList.size());
//        for (String string : maxList) {
//            map.put(string, 1);
//        }
//        for (String string : minList) {
//            if(map.get(string)!=null)
//            {
//                map.put(string, 2);
//                continue;
//            }
//            diff.add(string);
//        }
//        for(Map.Entry<String, Integer> entry:map.entrySet())
//        {
//            if(entry.getValue()==1)
//            {
//                diff.add(entry.getKey());
//            }
//        }
//        return diff;
//    }



}
