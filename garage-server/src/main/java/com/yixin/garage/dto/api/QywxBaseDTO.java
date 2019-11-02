package com.yixin.garage.dto.api;

import javax.validation.constraints.NotNull;

import com.yixin.common.utils.BaseDTO;
/**
 * 
 * @ClassName: QywxBaseDTO
 * @Description 企业微信入参BaseDTO
 * @author  YixinCapital -- lizhongxin	   
 * @date  2019年1月24日 上午11:46:11
 *
 */
public class QywxBaseDTO extends BaseDTO{
	
	private static final long serialVersionUID = -8810053313993295255L;
	/**
     * 操作人域账号
     */
	@NotNull(message = "操作人域账号不能为空")
    private String domainAccount;
	
    public String getDomainAccount() {
		return domainAccount;
	}
	public void setDomainAccount(String domainAccount) {
		this.domainAccount = domainAccount;
	}
    

}

