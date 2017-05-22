package com.hdos.platform.base.fileuploader.model;

import java.io.Serializable;

import com.hdos.platform.core.base.BaseVO;

/**
 * FileVO
 * @author Administrator
 */
public class FileVO extends BaseVO implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 2119764446273916845L;

	/** 文件ID */
	private String fileId;
	
	/** 文件名称 */
	private String fileName;

	/** 文件类型 */
	private String fileType;

	/** 文件大小 */
	private String fileSize;

	/** 文件路径 */
	private String discPath;
	
	/** 创建人 */
	private String creater;

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getDiscPath() {
		return discPath;
	}

	public void setDiscPath(String discPath) {
		this.discPath = discPath;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}
}
