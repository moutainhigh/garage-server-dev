package com.yixin.garage.dao.garage;

import com.yixin.garage.dto.garage.SgContactToGarageTempDTO;
import com.yixin.garage.dto.vehicle.SgVehicleInfoDTO;
import com.yixin.garage.entity.garage.SgContactToGarageTemp;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author libochen
 * @since 2019-08-05
 */
public interface SgContactToGarageTempMapper extends BaseMapper<SgContactToGarageTemp> {


    List<SgContactToGarageTempDTO> queryTemp(@Param("contact") String contact, @Param("contactTel") String contactTel);

}
