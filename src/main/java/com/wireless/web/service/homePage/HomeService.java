package com.wireless.web.service.homePage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wireless.web.dao.homePage.HomeDao;
import com.wireless.web.model.User;
import com.wireless.web.service.reportManager.IntervalService;
import com.wireless.web.utils.StringUtils;
import com.wireless.web.utils.TestUtils;
import com.wireless.web.utils.TimeUtils;

@Service("homeService")
public class HomeService {

	private static final Logger logger = LoggerFactory
			.getLogger(HomeService.class);
	
	@Autowired
	HomeDao dao;
	
	@Autowired
	IntervalService intervalService;
	/**
	 * 查询采样点数据和覆盖数据查询
	 * @return
	 */
	public Map<String ,Object> getPointInfo(User user ,String time){
		Map<String ,Object> map = new HashMap<String ,Object>();
		//采样点数据
		List<Map<String ,Object>> pointList=null;
		//覆盖数据
		List<Map<String ,Object>> coverList=null;
		try {
			Map<String ,Object> param = new HashMap<String ,Object>();
			param.put("telecoms", user.getOperator());
			param.put("time", time);
			//区域权限
			if(!"-1".equals(user.getCounty())){
				param.put("regionCode", user.getCounty());
			}else{
				if(!"-1".equals(user.getDistrict())){
					param.put("regionCode", user.getDistrict());
				}else{
					param.put("regionCode", user.getProvince());
				}
			}
			pointList = getPointList(param);
			coverList = dao.queryCoverInfo(param);;
		} catch (Exception e) {
			logger.error("query infomation of point or cover rate",e);
		}
		map.put("pointList",pointList);
		map.put("coverList",coverList);
		return map;
	}
	
	/**
	 * 获取采样点数据
	 * @param user
	 * @param time
	 * @return
	 */
	private List<Map<String ,Object>> getPointList(Map<String ,Object> param){
		List<Map<String ,Object>> pointList = new ArrayList<Map<String ,Object>>();
		Map<String ,Object> pointResult = dao.queryPointInfo(param);
		//String[] name = {"信号总采样点数","业务总采样点数","测试总里程","测试覆盖率","主干道测试覆盖率","RSRP","SNR","下行链路带宽<br/>(Mbps)"};
		String[] name = {"信号总采样点数","业务总采样点数","测试覆盖总里程(km)","网络掉线率(%)","业务掉线率(%)","RSRP(dBm)","SNR(dB)","下行链路带宽(Mbps)"};
		//String[] col = {"pointSignal","pointBusiness","testMileage","testCoverage","testCoverageMain","lteRsrp","lteSnr","bwLink"};
		String[] col = {"pointSignal","pointBusiness","testMileage","networkRate","pingRate","lteRsrp","lteSnr","bwLink"};
		Map<String ,Object> map = null;
		for(int i=0;i<name.length;i++){
			map = new HashMap<String ,Object>();
			map.put("name", name[i]);
			if(pointResult!=null){
				map.put("value", pointResult.get(col[i]));
			}else{
				map.put("value", '0');
			}
			pointList.add(map);
		}
		return pointList;
	}
	
