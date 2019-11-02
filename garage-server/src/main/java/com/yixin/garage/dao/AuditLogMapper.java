package com.yixin.garage.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yixin.garage.entity.AuditLog;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lizhongxin
 * @since 2019-01-03
 */
public interface AuditLogMapper extends BaseMapper<AuditLog> {

	List<String> getBussIdByTypeAndDealUser(@Param("logTypeCode") String logTypeCode,
                                            @Param("dealUserAccount") String dealUserAccount,
                                            @Param("limitStart") Integer limitStart,
                                            @Param("rowCount") Integer rowCount);
	
//	IPage<AuditLog> selectAuditLogDto(Page<AuditLog> page, @Param("auditLogDTO") AuditLogDTO dto);

}
