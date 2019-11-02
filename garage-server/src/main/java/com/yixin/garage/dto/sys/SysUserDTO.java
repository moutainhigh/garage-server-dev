package com.yixin.garage.dto.sys;
/**
 * 用户信息
 * @author lishuaifeng
 *  
 *
 */
public class SysUserDTO {
	/**
	 * 域账号
	 */
	private String domainName;
	/**
	 * 中文名
	 */
	private String cnName;
	/**
	 * 手机号
	 */
	private String mobilePhone;
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public String getCnName() {
		return cnName;
	}
	public void setCnName(String cnName) {
		this.cnName = cnName;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	@Override
	public String toString() {
		return "UserInfoDTO [domainName=" + domainName + ", cnName=" + cnName + ", mobilePhone=" + mobilePhone + "]";
	}
	
	
}
