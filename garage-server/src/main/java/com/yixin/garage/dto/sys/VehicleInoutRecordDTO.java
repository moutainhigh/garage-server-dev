package com.yixin.garage.dto.sys;

import java.util.Date;

import com.yixin.common.utils.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain=true)
public class VehicleInoutRecordDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 838603478785192866L;
	// 车架号
	private String vin;

	// 申请编号
	private String applyNum;

	// 车牌号
	private String licNum;

	// 出入类型(入库1、出库2）
	private Integer type;

	// 数据生成日期(数据create_time)
	private Date dataCreateDate;

	// 出入库日期 （入库时间，出库单创建时间）
	private Date inoutDate;

	// 租赁属性(1正租，2回租）
	private Integer rentType;

	// 业务类型（garageOrder中取）
	private String businessTypeName;

	// 车库名
	private String garageName;

	// 车库id
	private String garageId;

	// 备注 (log)
	private String remark;

}
