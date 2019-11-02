package com.yixin.garage.controller.sys;


import java.util.HashMap;
import java.util.Map;

import com.yixin.garage.core.base.BaseAssembler;
import com.yixin.garage.entity.AttachSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.dto.sys.AttachSourceDTO;
import com.yixin.garage.util.CommonFileUtil;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lizhongxin
 * @since 2019-01-07
 */
@RestController
@RequestMapping("/attachSource")
public class AttachSourceController {
    private static final Logger logger = LoggerFactory.getLogger(AttachSourceController.class);

    @Autowired
    private CommonFileUtil fileUtil;

    /**
     * 查看图片
     */
    @Value("${viewFileServerPath}")
    private String viewFileServerPath;
    
    /**
     * 
     * @Description: 上传文件，不包含压缩等操作
     * @param file
     * @return InvokeResult<AttachSourceDTO>
     * @throws 
     * @author YixinCapital -- yangfei02
     *	       2019年1月7日 下午5:03:12
     */
    @PostMapping("/uploadFile")
    public InvokeResult<AttachSourceDTO> uploadFile(@RequestParam("file") MultipartFile file){
        logger.info("开始上传文件");
        InvokeResult<AttachSourceDTO> result = new InvokeResult<>();
        AttachSourceDTO attachSourceDTO = new AttachSourceDTO();
        try{
            attachSourceDTO.setSourceAttchName(file.getOriginalFilename());
            String fileId = fileUtil.uploadWebFile(file, false, null);
            if(StringUtils.isBlank(fileId)){
                logger.error("上传文件出错");
                result.failure("上传文件出错");
                return result;
            }
            //设置原始文件存储路径
            attachSourceDTO.setAttchPath(fileId);
            attachSourceDTO.setViewUrl(viewFileServerPath + fileId);
            AttachSource attachSource = new AttachSource();
            BaseAssembler.mapObjWithoutNullAndBaseColumn(attachSourceDTO,attachSource);
            attachSource.insert();
            attachSourceDTO.setId(attachSource.getId());
            result.setData(attachSourceDTO);
        }catch (Throwable t){
            logger.error("上传文件异常",t);
            result.failure("上传文件异常");
        }
        logger.info("上传文件正常返回：{}", JSONObject.toJSONString(result));
        return result;
    }
    
    /**
     * 
     * @Description: 上传图片，包含压缩等操作（此方法尚未用到，未经过测试）
     * @param file
     * @return InvokeResult<AttachSourceDTO>
     * @throws 
     * @author YixinCapital -- yangfei02
     *	       2019年1月7日 下午5:12:00
     */
    @PostMapping("/uploadImageFile")
    public InvokeResult<AttachSourceDTO> uploadImageFile(@RequestParam("file") MultipartFile file){
        logger.info("开始上传图片");
        InvokeResult<AttachSourceDTO> result = new InvokeResult<>();
        AttachSourceDTO attachSourceDTO = new AttachSourceDTO();
        try{
            attachSourceDTO.setSourceAttchName(file.getOriginalFilename());
            String fileId = fileUtil.uploadWebFile(file, false, null);
            if(StringUtils.isBlank(fileId)){
                logger.error("上传图片出错");
                result.failure("上传图片出错");
                return result;
            }
            //设置原始图片存储路径
            attachSourceDTO.setAttchPath(fileUtil.getFileUrl(fileId));
            //上传压缩后图片
            Map<String, Integer> zoomMap = new HashMap<>();
            zoomMap.put("zoonFlag",1);
            zoomMap.put("newWidth",150);
            String compressFileId = fileUtil.uploadWebFile(file, false, zoomMap);
            if(StringUtils.isBlank(compressFileId)){
                logger.error("上传压缩图片出错");
                result.failure("上传压缩图片出错");
                return result;
            }
            attachSourceDTO.setCompressAttchPath(fileUtil.getFileUrl(compressFileId));
            result.setData(attachSourceDTO);
        }catch (Throwable t){
            logger.error("上传图片异常",t);
            result.failure("上传图片异常");
        }
        logger.info("上传图片正常返回：{}", JSONObject.toJSONString(result));
        return result;
    }

}
