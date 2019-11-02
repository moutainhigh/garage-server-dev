package com.yixin.garage.dao.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.garage.dto.order.SgGarageDetailDTO;
import com.yixin.garage.dto.order.SgGarageOrderDTO;
import com.yixin.garage.dto.vehicle.SgVehicleInfoDTO;
import com.yixin.garage.entity.order.SgGarageDetail;
import com.yixin.garage.entity.order.SgGarageOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liyaqing
 * @since 2019-08-19
 */
public interface SgGarageOrderMapper extends BaseMapper<SgGarageOrder> {

    String createGarageOrderTaskNum(@Param("dateString") String dateString);

}
