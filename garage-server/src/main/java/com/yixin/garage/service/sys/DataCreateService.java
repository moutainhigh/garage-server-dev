package com.yixin.garage.service.sys;

import com.yixin.common.exception.BzException;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.dto.sys.CityInfoDTO;
import com.yixin.garage.dto.sys.RegionInfoDTO;
import com.yixin.garage.dto.sys.UploadFileDTO;

import java.util.List;

/**
 * 初始化数据接口
 */
public interface DataCreateService {



    /**
    * dataCreateService(初始化数据接口)
    * @param fileDTO
    * @return
    * com.yixin.common.utils.InvokeResult<java.lang.String>
    * @author: YixinCapital -- libochen
    * 2019/9/17 19:04
    */
    InvokeResult<String> dataCreateService(UploadFileDTO fileDTO) throws BzException;

}
