package com.hdos.platform.base.operationlog.model;

import java.io.Serializable;

/**
 * 日志定义查询条件
 * 
 * @author matao
 * @date 2016年6月18日
 */
public class UserLogConfigCondition implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -8270313967412745464L;

	private String logcfgOper;
	private String logcfgMark;
	private String logcfgStatus;

	public String getLogcfgOper() {
		return logcfgOper;
	}

	public void setLogcfgOper(String logcfgOper) {
		this.logcfgOper = logcfgOper;
	}

	public String getLogcfgMark() {
		return logcfgMark;
	}

	public void setLogcfgMark(String logcfgMark) {
		this.logcfgMark = logcfgMark;
	}

	public String getLogcfgStatus() {
		return logcfgStatus;
	}

	public void setLogcfgStatus(String logcfgStatus) {
		this.logcfgStatus = logcfgStatus;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return "LogManageQueryCondition [logcfgOper=" + logcfgOper + ", logcfgMark=" + logcfgMark + ", logcfgStatus="
		        + logcfgStatus + "]";
	}

}
