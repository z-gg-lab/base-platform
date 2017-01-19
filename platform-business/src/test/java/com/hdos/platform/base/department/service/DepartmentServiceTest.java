package com.hdos.platform.base.department.service;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.hdos.platform.base.BaseTest;
import com.hdos.platform.base.department.model.DepartmentVO;
import com.hdos.platform.base.department.service.DepartmentService;
@Transactional
public class DepartmentServiceTest extends BaseTest {

	
	@Autowired
	private DepartmentService departmentService;
	
	@Test
	public void testQueryTreeByParentId() {
		assertNotNull(departmentService.queryTreeByParentId("-1"));
	}

	@Test
	public void testQueryAllTreeByParentId() {
		assertNotNull(departmentService.queryAllTreeByParentId("-1"));
	}

	@Test
	public void testSaveDepartment() {
		DepartmentVO departmentVO = new DepartmentVO();
		departmentVO.setParentId("-1");
		departmentVO.setDepartmentName("junit");
		departmentService.saveDepartment(departmentVO);
	}

	@Test
	public void testQueryByParentId() {
		assertNotNull(departmentService.queryByParentId("-1"));
	}

	@Test
	public void testDelete() throws Exception {
		departmentService.delete("402882e6-586fead7-0158-6fead724-0001");
	}

	@Test
	public void testCountSubDepartments() {
		departmentService.countSubDepartments("402882e6-586fead7-0158-6fead724-0001");
	}

	@Test
	public void testSearchFirstDepartment() {
		assertNotNull(departmentService.searchFirstDepartment());
	}

	@Test
	public void testGetFirstName() {
		assertNotNull(departmentService.getFirstName());
	}

	@Test
	public void testGetById() {
		assertNull(departmentService.getById("121"));
	}

	@Test
	public void testGetIdByFullName() {
		assertNull(departmentService.getIdByFullName("junit"));
	}

}
