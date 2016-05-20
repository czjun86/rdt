package com.wireless.web.dao.epinfoManager;

import java.util.List;
import java.util.Map;

import com.wireless.web.dao.commManager.PageMapper;

public interface ImportEpinfoDao extends PageMapper {

	public List<String> queryAllImei();

	public String queryArea(Map<String, Object> param);
}
