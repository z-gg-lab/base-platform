package com.hdos.platform.base.operationlog.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hdos.platform.base.operationlog.mapper.UserLogConfigMapper;
import com.hdos.platform.base.operationlog.model.UserLogConfigCondition;
import com.hdos.platform.base.operationlog.model.UserLogConfigVO;
import com.hdos.platform.common.page.Page;
import com.hdos.platform.common.page.PageImpl;
import com.hdos.platform.common.util.StringUtils;
import com.hdos.platform.core.base.BaseService;

@Service
@Transactional
public class UserLogConfigService extends BaseService<UserLogConfigVO> {
	
	@Autowired
	private UserLogConfigMapper userLogConfigMapper;
	
	/**
	 * 新增日志定义对象
	 * 
	 * @param logManage
	 * @return
	 */
	public UserLogConfigVO saveLogManage(UserLogConfigVO logManage) {
		if(StringUtils.isEmpty(logManage.getLogcfgId())) {
			logManage.setLogcfgId(generateKey(logManage));
			Date data = new Date();
			logManage.setCreateTime(data);
			logManage.setUpdateTime(data);
			userLogConfigMapper.insert(logManage);
		} else {
			logManage.setUpdateTime(new Date());
			userLogConfigMapper.update(logManage);
		}
		return logManage;
	}

	/**
	 * 删除日志定义对象
	 * 
	 * @param logcfgId
	 * @return
	 */
	public void deleteLogManage(String logcfgId) {
		String logcfgIds = logcfgId.substring(0, logcfgId.length() -1);
		List<String> ids = Arrays.asList(logcfgIds.split(","));
		
		for(String id : ids) {
			userLogConfigMapper.delete(id);
		}
		
	}

	/**
	 * 启用日志定义对象
	 * 
	 * @param logcfgId
	 * @return
	 */
	public void enableLogManage(String logcfgId) {
		String logcfgIds = logcfgId.substring(0, logcfgId.length() -1);
		List<String> ids = Arrays.asList(logcfgIds.split(","));
		
		for(String id : ids) {
			UserLogConfigVO userLogConfigVO = new UserLogConfigVO();
			userLogConfigVO.setLogcfgId(id);
			userLogConfigVO.setLogcfgStatus(UserLogConfigConst.STATUS_ENABLE);
			
			userLogConfigMapper.updateLogManageStatus(userLogConfigVO);
		}
		
	}

	/**
	 * 停用日志定义对象
	 * 
	 * @param logcfgId
	 * @return
	 */
	public void disableLogManage(String logcfgId) {
		String logcfgIds = logcfgId.substring(0, logcfgId.length() -1);
		List<String> ids = Arrays.asList(logcfgIds.split(","));
		
		for(String id : ids) {
			UserLogConfigVO userLogConfigVO = new UserLogConfigVO();
			userLogConfigVO.setLogcfgId(id);
			userLogConfigVO.setLogcfgStatus(UserLogConfigConst.STATUS_DISABLE);
			
			userLogConfigMapper.updateLogManageStatus(userLogConfigVO);
		}
	}
	
	/**
	 * 根据条件查询日志模板定义
	 * 
	 * @param logManageCondition
	 * @return
	 */
	public Page<UserLogConfigVO> queryLogManageByCondition(UserLogConfigCondition userLogConfigCondition, int pageNumber, int pageSize) {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("logcfgMark", userLogConfigCondition.getLogcfgMark());
		condition.put("logcfgOper", userLogConfigCondition.getLogcfgOper());
		if(StringUtils.isEmpty(userLogConfigCondition.getLogcfgStatus())) {
			condition.put("logcfgStatus", "0");
		} else {
			condition.put("logcfgStatus", userLogConfigCondition.getLogcfgStatus());
		}
		
		int total = userLogConfigMapper.count(condition);
		RowBounds rowBounds = new RowBounds(pageNumber - 1, pageSize);
		List<UserLogConfigVO> content = total > 0 ? userLogConfigMapper.list(condition, rowBounds) : new ArrayList<UserLogConfigVO>(0);
		return new PageImpl<UserLogConfigVO>(content, pageNumber, pageSize, total);
	}

	/**
	 * 根据日志模板ID，查询模板详情
	 * 
	 * @param logcfgId
	 * @return
	 */
	public UserLogConfigVO viewLogManage(String logcfgId) {
		return userLogConfigMapper.getById(logcfgId);
	}
	
	/**
	 * 根据日志模板ID，查询模板详情
	 * 
	 * @param logcfgId
	 * @return
	 */
	public UserLogConfigVO viewLogManageByMark(String mark) {
		return userLogConfigMapper.viewLogManageByMark(mark);
	}
	
	
}
