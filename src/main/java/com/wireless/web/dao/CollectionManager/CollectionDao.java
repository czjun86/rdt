package com.wireless.web.dao.CollectionManager;

import java.util.Map;

import com.wireless.web.dao.commManager.PageMapper;

public interface CollectionDao extends PageMapper {

	public void update(Map<String, Object> param);
	
	public void insert(Map<String, Object> param);

	public Integer isUsed(Integer id);
	
	public void delete(Map<String ,Object> param);

	public Integer checkName(String name);

	public Map<String ,Object> queryCollection(Integer id);

}
