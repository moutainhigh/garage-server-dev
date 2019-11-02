package com.yixin.garage.dto.inventory;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yixin.common.utils.BaseDTO;
import com.yixin.common.utils.serializer.JsonDateDeserializer;
import com.yixin.common.utils.serializer.JsonDateSerializer;
import com.yixin.garage.enums.garage.inventory.InventoryApprovalStateEnum;
import com.yixin.garage.enums.garage.inventory.InventoryResultTypeEnum;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description: 资源车盘点 详单dto
 * @author: YixinCapital -- libochen
 * @create: 2019/10/11 10:36
 **/
@Data
public class SgInventoryDetailsDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    /**
     * 审批主单id
     */
    private String sgBillId;

    //------------车辆信息start------------//
    /**
     * 车架号
     */
    private String vin;

    /**
     * alix申请编号
     */
    private String alixNum;

    /**
     * 牌照号
     */
    private String licNum;

    /**
     * 品牌
     */
    private String brandStr;

    /**
     * 颜色
     */
    private String color;


    /**
     * 车型
     */
    private String vehicleClassStr;

    /**
     * 入库日期
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date actualStorageTime;

    /**
     * 实际颜色
     */
    private String actualColor;

    //------------车辆信息end------------//

    /**
     * 车库地址
     */
    private String garageAddresss;




    //------------审核信息start------------//

    /**
     * 审核状态
     */
    private Integer approvalStatus;
    private String approvalStatusStr;

    /**
     * 不合格原因
     */
    private String unqualifiedReasons;

    //------------审核信息end------------//


    //------------盘点信息start------------//
    /**
     * 实际具体停放地址
     */
    private String actualParkAddress;

    /**
     * 实际具体停放车库
     */
    private String actualGarageName;

    /**
     * 外观是否损坏
     */
    private String isAppearanceDamage;
    private String isAppearanceDamageStr;

    /**
     * 外观是否损坏
     * 具体情况说明
     */
    private String appearanceDamageDescription;

    /**
     * 盘点结果类型
     */
    private Integer resultType;
    private String resultTypeStr;

    /**
     * 盘点结果类型
     * 具体情况说明
     */
    private String resultTypeDescription;

    /**
     * 盘点人姓名
     */
    private String pandianName;

    /**
     * 盘点日期
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date pandianDate;

    /**
     * 车架号照片
     */
    private String vinAttId;
    private String vinPhotoAttId;
    private String vinPhotoView;
    private String vinPhotoName;

    /**
     * 车头正面照片
     */
    private String carHeadAttId;
    private String carHeadPhotoAttId;
    private String carHeadPhotoView;
    private String carHeadPhotoName;


    //------------盘点信息end------------//

    //------------盘点详情start------------//

    /**
     * 是否为手动增加车辆（其他在库车辆）
     */
    private Boolean manualAdd;
    private String manualAddStr;

    /**
     * 是否盘点
     * 0 false 否
     * 1 true  是
     */
    private Boolean inventory;
    private String inventoryStr;


    /**
     * 登录人域账号（备用）
     */
    private String userName;

    public String getResultTypeStr() {
        if (this.resultType != null) {
            return InventoryResultTypeEnum.getDisplayNameByIndex(this.resultType);
        } else {
            return "";
        }
    }

    public String getApprovalStatusStr() {

        if (this.approvalStatus != null) {
            return InventoryApprovalStateEnum.getDisplayNameByIndex(this.approvalStatus);
        } else {
            return "";
        }
    }

    public String getIsAppearanceDamageStr() {
        if (isAppearanceDamage == null) {
            return null;
        }
        if(isAppearanceDamage.equals("1")){
            return "是";
        }else{
            return "否";
        }
    }

    public String getInventoryStr() {
        if (this.inventory == null) {
            return "";
        }
        if(inventory){
            return "已盘点";
        }else{
            return "未盘点";
        }
    }


    public String getManualAddStr() {
        if (this.manualAdd == null) {
            return "";
        }
        if(manualAdd){
            return "是";
        }else{
            return "否";
        }
    }

}
