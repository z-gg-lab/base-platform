package com.hdos.platform.base.operationlog.mapper;

import org.springframework.stereotype.Repository;

import com.hdos.platform.base.operationlog.model.UserActivityLogVO;
import com.hdos.platform.core.base.BaseMapper;

@Repository
public interface UserActivityLogMapper extends BaseMapper<UserActivityLogVO> {
//	/**
//	 * 记录用户活动日志
//	 * 
//	 * @param activityLogPO
//	 */
//	void addUserActivityLog(UserActivityLogVO activityLogVO);
//	
//	/**
//	 * 查询用户活动日志
//	 * @param systemMark	
//	 * @param userLogCondition
//	 * @param pager
//	 * @return
//	 */
//	List<UserActivityLogVO> queryUserLogByCondition(UserActivityLogQueryCondition USERLOGCONDITION);
//	
//	UserActivityLogTemplateVO findUserActivityLogTemplate(UserActivityLogTemplateVO templatePO);
}
