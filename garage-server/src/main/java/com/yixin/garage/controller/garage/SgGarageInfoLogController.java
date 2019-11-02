package com.yixin.garage.controller.garage;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.dto.garage.YxGarageInfoLogDTO;
import com.yixin.garage.service.garage.ISgGarageInfoLogService;
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
 * @since 2019-08-01
 */
@RestController
@RequestMapping("/sgGarageInfoLog")
public class SgGarageInfoLogController {

    private final static Logger logger=LoggerFactory.getLogger(SgGarageInfoLogController.class);

    @Autowired
    ISgGarageInfoLogService iSgGarageInfoLogService;

    /**
    * pageQueryLog(分页查询车库日志)
    * @param dto
    * @return
    * com.yixin.common.utils.Page<com.yixin.garage.dto.garage.YxGarageInfoLogDTO>
    * @author: YixinCapital -- libochen
    * 2019/8/7 17:37
    */
    @RequestMapping("/pageQueryLog")
    @ResponseBody
    public InvokeResult<Page<YxGarageInfoLogDTO>> pageQueryLog(@RequestBody YxGarageInfoLogDTO dto){

        logger.info("车库日志，查询参数为：[{}]",JacksonUtil.fromObjectToJson(dto));
        try {
            Page<YxGarageInfoLogDTO> page =  iSgGarageInfoLogService.pageQueryLog(dto);
            return ResultUtil.success(page);

        }catch (Exception e){
            logger.error("车库日志查询异常：[{}]", e.getMessage(), e);
            return ResultUtil.error(e.getMessage());
        }


    }



}
