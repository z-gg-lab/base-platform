package com.hdos.platform.base.component.model;

import com.hdos.platform.core.base.BaseVO;

/**
 * Sms对象
 * 
 * @author zhuw
 * @version 1.0
 */

public class SmsVO extends BaseVO {

	private static final long serialVersionUID = 1L;

	// 短信ID
	private String smsId;
	
	// 模板配置ID
	private String smsTemplateId;
	
	// 模板ID
	private String templateId;

	// 手机号码
	private String mobile;

	// 信息参数{1}
	private String smsParameter1;

	// 信息参数{2}
	private String smsParameter2;

	// 信息参数{3}
	private String smsParameter3;

	// 信息参数{4}
	private String smsParameter4;

	// 信息参数{5}
	private String smsParameter5;

	// 发送时间
	private String sendTime;

	// 有效时间
	private String expiryTime;

	// IP
	private String ipAddress;

	public String getSmsId() {
		return smsId;
	}

	public void setSmsId(String smsId) {
		this.smsId = smsId;
	}

	public String getSmsTemplateId() {
		return smsTemplateId;
	}

	public void setSmsTemplateId(String smsTemplateId) {
		this.smsTemplateId = smsTemplateId;
	}
	
	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getSmsParameter1() {
		return smsParameter1;
	}

	public void setSmsParameter1(String smsParameter1) {
		this.smsParameter1 = smsParameter1;
	}

	public String getSmsParameter2() {
		return smsParameter2;
	}

	public void setSmsParameter2(String smsParameter2) {
		this.smsParameter2 = smsParameter2;
	}

	public String getSmsParameter3() {
		return smsParameter3;
	}

	public void setSmsParameter3(String smsParameter3) {
		this.smsParameter3 = smsParameter3;
	}

	public String getSmsParameter4() {
		return smsParameter4;
	}

	public void setSmsParameter4(String smsParameter4) {
		this.smsParameter4 = smsParameter4;
	}

	public String getSmsParameter5() {
		return smsParameter5;
	}

	public void setSmsParameter5(String smsParameter5) {
		this.smsParameter5 = smsParameter5;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(String expiryTime) {
		this.expiryTime = expiryTime;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

}