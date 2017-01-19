package com.hdos.platform.base.menu.service;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.hdos.platform.base.menu.mapper.MenuMapper;
import com.hdos.platform.base.menu.model.MenuVO;
import com.hdos.platform.base.operation.mapper.OperationMapper;
import com.hdos.platform.base.operation.model.OperationVO;
import com.hdos.platform.common.vo.TreeVO;
import com.hdos.platform.core.base.BaseService;

/**
 * 
 * @author cyong 菜单的管理
 */
@Service
@Transactional
public class MenuService extends BaseService<MenuVO> {

	/** 菜单编辑类型：新增（目录或者菜单） */
	private static final int EDIT_TYPE_ADD = 0;

	/** 菜单编辑类型：添加同级目录 */
	private static final int EDIT_TYPE_ADD_SAMELEVEL_DIR = 1;

	/** 菜单编辑类型：添加下级目录 */
	private static final int EDIT_TYPE_ADD_LOWLEVEL_DIR = 2;

	/** 菜单编辑类型：添加菜单 */
	private static final int EDIT_TYPE_ADD_MENU = 3;

	/** 菜单编辑类型：修改 */
	private static final int EDIT_TYPE_UPDATE = 4;

	@Autowired
	private MenuMapper menuMapper;

	@Autowired
	private OperationMapper operationMapper;

	private final Logger logger = LoggerFactory.getLogger(MenuService.class);

	/**
	 * 根据父节点id获取菜单
	 * 
	 * @param pMenuId
	 * @return
	 */
	public List<TreeVO> queryTreeByPMenuId(String pMenuId) {

		List<MenuVO> menuList = menuMapper.queryMenuByPMenuId(pMenuId);

		return menuListToTreeList(menuList);
	}

	/**
	 * 根据父节点id和roleId获取菜单(tree)
	 * 
	 * @param pMenuId
	 * 
	 * @return
	 */
	public List<TreeVO> queryTreeByRole(String pMenuId, String userId) {

		MenuVO menuVO = new MenuVO();
		menuVO.setpMenuId(pMenuId);
		menuVO.setUserId(userId);
		List<MenuVO> menuList = menuMapper.queryTreeByRole(menuVO);
		return menuListToTreeList(menuList);
	}

	/**
	 * 根据父节点id和roleId获取菜单(menu)
	 * 
	 * @param pMenuId
	 * 
	 * @return
	 */
	public List<MenuVO> queryMenuByRole(String pMenuId, String userId) {

		MenuVO menuVO = new MenuVO();
		menuVO.setpMenuId(pMenuId);
		menuVO.setUserId(userId);
		List<MenuVO> menuList = menuMapper.queryTreeByRole(menuVO);
		return menuList;
	}

	/**
	 * 根据菜单名称和roleId获取菜单(tree)
	 * 
	 * @param pMenuId
	 * 
	 * @return
	 */
	public List<TreeVO> queryTreeByRoleMenuId(String menuId, String userId) {

		MenuVO menuVO = menuMapper.readMenuByMenuId(menuId);
		menuVO.setUserId(userId);
		menuVO.setpMenuId(menuVO.getMenuId());
		List<MenuVO> menuList = menuMapper.queryTreeByRole(menuVO);
		return menuListToTreeList(menuList);
	}

	/**
	 * 根据父节点id和roleId获取菜单并选中
	 * 
	 * @param pMenuId
	 * 
	 * @return
	 */
	public List<TreeVO> getTreeDataByRoleSelected(String pMenuId, String roleId) {

		MenuVO menuVO = new MenuVO();
		menuVO.setpMenuId(pMenuId);
		menuVO.setRoleId(roleId);

		List<MenuVO> menuListSelect = menuMapper.getTreeDataByRoleSelected(menuVO);
		List<MenuVO> menuList = menuMapper.queryMenuByPMenuId(pMenuId);

		List<OperationVO> listOperation = new ArrayList<OperationVO>();

		Map<String, Object> condition = new HashMap<String, Object>();

		if (menuList != null && menuList.size() != 0) {
			condition.put("menuList", menuList);
			listOperation = operationMapper.listOperation(condition);
		}
		// 菜单树下挂操作操作列表
		if (listOperation != null && listOperation.size() != 0) {
			for (int i = 0; i < menuList.size(); i++) {
				for (int j = 0; j < listOperation.size(); j++) {
					if (listOperation.get(j).getMenuId().equals(menuList.get(i).getMenuId())) {
						menuList.get(i).setIsDir(1);
						menuList.get(i).setFlag(true);
						break;
					}
				}
			}
		}
		// 给菜单增加checked属性
		for (int i = 0; i < menuList.size(); i++) {
			for (int j = 0; j < menuListSelect.size(); j++) {
				if (menuList.get(i).getMenuId().equals(menuListSelect.get(j).getMenuId())) {
					menuList.get(i).setChecked(true);
					break;
				}
			}
		}

		return menuListToTreeList(menuList);
	}

