package com.hdos.platform.base.component;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 用户选择控件控制器
 * @author ssj
 *
 */
@Controller
@RequestMapping("/component/userselect")

public class UserSelectController {
	
	/**
	 * 初始化页面
	 */
	@RequestMapping(value = "/userSelect")
	public String userSelect4Component(String userIds, String type, String isSingle, 
			String methodOfType, String methodOfUser, String isMerchant, Model model){
		model.addAttribute("userIds",userIds);
		model.addAttribute("isSingle",isSingle);
		model.addAttribute("type",type);
		model.addAttribute("methodOfType",methodOfType);
		model.addAttribute("methodOfUser",methodOfUser);
		model.addAttribute("isMerchant", isMerchant);
		return "base/component/userSelect";
	}

}
