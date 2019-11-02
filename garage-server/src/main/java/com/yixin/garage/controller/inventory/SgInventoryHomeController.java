package com.yixin.garage.controller.inventory;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.common.utils.InvokeResult;

import com.yixin.garage.dto.garage.SgGarageInfoDTO;
import com.yixin.garage.dto.inventory.SgInventoryGrageIdsDTO;
import com.yixin.garage.dto.inventory.SgInventoryHomeDTO;
import com.yixin.garage.dto.vehicle.SgVehicleInfoDTO;
import com.yixin.garage.service.garage.ISgGarageInfoService;
import com.yixin.garage.service.inventory.ISgInventoryHomeService;
import com.yixin.garage.util.JacksonUtil;
import com.yixin.garage.util.ResultUtil;
import org.apache.commons.lang.StringUtils;
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
@RequestMapping("/sgInventoryHome")
public class SgInventoryHomeController {

    private final static Logger logger=LoggerFactory.getLogger(SgInventoryHomeController.class);

    @Autowired
    private ISgInventoryHomeService sgInventoryHomeService;

    @Autowired
    ISgGarageInfoService iSgGarageInfoService;


    /**
    * createInventory(创建盘点任务)
    * @param homeDTO
    * @return
    * @author: YixinCapital -- libochen
    * 2019/10/11 10:55
    */
    @RequestMapping("/createInventory")
    @ResponseBody
    public InvokeResult<String> createInventory(@RequestBody SgInventoryHomeDTO homeDTO) {
        InvokeResult<String> result = new InvokeResult<String>();
        try {
            String errMsg = sgInventoryHomeService.createInventory(homeDTO);
            if (StringUtils.isNotEmpty(errMsg)) {
                result.failure(errMsg);
                return result;
            }
        } catch (Exception e) {
            logger.error("创建盘库任务createInventory() failed：", e);
            result.failure("创建盘库任务失败："+e.getMessage());
        }
        return result;
    }

    /**
    * pageQueryHome(分页查询盘点任务列表)
    * @param dto
    * @return
    * @author: YixinCapital -- libochen
    * 2019/10/11 16:26
    */
    @RequestMapping("/pageQuery")
    @ResponseBody
    public InvokeResult<Page<SgInventoryHomeDTO>> pageQuery(@RequestBody SgInventoryHomeDTO dto) {
        logger.info("盘点主页列表查询，查询参数为：{}",JacksonUtil.fromObjectToJson(dto));
        try {
            Page<SgInventoryHomeDTO> page =  sgInventoryHomeService.pageQuery(dto);
            return ResultUtil.success(page);
        }catch (Exception e){
            logger.error("盘点主页列表查询异常：[{}]", e.getMessage(), e);
            return ResultUtil.error(e.getMessage());
        }
    }

    /**
    * getHomeView(查看盘点任务详情)
    * @param dto
    * @return
    * @author: YixinCapital -- libochen
    * 2019/10/15 14:56
    */
    @RequestMapping("/getHomeView")
    @ResponseBody
    public InvokeResult<SgInventoryHomeDTO> getHomeView(@RequestBody SgInventoryHomeDTO dto) {
        logger.info("盘点任务查看页面，查询参数为：{}",JacksonUtil.fromObjectToJson(dto));
        try {
            SgInventoryHomeDTO resultDto =  sgInventoryHomeService.getHomeView(dto);
            return ResultUtil.success(resultDto);
        }catch (Exception e){
            logger.error("盘点任务查看页面异常：[{}]", e.getMessage(), e);
            return ResultUtil.error(e.getMessage());
        }
    }


    /**
    * checkData(发布前检查是否存在盘点未完成的任务)
    * @param dto
    * @return
    * com.yixin.common.utils.InvokeResult<java.lang.String>
    * @author: YixinCapital -- libochen
    * 2019/10/15 14:57
    */
    @RequestMapping("/checkData")
    @ResponseBody
    public InvokeResult<String> checkData(@RequestBody SgInventoryHomeDTO dto) {
        InvokeResult<String> result = new InvokeResult<String>();
        try {
            String errMsg = sgInventoryHomeService.checkData(dto);
            if (StringUtils.isNotEmpty(errMsg)) {
                result.failure(errMsg);
            }else{
                result.setData("可以发布！");
            }
        } catch (Exception e) {
            result.failure(e.getMessage());
        }
        return result;
    }


