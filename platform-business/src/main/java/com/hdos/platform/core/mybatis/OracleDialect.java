package com.hdos.platform.core.mybatis;

/**
 * oracle分页条件
 * 
 * @author Arthur
 * @version 1.0
 * @since 2016-6-1
 */
public class OracleDialect extends Dialect {

	@Override
	public String getLimitString(String sql, int offset, int limit) {
		String strSQL = sql.trim();
		StringBuffer pagingSelect = new StringBuffer(strSQL.length() + 100);
		pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
		pagingSelect.append(strSQL);
		pagingSelect.append(" ) row_ ) where rownum_ > " + offset + " and rownum_ <= " + (offset + limit));
		return pagingSelect.toString();
	}
}
