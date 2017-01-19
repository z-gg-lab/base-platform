/**
 * Copyright (c) 2005-2012 https://github.com/zhangkaitao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.hdos.platform.core.shiro;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.hdos.platform.base.role.service.RoleService;
import com.hdos.platform.base.user.model.AccountVO;
import com.hdos.platform.base.user.service.AccountService;

/**
 * UserRealm
 * 
 * @author Arthur
 * @version 1.0
 * @since 2016-5-10
 */
public class UserRealm extends AuthorizingRealm {

	@Autowired
	AccountService accountService;

	@Autowired
	RoleService roleService;
	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//		String username = (String) principals.fromRealm(getName()).iterator().next();

		UserProfile userProfile =  (UserProfile) principals.fromRealm(getName()).iterator().next();
		
		
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		// TODO: 查询当前用户的 role 和 permission 串，添加到授权对象中
		List<String> roles = roleService.queryRoleByuserAccount(userProfile.getAccount());
		List<String> permissions = roleService.queryOperationByuserAccount(userProfile.getAccount());
		info.addRoles(roles);
		info.addStringPermissions(permissions);

		// TODO: 使用 SecurityUtils.getSubject() 相关方法验证权限
		// SecurityUtils.getSubject().hasRole("xxx");
		// SecurityUtils.getSubject().isPermitted("xxx");

		return info;

	}

	/**
	 * 认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {

		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		String username = token.getUsername();
		String password = DigestUtils.md5Hex(String.valueOf(token.getPassword()));
		AccountVO accountVO = accountService.queryAccountByAccountAndPwd(username, password);

		if (null != accountVO) {
			// TODO: 这里应该获取用户对象，丰富 UserProfile 内容
			UserProfile profile = new UserProfile(accountVO.getUserId(), accountVO.getUserAccount(), accountVO.getUserAccount());
			return new SimpleAuthenticationInfo(profile, password, getName());
		}

		return null;
	}
}
