package com.yixin.garage.service.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.yixin.garage.util.RestUtil;

/**
 * Created by lianghaoguan on 2018/3/3.
 */
public abstract class MessageSendService {

    private final static Logger logger = LoggerFactory.getLogger(MessageSendService.class);

    @Value("${msgCenter}")
    private String sendMessageUrl;

    @Async
    public void sendMessage(String content,String... args){
        logger.info("开始发送邮件/短信");
        logger.info("=============接收方邮箱：{}",args[1]);
        Object object = assemblyObj(content,args);
        if(object!=null){
            RestUtil.sendRequest(sendMessageUrl,object);
        }
        logger.info("邮件/短信发送成功");
    }

    public abstract Object assemblyObj(String content,String... args);
}
