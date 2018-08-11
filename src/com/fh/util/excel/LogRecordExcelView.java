package com.fh.util.excel;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.fh.entity.PageData;
import com.fh.util.Tools;
/**
 * 导入到EXCEL
 * @author FH 
 */
public class LogRecordExcelView extends AbstractXlsxView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workBook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Date date = new Date();
		String filename = "LogRecord-"+Tools.date2Str(date, "yyyyMMddHHmmss");
		response.setHeader("Content-Disposition", "attachment;filename="+filename+".xlsx");
		response.setContentType("application/ms-excel; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        short width = 20;
        XSSFSheet sheet = (XSSFSheet) workBook.createSheet("日志记录数据");
        sheet.setDefaultColumnWidth(width);
        
		@SuppressWarnings("unchecked")
		List<String> titles = (List<String>) model.get("titles");
		int headerLength = titles.size();
		
		//标题的样式
		XSSFCellStyle headerStyle = (XSSFCellStyle) workBook.createCellStyle();
		headerStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		headerStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		XSSFFont headerFont = (XSSFFont) workBook.createFont();
		headerFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		headerFont.setFontHeightInPoints((short)11);
		headerStyle.setFont(headerFont);
		
		//设置标题
		Row headerRow = sheet.createRow(0);
		for(int i=0; i<headerLength; i++){ 
			String title = titles.get(i);
			headerRow.createCell(i).setCellValue(title);
			headerRow.getCell(i).setCellStyle(headerStyle);
		}
		
		XSSFCellStyle contentStyle = (XSSFCellStyle) workBook.createCellStyle(); //内容样式
		contentStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		@SuppressWarnings("unchecked")
		List<PageData> logRecordList = (List<PageData>) model.get("logRecordList");
		int alarmCount = logRecordList.size();
		int rowCount = 1;
		for(int i=0; i<alarmCount; i++){
			Row logRecordRow = sheet.createRow(rowCount++);
			PageData pageData = logRecordList.get(i);
			logRecordRow.createCell(0).setCellValue(pageData.getString("USERNAME"));
			logRecordRow.createCell(1).setCellValue(pageData.getString("CONTENT"));
			logRecordRow.createCell(2).setCellValue(pageData.getString("CZTIME"));
		}
		
	}

}
