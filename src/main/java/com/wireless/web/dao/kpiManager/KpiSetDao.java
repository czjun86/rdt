package com.wireless.web.dao.kpiManager;

import java.util.List;
import java.util.Map;

import com.wireless.web.model.KpiSettings;

public interface KpiSetDao {

	/**查询id对应kpi配置**/
	public Map<String ,Object> queryKpi(Map<String ,Object> param);
	
	/**查询用户时候拥有自己的配置**/
	public Map<String ,Object> myKpi(Map<String ,Object> param);
	
	/**插入kpi配置**/
	public void insertKpi(Map<String ,Object> param);
	
	/**修改kpi配置**/
	public void updateKpi(Map<String ,Object> param);
	
	/**获取所有区间设置**/
	public List<KpiSettings> getAllKpi(Map<String ,Object> map);
}
