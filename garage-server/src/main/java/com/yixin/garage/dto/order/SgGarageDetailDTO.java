package com.yixin.garage.dto.order;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yixin.common.utils.BaseDTO;
import com.yixin.common.utils.serializer.JsonDateDeserializer;
import com.yixin.common.utils.serializer.JsonDateSerializer;
import com.yixin.garage.dto.sys.AttachSourceDTO;
import com.yixin.garage.enums.garage.*;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @author YixinCapital -- liyaqing
 * @description: TODO
 * @date 2019/8/911:48
 */
@Data
public class SgGarageDetailDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;


    /**
     * alixNum
     */
    private String alixNum;

    /**
     * 出入库单状态
     */
    private Integer billStatus;
    private String billStatusStr;

    /**
     * 收车人员
     */
    private String colectPerson;

    /**
     * 收车人员证件号
     */
    private String colectPersonNum;

    /**
     * 合同编号
     */
    private String contractNo;

    /**
     * 出入库标识
     * 0-出库;1-入库；2其他
     */
    private Integer garageSign;
    private String  garageSignStr;

    /**
     * 入库时订单是否取消
     */
    private String isCancel;

    /**
     * 入库类型
     */
    private String orderType;
    private String orderTypeStr;

    /**
     * 出库原因
     */
    private Integer outReason;
    private String outReasonStr;

    /**
     * 推送来源 0-融后 1-临时出库
     */
    private Integer pushSource;
    private String pushSourceStr;

    /**
     * 备注
     */
    private String remark;

    /**
     * 订单ID
     */
    private String sgGaragOrderId;

    /**
     * 车辆ID
     */
    private String sgVehicleId;

    /**
     * 车库ID
     */
    private String sgGarageInfoId;

    /**
     * 车库Name
     */
    private String garageName;

    /**
     * 出入库单号
     */
    private String taskNum;

    /**
     * 车架号
     */
    private String vin;

    /**
     * 托运费
     */
    private String consignmentFee;
    /**
     * 运输方式
     */
    private String shippingType;
    private String shippingTypeStr;

    /**
     * ordre 订单单号
     */
    private String orderTaskNum;

    /**
     * order 订单状态
     */
    private Integer orderStatus;

    /**
     * 实际用车时间
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date actualStartTime;

    /**
     * 实际入库时间
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date actualEndTime;

    /**
     * 预计还车时间
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date predictEndTime;

    /**
     * 创建时间 - 开始
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date createTimeBegin;

    /**
     * 创建时间 - 结束
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date createTimeEnd;


    /**
     * 附件集合
     */
    private List<AttachSourceDTO> attachSourceDTOList;

    /**
     * 车辆出库附件
     */
    private List<String> attIds;

    /**
     * 车辆其他附件
     */
    private List<String> otherAttIds;

    /**
     * 入库ID
     */
    private String sgGarageRKDatailId;


    /**
     * 临时出库时间
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date tempActualTime;

    /**
     * 临时入库时间
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date tempPredictTime;

    public String getBillStatusStr() {
        if (this.billStatus != null) {
            return SgAllocateTaskStatusEnum.getDisplayNameByIndex(this.billStatus);
        } else {
            return "";
        }
    }

    public void setBillStatusStr(String billStatusStr) {
        this.billStatusStr = billStatusStr;
    }

    public String getGarageSignStr() {
        if (this.garageSign != null) {
            return RCAllocateGarageSignEnum.getDisplayNameByIndex(this.garageSign);
        } else {
            return "";
        }
    }

    public void setGarageSignStr(String garageSignStr) {
        this.garageSignStr = garageSignStr;
    }

    public String getPushSourceStr() {
        if (this.pushSource != null) {
            return PushSourceEnum.getDisplayNameByIndex(this.pushSource);
        } else {
            return "";
        }
    }

    public void setPushSourceStr(String pushSourceStr) {
        this.pushSourceStr = pushSourceStr;
    }

    public String getOutReasonStr() {
        if (this.outReason != null) {
            return OutReasonEnum.getDisplayNameByIndex(this.outReason);
        } else {
            return "";
        }
    }

    public void setOutReasonStr(String outReasonStr) {
        this.outReasonStr = outReasonStr;
    }

    public String getShippingTypeStr() {
        if (!StringUtils.isEmpty(this.shippingType)) {
            return ShippingTypeEnum.getDisplayNameByIndex(this.shippingType);
        } else {
            return "";
        }
    }

    public void setShippingTypeStr(String shippingTypeStr) {
        this.shippingTypeStr = shippingTypeStr;
    }

    public String getOrderTypeStr() {
        if (!StringUtils.isEmpty(this.orderType)) {
            return SgOrderInTypeEnum.getDisplayNameByIndex(this.orderType);
        } else {
            return "";
        }
    }

    public void setOrderTypeStr(String orderTypeStr) {
        this.orderTypeStr = orderTypeStr;
    }
}
