package com.wireless.web.dao.userManager;

import java.util.List;
import java.util.Map;

import com.wireless.web.dao.commManager.PageMapper;

public interface GroupDao extends PageMapper {
	/**
	 * 判断是否重名
	 */
	Integer checkGroupName(String groupName);
	
	/**
	 * 保存新增组
	 */
	public void saveGroup(Map<String ,Object> param);
	
	/**
	 * 默认保存首页菜单
	 * @param param
	 */
	public void saveHomeMenu(Map<String ,Object> param);
	
	
	/**
	 * 逻辑删除
	 * @param param
	 */
	public void deleteGroup(Map<String ,Object> param);
	
	public List<Map<String ,Object>> queryGroups();
	
	public List<Map<String ,Object>> queryMyGroups(Integer roleid);
	
	public void deleteRoleRight(String roleid);
	
	public void insertByBatch(Map<String ,Object> param);

	public Integer hasUser(String roleid);

}
