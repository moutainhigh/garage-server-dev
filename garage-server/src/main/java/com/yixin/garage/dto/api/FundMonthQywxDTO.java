package com.yixin.garage.dto.api;

import java.math.BigDecimal;


import lombok.Data;

/**
 * 
 * @ClassName: FundMonthQywxDTO
 * @Description 资金项目微信对接 
 * @author  YixinCapital -- mjj	   
 * @date  2019年2月13日 下午3:29:56
 *
 */
@Data
public class FundMonthQywxDTO extends QywxBaseDTO {

	private static final long serialVersionUID = 1L;

    /**
     * 计划期间
     */
    private String planPeriod;

    /**
     * 资金缺口
     */
    private BigDecimal gapAmount;
    private BigDecimal gapAmountWan;

    /**
     * 审批状态
     */
    private String approveStatus;
    
    /**
     * 审批状态名称
     */
    private String approveStatusName;
    
    
    /**
     * 换算单位字段
     */
    private BigDecimal unitNum = BigDecimal.ONE;
    
    /**
     * 审批类型
     */
    private String auditBussType;
    
    /**
     * 列表页title
     */
    private String title;
    
    
  
}
