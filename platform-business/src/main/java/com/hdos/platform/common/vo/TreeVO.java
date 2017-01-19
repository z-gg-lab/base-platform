package com.hdos.platform.common.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * 通用easyui tree VO
 * 
 * @author chenyang
 * 
 */
public class TreeVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 默认根节点ID */
	public static final String DEFAULT_ROOT_NODE_ID = "-1";

	/** 菜单打开状态 */
	public static final String STATE_OPEN = "open";

	/** 菜单关闭状态 */
	public static final String STATE_CLOSED = "closed";

	/** 属性：url */
	public static final String ATTR_URL = "url";

	/** id */
	private String id;

	/** text */
	private String text;

	/** iconCls */
	private String iconCls;

	/** state */
	private String state;

	/** attributes */
	private JSONObject attributes;

	/** children */
	private List<TreeVO> children;
	
	/** checked */
	private Boolean checked;

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(JSONObject attributes) {
		this.attributes = attributes;
	}

	public List<TreeVO> getChildren() {
		return children;
	}

	public void setChildren(List<TreeVO> children) {
		this.children = children;
	}

	public TreeVO addAttribute(String attrName, Object attrValue) {
		if (null == this.attributes) {
			this.attributes = new JSONObject();
		}
		this.attributes.put(attrName, attrValue);
		return this;
	}
}
