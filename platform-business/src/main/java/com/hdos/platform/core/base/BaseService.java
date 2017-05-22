package com.hdos.platform.core.base;

import com.hdos.platform.common.util.PrimaryKeyUtils;

public class BaseService<T> {

	/** 编辑类型：新增 */
	public static final int EDIT_TYPE_NEW = 0;
	
	/** 编辑类型：修改 */
	public static final int EDIT_TYPE_UPDATE = 1;
	
	/**
	 * 生成主键
	 * @param vo
	 * @return
	 */
	protected String generateKey(T vo) {
		return PrimaryKeyUtils.generate(vo);
	}
}
