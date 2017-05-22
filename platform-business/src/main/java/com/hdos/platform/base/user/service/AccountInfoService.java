package com.hdos.platform.base.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hdos.platform.base.department.model.DepartmentVO;
import com.hdos.platform.base.department.service.DepartmentService;
import com.hdos.platform.base.user.mapper.AccountInfoMapper;
import com.hdos.platform.base.user.model.AccountInfoVO;
import com.hdos.platform.common.page.Page;
import com.hdos.platform.common.page.PageImpl;
import com.hdos.platform.common.util.StringUtils;

@Service
@Transactional
public class AccountInfoService {
	
	@Autowired
	private AccountInfoMapper accountInfoMapper;
	@Autowired
	private DepartmentService departmentService;
	
	/**
	 * 根据输入搜索用户信息
	 * @param condition
	 * @param pageNumber
	 * @param pageSize
	 * @param isMerchant
	 * @return
	 */
	public Page<AccountInfoVO> findPage(Map<String, Object> condition, int pageNumber, int pageSize, String isMerchant) {
		int total = accountInfoMapper.count(condition);
		if("false".equals(isMerchant)){
			
			RowBounds rowBounds = new RowBounds((pageNumber - 1) * pageSize, pageSize);
			List<AccountInfoVO> content = total > 0 ? accountInfoMapper.searchUserDepartment(condition, rowBounds) : new ArrayList<AccountInfoVO>(0);
			return new PageImpl<AccountInfoVO>(content, pageNumber, pageSize, total);
		}else{
			RowBounds rowBounds = new RowBounds((pageNumber - 1) * pageSize, pageSize);
			List<AccountInfoVO> content = total > 0 ? accountInfoMapper.searchUserMerchant(condition, rowBounds) : new ArrayList<AccountInfoVO>(0);
			return new PageImpl<AccountInfoVO>(content, pageNumber, pageSize, total);
		}
	}
	
	/**
	 * 显示用户信息
	 * @param parentIds
	 * @param pageNumber
	 * @param pageSize
	 * @param isMerchant
	 * @return
	 */
	public Page<AccountInfoVO> findPageByDepartmentId(String parentIds, int pageNumber, int pageSize, String isMerchant) {
		if("false".equals(isMerchant)){
			int total = accountInfoMapper.countByDepartmentId(parentIds);
			RowBounds rowBounds = new RowBounds((pageNumber - 1) * pageSize, pageSize);
			List<AccountInfoVO> content = total > 0 ? accountInfoMapper.listByDepartmentId(parentIds, rowBounds) : new ArrayList<AccountInfoVO>(0);
			return new PageImpl<AccountInfoVO>(content, pageNumber, pageSize, total);
		}else{
			int total = accountInfoMapper.countByMerchantId(parentIds);
			RowBounds rowBounds = new RowBounds((pageNumber - 1) * pageSize, pageSize);
			List<AccountInfoVO> content = total > 0 ? accountInfoMapper.listByMerchantId(parentIds, rowBounds) : new ArrayList<AccountInfoVO>(0);
			return new PageImpl<AccountInfoVO>(content, pageNumber, pageSize, total);
		}
	}
	
	/**
	 * 根据部门ID获取用户信息
	 * @param ids
	 * @return
	 */
	public List<AccountInfoVO> findByDepartmentId4Role(String[] ids) {
		
		List<AccountInfoVO> content =  accountInfoMapper.listByDepartmentId4Role(ids) ;
		return content;
	}
	
	/**
	 * 读取用户信息
	 * @param userId
	 * @return
	 */
	public AccountInfoVO readAccountInfoById(String userId){
		return accountInfoMapper.readAccountInfoById(userId);
	}

	/**
	 * 查找所用用户信息
	 * @return
	 */
	public List<AccountInfoVO> findAll() {
		return accountInfoMapper.findAll();
	}
	
