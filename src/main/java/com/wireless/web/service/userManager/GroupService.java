package com.wireless.web.service.userManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wireless.support.PageBean;
import com.wireless.web.dao.commManager.BaseDao;
import com.wireless.web.dao.userManager.GroupDao;
import com.wireless.web.service.commManager.BaseService;

@Service("groupService")
public class GroupService extends BaseService {

	@Autowired
	GroupDao groupDao;
	@Autowired
	BaseDao baseDao;
	
	private static Logger logger = LoggerFactory.getLogger(GroupService.class);
	
	/**
	 * 分页查询用户组信息
	 * 
	 * @param params
	 *            查询参数
	 * @return
	 */
	public PageBean queryPage(Map<String, Object> params) {
		try {
			PageBean page = super.queryForList(GroupDao.class, params);
			return page;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;		
	}
	
	/**
	 * 判断是否重名
	 */
	public int checkGroupName(String groupName){
		return groupDao.checkGroupName(groupName);
	}
	
	
	public void saveGroup(Map<String ,Object> param)throws Exception{
		groupDao.saveGroup(param);
		groupDao.saveHomeMenu(param);
	}
	
	public void deleteGroup(Map<String ,Object> param)throws Exception{
		groupDao.deleteGroup(param);
	}
	
	public List<Map<String ,Object>> queryGroups(){
		return groupDao.queryGroups();
	}
	
	public List<Map<String ,Object>> queryMyGroups(Integer roleid){
		return groupDao.queryMyGroups(roleid);
	}

	/**
	 * 保存权限设置
	 * @param roleid
	 * @param list
	 * @throws Exception
	 */
	public void saveGroupIds(String roleid, List<String> list) throws Exception{
		List<Map<String ,Object>> roleRights = new ArrayList<Map<String ,Object>>();
		Map<String ,Object> roleRight = null;
		//默认添加首页菜单
		roleRight = new HashMap<String ,Object>();
		roleRight.put("roleid", roleid);
		roleRight.put("rightid", 1);
		roleRights.add(roleRight);
		//添加选择菜单
		for(String str:list){
			roleRight = new HashMap<String ,Object>();
			roleRight.put("roleid", roleid);
			roleRight.put("rightid", str);
			roleRights.add(roleRight);
		}
		//删除已有所有关系
		groupDao.deleteRoleRight(roleid);
		//批量插入新关系
		this.baseDao.batchInsert(GroupDao.class, roleRights, 200);
	}

	public Integer hasUser(String groupid) {
		Integer msg = -1;
		try {
			Integer i = groupDao.hasUser(groupid);
			if(i>0){
				msg = 0;
			}else{
				msg = 1;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return msg;
	}
}
