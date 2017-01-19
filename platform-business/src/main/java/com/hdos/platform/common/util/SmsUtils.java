package com.hdos.platform.common.util;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdos.platform.base.component.service.BaseSmsService;
import com.hdos.platform.core.SpringContextHolder;

/**
 * 编码工具类
 * 
 * @author zhuw
 */
public final class SmsUtils {

	private final static Logger logger = LoggerFactory.getLogger(SmsUtils.class);
	private static BaseSmsService baseSmsService;
	private static BaseSmsService getBaseSmsService() {
		if (null == baseSmsService) {
			baseSmsService = SpringContextHolder.getBean(BaseSmsService.class);
		}
		return baseSmsService;
	}
	
	/**
	 * 发送信息
	 * @param busniessKey
	 * @param businessId 
	 * @return
	 * @throws ParseException 
	 * @throws UnknownHostException 
	 */
	public static Map<String, Object> sendSMS(String mobile,String templateId,List<String> smsParameters) {
		
		try {
			return getBaseSmsService().sendMessage(mobile, templateId, smsParameters);
		} catch (UnknownHostException e) {
			logger.info(e.getMessage());
			return null;
		} catch (ParseException e) {
			logger.info(e.getMessage());
			return null;
		}
		
	}
	
}
