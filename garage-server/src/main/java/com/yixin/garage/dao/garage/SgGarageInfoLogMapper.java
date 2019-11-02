package com.yixin.garage.dao.garage;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.garage.dto.garage.YxGarageInfoLogDTO;
import com.yixin.garage.entity.garage.SgGarageInfoLog;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author libochen
 * @since 2019-08-01
 */
public interface SgGarageInfoLogMapper extends BaseMapper<SgGarageInfoLog> {

    IPage<YxGarageInfoLogDTO> pageQueryLog(@Param("page")Page<SgGarageInfoLog> pageParam,@Param("queryDto") YxGarageInfoLogDTO dto);

}
