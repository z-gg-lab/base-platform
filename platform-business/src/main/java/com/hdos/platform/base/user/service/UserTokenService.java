package com.hdos.platform.base.user.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.util.Base64;
import com.alibaba.fastjson.JSONObject;
import com.hdos.platform.base.user.mapper.UserTokenMapper;
import com.hdos.platform.base.user.model.AccountVO;
import com.hdos.platform.base.user.model.UserTokenVO;
import com.hdos.platform.base.user.model.UserVO;
import com.hdos.platform.common.apireturnmessage.ApiReturnMessage;
import com.hdos.platform.common.util.CacheUtils;
import com.hdos.platform.common.util.PrimaryKeyUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserTokenService {
	@Autowired
	private UserTokenMapper userTokenMapper;
	@Autowired
	private AccountService accountService;
	@Autowired
	private UserService userService;
	private static final Logger logger = LoggerFactory.getLogger(UserTokenService.class);
	/** 用户所属类型 */
	/** 用户所属商户 */
	private static final String MERCHANT_TYPE = "1002";
	/** 缓存存在时间 */
	private final static long TIME_OUT = 10000000;

	public String createToken(String username, String password, String captcha, String usercaptchaId,
			String captchaString, String captchaId) {
		if (CacheUtils.get(username) == null) {
			int loginCount = 0;
			CacheUtils.put(username, loginCount, TIME_OUT);
		}
		int loginCount = Integer.parseInt(CacheUtils.get(username).toString()) + 1;
		CacheUtils.put(username, loginCount, TIME_OUT);
		JSONObject jsonObject = new JSONObject();
		password = DigestUtils.md5Hex(password);
		AccountVO accountVO = accountService.queryAccountByAccountAndPwd(username, password);
		if (loginCount > 5 && (captchaString == null || captchaId == null)) {
			jsonObject.put("resultCode", ApiReturnMessage.E100002);
			jsonObject.put("errorMsg", ApiReturnMessage.E100002_MSG);
			jsonObject.put("needCaptcha", ApiReturnMessage.E100010);
			return jsonObject.toString();
		} else if (loginCount > 5 && (!(captcha.equalsIgnoreCase(captchaString) && usercaptchaId.equals(captchaId)))) {
			jsonObject.put("resultCode", ApiReturnMessage.E100002);
			jsonObject.put("errorMsg", ApiReturnMessage.E100002_MSG);
			jsonObject.put("needCaptcha", ApiReturnMessage.E100010);
			return jsonObject.toString();
		} else if (null != accountVO) {
			UserVO userVO = userService.findById(accountVO.getUserId());
			if (MERCHANT_TYPE.equals(userVO.getType())) {
				UserTokenVO userTokenVO = new UserTokenVO();
				userTokenMapper.delete(username);
				String userToken = PrimaryKeyUtils.generate(userTokenVO);
				userTokenVO.setToken(userToken);
				userTokenVO.setUserAccount(username);
				userTokenMapper.insert(userTokenVO);
				CacheUtils.put(username, 0, TIME_OUT);
				jsonObject.put("resultCode", ApiReturnMessage.S100000);
				jsonObject.put("token", userToken);
				return jsonObject.toString();
			}
			if (loginCount >= 5) {
				jsonObject.put("needCaptcha", ApiReturnMessage.E100010);
			} else {
				jsonObject.put("needCaptcha", ApiReturnMessage.E100009);
			}
			jsonObject.put("resultCode", ApiReturnMessage.E100001);
			jsonObject.put("errorMsg", ApiReturnMessage.E100001_MSG);
			return jsonObject.toString();

		}
		jsonObject.put("resultCode", ApiReturnMessage.E100002);
		jsonObject.put("errorMsg", ApiReturnMessage.E100002_MSG);
		if (loginCount >= 5) {
			jsonObject.put("needCaptcha", ApiReturnMessage.E100010);
		} else {
			jsonObject.put("needCaptcha", ApiReturnMessage.E100009);
		}
		return jsonObject.toString();
	}

	public String createCaptcha(String account, BufferedImage image, StringBuffer code) {
		if (account == null || account == "") {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("resultCode", ApiReturnMessage.E100014);
			jsonObject.put("errorMsg", ApiReturnMessage.E100014_MSG);
			return jsonObject.toString();
		}
		ByteArrayOutputStream outputStream = null;
		String base64String = null;
		try {
			outputStream = new ByteArrayOutputStream();
			ImageIO.write(image, "png", outputStream);
			base64String = Base64.byteArrayToBase64(outputStream.toByteArray());
		} catch (IOException e) {
			logger.error("IOException错误。", e);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("resultCode", ApiReturnMessage.E100014);
			jsonObject.put("errorMsg", ApiReturnMessage.E100014_MSG);
			return jsonObject.toString();
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("resultCode", ApiReturnMessage.S100000);
		jsonObject.put("captchaImage", base64String);
		String captchaId = PrimaryKeyUtils.generate(account);
		jsonObject.put("captchaId", captchaId);
		CacheUtils.put(account + "captchaString", code.toString(), TIME_OUT);
		CacheUtils.put(account + "captchaId", captchaId, TIME_OUT);
		return jsonObject.toString();
	}
}
