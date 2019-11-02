package com.yixin.garage.dao.zhongtai;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.garage.dto.zhongtai.SgGarageSendInfoDTO;
import com.yixin.garage.entity.zhongtai.SgSendInfoToZhongtai;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zhouhang
 * @since 2019-08-29
 */
public interface SgSendInfoToZhongtaiMapper extends BaseMapper<SgSendInfoToZhongtai> {

    IPage<SgGarageSendInfoDTO> pageQuery(@Param("page") Page<SgSendInfoToZhongtai> page, @Param("dto") SgGarageSendInfoDTO dto);

    List<SgSendInfoToZhongtai> pageQueryList(@Param("type") Integer type);
}
