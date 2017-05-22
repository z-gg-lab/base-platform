package com.hdos.platform.base.config.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.hdos.platform.base.config.mapper.ConfigMapper;
import com.hdos.platform.base.config.model.ConfigVO;
import com.hdos.platform.common.page.Page;
import com.hdos.platform.common.page.PageImpl;
import com.hdos.platform.common.util.CacheUtils;
import com.hdos.platform.common.util.ConfigUtils;
import com.hdos.platform.common.vo.TreeVO;
import com.hdos.platform.core.base.BaseService;

/**
 * 配置管理
 * 
 * @author chenyang
 */
@Service
@Transactional
public class ConfigService extends BaseService<ConfigVO> {

	/** 配置管理 */
	@Autowired
	private ConfigMapper configMapper;

	/**
	 * 保存操作
	 * 
	 * @param vo
	 *            对象
	 * @return 主键
	 */
	public String save(ConfigVO vo) {

		vo.setCreateTime(new Timestamp(System.currentTimeMillis()));
		vo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		
		if (StringUtils.isNotBlank(vo.getId())) {
			configMapper.update(vo);
		} else {
			// 新增
			vo.setId(generateKey(vo));
			String pid = vo.getParentId();
			if (null == pid) {
				pid = TreeVO.DEFAULT_ROOT_NODE_ID;
				vo.setParentId(pid);
			}
			// 改变父节点的叶子标识
			this.refreshLeafMark(pid, 1);
			// 插入子节点
			configMapper.insert(vo);
		}
		CacheUtils.put(ConfigUtils.getCacheKey(vo.getConfigKey()), vo.getConfigValue());
		return vo.getId();
	}

	/**
	 * 刷新目标结点的叶子结点标识
	 * 
	 * @param id
	 *            主键
	 * @param increment
	 *            目标结点的子节点增量
	 */
	private void refreshLeafMark(String id, int increment) {
		ConfigVO vo = configMapper.getById(id);
		if (null != vo) {
			int oldMark = vo.getLeafMark();
			// 查找子节点数目
			Map<String, Object> condition = Maps.newHashMap();
			condition.put("parentId", id);
			boolean hasChildren = configMapper.count(condition) + increment > 0;
			vo.setLeafMark(hasChildren ? ConfigVO.LEAF_NO : ConfigVO.LEAF_YES);
			if (vo.getLeafMark() != oldMark) {
				vo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				configMapper.update(vo);
			}
		}
	}

	/**
	 * 查询列表
	 * 
	 * @param condition
	 *            条件
	 * @return 列表
	 */
	public List<ConfigVO> find(Map<String, Object> condition) {
		return configMapper.list(condition);
	}

	/**
	 * 分页查询
	 * 
	 * @param condition
	 *            条件
	 * @param pageNumber
	 *            页码，从 1 开始
	 * @param pageSize
	 *            每页条数
	 * @return
	 */
	public Page<ConfigVO> findPage(Map<String, Object> condition, int pageNumber, int pageSize) {
		int total = configMapper.count(condition);
		RowBounds rowBounds = new RowBounds((pageNumber - 1) * pageSize, pageSize);
		List<ConfigVO> content = total > 0 ? configMapper.list(condition, rowBounds) : new ArrayList<ConfigVO>(0);
		return new PageImpl<ConfigVO>(content, pageNumber, pageSize, total);
	}

	/**
	 * 根据主键级联删除数据
	 * 
	 * @param id
	 *            主键
	 */
	public void deleteCascadeById(String id) {
		ConfigVO vo = configMapper.getById(id);
		if (null != vo) {
			this.refreshLeafMark(vo.getParentId(), -1);
			configMapper.deleteCascade(id);
			CacheUtils.delete(ConfigUtils.getCacheKey(vo.getConfigKey()));
		}
	}

	/**
	 * 根据主键查找
	 * 
	 * @param id
	 *            主键
	 * @return vo
	 */
	public ConfigVO findById(String id) {
		return configMapper.getById(id);
	}

	/**
	 * 根据配置key查找
	 *
	 * @param key
	 *            配置key
	 * @return vo
	 */
	public ConfigVO findByKey(String key) {
		return configMapper.getByKey(key);
	}

	/**
	 * 根据配置key查找子节点
	 * 
	 * @param key
	 *            配置key
	 * @return list
	 */
	public List<ConfigVO> getSubByKey(String key) {
		return configMapper.getSubByKey(key);
	}

	/**
	 * 根据配置key获取子节点
	 * 
	 * @param key
	 * @return
	 */
	public List<ConfigVO> findSubConfigsByKey(String key) {

		ConfigVO configVO = configMapper.getByKey(key);
		if (null == configVO) {
			return null;
		}
		String fullCode = configVO.getFullCode();
		return configMapper.getByFullCode(fullCode);
	}
}
