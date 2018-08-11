package com.fh.util.fileUploadDowload;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

public class ZipFileUtil {
	
	//缓存解压的文件
	private static List<File> cacheFileList = new ArrayList<File>();
	//缓存每次上传ZIP解压之后的文件夹路径
	private static List<String> cacheFilePathList = new ArrayList<String>();
	/**
	 * 解压上传的ZIP包
	 */
	public static List<File> resolveUploadZipFile(MultipartFile zipFile, String path) throws Exception {
		
		if (zipFile.isEmpty()) {
			throw new Exception("文件不能为空");
		}
		// 获取ZIP包的名字
		String zipFileName = zipFile.getOriginalFilename();
		int pos = zipFileName.lastIndexOf(".");
		// 后缀名
		String extName = zipFileName.substring(pos + 1).toLowerCase();
		// 判断上传文件必须是ZIP或者是RAR否则不允许上传
		if (!extName.equals("zip") && !extName.equals("rar")) {
			throw new Exception("上传文件格式错误，请重新上传");
		}
		
		// 根据服务器的文件保存地址和原文件名创建目录文件全路径
		File convertFile = new File(path  + "/" +zipFileName);
		if (convertFile.exists()) {
			//删除缓存的文件
			convertFile.delete();
			convertFile.mkdirs();
        }
		zipFile.transferTo(convertFile);
		// 解压文件的目录
		String descDir = path + "/" + zipFileName.substring(0, zipFileName.lastIndexOf(".")) + "/";
		if (extName.equals("zip")) {
			//删除存在的文件
			for (int i = 0; i < cacheFilePathList.size(); i++) {
				deleteUploadFolder(cacheFilePathList.get(i));
			}
			//删除之后清空缓存
			cacheFilePathList.clear();
			// 开始解压ZIP
			List<File> unZipFiles = unZipFiles(convertFile, descDir);
			//删除缓存的文件
			convertFile.delete();
			return unZipFiles;
		} else {
			throw new Exception("文件格式不正确不能解压");
		}
	}

	/**
	 * 解压文件到指定目录
	 */
	public static List<File> unZipFiles(File zipFile, String descDir) throws IOException {
		//每次清空前一次的缓存
		cacheFileList.clear();
		//添加解压的目录
		cacheFilePathList.add(descDir);
		
		ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
		ZipEntry zipEntry = null;
		FileOutputStream fos = null;
		File file = null;
		while ((zipEntry = zipInputStream.getNextEntry()) != null) {
			if (true == zipEntry.isDirectory()) {
				file = new File(zipEntry.getName());
				file.mkdir();
			} else {
				String outPath = (descDir+zipEntry.getName());
				System.err.println("解压压缩包中的文件:"+outPath);
				//判断路径是否存在,不存在则创建文件路径  
				File everyFile = new File(outPath.substring(0, outPath.lastIndexOf('/')));  
				if(!everyFile.exists()){ 
					everyFile.mkdirs();  
				} 
				File tempFile = new File(outPath);
				fos = new FileOutputStream(tempFile);
				byte[] buf = new byte[2048];
				int len = 0;
				while ((len = zipInputStream.read(buf, 0, buf.length)) != -1) {
					fos.write(buf, 0, len);
				}
				fos.close();
				cacheFileList.add(tempFile);
			}
			zipInputStream.closeEntry();
		}
		zipInputStream.close();
		return cacheFileList;
	}
	
	/**
	 * 删除文件夹 
	 */
    public  static void deleteUploadFolder(String folderPath) {  
        try {  
        	deleteFileAndSubDirectory(folderPath); 			// 删除完里面所有内容  
            File voidPath = new File(folderPath.toString());  
            voidPath.delete(); 								// 删除空文件夹  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }
	
	/**
	 * 删除某路径下面所有的文件和文件夹
	 */
	public static boolean deleteFileAndSubDirectory(String delpath) throws Exception {  
        boolean flag = false;  
        File file = new File(delpath);  
        if (!file.exists()) {  
            return flag;  
        }  
        if (!file.isDirectory()) {  
            return flag;  
        }  
        String[] tempList = file.list();  
        File temp = null;  
        for (int i = 0; i < tempList.length; i++) {  
            if (delpath.endsWith(File.separator)) {  
                temp = new File(delpath + tempList[i]);  
            } else {  
                temp = new File(delpath + File.separator + tempList[i]);  
            }  
            if (temp.isFile()) {  
                temp.delete();  
            }  
            if (temp.isDirectory()) {  
            	deleteFileAndSubDirectory(delpath + "/" + tempList[i]);	// 先删除文件夹里面的文件  
            	deleteUploadFolder(delpath + "/" + tempList[i]);		// 再删除空文件夹  
                flag = true;  
            }  
        }  
        return flag;
    }
	
	/**
	 * 压缩文件成ZIP包 
	 */
	public static void zipFile(List<File> files, ZipOutputStream outputStream) throws IOException, ServletException {
		try {
			int size = files.size();
			// 压缩列表中的文件
			for (int i = 0; i < size; i++) {
				File file = (File) files.get(i);
				zipFile(file, outputStream);
			}
		} catch (IOException e) {
			throw e;
		}
	}

	public static void zipFile(File inputFile, ZipOutputStream outputstream) throws IOException, ServletException {
		try {
			if (inputFile.exists()) {
				if (inputFile.isFile()) {
					FileInputStream inStream = new FileInputStream(inputFile);
					BufferedInputStream bInStream = new BufferedInputStream(inStream);
					ZipEntry entry = new ZipEntry(inputFile.getName());
					outputstream.putNextEntry(entry);

					final int MAX_BYTE = 10 * 1024 * 1024; 	// 最大的流为10M
					long streamTotal = 0; 					// 接受流的容量
					int streamNum = 0; 						// 流需要分开的数量
					int leaveByte = 0; 						// 文件剩下的字符数
					byte[] inOutbyte; 						// byte数组接受文件的数据

					streamTotal = bInStream.available(); 					// 通过available方法取得流的最大字符数
					streamNum = (int) Math.floor(streamTotal / MAX_BYTE); 	// 取得流文件需要分开的数量
					leaveByte = (int) streamTotal % MAX_BYTE; 				// 分开文件之后,剩余的数量

					if (streamNum > 0) {
						for (int j = 0; j < streamNum; ++j) {
							inOutbyte = new byte[MAX_BYTE];
							// 读入流,保存在byte数组
							bInStream.read(inOutbyte, 0, MAX_BYTE);
							outputstream.write(inOutbyte, 0, MAX_BYTE); 	// 写出流
						}
					}
					// 写出剩下的流数据
					inOutbyte = new byte[leaveByte];
					bInStream.read(inOutbyte, 0, leaveByte);
					outputstream.write(inOutbyte);
					// Closes the current ZIP entry and positions the stream for writing the next entry
					outputstream.closeEntry(); 
					bInStream.close(); // 关闭
					inStream.close();
				}
			} else {
				throw new ServletException("文件不存在！");
			}
		} catch (IOException e) {
			throw e;
		}
	}
	
	/**
	 * 弹出下载框，下载ZIP包 
	 */
	public static void downloadFile(File file, HttpServletResponse response, boolean isDelete) {
		try {
			// 以流的形式下载文件。
			BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String(file.getName().getBytes("UTF-8"), "ISO-8859-1"));
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
			if (isDelete) {
				file.delete(); // 是否将生成的服务器端文件删除
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
}
