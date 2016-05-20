package com.wireless.web.dao.reportManager;

import java.util.List;
import java.util.Map;

import com.wireless.web.model.Area;
import com.wireless.web.model.Quotas;
import com.wireless.web.model.RoadInfo4ESBean;

public interface AreaDao {

	public List<Area> queryArea(String firstId);

	public List<Area> getChildren(String id);

	public List<Area> getLevel(Integer id);

	public List<Area> getBrother(String id);

	/** 查询一级区域 **/
	public List<Area> queryCounty();

	/** 查询一级区域权限含权限 **/
	public List<Area> queryCountyById(String id);

	/** 根据父ID差下一级区域 **/
	public List<Area> queryCity(String id);

	public List<Area> queryThisArea(String id);

	/** 根据父ID差下一级区域含权限 **/
	public List<Area> queryChildrenById(Map<String, Object> param);

	/** 查唯一3级区域 **/
	public List<Area> queryThird(String id);

	/** 查询相应网络指标 **/
	public List<Quotas> queryQuotas(Map<String, Object> param);

	/** 查询道路 **/
	public List<Area> getRoad(Map<String, Object> param);
	/** 查询道路 **/
	public List<Area> getAutocompleteRoad(Map<String, Object> param);
	

	/** 查询道路详情(对应区域信息等) **/
	public List<RoadInfo4ESBean> queryRoadInfo(Map<String, Object> param);

	public String getRoadName(String roadId);

}