	/**
	 * 根据condition，查找用户信息
	 * @param condition
	 * @param isMerchantForDepartment
	 * @return
	 */
	public List<AccountInfoVO> findByCondition(Map<String, Object> condition, String isMerchantForDepartment) {
		if("false".equals(isMerchantForDepartment)){
			List<AccountInfoVO> content =  accountInfoMapper.listByCondition(condition) ;
			return content;
		}
		if("true".equals(isMerchantForDepartment)){
			List<AccountInfoVO> content =  accountInfoMapper.listByConditionMerchant(condition) ;
			return content;
		}
		List<AccountInfoVO> list = null;
		return list;
	}

	/**
	 * 根据给定的userIds，返回用户信息。
	 * @param condition
	 * @param isMerchantForDepartment 
	 * @return
	 */
	public java.util.List<AccountInfoVO> findByConditionOfSelectdUserIds(Map<String, Object> condition, String isMerchantForDepartment) {
		if("false".equals(isMerchantForDepartment)){
			List<AccountInfoVO> content =accountInfoMapper.findByConditionOfSelectdUserIds(condition);
			return content;
		}
		if("true".equals(isMerchantForDepartment)){
			List<AccountInfoVO> content =accountInfoMapper.findByConditionOfSelectdUserIdsMerchant(condition);
			return content;
		}
		List<AccountInfoVO> content = null;
		return content;
	}

	/**
	 * 根据userId获取用户信息
	 * @param userId
	 * @return
	 */
	public AccountInfoVO getUserInfo(String userId) {
		AccountInfoVO accountInfoVO = accountInfoMapper.getUserInfo(userId);
		//当为操作员用户时，赋予部门名称
		if(!StringUtils.isEmpty(accountInfoVO.getDepartmentId())){
			DepartmentVO departmentVO = departmentService.getById(accountInfoVO.getDepartmentId());
			accountInfoVO.setDepartmentName(departmentVO.getDepartmentName());
			accountInfoVO.setDepartmentId(departmentVO.getDepartmentId());
			accountInfoVO.setDepartmentCode(departmentVO.getDepartmentCode());
		}
		//当为商户用户时，赋予商户名称和编码
		if(!StringUtils.isEmpty(accountInfoVO.getMerchantId())){
			AccountInfoVO merchantVO = departmentService.getByMerchantId(accountInfoVO.getMerchantId());
			accountInfoVO.setMerchantCode(merchantVO.getMerchantCode());
			accountInfoVO.setMerchantName(merchantVO.getMerchantName());
			accountInfoVO.setMerchantId(merchantVO.getMerchantId());
		}
		return accountInfoVO;
	}

	/**
	 * 根据账号获取用户ID
	 * @param account
	 * @return
	 */
	public String getUserIdByAccount(String account) {
		return accountInfoMapper.getUserIdByAccount(account);
	}

	/**
	 * 获得排序第一的账号
	 * @return
	 */
	public String getAccountFirst() {
		return accountInfoMapper.getAccountFirst();
	}

	/**
	 * 根据账号查询，控件
	 * @param condition
	 * @param isMerchantForDepartment
	 * @return
	 */
	public java.util.List<AccountInfoVO> findByConditionKeywordAccount(Map<String, Object> condition,
			String isMerchantForDepartment) {
		List<AccountInfoVO> content =accountInfoMapper.findByConditionKeywordAccountDepartment(condition);
		List<AccountInfoVO> contentMerchant =accountInfoMapper.findByConditionKeywordAccountMerchant(condition);
		content.addAll(contentMerchant);
		return content;
	}

	public java.util.List<AccountInfoVO> findByConditionKeywordUserCode(Map<String, Object> condition,
			String isMerchantForDepartment) {
		List<AccountInfoVO> content =accountInfoMapper.findByConditionKeywordUserCodeDepartment(condition);
		List<AccountInfoVO> contentMerchant =accountInfoMapper.findByConditionKeywordUserCodeMerchant(condition);
		content.addAll(contentMerchant);
		return content;
	}
}
