package com.hdos.platform.common.util;

import java.util.List;

import com.hdos.platform.base.config.model.ConfigVO;
import com.hdos.platform.base.config.service.ConfigService;
import com.hdos.platform.core.SpringContextHolder;

/**
 * 配置缓存
 * 
 * @author Arthur
 */
public final class ConfigUtils {

	/**
	 * 私有构造函数，禁止实例化
	 */
	private ConfigUtils() {
	}

	/** 配置管理 */
	private static ConfigService configService;

	/**
	 * @return 配置管理对象
	 */
	private static ConfigService getConfigService() {
		if (null == configService) {
			configService = SpringContextHolder.getBean(ConfigService.class);
		}
		return configService;
	}

	/**
	 * 获取缓存key
	 * 
	 * @param key
	 *            配置key
	 * @return 缓存key
	 */
	public static String getCacheKey(String key) {
		return "CONFIG_CACHE:" + key;
	}

	/**
	 * 获取配置项的值
	 * 
	 * @param key
	 *            配置项的键
	 * @return 配置项的值
	 */
	public static String get(String key) {
		final String cacheKey = getCacheKey(key);
		String resultValue = CacheUtils.get(cacheKey);
		if (null == resultValue) {
			ConfigVO vo = getConfigService().findByKey(key);
			if (null != vo) {
				resultValue = vo.getConfigValue();
				CacheUtils.put(cacheKey, resultValue);
			}
		}
		return resultValue;
	}

	/**
	 * 根据key获取所有子节点
	 * 
	 * @param key
	 * @return
	 */
	public static List<ConfigVO> getList(String key) {
		// final String cacheKey = getCacheKey(key);
		// String resultValue = getJedisTemplate().get(cacheKey);
		// if (null == resultValue) {
		List<ConfigVO> list = getConfigService().findSubConfigsByKey(key);
		// if( null != list) {
		// resultValue = list;
		// getJedisTemplate().se
		// }
		// }
		return list;
	}
}
