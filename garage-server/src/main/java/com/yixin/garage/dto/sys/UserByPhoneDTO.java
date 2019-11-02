package com.yixin.garage.dto.sys;

import com.yixin.common.utils.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author lizhongxin
 * @since 2018-12-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserByPhoneDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    private String organizationId;
    
    private String username;

    private String password;

    private String attendanceType;

    private String employeeNumber;

    private String departmentName;

    private String business_department_id;

    private String business_department_name;

    private String cnName;

}
