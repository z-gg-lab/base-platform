package com.hdos.platform.base.user.model;

import com.hdos.platform.core.base.BaseVO;

public class AccountInfoVO extends BaseVO{
	private static final long serialVersionUID = 1L;

	/**
	 *  用户ID
	 */
	private String userId;

	/**
	 *  用户昵称
	 */
	private String nickName;

	/**
	 *  用户姓名
	 */
	private String userName;

	/**
	 *  用户类型
	 */
	private String type;

	/**
	 *  用户性别
	 */
	private int gender;

	/**
	 *  用户电话
	 */
	private String phone;

	/**
	 *  用户邮箱
	 */
	private String email;

	/**
	 *  用户描述
	 */
	private String description;

	/**
	 *  用户账号
	 */
	private String userAccount;
	
	/**
	 *  用户密码
	 */
	private String pwd;

	/**
	 *  用户部门ID
	 */
	private String departmentId;
	
	/**
	 * 商户ID
	 */
	private String merchantId;
	
	/**
	 *  用户部门名称
	 */
	private String departmentName;
	/**
	 *  用户部门编码
	 */
	private String departmentCode;
	/**
	 * 机构编码
	 */
	private String fullCode;
	/**
	 * 商户编码
	 * @param merchantId
	 */
	private String  merchantCode;
	/**
	 * 商户名称
	 * @param merchantId
	 */
	private String merchantName;
	/**
	 *用户编码 
	 */
	private String userCode;
	/**
	 * 用户角色
	 */
	private String userRole;
	
	
	
	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	
	
	public String getFullCode() {
		return fullCode;
	}

	public void setFullCode(String fullCode) {
		this.fullCode = fullCode;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public void setMerchantId(String merchantId){
		this.merchantId = merchantId;
	}
	
	public String getMerchantId(){
		return merchantId;
	}
	public String getDepartmentName(){
		return departmentName;
	}
	public void setDepartmentName(String departmentName){
		this.departmentName = departmentName;
	}
	
	public String getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	/**
	 * 从accountInfoVO中传递值到UserVO
	 * 
	 */
	public static UserVO accountInfo2UserVO(AccountInfoVO aivo){
		UserVO uvo = new UserVO();
		uvo.setUserId(aivo.getUserId());
		uvo.setNickName(aivo.getNickName());
		uvo.setUserName(aivo.getUserName());
		uvo.setType(aivo.getType());
		uvo.setGender(aivo.getGender());
		uvo.setPhone(aivo.getPhone());
		uvo.setEmail(aivo.getEmail());
		uvo.setDescription(aivo.getDescription());
		uvo.setDepartmentId(aivo.getDepartmentId());
		uvo.setMerchantId(aivo.getMerchantId());
		uvo.setUserAccount(aivo.getUserAccount());
		uvo.setPwd(aivo.getPwd());
		uvo.setUserCode(aivo.getUserCode());
		return uvo;
		
	}
	
	/**
	 * 从accountInfoVO中传递值到AccountVO
	 * 
	 */
	public static AccountVO accountInfoVO2AccountVO(AccountInfoVO aivo){
		AccountVO avo = new AccountVO();
		avo.setUserAccount(aivo.getUserAccount());
		avo.setUserId(aivo.getUserId());
		avo.setPwd(aivo.getPwd());
		
		return avo;
	}
	
	
}
