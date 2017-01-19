/**
 * 
 */
package com.hdos.platform.base.user.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hdos.platform.base.baseController.InterceptorHandle;
import com.hdos.platform.base.captcha.CaptchaException;
import com.hdos.platform.base.captcha.CaptchaService;
import com.hdos.platform.base.captcha.CaptchaServlet;
import com.hdos.platform.base.filter.LoginContext;
import com.hdos.platform.base.filter.LoginUserInfo;
import com.hdos.platform.base.operationlog.service.UserActivityLogService;
import com.hdos.platform.base.user.model.LoginUserVO;
import com.hdos.platform.common.util.CacheUtils;
import com.hdos.platform.common.util.ConfigUtils;
import com.hdos.platform.common.util.CookieContants;
import com.hdos.platform.core.shiro.UserProfile;

/**
 * 
 * @author chenyang
 *
 */
@Controller
public class LoginController {

	/** 日志类 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private UserActivityLogService userActivityLogService;

	public static final String DEFAULT_CAPTCHA_PARAM = "captcha";

	private String captchaParam = DEFAULT_CAPTCHA_PARAM;

	private final CaptchaService captchaService = new CaptchaService();

	public void setCaptchaParam(String captchaParam) {
		this.captchaParam = captchaParam;
	}

	public String getCaptchaParam() {
		return captchaParam;
	}

	protected String getCaptcha(ServletRequest request) {
		return WebUtils.getCleanParam(request, getCaptchaParam());
	}

	/**
	 * 登录页面请求
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(HttpServletResponse response, Model model) {
		if (this.isLoggedIn()) {
			return "forward:/index";
		} else {
			model.addAttribute("copyrightInfo", ConfigUtils.get("COPYRIGHT_INFO"));
			model.addAttribute("systemName", ConfigUtils.get("SYSTEM_NAME"));
			return "login";
		}
	}
	@RequestMapping(value = "/changeTheme", method = RequestMethod.POST)
	public void theme(HttpServletRequest request,HttpServletResponse response, Model model) {
		String theme = "";
		if(request.getParameter("theme")!=null) {
		theme = request.getParameter("theme");
		}
		Cookie cookie = new Cookie("110110",theme);
		cookie.setMaxAge(15 * 60 * 1000);
		response.addCookie(cookie);
		cookie.setSecure(true);
	}
	/**
	 * 登录页面异步请求
	 * 
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/loginajax", method = RequestMethod.GET)
	public String loginajax(HttpServletRequest request, HttpServletResponse response, Model model)
			throws UnsupportedEncodingException {

		Cookie cookies[] = request.getCookies();
		if (cookies != null) {
			// cookie 中文转码
			for (int i = 0; i < cookies.length; i++) {
				if (CookieContants.COOKIE_NAME.equals(URLDecoder.decode(cookies[i].getName(), "utf-8"))) {
					model.addAttribute("username", URLDecoder.decode(cookies[i].getValue(), "utf-8"));
				}
			}
		}

		return "loginajax";
	}

	/**
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/preclickmenu")
	@ResponseBody
	public String preClickMenu() {
		return "true";
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param username
	 * @param password
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/doLoginajax", method = RequestMethod.GET)
	@ResponseBody
	public String loginajax(HttpServletRequest request, HttpServletResponse response, String username, String password,
			Model model) throws UnsupportedEncodingException {

		// 页面可以根据该属性来决定是否显示验证码
		boolean isCaptchaNeeded = captchaService.isCaptchaNeeded(request);

		String submitted = this.getCaptcha(request);

		// 验证码是否正确
		if (isCaptchaNeeded) {
			if (submitted == null
					|| !submitted.equalsIgnoreCase((String) CacheUtils.get((CaptchaServlet.KEY_CAPTCHA)))) {
				return "yzm";
			}
		}

		Cookie cookies[] = request.getCookies();

		// 判断是否是当前登录用户
		if (cookies != null) {
			// cookie 中文转码
			for (int i = 0; i < cookies.length; i++) {
				if (CookieContants.COOKIE_NAME.equals(URLDecoder.decode(cookies[i].getName(), "utf-8"))) {
					if (!username.equals(URLDecoder.decode(cookies[i].getValue(), "utf-8"))) {
						model.addAttribute("errorMsg", "请输入当前的用户名");
						return "cookie";
					}
				}
			}
		}

		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);

			// 重置cookie的有效期
			Cookie[] oldCookies = request.getCookies();
			if (oldCookies != null) {
				for (Cookie cookie : oldCookies) {
					cookie.setMaxAge(60 * 60 * 1000);
				}
			}
			// 清空错误次数
			captchaService.clearFailureCount(request);
			return "succ";
		} catch (UnknownAccountException e) {
			LOGGER.info("用户名或密码错误！", e.getMessage());
			captchaService.addFailureCount(request);
			return "fail";
		} catch (IncorrectCredentialsException e) {
			LOGGER.info("用户名或密码错误！", e.getMessage());
			captchaService.addFailureCount(request);
			return "fail";
		} catch (CaptchaException e) {
			LOGGER.info("验证码错误！", e.getMessage());
			captchaService.addFailureCount(request);
			return "fail";
		} catch (AuthenticationException e) {
			LOGGER.info("用户名或密码错误！", e.getMessage());
			captchaService.addFailureCount(request);
			return "fail";
		}

	}

	/**
	 * 登录之后，真正登录的POST请求由Filter完成
	 * 
	 * @param username
	 *            username
	 * @param request
	 *            request
	 * @param response
	 *            response
	 * @param model
	 *            model
	 * @return 路径
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String username,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("systemName", ConfigUtils.get("SYSTEM_NAME"));
		model.addAttribute("copyrightInfo", ConfigUtils.get("COPYRIGHT_INFO"));
		if (this.isLoggedIn()) {
			LoginUserInfo loginUserInfo = LoginContext.getCurrentUser();
			recordUserLoginAndOut(loginUserInfo, "登录系统", "登录系统成功", true);
			return "forward:/index";
		}
		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
		// 分析错误原因
		String msg = this.parseException(request, username);
		model.addAttribute("errorMsg", msg);
		return "login";
	}

	/**
	 * 登出
	 * 
	 * @return
	 */
	@RequestMapping(value = "/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Subject subject = SecurityUtils.getSubject();
		LoginUserInfo loginUserInfo = LoginContext.getCurrentUser();
		// if (null != subject.getPrincipal() && subject.isAuthenticated()) {
		subject.logout();
		recordUserLoginAndOut(loginUserInfo, "退出系统", "退出系统成功", true);
		// 清空cookie
		Cookie cookies[] = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				cookie.setMaxAge(0);
			}
		}
		// }
		return "redirect:/login";
	}

	private void recordUserLoginAndOut(LoginUserInfo loginUserInfo, String operateType, String content,
			boolean success) {
		try {
			LoginUserVO loginUserVO = new LoginUserVO();
			if (loginUserInfo != null) {
				loginUserVO.setIp(loginUserInfo.getIp());
				loginUserVO.setUserAccount(loginUserInfo.getUserAccount());
				loginUserVO.setUserId(loginUserInfo.getUserId());
				loginUserVO.setUserName(loginUserInfo.getUserName());
				userActivityLogService.recordUserLog(loginUserVO, operateType, content, success);
			}
		} catch (Exception e) {
			LOGGER.info("登录时记录日志失败。", e);
		}
	}

	/**
	 * @return 是否已登录
	 */
	private boolean isLoggedIn() {
		Subject subject = SecurityUtils.getSubject();
		UserProfile profile = (UserProfile) subject.getPrincipal();
		return null != profile && subject.isAuthenticated() || subject.isRemembered();
	}

	/**
	 * 解析登录错误
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param username
	 *            username
	 * @return 错误文字信息
	 */
	private String parseException(HttpServletRequest request, String username) {
		String errorString = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		Class<?> error = null;
		try {
			if (errorString != null) {
				error = Class.forName(errorString);
			}
		} catch (ClassNotFoundException e) {
			LOGGER.error(e.getMessage());
		}
		String msg = "其他错误！";
		if (error != null) {
			if (error.equals(UnknownAccountException.class)) {
				msg = "用户名或密码错误！";
			} else if (error.equals(IncorrectCredentialsException.class)) {
				msg = "用户名或密码错误！";
			} else if (error.equals(CaptchaException.class)) {
				msg = "验证码错误！";
			} else if (error.equals(AuthenticationException.class)) {
				msg = "用户名或密码错误！";
			} else if (error.equals(DisabledAccountException.class)) {
				msg = "登录帐号被锁定，请稍后再试！";
			}
			LoginUserInfo loginUserInfo = LoginContext.getCurrentUser();
			loginUserInfo.setUserAccount(username);
			recordUserLoginAndOut(loginUserInfo, "登录系统", "登录系统失败，原因：" + msg, false);
		}
		return "登录失败，" + msg;
	}
}
