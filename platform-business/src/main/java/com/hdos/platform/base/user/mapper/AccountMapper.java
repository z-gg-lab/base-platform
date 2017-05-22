package com.hdos.platform.base.user.mapper;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hdos.platform.base.user.model.AccountVO;
import com.hdos.platform.core.base.BaseMapper;

@Repository
public interface AccountMapper extends BaseMapper<AccountVO>{
	/**
	 * 根据帐号和密码查询账户信息
	 * @param params
	 * @return
	 */
	AccountVO queryAccountByAccountAndPwd(Map<String, Object> params);

	String getUserIdByAccount(String account);
	
	/**
	 * 解锁
	 * @param password
	 * @param userId
	 * @return AccountVO
	 */
	AccountVO unlock(String password, String userId);
}
