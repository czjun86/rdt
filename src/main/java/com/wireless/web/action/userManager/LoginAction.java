package com.wireless.web.action.userManager;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.wireless.web.action.commManager.SuperAction;
import com.wireless.web.utils.WebConstants;

/**
 * 
 * 
 * @ClassName: LoginAction
 * 
 * @Description: 登录
 * 
 * @author: czj
 * 
 * @date: 2014-2-10 下午4:14:47
 */
@Controller
@RequestMapping("/login")
public class LoginAction extends SuperAction {

	@RequestMapping(value = "")
	public ModelAndView login(HttpServletRequest request,
			HttpServletResponse response, String username, String password)
			throws IOException {
		return redirect("/");
	}

	@RequestMapping(value = "logout")
	public ModelAndView logout(HttpServletRequest request,
			HttpServletResponse response)
			throws IOException {
		request.getSession().removeAttribute(WebConstants.USER_ALL_RIGHTS);
		request.getSession().removeAttribute(WebConstants.USER_OP_RIGHTS);
		request.getSession().removeAttribute(WebConstants.USER_OP_FUNCTIONS);
		request.getSession().removeAttribute(WebConstants.USER_OP_ACTIVE);		
		request.getSession().removeAttribute(WebConstants.USER_OP_PARENT_ACTIVE);
		
		Subject currentUser = SecurityUtils.getSubject();		
		if(currentUser != null){
			currentUser.logout();
		}
		return redirect("/");
	}

}
