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
import java.util.Collections;
import java.util.Comparator;
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
@Service("playBackMapDaoES")
public class PlayBackMapDaoES extends MapDaoES {
	private static Logger log = LoggerFactory.getLogger(PlayBackMapDaoES.class);
	private VoBean voBean;

	/**
	 * 
	 * 功能说明: 查询采样点具体信息 修改者名字: dsk 修改日期 2015年12月9日 修改内容
	 * 
	 * @参数： @param map
	 * @参数： @param kpiSettings
	 * @参数： @return
	 * @throws
	 */
	public Map<String, Object> getPoint(Map<String, Object> map, List<KpiSettings> kpiSettings) throws Exception {

		voBean = (VoBean) map.get("vo");
		List<Points> listPoints = new ArrayList<Points>(); // 指标
		List<Points> warnPoints = new ArrayList<Points>(); // 事件
		List<String> xDate = new ArrayList<String>(); // 时间
		List<String> rsrpData = new ArrayList<String>(); // rsrp
		List<String> snrData = new ArrayList<String>(); // snr
		List<String> linkData = new ArrayList<String>(); // bw_link
		List<String> pingData = new ArrayList<String>(); // ping_delay
		List<String> pciData = new ArrayList<String>(); // pci

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
		int index_num = 0;
		String pci_last = null;
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
					ptsKpi.setTime(StringUtils.findDate(mapHits.get("test_time").getValue() + ""));
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
					// 区间颜色根据事件配置,重新赋值
					ptsEvent.setLat(ptsKpi.getLat());
					ptsEvent.setLng(ptsKpi.getLng());
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

		// 按照时间升序排序
		Collections.sort(listPoints, new Comparator<Points>() {
			@Override
			public int compare(Points p1, Points p2) {
				return p1.getTime().compareTo(p2.getTime());
			}
		});

		// 折线图数据
		for (Points p : listPoints) {
//			// 判断没有值给"-",PCI跟前一个值相同给"-"
//			if (null == p.getPci() || p.getPci().equals("-999") || p.getPci().endsWith("-998")) {
//				p.setPci("-");
//			}
//			if (index_num == 0) {
//				pciData.add(p.getPci());
//			} else {
//				if (p.getPci().equals(pci_last)) {
//					pciData.add("-");
//				} else {
//					pciData.add(p.getPci());
//				}
//			}
//			pci_last = p.getPci();
			pciData.add(p.getPci());
			xDate.add(p.getTime().split(" ")[1]);
			// 无覆改
			if (null == p.getInter() || p.getInter().equals("7")) {
				rsrpData.add("-");
				snrData.add("-");
				linkData.add("-");
				pingData.add("-");
			} else {
				String rsr_p = p.getRsrp().split(" ")[0];
				rsrpData.add(rsr_p);
				String sn_r = p.getSnr().split(" ")[0];
				snrData.add(sn_r);
				String lin_k = p.getLink().split(" ")[0];
				linkData.add(lin_k == "N/A" ? "-" : lin_k);
				String pin_g = p.getDelay().split(" ")[0];
				pingData.add(pin_g == "N/A" ? "-" : pin_g);
			}
			index_num++;
		}

		map.put("points", listPoints);
		map.put("warnPoints", warnPoints);
		map.put("xrotate", xDate);
		map.put("pci", pciData);
		map.put("rsrp", rsrpData);
		map.put("snr", snrData);
		map.put("link", linkData);
		map.put("ping", pingData);
		return map;
	}
}
