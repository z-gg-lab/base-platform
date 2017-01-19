package com.hdos.platform.base.user.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hdos.platform.base.department.service.DepartmentService;
import com.hdos.platform.base.role.mapper.RoleMapper;
import com.hdos.platform.base.role.model.RoleVO;
import com.hdos.platform.base.role.service.RoleService;
import com.hdos.platform.base.user.mapper.AccountMapper;
import com.hdos.platform.base.user.mapper.UserMapper;
import com.hdos.platform.base.user.model.AccountInfoVO;
import com.hdos.platform.base.user.model.AccountVO;
import com.hdos.platform.base.user.model.UserVO;
import com.hdos.platform.common.page.Page;
import com.hdos.platform.common.page.PageImpl;
import com.hdos.platform.common.util.CodeGenerateUtils;
import com.hdos.platform.common.util.PrimaryKeyUtils;
import com.hdos.platform.core.base.BaseService;

/**
 * 用户服务类
 */
@Service
@Transactional
public class UserService extends BaseService<AccountInfoVO> {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private AccountMapper accountMapper;
	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private RoleService roleService;

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
	public Page<UserVO> findPage(Map<String, Object> condition, int pageNumber, int pageSize) {
		int total = userMapper.count(condition);
		RowBounds rowBounds = new RowBounds((pageNumber - 1) * pageSize, pageSize);
		List<UserVO> content = total > 0 ? userMapper.list(condition, rowBounds) : new ArrayList<UserVO>(0);
		return new PageImpl<UserVO>(content, pageNumber, pageSize, total);
	}

	public String getUserIdByAccount(String userAccount) {
		return userMapper.getUserIdByAccount(userAccount);
	}

	/**
	 * 根据userId查找用户信息
	 */
	public List<UserVO> serchByUserId(String[] ids) {
		return userMapper.serchByUserId(ids);
	}
	
	/**
	 * @param roleId
	 * @return
	 */
	public List<UserVO> findAllUsers(String roleId) {
		
		List<UserVO> listUser = userMapper.findAllUsers();
		List<UserVO> listSelect = userMapper.findSelectUsers(roleId);
		List<AccountVO> listAccount = userMapper.findAllAccount();
		
		for (int i = 0; i < listUser.size(); i++) {
			for (int j = 0; j < listAccount.size(); j++) {
				if(listUser.get(i).getUserId().equals(listAccount.get(j).getUserId())){
					listUser.get(i).setUserAccount(listAccount.get(j).getUserAccount());
					break;
				}
			}
		}
		for (int i = 0; i < listUser.size(); i++) {
			for (int j = 0; j < listSelect.size(); j++) {
				if(listUser.get(i).getUserId().equals(listSelect.get(j).getUserId())){
					listUser.get(i).setChecked(true);
					break;
				}
			}
		}
		
		return listUser;
	}
	
	/**
	 * 验证用户名是否重复
	 */
	public boolean verifyRepeat(AccountInfoVO userAccount){
		return userMapper.verifyRepeat(userAccount) >= 1 ? true :false;
	}
	
