package com.yixin.garage.dto.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yixin.common.utils.BaseDTO;
import com.yixin.common.utils.serializer.JsonDateDeserializer;
import com.yixin.common.utils.serializer.JsonDateSerializer;
import com.yixin.garage.dto.sys.AttachSourceDTO;
import com.yixin.garage.dto.vehicle.SgVehicleInfoDTO;
import com.yixin.garage.enums.garage.InUseApproveStatusEnum;
import com.yixin.garage.enums.garage.RCZBInBillStatusEnum;
import com.yixin.garage.enums.garage.SgAllocateTaskStatusEnum;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 出库入库订单详情DTO Package : com.yixin.rentcar.dto.vehicleoperation
 * 
 * @author YixinCapital -- qinyunfei 2016年6月15日 下午4:12:21
 *
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SgTaskBillDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;
	/**
	 * 出库入库单
	 */
	private SgGarageOrderDTO sgGarageOrderDTO;

	/**
	 * 工单号
	 */
	private String taskNum;

	/***
	 * 车辆信息
	 */
	private SgVehicleInfoDTO sgVehicleInfoDTO;

	/**
	 * 车架号
	 */
	private String vin;

	/**
	 * 车牌号
	 */
	private String licNum;

	/**
	 * 出入库编号
	 */
	private String garageOrderNum;

	/**
	 * 客户名称
	 */
	private String custName;

	/**
	 * 加盟商ID
	 */
	private String ownerFranId;

	/**
	 * 预计还车时间(时间段开始时间)
	 */
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date predictEndTimeFrom;

	/**
	 * 预计还车时间(时间段结束时间)
	 */
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date predictEndTimeTo;

	/**
	 * 实际用车时间(时间段开始时间)
	 */
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date actualStartTimeFrom;

	/**
	 * 实际用车时间(时间段结束时间)
	 */
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date actualStartTimeTo;
	// -------------------使用中车辆页面查询请求参数----------------------

	/**
	 * 出库入库单id
	 */
	private String sgGarageOrderId;

	/**
	 * 承租人
	 */
	private String lessee;

	/**
	 * 订单类别（经营性租赁，库间调配，其他用车，融资租赁）
	 */
	private Integer orderType;

	/**
	 * 合同号
	 */
	private String contractNum;
	
	/**
	 * 调配状态
	 */
	private Integer billStatus;
	private String billStatusStr;
	
	/**
     * 出库单号
     */
    private String outTaskNum;
    
    /**
     * 入库单号
     */
    private String inTaskNum;

	private String approveRkDetailId;
    
    /**
     * 入库总部审批Id
     */
    private String garageRkDetailId;

    /**
     * 出库总部审批Id
     */
	private String garageCkDetailId;
    
    /**
	 * 信息附件
	 */
	private List<AttachSourceDTO> attachSourceDTOList;
	
	/**
	 * 出库凭证附件Ids
	 */
	private String outAllocateTasksIds;
	
	/**
	 * 其他附件Ids
	 */
	private String othersAttIds;
 
	/**附件ids*/
	private String attIds;

	/**
	 * 出入库单 主单状态
	 */
	private Integer detailZbBillStatus;
	private String detailZbBillStatusStr;
	
	/**
	 * 出入库单ID
	 */
	private String detailId;
	
	/**
	 * 使用中车辆审批状态
	 */
	private Integer inUseApproveStatus;
	private String inUseApproveStatusStr;
	
	/**
	 * 车辆Id
	 */
	private String sgVehicleInfoId;
	
	/**
	 * 是否回填车损附件
	 */
	private String vehicleDescCarAtt;
	
	/**
	 * 发起调配类型
	 */
	private String initiateDepType;

	/**
	 * 是否为Alix推送
	 */
	private String alixPush;



	public String getInUseApproveStatusStr() {
		if (this.inUseApproveStatus != null) {
			return InUseApproveStatusEnum.getDisplayNameByIndex(this.inUseApproveStatus);
		} else {
			return "";
		}
	}

	public void setInUseApproveStatusStr(String inUseApproveStatusStr) {
		this.inUseApproveStatusStr = inUseApproveStatusStr;
	}
	


	public String getBillStatusStr() {
		if (this.billStatus != null) {
            return SgAllocateTaskStatusEnum.getDisplayNameByIndex(this.billStatus);
		} else {
			return "";
		}
	}



	public String getDetailZbBillStatusStr() {
		if (this.detailZbBillStatus != null) {
			return RCZBInBillStatusEnum.getDisplayNameByIndex(this.detailZbBillStatus);
		} else {
			return "";
		}
	}

	public void setDetailZbBillStatusStr(String detailZbBillStatusStr) {
		this.detailZbBillStatusStr = detailZbBillStatusStr;
	}



}
