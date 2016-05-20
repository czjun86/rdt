/**     
 * @文件名称: RoadQualityDaoES.java  
 * @类路径: com.wireless.web.daoElastic.reportManager  
 * @描述: 道路覆盖对比分析  
 * @作者：dsk  
 * @时间：2015年12月7日 上午10:33:44  
 * @版本：V1.0     
 */
package com.wireless.web.daoElastic.reportManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.filter.InternalFilter;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.pipeline.InternalSimpleValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.wireless.web.daoElastic.utils.ESConnUtils;
import com.wireless.web.daoElastic.utils.StringUtils;
import com.wireless.web.model.RoadInfo4ESBean;
import com.wireless.web.utils.NumUtils;
import com.wireless.web.utils.TimeUtils;
import com.wireless.web.utils.WebConstants;

/**
 * @类功能说明：
 * @类修改者：
 * @修改日期：
 * @修改说明：
 * @公司名称：
 * @作者：dsk
 * @创建时间：2015年12月7日 上午10:33:44
 * @版本：V1.0
 */
@Service("roadQualityDaoES")
public class RoadQualityDaoES {
	static Logger log = LoggerFactory.getLogger(RoadQualityDaoES.class);

	public List<Map<String, Object>> queryList(Map<String, Object> param, List<RoadInfo4ESBean> roadInfos) throws Exception {
		// 结果列表
		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();

		// 分页信息
		Integer beanIndex = param.get("pageIndex") == null ? null : Integer.parseInt(param.get("pageIndex") + "");
		Integer beanSize = param.get("pageSize") == null ? null : Integer.parseInt(param.get("pageSize") + "");

		// 客户端连接
		TransportClient tc = ESConnUtils.getTransportClient();
		// index,type
		String queryTime = String.valueOf(param.get("starttime")).replace("-", ".").trim() + "-" + String.valueOf(param.get("endtime")).replace("-", ".").trim();
		List<String> searchList = StringUtils.findMidIndex4ES(queryTime, String.valueOf(param.get("province")).trim());

		String[] docs = new String[searchList.size()];
		for (int i = 0; i < searchList.size(); i++) {
			docs[i] = searchList.get(i);
		}
		SearchRequestBuilder srb = tc.prepareSearch(docs);

		// 查询条件转换为JSON
		String strQry = this.qryRoadCondition(param);
		srb.setSource(strQry);
		
		// 执行查询
		log.debug(srb + "");
		SearchResponse sr = srb.execute().actionGet();
		log.debug("TookInMillis: " + sr.getTookInMillis());
		
		// 返回结果处理
		lists = this.handleResult(param, sr);

		// 按照对比指标升序排序
		Collections.sort(lists, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				return new Double(o1.get("diffValue") + "").compareTo(new Double(o2.get("diffValue") + ""));
			}
		});
		// 降序
		Collections.reverse(lists);
		return query4Page(beanIndex, beanSize, lists, roadInfos);
	}

	/**
	 * 功能说明: 分页 修改者名字: dsk 修改日期 2015年12月10日 修改内容
	 * 
	 * @参数： @param beanIndex
	 * @参数： @param beanSize
	 * @参数： @param list
	 * @参数： @return
	 * @throws
	 */
	private List<Map<String, Object>> query4Page(Integer beanIndex, Integer beanSize, List<Map<String, Object>> list, List<RoadInfo4ESBean> roadInfos) {
		// 分页处理
		List<Map<String, Object>> reMap = new ArrayList<Map<String, Object>>();
		int pageIndex = beanIndex == null ? WebConstants.PAGE_DEFAULT_PAGEINDEX : beanIndex;// 当前页数
		int pageSize = beanSize == null ? WebConstants.PAGE_DEFAULT_PAGESIZE : beanSize;// 每页数据量
		int stIndex = 0;
		int edIndex = list.size();
		if (pageIndex != -1 && pageSize != -1) {// 分页条件
			// stIndex = pageIndex == 1 ? 0 : (pageIndex - 1) * pageSize - 1;//
			// 起始下标
			// edIndex = stIndex + pageSize + 1;// 结束下标
			stIndex = pageIndex == 1 ? 0 : (pageIndex - 1) * pageSize;// 起始下标
			edIndex = stIndex + pageSize;// 结束下标
		}

		for (int i = stIndex; i < edIndex && i < list.size(); i++) {
			Map<String, Object> mapInfo = list.get(i);
			// 使用数据库查询,组装道路信息其他信息(含跳转信息)
			for (RoadInfo4ESBean road : roadInfos) {
				if (String.valueOf(mapInfo.get("roadId")).equalsIgnoreCase(String.valueOf(road.getRoadId())) && mapInfo.get("regionCode").equals(road.getRegionCode())) {
					mapInfo.put("cityName", road.getCityName());
					mapInfo.put("countryName", road.getCountryName());
					mapInfo.put("roadName", road.getRoadName());
					mapInfo.put("roadType", road.getRoadType());
					mapInfo.put("roadLevel", road.getRoadLevel());
					mapInfo.put("parentId", road.getParentId());
					break;
				}
			}
			reMap.add(mapInfo);
		}

		// 返回分页信息
		if (pageIndex != -1 && pageSize != -1) {// 分页条件
			// 计算总页数
			int pageCount = list.size() / pageSize;
			if (list.size() % pageSize > 0) {
				pageCount++;
			}

			Map<String, Object> pagem = new HashMap<>();
			pagem.put("pageIndex", pageIndex);// 当前页
			pagem.put("pageSize", pageSize);// 每页数据量
			pagem.put("totalPage", pageCount == 0 ? 1 : pageCount);// 总页数
			pagem.put("totalCount", list.size());// 总行数
			reMap.add(pagem);
		}
		return reMap;
	}

	/**
	 * 
	 * 功能说明: 返回JSON转换 修改者名字: dsk 修改日期 2015年12月28日 修改内容
	 * 
	 * @参数： @param param
	 * @参数： @param re
	 * @参数： @return
	 * @throws
	 */
	private List<Map<String, Object>> handleResult(Map<String, Object> param, SearchResponse sr) {
		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		Integer kpiId = Integer.parseInt(param.get("kpiId") + ""); // 指标:1.lte_rsrp,2.lte_snr,4.bw_link,8.wireless_coverage
		String selfTelcoms = param.get("operator") + "";// 自身运营商: 1.联通,2.移动,3.电信
		String compTelcoms = param.get("telecoms") + "";// 对比运营商
		Integer compare = Integer.parseInt(param.get("compare") + "");// 对比方式:1.优势分析,2.劣势分析
		Double threshold = param.get("threshold") == null ? null : Double.parseDouble(param.get("threshold") + "");// 对比阀值

		Map<String, Aggregation> aggMap = sr.getAggregations().getAsMap();
		InternalFilter itlFilter = (InternalFilter) aggMap.get("aggAll");// 最外层聚合
		Map<String, Aggregation> aggMap1 = itlFilter.getAggregations().asMap();
		StringTerms aggRegionCode = (StringTerms) aggMap1.get("aggRegionCode");// 区域聚合
		// 区域桶迭代
		Iterator<Bucket> iteratorRegionCode = aggRegionCode.getBuckets().iterator();
		String strRegionCode = "";// 区域代码
		String strRoadId = "";// 道路ID
		while (iteratorRegionCode.hasNext()) {
			Bucket bucketRegionCode = iteratorRegionCode.next(); // 区域桶
			strRegionCode = bucketRegionCode.getKeyAsString();// 区域代码
			// 道路ID桶
			Map<String, Aggregation> aggMap2 = bucketRegionCode.getAggregations().getAsMap();
			StringTerms stAgg2 = (StringTerms) aggMap2.get("aggRoadId");
			Iterator<Bucket> iteratorRoadId = stAgg2.getBuckets().iterator();
			while (iteratorRoadId.hasNext()) {
				Bucket bucketRoadId = iteratorRoadId.next();
				strRoadId = bucketRoadId.getKeyAsString().toUpperCase();// 道路ID
				// 运营商桶
				Map<String, Aggregation> aggMap3 = bucketRoadId.getAggregations().getAsMap();
				StringTerms stAgg3 = (StringTerms) aggMap3.get("aggTelecoms");
				Iterator<Bucket> iteratorTelecoms = stAgg3.getBuckets().iterator();
				int flagTelcoms = 0;// 运营商个数标示
				Map<String, Object> mapResult = new HashMap<String, Object>();// 结果
				Map<String, Object> mapSelf = new HashMap<String, Object>();// 自身运营商
				Map<String, Object> mapComp = new HashMap<String, Object>();// 对比运营商
				while (iteratorTelecoms.hasNext()) {
					Bucket bucketTelecoms = iteratorTelecoms.next();
					String telecoms = bucketTelecoms.getKeyAsString();
					String avgNetwrokRate = ((InternalSimpleValue)bucketTelecoms.getAggregations().get("rateNetworkDrop")).getValueAsString();// 网路掉线率
					String avgPingRate = ((InternalSimpleValue)bucketTelecoms.getAggregations().get("ratePingDrop")).getValueAsString();// 业务掉线率
					String avgCover = ((InternalSimpleValue)bucketTelecoms.getAggregations().get("rateCover")).getValueAsString();// 覆盖率
					String avgRsrp = ((InternalSimpleValue)bucketTelecoms.getAggregations().get("avgRsrp")).getValueAsString();// rsrp
					String avgBwLink = ((InternalSimpleValue)bucketTelecoms.getAggregations().get("avgBwLink")).getValueAsString();// bw_link
					String avgSnr = ((InternalSimpleValue)bucketTelecoms.getAggregations().get("avgSnr")).getValueAsString();// snr
					String cntLteToWcdma = ((InternalSimpleValue)bucketTelecoms.getAggregations().get("cntNetwork43")).getValueAsString();// 4g切3g

					// 运营商为自身运营商或者对比运营商
					if (telecoms.equals(selfTelcoms)) {
						flagTelcoms++;
						mapSelf.put("wirelessCoverage", NumUtils.isNumber(avgCover) ? NumUtils.resNumber(NumberUtils.toDouble(avgCover) * 100, "0.00") : "N/A");
						mapSelf.put("lteRsrp", NumUtils.isNumber(avgRsrp) ? NumUtils.resNumber(NumberUtils.toDouble(avgRsrp), "0.00") : "N/A");
						mapSelf.put("lteSnr", NumUtils.isNumber(avgSnr) ? NumUtils.resNumber(NumberUtils.toDouble(avgSnr), "0.00") : "N/A");
						mapSelf.put("bwLink", NumUtils.isNumber(avgBwLink) ? NumUtils.resNumber(NumberUtils.toDouble(avgBwLink), "0.00") : "N/A");
						mapSelf.put("networkRate", NumUtils.isNumber(avgNetwrokRate) ? NumUtils.resNumber(NumberUtils.toDouble(avgNetwrokRate) * 100, "0.00") : "N/A");
						mapSelf.put("pingRate", NumUtils.isNumber(avgPingRate) ? NumUtils.resNumber(NumberUtils.toDouble(avgPingRate) * 100, "0.00") : "N/A");
						mapSelf.put("lteToWcdma", NumUtils.isNumber(avgNetwrokRate) && NumUtils.isNumber(cntLteToWcdma) ? NumUtils.resNumber(NumberUtils.toDouble(cntLteToWcdma), "0") : "N/A");
					} else if (telecoms.equals(compTelcoms)) {
						flagTelcoms++;
						mapComp.put("wirelessCoverage", NumUtils.isNumber(avgCover) ? NumUtils.resNumber(NumberUtils.toDouble(avgCover) * 100, "0.00") : "N/A");
						mapComp.put("lteRsrp", NumUtils.isNumber(avgRsrp) ? NumUtils.resNumber(NumberUtils.toDouble(avgRsrp), "0.00") : "N/A");
						mapComp.put("lteSnr", NumUtils.isNumber(avgSnr) ? NumUtils.resNumber(NumberUtils.toDouble(avgSnr), "0.00") : "N/A");
						mapComp.put("bwLink", NumUtils.isNumber(avgBwLink) ? NumUtils.resNumber(NumberUtils.toDouble(avgBwLink), "0.00") : "N/A");
						mapComp.put("networkRate", NumUtils.isNumber(avgNetwrokRate) ? NumUtils.resNumber(NumberUtils.toDouble(avgNetwrokRate) * 100, "0.00") : "N/A");
						mapComp.put("pingRate", NumUtils.isNumber(avgPingRate) ? NumUtils.resNumber(NumberUtils.toDouble(avgPingRate) * 100, "0.00") : "N/A");
						mapComp.put("lteToWcdma", NumUtils.isNumber(cntLteToWcdma) ? NumUtils.resNumber(NumberUtils.toDouble(cntLteToWcdma), "0") : "N/A");
					}

					// 对比运营商都有数据
					if (flagTelcoms == 2) {
						Double diffValue = null;
						String valueSelf = mapSelf.get(this.kpiId2Name(kpiId)) + "";
						String valueComp = mapComp.get(this.kpiId2Name(kpiId)) + "";

						// 排除对比指标为空
						if (!NumUtils.isNumber(valueSelf) || !NumUtils.isNumber(valueComp)) {
							continue;
						}
						if (compare == 1) {
							diffValue = NumUtils.resDouble(NumberUtils.toDouble(valueSelf), 2) - NumUtils.resDouble(NumberUtils.toDouble(valueComp), 2);
						} else if (compare == 2) {
							diffValue = NumUtils.resDouble(NumberUtils.toDouble(valueComp), 2) - NumUtils.resDouble(NumberUtils.toDouble(valueSelf), 2);
						}
						// 对比指标阀值
						if (null != threshold && diffValue <= threshold) {
							continue;
						}

						mapResult.put("roadId", strRoadId);
						mapResult.put("regionCode", strRegionCode);
						mapResult.put("wirelessCoverage", mapSelf.get("wirelessCoverage"));
						mapResult.put("lteRsrp", mapSelf.get("lteRsrp"));
						mapResult.put("lteSnr", mapSelf.get("lteSnr"));
						mapResult.put("bwLink", mapSelf.get("bwLink"));
						mapResult.put("networkRate", mapSelf.get("networkRate"));
						mapResult.put("pingRate", mapSelf.get("pingRate"));
						mapResult.put("lteToWcdma", mapSelf.get("lteToWcdma"));
						mapResult.put("selfValue", NumUtils.resNumber(Double.valueOf(valueSelf), "0.00"));
						mapResult.put("compValue", NumUtils.resNumber(Double.valueOf(valueComp), "0.00"));
						mapResult.put("diffValue", NumUtils.resNumber(diffValue, "0.00"));
						lists.add(mapResult);
					}
				}
			}
		}

		return lists;
	}

	/**
	 * 功能说明: 查询条件 修改者名字: dsk 修改日期 2015年12月10日 修改内容
	 * 
	 * @参数： @param param
	 * @参数： @return
	 * @throws
	 */
	private String qryRoadCondition(Map<String, Object> param) {
		String starttime = param.get("starttime") + "";
		String endtime = param.get("endtime") + "";
		String stime = TimeUtils.chDate(starttime.replaceAll("\\.", "-") + " 00:00:00", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		String etime = TimeUtils.chDate(endtime.replaceAll("\\.", "-") + " 23:59:59", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		String operator = param.get("operator") + "";
		String telecoms = param.get("telecoms") + "";
		Integer roadType = Integer.parseInt(param.get("roadType") + "");
		Integer roadLevel = Integer.parseInt(param.get("roadLevel") + "");
		String province = param.get("province") + "";
		String district = param.get("district") + "";
		String area = param.get("area") + "";

		String qryPreArea = "50";
		// 区域;
		if (null != area && !area.equals("-1")) {
			qryPreArea = area;
		} else if (null != district && !district.equals("-1")) {
			qryPreArea = district.substring(0, 4);
		} else if (null != province && !province.equals("-1")) {
			qryPreArea = province.substring(0, 2);
		}
		// 加入查询条件
		JSONObject params = new JSONObject(); // 最终查询条件
		params.put("from", 0);
		params.put("size", 0);
		params.put("explain", false);

		// 单独组装聚合桶
		JSONObject aggs = new JSONObject(); // 第一层聚合
		JSONObject aggAll = new JSONObject(); // 第一层聚合(过滤条件)
		JSONObject filter = new JSONObject(); // 过滤条件
		JSONObject bool = new JSONObject(); // 条件判断
		JSONArray boolMust = new JSONArray();
		JSONObject boolNotMust = new JSONObject();
		// 查询时间
		boolMust.add("{'range':{'test_time': {'from': '" + stime + "','to': '" + etime + "','include_lower': true,'include_upper': true}}}");
		// 运营商
		boolMust.add("{'terms':{'telecoms':[" + operator + "," + telecoms + "]}}");
		// 区域
		boolMust.add("{'prefix':{'region_code':" + qryPreArea + "}}");
		// 道路类型
		if (null != roadType && roadType != -1) {
			boolMust.add("{'terms':{'road_type':[" + roadType + "]}}");
		}
		// 道路等级
		if (null != roadLevel && roadLevel != -1) {
			boolMust.add("{'terms':{'road_level':[" + roadLevel + "]}}");
		}
		// 仅查询4G数据
		// boolMust.add("{'terms':{'network_type':[-1,0,13]}}");
		// 道路点不为空
		boolNotMust.put("terms", JSONObject.fromObject("{'point_code':['']}"));

		// 组装JSON
		bool.put("must", "[" + boolMust + "]");
		bool.put("must_not", boolNotMust);
		filter.put("bool", bool);
		aggAll.put("filter", filter);

		// 聚合拼接
		StringBuffer temp = new StringBuffer();
		temp.append("{\"aggRegionCode\":{\"terms\":{\"field\":\"region_code\",\"size\":2147483647},\"aggs\":{");
		temp.append("\"aggRoadId\":{\"terms\":{\"field\":\"road_id\",\"size\":2147483647},\"aggs\":{\"aggTelecoms\":{\"terms\":{");
		temp.append("\"field\":\"telecoms\",\"size\":2147483647},\"aggs\":{\"aggRoadAll\":{\"terms\":{\"script\":{\"inline\":");
		temp.append("\"doc['point_code'].value + doc['region_code'].value + doc['road_id'].value + doc['telecoms'].value\"},");
		temp.append("\"size\":2147483647},\"aggs\":{\"valRsrpPoint\":{\"sum\":{\"field\":\"val_lte_rsrp\"}},\"numRsrpPoint\":{\"sum");
		temp.append("\":{\"field\":\"num_lte_rsrp\"}},\"avgRsrpPoint\":{\"bucket_script\":{\"buckets_path\":{\"valRsrpPath");
		temp.append("\":\"valRsrpPoint\",\"numRsrpPath\":\"numRsrpPoint\"},\"script\":\"valRsrpPath/numRsrpPath\"}},");
		temp.append("\"valSnrPoint\":{\"sum\":{\"field\":\"val_lte_snr\"}},\"numSnrPoint\":{\"sum\":{\"field\":");
		temp.append("\"num_lte_snr\"}},\"avgSnrPoint\":{\"bucket_script\":{\"buckets_path\":{\"valSnrPath\":");
		temp.append("\"valSnrPoint\",\"numSnrPath\":\"numSnrPoint\"},\"script\":\"valSnrPath/numSnrPath\"}},");
		temp.append("\"valBwLinkPoint\":{\"sum\":{\"field\":\"val_bw_link\"}},\"numBwLinkPoint\":{\"sum\":{");
		temp.append("\"field\":\"num_bw_link\"}},\"avgBwLinkPoint\":{\"bucket_script\":{\"buckets_path\":{");
		temp.append("\"valBwLinkPath\":\"valBwLinkPoint\",\"numBwLinkPath\":\"numBwLinkPoint\"},\"script\":");
		temp.append("\"valBwLinkPath/numBwLinkPath/1024\"}},\"numNetworkEventPoint\":{\"sum\":{\"field\":");
		temp.append("\"num_network_event\"}},\"numNetworkDropPoint\":{\"sum\":{\"field\":\"num_network_drop");
		temp.append("\"}},\"numNetwork43Point\":{\"sum\":{\"field\":\"num_network_43\"}},\"rateNetworkDropPoint");
		temp.append("\":{\"bucket_script\":{\"buckets_path\":{\"numNetworkEventPath\":\"numNetworkEventPoint\",");
		temp.append("\"numNetworkDropPath\":\"numNetworkDropPoint\"},\"script\":\"numNetworkDropPath/numNetworkEventPath");
		temp.append("\"}},\"numPingEventPoint\":{\"sum\":{\"field\":\"num_ping_event\"}},\"numPingDropPoint\":{");
		temp.append("\"sum\":{\"field\":\"num_ping_drop\"}},\"ratePingDropPoint\":{\"bucket_script\":{\"buckets_path");
		temp.append("\":{\"numPingEventPath\":\"numPingEventPoint\",\"numPingDropPath\":\"numPingDropPoint\"},");
		temp.append("\"script\":\"numPingDropPath/numPingEventPath\"}},\"numCover\":{\"bucket_script\":{\"buckets_path");
		temp.append("\":{\"avgSnrPath\":\"avgSnrPoint\",\"avgRsrpPath\":\"avgRsrpPoint\"},\"script\":");
		temp.append("\"if(avgRsrpPath>=-105 && avgSnrPath>=-3){return 1;}else{return 0;}\"}},");
		temp.append("\"numUnCover\":{\"bucket_script\":{\"buckets_path\":{\"avgSnrPath\":\"avgSnrPoint\",\"avgRsrpPath");
		temp.append("\":\"avgRsrpPoint\"},\"script\":\"if(avgRsrpPath>=-105 && avgSnrPath>=-3){return 0;}else{return 1;}\"}}}},");
		temp.append("\"avgSnr\":{\"avg_bucket\":{\"buckets_path\":\"aggRoadAll>avgSnrPoint\"}},\"avgRsrp\":{\"avg_bucket\":{");
		temp.append("\"buckets_path\":\"aggRoadAll>avgRsrpPoint\"}},\"avgBwLink\":{\"avg_bucket\":{\"buckets_path\":");
		temp.append("\"aggRoadAll>avgBwLinkPoint\"}},\"rateNetworkDrop\":{\"avg_bucket\":{\"buckets_path\":");
		temp.append("\"aggRoadAll>rateNetworkDropPoint\"}},\"cntNetwork43\":{\"sum_bucket\":{\"buckets_path");
		temp.append("\":\"aggRoadAll>numNetwork43Point\"}},\"ratePingDrop\":{\"avg_bucket\":{\"buckets_path");
		temp.append("\":\"aggRoadAll>ratePingDropPoint\"}},\"cntCover\":{\"avg_bucket\":{\"buckets_path\":");
		temp.append("\"aggRoadAll>numCover\"}},\"cntUnCover\":{\"avg_bucket\":{\"buckets_path\":\"aggRoadAll>numUnCover");
		temp.append("\"}},\"rateCover\":{\"bucket_script\":{\"buckets_path\":{\"cntCoverPath\":\"cntCover\",\"cntUnCoverPath");
		temp.append("\":\"cntUnCover\"},\"script\":\"cntCoverPath / (cntCoverPath+cntUnCoverPath)\"}}}}}}}}}");

		// 聚合加入JSON
		aggAll.put("aggs", JSONObject.fromObject(temp.toString()));
		aggs.put("aggAll", aggAll);

		// 最终组合
		params.put("aggs", aggs);

		return params.toString();
	}

	// 根据 kpiId 获取指标名称
	private String kpiId2Name(int kpiId) {
		// 指标: 1.lte_rsrp,2.lte_snr,4.bw_link,8.wireless_coverage
		String kpiName = "";
		switch (kpiId) {
		case 1:
			kpiName = "lteRsrp";
			break;
		case 2:
			kpiName = "lteSnr";
			break;
		case 4:
			kpiName = "bwLink";
			break;
		case 8:
			kpiName = "wirelessCoverage";
			break;
		case 9:
			kpiName = "networkRate";
			break;
		case 10:
			kpiName = "pingRate";
			break;
		default:
			break;
		}
		return kpiName;
	}
}
