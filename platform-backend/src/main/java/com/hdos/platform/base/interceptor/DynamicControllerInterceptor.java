package com.hdos.platform.base.interceptor;

import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;

import com.hdos.platform.base.interceptor.MethodInterceptorTarget;

/**
 * 
 * @author chenyang
 *
 */
public class DynamicControllerInterceptor {
	private static final MethodInterceptorTarget target = new MethodInterceptorTarget();

	public static Runnable registerServiceInterceptor(MethodInterceptor methodInterceptor) {
		return target.registerServiceInterceptor(methodInterceptor);
	}

	protected List<MethodInterceptor> serviceInterceptors() {
		return target.serviceInterceptors();
	}
}
