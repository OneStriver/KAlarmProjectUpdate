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

import com.fh.entity.faultManagement.historyAlarm.OptionQueryAlarm;
import com.fh.util.Tools;
/**
 * 导入到EXCEL
 * @author FH 
 */
public class HistoryAlarmExcelView extends AbstractXlsxView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workBook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Date date = new Date();
		String filename = "HistoryAlarm-"+Tools.date2Str(date, "yyyyMMddHHmmss");
		response.setHeader("Content-Disposition", "attachment;filename="+filename+".xlsx");
		response.setContentType("application/ms-excel; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        short width = 20;
        XSSFSheet sheet = (XSSFSheet) workBook.createSheet("历史告警数据");
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
		List<OptionQueryAlarm> alarmList = (List<OptionQueryAlarm>) model.get("historyAlarmList");
		int alarmCount = alarmList.size();
		int rowCount = 1;
		for(int i=0; i<alarmCount; i++){
			Row alarmRow = sheet.createRow(rowCount++);
			OptionQueryAlarm alarmObj = alarmList.get(i);
			alarmRow.createCell(0).setCellValue(alarmObj.getAlarmNumber());
			alarmRow.createCell(1).setCellValue(alarmObj.getDeviceName());
			alarmRow.createCell(2).setCellValue(alarmObj.getAlarmLevel());
			alarmRow.createCell(3).setCellValue(alarmObj.getAlarmDescription());
			alarmRow.createCell(4).setCellValue(alarmObj.getAlarmReason());
			alarmRow.createCell(5).setCellValue(alarmObj.getRecommendMeasure());
			alarmRow.createCell(7).setCellValue(alarmObj.getAlarmDetail());
			alarmRow.createCell(8).setCellValue(alarmObj.getAlarmHappenTime());
			alarmRow.createCell(9).setCellValue(alarmObj.getAlarmAckTime());
			alarmRow.createCell(10).setCellValue(alarmObj.getAlarmAckPerson());
			alarmRow.createCell(11).setCellValue(alarmObj.getAlarmClearTime());
			alarmRow.createCell(12).setCellValue(alarmObj.getAlarmClearPerson());
		}
		
	}

}
