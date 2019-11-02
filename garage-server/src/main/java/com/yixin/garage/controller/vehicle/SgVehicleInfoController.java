package com.yixin.garage.controller.vehicle;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yixin.common.exception.BzException;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.dto.order.SgGarageOrderDTO;
import com.yixin.garage.dto.vehicle.SgVehicleInfoDTO;
import com.yixin.garage.service.vehicle.ISgVehicleInfoService;
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
 * @since 2019-08-16
 */
@RestController
@RequestMapping("/sgVehicleInfo")
public class SgVehicleInfoController {

    private Logger logger = LoggerFactory.getLogger(SgVehicleInfoController.class);

    @Autowired
    private ISgVehicleInfoService sgVehicleInfoService;

    /**
     * @Title: create
     * @Description: 新增车辆
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/16 17:50
     */
    @RequestMapping("/create")
    @ResponseBody
    public InvokeResult<String> create(@RequestBody SgVehicleInfoDTO sgVehicleInfoDTO) throws BzException {
        logger.info("新增车辆-sgVehicleInfoDTO：{}", JSONObject.toJSONString(sgVehicleInfoDTO));
        try {
            sgVehicleInfoService.create(sgVehicleInfoDTO);
            return ResultUtil.success("新增成功！");

        } catch (Exception e) {
            logger.error("增加车辆失败--create", e);
            throw new BzException(e.getMessage());
        }
    }


    /**
     * @Title: update
     * @Description: 编辑车辆信息
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/19 13:58
     */
    @RequestMapping("/update")
    @ResponseBody
    public InvokeResult<String> update(@RequestBody SgVehicleInfoDTO sgVehicleInfoDTO) throws BzException {
        logger.info("编辑车辆信息-sgVehicleInfoDTO：{}", JSONObject.toJSONString(sgVehicleInfoDTO));
        try {
            sgVehicleInfoService.update(sgVehicleInfoDTO);
            return ResultUtil.success("修改成功！");

        } catch (Exception e) {
            logger.error("编辑车辆信息失败--update", e);
            throw new BzException(e.getMessage());
        }
    }

    /**
     * @Title: getBill
     * @Description: 查看详情
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/19 14:14
     */
    @ResponseBody
    @RequestMapping("/getBill")
    public InvokeResult<SgVehicleInfoDTO> getBill(@RequestParam("vehicleId") String vehicleId) {
        logger.info("查看车辆信息ID为：{}" + vehicleId);
        if(StringUtils.isBlank(vehicleId)){
            return ResultUtil.error("入参id不能为空！");
        }
        return sgVehicleInfoService.getBill(vehicleId.trim());
    }

    /**
     * @Title: pageQuery
     * @Description: 分页查询
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/19 15:02
     */
    @RequestMapping("/pageQuery")
    @ResponseBody
    public InvokeResult<IPage<SgVehicleInfoDTO>> pageQuery(@RequestBody SgVehicleInfoDTO sgVehicleInfoDTO) {
        logger.info("车辆列表查询，查询参数为：[{}]",JSONObject.toJSONString(sgVehicleInfoDTO));
        IPage<SgVehicleInfoDTO> page = null;
        try {
            page = sgVehicleInfoService.pageQuery(sgVehicleInfoDTO);
            return ResultUtil.success(page);
        }catch (Exception e){
            logger.error("车辆列表查询异常：[{}]", e.getMessage(), e);
            return ResultUtil.error(e.getMessage());
        }
    }

    /**
     * @Title: orderPageQuery
     * @Description: 订单列表查询
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/21 13:55
     */
    @RequestMapping("/orderPageQuery")
    @ResponseBody
    public InvokeResult<IPage<SgVehicleInfoDTO>> orderPageQuery(@RequestBody SgVehicleInfoDTO sgVehicleInfoDTO) {
        logger.info("订单列表查询查询参数为：[{}]",JSONObject.toJSONString(sgVehicleInfoDTO));
        IPage<SgVehicleInfoDTO> page = null;
        try {
            page = sgVehicleInfoService.orderPageQuery(sgVehicleInfoDTO);
            return ResultUtil.success(page);
        }catch (Exception e){
            logger.error("订单列表查询失败：[{}]", e.getMessage(), e);
            return ResultUtil.error(e.getMessage());
        }
    }


    /**
     * @Title: checkOrderBill
     * @Description: 车辆-订单综合查询
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/21 20:08
     */
    @ResponseBody
    @RequestMapping("/checkOrderBill")
    public InvokeResult<SgVehicleInfoDTO> checkOrderBill(@RequestParam("vehicleId") String vehicleId) {
        logger.info("车辆-订单综合查询：{}" + vehicleId);
        if(StringUtils.isBlank(vehicleId)){
            return ResultUtil.error("入参id不能为空！");
        }
        return sgVehicleInfoService.checkOrderBill(vehicleId.trim());
    }

}
