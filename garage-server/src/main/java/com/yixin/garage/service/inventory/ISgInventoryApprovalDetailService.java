package com.yixin.garage.service.inventory;

import com.yixin.common.exception.BzException;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.dto.inventory.SgInventoryBillDTO;
import com.yixin.garage.dto.inventory.SgInventoryBillReturnDTO;
import com.yixin.garage.dto.inventory.SgInventoryDetailsDTO;
import com.yixin.garage.dto.inventory.SgInventoryResultStatisticsDTO;
import com.yixin.garage.entity.inventory.SgInventoryApprovalDetail;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author libochen
 * @since 2019-10-11
 */
public interface ISgInventoryApprovalDetailService extends IService<SgInventoryApprovalDetail> {



    SgInventoryDetailsDTO getDetailView(SgInventoryDetailsDTO dto) throws BzException;

    String saveDetail(SgInventoryDetailsDTO dto) throws BzException;

    String otherAdd(SgInventoryDetailsDTO dto) throws BzException;

    String otherDelete(SgInventoryDetailsDTO dto) throws BzException;

    InvokeResult<SgInventoryResultStatisticsDTO> resultStatistics(SgInventoryBillDTO dto) throws BzException;

    String saveApproveDetail(SgInventoryDetailsDTO dto) throws BzException;
}
