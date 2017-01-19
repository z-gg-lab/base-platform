package com.hdos.platform.base.captcha;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hdos.platform.base.operationlog.service.UserActivityLogService;
import com.hdos.platform.base.user.model.LoginUserVO;
import com.hdos.platform.base.utils.IpUtils;
import com.hdos.platform.core.shiro.UserProfile;

/**
 * CustomFormAuthenticationFilter
 * @author chenyang
 *
 */
public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {
	private static final Logger logger = LoggerFactory.getLogger(CustomFormAuthenticationFilter.class);
	// TODO: service 初始化
	private final CaptchaService captchaService = new CaptchaService();
	
	@Autowired
	private UserActivityLogService userActivityLogService;

	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
		captchaService.clearFailureCount(request);
		recordUserLogin(subject, request);
		return super.onLoginSuccess(token, subject, request, response);
	}

	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
		captchaService.addFailureCount(request);
		return super.onLoginFailure(token, e, request, response);
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		// 是否需要验证码
		request.setAttribute("isCaptchaNeeded", captchaService.isCaptchaNeeded(request));
		// 验证码验证失败则不再继续
		if (request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME) != null) {
			return true;
		}
		return super.onAccessDenied(request, response, mappedValue);
	}
	
	private void recordUserLogin(Subject subject, ServletRequest request) {
		try {
			UserProfile userProfile = (UserProfile)subject.getPrincipal();
			LoginUserVO loginUserVO = new LoginUserVO();
			loginUserVO.setUserId(userProfile.getUserId());
			loginUserVO.setUserName(userProfile.getName());
			loginUserVO.setUserAccount(userProfile.getAccount());
			loginUserVO.setIp(IpUtils.getIpAddr((HttpServletRequest)request));
			userActivityLogService.recordUserLog(loginUserVO, "登录系统", "登录系统成功", true);
		} catch(Exception e) {
			logger.info("Exception",e);
		}
	}
}
