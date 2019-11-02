package com.yixin.garage.controller.order;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yixin.common.exception.BzException;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.controller.vehicle.SgVehicleInfoController;
import com.yixin.garage.dto.order.SgGarageDetailDTO;
import com.yixin.garage.dto.order.SgGarageOrderDTO;
import com.yixin.garage.dto.vehicle.SgVehicleInfoDTO;
import com.yixin.garage.entity.order.SgGarageDetail;
import com.yixin.garage.service.order.ISgGarageOrderService;
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
 * @author liyaqing
 * @since 2019-08-19
 */
@RestController
@RequestMapping("/sgGarageOrder")
public class SgGarageOrderController {

    private Logger logger = LoggerFactory.getLogger(SgVehicleInfoController.class);

    @Autowired
    private ISgGarageOrderService sgGarageOrderService;

    /**
     * @Title: garageOutBranch
     * @Description: 处置专员-处理
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/19 19:47
     */
    @RequestMapping("/garageOutBranch")
    @ResponseBody
    public InvokeResult<String> garageOutBranch(@RequestBody SgGarageOrderDTO sgGarageOrderDTO) throws BzException {
        logger.info("出库代办-处理-sgGarageOrderDTO：{}", JSONObject.toJSONString(sgGarageOrderDTO));
        try {
            sgGarageOrderService.garageOutBranch(sgGarageOrderDTO);
            return ResultUtil.success("处理成功！");
        } catch (Exception e) {
            logger.error("处理失败", e);
            throw new BzException(e.getMessage());
        }
    }


    /**
     * @Title: approveGarageOut
     * @Description: 资产专员-审批
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/20 10:38
     */
    @RequestMapping("/approveGarageOut")
    @ResponseBody
    public InvokeResult<String> approveGarageOut(@RequestBody SgGarageOrderDTO sgGarageOrderDTO) throws BzException {
        logger.info("出库审批-sgGarageOrderDTO：{}", JSONObject.toJSONString(sgGarageOrderDTO));
        try {
            sgGarageOrderService.approveGarageOut(sgGarageOrderDTO);
            return ResultUtil.success("审批成功！");
        } catch (Exception e) {
            logger.error("审批失败", e);
            throw new BzException(e.getMessage());
        }
    }

    /**
     * @Title: reject
     * @Description: 审批拒绝
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/20 14:05
     */
    @RequestMapping("/reject")
    @ResponseBody
    public InvokeResult<String> reject(@RequestBody SgGarageOrderDTO sgGarageOrderDTO) throws BzException {
        logger.info("拒绝-sgGarageOrderDTO：{}", JSONObject.toJSONString(sgGarageOrderDTO));
        try {
            sgGarageOrderService.reject(sgGarageOrderDTO);
            return ResultUtil.success("审批拒绝成功！");
        } catch (Exception e) {
            logger.error("审批拒绝失败", e);
            throw new BzException(e.getMessage());
        }
    }


    /**
     * @Title: pageQuery
     * @Description: 出入库列表查询
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/20 14:34
     */
    @RequestMapping("/pageQuery")
    @ResponseBody
    public InvokeResult<IPage<SgGarageDetailDTO>> pageQuery(@RequestBody SgGarageDetailDTO sgGarageDetailDTO) {
        logger.info("出库列表查询，查询参数为：[{}]",JSONObject.toJSONString(sgGarageDetailDTO));
        IPage<SgGarageDetailDTO> page = null;
        try {
            page = sgGarageOrderService.pageQuery(sgGarageDetailDTO);
            return ResultUtil.success(page);
        }catch (Exception e){
            logger.error("出库列表查询异常：[{}]", e.getMessage(), e);
            return ResultUtil.error(e.getMessage());
        }
    }


    /**
     * @Title: getBill
     * @Description: 查询出库详情
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/20 19:22
     */
    @ResponseBody
    @RequestMapping("/getBill")
    public InvokeResult<SgGarageOrderDTO> getBill(@RequestParam("sgGarageDetailId") String sgGarageDetailId) {
        logger.info("查询出库详情，查询参数为：[{}]",JSONObject.toJSONString(sgGarageDetailId));
        try {
            if(StringUtils.isBlank(sgGarageDetailId)){
                return ResultUtil.error("入参id不能为空！");
            }
            SgGarageOrderDTO garageOrderDTO = sgGarageOrderService.getBill(sgGarageDetailId);
            return ResultUtil.success(garageOrderDTO);
        }catch (Exception e){
            logger.error("查询出库详情异常：[{}]", e.getMessage(), e);
            return ResultUtil.error(e.getMessage());
        }
    }


    /**
     * @Title: createTempOut
     * @Description: 新增临时出库
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/21 10:45
     */
    @RequestMapping("/createTempOut")
    @ResponseBody
    public InvokeResult<String> createTempOut(@RequestBody SgGarageDetailDTO sgGarageDetailDTO) throws BzException {
        logger.info("新增临时出库-sgGarageDetailDTO：{}", JSONObject.toJSONString(sgGarageDetailDTO));
        try {
            sgGarageOrderService.createTempOut(sgGarageDetailDTO);
            return ResultUtil.success("临时出库成功！");

        } catch (Exception e) {
            logger.error("新增临时出库失败--create", e);
            throw new BzException(e.getMessage());
        }
    }




}
