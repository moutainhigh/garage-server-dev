package com.yixin.garage.service.api.impl;

import com.yixin.common.exception.BzException;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.dto.sys.SysUserDTO;
import com.yixin.garage.enums.RoleEnum;
import com.yixin.garage.service.api.SgAPPGarageForPankuService;
import com.yixin.garage.service.sys.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author YixinCapital -- liyaqing
 * @description: TODO
 * @date 2019/10/1213:45
 */
@Service
@Transactional
public class SgAPPGarageForPankuServiceImpl implements SgAPPGarageForPankuService {

    private Logger logger = LoggerFactory.getLogger(SgAPPGarageForPankuServiceImpl.class);

    @Autowired
    private IUserService userService;

    /**
     * @Title: queryPageByUserName
     * @Description: 根据域账号查看此人能够看到的页面
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/10/12 14:11
     */
    @Override
    public InvokeResult<List<String>> queryPageByUserName(String userName) throws BzException {
        logger.info("根据域账号查看此人能够看到的页面- userName：{}" + userName);
        InvokeResult<List<String>> result = new InvokeResult<List<String>>();
        Set<String> set = new HashSet<>();
        try{

            //判断是否有车库一览权限
            List<SysUserDTO> checkList = userService.getUserInfoByRoleName(RoleEnum.GARAGE_CHECKLIST_ROLE.getRoleName());
            if(checkList != null && !checkList.isEmpty()){
                logger.info("===============车库一览角色：[" + checkList + "]=================");
                for(SysUserDTO dto : checkList){
                    if(dto.getDomainName().equals(userName)){
                        set.add("a"); //a：有车库一览页面查看权限
                    }
                }
            }

            //判断是否有库管管理员权限
            List<SysUserDTO> manageNameList = userService.getUserInfoByRoleName(RoleEnum.GARAGE_MANAGE_ROLE.getRoleName());
            if(manageNameList != null && !manageNameList.isEmpty()){
                logger.info("===============库管角色：[" + manageNameList + "]=================");
                for(SysUserDTO dto : manageNameList){
                    if(dto.getDomainName().equals(userName)){
                        set.add("b"); //b：有盘库页面查看权限
                    }
                }
            }

            //判断是否有资产经理权限
            List<SysUserDTO> commissionerList = userService.getUserInfoByRoleName(RoleEnum.ASSETS_COMMISSIONER_ROLE.getRoleName());
            //处置专员
            List<SysUserDTO> disposeList = userService.getUserInfoByRoleName(RoleEnum.DISPOSE_COMMISSIONER_ROLE.getRoleName());
            if(commissionerList != null && !commissionerList.isEmpty()){
                logger.info("===============资产经理角色：[" + commissionerList + "]=================");
                for(SysUserDTO dto : commissionerList){
                    if(dto.getDomainName().equals(userName)){
                        set.add("c"); //c：有库管汇总页面查看权限
                    }
                }
            }
            if(disposeList != null && !disposeList.isEmpty()){
                logger.info("===============处置专员角色：[" + disposeList + "]=================");
                for(SysUserDTO dto : disposeList){
                    if(dto.getDomainName().equals(userName)){
                        set.add("c"); //c：有库管汇总页面查看权限
                    }
                }
            }
            List<String> list = new ArrayList<>(set);
            result.setData(list);
            result.success();
        }catch(Exception e){
            logger.error("queryPageByUserName() failed: " + e.getMessage());
            throw new BzException(e.getMessage());
        }
        return result;
    }
}
