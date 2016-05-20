package com.wireless.web.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestUtils {

	public static Map<String ,Object> point(){
		Map<String ,Object> map = new HashMap<String ,Object>();
		String[] col = {"pointSignal","pointBusiness","testMileage","testCoverage","testCoverageMain","lteRsrp","lteSnr","bwLink"};
		for(String str:col){
			map.put(str, String.format("%.0f", Math.random()*100));
		}
		return map;
	}
	
	public static List<Map<String ,Object>> cover(){
		List<Map<String ,Object>> list = new ArrayList<Map<String ,Object>>();
		Map<String ,Object> map = null;
		for(int i=0;i<10;i++){
			map = new HashMap<String ,Object>();
			map.put("cityName", "重庆市");
			map.put("contryName", "渝中区");
			map.put("roadName", "中山四路");
			map.put("coverRate", String.format("%.0f", Math.random()*100));
			map.put("rsrp", "-"+String.format("%.0f", Math.random()*100));
			map.put("snr", "-"+String.format("%.0f", Math.random()*100));
			list.add(map);
		}
		return list;
	}
	
	public static List<Map<String ,Object>> CoverInfo(){
		List<Map<String ,Object>> list = new ArrayList<Map<String ,Object>>();
		
		DateFormat  df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, -22);
		
		Map<String ,Object> map = null;
		for(int i=0;i<21;i++){
			c.add(Calendar.DATE, 1);
			String time = df.format(c.getTime());
			map = new HashMap<String ,Object>();
			map.put("operator", "1");
			map.put("time", time);
			map.put("value", String.format("%.0f", Math.random()*100));
			list.add(map);
			
			map = new HashMap<String ,Object>();
			map.put("operator", "2");
			map.put("time", time);
			map.put("value", String.format("%.0f", Math.random()*100));
			list.add(map);
			
			map = new HashMap<String ,Object>();
			map.put("operator", "3");
			map.put("time", time);
			map.put("value", String.format("%.0f", Math.random()*100));
			list.add(map);
		}
		
		return list;
	}
	public static Map<String ,Object> totalInfo(){
		Map<String ,Object> map = new HashMap<String ,Object>();
		map.put("rsrp_range_one",String.format("%.0f", Math.random()*100));
		map.put("rsrp_range_two",String.format("%.0f", Math.random()*100));
		map.put("rsrp_range_three",String.format("%.0f", Math.random()*100));
		map.put("rsrp_range_four",String.format("%.0f", Math.random()*100));
		map.put("rsrp_range_five",String.format("%.0f", Math.random()*100));
		map.put("rsrp_range_six",String.format("%.0f", Math.random()*100));
		map.put("snr_range_one",String.format("%.0f", Math.random()*100));
		map.put("snr_range_two",String.format("%.0f", Math.random()*100));
		map.put("snr_range_three",String.format("%.0f", Math.random()*100));
		map.put("snr_range_four",String.format("%.0f", Math.random()*100));
		map.put("snr_range_five",String.format("%.0f", Math.random()*100));
		map.put("snr_range_six",String.format("%.0f", Math.random()*100));
		return map;
	}
	
	public static List<String> getRange(){
		List<String> list = new ArrayList<String>();
		list.add("(-113,-100)");
		list.add("[-100,-85)");
		list.add("[-85,-60)");
		list.add("[-60,-45)");
		list.add("[-45,-30)");
		list.add("[-15,0)");
		return list;
	}
	
	public static List<String> getColor(){
		List<String> list = new ArrayList<String>();
		list.add("#27a9e3");
		list.add("#28b779");
		list.add("#c64eae");
		list.add("#ffb848");
		list.add("#5369cb");
		list.add("#cd7f32");
		return list;
	}
}
