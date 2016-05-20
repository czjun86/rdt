/**     
 * @文件名称: MapDaoImpl.java  
 * @类路径: com.wireless.web.daoElastic  
 * @描述: GIS 采样点计算  
 * @作者：dsk  
 * @时间：2015年11月26日 上午10:03:00  
 * @版本：V1.0     
 */
package com.wireless.web.daoElastic.mapManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.wireless.web.daoElastic.utils.ESConnUtils;
import com.wireless.web.daoElastic.utils.QueryUtils;
import com.wireless.web.daoElastic.utils.StringUtils;
import com.wireless.web.model.KpiSettings;
import com.wireless.web.model.Points;
import com.wireless.web.model.VoBean;

/**
 * @类功能说明：
 * @类修改者：
 * @修改日期：
 * @修改说明：
 * @公司名称：
 * @作者：dsk
 * @创建时间：2015年11月26日 上午10:03:00
 * @版本：V1.0
 */
@Service("mapDaoES")
public class MapDaoES {
	private static Logger log = LoggerFactory.getLogger(EpinfoMapDaoES.class);
	private VoBean voBean;

	public Map<String, Object> getPoint(Map<String, Object> map, List<KpiSettings> kpiSettings) throws Exception {

		voBean = (VoBean) map.get("vo");
		List<Points> listPoints = new ArrayList<Points>();
		List<Points> warnPoints = new ArrayList<Points>();

		// 事件: -1,无事件,9网络掉线,10业务掉线,114G切换3G
		String voWarn = (null != voBean.getWarn() && !"".equals(voBean.getWarn())) ? voBean.getWarn() : "-1";
		Integer qryKpi = voBean.getKpiType();
		String qryKpiName = StringUtils.getKpiName(qryKpi);
		// 指标区间配置
		KpiSettings kpiSet = QueryUtils.getKpiSetByList(kpiSettings, qryKpi);
		// 事件区间配置
		KpiSettings eventSet = voWarn.equals("-1") ? null : QueryUtils.getKpiSetByList(kpiSettings, Integer.parseInt(voWarn));

		TransportClient tc = ESConnUtils.getTransportClient();

		BoolQueryBuilder boolQuery = QueryUtils.qryMapCondition(voBean, 1);

		// index,type
		String queryTime = voBean.getStarttime().replace("-", ".").trim() + "-" + voBean.getEndtime().replace("-", ".").trim();
		List<String> searchList = StringUtils.findIndex4ES(queryTime, voBean.getProvince());

		String[] docs = new String[searchList.size()];
		for (int i = 0; i < searchList.size(); i++) {
			docs[i] = searchList.get(i);
		}
		SearchRequestBuilder srb = tc.prepareSearch(docs);

		srb.addFields("id", "imei", "longitude", "latitude", "lte_ci", "pci", "enode_bid", "tac", "lte_rsrp", "lte_rsrq", "lte_snr", "bw_link", "ping_avg_delay", "ping_lose_rate", "test_time", "amend_mark", "network_event", "ping_event");
		srb.setQuery(boolQuery);
		SearchResponse scrollResp = srb.setScroll(new TimeValue(ESConnUtils.ES_SCNA_TIME)).setSize(ESConnUtils.ES_SCNA_SIZE).execute().actionGet();

		// json
		log.debug(srb + "");
		log.debug("TotalHits: " + scrollResp.getHits().getTotalHits());

		Double kpiValue = null;
		Double bwLink = null;
		Double pingAvgDelay = null;
		Double pingLoseRate = null;
		String kpiItv = "0";
		String kpiItvClr = "";
		DecimalFormat df2 = new DecimalFormat("0.00");
		String networkEvent = "";
		String pingEvent = "";
		while (true) {
			log.debug("ScrollTookInMillis: " + scrollResp.getTookInMillis());
			for (SearchHit hit : scrollResp.getHits().getHits()) {

				Map<String, SearchHitField> mapHits = hit.getFields();
				Points ptsKpi = new Points();
				Points ptsEvent = new Points();
				// 事件
				networkEvent = mapHits.get("network_event").getValue() + "";
				pingEvent = mapHits.get("ping_event").getValue() + "";
				// 区间判断
				// RSRP,SNR无覆盖判断
				if (null != qryKpi && ((qryKpi == 1 || qryKpi == 2) && mapHits.get("amend_mark").getValue().toString().equals("3"))) {
					ptsKpi.setState("#000000");
					ptsKpi.setInter("7");
					ptsKpi.setLng(mapHits.get("longitude").getValue() + "");
					ptsKpi.setLat(mapHits.get("latitude").getValue() + "");
				} else {
					ptsKpi.setImei(mapHits.get("imei").getValue() + "");
					ptsKpi.setLng(mapHits.get("longitude").getValue() + "");
					ptsKpi.setLat(mapHits.get("latitude").getValue() + "");
					ptsKpi.setCi(mapHits.get("lte_ci").getValue() + "");
					ptsKpi.setPci(mapHits.get("pci").getValue() + "");
					ptsKpi.setBid(mapHits.get("enode_bid").getValue() + "");
					ptsKpi.setTac(mapHits.get("tac").getValue() + "");
					ptsKpi.setRsrp(mapHits.get("lte_rsrp").getValue() + " dBm");
					ptsKpi.setRsrq(mapHits.get("lte_rsrq").getValue() + " dB");
					ptsKpi.setSnr(mapHits.get("lte_snr").getValue() + " dB");
					ptsKpi.setTime(StringUtils.findDate(mapHits.get("test_time").getValue() + ""));

					bwLink = Double.parseDouble(mapHits.get("bw_link").getValue() + "");
					pingAvgDelay = Double.parseDouble(mapHits.get("ping_avg_delay").getValue() + "");
					pingLoseRate = Double.parseDouble(mapHits.get("ping_lose_rate").getValue() + "");

					ptsKpi.setLink(bwLink == -998 ? "N/A" : df2.format(bwLink / 1024) + " Mbps");
					ptsKpi.setDelay(pingAvgDelay == -998 ? "N/A" : pingAvgDelay + " ms");
					ptsKpi.setLose(pingLoseRate == -998 ? "N/A" : pingLoseRate + " %");

					kpiValue = Double.parseDouble(mapHits.get(qryKpiName).getValue() + "");
					// bw_link
					kpiValue = qryKpi == 4 ? kpiValue / 1024 : kpiValue;
					kpiItv = QueryUtils.kpiInterval(kpiSet, kpiValue);
					kpiItvClr = QueryUtils.kpiIntervalColor(kpiSet, kpiItv);
					ptsKpi.setInter(kpiItv);
					ptsKpi.setState(kpiItvClr);

				}
				// 配置区间内
				if (null != ptsKpi.getInter() && ptsKpi.getInter() != "0") {
					// 采样点网络事件判断
					ptsKpi.setNetWarn(convertNetworkEvent(networkEvent, 1));
					// 采样点业务事件判断
					ptsKpi.setDicWarn(convertNetworkEvent(pingEvent, 2));
					// 添加到集合中
					listPoints.add(ptsKpi);

					// 事件集合
					ptsEvent.setLat(ptsKpi.getLat());
					ptsEvent.setLng(ptsKpi.getLng());
					// 区间颜色根据事件配置,重新赋值
					ptsEvent.setInter("6");
					// 4G切换3G 固定颜色(红色)
					if (voWarn.equals("11")) {
						ptsEvent.setState("#FF0000");
					} else if (!voWarn.equals("-1")) {
						ptsEvent.setState(QueryUtils.kpiIntervalColor(eventSet, "6"));
					}
					// -1,无事件,9网络掉线,10业务掉线,114G切换3G
					switch (voWarn) {
					case "-1":
						break;
					case "9":
						if (networkEvent.equals("1") || networkEvent.equals("3")) {
							ptsEvent.setWarn("9");
							warnPoints.add(ptsEvent);
						}
						break;
					case "10":
						if (pingEvent.equals("1")) {
							ptsEvent.setWarn("10");
							warnPoints.add(ptsEvent);
						}
						break;
					case "11":
						if (networkEvent.equals("5")) {
							ptsEvent.setWarn("11");
							warnPoints.add(ptsEvent);
						}
						break;
					default:
						break;
					}
				}
			}
			scrollResp = tc.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(ESConnUtils.ES_SCNA_TIME)).execute().actionGet();
			// Break condition: No hits are returned
			if (scrollResp.getHits().getHits().length == 0) {
				break;
			}
		}
		map.put("points", listPoints);
		map.put("warnPoints", warnPoints);
		return map;
	}

	/**
	 * 功能说明: 生成Excel 修改者名字: dsk 修改日期 2015年12月9日 修改内容
	 * 
	 * @参数： @param map
	 * @参数： @param kpiSettings
	 * @参数： @return
	 * @throws
	 */
	public List<Map<String, Object>> getPointExcel(Map<String, Object> map, KpiSettings kpiSettings) throws Exception {

		voBean = (VoBean) map.get("vo");
		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		Integer qryKpi = voBean.getKpiType();
		String qryKpiName = StringUtils.getKpiName(qryKpi);
		String qryKpiInterv = voBean.getKpiInterv();

		// 获取ES连接
		TransportClient tc = ESConnUtils.getTransportClient();

		// 查询条件
		BoolQueryBuilder boolQuery = QueryUtils.qryMapCondition(voBean, 1);

		// index,type
		String queryTime = voBean.getStarttime().replace("-", ".").trim() + "-" + voBean.getEndtime().replace("-", ".").trim();
		List<String> searchList = StringUtils.findIndex4ES(queryTime, voBean.getProvince());

		String[] docs = new String[searchList.size()];
		for (int i = 0; i < searchList.size(); i++) {
			docs[i] = searchList.get(i);
		}
		SearchRequestBuilder srb = tc.prepareSearch(docs);

		srb.addFields("id", "imei", "longitude", "latitude", "lte_ci", "pci", "enode_bid", "tac", "lte_rsrp", "lte_rsrq", "lte_snr", "bw_link", "ping_avg_delay", "ping_lose_rate", "test_time", "amend_mark", "network_event", "ping_event");
		srb.setPostFilter(boolQuery);
		SearchResponse scrollResp = srb.setScroll(new TimeValue(ESConnUtils.ES_SCNA_TIME)).setSize(ESConnUtils.ES_SCNA_SIZE).execute().actionGet();

		// json
		log.debug(srb + "");
		log.debug("TotalHits: " + scrollResp.getHits().getTotalHits());

		Double kpiValue = null;
		Double bwLink = null;
		Double pingAvgDelay = null;
		Double pingLoseRate = null;
		String kpiItv = "0";
		String networkEvent = "";
		String pingEvent = "";
		DecimalFormat df2 = new DecimalFormat("0.00");
		while (true) {
			log.debug("ScrollTookInMillis: " + scrollResp.getTookInMillis());
			for (SearchHit hit : scrollResp.getHits().getHits()) {

				Map<String, SearchHitField> mapHits = hit.getFields();
				Map<String, Object> pointMap = new HashMap<String, Object>();
				// 事件
				networkEvent = mapHits.get("network_event").getValue() + "";
				pingEvent = mapHits.get("ping_event").getValue() + "";
				// 区间判断
				// RSRP,SNR无覆盖判断
				if (null != qryKpi && ((qryKpi == 1 || qryKpi == 2) && mapHits.get("amend_mark").getValue().toString().equals("3"))) {
				} else {
					pointMap.put("imei", mapHits.get("imei").getValue() + "");
					pointMap.put("lng", mapHits.get("longitude").getValue() + "");
					pointMap.put("lat", mapHits.get("latitude").getValue() + "");
					pointMap.put("ci", mapHits.get("lte_ci").getValue() + "");
					pointMap.put("pci", mapHits.get("pci").getValue() + "");
					pointMap.put("bid", mapHits.get("enode_bid").getValue() + "");
					pointMap.put("tac", mapHits.get("tac").getValue() + "");
					pointMap.put("rsrp", mapHits.get("lte_rsrp").getValue() + "");
					pointMap.put("rsrq", mapHits.get("lte_rsrq").getValue() + "");
					pointMap.put("snr", mapHits.get("lte_snr").getValue() + "");
					pointMap.put("delay", mapHits.get("ping_avg_delay").getValue() + "");
					pointMap.put("lose", mapHits.get("ping_lose_rate").getValue() + "");
					pointMap.put("time", StringUtils.findDate(mapHits.get("test_time").getValue() + ""));

					bwLink = Double.parseDouble(mapHits.get("bw_link").getValue() + "");
					pingAvgDelay = Double.parseDouble(mapHits.get("ping_avg_delay").getValue() + "");
					pingLoseRate = Double.parseDouble(mapHits.get("ping_lose_rate").getValue() + "");

					pointMap.put("link", bwLink == -998 ? "N/A" : df2.format(bwLink / 1024) + "");
					pointMap.put("delay", pingAvgDelay == -998 ? "N/A" : pingAvgDelay + "");
					pointMap.put("lose", pingLoseRate == -998 ? "N/A" : pingLoseRate + "");

					kpiValue = Double.parseDouble(mapHits.get(qryKpiName).getValue() + "");
					// bw_link
					kpiValue = qryKpi == 4 ? kpiValue / 1024 : kpiValue;
					kpiItv = QueryUtils.kpiInterval(kpiSettings, kpiValue);
					pointMap.put("inter", kpiItv);

					// 事件判定
					pointMap.put("netWarn", convertNetworkEvent(networkEvent, 1));
					pointMap.put("dicWarn", convertNetworkEvent(pingEvent, 2));

					// 配置区间内
					if (null != kpiItv && kpiItv != "0" && null != qryKpiInterv && qryKpiInterv.indexOf(kpiItv) != -1) {
						lists.add(pointMap);
					}
				}
			}
			scrollResp = tc.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(ESConnUtils.ES_SCNA_TIME)).execute().actionGet();
			// Break condition: No hits are returned
			if (scrollResp.getHits().getHits().length == 0) {
				break;
			}
		}
		return lists;
	}

	/**
	 * 
	 * 功能说明: 全选平均值计算 修改者名字: dsk 修改日期 2015年12月9日 修改内容
	 * 
	 * @参数： @param map
	 * @参数： @param kpiSettings
	 * @throws
	 */
	public void getProAvgPoint(Map<String, Object> map, KpiSettings kpiSettings) throws Exception {
		voBean = (VoBean) map.get("vo");
		Integer qryKpi = voBean.getKpiType();
		String qryKpiName = StringUtils.getKpiName(qryKpi);
		String qryKpiInterv = voBean.getKpiInterv();
		// 获取连接
		TransportClient tc = ESConnUtils.getTransportClient();
		// 查询条件
		BoolQueryBuilder boolQuery = QueryUtils.qryMapCondition(voBean, 1);

		// index,type
		String queryTime = voBean.getStarttime().replace("-", ".").trim() + "-" + voBean.getEndtime().replace("-", ".").trim();
		List<String> searchList = StringUtils.findIndex4ES(queryTime, voBean.getProvince());

		String[] docs = new String[searchList.size()];
		for (int i = 0; i < searchList.size(); i++) {
			docs[i] = searchList.get(i);
		}
		SearchRequestBuilder srb = tc.prepareSearch(docs);

		srb.addFields("id", "imei", "longitude", "latitude", "lte_ci", "pci", "enode_bid", "tac", "lte_rsrp", "lte_rsrq", "lte_snr", "bw_link", "ping_avg_delay", "ping_lose_rate", "test_time", "amend_mark", "network_event", "ping_event");
		srb.setPostFilter(boolQuery);
		SearchResponse scrollResp = srb.setScroll(new TimeValue(ESConnUtils.ES_SCNA_TIME)).setSize(ESConnUtils.ES_SCNA_SIZE).execute().actionGet();

		// json
		log.debug(srb + "");
		log.debug("TotalHits: " + scrollResp.getHits().getTotalHits());

		Double kpiValue = null;
		String kpiItv = "0";
		// 框选结果
		int pointSignal = 0;
		int pointBusiness = 0;
		double lteRsrp = 0;
		double lteRsrq = 0;
		double lteSnr = 0;
		double bwLink = 0;
		double pingAvgDelay = 0;
		double pingLoseRate = 0;
		double pingAvgDelayCnt = 0;
		double pingLoseRateCnt = 0;
		int arrNet[] = { 0, 0, 0, 0, 0, 0, 0, 0 };
		int arrPing[] = { 0, 0 };
		int networkCount = 0;
		int pingCount = 0;
		int lteToWcdmaCount = 0;
		String networkEvent = "";
		String pingEvent = "";
		while (true) {
			log.debug("ScrollTookInMillis: " + scrollResp.getTookInMillis());
			for (SearchHit hit : scrollResp.getHits().getHits()) {

				Map<String, SearchHitField> mapHits = hit.getFields();

				// 事件
				networkEvent = mapHits.get("network_event").getValue() + "";
				pingEvent = mapHits.get("ping_event").getValue() + "";
				// 区间判断
				if (null != qryKpi && ((qryKpi == 1 || qryKpi == 2) && mapHits.get("amend_mark").getValue().toString().equals("3"))) {
				} else {
					kpiValue = Double.parseDouble(mapHits.get(qryKpiName).getValue() + "");
					// bw_link
					kpiValue = qryKpi == 4 ? kpiValue / 1024 : kpiValue;
					kpiItv = QueryUtils.kpiInterval(kpiSettings, kpiValue);
					if (kpiItv != "0" && null != qryKpiInterv && qryKpiInterv.indexOf(kpiItv) != -1) {
						pointSignal++;
						lteRsrp += Double.parseDouble(mapHits.get("lte_rsrp").getValue() + "");
						lteRsrq += Double.parseDouble(mapHits.get("lte_rsrq").getValue() + "");
						lteSnr += Double.parseDouble(mapHits.get("lte_snr").getValue() + "");
						if (Double.parseDouble(mapHits.get("bw_link").getValue() + "") != -998) {
							pointBusiness++;
							bwLink += Double.parseDouble(mapHits.get("bw_link").getValue() + "");
							if (Double.parseDouble(mapHits.get("ping_avg_delay").getValue() + "") != -998) {
								pingAvgDelayCnt++;
								pingAvgDelay += Double.parseDouble(mapHits.get("ping_avg_delay").getValue() + "");
							}
							if (Double.parseDouble(mapHits.get("ping_lose_rate").getValue() + "") != -998) {
								pingLoseRateCnt++;
								pingLoseRate += Double.parseDouble(mapHits.get("ping_lose_rate").getValue() + "");
							}
						}
						if (networkEvent != "" & Integer.parseInt(networkEvent) >= 0 && Integer.parseInt(networkEvent) <= 7) {
							arrNet[Integer.parseInt(networkEvent)]++;
						}
						if (pingEvent != "" & Integer.parseInt(pingEvent) >= 0 && Integer.parseInt(pingEvent) <= 1) {
							arrPing[Integer.parseInt(pingEvent)]++;
						}
					}
				}
			}
			scrollResp = tc.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(ESConnUtils.ES_SCNA_TIME)).execute().actionGet();
			// Break condition: No hits are returned
			if (scrollResp.getHits().getHits().length == 0) {
				break;
			}
		}
		// 组装结果
		DecimalFormat df0 = new DecimalFormat("0");
		DecimalFormat df2 = new DecimalFormat("0.00");
		map.put("signalCount", pointSignal);
		map.put("busCount", pointBusiness);
		map.put("rsrp", pointSignal == 0 ? "N/A" : df2.format(lteRsrp / pointSignal) + " dBm");
		map.put("rsrq", pointSignal == 0 ? "N/A" : df0.format(lteRsrq / pointSignal) + " dB");
		map.put("snr", pointSignal == 0 ? "N/A" : df2.format(lteSnr / pointSignal) + " dB");
		map.put("link", pointBusiness == 0 ? "N/A" : df2.format((bwLink / 1024) / pointBusiness) + " Mbps");
		map.put("delay", pingAvgDelayCnt == 0 ? "N/A" : df0.format(pingAvgDelay / pingAvgDelayCnt) + " ms");
		map.put("lose", pingLoseRateCnt == 0 ? "N/A" : df2.format(pingLoseRate / pingLoseRateCnt) + " %");
		// 网络掉线事件、4G切3G事件、业务掉线事件信息
		networkCount = arrNet[1] + arrNet[3];
		pingCount = arrPing[1];
		lteToWcdmaCount = arrNet[5];
		double totalNetwork = 0, totalPing = 0;
		for (int i = 0; i < arrNet.length; i++) {
			totalNetwork += arrNet[i];
		}
		for (int i = 0; i < arrPing.length; i++) {
			totalPing += arrPing[i];
		}
		map.put("networkCount", totalNetwork == 0 ? "N/A" : networkCount);
		map.put("pingCount", totalPing == 0 ? "N/A" : pingCount);
		map.put("lteToWcdmaCount", totalNetwork == 0 ? "N/A" : arrNet[5]);
		map.put("networkRate", totalNetwork == 0 ? "N/A" : df2.format((double) networkCount / totalNetwork * 100) + " %");
		map.put("pingRate", totalPing == 0 ? "N/A" : df2.format((double) pingCount / totalPing * 100) + " %");
		map.put("lteToWcdmaRate", totalNetwork == 0 ? "N/A" : df2.format((double) lteToWcdmaCount / totalNetwork * 100) + " %");
		map.put("rs", 1);
	}

	protected String convertNetworkEvent(String event, int type) {
		String re = "-";
		if (type == 1) {
			switch (event) {
			case "0":
				re = "无事件";
				break;
			case "1":
				re = "4G网络掉线";
				break;
			case "2":
				re = "4G网络连接";
				break;
			case "3":
				re = "3G网络掉线";
				break;
			case "4":
				re = "3G网络连接";
				break;
			case "5":
				re = "4G下切3G";
				break;
			case "6":
				re = "3G上切4G";
				break;
			default:
				re = "N/A";
				break;
			}
		} else if (type == 2) {
			switch (event) {
			case "0":
				re = "无掉线";
				break;
			case "1":
				re = "掉线";
				break;
			default:
				re = "N/A";
				break;
			}
		}

		return re;
	}
}
