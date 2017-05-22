/**
 * 
 */
package com.hdos.platform.base.operationlog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hdos.platform.base.operationlog.model.UserActivityLogQueryCondition;
import com.hdos.platform.base.operationlog.model.UserActivityLogVO;
import com.hdos.platform.base.operationlog.service.UserActivityLogService;
import com.hdos.platform.common.page.Page;

/**
 * 操作日志
 * @author chenyang
 *
 */
@Controller
@RequestMapping("/operationlog")
public class OperationLogController {
	
	private final Logger logger = LoggerFactory.getLogger(OperationLogController.class);
	
	@Autowired
	private UserActivityLogService userActivityLogService;

	/**
	 * 获取菜单数据（tree类型）
	 * 
	 * @param rid
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/queryLogByCondition")
	@ResponseBody
	public String queryLogManageByCondition(String logAddress, String logOper, String logStatus, String logTime1,
			String logTime2, String logUser, int page, int rows) {
		UserActivityLogQueryCondition userActivityLogQueryCondition = new UserActivityLogQueryCondition();
		userActivityLogQueryCondition.setLogAddress(logAddress);
		userActivityLogQueryCondition.setLogOper(logOper);
		userActivityLogQueryCondition.setLogStatus(logStatus);
		userActivityLogQueryCondition.setLogTime1(logTime1);
		userActivityLogQueryCondition.setLogTime2(logTime2);
		userActivityLogQueryCondition.setLogUser(logUser);
		
		Page<UserActivityLogVO> userActivityLogVO  = userActivityLogService.queryUserLogByCondition(userActivityLogQueryCondition, page, rows);
		JSONObject rst = new JSONObject();
		rst.put("total", userActivityLogVO.getTotalElements());
		rst.put("rows", userActivityLogVO.getContent());
		return JSONObject.toJSONString(rst);
	}


	/**
	 * 跳转到操作日志查询的页面
	 * 
	 * @return
	 */
	@RequestMapping("/init")
	public String init() {
		if(logger.isInfoEnabled()) {
			logger.info("[操作日志界面初始化成功]");
		}
		return "base/operationlog/operationLog";
	}


}
