package com.hdos.platform.base;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
({"/applicationContext*.xml"})
public class BaseTest {
	
	protected String rootPath;
	
	@Before
	public void before() {
		rootPath = this.getClass().getClassLoader().getResource("").getPath();
	}
	
	@Test
	public void test() {
		
	}
}
