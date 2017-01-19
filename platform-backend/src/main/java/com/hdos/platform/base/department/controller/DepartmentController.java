package com.hdos.platform.base.department.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hdos.platform.base.config.model.ConfigVO;
import com.hdos.platform.base.department.model.DepartmentVO;
import com.hdos.platform.base.department.service.DepartmentService;
import com.hdos.platform.base.user.model.AccountInfoVO;
import com.hdos.platform.common.util.ConfigContants;
import com.hdos.platform.common.util.ConfigUtils;
import com.hdos.platform.common.vo.TreeVO;

/**
 * 机构管理
 * 
 * @author zhuw
 * 
 */
@Controller
@RequestMapping("/department")
public class DepartmentController {

	private final Logger logger = LoggerFactory
			.getLogger(DepartmentController.class);
	@Autowired
	private DepartmentService departmentService;

	/**
	 * 根据父级id获取机构树
	 * 
	 * @param rid
	 * @return
	 */
	@RequestMapping(value = "treeData/{rid}")
	@ResponseBody
	public String getTreeData(@PathVariable("rid") String rid) {

		List<TreeVO> treeList = departmentService.queryTreeByParentId(rid);
		return JSONObject.toJSONString(treeList);
	}

	/**
	 * 获取所有的树
	 *
	 * @return
	 */
	@RequestMapping(value = "/allTreeData")
	@ResponseBody
	public String getAllTreeData() {

		List<TreeVO> treeList = departmentService.queryAllTreeByParentId("-1");
		return JSONObject.toJSONString(treeList);
	}

	/**
	 * 获取机构信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "departmentData")
	@ResponseBody
	public String getDepartmentData(String id) {

		if (StringUtils.isEmpty(id)) {
			id = "-1";
		}
		List<DepartmentVO> list = departmentService.queryByParentId(id);

		return JSONObject.toJSONString(list);
	}

	/**
	 * 初始化界面
	 * 
	 * @return
	 */
	@RequestMapping("/init")
	public String init() {
		if (logger.isInfoEnabled()) {
			logger.info("[机构管理初始化成功]");
		}
		return "base/department/departmentMain";
	}

	/**
	 * 编辑机构
	 * 
	 * @param departmentId
	 * @param editType
	 * @param model
	 * @return
	 */
	@RequestMapping("/edit/{departmentId}/{editType}")
	public String departmentEdit(
			@PathVariable("departmentId") String departmentId,
			@PathVariable("editType") int editType, Model model) {

		DepartmentVO departmentVO = departmentService
				.buildDepartmentVOByEditType(departmentId, editType);
		model.addAttribute("departmentVO", departmentVO);
		return "base/department/departmentEdit";
	}

	/**
	 * 保存机构
	 * 
	 * @param departmentVO
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "text/htm;charset=UTF-8")
	@ResponseBody
	public String saveDepartment(DepartmentVO departmentVO) {
		Map<String,Object> resultMap = departmentService.saveDepartment(departmentVO);
		resultMap.put("departmentVO",departmentVO);
		return JSONObject.toJSONString(resultMap);
	}

	/**
	 * 统计子机构
	 * 
	 * @param departmentId
	 * @return
	 */
	@RequestMapping("/countSubDepartments/{departmentId}")
	@ResponseBody
	public String countSubDepartments(
			@PathVariable("departmentId") String departmentId) {

		int count = departmentService.countSubDepartments(departmentId);
		return String.valueOf(count);
	}

	/**
	 * 删除机构
	 * @param departmentId
	 * @return
	 */
	@RequestMapping("/delete/{departmentId}")
	@ResponseBody
	public String delete(@PathVariable("departmentId") String departmentId) {
		try {
			departmentService.delete(departmentId);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return "false";
		}
		return "success";
	}

	/**
	 * 机构类型选择下拉框
	 * 
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getCombobox", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String getCombobox(Model model) {
		List<ConfigVO> vo = ConfigUtils.getList(ConfigContants.DEPARTMENT_TYPE);

		return JSONObject.toJSONString(vo);
	}

	/**
	 * 获取第一个部门名称
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getFirstName")
	@ResponseBody
	public DepartmentVO getFirstName() {
		DepartmentVO departmentVO = new DepartmentVO();
		departmentVO.setDepartmentName(departmentService.getFirstName());
		return departmentVO;
	}

	/**
	 * 判断是否是商户
	 * 
	 * @param merchantId
	 * @return
	 */
	@RequestMapping("/countmerchant")
	@ResponseBody
	public String countMerchant(String merchantId) {
		AccountInfoVO vo = departmentService.getByMerchantId(merchantId);
		if (vo != null) {
			return "true";
		} else {
			return "false";
		}
	}

}
