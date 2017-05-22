package com.hdos.platform.core.jpa;

import java.io.Serializable;

/**
 * 主键生成器，from Hibernate
 * 
 * @author Arthur
 */
public interface IdentifierGenerator {

	/**
	 * @param object
	 *            对象
	 * @return 主键
	 */
	public Serializable generate(Object object);
}
