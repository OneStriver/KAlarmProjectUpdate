package com.fh.util.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcelToDBUtil {
	/**
	 * 对外提供读取excel的方法
	 */
	public static List<List<Object>> readExcel(File file) throws IOException {
		String fileName = file.getName();
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1);
		if ("xls".equals(extension)) {
			return read2003Excel(file);
		} else if ("xlsx".equals(extension)) {
			return read2007Excel(file);
		} else {
			throw new IOException("不支持的文件类型");
		}
	}

	/**
	 * 读取 office 2003 excel
	 */
	private static List<List<Object>> read2003Excel(File file) throws IOException {
		List<List<Object>> list = new ArrayList<List<Object>>();
		HSSFWorkbook hwb = new HSSFWorkbook(new FileInputStream(file));
		HSSFSheet sheet = hwb.getSheetAt(0);
		Object value = null;
		HSSFRow row = null;
		HSSFCell cell = null;
		int firstRowNum = sheet.getFirstRowNum();
		int rowCount = sheet.getPhysicalNumberOfRows();
		for (int i = (firstRowNum+1); i < rowCount; i++) {
			row = sheet.getRow(i);
			List<Object> linked = new ArrayList<Object>();
			for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
				cell = row.getCell(j);
				DecimalFormat df = new DecimalFormat("0");							// 格式化 number String
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	// 格式化日期字符串
				DecimalFormat nf = new DecimalFormat("0.00");						// 格式化数字
				switch (cell.getCellType()) {
				case XSSFCell.CELL_TYPE_STRING:
					System.out.println("第" + i + "行,第" + j + " 列是  String type");
					value = cell.getStringCellValue();
					break;
				case XSSFCell.CELL_TYPE_NUMERIC:
					System.out.println("第" + i + "行,第" + j + " 列是  Number type ; DateFormt:" + cell.getCellStyle().getDataFormatString());
					if ("@".equals(cell.getCellStyle().getDataFormatString())) {
						value = df.format(cell.getNumericCellValue());
					} else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
						value = nf.format(cell.getNumericCellValue());
					} else {
						value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
					}
					break;
				case XSSFCell.CELL_TYPE_BOOLEAN:
					System.out.println("第" + i + "行,第" + j + " 列是  Boolean type");
					value = cell.getBooleanCellValue();
					break;
				case XSSFCell.CELL_TYPE_BLANK:
					System.out.println("第" + i + "行,第" + j + " 列是  Blank type");
					value = "";
					break;
				default:
					System.out.println("第" + i + "行,第" + j + " 列是  default type");
					value = cell.toString();
				}
				if (value == null || "".equals(value)) {
					value = "";
				}
				linked.add(value);
			}
			list.add(linked);
		}
		//关闭HSSFWorkBook
		hwb.close();
		return list;
	}

	/**
	 * 读取Office 2007 excel
	 */
	private static List<List<Object>> read2007Excel(File file) throws IOException {
		List<List<Object>> list = new ArrayList<List<Object>>();
		// 构造 XSSFWorkbook 对象,strPath 传入文件路径
		XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
		// 读取第一章表格内容
		XSSFSheet sheet = xwb.getSheetAt(0);
		Object value = null;
		XSSFRow row = null;
		XSSFCell cell = null;
		int firstRowNum = sheet.getFirstRowNum();
		int rowCount = sheet.getPhysicalNumberOfRows();
		for (int i = (firstRowNum+1); i < rowCount; i++) {
			row = sheet.getRow(i);
			List<Object> linked = new ArrayList<Object>();
			int firstCellNum = row.getFirstCellNum();
			int lastCellNum = row.getLastCellNum();
			for (int j = firstCellNum; j < lastCellNum; j++) {
				cell = row.getCell(j);
				DecimalFormat df = new DecimalFormat("0");							// 格式化 number String
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	// 格式化日期字符串
				DecimalFormat nf = new DecimalFormat("0.00");						// 格式化数字
				switch (cell.getCellType()) {
				case XSSFCell.CELL_TYPE_STRING:
					System.out.println("第" + i + "行,第" + j + " 列是  String type");
					value = cell.getStringCellValue();
					break;
				case XSSFCell.CELL_TYPE_NUMERIC:
					System.out.println("第" + i + "行,第" + j + " 列是  Number type ; DateFormt:" + cell.getCellStyle().getDataFormatString());
					System.err.println(cell.getNumericCellValue());
					if ("@".equals(cell.getCellStyle().getDataFormatString())) {
						value = df.format(cell.getNumericCellValue());
					} else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
						value = nf.format(cell.getNumericCellValue());
					} else {
						value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
					}
					break;
				case XSSFCell.CELL_TYPE_BOOLEAN:
					System.out.println("第" + i + "行,第" + j + " 列是  Boolean type");
					value = cell.getBooleanCellValue();
					break;
				case XSSFCell.CELL_TYPE_BLANK:
					System.out.println("第" + i + "行,第" + j + " 列是  Blank type");
					value = "";
					break;
				default:
					System.out.println("第" + i + "行,第" + j + " 列是  default type");
					value = cell.toString();
				}
				if (value == null || "".equals(value)) {
					value = "";
				}
				linked.add(value);
			}
			list.add(linked);
		}
		//关闭XSSFWorkBook
		xwb.close();
		return list;
	}
	
}
