package com.yixin.garage.dto;

import java.io.Serializable;

import com.yixin.garage.dto.api.QywxBaseDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 融资项目
 * </p>
 *
 * @author lizhongxin
 * @since 2018-12-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class AuditResultDTO implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    @ApiModelProperty(value="业务记录id")
    private String bussId;

    /**
     * 审批结果  通过 true , 驳回false
     */
    @ApiModelProperty(value="审批结果  通过 true , 驳回false")
    private Boolean auditResult;
    
    /**
     * 审批意见
     */
    @ApiModelProperty(value="审批意见")
    private String remark;
   
}
