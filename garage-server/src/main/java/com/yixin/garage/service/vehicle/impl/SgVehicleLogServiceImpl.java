package com.yixin.garage.service.vehicle.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yixin.common.exception.BzException;
import com.yixin.garage.core.common.factory.PageFactory;
import com.yixin.garage.dao.vehicle.SgVehicleLogMapper;
import com.yixin.garage.dto.vehicle.SgVehicleLogDTO;
import com.yixin.garage.entity.vehicle.SgVehicleLog;
import com.yixin.garage.service.vehicle.ISgVehicleLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liyaqing
 * @since 2019-08-05
 */
@Service
public class SgVehicleLogServiceImpl extends ServiceImpl<SgVehicleLogMapper, SgVehicleLog> implements ISgVehicleLogService {

    private final static Logger logger = LoggerFactory.getLogger(SgVehicleLogServiceImpl.class);

    @Autowired
    private SgVehicleLogMapper sgVehicleLogMapper;

    @Override
    public IPage<SgVehicleLogDTO> pageQueryLog(SgVehicleLogDTO sgVehicleLogDTO) throws BzException {
        logger.info("分页查询日志入参：{}", JSONObject.toJSON(sgVehicleLogDTO));
        Page<SgVehicleLogDTO> dtoPage = null;
        try{
            Page<SgVehicleLog> page = new PageFactory<SgVehicleLog>().defaultPage(sgVehicleLogDTO);
            IPage<SgVehicleLogDTO> pageList = sgVehicleLogMapper.pageQueryLog(page, sgVehicleLogDTO);
            logger.info("=====共查询[" + pageList.getTotal()+ "]条数据");
            dtoPage = new Page<SgVehicleLogDTO>(pageList.getCurrent(), pageList.getSize(), pageList.getTotal()).setRecords(pageList.getRecords());
        }catch (Exception e){
            logger.error("pageQuery() failed：", e);
            throw new BzException("pageQuery() failed:",e);
        }
        return dtoPage;
    }
}
