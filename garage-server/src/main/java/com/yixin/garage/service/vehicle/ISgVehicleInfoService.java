package com.yixin.garage.service.vehicle;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yixin.common.exception.BzException;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.dto.api.rentcar.SgVehicleByFreeDTO;
import com.yixin.garage.dto.order.SgGarageOrderDTO;
import com.yixin.garage.dto.vehicle.SgVehicleInfoDTO;
import com.yixin.garage.entity.vehicle.SgVehicleInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liyaqing
 * @since 2019-08-16
 */
public interface ISgVehicleInfoService extends IService<SgVehicleInfo> {

    /**
     * @Title: create
     * @Description: 新增车辆
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/16 17:50
     */
    InvokeResult<String> create(SgVehicleInfoDTO sgVehicleInfoDTO) throws BzException;


    /**
     * @Title: update
     * @Description: 编辑车辆信息
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/19 13:58
     */
    InvokeResult<String> update(SgVehicleInfoDTO sgVehicleInfoDTO) throws BzException;

    /**
     * @Title: getBill
     * @Description: 根据ID查看车辆详情
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/19 14:13
     */
    InvokeResult<SgVehicleInfoDTO> getBill(String vehicleId) throws BzException;

    /**
     * @Title: pageQuery
     * @Description: 分页查询
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/19 15:02
     */
    IPage<SgVehicleInfoDTO> pageQuery(SgVehicleInfoDTO sgVehicleInfoDTO) throws BzException;

    /**
     * @Title: orderPageQuery
     * @Description: 订单列表分页查询
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/21 14:00
     */
    IPage<SgVehicleInfoDTO> orderPageQuery(SgVehicleInfoDTO sgVehicleInfoDTO) throws BzException;

    /**
     * @Title: 车辆-订单综合查新
     * @Description: TODO
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/21 20:08
     */
    InvokeResult<SgVehicleInfoDTO> checkOrderBill(String vehicleId) throws BzException;

    /**
     * @Title: queryFreeVeh
     * @Description: 查询在库车辆
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/9/16 15:50
     */
    List<SgVehicleByFreeDTO> queryFreeVeh() throws BzException;


    /**
     * 定时刷新车辆四级码值
     */
    String updateVehicleFour() throws BzException;

}
