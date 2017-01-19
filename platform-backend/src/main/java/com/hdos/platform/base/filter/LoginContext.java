package com.hdos.platform.base.filter;


/**
 * 登录上下文
 * @author chenyang
 *
 */
public abstract class LoginContext {
	
	protected static final ThreadLocal<LoginUserInfo> USERVO_THREAD = new ThreadLocal<LoginUserInfo>(); 
	
	public static LoginUserInfo getCurrentUser() {
		return USERVO_THREAD.get();
	}

}
	