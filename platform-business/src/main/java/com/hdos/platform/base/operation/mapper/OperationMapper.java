package com.hdos.platform.base.operation.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.hdos.platform.base.menu.model.MenuVO;
import com.hdos.platform.base.operation.model.OperationVO;
import com.hdos.platform.core.base.BaseMapper;

@Repository
public interface OperationMapper extends BaseMapper<OperationVO> {

	
	List<OperationVO> listOperation(@Param("condition") Map<String, Object> condition);
	
	List<OperationVO> listOperationSelect(OperationVO operationVO);
	
	void deleteOperationRole(String roleId);
	
	void insertOperationRole(List<OperationVO> list);
}
