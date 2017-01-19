package com.hdos.platform.base.user.model;

import com.hdos.platform.core.base.BaseVO;


/**
 * UserToken对象
 * @author caicai
 * @version 1.0
 */

public class UserTokenVO extends BaseVO {

	private static final long serialVersionUID = 1L;
	
	
	/** 用户TOKEN*/
	private String token;
	
	/** 账号*/
	private String userAccount;
	
	
	public String getToken() {
		return  token;
	}

	public void setToken(String  token) {
		 this.token = token;
	}
	
	public String getUserAccount() {
		return  userAccount;
	}

	public void setUserAccount(String  userAccount) {
		 this.userAccount = userAccount;
	}
	
}