package com.hdos.platform.base.captcha;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

/**
 * CaptchaValidateFilter
 * @author chenyang
 *
 */
public class CaptchaValidateFilter extends AccessControlFilter {

	public static final String DEFAULT_CAPTCHA_PARAM = "captcha";

	private String captchaParam = DEFAULT_CAPTCHA_PARAM;

	// TODO: service 初始化
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

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {

		HttpServletRequest req = WebUtils.toHttp(request);
		// 页面可以根据该属性来决定是否显示验证码
		boolean isCaptchaNeeded = captchaService.isCaptchaNeeded(req);
		request.setAttribute("isCaptchaNeeded", isCaptchaNeeded);

		// 2、判断验证码是否禁用 或不是表单提交（允许访问）
		if (!isCaptchaNeeded || !"post".equalsIgnoreCase(req.getMethod())) {
			return true;
		}
		// 3、此时是表单提交，验证验证码是否正确
		String submitted = this.getCaptcha(request);
		return captchaService.validateCaptcha(req, submitted);
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		// 如果验证码失败了，存储失败key属性
		request.setAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, CaptchaException.class.getName());
		return true;
	}
}
