package com.yixin.garage.dto.order;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yixin.common.utils.BaseDTO;
import com.yixin.common.utils.serializer.JsonDateDeserializer;
import com.yixin.common.utils.serializer.JsonDateSerializer;
import com.yixin.garage.dto.garage.SgGarageInfoDTO;
import com.yixin.garage.dto.sys.AttachSourceDTO;
import com.yixin.garage.dto.vehicle.SgVehicleInfoDTO;
import com.yixin.garage.enums.garage.*;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;
import java.util.List;

/**
 * 出库入库单DTO Package : com.yixin.rentcar.dto.vehicleoperation
 * 
 * @author YixinCapital -- qinyunfei 2016年6月15日 下午4:19:45
 *
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SgGarageOrderDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	/**
	 * 实际出库时间
	 */
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date actualStartTime;

	/**
	 * 调配原因
	 */
	private String assignReason;

	/**
	 * 证件号码
	 */
	private String cerNum;
	/**
	 * 证件类型
	 */
	private String cerType;
	private String cerTypeStr;

	/**
	 * 托运费
	 */
	private String consignmentFee;

	/**
	 * 联系电话
	 */
	private String contactTel;

	/**
	 * 司机姓名
	 */
	private String driverName;

	/**
	 * 物流公司
	 */
	private String logiCompany;

	/**
	 * 运输车辆车牌号
	 */
	private String logiLicNum;

	/**
	 * 订单状态
	 */
	private Integer orderStatus;
	private String  orderStatusStr;

	/**
	 * 预计入库时间
	 */
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date predictEndTime;

	/**
	 * 推送来源
	 */
	private Integer pushSource;
	private String pushSourceStr;
	/**
	 * 出库单号(仅用于融后推送的出入库)
	 */
	@TableField("sgGarageCKDatail_id")
	private String sgGarageCKDatailId;

	/**
	 * 入库单号(仅用于融后推送的出入库)
	 */
	@TableField("sgGarageRKDatail_id")
	private String sgGarageRKDatailId;

	/**
	 * 预计出库库车库
	 */
	private String sgGarageInfoFromId;

	/**
	 * 预计入库车库
	 */
	private String sgGarageInfoToId;

	/**
	 * 车辆ID
	 */
	private String sgVehicleId;

	/**
	 * 运输方式
	 */
	private String shippingType;
	private String shippingTypeStr;

	/**
	 * 调配单任务单号
	 */
	private String taskNum;
	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 入库车库
	 */
	private String garageInName;

    /**
     * 出库车库
     */
    private String garageOutName;

	/**
	 * 车辆出库附件
	 */
	private List<String> attIds;

	/**
	 * 车辆其他附件
	 */
	private List<String> otherAttIds;

	/**
	 * 删除附件集合
	 */
	private List<String> delAttIds;

	/**
	 * 车辆DTO
	 */
	private List<SgVehicleInfoDTO> vehicleInfoDTOList;

	/**
	 * 附件集合
	 */
	private List<AttachSourceDTO> attachSourceDTOList;

	/**
	 * 其他附件集合
	 */
	private List<AttachSourceDTO> otherAttDTOList;

	/**
	 * 里程
	 */
	private BigDecimal mileage;

	/**
	 * 出入库单号
	 */
	private String detailTaskNum;

	public String getCerTypeStr() {
		if (this.cerType != null) {
			return CerTypeCodeEnum.getDisplayNameByIndex(this.cerType);
		} else {
			return "";
		}
	}
	public void setCerTypeStr(String cerTypeStr) {
		this.cerTypeStr = cerTypeStr;
	}
	public String getOrderStatusStr() {
		if (this.orderStatus != null) {

			return SgAllocateTaskStatusEnum.getDisplayNameByIndex(this.orderStatus);
		} else {
			return "";
		}
	}
	public void setOrderStatusStr(String orderStatusStr) {
		this.orderStatusStr = orderStatusStr;
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

}