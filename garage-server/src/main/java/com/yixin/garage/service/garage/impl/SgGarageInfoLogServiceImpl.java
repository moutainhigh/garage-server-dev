package com.yixin.garage.service.garage.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yixin.garage.auth.CurrentUser;
import com.yixin.garage.core.common.factory.PageFactory;
import com.yixin.garage.dao.garage.SgGarageInfoLogMapper;
import com.yixin.garage.dto.garage.SgGarageInfoDTO;
import com.yixin.garage.dto.garage.YxGarageInfoLogDTO;
import com.yixin.garage.entity.garage.SgGarageInfo;
import com.yixin.garage.entity.garage.SgGarageInfoLog;
import com.yixin.garage.service.garage.ISgGarageInfoLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author libochen
 * @since 2019-08-01
 */
@Service
public class SgGarageInfoLogServiceImpl extends ServiceImpl<SgGarageInfoLogMapper, SgGarageInfoLog> implements ISgGarageInfoLogService {

    private final static Logger logger = LoggerFactory.getLogger(SgGarageInfoLogServiceImpl.class);


    //车库日志
    @Override
    public void createGarageLog(SgGarageInfo sgGarageInfo, SgGarageInfoDTO dto, String event) {
        SgGarageInfoLog sgGarageInfoLog = new SgGarageInfoLog();
        sgGarageInfoLog.setEvent(event);
        sgGarageInfoLog.setRemark(sgGarageInfo.getRemark());
        sgGarageInfoLog.setSgGarageInfoId(sgGarageInfo.getId());
        sgGarageInfoLog.setCreateTime(new Date());// 操作时间
        sgGarageInfoLog.setCreatorName(CurrentUser.getCnName());// 操作人
        sgGarageInfoLog.setCreatorId(CurrentUser.getUserId());
        this.save(sgGarageInfoLog);
    }

    @Override
    public Page<YxGarageInfoLogDTO> pageQueryLog(YxGarageInfoLogDTO dto) {

        logger.info("分页查询车库入参：{}", JSONObject.toJSON(dto));
        Page<SgGarageInfoLog> pageParam = new PageFactory<SgGarageInfoLog>().defaultPage(dto);
        IPage<YxGarageInfoLogDTO> pageProject = this.baseMapper.pageQueryLog(pageParam, dto);

        return new Page<YxGarageInfoLogDTO>(pageProject.getCurrent(),
                pageProject.getSize(),pageProject.getTotal()).setRecords(pageProject.getRecords());

    }

//    @Override
//    public Page<SecurityGarageInfoDTO> pageQuery(SecurityGarageInfoDTO dto) throws BzException {
//        logger.info("分页查询车库入参：{}", JSONObject.toJSON(dto));
//        Page<SgGarageInfoLog> pageParam = new PageFactory<SgGarageInfoLog>().defaultPage(dto);
//        //权限控制
//        //获取当前登录人的角色集合，判断是否拥有总部权限
//        Boolean hasRoles = CurrentUser.hasRole(RoleEnum.FINANCING_FUND_DIRECTOR);
//
//        //如果当前登录人角色为 车库管理员，则只能看到对应车库的车
//        if (hasRoles) {
//            dto.setCreatorName(CurrentUser.getUsername());
//        }
//        IPage<YxGarageInfoLogDTO> pageProject = this.baseMapper.pageQuery(pageParam, dto);
//        //List<SecurityGarageInfoDTO> resultList = pageProject.getRecords();
//        return new Page<SecurityGarageInfoDTO>(pageProject.getCurrent(),pageProject.getSize(),pageProject.getTotal()).setRecords(pageProject.getRecords());
//    }


}
