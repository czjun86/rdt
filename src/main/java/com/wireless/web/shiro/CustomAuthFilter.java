package com.wireless.web.shiro;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wireless.web.model.Right;
import com.wireless.web.utils.WebConstants;

import net.sf.json.JSONObject;
/**
 * <p>
 * 文件名称: CustomAuthFilter.java
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
public class CustomAuthFilter extends AccessControlFilter{
	
	private static Logger logger = LoggerFactory.getLogger(CustomAuthFilter.class);	
	
	private final static String OP_MENU = "op_menu";
    
    @SuppressWarnings("unchecked")
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response,
            Object mappedValue) throws Exception {
    	
        // 是否登录
        Subject subject = getSubject(request, response);
        if(!subject.isAuthenticated()) {
            return false;
        } else {
        	HashMap<String, Object> prinMap = (HashMap<String, Object>)subject.getPrincipal();
        	String username = (String)prinMap.get("username");
        	HttpServletRequest req = (HttpServletRequest) request;
        	String contextPath = req.getContextPath();
        	int length = contextPath.length();
        	String urlAll = req.getRequestURI();
        	String url = urlAll.substring(length);
        	processMenuRequest(req,url,prinMap);
        	/*if(!subject.isPermitted(url)) {
        		return false;
        	}*/
        	
        }
       
        return true;
    }
    
   /**
    * 菜单处理
    * @param request
    * @param url
    */
   @SuppressWarnings("unchecked")
   protected void processMenuRequest(HttpServletRequest request, String url, HashMap<String, Object> prinMap){
	   String op = request.getParameter(OP_MENU);
	   if(StringUtils.isNotEmpty(op)){
		   List<Right> rightGroup = (List<Right>)prinMap.get(WebConstants.USER_ALL_RIGHTS);
		   for(Right right : rightGroup){
			   if(op.equals(right.getRightid().toString())){				   
				   request.getSession().setAttribute(WebConstants.USER_OP_ACTIVE, Integer.valueOf(op));
				   request.getSession().setAttribute(WebConstants.USER_OP_PARENT_ACTIVE, right.getParentid());
				   break;
			   }
		   }
	   }
   }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
            throws Exception {
    	 HttpServletRequest req = (HttpServletRequest) request;
         HttpServletResponse res = (HttpServletResponse) response;
         res.sendRedirect(req.getContextPath() + "/sessionInvalid" );
         /*java.io.PrintWriter out = response.getWriter();  
         out.println("<html>");  
         out.println("<script>");  
         out.println("window.parent.parent.location.href='"+req.getContextPath() + "/" + this.getLoginUrl()+"'");  
         out.println("</script>");  
         out.println("</html>"); */
         
        return false;
    }

    @Override
    public String getLoginUrl() {
        return super.getLoginUrl();
    }
}
