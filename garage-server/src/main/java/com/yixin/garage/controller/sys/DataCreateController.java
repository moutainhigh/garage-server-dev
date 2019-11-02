package com.yixin.garage.controller.sys;


import com.yixin.common.utils.BaseDTO;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.dto.sys.UploadFileDTO;
import com.yixin.garage.service.sys.DataCreateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Transactional
@RestController
@RequestMapping("/dataCreateController")
public class DataCreateController {
    private final static Logger logger = LoggerFactory.getLogger(DataCreateController.class);

    private static final String ALLOW_FILE_TYPE = ".xlsx";

    private static final String DEFAULT_ENCODE_TYPE = "utf-8";

    /**
     * 上传解析Request属性fileSize
     */
    private static final String FILE_SIZE = "fileSize";
    private static final String FILE_TYPE = "fileType";

    @Autowired
    private DataCreateService dataCreateService;


    @PostMapping("/uploadFile")
    public InvokeResult<String> uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest req, BaseDTO baseDTO){
        logger.info("数据初始化接口开始");

        InvokeResult<String> result = new InvokeResult<String>();
        String errMsg = "";
        try {
            // 提取文件文件名
            String fileName = java.net.URLDecoder.decode(file.getOriginalFilename(), DEFAULT_ENCODE_TYPE);
            String fileName1 = fileName.toLowerCase();
            if(!(fileName1.endsWith(".xlsx")||fileName1.endsWith(".xls"))){
                result.failure("上传文件格式有误，请重新上传！");
                return result;
            }
            // 判断文件类型是否符合
            if (fileName.endsWith(ALLOW_FILE_TYPE) || file.equals(ALLOW_FILE_TYPE.toUpperCase())) {
                String fileSize = req.getParameter(FILE_SIZE) == null ? "" : req.getParameter(FILE_SIZE);
                UploadFileDTO fileDTO = new UploadFileDTO();
                fileDTO.setStream(file.getBytes());
                fileDTO.setFileName(fileName);
                fileDTO.setFileSize(fileSize);
                fileDTO.setCreatorId(baseDTO.getCreatorId());
                fileDTO.setCreatorName(baseDTO.getCreatorName());
                fileDTO.setCurrentUser(baseDTO.getCurrentUser());
                fileDTO.setCreatorDepartmentId(baseDTO.getCreatorDepartmentId());
                fileDTO.setCreatorDepartmentName(baseDTO.getCreatorDepartmentName());
                result = dataCreateService.dataCreateService(fileDTO);

            } else {
                errMsg = "导入分配文件类型错误（仅支持xlsx类型）";
                logger.error("vehicleLoanImport() failed：{}", errMsg);
                result.failure(errMsg);
            }

        } catch (Exception e) {
            logger.error("vehicleLoanImport() failed：{}", e);
            result.failure("vehicleLoanImport() failed：" + e.getMessage());
        }



        return null;
    }
}
