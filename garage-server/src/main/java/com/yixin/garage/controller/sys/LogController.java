/**
 * Copyright 2018-2020 stylefeng & fengshuonan (https://gitee.com/stylefeng)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yixin.garage.controller.sys;

import java.util.List;
import java.util.Map;

import com.yixin.garage.entity.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import com.yixin.garage.core.common.annotation.BussinessLog;
import com.yixin.garage.core.common.annotation.Permission;
import com.yixin.garage.core.common.constant.Const;
import com.yixin.garage.core.common.constant.state.BizLogType;
import com.yixin.garage.core.common.factory.PageFactory;
import com.yixin.garage.core.model.page.PageResult;
import com.yixin.garage.service.sys.IOperationLogService;
import com.yixin.garage.wrapper.LogWarpper;

import cn.hutool.core.bean.BeanUtil;

/**
 * 日志管理的控制器
 *
 * @author fengshuonan
 * @Date 2017年4月5日 19:45:36
 */
@Controller
@RequestMapping("/log")
public class LogController  {

    private static String PREFIX = "/system/log/";

    @Autowired
    private IOperationLogService operationLogService;

    /**
     * 跳转到日志管理的首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "log.html";
    }

    /**
     * 查询操作日志列表
     */
    @RequestMapping("/list")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public PageResult list(@RequestParam(required = false) String beginTime, @RequestParam(required = false) String endTime, @RequestParam(required = false) String logName, @RequestParam(required = false) Integer logType) {
    	Page<OperationLog> page = new PageFactory<OperationLog>().defaultPage();
        List<Map<String, Object>> result = operationLogService.getOperationLogs(page, beginTime, endTime, logName, BizLogType.valueOf(logType), "", false);
//        List<Map<String, Object>> result = operationLogService.getOperationLogs(page, beginTime, endTime, logName, BizLogType.valueOf(logType), page.getOrderByField(), page.isAsc());
    	
    	page.setRecords(new LogWarpper(result).wrap());
        return new PageResult<>(page);
    }

    /**
     * 查询操作日志详情
     */
    @RequestMapping("/detail/{id}")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Object detail(@PathVariable Integer id) {
//        OperationLog operationLog = operationLogService.selectById(id);
        OperationLog operationLog = operationLogService.getById(id);
//        operationLogService.getOne(new QueryWrapper().eq("", ""))
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(operationLog);
        return new LogWarpper(stringObjectMap).wrap();
    }

    /**
     * 清空日志
     */
    @BussinessLog(value = "清空业务日志")
    @RequestMapping("/delLog")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Object delLog() {
        SqlRunner.db().delete("delete from sys_operation_log");
        return "SUCCESS";
    }
}
