package com.hdos.platform.base.component.model;

import com.hdos.platform.core.base.BaseVO;

/**
 * ExcelImportTemplate对象
 * 
 * @author zhuw
 * @version 1.0
 */

public class ExcelImportTemplateVO extends BaseVO {

	private static final long serialVersionUID = 1L;

	/** EXCEL导入模板ID */
	private String excelImportTemplateId;

	/** 表名 */
	private String tableName;

	/** 模板名 */
	private String templateName;

	/** 校验规则 */
	private String rule;
	
	/** excel唯一标识*/
	private String excelKey;

	/** 配置 */
	private String config;

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public String getExcelImportTemplateId() {
		return excelImportTemplateId;
	}

	public void setExcelImportTemplateId(String excelImportTemplateId) {
		this.excelImportTemplateId = excelImportTemplateId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getExcelKey() {
		return excelKey;
	}

	public void setExcelKey(String excelKey) {
		this.excelKey = excelKey;
	}

	
}