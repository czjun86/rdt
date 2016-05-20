package com.wireless.web.action.userManager;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wireless.support.PageBean;
import com.wireless.uitl.MD5;
import com.wireless.web.dao.reportManager.AreaDao;
import com.wireless.web.model.Area;
import com.wireless.web.model.User;
import com.wireless.web.service.userManager.UserService;
import com.wireless.web.shiro.OperatorToken;
import com.wireless.web.utils.UserUtil;



@Controller
@RequestMapping("/operator")
public class OperateManagerAction {

	@Autowired
	private UserService userService;
	@Autowired
	protected AreaDao areaDao;
	
	private static Logger logger = LoggerFactory.getLogger(OperateManagerAction.class);
	
	@RequestMapping(value = "/opManager")
	public ModelAndView opManagerPage(HttpServletRequest request, Integer pageIndex, Integer pageSize) {
		ModelAndView mv = new ModelAndView("/userManager/operatorManager");	
		Map<String, Object> params = new HashMap<String, Object>();				
		params.put("pageSize", pageSize);
		params.put("pageIndex", pageIndex);
		PageBean page = userService.queryUser(params);		
		mv.addObject("page", page);
		return mv;
	}
	
	/**
	 * 进入用户编辑页面
	 * @param request
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/editUser")
	public ModelAndView editUser(HttpServletRequest request, Integer userId) {
		ModelAndView mv = new ModelAndView("/userManager/editUser");
		Map<String, Object> params = new HashMap<String, Object>();
		User user = new User();
		if(userId!=null){
			params.put("userId", userId);
			user = userService.queryUserById(params);	
		}else{
				user.setOperator("1");
		}
		List<Area> provinces = new ArrayList<Area>();
		List<Area> districts = new ArrayList<Area>();
		List<Area> countys = new ArrayList<Area>();
		List<Map<String ,Object>> groups = new ArrayList<Map<String ,Object>>();
		groups = userService.getGroups();
		//省
		provinces = areaDao.getLevel(1);
		//市
		//自身存在市的时候用自身存的数据查询
		if(user.getDistrict()!=null&&!"".equals(user.getDistrict())){
			if(!"-1".equals(user.getDistrict())){
				districts = areaDao.getBrother(user.getDistrict());
			}else{
				districts = areaDao.getChildren(user.getProvince());
			}
		}else{
			if(provinces.size()>0){
				districts = areaDao.getChildren(provinces.get(0).getId());
			}
		}
		
		//区/县
		if(user.getCounty()!=null&&!"".equals(user.getCounty())){
			if(!"-1".equals(user.getCounty())){
				countys = areaDao.getBrother(user.getCounty());
			}else if(!"-1".equals(user.getDistrict())){
				countys = areaDao.getChildren(user.getDistrict());
			}
		}else{
			if(districts.size()>0){
				countys = areaDao.getChildren(districts.get(0).getId());
			}
		}
		if(user.getTeleAuth()!=null){
			String teleAuth = user.getTeleAuth();
			mv.addObject("checkuni", teleAuth.substring(0,1));
			mv.addObject("checkmob", teleAuth.substring(1,2));
			mv.addObject("checktele", teleAuth.substring(2,3));
		}else{
			mv.addObject("checkuni", "1");
			mv.addObject("checkmob", "0");
			mv.addObject("checktele", "0");
		}
		mv.addObject("provinces",provinces);
		mv.addObject("districts",districts);
		mv.addObject("countys",countys);
		mv.addObject("groups",groups);
		mv.addObject("user", user);
		return mv;
	}
	
	/**
	 * 进入用户编辑页面查询select2数据
	 * @param request
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/selectData")
	public @ResponseBody Map<String ,Object> selectData(Integer userid,HttpServletRequest request) {
		Map<String, Object> params = new HashMap<String, Object>();
		User user = new User();
		if(userid!=null){
			params.put("userId", userid);
			user = userService.queryUserById(params);	
		}
		List<Area> provinces = new ArrayList<Area>();
		List<Area> districts = new ArrayList<Area>();
		List<Area> countys = new ArrayList<Area>();
		List<Map<String ,Object>> groups = new ArrayList<Map<String ,Object>>();
		groups = userService.getGroups();
		//省
		provinces = areaDao.getLevel(1);
		//市
		//自身存在市的时候用自身存的数据查询
		if(user.getDistrict()!=null&&!"".equals(user.getDistrict())){
			districts = areaDao.getBrother(user.getDistrict());
		}else{
			if(provinces.size()>0){
				districts = areaDao.getChildren(provinces.get(0).getId());
			}
		}
		
		//区/县
		if(user.getCounty()!=null&&!"".equals(user.getCounty())){
			countys = areaDao.getBrother(user.getCounty());
		}else{
			if(districts.size()>0){
				countys = areaDao.getChildren(districts.get(0).getId());
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("provinces",provinces);
		map.put("districts",districts);
		map.put("countys",countys);
		map.put("groups",groups);
		map.put("user", user);
		return map;
	}
	
	/**
	 * 修改或者新添用户
	 * @param user
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/saveUser")
	public @ResponseBody Map<String ,Integer> saveUser(@ModelAttribute User user,HttpServletRequest request) {
		Map<String, Integer> re = new HashMap<String, Integer>();
		Map<String, Object> params = new HashMap<String, Object>();
		Integer num = -1;
		params.put("userName", user.getUserName());
		if(user.getName()!=null&&!user.getName().equals("")){
			params.put("name", user.getName());
		}else{
			params.put("name", "");
		}
		if(user.getEmail()!=null&&!user.getEmail().equals("")){
			params.put("email", user.getEmail());
		}else{
			params.put("email", "");
		}
		if(user.getPhone()!=null&&!user.getPhone().equals("")){
			params.put("phone", user.getPhone());
		}else{
			params.put("phone", "");
		}
		if(user.getOperator()!=null&&!user.getOperator().equals("")){
			params.put("operator", user.getOperator());
		}
		if(user.getOperator()!=null&&!user.getOperator().equals("")){
			params.put("roleid", user.getRoleid());
		}
		//省
		if(user.getProvince()!=null&&!user.getProvince().equals("")){
			params.put("province", user.getProvince());
		}
		//市
		if(user.getDistrict()!=null&&!user.getDistrict().equals("")){
			params.put("district", user.getDistrict());
		}
		//区县
		if(user.getCounty()!=null&&!user.getCounty().equals("")){
			params.put("county", user.getCounty());
		}
		
		//区县
		if(user.getTeleAuth()!=null&&!user.getTeleAuth().equals("")){
			params.put("teleAuth", user.getTeleAuth());
		}else{
			params.put("teleAuth", "000");
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String time = df.format(new Date());
		params.put("create_date", time);
		params.put("modify_date", time);
		params.put("islock", user.getIslock());
		//判断是添加还是修改
		if(user.getUserid()!=null){
			//修改用户
			params.put("userid", user.getUserid());
			num = userService.updUser(params);
		}else{
			//添加用户
			//默认的密码为123456
			params.put("password", "E10ADC3949BA59ABBE56E057F20F883E");
			num = userService.addUser(params);
		}
		re.put("msg", num);
		return re;
	}
	
	
	/**
	 * 修改用户启用状态
	 * @param user
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/changeUserState")
	public @ResponseBody Map<String ,Integer> changeUserState(@ModelAttribute User user,HttpServletRequest request) {
		Map<String, Integer> re = new HashMap<String, Integer>();
		Map<String, Object> params = new HashMap<String, Object>();
		Integer num = -1;
		
		params.put("islock", user.getIslock());
		//判断是添加还是修改
		params.put("userId", user.getUserid());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String time = df.format(new Date());
		params.put("modify_date", time);
		num = userService.changeUserState(params);
		re.put("msg", num);
		return re;
	}
	
	
	/**
	 * 检查用户名是否重复
	 * @param request
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/checkUserName")
	public @ResponseBody String checkUserName(@ModelAttribute User user,HttpServletRequest request) {
		String re = "true";
		Map<String, Object> params = new HashMap<String, Object>();
		Integer num = -1;
		
		//判断是修改用户的情况还是新添用户的情况
		if(user.getUserid()!=null){
			params.put("userId", user.getUserid());
		}
		params.put("userName", user.getUserName());
		num = userService.checkUserName(params);
		if(num!=0){
			re="false";
		}
		return re;
	}
	
	/**
	 * 前往用户修改密码页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/toUpdPwd")
	public ModelAndView toUpdPwd(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/userManager/modfiyPassword");
		User user = new User();
		//获取当前登陆用户编号
		OperatorToken opt = UserUtil.getLoginUser(request);
		//设置用户
		user.setUserName(opt.getUsername());
		user.setUserid(opt.getUserid());
		mv.addObject("user", user);
		return mv;
	}
	
	/**
	 * 检查旧密码是否正确 (旧密码存放在password属性里)
	 * @param request
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/checkOldPwd")
	public @ResponseBody String checkOldPwd(@ModelAttribute User user,HttpServletRequest request) {
		String re = "true";
		Map<String, Object> params = new HashMap<String, Object>();
		Integer num = -1;
		
		//判断旧密码是否正确
		if(user.getUserid()!=null){
			params.put("userId", user.getUserid());
			params.put("password", toMD5(user.getPassword()));
		}
		
		num = userService.checkOldPwd(params);
		if(num!=0){
			re="false";
		}
		return re;
	}
	
	/**
	 * 修改用户密码
	 * @param request
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/modfiyPassword")
	public @ResponseBody Map<String ,Integer> modfiyPassword(@ModelAttribute User user,HttpServletRequest request) {
		Map<String ,Integer> re = new HashMap<String ,Integer>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", user.getUserid());
		params.put("password", toMD5(user.getPassword()));
		//修改密码
		Integer num = -1;
		num = userService.modfiyPassword(params);
		re.put("re", num);
		return re;
	}
	
	/**
	 * 转化字符串为MD5加密
	 */
	
