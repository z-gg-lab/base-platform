package com.hdos.platform.base.user.mapper;

import org.springframework.stereotype.Repository;

import com.hdos.platform.base.user.model.UserTokenVO;
import com.hdos.platform.core.base.BaseMapper;

@Repository
public interface UserTokenMapper extends BaseMapper<UserTokenVO> {
	/**
	 * 查询 id
	 * 
	 * @param id
	 *            主键
	 * @return 对象
	 */
	UserTokenVO getByUserAccount(String userAccount);
}