	/**
	 * 根据编辑类型，组装初始化菜单vo
	 * 
	 * @param menuId
	 * @param editType
	 * @return
	 */
	public MenuVO buildMenuVOByEditType(String menuId, int editType) {

		// 读取当前参考的菜单VO
		MenuVO menuVOSelected = menuMapper.readMenuByMenuId(menuId);
		if (null == menuVOSelected) {
			return null;
		}
		MenuVO initMenuVO = new MenuVO();
		initMenuVO.setStatus(MenuVO.STATUS_NORMAL);

		// 新增同级目录
		if (EDIT_TYPE_ADD_SAMELEVEL_DIR == editType) {
			initMenuVO.setIsDir(MenuVO.IS_DIR);
			initMenuVO.setpMenuId(menuVOSelected.getpMenuId());
			// 获取父级的名字
			MenuVO pMenuVO = menuMapper.readMenuByMenuId(menuVOSelected.getpMenuId());
			if (null != pMenuVO) {
				initMenuVO.setpMenuName(pMenuVO.getMenuName());
			}
		}

		// 新增下级目录
		if (EDIT_TYPE_ADD_LOWLEVEL_DIR == editType) {
			initMenuVO.setIsDir(MenuVO.IS_DIR);
			initMenuVO.setpMenuId(menuVOSelected.getMenuId());
			initMenuVO.setpMenuName(menuVOSelected.getMenuName());
		}

		// 新增菜单
		if (EDIT_TYPE_ADD_MENU == editType) {
			initMenuVO.setIsDir(MenuVO.IS_NOT_DIR);
			// 目录下增下级菜单
			if (menuVOSelected.getIsDir() == MenuVO.IS_DIR) {
				initMenuVO.setpMenuId(menuVOSelected.getMenuId());
				initMenuVO.setpMenuName(menuVOSelected.getMenuName());
			} else if (menuVOSelected.getIsDir() == MenuVO.IS_NOT_DIR) {
				// 菜单则曾同级菜单
				initMenuVO.setpMenuId(menuVOSelected.getpMenuId());
				// 获取父级的名字
				MenuVO pMenuVO = menuMapper.readMenuByMenuId(menuVOSelected.getpMenuId());
				if (null != pMenuVO) {
					initMenuVO.setpMenuName(pMenuVO.getMenuName());
				}
			}
		}

		// 修改
		if (EDIT_TYPE_UPDATE == editType) {
			initMenuVO = menuVOSelected;
			// 获取父级的名字
			MenuVO pMenuVO = menuMapper.readMenuByMenuId(menuVOSelected.getpMenuId());
			if (null != pMenuVO) {
				initMenuVO.setpMenuName(pMenuVO.getMenuName());
			}
		}
		initMenuVO.setEditType(editType);
		return initMenuVO;
	}

	/**
	 * 保存菜单
	 * 
	 * @param menuVO
	 */
	public void saveMenu(MenuVO menuVO) {
		int editType;
		if (null == menuVO) {
			logger.error("保存菜单ERROR：" + menuVO);
			throw new IllegalArgumentException();
		}
		if (StringUtils.isNotEmpty(menuVO.getMenuId())) {// 更新
			menuVO.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			menuMapper.updateMenu(menuVO);	
			
			editType = EDIT_TYPE_UPDATE;
		} else {// 新增
			menuVO.setMenuId(generateKey(menuVO));
			// 20161226 --- oracle 没有now(),改成后台传值
			menuVO.setCreateTime(new Timestamp(System.currentTimeMillis()));
			menuVO.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			menuMapper.insertMenu(menuVO);
			editType = EDIT_TYPE_ADD;
		}
		if (menuVO.getIsDir() == MenuVO.IS_DIR) {
			menuVO.setState(TreeVO.STATE_CLOSED);
		} else if (menuVO.getIsDir() == MenuVO.IS_NOT_DIR) {
			menuVO.setState(TreeVO.STATE_OPEN);
		}
		menuVO.setEditType(editType);
	}

