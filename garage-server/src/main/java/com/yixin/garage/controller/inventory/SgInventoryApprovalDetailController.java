package com.yixin.garage.controller.inventory;


import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.dto.inventory.SgInventoryBillReturnDTO;
import com.yixin.garage.dto.inventory.SgInventoryDetailsDTO;
import com.yixin.garage.entity.inventory.SgInventoryApprovalDetail;
import com.yixin.garage.service.inventory.ISgInventoryApprovalDetailService;
import com.yixin.garage.service.inventory.impl.SgInventoryApprovalDetailServiceImpl;
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
@RequestMapping("/sgInventoryDetail")
public class SgInventoryApprovalDetailController {

    private final static Logger logger=LoggerFactory.getLogger(SgInventoryApprovalDetailController.class);


    @Autowired
    ISgInventoryApprovalDetailService sgInventoryDetailService;



    /**
    * getDetailView(获取详单详情页面)
    * @param dto
    * @return
    * com.yixin.common.utils.InvokeResult<com.yixin.garage.dto.inventory.SgInventoryDetailsDTO>
    * @author: YixinCapital -- libochen
    * 2019/10/12 17:15
    */
    @RequestMapping("/getDetailView")
    @ResponseBody
    public InvokeResult<SgInventoryDetailsDTO> getDetailView(@RequestBody SgInventoryDetailsDTO dto) {
        logger.info("盘点详单详情，查询参数为：{}",JacksonUtil.fromObjectToJson(dto));
        try {
            SgInventoryDetailsDTO resultDto =  sgInventoryDetailService.getDetailView(dto);
            return ResultUtil.success(resultDto);
        }catch (Exception e){
            logger.error("盘点详单详情异常：[{}]", e.getMessage(), e);
            return ResultUtil.error(e.getMessage());
        }
    }

    /**
    * saveDetail(保存盘点数据)
    * @param dto
    * @return
    * com.yixin.common.utils.InvokeResult<java.lang.String>
    * @author: YixinCapital -- libochen
    * 2019/10/16 10:48
    */
    @RequestMapping("/saveDetail")
    @ResponseBody
    public InvokeResult<String> saveDetail(@RequestBody SgInventoryDetailsDTO dto) {
        logger.info("保存盘点结果，参数为：{}",JacksonUtil.fromObjectToJson(dto));
        try {
            String msg =  sgInventoryDetailService.saveDetail(dto);
            return ResultUtil.success("保存成功");
        }catch (Exception e){
            logger.error("保存盘点结果异常：[{}]", e.getMessage(), e);
            return ResultUtil.error(e.getMessage());
        }
    }


    @RequestMapping("/otherAdd")
    @ResponseBody
    public InvokeResult<String> otherAdd(@RequestBody SgInventoryDetailsDTO dto) {
        logger.info("其他在库车辆新增，参数为：{}",JacksonUtil.fromObjectToJson(dto));
        try {
            String msg =  sgInventoryDetailService.otherAdd(dto);
            return ResultUtil.success("新增成功");
        }catch (Exception e){
            logger.error("其他在库车辆新增：[{}]", e.getMessage(), e);
            return ResultUtil.error(e.getMessage());
        }
    }

    @RequestMapping("/otherDelete")
    @ResponseBody
    public InvokeResult<String> otherDelete(@RequestBody SgInventoryDetailsDTO dto) {
        logger.info("其他在库车辆删除，参数为：{}",JacksonUtil.fromObjectToJson(dto));
        try {
            String msg =  sgInventoryDetailService.otherDelete(dto);
            return ResultUtil.success("删除成功");
        }catch (Exception e){
            logger.error("其他在库车辆删除异常：[{}]", e.getMessage(), e);
            return ResultUtil.error(e.getMessage());
        }
    }


    @RequestMapping("/saveApproveDetail")
    @ResponseBody
    public InvokeResult<String> saveApproveDetail(@RequestBody SgInventoryDetailsDTO dto) {
        logger.info("保存审批结果，参数为：{}",JacksonUtil.fromObjectToJson(dto));
        try {
            String msg =  sgInventoryDetailService.saveApproveDetail(dto);
            return ResultUtil.success("审核成功!");
        }catch (Exception e){
            logger.error("审核成功异常：[{}]", e.getMessage(), e);
            return ResultUtil.error(e.getMessage());
        }
    }












}
