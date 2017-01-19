package com.hdos.platform.base.user.service;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hdos.platform.base.BaseTest;
import com.hdos.platform.base.user.model.UserVO;
import com.hdos.platform.base.user.service.UserService;

public class UserServiceTest extends BaseTest {
	
	@Autowired
	private UserService userService;

	@Test
	public void testFindPage() {
	}

	@Test
	public void testGetUserIdByAccount() {
	}

	@Test
	public void testSerchByUserId() {
		UserVO userVO = userService.findById("402882e7-56b0c799-0156-b0c79902-0000");
		System.out.println(userVO);
	}

	@Test
	public void testFindAllUsers() {
	}

	@Test
	public void testVerifyRepeat() {
	}

	@Test
	public void testSaveUser() {
	}

	@Test
	public void testSaveUserRole() {
	}

	@Test
	public void testSaveUserRoleSecond() {
	}

	@Test
	public void testDeleteUsers() {
	}

	@Test
	public void testDeleteById() {
	}

	@Test
	public void testFindById() {
	}

	@Test
	public void testFindByClassId() {
	}

	@Test
	public void testSave() {
	}

	@Test
	public void testQueryDepartmentName() {
	}

	@Test
	public void testListTreeOfRole() {
	}

}
