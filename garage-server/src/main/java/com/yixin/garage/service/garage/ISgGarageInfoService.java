package com.yixin.garage.service.garage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yixin.common.exception.BzException;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.dto.garage.SgGarageInfoDTO;
import com.yixin.garage.dto.inventory.SgInventoryGrageIdsDTO;
import com.yixin.garage.dto.inventory.SgInventoryHomeDTO;
import com.yixin.garage.dto.sys.YxBaseRegionDTO;
import com.yixin.garage.entity.garage.SgGarageInfo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author libochen
 * @since 2019-08-01
 */
public interface ISgGarageInfoService extends IService<SgGarageInfo> {

    Page<SgGarageInfoDTO> pageQuery(SgGarageInfoDTO dto) throws BzException;

    InvokeResult<String> createGarage(SgGarageInfoDTO dto);

    InvokeResult<SgGarageInfoDTO> garageDetail(String id);

    InvokeResult<List<SgGarageInfoDTO>> getGarageInfoList();

    InvokeResult<List<SgGarageInfoDTO>> getGarageInfoAllList(SgGarageInfoDTO dto);

    InvokeResult<String> updateGarage(SgGarageInfoDTO dto);

}
