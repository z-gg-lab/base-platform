package com.hdos.platform.base.menu.service;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.hdos.platform.base.BaseTest;
import com.hdos.platform.base.menu.model.MenuVO;
import com.hdos.platform.base.menu.service.MenuService;
@Transactional
public class MenuServiceTest extends BaseTest {

	@Autowired
	private MenuService menuService;
	
	@Test
	public void testQueryTreeByPMenuId() {
		List<MenuVO> list = menuService.queryMenuByPMenuId("-1");
		assertNotNull(list);
	}

	@Test
	public void testQueryTreeByRole() {
		assertNotNull(menuService.queryTreeByRole("-1","402882e5-567d6841-0156-7d693379-0001"));
	}

	@Test
	public void testGetTreeDataByRoleSelected() {
		assertNotNull(menuService.getTreeDataByRoleSelected("-1", "402882e5-567d6841-0156-7d693379-0001"));
	}

	@Test
	public void testBuildMenuVOByEditType() {
		assertNotNull(menuService.buildMenuVOByEditType("241f86af-d55e-423c-966b-fb5403012771",3));
	}

	@Test
	public void testSaveMenu() {
		MenuVO menuVO = new MenuVO();
		menuVO.setpMenuId("-1");
		menuVO.setMenuName("junit");
		menuService.saveMenu(menuVO);
	}

	@Test
	public void testSortMenu() {

	}

	@Test
	public void testSaveMenuRole() {
		String[] menuIds = {"241f86af-d55e-423c-966b-fb5403012771"};
		menuService.saveMenuRole(menuIds, "402882e5-567d6841-0156-7d693379-0001");
	}

	@Test
	public void testQueryMenuByPMenuId() {
		assertNotNull(menuService.queryMenuByPMenuId("-1"));
	}

	@Test
	public void testDeleteMenu() {
		menuService.deleteMenu("402882e6-5847b5d2-0158-48028eb2-0033");
	}

	@Test
	public void testCountSubMenus() {
		assertNotNull(menuService.countSubMenus("-1"));
	}

}
