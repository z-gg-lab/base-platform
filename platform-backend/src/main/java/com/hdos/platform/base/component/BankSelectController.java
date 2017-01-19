package com.hdos.platform.base.component;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hdos.platform.base.component.mapper.BankMapper;
import com.hdos.platform.base.component.model.BankVO;
import com.hdos.platform.base.component.service.BankService;

/**
 * 银行选择控件控制器
 * @author ssj
 *
 */
@Controller
@RequestMapping("/component/bank")
public class BankSelectController {
	
	@Autowired
	private BankService bankService;

	/**
	 * 查询银行信息
	 * @return
	 */
	@RequestMapping(value = "/bankSelect")
	@ResponseBody
	public String bankSelect() {
		List<BankVO> list = bankService.getBankInfo();
		return JSONObject.toJSONString(list);
	}
}