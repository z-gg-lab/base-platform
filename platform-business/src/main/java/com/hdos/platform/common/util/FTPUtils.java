package com.hdos.platform.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FTP工具类
 * @author Administrator
 *
 */
public class FTPUtils {

	private static Logger logger = LoggerFactory.getLogger(FTPUtils.class);
	
	private static final String FTP_IP = "ftpIp";
	private static final String FTP_PORT = "ftpPort";
	private static final String FTP_ACCOUT = "ftpAccout";
	private static final String FTP_PWD = "ftpPassword";
	
	/**
	 * 从FTP下载文件
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static File downloadFromFtp(String path) throws IOException {

		FTPClient ftpClient = new FTPClient();
		
		File localFile = null;
		
		//读取properties文件
		Properties properties = new Properties();
		InputStream is = ClassLoader.getSystemResourceAsStream("ftp.properties");
		properties.load(is); 
		
		String ftpIp = properties.getProperty(FTP_IP);
		String ftpPort = properties.getProperty(FTP_PORT);
		String ftpAccout = properties.getProperty(FTP_ACCOUT);
		String ftpPassword = properties.getProperty(FTP_PWD);
		
		OutputStream ios = null;
		
		try {

			ftpClient.setControlEncoding("UTF-8");
			ftpClient.setConnectTimeout(30000);

			ftpClient.connect(ftpIp, Integer.parseInt(ftpPort));

			//设置Linux环境
            FTPClientConfig conf = new FTPClientConfig( FTPClientConfig.SYST_UNIX);
            
            ftpClient.configure(conf);
			
            // 登录
			boolean loginResult = ftpClient.login(ftpAccout, ftpPassword);
			
			System.out.println(loginResult);
			
			if (loginResult) {
				ftpClient.setBufferSize(1024);
				// 设置文件类型（二进制）
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
				localFile = new File(path.substring(path.lastIndexOf("/")));
				ios = new FileOutputStream(localFile);     
				ftpClient.retrieveFile(path, ios);
			}
		} catch (IOException e) {
			logger.info(e.getMessage());
		} finally {
			try {
				ftpClient.disconnect();
				if (ios != null) {
					ios.close();
				}
			} catch (IOException e) {
				logger.info(e.getMessage());
			}
		}
		return localFile;
	}
	
	
	/**
	 * 从FTP下载文件
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static InputStream downloadFromFtP(String path) throws IOException {

		FTPClient ftpClient = new FTPClient();
		
		//读取properties文件
		Properties properties = new Properties();
		InputStream is = ClassLoader.getSystemResourceAsStream("ftp.properties");
		properties.load(is); 
		
		String ftpIp = properties.getProperty(FTP_IP);
		String ftpPort = properties.getProperty(FTP_PORT);
		String ftpAccout = properties.getProperty(FTP_ACCOUT);
		String ftpPassword = properties.getProperty(FTP_PWD);
		
		InputStream in = null;

		try {

			ftpClient.setControlEncoding("UTF-8");
			ftpClient.setConnectTimeout(30000);

			ftpClient.connect(ftpIp, Integer.parseInt(ftpPort));

			boolean loginResult = ftpClient.login(ftpAccout, ftpPassword);// 登录

            
            //设置linux环境
            FTPClientConfig conf = new FTPClientConfig( FTPClientConfig.SYST_UNIX);
            ftpClient.configure(conf);
			
			System.out.println(loginResult);
			
			if (loginResult) {
				ftpClient.setBufferSize(1024);
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
				// 设置文件类型（二进制）
				in = ftpClient.retrieveFileStream(path);
				
			}
		} catch (IOException e) {
			logger.info(e.getMessage());
		} finally {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				logger.info(e.getMessage());
			}
		}
		return in;
	}
}
