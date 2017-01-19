package com.hdos.platform.core.shiro;

import java.security.NoSuchAlgorithmException;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hdos.platform.base.user.mapper.UserTokenMapper;
import com.hdos.platform.common.crypto.SHA1;

public class ApiStatelessRealm extends AuthorizingRealm {
	@Autowired
	private UserTokenMapper userTokenMapper;

	private final Logger logger = LoggerFactory.getLogger(ApiStatelessRealm.class);
	
	public boolean supports(AuthenticationToken token) {
		// 仅支持StatelessToken类型的Token
		return token instanceof StatelessToken;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		StatelessToken statelessToken = (StatelessToken) token;
		String account = statelessToken.getUsername();
		String userToken = userTokenMapper.getByUserAccount(account).getToken();
		// String key = getKey(account);// 根据用户名获取密钥（和客户端的一样）
		// 在服务器端生成客户端参数消息摘要
		try {
			String serversignature = SHA1.genWithAmple(account, statelessToken.getJsonParams(),
					statelessToken.getTimestamp(), userToken);
			// 然后进行客户端消息摘要和服务器端消息摘要的匹配
			return new SimpleAuthenticationInfo(account, serversignature, getName());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			logger.info(e.getMessage());
			return null;
		}

	}

	private String getKey(String username) {// 得到密钥，此处硬编码一个
		if ("admin".equals(username)) {
			return "dadadswdewq2ewdwqdwadsadasd";
		}
		return null;
	}

}
