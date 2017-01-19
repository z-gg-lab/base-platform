package com.hdos.platform.core.base;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseMapper<T> {

	/**
	 * 查询列表
	 * 
	 * @param condition
	 *            条件
	 * @return 列表
	 */
	List<T> list(@Param("condition") Map<String, Object> condition);

	/**
	 * 查询分页
	 * 
	 * @param condition
	 *            条件
	 * @return 列表
	 */
	List<T> list(@Param("condition") Map<String, Object> condition, @Param("rowBounds") RowBounds rowBounds);

	/**
	 * 查询个数
	 * 
	 * @param condition
	 *            条件
	 * @return 个数
	 */
	int count(@Param("condition") Map<String, Object> condition);

	/**
	 * 查询 id
	 * 
	 * @param id
	 *            主键
	 * @return 对象
	 */
	T getById(String id);

	/**
	 * 添加
	 * 
	 * @param entity
	 *            对象
	 */
	void insert(T entity);

	/**
	 * 更新
	 * 
	 * @param entity
	 *            对象
	 */
	void update(T entity);

	/**
	 * 根据主键删除
	 * 
	 * @param id
	 *            主键
	 * @return 成功条数
	 */
	int delete(String id);

	/**
	 * 根据主键批量删除
	 * 
	 * @param id
	 *            主键
	 * @return 成功条数
	 */
	int deleteInBulk(String... ids);
}
