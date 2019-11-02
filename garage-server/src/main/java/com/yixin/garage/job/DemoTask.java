package com.yixin.garage.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.yixin.common.utils.JobParamDTO;
import com.yixin.common.utils.JobStatus;
import com.yixin.garage.core.job.JobExecutor;

/**
 * 
 * @ClassName: DemoTask
 * @Description 定时任务Domo
 * @author  YixinCapital -- lizhongxin	   
 * @date  2018年12月26日 下午6:00:01
 *
 */
@Component("demoTask")
public class DemoTask  implements JobExecutor{
	private static Logger logger = LoggerFactory.getLogger(DemoTask.class);
	
	@Override
	public JobParamDTO execute(JobParamDTO jobParamDTO) {
		logger.info("==============================执行任务开始================================");	
		try {
			logger.info("执行相应任务。。。。。");
			jobParamDTO.setStatus(JobStatus.execute_success.getIndex());
			jobParamDTO.setResultContent("任务执行完毕！");
			jobParamDTO.setRetryFlag(false);
			logger.info("==============================执行任务结束================================");
			return jobParamDTO;
		}catch (Exception ex) {
			logger.error("执行任务异常！",ex);
			jobParamDTO.setStatus(JobStatus.execute_falied.getIndex());
			jobParamDTO.setResultContent("执行任务异常！");
			jobParamDTO.setRetryFlag(true);
			return jobParamDTO;
		}
		
	}
	
	
}

