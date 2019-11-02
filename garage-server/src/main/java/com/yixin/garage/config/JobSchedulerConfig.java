package com.yixin.garage.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.yixin.garage.core.job.JobRabbitMqReceiver;

/**
 * 
 * @ClassName: JobSchedulerConfig
 * @Description 定时任务配置
 * @author  YixinCapital -- lizhongxin	   
 * @date  2018年12月26日 下午5:54:19
 *
 */
@Configuration
public class JobSchedulerConfig {
	
	@Value("${yixin.projectName}")
	private String projectName;
	
	/**
	 * @Title: jobExecutor   
	 * @Description: 调度中心线程池配置  
	 * @return  ThreadPoolTaskExecutor
	 * @author YixinCapital -- lizhongxin
	 *	       2018年12月26日 下午4:22:58
	 */

	@Bean("rabbitMqJobScheduler")
	public JobRabbitMqReceiver jobRabbitMqReceiver(){
		JobRabbitMqReceiver jobRabbitMqReceiver = new JobRabbitMqReceiver();
		jobRabbitMqReceiver.setProjectName(projectName);
		return jobRabbitMqReceiver;
	}
	
	
}

