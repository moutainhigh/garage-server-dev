package com.yixin.garage.dto.order;

import com.yixin.common.utils.BaseDTO;
import lombok.Data;

/**
 * 调配出入库审批日志信息
 * @Title: YxGarageOrderLogDTO.java  
 * @Package com.yixin.rentcar.dto.vehicleoperation  
 * @Description: TODO  
 * @author YixinCapital -- liyaqing 
 * @date 2018年8月9日
 */
@Data
public class SgGarageOrderLogDTO extends BaseDTO {
	
	/**
     * 事件类型
     */
    private String event;

    /**
     * 事件码
     */
    private Integer eventcode;
    
    
    /**
     * 出入库车库id
     */
    private String sgGarageOrderId;

    /**
     * 车库
     */
    private String sgGarageInfoId;

    /**
     * 信息备注
     */
    private String remark;

}
