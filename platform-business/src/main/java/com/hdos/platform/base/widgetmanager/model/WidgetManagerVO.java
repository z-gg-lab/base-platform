package com.hdos.platform.base.widgetmanager.model;

import com.hdos.platform.core.base.BaseVO;

public class WidgetManagerVO extends BaseVO {

	private static final long serialVersionUID = 1L;
	/**
	 * 控件Id
	 */
	private String widgetId;
	/**
	 * 控件名
	 */
	private String widgetName;
	/**
	 * 控件路径
	 */
	private String widgetPath;

	/**
	 * 创建者ID
	 */
	private String operatorId;
	/**
	 * 创建者
	 */
	private String operator;
	/**
	 * 控件版本
	 */
	private String widgetVersion;
	/**
	 * 旧版本
	 */
	private String oldWidgetVersion;
	/**
	 * 控件类型
	 */
	private String widgetType;
	/**
	 * 控件描述
	 * @return
	 */
	private String widgetExplain;
	/**
	 * 文件ID
	 * @return
	 */
	private String fileId;
	

	public String getWidgetId() {
		return widgetId;
	}

	public void setWidgetId(String widgetId) {
		this.widgetId = widgetId;
	}

	public String getWidgetName() {
		return widgetName;
	}

	public void setWidgetName(String widgetName) {
		this.widgetName = widgetName;
	}

	public String getWidgetPath() {
		return widgetPath;
	}

	public void setWidgetPath(String widgetPath) {
		this.widgetPath = widgetPath;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getWidgetVersion() {
		return widgetVersion;
	}

	public void setWidgetVersion(String widgetVersion) {
		this.widgetVersion = widgetVersion;
	}

	public String getOldWidgetVersion() {
		return oldWidgetVersion;
	}

	public void setOldWidgetVersion(String oldWidgetVersion) {
		this.oldWidgetVersion = oldWidgetVersion;
	}

	public String getWidgetType() {
		return widgetType;
	}

	public void setWidgetType(String widgetType) {
		this.widgetType = widgetType;
	}

	
	public String getWidgetExplain() {
		return widgetExplain;
	}

	public void setWidgetExplain(String widgetExplain) {
		this.widgetExplain = widgetExplain;
	}

	
	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

}
