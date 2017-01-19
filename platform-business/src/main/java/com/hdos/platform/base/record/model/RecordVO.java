package com.hdos.platform.base.record.model;

import java.util.Date;

import com.hdos.platform.core.base.BaseVO;


/**
 * MerchantApplyRecord对象
 * @author caicai
 * @version 1.0
 */

public class RecordVO extends BaseVO {

	private static final long serialVersionUID = 1L;
	
	
	//记录ID
	private String recordId;
	
	//商户ID
	private String merchantId;
	
	//操作人
	private String operator;
	
	//操作人姓名
	private String operatorName;
	
	//操作时间
	private Date operateTime;
	
	//操作后状态
	private String state;
	
	//操作后状态描述
	private String stateDes;
	
	//操作类型
	private int operateType;
	
	//操作描述
	private String operateDes;
	
	//上一个记录
	private String priorRecord;

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}
 
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStateDes() {
		return stateDes;
	}

	public void setStateDes(String stateDes) {
		this.stateDes = stateDes;
	}

	public int getOperateType() {
		return operateType;
	}

	public void setOperateType(int operateType) {
		this.operateType = operateType;
	}

	 
	public String getOperateDes() {
		return operateDes;
	}

	public void setOperateDes(String operateDes) {
		this.operateDes = operateDes;
	}

	public String getPriorRecord() {
		return priorRecord;
	}

	public void setPriorRecord(String priorRecord) {
		this.priorRecord = priorRecord;
	}

}