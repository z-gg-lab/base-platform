package com.hdos.platform.base.menu.model;

import com.hdos.platform.core.base.BaseVO;

/**
 * 菜单
 * 
 * @author chenyang
 *
 */
public class MenuVO extends BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 菜单类型：目录 */
	public static final int IS_DIR = 1;

	/** 菜单类型：菜单 */
	public static final int IS_NOT_DIR = 0;

	/** 菜单ID */
	private String menuId;

	/** 菜单名 */
	private String menuName;

	/** 父级ID */
	private String pMenuId;

	/** 父节点ID */
	
	private String parentId;
	
	/** 父级名 */
	private String pMenuName;

	/** URL */
	private String url;

	/** iconCls */
	private String iconCls;
	
	/** 是否目录 */
	private int isDir;

	/** state */
	private String state;
	
	/** 更新类型 */
	private int editType;

	/** 角色ID */
	private String roleId;
	
	/** 用户ID */
	private String userId;
	
	/** 是否被选中 */
	private boolean checked;
	
	/** 是否有功能列表 */
	private boolean flag;

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	public boolean getFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public boolean getChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getpMenuId() {
		return pMenuId;
	}

	public void setpMenuId(String pMenuId) {
		this.pMenuId = pMenuId;
	}

	public String getpMenuName() {
		return pMenuName;
	}

	public void setpMenuName(String pMenuName) {
		this.pMenuName = pMenuName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getIsDir() {
		return isDir;
	}

	public void setIsDir(int isDir) {
		this.isDir = isDir;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getEditType() {
		return editType;
	}

	public void setEditType(int editType) {
		this.editType = editType;
	}

	@Override
	public String toString() {
		return "MenuVO [menuId=" + menuId + ", menuName=" + menuName + ", pMenuId=" + pMenuId + ", pMenuName="
				+ pMenuName + ", url=" + url + ", isDir=" + isDir + ", state=" + state + ", editType=" + editType + "]";
	}
	

}
