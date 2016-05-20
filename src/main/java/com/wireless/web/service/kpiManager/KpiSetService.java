package com.wireless.web.service.kpiManager;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wireless.web.dao.kpiManager.KpiSetDao;
import com.wireless.web.model.KpiBean;
import com.wireless.web.utils.StringUtils;

@Service("kpiSetService")
public class KpiSetService {

	private static final Logger logger = LoggerFactory
			.getLogger(KpiSetService.class);
	
	private String [] num = {"one","two","three","four","five","six"};
	
	@Autowired
	protected KpiSetDao kpiSetDao;
	
	/**
	 * 查询对应配置信息
	 * @param param
	 * @return
	 */
	public Map<String ,Object> getKpi(Map<String ,Object> param){
		Map<String ,Object> map = new HashMap<String ,Object>();
		Map<String ,Object> result = kpiSetDao.queryKpi(param);
		map.put("kpiType" ,param.get("kpiType"));
		if(result!=null){
			map.put("kpiGrapId" ,result.get("shape_id"));
			for(int i=1 ;i<=6 ; i++){
				String id = num[i-1];
				map.put("kpiRangVal"+i+"1" ,result.get("range_"+id+"_start"));
				map.put("kpiRangVal"+i+"2" ,result.get("range_"+id+"_end"));
				Map<String ,Object> relation = getRelation((Integer) result.get("range_"+id+"_state"));
				map.put("kpiRangOpt"+i+"1" ,relation.get("start"));
				map.put("kpiRangOpt"+i+"2" ,relation.get("end"));
				map.put("kpiRangColor"+i ,result.get("range_"+id+"_color"));
			}
		}
		return map;
	}
	
	/**
	 * 根据关系id获取符号
	 * @param id
	 * @return
	 */
	public Map<String ,Object> getRelation(Integer id){
		Map<String ,Object> map = new HashMap<String ,Object>();
			if(id == 1||id == 3){
				map.put("start", 1);
			}else if(id == 2||id == 4){
				map.put("start", 2);
			}else{
				map.put("start", 1);
			}
			
			if(id == 1||id == 4){
				map.put("end", 1);
			}else if(id == 2||id == 3){
				map.put("end", 2);
			}else{
				map.put("end", 1);
			}
		return map;
	}
	
	/**
	 * 根据符号获取关系id
	 * @param id
	 * @return
	 */
	public Integer getRelation(Integer start,Integer end){
		int i = 0;
		if(start == 1 && end == 1){
			i = 1;
		}else if(start == 2 && end == 2){
			i = 2;
		}else if(start == 1 && end == 2){
			i = 3;
		}else if(start == 2 && end == 1){
			i = 4;
		}
		return i;
	}
	/**
	 * 保存页面kpi配置
	 * @param kpiBean
	 * @param pm
	 * @return
	 */
	public Map<String ,Object> saveKpi(KpiBean kpiBean ,Map<String ,Object> pm){
		Map<String ,Object> map = new HashMap<String ,Object>();
		Map<String ,Object> opt = kpiSetDao.myKpi(pm);
		
		Map<String, Object> param = new HashMap<String ,Object>();
		try {
			param = createParameter(kpiBean);
			param.put("userid", pm.get("id"));
			param.put("telecoms", pm.get("operator"));
			if(opt!=null){
				kpiSetDao.updateKpi(param);
			}else{
				kpiSetDao.insertKpi(param);
			}
			map.put("result", "1");
		} catch (Exception e) {
			map.put("result", "-1");
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 组装页面配置参数
	 * @param kpiBean
	 * @return
	 */
	private Map<String ,Object> createParameter(KpiBean kpiBean) throws Exception{
		Map<String ,Object> param = new HashMap<String ,Object>();
		param.put("kpi_type", kpiBean.getKpiType());//指标类型
		param.put("kpi_name", StringUtils.getKpi(kpiBean.getKpiType()));//指标名称
		
		Method val1 = null;
		Method val2 = null;
		Method opt1 = null;
		Method opt2 = null;
		Method color = null;
		Class clazz = kpiBean.getClass(); 
		for(int i=1;i<=num.length;i++){
			String id = num[i-1];
			
			val1 = clazz.getDeclaredMethod("getKpiRangVal"+i+"1");
			val2 = clazz.getDeclaredMethod("getKpiRangVal"+i+"2");
			opt1 = clazz.getDeclaredMethod("getKpiRangOpt"+i+"1");
			opt2 = clazz.getDeclaredMethod("getKpiRangOpt"+i+"2");
			color = clazz.getDeclaredMethod("getKpiRangColor"+i);
			
			param.put("range_"+id+"_start", val1.invoke(kpiBean));
			param.put("range_"+id+"_end", val2.invoke(kpiBean));
			param.put("range_"+id+"_state", getRelation((Integer)opt1.invoke(kpiBean),(Integer)opt2.invoke(kpiBean)));
			param.put("range_"+id+"_color", "#"+color.invoke(kpiBean));
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String time = df.format(new Date());
		param.put("shape", StringUtils.getShape(kpiBean.getKpiGrapId()));//图形
		param.put("shape_id", kpiBean.getKpiGrapId());//图形id
		param.put("modify_time", time);//图形id
		param.put("create_time", time);//图形id
		return param;
	}
}
