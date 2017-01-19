package com.hdos.platform.base.component.mapper;

import org.springframework.stereotype.Repository;

import com.hdos.platform.base.component.model.SmsVO;
import com.hdos.platform.core.base.BaseMapper;

@Repository
public interface SmsMapper extends BaseMapper<SmsVO> {

	/**
	 * 获取最新的验证码，通过手机号。
	 * 
	 * @param phone
	 * @return
	 */
	String getLatestByPhone(String phone);

	/**
	 * 获取最新的验证码，通过手机号。
	 * 
	 * @param phone
	 * @return
	 */
	int isSafe(String start, String end);

	/**
	 * 获取对应短信验证码的状态
	 * 
	 * @param phone
	 * @param message
	 * @return
	 */
	int getmessageStatus(String phone, String message);
}
