package com.hdos.platform.base.operationlog.mapper;

import org.springframework.stereotype.Repository;

import com.hdos.platform.base.operationlog.model.UserLogConfigVO;
import com.hdos.platform.core.base.BaseMapper;

@Repository
public interface UserLogConfigMapper extends BaseMapper<UserLogConfigVO> {

	/**
	 * 
	 * @param managePO
	 * @param logcfgId
	 * @param statusDisable
	 */
	void updateLogManageStatus(UserLogConfigVO managePO);

	/**
	 * 
	 * @param logcfgMark
	 * @param logcfgId
	 * @return
	 */
	boolean existsLogcfgMark(String logcfgMark, String logcfgId);
	
	/**
	 * 
	 * @param logcfgMark
	 * @return
	 */
	UserLogConfigVO viewLogManageByMark(String mark);
}
