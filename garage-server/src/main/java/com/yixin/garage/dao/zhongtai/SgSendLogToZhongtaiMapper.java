package com.yixin.garage.dao.zhongtai;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.garage.dto.zhongtai.SgGarageSendInfoDTO;
import com.yixin.garage.entity.zhongtai.SgSendLogToZhongtai;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhouhang
 * @since 2019-08-29
 */
public interface SgSendLogToZhongtaiMapper extends BaseMapper<SgSendLogToZhongtai> {

    /**
     * @Title: pageQueryLog
     * @Description: 分页查询推送中台日志信息列表
     * @param:
     * @author YixinCapital -- zhouhang
     * @date 2019/8/29 15:08
     */
    IPage<SgGarageSendInfoDTO> pageQueryLog(@Param("page") Page<SgSendLogToZhongtai> page, @Param("dto") SgGarageSendInfoDTO dto);
}
