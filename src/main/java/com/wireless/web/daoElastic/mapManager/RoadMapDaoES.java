/**     
 * @文件名称: RaodMapDaoES.java  
 * @类路径: com.wireless.web.daoElastic.mapManager  
 * @描述: 地图道路点查询  
 * @作者：dsk  
 * @时间：2015年12月2日 上午10:37:20  
 * @版本：V1.0     
 */
package com.wireless.web.daoElastic.mapManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.InternalFilter;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Order;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.wireless.web.daoElastic.utils.ESConnUtils;
import com.wireless.web.daoElastic.utils.QueryUtils;
import com.wireless.web.daoElastic.utils.StringUtils;
import com.wireless.web.model.KpiSettings;
import com.wireless.web.model.Pot;
import com.wireless.web.model.RoadPoints;
import com.wireless.web.model.VoBean;
import com.wireless.web.utils.NumUtils;

/**
 * @类功能说明：
 * @类修改者：
 * @修改日期：
 * @修改说明：
 * @公司名称：
 * @作者：dsk
 * @创建时间：2015年12月2日 上午10:37:20
 * @版本：V1.0
 */
@Service("roadMapDaoES")
public class RoadMapDaoES {
	private static Logger log = LoggerFactory.getLogger(RoadMapDaoES.class);
	private TransportClient tc;

