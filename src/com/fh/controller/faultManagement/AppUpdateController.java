package com.fh.controller.faultManagement;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

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
	private String updateZipPath = "D:\\AppUpdateVersion.zip";
	private Gson gson = new Gson();
	/**
	 * 获取版本号信息
	 */
	@RequestMapping(value="/getVersionInfo")
	@ResponseBody
	public String getVersionInfo(String versionNumber){
		String updateFlag = "0";
		//获取某个路径下面的文件
		String[] versionNumberArray = versionNumber.trim().split("\\.");
		File updateAppFile = new File(updateZipPath);
		try(ZipFile zipFile = new ZipFile(updateAppFile);
			InputStream inputStream = new BufferedInputStream(new FileInputStream(updateAppFile)); 
			ZipInputStream zipInputStream = new ZipInputStream(inputStream);) {
			ZipEntry zipEntry; 
			while ((zipEntry = zipInputStream.getNextEntry()) != null) { 
				if (zipEntry.isDirectory()) {
					logger.info("文件名:" + zipEntry.getName());
				}
				String fileName = zipEntry.getName();
				if(fileName.endsWith(".properties")){
					logger.info("文件名:" + fileName + ",大小:"+ zipEntry.getSize() + "Bytes."); 
					Properties properties = new Properties();
					properties.load(zipFile.getInputStream(zipEntry));
					String propertyStr = properties.getProperty("appVersion");
					String[] propertyStrArray = propertyStr.split("\\.");
					for (int i = 0; i < propertyStrArray.length; i++) {
						Integer sendValue = Integer.valueOf(versionNumberArray[i]);
						Integer getValue = Integer.valueOf(propertyStrArray[i]);
						if(sendValue<getValue){
							updateFlag=  "1";
							break;
						}
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return gson.toJson(updateFlag);
	}
	
	@RequestMapping(value="/getApkFile")
	public void getApkFile(HttpServletResponse response){
		//获取某个路径下面的文件
		InputStream apkInputStream = null;
		File updateAppFile = new File(updateZipPath);
		try(ZipFile zipFile = new ZipFile(updateAppFile);
			InputStream inputStream = new BufferedInputStream(new FileInputStream(updateAppFile)); 
			ZipInputStream zipInputStream = new ZipInputStream(inputStream);) {
			ZipEntry zipEntry; 
			while ((zipEntry = zipInputStream.getNextEntry()) != null) { 
				String fileName = zipEntry.getName();
				if (zipEntry.isDirectory()) {
					System.err.println("不是APK文件");
				}else if(fileName.endsWith(".properties")){
					System.err.println("文件名:" + zipEntry.getName());
				}else{
					apkInputStream = zipFile.getInputStream(zipEntry);
				}
			}
			setFileDownloadHeader(response, "UpdateAppVersion.apk");
			IOUtils.copy(apkInputStream, response.getOutputStream());
			apkInputStream.close();
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
