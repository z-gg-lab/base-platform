package com.hdos.platform.base.component.model;

import com.hdos.platform.core.base.BaseVO;

/**
 * SmsTemplate对象
 * 
 * @author zhuw
 * @version 1.0
 */

public class SmsTemplateVO extends BaseVO {

	private static final long serialVersionUID = 1L;

	// 模板配置ID
	private String smsTemplateId;

	// 模板ID
	private String templateId;

	// 模板内容
	private String templateContent;

	// 短信签名
	private String smsSignature;

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

	public String getTemplateContent() {
		return templateContent;
	}

	public void setTemplateContent(String templateContent) {
		this.templateContent = templateContent;
	}

	public String getSmsSignature() {
		return smsSignature;
	}

	public void setSmsSignature(String smsSignature) {
		this.smsSignature = smsSignature;
	}

}