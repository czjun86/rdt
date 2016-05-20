package com.wireless.web.dao.reportManager;

import java.util.List;
import java.util.Map;

import com.wireless.web.dao.commManager.PageMapper;

public interface AreaKpiDao extends PageMapper {

	void areaAnalysis(Map<String, Object> params);
	
	public List<Map<String ,Object>> exportList(Map<String ,Object> param);
}
