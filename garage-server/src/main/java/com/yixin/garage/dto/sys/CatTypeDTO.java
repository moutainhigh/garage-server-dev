package com.yixin.garage.dto.sys;

import com.yixin.common.utils.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

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
public class CatTypeDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 主品牌ID
     */
    private String masterBrandId;

    /**
     * 主品牌名称
     */
    private String masterBrandName;

    /**
     * 车辆品牌ID
     */
    private String makeId;

    /**
     * 车辆品牌名称
     */
    private String makeName;

    /**
     * 车系ID
     */
    private String modelId;

    /**
     * 车系名称
     */
    private String modelName;

    /**
     * 车型ID
     */
    private String entityId;

    /**
     * 车型名称
     */
    private String name;

    /**
     * 新车指导价
     */
    private String nowmsrp;

    /**
     * 年款
     */
    private String year;

    /**
     * 排量（ML 单位）
     */
    private String exhaust;

    /**
     * 排量（L 单位）
     */
    private String exhaustFloat;

    /**
     * 座位数
     */
    private String seatNum;

    /**
     * 座位数（集合）
     */
    private List<Integer> seatNumArr;
}
