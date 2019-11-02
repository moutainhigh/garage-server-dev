package com.yixin.garage.service.inventory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.common.exception.BzException;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.dto.garage.SgGarageInfoDTO;
import com.yixin.garage.dto.inventory.SgInventoryGrageIdsDTO;
import com.yixin.garage.dto.inventory.SgInventoryHomeDTO;
import com.yixin.garage.dto.vehicle.SgVehicleInfoDTO;
import com.yixin.garage.entity.inventory.SgInventoryHome;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author libochen
 * @since 2019-10-11
 */
public interface ISgInventoryHomeService extends IService<SgInventoryHome> {

    /**
     * createInventory(创建盘库任务)
     * @param homeDTO
     * @return
     * java.lang.String
     * @author: YixinCapital -- libochen
     * 2019/1/9 15:16
     */
    String createInventory(SgInventoryHomeDTO homeDTO);

    String updateInventory(SgInventoryHomeDTO homeDTO);

    Page<SgInventoryHomeDTO> pageQuery(SgInventoryHomeDTO dto) throws BzException;

    SgInventoryHomeDTO getHomeView(SgInventoryHomeDTO dto) throws BzException;

    /**
     * checkData(任务是否能发布校验)
     * @param dto
     * @return
     * java.lang.String
     * @author: YixinCapital -- libochen
     * 2019/10/11 10:36
     */
    String checkData(SgInventoryHomeDTO dto);

    /**
    * issue(发布盘库任务)
    * @param dto
    * @return
    * java.lang.String
    * @author: YixinCapital -- libochen 
    * 2019/10/11 17:27 
    */
    String issue(SgInventoryHomeDTO dto);


    InvokeResult<List<String>> getInventoryList();


    InvokeResult<List<SgInventoryGrageIdsDTO>> getGarageInfoListForApp(SgInventoryHomeDTO dto);

}
