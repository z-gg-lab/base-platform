package com.hdos.platform.base.component.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloopen.rest.sdk.CCPRestSDK;
import com.hdos.platform.base.component.mapper.SmsMapper;
import com.hdos.platform.common.util.ConfigContants;
import com.hdos.platform.common.util.ConfigUtils;

@Service
@Transactional
public class SmsService extends BaseSmsService {

	@Autowired
	private SmsMapper smsMapper;

	@Override
	String sendSMS(String mobile, String templateId, String[] messages) {

		String APP_ID = ConfigUtils.get(ConfigContants.APP_ID);
		String ACCOUNT_SID = ConfigUtils.get(ConfigContants.ACCOUNT_SID);
		String AUTH_TOKEN = ConfigUtils.get(ConfigContants.AUTH_TOKEN);

		HashMap<String, Object> result = null;

		CCPRestSDK restAPI = new CCPRestSDK();
		// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
		restAPI.init("sandboxapp.cloopen.com", "8883");
		// 初始化主帐号和主帐号TOKEN
		restAPI.setAccount(ACCOUNT_SID, AUTH_TOKEN);
		restAPI.setAppId(APP_ID);// 初始化应用ID
		result = restAPI.sendTemplateSMS(mobile, templateId, messages);

		if ("000000".equals(result.get("statusCode"))) {
			// 正常返回输出data包体信息（map）
			return "发送成功";
		} else {
			// 异常返回输出错误码和错误信息
			return (String) result.get("statusMsg");
		}
	}

	/**
	 * 获取对应短信验证码的状态
	 * 
	 * @param phone
	 * @param message
	 * @return
	 */
	public int getmessageStatus(String phone, String message) {
		return smsMapper.getmessageStatus(phone, message);
	}
}
