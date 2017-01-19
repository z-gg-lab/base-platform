package com.hdos.platform.base.component.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hdos.platform.base.component.mapper.BankMapper;
import com.hdos.platform.base.component.model.BankVO;


@Service
@Transactional
public class BankService {

	@Autowired
	private BankMapper bankMapper;

	/**
	 * 查询银行信息
	 * @return
	 */
	public List<BankVO> getBankInfo() {
		return bankMapper.listBank();
	}
	
	/**
	 * 通过银行名称查询bankId
	 * @return
	 */
	public String getBankIdByBankName(String bankName) {
		return bankMapper.getBankIdByBankName(bankName);
	}
	
}