	/**
	 * 保存操作
	 * 
	 * @param accountInfoVO
	 *            用户信息
	 */
	public String saveUser(AccountInfoVO accountInfoVO, String isMerchant) {
		if(StringUtils.isEmpty(accountInfoVO.getDepartmentId()))//当为根节点的时候
		{
			accountInfoVO.setDepartmentId(departmentService.searchFirstDepartment());
		}
		if(StringUtils.isEmpty(accountInfoVO.getDepartmentId())){
			return "NoDepartment";
		}
		UserVO userVO = null;
		AccountVO accountVO = null;
		if (StringUtils.isEmpty(accountInfoVO.getUserId())) {// 新增
			if(verifyRepeat(accountInfoVO)){
				return "false";
			}
			String tmp = generateKey(accountInfoVO);
			accountInfoVO.setUserId(tmp);
			tmp = DigestUtils.md5Hex(accountInfoVO.getPwd());
			accountInfoVO.setPwd(tmp);
			userVO = AccountInfoVO.accountInfo2UserVO(accountInfoVO);
			accountVO = AccountInfoVO.accountInfoVO2AccountVO(accountInfoVO);
			//oracle兼容性修改
			userVO.setCreateTime(new Timestamp(System.currentTimeMillis()));
			userVO.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			accountVO.setCreateTime(new Timestamp(System.currentTimeMillis()));
			accountVO.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			//可手动输入用户编码2016年12月29日
			if(StringUtils.isEmpty(userVO.getUserCode())){
				userVO.setUserCode(CodeGenerateUtils.getCodeGenerator("userCode", null));
				//自动生成重复了，则增加标识
				if(!verifyUserCode(userVO.getUserCode())){
					userVO.setUserCode(userVO.getUserCode()+"_2");
				}
			}else{
				//校验用户编码是否唯一
				if(!verifyUserCode(accountInfoVO.getUserCode())){
					return "falseUserCode";
				}
			}
			if(!StringUtils.isEmpty(accountInfoVO.getUserRole())){
				String roleIds = accountInfoVO.getUserRole();
				String[] roleId = roleIds.split(",");
				int i = 0;
				while(i <roleId.length){
					userMapper.createRole(accountInfoVO.getUserId(),roleId[i]);
					i++;
				}
			}
			if("true".equals(isMerchant)){
				userVO.setMerchantId(userVO.getDepartmentId());
				userMapper.insertAsMerchantUser(userVO);
				accountMapper.insert(accountVO);
			}
			if("false".equals(isMerchant)){
				userMapper.insert(userVO);
				accountMapper.insert(accountVO);
			}
			return "true";
		} else {// 更新
			//校验用户编码是否唯一
			if(!verifyUserCodeExceptSelf(accountInfoVO.getUserCode(),accountInfoVO.getUserId())){
				return "falseUserCode";
			}
			//处理角色信息
			if(!StringUtils.isEmpty(accountInfoVO.getUserRole())){
				String userId = accountInfoVO.getUserId();
				roleService.deleteAllRole(userId);
				String roleIds = accountInfoVO.getUserRole();
				String[] roleId = roleIds.split(",");
				int i = 0;
				while(i <roleId.length){
					userMapper.createRole(accountInfoVO.getUserId(),roleId[i]);
					i++;
				}
			}else{
				String userId = accountInfoVO.getUserId();
				roleService.deleteAllRole(userId);
			}
			if(StringUtils.isEmpty(accountInfoVO.getPwd())){//当用户未修改密码时	
				userVO = AccountInfoVO.accountInfo2UserVO(accountInfoVO);
				accountVO = AccountInfoVO.accountInfoVO2AccountVO(accountInfoVO);
				userVO.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				accountVO.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				userMapper.update(userVO);
				return "true";
			}else{//用户修改了密码			
				String tmp = DigestUtils.md5Hex(accountInfoVO.getPwd());
				accountInfoVO.setPwd(tmp);
				userVO = AccountInfoVO.accountInfo2UserVO(accountInfoVO);
				accountVO = AccountInfoVO.accountInfoVO2AccountVO(accountInfoVO);
				userVO.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				accountVO.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				userMapper.update(userVO);
				accountMapper.update(accountVO);
				return "true";
			}
		}
	}

	/**
	 * 校验用户编码是否唯一，排除自身
	 * @param userCode
	 * @param userId
	 * @return
	 */
	private boolean verifyUserCodeExceptSelf(String userCode, String userId) {
		return userMapper.verifyUserCodeExceptSelf(userCode, userId) >= 1 ? false :true;
	}

	/**
	 * 校验用户编码是否唯一
	 * @param userCode
	 */
	public boolean verifyUserCode(String userCode) {
		return userMapper.verifyUserCode(userCode) >= 1 ? false :true;
	}

	/**
	 * 保存用户角色信息
	 * @param userIds
	 * @param roleId
	 */
	public void saveUserRole(String[] userIds,String roleId) {
		
		// 无论新增还是修改都先删除roleId对应的menuId
		userMapper.deleteUserRole(roleId);
		
		// 批量insert
		List<UserVO> list = new ArrayList<UserVO>();
		for (String userId : userIds) {
			UserVO userVO = new UserVO();
			userVO.setUserId(userId);
			userVO.setRoleId(roleId);
			list.add(userVO);
		}
		//这里不再使用list.size()!=0，而是使用list.isEmpty()
		if(list!=null&&!list.isEmpty()){
			userMapper.insertUserRole(list);
		}
	}
	public void saveUserRoleSecond(String[] userIds,String roleId) {
			List<UserVO> list = new ArrayList<UserVO>();
			for (String userId : userIds) {
				UserVO userVO = new UserVO();
				userVO.setUserId(userId);
				userVO.setRoleId(roleId);
				list.add(userVO);
			}
			//里不再使用list.size()!=0，而是使用list.isEmpty()
			if(list!=null&&!list.isEmpty()){
				for(UserVO userVO : list){
					userMapper.insertUserRoleSingle(userVO.getUserId(),userVO.getRoleId());
				}
			}
		}
	
