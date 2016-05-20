package com.wireless.web.dao.mapManager;

import java.util.List;
import java.util.Map;

import cn.poweropt.rtCms.entity.RoadInfo;
import cn.poweropt.rtCms.entity.RoadPoint;

import com.wireless.web.model.Area;
import com.wireless.web.model.ReplenishRoadPoionts;


public interface ReplenishRoadPointsDao {
	public List<Area> getAreaNameByUserId(Map<String ,Object> map);//根据用户编号获取权限区域
	public List<ReplenishRoadPoionts> getPoint(Map<String ,String> map);//获取道路点
	public void saveMarks(Map<String ,String> map);//保存手动标记的道路点
	public RoadInfo getRoadInfoByRoadId(String roadId);//根据道路编号获取道路信息
	public List<RoadPoint> getPointsByRoadId(String roadId);//根据道路编号获取道路点集合信息
	public void removePoint(Map<String ,Object> map);//删除道路点
}
