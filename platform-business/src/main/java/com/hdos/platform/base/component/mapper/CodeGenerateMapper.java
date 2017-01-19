package com.hdos.platform.base.component.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.hdos.platform.base.component.model.CodeGenerateDetailVO;
import com.hdos.platform.base.component.model.CodeGenerateVO;
import com.hdos.platform.core.base.BaseMapper;

@Repository
public interface CodeGenerateMapper extends BaseMapper<CodeGenerateVO> {
	/**
	 * 使用行级锁
	 * @param codeGenerateVO
	 * @return
	 */
	public CodeGenerateVO lock(CodeGenerateVO codeGenerateVO);
	
	/**
	 * 插入明细表
	 * @param codeGenerateDetailVO
	 */
	public void insertDetail(CodeGenerateDetailVO codeGenerateDetailVO);
	
	/**
	 * 查询明细编码
	 * @param busniessId
	 * @return
	 */
	List<CodeGenerateDetailVO> listDetail(String busniessId);
	
	/**
	 * 统计busniessName个数
	 * @param condition
	 * @return
	 */
	int countName(@Param("condition") Map<String, Object> condition);
	
	/**
	 * 统计busniessKey个数
	 * @param condition
	 * @return
	 */
	int countKey(@Param("condition") Map<String, Object> condition);
	
	/**
	 * 验证编码是否被使用过
	 * @param ids
	 * @return
	 */
	List<CodeGenerateVO> verifyCode(String... ids);
	
	/**
	 * 更新
	 * 
	 * @param codeGenerateVO
	 *            对象
	 */
	void updateForCode(CodeGenerateVO codeGenerateVO);
	
}
