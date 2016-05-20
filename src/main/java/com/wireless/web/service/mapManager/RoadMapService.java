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
import com.wireless.web.dao.mapManager.RoadMapDao;
import com.wireless.web.daoElastic.mapManager.RoadMapDaoES;
import com.wireless.web.model.Area;
import com.wireless.web.model.KpiSettings;
import com.wireless.web.model.Pot;
import com.wireless.web.model.RoadPoints;
import com.wireless.web.model.VoBean;
import com.wireless.web.utils.ExportUtils;

@Service("roadMapService")
public class RoadMapService {

	private static final Logger logger = LoggerFactory
			.getLogger(RoadMapService.class);
	
	@Autowired
	protected RoadMapDao roadMapDao;
	@Autowired
	protected KpiSetDao kpiSetDao;
	@Autowired
	protected RoadMapDaoES roadMapDaoES;
	/**
	 * 根据用户ID查询出对应的一级行政区域名称
	 */
	
	public Area getAreaNameByUserId(String userId)
	{
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("userId",userId);
		Area area=null;
		List<Area> list=roadMapDao.getAreaNameByUserId(map);
		if(list!=null&&list.size()>0){
			area=list.get(0);
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
		List<KpiSettings> kpis = roadMapDao.getKpi(map);
		if (kpis.size() == 0) {
			vo.setUserId(0);
			map.put("isWarn", isWarn);
			map.put("vo", vo);
			kpis = roadMapDao.getKpi(map);
		}
		return kpis;
	}
	/**
	 * 查询采样点数据
	 * isdiejia,是否叠加
	 * @return
	 */
	public List<RoadPoints> getPoint(VoBean vv){
		//先调用相信过程再查询表数据,运营商对比不调用存储过程
		List<RoadPoints> points=new ArrayList<RoadPoints>();	
//		points=roadMapDao.getPoint(map);
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("vo", vv);
			points = roadMapDaoES.getPoint(vv);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return points;
	}
	/**
	 * 查询采样点数据
	 * isdiejia,是否叠加
	 * @return
	 */
	public Map<String, Object> getPot(VoBean vv) {
		Map<String, Object> points = null;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			List<Pot> roadPots = null;

			map.put("id", vv.getUserId());
			map.put("operator", vv.getOperator());
			List<KpiSettings> kpiSettings = kpiSetDao.getAllKpi(map);
			map.put("vo", vv);
			roadPots = null != vv.getLevel() && vv.getLevel() == 1 ? roadMapDao.getRoadPoint(map) : null;
			points = roadMapDaoES.getPot(map, kpiSettings, roadPots);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return points;
	}
	/**
	 * 查询采样点平值
	 * @return
	 */
	public Map<String ,Object> getPointAvg(Map<String, Object> map){
		//先调用相信过程再查询表数据
//		List<Map<String ,Object>> list= roadMapDao.getProAvgPoint(map);
		//getPointAvg(Map<String, Object> map,VoBean vv, KpiSettings kpiSettings)
		try {
			VoBean vv = (VoBean)map.get("vo");
			KpiSettings kpiSettings = this.getKpi(vv,"0").get(0);
			roadMapDaoES.getPointAvg(map, vv, kpiSettings);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public String exportPoint(HttpServletRequest request,VoBean bean) throws Exception{
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("vo", bean);
		
		KpiSettings kpiSettings = this.getKpi(bean,"0").get(0);
		roadMapDaoES.getPointAvg(param, bean, kpiSettings);
//		roadMapDao.getProAvgPoint(param);
//		List<Map<String ,Object>>list = roadMapDao.getPointExcel(param);
		List<Map<String ,Object>>list = roadMapDaoES.getPointExcel(bean, kpiSettings);
		List<Object> titleName = new ArrayList<Object>();
		List<String> columnName = new ArrayList<String>();
		List<Integer> type = new ArrayList<Integer>();
		String[] tn = {"道路名称","信号采样点数量","性能采样点数量","经度","纬度","服务小区数量",
				"主服务小区CI","主服务小区CI占比","CI2","CI2占比","CI3","CI3占比","CI4","CI4占比","CI5","CI5占比",
				"主服务小区pci","主服务小区pci占比","PCI2","PCI2占比","PCI3","PCI3占比","PCI4","PCI4占比","PCI5","PCI5占比",
				"主服务小区ENodeBID","主服务小区ENodeBID占比",
				"服务TAC数量","主服务小区TAC","主服务小区TAC占比","TAC2","TAC2占比","TAC3","TAC3占比","TAC4","TAC4占比","TAC5","TAC5占比",
				"lte_rsrp(dBm)","lte_rsrq(dB)","SNR(dB)","下行链路带宽(Mbps)","时延(ms)","丢包(%)","覆盖率(%)",
				"网络掉线次数","业务掉线次数","4G切3G次数","网络掉线率(%)","业务掉线率(%)"
				//,"4G切3G概率(%)"
				};
		String[] cn = {"road_name","count","con","lng","lat","cellCnt",
				"ci1","ciRate1","ci2","ciRate2","ci3","ciRate3","ci4","ciRate4","ci5","ciRate5",
				"pci1","pciRate1","pci2","pciRate2","pci3","pciRate3","pci4","pciRate4","pci5","pciRate5",
				"bid1","bidRate1",
				"tacCnt","tac1","tacRate1","tac2","tacRate2","tac3","tacRate3","tac4","tacRate4","tac5","tacRate5",
				"rsrp","rsrq","snr","link","delay","lose","cover",
				"networkCount","pingCount","lteToWcdmaCount","networkRate","pingRate"
				//,"lteToWcdmaRate"
				};
		Integer [] tp = {1,2,2,1,1,2,
				1,1,1,1,1,1,1,1,1,1,
				1,1,1,1,1,1,1,1,1,1,
				1,1,
				2,2,1,2,1,2,1,2,1,2,1,
				2,2,2,2,2,2,2,
				2,2,2,2,2
				//,2
				};
		for(int i=0;i<cn.length;i++){
			titleName.add(tn[i]);
			columnName.add(cn[i]);
			type.add(tp[i]);
		}
		List<Map<String ,Object>>list2 = new ArrayList<Map<String ,Object>>();
		list2.add(param);
		List<Object> titleName2 = new ArrayList<Object>();
		List<String> columnName2 = new ArrayList<String>();
		List<Integer> type2 = new ArrayList<Integer>();
		String[] tn2 = {"信号采样点数量","性能采样点数量","lte_rsrp(dBm)","lte_rsrq(dB)","SNR(dB)","下行链路带宽(Mbps)","时延(ms)","丢包(%)",
				"网络掉线次数","业务掉线次数","4G切3G次数","网络掉线率","业务掉线率"};
		String[] cn2 = {"signalCount","busCount","rsrp","rsrq","snr","link","delay","lose",
				"networkCount","pingCount","lteToWcdmaCount","networkRate","pingRate"};
		Integer [] tp2 = {1,1,1,1,1,1,1,1,
				2,2,2,1,1};
		for(int i=0;i<cn2.length;i++){
			titleName2.add(tn2[i]);
			columnName2.add(cn2[i]);
			type2.add(tp2[i]);
		}
		List<String> sheetName = new ArrayList<String>();
		sheetName.add("道路点详情");
		sheetName.add("道路点均值");
		List<String> sheetColumn  = new ArrayList<String>();
		sheetColumn.add("point");
		sheetColumn.add("pointAvg");
		Map<String ,Object> tln = new HashMap<String ,Object>();
		tln.put("point", titleName);
		tln.put("pointAvg", titleName2);
		Map<String ,Object> col = new HashMap<String ,Object>();
		col.put("point", columnName);
		col.put("pointAvg", columnName2);
		Map<String ,Object> lt = new HashMap<String ,Object>();
		lt.put("point", list);
		lt.put("pointAvg", list2);
		Map<String ,Object> types = new HashMap<String ,Object>();
		types.put("point", type);
		types.put("pointAvg", type2);
		String fileName = ExportUtils.createExcel(request, sheetName,sheetColumn, tln, col,types, lt);
		return fileName;
	}
}
