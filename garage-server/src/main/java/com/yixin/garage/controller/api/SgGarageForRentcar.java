package com.yixin.garage.controller.api;

import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.dto.api.rentcar.SgVehicleByFreeDTO;
import com.yixin.garage.service.vehicle.ISgVehicleInfoService;
import com.yixin.garage.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YixinCapital -- liyaqing
 * @description: TODO
 * @date 2019/9/1615:41
 */
@RestController
@RequestMapping("/api/sgGarageForRentcar")
public class SgGarageForRentcar {

    private final static Logger logger=LoggerFactory.getLogger(SgGarageForRentcar.class);

    @Autowired
    private ISgVehicleInfoService sgVehicleInfoService;


    @ResponseBody
    @RequestMapping("/queryFreeVeh")
    public InvokeResult<List<SgVehicleByFreeDTO>> queryFreeVeh(String vehicleId) {
        InvokeResult<List<SgVehicleByFreeDTO>> result = new InvokeResult<List<SgVehicleByFreeDTO>>();
        List<SgVehicleByFreeDTO> list = new ArrayList<SgVehicleByFreeDTO>();
        logger.info("查询在库车辆开始");
        try {
            list = sgVehicleInfoService.queryFreeVeh();
            result.setData(list);
            result.success();
        }catch (Exception e){
            logger.error("查询在库车辆失败：[{}]", e.getMessage(), e);
            return ResultUtil.error(e.getMessage());
        }
        return result;
    }
}
