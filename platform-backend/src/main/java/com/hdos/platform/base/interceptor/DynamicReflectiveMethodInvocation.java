package com.hdos.platform.base.interceptor;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Dynamic ReflectiveMethodInvocation
 * @author chenyang
 *
 */
class DynamicReflectiveMethodInvocation implements MethodInvocation {

	private MethodInvocation invokerInvocation;

	/**
	 * List of MethodInterceptor and InterceptorAndDynamicMethodMatcher that
	 * need dynamic checks.
	 */
	private final List<MethodInterceptor> interceptorsAndDynamicMethodMatchers;

	/**
	 * Index from 0 of the current interceptor we're invoking. -1 until we
	 * invoke: then the current interceptor.
	 */
	private int currentInterceptorIndex = -1;

	/**
	 * @param invocation
	 * @param interceptorsAndDynamicMethodMatchers
	 */
	public DynamicReflectiveMethodInvocation(MethodInvocation invocation,
	        List<MethodInterceptor> interceptorsAndDynamicMethodMatchers) {
		this.invokerInvocation = invocation;
		this.interceptorsAndDynamicMethodMatchers = interceptorsAndDynamicMethodMatchers;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object[] getArguments() {
		return invokerInvocation.getArguments();
	}

	/**
	 * {@inheritDoc}
	 */
	public AccessibleObject getStaticPart() {
		return invokerInvocation.getStaticPart();
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getThis() {
		return invokerInvocation.getThis();
	}

	/**
	 * {@inheritDoc}
	 */
	public Object proceed() throws Throwable {
		// We start with an index of -1 and increment early.
		if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
			return invokerInvocation.proceed();
		}

		MethodInterceptor interceptor = this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);

		// It's an interceptor, so we just invoke it: The pointcut will have
		// been evaluated statically before this object was constructed.
		return interceptor.invoke(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public Method getMethod() {
		return invokerInvocation.getMethod();
	}

}
