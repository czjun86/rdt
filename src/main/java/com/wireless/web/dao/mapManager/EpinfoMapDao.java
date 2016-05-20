package com.wireless.web.dao.mapManager;

import java.util.List;
import java.util.Map;

import com.wireless.web.model.Area;
import com.wireless.web.model.KpiSettings;
import com.wireless.web.model.Points;

public interface EpinfoMapDao {
	public List<Area> getAreaNameByUserId(Map<String ,Object> map);
	public List<KpiSettings> getKpi(Map<String ,Object> map);
	public List<Points> getPoint(Map<String ,Object> map);
	public List<Map<String ,Object>> getPointExcel(Map<String ,Object> map);
	public void getPro(Map<String ,Object> map);
	public List<Map<String ,Object>> getProAvgPoint(Map<String ,Object> map);
//	public List<Object> getRoadPoint();
//	public void getProRoad(Map<String ,Object> map);
}
