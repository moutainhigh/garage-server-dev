package com.yixin.garage.controller.inventory;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.controller.garage.SgGarageInfoController;
import com.yixin.garage.dto.garage.SgGarageInfoDTO;
import com.yixin.garage.dto.inventory.SgInventoryLogDTO;
import com.yixin.garage.entity.inventory.SgInventoryLog;
import com.yixin.garage.service.garage.ISgGarageInfoService;
import com.yixin.garage.service.inventory.ISgInventoryLogService;
import com.yixin.garage.util.JacksonUtil;
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
 * @author libochen
 * @since 2019-10-11
 */
@RestController
@RequestMapping("/sgInventoryLog")
public class SgInventoryLogController {
    private final static Logger logger=LoggerFactory.getLogger(SgInventoryLogController.class);

    @Autowired
    ISgInventoryLogService inventoryLogService;

    @RequestMapping("/pageQueryLog")
    @ResponseBody
    public InvokeResult<IPage<SgInventoryLog>> pageQueryLog(@RequestBody SgInventoryLogDTO dto) {
        logger.info("审批日志查询，查询参数为：[{}]",JacksonUtil.fromObjectToJson(dto));
        try {
            IPage<SgInventoryLog> page =  inventoryLogService.pageQueryLog(dto);
            return ResultUtil.success(page);
        }catch (Exception e){
            logger.error("车库列表查询异常：[{}]", e.getMessage(), e);
            return ResultUtil.error(e.getMessage());
        }
    }




}
