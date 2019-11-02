package com.yixin.garage.dao.vehicle;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.garage.dto.inventory.SgInventoryHomeDTO;
import com.yixin.garage.dto.vehicle.SgVehicleInfoDTO;
import com.yixin.garage.entity.vehicle.SgVehicleInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liyaqing
 * @since 2019-08-16
 */
public interface SgVehicleInfoMapper extends BaseMapper<SgVehicleInfo> {

    /**
     * @Title: getBill
     * @Description: 查看详情
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/19 14:17
     */
    SgVehicleInfoDTO getBill(@Param("sgVehicleId") String sgVehicleId);

    /**
     * @Title: pageQuery
     * @Description: 分页查询车辆信息
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/19 15:08
     */
    IPage<SgVehicleInfoDTO> pageQuery(@Param("page") Page<SgVehicleInfo> page, @Param("dto") SgVehicleInfoDTO dto,@Param("garageAdmins") List<String> garageAdmins);

    /**
     * @Title: orderPageQuery
     * @Description: 订单列表分页查询
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/21 14:04
     */
    IPage<SgVehicleInfoDTO> orderPageQuery(@Param("page") Page<SgVehicleInfo> page, @Param("dto") SgVehicleInfoDTO dto);


    /**
    * pageQuery(盘点查询当期盘点下车辆)
    * @param page
    * @param dto
    * @return
    * @author: YixinCapital -- libochen
    * 2019/10/11 16:39
    */
    IPage<SgVehicleInfoDTO> getHomeView(@Param("page") Page<SgVehicleInfo> page, @Param("dto") SgInventoryHomeDTO dto);

}
