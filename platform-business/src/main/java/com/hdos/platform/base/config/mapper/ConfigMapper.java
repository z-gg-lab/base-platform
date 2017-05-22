package com.hdos.platform.base.config.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hdos.platform.base.config.model.ConfigVO;
import com.hdos.platform.core.base.BaseMapper;

/**
 * 配置项
 * 
 * @author Arthur
 */
@Repository
public interface ConfigMapper extends BaseMapper<ConfigVO> {

	/**
	 * 查询 key
	 * 
	 * @param key
	 *            配置key
	 * @return 对象
	 */
	ConfigVO getByKey(String key);

	/**
	 * 查询key的子节点
	 * @param key
	 * @return
	 */
	List<ConfigVO> getSubByKey(String key);
	
	/**
	 * 根据主键级联删除
	 * 
	 * @param id
	 *            主键
	 * @return 对象
	 */
	void deleteCascade(String id);
	
	/**
	 * 根据父节点CODE查找所有子节点
	 * @param fullCode
	 * @return
	 */
	List<ConfigVO> getByFullCode(String fullCode);
	
	/**
	 * 根据key更新value
	 * @param key
	 */
	void updateValueByKey(String value,String key);
}
