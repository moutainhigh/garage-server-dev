package com.yixin.garage.mq;


import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * 同步白名单信息
 * @author yangfei02
 *
 */
//@Component
public class RentCarVehicleImportConsumer {
	public static final Logger logger = LoggerFactory.getLogger(RentCarVehicleImportConsumer.class);


	private Logger log = LoggerFactory.getLogger(RentCarVehicleImportConsumer.class);

	@RabbitListener(queues = "loanGetGarageInfo")
     public void process(Message message, Channel channel) throws IOException {
 		// 采用手动应答模式, 手动确认应答更为安全稳定
		 channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
		 log.info("receive: " + new String(message.getBody()));
	 }
}