	/**
	 * 功能说明: 查询道路点指标区间,颜色,以及经纬度 修改者名字: dsk 修改日期 2015年12月9日 修改内容
	 * 
	 * @参数： @param vv
	 * @参数： @param kpiSettings
	 * @参数： @param roadPots
	 * @参数： @return
	 * @throws
	 */
	public Map<String, Object> getPot(Map<String, Object> map, List<KpiSettings> kpiSettings, List<Pot> roadPots) throws Exception {
		VoBean vv = (VoBean) map.get("vo");
		List<Pot> listPotKpi = new ArrayList<Pot>(); // 指标
		List<Pot> listPotEvent = new ArrayList<Pot>(); // 事件

		// 事件: -1,无事件,9网络掉线,10业务掉线,114G切换3G
		String voWarn = (null != vv.getWarn() && !"".equals(vv.getWarn())) ? vv.getWarn() : "-1";
		Integer qryKpi = vv.getKpiType();
		String qryKpiName = StringUtils.getKpiName(qryKpi);
		// 指标区间配置
		KpiSettings kpiSet = QueryUtils.getKpiSetByList(kpiSettings, qryKpi);
		// 事件区间配置
		KpiSettings eventSet = voWarn.equals("-1") ? null : QueryUtils.getKpiSetByList(kpiSettings, Integer.parseInt(voWarn));

		tc = ESConnUtils.getTransportClient();
		BoolQueryBuilder boolQuery = QueryUtils.qryMapCondition(vv, 2);
		// 排除道路点为空
		boolQuery.mustNot(new TermsQueryBuilder("point_code", ""));

		FilterAggregationBuilder aggregation = AggregationBuilders.filter("pointCode").filter(boolQuery);

		TermsBuilder pcTermsBuilder = AggregationBuilders.terms("pointCodeAgg").field("point_code").size(Integer.MAX_VALUE);
		TermsBuilder telTermsBuilder = AggregationBuilders.terms("telecoms").field("telecoms");
		telTermsBuilder.subAggregation(AggregationBuilders.max("lat").field("bd_latitude"));
		telTermsBuilder.subAggregation(AggregationBuilders.max("lng").field("bd_longitude"));
		telTermsBuilder.subAggregation(AggregationBuilders.avg(qryKpiName).field(qryKpiName));

		if (qryKpi == 1 || qryKpi == 2) {
			telTermsBuilder.subAggregation(AggregationBuilders.sum("non_cover_cnt").script(new Script("if(doc['amend_mark'].value==3){return 1;}else{return 0;}")));
		}
		// 无事件
		if (!voWarn.equals("-1")) {
			telTermsBuilder.subAggregation(AggregationBuilders.sum("networkCount").script(new Script("if(doc['network_event'].value==3||doc['network_event'].value==1){return 1;}else{return 0;}")));
			telTermsBuilder.subAggregation(AggregationBuilders.sum("pingCount").script(new Script("if(doc['ping_event'].value==1){return 1;}else{return 0;}")));
			telTermsBuilder.subAggregation(AggregationBuilders.sum("lteToWcdmaCount").script(new Script("if(doc['network_event'].value==5){return 1;}else{return 0;}")));
			telTermsBuilder.subAggregation(AggregationBuilders.sum("networkTotal").script(new Script("if(doc['network_event'].value>=0){return 1;}else{return 0;}")));
			telTermsBuilder.subAggregation(AggregationBuilders.sum("pingTotal").script(new Script("if(doc['ping_event'].value>=0){return 1;}else{return 0;}")));
		}
		pcTermsBuilder.subAggregation(telTermsBuilder);
		aggregation.subAggregation(pcTermsBuilder);

		// index,type
		String queryTime = vv.getStarttime().replace("-", ".").trim() + "-" + vv.getEndtime().replace("-", ".").trim();
		List<String> searchList = StringUtils.findIndex4ES(queryTime, vv.getProvince());

		String[] docs = new String[searchList.size()];
		for (int i = 0; i < searchList.size(); i++) {
			docs[i] = searchList.get(i);
		}
		SearchRequestBuilder srb = tc.prepareSearch(docs);
		srb.setFrom(0).setSize(0);
		srb.addAggregation(aggregation);

		SearchResponse scrollResp = srb.execute().actionGet();
		// json
		log.debug(srb + "");
		log.debug("TookInMillis: " + scrollResp.getTookInMillis());
		Map<String, Aggregation> aggMap = scrollResp.getAggregations().getAsMap();
		InternalFilter itlFilter = (InternalFilter) aggMap.get("pointCode");
		Map<String, Aggregation> aggMap1 = itlFilter.getAggregations().asMap();
		StringTerms pcTerms = (StringTerms) aggMap1.get("pointCodeAgg");

		Iterator<Bucket> pcBucketIt = pcTerms.getBuckets().iterator();
		Max latMax, lngMax;
		Avg kpiAvg;
		Sum nonCover, networkCount, pingCount, lteToWcdmaCount;
		Sum networkTotal, pingTotal;
		Double kpiValue = null;
		String kpiItv = "0";
		String kpiItvClr = "";
		Double eventRate;// 事件阈值率
		boolean eventFlag = true;// 事件标示
		String eventHit = "";// 事件提示信息
		while (pcBucketIt.hasNext()) {
			Bucket pcBucket = pcBucketIt.next();

			Map<String, Aggregation> aggMap2 = pcBucket.getAggregations().asMap();
			StringTerms telTerms = (StringTerms) aggMap2.get("telecoms");
			Iterator<Bucket> telBucketIt = telTerms.getBuckets().iterator();

			while (telBucketIt.hasNext()) {
				Bucket telBucket = telBucketIt.next();

				latMax = telBucket.getAggregations().get("lat");
				lngMax = telBucket.getAggregations().get("lng");
				kpiAvg = telBucket.getAggregations().get(qryKpiName);
				kpiValue = kpiAvg.getValue();
				// bw_link
				kpiValue = qryKpi == 4 ? kpiValue / 1024 : kpiValue;
				kpiItv = QueryUtils.kpiInterval(kpiSet, kpiValue);

				// 点集合经纬度为空,指标区间未匹配
				if (!NumUtils.isNumber(latMax.getValue()) || kpiItv == "0") {
					continue;
				}

				Pot potKpi = new Pot();
				Pot potEvent = new Pot();
				potKpi.setId(pcBucket.getKey() + "");
				potKpi.setLat(latMax.getValue());
				potKpi.setLng(lngMax.getValue());
				potKpi.setTelecoms(telBucket.getKey() + "");
				kpiItvClr = QueryUtils.kpiIntervalColor(kpiSet, kpiItv);
				potKpi.setState(kpiItvClr);
				potKpi.setInter(kpiItv);

				// 无覆盖判断
				if (qryKpi == 1 || qryKpi == 2) {
					nonCover = telBucket.getAggregations().get("non_cover_cnt");
					double abc = nonCover.getValue() / telBucket.getDocCount();
					if (abc >= 0.7) {
						potKpi.setState("#000000");
						potKpi.setInter("7");
					}
				}
				listPotKpi.add(potKpi);
				// 无事件
				if (!voWarn.equals("-1")) {
					eventRate = null;
					eventFlag = true;
					networkCount = telBucket.getAggregations().get("networkCount");
					pingCount = telBucket.getAggregations().get("pingCount");
					lteToWcdmaCount = telBucket.getAggregations().get("lteToWcdmaCount");
					networkTotal = telBucket.getAggregations().get("networkTotal");
					pingTotal = telBucket.getAggregations().get("pingTotal");

					if (voWarn.equals("9")) {// 网络掉线
						eventRate = (networkTotal.getValue() == 0) ? -1 : networkCount.getValue() / networkTotal.getValue() * 100;
						eventHit = "网络掉线率:" + NumUtils.resNumber(eventRate, "0.00") + "%,掉线次数:" + NumUtils.resNumber(networkCount.getValue(), "0");
					} else if (voWarn.equals("10")) {// 业务掉线
						eventRate = (pingTotal.getValue() == 0) ? -1 : pingCount.getValue() / pingTotal.getValue() * 100;
						eventHit = "业务掉线率:" + NumUtils.resNumber(eventRate, "0.00") + "%,掉线次数:" + NumUtils.resNumber(pingCount.getValue(), "0");
					} else if (voWarn.equals("11")) {// 4G切3G
						eventRate = (networkTotal.getValue() == 0) ? -1 : lteToWcdmaCount.getValue() / networkTotal.getValue() * 100;
						eventHit = "4G切3G次数:" + NumUtils.resNumber(lteToWcdmaCount.getValue(), "0");
					}

					if (vv.getWarnValue() != null && !vv.getWarnValue().equals("") && eventRate != null && eventRate != null && eventRate != -1) {// 阈值不为空
						// 查询符号:1.> 2.>= 3.= 4.< 5.<=
						switch (vv.getWarnSymbol()) {
						case "1":
							eventFlag = eventRate > Double.parseDouble(vv.getWarnValue()) ? true : false;
							break;
						case "2":
							eventFlag = eventRate >= Double.parseDouble(vv.getWarnValue()) ? true : false;
							break;
						case "3":
							eventFlag = eventRate == Double.parseDouble(vv.getWarnValue()) ? true : false;
							break;
						case "4":
							eventFlag = eventRate < Double.parseDouble(vv.getWarnValue()) ? true : false;
							break;
						case "5":
							eventFlag = eventRate <= Double.parseDouble(vv.getWarnValue()) ? true : false;
							break;
						default:
							break;
						}
					} else {
						// 阈值为空,事件点不为空(即:当阈值为空时,仅显示有事件的道路点)
						eventFlag = (eventRate == -1) ? false : true;
					}
					// 事件点满足条件
					if (eventFlag) {
						potEvent.setIsWarn("1");
						potEvent.setLat(potKpi.getLat());
						potEvent.setLng(potKpi.getLng());
						potEvent.setTelecoms(potKpi.getTelecoms());
						// 4G切换3G 固定颜色(红色)
						if (voWarn.equals("11")) {
							potEvent.setState("#FF0000");
							potEvent.setInter("6");
						} else if (!voWarn.equals("-1")) {
							String eventInter = QueryUtils.kpiInterval(eventSet, eventRate);
							potEvent.setInter(eventInter);
							potEvent.setState(QueryUtils.kpiIntervalColor(eventSet, eventInter));
						}
						potEvent.setDicWarn(eventHit);
						// 添加到列表中
						listPotEvent.add(potEvent);
					}
				}

			}
		}
		// 补轨迹点
		if (null != vv.getLevel() && vv.getLevel() == 1) {
			String strTel = "";
			if (null == vv.getDubiOperator() || vv.getDubiOperator() == "") {
				strTel = vv.getOperator() + "";
			} else {
				strTel = vv.getDubiOperator();
			}
			String tels[] = strTel.split(",");
			List<Pot> tempList = listPotKpi;
			for (int i = 0; i < tels.length; i++) {
				String duibi = tels[i];

				for (Pot pot1 : roadPots) {
					int flag = 0;
					for (Pot pot2 : tempList) {
						if (duibi.equals(pot2.getTelecoms()) && pot1.getId().equals(pot2.getId())) {
							flag = 1;
							break;
						}
					}
					if (flag == 0) {
						Pot pot = new Pot();
						pot.setInter("8");
						pot.setState("#cccccc");
						pot.setTelecoms(duibi);
						pot.setId(pot1.getId());
						pot.setLat(pot1.getLat());
						pot.setLng(pot1.getLng());
						listPotKpi.add(pot);
					}
				}
			}
		}
		map.put("points", listPotKpi);
		map.put("warnPoints", listPotEvent);
		return map;
	}

