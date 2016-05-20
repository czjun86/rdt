package com.wireless.web.dao.VersionManager;

import java.util.Map;

import com.wireless.web.dao.commManager.PageMapper;

public interface VersionDao extends PageMapper{

	int hasUsed(Integer id);

	void delete(Integer id);

	Integer checkName(String name);

	Map<String, Object> queryVersion(Integer id);

	void saveInfo(Map<String, Object> map);

}
