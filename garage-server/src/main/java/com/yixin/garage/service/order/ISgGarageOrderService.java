package com.yixin.garage.service.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yixin.common.exception.BzException;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.controller.vehicle.SgVehicleInfoController;
import com.yixin.garage.dto.order.SgGarageDetailDTO;
import com.yixin.garage.dto.order.SgGarageOrderDTO;
import com.yixin.garage.dto.vehicle.SgVehicleInfoDTO;
import com.yixin.garage.entity.order.SgGarageDetail;
import com.yixin.garage.entity.order.SgGarageOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liyaqing
 * @since 2019-08-19
 */
public interface ISgGarageOrderService extends IService<SgGarageOrder> {

    /**
     * @Title: garageOutBranch
     * @Description: 分公司出库处理
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/20 10:42
     */
    InvokeResult<String> garageOutBranch(SgGarageOrderDTO sgGarageOrderDTO) throws BzException;

    /**
     * @Title: approveGarageOut
     * @Description: 出库-总部审批
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/20 10:46
     */
    InvokeResult<String> approveGarageOut(SgGarageOrderDTO sgGarageOrderDTO) throws BzException;

    /**
     * @Title: reject
     * @Description: 审批拒绝
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/20 14:05
     */
    InvokeResult<String> reject(SgGarageOrderDTO sgGarageOrderDTO) throws BzException;

    /**
     * @Title: pageQueryCK
     * @Description: 分页查询出库列表
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/20 14:37
     */
    IPage<SgGarageDetailDTO> pageQuery(SgGarageDetailDTO sgGarageDetailDTO) throws BzException;

    /**
     * @Title: getBill
     * @Description: 查看详情
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/20 18:17
     */
    SgGarageOrderDTO getBill(String sgGarageDetailId) throws BzException;

    /**
     * @Title: createTempOut
     * @Description: 新增临时出库
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/21 10:47
     */
    InvokeResult<String> createTempOut(SgGarageDetailDTO sgGarageDetailDTO) throws BzException;


}
