
package com.yixin.garage.service.impl.sys;

import java.util.List;
import java.util.Map;

import com.yixin.garage.dao.OperationLogMapper;
import com.yixin.garage.entity.OperationLog;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yixin.garage.service.sys.IOperationLogService;


/**
 * <p>
 * 操作日志 服务实现类
 * </p>
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements IOperationLogService {

    @Override
    public List<Map<String, Object>> getOperationLogs(Page<OperationLog> page, String beginTime, String endTime, String logName, String s, String orderByField, boolean asc) {
        return this.baseMapper.getOperationLogs(page, beginTime, endTime, logName, s, orderByField, asc);
    }
}
