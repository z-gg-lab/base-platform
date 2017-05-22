package com.hdos.platform.common.cache.impl;

import redis.clients.jedis.BinaryJedisCommands;
import redis.clients.jedis.JedisCommands;
import redis.clients.util.Pool;

import com.google.common.base.Charsets;
import com.hdos.platform.common.cache.ICache;
import com.hdos.platform.core.redis.JedisTemplate;
import com.hdos.platform.core.shiro.SerializeUtils;

/**
 * Jedis缓存实现
 * 
 * @param <T>
 *            xx
 * @param <BinaryJedis>
 *            xx
 */
public class JedisCache<T extends BinaryJedisCommands & JedisCommands> extends JedisTemplate<T> implements ICache {

	/**
	 * 构造函数
	 * 
	 * @param jedisPool
	 *            xx
	 */
	public JedisCache(Pool<T> jedisPool) {
		super(jedisPool);
	}

	/**
	 * 获取key的字节数组
	 * 
	 * @param key
	 *            key
	 * @return 字节数组
	 */
	private byte[] getKeyBytes(final String key) {
		return key.getBytes(Charsets.UTF_8);
	}

	@Override
	public boolean put(String key, Object value, final long timeout) {
		final byte[] keyBytes = this.getKeyBytes(key);
		final byte[] valueBytes = SerializeUtils.serialize(value);
		return execute(new JedisAction<Boolean, T>() {
			@Override
			public Boolean action(T jedis) {
				String result = jedis.set(keyBytes, valueBytes);
				jedis.pexpire(keyBytes, timeout);
				return "OK".equalsIgnoreCase(result);
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V> V get(String key) {
		final byte[] keyBytes = this.getKeyBytes(key);
		return (V) SerializeUtils.deserialize(super.get(keyBytes));
	}

	@Override
	public boolean delete(String key) {
		final byte[] keyBytes = this.getKeyBytes(key);
		return super.del(keyBytes);
	}
}
