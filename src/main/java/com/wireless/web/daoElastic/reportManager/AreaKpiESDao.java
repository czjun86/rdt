/**     
* @文件名称: AreaKpiESDao.java  
* @类路径: com.wireless.web.dao.reportManager  
* @描述: TODO  
* @作者：tc  
* @时间：2015年12月7日 下午8:56:48  
* @版本：V1.0     
*/
package com.wireless.web.daoElastic.reportManager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.stereotype.Service;

import com.wireless.web.daoElastic.utils.ESConnUtils;
import com.wireless.web.daoElastic.utils.StringUtils;
import com.wireless.web.model.AreaKpi4ESBean;
import com.wireless.web.model.AreaKpiBean;
import com.wireless.web.model.QueryBean;
import com.wireless.web.utils.NumUtils;
import com.wireless.web.utils.TimeUtils;
import com.wireless.web.utils.WebConstants;

/**  
 * @类功能说明：  区域KPI排名 ES DAO类
 * @类修改者：  
 * @修改日期：  
 * @修改说明：  
 * @公司名称：  
 * @作者：tc  
 * @创建时间：2015年12月7日 下午8:56:48  
 * @版本：V1.0  
 */

@Service("areaKpiESDao")
public class AreaKpiESDao {
	/**
	 * 
	* 函数功能说明    根据条件查询区域KPI排名
	* tc  2015年12月7日 
	* 修改者名字修改日期 
	* 修改内容 
	* @参数： @param bean
	* @参数： @return
	* @参数： @throws Exception   
	* @throws
	 */
	public List<Map<String ,Object>> queryAreaKpiByES(QueryBean bean)  throws Exception{
		List<Map<String ,Object>> reMap = new ArrayList<Map<String ,Object>>();
		
		//转化查询条件
		Map<String ,String> times = TimeUtils.getSETime(bean.getQueryTime());
		String stime = TimeUtils.chDate(times.get("starttime")+" 00:00:00", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		String etime = TimeUtils.chDate(times.get("endtime")+" 23:59:59", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		String regCode = "";
		String pro = bean.getProvince();//一级区域
		String dis = bean.getDistrict();//二级区域
		String area = bean.getArea();//三级区域
		if(dis.equals("-1")){
			regCode = pro.substring(0, 2);
		}else if(area.equals("-1")){
			regCode = dis.substring(0, 4);
		}else{
			regCode = area;
		}
		//根据查询时间，计算索引
		List<String> indexList = StringUtils.findIndex4ES(bean.getQueryTime(), pro);
		//加入查询条件
        JSONObject params = new JSONObject();
        params.put("from", 0);
        params.put("size", 0);
        params.put("explain", false);
        params.put("query", JSONObject.fromObject("{'prefix': {'region_code': '"+regCode+"'}}"));
        
        //单独组装聚合桶
        JSONObject aggs = new JSONObject();
        JSONObject aggFilter = new JSONObject();  //size 2147483647
        aggFilter.put("filter", JSONObject.fromObject("{'and': {'filters': [{'range':{'test_time': {'from': '"+stime+"','to': '"
        +etime+"','include_lower': true,'include_upper': true}}},{'not': {'terms': {'point_code': ['']}}}]}}"));
        
        StringBuffer temp = new StringBuffer();
        temp.append("{\"regAgg\":{\"terms\":{\"field\":\"region_code\",\"size\":2147483647},\"aggs\":{\"tmsAgg\":{\"terms\":{");
        temp.append("\"field\":\"telecoms\",\"size\":2147483647},\"aggs\":{\"roadAgg\":{\"terms\":{\"script\"");
        temp.append(":\"doc['point_code'].value + doc['region_code'].value + ");
        temp.append(" doc['telecoms'].value\",\"size\":2147483647},\"aggs\":{\"rsrp_avg2\":{\"avg\":{\"field\"");
        temp.append(":\"lte_rsrp\"}},\"snr_avg2\":{\"avg\":{\"field\":\"lte_snr\"}},");
        temp.append("\"if_cover\":{\"bucket_script\":{\"buckets_path\":{\"rsrp_val\":\"rsrp_avg2\",\"snr_val\":\"snr_avg2\"}");
        temp.append(",\"script\":\"if(rsrp_val>=-105 && snr_val>=-3){return 1;}else{return 0;}\"}},\"if_uncover\":");
        temp.append("{\"bucket_script\":{\"buckets_path\":{\"rsrp_val\":\"rsrp_avg2\",\"snr_val\"");
        temp.append(":\"snr_avg2\"},\"script\":\"if(rsrp_val>=-105 && snr_val>=-3){return 0;}else{return 1;}\"}},");
        temp.append("\"netdrops_count2\":{\"sum\":{\"script\":\"if(doc['network_event'].value == ");
        temp.append("1 || doc['network_event'].value == 3){return 1} else {return 0}\"}},\"unnetdrops_count2\"");
        temp.append(":{\"sum\":{\"script\":\"if(doc['network_event'].value == 1 || doc['network_event'].value == 3&&doc['network_event'].value != -999){return 0} else {return 1}\"}},");
        temp.append("\"netdrops_lv2\":{\"bucket_script\":{\"buckets_path\":{\"mc\":\"netdrops_count2\",\"unmc\":\"unnetdrops_count2\"},\"script\":\"mc / (mc+unmc)\"}},");
        temp.append("\"busdrops_count2\":{\"sum\":{\"script\":\"if(doc['ping_event'].value == 1 ){return 1} else {return 0}\"}},\"unbusdrops_count2\"");
        temp.append(":{\"sum\":{\"script\":\" if(doc['ping_event'].value == 0 ){return 1} else {return 0}\"}},");
        temp.append("\"busdrops_lv2\":{\"bucket_script\":{\"buckets_path\":{\"mc\":\"busdrops_count2\",\"unmc\"");
        temp.append(" :\"unbusdrops_count2\"},\"script\":\"mc / (mc+unmc)\"}},\"aggHttpFilter\":{\"filter\":");
        temp.append("{\"not\":{\"filter\":{\"terms\":{\"bw_link\":[\"-998\"]}}}},\"aggs\":{\"http_avg2\":{\"avg\"");
        temp.append(":{\"field\":\"bw_link\"}}}}}},\"regName\":{\"terms\":{\"field\":\"region_name\"}},\"rsrp_avg\":");
        temp.append("{\"avg_bucket\":{\"buckets_path\":\"roadAgg>rsrp_avg2\"}},\"snr_avg\":{\"avg_bucket\":{\"");
        temp.append("buckets_path\":\"roadAgg>snr_avg2\"}},\"http_avg\":{\"avg_bucket\":{\"buckets_path\":\"roadAgg>");
        temp.append("aggHttpFilter>http_avg2\"}},\"cover_count\":{\"sum_bucket\":{\"buckets_path\":\"roadAgg>if_cover\"}},");
        temp.append("\"uncover_count\":{\"sum_bucket\":{\"buckets_path\":\"roadAgg>if_uncover\"}},");
        temp.append("\"cover_lv\":{\"bucket_script\":{\"buckets_path\":{\"mc\":\"cover_count\",");
        temp.append("\"unmc\":\"uncover_count\"},\"script\":\"mc / (mc+unmc)\"}},");
        temp.append("\"netdrops_lv\":{\"avg_bucket\":{\"buckets_path\":\"roadAgg>netdrops_lv2\"}},");
        temp.append("\"busdrops_lv\":{\"avg_bucket\":{\"buckets_path\":\"roadAgg>busdrops_lv2\"}}}}}}}");
        
        aggFilter.put("aggs", JSONObject.fromObject(temp.toString()));
        
        aggs.put("aggFilter", aggFilter);
        
        //最终组合
        params.put("aggs", aggs);

        
        //结果
        List<AreaKpiBean> list = new ArrayList<AreaKpiBean>(); 
		//指标类型   
		int kpiType = bean.getKpiType();
		
		
		/**esAPI方式获取结果开始**/
	     //获取客户端
//	     TransportClient client = ESConnUtils.getTransportClient();
//	     //组合索引
//	     String[] indexs = new String[indexList.size()];
//	     for(int i=0;i<indexList.size();i++){
//	      indexs[i]=indexList.get(i);
//	     }
//	     SearchResponse sr = client.prepareSearch(indexs).setSource(params.toString()).execute().actionGet();
		/**esAPI方式获取结果结束**/
		
	     
		/**http方式获取结果开始**/
        String re = ESConnUtils.querESByJson(indexList, null, params.toString());
		//解析json结果
		list = parseByJson(re, kpiType);
		/**http方式获取结果结束**/
		
		//分页处理
		int pageIndex = bean.getPageIndex()==null?WebConstants.PAGE_DEFAULT_PAGEINDEX:bean.getPageIndex();//当前页数
		int pageSize = bean.getPageSize()==null?WebConstants.PAGE_DEFAULT_PAGESIZE:bean.getPageSize();//每页数据量
		int stIndex = 0;
		int edIndex = list.size();
		if(pageIndex!=-1&&pageSize!=-1){//分页条件
			stIndex = pageIndex==1?0:(pageIndex-1)*pageSize;//起始下标
			edIndex = stIndex+pageSize;//结束下标
		}
		
		for(int i=stIndex;i<edIndex&&i<list.size();i++){
			AreaKpiBean ab = list.get(i);
			Map<String ,Object> m = new HashMap<String, Object>();
			m.put("cityName", ab.getCname1());//一级区域
			if(ab.getCname2().equals("市辖区")){
				m.put("countryName", ab.getCname3());//三级区域
			}else{
				m.put("countryName", ab.getCname2());//二级区域
			}
			m.put("district", ab.getCname3());//三级区域
			//运营商
			m.put("telecoms1", ab.getTel1());
			m.put("telecoms2", ab.getTel2());
			m.put("telecoms3", ab.getTel3());
			//KPI值
			m.put("kpiValue1", ab.getVal1());
			m.put("kpiValue2", ab.getVal2());
			m.put("kpiValue3", ab.getVal3());
			reMap.add(m);
		}
		
		//返回分页信息
		if(pageIndex!=-1&&pageSize!=-1){//分页条件
			// 计算总页数
			int pageCount = list.size() / pageSize;
			if (list.size() % pageSize > 0) {
				pageCount++;
			}
			
			Map<String ,Object> pagem = new HashMap<>();
			pagem.put("pageIndex", pageIndex);//当前页
			pagem.put("pageSize", pageSize);//每页数据量
			pagem.put("totalPage", pageCount==0?1:pageCount);//总页数
			pagem.put("totalCount", list.size());//总行数
			
			reMap.add(pagem);
		}
        
        return reMap;
    }
	
	
	/**
	 * 
	* 函数功能说明    根据返回的json字符串解析结果
	* tc  2016年1月6日 
	* 修改者名字修改日期 
	* 修改内容 
	* @参数： @param re  返回的结果
	* @参数： @param kpiType  指标类型
	* @参数： @return
	* @参数： @throws Exception   
	* @throws
	 */
	private List<AreaKpiBean> parseByJson(String re,int kpiType) throws Exception{
		List<AreaKpiBean> list = new ArrayList<AreaKpiBean>();
		//删除多余的桶内容
        re = re.replaceAll("\"roadAgg\".*?]},", "");
        
        JSONObject results = JSONObject.fromObject(re);
        
        JSONArray regBuckets = results.getJSONObject("aggregations").getJSONObject("aggFilter").getJSONObject("regAgg").getJSONArray("buckets");
        for(int i=0;i<regBuckets.size();i++){
        	JSONArray tmsBuckets = regBuckets.getJSONObject(i).getJSONObject("tmsAgg").getJSONArray("buckets");
        	List<AreaKpi4ESBean> l = new ArrayList<AreaKpi4ESBean>();//每个区域下所有运营商数据集合
        	//保存每个区域完整的名称
			String area1 = "";//一级区域
			String area2 = "";//二级区域
			String area3 = "";//三级区域
			int telFlag = 0;
        	for(int k=0;k<tmsBuckets.size();k++){
        		AreaKpi4ESBean akes = new AreaKpi4ESBean();
        		JSONObject tmsBucket = tmsBuckets.getJSONObject(k);
        		//获取运营商名称
				String tmsId = tmsBucket.getString("key");  //运营商编号
				if(tmsId.equals("1")){
					akes.setTel("联通");
					telFlag+=1;
				}else if(tmsId.equals("2")){
					akes.setTel("移动");
					telFlag+=2;
				}else if(tmsId.equals("3")){
					akes.setTel("电信");
					telFlag+=4;
				}
				
				//获取区域名称
				String regName = tmsBucket.getJSONObject("regName").getJSONArray("buckets")
						.getJSONObject(0).getString("key");
				String [] areas = regName.split("\\|");
				if(areas.length<3){//直辖市
					area1=areas[0];
					area2="市辖区";
					area3=areas[1];
				}else{
					area1=areas[0];
					area2=areas[1];
					area3=areas[2];
				}
				
				//获取kpi值
				if(kpiType==1){//rsrp
					Double avg =  NumberUtils.toDouble(tmsBucket.getJSONObject("rsrp_avg").getString("value"));
					//保留2位小数
					akes.setVal(NumUtils.resDouble(avg, 2));
				}else if(kpiType==2){//snr
					Double avg =  NumberUtils.toDouble(tmsBucket.getJSONObject("snr_avg").getString("value"));
					//保留2位小数
					akes.setVal(NumUtils.resDouble(avg, 2));
				}else if(kpiType==4){//http下载速度
					String a = tmsBucket.getJSONObject("http_avg").getString("value");
					if(NumUtils.isNumber(a)){
						double d = NumberUtils.toDouble(a);
						//转换为MB，保留2位小数
						akes.setVal(NumUtils.resDouble(d/1024, 2));
					}else{
						akes.setVal(null);
					}
				}else if(kpiType==8){//覆盖率
					String c = tmsBucket.getJSONObject("cover_lv").getString("value");
					if(NumUtils.isNumber(c)){
						double d = NumberUtils.toDouble(c);
						//保留2位小数
						akes.setVal(NumUtils.resDouble(d*100, 2));
					}else{
						akes.setVal(null);
					}
				}else if(kpiType==9){//网络掉线
					String c = tmsBucket.getJSONObject("netdrops_lv").getString("value");//符合条件数量
					if(NumUtils.isNumber(c)){
						double d = NumberUtils.toDouble(c);
						//保留2位小数
						akes.setVal(NumUtils.resDouble(d*100, 2));
					}else{
						akes.setVal(null);
					}
				}else if(kpiType==10){//业务掉线
					String c = tmsBucket.getJSONObject("busdrops_lv").getString("value");//符合条件数量
					if(NumUtils.isNumber(c)){
						double d = NumberUtils.toDouble(c);
						//保留2位小数
						akes.setVal(NumUtils.resDouble(d*100, 2));
					}else{
						akes.setVal(null);
					}
				}
				//加入每个区域的运营商集合
				l.add(akes);
			}
				
        	//补全缺少的运营商
			if(telFlag!=7){//证明缺少了运营商数据
				AreaKpi4ESBean ak1 = new AreaKpi4ESBean();
				AreaKpi4ESBean ak2 = new AreaKpi4ESBean();
				if(telFlag==1){//缺少移动和电信
					ak1.setTel("移动");
					ak2.setTel("电信");
					l.add(ak1);
					l.add(ak2);
				}else if(telFlag==2){//缺少联通和电信
					ak1.setTel("联通");
					ak2.setTel("电信");
					l.add(ak1);
					l.add(ak2);
				}else if(telFlag==4){//缺少联通和移动
					ak1.setTel("联通");
					ak2.setTel("移动");
					l.add(ak1);
					l.add(ak2);
				}else if(telFlag==3){//缺少电信
					ak1.setTel("电信");
					l.add(ak1);
				}else if(telFlag==5){//缺少移动
					ak1.setTel("移动");
					l.add(ak1);
				}else if(telFlag==6){//缺少联通
					ak1.setTel("联通");
					l.add(ak1);
				}	
				
			}
        
        /**排序每个区域内的运营商**/
		Collections.sort(l); //升序排序
		Collections.reverse(l);//反转(改为降序)
		
		
		/**排序后加入返回大集合**/
		AreaKpiBean ab = new AreaKpiBean();
		ab.setCname1(area1);//一级区域
		ab.setCname2(area2);//二级区域
		ab.setCname3(area3);//三级区域
		for(int j=0;j<l.size();j++){
			AreaKpi4ESBean akb = l.get(j);
			Class c = ab.getClass();
			Method telMed = c.getMethod("setTel"+(j+1),String.class);
			Method valMed = c.getMethod("setVal"+(j+1),Double.class);
			//执行方法
			telMed.invoke(ab, akb.getTel());
			valMed.invoke(ab, akb.getVal());
		}
		
		/**完成单个区域内所有运营商的内容填充**/
		list.add(ab);
	}
        
		//按照第一名的值倒叙排序
		Collections.sort(list); //升序排序
		Collections.reverse(list);//反转(改为降序)
		
		return list;
	}
	
		
}
