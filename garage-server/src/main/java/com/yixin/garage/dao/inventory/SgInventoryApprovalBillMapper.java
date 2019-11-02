package com.yixin.garage.dao.inventory;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.garage.dto.inventory.SgInventoryBillDTO;
import com.yixin.garage.dto.inventory.SgInventoryHomeDTO;
import com.yixin.garage.entity.inventory.SgInventoryApprovalBill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yixin.garage.entity.inventory.SgInventoryHome;
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
public interface SgInventoryApprovalBillMapper extends BaseMapper<SgInventoryApprovalBill> {

    List<SgInventoryBillDTO> pageQueryBill( @Param("queryDto") SgInventoryBillDTO queryDto,@Param("hasRoles") Boolean hasRoles,@Param("userName") String userName);

}
