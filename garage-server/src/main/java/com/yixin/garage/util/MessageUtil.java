package com.yixin.garage.util;

import com.yixin.garage.core.util.SpringContextHolder;
import com.yixin.garage.service.message.MessageSendService;

public class MessageUtil {
    private static MessageSendService messageSendService;
    public static void sendMail(String content,String title, String mailTo){
        if(messageSendService == null){
            messageSendService = (MessageSendService) SpringContextHolder.getApplicationContext()
                    .getBean("mailMessageService");
        }
        messageSendService.sendMessage(content, title, mailTo);
    }
}

