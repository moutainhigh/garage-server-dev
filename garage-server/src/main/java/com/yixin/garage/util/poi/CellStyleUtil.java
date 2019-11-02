package com.yixin.garage.util.poi;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

import cn.hutool.core.util.StrUtil;

public class CellStyleUtil {
	
	/**
	 * 给cell设置颜色
	 * 
	 * @param cellStyle {@link CellStyle}
	 * @param color 背景颜色
	 * @param fillPattern 填充方式 {@link FillPatternType}枚举
	 * @return {@link CellStyle}
	 */
	public static CellStyle setColor(CellStyle cellStyle, short color, FillPatternType fillPattern) {
		cellStyle.setFillForegroundColor(color);
		cellStyle.setFillPattern(fillPattern);
		return cellStyle;
	}
	
	/**
	 * 给cell设置颜色
	 * 
	 * @param cellStyle {@link CellStyle}
	 * @param color 背景颜色
	 * @param fillPattern 填充方式 {@link FillPatternType}枚举
	 * @return {@link CellStyle}
	 */
	public static CellStyle setColor(CellStyle cellStyle, IndexedColors color, FillPatternType fillPattern) {
		return setColor(cellStyle, color.index, fillPattern);
	}
	
	
	/**
	 * 设置字体样式
	 * 
	 * @param font 字体{@link Font}
	 * @param color 字体颜色
	 * @param fontSize 字体大小
	 * @param fontName 字体名称，可以为null使用默认字体
	 * @return {@link Font}
	 */
	public static Font setFontStyle(Font font, short color, short fontSize, String fontName) {
		if(color > 0) {
			font.setColor(color);
		}
		if(fontSize > 0) {
			font.setFontHeightInPoints(fontSize);
		}
		if(StrUtil.isNotBlank(fontName)) {
			font.setFontName(fontName);
		}
		return font;
	}

	/**
	 * 创建默认普通单元格样式
	 * 
	 * <pre>
	 * 1. 文字上下左右居中
	 * 2. 细边框，黑色
	 * </pre>
	 * 
	 * @param workbook {@link Workbook} 工作簿
	 * @return {@link CellStyle}
	 */
	public static CellStyle createDefaultCellStyle(Workbook workbook) {
		final CellStyle cellStyle = workbook.createCellStyle();
		setAlign(cellStyle, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
		setBorder(cellStyle, BorderStyle.THIN, IndexedColors.BLACK);
		return cellStyle;
	}
	

	/**
	 * 设置cell文本对齐样式
	 * 
	 * @param cellStyle {@link CellStyle}
	 * @param halign 横向位置
	 * @param valign 纵向位置
	 * @return {@link CellStyle}
	 */
	public static CellStyle setAlign(CellStyle cellStyle, HorizontalAlignment halign, VerticalAlignment valign) {
		cellStyle.setAlignment(halign);
		cellStyle.setVerticalAlignment(valign);
		return cellStyle;
	}

	/**
	 * 设置cell的四个边框粗细和颜色
	 * 
	 * @param cellStyle {@link CellStyle}
	 * @param borderSize 边框粗细{@link BorderStyle}枚举
	 * @param colorIndex 颜色的short值
	 * @return {@link CellStyle}
	 */
	public static CellStyle setBorder(CellStyle cellStyle, BorderStyle borderSize, IndexedColors colorIndex) {
		cellStyle.setBorderBottom(borderSize);
		cellStyle.setBottomBorderColor(colorIndex.index);

		cellStyle.setBorderLeft(borderSize);
		cellStyle.setLeftBorderColor(colorIndex.index);

		cellStyle.setBorderRight(borderSize);
		cellStyle.setRightBorderColor(colorIndex.index);

		cellStyle.setBorderTop(borderSize);
		cellStyle.setTopBorderColor(colorIndex.index);

		return cellStyle;
	}
	
	/**
	 * 创建字体
	 * 
	 * @param workbook {@link Workbook}
	 * @param color 字体颜色
	 * @param fontSize 字体大小
	 * @param fontName 字体名称，可以为null使用默认字体
	 * @return {@link Font}
	 */
	public static Font createFont(Workbook workbook, short color, short fontSize, String fontName) {
		final Font font = workbook.createFont();
		return setFontStyle(font, color, fontSize, fontName);
	}
	/**
	 * @Title: createNumberStyle   
	 * @Description: 创建数值类型样式   
	 * @param wb
	 * @return   CellStyle   
	 * @author YixinCapital -- lizhongxin
	 *	       2019年2月17日 下午4:44:08
	 */
	public static CellStyle createNumberStyle(Workbook wb, boolean isBold ,boolean isInteger){
		CellStyle cellStyle = wb.createCellStyle();
		Font font = createFont(wb,HSSFColor.HSSFColorPredefined.BLACK.getIndex(), (short) 9,"微软雅黑");
		font.setBold(isBold);
		cellStyle.setFont(font);
		DataFormat dataFormat = wb.createDataFormat();
		if(isInteger){
			cellStyle.setDataFormat(dataFormat.getFormat("#,##0"));//保留整数
		}else{
			cellStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));//保留两位小数
		}
		return cellStyle;
	}
	/**
	 * @Title: createNumberStyle   
	 * @Description: 创建文本类型样式   
	 * @param wb
	 * @return   CellStyle   
	 * @author YixinCapital -- lizhongxin
	 *	       2019年2月17日 下午4:44:08
	 */
	public static CellStyle createTextStyle(Workbook wb, boolean isBold){
		CellStyle cellStyle = wb.createCellStyle();
		Font font = createFont(wb,HSSFColor.HSSFColorPredefined.BLACK.getIndex(), (short) 9,"微软雅黑");
		font.setBold(isBold);
		cellStyle.setFont(font);
		return cellStyle;
	}
	/**
	 * 
	 * @Title: createDateStyle   
	 * @Description: 设置日期样式 为   yyy-mm-dd
	 * @param wb
	 * @param isBold
	 * @return  CellStyle
	 * @author YixinCapital -- lizhongxin
	 *	       2019年2月17日 下午7:13:27
	 */
	public static CellStyle createDateStyle(Workbook wb, boolean isBold) {
		CellStyle cellStyle = wb.createCellStyle();
		Font font = createFont(wb,HSSFColor.HSSFColorPredefined.BLACK.getIndex(), (short) 9,"微软雅黑");
		font.setBold(isBold);
		cellStyle.setFont(font);
		DataFormat dataFormat = wb.createDataFormat();
		cellStyle.setDataFormat(dataFormat.getFormat("yyy-mm-dd"));
		return cellStyle;
	}

	public static CellStyle createPercentStyleStyle(Workbook wb) {
		CellStyle cellStyle = wb.createCellStyle();
		Font font = createFont(wb,HSSFColor.HSSFColorPredefined.BLACK.getIndex(), (short) 9,"微软雅黑");
		cellStyle.setFont(font);
		DataFormat dataFormat = wb.createDataFormat();
		cellStyle.setDataFormat(dataFormat.getFormat("0.00%"));
		return cellStyle;
	}
	
	
}

