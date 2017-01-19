package com.hdos.platform.base.baseController;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;

@Controller
public class InterceptorHandle {
	public static Boolean handleRequest(HttpServletRequest request, HttpServletResponse response) {
		// 调用了该方法，则给cookie，session赋值
		String Token = UUID.randomUUID().toString();
		String url = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()
				+ request.getServletPath();
		int hashToken = url.hashCode();
		Cookie cookie = new Cookie(String.valueOf(hashToken), Token);
		cookie.setMaxAge(15 * 60 * 1000);
		cookie.setPath(url);
		cookie.setDomain(request.getServletPath());
		cookie.setSecure(true);
		response.addCookie(cookie);
		Subject subject = SecurityUtils.getSubject();
		subject.getSession().setAttribute(String.valueOf(hashToken), Token);
		return true;
	}
}
