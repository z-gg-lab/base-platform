package com.hdos.platform.base.operationlog.model;

import java.io.Serializable;

/**
 * 用户活动日志查询条件VO
 * 
 * @author matao
 * @date 2016年6月18日
 */
public class UserActivityLogQueryCondition implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 3748473476254163944L;

	private String logOper;
	private String logUser;
	private String logAddress;
	private String logTime1;
	private String logTime2;
	private String logStatus;

	public String getLogOper() {
		return logOper;
	}

	public void setLogOper(String logOper) {
		this.logOper = logOper;
	}

	public String getLogUser() {
		return logUser;
	}

	public void setLogUser(String logUser) {
		this.logUser = logUser;
	}

	public String getLogAddress() {
		return logAddress;
	}

	public void setLogAddress(String logAddress) {
		this.logAddress = logAddress;
	}

	public String getLogTime1() {
		return logTime1;
	}

	public void setLogTime1(String logTime1) {
		this.logTime1 = logTime1;
	}

	public String getLogTime2() {
		return logTime2;
	}

	public void setLogTime2(String logTime2) {
		this.logTime2 = logTime2;
	}

	public String getLogStatus() {
		return logStatus;
	}

	public void setLogStatus(String logStatus) {
		this.logStatus = logStatus;
	}

}
