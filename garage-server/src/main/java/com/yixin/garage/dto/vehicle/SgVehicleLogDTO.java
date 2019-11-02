package com.yixin.garage.dto.vehicle;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yixin.common.utils.BaseDTO;
import com.yixin.common.utils.serializer.JsonDateDeserializer;
import com.yixin.common.utils.serializer.JsonDateSerializer;
import lombok.Data;

import java.util.Date;

/**
 * @author YixinCapital -- liyaqing
 * @description: 车辆日志
 * @date 2019/8/619:24
 */
@Data
public class SgVehicleLogDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    private String event;

    private String remark;

    private String vehicleId;

    private String taskBillId;

    private Integer eventcode;
    /**
     * 合同号
     */
    private String contractNum;

    /**
     * 申请编号
     */
    private String applyNum;

    /**
     * 实际入库时间
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date actualStorageTime;

    /**
     * 操作出入库的车库id
     */
    private String garageId;
    /**
     * 操作出入库的车库名字
     */
    private String garageName;

    /**
     * 关联业务ID
     */
    private String businessId;

    private String garageOrderId;
}
