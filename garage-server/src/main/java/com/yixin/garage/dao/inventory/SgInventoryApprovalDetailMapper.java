package com.yixin.garage.dao.inventory;

import com.yixin.garage.dto.inventory.SgInventoryBillDTO;
import com.yixin.garage.entity.inventory.SgInventoryApprovalDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liyaqing
 * @since 2019-10-11
 */
public interface SgInventoryApprovalDetailMapper extends BaseMapper<SgInventoryApprovalDetail> {

    String resultStatistics(@Param("queryDto") SgInventoryBillDTO dto,
                            @Param("resultType") Integer resultType,
                            @Param("manualAdd") boolean manualAdd);


    String otherDelete(@Param("id") String id);
}
