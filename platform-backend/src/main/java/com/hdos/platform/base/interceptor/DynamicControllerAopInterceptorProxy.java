package com.hdos.platform.base.interceptor;

import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * DynamicControllerInterceptor 拦截器
 * @author chenyang
 *
 */
public class DynamicControllerAopInterceptorProxy extends DynamicControllerInterceptor implements MethodInterceptor {
	/**
	 * {@inheritDoc}
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable {
		List<MethodInterceptor> interceptors = serviceInterceptors();
		return new DynamicReflectiveMethodInvocation(invocation, interceptors).proceed();
	}
}
