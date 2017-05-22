package com.hdos.platform.base.menu.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hdos.platform.base.menu.model.MenuVO;

@Repository
public interface MenuMapper {
	
	/**
	 * 根据父节点id获取菜单
	 * @param pMenuId
	 * @return
	 */
	List<MenuVO> queryMenuByPMenuId(String pMenuId);

	/**
	 * 
	 * @param pMenuId
	 * @param sortNo
	 * @return
	 */
	List<MenuVO> queryMenuBySortNoGT(String pMenuId,String sortNo);
	
	/**
	 * 
	 * @param pMenuId
	 * @param sortNo
	 * @return
	 */
	List<MenuVO> queryMenuBySortNoLT(String pMenuId,String sortNo);
	
	/**
	 * 根据用户权限获取菜单
	 * @param pMenuId
	 * @return
	 */
	List<MenuVO> queryTreeByRole(MenuVO menuVO);
	
	/**
	 * 根据角色
	 * @return
	 */
	List<MenuVO> getTreeDataByRoleSelected(MenuVO menuVO);
	
	
	
	/**
	 * 根据菜单id获取菜单
	 * @param menuId
	 * @return
	 */
	MenuVO readMenuByMenuId(String menuId);
	
	/**
	 * 添加一个菜单
	 * @param menuVO 菜单对象
	 * @return
	 */
	void insertMenu(MenuVO menuVO);
	
	/**
	 * 添加菜单角色
	 * @param
	 * @return
	 */
	void insertMenuRole(List<MenuVO> list);
	
	/**
	 * 删除一个菜单 菜单的 id
	 * @param menuId
	 * @return
	 */
	void deleteMenuByMenuId(String menuId);
	
	/**
	 * 修改菜单
	 * @param menuVO 菜单对象
	 * @return
	 */
	void updateMenu(MenuVO menuVO);
	
	/**
	 * 菜单排序
	 * @param menuVO 菜单对象
	 * @return
	 */
	void sortMenu(MenuVO menuVO);
	
	/**
	 * 删除菜单角色关系
	 * @param roleid
	 * @return
	 */
	void deleteMenuRole(String roleId);
	
	
	/**
	 * 统计其下子菜单的个数
	 * @param id
	 * @return 
	 */
	int countSubMenus(String id);
}
