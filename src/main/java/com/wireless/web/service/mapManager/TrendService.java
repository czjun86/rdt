package com.wireless.web.service.mapManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wireless.web.dao.mapManager.TrendDao;
import com.wireless.web.model.VoBean;
import com.wireless.web.utils.StringUtils;

@Service("trendService")
public class TrendService {

	@Autowired
	private TrendDao dao;
	/**
	 * 
	 * @param map
	 * @param com.wireless.web.model.VoBean
	 * 			：vo.starttime	开始时间
	 * 			：vo.endtime 结束时间
	 * 			：vo.areaid	查询区域id
	 * 			：vo.kpiType  查询指标类型id
	 * 			：vo.netType  网络制式
	 * 			：vo.level 1,2,3查询区域表切查询区域等级为level对应值，4，5查询网格，4查询500网格，5查询250网格
	 * 			：vo.slng 南经度
	 * 			：vo.nlng 北经度
	 * 			：vo.slat 南纬度
	 * 			：vo.nlat 北纬度
	 * @return
	 */
	public Map<String ,Object> queryTrend(Map<String ,Object> map ,VoBean vo){
		List<Map<String ,Object>> list = dao.queryTrend(vo);
		List<String> trendName = StringUtils.getOperator();
		List<List<Object>> trendData = new ArrayList<List<Object>>();
		List<String> trendX = new ArrayList<String>();
		
		List<Object> uni = new ArrayList<Object>();//联通
		List<Object> mob = new ArrayList<Object>();//移动
		List<Object> tel = new ArrayList<Object>();//电信
		//List<Object> other = new ArrayList<Object>();//其他
		//根据结束时间，生成七天X轴刻度
		trendX = StringUtils.getSevenDate(vo.getEndtime());
		//根据X轴刻度和运营商对应取值，当天无值，默认给0
		for(int i=0;i<trendX.size();i++){
			String time = trendX.get(i);
			for(Map<String ,Object> value :list){
				if(time.equals(value.get("time").toString())){
					String type = value.get("wifiComs").toString();
					if("1".equals(type)){
						uni.add(value.get("uni"));
					}else if("2".equals(type)){
						mob.add(value.get("mob"));
					}else if("3".equals(type)){
						tel.add(value.get("tel"));
					}
					/*else if("4".equals(type)){
						other.add(value.get("other").toString());
					}*/
				}else{
					continue;
				}
			}
			//当次数据集合大小小于循环次数时
			//说明当前运营商在该次没有数据需要补一条
			if(uni.size()<i+1){
				uni.add(0);
			}
			if(mob.size()<i+1){
				mob.add(0);
			}
			if(tel.size()<i+1){
				tel.add(0);
			}
			/*if(other.size()<i+1){
				other.add(0);
			}*/
		}
		
		trendData.add(uni);
		trendData.add(mob);
		trendData.add(tel);
		//trendData.add(other);
		
		map.put("trendName", trendName);
		map.put("trendData", trendData);
		map.put("trendX", trendX);
		
		//宽带用户数量
		List<Map<String ,Object>> prolist = new ArrayList<Map<String ,Object>>();
		Map<String ,Object> proMap = null;
		List<String> proName = null;
		String type = null;
		int unicnt = 0;
		int mobcnt = 0;
		int telcnt = 0;
		int othercnt = 0;
		for(String time:trendX){
			proMap = new HashMap<String ,Object>();
			proName = StringUtils.getOperator();
			List<Integer> proData = new ArrayList<Integer>();
			//四家运营商用户数默认为0，保证每天有该运营商
			unicnt = 0;
			mobcnt = 0;
			telcnt = 0;
			othercnt = 0;
			//获取当天该运营商用户数
			for(Map<String ,Object> value :list){
				type = value.get("wifiComs").toString();
				if("1".equals(type) && (time.equals(value.get("time").toString()))){
					unicnt = Integer.valueOf(value.get("unicnt").toString());
					break;
				}
			}
			for(Map<String ,Object> value :list){
				type = value.get("wifiComs").toString();
				if("2".equals(type) && (time.equals(value.get("time").toString()))){
					mobcnt = Integer.valueOf(value.get("mobcnt").toString());
					break;
				}
			}
			for(Map<String ,Object> value :list){
				type = value.get("wifiComs").toString();
				if("3".equals(type) && (time.equals(value.get("time").toString()))){
					telcnt = Integer.valueOf(value.get("telcnt").toString());
					break;
				}
			}
			/*for(Map<String ,Object> value :list){
				type = value.get("wifiComs").toString();
				if("4".equals(type) && (time.equals(value.get("time").toString()))){
					othercnt = Integer.valueOf(value.get("othercnt").toString());
					break;
				}
			}*/

			//按运营商顺序放入
			proData.add(unicnt);
			proData.add(mobcnt);
			proData.add(telcnt);
			//proData.add(othercnt);
			
			proMap.put("proName", proName);
			proMap.put("proData", proData);
			prolist.add(proMap);
		}
		//完全没有数据，补个空值---饼图
		if(trendX.size()<1){
			proMap = new HashMap<String ,Object>();
			proName = new ArrayList<String>();
			List<Integer> proData = new ArrayList<Integer>();
			proMap.put("proName", proName);
			proMap.put("proData", proData);
			prolist.add(proMap);
		}
		map.put("pros", prolist);
		return map;
	}
}
