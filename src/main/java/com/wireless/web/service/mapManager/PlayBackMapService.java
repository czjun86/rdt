package com.wireless.web.service.mapManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wireless.web.dao.kpiManager.KpiSetDao;
import com.wireless.web.dao.mapManager.MapDao;
import com.wireless.web.daoElastic.mapManager.PlayBackMapDaoES;
import com.wireless.web.model.Area;
import com.wireless.web.model.KpiSettings;
import com.wireless.web.model.Points;
import com.wireless.web.model.VoBean;
import com.wireless.web.utils.ExportUtils;

@Service("playBackMapService")
public class PlayBackMapService {

	private static final Logger logger = LoggerFactory
			.getLogger(PlayBackMapService.class);

	@Autowired
	protected MapDao mapDao;
	@Autowired
	protected KpiSetDao kpiSetDao;
	@Autowired
	protected PlayBackMapDaoES playBackMapDaoES;

	/**
	 * 根据用户ID查询出对应的一级行政区域名称
	 */

	public Area getAreaNameByUserId(String userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		Area area = null;
		List<Area> list = mapDao.getAreaNameByUserId(map);
		if (list != null && list.size() > 0) {
			area = list.get(0);
		}
		return area;
	}

	/**
	 * 查询点
	 */
	public List<KpiSettings> getKpi(VoBean vo,String isWarn) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isWarn", isWarn);
		map.put("vo", vo);
		List<KpiSettings> kpis = mapDao.getKpi(map);
		if (kpis.size() == 0) {
			vo.setUserId(0);
			map.put("vo", vo);
			map.put("isWarn", isWarn);
			kpis = mapDao.getKpi(map);
		}
		return kpis;
	}

	/**
	 * 查询采样点数据
	 * 
	 * @return
	 */
	public Map<String, Object> getPoint(Map<String, Object> map) {
		
		Map<String, Object> map1 = new HashMap<String, Object>();
		Map<String, Object> query = new HashMap<String, Object>();
		try {
			VoBean vb = (VoBean) map.get("vo");
			query.put("id", vb.getUserId());
			query.put("operator", vb.getOperator());
			List<KpiSettings> kpiSettings = kpiSetDao.getAllKpi(query);
			map1 = playBackMapDaoES.getPoint(map, kpiSettings);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map1;
	}

	/**
	 * 查询采样点平值
	 * 
	 * @return
	 */
	public Map<String, Object> getPointAvg(Map<String, Object> map) {
		// 先调用相信过程再查询表数据
		//List<Map<String, Object>> list = mapDao.getProAvgPoint(map);
		try {
			KpiSettings kpiSettings = this.getKpi((VoBean) map.get("vo"),"0").get(0);
			playBackMapDaoES.getProAvgPoint(map, kpiSettings);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public String exportPoint(HttpServletRequest request, VoBean bean)
			throws Exception {
		KpiSettings kpiSettings = this.getKpi(bean,"0").get(0);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("vo", bean);
		playBackMapDaoES.getProAvgPoint(param, kpiSettings);
		List<Map<String, Object>> list = playBackMapDaoES.getPointExcel(param,kpiSettings);
		List<Object> titleName = new ArrayList<Object>();
		List<String> columnName = new ArrayList<String>();
		List<Integer> type = new ArrayList<Integer>();
		String[] tn = { "IMEI", "时间", "经度", "纬度", "lte_ci", "pci", "enode_bid",
				"tac", "lte_rsrp(dBm)", "lte_rsrq(dB)", "SNR(dB)",
				"下行链路带宽(Mbps)", "时延(ms)", "丢包(%)" };
		String[] cn = { "imei", "time", "lng", "lat", "ci", "pci", "bid",
				"tac", "rsrp", "rsrq", "snr", "link", "delay", "lose" };
		Integer[] tp = { 1, 1, 1, 1, 2, 1, 1, 1, 2, 2, 2, 2, 2, 2 };
		for (int i = 0; i < cn.length; i++) {
			titleName.add(tn[i]);
			columnName.add(cn[i]);
			type.add(tp[i]);
		}
		List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
		list2.add(param);
		List<Object> titleName2 = new ArrayList<Object>();
		List<String> columnName2 = new ArrayList<String>();
		List<Integer> type2 = new ArrayList<Integer>();
		String[] tn2 = { "信号采样点数量", "性能采样点数量", "lte_rsrp(dBm)", "lte_rsrq(dB)",
				"SNR(dB)", "下行链路带宽(Mbps)", "时延(ms)", "丢包(%)" };
		String[] cn2 = { "signalCount", "busCount", "rsrp", "rsrq", "snr",
				"link", "delay", "lose" };
		Integer[] tp2 = { 1, 1, 1, 1, 1, 1, 1, 1 };
		for (int i = 0; i < cn2.length; i++) {
			titleName2.add(tn2[i]);
			columnName2.add(cn2[i]);
			type2.add(tp2[i]);
		}
		List<String> sheetName = new ArrayList<String>();
		sheetName.add("采样点详情");
		sheetName.add("采样点均值");
		List<String> sheetColumn = new ArrayList<String>();
		sheetColumn.add("point");
		sheetColumn.add("pointAvg");
		Map<String, Object> tln = new HashMap<String, Object>();
		tln.put("point", titleName);
		tln.put("pointAvg", titleName2);
		Map<String, Object> col = new HashMap<String, Object>();
		col.put("point", columnName);
		col.put("pointAvg", columnName2);
		Map<String, Object> lt = new HashMap<String, Object>();
		lt.put("point", list);
		lt.put("pointAvg", list2);
		Map<String, Object> types = new HashMap<String, Object>();
		types.put("point", type);
		types.put("pointAvg", type2);
		String fileName = ExportUtils.createExcel(request, sheetName,
				sheetColumn, tln, col, types, lt);
		return fileName;
	}
}