	/**
	 * 删除操作
	 * 
	 */
	public void deleteUsers(String... userIds) {

		userMapper.deleteInBulk(userIds);
		roleMapper.deleteUserRole(userIds);
		accountMapper.deleteInBulk(userIds);
	}

	/**
	 * 根据主键级联删除数据
	 * 
	 * @param userId
	 *            主键
	 */
	public void deleteById(String userId) {
		userMapper.delete(userId);
	}

	/**
	 * 根据主键获取实体
	 * 
	 * @param userId
	 *            主键
	 * @return 实体
	 */
	public UserVO findById(String userId) {
		UserVO entity = userMapper.getById(userId);
		return entity;
	}

	/**
	 * 查询列表
	 * 
	 * @param condition
	 *            条件
	 * @param cascade
	 *            是否级联获取用户信息
	 * @return 列表
	 */
	public List<UserVO> findByClassId(String classId) {
		return userMapper.findByClassId(classId);
	}
	
	/**
	 * 保存操作
	 * 
	 * @param vo
	 *            对象
	 * @return 主键
	 */
	public String save(UserVO vo) {
		String tmp = DigestUtils.md5Hex(vo.getPwd());
		vo.setPwd(tmp);
		AccountVO accountVO=UserVO.userVO2AccountVO(vo);
		// 处理主表
		if (StringUtils.isNotBlank(vo.getUserId())) {
			userMapper.update(vo);
			accountMapper.update(accountVO);
		} else {
			// 新增
			vo.setUserId(PrimaryKeyUtils.generate(vo));
			userMapper.insert(vo);
		}
		return vo.getUserId();
	}

	/**
	 * 查询用户所示机构
	 * @param userId
	 * @return String
	 */
	public String queryDepartmentName(String userId) {
		String tmp = null;
		UserVO userVO = userMapper.getById(userId);
		if(userVO != null){
			if(userVO.getMerchantId() != null && !"".equals(userVO.getMerchantId())){
				tmp = userMapper.queryMerchantName(userId);
			}
			if(userVO.getDepartmentId() != null && !"".equals(userVO.getDepartmentId())){
				tmp = userMapper.queryDepartmentName(userId);
			}
		}
		return tmp ;
	}

	/**
	 * 查询相对于所选角色的所有被分配的用户
	 * @return
	 */
	public List<UserVO> listTreeOfRole(String roleId) {
		List<UserVO> userVO = userMapper.findSelectUsers(roleId);
		//userVO中没有departmentName的信息，因此根据调用函数，为userVO得到的userIds补全departmentName;
		return userVO;
	}

	/**
	 * 获取用户角色
	 * @param userId
	 * @return
	 */
	public List<RoleVO> userHasRole(String userId) {
		return userMapper.getUserRole(userId);
	}

	public String saveUserProfile(AccountInfoVO accountInfoVO, String isMerchant) {		
		UserVO userVO = null;
		AccountVO accountVO = null;		
		if(StringUtils.isEmpty(accountInfoVO.getPwd())){//当用户未修改密码时	
			userVO = AccountInfoVO.accountInfo2UserVO(accountInfoVO);
			accountVO = AccountInfoVO.accountInfoVO2AccountVO(accountInfoVO);
			userVO.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			accountVO.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			userMapper.update(userVO);
			return "true";
		}else{//用户修改了密码			
			String tmp = DigestUtils.md5Hex(accountInfoVO.getPwd());
			accountInfoVO.setPwd(tmp);
			userVO = AccountInfoVO.accountInfo2UserVO(accountInfoVO);
			accountVO = AccountInfoVO.accountInfoVO2AccountVO(accountInfoVO);
			userVO.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			accountVO.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			userMapper.update(userVO);
			accountMapper.update(accountVO);
			return "true";
		}
	}
}
