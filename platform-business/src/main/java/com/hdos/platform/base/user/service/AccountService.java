package com.hdos.platform.base.user.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hdos.platform.base.user.mapper.AccountMapper;
import com.hdos.platform.base.user.model.AccountVO;

@Service
@Transactional
public class AccountService {
	
	@Autowired
	private AccountMapper accountMapper;	
	/**
	 * 根据帐号和密码查询账户信息
	 * @param userAccount
	 * @param pwd
	 * @return
	 */
	public AccountVO queryAccountByAccountAndPwd(String userAccount,String pwd) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userAccount", userAccount);
		params.put("pwd", pwd);
		return accountMapper.queryAccountByAccountAndPwd(params);
	}
}
