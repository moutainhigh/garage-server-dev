package com.yixin.garage.service.order;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.dto.order.SgGarageDetailDTO;
import com.yixin.garage.dto.order.SgGarageOrderDTO;
import com.yixin.garage.dto.order.SgGarageOrderLogDTO;
import com.yixin.garage.entity.order.SgGarageDetail;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liyaqing
 * @since 2019-08-19
 */
public interface ISgGarageDetailService extends IService<SgGarageDetail> {

    /**
    * pageQueryRKOrder(分页查询列表页)
    * @param dto
    * @return
    * @author: YixinCapital -- libochen
    * 2019/8/20 10:46
    */
    Page<SgGarageDetailDTO> pageQueryRKOrder(SgGarageDetailDTO dto);

    /**1
    * approveIn(入库审批)
    * @param id
    * @return
    * com.yixin.common.utils.InvokeResult<java.lang.String>
    * @author: YixinCapital -- libochen
    * 2019/8/20 10:46
    */
    InvokeResult<String> approveIn(SgGarageOrderDTO dto);

    
    /**
    * inReject(审批驳回)
    * @param sgGarageOrderDTO
    * @return
    * com.yixin.common.utils.InvokeResult<java.lang.String>
    * @author: YixinCapital -- libochen 
    * 2019/8/20 10:46 
    */
    InvokeResult<String> inReject(SgGarageOrderDTO sgGarageOrderDTO);


    /**
    * getBill(查看入库详情)
    * @param sgGarageOrderId
    * @return
    * com.yixin.garage.dto.order.SgGarageOrderDTO
    * @author: YixinCapital -- libochen
    * 2019/8/21 11:13
    */
    SgGarageOrderDTO getBill(String sgGarageOrderId);


    /**
    * pageQueryLog(分页查询审批日志)
    * @param sgGarageOrderLogDTO
    * @return
    * @author: YixinCapital -- libochen
    * 2019/8/21 11:38
    */
    Page<SgGarageOrderLogDTO> pageQueryLog(SgGarageOrderLogDTO sgGarageOrderLogDTO);
}
