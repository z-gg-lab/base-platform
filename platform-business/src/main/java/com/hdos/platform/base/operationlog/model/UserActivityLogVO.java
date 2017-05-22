package com.hdos.platform.base.operationlog.model;

import java.util.Date;

/**
 * 用户活动日志PO
 * 
 * @author matao
 * @date 2016年6月18日
 */
public class UserActivityLogVO {
	private String logId;
	private String logOper;
	private String logContent;
	private String logAddress;
	private String logUserid;
	private String logUser;
	private Date logTime;
	private int logStatus;
	private String systemMark;
	private String logOrgId;

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getLogOper() {
		return logOper;
	}

	public void setLogOper(String logOper) {
		this.logOper = logOper;
	}

	public String getLogContent() {
		return logContent;
	}

	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}

	public String getLogAddress() {
		return logAddress;
	}

	public void setLogAddress(String logAddress) {
		this.logAddress = logAddress;
	}

	public String getLogUserid() {
		return logUserid;
	}

	public void setLogUserid(String logUserid) {
		this.logUserid = logUserid;
	}

	public String getLogUser() {
		return logUser;
	}

	public void setLogUser(String logUser) {
		this.logUser = logUser;
	}

	public Date getLogTime() {
		return logTime;
	}

	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}

	public int getLogStatus() {
		return logStatus;
	}

	public void setLogStatus(int logStatus) {
		this.logStatus = logStatus;
	}

	public String getSystemMark() {
		return systemMark;
	}

	public void setSystemMark(String systemMark) {
		this.systemMark = systemMark;
	}

	public String getLogOrgId() {
		return logOrgId;
	}

	public void setLogOrgId(String logOrgId) {
		this.logOrgId = logOrgId;
	}

}
