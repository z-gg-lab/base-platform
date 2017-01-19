package com.hdos.platform.core.shiro;

import java.io.Serializable;

/**
 * 用户信息类
 * 
 * @author Arthur
 */
public class UserProfile implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 4233496923304975404L;

	/**
	 * 用户ID,内部唯一标识
	 */
	private String userId;

	/** 账户名 */
	private String account;

	/** 用户名 */
	private String name;

	/**
	 * 信息类
	 */
	public UserProfile() {
		super();
	}

	/**
	 * 信息类
	 * 
	 * @param userId
	 *            用户ID
	 * @param account
	 *            账户名
	 * @param name
	 *            用户名
	 */
	public UserProfile(String userId, String account, String name) {
		super();
		this.userId = userId;
		this.account = account;
		this.name = name;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
