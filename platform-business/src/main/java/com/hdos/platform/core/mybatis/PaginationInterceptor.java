package com.hdos.platform.core.mybatis;

import java.sql.Connection;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.RowBounds;

import com.hdos.platform.core.mybatis.Dialect.Type;

/**
 * 分页
 * 
 * @author Arthur
 * @version 1.0
 * @since 2016-6-1
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class PaginationInterceptor implements Interceptor {

	/** 数据库类型 */
	private Type databaseType = Dialect.Type.H2;

	/**
	 * 设值数据库类型
	 * 
	 * @param databaseType
	 *            数据库类型
	 */
	public void setDatabaseType(Dialect.Type databaseType) {
		this.databaseType = databaseType;
	}

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
		RowBounds rowBounds = (RowBounds) metaStatementHandler.getValue("delegate.rowBounds");
		if (rowBounds == null || rowBounds == RowBounds.DEFAULT) {
			return invocation.proceed();
		}
		Dialect dialect = null;
		switch (databaseType) {
		case MYSQL:
			dialect = new MySqlDialect();
			break;
		case ORACLE:
			dialect = new OracleDialect();
			break;
		case POSTGRESQL:
			dialect = new PostgreSqlDialect();
			break;
		case H2:
			dialect = new H2Dialect();
			break;
		default:
		}

		String originalSql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
		metaStatementHandler.setValue("delegate.boundSql.sql", dialect.getLimitString(originalSql, rowBounds.getOffset(), rowBounds.getLimit()));
		metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
		metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {

	}

}
