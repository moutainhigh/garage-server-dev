package com.yixin.garage.controller.vehicle;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.dto.vehicle.SgVehicleLogDTO;
import com.yixin.garage.service.vehicle.ISgVehicleLogService;
import com.yixin.garage.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liyaqing
 * @since 2019-08-05
 */
@RestController
@RequestMapping("/sgVehicleLog")
public class SgVehicleLogController {

    private Logger logger = LoggerFactory.getLogger(SgVehicleLogController.class);

    @Autowired
    private ISgVehicleLogService sgVehicleLogService;

    /**
     * @Title: pageQueryLog
     * @Description: 分页查询日志接口
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/22 15:19
     */
    @RequestMapping("/pageQueryLog")
    @ResponseBody
    public InvokeResult<IPage<SgVehicleLogDTO>> pageQueryLog(@RequestBody SgVehicleLogDTO sgVehicleLogDTO) {
        logger.info("车辆日志列表查询，查询参数为：[{}]",JSONObject.toJSONString(sgVehicleLogDTO));
        IPage<SgVehicleLogDTO> page = null;
        try {
            page = sgVehicleLogService.pageQueryLog(sgVehicleLogDTO);
            return ResultUtil.success(page);
        }catch (Exception e){
            logger.error("车辆日志列表查询：[{}]", e.getMessage(), e);
            return ResultUtil.error(e.getMessage());
        }
    }

}
