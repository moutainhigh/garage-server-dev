package com.yixin.garage.service.impl.sys;

import com.yixin.common.exception.BzException;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.dto.api.forLoan.SgGarageForLoanGarageInDTO;
import com.yixin.garage.dto.sys.UploadFileDTO;
import com.yixin.garage.service.api.SgGarageForLoanService;
import com.yixin.garage.service.sys.DataCreateService;
import com.yixin.garage.util.excel.impt.ImportExcel;
import com.yixin.garage.util.excel.inptVO.DataCreateImportVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Transactional
@Service
public class DataCreateServiceImpl implements DataCreateService {

    Logger logger = LoggerFactory.getLogger(DataCreateServiceImpl.class);

    @Value("${fileContextPath}")
    private String fileContextPath;

    @Autowired
    private SgGarageForLoanService sgGarageForLoanService;




    @Override
    public InvokeResult<String> dataCreateService(UploadFileDTO fileDTO) throws BzException {
        logger.info("==============数据初始化接口开始 DataCreateServiceImpl==============");
        InvokeResult<String> result = new InvokeResult<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        File file = null;
        try {
            file = new File(fileContextPath+"sgGarage" + new Date().getTime() + ".xlsx");
            OutputStream output = new FileOutputStream(file);
            BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
            bufferedOutput.write(fileDTO.getStream());

            // 获取导入文件，通过ImportExcel工具类解析
            ImportExcel excel = new ImportExcel(file.getPath(), 0, 0);
            if(excel == null){
                return result.failure("导入失败，导入的模板不正确，请确认");
            }
            final List<DataCreateImportVO> dataCreateImportVOList = excel.getDataList(DataCreateImportVO.class);

            // 校验开始
            if (CollectionUtils.isEmpty(dataCreateImportVOList)) {
                return result.failure("导入失败，导入数据不能为空。");
            }

            for (int i = 0; i < dataCreateImportVOList.size(); i++) {
                result = sgGarageForLoanService.dateCreate(dataCreateImportVOList.get(i));
                if (result.isHasErrors()) {
                    return result;
                }
            }
        } catch (Exception e) {
            logger.error("dataCreateService() failed",e);
            throw new BzException(e.getMessage(), e);
        }

        return result;
    }
}
