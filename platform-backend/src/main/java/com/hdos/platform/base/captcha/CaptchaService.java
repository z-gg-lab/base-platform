package com.hdos.platform.base.captcha;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import com.hdos.platform.common.util.CacheUtils;
import com.hdos.platform.common.util.ConfigContants;
import com.hdos.platform.common.util.ConfigUtils;

/**
 * CaptchaService
 * @author tkpad
 *
 */
public class CaptchaService {

	public boolean isCaptchaNeeded(ServletRequest request) {
		// TODO: 从配置读取 验证码开关 和 免验证码失败次数
		// 20160901 开启验证码
		boolean captchaEnabled = true;// "true".equalsIgnoreCase(ConfigUtils.get("captchaEnabled"));

		// TODO: 从其他地方读取已经登录失败的次数
		int failureCount = this.getFailureCount(request);

		return captchaEnabled && failureCount >= ConfigContants.FAILURETHREHOLDWITHOUTCAPTCHA;
	}

	public boolean validateCaptcha(HttpServletRequest request, String captcha) {
		return captcha != null && captcha.equalsIgnoreCase((String) request.getSession().getAttribute(CaptchaServlet.KEY_CAPTCHA));
	}

	private String getCacheKey(ServletRequest request) {
		String username = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
		return "login-failure-count:" + username;
	}

	private int getFailureCount(ServletRequest request) {
		Integer count = CacheUtils.get(this.getCacheKey(request));
		return count != null ? count.intValue() : 0;
	}

	public void addFailureCount(ServletRequest request) {
		String cacheKey = this.getCacheKey(request);
		Integer count = CacheUtils.get(cacheKey);
		int newCount = (count != null ? count.intValue() : 0) + 1;
		CacheUtils.put(cacheKey, newCount, 5 * 60 * 1000);
	}

	public void clearFailureCount(ServletRequest request) {
		CacheUtils.delete(this.getCacheKey(request));
	}
}
