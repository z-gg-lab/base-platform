package com.hdos.platform.common.util;

import org.apache.commons.lang.StringUtils;

/**
 * SQL字符串工具类
 * 
 * @author Arthur
 */
public final class SqlUtils {

	/** 工具类 */
	private SqlUtils() {
	}

	/**
	 * mysql模糊查询时，如果查询关键字本身包含_和%，需要转义
	 * 
	 * @param queryKey
	 *            查询关键字
	 * @return 转义字符
	 */
	public static String likeClause(final String keyword) {
		return "%" + escape(keyword) + "%";
	}

	/**
	 * mysql模糊查询时，如果查询关键字本身包含_和%，需要转义
	 * 
	 * @param queryKey
	 *            查询关键字
	 * @return 转义字符
	 */
	public static String startWithClause(final String keyword) {
		return escape(keyword) + "%";
	}

	/**
	 * mysql模糊查询时，如果查询关键字本身包含_和%，需要转义
	 * 
	 * @param queryKey
	 *            查询关键字
	 * @return 转义字符
	 */
	public static String endWithClause(final String keyword) {
		return "%" + escape(keyword);
	}

	/**
	 * mysql模糊查询时，如果查询关键字本身包含_和%，需要转义
	 * 
	 * @param queryKey
	 *            查询关键字
	 * @return 转义字符
	 */
	public static String escape(final String queryKey) {
		String[] encodeArr = { "_", "%" };
		String rst = queryKey;
		for (String encode : encodeArr) {
			rst = StringUtils.replace(rst, encode, "/" + encode);
		}
		return rst;
	}
}
