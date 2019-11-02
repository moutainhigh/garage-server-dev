package com.yixin.garage.dto.sys;

import com.yixin.common.utils.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 
 * @ClassName: AttachSourceDTO
 * @Description 附件资源DTO
 * @author YixinCapital -- mjj
 * @date 2018年5月30日 上午9:19:26
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true)
public class AttachSourceDTO extends BaseDTO {

    private static final long serialVersionUID = -1L;

    /**
     * 业务表id
     */
    private String bussId;
    
    /**
     * 业务类型：用于区分同一个bussId对应的多个附件
     */
    private String bussType;

    /**
     * 附件类型
     */
    private String attchType;

    /**
     * 附件存储位置
     */
    private String attchPath;
    
    /**
     * 附件访问路径
     */
    private String viewUrl;

    /**
     * 资源附件原始名称
     */
    private String sourceAttchName;

    /**
     * 压缩后图片路径
     */
    private String compressAttchPath;

}
