package com.yixin.garage.entity.inventory;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yixin.common.utils.serializer.JsonDateDeserializer;
import com.yixin.common.utils.serializer.JsonDateSerializer;
import com.yixin.garage.core.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author liyaqing
 * @since 2019-10-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SgInventoryLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 审批节点
     */
    private String operationNode;

    /**
     * 备注
     */
    private String remark;

    /**
     * 申请主单id
     */
    private String inventoryApprovalBillId;

    /**
     * 盘点状态
     */
    private String stat;


    /**
     * 操作时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date operationTime;
}
