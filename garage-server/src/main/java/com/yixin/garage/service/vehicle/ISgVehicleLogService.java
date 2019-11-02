package com.yixin.garage.service.vehicle;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yixin.common.exception.BzException;
import com.yixin.garage.dto.vehicle.SgVehicleLogDTO;
import com.yixin.garage.entity.vehicle.SgVehicleLog;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liyaqing
 * @since 2019-08-05
 */
public interface ISgVehicleLogService extends IService<SgVehicleLog> {

    /**
     * @Title: pageQuery
     * @Description: 分页查询
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/6 19:31
     */
    IPage<SgVehicleLogDTO> pageQueryLog(SgVehicleLogDTO sgVehicleLogDTO) throws BzException;

}
