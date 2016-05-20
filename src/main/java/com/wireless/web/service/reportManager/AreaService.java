package com.wireless.web.service.reportManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wireless.web.dao.reportManager.AreaDao;
import com.wireless.web.model.Area;
import com.wireless.web.model.Quotas;
import com.wireless.web.model.User;

@Service("areaService")
public class AreaService {

	private static final Logger logger = LoggerFactory
			.getLogger(AreaService.class);
	@Autowired
	protected AreaDao dao;
	
	public List<Area> queryArea(String firstId){
		 List<Area> list = new ArrayList<Area>();
		 list = dao.queryArea(firstId);
		return list;
	}
	
	/**
	 * 查询第一级区域
	 * @return
	 */
	public List<Area> queryCounty(){
		 List<Area> countys = new ArrayList<Area>();
		 countys = dao.queryCounty();
		return countys;
	}
	
	/**
	 * 查询第一级区域
	 * @return
	 */
	public List<Area> queryCountyById(String id){
		 List<Area> countys = new ArrayList<Area>();
		 countys = dao.queryCountyById(id);
		return countys;
	}
	
	/**
	 * 根据父id差下一级区域
	 * @param id
	 * @return
	 */
	public List<Area> queryChildren(String id){
		 List<Area> citys = new ArrayList<Area>();
		 citys = dao.queryCity(id);
		return citys;
	}
	
	/**
	 * 根据当前区域
	 * @param id
	 * @return
	 */
	public List<Area> queryThisArea(String id){
		 List<Area> areas = new ArrayList<Area>();
		 areas = dao.queryThisArea(id);
		return areas;
	}
	/**
	 * 根据父id差下一级区域
	 * @param id
	 * @return
	 */
	public List<Area> queryChildrenById(String id ,String did){
		 List<Area> citys = new ArrayList<Area>();
		 Map<String ,Object> map = new HashMap<String ,Object>();
		 map.put("id", id);
		 map.put("did", did);
		 citys = dao.queryChildrenById(map);
		return citys;
	}
	
	/**
	 * 根据父id差下一级区域
	 * @param id
	 * @return
	 */
	public List<Area> queryThird(String id){
		 List<Area> areas = new ArrayList<Area>();
		 areas = dao.queryThird(id);
		return areas;
	}
	
	/**
	 * 根据运营商和网络类型查询对应指标
	 * @param operator//运营商
	 * @param netType//网络类型   无网络(-1),没取到(0),wifi(1),2G(2),3G(3),4G(4)
	 * @param Quotas//指标大类  1 无线层, 2 网络层, 3 应用层
	 * @return list
	 */
	public List<Quotas> getQuotas(Integer operator ,Integer netType,Integer kpiClass){
		Map<String ,Object> param = new HashMap<String ,Object>();
		param.put("operator", operator);
		param.put("netType", netType);
		param.put("kpiClass", kpiClass);
		List<Quotas> list = new ArrayList<Quotas>();
		list = dao.queryQuotas(param);
		return list;
	}
	
	/**
	 * 查询
	 */
	public List<Area> getRoad(String areaId ,Integer roadType ,Integer roadLevel){
		List<Area> list = new ArrayList<Area>();
		Map<String ,Object> param = new HashMap<String ,Object>();
		param.put("areaId", areaId);
		param.put("roadType", roadType);
		param.put("roadLevel", roadLevel);
		list = dao.getRoad(param);
		return list;
	}
	
	/**
	 * 查询
	 */
	public List<Area> getRoad(String roadName ,Integer roadType ,Integer roadLevel ,String area){
		List<Area> list = new ArrayList<Area>();
		Map<String ,Object> param = new HashMap<String ,Object>();
		param.put("roadName", roadName);
		param.put("roadType", roadType);
		param.put("roadLevel", roadLevel);
		param.put("area", area);
		list = dao.getAutocompleteRoad(param);
		return list;
	}
	
