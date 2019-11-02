package com.yixin.garage.controller.zhongtai;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.dto.zhongtai.SgGarageSendInfoDTO;
import com.yixin.garage.service.zhongtai.ISgSendInfoToZhongtaiService;
import com.yixin.garage.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhouhang
 * @since 2019-08-29
 */
@RestController
@RequestMapping("/sgSendInfoToZhongtai")
public class SgSendInfoToZhongtaiController {

    private Logger logger = LoggerFactory.getLogger(SgSendInfoToZhongtaiController.class);

    @Autowired
    private ISgSendInfoToZhongtaiService sgSendInfoToZhongtaiService;

    /**
     * @Title: pageQuery
     * @Description: 分页查询
     * @param:
     * @author YixinCapital -- zhouhang
     * @date 2019/8/29 15:02
     */
    @RequestMapping("/pageQuery")
    @ResponseBody
    public InvokeResult<IPage<SgGarageSendInfoDTO>> pageQuery(@RequestBody SgGarageSendInfoDTO sgGarageSendInfoDTO) {
        logger.info("推送中台信息列表查询，查询参数为：[{}]",JSONObject.toJSONString(sgGarageSendInfoDTO));
        IPage<SgGarageSendInfoDTO> page = null;
        try {
            page = sgSendInfoToZhongtaiService.pageQuery(sgGarageSendInfoDTO);
            return ResultUtil.success(page);
        }catch (Exception e){
            logger.error("推送中台信息列表查询：[{}]", e.getMessage(), e);
            return ResultUtil.error(e.getMessage());
        }
    }

    @RequestMapping("/pageQueryLog")
    @ResponseBody
    public InvokeResult<IPage<SgGarageSendInfoDTO>> pageQueryLog(@RequestBody SgGarageSendInfoDTO sgGarageSendInfoDTO) {
        logger.info("推送中台信息列表查询，查询参数为：[{}]",JSONObject.toJSONString(sgGarageSendInfoDTO));
        IPage<SgGarageSendInfoDTO> page = null;
        try {
            page = sgSendInfoToZhongtaiService.pageQueryLog(sgGarageSendInfoDTO);
            return ResultUtil.success(page);
        }catch (Exception e){
            logger.error("推送中台信息列表查询：[{}]", e.getMessage(), e);
            return ResultUtil.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/sendInfoToZT")
    @ResponseBody
    public InvokeResult<String> sendInfoToZT(@RequestBody SgGarageSendInfoDTO sgGarageSendInfoDTO) {
        logger.info("推送中台信息列表查询，查询参数为：",JSONObject.toJSONString(sgGarageSendInfoDTO));
        InvokeResult<String> result = new InvokeResult<>();
        try {
            result = sgSendInfoToZhongtaiService.sendInfoToZT(sgGarageSendInfoDTO);
            result.setData("发送成功");
            return result.success();
        }catch (Exception e){
            logger.error("推送中台信息失败：[{}]", e.getMessage(), e);
            result.setData("发送失败");
            return result;
        }
    }
}
