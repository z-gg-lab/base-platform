package com.hdos.platform.common.cache.impl;

import java.util.Date;
import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.Maps;
import com.hdos.platform.common.cache.ICache;

/**
 * 内存缓存 实现
 * 
 * @author Arthur
 */
public class MapCache implements ICache {

	/** 内容存储 */
	private final ConcurrentMap<String, Object> dataStore = Maps.newConcurrentMap();

	/** 过期时间存储 */
	private final ConcurrentMap<String, Date> deadlineStore = Maps.newConcurrentMap();

	@Override
	public boolean put(String key, Object value, long timeout) {
		try {
			dataStore.put(key, value);
			deadlineStore.put(key, new Date(System.currentTimeMillis() + timeout));
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String key) {
		// TODO Auto-generated method stub
		Date deadline = deadlineStore.get(key);
		if (deadline != null && deadline.before(new Date(System.currentTimeMillis()))) {
			return null;
		}
		return (T) dataStore.get(key);
	}

	@Override
	public boolean delete(String key) {
		try {
			dataStore.remove(key);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
