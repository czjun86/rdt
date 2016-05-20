package com.wireless.web.dao.reportManager;

import java.util.Map;

import com.wireless.web.dao.commManager.PageMapper;

public interface TaskReportDao extends PageMapper {
	public void addTask(Map<String,Object> params);
}
