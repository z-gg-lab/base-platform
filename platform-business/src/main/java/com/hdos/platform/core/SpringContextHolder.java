package com.hdos.platform.core;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

/**
 * 持有 Spring 上下文
 * 
 * @author Arthur
 */
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {

	/** Spring 上下文 */
	private static ApplicationContext context = null;

	@Override
	public void destroy() throws Exception {
		clear();
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// 不重复设置
		if (context == null) {
			SpringContextHolder.setContext(applicationContext);
		}
	}
	
	public static void setContext(ApplicationContext applicationContext) {
		context = applicationContext;
	}

	/**
	 * @return 上下文
	 */
	public static ApplicationContext getApplicationContext() {
		Assert.notNull(context, "The applicationContext should not be null");
		return context;
	}

	/**
	 * 清除上下文
	 */
	public static void clear() {
		context = null;
	}

	/**
	 * 根据名称获取Bean对象
	 * 
	 * @param name
	 *            名称
	 * @return 对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		Assert.notNull(context, "The applicationContext should not be null");
		return (T) context.getBean(name);
	}

	/**
	 * 根据名称和类型获取Bean对象
	 * 
	 * @param name
	 *            名称
	 * @param clazz
	 *            类型
	 * @return 对象
	 */
	public static <T> T getBean(String name, Class<T> clazz) {
		Assert.notNull(context, "The applicationContext should not be null");
		return context.getBean(name, clazz);
	}

	/**
	 * 根据类型获取Bean对象
	 * 
	 * @param clazz
	 *            类型
	 * @return 对象
	 */
	public static <T> T getBean(Class<T> clazz) {
		Assert.notNull(context, "The applicationContext should not be null");
		return context.getBean(clazz);
	}
}
