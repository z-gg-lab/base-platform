package com.hdos.platform.base.captcha;

import org.apache.shiro.authc.AuthenticationException;

/**
 * CaptchaException
 * @author chenyang
 *
 */
public class CaptchaException extends AuthenticationException {

	/** serialVersionUID */
	private static final long serialVersionUID = 750393774797096991L;

	public CaptchaException() {
		super();
	}

	public CaptchaException(String message, Throwable cause) {
		super(message, cause);
	}

	public CaptchaException(String message) {
		super(message);
	}
}