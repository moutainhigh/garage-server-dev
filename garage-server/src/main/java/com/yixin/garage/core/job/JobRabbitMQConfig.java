package com.yixin.garage.core.job;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.yixin.common.utils.JobConstant;

@EnableRabbit
@Configuration
public class JobRabbitMQConfig {
	// RabbitMQ的配置信息
	@Value("${spring.rabbitmq.addresses}")
    private String rabbitAddress;
	@Value("${spring.rabbitmq.username}")
    private String rabbitUser;
	@Value("${spring.rabbitmq.password}")
    private String rabbitPwd;
	@Value("${yixin.projectName}")
	private String projectName;

	public String getRabbitAddress() {
        return rabbitAddress;
    }

    public void setRabbitAddress(String rabbitAddress) {
        this.rabbitAddress = rabbitAddress;
    }

    public String getRabbitUser() {
        return rabbitUser;
    }

    public void setRabbitUser(String rabbitUser) {
        this.rabbitUser = rabbitUser;
    }

    public String getRabbitPwd() {
        return rabbitPwd;
    }

    public void setRabbitPwd(String rabbitPwd) {
        this.rabbitPwd = rabbitPwd;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Bean(name="jobConnectionFactory")
	public ConnectionFactory jobConnectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setAddresses(rabbitAddress);
		connectionFactory.setUsername(rabbitUser);
		connectionFactory.setPassword(rabbitPwd);
		connectionFactory.setVirtualHost(JobConstant.VIRTUALHOST);
		connectionFactory.setPublisherConfirms(true);// 确认机制
		return connectionFactory;
	}

	@Bean(name="jobRabbitTemplate")
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public AmqpTemplate jobRabbitTemplate() {
		RabbitTemplate template = new RabbitTemplate(this.jobConnectionFactory());
		template.setMessageConverter(this.jobJsonMessageConverter());
		template.setMandatory(true);
		return template;
	}

	@Bean
	public DirectExchange directExchange() {
		return new DirectExchange("job.receive.exchange.direct."+projectName);
	}
	/**
	 * 绑定
	 *
	 * @return
	 */
	@Bean
	public Binding binding() {
		return BindingBuilder.bind(queueReceive()).to(directExchange())
				.with("job.receive.routingKey."+projectName);
	}

	@Bean
	public Queue queueReceive() {
		return new Queue(JobConstant.JOB_RECEIVE_TOPIC+"." + projectName, true); // 队列持久
	}

	@Bean(name="jobJsonMessageConverter")
	public MessageConverter jobJsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean(name="jobRabbitMqReceiver")
	JobRabbitMqReceiver jobRabbitMqReceiver() {
		JobRabbitMqReceiver receiver = new JobRabbitMqReceiver();
		receiver.setProjectName(projectName);
		return receiver;
	}

	@Bean(name="jobListenerAdapter")
    MessageListenerAdapter jobListenerAdapter() {
		return new MessageListenerAdapter(jobRabbitMqReceiver(), "onMessage");
	}

	@Bean(name="jobMessageListenerContainer")
	public SimpleMessageListenerContainer jobMessageListenerContainer(
			@Qualifier("jobListenerAdapter")MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(this.jobConnectionFactory());
		container.setQueueNames(JobConstant.JOB_RECEIVE_TOPIC+"." + projectName);
		container.setExposeListenerChannel(true);
		container.setMaxConcurrentConsumers(15);
		container.setConcurrentConsumers(3);
		container.setAcknowledgeMode(AcknowledgeMode.MANUAL); // 设置确认模式手工确认
		container.setMessageListener(listenerAdapter);
		container.setDefaultRequeueRejected(false);
		return container;
	}

//	@Bean
//	public CharacterEncodingFilter characterEncodingFilter() {
//		CharacterEncodingFilter filter = new CharacterEncodingFilter();
//		filter.setEncoding("UTF-8");
//		filter.setForceEncoding(true);
//		return filter;
//	}

}