	/**
	 * 区域和道路查询
	 * @param mv
	 * @param province
	 * @param district
	 * @param area
	 * @param roadType
	 * @param roadLevel
	 * @return
	 */
	public Map<String ,Object> getAreaRoad(
			String province,String district ,String area,
			Integer roadType ,Integer roadLevel){
		Map<String ,Object> map = new HashMap<String ,Object>();
		//查询对应区域信息
		List<Area> countrys = new ArrayList<Area>();
		List<Area> citys = new ArrayList<Area>();
		List<Area> areas = new ArrayList<Area>();
		List<Area> roads = new ArrayList<Area>();
		countrys = queryCounty();//差省级
		citys = queryChildren(province);// 查市级
		if(district!=null && !"-1".equals(district)){
			areas = queryChildren(district);// 查区级
			//3级区域为全部，查询2级下所有
			if(area!=null && !"-1".equals(area)){
				roads = getRoad(area,roadType,roadLevel);
			}else{
				roads = getRoad(district.substring(0,4)+"%",roadType,roadLevel);
			}
		}else{
			//2级区域为全部，查询1级下所有
			roads = getRoad(province.substring(0,2)+"%",roadType,roadLevel);
		}
		map.put("countrys" ,countrys);
		map.put("citys" ,citys);
		map.put("areas" ,areas);
		map.put("roads" ,roads);
		return map;
	}
	
	/**
	 * 区域查询
	 * @param mv
	 * @param province
	 * @param district
	 * @param area
	 * @param roadType
	 * @param roadLevel
	 * @return
	 */
	public Map<String ,Object> getAreaRoad(
			String province,String district ,String area){
		Map<String ,Object> map = new HashMap<String ,Object>();
		//查询对应区域信息
		List<Area> countrys = new ArrayList<Area>();
		List<Area> citys = new ArrayList<Area>();
		List<Area> areas = new ArrayList<Area>();
		countrys = queryCounty();//差省级
		citys = queryChildren(province);// 查市级
		if(district!=null && !"-1".equals(district)){
			areas = queryChildren(district);// 查区级
		}
		map.put("countrys" ,countrys);
		map.put("citys" ,citys);
		map.put("areas" ,areas);
		return map;
	}
	
	/**
	 * 区域和道路查询  含权限
	 * @param mv
	 * @param province
	 * @param district
	 * @param area
	 * @param roadType
	 * @param roadLevel
	 * @return
	 */
	public Map<String ,Object> getAreaRoadAuthority(
			String province,String district ,String area,
			Integer roadType ,Integer roadLevel ,User user){
		Map<String ,Object> map = new HashMap<String ,Object>();
		//查询对应区域信息
		List<Area> provinces = new ArrayList<Area>();
		List<Area> districts = new ArrayList<Area>();
		List<Area> areas = new ArrayList<Area>();
		List<Area> roads = new ArrayList<Area>();
		provinces = queryCountyById(user.getProvince());//差省级
		if(isInList(provinces ,province)){
			if("-1".equals(user.getDistrict())){//有当前所有2级区域权限
				districts = queryChildren(province);// 查市级
				if("-1".equals(district)){//传入2级为-1，查全部2级对应3级
					roads = getRoad(province.substring(0,2)+"%",roadType,roadLevel);
				}else{
					if("-1".equals(user.getCounty())){
						areas = queryChildren(district);// 查区级
						if("-1".equals(area)){
							roads = getRoad(district.substring(0,4)+"%",roadType,roadLevel);
						}else{
							roads = getRoad(area,roadType,roadLevel);
						}
					}else{
						areas = queryThird(user.getCounty());// 查区级
						roads = getRoad(user.getCounty(),roadType,roadLevel);
					}
				}
			}else{//当前所有2级区域中只有唯一一个区域权限
				districts = queryChildrenById(province,user.getDistrict());// 查市级
				if("-1".equals(user.getCounty())){
					areas = queryChildren(user.getDistrict());// 查区级
					if("-1".equals(area)){
						roads = getRoad(user.getDistrict().substring(0,4)+"%",roadType,roadLevel);
					}else{
						roads = getRoad(area,roadType,roadLevel);
					}
				}else{
					areas = queryThird(user.getCounty());// 查区级
					roads = getRoad(user.getCounty(),roadType,roadLevel);
				}
			}
		}else{
			if("-1".equals(user.getDistrict())){
				districts = queryChildren(user.getProvince());// 查市级
				roads = getRoad(province.substring(0,2)+"%",roadType,roadLevel);
			}else{
				districts = queryChildrenById(province,user.getDistrict());// 查市级
				if("-1".equals(user.getCounty())){
					areas = queryChildren(user.getDistrict());// 查区级
					roads = getRoad(user.getDistrict().substring(0,4)+"%",roadType,roadLevel);
				}else{
					areas = queryThird(user.getCounty());// 查区级
					roads = getRoad(user.getCounty(),roadType,roadLevel);
				}
			}
			
		}
		map.put("countrys" ,provinces);
		map.put("citys" ,districts);
		map.put("areas" ,areas);
		map.put("roads" ,roads);
		return map;
	}
	
