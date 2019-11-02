package com.yixin.garage.controller.inventory;


import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.dto.inventory.SgInventoryBillDTO;
import com.yixin.garage.dto.inventory.SgInventoryBillReturnDTO;
import com.yixin.garage.dto.inventory.SgInventoryDetailsDTO;
import com.yixin.garage.dto.inventory.SgInventoryResultStatisticsDTO;
import com.yixin.garage.service.inventory.ISgInventoryApprovalBillService;
import com.yixin.garage.service.inventory.ISgInventoryApprovalDetailService;
import com.yixin.garage.util.JacksonUtil;
import com.yixin.garage.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author libochen
 * @since 2019-10-11
 */
@RestController
@RequestMapping("/sgInventoryBill")
public class SgInventoryApprovalBillController {

    private final static Logger logger=LoggerFactory.getLogger(SgInventoryApprovalBillController.class);

    @Autowired
    private ISgInventoryApprovalBillService sgInventoryBillService;

    @Autowired
    private ISgInventoryApprovalDetailService sgInventoryDetailService;


    @RequestMapping("/pageQueryBill")
    @ResponseBody
    public InvokeResult<List<SgInventoryBillDTO>> pageQueryBill(@RequestBody SgInventoryBillDTO dto) {
        logger.info("盘点列表查询，查询参数为：{}",JacksonUtil.fromObjectToJson(dto));
        try {
            List<SgInventoryBillDTO> page =  sgInventoryBillService.pageQueryBill(dto);
            return ResultUtil.success(page);
        }catch (Exception e){
            logger.error("盘点列表查询异常：[{}]", e.getMessage(), e);
            return ResultUtil.error(e.getMessage());
        }
    }


    @RequestMapping("/getBillView")
    @ResponseBody
    public InvokeResult<SgInventoryBillReturnDTO> getBillView(@RequestBody SgInventoryDetailsDTO dto) {
        logger.info("盘点主单详情，查询参数为：{}",JacksonUtil.fromObjectToJson(dto));
        try {
            SgInventoryBillReturnDTO resultDto =  sgInventoryBillService.getBillView(dto);
            return ResultUtil.success(resultDto);
        }catch (Exception e){
            logger.error("盘点列表查询异常：[{}]", e.getMessage(), e);
            return ResultUtil.error(e.getMessage());
        }
    }

    @RequestMapping("/resultStatistics")
    @ResponseBody
    public InvokeResult<SgInventoryResultStatisticsDTO> resultStatistics(@RequestBody SgInventoryBillDTO dto) {
        logger.info("盘点结果统计，查询参数为：{}",JacksonUtil.fromObjectToJson(dto));
        try {
            InvokeResult<SgInventoryResultStatisticsDTO> resultDto =  sgInventoryDetailService.resultStatistics(dto);
            return resultDto;
        }catch (Exception e){
            logger.error("盘点结果统计异常：[{}]", e.getMessage(), e);
            return ResultUtil.error(e.getMessage());
        }
    }


    @RequestMapping("/submitBill")
    @ResponseBody
    public InvokeResult<String> submitBill(@RequestBody SgInventoryBillDTO dto) {
        logger.info("submitBill()，参数为：{}",JacksonUtil.fromObjectToJson(dto));
        InvokeResult<String> result = new InvokeResult<String>();
        try {
            result =  sgInventoryBillService.submitBill(dto);
            if(result.isSuccess()){
                result.setData("提交成功！");
            }
            return result;
        }catch (Exception e){
            logger.error("盘点结果统计异常：[{}]", e.getMessage(), e);
            return ResultUtil.error(result.getErrorMessage());
        }
    }


    @RequestMapping("/submitApproveBill")
    @ResponseBody
    public InvokeResult<String> submitApproveBill(@RequestBody SgInventoryBillDTO dto) {
        logger.info("submitApproveBill，参数为：{}",JacksonUtil.fromObjectToJson(dto));
        InvokeResult<String> result = new InvokeResult<String>();
        try {
            result =  sgInventoryBillService.submitApproveBill(dto);
            if(result.isSuccess()){
                result.setData("提交成功！");
            }
            return result;
        }catch (Exception e){
            logger.error("盘点审批异常：[{}]", e.getMessage(), e);
            return ResultUtil.error(result.getErrorMessage());
        }

    }


    @RequestMapping("/rejectBill")
    @ResponseBody
    public InvokeResult<String> rejectBill(@RequestBody SgInventoryBillDTO dto) {
        logger.info("rejectBill，参数为：{}",JacksonUtil.fromObjectToJson(dto));
        InvokeResult<String> result = new InvokeResult<String>();
        try {
            result =  sgInventoryBillService.rejectBill(dto);
            if(result.isSuccess()){
                result.setData("驳回成功！");
            }
            return result;
        }catch (Exception e){
            logger.error("盘点驳回异常：[{}]", e.getMessage(), e);
            return ResultUtil.error(result.getErrorMessage());
        }

    }

    @RequestMapping("/submitSendMail")
    @ResponseBody
    public InvokeResult<String> submitSendMail(@RequestBody SgInventoryBillDTO dto) {
        logger.info("submitSendMail，参数为：{}",JacksonUtil.fromObjectToJson(dto));
        InvokeResult<String> result = new InvokeResult<String>();
        try {
            result =  sgInventoryBillService.submitSendMail(dto);
            if(result.isSuccess()){
                result.setData("提交成功！");
            }
            return result;
        }catch (Exception e){
            logger.error("submitSendMail()异常：[{}]", e.getMessage(), e);
            return ResultUtil.error(result.getErrorMessage());
        }

    }




}
