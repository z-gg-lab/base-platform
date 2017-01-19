package com.hdos.platform.base.operation.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.hdos.platform.base.operation.mapper.OperationMapper;
import com.hdos.platform.base.operation.model.OperationVO;
import com.hdos.platform.common.page.Page;
import com.hdos.platform.common.page.PageImpl;
import com.hdos.platform.common.util.StringUtils;
import com.hdos.platform.common.vo.TreeVO;
import com.hdos.platform.core.base.BaseService;

/**
 * OperationService
 * @author regis
 *
 */
@Service
@Transactional
public class OperationService extends BaseService<OperationVO> {
	
	@Autowired
	private OperationMapper operationMapper;
	
	private static final Logger logger = LoggerFactory.getLogger(OperationService.class);
	
	/**
	 * 根据菜单id查询操作
	 * @param menuId
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<OperationVO> findOperationByMenuId(String menuId,int pageNumber, int pageSize) {
		
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("menuId", menuId);
		int total = operationMapper.count(params);
		RowBounds rowBounds = new RowBounds((pageNumber - 1) * pageSize, pageSize);
		List<OperationVO> rstList = operationMapper.list(params, rowBounds);
		return new PageImpl<OperationVO>(rstList, pageNumber, pageSize, total);
	}
	
	/**
	 * 保存功能
	 * @param operationVO
	 */
	public int saveOperation(OperationVO operationVO) {
		if (null == operationVO) {
			logger.error("保存功能ERROR：" + operationVO);
			throw new IllegalArgumentException();
		}

		operationVO.setCreateTime(new Timestamp(System.currentTimeMillis()));
		operationVO.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		
		if (StringUtils.isEmpty(operationVO.getOperationId())) {// 新增
			operationVO.setOperationId(generateKey(operationVO));
			operationMapper.insert(operationVO);
			return EDIT_TYPE_NEW;
		} else {// 更新
			operationMapper.update(operationVO);
			return EDIT_TYPE_UPDATE;
		}
	}
	
	/**
	 * 根据id获取功能
	 * @param operationId
	 * @return
	 */
	public OperationVO readOperationById(String operationId) {
		return operationMapper.getById(operationId);
	}
	
	/**
	 * 批量删除功能
	 * @param operationIds
	 */
	public void deleteRoles(String... operationIds) {
		
		operationMapper.deleteInBulk(operationIds);
	}

	
	/**
	 * 根据父节点id获取菜单
	 * @param pMenuId
	 * @return
	 */
	public List<TreeVO> queryTreeByMenuId(String menuId,String roleId) {
		
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("menuId", menuId);
		List<OperationVO> operationList = operationMapper.list(condition);
		
		OperationVO operationVO = new OperationVO();
		operationVO.setRoleId(roleId);
		
		List<OperationVO> operationListSelect = operationMapper.listOperationSelect(operationVO);
		
		
		for (int i = 0; i < operationList.size(); i++) {
			for (int j = 0; j < operationListSelect.size(); j++) {
				if(operationList.get(i).getOperationId().equals(operationListSelect.get(j).getOperationId())){
					operationList.get(i).setChecked(true);
					break;
				}
			}
		}
		
		return operationListToTreeList(operationList); 
	}
	
	/**
	 * menuList转treeList
	 * 
	 * @param treeList
	 * @return
	 */
	private List<TreeVO> operationListToTreeList(List<OperationVO> operationList) {

		List<TreeVO> treeList = new ArrayList<TreeVO>();
		for (OperationVO operationVO : operationList) {
			TreeVO treeVO = operationVOToTreeVO(operationVO);
			treeList.add(treeVO);
		}
		return treeList;
	}
	
	/**
	 * menuVO转treeVO
	 * 
	 * @param menuVO
	 * @return
	 */
	private TreeVO operationVOToTreeVO(OperationVO operationVO) {

		TreeVO treeVO = new TreeVO();
		treeVO.setId(operationVO.getOperationId());
		treeVO.setText(operationVO.getOperationName());
		treeVO.setState(TreeVO.STATE_OPEN);
		JSONObject attributes = new JSONObject();
//		attributes.put("flag",true);
		if(operationVO.getChecked()){
			treeVO.setChecked(operationVO.getChecked());
		}
		treeVO.setAttributes(attributes);
		return treeVO;
	}
	
	/**
	 * 保存菜单角色关系
	 * @param menuVO
	 */
	public void saveOperation_Role(String[] operationIds,String roleId) {
		
		// 无论新增还是修改都先删除roleId对应的menuId
		operationMapper.deleteOperationRole(roleId);
		
		List<OperationVO> list = new ArrayList<OperationVO>();
		for (String operationId : operationIds) {
			OperationVO operationVO = new OperationVO();
			operationVO.setOperationId(operationId);
			operationVO.setRoleId(roleId);
			list.add(operationVO);
		}
		
		if(list!=null&&list.size()!=0){
			operationMapper.insertOperationRole(list);
		}
		
		
		
	}
	
	
	
}
