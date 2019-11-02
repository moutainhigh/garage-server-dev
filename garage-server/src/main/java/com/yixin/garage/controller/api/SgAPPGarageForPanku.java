package com.yixin.garage.controller.api;

import com.yixin.common.exception.BzException;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.service.api.SgAPPGarageForPankuService;
import com.yixin.garage.util.ResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author YixinCapital -- liyaqing
 * @description: TODO
 * @date 2019/10/1213:56
 */

@RestController
@RequestMapping("/api/sgAPPGarageForPanku")
public class SgAPPGarageForPanku {

    private final static Logger logger = LoggerFactory.getLogger(SgAPPGarageForPanku.class);

    @Autowired
    private SgAPPGarageForPankuService sgAPPGarageForPankuService;

    /**
     * @Title: queryPageByUserName
     * @Description: 根据域账号查看此人能够看到的页面
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/10/12 14:05
     */
    @RequestMapping("/queryPageByUserName")
    @ResponseBody
    @Validated
    public InvokeResult<List<String>> queryPageByUserName(@RequestParam("userName") String userName){
        logger.info("根据域账号查看此人能够看到的页面: userName：{}" + userName);
        InvokeResult<List<String>> result = new InvokeResult<List<String>>();
        try {
            if(StringUtils.isNotBlank(userName)){
                result = sgAPPGarageForPankuService.queryPageByUserName(userName);
            }else{
                logger.info("域账号[userName]不允许为空");
                throw new BzException("域账号[userName]不允许为空");
            }
        }catch (Exception e){
            logger.error("queryPageByUserName() 查询失败：[{}]", e.getMessage(), e);
            return ResultUtil.error(e.getMessage());
        }
        return result;
    }


}
