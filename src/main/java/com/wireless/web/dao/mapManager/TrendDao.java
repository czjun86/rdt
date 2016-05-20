package com.wireless.web.dao.mapManager;

import java.util.List;
import java.util.Map;

import com.wireless.web.model.VoBean;

public interface TrendDao {

	List<Map<String ,Object>> queryTrend(VoBean vo);
}
