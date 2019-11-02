package com.yixin.garage.util;

import com.yixin.common.utils.JobParamDTO;
import com.yixin.common.utils.JobStatus;

public class YxJobResultUtil {
	
	public static JobParamDTO success(String taskMsg) {
		JobParamDTO jobParamDTO = new  JobParamDTO();
		jobParamDTO.setStatus(JobStatus.execute_success.getIndex());
		jobParamDTO.setResultContent(taskMsg);
		jobParamDTO.setRetryFlag(false);
        return jobParamDTO;
    }
	
	public static JobParamDTO error(String errorMessage) {
		JobParamDTO jobParamDTO = new  JobParamDTO();
		jobParamDTO.setStatus(JobStatus.execute_falied.getIndex());
		jobParamDTO.setResultContent(errorMessage);
		jobParamDTO.setRetryFlag(true);
		return jobParamDTO;
    }
}

