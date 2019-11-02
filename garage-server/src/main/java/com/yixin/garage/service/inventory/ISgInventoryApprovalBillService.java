package com.yixin.garage.service.inventory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.common.exception.BzException;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.dto.inventory.SgInventoryBillDTO;
import com.yixin.garage.dto.inventory.SgInventoryBillReturnDTO;
import com.yixin.garage.dto.inventory.SgInventoryDetailsDTO;
import com.yixin.garage.dto.inventory.SgInventoryResultStatisticsDTO;
import com.yixin.garage.entity.inventory.SgInventoryApprovalBill;
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
public interface ISgInventoryApprovalBillService extends IService<SgInventoryApprovalBill> {

    /**
    * pageQueryBill(盘点页列表查询)
    * @param dto
    * @return
    * @author: YixinCapital -- libochen
    * 2019/10/12 10:23
    */
    List<SgInventoryBillDTO> pageQueryBill(SgInventoryBillDTO dto) throws BzException;


    /**
    * getBillView(主单查看详情接口)
    * @param dto
    * @return
    * com.yixin.garage.dto.inventory.SgInventoryBillReturnDTO
    * @author: YixinCapital -- libochen
    * 2019/10/12 11:01
    */
    SgInventoryBillReturnDTO getBillView(SgInventoryDetailsDTO dto) throws BzException;

    /**
    * submitBill(提交盘点任务)
    * @param dto
    * @return
    * com.yixin.common.utils.InvokeResult<java.lang.String>
    * @author: YixinCapital -- libochen
    * 2019/10/22 19:19
    */
    InvokeResult<String> submitBill(SgInventoryBillDTO dto) throws BzException;

    /**
    * submitApproveBill(提交盘点审批)
    * @param dto
    * @return
    * com.yixin.common.utils.InvokeResult<java.lang.String>
    * @author: YixinCapital -- libochen 
    * 2019/10/22 19:19 
    */
    InvokeResult<String> submitApproveBill(SgInventoryBillDTO dto) throws BzException;


    /**
    * rejectBill(盘点审批驳回)
    * @param dto
    * @return
    * com.yixin.common.utils.InvokeResult<java.lang.String>
    * @author: YixinCapital -- libochen
    * 2019/10/22 19:20
    */
    InvokeResult<String> rejectBill(SgInventoryBillDTO dto) throws BzException;

    /**
    * submitSendMail(通过并上报经理)
    * @param dto
    * @return
    * com.yixin.common.utils.InvokeResult<java.lang.String>
    * @author: YixinCapital -- libochen
    * 2019/10/22 19:20
    */
    InvokeResult<String> submitSendMail(SgInventoryBillDTO dto) throws BzException;


}
