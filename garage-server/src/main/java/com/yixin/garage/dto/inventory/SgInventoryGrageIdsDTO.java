package com.yixin.garage.dto.inventory;

import lombok.Data;

/**
 * @description: 资源车盘点主页面所需dto
 * @author: YixinCapital -- libochen
 * @create: 2019-01-08 18:24
 **/
@Data
public class SgInventoryGrageIdsDTO {

    private static final long serialVersionUID = 1L;
    /**
     * 车库id
     */
    private String id;

    /**
     * 车库name
     */
    private String name;

}
