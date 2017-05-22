package com.hdos.platform.common.util;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * 用户信息工具类
 * 
 * @author Arthur
 */
public final class UserUtils {

	/** 工具类 */
	private UserUtils() {
	}

	/**
	 * 判断是否有权限
	 * 
	 * @param name
	 *            权限名称
	 * @return 是否有权限
	 */
	public static boolean hasPermission(String name) {
		if (StringUtils.isEmpty(name)) {
			return true;
		}
		Subject subject = SecurityUtils.getSubject();
		return subject != null && subject.isPermitted(name);
	}

	/**
	 * 设置session属性
	 * 
	 * @param key
	 *            key
	 * @param value
	 *            value
	 */
	public static void setSessionAttribute(String key, String value) {
		Session session = SecurityUtils.getSubject().getSession(true);
		session.setAttribute(key, value);
	}

	/**
	 * 设置session属性
	 * 
	 * @param key
	 *            key
	 * @return 值
	 */
	public static Object getSessionAttribute(String key) {
		Session session = SecurityUtils.getSubject().getSession(true);
		return session.getAttribute(key);
	}
}
