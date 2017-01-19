/**
 * 
 */
package com.hdos.platform.base.role.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hdos.platform.base.role.model.RoleVO;
import com.hdos.platform.base.role.service.RoleService;
import com.hdos.platform.common.page.Page;

/**
 * 
 * @author chenyang
 *
 */
@Controller
@RequestMapping("/role")
public class RoleController {
	
	@Autowired
	private RoleService roleService;
	
	/**
	 * 角色管理主页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/init")
	public String init(Model model) {
		return "base/role/roleMain";
	}
	
	/**
	 * 分页查询角色数据
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public String list(String roleName,int page,int rows) {
		
		Map<String, Object> queryCondition = new HashMap<String,Object>();
		queryCondition.put("roleName", roleName);
		Page<RoleVO> rolePage = roleService.findPage(queryCondition, page, rows);
		JSONObject rst = new JSONObject();
		rst.put("total", rolePage.getTotalElements());
		rst.put("rows", rolePage.getContent());
		return rst.toJSONString();
	}
	
	/**
 	 * 角色管理之人员分配控制器
	 * @return
	 */
	@RequestMapping(value = "/roleUserList")
	public String roleUserList(String roleId,Model model){
		model.addAttribute("roleId", roleId);
		return "base/role/roleUserList";
	}
	
	/**
	 * 新增角色
	 * @return
	 */
	@RequestMapping(value = "/add")
	public String addRole() {
		return "base/role/roleEdit";
	}
	
	/**
	 * 修改角色
	 * @param roldId
	 * @return
	 */
	@RequestMapping(value = "/edit/{roleId}")
	public String editRole(@PathVariable("roleId") String roleId,Model model) {
		
		RoleVO roleVO = roleService.readRoleById(roleId);
		model.addAttribute("roleVO", roleVO);
		return "base/role/roleEdit";
	}
	
	/**
	 * 保存角色
	 * @param roleVO
	 * @return
	 */
	@RequestMapping(value = "/save")
	@ResponseBody
	public String saveRole(RoleVO roleVO) {
		boolean verifyRepeat = roleService.saveRole(roleVO);
		return verifyRepeat ? "false" : "success";
	}
	
	
	
	/**
	 * 删除角色
	 * @param roleIds
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public String deleteRole(String roleIds) {
		String[] ids = roleIds.split(",");
		if(!roleService.deleteRoles(ids)){
			return "false";
		}
		return "success";
	}
	
	/**
	 * 删除角色下的用户
	 * @param roleIds，users
	 * @return
	 */
	@RequestMapping(value = "/deleteUsers")
	@ResponseBody
	public void deleteRole(String roleId, String users) {
		String[] ids = users.split(",");
		roleService.deleteRoleUsers(roleId,ids);
	}
	
	
	/**
	 * 显示所有未分配角色，分页
	 * @param page
	 * @param rows
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/listExcludeUser",produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listExcludeUser(int page,int rows, String userId) {
		Map<String, Object> queryCondition = new HashMap<String,Object>();
		queryCondition.put("userId", userId);
		Page<RoleVO> rolePage = roleService.findPageExcludeUser(queryCondition, page, rows, userId);
		JSONObject rst = new JSONObject();
		rst.put("total", rolePage.getTotalElements());
		rst.put("rows", rolePage.getContent());
		return rst.toJSONString();
	}
	
	/**
	 * 显示所有角色，不分页
	 * @return
	 */
	@RequestMapping(value = "/listAll",produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listAll() {
		List<RoleVO> content = roleService.listAll();
		return JSONObject.toJSONString(content);
	}
	
	/**
	 * 显示所有未分配角色，不分页
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/listExcludeUserNoPage",produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listExcludeUserNoPage(String userId) {
		Map<String, Object> queryCondition = new HashMap<String,Object>();
		queryCondition.put("userId", userId);
		List<RoleVO> content = roleService.listExcludeUserNoPage(queryCondition);
		return JSONObject.toJSONString(content);
	}
	
	/**
	 * 显示所有已分配角色，不分页
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/listUserNoPage",produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listUserNoPage(String userId) {
		Map<String, Object> queryCondition = new HashMap<String,Object>();
		queryCondition.put("userId", userId);
		List<RoleVO> content = roleService.listUserNoPage(queryCondition);
		return JSONObject.toJSONString(content);
	}
}