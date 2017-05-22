package com.hdos.platform.base.record.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hdos.platform.common.util.PrimaryKeyUtils;
@Service
@Transactional
public class RecordService<T> {
	

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
