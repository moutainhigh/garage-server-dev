package com.yixin.garage.util.poi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

public class SheetUtil {
	
	 public static Sheet copySheet(Sheet sheetFrom, Sheet sheetTo) {  
		   
	        // 初期化  
	        CellRangeAddress region = null;  
	        Row rowFrom = null;  
	        Row rowTo = null;  
	        Cell cellFrom = null;  
	        Cell cellTo = null;  
	          
	        //复制合并区域
	        for (int i = 0; i < sheetFrom.getNumMergedRegions(); i++) {  
	            region = sheetFrom.getMergedRegion(i);  
	            if ((region.getFirstColumn() >= sheetFrom.getFirstRowNum())  
	                    && (region.getLastRow() <= sheetFrom.getLastRowNum())) {  
	            sheetTo.addMergedRegion(region);  
	            }  
	        }  
	  
	        //循环复制每一行 
	        for (int intRow = sheetFrom.getFirstRowNum(); intRow < sheetFrom.getLastRowNum(); intRow++) {  
	            rowFrom = sheetFrom.getRow(intRow);  
	            rowTo = sheetTo.createRow(intRow);  
	            if (null == rowFrom)  
	                continue;  
	            rowTo.setHeight(rowFrom.getHeight());  
	            //循环复制每一列
	            for (int intCol = 0; intCol < rowFrom.getLastCellNum(); intCol++) {  
	                //セル幅のコピー  
	                sheetTo.setDefaultColumnStyle(intCol, sheetFrom.getColumnStyle(intCol));  
	                sheetTo.setColumnWidth(intCol, sheetFrom.getColumnWidth(intCol));  
	                cellFrom = rowFrom.getCell(intCol);  
	                cellTo = rowTo.createCell(intCol);  
	                if (null == cellFrom)  
	                    continue;  
	            	CellUtil.copyCell(cellFrom, cellTo);
	            }
	        }  
	          
	        return sheetTo;  
	  
	    }

	public static Row getOrCreateRow(Sheet sheet, int rownum) {
		Row row = sheet.getRow(rownum);
		if(row ==  null)  row = sheet.createRow(rownum);
		return row;
	}  
}

