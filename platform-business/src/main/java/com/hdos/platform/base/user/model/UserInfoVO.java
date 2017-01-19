package com.hdos.platform.base.user.model;

import com.hdos.platform.core.base.BaseVO;

/**
 * 用户扩展信息
 * 
 */
public class UserInfoVO extends BaseVO {

	/** 序列串 */
	private static final long serialVersionUID = -4260866178183029619L;

	/** 主键 */
	private String userInfoId;

	/** 用户ID */
	private String userId;

	/** 院系ID */
	private String departmentId;

	/** 班级ID */
	private String classId;

	/** 专业ID */
	private String subjectId;

	/** QQ号码 */
	private String qqId;

	/** 微信号 */
	private String wxId;

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the departmentId
	 */
	public String getDepartmentId() {
		return departmentId;
	}

	/**
	 * @param departmentId
	 *            the departmentId to set
	 */
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	/**
	 * @return the classId
	 */
	public String getClassId() {
		return classId;
	}

	/**
	 * @param classId
	 *            the classId to set
	 */
	public void setClassId(String classId) {
		this.classId = classId;
	}

	/**
	 * @return the subjectId
	 */
	public String getSubjectId() {
		return subjectId;
	}

	/**
	 * @param subjectId
	 *            the subjectId to set
	 */
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	/**
	 * @return the qqId
	 */
	public String getQqId() {
		return qqId;
	}

	/**
	 * @param qqId
	 *            the qqId to set
	 */
	public void setQqId(String qqId) {
		this.qqId = qqId;
	}

	/**
	 * @return the wxId
	 */
	public String getWxId() {
		return wxId;
	}

	/**
	 * @param wxId
	 *            the wxId to set
	 */
	public void setWxId(String wxId) {
		this.wxId = wxId;
	}

	/**
	 * @return the userInfoId
	 */
	public String getUserInfoId() {
		return userInfoId;
	}

	/**
	 * @param userInfoId
	 *            the userInfoId to set
	 */
	public void setUserInfoId(String userInfoId) {
		this.userInfoId = userInfoId;
	}
}
