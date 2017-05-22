package com.hdos.platform.base.component.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hdos.platform.base.component.mapper.CodeGenerateMapper;
import com.hdos.platform.base.component.model.CodeGenerateDetailVO;
import com.hdos.platform.base.component.model.CodeGenerateVO;
import com.hdos.platform.base.config.model.ConfigVO;
import com.hdos.platform.common.page.Page;
import com.hdos.platform.common.page.PageImpl;
import com.hdos.platform.common.util.PrimaryKeyUtils;
import com.hdos.platform.core.base.BaseService;

@Service
@Transactional
public class CodeGenerateService extends BaseService<CodeGenerateVO> {

	/** 生成策略：系统内共享 */
	private static final int SYSTEM_IN = 1;

	/** 生成策略：系统内按年共享 */
	private static final int SYSTEM_IN_YEAR = 4;

	/** 生成策略：系统内按月共享 */
	private static final int SYSTEM_IN_MONTH = 5;

	/** 生成策略：系统内按周共享 */
	private static final int SYSTEM_IN_WEEK = 6;

	/** 生成策略：系统内按日共享 */
	private static final int SYSTEM_IN_DAY = 7;

	/** 生成策略：机构内共享 */
	private static final int DEPARTMENT_IN = 2;

	/** 生成策略：机构内按年共享 */
	private static final int DEPARTMENT_IN_YEAR = 8;

	/** 生成策略：机构内按月共享 */
	private static final int DEPARTMENT_IN_MONTH = 9;

	/** 生成策略：机构内按周共享 */
	private static final int DEPARTMENT_IN_WEEK = 10;

	/** 生成策略：机构内按日共享 */
	private static final int DEPARTMENT_IN_DAY = 11;

	/** 生成策略：用户共享 */
	private static final int USER_IN = 3;

	/** 生成策略：用户按年共享 */
	private static final int USER_IN_YEAR = 12;

	/** 生成策略：用户按月共享 */
	private static final int USER_IN_MONTH = 13;

	/** 生成策略：用户按周共享 */
	private static final int USER_IN_WEEK = 14;

	/** 生成策略：用户按日共享 */
	private static final int USER_IN_DAY = 15;

	@Autowired
	private CodeGenerateMapper codeGenerateMapper;

	private static final Logger logger = LoggerFactory.getLogger(CodeGenerateService.class);

	/**
	 * 保存编码
	 * 
	 * @param codeGenerateVO
	 * @throws Exception
	 */
	public void saveCode(CodeGenerateVO codeGenerateVO) throws Exception {
		Map<String, Object> condition = new HashMap<String, Object>();
		// 新增编码
		if (codeGenerateVO.getId() == null || "".equals(codeGenerateVO.getId())) {
			condition.put("businessKey", codeGenerateVO.getBusinessKey());
			condition.put("businessName", codeGenerateVO.getBusinessName());
			codeGenerateVO.setId(generateKey(codeGenerateVO));
			if (codeGenerateMapper.count(condition) > 0) {
				logger.info("编码注册失败");
				throw new RuntimeException("存在同名的编码");
			}
			codeGenerateMapper.insert(codeGenerateVO);
		} else {
			// 更新
			condition.put("id", codeGenerateVO.getId());
			condition.put("businessKey", codeGenerateVO.getBusinessKey());
			condition.put("businessName", codeGenerateVO.getBusinessName());
			
			if (codeGenerateMapper.count(condition)> 1) {
				logger.info("编码注册失败");
				throw new RuntimeException("存在同名的编码");
			}
			if (codeGenerateMapper.countName(condition)> 0) {
				logger.info("编码注册失败");
				throw new RuntimeException("存在同名的业务名称 ");
			}
			
			if (codeGenerateMapper.countName(condition)> 0) {
				logger.info("编码注册失败");
				throw new RuntimeException("存在同名的业务类型");
			}
			
			codeGenerateMapper.update(codeGenerateVO);
		}

	}

	/**
	 * 批量删除编码
	 * 
	 * @param ids
	 */
	public void deleteCodes(String... ids) {
		List<CodeGenerateVO> list = codeGenerateMapper.verifyCode(ids);
		if(list.size()!=0){
			logger.info("存在使用过的编码");
			throw new RuntimeException("存在使用过的编码");
		}
		codeGenerateMapper.deleteInBulk(ids);
	}

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
	public Page<CodeGenerateVO> findPage(Map<String, Object> condition, int pageNumber, int pageSize) {
		int total = codeGenerateMapper.count(condition);
		RowBounds rowBounds = new RowBounds((pageNumber - 1) * pageSize, pageSize);
		List<CodeGenerateVO> content = total > 0 ? codeGenerateMapper.list(condition, rowBounds)
				: new ArrayList<CodeGenerateVO>(0);
		// 生成策略转换
		List<ConfigVO> list = this.getCombobox();
		for (int i = 0; i < content.size(); i++) {
			for (int j = 0; j < list.size(); j++) {
				if (String.valueOf(content.get(i).getGenerateType()).equals(list.get(j).getConfigValue())) {
					content.get(i).setType(list.get(j).getName());
				}
			}

		}
		return new PageImpl<CodeGenerateVO>(content, pageNumber, pageSize, total);
	}

	/**
	 * 根据Id获得编码
	 * 
	 * @return
	 */
	public CodeGenerateVO getById(String id) {
		return codeGenerateMapper.getById(id);
	}

	/**
	 * 构造下拉框
	 * 
	 * @return
	 */

