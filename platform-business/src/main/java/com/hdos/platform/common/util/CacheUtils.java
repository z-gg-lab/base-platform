package com.hdos.platform.common.util;

import org.springframework.util.Assert;

import com.hdos.platform.common.cache.ICache;
import com.hdos.platform.common.cache.impl.MapCache;
import com.hdos.platform.core.SpringContextHolder;

/**
 * 缓存缓存
 * 
 * @author Arthur
 */
public final class CacheUtils {

	/** 缓存管理 */
	private static ICache cache;

	/**
	 * 私有构造函数，禁止实例化
	 */
	private CacheUtils() {
	}

	/**
	 * @return 缓存实现
	 */
	private static ICache getCacheImpl() {
		if (null == cache) {
			cache = SpringContextHolder.getBean("cacheJedis");
		}
		if (null == cache) {
			cache = new MapCache();
		}
		Assert.notNull(cache, "缓存类没有找到默认实现");
		return cache;
	}

	/**
	 * 缓存并设置超时时间
	 * 
	 * @param key
	 *            key
	 * @param value
	 *            value
	 * @param timeout
	 *            超时毫秒数
	 * @return 是否成功
	 */
	public static boolean put(String key, Object value, long timeout) {
		return getCacheImpl().put(key, value, timeout);
	}

	/**
	 * 缓存
	 * 
	 * @param key
	 *            key
	 * @param value
	 *            value
	 * @return 是否成功
	 */
	public static boolean put(String key, Object value) {
		return getCacheImpl().put(key, value,ConfigContants.CACHE_TIMEOUT);
	}

	/**
	 * 获取缓存
	 * 
	 * @param key
	 *            key
	 * @return value
	 */
	public static <T> T get(String key) {
		return getCacheImpl().get(key);
	}

	/**
	 * 删除缓存
	 * 
	 * @param key
	 *            key
	 * @return 是否成功
	 */
	public static boolean delete(String key) {
		return getCacheImpl().delete(key);
	}
}
