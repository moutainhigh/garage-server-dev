package com.yixin.garage.dao.vehicle;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.garage.dto.order.SgGarageOrderLogDTO;
import com.yixin.garage.dto.vehicle.SgVehicleLogDTO;
import com.yixin.garage.entity.order.SgGarageOrderLog;
import com.yixin.garage.entity.vehicle.SgVehicleLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liyaqing
 * @since 2019-08-05
 */
public interface SgVehicleLogMapper extends BaseMapper<SgVehicleLog> {

    IPage<SgVehicleLogDTO> pageQueryLog(@Param("page") Page<SgVehicleLog> page, @Param("dto") SgVehicleLogDTO dto);

}
