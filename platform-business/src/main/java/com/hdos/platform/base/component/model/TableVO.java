package com.hdos.platform.base.component.model;

import java.io.Serializable;

/**
 * Table对象
 *
 * @author zhuw
 *
 */
public class TableVO implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/** 字段名 */
	private String columnName;
	/** 备注 */
	private String columnComment;
	/** 数据类型*/
	private String dataType;
	/** 数据长度*/
	private String dataLength;

	/** 排序 */
	private  int sortNo;
	public int getSortNo() {
		return sortNo;
	}
	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}
	public String getDataLength() {
		return dataLength;
	}

	public void setDataLength(String dataLength) {
		this.dataLength = dataLength;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnComment() {
		return columnComment;
	}

	public void setColumnComment(String columnComment) {
		this.columnComment = columnComment;
	}

}
