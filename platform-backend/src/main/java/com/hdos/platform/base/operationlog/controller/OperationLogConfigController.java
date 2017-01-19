package com.hdos.platform.base.operationlog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hdos.platform.base.operationlog.model.UserLogConfigCondition;
import com.hdos.platform.base.operationlog.model.UserLogConfigVO;
import com.hdos.platform.base.operationlog.service.UserLogConfigService;
import com.hdos.platform.common.page.Page;

/**
 * 操作日志配置
 * @author chenyang
 *
 */
@Controller
@RequestMapping("/operationlogconfig")
public class OperationLogConfigController {
	
	private final Logger logger = LoggerFactory.getLogger(OperationLogConfigController.class);
	
	@Autowired
	private UserLogConfigService userLogConfigService;

	/**
	 * 获取菜单数据（tree类型）
	 * 
	 * @param rid
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/queryLogManageByCondition")
	@ResponseBody
	public String queryLogManageByCondition(String logcfgOper, String logcfgMark,String logcfgStatus,int page, int rows) {
		UserLogConfigCondition userLogConfigCondition = new UserLogConfigCondition();
		userLogConfigCondition.setLogcfgMark(logcfgMark);
		userLogConfigCondition.setLogcfgOper(logcfgOper);
		userLogConfigCondition.setLogcfgStatus(logcfgStatus);
		
		Page<UserLogConfigVO> userLogConfigVO  = userLogConfigService.queryLogManageByCondition(userLogConfigCondition, page, rows);
		JSONObject rst = new JSONObject();
		rst.put("total", userLogConfigVO.getTotalElements());
		rst.put("rows", userLogConfigVO.getContent());
		return JSONObject.toJSONString(rst);
	}
	
	/**
	 * 保存菜单
	 * @param menuVO
	 * @return
	 */
	@RequestMapping(value="/save",produces="application/json;charset=utf-8")
	@ResponseBody
	public String save(UserLogConfigVO userLogConfigVO) {
		
		userLogConfigService.saveLogManage(userLogConfigVO);
		return JSONObject.toJSONString(userLogConfigVO);
	}

	/**
	 * 删除一个从菜单节点
	 * 
	 * @return
	 */
	@RequestMapping("/delete/{logcfgId}")
	@ResponseBody
	public String delete(@PathVariable("logcfgId") String logcfgId) {
		if(logger.isInfoEnabled()) {
			logger.info("[删除菜单]");
		}
		userLogConfigService.deleteLogManage(logcfgId);
		return "success";
	}
	
	/**
	 * 根据ID获取一个从菜单节点
	 * 
	 * @return
	 */
	@RequestMapping("/getById/{logcfgId}")
	@ResponseBody
	public String getById(@PathVariable("logcfgId") String logcfgId) {
		if(logger.isInfoEnabled()) {
			logger.info("[根据ID获取菜单]");
		}
		UserLogConfigVO userLogConfigVO = userLogConfigService.viewLogManage(logcfgId);
		return JSONObject.toJSONString(userLogConfigVO);
	}
	
	/**
	 * 启用日志模板
	 * 
	 * @param logcfgId
	 * @return
	 */
	@RequestMapping("/enableLogManage/{logcfgId}")
	@ResponseBody
	public String enableLogManage(@PathVariable("logcfgId") String logcfgId) {
		userLogConfigService.enableLogManage(logcfgId);
		return "success";
	}
	
	/**
	 * 停用日志模板
	 * 
	 * @param logcfgId
	 * @param loginUserInfo
	 * @return
	 */
	@RequestMapping("/disableLogManage/{logcfgId}")
	@ResponseBody
	public String disableLogManage(@PathVariable("logcfgId") String logcfgId) {
		userLogConfigService.disableLogManage(logcfgId);
		return "success";
	}

	/**
	 * 跳转到操作日志查询的页面
	 * 
	 * @return
	 */
	@RequestMapping("/init")
	public String init() {
		if(logger.isInfoEnabled()) {
			logger.info("[操作日志配置界面初始化成功]");
		}
		return "base/operationlog/operationLogConfig";
	}
	
	
	/**
	 * 操作日志编辑页面
	 * @return
	 */
	@RequestMapping("/edit/{logcfgId}")
	public String menuEdit(@PathVariable("logcfgId") String logcfgId, Model model) {
		
		UserLogConfigVO userLogConfigVO = null;
		if(!"add".equals(logcfgId)) {
			userLogConfigVO = userLogConfigService.viewLogManage(logcfgId);
		}
		
		model.addAttribute("userLogConfigVO", userLogConfigVO);
		return "base/operationlog/operationLogConfigEdit";
	}

}