    /**
    * issue(盘点任务发布)
    * @param dto
    * @return
    * com.yixin.common.utils.InvokeResult<java.lang.String>
    * @author: YixinCapital -- libochen
    * 2019/10/15 14:58
    */
    @RequestMapping("/issue")
    @ResponseBody
    public InvokeResult<String> issue(@RequestBody SgInventoryHomeDTO dto) {
        InvokeResult<String> result = new InvokeResult<String>();
        try {
            String errMsg = sgInventoryHomeService.issue(dto);
            if (StringUtils.isNotEmpty(errMsg)) {
                result.failure(errMsg);
            }else{
                result.setData("发布成功！");
            }
        } catch (Exception e) {
            result.failure(e.getMessage());
        }
        return result;
    }


    /**
    * updateInventory(编辑盘点任务)
    * @param homeDTO
    * @return
    * com.yixin.common.utils.InvokeResult<java.lang.String>
    * @author: YixinCapital -- libochen
    * 2019/10/15 14:58
    */
    @RequestMapping("/updateInventory")
    @ResponseBody
    public InvokeResult<String> updateInventory(@RequestBody SgInventoryHomeDTO homeDTO) {
        InvokeResult<String> result = new InvokeResult<String>();
        try {
            String errMsg = sgInventoryHomeService.updateInventory(homeDTO);
            if (StringUtils.isNotEmpty(errMsg)) {
                result.failure(errMsg);
                return result;
            }
        } catch (Exception e) {
            logger.error("编辑盘库任务updateInventory() failed：", e);
            result.failure("编辑盘库任务失败："+e.getMessage());
        }
        return result;
    }



    /**
    * getInventoryList(获取盘点期数列表)
    * @param
    * @return
    * com.yixin.common.utils.InvokeResult<java.util.List<java.lang.String>>
    * @author: YixinCapital -- libochen
    * 2019/10/15 16:37
    */
    @RequestMapping("/getInventoryList")
    @ResponseBody
    public InvokeResult<List<String>> getInventoryList() {
        InvokeResult<List<String>> result = new InvokeResult<List<String>>();
        try {
            result = sgInventoryHomeService.getInventoryList();
        } catch (Exception e) {
            logger.error("getInventoryList() failed：", e);
            result.failure("error");
        }
        return result;
    }

    /**
    * getGarageInfoList(获取车库列表)
    * @param
    * @return
    * @author: YixinCapital -- libochen
    * 2019/10/15 15:24
    */
    @RequestMapping("/getGarageInfoList")
    @ResponseBody
    public InvokeResult<List<SgGarageInfoDTO>> getGarageInfoList(@RequestBody SgGarageInfoDTO dto) {
        InvokeResult<List<SgGarageInfoDTO>> result = new InvokeResult<List<SgGarageInfoDTO>>();
        logger.info("garageDetail()入参为:[{}]",JSONObject.toJSON(dto));
        try {
            result = iSgGarageInfoService.getGarageInfoAllList(dto);
        } catch (Exception e) {
            logger.error("getGarageInfoList() failed：", e);
            result.failure("error");
        }
        return result;
    }



    @RequestMapping("/getGarageInfoListForApp")
    @ResponseBody
    public InvokeResult<List<SgInventoryGrageIdsDTO>> getGarageInfoListForApp(@RequestBody SgInventoryHomeDTO dto) {
        InvokeResult<List<SgInventoryGrageIdsDTO>> result = new InvokeResult<List<SgInventoryGrageIdsDTO>>();
        logger.info("garageDetail()入参为:[{}]",JSONObject.toJSON(dto));
        try {
            result = sgInventoryHomeService.getGarageInfoListForApp(dto);
        } catch (Exception e) {
            logger.error("getGarageInfoList() failed：", e);
            result.failure("error");
        }
        return result;
    }


}
