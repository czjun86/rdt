package com.wireless.web.dao.commManager;

import java.util.List;
import java.util.Map;

public interface PageMapper {
	
	public Integer queryForCount(Map<String, Object> params);
	
	public List<?> queryForList(Map<String, Object> params);
	
	public void insertByBatch(Map<String, Object> param);
	
}
