package com.hdos.platform.base.widgetmanager.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hdos.platform.base.config.model.ConfigVO;
import com.hdos.platform.base.filter.LoginContext;
import com.hdos.platform.base.widgetmanager.model.WidgetManagerVO;
import com.hdos.platform.base.widgetmanager.service.WidgetManagerService;
import com.hdos.platform.common.page.Page;
import com.hdos.platform.common.util.ConfigUtils;

/**
 * 控件管理控制器
 * 
 * @author
 * @version 1.0
 */
@Controller
@RequestMapping("/widgetmanager")
public class WidgetManagerController {

	private static final Logger logger = LoggerFactory.getLogger(WidgetManagerController.class);
	@Autowired
	private WidgetManagerService widgetManagerService;

	/**
	 * 分页查询数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public String list(String widgetName, int page, int rows) {

		Map<String, Object> queryCondition = new HashMap<String, Object>();
		queryCondition.put("widgetName", widgetName);
		Page<WidgetManagerVO> widgetPage = widgetManagerService.findPage(queryCondition, page, rows);
		JSONObject rst = new JSONObject();
		rst.put("total", widgetPage.getTotalElements());
		rst.put("rows", widgetPage.getContent());
		return rst.toJSONString();
	}

	/**
	 * 查询不同的最新ocx控件
	 */
	@RequestMapping(value = "/listpage")
	@ResponseBody
	public String listPage(@RequestParam("widgetVersions") String widgetVersions) {
		Page<WidgetManagerVO> ocxPage = widgetManagerService.findLastedWidget(widgetVersions);
		JSONObject rst = new JSONObject();
		rst.put("rows", ocxPage.getContent());
		return rst.toJSONString();
	}

	/**
	 * 查询不同的最新ocx控件
	 */
	@RequestMapping(value = "/listOfNewPage")
	@ResponseBody
	public String listOfNewPage() {
		Page<WidgetManagerVO> ocxPage = widgetManagerService.findNewWidget();
		JSONObject rst = new JSONObject();
		rst.put("rows", ocxPage.getContent());
		return rst.toJSONString();
	}

	/**
	 * 主页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/init")
	public String init() {
		return "base/widgetmanager/widgetManagerMain";
	}

	/**
	 * 编辑页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/add")
	public String addWidget() {
		return "base/widgetmanager/widgetManagerEdit";
	}

	/**
	 * 查看详情
	 */
	@RequestMapping(value = "/check")
	public String checkOcx() {
		return "base/widgetmanager/widgetManagerDetail";
	}

	/**
	 * 查看详情
	 */
	@RequestMapping(value = "/widgetCheck")
	public String widgetCheck() {
		return "base/widgetmanager/widgetManagerCheck";
	}

	/**
	 * 上传文件
	 * 
	 * @param ocxVersion
	 * @param zipFile
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String saveOcxData(@RequestParam("widgetVersion") String widgetVersion, String widgetType,
			String fileId, HttpServletResponse response) {
		return widgetManagerService.saveWidget(widgetVersion, widgetType,fileId,LoginContext.getCurrentUser().getUserName(), LoginContext.getCurrentUser().getUserId());

	}

	@RequestMapping(value = "/versionCheck")
	@ResponseBody
	public String hasLastedVersion(String widgetVersions) {

		return widgetManagerService.hasLastedVersionWidget(widgetVersions);

	}

	/**
	 * 删除
	 * 
	 * @param ocxId
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String deleteOcx(@RequestParam("widgetId") String widgetId) {
		return widgetManagerService.deleteWidget(widgetId);
	}

	/**
	 * 显示所有控件类型
	 * @return
	 */
	@RequestMapping(value = "/widgetType")
	@ResponseBody
	public String getFaceType() {
		List<ConfigVO> tmp = ConfigUtils.getList("WIDGET_TYPE");
		return JSONObject.toJSONString(tmp);
	}

}
