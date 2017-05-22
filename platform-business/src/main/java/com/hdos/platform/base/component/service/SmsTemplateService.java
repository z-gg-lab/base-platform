package com.hdos.platform.base.component.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hdos.platform.base.component.mapper.SmsTemplateMapper;
import com.hdos.platform.base.component.model.SmsTemplateVO;
import com.hdos.platform.common.page.Page;
import com.hdos.platform.common.page.PageImpl;
import com.hdos.platform.core.base.BaseService;

@Service
@Transactional
public class SmsTemplateService extends BaseService<SmsTemplateVO> {

	@Autowired
	private SmsTemplateMapper smsTemplateMapper;

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
	public Page<SmsTemplateVO> findPage(Map<String, Object> condition, int pageNumber, int pageSize) {
		int total = smsTemplateMapper.count(condition);
		RowBounds rowBounds = new RowBounds((pageNumber - 1) * pageSize, pageSize);
		List<SmsTemplateVO> content = total > 0 ? smsTemplateMapper.list(condition, rowBounds)
				: new ArrayList<SmsTemplateVO>(0);

		return new PageImpl<SmsTemplateVO>(content, pageNumber, pageSize, total);
	}

	/**
	 * 保存短信模板
	 * 
	 * @param smsTemplateVO
	 * @throws Exception
	 */
	public void saveSmsTemplate(SmsTemplateVO smsTemplateVO) throws Exception {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("templateId", smsTemplateVO.getTemplateId());
		smsTemplateVO.setCreateTime(new Timestamp(System.currentTimeMillis()));
		if (smsTemplateVO.getSmsTemplateId() == null || "".equals(smsTemplateVO.getSmsTemplateId())) {
			// 新增模板
			if (smsTemplateMapper.count(condition) > 0) {
				throw new RuntimeException("该模板已经存在");
			}
			smsTemplateVO.setSmsTemplateId(generateKey(smsTemplateVO));
			smsTemplateMapper.insert(smsTemplateVO);
		} else {
			// 修改模板
			condition.put("smsTemplateId", smsTemplateVO.getSmsTemplateId());
			if (smsTemplateMapper.count(condition) > 0) {
				throw new RuntimeException("该模板已经存在");
			}
			smsTemplateMapper.update(smsTemplateVO);
		}

	}

	/**
	 * 根据Id获得短信模板
	 * 
	 * @return
	 */
	public SmsTemplateVO getById(String id) {
		return smsTemplateMapper.getById(id);
	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 */
	public void deleteTemplate(String... ids) {
		smsTemplateMapper.deleteInBulk(ids);
	}
}
