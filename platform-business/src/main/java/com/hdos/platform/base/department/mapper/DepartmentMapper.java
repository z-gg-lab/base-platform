package com.hdos.platform.base.department.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hdos.platform.base.department.model.DepartmentVO;
import com.hdos.platform.base.user.model.AccountInfoVO;
import com.hdos.platform.core.base.BaseMapper;

@Repository
public interface DepartmentMapper extends BaseMapper<DepartmentVO> {

	/**
	 * 根据父级id获取机构
	 * 
	 * @param parentId
	 * @return
	 */
	List<DepartmentVO> queryByParentId(String parentId);

	/**
	 * 根据父节点查询所有旗下部门信息
	 * 
	 * @param parentId
	 * @return
	 */
	List<DepartmentVO> queryAllByParentId(String parentId);

	/**
	 * 添加一个机构
	 * 
	 * @param departmentVO
	 *            机构对象
	 * @return
	 */
	void insert(DepartmentVO departmentVO);

	/**
	 * 删除机构
	 * 
	 * @param departmentId
	 * @return
	 */
	void deleteByDepartmentId(String departmentId);

	/**
	 * 验证是否有用户
	 * 
	 * @param departmentId
	 * @return
	 */
	int verifyUser(String departmentId);

	/**
	 * 修改机构信息
	 * 
	 * @param departmentVO
	 * @return
	 */
	void update(DepartmentVO departmentVO);

	/**
	 * 插入FULL_NAME
	 */

	void updateFname(DepartmentVO departmentVO);

	/**
	 * 更新子机构的FULL_NAME
	 * 
	 * @param departmentVO
	 */
	void updateFullName(DepartmentVO departmentVO);

	/**
	 * 统计其下子机构的个数
	 * 
	 * @param id
	 * @return
	 */
	int countSubDepartments(String id);

	/**
	 * 获取机构的名字以及FULLCODE
	 * 
	 * @param parentId
	 * @return
	 */
	String getDepartmentName(String parentId);

	/**
	 * 获得机构的fullCode
	 * 
	 * @param parentId
	 * @return
	 */

	String getDepartmentFullCode(String parentId);

	/**
	 * 获取部门的id
	 * fullName
	 * @return
	 */
	String getIdByFullName(String fullName);
	/**
	 * 获取部门的id
	 * departmentCode
	 * @return
	 */
	String getIdByDepartmentCode(String departmentCode);	
	/**
	 * 获取第一个部门的名字和id
	 * 
	 * @return
	 */
	String searchFirstDepartment();

	/**
	 * 获取第一个名字
	 * 
	 * @return
	 */
	String getFirstName();
	
	/**
	 * 
	 * @return
	 */
	AccountInfoVO getByMerchantId(String merchantId);
	
	List<AccountInfoVO> getMerchantByDepartmentId(String departmentId);
	
	List<AccountInfoVO> getMerchantByListDepartment(Map<String, Object> condition);

	/**
	 * 根据fullcode获取部门id
	 * @param trim
	 * @return
	 */
	String getIdByFullCode(String trim);

	/**
	 * 校验用户编码是否唯一，排除自身
	 * @param userCode
	 * @param userId
	 * @return
	 */
	int verifyDepartmentCodeExceptSelf(String departmentCode, String departmentId);

	/**
	 * 校验用户编码是否唯一
	 * @param userCode
	 */
	int verifyDepartmentCode(String departmentCode);
}
