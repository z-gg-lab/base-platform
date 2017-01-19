package com.hdos.platform.base.role.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hdos.platform.base.role.mapper.RoleMapper;
import com.hdos.platform.base.role.model.RoleVO;
import com.hdos.platform.base.user.model.UserVO;
import com.hdos.platform.common.page.Page;
import com.hdos.platform.common.page.PageImpl;
import com.hdos.platform.common.util.PrimaryKeyUtils;
import com.hdos.platform.common.util.StringUtils;
import com.hdos.platform.core.base.BaseService;

/**
 * RoleService
 * @author Cyong
 * 
 */
@Service
@Transactional
public class RoleService extends BaseService<RoleVO> {

	@Autowired
	private RoleMapper roleMapper;

	private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

	/**
	 * 分页查询
	 * 
	 * @param condition
	 *            条件
	 * @param pageNumber
	 *            页码，从 1 开始
	 * @param pageSize
	 *            每页条数
	 * @return
	 */
	public Page<RoleVO> findPage(Map<String, Object> condition, int pageNumber, int pageSize) {
		int total = roleMapper.count(condition);
		RowBounds rowBounds = new RowBounds((pageNumber - 1) * pageSize, pageSize);
		List<RoleVO> content = total > 0 ? roleMapper.list(condition, rowBounds) : new ArrayList<RoleVO>(0);
		return new PageImpl<RoleVO>(content, pageNumber, pageSize, total);
	}

	/**
	 * 根据id获取角色
	 * @param roleId
	 * @return
	 */
	public RoleVO readRoleById(String roleId) {
		return roleMapper.getById(roleId);
	}

	/**
	 * 保存角色
	 * @param roleVO
	 */
	public boolean saveRole(RoleVO roleVO) {
		if (null == roleVO) {
			logger.error("保存菜单ERROR：" + roleVO);
			throw new IllegalArgumentException();
		}
		roleVO.setCreateTime(new Timestamp(System.currentTimeMillis()));
		roleVO.setUpdateTime(new Timestamp(System.currentTimeMillis()));

		if (StringUtils.isEmpty(roleVO.getRoleId())) {// 新增
			
			if(verifyRepeat(roleVO.getRoleName())){
				return true;
			}
			roleVO.setRoleId(generateKey(roleVO));
			roleMapper.insert(roleVO);
			return false;
		} else {// 更新
			roleMapper.update(roleVO);
			return false;
		}
	}
	
	/**
	 * 验证用户名是否重复
	 */
	public boolean verifyRepeat(String roleName){
		return roleMapper.verifyRepeat(roleName) >= 1 ? true:false;
	}
	
	/**
	 * 判断是否存在有基于此权限的用户
	 */
	public boolean verifyRight(String... roleIds){
		return roleMapper.verifyRight(roleIds) >=1 ? true : false;
	}
	
	/**
	 * 批量删除角色
	 * @param roleIds
	 */
	public boolean deleteRoles(String... roleIds) {
		if(verifyRight(roleIds)){
			return false;
		}
		roleMapper.deleteInBulk(roleIds);
		return true;
	}
	
	/**
	 * 查询该用户对应的角色名
	 * @param userAccount
	 * @return
	 */
	public List<String> queryRoleByuserAccount(String userAccount){
		
		return roleMapper.queryRoleByuserAccount(userAccount);
	}
	
	/**
	 * 查询该用户对应的操作名
	 * @param userAccount
	 * @return
	 */
	public List<String> queryOperationByuserAccount(String userAccount){
		
		return roleMapper.queryOperationByuserAccount(userAccount);
	}

	public void deleteRoleUsers(String roleId, String[] ids) {
		List<UserVO> list = new ArrayList<UserVO>();
		for (String userId : ids) {
			UserVO userVO = new UserVO();
			userVO.setUserId(userId);
			userVO.setRoleId(roleId);
			list.add(userVO);
		}
		if(list!=null&&!list.isEmpty()){
			for(UserVO userVO:list)
			roleMapper.deleteRoleUsers(userVO);
		}
	}

	/**
	 * 查找除用户对应角色外的角色
	 * @param queryCondition
	 * @param page
	 * @param rows
	 * @param userId
	 * @return
	 */
	public Page<RoleVO> findPageExcludeUser(Map<String, Object> condition, int pageNumber, int pageSize, String userId) {
		int total = roleMapper.countUserRole(userId);
		RowBounds rowBounds = new RowBounds((pageNumber - 1) * pageSize, pageSize);
		List<RoleVO> content = total > 0 ? roleMapper.listUserRole(userId, rowBounds) : new ArrayList<RoleVO>(0);
		return new PageImpl<RoleVO>(content, pageNumber, pageSize, total);
	}
	/**
	 * 显示所有角色，不分页版
	 * @return
	 */
	public List<RoleVO> listAll() {
		return roleMapper.listAll();
	}

	/**
	 * 显示所有未分配角色，不分页
	 * @param queryCondition
	 * @return
	 */
	public List<RoleVO> listExcludeUserNoPage(Map<String, Object> queryCondition) {
		return roleMapper.listExcludeUserNoPage(queryCondition);
	}
	
	/**
	 * 显示所有已分配角色，不分页
	 * @param queryCondition
	 * @return
	 */
	public List<RoleVO> listUserNoPage(Map<String, Object> queryCondition) {
		return roleMapper.listUserNoPage(queryCondition);
	}

	/**
	 * 删除所有用户角色
	 * @param userId
	 */
	public void deleteAllRole(String userId) {
		roleMapper.deleteAllRole(userId);
	}
}
