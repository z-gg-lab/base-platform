package com.hdos.platform.base.user.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.hdos.platform.base.user.model.AccountInfoVO;
import com.hdos.platform.core.base.BaseMapper;

@Repository
public interface AccountInfoMapper extends BaseMapper<AccountInfoVO>{
	/**
	 * 根据userID获取AccountInfoVO的信息
	 * @param userId
	 * @return
	 */
	AccountInfoVO readAccountInfoById(String userId);
	/**
	 * 根据隶属部门显示所有用户信息的个数
	 * @param parentIds
	 * @return
	 */
	int countByDepartmentId( String parentIds);
	/**
	 * 根据隶属部门显示所有用户信息
	 * @param parentIds
	 * @param rowBounds
	 * @return
	 */
	List<AccountInfoVO>  listByDepartmentId( String parentIds,RowBounds rowBounds );
	/**
	 * 根据部门ID获取给定树下的所有用户信息
	 * @param ids
	 * @return
	 */
	List<AccountInfoVO> listBydepartmentIdTree(String[] ids);
	
	/**
	 * 在给定角色下通过departmentIds获取所有用户信息
	 * @param ids
	 * @return
	 */
	List<AccountInfoVO> listByDepartmentId4Role(String[] ids);
	/**
	 * 获取所有基于AccountInfoVO的所有用户信息
	 * @return
	 */
	List<AccountInfoVO> findAll();
	/**
	 * 根据condition查找用户
	 * @param parentId
	 * @return
	 */
	List<AccountInfoVO> listByCondition(Map condition);
	/**
	 * 根据给定的userIds，返回用户信息。
	 * @param condition
	 * @return
	 */
	List<AccountInfoVO> findByConditionOfSelectdUserIds(Map<String, Object> condition);
	/**
	 * 更具商户ID查找用户数量
	 * @param parentIds
	 * @return
	 */
	int countByMerchantId(String parentIds);
	/**
	 * 更具商户ID查找用户信息
	 * @param parentIds
	 * @param rowBounds
	 * @return
	 */
	ArrayList<AccountInfoVO> listByMerchantId(String parentIds, RowBounds rowBounds);
	/**
	 * 根据condition查找商户用户
	 * @param parentId
	 * @return
	 */
	List<AccountInfoVO> listByConditionMerchant(Map condition);
	/**
	 * 根据给定的userIds，返回商户用户信息。
	 * @param condition
	 * @return
	 */
	List<AccountInfoVO> findByConditionOfSelectdUserIdsMerchant(Map<String, Object> condition);
	/**
	 * 根据userId获取用户信息
	 * @param userId
	 * @return
	 */
	AccountInfoVO getUserInfo(String userId);
	/**
	 * 对商户用户搜索返回查询个数
	 * @param condition
	 * @return
	 */
	int countSearchMerchant(Map<String, Object> condition);
	/**
	 * 对商户用户搜索返回用户信息
	 * @param object
	 * @param rowBounds
	 * @return
	 */
	ArrayList<AccountInfoVO> listSearchMerchant(Object object, RowBounds rowBounds);
	ArrayList<AccountInfoVO> searchUser(Map<String, Object> condition, RowBounds rowBounds);
	/**
	 * 根据userId和parentID查找用户信息
	 * @param condition
	 * @param rowBounds
	 * @return
	 */
	ArrayList<AccountInfoVO> listUser(Map<String, Object> condition, RowBounds rowBounds);
	/**
	 * 根据账号获取用户ID
	 * @param account
	 * @return
	 */
	String getUserIdByAccount(String account);
	/**
	 * 获得排序第一的账号
	 * @return
	 */
	String getAccountFirst();
	
	/**
	 * 查询部门下用户
	 * @param condition
	 * @param rowBounds
	 * @return
	 */
	ArrayList<AccountInfoVO> searchUserDepartment(@Param("condition") Map<String, Object> condition, RowBounds rowBounds);
	
	/**
	 * 查询商户下用户
	 * @param condition
	 * @param rowBounds
	 * @return
	 */
	ArrayList<AccountInfoVO> searchUserMerchant(@Param("condition") Map<String, Object> condition, RowBounds rowBounds);
	/**
	 * 根据账号查询，控件
	 * @param condition
	 * @return
	 */
	List<AccountInfoVO> findByConditionKeywordAccountDepartment(Map<String, Object> condition);
	
	/**
	 * 根据账号查询，控件
	 * @param condition
	 * @return
	 */
	List<AccountInfoVO> findByConditionKeywordAccountMerchant(Map<String, Object> condition);
	/**
	 * 根据编码查询，控件
	 * @param condition
	 * @return
	 */
	List<AccountInfoVO> findByConditionKeywordUserCodeDepartment(Map<String, Object> condition);
	/**
	 * 根据编码查询，控件
	 * @param condition
	 * @return
	 */
	List<AccountInfoVO> findByConditionKeywordUserCodeMerchant(Map<String, Object> condition);
	
}
