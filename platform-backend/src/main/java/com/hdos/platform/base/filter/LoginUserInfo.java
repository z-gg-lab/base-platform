package com.hdos.platform.base.filter;

import java.io.Serializable;

/**
 * 当前登录用户信息
 * @author chenyang
 *
 */
public class LoginUserInfo implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -7616932673049086977L;

	/**
	 * 用户Id
	 */
	private String userId;

	/**
	 * 用户名称
	 */
	private String userName;
	
	/**
	 * 用户账户
	 */
	private String userAccount;

	/**
	 * IP
	 */
	private String ip;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}


}
