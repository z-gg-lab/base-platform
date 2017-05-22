package com.hdos.platform.base.user.model;

import com.hdos.platform.core.base.BaseVO;

/**
 * 用户账号信息
 */
public class AccountVO extends BaseVO {
	private static final long serialVersionUID = 1L;

	/**
	 *  用户账号
	 */
	private String userAccount;
	
	/**
	 *  用户ID
	 */
	private String userId;
	
	/**
	 *  用户密码
	 */
	private String pwd;

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
}