	/**
	 * 查询对应指标趋势
	 * @param timeList 时间范围集合
	 * @param quota 查询指标类型
	 * @return
	 */
	public Map<String ,Object> getCoverRate(User user ,List<String> timeList , String quota){
		Map<String ,Object> param = new HashMap<String ,Object>();
		param.put("quota", quota);//查询指标
		param.put("starttime", timeList.get(0));//开始时间
		param.put("endtime", timeList.get(timeList.size()-1));//结束时间
		//区域权限
		if(!"-1".equals(user.getCounty())){
			param.put("regionCode", user.getCounty());
		}else{
			if(!"-1".equals(user.getDistrict())){
				param.put("regionCode", user.getDistrict());
			}else{
				param.put("regionCode", user.getProvince());
			}
		}
		List<Map<String ,Object>> list = dao.queryCoverRate(param);//查询数据
		
		Map<String ,Object> map = new HashMap<String ,Object>();
		List<String> name = StringUtils.getOperator(user.getTeleAuth());
		List<String> x = timeList;
		List<List<Object>> data = new ArrayList<List<Object>>();
		
		//获取每个运营商每个时间点的数据
		List<Object> lt = null;
		boolean flag = false;
		for(String na:name){
			lt = new ArrayList<Object>();
			for(String time:timeList){
				flag = false;
				for(Map<String ,Object> rlt:list){
					String Operator = StringUtils.getOperatorName(rlt.get("telecoms").toString());
					String rltTime = rlt.get("time").toString();
					//判断时间和运营商都相等的时候记录这条数据
					if(na.equals(Operator) && time.equals(rltTime)){
						lt.add(rlt.get("value")!=null&&!"".equals(rlt.get("value"))?rlt.get("value").toString():"0.00");
						flag = true;
						break;
					}
				}
				if(!flag){
					lt.add(0);
				}
			}
			data.add(lt);
		}
		
		map.put("name", name);
		List<String> timeListNoYear = TimeUtils.getNoYearTime(timeList);
		map.put("x", x);
		map.put("ax",timeListNoYear);
		map.put("data", data);
		map.put("prev", "prev");
		
		try {
			//比较查询结束时间是否在今天之后
			DateFormat  df = new SimpleDateFormat("yyyy-MM-dd");
			long queryDay =df.parse(timeList.get(timeList.size()-1)).getTime();
			
			Date date = new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.DATE, -1);
			String date2 = df.format(c.getTime());
			long nowDay =df.parse(date2).getTime();
			
			if(queryDay<nowDay){
				map.put("next", "next");
			}
		} catch (ParseException e) {
			logger.error("compare the time of query to today for button of next",e);
		}
		return map;
	}
	
	/**
	 * 查询指标占比
	 * @param quota 查询的指标
	 * @return
	 */
	public Map<String ,Object> getQuotaProportion(String time ,User user){
		Map<String ,Object> param = new HashMap<String ,Object>();
		param.put("time", time);//用户所属运营商
		param.put("telecoms", user.getOperator());//用户所属运营商
		if(!"-1".equals(user.getCounty())){
			param.put("regionCode", user.getCounty());
		}else{
			if(!"-1".equals(user.getDistrict())){
				param.put("regionCode", user.getDistrict());
			}else{
				param.put("regionCode", user.getProvince());
			}
		}
		Map<String ,Object> result = dao.queryQuotaProportion(param);//查询数据
		
		Map<String ,Object> map = new HashMap<String ,Object>();
		List<String> name = new ArrayList<String>();
		name.add(StringUtils.getOperatorName(user.getOperator()));
		
		Map<String ,Object> rsrpTotal = getQuotaInfo(user,"rsrp",name,result);
		Map<String ,Object> snrTotal = getQuotaInfo(user,"snr",name,result);;
		
		map.put("rsrpTotal", rsrpTotal);
		map.put("snrTotal", snrTotal);
		return map;
	}
	
	public Map<String ,Object> getQuotaInfo(User user ,String quota,List<String> name ,Map<String ,Object> result){
		Map<String ,Object> map = new HashMap<String ,Object>();
		List<String> x = new ArrayList<String>();
		List<String> colors = new ArrayList<String>();
		List<List<Object>> data = new ArrayList<List<Object>>();
		List<Object> list = new ArrayList<Object>();
		List<Object> rate = new ArrayList<Object>();
		if("rsrp".equals(quota)){
			Map<String ,Object> interval = intervalService.getOwnInterval(0,Integer.valueOf(user.getOperator()),"1");//kpiId  1 位rsrp
			x = (List<String>) interval.get("interval");
			colors = (List<String>) interval.get("color");
			if(result!=null){
				list.add(result.get("rsrp_range_one"));
				list.add(result.get("rsrp_range_two"));
				list.add(result.get("rsrp_range_three"));
				list.add(result.get("rsrp_range_four"));
				list.add(result.get("rsrp_range_five"));
				list.add(result.get("rsrp_range_six"));
				rate.add(result.get("rsrp1")+"%");
				rate.add(result.get("rsrp2")+"%");
				rate.add(result.get("rsrp3")+"%");
				rate.add(result.get("rsrp4")+"%");
				rate.add(result.get("rsrp5")+"%");
				rate.add(result.get("rsrp6")+"%");
			}else{
				list = Supplement();
			}
		}else if("snr".equals(quota)){
			Map<String ,Object> interval = intervalService.getOwnInterval(0,Integer.valueOf(user.getOperator()),"2");//kpiId  2 位snr
			x = (List<String>) interval.get("interval");
			colors = (List<String>) interval.get("color");
			if(result!=null){
				list.add(result.get("snr_range_one"));
				list.add(result.get("snr_range_two"));
				list.add(result.get("snr_range_three"));
				list.add(result.get("snr_range_four"));
				list.add(result.get("snr_range_five"));
				list.add(result.get("snr_range_six"));
				rate.add(result.get("snr1")+"%");
				rate.add(result.get("snr2")+"%");
				rate.add(result.get("snr3")+"%");
				rate.add(result.get("snr4")+"%");
				rate.add(result.get("snr5")+"%");
				rate.add(result.get("snr6")+"%");
			}else{
				list = Supplement();
			}
		}else{
			list = Supplement();
		}
		data.add(list);
		
		map.put("name", name);
		map.put("x", x);
		map.put("data", data);
		map.put("color", colors);
		map.put("rate", rate);
		return map;
	}
	
	/** 
	 * 区间值补零
	 * @return
	 */
	public List<Object> Supplement(){
		List<Object> list = new ArrayList<Object>();
		list.add("0");
		list.add("0");
		list.add("0");
		list.add("0");
		list.add("0");
		list.add("0");
		return list;
	}
}
