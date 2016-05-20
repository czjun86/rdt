package com.wireless.web.service.userManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wireless.support.PageBean;
import com.wireless.web.dao.userManager.UserMapper;
import com.wireless.web.model.User;
import com.wireless.web.service.commManager.BaseService;

@Service("userService")
public class UserService extends BaseService {

	private static Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserMapper userDao;
	
	/**
	 * 分页查询用户信息
	 * 
	 * @param params
	 *            查询参数
	 * @return
	 */
	public PageBean queryUser(Map<String, Object> params) {
		try {
			PageBean page = super.queryForList(UserMapper.class, params);
			return page;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;		
	}
	
	
	/**
	 * 根据编号查询用户信息,用于用户编辑
	 * 
	 * @param params
	 *            查询参数
	 * @return
	 */
	public User queryUserById(Map<String, Object> params) {
		User user = new User();
		try {
			user=userDao.queryUserById(params);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return user;
	}
	/**
	 * 添加用户
	 * 
	 * @param params
	 *            参数
	 * @return
	 */
	public Integer addUser(Map<String, Object> params) {
		Integer re =-1;
		try {
			re=userDao.addUser(params);
			userDao.addUserRole(params);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return re;
	}
	
	/**
	 * 修改用户
	 * 
	 * @param params
	 *            参数
	 * @return
	 */
	public Integer updUser(Map<String, Object> params) {
		Integer re =-1;
		try {
			re=userDao.updUser(params);
			userDao.updUserRole(params);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return re;
	}
	
	/**
	 * 修改用户状态
	 * 
	 * @param params
	 *            参数
	 * @return
	 */
	public Integer changeUserState(Map<String, Object> params) {
		Integer re =-1;
		try {
			re=userDao.changeUserState(params);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return re;
	}
	
	/**
	 * 检查用户名是否重复
	 * @param params
	 * @return
	 */
	public Integer checkUserName(Map<String, Object> params) {
		Integer re =0;
		try {
			re=userDao.checkUserName(params);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return re;
	}

	/**
	 * 修改密码是检查旧密码是否正确
	 * @param params
	 * @return
	 */
	public Integer checkOldPwd(Map<String, Object> params) {
		Integer re =0;
		try {
			re=userDao.checkOldPwd(params);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return re;
	}

	/**
	 * 修改用户密码
	 * @param params
	 * @return
	 */
	public Integer modfiyPassword(Map<String, Object> params) {
		Integer re =0;
		try {
			re=userDao.modfiyPassword(params);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return re;
	}
	
	public List<Map<String ,Object>> getGroups(){
		List<Map<String ,Object>> list = new ArrayList<Map<String ,Object>>();
		list = userDao.queryGroups();
		return list;
	}
	
	public Integer deleteUser(Integer userid){
		Integer re =-1;
		try {
			Map<String ,Object> param = new HashMap<String ,Object>();
			param.put("userid", userid);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			String time = df.format(new Date());
			param.put("modify_date", time);
			userDao.deleteUser(param);
			re = 1;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return re;
	}
}
