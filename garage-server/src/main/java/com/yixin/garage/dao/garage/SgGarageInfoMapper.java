package com.yixin.garage.dao.garage;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.garage.dto.garage.SgGarageInfoDTO;
import com.yixin.garage.entity.garage.SgGarageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author libochen
 * @since 2019-08-01
 */
public interface  SgGarageInfoMapper extends BaseMapper<SgGarageInfo> {

    /**
     * 分页查询车库列表
     */
    IPage<SgGarageInfoDTO> pageQuery(@Param("page") Page<SgGarageInfo> page, @Param("queryDto") SgGarageInfoDTO queryDto,
                                     @Param("hasRoles") Boolean hasRoles);
    String createGarageNum(@Param("queryDto") SgGarageInfoDTO securityGarageInfoDTO);

    List<SgGarageInfoDTO> getGarageInfoList(@Param("listQuery") List<String> listQuery, @Param("queryDto") SgGarageInfoDTO dto, @Param("isAll") Boolean isAll);



    /**
     * (根据车库名称查询车库)
     *
     * @param name
     * @return
     * @author: YixinCapital -- libochen
     * 2019/8/13 17:15
     */
    SgGarageInfoDTO queryGarageInfoByName(@Param("name")String name);

}
