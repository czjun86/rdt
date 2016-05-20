package com.wireless.web.shiro;

import java.util.HashMap;

import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;
/**
 * <p>
 * 文件名称: OperatorToken.java
 * </p>
 * <p>
 * 文件描述: 本类描述
 * </p>
 * <p>
 * 版权所有: 版权所有(C)2014
 * </p>
 * <p>
 * 公 司: wyl Corporation
 * </p>
 * <p>
 * 内容摘要:
 * </p>
 * <p>
 * 其他说明:
 * </p>
 * <p>
 * 完成日期：2014-2-14
 * </p>
 * <p>
 * 修改记录0：无
 * </p>
 * 
 * @version 1.0
 * @author tian.bo
 */
public class OperatorToken implements HostAuthenticationToken,
		RememberMeAuthenticationToken {

	private static final long serialVersionUID = -8726746180632735241L;
	private Integer userid;
	private String username;
	private String password;
	private String host;
	private HashMap<String, Object> principalMap;

	public OperatorToken(String username, String password, Integer userid, String host) {
		this.username = username;
		this.password = password;
		this.userid = userid;
		this.host = host;
		principalMap = new HashMap<String, Object>();
		if (username != null) {
			principalMap.put("username", username);
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public Object getPrincipal() {
		return principalMap;
	}

	public void setPrincipal(String key, Object val) {
		if (this.principalMap != null) {
			this.principalMap.put(key, val);
		} else {
			this.principalMap = new HashMap<String, Object>();
			this.principalMap.put(key, val);
		}
	}

	public Object getCredentials() {
		return this.password;
	}

	public boolean isRememberMe() {
		return false;
	}

	public String getHost() {
		return host;
	}
}
