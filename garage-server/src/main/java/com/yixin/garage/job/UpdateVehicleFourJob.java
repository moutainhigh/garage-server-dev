package com.yixin.garage.job;

import com.yixin.common.utils.JobParamDTO;
import com.yixin.common.utils.JobStatus;
import com.yixin.garage.core.job.JobExecutor;
import com.yixin.garage.service.vehicle.ISgVehicleInfoService;
import com.yixin.garage.service.zhongtai.ISgSendInfoToZhongtaiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * @ClassName: DemoTask
 * @Description 定时任务Domo
 * @author  YixinCapital -- lbc
 *
 */
@Component("updateVehicleFourJob")
public class UpdateVehicleFourJob implements JobExecutor{
	private static Logger logger = LoggerFactory.getLogger(UpdateVehicleFourJob.class);

	@Autowired
	private ISgVehicleInfoService sgVehicleInfoService;

	@Override
	public JobParamDTO execute(JobParamDTO jobParamDTO) {
		logger.info("==============================执行任务开始================================");
		String result = null;
		try {
			result = sgVehicleInfoService.updateVehicleFour();
			if (result!=null && result.equals(true)) {
				jobParamDTO.setStatus(JobStatus.execute_success.getIndex());
				jobParamDTO.setResultContent("任务执行完毕！");
				jobParamDTO.setRetryFlag(false);
			}
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

