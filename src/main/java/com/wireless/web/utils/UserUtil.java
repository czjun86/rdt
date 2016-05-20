/**
 * 用户工具类
 */
package com.wireless.web.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.wireless.web.shiro.OperatorToken;

/**
 * 用户工具类
 * @return shiro类OperatorToken
 * @author tc
 *
 */
public class UserUtil {
	public static OperatorToken getLoginUser(HttpServletRequest request){
		HttpSession session =  request.getSession();
		OperatorToken operatorToken = (OperatorToken) session.getAttribute(WebConstants.USERINFO);
		return operatorToken;
	}
}
