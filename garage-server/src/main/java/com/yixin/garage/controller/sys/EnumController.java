package com.yixin.garage.controller.sys;

import com.yixin.common.utils.InvokeResult;
import com.yixin.logsys.application.SystemControllerLog;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
* 获取枚举值
* Package : com.yixin.rentcar.controller.common
*
* @author: YixinCapital -- libochen
* 2019/8/21 16:26
*/
@RestController
@Repository
@RequestMapping("/enum")
public class EnumController {


    /**
     * 获取枚举值
     *
     * @param enumName
     * @return InvokeResult<List<EnumDTO>>
     * @throws Exception
     * @author YixinCapital -- tangzilong
     *	       2017年5月2日 上午10:52:41
     */

    @ResponseBody
    @RequestMapping(value = "/getEnumDataList")
    @SystemControllerLog(description="获取枚举值")
    public InvokeResult<List<EnumDTO>> getEnumDataList(String enumName) throws Exception {

        InvokeResult<List<EnumDTO>> result = new InvokeResult<List<EnumDTO>>();
        List<EnumDTO> enumDTOList = new ArrayList<EnumDTO>();
        Class<?> clazz = Class.forName("com.yixin.garage.enums." + enumName);
        Method method = clazz.getDeclaredMethod("getDataList", null);
        List<Enum<?>> lists = (List<Enum<?>>) method.invoke(clazz, null);
        for (Enum<?> enum1 : lists) {
            Method methodName = enum1.getClass().getDeclaredMethod("getName", null);
            Method methodValue = enum1.getClass().getDeclaredMethod("getValue", null);
            Method methodSelected = enum1.getClass().getDeclaredMethod("isSelected", null);
            enumDTOList.add(new EnumDTO(methodName.invoke(enum1, null), methodValue.invoke(enum1, null), methodSelected
                    .invoke(enum1, null)));
        }
        result.setData(enumDTOList);
        result.success();
        return result;
    }
}
