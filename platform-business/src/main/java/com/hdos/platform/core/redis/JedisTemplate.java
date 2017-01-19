package com.hdos.platform.core.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.BinaryJedisCommands;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.Pool;

/**
 * JedisTemplate 提供了一个template方法，负责对Jedis连接的获取与归还。 JedisAction<T> 和
 * JedisActionNoResult两种回调接口，适用于有无返回值两种情况。 同时提供一些最常用函数的封装
 * 
 * @param <T>
 *            xx
 * @param <BinaryJedis>
 *            xx
 */
public class JedisTemplate<T extends BinaryJedisCommands & JedisCommands> {

	/** LOG */
	private static Logger logger = LoggerFactory.getLogger(JedisTemplate.class);

	/** jEdis Pool */
	private final Pool<T> jedisPool;

	/**
	 * 构造函数
	 * 
	 * @param jedisPool
	 *            xx
	 */
	public JedisTemplate(Pool<T> jedisPool) {
		this.jedisPool = jedisPool;
	}

	/**
	 * 执行有返回结果的action。
	 * 
	 * @param jedisAction
	 *            xx
	 * @return xx
	 * @throws JedisException
	 *             xx
	 */
	protected <R> R execute(JedisAction<R, T> jedisAction) throws JedisException {
		T jedis = null;
		boolean broken = false;
		try {
			jedis = jedisPool.getResource();

			return jedisAction.action(jedis);
		} catch (JedisConnectionException e) {
			logger.error("Redis connection lost.", e);
			broken = true;
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	}

	/**
	 * 执行无返回结果的action。
	 * 
	 * @param jedisAction
	 *            xx
	 * @throws JedisException
	 *             xx
	 */
	protected void execute(JedisActionNoResult<T> jedisAction) throws JedisException {
		T jedis = null;
		boolean broken = false;
		try {
			jedis = jedisPool.getResource();
			jedisAction.action(jedis);
		} catch (JedisConnectionException e) {
			logger.error("Redis connection lost.", e);
			broken = true;
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	}

	/**
	 * 根据连接是否已中断的标志，分别调用returnBrokenResource或returnResource。
	 * 
	 * @param jedis
	 *            xx
	 * @param connectionBroken
	 *            xx
	 */
	protected void closeResource(T jedis, boolean connectionBroken) {
		if (jedis != null) {
			try {
				if (connectionBroken) {
					jedisPool.returnBrokenResource(jedis);
				} else {
					jedisPool.returnResource(jedis);
				}
			} catch (Exception e) {
				logger.error("Error happen when return jedis to pool, try to close it directly.", e);
			}
		}
	}

	/**
	 * 获取内部的pool做进一步的动作。
	 * 
	 * @return xx
	 */
	public Pool<T> getJedisPool() {
		return jedisPool;
	}

	/**
	 * 有返回结果的回调接口定义。
	 * 
	 * @param <R>
	 *            xx
	 * @param <T>
	 *            xx
	 */
	public interface JedisAction<R, T> {

		/**
		 * 执行并返回
		 * 
		 * @param jedis
		 *            xx
		 * @return xx
		 */
		R action(T jedis);
	}

	/**
	 * 无返回结果的回调接口定义。
	 * 
	 * @param <T>
	 *            xx
	 */
	public interface JedisActionNoResult<T> {

		/**
		 * 执行
		 * 
		 * @param jedis
		 *            xx
		 */
		void action(T jedis);
	}

	// ////////////// 常用方法的封装 ///////////////////////// //

	// ////////////// 公共 ///////////////////////////

	/**
	 * set value
	 * 
	 * @param key
	 *            key
	 * @param value
	 *            value
	 * @return xx
	 */
	public String set(final byte[] key, final byte[] value) {
		return execute(new JedisAction<String, T>() {
			@Override
			public String action(T jedis) {
				return jedis.set(key, value);
			}
		});
	}

	/**
	 * get by key
	 * 
	 * @param key
	 *            xx
	 * @return xx
	 */
	public byte[] get(final byte[] key) {
		return execute(new JedisAction<byte[], T>() {
			@Override
			public byte[] action(T jedis) {
				return jedis.get(key);
			}
		});
	}

	/**
	 * 删除key, 如果key存在返回true, 否则返回false。
	 * 
	 * @param key
	 *            xx
	 * @return xx
	 */
	public boolean del(final byte[] key) {
		return execute(new JedisAction<Boolean, T>() {
			@Override
			public Boolean action(T jedis) {
				return jedis.del(key) == 1;
			}
		});
	}

	/**
	 * set value
	 * 
	 * @param key
	 *            key
	 * @param value
	 *            value
	 * @param timeout
	 *            timeout in milliseconds
	 * @return xx
	 */
	public void set(final String key, final String value, final long timeout) {
		execute(new JedisActionNoResult<T>() {
			@Override
			public void action(T jedis) {
				jedis.set(key, value, null, "PX", timeout);
			}
		});
	}

	/**
	 * exists
	 * 
	 * @param key
	 *            key
	 * @param value
	 *            value
	 * @param timeout
	 *            timeout in milliseconds
	 * @return xx
	 */
	public boolean exists(final String key) {
		return execute(new JedisAction<Boolean, T>() {
			@Override
			public Boolean action(T jedis) {
				return jedis.exists(key);
			}
		});
	}

	/**
	 * expire
	 * 
	 * @param key
	 *            key
	 * @param timeout
	 *            timeout in milliseconds
	 * @return xx
	 */
	public boolean expire(final String key, final long timeout) {
		return execute(new JedisAction<Boolean, T>() {
			@Override
			public Boolean action(T jedis) {
				return jedis.pexpire(key, timeout) == 1;
			}
		});
	}

	/**
	 * ttl
	 * 
	 * @param key
	 *            key
	 * @return xx
	 */
	public long ttl(final String key) {
		return execute(new JedisAction<Long, T>() {
			@Override
			public Long action(T jedis) {
				return jedis.ttl(key);
			}
		});
	}

	/**
	 * set value to hash
	 * 
	 * @param key
	 *            key
	 * @param field
	 *            field
	 * @param value
	 *            value
	 * @return xx
	 */
	public void hset(final String key, final String field, final String value) {
		execute(new JedisActionNoResult<T>() {
			@Override
			public void action(T jedis) {
				jedis.hset(key, field, value);
			}
		});
	}

	/**
	 * set value to hash
	 * 
	 * @param key
	 *            key
	 * @param hash
	 *            hash
	 * @return xx
	 */
	public void hmset(final String key, final Map<String, String> hash) {
		execute(new JedisActionNoResult<T>() {
			@Override
			public void action(T jedis) {
				jedis.hmset(key, hash);
			}
		});
	}

	/**
	 * get by key
	 * 
	 * @param key
	 *            xx
	 * @param field
	 *            xx
	 * @return xx
	 */
	public String hget(final String key, final String field) {
		return execute(new JedisAction<String, T>() {
			@Override
			public String action(T jedis) {
				return jedis.hget(key, field);
			}
		});
	}

	/**
	 * get by fields
	 * 
	 * @param key
	 *            xx
	 * @param fields
	 *            xx
	 * @return xx
	 */
	public List<String> hmget(final String key, final String... fields) {
		return execute(new JedisAction<List<String>, T>() {
			@Override
			public List<String> action(T jedis) {
				return jedis.hmget(key, fields);
			}
		});
	}

	/**
	 * get all by key
	 * 
	 * @param key
	 *            xx
	 * @return xx
	 */
	public Map<String, String> hgetAll(final String key) {
		return execute(new JedisAction<Map<String, String>, T>() {
			@Override
			public Map<String, String> action(T jedis) {
				return jedis.hgetAll(key);
			}
		});
	}

	/**
	 * 删除key, 如果key存在返回true, 否则返回false。
	 * 
	 * @param key
	 *            xx
	 * @return xx
	 */
	public boolean hdel(final String key, final String... fields) {
		return execute(new JedisAction<Boolean, T>() {
			@Override
			public Boolean action(T jedis) {
				return jedis.hdel(key, fields) == 1;
			}
		});
	}

	/**
	 * set value to list
	 * 
	 * @param key
	 *            key
	 * @param member
	 *            member
	 * @return xx
	 */
	public void sadd(final String key, final String... member) {
		execute(new JedisActionNoResult<T>() {
			@Override
			public void action(T jedis) {
				jedis.sadd(key, member);
			}
		});
	}

	/**
	 * get value list
	 * 
	 * @param key
	 *            key
	 * @param member
	 *            member
	 * @return xx
	 */
	public Set<String> smembers(final String key) {
		return execute(new JedisAction<Set<String>, T>() {
			@Override
			public Set<String> action(T jedis) {
				return jedis.smembers(key);
			}
		});
	}

	/**
	 * remove from set
	 * 
	 * @param key
	 *            xx
	 * @param members
	 *            xx
	 * @return xx
	 */
	public boolean srem(final String key, final String... members) {
		return execute(new JedisAction<Boolean, T>() {
			@Override
			public Boolean action(T jedis) {
				return jedis.srem(key, members) == 1;
			}
		});
	}

	/**
	 * count set
	 * 
	 * @param key
	 *            xx
	 * @param members
	 *            xx
	 * @return xx
	 */
	public int scard(final String key) {
		return execute(new JedisAction<Integer, T>() {
			@Override
			public Integer action(T jedis) {
				return jedis.scard(key).intValue();
			}
		});
	}
}
