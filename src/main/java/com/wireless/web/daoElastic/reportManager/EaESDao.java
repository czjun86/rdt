/**     
 * @文件名称: EaESDao.java  
 * @类路径: com.wireless.web.daoElastic.reportManager  
 * @描述: TODO  
 * @作者：tc  
 * @时间：2015年12月10日 下午2:32:22  
 * @版本：V1.0     
 */
package com.wireless.web.daoElastic.reportManager;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.wireless.web.daoElastic.utils.ESConnUtils;
import com.wireless.web.daoElastic.utils.StringUtils;
import com.wireless.web.model.KpiSettings;
import com.wireless.web.model.QueryBean;
import com.wireless.web.model.RoadInfo4ESBean;
import com.wireless.web.utils.NumUtils;
import com.wireless.web.utils.TimeUtils;

/**
 * @类功能说明：
 * @类修改者：
 * @修改日期：
 * @修改说明：
 * @公司名称：
 * @作者：tc
 * @创建时间：2015年12月10日 下午2:32:22
 * @版本：V1.0
 */
@Service("EaESDao")
public class EaESDao {

	private static final Logger logger = LoggerFactory.getLogger(EaESDao.class);
	// 分组维度(0否1是)
	private int timeAgg, areaAgg, roadAgg, operatorAgg;
	// 是否区间计算(0否1是)
	private int rsrpItl, rsrqItl, snrItl, httpItl, delayItl, lostItl;

	// 分页信息
	private Integer beanIndex = null;
	private Integer beanSize = null;

	// 数据库查询信息
	private List<RoadInfo4ESBean> roadInfos = null;
	private List<KpiSettings> kpiInfos = null;

	/**
	 * 
	 * 函数功能说明 用ES查询指标综合分析 
	 * tc
	 * 2015年12月10日
	 * 修改者名字
	 * 修改日期 
	 * 修改内容
	 * 
	 * @参数： @param bean
	 * @参数： @return
	 * @参数： @throws Exception
	 * @throws
	 */
	public List<Map<String, Object>> queryComprehensiveByES(Map<String, Object> params, List<RoadInfo4ESBean> listRoadInfos, List<KpiSettings> kpiInfos) throws Exception {
		// 查询条件
		QueryBean bean = (QueryBean) params.get("queryBean");

		// 分页信息
		beanIndex = params.get("pageIndex") == null ? null : Integer.parseInt(params.get("pageIndex") + "");
		beanSize = params.get("pageSize") == null ? null : Integer.parseInt(params.get("pageSize") + "");
		this.roadInfos = listRoadInfos;
		this.kpiInfos = kpiInfos;
		
		String jsonResult = qryCondition(bean,kpiInfos);
		
		// 组装查询结果
		List<Map<String, Object>> listResult = fetchResponseResult(jsonResult);
		
		return query4Page(listResult);
	}

	/**
	 * 功能说明: 分页
	 * 
	 * @参数： @param list
	 * @参数： @return
	 * @throws
	 */
	private List<Map<String, Object>> query4Page(List<Map<String, Object>> list) {
		// 分页处理
		List<Map<String, Object>> reMap = new ArrayList<Map<String, Object>>();
		int stIndex = 0;
		int edIndex = list.size();
		if (beanIndex != -1 && beanSize != -1) {// 分页条件
			stIndex = beanIndex == 1 ? 0 : (beanIndex - 1) * beanSize;// 起始下标
			edIndex = stIndex + beanSize;// 结束下标
		}

		for (int i = stIndex; i < edIndex && i < list.size(); i++) {
			Map<String, Object> mapInfo = list.get(i);
			// 使用数据库查询,组装道路信息其他信息(含跳转信息)
			if (null != roadInfos) {
				if (areaAgg == 1 && roadAgg == 1) {
					for (RoadInfo4ESBean road : roadInfos) {
						if (String.valueOf(mapInfo.get("road_id")).equalsIgnoreCase(String.valueOf(road.getRoadId())) && mapInfo.get("region_code").equals(road.getRegionCode())) {
							mapInfo.put("parent_region", road.getCityName());
							mapInfo.put("region_name", road.getCountryName());
							mapInfo.put("road_name", road.getRoadName());
							mapInfo.put("road_type", road.getRoadType());
							mapInfo.put("road_level", road.getRoadLevel());
							mapInfo.put("level2_code", road.getParentId());
							break;
						}
					}
				} else if (areaAgg == 1 && roadAgg == 0) {
					for (RoadInfo4ESBean road : roadInfos) {
						if (mapInfo.get("region_code").equals(road.getRegionCode())) {
							mapInfo.put("parent_region", road.getCityName());
							mapInfo.put("region_name", road.getCountryName());
							mapInfo.put("level2_code", road.getParentId());
							break;
						}
					}
				} else if (areaAgg == 0 && roadAgg == 1) {
					for (RoadInfo4ESBean road : roadInfos) {
						if (String.valueOf(mapInfo.get("road_id")).equalsIgnoreCase(String.valueOf(road.getRoadId()))) {
							mapInfo.put("road_name", road.getRoadName());
							mapInfo.put("road_type", road.getRoadType());
							mapInfo.put("road_level", road.getRoadLevel());
							break;
						}
					}
				}
			}
			reMap.add(mapInfo);
		}

		// 返回分页信息
		if (beanIndex != -1 && beanSize != -1) {// 分页条件
			// 计算总页数
			int pageCount = list.size() / beanSize;
			if (list.size() % beanSize > 0) {
				pageCount++;
			}

			Map<String, Object> pagem = new HashMap<>();
			pagem.put("pageIndex", beanIndex);// 当前页
			pagem.put("pageSize", beanSize);// 每页数据量
			pagem.put("totalPage", pageCount == 0 ? 1 : pageCount);// 总页数
			pagem.put("totalCount", list.size());// 总行数
			reMap.add(pagem);
		}
		return reMap;
	}