	/**
	 * 功能说明: 获取道路点具体指标 修改者名字: dsk 修改日期 2015年12月9日 修改内容
	 * 
	 * @参数： @param vv
	 * @参数： @return
	 * @throws
	 */
	public List<RoadPoints> getPoint(VoBean vv) throws Exception {
		tc = ESConnUtils.getTransportClient();
		BoolQueryBuilder boolQuery = QueryUtils.qryMapCondition(vv, 2);
		List<RoadPoints> listRoadPoints = new ArrayList<RoadPoints>();
		// 道路点
		boolQuery.must(new TermsQueryBuilder("point_code", vv.getId()));

		FilterAggregationBuilder aggregation = AggregationBuilders.filter("pointCode").filter(boolQuery);

		TermsBuilder pcTermsBuilder = AggregationBuilders.terms("pointCodeAgg").field("point_code").size(Integer.MAX_VALUE);
		pcTermsBuilder.subAggregation(AggregationBuilders.terms("roadId").field("road_id"));
		pcTermsBuilder.subAggregation(AggregationBuilders.terms("roadName").field("road_name"));
		pcTermsBuilder.subAggregation(AggregationBuilders.sum("cover_cnt").script(new Script("if(doc['lte_rsrp'].value>=-105 && doc['lte_snr'].value>=-3){return 1;}else{return 0;}")));
		pcTermsBuilder.subAggregation(AggregationBuilders.sum("non_cover_cnt").script(new Script("if(doc['amend_mark'].value==3){return 1;}else{return 0;}")));
		pcTermsBuilder.subAggregation(AggregationBuilders.sum("point_business").script(new Script("if(doc['bw_link'].value!=-998){return 1;}else{return 0;}")));
		pcTermsBuilder.subAggregation(AggregationBuilders.avg("lte_rsrp").field("lte_rsrp"));
		pcTermsBuilder.subAggregation(AggregationBuilders.avg("lte_rsrq").field("lte_rsrq"));
		pcTermsBuilder.subAggregation(AggregationBuilders.avg("lte_snr").field("lte_snr"));
		pcTermsBuilder.subAggregation(AggregationBuilders.avg("bw_link").script(new Script("if(doc['bw_link'].value!=-998){return doc['bw_link'].value;}")));
		pcTermsBuilder.subAggregation(AggregationBuilders.avg("ping_avg_delay").script(new Script("if(doc['ping_avg_delay'].value!=-998){return doc['ping_avg_delay'].value;}")));
		pcTermsBuilder.subAggregation(AggregationBuilders.avg("ping_lose_rate").script(new Script("if(doc['ping_lose_rate'].value!=-998){return doc['ping_lose_rate'].value;}")));
		pcTermsBuilder.subAggregation(AggregationBuilders.terms("ciCnt").field("lte_ci").order(Order.count(false)));
		pcTermsBuilder.subAggregation(AggregationBuilders.terms("pciCnt").field("pci").order(Order.count(false)));
		pcTermsBuilder.subAggregation(AggregationBuilders.terms("tacCnt").field("tac").order(Order.count(false)));
		pcTermsBuilder.subAggregation(AggregationBuilders.terms("enodeBidCnt").field("enode_bid").order(Order.count(false)));
		pcTermsBuilder.subAggregation(AggregationBuilders.terms("cellCnt").field("cell").order(Order.count(false)));
		// 事件
		pcTermsBuilder.subAggregation(AggregationBuilders.sum("networkCount").script(new Script("if(doc['network_event'].value==3||doc['network_event'].value==1){return 1;}else{return 0;}")));
		pcTermsBuilder.subAggregation(AggregationBuilders.sum("pingCount").script(new Script("if(doc['ping_event'].value==1){return 1;}else{return 0;}")));
		pcTermsBuilder.subAggregation(AggregationBuilders.sum("lteToWcdmaCount").script(new Script("if(doc['network_event'].value==5){return 1;}else{return 0;}")));
		pcTermsBuilder.subAggregation(AggregationBuilders.sum("networkTotal").script(new Script("if(doc['network_event'].value>=0){return 1;}else{return 0;}")));
		pcTermsBuilder.subAggregation(AggregationBuilders.sum("pingTotal").script(new Script("if(doc['ping_event'].value>=0){return 1;}else{return 0;}")));

		aggregation.subAggregation(pcTermsBuilder);

		// index,type
		String queryTime = vv.getStarttime().replace("-", ".").trim() + "-" + vv.getEndtime().replace("-", ".").trim();
		List<String> searchList = StringUtils.findIndex4ES(queryTime, vv.getProvince());

		String[] docs = new String[searchList.size()];
		for (int i = 0; i < searchList.size(); i++) {
			docs[i] = searchList.get(i);
		}
		SearchRequestBuilder srb = tc.prepareSearch(docs);

		srb.setFrom(0).setSize(0);
		srb.addAggregation(aggregation);

		SearchResponse scrollResp = srb.execute().actionGet();

		// json
		log.debug(srb + "");
		log.debug("TookInMillis: " + scrollResp.getTookInMillis());

		Map<String, Aggregation> aggMap = scrollResp.getAggregations().getAsMap();
		InternalFilter itlFilter = (InternalFilter) aggMap.get("pointCode");
		Map<String, Aggregation> aggMap1 = itlFilter.getAggregations().asMap();
		StringTerms pcTerms = (StringTerms) aggMap1.get("pointCodeAgg");

		Iterator<Bucket> pcBucketIt = pcTerms.getBuckets().iterator();
		int i, j;
		double point_signal;
		double topRate;// topN百分比
		double cover_cnt, point_business, lte_rsrp, lte_rsrq, lte_snr, bw_link, ping_avg_delay, ping_lose_rate;
		Sum networkCount, pingCount, lteToWcdmaCount, networkTotal, pingTotal;
		while (pcBucketIt.hasNext()) {
			RoadPoints rp = new RoadPoints();
			initRoadPoints(rp);
			Bucket pcBucket = pcBucketIt.next();
			point_signal = pcBucket.getDocCount();

			cover_cnt = ((Sum) pcBucket.getAggregations().get("cover_cnt")).getValue();
			point_business = ((Sum) pcBucket.getAggregations().get("point_business")).getValue();
			lte_rsrp = ((Avg) pcBucket.getAggregations().get("lte_rsrp")).getValue();
			lte_rsrq = ((Avg) pcBucket.getAggregations().get("lte_rsrq")).getValue();
			lte_snr = ((Avg) pcBucket.getAggregations().get("lte_snr")).getValue();
			bw_link = ((Avg) pcBucket.getAggregations().get("bw_link")).getValue();
			ping_avg_delay = ((Avg) pcBucket.getAggregations().get("ping_avg_delay")).getValue();
			ping_lose_rate = ((Avg) pcBucket.getAggregations().get("ping_lose_rate")).getValue();
			// 事件
			networkCount = pcBucket.getAggregations().get("networkCount");
			pingCount = pcBucket.getAggregations().get("pingCount");
			lteToWcdmaCount = pcBucket.getAggregations().get("lteToWcdmaCount");
			networkTotal = pcBucket.getAggregations().get("networkTotal");
			pingTotal = pcBucket.getAggregations().get("pingTotal");
			rp.setNetworkCount(networkTotal.getValue() < 1 ? "N/A" : NumUtils.resNumber(networkCount.getValue(), "0"));
			rp.setPingCount(pingTotal.getValue() < 1 ? "N/A" : NumUtils.resNumber(pingCount.getValue(), "0"));
			rp.setLteToWcdmaCount(networkTotal.getValue() < 1 ? "N/A" : NumUtils.resNumber(lteToWcdmaCount.getValue(), "0"));
			rp.setNetworkRate(networkTotal.getValue() < 1 ? "N/A" : NumUtils.resNumber(networkCount.getValue() / networkTotal.getValue() * 100, "0.00") + " %");
			rp.setPingRate(pingTotal.getValue() < 1 ? "N/A" : NumUtils.resNumber(pingCount.getValue() / pingTotal.getValue() * 100, "0.00") + " %");

			rp.setCover(NumUtils.resNumber(cover_cnt / point_signal * 100, "0.00") + " %");
			rp.setCount(NumUtils.resNumber(point_signal, "0") + "");
			rp.setCon(NumUtils.resNumber(point_business, "0") + "");
			rp.setRsrp(NumUtils.resNumber(lte_rsrp, "0.00") + " dBm");
			rp.setRsrq(NumUtils.resNumber(lte_rsrq, "0") + " dB");
			rp.setSnr(NumUtils.resNumber(lte_snr, "0.00") + " dB");
			rp.setLink(NumUtils.isNumber(bw_link) ? NumUtils.resNumber(bw_link / 1024, "0.00") + " Mbps" : "N/A");
			rp.setDelay(NumUtils.isNumber(ping_avg_delay) ? NumUtils.resNumber(ping_avg_delay, "0") + " ms" : "N/A");
			rp.setLose(NumUtils.isNumber(ping_lose_rate) ? NumUtils.resNumber(ping_lose_rate, "0.00") + " %" : "N/A");

			// road_id
			StringTerms ltRoadId = pcBucket.getAggregations().get("roadId");
			Iterator<Bucket> itRoadId = ltRoadId.getBuckets().iterator();
			if (itRoadId.hasNext()) {
				rp.setRoad_id(itRoadId.next().getKey() + "");
			}
			// road_name
			StringTerms ltRoadName = pcBucket.getAggregations().get("roadName");
			Iterator<Bucket> itRoadName = ltRoadName.getBuckets().iterator();
			if (itRoadName.hasNext()) {
				rp.setRoad_name(itRoadName.next().getKey() + "");
			}
			// topN
			// ci
			LongTerms ltCi = pcBucket.getAggregations().get("ciCnt");
			Iterator<Bucket> itCi = ltCi.getBuckets().iterator();
			i = 0;
			while (itCi.hasNext()) {
				++i;
				Bucket bucket = itCi.next();
				topRate = bucket.getDocCount() / point_signal * 100;
				if (topRate > 5) {
					switch (i) {
					case 1:
						rp.setCi1(bucket.getKey() + "");
						rp.setCiRate1(NumUtils.resNumber(topRate, "0.00") + "%");
						break;
					case 2:
						rp.setCi2(bucket.getKey() + "");
						rp.setCiRate2(NumUtils.resNumber(topRate, "0.00") + "%");
						break;
					case 3:
						rp.setCi3(bucket.getKey() + "");
						rp.setCiRate3(NumUtils.resNumber(topRate, "0.00") + "%");
						break;
					case 4:
						rp.setCi4(bucket.getKey() + "");
						rp.setCiRate4(NumUtils.resNumber(topRate, "0.00") + "%");
						break;
					case 5:
						rp.setCi5(bucket.getKey() + "");
						rp.setCiRate5(NumUtils.resNumber(topRate, "0.00") + "%");
						break;
					default:
						break;
					}
				}
			}
			// pci
			LongTerms ltPci = pcBucket.getAggregations().get("pciCnt");
			Iterator<Bucket> itPci = ltPci.getBuckets().iterator();
			i = 0;
			j = 0;
			while (itPci.hasNext()) {
				++i;
				Bucket bucket = itPci.next();
				topRate = bucket.getDocCount() / point_signal * 100;
				if (topRate > 5) {
					++j;
					switch (i) {
					case 1:
						rp.setPci1(bucket.getKey() + "");
						rp.setPciRate1(NumUtils.resNumber(topRate, "0.00") + "%");
						break;
					case 2:
						rp.setPci2(bucket.getKey() + "");
						rp.setPciRate2(NumUtils.resNumber(topRate, "0.00") + "%");
						break;
					case 3:
						rp.setPci3(bucket.getKey() + "");
						rp.setPciRate3(NumUtils.resNumber(topRate, "0.00") + "%");
						break;
					case 4:
						rp.setPci4(bucket.getKey() + "");
						rp.setPciRate4(NumUtils.resNumber(topRate, "0.00") + "%");
						break;
					case 5:
						rp.setPci5(bucket.getKey() + "");
						rp.setPciRate5(NumUtils.resNumber(topRate, "0.00") + "%");
						break;
					default:
						break;
					}
					rp.setPciCnt(j + "");
				}
			}
			// tac
			LongTerms ltTac = pcBucket.getAggregations().get("tacCnt");
			Iterator<Bucket> itTac = ltTac.getBuckets().iterator();
			i = 0;
			j = 0;
			while (itTac.hasNext()) {
				++i;
				Bucket bucket = itTac.next();
				topRate = bucket.getDocCount() / point_signal * 100;
				if (topRate > 5) {
					++j;
					switch (i) {
					case 1:
						rp.setTac1(bucket.getKey() + "");
						rp.setTacRate1(NumUtils.resNumber(topRate, "0.00") + "%");
						break;
					case 2:
						rp.setTac2(bucket.getKey() + "");
						rp.setTacRate2(NumUtils.resNumber(topRate, "0.00") + "%");
						break;
					case 3:
						rp.setTac3(bucket.getKey() + "");
						rp.setTacRate3(NumUtils.resNumber(topRate, "0.00") + "%");
						break;
					case 4:
						rp.setTac4(bucket.getKey() + "");
						rp.setTacRate4(NumUtils.resNumber(topRate, "0.00") + "%");
						break;
					case 5:
						rp.setTac5(bucket.getKey() + "");
						rp.setTacRate5(NumUtils.resNumber(topRate, "0.00") + "%");
						break;
					default:
						break;
					}
					rp.setTacCnt(j + "");
				}
			}
			// bid
			LongTerms ltBid = pcBucket.getAggregations().get("enodeBidCnt");
			Iterator<Bucket> itBid = ltBid.getBuckets().iterator();
			i = 0;
			while (itBid.hasNext()) {
				++i;
				Bucket bucket = itBid.next();
				topRate = bucket.getDocCount() / point_signal * 100;
				if (topRate > 5) {
					switch (i) {
					case 1:
						rp.setBid1(bucket.getKey() + "");
						rp.setBidRate1(NumUtils.resNumber(topRate, "0.00") + "%");
						break;
					default:
						break;
					}
				}
			}
			// cell
			StringTerms ltCell = pcBucket.getAggregations().get("cellCnt");
			Iterator<Bucket> itCell = ltCell.getBuckets().iterator();
			j = 0;
			while (itCell.hasNext()) {
				Bucket bucket = itCell.next();
				topRate = bucket.getDocCount() / point_signal * 100;
				if (topRate > 5) {
					++j;
					rp.setCellCnt(j + "");
				}
			}
			//
			listRoadPoints.add(rp);
		}
		return listRoadPoints;
	}

