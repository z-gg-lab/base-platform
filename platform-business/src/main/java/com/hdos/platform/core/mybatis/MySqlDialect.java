package com.hdos.platform.core.mybatis;

/**
 * mysql分页条件
 * 
 * @author Arthur
 * @version 1.0
 * @since 2016-6-1
 */
public class MySqlDialect extends Dialect {

	@Override
	public String getLimitString(String sql, int offset, int limit) {
		return sql.replaceAll("[\r\n]", " ").replaceAll("\\s{2,}", " ") + " limit " + offset + " ," + limit;
	}

}
