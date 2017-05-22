package com.hdos.platform.base.component.model;

import com.hdos.platform.core.base.BaseVO;

/**
 * ExcelImportColumn对象
 * 
 * @author zhuw
 * @version 1.0
 */

public class ExcelImportColumnVO extends BaseVO {

	private static final long serialVersionUID = 1L;

	// EXCEL导入模板列ID
	private String excelImportColumnId;

	// EXCEL导入模板ID
	private String excelImportTemplateId;

	// 字段名
	private String columnName;

	// 列名
	private String fieldName;

	// 关联字段名
	private String relationfieldName;
	
	// 类型
	private String type;

	// 长度
	private String length;

	public String getRelationFieldName() {
		return relationfieldName;
	}

	public void setRelationFieldName(String relationfieldName) {
		this.relationfieldName = relationfieldName;
	}


	public String getExcelImportColumnId() {
		return excelImportColumnId;
	}

	public void setExcelImportColumnId(String excelImportColumnId) {
		this.excelImportColumnId = excelImportColumnId;
	}

	public String getExcelImportTemplateId() {
		return excelImportTemplateId;
	}

	public void setExcelImportTemplateId(String excelImportTemplateId) {
		this.excelImportTemplateId = excelImportTemplateId;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

}