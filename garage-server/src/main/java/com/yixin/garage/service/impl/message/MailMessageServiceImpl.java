package com.yixin.garage.service.impl.message;

import com.yixin.garage.dto.message.MessageDTO;
import com.yixin.garage.dto.message.mail.YxMailMessageDTO;
import com.yixin.garage.service.message.MessageSendService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Created by lianghaoguan on 2018/3/3.
 */
@Component("mailMessageService")
public class MailMessageServiceImpl extends MessageSendService {

    @Value("${mailfrom}")
    private String mailfrom;

    @Value("${mailCc}")
    private String mailCc;

    @Override
    public Object assemblyObj(String content,String... args) {
        MessageDTO<YxMailMessageDTO> messageDTO = new MessageDTO<>();
        messageDTO.setType(2);//2 代表的类型是邮件
        if(StringUtils.isEmpty(content)){
            return null;
        }
        YxMailMessageDTO yxMailMessage = new YxMailMessageDTO();
        yxMailMessage.setHtmlFlag("1");
        yxMailMessage.setMailFrom(mailfrom);
        yxMailMessage.setMailText(content);
        yxMailMessage.setMailTo(args[1]);
        yxMailMessage.setMailCc(mailCc);
        //设置邮件标题
        if(args.length<1){
            return null;
        }
        yxMailMessage.setMailSubject(args[0]);

        messageDTO.setData(yxMailMessage);
        return messageDTO;
    }
}
