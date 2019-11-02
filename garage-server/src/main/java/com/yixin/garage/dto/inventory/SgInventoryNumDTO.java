package com.yixin.garage.dto.inventory;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 盘点数量dto
 * @author: YixinCapital -- libochen
 * @create: 2019-01-17 14:01
 **/
@Data
public class SgInventoryNumDTO implements Serializable {

    /**
     * 清单数量
     */
    private String listNum;

    /**
     * 实际盘点数量
     */
    private String actualNum;

    /**
     * 合格数量
     */
    private String qualifiedNum;

    /**
     * 不合格数量
     */
    private String unQualifiedNum;

}
