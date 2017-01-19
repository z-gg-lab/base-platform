package com.hdos.platform.base.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hdos.platform.base.utils.IpUtils;
import com.hdos.platform.core.shiro.UserProfile;

/**
 * UserInfoFilter Filter
 * 用于在没请求时，在线程中设置用户信息
 * @author chenyang
 *
 */
public class UserInfoFilter extends OncePerRequestFilter {

	public UserInfoFilter() {
	}

	/**
	 * {@inheritDoc}
	 */
	protected void initFilterBean() throws ServletException {
		
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	        throws ServletException, IOException {

		//在主题中获取用户信息，如果获取不到，那么就返回IP地址
		Subject userSubject = SecurityUtils.getSubject();
		LoginUserInfo loginUserInfoVO = new LoginUserInfo();
		if(userSubject.getPrincipal() != null) {
			UserProfile userProfile = (UserProfile)userSubject.getPrincipal();
			loginUserInfoVO.setUserId(userProfile.getUserId());
			loginUserInfoVO.setUserName(userProfile.getName());
			loginUserInfoVO.setUserAccount(userProfile.getAccount());
			
		} else {
			loginUserInfoVO = new LoginUserInfo();
		}
		//设置客户端IP
		loginUserInfoVO.setIp(IpUtils.getIpAddr(request));
		
		//把用户信息设置到当前线程中
		LoginContextProcessor.setUserVOTread(loginUserInfoVO);
		try {
			filterChain.doFilter(request, response);
		} finally {
			LoginContextProcessor.setUserVOTread(null);
		}
	}

}
