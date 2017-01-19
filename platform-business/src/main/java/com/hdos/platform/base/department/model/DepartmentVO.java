package com.hdos.platform.base.department.model;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

import com.hdos.platform.base.config.model.ConfigVO;
import com.hdos.platform.common.vo.TreeNodeTransformer;
import com.hdos.platform.common.vo.TreeVO;
import com.hdos.platform.core.base.BaseVO;

/**
 * 机构管理
 * 
 * @author zhuw
 *
 */
public class DepartmentVO extends BaseVO implements TreeNodeTransformer{

	private static final long serialVersionUID = 1L;
	
	/** 非叶子结点 */
	public static final int LEAF_NO = 1;

	/** 叶子结点 */
	public static final int LEAF_YES = 2;
	
	/** 机构ID*/
	private String departmentId;
	
	/** 父级Id*/
	private String parentId;
	
	/** 机构名称*/
	private String departmentName;
	
	/** 机构全名*/
	private String fullName;
	
	/** 机构CODE*/
	private String code;
	
	/** 机构全CODE*/
	private String fullCode;
	
	/** 层级*/
	private BigDecimal departmentLevel;
	
	/** 机构类型*/
	private BigDecimal type;
	
	/** state */
	private String state;
	
	/**  备注*/
	private String remark;
	
	/**  部门编码*/
	private String departmentCode;
	
	/** 更新类型 */
	private int editType;
	
	/** 父级名称 */
	private String parentName;
	
	/** 是否叶子节点(1:非叶子节点、2:叶子节点) **/
	private int leafMark;
	
	/**商户编号**/
	private String infoCode;
	/** 是否有商户 **/
	private boolean hasMerchant;
	
	public String getDepartmentCode() {
		return departmentCode;
	}

	public String getInfoCode() {
		return infoCode;
	}

	public void setInfoCode(String infoCode) {
		this.infoCode = infoCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public boolean getHasMerchant() {
		return hasMerchant;
	}

	public void setHasMerchant(boolean hasMerchant) {
		this.hasMerchant = hasMerchant;
	}

	public int getLeafMark() {
		return leafMark;
	}

	public void setLeafMark(int leafMark) {
		this.leafMark = leafMark;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public int getEditType() {
		return editType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getFullCode() {
		return fullCode;
	}

	public void setFullCode(String fullCode) {
		this.fullCode = fullCode;
	}
	

	public void setEditType(int editType) {
		this.editType = editType;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public String getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public BigDecimal getDepartmentLevel() {
		return departmentLevel;
	}
	public void setDepartmentLevel(BigDecimal departmentLevel) {
		this.departmentLevel = departmentLevel;
	}
	public BigDecimal getType() {
		return type;
	}
	public void setType(BigDecimal type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "DepartmentVO [departmentId=" + departmentId + ", parentId=" + parentId + ", departmentName="
				+ departmentName + ", fullName=" + fullName + ", code=" + code + ", departmentLevel=" + departmentLevel + ", type=" + type
				+ ", state=" + state + ", editType=" + editType + ", remark=" + remark + "]";
	}

	@Override
	public TreeVO getTreeNode() {
		TreeVO treeNode = new TreeVO();
		treeNode.setId(this.getDepartmentId());
		treeNode.setText(this.getDepartmentName());
		if (DepartmentVO.LEAF_NO == this.getLeafMark()) {
			treeNode.setState(TreeVO.STATE_CLOSED);
		} else {
			treeNode.setState(TreeVO.STATE_OPEN);
		}
		// 添加属性
		treeNode.addAttribute("departmentId", StringUtils.trimToEmpty(this.getDepartmentId()));
		treeNode.addAttribute("code", StringUtils.trimToEmpty(this.getCode()));
		treeNode.addAttribute("fullCode", StringUtils.trimToEmpty((this.getFullCode())));
		treeNode.addAttribute("parentId", StringUtils.trimToEmpty(this.getParentId()));
		treeNode.addAttribute("departmentName", StringUtils.trimToEmpty(this.getDepartmentName()));
		treeNode.addAttribute("hasMerchant",this.getHasMerchant());

		return treeNode;
	}

}
