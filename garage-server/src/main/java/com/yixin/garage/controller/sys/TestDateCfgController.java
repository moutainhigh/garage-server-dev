package com.yixin.garage.controller.sys;


import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.util.DateUtil;
import com.yixin.garage.util.ResultUtil;

@RestController
@RequestMapping("/garageDateCfg")
public class TestDateCfgController {
    private final static Logger logger = LoggerFactory.getLogger(TestDateCfgController.class);
    
//    @RequestMapping("/getgarageDate")
//    @ResponseBody
//    public InvokeResult<String> getTestDate() {
//        return ResultUtil.success(DateUtil.dateToString(DateUtil.getgarageDate(),"yyyy/MM"));
//    }
}
