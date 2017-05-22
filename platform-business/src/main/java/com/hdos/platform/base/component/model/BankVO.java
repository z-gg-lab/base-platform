package com.hdos.platform.base.component.model;

import com.hdos.platform.core.base.BaseVO;

/**
 * 银行信息
 * 
 * @author Cocouzx
 *
 */
public class BankVO extends BaseVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 银行D */
	private String bankId;

	/** 银行名称 */
	private String bankName;

	/** 银行编码 */
	private String bankCode;

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

}
