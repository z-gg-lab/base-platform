/**
 * 
 */
package com.hdos.platform.base.index.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hdos.platform.base.department.controller.DepartmentController;
import com.hdos.platform.base.filter.LoginContext;
import com.hdos.platform.common.util.ConfigUtils;
import com.hdos.platform.common.util.CookieContants;

/**
 * @author chenyang
 * 
 */
@Controller
public class IndexController {
	
	private final Logger logger = LoggerFactory.getLogger(IndexController.class);
	/**
	 * 首页
	 * 
	 * @return view
	 */
	@RequestMapping(value = { "", "/", "/index" })
	public String login(HttpServletResponse response,Model model) {
		model.addAttribute("currentUser", LoginContext.getCurrentUser());
		// 进入首页表示登录成功，保存cookie
		Cookie cookie;
		model.addAttribute("systemName",ConfigUtils.get("SYSTEM_NAME"));
		model.addAttribute("copyrightInfo",ConfigUtils.get("COPYRIGHT_INFO"));
		try {
			cookie = new Cookie(CookieContants.COOKIE_NAME,URLEncoder.encode(LoginContext.getCurrentUser().getUserAccount(), "utf-8"));
			cookie.setMaxAge(60*60*1000);
			response.addCookie(cookie);
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return "index";
	}
}
