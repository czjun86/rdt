package com.wireless.web.service.userManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wireless.web.dao.userManager.UserMapper;
import com.wireless.web.model.Right;
import com.wireless.web.model.Role;
import com.wireless.web.model.User;
import com.wireless.web.shiro.OperatorToken;
import com.wireless.web.utils.WebConstants;

@Service("operatorService")
public class OperatorService {
	
	
	@Autowired
	private UserMapper userDao;
	
	/**
	 * 初始化操作员权限
	 * @param user
	 * @param aot
	 */
	public void initOperRight(User user, OperatorToken aot) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("userid", user.getUserid());
		List<Role> roleGroup = userDao.queryRoleByUserId(params);
		if(roleGroup == null || roleGroup.isEmpty()){
			return;
		}
		List<HashMap<String, Object>> roles = new ArrayList<HashMap<String,Object>>();
		for(Role r : roleGroup){
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("roleid", r.getRoleid());
			roles.add(item);
		}
		params.clear();
		params.put("size", roles.size());
		params.put("roleList", roles);
		List<Right> rightGroup = userDao.queryRightByRoleId(params);	
		aot.setPrincipal(WebConstants.USER_OP_RIGHTS, rightWrap(rightGroup));
		aot.setPrincipal(WebConstants.USER_OP_FUNCTIONS, functionWrap(rightGroup));
		aot.setPrincipal(WebConstants.USER_ALL_RIGHTS, rightGroup);
	}
		
	/**
	 * 功能权限包装
	 * @param rightGroup
	 * @return 
	 */
	private Map<String, String> functionWrap(List<Right> rightGroup) {
		
		Map<String, String> funMap = new HashMap<String, String>();
		
		for(Right r : rightGroup){
			if(r.getType() == 1){
				funMap.put(r.getModules(), r.getOperate());
			}
		}
		
		return funMap;
	}



	/**
	 * 菜单权限包装
	 * @param rightList
	 * @return
	 */
	private List<Right> rightWrap(List<Right> rightList){
		List<Right> rightGroup = new LinkedList<Right>();
		for(Right r : rightList){
			if(r.getParentid() == 0 && r.getType() == 0){
				rightGroup.add(r);
				subRightWrap(r,rightList);
			}else{
				continue;
			}
		}		
		return rightGroup;
	}
	
	/**
	 * 子菜单权限包装
	 * @param right
	 * @param rightList
	 */
	private void subRightWrap(Right right, List<Right> rightList){
		Set<Right> subRight = right.getSubRight();
		if(subRight == null){
			subRight = new LinkedHashSet<Right>();
		}
		for(Right r : rightList){
			if(r.getParentid() == right.getRightid() && r.getType() == 0){
				subRight.add(r);
			}
		}
		right.setSubRight(subRight);
	}

	/**
	 * 初始化操作员详细信息
	 * @param user
	 * @param aot
	 */
	public void initOperInfoDetail(User user, OperatorToken aot) {

		
	}
	

}
