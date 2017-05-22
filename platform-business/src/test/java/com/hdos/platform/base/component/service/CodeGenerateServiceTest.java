package com.hdos.platform.base.component.service;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.hdos.platform.base.BaseTest;
import com.hdos.platform.base.component.model.CodeGenerateVO;
import com.hdos.platform.base.component.service.CodeGenerateService;
@Transactional
public class CodeGenerateServiceTest extends BaseTest {

	@Autowired
	private CodeGenerateService codeGenerateService;
	
	@Test
	public void testSaveCode() {
		CodeGenerateVO codeGenerateVO =  new CodeGenerateVO();
		try {
			codeGenerateVO.setBusinessKey("junit");
			codeGenerateVO.setBusinessName("junit");
			codeGenerateService.saveCode(codeGenerateVO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testDeleteCodes() {
		codeGenerateService.deleteCodes("402882e6-58901111-0158-9011d391-0002");
	}

	@Test
	public void testFindPage() {
		 Map<String, Object> codition = new HashMap<String, Object>();
		 assertNotNull(codeGenerateService.findPage(codition,1,20));
	}

	@Test
	public void testGetById() {
		assertNull(codeGenerateService.getById("1"));
	}

	@Test
	public void testGetCombobox() {
		assertNotNull(codeGenerateService.getCombobox());
	}

	@Test
	public void testGetCode() {
		if(codeGenerateService.getCode("junit",null).isEmpty()){
			assertNotNull("junit");
		}
	}

}
