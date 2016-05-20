package com.wireless.web.service.reportManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wireless.support.PageBean;
import com.wireless.web.dao.kpiManager.KpiSetDao;
import com.wireless.web.dao.reportManager.AreaDao;
import com.wireless.web.dao.reportManager.ComprehensiveDao;
import com.wireless.web.model.KpiSettings;
import com.wireless.web.model.QueryBean;
import com.wireless.web.model.RoadInfo4ESBean;
import com.wireless.web.model.User;
import com.wireless.web.service.commManager.BaseService;
import com.wireless.web.utils.ExportUtils;
import com.wireless.web.utils.StringUtils;
import com.wireless.web.utils.TimeUtils;

@Service("eaService")
public class EaService extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(EaService.class);
	private String[] title = { "rsrp", "rsrp", "rsrq", "snr", "bw", "delay", "lose" };
	@Autowired
	AreaService areaService;
	@Autowired
	ComprehensiveDao comprehensiveDao;
	@Autowired
	AreaDao areaDao;
	@Autowired
	KpiSetDao kpiSetDao;

	/**
	 * 分页查询
	 * 
	 * @param params
	 * @return
	 */
	public PageBean queryPage(Map<String, Object> params) {
		try {
			PageBean page = super.queryForList(ComprehensiveDao.class, params);
			return page;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}

	/**
	 * 
	 * @param bean
	 *            查询条件
	 * @return
	 */
	public String getParam(QueryBean bean, User user) {
		String uuid = null;
		if (bean.getUuid() == null || "".equals(bean.getUuid().trim())) {
			uuid = StringUtils.getUuid();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("uuid", uuid);
			// 用户信息
			param.put("userId", user.getUserid());
			param.put("operator", user.getOperator());
			// 时间
			param.put("timegransel", bean.getTimegransel());
			Map<String, String> se = TimeUtils.getSETime(bean.getQueryTime());
			param.put("starttime", se.get("starttime"));
			param.put("endtime", se.get("endtime"));
			// 运营商
			param.put("telecoms", bean.getOperator());
			//imei
			param.put("imei", bean.getImei()!=null&&!"".equals(bean.getImei())?bean.getImei():"-1");
			// 区域
			param.put("province", bean.getProvince());
			param.put("district", bean.getDistrict());
			param.put("area", bean.getArea());
			// 道路
			param.put("roadType", bean.getRoadType());
			param.put("roadLevel", bean.getRoadLevel());
			param.put("roadId", bean.getRoadId());
			// 大于12Mbps占比
			param.put("threshold", Integer.valueOf(bean.getThreshold().intValue()));
			// 维度
			param.put("showTime", bean.getShowTime() != null ? bean.getShowTime() : 0);
			param.put("showArea", bean.getShowArea() != null ? bean.getShowArea() : 0);
			param.put("showRoad", bean.getShowRoad() != null ? bean.getShowRoad() : 0);
			param.put("showOperator", bean.getShowOperator() != null ? bean.getShowOperator() : 0);
			param.put("showInterval", bean.getShowInterval() != null ? bean.getShowInterval() : 0);
			int flag = -1;
			comprehensiveDao.kpiAnalysis(param);
			flag = NumberUtils.toInt(param.get("flag").toString());
		} else {
			uuid = bean.getUuid();
		}
		return uuid;
	}

	/**
	 * 查询列名
	 * 
	 * @param user
	 * @return
	 */
	public Map<String, Object> queryUserInterval(User user) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", user.getUserid());
		param.put("operator", user.getOperator());
		map = comprehensiveDao.queryUserInterval(param);
		return map;
	}

	/**
	 * 
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> export(HttpServletRequest request, QueryBean bean, User user) throws Exception {
		Map<String ,Object> param = new HashMap<String ,Object>();
		Map<String, Object> mapQuery = new HashMap<String, Object>();
		Map<String, Object> mapParam = new HashMap<String, Object>();
		bean.setPageIndex(-1);
		bean.setPageSize(-1);
		// 区域
		mapQuery.put("province", bean.getProvince());
		mapQuery.put("district", bean.getDistrict());
		mapQuery.put("area", bean.getArea());
		// 道路
		mapQuery.put("roadType", bean.getRoadType());
		mapQuery.put("roadLevel", bean.getRoadLevel());
		List<RoadInfo4ESBean> listRoadInfo = areaDao.queryRoadInfo(mapQuery);

		mapParam.put("queryBean", bean);
		mapParam.put("pageIndex", -1);
		mapParam.put("pageSize", -1);

		// query区间信息
		List<KpiSettings> kpiInfos = null;
		if (null != bean && null != bean.getShowInterval() && bean.getShowInterval() == 1) {
			Map<String, Object> mapKpi = new HashMap<String, Object>();
			mapKpi.put("operator", user.getOperator());
			mapKpi.put("id", user.getUserid());
			kpiInfos = kpiSetDao.getAllKpi(mapKpi);
		}
		
		 param.put("uuid", bean.getUuid());
		 List<Map<String, Object>> list = comprehensiveDao.exportList(param);

		Map<String, Object> columns = queryUserInterval(user);// 指标区间列名

		List<Object> titleName = new ArrayList<Object>();
		List<String> columnName = new ArrayList<String>();
		List<Integer> type = new ArrayList<Integer>();

		// 时间
		if (bean.getShowTime() != null && bean.getShowTime() == 1) {
			titleName.add("开始时间");
			titleName.add("结束时间");
			columnName.add("start_time");
			columnName.add("end_time");
			type.add(1);
			type.add(1);
		}
		// 区域
		if (bean.getShowArea() != null && bean.getShowArea() == 1) {
			titleName.add("市级");
			titleName.add("区县");
			columnName.add("parent_region");
			columnName.add("region_name");
			type.add(1);
			type.add(1);
		}
		// 道路
		if (bean.getShowRoad() != null && bean.getShowRoad() == 1) {
			// titleName.add("道路类型");
			// titleName.add("道路等级");
			titleName.add("道路名称");
			// columnName.add("road_type");
			// columnName.add("road_level");
			columnName.add("road_name");
			// type.add(1);
			// type.add(1);
			type.add(1);
		}
		// 运营商
		if (bean.getShowOperator() != null && bean.getShowOperator() == 1) {
			titleName.add("运营商");
			columnName.add("telecoms");
			type.add(1);
		}
		titleName.add("信号采样点数量");
		titleName.add("业务总采样点数");
		// titleName.add("弱覆盖点数量");
		// titleName.add("无线覆盖率(%)");
		titleName.add("测试里程(km)");
		titleName.add("覆盖里程(km)");
		titleName.add("弱覆盖里程(km)");
		titleName.add("道路点覆盖率(%)");
		
		titleName.add("网络掉线次数");
		titleName.add("网络掉线率(%)");
		titleName.add("业务掉线次数");
		titleName.add("业务掉线率(%)");
		titleName.add("4G下切3G次数");
		
		columnName.add("point_signal");
		columnName.add("point_business");
		// columnName.add("point_poor_coverage");
		// columnName.add("wireless_coverage");
		columnName.add("road_test_mileage");
		columnName.add("road_cover_mileage");
		columnName.add("road_poor_mileage");
		columnName.add("road_cover_rate");
		
		columnName.add("network_count");
		columnName.add("network_rate");
		columnName.add("ping_count");
		columnName.add("ping_rate");
		columnName.add("network_change");
		
		type.add(2);
		type.add(2);
		// type.add(2);
		// type.add(2);
		type.add(2);
		type.add(2);
		type.add(2);
		type.add(2);
		
		type.add(2);
		type.add(2);
		type.add(2);
		type.add(2);
		type.add(2);
		
		// RSRP
		titleName.add("RSRP(dBm)");
		columnName.add("lte_rsrp");
		type.add(2);
		if (bean.getShowInterval() != null && bean.getShowInterval() == 1) {
			if (bean.getRsrp() != null && bean.getRsrp() == 1) {
				titleName = getColumnName("RSRP", titleName, columns, "rsrp");
				columnName = getFieldName(columnName, "rsrp");
				type = getFieldType(type);
			}
		}
		// RSRQ
		titleName.add("RSRQ(dB)");
		columnName.add("lte_rsrq");
		type.add(2);
		if (bean.getShowInterval() != null && bean.getShowInterval() == 1) {
			if (bean.getRsrq() != null && bean.getRsrq() == 1) {
				titleName = getColumnName("RSRQ", titleName, columns, "rsrq");
				columnName = getFieldName(columnName, "rsrq");
				type = getFieldType(type);
			}
		}
		// SNR
		titleName.add("SNR(dB)");
		columnName.add("lte_snr");
		type.add(2);
		if (bean.getShowInterval() != null && bean.getShowInterval() == 1) {
			if (bean.getSnr() != null && bean.getSnr() == 1) {
				titleName = getColumnName("SNR", titleName, columns, "snr");
				columnName = getFieldName(columnName, "snr");
				type = getFieldType(type);
			}
		}
		// 下行链路带宽
		titleName.add("下行链路带宽(Mbps)");
		columnName.add("bw_link");
		type.add(2);
		titleName.add(">" + Integer.valueOf(bean.getThreshold().intValue()) + "Mbps比例");
		columnName.add("bw_link_rate");
		type.add(2);
		if (bean.getShowInterval() != null && bean.getShowInterval() == 1) {
			if (bean.getBroadband() != null && bean.getBroadband() == 1) {
				titleName = getColumnName("带宽", titleName, columns, "bw");
				columnName = getFieldName(columnName, "bw_link");
				type = getFieldType(type);
			}
		}
		// 延时
		titleName.add("时延(ms)");
		columnName.add("ping_avg_delay");
		type.add(2);
		if (bean.getShowInterval() != null && bean.getShowInterval() == 1) {
			if (bean.getDelay() != null && bean.getDelay() == 1) {
				titleName = getColumnName("时延", titleName, columns, "delay");
				columnName = getFieldName(columnName, "ping_delay");
				type = getFieldType(type);
			}
		}
		// 延时
		titleName.add("丢包率(%)");
		columnName.add("ping_lose_rate");
		type.add(2);
		if (bean.getShowInterval() != null && bean.getShowInterval() == 1) {
			if (bean.getLose() != null && bean.getLose() == 1) {
				titleName = getColumnName("丢包率", titleName, columns, "lose");
				columnName = getFieldName(columnName, "ping_lose");
				type = getFieldType(type);
			}
		}
		List<String> sheetName = new ArrayList<String>();
		sheetName.add("指标综合分析");
		List<String> sheetColumn = new ArrayList<String>();
		sheetColumn.add("kpi");
		Map<String, Object> tln = new HashMap<String, Object>();
		tln.put("kpi", titleName);
		Map<String, Object> col = new HashMap<String, Object>();
		col.put("kpi", columnName);
		Map<String, Object> types = new HashMap<String, Object>();
		types.put("kpi", type);
		Map<String, Object> lt = new HashMap<String, Object>();
		lt.put("kpi", list);
		String fileName = ExportUtils.createExcel(request, sheetName, sheetColumn, tln, col, types, lt);

		Map<String, String> map = new HashMap<String, String>();
		map.put("fileName", fileName);
		return map;
	}

	/**
	 * 区间列名获取
	 * 
	 * @param list
	 * @param columns
	 * @param name
	 * @return
	 */
	public List<Object> getColumnName(String kpiName, List<Object> list, Map<String, Object> columns, String name) {
		for (int i = 1; i <= 6; i++) {
			list.add(kpiName + columns.get(name + "Range" + i) + " (%)");
			list.add(kpiName + columns.get(name + "Range" + i) + " (km)");
		}
		return list;
	}

	/**
	 * 区间列字段获取
	 * 
	 * @param list
	 * @param name
	 * @return
	 */
	public List<String> getFieldName(List<String> list, String name) {
		for (int i = 1; i <= 6; i++) {
			list.add(name + "_ratio" + i);
			list.add(name + "_point" + i);
		}
		return list;
	}

	/**
	 * 区间列字段列类型
	 * 
	 * @param list
	 * @param name
	 * @return
	 */
	public List<Integer> getFieldType(List<Integer> list) {
		for (int i = 1; i <= 12; i++) {
			list.add(2);
		}
		return list;
	}

	public Map<String, Object> changeWorld(Map<String, Object> map) {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < title.length; j++) {
				String str = (String) map.get(title[j] + "Range" + (i + 1));
				str = str.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
				map.put(title[j] + "Range" + (i + 1), str);
			}
		}
		return map;
	}
}
