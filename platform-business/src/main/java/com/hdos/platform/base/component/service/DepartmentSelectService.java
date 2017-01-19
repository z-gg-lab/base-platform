package com.hdos.platform.base.component.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hdos.platform.base.department.mapper.DepartmentMapper;
import com.hdos.platform.base.department.model.DepartmentVO;
import com.hdos.platform.base.user.model.AccountInfoVO;
import com.hdos.platform.common.vo.TreeVO;

@Service
public class DepartmentSelectService {

	@Autowired
	private DepartmentMapper departmentMapper;

	/**
	 * 根据条件组装treeVO
	 * 
	 * @param condition
	 *            条件
	 * @return 列表
	 */
	public List<TreeVO> find(Map<String, Object> condition) {

		// 查询机构
		List<DepartmentVO> listDepartment = departmentMapper.list(condition);

		List<AccountInfoVO> listMerchant = new ArrayList<AccountInfoVO>();

		List<AccountInfoVO> listMerByDepartmentId = new ArrayList<AccountInfoVO>();

		// 是否显示商户
		if (condition.get("flag") != null && condition.get("flag").equals("true")) {
			// 根据机构id查询对应的商户
			// listMerByDepartmentId = merchantMapper.getByDepartmentId((String)
			// condition.get("parentId"));
			listMerByDepartmentId = departmentMapper.getMerchantByDepartmentId((String) condition.get("parentId"));
			if (listDepartment != null && listDepartment.size() != 0) {
				condition.put("listDepartment", listDepartment);
				listMerchant = departmentMapper.getMerchantByListDepartment(condition);
			}
			// 判断机构下是否有商户
			if (listMerchant != null && listMerchant.size() != 0) {
				for (int i = 0; i < listDepartment.size(); i++) {
					for (int j = 0; j < listMerchant.size(); j++) {
						if (listDepartment.get(i).getDepartmentId().equals(listMerchant.get(j).getDepartmentId())) {
							listDepartment.get(i).setHasMerchant(true);
							continue;
						}
					}
				}
			}
		}

		// 控制树的闭合状态
		for (DepartmentVO departmentVO : listDepartment) {

			if (departmentMapper.countSubDepartments(departmentVO.getDepartmentId()) == 0) {
				departmentVO.setLeafMark(0);
			} else {
				departmentVO.setLeafMark(1);
			}
		}

		return listToTreeList(listDepartment, listMerByDepartmentId);
	}

	/**
	 * departmentVO转treeVO
	 * 
	 * @param menuVO
	 * @return
	 */
	private TreeVO departmentVOToTreeVO(DepartmentVO departmentVO) {

		TreeVO treeVO = new TreeVO();
		treeVO.setId(departmentVO.getDepartmentId());
		treeVO.setText(departmentVO.getDepartmentName());

		if (departmentVO.getLeafMark() == 1 || departmentVO.getHasMerchant()) {
			treeVO.setState(TreeVO.STATE_CLOSED);
		} else {
			treeVO.setState(TreeVO.STATE_OPEN);
		}
		return treeVO;
	}

	/**
	 * departmentList转treeList
	 * 
	 * @param treeList
	 * @return
	 */
	private List<TreeVO> listToTreeList(List<DepartmentVO> listDepartment, List<AccountInfoVO> listMerByDepartmentId) {

		List<TreeVO> treeList = new ArrayList<TreeVO>();
		for (DepartmentVO departmentVO : listDepartment) {
			TreeVO treeVO = departmentVOToTreeVO(departmentVO);
			treeList.add(treeVO);
		}

		if (listMerByDepartmentId.size() > 0 && listMerByDepartmentId != null) {
			for (AccountInfoVO merchantVO : listMerByDepartmentId) {
				TreeVO treeVO = merchantVOToTreeVO(merchantVO);
				treeList.add(treeVO);
			}
		}

		return treeList;
	}

	/**
	 * merchantVO转treeVO
	 * 
	 * @param menuVO
	 * @return
	 */
	private TreeVO merchantVOToTreeVO(AccountInfoVO merchantVO) {

		TreeVO treeVO = new TreeVO();
		treeVO.setId(merchantVO.getMerchantId());
		treeVO.setText(merchantVO.getMerchantName());
		treeVO.setState(TreeVO.STATE_OPEN);
		JSONObject attributes = new JSONObject();
		treeVO.setAttributes(attributes);
		return treeVO;
	}

}
