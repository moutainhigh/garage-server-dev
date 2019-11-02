package com.yixin.garage.controller.sys;


import com.alibaba.fastjson.JSONObject;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.auth.CurrentUser;
import com.yixin.garage.dto.sys.UserDeptsDTO;
import com.yixin.garage.enums.RoleEnum;
import com.yixin.garage.util.ResultUtil;
import com.yixin.oa.web.dto.UserDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 判断当前登录用户权限 前端控制器
 * </p>
 *
 * @author libochen
 * @since 2019-08-28
 */
@RestController
@RequestMapping("/hasRole")
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping("/getAuthorize")
    @ResponseBody
    public InvokeResult<String> getAuthorize() {
        InvokeResult<String> result = new InvokeResult<String>();
        logger.info("查询当前登录用户是否存在权限{}");
        if(CurrentUser.hasRole(RoleEnum.GARAGE_MANAGE_ROLE)){
            return ResultUtil.success("garageManageRole");
        }
        else if(CurrentUser.hasRole(RoleEnum.DISPOSE_COMMISSIONER_ROLE)){
            return ResultUtil.success("disposeCommissionerRoleId");
        }
        else if(CurrentUser.hasRole(RoleEnum.ASSETS_COMMISSIONER_ROLE)){
            return ResultUtil.success("assetsCommissionerRoleId");
        }else{
            result.failure("当前用户没有操作本系统权限！");
        }
        return result;
    }





}
