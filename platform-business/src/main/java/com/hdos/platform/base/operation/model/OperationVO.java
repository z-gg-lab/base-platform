package com.hdos.platform.base.operation.model;

import com.hdos.platform.core.base.BaseVO;

/**
 * 功能VO
 * @author cyong
 *
 */
public class OperationVO extends BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 功能ID
	 */
	private String operationId;
	
	/**
	 * 菜单ID
	 */
	private String menuId;
	
	/**
	 * 功能名称
	 */
	private String operationName;
	
	/**
	 * 功能编码
	 */
	private String operationCode;
	
	/**
	 * 功能说明
	 */
	private String remark;

	
	/**
	 * 角色id 
	 */
	
	private String roleId;
	
	/** 是否被选中 */
	private boolean checked;
	
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

	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getOperationCode() {
		return operationCode;
	}

	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "OperationVO [operationId=" + operationId + ", menuId=" + menuId + ", operationName=" + operationName
				+ ", operationCode=" + operationCode + ", remark=" + remark + "]";
	}

}
