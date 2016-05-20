package com.wireless.web.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartsUtil {

	/**
	 * 根据传入时间段和粒度生成X轴时间刻度
	 * @param queryTime 传入时间
	 * @param timegransel 时间粒度
	 * @return
	 * @throws ParseException
	 */
	public static List<String> getTimeX(String queryTime,String timegransel) throws ParseException{
		List<String> list = new ArrayList<String>();
		String[] str = queryTime.split("-");
		String sta = str[0].trim();
		String end = str[1].trim();
		if("2".equals(timegransel)){//天粒度
			//计算相差天数
			DateFormat  df = new SimpleDateFormat("yyyy.MM.dd");
			DateFormat  df2 = new SimpleDateFormat("yyyy-MM-dd");
			Date date1 = df.parse(sta);
			Date date2 = df.parse(end);
			Calendar c1 = Calendar.getInstance();
			Calendar c2 = Calendar.getInstance();
			c1.setTime(date1);
			c2.setTime(date2);
			int DValue = (int) (((long)c2.getTimeInMillis()-(long)c1.getTimeInMillis())/(1000*3600*24));
			
			//生成list数据
			Date date = df.parse(sta);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			//加入开始时间
			list.add(sta.replaceAll("\\.", "-"));
			for(int i = 0;i<DValue;i++){
				c.add(Calendar.DATE, 1);
				list.add(df2.format(c.getTime()));
			}
		}else if("3".equals(timegransel)){//月粒度
			//计算相差多少月
			DateFormat  df = new SimpleDateFormat("yyyy.MM");
			DateFormat  df2 = new SimpleDateFormat("yyyy-MM");
			Date date1 = df.parse(sta);
			Date date2 = df.parse(end);
			Calendar c1 = Calendar.getInstance();
			Calendar c2 = Calendar.getInstance();
			c1.setTime(date1);
			c2.setTime(date2);
			int DValue = (c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR)) * 12 + 
					c2.get(Calendar.MONTH)- c1.get(Calendar.MONTH);
			
			//生成list数据
			Date date = df.parse(sta);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			//加入开始时间
			list.add(sta.replaceAll("\\.", "-").substring(0,7));
			for(int i = 0;i<DValue;i++){
				c.add(Calendar.MONTH, 1);
				list.add(df2.format(c.getTime()));
			}
		}
		
		return list;
	}
	
	/**
	 * 生成线形图和柱状图数据，时间为刻度
	 * @param ap 传入参数
	 * @param result 查询结构数据
	 * @return
	 * @throws ParseException 
	 */
	public static Map<String ,Object> getOption(String queryTime, String timegransel 
			,List<Map<String ,Object>> result) throws ParseException{
		Map<String ,Object> map = new HashMap<String ,Object>();
		//获取线形图线条对应运营商
		List<String> name = StringUtils.getOperator();
		//获取x轴时间刻度
		List<String> x = new ArrayList<String>();
		x = getTimeX(queryTime,timegransel);
		//获取每个时间刻度每个运营商的结果值
		List<List<Double>> data = new ArrayList<List<Double>>();
		
		String sname ="";//查询结果运营名
		String xtime ="";//查询结果时间
		double value =0;//查询结果值
		double dataValue = 0;//放入集合的值,默认值为0
		List<Double> list = null;
		for(String na:name){
			na = StringUtils.getOperatorIdToString(na);
			list = new ArrayList<Double>();
			for(String time : x){
				//每次匹配对应数据结果前重置，默认值为0
				dataValue = 0;
				for(Map<String ,Object> rlt:result){//循环数据
					sname = rlt.get("name").toString();
					xtime =  rlt.get("time").toString();
					value = Double.parseDouble(rlt.get("value").toString());//结果值
					//当有时间刻度和运营商完全匹配时放入值，然后跳出循环
					if(na.equals(sname) && time.equals(xtime)){
						dataValue = value;
						break;
					}
				}
				list.add(dataValue);
			}
			data.add(list);
		}
		
		map.put("name",name);
		map.put("x",x);
		map.put("data",data);
		return map;
	}
	
	/**
	 * 饼图生成
	 * @param ap
	 * @param result
	 * @return
	 */
	public static Map<String ,Object> getPieOption(List<Map<String ,Object>> result){
		Map<String ,Object> map = new HashMap<String ,Object>();
		//获取线形图线条对应运营商
		List<String> name = StringUtils.getOperator();
		List<Double> data = new ArrayList<Double>();
		String str = null;
		double value = 0;
		for(String na : name){
			String tna= StringUtils.getOperatorIdToString(na);
			value = 0;//默认值为0
			for(Map<String ,Object> rlt : result){
				str = rlt.get("name").toString();
				if(tna.equals(str)){
					value =  (double) rlt.get("value");
					break;
				}
			}
			data.add(value);
		}
		map.put("name",name);
		map.put("data",data);
		return map;
	}
	
	/**
	 * 生成线形图和柱状图数据，传入x刻度
	 * @param ap
	 * @param result
	 * @return
	 */
	public static Map<String ,Object> getBarOption(List<Map<String ,Object>> result,List<String> operators){
		Map<String ,Object> map = new HashMap<String ,Object>();
		//获取线形图线条对应运营商
		List<String> name = operators;
		//获取x轴时间刻度
		List<String> x = new ArrayList<String>();
		//获取每个时间刻度每个运营商的结果值
		List<List<Object>> data = new ArrayList<List<Object>>();
		
		List<Object> wifiValue = new ArrayList<Object>();
		List<Object> dataValue = new ArrayList<Object>();
		
		//获取x轴刻度
		for(Map<String ,Object> rlt : result){
			x.add(rlt.get("name").toString());
			wifiValue.add(rlt.get("wifiValue")==null||"".equals(rlt.get("wifiValue"))?0:rlt.get("wifiValue"));
			dataValue.add(rlt.get("dataValue")==null||"".equals(rlt.get("dataValue"))?0:rlt.get("dataValue"));
		}
		data.add(wifiValue);
		data.add(dataValue);
		
		map.put("name",name);
		map.put("x",x);
		map.put("data",data);
		return map;
	}
}
