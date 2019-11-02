package com.yixin.garage.controller.order;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.dto.order.SgGarageDetailDTO;
import com.yixin.garage.dto.order.SgGarageOrderDTO;
import com.yixin.garage.dto.order.SgGarageOrderLogDTO;
import com.yixin.garage.service.order.ISgGarageDetailService;
import com.yixin.garage.util.JacksonUtil;
import com.yixin.garage.util.ResultUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author libochen
 * @since 2019-08-01
 */
@RestController
@RequestMapping("/sgGarageDetail")
public class SgGarageDetailController {

    private final static Logger logger=LoggerFactory.getLogger(SgGarageDetailController.class);


    @Autowired
    ISgGarageDetailService iSgGarageDetailService;

    @RequestMapping("/pageQueryRKOrder")
    @ResponseBody
    public InvokeResult<Page<SgGarageDetailDTO>> pageQueryRKOrder(@RequestBody SgGarageDetailDTO dto){
        logger.info("入库单查询，查询参数为：[{}]",JacksonUtil.fromObjectToJson(dto));
        try {
            Page<SgGarageDetailDTO> page =  iSgGarageDetailService.pageQueryRKOrder(dto);
            return ResultUtil.success(page);
        }catch (Exception e){
            logger.error("入库单查询异常：[{}]", e.getMessage(), e);
            return ResultUtil.error(e.getMessage());
        }
    }

    /**
     * garageInBranch(入库审批)sgGarageDetailDTO
     * @param
     * @return
     * com.yixin.common.utils.InvokeResult<java.lang.String>
     * @author: YixinCapital -- libochen
     * 2019/8/13 16:26
     */
    @RequestMapping("/approveIn")
    @ResponseBody
    public InvokeResult<String> approveIn(@RequestBody SgGarageOrderDTO dto){
        logger.info("入库审批-approveIn：{}", JSONObject.toJSONString(dto));
        InvokeResult<String> result = new InvokeResult<>();
        try {
            result = iSgGarageDetailService.approveIn(dto);
        } catch (Exception e) {
            logger.error("garageInBranch() failed：", e.getMessage());
            result.failure(e.getMessage());
        }
        return result;
    }


    /**
     * inReject(审批驳回)
     * @param sgGarageOrderDTO
     * @return
     * com.yixin.common.utils.InvokeResult<java.lang.String>
     * @author: YixinCapital -- libochen
     * 2019/8/13 16:28
     */
    @RequestMapping("/inReject")
    @ResponseBody
    public InvokeResult<String> inReject(@RequestBody SgGarageOrderDTO sgGarageOrderDTO) {
        InvokeResult<String> result = new InvokeResult<String>();
        try {
            result = iSgGarageDetailService.inReject(sgGarageOrderDTO);
        } catch (Exception e) {
            logger.error("InReject() failed：", e);
            result.failure("error");
        }
        return result;
    }


    /**
    * getBill(查看入库详情)
    * @param dto
    * @return
    * com.yixin.common.utils.InvokeResult<com.yixin.garage.dto.order.SgGarageOrderDTO>
    * @author: YixinCapital -- libochen
    * 2019/8/21 11:12
    */
    @ResponseBody
    @RequestMapping("/getBill")
    public InvokeResult<SgGarageOrderDTO> getBill(@RequestBody SgGarageDetailDTO dto) {
        logger.info("查询入库详情，查询参数为：[{}]",JSONObject.toJSONString(dto));
        String sgGarageOrderId = dto.getSgGaragOrderId();
        try {
            if(StringUtils.isBlank(sgGarageOrderId)){
                return ResultUtil.error("入参id不能为空！");
            }
            SgGarageOrderDTO garageOrderDTO = iSgGarageDetailService.getBill(sgGarageOrderId);
            return ResultUtil.success(garageOrderDTO);
        }catch (Exception e){
            logger.error("查询入库详情异常：[{}]", e.getMessage(), e);
            return ResultUtil.error(e.getMessage());
        }
    }


    /**
    * pageQueryLog(分页查询审批日志)
    * @param dto
    * @return
    * com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.yixin.garage.dto.order.SgGarageOrderLogDTO>
    * @author: YixinCapital -- libochen
    * 2019/8/21 11:38
    */
    @ResponseBody
    @RequestMapping("/pageQueryLog")
    public InvokeResult<Page<SgGarageOrderLogDTO>> pageQueryLog(@RequestBody SgGarageOrderLogDTO dto){
        logger.info("查询入库日志详情，查询参数为：[{}]",JSONObject.toJSONString(dto));
        try {
            Page<SgGarageOrderLogDTO> page = iSgGarageDetailService.pageQueryLog(dto);
            return ResultUtil.success(page);
        } catch (Exception e) {
            logger.error("入库单查询异常：[{}]", e.getMessage(), e);
            return ResultUtil.error(e.getMessage());
        }
    }




}
