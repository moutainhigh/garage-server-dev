package com.yixin.garage.dao.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.garage.dto.order.SgGarageDetailDTO;
import com.yixin.garage.dto.order.SgGarageOrderDTO;
import com.yixin.garage.entity.order.SgGarageDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liyaqing
 * @since 2019-08-19
 */
public interface SgGarageDetailMapper extends BaseMapper<SgGarageDetail> {

    IPage<SgGarageDetailDTO> pageQuery(@Param("page") Page<SgGarageDetail> page, @Param("dto") SgGarageDetailDTO dto, @Param("garageSign") Integer garageSign,@Param("garageAdmins") List<String> garageAdmins);

    String createTaskNum(@Param("dateString") String dateString,@Param("garageSign") String garageSign);

    SgGarageDetail queryTempOut(@Param("sgGaragOrderId") String sgGaragOrderId);

    /**
     * @Title: queryOutDeatil
     * @Description: 根据推送来源查询出库单信息
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/29 15:50
     */
    SgGarageDetail queryOutDeatil(@Param("dto") SgGarageOrderDTO dto);

    Map<String,Object> getInfoListByBillNum(@Param("alixNum") String alixNum);

    /**
     * @Title: getGarageInExis
     * @Description: 判断是否为融后推送的数据
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/9/4 15:42
     */
    SgGarageDetail getGarageInExis(@Param("dto") SgGarageDetailDTO dto);

    String getContractNoByBillNum(@Param("alixNum") String alixNum);

}
