package com.hdos.platform.base.filter;


public class LoginContextProcessor extends LoginContext {
	
	public static void setUserVOTread(LoginUserInfo loginUserInfoVO) {
		USERVO_THREAD.set(loginUserInfoVO);
	}
}
