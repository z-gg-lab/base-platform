package com.hdos.platform.base.component.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hdos.platform.base.component.model.ExcelImportColumnVO;
import com.hdos.platform.base.component.model.ExcelImportTemplateVO;
import com.hdos.platform.base.component.model.TableVO;
import com.hdos.platform.core.base.BaseMapper;

@Repository
public interface ExcelImportTemplateMapper extends BaseMapper<ExcelImportTemplateVO> {

	/**
	 * 批量插入详细信息表
	 * 
	 * @param listDetail
	 */
	public void insertDetailBatch(List<ExcelImportColumnVO> list);

	/**
	 * 插入明细
	 * @param excelImportColumnVO
	 */
	public void insertDetail(ExcelImportColumnVO excelImportColumnVO);

	
	
	public List<TableVO> getColumnByTableName(String tableName);
	/**
	 * 获取字段名
	 * @param condition
	 * @return
	 */
	public List<TableVO> getColumnByTable(Map<String,Object> condition);

	/**
	 * 把excel的内容批量插入到对应的表
	 * 
	 * @param condition
	 */
	public void insertByExcel(Map<String, Object> condition);

	/**
	 * 获取表的主键
	 * 
	 * @param tableName
	 * @return
	 */
	public List<TableVO> getColumnByTablePRI(String tableName);

	/**
	 * 删除明细表
	 * 
	 * @param id
	 *            主键
	 * @return 成功条数
	 */
	int deleteInBulkDetail(String... ids);

	/**
	 * 查询明细表
	 * 
	 * @param id
	 * @return
	 */
	List<ExcelImportColumnVO> listDetail(String id);
	
	/**
	 * 根据模板的表名查找模板所对应的字段名
	 * @param tableName
	 * @return
	 */
	List<ExcelImportColumnVO> listFieldName(String tableName);
	
	/**
	 * 根据模板的可以值查找模板所对应的字段名
	 * @param tableName
	 * @return
	 */
	List<ExcelImportColumnVO> listFieldKey(String excelKey);
	/**
	 * 根据唯一标识找对应的对象
	 * @param key
	 * @return ExcelImportColumnVO
	 */
	ExcelImportTemplateVO getByKey(String key);
	
	/**
	 * 查询个数
	 * 
	 * @param condition
	 *            条件
	 * @return 个数
	 */
	int countKey(ExcelImportTemplateVO excelImportTemplateVO);
}
