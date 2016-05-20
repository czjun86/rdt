package com.wireless.web.dao.homePage;

import java.util.List;
import java.util.Map;

public interface HomeDao {
	
	Map<String ,Object> getRegion(Map<String, Object> param);
	
	/**查询采样点数据**/
	Map<String ,Object> queryPointInfo(Map<String, Object> param);
	
	/**查询覆盖数据**/
	List<Map<String ,Object>> queryCoverInfo(Map<String, Object> param);
	
	/**查询对应指标趋势**/
	List<Map<String ,Object>> queryCoverRate(Map<String, Object> param);
	
	/**查询指标占比**/
	Map<String ,Object> queryQuotaProportion(Map<String, Object> param);
}
