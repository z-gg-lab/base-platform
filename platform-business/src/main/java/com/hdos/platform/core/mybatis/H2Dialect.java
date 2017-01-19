package com.hdos.platform.core.mybatis;

/**
 * H2分页条件
 * 
 * @author Arthur
 * @version 1.0
 * @since 2016-6-1
 */
public class H2Dialect extends Dialect {

	@Override
	public String getLimitString(String sql, int offset, int limit) {
		return sql.replaceAll("[\r\n]", " ").replaceAll("\\s{2,}", " ") + " limit " + offset + " ," + limit;
	}
}
