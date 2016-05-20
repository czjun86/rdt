package com.wireless.web.service.reportManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wireless.web.dao.reportManager.IntervalDao;
import com.wireless.web.model.User;
import com.wireless.web.utils.StringUtils;

@Service("intervalService")
public class IntervalService {

	private String[] col ={"one","two","three","four","five","six"};
	@Autowired
	IntervalDao dao;
	/**
	 * 获取对应区间值,对应区域按颜色,对应指标形状
	 * @param user
	 * @param quota
	 * @return
	 */
	public Map<String ,Object> getOwnInterval(Integer userId ,Integer telecoms ,String kpiId){
		//组装查询条件
		Map<String ,Object> param = new HashMap<String ,Object>();
		param.put("id",userId);
		param.put("telecoms", telecoms);
		param.put("kpiId", kpiId);
		//查询数据
		Map<String ,Object> result = dao.queryInterval(param);
		
		//组装区间数据
		List<String> intervalList = assembleInterval(result);
		//组装颜色数据
		List<String> colorList = assembleColor(result);
		
		Map<String ,Object> map = new HashMap<String ,Object>();
		map.put("interval", intervalList);//区间值集合
		map.put("color", colorList);//颜色集合
		map.put("shape", result!=null?result.get("shape"):"BMAP_POINT_SHAPE_CIRCLE");//形状
		return map;
	}
	
	/**
	 * 组装区间值
	 * @param param
	 * @return
	 */
	public List<String> assembleInterval(Map<String ,Object> result){
		List<String> list = new ArrayList<String>();
		if(result!=null){
			for(String str : col){
				StringBuilder bd = new StringBuilder();
				int i = (int) result.get("range_"+str+"_state");
				if(i == 1){
					bd.append("(");
					bd.append(result.get("range_"+str+"_start").toString());
					bd.append(",");
					bd.append(result.get("range_"+str+"_end").toString());
					bd.append(")");
				}else if(i == 2){
					bd.append("[");
					bd.append(result.get("range_"+str+"_start").toString());
					bd.append(",");
					bd.append(result.get("range_"+str+"_end").toString());
					bd.append("]");
				}else if(i == 3){
					bd.append("(");
					bd.append(result.get("range_"+str+"_start").toString());
					bd.append(",");
					bd.append(result.get("range_"+str+"_end").toString());
					bd.append("]");
				}else if(i == 4){
					bd.append("[");
					bd.append(result.get("range_"+str+"_start").toString());
					bd.append(",");
					bd.append(result.get("range_"+str+"_end").toString());
					bd.append(")");
				}
				list.add(bd.toString());
			}
		}else{
			for(String str : col){
				list.add("(-,-)");
			}
		}
		return list;
	}
	
	/**
	 * 组装颜色
	 * @param param
	 * @return
	 */
	public List<String> assembleColor(Map<String ,Object> result){
		List<String> list = new ArrayList<String>();
		if(result!=null){
			for(String str : col){
				list.add(result.get("range_"+str+"_color").toString());
			}
		}else{
			for(String str : col){
				list.add("#0F0F0F");
			}
		}
		return list;
	}
}
