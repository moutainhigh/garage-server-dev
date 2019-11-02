package com.yixin.garage.service.api;

import com.yixin.common.exception.BzException;
import com.yixin.common.utils.InvokeResult;

import java.util.List;

/**
 * @author YixinCapital -- liyaqing
 * @description: 盘库APP相关接口
 * @date 2019/10/1211:47
 */
public interface SgAPPGarageForPankuService {

    /**
     * @Title: queryPageByUserName
     * @Description: 根据域账号查看此人能够看到的页面
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/10/12 13:43
     */
    InvokeResult<List<String>> queryPageByUserName (String userName) throws BzException;
}
