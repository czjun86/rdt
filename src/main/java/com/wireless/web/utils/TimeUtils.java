package com.wireless.web.utils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

public class TimeUtils {

	/**
	 * 获取昨天日期
	 * @return
	 */
	public static String getYesterday(){
		DateFormat  df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, -1);
		return df.format(c.getTime());
	}
	
	/**
	 * 获取日期
	 * @return
	 */
	public static String getTime(Integer i){
		DateFormat  df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, i);
		return df.format(c.getTime());
	}
	
	/**
	 * 获取不包括传入时间在内的前7天时间集合
	 * @param time
	 * @throws ParseException 
	 */
	public static List<String> getPrevSeven(String time) throws ParseException{
		List<String> list = new ArrayList<String>();
		
		DateFormat  df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		date=df.parse(time);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		
		//起始时间
		c.add(Calendar.DATE, -7);
		list.add(df.format(c.getTime()));
		
		//第二天开始时间
		for(int i = 0 ; i<6 ;i++){
			c.add(Calendar.DATE, 1);
			list.add(df.format(c.getTime()));
		}
		
		return list;
	}
	
	/**
	 * 去掉时间集合的年份
	 * @param times
	 * @return
	 */
	public static List<String> getNoYearTime(List<String> times){
		List<String> list = new ArrayList<String>();
		for(String time:times){
			list.add(time.substring(5,time.length()));
		}
		return list;
	}
	
	/**
	 * 获取不包括传入时间在内的后7天时间集合
	 * @param time
	 * @throws ParseException 
	 */
	public static List<String> getNextSeven(String time) throws ParseException{
		List<String> list = new ArrayList<String>();
		
		DateFormat  df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		date=df.parse(time);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		for(int i=0 ;i<7 ;i++){
			c.add(Calendar.DATE, 1);
			list.add(df.format(c.getTime()));
		}
		
		return list;
	}
	
	public static Map<String ,String> getSETime(String queryTime){
		Map<String ,String> map = new HashMap<String ,String>();
		String[] time = queryTime.split("-");
		String sta = time[0].replace(".", "-").trim();
		String end = time[1].replace(".", "-").trim();
		map.put("starttime", sta);
		map.put("endtime", end);
		return map;
	}
	
	/**
	 * 判断是否在第一个月时间内
	 */
	public static Map<String ,String> getOneMoon(String sta ,String end){
		Map<String ,String> map = new HashMap<String ,String>();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
			Date date = new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.DATE, -1);
			long now = sdf.parse(sdf.format(c.getTime())).getTime();//今天时间
			c.add(Calendar.DATE, -30);
			long cut =  sdf.parse(sdf.format(c.getTime())).getTime();//一月时间点
			
			long millionSta = sdf.parse(sta).getTime();
			long millionEnd = sdf.parse(end).getTime();
			
			if(millionEnd<=cut){
				map.put("hasTime", "-1");
				map.put("starttime", sta);
				map.put("endtime", end);
			}else if(cut<millionEnd && millionEnd<=now){
				if(millionSta<cut){
					map.put("hasTime", "1");
					map.put("starttime", sdf.format(new Date(cut)));
					map.put("endtime", sdf.format(new Date(millionEnd)));
				}else if(millionSta>=cut){
					map.put("hasTime", "1");
					map.put("starttime", sdf.format(new Date(millionSta)));
					map.put("endtime", sdf.format(new Date(millionEnd)));
				}
			}else if(now<millionEnd){
				if(now<millionSta){
					map.put("hasTime", "-1");
					map.put("starttime", sta);
					map.put("endtime", end);
				}else if(millionSta<=now && millionSta>=cut){
					map.put("hasTime", "1");
					map.put("starttime", sdf.format(new Date(millionSta)));
					map.put("endtime", sdf.format(new Date(now)));
				}else if(millionSta<cut){
					map.put("hasTime", "1");
					map.put("starttime", sdf.format(new Date(cut)));
					map.put("endtime", sdf.format(new Date(now)));
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//毫秒
		return map;
	}
	
	/**
	 * 计算传入的1个月时间判断是否在第一个月时间内
	 */
	public static Map<String ,String> getOneMoon(String queryTime){
		Map<String ,String> map = new HashMap<String ,String>();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
			Calendar c = Calendar.getInstance();
			String[] time = queryTime.split("-");
			long millionSta = sdf.parse(time[0]).getTime();
			long millionEnd = sdf.parse(time[1]).getTime();
			c.setTime(sdf.parse(time[1]));
			c.add(Calendar.DATE, -29);
			long cut =  sdf.parse(sdf.format(c.getTime())).getTime();//一月时间点
			if(millionSta>=cut){
				map.put("starttime", sdf.format(new Date(millionSta)));
				map.put("endtime", sdf.format(new Date(millionEnd)));
			}else{
				map.put("starttime", sdf.format(new Date(cut)));
				map.put("endtime", sdf.format(new Date(millionEnd)));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//毫秒
		return map;
	}
	/**
	 * 获取传入月份时间
	 * @param s 传入月份
	 * @param s 判断获取第一天或最后一天
	 * @return
	 * @throws ParseException
	 */
	public static String getMonthDay(String s ,String flag) throws ParseException{
		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd"); 
		SimpleDateFormat sd = new SimpleDateFormat("yyyy.MM"); 
		Date d = sd.parse(s);
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		if("first".equals(flag)){
		    c.add(Calendar.MONTH, 0);
		}else if("last".equals(flag)){
		    c.add(Calendar.MONTH, 1);
		    c.add(Calendar.DATE, -1);
		}
		String time = format.format(c.getTime());
	    return time;
	}
	
	/**
	 * 
	* 函数功能说明    转换时间字符串格式   参考:yyyy-MM-dd'T'HH:mm:ss.SSSZ和yyyy-MM-dd HH:mm:ss
	* tc  2015年12月4日 
	* 修改者名字修改日期 
	* 修改内容 
	* @参数： @param timeStr   要转换的时间字符串
	* @参数： @param fromFormat 转化格式
	* @参数： @param toFormat	     目标格式
	* @参数： @return   
	* @throws
	 */
	public static String chDate(String timeStr,String fromFormat,String toFormat){
		String re = "";
		//针对ES特殊格式的加工
		if(fromFormat.equals("yyyy-MM-dd'T'HH:mm:ss.SSSZ")){
			int idx = timeStr.lastIndexOf(":");
			timeStr = timeStr.substring(0, idx) + timeStr.substring(idx+1);
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat (fromFormat);
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(toFormat);
			re = formatter.format(sdf.parse (timeStr));
			//针对ES特殊格式的加工
			if(toFormat.equals("yyyy-MM-dd'T'HH:mm:ss.SSSZ")){
				re = re.substring(0, (re.length()-2)) +":"+ re.substring(re.length()-2);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return re;
	}
	
	/**
	 * 
	* 函数功能说明    转换毫秒时间到对应字符串格式
	* tc  2015年12月4日 
	* 修改者名字修改日期 
	* 修改内容 
	* @参数： @param timeNum   要转换的时间毫秒
	* @参数： @param fromFormat 转化格式
	* @参数： @return   
	* @throws
	 */
	public static String chDate(long timeNum,String fromFormat){
		String re = "";
		Date dt = new Date(timeNum);
		SimpleDateFormat sdf = new SimpleDateFormat (fromFormat);
		re = sdf.format(dt);
		return re;
	}
	
	public static void main(String[] args) {
		
	}
}
