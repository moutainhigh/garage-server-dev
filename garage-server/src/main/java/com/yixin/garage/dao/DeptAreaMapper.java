package com.yixin.garage.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yixin.garage.entity.DeptArea;

/**
 * <p>
 * 部门表 Mapper 接口
 * </p>
 *
 * @author lizhongxin
 * @since 2019-01-02
 */
public interface DeptAreaMapper extends BaseMapper<DeptArea> {
    /**
     * 
     * @Description: 查询部门大区
     * @param flag:1:已经分配的  0:未分配的
     * @param domainAccount:域账号
     * @return InvokeResult<String>
     * @throws 
     * @author YixinCapital -- yangfei02
     *         2019年3月12日 下午3:14:40
     */
    List<DeptArea> queryAreaDeptList(@Param("flag") String flag, @Param("domainAccount") String domainAccount);
}
