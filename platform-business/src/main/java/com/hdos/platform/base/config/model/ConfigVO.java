package com.hdos.platform.base.config.model;

import org.apache.commons.lang.StringUtils;

import com.hdos.platform.common.vo.TreeNodeTransformer;
import com.hdos.platform.common.vo.TreeVO;
import com.hdos.platform.core.base.BaseVO;

/**
 * 配置项
 * 
 * @author Arthur
 */
public class ConfigVO extends BaseVO implements TreeNodeTransformer {

	/** serialVersionUID */
	private static final long serialVersionUID = -232326413266358414L;

	/** 非叶子结点 */
	public static final int LEAF_NO = 1;

	/** 叶子结点 */
	public static final int LEAF_YES = 2;

	/** 配置项ID **/
	private String id;

	/** 配置项父节点ID **/
	private String parentId;

	/** 配置项名称 **/
	private String name;

	/** 配置项编码 **/
	private String code;

	/** 配置项全编码 **/
	private String fullCode;

	/** 配置描述 **/
	private String description;

	/** 是否叶子节点(1:非叶子节点、2:叶子节点) **/
	private int leafMark;

	/** 配置项编码 **/
	private String configKey;

	/** 配置项值 **/
	private String configValue;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the parentId
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * @param parentId
	 *            the parentId to set
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the fullCode
	 */
	public String getFullCode() {
		return fullCode;
	}

	/**
	 * @param fullCode
	 *            the fullCode to set
	 */
	public void setFullCode(String fullCode) {
		this.fullCode = fullCode;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the leafMark
	 */
	public int getLeafMark() {
		return leafMark;
	}

	/**
	 * @param leafMark
	 *            the leafMark to set
	 */
	public void setLeafMark(int leafMark) {
		this.leafMark = leafMark;
	}

	/**
	 * @return the configKey
	 */
	public String getConfigKey() {
		return configKey;
	}

	/**
	 * @param configKey
	 *            the configKey to set
	 */
	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	/**
	 * @return the configValue
	 */
	public String getConfigValue() {
		return configValue;
	}

	/**
	 * @param configValue
	 *            the configValue to set
	 */
	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

	@Override
	public TreeVO getTreeNode() {
		TreeVO treeNode = new TreeVO();
		treeNode.setId(this.getId());
		treeNode.setText(this.getName());
		if (ConfigVO.LEAF_NO == this.getLeafMark()) {
			treeNode.setState(TreeVO.STATE_CLOSED);
		} else {
			treeNode.setState(TreeVO.STATE_OPEN);
		}
		// 添加属性
		treeNode.addAttribute("code", StringUtils.trimToEmpty(this.getCode()));
		treeNode.addAttribute("fullCode", StringUtils.trimToEmpty((this.getFullCode())));
		treeNode.addAttribute("parentId", StringUtils.trimToEmpty(this.getParentId()));
		treeNode.addAttribute("description", StringUtils.trimToEmpty(this.getDescription()));
		treeNode.addAttribute("configKey", StringUtils.trimToEmpty(this.getConfigKey()));
		treeNode.addAttribute("configValue", StringUtils.trimToEmpty(this.getConfigValue()));

		return treeNode;
	}
}