	public List<ConfigVO> getCombobox() {

		Map<String, Integer> map = new LinkedHashMap<String, Integer>();
		map.put("系统内共享 ", SYSTEM_IN);
		map.put("系统内按年共享 ", SYSTEM_IN_YEAR);
		map.put("系统内按月共享", SYSTEM_IN_MONTH);
		map.put("系统内按周共享", SYSTEM_IN_WEEK);
		map.put("系统内按日共享", SYSTEM_IN_DAY);
		map.put("机构内共享 ", DEPARTMENT_IN);
		map.put("机构内按年共享", DEPARTMENT_IN_YEAR);
		map.put("机构内按月共享", DEPARTMENT_IN_MONTH);
		map.put("机构内按周共享", DEPARTMENT_IN_WEEK);
		map.put("机构内按日共享", DEPARTMENT_IN_DAY);
		map.put("用户共享", USER_IN);
		map.put("用户按年共享", USER_IN_YEAR);
		map.put("用户按月共享", USER_IN_MONTH);
		map.put("用户按周共享", USER_IN_WEEK);
		map.put("用户按日共享", USER_IN_DAY);

		List<ConfigVO> list = new ArrayList<ConfigVO>();

		for (Entry<String, Integer> entry : map.entrySet()) {
			ConfigVO vo = new ConfigVO();
			vo.setName(entry.getKey());
			vo.setConfigValue(String.valueOf(entry.getValue()));
			list.add(vo);
		}

		return list;

	}

	/**
	 * 获得编码
	 * 
	 * @param businessKey
	 * @return
	 */
	public List<CodeGenerateVO> getCode(String businessKey, String businessId) {

		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("businessKey", businessKey);
		// 行级锁
//		List<CodeGenerateVO> listLock = codeGenerateMapper.list(condition);
//		if (listLock.size() != 0) {
//			codeGenerateMapper.lock(listLock.get(0));
//		}
		
		List<CodeGenerateVO> list = codeGenerateMapper.list(condition);
		CodeGenerateVO codeGenerateVO = new CodeGenerateVO();
		CodeGenerateDetailVO vo = new CodeGenerateDetailVO();
		
		if (!list.isEmpty()) {
			// 更新当前序号
			codeGenerateVO.setId(list.get(0).getId());
			Calendar calendarNow = Calendar.getInstance();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(list.get(0).getLastGenerateTime()==null?new Timestamp(System.currentTimeMillis()):list.get(0).getLastGenerateTime());

			// 生成策略为按年共享，跨年当前序号变为1
			if (calendar.get(Calendar.YEAR) < calendarNow.get(Calendar.YEAR)
					&& list.get(0).getGenerateType() == SYSTEM_IN_YEAR
					|| list.get(0).getGenerateType() == DEPARTMENT_IN_YEAR
					|| list.get(0).getGenerateType() == USER_IN_YEAR) {
				vo.setNo(1);
				// 生成策略为按月共享，跨月变1
			} else if (calendar.get(Calendar.MONTH) + 1 != calendarNow.get(Calendar.MONTH) + 1
					&& list.get(0).getGenerateType() == SYSTEM_IN_MONTH
					|| list.get(0).getGenerateType() == DEPARTMENT_IN_MONTH
					|| list.get(0).getGenerateType() == USER_IN_MONTH) {
				vo.setNo(1);
				// 生成策略是按周共享，跨周当前序号变1
			} else if (calendar.get(Calendar.WEEK_OF_YEAR) < calendarNow.get(Calendar.WEEK_OF_YEAR)
					&& list.get(0).getGenerateType() == SYSTEM_IN_WEEK
					|| list.get(0).getGenerateType() == DEPARTMENT_IN_WEEK
					|| list.get(0).getGenerateType() == USER_IN_WEEK) {
				vo.setNo(1);
				// 生成策略是按日共享，跨日当前序号变1
			} else if (calendar.get(Calendar.DAY_OF_YEAR) < calendarNow.get(Calendar.DAY_OF_YEAR)
					&& list.get(0).getGenerateType() == SYSTEM_IN_DAY
					|| list.get(0).getGenerateType() == DEPARTMENT_IN_DAY
					|| list.get(0).getGenerateType() == USER_IN_DAY) {
				vo.setNo(1);
			} else {
				vo.setNo(list.get(0).getNo() + 1);
			}
			vo.setPubId(list.get(0).getId());
			vo.setId(PrimaryKeyUtils.generate(vo));
			vo.setBusinessId(businessId);
			codeGenerateVO.setNo(vo.getNo());
			codeGenerateVO.setLastGenerateTime(new Timestamp(System.currentTimeMillis()));
			vo.setLastGenerateTime(codeGenerateVO.getLastGenerateTime());
			codeGenerateMapper.updateForCode(codeGenerateVO);
			
			// 不插入明细 1 4 5 6 7
			if (list.get(0).getGenerateType() != SYSTEM_IN && list.get(0).getGenerateType() != SYSTEM_IN_YEAR
					&& list.get(0).getGenerateType() != SYSTEM_IN_MONTH
					&& list.get(0).getGenerateType() != SYSTEM_IN_WEEK
					&& list.get(0).getGenerateType() != SYSTEM_IN_DAY) {
				if (businessId == null) {
					throw new RuntimeException("businessId为空");
				} else {
					// 查询详细编码信息
					List<CodeGenerateDetailVO> listDetail = codeGenerateMapper.listDetail(businessId);
					
					if (listDetail.isEmpty()) {
						// 该businessId没有创建过编码，编码从1开始
						vo.setNo(1);
						codeGenerateMapper.insertDetail(vo);
						codeGenerateVO.setNo(vo.getNo());
						codeGenerateMapper.update(codeGenerateVO);
						return codeGenerateMapper.list(condition);
					}else {
						codeGenerateMapper.insertDetail(vo);
						// 更新主表
						codeGenerateMapper.update(codeGenerateVO);
						return codeGenerateMapper.list(condition);
					}
				}
			}
		}
		return codeGenerateMapper.list(condition);

	}

}
