package com.hdos.platform.base.operationlog.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hdos.platform.base.operationlog.mapper.UserActivityLogMapper;
import com.hdos.platform.base.operationlog.mapper.UserLogConfigMapper;
import com.hdos.platform.base.operationlog.model.UserActivityLogQueryCondition;
import com.hdos.platform.base.operationlog.model.UserActivityLogVO;
import com.hdos.platform.base.operationlog.model.UserLogConfigVO;
import com.hdos.platform.base.user.model.LoginUserVO;
import com.hdos.platform.common.page.Page;
import com.hdos.platform.common.page.PageImpl;
import com.hdos.platform.common.util.StringUtils;
import com.hdos.platform.core.base.BaseService;

@Service
@Transactional
public class UserActivityLogService extends BaseService<UserActivityLogVO> {
	
	@Autowired
	private UserActivityLogMapper userActivityLogMapper;
	
	@Autowired
	private UserLogConfigMapper userLogConfigMapper;
	
	
	
	public Page<UserActivityLogVO> queryUserLogByCondition(UserActivityLogQueryCondition userActivityLogQueryCondition, int pageNumber, int pageSize) {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("logOper", userActivityLogQueryCondition.getLogOper());
		condition.put("logUser", userActivityLogQueryCondition.getLogUser());
		condition.put("logAddress", userActivityLogQueryCondition.getLogAddress());
		if(StringUtils.isEmpty(userActivityLogQueryCondition.getLogStatus())) {
			condition.put("logStatus", "0");
		} else {
			condition.put("logStatus", userActivityLogQueryCondition.getLogStatus());
			
		}
		condition.put("logTime1", userActivityLogQueryCondition.getLogTime1());
		condition.put("logTime2", userActivityLogQueryCondition.getLogTime2());
		
		int total = userActivityLogMapper.count(condition);
		RowBounds rowBounds = new RowBounds(pageNumber - 1, pageSize);
		List<UserActivityLogVO> content = total > 0 ? userActivityLogMapper.list(condition, rowBounds) : new ArrayList<UserActivityLogVO>(0);
		return new PageImpl<UserActivityLogVO>(content, pageNumber, pageSize, total);
	}
	
	
	public void recordUserLog(LoginUserVO userVO, String operType, String detailContext, boolean success) {
		UserActivityLogVO userActivityLogVO = new UserActivityLogVO();
		userActivityLogVO.setLogOrgId(generateKey(userActivityLogVO));
		userActivityLogVO.setLogOper(operType);
		userActivityLogVO.setLogStatus(success ? 0 : 1);
		userActivityLogVO.setLogContent(detailContext);
		userActivityLogVO.setLogUserid(userVO.getUserId());
		if(StringUtils.isEmpty(userVO.getUserName())) {
			userActivityLogVO.setLogUser(userVO.getUserAccount());
		} else {
			userActivityLogVO.setLogUser(userVO.getUserName() + "(" + userVO.getUserAccount() + ")");
		}
		userActivityLogVO.setLogAddress(userVO.getIp());
		userActivityLogVO.setLogTime(new Date());

		userActivityLogMapper.insert(userActivityLogVO);
	}
	
	public UserLogConfigVO findUserLogConfigVO(String invokeUUID) {
		return userLogConfigMapper.viewLogManageByMark(invokeUUID);
	}
}
