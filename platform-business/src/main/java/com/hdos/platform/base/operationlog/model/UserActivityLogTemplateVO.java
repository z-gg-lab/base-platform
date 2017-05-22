package com.hdos.platform.base.operationlog.model;

/**
 * 用户活动日志PO
 * 
 * @author matao
 * @date 2016年6月18日
 */
public class UserActivityLogTemplateVO {

	/**
	 * 日志模板配置ID
	 */
	private String logcfgId;

	/**
	 * 日志模板配置业务唯一ID，采用类名+方法
	 */
	private String logcfgMark;

	/**
	 * 日志模板配置的操作名称
	 */
	private String logcfgOper;

	/**
	 * 日志模板配置的操作成功模板
	 */
	private String logcfgSuccess;

	/**
	 * 日志配置操作失败模型
	 */
	private String logcfgFailed;

	/**
	 * 系统标记
	 */
	private String systemMark;

	/**
	 * 日志模板状态
	 */
	private int logcfgStatus;

	public String getLogcfgId() {
		return logcfgId;
	}

	public void setLogcfgId(String logcfgId) {
		this.logcfgId = logcfgId;
	}

	public String getLogcfgMark() {
		return logcfgMark;
	}

	public void setLogcfgMark(String logcfgMark) {
		this.logcfgMark = logcfgMark;
	}

	public String getLogcfgOper() {
		return logcfgOper;
	}

	public void setLogcfgOper(String logcfgOper) {
		this.logcfgOper = logcfgOper;
	}

	public String getLogcfgSuccess() {
		return logcfgSuccess;
	}

	public void setLogcfgSuccess(String logcfgSuccess) {
		this.logcfgSuccess = logcfgSuccess;
	}

	public String getLogcfgFailed() {
		return logcfgFailed;
	}

	public void setLogcfgFailed(String logcfgFailed) {
		this.logcfgFailed = logcfgFailed;
	}

	public String getSystemMark() {
		return systemMark;
	}

	public void setSystemMark(String systemMark) {
		this.systemMark = systemMark;
	}

	public int getLogcfgStatus() {
		return logcfgStatus;
	}

	public void setLogcfgStatus(int logcfgStatus) {
		this.logcfgStatus = logcfgStatus;
	}

}
