package com.hdos.platform.base.operationlog.model;

import java.util.Date;

/**
 * 日志定义PO
 * 
 * @author matao
 * @date 2016年6月19日
 */
public class UserLogConfigVO {
	private String logcfgId;
	private String logcfgMark;
	private String logcfgOper;
	private String logcfgDesc;
	private int logcfgStatus;

	private Date createTime;

	private Date updateTime;

	private String systemMark;

	private String logcfgSuccess;
	private String logcfgFailed;
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
	public String getLogcfgDesc() {
		return logcfgDesc;
	}
	public void setLogcfgDesc(String logcfgDesc) {
		this.logcfgDesc = logcfgDesc;
	}
	public int getLogcfgStatus() {
		return logcfgStatus;
	}
	public void setLogcfgStatus(int logcfgStatus) {
		this.logcfgStatus = logcfgStatus;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getSystemMark() {
		return systemMark;
	}
	public void setSystemMark(String systemMark) {
		this.systemMark = systemMark;
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


}
