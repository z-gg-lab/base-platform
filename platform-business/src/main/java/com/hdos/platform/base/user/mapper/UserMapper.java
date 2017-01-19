package com.hdos.platform.base.user.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hdos.platform.base.role.model.RoleVO;
import com.hdos.platform.base.user.model.AccountInfoVO;
import com.hdos.platform.base.user.model.AccountVO;
import com.hdos.platform.base.user.model.UserVO;
import com.hdos.platform.core.base.BaseMapper;

/**
 * 用户
 * 
 */
@Repository
public interface UserMapper extends BaseMapper<UserVO> {

	/**
	 * 查询班级学生
	 * 
	 * @param classId
	 *            班级ID
	 * @return 列表
	 */
	List<UserVO> findByClassId(String classId);
	
	/**
	 * 通过userAccount获取UserId
	 * @param userAccount
	 * @return
	 */
	String getUserIdByAccount(String userAccount);
	/**
	 * 通过userAccount获取MerchantId
	 * @param userAccount
	 * @return
	 */
	String getMerchantIdByAccount(String userAccount);
	/**
	 * 通过merchantId获取userAccount
	 * @param merchantId
	 * @return
	 */
	List<String> getAccountByMerchantId(String merchantId);
	/**
	 * 通过userId的数组，返回userId数组对应用户的所有信息
	 * @param ids
	 * @return
	 */
	List<UserVO> serchByUserId(String[] ids);
	
	/**
	 * 将userId以及roleId的链表全部插入PUB_USER_R_ROLE表中
	 * @param list
	 */
	void insertUserRole(List<UserVO> list);
	
	/**
	 * 根据roleId删除用户角色
	 * @param roleId
	 */
	void deleteUserRole(String roleId);
	
	/**
	 * 找到所有的用户
	 * @return
	 */
	List<UserVO> findAllUsers();
	
	/**
	 * 根据roleId找到该角色下的所有用户
	 * @param roleId
	 * @return
	 */
	List<UserVO> findSelectUsers(String roleId);
	
	/**
	 * 返回所有的账号信息
	 * @return
	 */
	List<AccountVO> findAllAccount();
	
	/**
	 * 判断该用户是否重复
	 * @param accountInfoVO
	 * @return
	 */
	int verifyRepeat(AccountInfoVO accountInfoVO);
	
	/**
	 * 根据UserId查找隶属部门的名称
	 * @param userId
	 * @return
	 */
	String queryDepartmentName(String userId);
	
	/**
	 * 根据UserId查找隶属部门的名称
	 * @param userId
	 * @return
	 */
	String queryMerchantName(String userId);
	
	/**
	 * 返回角色之下的用户信息
	 * @param roleId
	 * @return
	 */
	List<UserVO> listTreeOfRole(String roleId);
	/**
	 * 对链表存储的userVO返回加上部门名称信息的链表
	 * @param userVO
	 * @return
	 */
	List<UserVO> usersGetDepartmentName(List<UserVO> userVO);

	/**
	 * 将用户保存为商户用户
	 * @param userVO
	 */
	void insertAsMerchantUser(UserVO userVO);

	String getMerchantIdByUserId(String userId);

	String getDepartmentIdByUserId(String userId);

	/**
	 * 获取用户角色
	 * @param userId
	 * @return
	 */
	List<RoleVO> getUserRole(String userId);

	/**
	 * 创建用户角色关联
	 * @param userId
	 * @param string
	 */
	void createRole(String userId, String string);

	/**
	 * 校验用户编码重复性
	 * @param userCode
	 * @return
	 */
	int verifyUserCode(String userCode);

	/**
	 * 校验用户编码重复性除去自身
	 * @param userCode
	 * @param userId 
	 * @return
	 */
	int verifyUserCodeExceptSelf(String userCode, String userId);

	/**
	 * 保存用户角色信息，单个
	 * @param userVO
	 */
	void insertUserRoleSingle(String userId, String roleId);
	
}
