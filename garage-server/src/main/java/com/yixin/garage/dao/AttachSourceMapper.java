package com.yixin.garage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yixin.garage.dto.sys.AttachSourceDTO;
import com.yixin.garage.entity.AttachSource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lizhongxin
 * @since 2019-01-07
 */
public interface AttachSourceMapper extends BaseMapper<AttachSource> {

    /**
     * @Title: queryByVehId
     * @Description: 根据bussId查询附件信息
     * @param:
     * @author YixinCapital -- liyaqing
     * @date 2019/8/19 14:34
     */
    List<AttachSourceDTO> queryAtts (@Param("bussId") String bussId,@Param("type") String type);


}
