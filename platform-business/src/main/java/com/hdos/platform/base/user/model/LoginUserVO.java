package com.hdos.platform.base.user.model;


/**
 * 用户信息
 * @author chenyang
 *
 */
public class LoginUserVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 用户ID*/
	private String userId;
	
	/** 用户昵称*/
	private String nickName;
	
	/** 用户姓名*/
	private String userName;
	
	/** 用户帐号*/
	private String userAccount;
	
	/** 用户IP*/
	private String ip;
	
	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
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
