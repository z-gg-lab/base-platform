package com.hdos.platform.base.component.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hdos.platform.base.component.model.BankVO;
import com.hdos.platform.core.base.BaseMapper;

@Repository
public interface BankMapper extends BaseMapper<BankVO> {
	/**
	 * 查询所有银行的信息
	 * @return
	 */
	
	public List<BankVO> listBank();
	/**
	 * 通过银行名称查询bankId
	 * @return
	 */
	public String getBankIdByBankName(String bankName);
	
	public String getBankNameByBankId(String bankId);
}
