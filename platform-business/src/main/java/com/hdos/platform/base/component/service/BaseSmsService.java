package com.hdos.platform.base.component.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.hdos.platform.base.component.mapper.SmsMapper;
import com.hdos.platform.base.component.mapper.SmsTemplateMapper;
import com.hdos.platform.base.component.model.SmsTemplateVO;
import com.hdos.platform.base.component.model.SmsVO;
import com.hdos.platform.base.config.mapper.ConfigMapper;
import com.hdos.platform.common.util.CacheUtils;
import com.hdos.platform.common.util.ConfigContants;
import com.hdos.platform.common.util.ConfigUtils;
import com.hdos.platform.common.util.PrimaryKeyUtils;
import com.hdos.platform.core.base.BaseService;

@Transactional
public abstract class BaseSmsService extends BaseService<SmsVO> {

	/**
	 * 有效期10分钟(毫秒数)
	 */
	private final static int VALID_TIME_1 = 600000;

	/**
	 * 有效期10分钟
	 */
	private final static String VALID_TIME_2 = "10";

	/**
	 * 发送时间限制2分钟一次
	 */
	private final static int LIMIT_TIME = 120000;
	
	/**
	 * 校验时间
	 */
	private final static int LIMIT_TIME_1 = 60000;

	/**
	 * 
	 */
	private final static String MOBILE_PATTERN = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

	@Autowired
	private SmsMapper smsMapper;
	@Autowired
	private SmsTemplateMapper smsTemplateMapper;
	
	@Autowired
	private ConfigMapper configMapper;

	/**
	 * 发送短信的方法
	 * 
	 * @param Mobile
	 * @return
	 */
	abstract String sendSMS(String mobile, String templateId, String[] messages);

	/**
	 * 
	 * @param smsVO
	 * @throws UnknownHostException
	 * @throws ParseException
	 * @throws Exception
	 */
	public Map<String, Object> sendMessage(String mobile, String templateId, List<String> smsParameters)
			throws UnknownHostException, ParseException {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date now = new Date();
		Date limit = new Date(now.getTime() - LIMIT_TIME_1);
		// 安全限制
		if (smsMapper.isSafe(simpleDateFormat.format(limit),simpleDateFormat.format(now))>Integer.valueOf(ConfigUtils.get("SMS_COUNT"))) {
//		if (smsMapper.isSafe("2016-11-23 17:11:00","2016-11-23 17:12:00")>Integer.valueOf(ConfigUtils.get(ConfigContants.SMS_COUNT))) {
			// 关闭系统短信开关
			configMapper.updateValueByKey("false",ConfigContants.SMS_ALLOWED);
			// 更新系统缓存
			CacheUtils.put(ConfigUtils.getCacheKey(ConfigContants.SMS_ALLOWED),"false");
			String[] messages1 = new String[2];
			messages1[0] = "911";
			messages1[1] = "911";
			sendSMS(ConfigUtils.get(ConfigContants.SMS_SAFE_MOBILE),templateId,messages1);
			result.put("msg", "系统异常，请联系管理员！");
			return result;
		}

		// 系统短信开关
		String flag = ConfigUtils.get(ConfigContants.SMS_ALLOWED);

		// 验证模板ID是否存在
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("templateId", templateId);
		List<SmsTemplateVO> listSmsTemplate = smsTemplateMapper.list(condition);

		if (listSmsTemplate.size() == 0) {
			result.put("msg", "该模板不存在");
			return result;
		}

		// 验证手机号码
		if (mobile.matches(MOBILE_PATTERN)) {
			Date now_10 = new Date(now.getTime() + VALID_TIME_1);
			// 随机获取验证码
			String temp = randomFor6();
			smsParameters.add(0, temp);
			if (smsParameters.size() == 1) {
				smsParameters.add(1, VALID_TIME_2);
			}
			String[] messages = smsParameters.toArray(new String[smsParameters.size()]);

			condition.put("mobile", mobile);
			List<SmsVO> listByMobile = smsMapper.list(condition);

			SmsVO smsVO = new SmsVO();
			smsVO.setSmsId(PrimaryKeyUtils.generate(smsVO));
			smsVO.setSmsTemplateId(listSmsTemplate.get(0).getSmsTemplateId());
			smsVO.setTemplateId(templateId);
			smsVO.setMobile(mobile);
			smsVO.setCreateTime(new Timestamp(System.currentTimeMillis()));

			switch (smsParameters.size()) {
			case 1:
				smsVO.setSmsParameter1(smsParameters.get(0));
				break;
			case 2:
				smsVO.setSmsParameter1(smsParameters.get(0));
				smsVO.setSmsParameter2(smsParameters.get(1));
				break;
			case 3:
				smsVO.setSmsParameter1(smsParameters.get(0));
				smsVO.setSmsParameter2(smsParameters.get(1));
				smsVO.setSmsParameter3(smsParameters.get(2));
				break;
			case 4:
				smsVO.setSmsParameter1(smsParameters.get(0));
				smsVO.setSmsParameter2(smsParameters.get(1));
				smsVO.setSmsParameter3(smsParameters.get(2));
				smsVO.setSmsParameter4(smsParameters.get(3));
				break;
			case 5:
				smsVO.setSmsParameter1(smsParameters.get(0));
				smsVO.setSmsParameter2(smsParameters.get(1));
				smsVO.setSmsParameter3(smsParameters.get(2));
				smsVO.setSmsParameter4(smsParameters.get(3));
				smsVO.setSmsParameter5(smsParameters.get(4));
				break;
			default:
				break;
			}

			smsVO.setSendTime(simpleDateFormat.format(now));
			smsVO.setExpiryTime(simpleDateFormat.format(now_10));
			smsVO.setIpAddress(InetAddress.getLocalHost().getHostAddress());

			if (listByMobile.size() != 0) {
				// 查看上次发送的验证码是否过期
				if (compareDate(now, simpleDateFormat.parse(listByMobile.get(0).getExpiryTime()))) {
					// 发送新验证码
					smsMapper.insert(smsVO);
					CacheUtils.put(mobile, smsVO.getSmsParameter1(), VALID_TIME_1);
					// 验证是否刷短信
				} else if (now.getTime()
						- simpleDateFormat.parse(listByMobile.get(0).getSendTime()).getTime() < LIMIT_TIME) {
					result.put("msg", "发送频率过快，请稍后再试");
					return result;
				} else {
					// 发送之前的验证码
					listByMobile.get(0).setSendTime(simpleDateFormat.format(now));
					smsMapper.update(listByMobile.get(0));
					messages[0] = listByMobile.get(0).getSmsParameter1();
					smsVO.setSmsParameter1(listByMobile.get(0).getSmsParameter1());
				}
			} else {
				smsMapper.insert(smsVO);
				CacheUtils.put(mobile, smsVO.getSmsParameter1(), VALID_TIME_1);
			}

			if (flag.equals("true")) {
				result.put("msg", sendSMS(mobile, templateId, messages));
			} else {
				result.put("msg", smsVO.getSmsParameter1());
			}

		} else {
			result.put("msg", "手机号码格式不正确");
		}
		return result;

	}

	/**
	 * 获取验证码
	 * 
	 * @return
	 */
	public static String randomFor6() {
		Random random = new Random();
		String result = "";
		for (int i = 0; i < 6; i++) {
			result += random.nextInt(10);
		}
		return result;
	}

	/**
	 * 比较两个时间的大小
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public boolean compareDate(Date d1, Date d2) {
		if (d1.getTime() > d2.getTime()) {
			return true;
		} else {
			return false;
		}
	}

}
