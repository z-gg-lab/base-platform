package com.hdos.platform.base.component.model;

import java.sql.Timestamp;
import java.util.Date;

import com.hdos.platform.core.base.BaseVO;

/**
 * CodeGenerateDetail对象
 * 
 * @version 1.0
 */

public class CodeGenerateDetailVO extends BaseVO {

	private static final long serialVersionUID = 1L;

	// ID
	private String id;

	// ID
	private String pubId;

	// 业务表主键
	private String businessId;

	// 当前序号
	private Integer no;

	// 最后编码生成时间
	private Timestamp lastGenerateTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPubId() {
		return pubId;
	}

	public void setPubId(String pubId) {
		this.pubId = pubId;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public Integer getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

	public Timestamp getLastGenerateTime() {
		return lastGenerateTime;
	}

	public void setLastGenerateTime(Timestamp lastGenerateTime) {
		this.lastGenerateTime = lastGenerateTime;
	}

}