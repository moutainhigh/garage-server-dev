package com.yixin.garage.service.garage.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yixin.common.exception.BzException;
import com.yixin.garage.dao.garage.SgContactToGarageTempMapper;
import com.yixin.garage.dto.api.forLoan.SgPersonDTO;
import com.yixin.garage.entity.garage.SgContactToGarageTemp;
import com.yixin.garage.service.garage.ISgContactToGarageTempService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author libochen
 * @since 2019-08-05
 */
@Service
public class SgContactToGarageTempServiceImpl extends ServiceImpl<SgContactToGarageTempMapper, SgContactToGarageTemp> implements ISgContactToGarageTempService {


    private final static Logger logger=LoggerFactory.getLogger(SgContactToGarageTempServiceImpl.class);



    @Override
    public List<String> selectGarageId(String currentUser) {
        List<String> result = new ArrayList<String>();
        try{
            QueryWrapper<SgContactToGarageTemp> queryWrapper = new QueryWrapper();
            queryWrapper.eq("contact", currentUser);
            queryWrapper.eq("IS_DELETED", 0);
            List<SgContactToGarageTemp> list = list(queryWrapper);

            if(list.size()>0){
                for (int i = 0; i <list.size() ; i++) {
                    result.add(list.get(i).getGarageInfoId());
                }
            }
        }catch (Exception e) {
            logger.error("查询可用车库异常：{}", e.getMessage(), e);
            throw new BzException(e.getMessage());
        }
        return result;
    }




    @Override
    public List<SgPersonDTO> selectContact(String garageId) {
        List<SgPersonDTO> result = new ArrayList<SgPersonDTO>();
        try{
            QueryWrapper<SgContactToGarageTemp> queryWrapper = new QueryWrapper();
            queryWrapper.eq("garage_info_id", garageId);
            queryWrapper.eq("IS_DELETED", 0);
            List<SgContactToGarageTemp> list = list(queryWrapper);

            if (null != list && list().size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    SgPersonDTO dto = new SgPersonDTO();
                    dto.setName(list.get(i).getContact());
                    dto.setPhone(list.get(i).getContactTel());
                    result.add(dto);
                }
            }
        }catch (Exception e) {
            logger.error("查询可用车库异常：{}", e.getMessage(), e);
            throw new BzException(e.getMessage());
        }
        return result;
    }









}
