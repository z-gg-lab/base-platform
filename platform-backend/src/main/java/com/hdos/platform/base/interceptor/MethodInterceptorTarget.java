package com.hdos.platform.base.interceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;

/**
 * 
 * @author chenyang
 *
 */
public class MethodInterceptorTarget {
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private final List<MethodInterceptorHandler> handlers = new ArrayList<MethodInterceptorHandler>();
	private volatile List<MethodInterceptor> usingHandlers;

	public Runnable registerServiceInterceptor(MethodInterceptor methodInterceptor) {

		// Found Order
		Order order = AnnotationUtils.findAnnotation(methodInterceptor.getClass(), Order.class);

		// Pick order value.
		int orderValue = order == null ? Ordered.LOWEST_PRECEDENCE : order.value();

		MethodInterceptorHandler handler = new MethodInterceptorHandler();
		handler.methodInterceptor = methodInterceptor;
		handler.orderValue = orderValue;

		lock.writeLock().lock();
		try {
			handlers.add(handler);

			MethodInterceptorHandler[] interceptorHandlers = handlers.toArray(new MethodInterceptorHandler[0]);

			// Sort By orderValue
			Arrays.sort(interceptorHandlers, new SortComparator());

			ArrayList<MethodInterceptor> arrayList = new ArrayList<MethodInterceptor>();
			for (MethodInterceptorHandler thandler : interceptorHandlers) {
				arrayList.add(thandler.methodInterceptor);
			}

			usingHandlers = Collections.unmodifiableList(arrayList);
		} finally {
			lock.writeLock().unlock();
		}
		return handler;
	}

	public List<MethodInterceptor> serviceInterceptors() {
		List<MethodInterceptor> tmp;
		lock.readLock().lock();
		try {
			tmp = usingHandlers;
		} finally {
			lock.readLock().unlock();
		}
		return tmp == null ? Collections.<MethodInterceptor> emptyList() : tmp;
	}

	class MethodInterceptorHandler implements Runnable {

		int orderValue;
		MethodInterceptor methodInterceptor;

		/**
		 * {@inheritDoc}
		 */
		public void run() {
			lock.writeLock().lock();
			try {
				handlers.remove(this);

				MethodInterceptorHandler[] interceptorHandlers = handlers.toArray(new MethodInterceptorHandler[0]);

				// Sort By orderValue
				Arrays.sort(interceptorHandlers, new SortComparator());

				ArrayList<MethodInterceptor> arrayList = new ArrayList<MethodInterceptor>();
				for (MethodInterceptorHandler thandler : interceptorHandlers) {
					arrayList.add(thandler.methodInterceptor);
				}

				usingHandlers = Collections.unmodifiableList(arrayList);
			} finally {
				lock.writeLock().unlock();
			}
		}
	}

	static class SortComparator implements Comparator<MethodInterceptorHandler> {

		/**
		 * {@inheritDoc}
		 */
		public int compare(MethodInterceptorHandler o1, MethodInterceptorHandler o2) {
			return o1.orderValue - o2.orderValue;
		}

	}
}
