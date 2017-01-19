package com.hdos.platform.core.shiro;

import org.apache.shiro.authc.AuthenticationToken;

public class StatelessToken implements AuthenticationToken {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
    private String jsonParams;
    private String clientDigest;
    private String timestamp;
    
   

	public StatelessToken(String username, String jsonParams, String clientDigest, String timestamp) {
		this.username = username;
		this.jsonParams = jsonParams;
		this.clientDigest = clientDigest;
		this.timestamp = timestamp;
	}

	@Override
	public Object getPrincipal() {
		return username;
	}

	@Override
	public Object getCredentials() {
		return clientDigest;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getJsonParams() {
		return jsonParams;
	}

	public void setJsonParams(String jsonParams) {
		this.jsonParams = jsonParams;
	}

	public String getClientDigest() {
		return clientDigest;
	}

	public void setClientDigest(String clientDigest) {
		this.clientDigest = clientDigest;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

}
