/**
 * 
 */
package com.hdos.platform.base.operation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hdos.platform.base.operation.model.OperationVO;
import com.hdos.platform.base.operation.service.OperationService;
import com.hdos.platform.common.page.Page;
import com.hdos.platform.common.vo.TreeVO;

/**
 * 
 * @author chenyang
 *
 */
@Controller
@RequestMapping("/operation")
public class OperationController {
	
	@Autowired
	private OperationService operationService;
	
	/**
	 * 功能列表初始化
	 * @param menuId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/init/{menuId}")
	public String init(@PathVariable("menuId") String menuId,Model model) {
		model.addAttribute("menuId", menuId);
		return "base/operation/operationMain";
	}
	
	/**
	 * 功能列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list/{menuId}")
	@ResponseBody
	public String list(@PathVariable("menuId")String menuId, int page,int rows) {
		
		Page<OperationVO> operationPage = operationService.findOperationByMenuId(menuId, page, rows);
		JSONObject rst = new JSONObject();
		rst.put("total", operationPage.getTotalElements());
		rst.put("rows", operationPage.getContent());
		return rst.toJSONString();
	}
	
	/**
	 * 新增功能
	 * @return
	 */
	@RequestMapping(value = "/add/{menuId}")
	public String add(@PathVariable("menuId")String menuId,Model model) {
		
		OperationVO operationVO = new OperationVO();
		operationVO.setMenuId(menuId);
		model.addAttribute("operationVO",operationVO);
		return "base/operation/operationEdit";
	}
	
	/**
	 * 保存功能
	 * @param operationVO
	 * @return
	 */
	@RequestMapping(value = "/save")
	@ResponseBody
	public String saveOperation(OperationVO operationVO) {
		int rst = operationService.saveOperation(operationVO);
		return String.valueOf(rst);
	}
	
	/**
	 * 修改功能
	 * @param operationId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/edit/{operationId}")
	public String editOperation(@PathVariable("operationId") String operationId,Model model) {
		
		OperationVO operationVO = operationService.readOperationById(operationId);
		model.addAttribute("operationVO", operationVO);
		return "base/operation/operationEdit";
	}
	
	/**
	 * 删除功能
	 * @param operationIds
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public String deleteRole(String operationIds) {
		String[] ids = operationIds.split(",");
		operationService.deleteRoles(ids);
		return "success";
	}
	
	/**
	 * 
	 * @param menuId
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value = "/getTreeData")
	@ResponseBody
	public String getTreeData(@RequestParam String menuId,String roleId) {
		
		
		List<TreeVO> treeList = operationService.queryTreeByMenuId(menuId,roleId);
		return JSONObject.toJSONString(treeList);
	}
	
	
	/**
	 * 保存菜单角色
	 * @param menuVO
	 * @return
	 */
	@RequestMapping(value="/saveOperation_Role")
	@ResponseBody
	public String saveOperation_Role(@RequestParam String[] operationIds,String roleId) {
		
		operationService.saveOperation_Role(operationIds,roleId);
		return "success";
	}
	
	
}
