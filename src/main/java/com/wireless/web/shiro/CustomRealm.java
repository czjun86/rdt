package com.wireless.web.shiro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LogoutAware;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.realm.CachingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.wireless.web.dao.userManager.UserMapper;

/**
 * <p>
 * 文件名称: CustomRealm.java
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
public class CustomRealm extends CachingRealm implements LogoutAware{
	private static Logger logger = LoggerFactory.getLogger(CustomRealm.class);
	
	@Autowired
	private UserMapper userDao;

	public boolean supports(AuthenticationToken token) {
		return (token != null) && (token instanceof OperatorToken);
	}

	public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token)
	throws AuthenticationException {
		// just set token
		final OperatorToken operatorToken = (OperatorToken) token;
		logger.info("============= username:" + operatorToken.getUsername() + " login");
		return new SimpleAuthenticationInfo(operatorToken.getPrincipal(), null, getName());
	}

	public boolean isPermitted(PrincipalCollection principals, String permission) {
		HashMap<String, Object> principalMap = (HashMap<String, Object>)principals.getPrimaryPrincipal(); 
		ArrayList urls = (ArrayList)principalMap.get("urls");
		String url = permission;
		List<HashMap<String, String>> right = null;//= userDao.isRightByUrl(url);
		if(null!=right) {
			return true;
		}
		if(url.lastIndexOf('/')>0){
			url=url.substring(url.lastIndexOf('/')+1);
		}
		for (int n = 0; n < urls.size(); n++) {
			String tempurl = (String) urls.get(n);
			if (null != tempurl && tempurl.length() > 0) {
				int index = tempurl.indexOf('?');
				if(index>0){
					tempurl= tempurl.substring(0, index);
				}
				if (matchesPageUrl(url, tempurl)) {
					return true;
				} else {
					continue;
				}
			} else {
				continue;
			}
		}
		return false;
	}

	public boolean isPermitted(PrincipalCollection subjectPrincipal, Permission permission) {

		return false;
	}

	public boolean[] isPermitted(PrincipalCollection subjectPrincipal, String... permissions) {

		return null;
	}

	public boolean[] isPermitted(PrincipalCollection subjectPrincipal, List<Permission> permissions) {

		return null;
	}

	public boolean isPermittedAll(PrincipalCollection subjectPrincipal, String... permissions) {

		return false;
	}

	public boolean isPermittedAll(PrincipalCollection subjectPrincipal,
			Collection<Permission> permissions) {

		return false;
	}

	public void checkPermission(PrincipalCollection subjectPrincipal, String permission)
	throws AuthorizationException {
	}

	public void checkPermission(PrincipalCollection subjectPrincipal, Permission permission)
	throws AuthorizationException {
	}

	public void checkPermissions(PrincipalCollection subjectPrincipal, String... permissions)
	throws AuthorizationException {
	}

	public void checkPermissions(PrincipalCollection subjectPrincipal,
			Collection<Permission> permissions) throws AuthorizationException {
	}

	public boolean hasRole(PrincipalCollection subjectPrincipal, String roleIdentifier) {

		return false;
	}

	public boolean[] hasRoles(PrincipalCollection subjectPrincipal, List<String> roleIdentifiers) {
		return null;
	}

	public boolean hasAllRoles(PrincipalCollection subjectPrincipal,
			Collection<String> roleIdentifiers) {

		return false;
	}

	public void checkRole(PrincipalCollection subjectPrincipal, String roleIdentifier)
	throws AuthorizationException {

	}

	public void checkRoles(PrincipalCollection subjectPrincipal, Collection<String> roleIdentifiers)
	throws AuthorizationException {

	}

	public void checkRoles(PrincipalCollection subjectPrincipal, String... roleIdentifiers)
	throws AuthorizationException {

	}

	public void onLogout(PrincipalCollection principals) {

	}
	
	
	  /**
     * 判断表达式是否符合url
     * 
     * @param url
     *            url
     * @param exp
     *            表达式
     * @return 表达式是否符合url
     */
    private static boolean matchesPageUrl(String url, String exp) {
        if(url != null){
            String regex = exp.replaceAll(".*/", "").replaceAll("\\.", "\\\\.").replaceAll("\\*", ".*");
            return url.matches(regex);
        }else{
            return false;
        }
    }
}
