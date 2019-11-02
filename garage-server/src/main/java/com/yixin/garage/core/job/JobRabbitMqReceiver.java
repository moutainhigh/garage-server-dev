package com.yixin.garage.core.job;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Resource;

import com.yixin.common.utils.*;
import com.yixin.garage.core.util.SpringContextHolder;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.rabbitmq.client.Channel;

/**
 * 任务调度RabbitMQ
 * 
 * Package : com.yixin.common.system.job
 * 
 * @author YixinCapital -- wujt 2016年8月12日 上午9:13:02
 *
 */
public class JobRabbitMqReceiver implements ChannelAwareMessageListener {

	private ConcurrentMap<String, String> localMap = new ConcurrentHashMap<>();

	private final Logger LOGGER = LoggerFactory.getLogger(JobRabbitMqReceiver.class);


	@Resource
	private RabbitTemplate jobRabbitTemplate;

	private String projectName;

	private ObjectMapper mapper = new ObjectMapper();

	public void handleMessage(JobResultDTO resultDTO) {
		if (resultDTO != null) {
			String projectKey = resultDTO.getProjectKey();
			String recordId = resultDTO.getRecordId();
			LOGGER.info("1.RecordId [{}]_rabbitMQ:Getted projectKey:{}", recordId, projectKey);
			String re = localMap.get(recordId);
			if (StringUtils.isNotBlank(re)) {
				LOGGER.info("2.RecordId [{}]_rabbitMQ:Handling projectKey:{}....", recordId,
						projectKey);
			} else {
				localMap.put(recordId, recordId);
				if (projectName.equals(projectKey)) { // 是当前系统的任务时，执行此任务
					LOGGER.info("3.RecordId [{}]_rabbitMQ:start execute job :{}", recordId,
							resultDTO.getJobKey());
					start(resultDTO);
					execute(resultDTO, 3);
					LOGGER.info("5.RecordId [{}]_rabbitMQ:Deal projectKey:{} success!", recordId,
							projectKey);
				}
			}
		}
	}

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		String result="";
		JobResultDTO resultDTO;
		String projectKey="";
		try{
			result = new String(message.getBody(),"utf-8");
			LOGGER.info("接收到调度任务消息, {}",result);
			if(org.springframework.util.StringUtils.isEmpty(result)){
				LOGGER.error("接收消息内容为空,丢弃该消息！");
				return ;
			}
			resultDTO = parseMessage(result);
			if (resultDTO != null) {
				projectKey = resultDTO.getProjectKey();
				handleMessage(resultDTO);
			}else{
				LOGGER.info("6.RecordId_rabbitMQ:从 {} 接收到的消息不符合标准，舍弃：{}", JobConstant.JOB_RECEIVE_TOPIC,
						message);
			}
		}catch (Exception e){
			LOGGER.error("6.RecordId_rabbitMQ:从 {} 接收到的消息不符合标准，舍弃：{}", JobConstant.JOB_RECEIVE_TOPIC,
					message);
		}finally {
			try {
				if (projectKey.equals(projectName)) {
					channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
				}
			} catch (IOException e1) {
				LOGGER.error("确认错误格式的消息异常，{}, {}",result,e1.getMessage());
			}
		}
	}

	/**
	 * 用反射的执行定时任务
	 * 
	 * @param resultDTO
	 * @return
	 * @author YixinCapital -- wujt 2016年8月15日 上午10:58:52
	 */
	private JobResultDTO execute(JobResultDTO resultDTO, int retries) {
		try {
			LOGGER.info("execute0.RecordId [{}]_rabbitMQ: come in execute...!",
					resultDTO.getRecordId());
			String jobKey = resultDTO.getJobKey();
			if (StringUtils.isNotBlank(jobKey)) {
				LOGGER.info("execute1.RecordId [{}]_rabbitMQ: Start execute...!",
						resultDTO.getRecordId());
				Class<?> clazz = Class.forName(jobKey);
				JobParamDTO paramDTO = new JobParamDTO();
				paramDTO.setAgrs(resultDTO.getAgrs());
				// 先从spring容器中取
				JobExecutor instance = null;
				try {
					instance = (JobExecutor) SpringContextHolder.getApplicationContext()
							.getBean(clazz);
				} catch (Exception e) {
					LOGGER.error(
							"execute2.RecordId [{}]_rabbitMQ:getInstance from springfactory has error",
							resultDTO.getRecordId(), e);
					resultDTO.setStatus(JobStatus.execute_exception.getIndex());
				}
				if (instance == null) {
					instance = (JobExecutor) clazz.newInstance();
				}
				if (instance != null) {
					LOGGER.info("execute3.RecordId [{}]_rabbitMQ:execute job ："
							+ resultDTO.getJobKey() + " start ...", resultDTO.getRecordId());
					paramDTO = instance.execute(paramDTO);
					resultDTO.setResultCode(paramDTO.getResultCode());
					resultDTO.setResultContent(paramDTO.getResultContent());
					resultDTO.setRetryFlag(paramDTO.isRetryFlag());
					resultDTO.setStatus(paramDTO.getStatus());
					LOGGER.info("execute4.RecordId [{}]_rabbitMQ:execute job ："
							+ resultDTO.getJobKey() + " end !!!", resultDTO.getRecordId());
				}
			}
		} catch (Exception e) {
			LOGGER.error("execute5.RecordId [{}]_rabbitMQ:execute job ：" + resultDTO.getJobKey()
					+ " has errors:", resultDTO.getRecordId(), e);
			resultDTO.setStatus(JobStatus.execute_exception.getIndex());
		} finally {
			if (resultDTO != null) {
				if (resultDTO.isRetryFlag() && retries > 0) { // 是否重发
					int t = retries - 1;
					resultDTO = execute(resultDTO, t); // 重新执行
				} else { // 无需再次执行时，发送消息给调度中心
					resultDTO.setEndExecuteTime(new Date());
					sendToRabbitMQ(resultDTO);
				}
			}
			localMap.remove(resultDTO.getRecordId());
		}
		return resultDTO;
	}

	/**
	 * 解析参数
	 * 
	 * @param message
	 * @return
	 * @author YixinCapital -- wujt 2016年8月15日 上午10:58:35
	 */
	private JobResultDTO parseMessage(String message) {
		JobResultDTO resultDTO = null;
		try {
			resultDTO = mapper.readValue(message, JobResultDTO.class);
		} catch (Exception e) {
			LOGGER.error("parse Message to JobResultDTO error!");
		}
		return resultDTO;
	}

	/**
	 * 开始执行时调用
	 * 
	 * @author YixinCapital -- wujt 2016年8月12日 上午9:46:52
	 */
	public void start(JobResultDTO jobResultDTO) {
		if (jobResultDTO != null) {
			jobResultDTO.setStartExecuteTime(new Date());
			jobResultDTO.setStatus(JobStatus.executing.getIndex());
			sendToRabbitMQ(jobResultDTO);
			LOGGER.info("4.RecordId [{}]_rabbitMQ:send startStatus projectKey:{} success!",
					jobResultDTO.getRecordId(), jobResultDTO.getJobKey());
		}
	}

	private void sendToRabbitMQ(JobResultDTO jobResultDTO) {
		String message = null;
		try {
			message = mapper.writeValueAsString(jobResultDTO);
		} catch (Exception e) {
			LOGGER.error("RecordId [{}]_rabbitMQ:transform jobResultDTO to json has error:",
					jobResultDTO.getRecordId(), e);
		}
		if (message != null) {
			try {
				jobRabbitTemplate.convertAndSend(JobConstant.EXCHANGESEND, JobConstant.ROUTINGKEYSEND,jobResultDTO);
				LOGGER.info(String.format("RecordId [{}]_rabbitMQ:[%s] sent message: %s",
						jobResultDTO.getRecordId(), JobConstant.EXCHANGESEND, message));
			} catch (Exception e) {
				LOGGER.error("RecordId [{}]_rabbitMQ:send message to rabbitMQ error！",
						jobResultDTO.getRecordId(), e);
			}
			LOGGER.info("RecordId [{}]_rabbitMQ:send message {} to " + JobConstant.JOB_SEND_TOPIC
					+ " sucess!", jobResultDTO.getRecordId(), message);
		}
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public ObjectMapper getMapper() {
		return mapper;
	}

	public void setMapper(ObjectMapper mapper) {
		this.mapper = mapper;
	}

}
