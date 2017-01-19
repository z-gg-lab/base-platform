package com.hdos.platform.base.role.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.hdos.platform.base.role.model.RoleVO;
import com.hdos.platform.base.user.model.UserVO;
import com.hdos.platform.core.base.BaseMapper;

@Repository
public interface RoleMapper extends BaseMapper<RoleVO> {
	public int verifyRepeat(String roleName);
	public int verifyRight(String... roleIds);
	public List<String> queryRoleByuserAccount(String userAccount);
	public List<String> queryOperationByuserAccount(String userAccount);
	public void deleteRoleUsers(UserVO userVO);
	/**
	 * 在PUB_USER_R_ROLE中删除userIds所对应的记录。
	 * @param userIds
	 */
	public void deleteUserRole(String... userIds);
	/**
	 * 用户对应的角色
	 * @return
	 */
	public ArrayList<RoleVO> listUserRole(String userId, RowBounds rowBounds);
	/**
	 * 计数，根据用户角色
	 * @param userId
	 * @return
	 */
	public int countUserRole(String userId);
	
	/**
	 * 显示所有角色，不分页版
	 * @return
	 */
	public List<RoleVO> listAll();
	/**
	 * 显示所有未分配角色，不分页
	 * @param queryCondition
	 * @return
	 */
	public List<RoleVO> listExcludeUserNoPage(@Param("condition") Map<String, Object> condition);
	/**
	 * 显示所有已分配角色，不分页
	 * @param queryCondition
	 * @return
	 */
	public List<RoleVO> listUserNoPage(@Param("condition") Map<String, Object> condition);
	/**
	 * 删除所有用户角色
	 * @param userId
	 */
	public void deleteAllRole(String userId);
	
}
