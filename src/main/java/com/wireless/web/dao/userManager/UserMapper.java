package com.wireless.web.dao.userManager;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.wireless.web.dao.commManager.PageMapper;
import com.wireless.web.model.Right;
import com.wireless.web.model.Role;
import com.wireless.web.model.User;

public interface UserMapper extends PageMapper {
	User queryUserByUsername(@Param(value = "username") String username,
			@Param(value = "password") String password);

	void loginValidate(Map<String, Object> params);
	
	List<Right> queryRightByRoleId(Map<String, Object> params);
	
	List<Role> queryRoleByUserId(Map<String, Object> params);
	
	//根据用户编号查询用户
	User queryUserById(Map<String, Object> params);
	
	//添加用户
	Integer addUser(Map<String, Object> params);
	
	//添加对应权限
	void addUserRole(Map<String, Object> params);
		
	//修改用户
	Integer updUser(Map<String, Object> params);
	
	//添加用户对应权限
	void updUserRole(Map<String, Object> params);
	//修改用户状态
	Integer changeUserState(Map<String, Object> params);
	
	//检查用户名是否重复
	Integer checkUserName(Map<String, Object> params);
	
	//修改密码时检查旧密码是否正确
	Integer checkOldPwd(Map<String, Object> params);
	
	//修改用户密码
	Integer modfiyPassword(Map<String, Object> params);
	
	//查询用户组
	List<Map<String ,Object>> queryGroups();
	
	//物理删除用户
	void deleteUser(Map<String, Object> params);
	
	public Integer hasMenu(Integer userid);
}
