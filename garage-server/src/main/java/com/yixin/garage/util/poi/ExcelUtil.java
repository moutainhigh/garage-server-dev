package com.yixin.garage.util.poi;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.yixin.common.exception.BzException;

public class ExcelUtil {
	
	private final  static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);
	
	/**
	 * 
	 * @Title: getOutResponseEntity   
	 * @Description: 组装controller导出返回结果   
	 * @param wb
	 * @param fileName
	 * @return  ResponseEntity<byte[]>     
	 * @author YixinCapital -- lizhongxin
	 *	       2019年1月16日 下午12:07:38
	 */
	public static ResponseEntity<byte[]> getOutResponseEntity(Workbook wb,String fileName){
		HttpHeaders headers = new HttpHeaders();
  		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			final MediaType mediaType = MediaType
					.parseMediaType("application/force-download;charset=UTF-8");// 强制下载mediatype
			headers.setContentType(mediaType);
			fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
			headers.setContentDispositionFormData("attachment", fileName);
			wb.write(out);
			return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("生成Excel文件过程发生异常，信息为：{}",e);
			throw new BzException("生成Excel文件过程发生异常");
		}finally{
			try {
				out.close();
				wb.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 
	 * @Title: createWorkbookByFilePath   
	 * @Description: 根据Excel 模版路径返回workbook
	 * @param filePath
	 * @return  Workbook    
	 * @author YixinCapital -- lizhongxin
	 *	       2019年1月16日 下午1:46:54
	 */
	public static Workbook createWorkbookByFilePath(String filePath){
		InputStream inputStream = null;
		try {
			ClassPathResource resource = new ClassPathResource(filePath);
			inputStream = resource.getInputStream();
//			file = new FileInputStream(inputStream);
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
			throw new BzException("Excel模版未找到,模版路径："+filePath);
		} 
		try {
			return WorkbookFactory.create(inputStream);
		} catch (Exception e1) {
			logger.error("创建workbook失败，错误信息：{}",e1.getMessage(),e1);
			throw new BzException("创建workbook失败");
		} finally {
			try {
				inputStream.close();
			}catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		
	}
	
	
	public static ResponseEntity<byte[]> getTemplateResponse(String templateLoc,String fileName){
		HttpHeaders headers = new HttpHeaders();
  		InputStream inputStream = null;
		try {
			ClassPathResource resource = new ClassPathResource(templateLoc);
			inputStream = resource.getInputStream();
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
			throw new BzException("Excel模版未找到,模版路径："+templateLoc);
		} 
		
		try {
			final MediaType mediaType = MediaType
					.parseMediaType("application/force-download;charset=UTF-8");// 强制下载mediatype
			headers.setContentType(mediaType);
			fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
			headers.setContentDispositionFormData("attachment", fileName);
			return new ResponseEntity<>(input2byte(inputStream), headers, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("生成Excel文件过程发生异常，信息为：{}",e);
			throw new BzException("生成Excel文件过程发生异常");
		}finally{
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static final byte[] input2byte(InputStream inStream)  {  
        byte[] buff = new byte[100];  
        int rc = 0;  
        byte[] result = null;
        try(ByteArrayOutputStream swapStream = new ByteArrayOutputStream(); ) {
			while ((rc = inStream.read(buff, 0, 100)) > 0) {  
			    swapStream.write(buff, 0, rc);  
			}
			result = swapStream.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} 
        return result;
        
        
    }  
}


