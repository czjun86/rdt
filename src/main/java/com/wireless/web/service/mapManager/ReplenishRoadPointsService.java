package com.wireless.web.service.mapManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.poweropt.rtCms.entity.RoadInfo;
import cn.poweropt.rtCms.entity.RoadPoint;

import com.wireless.call.MemcachedClient;
import com.wireless.web.dao.mapManager.ReplenishRoadPointsDao;
import com.wireless.web.model.Area;
import com.wireless.web.model.ReplenishRoadPoionts;
import com.wireless.web.utils.ReplenishRoadPointsUtils;

@Service("replenishRoadPointsService")
public class ReplenishRoadPointsService {

	private static final Logger logger = LoggerFactory
			.getLogger(ReplenishRoadPointsService.class);
	
	@Autowired
	protected ReplenishRoadPointsDao dao;
	
	@Autowired
	protected MemcachedClient mc;

	/**
	 * 根据用户ID查询出对应的一级行政区域名称
	 */
	
	public Area getAreaNameByUserId(String userId)
	{
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("userId",userId);
		Area area=null;
		List<Area> list=dao.getAreaNameByUserId(map);
		if(list!=null&&list.size()>0){
			area=list.get(0);
		}
		return area;
	}
	
	/**
	 * 查询采样点数据
	 * @return
	 */
	public List<ReplenishRoadPoionts> getPoint(Map<String, String> map){
		List<ReplenishRoadPoionts> points=new ArrayList<ReplenishRoadPoionts>();
		points=dao.getPoint(map);
		return points;
	}
	
	/**
	 * 保存标记的道路点信息
	 * @return
	 */
	public int saveMarks(String[] list,String roadId,String roadName){
		int re = 1;
		try {
			for(int i =0;i<list.length;i++){
				//获取经纬度
				String lng = list[i].split(",")[0];
				String lat = list[i].split(",")[1];
				
				Map<String, String> map = new HashMap<>();
				//设置uuid
				map.put("uuid", ReplenishRoadPointsUtils.findFlowUUID());
				//设置道路编号
				map.put("road_id", roadId);
				//设置道路名称
				map.put("road_name", roadName);
				//设置百度经纬度
				map.put("bd_longitude", lng);
				map.put("bd_latitude", lat);
				//设置高德经纬度
				Map<String, Double> lnglat = ReplenishRoadPointsUtils.bdTogcj(NumberUtils.toDouble(lng), NumberUtils.toDouble(lat));
				map.put("gd_longitude", lnglat.get("lng").toString());
				map.put("gd_latitude", lnglat.get("lat").toString());
				dao.saveMarks(map);
			}
			//把这个道路重新加载至MemCache
			RoadInfo roadInfo = dao.getRoadInfoByRoadId(roadId);//获取道路信息
			List<RoadPoint> points = dao.getPointsByRoadId(roadId);//获取道路点集合
			roadInfo.setRoadPoints(points);
			Map<String,Object> m =new HashMap<String, Object>();
			m.put("roadInfo", roadInfo);
			mc.set(roadId, m);//存入缓存
		} catch (Exception e) {
			e.printStackTrace();
			re = 0;
		}
		
		return re;
	}
	
	/**
	 * 删除道路点
	 * @return
	 */
	public int removePoint(String roadId,String uuid){
		int re = 1;
		try {
			Map<String ,Object> map = new HashMap<>();
			map.put("roadId", roadId);
			map.put("uuid", uuid);
			dao.removePoint(map);
			//删掉缓存内这个道路点
			Map<String,Object> m = (Map<String,Object>) mc.get(roadId);
			RoadInfo roadInfo = (RoadInfo) m.get("roadInfo");//获取道路信息
			List<RoadPoint> points = roadInfo.getRoadPoints();//获取道路点集合
			int index = -1;
			for(int i=0;i<points.size();i++){
				if(points.get(i).getUuid().equals(uuid)){
					index = i;
					break;
				}
			}
			//删除
			if(index!=-1){
				points.remove(index);
			}
			roadInfo.setRoadPoints(points);
			m.put("roadInfo", roadInfo);
			mc.set(roadId, m);//存入缓存
		} catch (Exception e) {
			e.printStackTrace();
			re = 0;
		}
		
		return re;
	}
	
}
