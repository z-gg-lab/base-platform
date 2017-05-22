/**
 * 
 */
package com.hdos.platform.base.profile.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hdos.platform.base.user.model.AccountInfoVO;
import com.hdos.platform.base.user.model.UserVO;
import com.hdos.platform.base.user.service.AccountInfoService;
import com.hdos.platform.base.user.service.UserService;
import com.hdos.platform.common.util.CacheUtils;

/**
 * 个人中心
 * 
 * @author Arthur
 */
@Controller
@RequestMapping("/profile")
public class ProfileController {

	/** 用户服务 */
	@Autowired
	private UserService userService;
	
	@Autowired
	private AccountInfoService accountInfoService;
	/**
	 * 个人中心编辑个人信息
	 * 
	 * @param userId
	 * @param model
	 * @return view
	 */
	@RequestMapping(value = "/edit")
	public String profileEdit(@RequestParam String userId, Model model) {
		AccountInfoVO accountInfoVO = accountInfoService.readAccountInfoById(userId);
		model.addAttribute("accountInfoVO", accountInfoVO);
		return "base/profile/profileEdit";
	}

	/**
	 * 保存用户
	 * @return
	 */
	@RequestMapping(value = "/save")
	@ResponseBody
	public String saveProfile(AccountInfoVO accountInfoVO, String isMerchant) {
		String res = userService.saveUserProfile(accountInfoVO,isMerchant);
		return "true";
	}
	
	/**
	 * 
	 * @return String
	 */
	@RequestMapping(value = "/unlock", method=RequestMethod.POST, produces="text/html;charset=UTF-8")
	@ResponseBody
	public String unlock(String password, String userId, String globalTimeout, HttpServletRequest request){
		String returnInfo = userService.unlock(password, userId);
		if(("success").equals(returnInfo)) {
			int globalSessionTimeout= Integer.parseInt(globalTimeout);
			request.getSession().setMaxInactiveInterval(globalSessionTimeout);
		}
		return returnInfo;
	}
}
