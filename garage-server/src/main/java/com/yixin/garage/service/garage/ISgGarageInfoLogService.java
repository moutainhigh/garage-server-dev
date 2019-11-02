package com.yixin.garage.service.garage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yixin.garage.dto.garage.SgGarageInfoDTO;
import com.yixin.garage.dto.garage.YxGarageInfoLogDTO;
import com.yixin.garage.entity.garage.SgGarageInfo;
import com.yixin.garage.entity.garage.SgGarageInfoLog;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author libochen
 * @since 2019-08-01
 */
public interface ISgGarageInfoLogService extends IService<SgGarageInfoLog> {

    void createGarageLog(SgGarageInfo sgGarageInfo, SgGarageInfoDTO dto, String event);
    

    Page<YxGarageInfoLogDTO> pageQueryLog(YxGarageInfoLogDTO dto);
}
