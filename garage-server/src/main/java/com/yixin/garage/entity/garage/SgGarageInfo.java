package com.yixin.garage.entity.garage;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yixin.garage.core.base.BaseEntity;

/**
 * <p>
 * 
 * </p>
 *
 * @author libochen
 * @since 2019-08-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SgGarageInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 城市
     */
    private String city;

    /**
     * 城市名称
     */
    @TableField("cityStr")
    private String cityStr;

    /**
     * 负责人姓名
     */
    private String contact;

    /**
     * 联系人电话
     */
    private String contactTele;

    /**
     * 区县
     */
    private String country;

    /**
     * 区县名称
     */
    @TableField("countryStr")
    private String countryStr;

    /**
     * 车库地址
     */
    private String garageAddresss;

    /**
     * 车库名称
     */
    private String garageName;

    /**
     * 车库编号
     */
    private String garageNum;

    /**
     * 车库状态
     */
    private Integer garageStatus;

    /**
     * 已使用车位数
     */
    private Integer parkedNum;

    /**
     * 总车位数
     */
    private Integer parkingNum;

    /**
     * 省份
     */
    private String province;

    /**
     * 省份名称
     */
    @TableField("provinceStr")
    private String provinceStr;

    /**
     * 备注
     */
    private String remark;

    /**
     * 租期开始日期
     */
    private Date tenancyBeginDate;

    /**
     * 租期结束日期
     */
    private Date tenancyEndDate;

    /**
     * 经营属性
     */
    private Integer operateAttr;

    /**
     * 联系人身份证号
     */
    @TableField("contact_cardID")
    private String contactCardid;

    private Integer tenancySign;

    private String deliveryTele;

//    private Integer largeParking;

    @TableField("parkingCost_day")
    private BigDecimal parkingcostDay;

    @TableField("parkingCost_hour")
    private BigDecimal parkingcostHour;

    @TableField("parkingCost_month")
    private BigDecimal parkingcostMonth;

//    private Integer smallParking;

//    private Integer standardParking;

    @TableField("yxAttachment")
    private String yxAttachment;

    @TableField("invoice_Type")
    private Integer invoiceType;

}
