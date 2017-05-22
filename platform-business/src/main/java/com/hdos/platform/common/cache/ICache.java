package com.hdos.platform.common.cache;

/**
 * 缓存接口
 * 
 * @author Arthur
 */
public interface ICache {

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
	public boolean put(String key, Object value, long timeout);

	/**
	 * 获取缓存
	 * 
	 * @param key
	 *            key
	 * @return value
	 */
	public <T> T get(String key);

	/**
	 * 删除缓存
	 * 
	 * @param key
	 *            key
	 * @return 是否成功
	 */
	public boolean delete(String key);
}