	private String toMD5(String str){
		String pwd="";
		try {
			pwd = MD5.encrypt(str);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pwd;
	}
	
	
	/**
	 * 进入用户编辑页面
	 * @param request
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/provinceChange")
	public @ResponseBody Map<String ,Object> provinceChange(HttpServletRequest request
			, String id ,Integer flag) {
		Map<String ,Object> map = new HashMap<String ,Object>();
		List<Area> countys = new ArrayList<Area>();
		if(flag == 1){
			List<Area> districts = new ArrayList<Area>();
			if(id != null && !"".equals(id)){
				districts = areaDao.getChildren(id);
			}
			//县
			if(districts.size()>0){
				countys = areaDao.getChildren(districts.get(0).getId());
			}
			map.put("districts",districts);
		}else if (flag == 2){
			if(id != null && !"".equals(id)){
				countys = areaDao.getChildren(id);
			}
		}
		map.put("countys",countys);
		return map;
	}
	
	/**
	 * 逻辑删除用户
	 * @param request
	 * @param userid
	 * @return
	 */
	@RequestMapping(value = "/deleteUser")
	public @ResponseBody Map<String ,Object> deleteUser(HttpServletRequest request
			, Integer userid){
		Map<String ,Object> msg = new HashMap<String ,Object>();
		int re = userService.deleteUser(userid);
		msg.put("msg", re);
		return msg;
	}
}
