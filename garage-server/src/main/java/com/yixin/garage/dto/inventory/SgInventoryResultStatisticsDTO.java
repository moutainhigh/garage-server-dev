package com.yixin.garage.dto.inventory;

import lombok.Data;

/**
 * @description: 资源车盘点主页面所需dto
 * @author: YixinCapital -- libochen
 * @create: 2019-01-08 18:24
 **/
@Data
public class SgInventoryResultStatisticsDTO {

    private static final long serialVersionUID = 1L;
    /**
     * 在库数量
     */
    private String numberOfLibraries;

    /**
     * 临时出库数量
     */
    private String temporaryDeliveryQuantity;

    /**
     * 未在库数量
     */
    private String quantityNotInStock;

    /**
     * 需补推入库数量
     */
    private String quantityToBeAdded;

    /**
     * 存量数量（也就是发布任务的车辆数）
     */
    private String stockQuantity;


}
