package com.yixin.garage.dto.inventory;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yixin.common.utils.BaseDTO;
import com.yixin.common.utils.serializer.JsonDateDeserializer;
import com.yixin.common.utils.serializer.JsonDateSerializer;
import com.yixin.garage.enums.garage.OperateAttrEnum;
import com.yixin.garage.enums.garage.RcResourceVehicleTypeEnum;
import com.yixin.garage.enums.garage.inventory.InventoryHomeStateEnum;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description: 资源车盘点主页面所需dto
 * @author: YixinCapital -- libochen
 * @create: 2019-10-10 18:24
 **/
@Data
public class SgInventoryHomeDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 盘点期数
     */
    private String billNum;

    /**
     * 起始时间
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date startTime;

    /**
     * 截止时间
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date endTime;

    /**
     * 发布状态
     */
    private Integer state;

    /**
     * 发布状态
     */
    private String stateStr;

    /**
     * 指定车库类型
     */
    private String appoint;

    /**
     * 车库名称id集合
     * 当指定类型为 指定车库时有值
     */
    private List<SgInventoryGrageIdsDTO> garages;

    /**
     * 车库名称ids
     */
    private String garageIds;

    /**
     * 登录人域账号（备用）
     */
    private String userName;

    public String getStateStr() {
        if (this.state != null) {
            return InventoryHomeStateEnum.getDisplayNameByIndex(this.state);
        } else {
            return "";
        }
    }

}