	/**
	 * 区域查询 含权限
	 * @param mv
	 * @param province
	 * @param district
	 * @param area
	 * @param roadType
	 * @param roadLevel
	 * @return
	 */
	public Map<String ,Object> getAreaRoadAuthority(
			String province,String district ,String area ,User user){
		Map<String ,Object> map = new HashMap<String ,Object>();
		//查询对应区域信息
		List<Area> provinces = new ArrayList<Area>();
		List<Area> districts = new ArrayList<Area>();
		List<Area> areas = new ArrayList<Area>();
		provinces = queryCountyById(user.getProvince());//差省级
		if(isInList(provinces ,province)){
			if("-1".equals(user.getDistrict())){//有当前所有2级区域权限
				districts = queryChildren(province);// 查市级
				if("-1".equals(district)){//传入2级为-1，查全部2级对应3级
				}else{
					if("-1".equals(user.getCounty())){
						areas = queryChildren(district);// 查区级
					}else{
						areas = queryThird(user.getCounty());// 查区级
					}
				}
			}else{//当前所有2级区域中只有唯一一个区域权限
				districts = queryChildrenById(province,user.getDistrict());// 查市级
				if("-1".equals(user.getCounty())){
					areas = queryChildren(user.getDistrict());// 查区级
				}else{
					areas = queryThird(user.getCounty());// 查区级
				}
			}
		}else{
			if("-1".equals(user.getDistrict())){
				districts = queryChildren(user.getProvince());// 查市级
			}else{
				districts = queryChildrenById(province,user.getDistrict());// 查市级
			}
			
		}
		map.put("countrys" ,provinces);
		map.put("citys" ,districts);
		map.put("areas" ,areas);
		return map;
	}
	
	/**
	 * 判断区域是否在集合内
	 * @return
	 */
	public boolean isInList(List<Area> list ,String name){
		for(Area area:list){
			if(name.equals(area.getId())){
				return true;
			}
		}
		return false;
	}
	/**
	 * 获取权限拥有的运营商
	 * @param myOperator
	 * @return
	 */
	public List<Map<String ,Object>> getMyOperator(String myOperator){
		 List<Map<String ,Object>> list = new ArrayList<Map<String ,Object>>();
		 String [] ids = {"1","2","3"};
		 String [] names = {"联通","移动","电信"};
		 Map<String ,Object> map = null;
		 for(int i = 0; i<ids.length;i++){
			 if("1".equals(myOperator.substring(i,i+1))){
				 map = new HashMap<String ,Object>();
				 map.put("id", ids[i]);
				 map.put("text", names[i]);
				 list.add(map);
			 }
		 }
		 return list;
	}

	public String getRoadName(String roadId) {
		return dao.getRoadName(roadId);
	}
}
