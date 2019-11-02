package com.yixin.garage.dto.zhongtai;


import com.yixin.common.utils.BaseDTO;
import com.yixin.garage.enums.garage.zhongtai.BillStatusEnum;
import com.yixin.garage.enums.garage.zhongtai.SendTypeEnum;
import lombok.Data;

/**
 *
 * @author YixinCapital -- zhouhang
 * 2019年8月29日 上午9:27:11
 */
@Data
public class SgGarageSendInfoDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;
	/**
	 * 单号
	 */
	private String billNum;

	/**
	 * 单子类型
	 */
	private Integer type;
	private String typeStr;

	/**
	 * 接口类型
	 */
	private String interfaceName;
	private String interfaceNameStr;

	/**
	 * 发送次数
	 */
	private Integer sendCount;

	/**
	 * 发送状态
	 */
	private Integer stat;

	/**
	 * 每次发送数据的唯一标识(批次号)
	 */
	private String batchNum;

	/**
	 * 行数
	 */
	private Integer lineCount;

	/**
	 * 发送内容
	 */
	private String sendInfo;

	/**
	 * 推送类型
	 */
	private String pushMode;

	public String getTypeStr() {
		if(this.type != null ){
			this.typeStr = BillStatusEnum.getDisplayNameByIndex(this.type);
		}else{
			typeStr = "";
		}
		return typeStr;
	}

	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}

	public String getInterfaceNameStr() {
		if(this.interfaceName != null ){
			this.interfaceNameStr = SendTypeEnum.getDisplayNameByIndex(this.interfaceName);
		}else{
			interfaceNameStr = "";
		}
		return interfaceNameStr;
	}
	public void setInterfaceNameStr(String interfaceNameStr) {
		this.interfaceNameStr = interfaceNameStr;
	}

}