	/**
	 * 功能说明: 组装查询结果
	 * 
	 * @参数： @param sr
	 * @throws
	 */
	private List<Map<String, Object>> fetchResponseResult(String jsonResult) {
		List<Map<String, Object>> listResult = new ArrayList<Map<String, Object>>();

		JSONObject results = JSONObject.fromObject(jsonResult);

		DecimalFormat df0 = new DecimalFormat("0");
		DecimalFormat df2 = new DecimalFormat("0.00");
		
		JSONArray timeBuckets = results.getJSONObject("aggregations").getJSONObject("aggFilter").getJSONObject("timeAgg").getJSONArray("buckets");
		
		for(int i = 0;i<timeBuckets.size();i++){
			JSONArray areaBuckets = timeBuckets.getJSONObject(i).getJSONObject("areaAgg").getJSONArray("buckets");
			for(int k=0;k<areaBuckets.size();k++){
				JSONObject areaJSONObject = areaBuckets.getJSONObject(k);
				String regCode = areaJSONObject.getString("key");//获取区域编码
				JSONArray roadBuckets = areaJSONObject.getJSONObject("roadAgg").getJSONArray("buckets");
				for(int j=0;j<roadBuckets.size();j++){
					JSONObject roadJSONObject = roadBuckets.getJSONObject(j);
					String roadId = roadJSONObject.getString("key");//获取道路编号
					JSONArray tmsBuckets = roadJSONObject.getJSONObject("operatorAgg").getJSONArray("buckets");
					for(int m=0;m<tmsBuckets.size();m++){
						JSONObject tmsJSONObject = tmsBuckets.getJSONObject(m);
						String tmsId = tmsJSONObject.getString("key");//获取运营商编号
						Map<String, Object> mapResult = new HashMap<String, Object>();// 结果
						// lost_avg
						String lostAvg = tmsJSONObject.getJSONObject("lost_avg").getString("value");
						mapResult.put("ping_lose_rate", NumUtils.isNumber(lostAvg) ? NumUtils.resDouble(NumberUtils.toDouble(lostAvg), 2) : "N/A");
						// rsrp_avg
						String rsrpAvg = tmsJSONObject.getJSONObject("rsrp_avg").getString("value");
						mapResult.put("lte_rsrp", NumUtils.resDouble(NumberUtils.toDouble(rsrpAvg), 2));
						// http_avg
						String httpAvg = tmsJSONObject.getJSONObject("http_avg").getString("value");
						mapResult.put("bw_link", NumUtils.isNumber(httpAvg) ? NumUtils.resDouble(NumberUtils.toDouble(httpAvg) / 1024, 2) : "N/A");
						// signal
						int signal = tmsJSONObject.getJSONObject("signal").getInt("value");
						mapResult.put("point_signal", signal);
						// rsrq_avg
						String rsrqAvg = tmsJSONObject.getJSONObject("rsrq_avg").getString("value");
						mapResult.put("lte_rsrq", NumUtils.isNumber(rsrqAvg) ? df0.format(NumberUtils.toDouble(rsrqAvg)) : "N/A");

						// snr_avg
						String snrAvg = tmsJSONObject.getJSONObject("snr_avg").getString("value");
						mapResult.put("lte_snr", NumUtils.resDouble(NumberUtils.toDouble(snrAvg), 2));

						// testMileage
						String testMileage = tmsJSONObject.getJSONObject("testMileage").getString("value");
						mapResult.put("road_test_mileage", NumUtils.resDouble(NumberUtils.toDouble(testMileage)/1000, 2));

						// coverMileage
						String coverMileage = tmsJSONObject.getJSONObject("coverMileage").getString("value");
						mapResult.put("road_cover_mileage", NumUtils.resDouble(NumberUtils.toDouble(coverMileage)*30/1000,2));

						// weakCoverMileage
						String weakCoverMileage = tmsJSONObject.getJSONObject("weakCoverMileage").getString("value");
						mapResult.put("road_poor_mileage", NumUtils.resDouble(NumberUtils.toDouble(weakCoverMileage)*30/1000, 2));

						// roadCoverRate
						String roadCoverRate = tmsJSONObject.getJSONObject("roadCoverRate").getString("value");
						mapResult.put("road_cover_rate", NumUtils.resDouble(NumberUtils.toDouble(roadCoverRate)*100, 2));

						// bigRate
						String bigRate = tmsJSONObject.getJSONObject("bigRate").getString("value");
						mapResult.put("bw_link_rate", NumUtils.resDouble(NumberUtils.toDouble(bigRate)*100, 2));

						// busCount
						int busCount = tmsJSONObject.getJSONObject("busCount").getInt("value");
						mapResult.put("point_business", busCount);

						// delay_avg
						String delayAvg = tmsJSONObject.getJSONObject("delay_avg").getString("value");
						mapResult.put("ping_avg_delay", NumUtils.isNumber(delayAvg) ? df0.format(NumberUtils.toDouble(delayAvg))  : "N/A");
						
						// time
						if (timeAgg == 1) {
							// endTime
							String et = tmsJSONObject.getJSONObject("endTime").get("value").toString();
							//转化科学计数法为Long型
							BigDecimal etbd = new BigDecimal(et);
							
							String endTime = TimeUtils.chDate(NumberUtils.toLong(etbd.toPlainString()), "yyyy-MM-dd");
							mapResult.put("end_time", endTime);
							// startTime
							String st = tmsJSONObject.getJSONObject("startTime").get("value").toString();
							//转化科学计数法为Long型
							BigDecimal stbd = new BigDecimal(st); 
							
							String startTime = TimeUtils.chDate(NumberUtils.toLong(stbd.toPlainString()), "yyyy-MM-dd");
							mapResult.put("start_time", startTime);
						} else {
							mapResult.put("start_time", null);
							mapResult.put("end_time", null);
						}
						
						// region_code
						mapResult.put("region_code", areaAgg == 1 ? regCode : null);
						
						// road_id
						mapResult.put("road_id", roadAgg == 1 ? roadId : null);
						
						// telecoms
						if(operatorAgg == 1){
							String strTelecoms = "";
							switch (tmsId) {
							case "1":
								strTelecoms = "联通";
								break;
							case "2":
								strTelecoms = "移动";
								break;
							case "3":
								strTelecoms = "电信";
								break;
							default:
								break;
							}
							mapResult.put("telecoms",strTelecoms);
						}
						
						//网络掉线次数
						int networkCount = tmsJSONObject.getJSONObject("netdrops_count").getInt("value");
						mapResult.put("network_count", networkCount);
						
						//网络掉线率
						String networkRate = tmsJSONObject.getJSONObject("netdrops_lv").getString("value");
						mapResult.put("network_rate", NumUtils.resDouble(NumberUtils.toDouble(networkRate)*100, 2));
						
						//业务掉线次数
						int pingCount = tmsJSONObject.getJSONObject("busdrops_count").getInt("value");
						mapResult.put("ping_count", pingCount);
						
						//业务掉线率
						String pingRate = tmsJSONObject.getJSONObject("busdrops_lv").getString("value");
						mapResult.put("ping_rate", NumUtils.resDouble(NumberUtils.toDouble(pingRate)*100, 2));
						
						//4G下切3G次数
						int networkChange = tmsJSONObject.getJSONObject("4gto3g_count").getInt("value");
						mapResult.put("network_change", networkChange);
						
						/**指标区间统计组装     开始**/
						
						
						/**rsrp组装     开始**/
						
						if(rsrpItl == 1){
							for(int f=1;f<=6;f++){
								String val = tmsJSONObject.getJSONObject("rsrp_interval_"+f).getString("value");
								mapResult.put("rsrp_ratio"+f, NumUtils.resDouble(NumberUtils.toDouble(val)*100, 2));
								String con = tmsJSONObject.getJSONObject("rsrp_count_"+f).getString("value");
								mapResult.put("rsrp_point"+f, NumUtils.resDouble(NumberUtils.toDouble(con)*30/1000, 2));
							}
						}
						
						/**rsrp组装     结束**/
						
						
						/**snr组装     开始**/
						
						if(snrItl == 1){
							for(int f=1;f<=6;f++){
								String val = tmsJSONObject.getJSONObject("snr_interval_"+f).getString("value");
								mapResult.put("snr_ratio"+f, NumUtils.resDouble(NumberUtils.toDouble(val)*100, 2));
								String con = tmsJSONObject.getJSONObject("snr_count_"+f).getString("value");
								mapResult.put("snr_point"+f, NumUtils.resDouble(NumberUtils.toDouble(con)*30/1000, 2));
							}
						}
						
						/**snr组装     结束**/
						
						
						
						/**rsrq组装     开始**/
						
						if(rsrqItl == 1){
							for(int f=1;f<=6;f++){
								String val = tmsJSONObject.getJSONObject("rsrq_interval_"+f).getString("value");
								mapResult.put("rsrq_ratio"+f, NumUtils.resDouble(NumberUtils.toDouble(val)*100, 2));
								String con = tmsJSONObject.getJSONObject("rsrq_count_"+f).getString("value");
								mapResult.put("rsrq_point"+f, NumUtils.resDouble(NumberUtils.toDouble(con)*30/1000, 2));
							}
						}
						
						/**rsrq组装     结束**/
						
						
						/**下行链路带宽组装     开始**/
						
						if(httpItl == 1){
							for(int f=1;f<=6;f++){
								String val = tmsJSONObject.getJSONObject("http_interval_"+f).getString("value");
								mapResult.put("bw_link_ratio"+f, NumUtils.resDouble(NumberUtils.toDouble(val)*100, 2));
								String con = tmsJSONObject.getJSONObject("http_count_"+f).getString("value");
								mapResult.put("bw_link_point"+f, NumUtils.resDouble(NumberUtils.toDouble(con)*30/1000, 2));
							}
						}
						
						/**下行链路带宽组装     结束**/
						
						/**延时组装     开始**/
						
						if(delayItl == 1){
							for(int f=1;f<=6;f++){
								String val = tmsJSONObject.getJSONObject("delay_interval_"+f).getString("value");
								mapResult.put("ping_delay_ratio"+f, NumUtils.resDouble(NumberUtils.toDouble(val)*100, 2));
								String con = tmsJSONObject.getJSONObject("delay_count_"+f).getString("value");
								mapResult.put("ping_delay_point"+f, NumUtils.resDouble(NumberUtils.toDouble(con)*30/1000, 2));
							}
						}
						
						/**延时组装     结束**/
						
						
						/**丢包组装     开始**/
						
						if(lostItl == 1){
							for(int f=1;f<=6;f++){
								String val = tmsJSONObject.getJSONObject("lost_interval_"+f).getString("value");
								mapResult.put("ping_lose_ratio"+f, NumUtils.resDouble(NumberUtils.toDouble(val)*100, 2));
								String con = tmsJSONObject.getJSONObject("lost_count_"+f).getString("value");
								mapResult.put("ping_lose_point"+f, NumUtils.resDouble(NumberUtils.toDouble(con)*30/1000, 2));
							}
						}
						
						/**丢包组装     结束**/
						
						/**指标区间统计组装     结束**/
						

						// add
						listResult.add(mapResult);
						
					}
				}
			}
		}

		
		// 按照时间降序,道路覆盖率升序排序
		Collections.sort(listResult, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				int comp1 = 0;
				if (timeAgg == 1) {
					comp1 = o1.get("start_time").toString().compareTo(o2.get("start_time").toString());
					if (comp1 == 0) {
						comp1 = Double.valueOf(o2.get("road_cover_rate") + "").compareTo(Double.valueOf(o1.get("road_cover_rate") + ""));
					}
				} else {
					comp1 = Double.valueOf(o2.get("road_cover_rate") + "").compareTo(Double.valueOf(o1.get("road_cover_rate") + ""));
				}
				return comp1;
			}
		});
		// 降序
		Collections.reverse(listResult);

		return listResult;
	}

	/**
	 * @throws Exception 
	 * @throws ParseException
	 *功能说明: 综合查询条件组装 并查询
	 * 
	 * @参数： @param bean
	 * @参数： @return
	 * @throws
	 */
	private String qryCondition(QueryBean bean,List<KpiSettings> kpiInfos) throws Exception {
		Map<String, String> times = TimeUtils.getSETime(bean.getQueryTime());
		String stime = null;
		String etime = null;
		if (null != bean.getTimegransel() && bean.getTimegransel().equals("3")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(times.get("endtime") + "-01"));
			calendar.roll(Calendar.DATE, -1);
			stime = TimeUtils.chDate(times.get("starttime") + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
			etime = TimeUtils.chDate(sdf.format(calendar.getTime()) + " 23:59:59", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		} else {
			stime = TimeUtils.chDate(times.get("starttime") + " 00:00:00", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
			etime = TimeUtils.chDate(times.get("endtime") + " 23:59:59", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		}
		String operator = bean.getOperator();
		String province = bean.getProvince();
		String district = bean.getDistrict();
		String area = bean.getArea();
		Integer roadType = bean.getRoadType();
		Integer roadLevel = bean.getRoadLevel();
		String roadId = bean.getRoadId();
		String imei = bean.getImei();
		
		//聚合判定
		timeAgg = bean.getShowTime() != null ? bean.getShowTime() : 0;
		areaAgg = bean.getShowArea() != null ? bean.getShowArea() : 0;
		roadAgg = bean.getShowRoad() != null ? bean.getShowRoad() : 0;
		operatorAgg = bean.getShowOperator() != null ? bean.getShowOperator() : 0;
		
		//区间指标判定
		rsrpItl = bean.getRsrp() != null ? bean.getRsrp() : 0;
		rsrqItl = bean.getRsrq() != null ? bean.getRsrq() : 0;
		snrItl = bean.getSnr() != null ? bean.getSnr() : 0;
		delayItl = bean.getDelay() != null ? bean.getDelay() : 0;
		lostItl = bean.getLose() != null ? bean.getLose() : 0;
				
		//根据查询时间，计算索引
		List<String> indexList = StringUtils.findIndex4ES(bean.getQueryTime(), province);
		
		
		// 设置查询条件
		JSONObject jsonParams = new JSONObject();
		jsonParams.put("from", 0);
		jsonParams.put("size", 0);
		jsonParams.put("explain", false);
		//组装聚合桶过滤条件
        JSONObject aggs = new JSONObject();//最外层桶
        JSONObject aggFilter = new JSONObject();
        JSONObject andFilter = new JSONObject();//桶条件
        JSONArray filters = new JSONArray();
        filters.add(JSONObject.fromObject("{'range':{'test_time': {'from': '"+stime+"','to': '"+etime+"','include_lower': true,'include_upper': true}}}"));
        //排除没有归属到道路点的采样点
        filters.add(JSONObject.fromObject("{'not': {'terms': {'point_code': ['']}}}"));
        
		// 运营商
		if (null != operator) {
			if (operator.length() == 1 && !operator.equals("-1")) { // 单个运营商
				filters.add(JSONObject.fromObject("{'terms': {'telecoms': ['"+operator+"']}}"));
			} else if (operator.length() == 3) {// 权限范围内,全部运营商(形如:010,第一位：联通；第二位：移动；第三位：电信)1,有0,无
				String authOpt = "";
				authOpt += operator.substring(0, 1).equals("1") ? "1," : "";
				authOpt += operator.substring(1, 2).equals("1") ? "2," : "";
				authOpt += operator.substring(2, 3).equals("1") ? "3," : "";
				if (!authOpt.equals("")) {
					String opt[] = authOpt.split(",");
					switch (opt.length) {
					case 1:
						filters.add(JSONObject.fromObject("{'terms': {'telecoms': ['"+opt[0]+"']}}"));
						break;
					case 2:
						filters.add(JSONObject.fromObject("{'terms': {'telecoms': ['"+opt[0]+"','"+opt[1]+"']}}"));
						break;
					case 3:
						filters.add(JSONObject.fromObject("{'terms': {'telecoms': ['"+opt[0]+"','"+opt[1]+"','"+opt[2]+"']}}"));
						break;
					default:
						break;
					}
				}
			}
			// 区域
			if (null != area && !area.equals("-1")) {
				filters.add(JSONObject.fromObject("{'prefix': {'region_code': '"+area+"'}}"));
			} else if (null != district && !district.equals("-1")) {
				filters.add(JSONObject.fromObject("{'prefix': {'region_code': '"+district.substring(0, 4)+"'}}"));
			} else if (null != province && !province.equals("-1")) {
				filters.add(JSONObject.fromObject("{'prefix': {'region_code': '"+province.substring(0, 2)+"'}}"));
			}
		}
		// 道路等级
		if (null != roadLevel && roadLevel != -1) {
			filters.add(JSONObject.fromObject("{'term': {'road_level': "+roadLevel+"}}"));
		}
		// 道路类型
		if (null != roadType && roadType != -1) {
			filters.add(JSONObject.fromObject("{'term': {'road_type': "+roadType+"}}"));
		}
		// 道路ID
		if (null != roadId && !roadId.equals("-1")) {
			filters.add(JSONObject.fromObject("{'term': {'road_id': '"+roadId+"'}}"));
		}
		//IMEI
		if(null != imei && !imei.equals("")){
			filters.add(JSONObject.fromObject("{'term': {'imei': '"+imei+"'}}"));
		}
		
		andFilter.put("and", filters);
		aggFilter.put("filter", andFilter);
		
		//组装过滤桶
		StringBuffer temp = new StringBuffer();
		
		temp.append("{\"timeAgg\":{\"terms\":{\"script\":\"");
		
		// 时间维度统计
		if (timeAgg == 1) {
			String st = null != bean.getTimegransel() && bean.getTimegransel().equals("3")?times.get("starttime")+"-01":times.get("starttime");
			temp.append("floor((((new Date().parse('yyyy-MM-dd',new Date(doc['test_time'].value).format('yyyy-MM-dd'))");
			temp.append(".getTime())-(new Date().parse('yyyy-MM-dd','"+st+"').getTime()))/(1000*3600*24))/");
			int days = 0;
			switch (bean.getTimegransel()) {
			case "1":// 天
				days = 1;
				break;
			case "2":// 周
				days = 7;
				break;
			case "3":// 月
				days = 30;
				break;
			default:
				break;
			}
			temp.append(days+")");
		} else {
			temp.append("9999");
		}
		temp.append("\",\"size\":2147483647},\"aggs\":{\"areaAgg\":{\"terms\":{\"script\":\"");
		
		//区域维度统计
		if (areaAgg == 1) {
			temp.append("doc['region_code'].value");
		}else{
			temp.append("'-999'");
		}
		temp.append("\",\"size\":2147483647},\"aggs\":{\"roadAgg\":{\"terms\":{\"script\":\"");
		
		//道路维度统计
		if(roadAgg == 1){
			temp.append("doc['road_id'].value");
		}else{
			temp.append("'-999'");
		}
		temp.append("\",\"size\":2147483647},\"aggs\":{\"operatorAgg\":{\"terms\":{\"script\":\"");
		//运营商维度统计
		if(operatorAgg == 1){
			temp.append("doc['telecoms'].value");
		}else{
			temp.append("'-999'");
		}
		
        temp.append("\",\"size\":2147483647},\"aggs\":{\"roadPointAgg\":{\"terms\":{\"script\":\"");
		
		temp.append("doc['point_code'].value + doc['telecoms'].value");
		
		if(areaAgg==1){
			temp.append(" + doc['region_code'].value ");
		}
		
		temp.append("\",\"size\":2147483647},\"aggs\":{");
		/**指标区间统计组装     开始**/
		
		
		/**rsrp组装     开始**/
		
		if(rsrpItl == 1){
			List<Map<String, String>> list = itlHander(kpiInfos.get(0));
			for(int i=0;i<list.size();i++){
				String stv = list.get(i).get("val_st").toString();
				String edv = list.get(i).get("val_ed").toString();
				String opt1 = list.get(i).get("val_opt1").toString();
				String opt2 = list.get(i).get("val_opt2").toString();
				
				temp.append("\"rsrp_itl_"+(i+1)+"\":{\"bucket_script\":{\"buckets_path\":{\"itl_val\":\"rsrp_avg2\"},");
				temp.append("\"script\":\"if(itl_val "+opt1+" "+stv+" && itl_val "+opt2+" "+edv+"){return 1;}else{return 0;}\"}},");
				temp.append("\"rsrp_unitl_"+(i+1)+"\":{\"bucket_script\":{\"buckets_path\":{\"itl_val\":\"rsrp_avg2\"},");
				temp.append("\"script\":\"if(itl_val "+opt1+" "+stv+" && itl_val "+opt2+" "+edv+"){return 0;}else{return 1;}\"}},");
			}
		}
		
		/**rsrp组装     结束**/
		
		
		/**snr组装     开始**/
		
		if(snrItl == 1){
			List<Map<String, String>> list = itlHander(kpiInfos.get(1));
			for(int i=0;i<list.size();i++){
				String stv = list.get(i).get("val_st").toString();
				String edv = list.get(i).get("val_ed").toString();
				String opt1 = list.get(i).get("val_opt1").toString();
				String opt2 = list.get(i).get("val_opt2").toString();
				
				temp.append("\"snr_itl_"+(i+1)+"\":{\"bucket_script\":{\"buckets_path\":{\"itl_val\":\"rsrp_avg2\"},");
				temp.append("\"script\":\"if(itl_val "+opt1+" "+stv+" && itl_val "+opt2+" "+edv+"){return 1;}else{return 0;}\"}},");
				temp.append("\"snr_unitl_"+(i+1)+"\":{\"bucket_script\":{\"buckets_path\":{\"itl_val\":\"snr_avg2\"},");
				temp.append("\"script\":\"if(itl_val "+opt1+" "+stv+" && itl_val "+opt2+" "+edv+"){return 0;}else{return 1;}\"}},");
			}
		}
		
		/**snr组装     结束**/
		
		
		
		/**rsrq组装     开始**/
		
		if(rsrqItl == 1){
			List<Map<String, String>> list = itlHander(kpiInfos.get(2));
			for(int i=0;i<list.size();i++){
				String stv = list.get(i).get("val_st").toString();
				String edv = list.get(i).get("val_ed").toString();
				String opt1 = list.get(i).get("val_opt1").toString();
				String opt2 = list.get(i).get("val_opt2").toString();
				
				temp.append("\"rsrq_itl_"+(i+1)+"\":{\"bucket_script\":{\"buckets_path\":{\"itl_val\":\"rsrp_avg2\"},");
				temp.append("\"script\":\"if(itl_val "+opt1+" "+stv+" && itl_val "+opt2+" "+edv+"){return 1;}else{return 0;}\"}},");
				temp.append("\"rsrq_unitl_"+(i+1)+"\":{\"bucket_script\":{\"buckets_path\":{\"itl_val\":\"aggRsrqFilter>rsrq_avg2\"},");
				temp.append("\"script\":\"if(itl_val "+opt1+" "+stv+" && itl_val "+opt2+" "+edv+"){return 0;}else{return 1;}\"}},");
			}
		}
		
		/**rsrq组装     结束**/
		
		
		/**下行链路带宽组装     开始**/
		
		if(httpItl == 1){
			List<Map<String, String>> list = itlHander(kpiInfos.get(3));
			for(int i=0;i<list.size();i++){
				String stv = list.get(i).get("val_st").toString();
				String edv = list.get(i).get("val_ed").toString();
				String opt1 = list.get(i).get("val_opt1").toString();
				String opt2 = list.get(i).get("val_opt2").toString();
				
				temp.append("\"http_itl_"+(i+1)+"\":{\"bucket_script\":{\"buckets_path\":{\"itl_val\":\"rsrp_avg2\"},");
				temp.append("\"script\":\"if(itl_val "+opt1+" "+stv+" && itl_val "+opt2+" "+edv+"){return 1;}else{return 0;}\"}},");
				temp.append("\"http_unitl_"+(i+1)+"\":{\"bucket_script\":{\"buckets_path\":{\"itl_val\":\"aggHttpFilter>http_avg2\"},");
				temp.append("\"script\":\"if(itl_val "+opt1+" "+stv+" && itl_val "+opt2+" "+edv+"){return 0;}else{return 1;}\"}},");
			}
		}
		
		/**下行链路带宽组装     结束**/
		
		/**延时组装     开始**/
		
		if(delayItl == 1){
			List<Map<String, String>> list = itlHander(kpiInfos.get(4));
			for(int i=0;i<list.size();i++){
				String stv = list.get(i).get("val_st").toString();
				String edv = list.get(i).get("val_ed").toString();
				String opt1 = list.get(i).get("val_opt1").toString();
				String opt2 = list.get(i).get("val_opt2").toString();
				
				temp.append("\"delay_itl_"+(i+1)+"\":{\"bucket_script\":{\"buckets_path\":{\"itl_val\":\"rsrp_avg2\"},");
				temp.append("\"script\":\"if(itl_val "+opt1+" "+stv+" && itl_val "+opt2+" "+edv+"){return 1;}else{return 0;}\"}},");
				temp.append("\"delay_unitl_"+(i+1)+"\":{\"bucket_script\":{\"buckets_path\":{\"itl_val\":\"delayFilter>delay_avg2\"},");
				temp.append("\"script\":\"if(itl_val "+opt1+" "+stv+" && itl_val "+opt2+" "+edv+"){return 0;}else{return 1;}\"}},");
			}
		}
		
		/**延时组装     结束**/
		
		
		/**丢包组装     开始**/
		
		if(lostItl == 1){
			List<Map<String, String>> list = itlHander(kpiInfos.get(5));
			for(int i=0;i<list.size();i++){
				String stv = list.get(i).get("val_st").toString();
				String edv = list.get(i).get("val_ed").toString();
				String opt1 = list.get(i).get("val_opt1").toString();
				String opt2 = list.get(i).get("val_opt2").toString();
				
				temp.append("\"lost_itl_"+(i+1)+"\":{\"bucket_script\":{\"buckets_path\":{\"itl_val\":\"rsrp_avg2\"},");
				temp.append("\"script\":\"if(itl_val "+opt1+" "+stv+" && itl_val "+opt2+" "+edv+"){return 1;}else{return 0;}\"}},");
				temp.append("\"lost_unitl_"+(i+1)+"\":{\"bucket_script\":{\"buckets_path\":{\"itl_val\":\"loseFilter>lost_avg2\"},");
				temp.append("\"script\":\"if(itl_val "+opt1+" "+stv+" && itl_val "+opt2+" "+edv+"){return 0;}else{return 1;}\"}},");
			}
		}
		
		/**丢包组装     结束**/
		
		/**指标区间统计组装     结束**/
		temp.append("\"rsrp_avg2\":{\"avg\":{\"field\":\"lte_rsrp\"}},\"aggRsrqFilter\":{\"filter\":{\"not\":{\"filter\":{\"terms\":");
		temp.append("{\"lte_rsrq\":[\"-999\"]}}}},\"aggs\":{\"rsrq_avg2\":{\"avg\":{\"field\":\"lte_rsrq\"}}}},\"snr_avg2\":");
		temp.append("{\"avg\":{\"field\":\"lte_snr\"}},\"aggHttpFilter\":{\"filter\":{\"not\":{\"filter\":{\"terms\"");
		temp.append(":{\"bw_link\":[\"-998\"]}}}},\"aggs\":{\"http_avg2\":{\"avg\":{\"field\":\"bw_link\"}}}},");
		temp.append("\"delayFilter\":{\"filter\":{\"not\":{\"filter\":{\"terms\":{\"ping_avg_delay\":[\"-998\"]}}}}");
		temp.append(",\"aggs\":{\"delay_avg2\":{\"avg\":{\"field\":\"ping_avg_delay\"}}}},\"loseFilter\":{\"filter\":");
		temp.append("{\"not\":{\"filter\":{\"terms\":{\"ping_lose_rate\":[\"-998\"]}}}},\"aggs\":{\"lost_avg2\":{\"avg\":");
		temp.append("{\"field\":\"ping_lose_rate\"}}}},\"if_bigRate\":{\"bucket_script\":{\"buckets_path\":{\"http_val\":\"");
		temp.append("aggHttpFilter>http_avg2\"},\"script\":\"if(http_val>=12288){return 1;}else{return 0;}\"}},");
		temp.append("\"if_unbigRate\":{\"bucket_script\":{\"buckets_path\":{\"http_val\":\"aggHttpFilter>http_avg2\"");
		temp.append("},\"script\":\"if(http_val>=12288){return 0;}else{return 1;}\"}},\"if_cover\":{\"bucket_script\":");
		temp.append("{\"buckets_path\":{\"rsrp_val\":\"rsrp_avg2\",\"snr_val\":\"snr_avg2\"},\"script\":\"if(rsrp_val>=-105 && ");
		temp.append("snr_val>=-3){return 1;}else{return 0;}\"}},\"if_uncover\":{\"bucket_script\":{\"buckets_path\":{\"rsrp_val\":");
		temp.append("\"rsrp_avg2\",\"snr_val\":\"snr_avg2\"},\"script\":\"if(rsrp_val>=-105 && snr_val>=-3){return 0;}");
		temp.append("else{return 1;}\"}},\"netdrops_count2\":{\"sum\":{\"script\":\"if(doc['network_event'].value");
		temp.append(" == 1 || doc['network_event'].value == 3){return 1} else {return 0}\"}},\"unnetdrops_count2\":");
		temp.append("{\"sum\":{\"script\":\"if((doc['network_event'].value == 1 || doc['network_event'].value == 3)&&doc['network_event'].value != -999)");
		temp.append("{return 0} else {return 1}\"}},\"netdrops_lv2\":{\"bucket_script\":{\"buckets_path\":");
		temp.append("{\"mc\":\"netdrops_count2\",\"unmc\":\"unnetdrops_count2\"},\"script\":\"mc / (mc+unmc)\"}},");
		temp.append("\"busdrops_count2\":{\"sum\":{\"script\":\"if(doc['ping_event'].value == 1 ){return 1} else {return ");
		temp.append("0}\"}},\"unbusdrops_count2\":{\"sum\":{\"script\":\"if(doc['ping_event'].value == 0 ){return 1} else");
		temp.append(" {return 0}\"}},\"busdrops_lv2\":{\"bucket_script\":{\"buckets_path\":{\"mc\":\"busdrops_count2\",\"unmc\"");
		temp.append(":\"unbusdrops_count2\"},\"script\":\"mc / (mc+unmc)\"}}}},");
		
		/**指标区间统计组装     开始**/
		
		
		/**rsrp组装     开始**/
		
		if(rsrpItl == 1){
			for(int i=0;i<6;i++){
				temp.append("\"rsrp_count_"+(i+1)+"\":{\"sum_bucket\":{\"buckets_path\":\"roadPointAgg>rsrp_itl_"+(i+1)+"\"}},");
				temp.append("\"rsrp_un_count_"+(i+1)+"\":{\"sum_bucket\":{\"buckets_path\":\"roadPointAgg>rsrp_unitl_"+(i+1)+"\"}},");
				temp.append("\"rsrp_interval_"+(i+1)+"\":{\"bucket_script\":{\"buckets_path\":{\"mc\":\"rsrp_count_"+(i+1)+"\",");
				temp.append("\"unmc\":\"rsrp_un_count_"+(i+1)+"\"},\"script\":\"mc / (mc+unmc)\"}},");
			}
		}
		
		/**rsrp组装     结束**/
		
		
		/**snr组装     开始**/
		
		if(snrItl == 1){
			for(int i=0;i<6;i++){
				temp.append("\"snr_count_"+(i+1)+"\":{\"sum_bucket\":{\"buckets_path\":\"roadPointAgg>snr_itl_"+(i+1)+"\"}},");
				temp.append("\"snr_un_count_"+(i+1)+"\":{\"sum_bucket\":{\"buckets_path\":\"roadPointAgg>snr_unitl_"+(i+1)+"\"}},");
				temp.append("\"snr_interval_"+(i+1)+"\":{\"bucket_script\":{\"buckets_path\":{\"mc\":\"snr_count_"+(i+1)+"\",");
				temp.append("\"unmc\":\"snr_un_count_"+(i+1)+"\"},\"script\":\"mc / (mc+unmc)\"}},");
			}
		}
		
		/**snr组装     结束**/
		
		
		
		/**rsrq组装     开始**/
		
		if(rsrqItl == 1){
			for(int i=0;i<6;i++){
				temp.append("\"rsrq_count_"+(i+1)+"\":{\"sum_bucket\":{\"buckets_path\":\"roadPointAgg>rsrq_itl_"+(i+1)+"\"}},");
				temp.append("\"rsrq_un_count_"+(i+1)+"\":{\"sum_bucket\":{\"buckets_path\":\"roadPointAgg>rsrq_unitl_"+(i+1)+"\"}},");
				temp.append("\"rsrq_interval_"+(i+1)+"\":{\"bucket_script\":{\"buckets_path\":{\"mc\":\"rsrq_count_"+(i+1)+"\",");
				temp.append("\"unmc\":\"rsrq_un_count_"+(i+1)+"\"},\"script\":\"mc / (mc+unmc)\"}},");
			}
		}
		
		/**rsrq组装     结束**/
		
		
		/**下行链路带宽组装     开始**/
		
		if(httpItl == 1){
			for(int i=0;i<6;i++){
				temp.append("\"http_count_"+(i+1)+"\":{\"sum_bucket\":{\"buckets_path\":\"roadPointAgg>http_itl_"+(i+1)+"\"}},");
				temp.append("\"http_un_count_"+(i+1)+"\":{\"sum_bucket\":{\"buckets_path\":\"roadPointAgg>http_unitl_"+(i+1)+"\"}},");
				temp.append("\"http_interval_"+(i+1)+"\":{\"bucket_script\":{\"buckets_path\":{\"mc\":\"http_count_"+(i+1)+"\",");
				temp.append("\"unmc\":\"http_un_count_"+(i+1)+"\"},\"script\":\"mc / (mc+unmc)\"}},");
			}
		}
		
		/**下行链路带宽组装     结束**/
		
		/**延时组装     开始**/
		
		if(delayItl == 1){
			for(int i=0;i<6;i++){
				temp.append("\"delay_count_"+(i+1)+"\":{\"sum_bucket\":{\"buckets_path\":\"roadPointAgg>delay_itl_"+(i+1)+"\"}},");
				temp.append("\"delay_un_count_"+(i+1)+"\":{\"sum_bucket\":{\"buckets_path\":\"roadPointAgg>delay_unitl_"+(i+1)+"\"}},");
				temp.append("\"delay_interval_"+(i+1)+"\":{\"bucket_script\":{\"buckets_path\":{\"mc\":\"delay_count_"+(i+1)+"\",");
				temp.append("\"unmc\":\"delay_un_count_"+(i+1)+"\"},\"script\":\"mc / (mc+unmc)\"}},");
			}
		}
		
		/**延时组装     结束**/
		
		
		/**丢包组装     开始**/
		
		if(lostItl == 1){
			for(int i=0;i<6;i++){
				temp.append("\"lost_count_"+(i+1)+"\":{\"sum_bucket\":{\"buckets_path\":\"roadPointAgg>lost_itl_"+(i+1)+"\"}},");
				temp.append("\"lost_un_count_"+(i+1)+"\":{\"sum_bucket\":{\"buckets_path\":\"roadPointAgg>lost_unitl_"+(i+1)+"\"}},");
				temp.append("\"lost_interval_"+(i+1)+"\":{\"bucket_script\":{\"buckets_path\":{\"mc\":\"lost_count_"+(i+1)+"\",");
				temp.append("\"unmc\":\"lost_un_count_"+(i+1)+"\"},\"script\":\"mc / (mc+unmc)\"}},");
			}
		}
		
		/**丢包组装     结束**/
		
		/**指标区间统计组装     结束**/
		
		
		temp.append("\"signal\":{\"value_count\":{\"field\":\"id\"}},");
		temp.append("\"busCount\":{\"sum\":{\"script\":\"if(doc['bw_link'].value>-998){return 1;}else{return 0;}\"}},");
		temp.append("\"testMileage\":{\"sum\":{\"field\":\"last_distance\"}},\"coverMileage\":{\"bucket_script\":{\"buckets_path\":");
		temp.append("{\"mc\": \"cover_count\",\"unmc\": \"uncover_count\"},\"script\": \"mc+unmc\"}},");
		temp.append("\"weakCoverMileage\":{\"sum_bucket\":{\"buckets_path\":\"roadPointAgg>if_uncover\"}},");
		temp.append("\"cover_count\":{\"sum_bucket\":{\"buckets_path\":\"roadPointAgg>if_cover\"}},\"uncover_count\":");
		temp.append("{\"sum_bucket\":{\"buckets_path\":\"roadPointAgg>if_uncover\"}},\"roadCoverRate\":{\"bucket_script\":");
		temp.append("{\"buckets_path\":{\"mc\":\"cover_count\",\"unmc\":\"uncover_count\"},\"script\":\"mc / ");
		temp.append("(mc+unmc)\"}},\"netdrops_lv\":{\"avg_bucket\":{\"buckets_path\":\"roadPointAgg>netdrops_lv2\"}},");
		temp.append("\"busdrops_lv\":{\"avg_bucket\":{\"buckets_path\":\"roadPointAgg>busdrops_lv2\"}},");
		temp.append("\"netdrops_count\":{\"sum\":{\"script\":\"if(doc['network_event'].value == 1 || doc['network_event'].value == 3)");
		temp.append("{return 1} else {return 0}\"}},\"busdrops_count\":{\"sum\":{\"script\":\"if(doc['ping_event'].value == 1 )");
		temp.append("{return 1} else {return 0}\"}},\"4gto3g_count\":{\"sum\":{\"script\":\"if(doc['network_event'].value == 5 )");
		temp.append("{return 1} else {return 0}\"}},\"rsrp_avg\":{\"avg_bucket\":{\"buckets_path\":\"roadPointAgg>rsrp_avg2\"}},");
		temp.append("\"rsrq_avg\":{\"avg_bucket\":{\"buckets_path\":\"roadPointAgg>aggRsrqFilter>rsrq_avg2\"}},\"snr_avg\":");
		temp.append("{\"avg_bucket\":{\"buckets_path\":\"roadPointAgg>snr_avg2\"}},\"http_avg\":{\"avg_bucket\":");
		temp.append("{\"buckets_path\":\"roadPointAgg>aggHttpFilter>http_avg2\"}},\"bigRate_count\":{\"sum_bucket\":");
		temp.append("{\"buckets_path\":\"roadPointAgg>if_bigRate\"}},\"unbigRate_count\":{\"sum_bucket\":{\"buckets_path\"");
		temp.append(":\"roadPointAgg>if_unbigRate\"}},\"bigRate\":{\"bucket_script\":{\"buckets_path\":");
		temp.append("{\"mc\":\"bigRate_count\",\"unmc\":\"unbigRate_count\"},\"script\":\"mc / (mc+unmc)\"}},");
		temp.append("\"delay_avg\":{\"avg_bucket\":{\"buckets_path\":\"roadPointAgg>delayFilter>delay_avg2\"}},");
		temp.append("\"lost_avg\":{\"avg_bucket\":{\"buckets_path\":\"roadPointAgg>loseFilter>lost_avg2\"}},");
		temp.append("\"startTime\":{\"min\":{\"field\":\"test_time\",\"format\":\"yyyy-MM-dd\"}},\"endTime\":");
		temp.append("{\"max\":{\"field\":\"test_time\",\"format\":\"yyyy-MM-dd\"}}}}}}}}}}}}}}");
		
		aggFilter.put("aggs",JSONObject.fromObject(temp.toString()));
		
		
		aggs.put("aggFilter", aggFilter);
		
		//最终组合
        jsonParams.put("aggs", aggs);
        
        
        String re = ESConnUtils.querESByJson(indexList, null, jsonParams.toString());
        
       //删除多余的桶内容
       re = re.replaceAll("\"roadPointAgg\".*?]},", "");
       System.out.println("ES返回处理后结果:"+re);
	   return re;
	}
	
	private List<Map<String,String>> itlHander(KpiSettings kpiSettings) throws Exception{
		Class c = kpiSettings.getClass();
		
		//获取6个区间开始值和结束值还有符号
		List<Map<String,String>> re = new ArrayList<>();
		for(int i=0;i<6;i++){
			Map<String,String> map = new HashMap<>();
			String index = i==0?"one":i==1?"two":i==2?"three":i==3?"four":i==4?"five":i==5?"six":"";
			Method stvalMed = c.getMethod("getRange_"+index+"_start");//区间起始值
			Method edvalMed = c.getMethod("getRange_"+index+"_end");//区间结束值
			Method sevalMed = c.getMethod("getRange_"+index+"_state");//区间状态
			//执行方法
			String val_st = (String) stvalMed.invoke(kpiSettings);
			String val_ed = (String) edvalMed.invoke(kpiSettings);
			int val_se = (int) sevalMed.invoke(kpiSettings);
			String val_opt1 = "";
			String val_opt2 = "";
			switch (val_se) {
			case 1:
				val_opt1=">";
				val_opt2="<";
				break;
			case 2:
				val_opt1=">=";
				val_opt2="<=";
				break;
			case 3:
				val_opt1=">";
				val_opt2="<=";
				break;
			case 4:
				val_opt1=">=";
				val_opt2="<";
				break;
			}
			map.put("val_st", val_st);
			map.put("val_ed", val_ed);
			map.put("val_opt1", val_opt1);
			map.put("val_opt2", val_opt2);
			re.add(map);
		}
		
		return re;
	}

}
