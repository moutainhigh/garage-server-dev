package com.yixin.garage.service.impl.sys;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.dto.sys.UserByPhoneDTO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yixin.common.exception.BzException;
import com.yixin.garage.core.base.BaseAssembler;
import com.yixin.garage.dto.sys.SysUserDTO;
import com.yixin.garage.enums.RoleEnum;
import com.yixin.garage.service.sys.IUserService;
import com.yixin.garage.util.ConfigUtil;
import com.yixin.garage.util.RestUtil;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author lizhongxin
 * @since 2019-01-02
 */
@Service
public class UserServiceImpl implements IUserService {

    private static final  Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    /*@Override
    public List<User> queryUserByRoleKey(String roleKey) {
        return baseMapper.queryUserByRoleKey(roleKey);
    }*/
    @Value("${usc.getUsersInfoByRoleId}")
    private String getUsersInfoByRoleIdURL;
    
    @Value("${usc.nuscWebApi}")
    private String nuscWebApi;

    @Value("${usc.getUserByPhoneNO}")
    private String getUserByPhoneNOURL;

    @Autowired
    public IUserService userService;
    
//    @Autowired
//    public IUserDeptService userDeptService;
    
    @Override
    public List<SysUserDTO> getUserInfoByRoleName(String roleName){
        try {
            logger.info("通过角色名获取用户信息接口getUserInfoByRoleName,入参:{}",roleName);
            if(RoleEnum.contains(roleName)){
                String roleId = ConfigUtil.getValue(roleName);
                String resStr = RestUtil.sendRequest(getUsersInfoByRoleIdURL+roleId, new HashMap());
                logger.info("根据角色名{}，角色id：{}，查询角色下用户返回结果字符串：{}",roleName,roleId,resStr);
                JSONObject jsonObj = JSONObject.parseObject(resStr);
                if(jsonObj==null){
                    return null;
                }
                if(!jsonObj.getBooleanValue("success")){
                    throw new BzException("查询角色下用户出错，"+jsonObj.getString("errorMessage"));
                }
                if(CollectionUtils.isEmpty(jsonObj.getJSONArray("data"))){
                    return null;
                }
                int len = jsonObj.getJSONArray("data").size();
                List<SysUserDTO> resList = new ArrayList<>();
                for(int i=0;i<len;i++){
                    JSONObject json = jsonObj.getJSONArray("data").getJSONObject(i);
                    SysUserDTO user = new SysUserDTO();
                    BaseAssembler.mapObj(json, user);
                    resList.add(user);
                }
                return resList;
            }else{
                throw new BzException("角色名不在角色字典内，无法查询角色下用户，当前角色名："+roleName);
            }
        } catch (Exception e) {
            if(e instanceof BzException){
                throw e;
            }
            logger.info("根据角色查询用户出错，信息为：{}",e);
            throw new BzException("根据角色查询用户发生异常！");
        }
    }


    @Override
    public UserByPhoneDTO getUserByPhoneNO(String phoneNo){
        try {
            logger.info("通过手机号获取用户信息接口getUserByPhoneNO,入参:{}",phoneNo);
            InvokeResult<UserByPhoneDTO> result = new InvokeResult<UserByPhoneDTO>();
            if (phoneNo != null && !phoneNo.equals("")) {
                String resStr = RestUtil.sendRequest(getUserByPhoneNOURL + phoneNo, new HashMap());
                logger.info("根据手机号，查询下用户返回结果字符串：{}", resStr);
//                JSONObject jsonObj = JSONObject.parseObject(resStr);
                result = JSON.parseObject(resStr, new TypeReference<InvokeResult<UserByPhoneDTO>>(){});

                if (result == null) {
                    return null;
                }
                if (!result.isSuccess()) {
                    throw new BzException("查询手机号下用户出错，" + result.getErrorMessage());
                }

                UserByPhoneDTO resultDto = new UserByPhoneDTO();
                resultDto = (UserByPhoneDTO) result.getData();
                return resultDto;
            } else {
                throw new BzException("手机号入参为空，请检查！！！");
            }
        } catch (Exception e) {
            if(e instanceof BzException){
                throw e;
            }
            logger.info("根据手机号查询用户出错，信息为：{}",e);
            throw new BzException("根据手机号查询用户发生异常！");
        }
    }











//    @Override
//    public User getUserByDomainAccount(String domainAccount) {
//        return this.getOne(new QueryWrapper<User>().eq("domain_account",domainAccount));
//    }
//
//    @Override
//    public String getUserNameByRoleName(String roleName) {
//        StringBuilder nextUserNames = new StringBuilder("");
//        // 获取下一操作人需要的权限角色名称对应的用户列表
//        List<SysUserDTO> userList = userService.getUserInfoByRoleName(roleName);
//        if (userList != null) {
//            for (SysUserDTO one : userList) {
//                if (StringUtils.isEmpty(one.getDomainName())) {
//                    continue;
//                }
//                if (nextUserNames.length() == 0) {
//                    nextUserNames.append(one.getCnName());
//                } else {
//                    nextUserNames.append(",").append(one.getCnName());
//                }
//            }
//        }
//
//        return nextUserNames.toString();
//    }

//    @Override
//    public List<Integer> getDeptIdByDomainAccount(String domainAccount) {
//        Map<String, Object> columnMap = new HashMap<>();
//        Object domain_account = columnMap.put("domain_account", domainAccount);
//        Collection<UserDept> list = userDeptService.listByMap(columnMap);
//        List<Integer> reList = new ArrayList<>();
//        for (UserDept ud : list) {
//            reList.add(ud.getDeptId());
//        }
//        return reList;
//    }
}
