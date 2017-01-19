package com.hdos.platform.common.util;

import com.hdos.platform.core.jpa.IdentifierGenerator;
import com.hdos.platform.core.jpa.UUIDHexGenerator;

/**
 * 主键工具类
 * 
 * @author Arthur
 */
public final class PrimaryKeyUtils {

	/** 主键生成器 */
	private static IdentifierGenerator generator = new UUIDHexGenerator();

	/** 工具类 */
	private PrimaryKeyUtils() {
	}

	/**
	 * mysql模糊查询时，如果查询关键字本身包含_和%，需要转义
	 * 
	 * @param queryKey
	 *            查询关键字
	 * @return 转义字符
	 */
	public static String generate(Object o) {
		return generator.generate(o).toString();
	}
}
