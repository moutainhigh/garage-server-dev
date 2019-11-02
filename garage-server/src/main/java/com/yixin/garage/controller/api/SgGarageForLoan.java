package com.yixin.garage.controller.api;


import com.alibaba.fastjson.JSON;
import com.yixin.common.exception.BzException;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.dto.api.forLoan.SgGarageForLoanGarageInDTO;
import com.yixin.garage.dto.api.forLoan.SgGarageForLoanGarageOutDTO;
import com.yixin.garage.dto.api.forLoan.SgQueryVehicleIfoStatDTO;
import com.yixin.garage.service.api.SgGarageForLoanService;
import com.yixin.garage.util.BeanUtil;
import com.yixin.garage.util.JacksonUtil;
import com.yixin.garage.util.ResultUtil;
import com.yixin.garage.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author libochen
 * @since 2019-08-01
 */
@RestController
@RequestMapping("/api/sgGarageForLoan")
public class SgGarageForLoan {

    private final static Logger logger=LoggerFactory.getLogger(SgGarageForLoan.class);

//    @Autowired
//    ISgGarageInfoLogService iSgGarageInfoLogService;

    @Autowired
    SgGarageForLoanService sgGarageForLoanService;


//    @Autowired
//    private RabbitTemplate rabbitTemplate;

    /**
    * inGarage(融后入库指令对接接口)
    * @param dto
    * @return
    * com.yixin.common.utils.Page<com.yixin.garage.dto.garage.YxGarageInfoLogDTO>
    * @author: YixinCapital -- libochen
    * 2019/8/7 17:37
    */
    @RequestMapping("/inGarage")
    @ResponseBody
    @Validated
    public InvokeResult<String> inGarage(@RequestBody SgGarageForLoanGarageInDTO dto){
        BeanUtil.setEmptyStrFields2Null(dto);
        logger.info("融后入库指令对接接口，入参参数为：[{}]",JacksonUtil.fromObjectToJson(dto));
        InvokeResult<String> result = new InvokeResult<String>();
        try {

            InvokeResult validate = ValidatorUtil.validate(dto);
            if(validate.isSuccess() ){//必填项完整
                result = sgGarageForLoanService.inGarage(dto);
            }else{
                logger.info("入库指令对接接口存在空值，空值字段信息为：{}",validate.getErrorMessage());
                throw new BzException("入库指令对接接口存在空值：" + validate.getErrorMessage());
            }

        }catch (Exception e){
            logger.error("融后入库指令接口异常：[{}]", e.getMessage(), e);
            return ResultUtil.error(e.getMessage());
        }
        return result;
    }


    /**
    * outGarage(接收中瑞出库指令接口)
    * @param dto
    * @return
    * com.yixin.common.utils.InvokeResult<java.lang.String>
    * @author: YixinCapital -- libochen
    * 2019/8/20 14:09
    */
    @RequestMapping("/outGarage")
    @ResponseBody
    public InvokeResult<String> outGarage(@RequestBody SgGarageForLoanGarageOutDTO dto){
        InvokeResult<String> result = new InvokeResult<String>();
        logger.info("融后出库指令对接接口，入参参数为：[{}]",JacksonUtil.fromObjectToJson(dto));
        try {
            result = sgGarageForLoanService.outGarage(dto);

        }catch (Exception e){
            logger.error("出库指令接口：[{}]", e.getMessage(), e);
            return ResultUtil.error(e.getMessage());
        }
        return result;
    }




    /**
    * queryVehicleStat(中瑞查询车辆状态接口)
    * @param dto
    * @return
    * com.yixin.common.utils.InvokeResult<java.lang.String>
    * @author: YixinCapital -- libochen
    * 2019/8/20 14:09
    */
    @RequestMapping("/queryVehicleStat")
    @ResponseBody
    public InvokeResult<SgQueryVehicleIfoStatDTO> queryVehicleStat(@RequestBody SgQueryVehicleIfoStatDTO dto){
        BeanUtil.setEmptyStrFields2Null(dto);
        InvokeResult<SgQueryVehicleIfoStatDTO> result = new InvokeResult<SgQueryVehicleIfoStatDTO>();
        logger.info("中瑞查询车辆状态接口，入参参数为：[{}]",JacksonUtil.fromObjectToJson(dto));
        try {
            result = sgGarageForLoanService.queryVehicleStat(dto);

        }catch (Exception e){
            logger.error("车库状态查询异常：[{}]", e.getMessage(), e);
            return ResultUtil.error(e.getMessage());
        }
        return result;
    }



    @RequestMapping("/mq")
    @ResponseBody
    public InvokeResult<String> Formq(@RequestBody SgGarageForLoanGarageOutDTO dto){
        InvokeResult<String> result = new InvokeResult<String>();
        logger.info("中瑞查询车辆状态接口，入参参数为：[{}]",JacksonUtil.fromObjectToJson(dto));
        try {
//            rabbitTemplate.convertAndSend("loanGetGarageInfoRoutingKey", JSON.toJSONString(dto));

//            rabbitTemplate.convertAndSend("loanGetGarageInfoRoutingKey", JSON.toJSONString(dto));
//            rabbitTemplate.setExchange("sg.loan.garage");
//            rabbitTemplate.setRoutingKey("loanGetGarageInfoRoutingKey");
//            rabbitTemplate.convertAndSend("sg.loan.garage", "loanGetGarageInfoRoutingKey","123123");

        }catch (Exception e){
            logger.error("车库状态查询异常：[{}]", e.getMessage(), e);
            return ResultUtil.error(e.getMessage());
        }
        return result;
    }


}
