package com.wireless.web.dao.reportManager;

import java.util.List;
import java.util.Map;

import com.wireless.web.dao.commManager.PageMapper;

public interface ComprehensiveDao extends PageMapper {

	void kpiAnalysis(Map<String, Object> params);
	
	public Map<String ,Object> queryUserInterval(Map<String ,Object> param);
	
	/** 报表导出 **/
	public List<Map<String ,Object>> exportList(Map<String ,Object> param);
}
