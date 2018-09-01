package com.fh.controller.faultManagement;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fh.mqtt.MqttMessageServer;
import com.google.gson.Gson;

@Controller
@RequestMapping(value="/app")
public class AppUpdateController {
	
	private static Logger logger = Logger.getLogger(MqttMessageServer.class);
	private String updateZipPath = "/home/app";
	private Gson gson = new Gson();
	private Properties properties = new Properties();
	
	/**
	 * 获取版本号信息
	 */
	@RequestMapping(value="/getVersionInfo")
	@ResponseBody
	public String getVersionInfo(String versionNumber){
		String updateFlag = "0";
		try {
			//获取某个路径下面的文件
			String[] versionNumberArray = versionNumber.trim().split("\\.");
			File updateAppFile = new File(updateZipPath);
		    File[] tempList = updateAppFile.listFiles();
			for (int i = 0; i < tempList.length; i++) {
				File tempFile = tempList[i];
				if(tempFile.getName().endsWith(".properties")){
					InputStream inputStream = new BufferedInputStream(new FileInputStream(tempFile));
					properties.load(inputStream);
					properties.getProperty("appVersion");
					String propertyStr = properties.getProperty("appVersion");
					logger.info("版本号信息:"+propertyStr);
					String[] propertyStrArray = propertyStr.split("\\.");
					for (int j = 0; j < propertyStrArray.length; j++) {
						Integer sendValue = Integer.valueOf(versionNumberArray[j]);
						Integer getValue = Integer.valueOf(propertyStrArray[j]);
						if(sendValue<getValue){
							updateFlag=  "1";
							break;
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info(">>>>>>>>返回值>>>>>>>>:"+updateFlag);
		return gson.toJson(updateFlag);
	}
	
	@RequestMapping(value="/getApkFile")
	public void getApkFile(HttpServletResponse response){
		//获取某个路径下面的文件
		InputStream apkInputStream = null;
		try {
			File updateAppFile = new File(updateZipPath);
		    File[] tempList = updateAppFile.listFiles();
			for (int i = 0; i < tempList.length; i++) {
				File tempFile = tempList[i];
				if(tempFile.getName().endsWith(".apk")){
					apkInputStream = new FileInputStream(tempFile);
				}
			}
			if(apkInputStream!=null){
				logger.info(">>>>>>>>下载APK文件>>>>>>>>");
				setFileDownloadHeader(response, "UpdateAppVersion.apk");
				IOUtils.copy(apkInputStream, response.getOutputStream());
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setFileDownloadHeader(HttpServletResponse response, String fileName) {
		try {
			//中文文件名支持
			String encodedfileName = new String(fileName.getBytes(), "ISO8859-1");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + "\"");
		} catch (UnsupportedEncodingException e) {
		}
	}
	
}
