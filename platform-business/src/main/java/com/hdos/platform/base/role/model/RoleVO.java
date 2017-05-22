package com.hdos.platform.base.role.model;

import com.hdos.platform.core.base.BaseVO;

/**
 * 角色VO
 * 
 * @author chenyang
 *
 */
public class RoleVO extends BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *  角色ID
	 */
	private String roleId;
	
	/**
	 *  角色名称
	 */
	private String roleName;
	
	/**
	 *  角色描述
	 */
	private String remark;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "RoleVO [roleId=" + roleId + ", roleName=" + roleName + ", remark=" + remark + "]";
	}
}
