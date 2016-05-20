package com.wireless.web.dwr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wireless.web.dao.userManager.UserMapper;
import com.wireless.web.model.Right;
import com.wireless.web.model.User;
import com.wireless.web.service.userManager.OperatorService;
import com.wireless.web.shiro.OperatorToken;
import com.wireless.web.utils.WebConstants;

@Component
@RemoteProxy(name = "loginDwr")
public class LoginDwr {

	private static Logger logger = LoggerFactory.getLogger(LoginDwr.class);

	@Autowired
	private UserMapper userDao;
	
	@Autowired
	private OperatorService operatorService;

	/**
	 * 登录验证
	 * @param username
	 *         用户名
	 * @param pwd
	 *         密码
	 * @param code
	 *         验证码,暂时没用
	 * @return
	 */
	public HashMap<String, String> validate(String username, String pwd,String verifyCode,
			String code ,HttpServletRequest request) {
		//获取用户信息
		HttpSession session = request.getSession();
		String verify = (String) session.getAttribute(WebConstants.VERIFYCODE);
		HashMap<String, String> rtnMap = new HashMap<String, String>();
		if(verify.toUpperCase().equals(verifyCode.toUpperCase())){
			String encryptpassword = null;
			try {
				encryptpassword = com.wireless.uitl.MD5.encrypt(pwd).toUpperCase();
			} catch (Exception e) {
				encryptpassword = "";
				logger.error(e.toString());
				throw new RuntimeException(e);
			}
	
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("i_username", username);
			params.put("i_password", encryptpassword);
	
			int o_result=-1;
			int o_userid=-1;
			String message = "";
			try {
				//数据库登录验证
				userDao.loginValidate(params);
				o_result = NumberUtils.toInt(params.get("o_result").toString());
				if(params.get("o_userid")!=null){//出现登陆验证不通过时，存储过程没有给o_userid赋值
					o_userid = NumberUtils.toInt(params.get("o_userid").toString());
				}
				
				if (o_result == WebConstants.LONIN_STATUS_SUCCESS) {
					if(userDao.hasMenu(o_userid)>0){//判断是否有菜单
						OperatorToken aot = new OperatorToken(username,
								encryptpassword, o_userid, null);
						User user = new User();
						user.setUserid(o_userid);
						operatorService.initOperInfoDetail(user,aot);
						operatorService.initOperRight(user, aot);
						Subject currentUser = SecurityUtils.getSubject();				
						currentUser.login(aot); // 登录验证还是由应用本身代码完成,shiro只是提供principl的保持
						setSession(aot,rtnMap);
						logger.info("login success!!");
					}else{
						message = "对应用户组下没有任何菜单";
						o_result = WebConstants.LONIN_STATUS_NOMENU;
					}
				} else if (o_result == WebConstants.LONIN_STATUS_NOBODY) { // 无效的账号
					message = "用户不存在";
				} else if (o_result == WebConstants.LONIN_STATUS_PWDERROR) { // 密码错误
					message = "密码错误";
				} else if (o_result == WebConstants.LONIN_STATUS_LOCKED) { // 用户被锁定
					message = "用户被锁定";
				} else if (o_result == WebConstants.LONIN_STATUS_DBFAILD) { // 数据库操作失败
					message = "数据库查询失败";
				}
	
			} catch (Exception ex) {
				logger.error(ex.toString());			
				o_result = WebConstants.LONIN_STATUS_DBFAILD;// 数据库异常
				message = "数据库查询失败";
			}
			rtnMap.put("code", String.valueOf(o_result));
			rtnMap.put("message", message);
		}else{
			rtnMap.put("code", String.valueOf(WebConstants.LONIN_STATUS_CODEERROR));
			rtnMap.put("message", "验证码错误");
		}
		return rtnMap;
	}
	
	/**
	 * set session
	 * @param aot
	 *         token
	 * @param rtnMap 
	 */
	@SuppressWarnings("unchecked")
	public void setSession(OperatorToken aot, HashMap<String,String> rtnMap){
		HashMap<String, Object> principalMap = (HashMap<String, Object>) aot.getPrincipal();
		HttpSession session = WebContextFactory.get().getSession();
		session.setAttribute(WebConstants.USER_OP_RIGHTS, principalMap.get(WebConstants.USER_OP_RIGHTS)); //菜单权限
		session.setAttribute(WebConstants.USER_OP_FUNCTIONS, principalMap.get(WebConstants.USER_OP_FUNCTIONS));	//功能权限	
		List<Right> rightGroup = (List<Right>)principalMap.get(WebConstants.USER_ALL_RIGHTS);//所有权限
		session.setAttribute(WebConstants.USER_OP_ACTIVE, -9999);//当前选中
		session.setAttribute(WebConstants.USER_OP_PARENT_ACTIVE, -9999);//父级选中
		session.setAttribute(WebConstants.USERINFO, aot);//父级选中
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", aot.getUserid());
		session.setAttribute(WebConstants.USER, userDao.queryUserById(params));//user信息
		for(Right right : rightGroup){
			if(right.getShowtype() == 1){
				session.setAttribute(WebConstants.USER_OP_ACTIVE, right.getRightid());
				aot.setPrincipal(WebConstants.USER_HOME_PAGE, right.getUrl());
				rtnMap.put(WebConstants.USER_HOME_PAGE, right.getUrl());
				break;
			}
		}		
	}	

}
