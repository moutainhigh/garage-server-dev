package com.yixin.garage.dto.inventory;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 盘点数量dto
 * @author: YixinCapital -- libochen
 * @create: 2019-01-17 14:01
 **/
@Data
public class SgInventoryBillReturnDTO implements Serializable {

    /**
     * 实际在库列表
     */
    private List<SgInventoryDetailsDTO> inGarageVehicles;

    /**
     * 手动新增在库列表
     */
    private List<SgInventoryDetailsDTO> othersInVehicles;

    /**
     * 盘点数量统计
     */
    private SgInventoryNumDTO inventoryNum;

    /**
     * 审批意见
     */
    private String approvalOpinion;

    /**
     * 审批状态
     */
    private Integer billState;


}
