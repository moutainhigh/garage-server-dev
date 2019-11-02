package com.yixin.garage.dao.inventory;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.garage.dto.garage.SgGarageInfoDTO;
import com.yixin.garage.dto.inventory.SgInventoryGrageIdsDTO;
import com.yixin.garage.dto.inventory.SgInventoryHomeDTO;
import com.yixin.garage.entity.garage.SgGarageInfo;
import com.yixin.garage.entity.inventory.SgInventoryHome;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liyaqing
 * @since 2019-10-11
 */
public interface SgInventoryHomeMapper extends BaseMapper<SgInventoryHome> {


    String createBillNum(@Param("dateString") String dateString);


    IPage<SgInventoryHomeDTO> pageQuery(@Param("page") Page<SgInventoryHome> page, @Param("queryDto") SgInventoryHomeDTO queryDto);

    List<SgInventoryGrageIdsDTO> getGarageInfoListForApp(@Param("queryDto") SgInventoryHomeDTO queryDto,
                                                         @Param("hasRoles") Boolean hasRoles,
                                                         @Param("userName") String userName,
                                                         @Param("isAll") Boolean isAll);



}
