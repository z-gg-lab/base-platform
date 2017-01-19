package com.hdos.platform.base.uploader.controller;

import java.io.Serializable;

import com.hdos.platform.common.util.ConfigContants;
import com.hdos.platform.common.util.ConfigUtils;

/**
 * 上传文件配置
 * @author chenyang
 *
 */
public class FileUploadConfig implements Serializable {
	
	private static final long serialVersionUID = 8807045514579647104L;

	/** 文件存储路径 */
	private static String localPath = ConfigUtils.get(ConfigContants.LOCAL_FILE_PATH);
	
	/** 文件存储路径 */
	private static String netPath = ConfigUtils.get(ConfigContants.FTP_UPLOAD_PATH);
	
	/** 允许上传文件大小 */
	private static String maxSize;
	
	/** 允许上传文件类型 */
	private static String fileType;

	public static String getNetPath() {
		return netPath;
	}

	public static void setNetPath(String netPath) {
		FileUploadConfig.netPath = netPath;
	}

	public static String getLocalPath() {
		return localPath;
	}

	public static void setLocalPath(String newlocalPath) {
		localPath = newlocalPath;
	}


	public static String getMaxSize() {
		return maxSize;
	}

	public static void setMaxSize(String newmaxSize) {
		maxSize = newmaxSize;
	}

	public static String getFileType() {
		return fileType;
	}

	public static void setFileType(String newfileType) {
		fileType = newfileType;
	}
	
}
