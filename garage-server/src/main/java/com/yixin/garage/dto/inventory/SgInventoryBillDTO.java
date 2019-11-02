package com.yixin.garage.dto.inventory;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yixin.common.utils.BaseDTO;
import com.yixin.common.utils.serializer.JsonDateDeserializer;
import com.yixin.common.utils.serializer.JsonDateSerializer;
import com.yixin.garage.enums.garage.inventory.InventoryBillStateEnum;
import com.yixin.garage.enums.garage.inventory.InventoryHomeStateEnum;
import com.yixin.garage.enums.garage.inventory.InventoryResultTypeEnum;
import lombok.Data;

import java.util.Date;

/**
 * @description: 资源车盘点 主单dto
 * @author: YixinCapital -- libochen
 * @create: 2019-01-08 18:25
 **/
@Data
public class SgInventoryBillDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    //==================盘点主单所需字段Star==================//

    /**
     * 盘点期数（任务编号）
     */
    private String billNum;

    /**
     * 车库名称
     */
    private String garageInfoName;

    /**
     * 车库id
     */
    private String garageInfoId;

    /**
     * 车库地址
     */
    private String garageAddresss;


    /**
     * 盘点状态
     */
    private Integer approvalState;

    /**
     * 盘点状态
     */
    private String approvalStateStr;

    /**
     * 库管姓名
     */
    private String contact;

    /**
     * 最终完成时间
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date finalizedTime;

    //==================盘点主单所需字段End==================//


    //==================盘点审批主单所需字段Star==================//

    /**
     * 提交时间
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date submitTime;

    /**
     * 审批意见
     */
    private String approvalOpinion;


    /**
     * 盘点home表id（外键）
     */
    private String inventoryHomeId;

    /**
     * 提交人姓名
     */
    private String submitterName;

    /**
     * 登录人域账号（app用）
     */
    private String userName;

    /**
     * 登录人姓名（app用）
     */
    private String cnName;

    /**
     * 截止时间判断盘点按钮
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date endTime;


    /**
     * 盘点结果类型
     */
    private Integer resultType;
    private String resultTypeStr;



    public String getApprovalStateStr() {
        if (this.approvalState != null) {
            return InventoryBillStateEnum.getDisplayNameByIndex(this.approvalState);
        } else {
            return "";
        }
    }

    public String getResultTypeStr() {
        if (this.resultType != null) {
            return InventoryResultTypeEnum.getDisplayNameByIndex(this.resultType);
        } else {
            return "";
        }
    }

}
