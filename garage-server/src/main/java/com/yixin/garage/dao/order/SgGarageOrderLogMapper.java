package com.yixin.garage.dao.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.garage.dto.order.SgGarageOrderLogDTO;
import com.yixin.garage.entity.order.SgGarageOrderLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liyaqing
 * @since 2019-08-07
 */
public interface SgGarageOrderLogMapper extends BaseMapper<SgGarageOrderLog> {

    IPage<SgGarageOrderLogDTO> pageQueryLog(@Param("page") Page<SgGarageOrderLog> page, @Param("dto") SgGarageOrderLogDTO dto);
}
