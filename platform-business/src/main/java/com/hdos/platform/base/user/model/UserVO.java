package com.hdos.platform.base.user.model;

import com.hdos.platform.core.base.BaseVO;

/**
 * 用户信息
 * 
 */
public class UserVO extends BaseVO {
	private static final long serialVersionUID = 1L;
	
	/**
	 *  用户ID 
	 */
	private String userId;
	
	/**
	 *  用户账号 
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
	 *  用户部门ID
	 */
	private String departmentId;
	
	/**
	 * 商户ID
	 */
	private String merchantId;
	
	
	/**
	 *  用户角色ID
	 */
	private String roleId;
	
	/**
	 *  用户选择
	 */
	private boolean checked;

	/**
	 *  用户账号
	 */
	private String userAccount;
	
	/**
	 * 用户密码
	 */
	private String pwd;
	/**
	 *  用户部门名称
	 */
	private String departmentName;
	/**
	 *用户编码 
	 */
	private String userCode;
	
	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public void setMerchantId(String merchantId){
		this.merchantId = merchantId;
	}
	
	public String getMerchantId(){
		return merchantId;
	}
	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getDepartmentName(){
		return departmentName;
	}
	
	public void setDepartmentName(String departmentName){
		this.departmentName = departmentName;
	}
	
	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public boolean getChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
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
	
	
	
	/**
	 * 从UserVO中传值到AccountVO
	 * 
	 */
	public static AccountVO userVO2AccountVO(UserVO uvo){
		
		AccountVO avo = new AccountVO();
		avo.setUserAccount(uvo.getUserAccount());
		avo.setUserId(uvo.getUserId());
		avo.setPwd(uvo.getPwd());
		return avo;
	}
	
}
