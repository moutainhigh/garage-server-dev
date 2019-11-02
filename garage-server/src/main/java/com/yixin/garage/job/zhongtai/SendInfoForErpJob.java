package com.yixin.garage.job.zhongtai;

import com.yixin.common.utils.JobParamDTO;
import com.yixin.common.utils.JobStatus;
import com.yixin.garage.core.job.JobExecutor;
import com.yixin.garage.service.zhongtai.ISgSendInfoToZhongtaiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * @ClassName: DemoTask
 * @Description 定时任务Domo
 * @author  YixinCapital -- lizhongxin	   
 * @date  2018年12月26日 下午6:00:01
 *
 */
@Component("sendInfoForErpJob")
public class SendInfoForErpJob implements JobExecutor{
	private static Logger logger = LoggerFactory.getLogger(SendInfoForErpJob.class);

	@Autowired
	private ISgSendInfoToZhongtaiService sgSendInfoToZhongtaiService;

	@Override
	public JobParamDTO execute(JobParamDTO jobParamDTO) {
		logger.info("==============================执行任务开始================================");	
		try {
			sgSendInfoToZhongtaiService.sendInfoToZT();
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

