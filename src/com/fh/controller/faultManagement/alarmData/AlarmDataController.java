package com.fh.controller.faultManagement.alarmData;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fh.controller.base.BaseController;
import com.fh.entity.PageData;
import com.fh.entity.faultManagement.alarmData.AlarmDataEntity;
import com.fh.entity.faultManagement.historyAlarm.DeviceType;
import com.fh.util.excel.ReadExcelToDBUtil;
import com.fh.util.fileUploadDowload.ZipFileUtil;
import com.google.gson.Gson;

/** 
 * 说明：告警数据管理
 * 创建人：FH 
 */
@Controller
@RequestMapping(value="/alarmData")
public class AlarmDataController extends BaseController {
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	
	/**
	 * 获取设备类型数据
	 */
	@RequestMapping(value="/getDeviceType",produces="text/html;charset=UTF-8")
	@ResponseBody
	public String getDeviceType() throws Exception{
		List<DeviceType> list = new ArrayList<DeviceType>();
		DeviceType d1 = new DeviceType();
		d1.setDeviceId("1");
		d1.setDeviceName("交换机");
		list.add(d1);
		DeviceType d2 = new DeviceType();
		d2.setDeviceId("2");
		d2.setDeviceName("授时设备");
		list.add(d2);
		DeviceType d3 = new DeviceType();
		d3.setDeviceId("3");
		d3.setDeviceName("功放控制器");
		list.add(d3);
		Gson gson = new Gson();
		String jsonStr = gson.toJson(list);
		return jsonStr;
	}
	
	/**
	 * 转到告警数据管理页面
	 */
	@RequestMapping(value="/toAlarmDataPage")
	public ModelAndView toRealTimeAlarmPage()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.addObject("pd", pd);	
		mv.setViewName("faultManagement/alarmData/alarmData_list");
		return mv;
	}
	
	/**
	 * 获取告警数据
	 */
	@RequestMapping(value="/getAlarmData")
	@ResponseBody
	public Map<String, Object> getAlarmData(int pageNumber,int pageSize) throws Exception{
		logBefore(logger, "获取数据库中告警数据");
		Map<String, Object> map = new HashMap<String, Object>();
		List<AlarmDataEntity> list = new ArrayList<AlarmDataEntity>();
		AlarmDataEntity a1 = new AlarmDataEntity();
		a1.setEquipType("1");
		a1.setCode("123");
		a1.setSeverity("2");
		a1.setAlarmType("1");
		a1.setDesc("111");
		a1.setCause("未知");
		a1.setTreatment("未知");
		a1.setAddition("未知");
		a1.setAutoClearEnable("1");
		a1.setAutoClearTimeout("2018-3-15");
		a1.setSuppress("1");
		list.add(a1);
		AlarmDataEntity a2 = new AlarmDataEntity();
		a2.setEquipType("2");
		a2.setCode("345");
		a2.setSeverity("2");
		a2.setAlarmType("2");
		a2.setDesc("111");
		a2.setCause("未知");
		a2.setTreatment("未知");
		a2.setAddition("未知");
		a2.setAutoClearEnable("2");
		a2.setAutoClearTimeout("2018-3-22");
		a2.setSuppress("2");
		list.add(a2);
		
		map.put("total", list.size());  
        map.put("rows", list);
		return map;
	}
	
	/**
	 * 添加告警数据
	 */
	@RequestMapping("saveAlarmData")
	@ResponseBody 
	public String saveAlarmData(AlarmDataEntity alarmDataEntity){
		//写入数据库中
		
		//查询出数据中现有的所有数据
		
		return "测试";
	}
	
	/**
	 * 更新告警数据
	 */
	@RequestMapping("updateAlarmData")
	@ResponseBody 
	public String updateAlarmData(AlarmDataEntity alarmDataEntity){
		//更新数据库中
		
		//查询出数据中现有的所有数据
		
		return "测试";
	}
	
	/**
	 * 删除告警数据
	 */
	@RequestMapping("deleteSelectAlarmDatas")
	@ResponseBody 
	public String deleteSelectAlarmDatas(@RequestParam(value="sendSelectRows") String[] sendSelectRows){
		//删除数据库中的内容
		System.err.println("===========================");
		return "测试删除告警";
	}
	
	/**
	 * 开启告警数据抑制
	 */
	@RequestMapping("openAlarmSuppression")
	@ResponseBody 
	public String controlSelectAlarmDatas(@RequestParam(value="openAlarmControlRows") String[] openAlarmControlRows){
		//抑制数据库中的内容
		System.err.println("开启===========================");
		return "测试开启抑制告警";
	}
	
	/**
	 * 关闭告警数据抑制
	 */
	@RequestMapping("closeAlarmSuppression")
	@ResponseBody 
	public String closeAlarmSuppression(@RequestParam(value="closeAlarmControlRows") String[] closeAlarmControlRows){
		//抑制数据库中的内容
		System.err.println("关闭===========================");
		return "测试关闭抑制告警";
	}
	
	/**
	 * 获取ZIP包中的文件，并导入数据库中
	 */
	@RequestMapping("/uploadAlarmDataZipPackage")
	@ResponseBody
	public String uploadAlarmDataZipPackage(@RequestParam(value = "alarmDataFile") MultipartFile zipFile) throws Exception {
		String projectPath = System.getProperty("www.sunkaisens.com");
		System.err.println("项目根目录:"+projectPath);
		List<File> fileList = ZipFileUtil.resolveUploadZipFile(zipFile, projectPath.substring(0,projectPath.length()-1));
		for (int i = 0; i < fileList.size(); i++) {
			String name = fileList.get(i).getName();
			System.err.println("文件名字:"+name);
			List<List<Object>> readExcelValueList = ReadExcelToDBUtil.readExcel(fileList.get(i));
			System.err.println("======================"+readExcelValueList);
		}
        String substring = "123";
		return substring;
	}
	
	/**
	 * 将数据库中的数据导出ZIP包
	 */
	@RequestMapping("/exportAllAlarmDatas")
	public void exportAllAlarmDatas(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String projectPath = System.getProperty("www.sunkaisens.com");
		
		List<File> files = new ArrayList<File>();
		File file1 = new File("D:\\file1.xlsx");
		File file2 = new File("D:\\file2.xlsx");
		File file3 = new File("D:\\file3.xlsx");
		files.add(file1);
		files.add(file2);
		files.add(file3);

		String zipFileName = "告警数据_"+sdf.format(new Date()) + ".zip";
		// 在服务器端创建打包下载的临时文件
		File fileZip = new File(projectPath + zipFileName);
		// 文件输出流
		FileOutputStream outStream = new FileOutputStream(fileZip);
		// 压缩流
		ZipOutputStream toClient = new ZipOutputStream(outStream);
		ZipFileUtil.zipFile(files, toClient);
		toClient.close();
		outStream.close();
		ZipFileUtil.downloadFile(fileZip, response, true);
		
	}

}
