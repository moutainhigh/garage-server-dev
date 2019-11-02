package com.yixin.garage.service.inventory.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.common.exception.BzException;
import com.yixin.common.utils.DateUitls;
import com.yixin.garage.auth.CurrentUser;
import com.yixin.garage.core.common.factory.PageFactory;
import com.yixin.garage.dto.inventory.SgInventoryBillDTO;
import com.yixin.garage.dto.inventory.SgInventoryLogDTO;
import com.yixin.garage.entity.inventory.SgInventoryApprovalBill;
import com.yixin.garage.entity.inventory.SgInventoryLog;
import com.yixin.garage.dao.inventory.SgInventoryLogMapper;
import com.yixin.garage.enums.garage.inventory.InventoryBillStateEnum;
import com.yixin.garage.service.inventory.ISgInventoryLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author libochen
 * @since 2019-10-11
 */
@Service
@Transactional
public class SgInventoryLogServiceImpl extends ServiceImpl<SgInventoryLogMapper, SgInventoryLog> implements ISgInventoryLogService {

    private Logger logger = LoggerFactory.getLogger(SgInventoryLogServiceImpl.class);


    @Override
    public void createInventoryLog(SgInventoryApprovalBill bill, SgInventoryBillDTO dto, String operationNode, String remark, String stat) {

        SgInventoryLog inventoryLog = new SgInventoryLog();
        //CurrentUser.getUsername()域账号
        inventoryLog.setInventoryApprovalBillId(bill.getId());
        inventoryLog.setOperationNode(operationNode);
        inventoryLog.setStat(stat);
        inventoryLog.setRemark(remark);//备注
        inventoryLog.setOperationTime(new Date());
        if (null != dto.getUserName() && !dto.getUserName().equals("")) {
            inventoryLog.setCreatorName(dto.getCnName());//登录人 中文名
            inventoryLog.setCreatorId(dto.getUserName());//域账号
        } else {
            inventoryLog.setCreatorName(CurrentUser.getCnName());//登录人 中文名
            inventoryLog.setCreatorId(CurrentUser.getUsername());//域账号
        }
        inventoryLog.insert();
    }

    @Override
    public IPage<SgInventoryLog> pageQueryLog(SgInventoryLogDTO dto) throws BzException {
        logger.info("分页查询车库入参：{}", JSONObject.toJSON(dto));

        Page<SgInventoryLog> pageParam = new PageFactory<SgInventoryLog>().defaultPage(dto);

        QueryWrapper<SgInventoryLog> sgInventoryLogQueryWrapper = new QueryWrapper();
        sgInventoryLogQueryWrapper.eq("inventory_approval_bill_id", dto.getId());
        sgInventoryLogQueryWrapper.orderByDesc("CREATE_TIME");
        return this.baseMapper.selectPage(pageParam, sgInventoryLogQueryWrapper);

//        Page<SgGarageInfo> pageParam = new PageFactory<SgGarageInfo>().defaultPage(dto);
//        //权限控制
//        //获取当前登录人的角色集合，判断是否拥有总部权限
//        Boolean hasRoles = CurrentUser.hasRole(RoleEnum.GARAGE_MANAGE_ROLE);
//        logger.info("当前登录人为：" + CurrentUser.getCnName()+"是否存在库管权限："+ hasRoles);
////        Boolean hasRoles = false;
//        //如果当前登录人角色为 车库管理员，则只能看到对应车库的车
//        if (hasRoles) {
//            dto.setCreatorName(CurrentUser.getCnName());
//            logger.info("当前登录人为：" + CurrentUser.getCnName());
//        }
//        IPage<SgGarageInfoDTO> pageProject = this.baseMapper.pageQuery(pageParam, dto,hasRoles);
//        //List<SecurityGarageInfoDTO> resultList = pageProject.getRecords();
//        return new Page<SgGarageInfoDTO>(pageProject.getCurrent(),pageProject.getSize(),pageProject.getTotal()).setRecords(pageProject.getRecords());
    }


}
