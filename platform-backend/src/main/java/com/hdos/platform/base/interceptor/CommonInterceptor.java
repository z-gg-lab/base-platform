package com.hdos.platform.base.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.hdos.platform.common.util.ConfigContants;
import com.hdos.platform.common.util.ConfigUtils;

public class CommonInterceptor extends HandlerInterceptorAdapter {
	private final Logger logger = LoggerFactory.getLogger(CommonInterceptor.class);

	/**
	 * 拦截所有的请求 打印请求的信息
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
	//	String cookieToken = null;
		String url = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()
				+ request.getServletPath();
	//	int hashToken = url.hashCode();
		String configDebug = ConfigUtils.get(ConfigContants.IS_DEBUG);
		if ("true".equals(configDebug)) {
			if (!url.contains("/statics")) {
				if (logger.isInfoEnabled()) {
					logger.info("请求的Url:" + url);
					logger.info("对应的控制器以及方法:" + handler.toString());
				}
			}
		}
		// 对静态资源不拦截
		if (url.contains("/statics")) {
			return super.preHandle(request, response, handler);
		}
		if(request.getServletPath().equals("/statics/base/error/404.html")||request.getServletPath().equals("/statics/base/error/500.html")||request.getServletPath().equals("/login")) {
            return super.preHandle(request, response, handler);
        }
		// 取出cookie中对应key的值
//		Cookie[] cookie1 = request.getCookies();
//		if (cookie1 != null) {
//			for (int i = 0; i < cookie1.length; i++) {
//				Cookie cook = cookie1[i];
//				if (cook != null && cook.getName().equalsIgnoreCase(String.valueOf(hashToken))) { // 获取键
//					cookieToken = cook.getValue().toString(); // 获取值
//
//				}
//			}
//		}
		// cookieToken = request.getParameter("clientToken");
		//Subject subject = SecurityUtils.getSubject();
		//Object sessionToken = subject.getSession().getAttribute(String.valueOf(hashToken));
		// Cookie中的token为空或者cookie中的token与session中的token相等
//		if (sessionToken == null || cookieToken == null || sessionToken.equals(cookieToken)) {
//			String Token = UUID.randomUUID().toString();
//			// 并将cookie与session重新赋值
//			Cookie cookie = new Cookie(String.valueOf(hashToken), Token);
//			cookie.setMaxAge(15 * 60 * 1000);
//			cookie.setDomain(request.getServletPath());
//			cookie.setPath(url);
//			cookie.setSecure(true);
//			response.addCookie(cookie);
//			subject.getSession().setAttribute(String.valueOf(hashToken), Token);
			return super.preHandle(request, response, handler);
//		}
//		return false;
//
	}
}