	/**
	 * 
	 * 功能说明: 框选计算平均值 修改者名字: dsk 修改日期 2015年12月9日 修改内容
	 * 
	 * @参数： @param map
	 * @参数： @param vv
	 * @参数： @param kpiSettings
	 * @throws
	 */
	public void getPointAvg(Map<String, Object> map, VoBean vv, KpiSettings kpiSettings) throws Exception {
		Integer qryKpi = vv.getKpiType();
		String qryKpiName = StringUtils.getKpiName(qryKpi);
		String qryKpiInterv = vv.getKpiInterv();
		tc = ESConnUtils.getTransportClient();
		BoolQueryBuilder boolQuery = QueryUtils.qryMapCondition(vv, 2);
		// 排除道路点为空
		boolQuery.mustNot(new TermsQueryBuilder("point_code", ""));

		FilterAggregationBuilder aggregation = AggregationBuilders.filter("all").filter(boolQuery);

		TermsBuilder pcTermsBuilder = AggregationBuilders.terms("allAgg").field("point_code").size(Integer.MAX_VALUE);
		pcTermsBuilder.subAggregation(AggregationBuilders.sum("point_business").script(new Script("if(doc['bw_link'].value!=-998){return 1;}else{return 0;}")));
		pcTermsBuilder.subAggregation(AggregationBuilders.avg("lte_rsrp").field("lte_rsrp"));
		pcTermsBuilder.subAggregation(AggregationBuilders.avg("lte_rsrq").field("lte_rsrq"));
		pcTermsBuilder.subAggregation(AggregationBuilders.avg("lte_snr").field("lte_snr"));
		pcTermsBuilder.subAggregation(AggregationBuilders.avg("bw_link").script(new Script("if(doc['bw_link'].value!=-998){return doc['bw_link'].value;}")));
		pcTermsBuilder.subAggregation(AggregationBuilders.avg("ping_avg_delay").script(new Script("if(doc['ping_avg_delay'].value!=-998){return doc['ping_avg_delay'].value;}")));
		pcTermsBuilder.subAggregation(AggregationBuilders.avg("ping_lose_rate").script(new Script("if(doc['ping_lose_rate'].value!=-998){return doc['ping_lose_rate'].value;}")));
		// 事件
		pcTermsBuilder.subAggregation(AggregationBuilders.sum("networkCount").script(new Script("if(doc['network_event'].value==3||doc['network_event'].value==1){return 1;}else{return 0;}")));
		pcTermsBuilder.subAggregation(AggregationBuilders.sum("pingCount").script(new Script("if(doc['ping_event'].value==1){return 1;}else{return 0;}")));
		pcTermsBuilder.subAggregation(AggregationBuilders.sum("lteToWcdmaCount").script(new Script("if(doc['network_event'].value==5){return 1;}else{return 0;}")));
		pcTermsBuilder.subAggregation(AggregationBuilders.sum("networkTotal").script(new Script("if(doc['network_event'].value>=0){return 1;}else{return 0;}")));
		pcTermsBuilder.subAggregation(AggregationBuilders.sum("pingTotal").script(new Script("if(doc['ping_event'].value>=0){return 1;}else{return 0;}")));

		aggregation.subAggregation(pcTermsBuilder);

		// index,type
		String queryTime = vv.getStarttime().replace("-", ".").trim() + "-" + vv.getEndtime().replace("-", ".").trim();
		List<String> searchList = StringUtils.findIndex4ES(queryTime, vv.getProvince());

		String[] docs = new String[searchList.size()];
		for (int i = 0; i < searchList.size(); i++) {
			docs[i] = searchList.get(i);
		}
		SearchRequestBuilder srb = tc.prepareSearch(docs);

		srb.setFrom(0).setSize(0);
		srb.addAggregation(aggregation);

		SearchResponse scrollResp = srb.execute().actionGet();
		// json
		log.debug(srb + "");
		log.debug("TookInMillis: " + scrollResp.getTookInMillis());
		Map<String, Aggregation> aggMap = scrollResp.getAggregations().getAsMap();
		InternalFilter itlFilter = (InternalFilter) aggMap.get("all");
		Map<String, Aggregation> aggMap1 = itlFilter.getAggregations().asMap();
		StringTerms pcTerms = (StringTerms) aggMap1.get("allAgg");

		Iterator<Bucket> pcBucketIt = pcTerms.getBuckets().iterator();
		Double kpiValue = null;
		String kpiItv = "0";
		// 框选结果
		int pointSignal = 0;
		int pointBusiness = 0;
		double lteRsrp = 0, lteRsrq = 0, lteSnr = 0, bwLink = 0, pingAvgDelay = 0, pingLoseRate = 0;
		double pointCnt = 0, bussCnt = 0, networkCount = 0, pingCount = 0, lteToWcdmaCount = 0;
		double networkRate = 0, pingRate = 0;// 网络掉线率,业务掉线率
		double networkAll = 0, pingAll = 0;// 网络掉线率,业务掉线率
		Sum sumNetwork, sumPing, sumLteToWcdma, sumPB;
		Sum networkTotal, pingTotal;
		Avg avgQryKpi, avgRsrp, avgRsrq, avgSnr, avgLink, avgDelay, avgLose;
		while (pcBucketIt.hasNext()) {
			Bucket pcBucket = pcBucketIt.next();
			avgQryKpi = pcBucket.getAggregations().get(qryKpiName);
			kpiValue = avgQryKpi.getValue();
			kpiValue = qryKpi == 4 ? kpiValue / 1024 : kpiValue;
			if (!NumUtils.isNumber(kpiValue)) {
				continue;
			}
			kpiItv = QueryUtils.kpiInterval(kpiSettings, kpiValue);
			if (kpiItv != "0" && null != qryKpiInterv && qryKpiInterv.indexOf(kpiItv) != -1) {
				pointCnt++;
				sumPB = pcBucket.getAggregations().get("point_business");
				avgRsrp = pcBucket.getAggregations().get("lte_rsrp");
				avgRsrq = pcBucket.getAggregations().get("lte_rsrq");
				avgSnr = pcBucket.getAggregations().get("lte_snr");
				avgLink = pcBucket.getAggregations().get("bw_link");
				avgDelay = pcBucket.getAggregations().get("ping_avg_delay");
				avgLose = pcBucket.getAggregations().get("ping_lose_rate");
				// 事件次数
				sumNetwork = pcBucket.getAggregations().get("networkCount");
				sumPing = pcBucket.getAggregations().get("pingCount");
				sumLteToWcdma = pcBucket.getAggregations().get("lteToWcdmaCount");
				networkTotal = pcBucket.getAggregations().get("networkTotal");
				pingTotal = pcBucket.getAggregations().get("pingTotal");
				networkCount += sumNetwork.getValue();
				pingCount += sumPing.getValue();
				lteToWcdmaCount += sumLteToWcdma.getValue();
				networkAll += networkTotal.getValue();
				pingAll += pingTotal.getValue();
				networkRate += networkTotal.getValue() < 1 ? 0 : (sumNetwork.getValue() / networkTotal.getValue());
				pingRate += pingTotal.getValue() < 1 ? 0 : (sumPing.getValue() / pingTotal.getValue());
				// 指标值
				pointSignal += pcBucket.getDocCount();
				pointBusiness += sumPB.getValue();
				lteRsrp += avgRsrp.getValue();
				lteRsrq += avgRsrq.getValue();
				lteSnr += avgSnr.getValue();
				bussCnt = sumPB.getValue() != 0 ? bussCnt + 1 : bussCnt;
				bwLink = NumUtils.isNumber(avgLink.getValue()) ? bwLink + avgLink.getValue() : bwLink;
				pingAvgDelay = NumUtils.isNumber(avgDelay.getValue()) ? pingAvgDelay + avgDelay.getValue() : pingAvgDelay;
				pingLoseRate = NumUtils.isNumber(avgLose.getValue()) ? pingLoseRate + avgLose.getValue() : pingLoseRate;
			}
		}
		// 组装结果
		map.put("signalCount", pointSignal);
		map.put("busCount", pointBusiness);
		map.put("rsrp", pointCnt == 0 ? "N/A" : NumUtils.resNumber(lteRsrp / pointCnt, "0.00") + " dBm");
		map.put("rsrq", pointCnt == 0 ? "N/A" : NumUtils.resNumber(lteRsrq / pointCnt, "0") + " dB");
		map.put("snr", pointCnt == 0 ? "N/A" : NumUtils.resNumber(lteSnr / pointCnt, "0.00") + " dB");
		map.put("link", pointBusiness == 0 ? "N/A" : NumUtils.resNumber((bwLink / 1024) / bussCnt, "0.00") + " Mbps");
		map.put("delay", pointBusiness == 0 ? "N/A" : NumUtils.resNumber(pingAvgDelay / bussCnt, "0") + " ms");
		map.put("lose", pointBusiness == 0 ? "N/A" : NumUtils.resNumber(pingLoseRate / bussCnt, "0.00") + " %");
		// 事件信息
		map.put("networkCount", networkAll < 1 ? "N/A" : NumUtils.resNumber(networkCount, "0"));
		map.put("pingCount", pingAll < 1 ? "N/A" : NumUtils.resNumber(pingCount, "0"));
		map.put("lteToWcdmaCount", networkAll < 1 ? "N/A" : NumUtils.resNumber(lteToWcdmaCount, "0"));
		map.put("networkRate", networkAll < 1 ? "N/A" : NumUtils.resNumber(networkRate / pointCnt * 100, "0.00") + "%");
		map.put("pingRate", pingAll < 1 ? "N/A" : NumUtils.resNumber(pingRate / pointCnt * 100, "0.00") + "%");
		map.put("rs", 1);
	}

