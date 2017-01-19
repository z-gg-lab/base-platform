package com.hdos.platform.base.component.model;

import java.sql.Timestamp;
import java.util.Date;

import com.hdos.platform.core.base.BaseVO;

/**
 * CodeGenerate对象
 * 
 * @version 1.0
 */

public class CodeGenerateVO extends BaseVO {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private String id;
	/**
	 * 业务名称
	 */
	private String businessName;

	/**
	 * 业务类型
	 */
	private String businessKey;

	/**
	 * 生成策略
	 */
	private Integer generateType;

	/**
	 * 页面展示生成策略
	 */
	private String type;

	/**
	 * 最后编码生成时间
	 */
	private Timestamp lastGenerateTime;

	/**
	 * 序号规则
	 */
	private String rule;

	/**
	 * 序号长度
	 */
	private Integer length;

	/**
	 * 当前序号
	 */
	private Integer no;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public Integer getGenerateType() {
		return generateType;
	}

	public void setGenerateType(Integer generateType) {
		this.generateType = generateType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Timestamp getLastGenerateTime() {
		return lastGenerateTime;
	}

	public void setLastGenerateTime(Timestamp lastGenerateTime) {
		this.lastGenerateTime = lastGenerateTime;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

}