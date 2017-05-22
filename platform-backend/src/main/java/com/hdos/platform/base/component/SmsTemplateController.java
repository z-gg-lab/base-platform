package com.hdos.platform.base.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hdos.platform.base.component.model.SmsTemplateVO;
import com.hdos.platform.base.component.service.SmsTemplateService;
import com.hdos.platform.common.page.Page;
import com.hdos.platform.common.util.SmsUtils;

/**
 * 短信模板配置控制器
 * 
 * @author zhuw
 * @version 1.0
 */
@Controller
@RequestMapping("/component/smsTemplate")
public class SmsTemplateController {

	@Autowired
	private SmsTemplateService smsTemplateService;

	private static final Logger logger = LoggerFactory.getLogger(SmsTemplateController.class);

	/**
	 * 初始化主页面
	 * 
	 * @return
	 */
	@RequestMapping("/init")
	public String init() {
//		try {
//			List<String> list = new ArrayList<String>();
//			Map<String,Object> result = SmsUtils.sendSMS("15665412278", "1",list);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		if (logger.isInfoEnabled()) {
			logger.info("页面初始化成功");
		}
		return "base/component/smstemplate/smsTemplateMain";
	}

	/**
	 * 打开新增页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/add")
	public String addRole() {
		return "base/component/smstemplate/smsTemplateEdit";
	}

	/**
	 * 修改短信模板
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/edit/{id}")
	public String editRole(@PathVariable("id") String id, Model model) {

		SmsTemplateVO smsTemplateVO = smsTemplateService.getById(id);
		model.addAttribute("smsTemplateVO", smsTemplateVO);
		return "base/component/smstemplate/smsTemplateEdit";
	}

	/**
	 * 分页查询短信模板
	 * 
	 * @return
	 */
	@RequestMapping(value = "/querySmsTemplate")
	@ResponseBody
	public String querycode(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "rows", defaultValue = "20") int pageSize, Model model) {

		Map<String, Object> queryCondition = new HashMap<String, Object>();
		Page<SmsTemplateVO> codePage = smsTemplateService.findPage(queryCondition, pageNumber, pageSize);
		JSONObject rst = new JSONObject();
		rst.put("total", codePage.getTotalElements());
		rst.put("rows", codePage.getContent());
		return rst.toJSONString();
	}

	/**
	 * 保存短信模板
	 * 
	 * @param smsTemplateVO
	 * @return
	 */
	@RequestMapping(value = "/save")
	@ResponseBody
	public String saveSmsTemplate(SmsTemplateVO smsTemplateVO) {
		try {
			smsTemplateService.saveSmsTemplate(smsTemplateVO);
		} catch (Exception e) {
			logger.info("Exception",e);
			return "fail";
		}
		return "succ";
	}

	/**
	 * 删除模板
	 * @param roleIds
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public String deleteRole(String ids) {
		String[] id = ids.split(",");
		smsTemplateService.deleteTemplate(id);
		return "success";
	}
	
	
}
