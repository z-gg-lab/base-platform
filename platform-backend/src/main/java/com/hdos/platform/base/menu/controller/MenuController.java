package com.hdos.platform.base.menu.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hdos.platform.base.filter.LoginContext;
import com.hdos.platform.base.menu.model.MenuVO;
import com.hdos.platform.base.menu.service.MenuService;
import com.hdos.platform.common.vo.TreeVO;

/**
 * 菜单
 *
 * @author chenyang
 *
 */
@Controller
@RequestMapping("/menu")
public class MenuController {

	private final Logger logger = LoggerFactory.getLogger(MenuController.class);
	@Autowired
	private MenuService menuService;

	/**
	 * 获取菜单数据（tree类型）
	 *
	 * @param rid
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "treeData/{rid}")
	@ResponseBody
	public String getTreeData(@PathVariable("rid") String rid) {

		List<TreeVO> treeList = menuService.queryTreeByPMenuId(rid);
		return JSONObject.toJSONString(treeList);
	}

	/**
	 * 根据用户获取菜单数据（tree类型）
	 *
	 * @param rid
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "treeDataByRole/{rid}")
	@ResponseBody
	public String getTreeDataByRole(@PathVariable("rid") String rid) {

		// 获取当前用户
		String userId = LoginContext.getCurrentUser().getUserId();

		List<TreeVO> treeList = menuService.queryTreeByRole(rid, userId);
		return JSONObject.toJSONString(treeList);
	}

	/**
	 * 获取菜单数据（menu类型）
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "menuDataByRole", method = RequestMethod.POST, produces = "text/htm;charset=UTF-8")
	@ResponseBody
	public String getMenuDataByRole(String id) {

		String userId = LoginContext.getCurrentUser().getUserId();

		List<MenuVO> menuList = menuService.queryMenuByRole(id, userId);

		return JSONObject.toJSONString(menuList);
	}

	/**
	 * 根据用户获取菜单数据（tree类型）
	 *
	 * @param rid
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "treeDataByRoleMenuId/{menuId}")
	@ResponseBody
	public String treeDataByRoleMenuId(@PathVariable("menuId") String menuId) {

		// 获取当前用户
		String userId = LoginContext.getCurrentUser().getUserId();

		List<TreeVO> treeList = menuService.queryTreeByRoleMenuId(menuId, userId);
		return JSONObject.toJSONString(treeList);
	}

	/**
	 * 根据用户获取菜单数据（tree类型） 选中拥有的菜单权限
	 *
	 * @param rid
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "getTreeDataByRoleSelected")
	@ResponseBody
	public String getTreeDataByRoleSelected(@RequestParam() String pMenuId, String roleId) {

		List<TreeVO> treeList = menuService.getTreeDataByRoleSelected(pMenuId, roleId);
		return JSONObject.toJSONString(treeList);
	}

	/**
	 * 获取菜单数据（menu类型）
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "menuData", method = RequestMethod.POST, produces = "text/htm;charset=UTF-8")
	@ResponseBody
	public String getMenuata(String id) {

		if (StringUtils.isEmpty(id)) {
			id = "-1";
		}
		List<MenuVO> menuList = menuService.queryMenuByPMenuId(id);

		return JSONObject.toJSONString(menuList);
	}

	/**
	 * 初始化主页面
	 *
	 * @return
	 */
	@RequestMapping("/init")
	public String init() {
		if (logger.isInfoEnabled()) {
			logger.info("[菜单界面初始化成功]");
		}
		return "base/menu/menuMain";
	}

	/**
	 * 菜单编辑页面
	 *
	 * @param menuId
	 * @param editType
	 * @param model
	 * @return
	 */
	@RequestMapping("/edit/{menuId}/{editType}")
	public String menuEdit(@PathVariable("menuId") String menuId, @PathVariable("editType") int editType, Model model) {

		MenuVO menuVO = menuService.buildMenuVOByEditType(menuId, editType);
		model.addAttribute("menuVO", menuVO);
		return "base/menu/menuEdit";
	}

	/**
	 * 初始化图标
	 *
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/listIcons")
	public String listIcons(HttpServletRequest request, Model model) {
		model.addAttribute("icons", JSONObject
				.toJSONString(menuService.getIcon(request.getServletContext().getRealPath("/statics/base/icons"))));
		return "base/menu/icon";
	}

	/**
	 * 保存菜单
	 *
	 * @param menuVO
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "text/htm;charset=UTF-8")
	@ResponseBody
	public String saveMenu(MenuVO menuVO) {

		menuService.saveMenu(menuVO);
		return JSONObject.toJSONString(menuVO);
	}

	/**
	 * 菜单排序
	 *
	 * @param parentId
	 * @param dropMenuId
	 * @param targetMenuId
	 * @param point
	 * @return
	 */
	@RequestMapping(value = "/sort", method = RequestMethod.POST, produces = "text/htm;charset=UTF-8")
	@ResponseBody
	public String sortMenu(String parentId, String dropMenuId, String targetMenuId,String point) {
		menuService.sortMenu(parentId, dropMenuId, targetMenuId,point);
		return JSONObject.toJSONString(null);
	}

	/**
	 * 保存菜单角色
	 *
	 * @param menuVO
	 * @return
	 */
	@RequestMapping(value = "/saveMenuRole")
	@ResponseBody
	public String saveMenuRole(@RequestParam String[] menuIds, String roleId) {

		menuService.saveMenuRole(menuIds, roleId);
		return "success";
	}

	/**
	 * 查询菜单子节点个数
	 *
	 * @param menuId
	 * @return
	 */
	@RequestMapping("/countSubMenus/{menuId}")
	@ResponseBody
	public String countSubMenus(@PathVariable("menuId") String menuId) {

		int count = menuService.countSubMenus(menuId);
		return String.valueOf(count);
	}

	/**
	 * 删除一个从菜单节点
	 *
	 * @param menuId
	 * @return
	 */
	@RequestMapping("/delete/{menuId}")
	@ResponseBody
	public String delete(@PathVariable("menuId") String menuId) {
		if (logger.isInfoEnabled()) {
			logger.info("[删除菜单]");
		}
		menuService.deleteMenu(menuId);
		return "success";
	}
}
