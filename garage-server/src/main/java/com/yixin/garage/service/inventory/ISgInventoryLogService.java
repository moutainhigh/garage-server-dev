package com.yixin.garage.service.inventory;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yixin.common.exception.BzException;
import com.yixin.garage.dto.inventory.SgInventoryBillDTO;
import com.yixin.garage.dto.inventory.SgInventoryLogDTO;
import com.yixin.garage.entity.inventory.SgInventoryApprovalBill;
import com.yixin.garage.entity.inventory.SgInventoryLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author libochen
 * @since 2019-10-11
 */
public interface ISgInventoryLogService extends IService<SgInventoryLog> {

     /***
      * createInventoryLog(创建盘点日志)
      * @param bill 审批主单
      * @param dto  传入参数
      * @param operationNode  审批节点
      * @param remark  备注
      * @return
      * void
      * @author: YixinCapital -- libochen
      * 2019/10/22 19:33
      */
     void createInventoryLog(SgInventoryApprovalBill bill, SgInventoryBillDTO dto, String operationNode, String remark, String stat);


     IPage<SgInventoryLog> pageQueryLog(SgInventoryLogDTO dto) throws BzException;
}