	/**
	 * 拖拽排序
	 * @param parentId
	 * @param dropMenuId
	 * @param targetMenuId
	 * @param point
	 */
	public void sortMenu(String parentId, String dropMenuId, String targetMenuId,String point) {

		if (parentId.equals("null")) {
			parentId = "-1";
		}
		
		//目标菜单
		MenuVO targetMenuVO = menuMapper.readMenuByMenuId(targetMenuId);
		//拖动的菜单
		MenuVO dropMenuVO = menuMapper.readMenuByMenuId(dropMenuId);

		dropMenuVO.setpMenuId(parentId);
		
		//判断是移动的状态 
		if ("top".equalsIgnoreCase(point)) {
			dropMenuVO.setSortNo(targetMenuVO.getSortNo() - 1);
		}else {
			dropMenuVO.setSortNo(targetMenuVO.getSortNo() + 1);
		}
		
		//该父节点下的所有子节点的sortNo都相应改变
		List<MenuVO> listMenuGT = menuMapper.queryMenuBySortNoGT(parentId,String.valueOf(targetMenuVO.getSortNo()));
		List<MenuVO> listMenuLT = menuMapper.queryMenuBySortNoLT(parentId,String.valueOf(targetMenuVO.getSortNo()));
		if ("top".equalsIgnoreCase(point)) {
			
			if (listMenuLT.size()!=0) {
				for (MenuVO menuVO : listMenuLT) {
					menuVO.setSortNo(menuVO.getSortNo()-1);
					menuMapper.sortMenu(menuVO);
				}
			}
		}else {
			
			if (listMenuGT.size()!=0) {
				for (MenuVO menuVO : listMenuGT) {
					menuVO.setSortNo(menuVO.getSortNo()+1);
					menuMapper.sortMenu(menuVO);
				}
			}
		}
		
		menuMapper.sortMenu(dropMenuVO);
	}

	/**
	 * 保存菜单角色关系
	 * 
	 * @param menuVO
	 */
	public void saveMenuRole(String[] menuIds, String roleId) {

		// 无论新增还是修改都先删除roleId对应的menuId
		menuMapper.deleteMenuRole(roleId);

		List<MenuVO> list = new ArrayList<MenuVO>();
		for (String menuId : menuIds) {
			MenuVO menuVO = new MenuVO();
			menuVO.setMenuId(menuId);
			menuVO.setRoleId(roleId);
			list.add(menuVO);
		}

		if (list != null && list.size() != 0) {
			menuMapper.insertMenuRole(list);
		}

	}

	/**
	 * 根据父节点id获取菜单
	 * 
	 * @param pMenuId
	 * @return
	 */
	public List<MenuVO> queryMenuByPMenuId(String pMenuId) {
		return menuMapper.queryMenuByPMenuId(pMenuId);
	}

	/**
	 * 删除一个菜单
	 * 
	 * @param menuId
	 *            菜单的id
	 * @return
	 */
	public void deleteMenu(String menuId) {
		menuMapper.deleteMenuByMenuId(menuId);
	}

	/**
	 * 根据id 查询其下子菜单的个数
	 * 
	 * @param id
	 *            菜单的id
	 * @return 子菜单的个数
	 */
	public int countSubMenus(String id) {
		return menuMapper.countSubMenus(id);
	}

	/**
	 * menuVO转treeVO
	 * 
	 * @param menuVO
	 * @return
	 */
	private TreeVO menuVOToTreeVO(MenuVO menuVO) {

		TreeVO treeVO = new TreeVO();
		treeVO.setId(menuVO.getMenuId());
		treeVO.setText(menuVO.getMenuName());
		// 设置tree的图标
		treeVO.setIconCls(menuVO.getIconCls());
		// 如果是目录state="closed" 不是目录 state="open"
		if (menuVO.getIsDir() == MenuVO.IS_DIR) {
			treeVO.setState(TreeVO.STATE_CLOSED);
		} else {
			treeVO.setState(TreeVO.STATE_OPEN);
		}
		JSONObject attributes = new JSONObject();
		if (StringUtils.isNotEmpty(menuVO.getUrl())) {
			attributes.put(TreeVO.ATTR_URL, menuVO.getUrl());
		}

		if (StringUtils.isNotEmpty(menuVO.getpMenuId())) {

			attributes.put("pMenuId", menuVO.getpMenuId());
		}

		if (menuVO.getChecked()) {
			attributes.put("isDir", menuVO.getIsDir());
			treeVO.setChecked(menuVO.getChecked());
		}

		if (menuVO.getFlag()) {
			attributes.put("flag", menuVO.getFlag());
		} else {
			attributes.put("flag", false);
		}
		treeVO.setAttributes(attributes);
		return treeVO;
	}

	/**
	 * menuList转treeList
	 * 
	 * @param treeList
	 * @return
	 */
	private List<TreeVO> menuListToTreeList(List<MenuVO> menuList) {

		List<TreeVO> treeList = new ArrayList<TreeVO>();
		for (MenuVO menuVO : menuList) {
			TreeVO treeVO = menuVOToTreeVO(menuVO);
			treeList.add(treeVO);
		}
		return treeList;
	}

	/**
	 * 获得项目路径下的图标名称
	 * 
	 * @param path
	 * @return
	 */
	public List<String> getIcon(String path) {

		File file = new File(path);
		File[] files = file.listFiles();

		List<String> list = new ArrayList<String>();

		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			list.add(f.getName());
		}
		return list;
	}

}