	/**
	 * 功能说明: 导出为Excel 修改者名字: dsk 修改日期 2015年12月9日 修改内容
	 * 
	 * @参数： @param vv
	 * @参数： @param kpiSettings
	 * @参数： @return
	 * @throws
	 */
	public List<Map<String, Object>> getPointExcel(VoBean vv, KpiSettings kpiSettings) throws Exception {
		Integer qryKpi = vv.getKpiType();
		String qryKpiName = StringUtils.getKpiName(qryKpi);
		String qryKpiInterv = vv.getKpiInterv();
		tc = ESConnUtils.getTransportClient();
		BoolQueryBuilder boolQuery = QueryUtils.qryMapCondition(vv, 2);
		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		// 道路点
		boolQuery.mustNot(new TermsQueryBuilder("point_code", ""));

		FilterAggregationBuilder aggregation = AggregationBuilders.filter("pointCode").filter(boolQuery);

		TermsBuilder pcTermsBuilder = AggregationBuilders.terms("pointCodeAgg").field("point_code").size(Integer.MAX_VALUE);
		pcTermsBuilder.subAggregation(AggregationBuilders.terms("roadId").field("road_id"));
		pcTermsBuilder.subAggregation(AggregationBuilders.terms("roadName").field("road_name"));
		pcTermsBuilder.subAggregation(AggregationBuilders.max("lat").field("bd_latitude"));
		pcTermsBuilder.subAggregation(AggregationBuilders.max("lng").field("bd_longitude"));
		pcTermsBuilder.subAggregation(AggregationBuilders.sum("cover_cnt").script(new Script("if(doc['lte_rsrp'].value>=-105 && doc['lte_snr'].value>=-3){return 1;}else{return 0;}")));
		pcTermsBuilder.subAggregation(AggregationBuilders.sum("non_cover_cnt").script(new Script("if(doc['amend_mark'].value==3){return 1;}else{return 0;}")));
		pcTermsBuilder.subAggregation(AggregationBuilders.sum("point_business").script(new Script("if(doc['bw_link'].value!=-998){return 1;}else{return 0;}")));
		pcTermsBuilder.subAggregation(AggregationBuilders.avg("lte_rsrp").field("lte_rsrp"));
		pcTermsBuilder.subAggregation(AggregationBuilders.avg("lte_rsrq").field("lte_rsrq"));
		pcTermsBuilder.subAggregation(AggregationBuilders.avg("lte_snr").field("lte_snr"));
		pcTermsBuilder.subAggregation(AggregationBuilders.avg("bw_link").script(new Script("if(doc['bw_link'].value!=-998){return doc['bw_link'].value;}")));
		pcTermsBuilder.subAggregation(AggregationBuilders.avg("ping_avg_delay").script(new Script("if(doc['ping_avg_delay'].value!=-998){return doc['ping_avg_delay'].value;}")));
		pcTermsBuilder.subAggregation(AggregationBuilders.avg("ping_lose_rate").script(new Script("if(doc['ping_lose_rate'].value!=-998){return doc['ping_lose_rate'].value;}")));
		// topN
		pcTermsBuilder.subAggregation(AggregationBuilders.terms("ciCnt").field("lte_ci").order(Order.count(false)));
		pcTermsBuilder.subAggregation(AggregationBuilders.terms("pciCnt").field("pci").order(Order.count(false)));
		pcTermsBuilder.subAggregation(AggregationBuilders.terms("tacCnt").field("tac").order(Order.count(false)));
		pcTermsBuilder.subAggregation(AggregationBuilders.terms("enodeBidCnt").field("enode_bid").order(Order.count(false)));
		pcTermsBuilder.subAggregation(AggregationBuilders.terms("cellCnt").field("cell").order(Order.count(false)));
		// 事件
		pcTermsBuilder.subAggregation(AggregationBuilders.sum("networkCount").script(new Script("if(doc['network_event'].value==3||doc['network_event'].value==1){return 1;}else{return 0;}")));
		pcTermsBuilder.subAggregation(AggregationBuilders.sum("pingCount").script(new Script("if(doc['ping_event'].value==1){return 1;}else{return 0;}")));
		pcTermsBuilder.subAggregation(AggregationBuilders.sum("lteToWcdmaCount").script(new Script("if(doc['network_event'].value==5){return 1;}else{return 0;}")));
		pcTermsBuilder.subAggregation(AggregationBuilders.sum("networkTotal").script(new Script("if(doc['network_event'].value>=0){return 1;}else{return 0;}")));
		pcTermsBuilder.subAggregation(AggregationBuilders.sum("pingTotal").script(new Script("if(doc['ping_event'].value>=0){return 1;}else{return 0;}")));

		aggregation.subAggregation(pcTermsBuilder);

		// index,type
		String queryTime = vv.getStarttime().replace("-", ".").trim() + "-" + vv.getEndtime().replace("-", ".").trim();
		List<String> searchList = StringUtils.findIndex4ES(queryTime, vv.getProvince());

		String[] docs = new String[searchList.size()];
		for (int i = 0; i < searchList.size(); i++) {
			docs[i] = searchList.get(i);
		}
		SearchRequestBuilder srb = tc.prepareSearch(docs);

		srb.setFrom(0).setSize(0);
		srb.addAggregation(aggregation);

		SearchResponse scrollResp = srb.execute().actionGet();

		// json
		log.debug(srb + "");
		log.debug("TookInMillis: " + scrollResp.getTookInMillis());

		Map<String, Aggregation> aggMap = scrollResp.getAggregations().getAsMap();
		InternalFilter itlFilter = (InternalFilter) aggMap.get("pointCode");
		Map<String, Aggregation> aggMap1 = itlFilter.getAggregations().asMap();
		StringTerms pcTerms = (StringTerms) aggMap1.get("pointCodeAgg");

		Iterator<Bucket> pcBucketIt = pcTerms.getBuckets().iterator();
		int i, j;
		double point_signal;
		Double kpiValue = null;
		String kpiItv = "0";
		Max latMax, lngMax;
		double cover_cnt, point_business, lte_rsrp, lte_rsrq, lte_snr, bw_link, ping_avg_delay, ping_lose_rate;
		Sum networkCount, pingCount, lteToWcdmaCount, networkTotal, pingTotal;
		while (pcBucketIt.hasNext()) {
			Bucket pcBucket = pcBucketIt.next();

			Avg avgQryKpi = (Avg) pcBucket.getAggregations().get(qryKpiName);
			kpiValue = avgQryKpi.getValue();
			kpiValue = qryKpi == 4 ? kpiValue / 1024 : kpiValue;
			if (!NumUtils.isNumber(kpiValue)) {
				continue;
			}
			kpiItv = QueryUtils.kpiInterval(kpiSettings, kpiValue);
			// 未在区间配置内或者非框选区间
			if (kpiItv == "0" || null == qryKpiInterv || qryKpiInterv.indexOf(kpiItv) == -1) {
				continue;
			}

			// 指标计算
			point_signal = pcBucket.getDocCount();
			cover_cnt = ((Sum) pcBucket.getAggregations().get("cover_cnt")).getValue();
			point_business = ((Sum) pcBucket.getAggregations().get("point_business")).getValue();
			lte_rsrp = ((Avg) pcBucket.getAggregations().get("lte_rsrp")).getValue();
			lte_rsrq = ((Avg) pcBucket.getAggregations().get("lte_rsrq")).getValue();
			lte_snr = ((Avg) pcBucket.getAggregations().get("lte_snr")).getValue();
			bw_link = ((Avg) pcBucket.getAggregations().get("bw_link")).getValue();
			ping_avg_delay = ((Avg) pcBucket.getAggregations().get("ping_avg_delay")).getValue();
			ping_lose_rate = ((Avg) pcBucket.getAggregations().get("ping_lose_rate")).getValue();
			latMax = (Max) pcBucket.getAggregations().get("lat");
			lngMax = (Max) pcBucket.getAggregations().get("lng");
			// 事件
			networkCount = pcBucket.getAggregations().get("networkCount");
			pingCount = pcBucket.getAggregations().get("pingCount");
			lteToWcdmaCount = pcBucket.getAggregations().get("lteToWcdmaCount");
			networkTotal = pcBucket.getAggregations().get("networkTotal");
			pingTotal = pcBucket.getAggregations().get("pingTotal");
			// 道路点经纬度有值
			if (!NumUtils.isNumber(latMax.getValue()) || !NumUtils.isNumber(lngMax.getValue())) {
				continue;
			}
			// 返回信息
			Map<String, Object> rpMap = new HashMap<String, Object>();
			rpMap.put("inter", kpiItv);
			rpMap.put("telecoms", vv.getOperator());
			rpMap.put("lat", latMax.getValue());
			rpMap.put("lng", lngMax.getValue());
			rpMap.put("cover", NumUtils.resNumber(cover_cnt / point_signal * 100, "0.00"));
			rpMap.put("count", point_signal);
			rpMap.put("con", point_business);
			rpMap.put("rsrp", NumUtils.resNumber(lte_rsrp, "0.00"));
			rpMap.put("rsrq", NumUtils.resNumber(lte_rsrq, "0"));
			rpMap.put("snr", NumUtils.resNumber(lte_snr, "0.00"));
			rpMap.put("link", NumUtils.isNumber(bw_link) ? NumUtils.resNumber(bw_link / 1024, "0.00") : "N/A");
			rpMap.put("delay", NumUtils.isNumber(ping_avg_delay) ? NumUtils.resNumber(ping_avg_delay, "0") : "N/A");
			rpMap.put("lose", NumUtils.isNumber(ping_lose_rate) ? NumUtils.resNumber(ping_lose_rate, "0.00") : "N/A");
			rpMap.put("networkCount", networkTotal.getValue() < 1 ? "N/A" : NumUtils.resNumber(networkCount.getValue(), "0"));
			rpMap.put("pingCount", pingTotal.getValue() < 1 ? "N/A" : NumUtils.resNumber(pingCount.getValue(), "0"));
			rpMap.put("lteToWcdmaCount", networkTotal.getValue() < 1 ? "N/A" : NumUtils.resNumber(lteToWcdmaCount.getValue(), "0"));
			rpMap.put("networkRate", networkTotal.getValue() < 1 ? "N/A" : NumUtils.resNumber(networkCount.getValue() / networkTotal.getValue() * 100, "0.00"));
			rpMap.put("pingRate", pingTotal.getValue() < 1 ? "N/A" : NumUtils.resNumber(pingCount.getValue() / pingTotal.getValue() * 100, "0.00"));

			// road_id
			StringTerms ltRoadId = pcBucket.getAggregations().get("roadId");
			Iterator<Bucket> itRoadId = ltRoadId.getBuckets().iterator();
			if (itRoadId.hasNext()) {
				rpMap.put("road_id", itRoadId.next().getKey());
			}
			// road_name
			StringTerms ltRoadName = pcBucket.getAggregations().get("roadName");
			Iterator<Bucket> itRoadName = ltRoadName.getBuckets().iterator();
			if (itRoadName.hasNext()) {
				rpMap.put("road_name", itRoadName.next().getKey());
			}
			// ci
			LongTerms ltCi = pcBucket.getAggregations().get("ciCnt");
			Iterator<Bucket> itCi = ltCi.getBuckets().iterator();
			i = 0;
			double topRate;// topN百分比
			while (itCi.hasNext()) {
				++i;
				Bucket bucket = itCi.next();
				topRate = bucket.getDocCount() / point_signal * 100;
				if (topRate > 5) {
					switch (i) {
					case 1:
						rpMap.put("ci1", bucket.getKey());
						rpMap.put("ciRate1", NumUtils.resNumber(topRate, "0.00"));
						break;
					case 2:
						rpMap.put("ci2", bucket.getKey());
						rpMap.put("ciRate2", NumUtils.resNumber(topRate, "0.00"));
						break;
					case 3:
						rpMap.put("ci3", bucket.getKey());
						rpMap.put("ciRate3", NumUtils.resNumber(topRate, "0.00"));
						break;
					case 4:
						rpMap.put("ci4", bucket.getKey());
						rpMap.put("ciRate4", NumUtils.resNumber(topRate, "0.00"));
						break;
					case 5:
						rpMap.put("ci5", bucket.getKey());
						rpMap.put("ciRate5", NumUtils.resNumber(topRate, "0.00"));
						break;
					default:
						break;
					}
				}
			}
			// pci
			LongTerms ltPci = pcBucket.getAggregations().get("pciCnt");
			Iterator<Bucket> itPci = ltPci.getBuckets().iterator();
			i = 0;
			j = 0;
			while (itPci.hasNext()) {
				++i;
				Bucket bucket = itPci.next();
				topRate = bucket.getDocCount() / point_signal * 100;
				if (topRate > 5) {
					++j;
					switch (i) {
					case 1:
						rpMap.put("pci1", bucket.getKey());
						rpMap.put("pciRate1", NumUtils.resNumber(topRate, "0.00"));
						break;
					case 2:
						rpMap.put("pci2", bucket.getKey());
						rpMap.put("pciRate2", NumUtils.resNumber(topRate, "0.00"));
						break;
					case 3:
						rpMap.put("pci3", bucket.getKey());
						rpMap.put("pciRate3", NumUtils.resNumber(topRate, "0.00"));
						break;
					case 4:
						rpMap.put("pci4", bucket.getKey());
						rpMap.put("pciRate4", NumUtils.resNumber(topRate, "0.00"));
						break;
					case 5:
						rpMap.put("pci5", bucket.getKey());
						rpMap.put("pciRate5", NumUtils.resNumber(topRate, "0.00"));
						break;
					default:
						break;
					}
					rpMap.put("pciCnt", j);
				}
			}
			// tac
			LongTerms ltTac = pcBucket.getAggregations().get("tacCnt");
			Iterator<Bucket> itTac = ltTac.getBuckets().iterator();
			i = 0;
			j = 0;
			while (itTac.hasNext()) {
				++i;
				Bucket bucket = itTac.next();
				topRate = bucket.getDocCount() / point_signal * 100;
				if (topRate > 5) {
					++j;
					switch (i) {
					case 1:
						rpMap.put("tac1", bucket.getKey());
						rpMap.put("tacRate1", NumUtils.resNumber(topRate, "0.00"));
						break;
					case 2:
						rpMap.put("tac2", bucket.getKey());
						rpMap.put("tacRate2", NumUtils.resNumber(topRate, "0.00"));
						break;
					case 3:
						rpMap.put("tac3", bucket.getKey());
						rpMap.put("tacRate3", NumUtils.resNumber(topRate, "0.00"));
						break;
					case 4:
						rpMap.put("tac4", bucket.getKey());
						rpMap.put("tacRate4", NumUtils.resNumber(topRate, "0.00"));
						break;
					case 5:
						rpMap.put("tac5", bucket.getKey());
						rpMap.put("tacRate5", NumUtils.resNumber(topRate, "0.00"));
						break;
					default:
						break;
					}
					rpMap.put("tacCnt", j);
				}
			}
			// bid
			LongTerms ltBid = pcBucket.getAggregations().get("enodeBidCnt");
			Iterator<Bucket> itBid = ltBid.getBuckets().iterator();
			i = 0;
			while (itBid.hasNext()) {
				++i;
				Bucket bucket = itBid.next();
				topRate = bucket.getDocCount() / point_signal * 100;
				if (topRate > 5) {
					switch (i) {
					case 1:
						rpMap.put("bid1", bucket.getKey());
						rpMap.put("bidRate1", NumUtils.resNumber(topRate, "0.00"));
						break;
					default:
						break;
					}
				}
			}
			// cell
			StringTerms ltCell = pcBucket.getAggregations().get("cellCnt");
			Iterator<Bucket> itCell = ltCell.getBuckets().iterator();
			j = 0;
			while (itCell.hasNext()) {
				Bucket bucket = itCell.next();
				if (bucket.getDocCount() / point_signal * 100 > 5) {
					++j;
					rpMap.put("cellCnt", j);
				}
			}
			//
			lists.add(rpMap);
		}
		return lists;
	}

