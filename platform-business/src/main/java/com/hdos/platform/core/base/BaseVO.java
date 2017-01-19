package com.hdos.platform.core.base;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * BASE VO
 * 
 * @author chenyang
 * 
 */
public abstract class BaseVO implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 2779452316629484874L;
	
	/** 未删除状态*/
	public static final int STATUS_NORMAL=0;
	
	/**删除状态*/
	public static final int STATUS_DELETE=-1;

	/** 状态 */
	protected int status;

	/** 创建时间 */
	protected Timestamp createTime;

	/** 修改时间 */
	protected Timestamp updateTime;

	/** 排序 */
	protected int sortNo;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public int getSortNo() {
		return sortNo;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}
}
