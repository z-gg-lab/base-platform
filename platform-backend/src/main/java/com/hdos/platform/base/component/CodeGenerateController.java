package com.hdos.platform.base.component;

import java.util.HashMap;
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
import com.hdos.platform.base.component.model.CodeGenerateVO;
import com.hdos.platform.base.component.service.CodeGenerateService;
import com.hdos.platform.common.page.Page;

/**
 * 编码组件控制器
 * @author zhuw
 * @version 1.0
 */
@Controller
@RequestMapping("/component/codegenerate")
public class CodeGenerateController {
   
	@Autowired
	private CodeGenerateService codeGenerateService;
	
	private static final Logger logger = LoggerFactory.getLogger(CodeGenerateService.class);
	
	/**
	 * 初始化主页面
	 */
	@RequestMapping("/init")
	public String init(){
//		String code = CodeGenerateUtils.getCodeGenerator("test","1");
//		System.out.println(code);
		if(logger.isInfoEnabled()) {
			logger.info("页面初始化成功");
		}
		return "base/component/codegenerate/codeGenerateMain";
	}
	
	/**
	 * 打开新增页面
	 * @return
	 */
	@RequestMapping(value = "/add")
	public String addRole() {
		return "base/component/codegenerate/codeGenerate";
	}
	
	/**
	 * 修改编码
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/edit/{id}")
	public String editRole(@PathVariable("id") String id,Model model) {
		
		CodeGenerateVO codeGenerateVO = codeGenerateService.getById(id);
		model.addAttribute("codeGenerateVO", codeGenerateVO);
		return "base/component/codegenerate/codeGenerate";
	}
	
	/**
	 * 保存编码
	 * @param codeGenerateVO
	 * @return
	 */
	@RequestMapping(value = "/save")
	@ResponseBody
	public String saveCode(CodeGenerateVO codeGenerateVO){
		try {
			codeGenerateService.saveCode(codeGenerateVO);
		} catch (Exception e) {
			logger.info("Exception",e);
			return "fail";
		}
		return "succ";
	}
	
	
	
	/**
	 * 分页查询编码数据
	 * @return
	 */
	@RequestMapping(value = "/querycode")
	@ResponseBody
	public String querycode(@RequestParam(value = "page", defaultValue = "1") int pageNumber,@RequestParam(value = "rows", defaultValue = "20") int pageSize, Model model) {
		
		Map<String, Object> queryCondition = new HashMap<String,Object>();
		Page<CodeGenerateVO> codePage = codeGenerateService.findPage(queryCondition, pageNumber, pageSize);
		JSONObject rst = new JSONObject();
		rst.put("total", codePage.getTotalElements());
		rst.put("rows", codePage.getContent());
		return rst.toJSONString();
	}
	

	/**
	 * 删除编码
	 * @param roleIds
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public String deleteRole(String ids) {
		String[] id = ids.split(",");
		try {
			codeGenerateService.deleteCodes(id);
		} catch (Exception e) {
			// TODO: handle exception
			return "false";
		}
		return "success";
	}
	
	
	
	
	/**
	 * 下拉框
	 * @param departmentVO
	 * @return
	 */
	@RequestMapping(value="/getcombobox",produces="application/json;charset=utf-8")
	@ResponseBody
	public String getCombobox(Model model) {
		
		return JSONObject.toJSONString(codeGenerateService.getCombobox());
	}
	
}