	// 道路点信息,值初始化
	private void initRoadPoints(RoadPoints rp) {
		String falg = "N/A";
		rp.setCount(falg);
		rp.setCon(falg);
		rp.setCi1(falg);
		rp.setCi2(falg);
		rp.setCi3(falg);
		rp.setCi4(falg);
		rp.setCi5(falg);
		rp.setCiRate1(falg);
		rp.setCiRate2(falg);
		rp.setCiRate3(falg);
		rp.setCiRate4(falg);
		rp.setCiRate5(falg);
		rp.setPci1(falg);
		rp.setPci2(falg);
		rp.setPci3(falg);
		rp.setPci4(falg);
		rp.setPci5(falg);
		rp.setPciRate1(falg);
		rp.setPciRate2(falg);
		rp.setPciRate3(falg);
		rp.setPciRate4(falg);
		rp.setPciRate5(falg);
		rp.setBid1(falg);
		rp.setBidRate1(falg);
		rp.setCellCnt(falg);
		rp.setTacCnt(falg);
		rp.setPciCnt(falg);
		rp.setTac1(falg);
		rp.setTac2(falg);
		rp.setTac3(falg);
		rp.setTac4(falg);
		rp.setTac5(falg);
		rp.setTacRate1(falg);
		rp.setTacRate2(falg);
		rp.setTacRate3(falg);
		rp.setTacRate4(falg);
		rp.setTacRate5(falg);
		rp.setRsrp(falg);
		rp.setRsrq(falg);
		rp.setSnr(falg);
		rp.setLink(falg);
		rp.setLose(falg);
		rp.setDelay(falg);
		rp.setCover(falg);
	}
}
