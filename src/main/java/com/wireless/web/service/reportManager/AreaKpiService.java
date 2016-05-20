package com.wireless.web.service.reportManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wireless.support.PageBean;
import com.wireless.web.dao.reportManager.AreaKpiDao;
import com.wireless.web.model.QueryBean;
import com.wireless.web.model.User;
import com.wireless.web.service.commManager.BaseService;
import com.wireless.web.utils.ExportUtils;
import com.wireless.web.utils.StringUtils;
import com.wireless.web.utils.TimeUtils;
import com.wireless.web.utils.WebConstants;

@Service("areaKpiService")
public class AreaKpiService extends BaseService {

	private static final Logger logger = LoggerFactory
			.getLogger(AreaKpiService.class);
	
	@Autowired
	AreaKpiDao areaKpiDao;
	/**
	 * 分页查询
	 * @param params
	 * @return
	 */
	public PageBean queryPage(Map<String, Object> params){
		try {
			PageBean page = super.queryForList(AreaKpiDao.class, params);
			return page;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	public PageBean queryKpi(QueryBean bean ,User user){
		Map<String, Object> param = new HashMap<String, Object>();
		/**
		//时间
		Map<String ,String> se = TimeUtils.getSETime(bean.getQueryTime());
		param.put("starttime" ,se.get("starttime"));
		param.put("endtime" ,se.get("endtime"));
		//区域
		param.put("province", bean.getProvince());
		param.put("district", bean.getDistrict());
		param.put("area", bean.getArea());
		//Kpi
		param.put("kpiId", bean.getKpiType());
		**/
		String uuid = getParam(bean);
		bean.setUuid(uuid);//赋值查询UUID
		param.put("uuid", uuid);
		//分页
		param.put("pageIndex", bean.getPageIndex()==null?WebConstants.PAGE_DEFAULT_PAGEINDEX:bean.getPageIndex());
		param.put("pageSize", bean.getPageSize()==null?WebConstants.PAGE_DEFAULT_PAGESIZE:bean.getPageSize());
		//Kpi
		param.put("kpiId", bean.getKpiType());
		PageBean page = queryPage(param);
		List<Map<String ,Object>> list = Shield((List<Map<String ,Object>>)page.getList() ,user);
		page.setList(list);
		return page;
	}
	
	
	/**
	 * 
	 * @param bean 查询条件
	 * @return
	 */
	public String getParam(QueryBean bean){
		String uuid = null;
		if(bean.getUuid() == null || "".equals(bean.getUuid().trim())){
			uuid = StringUtils.getUuid();
			Map<String ,Object> param = new HashMap<String ,Object>();
			param.put("uuid", uuid);
			//时间
			Map<String ,String> se = TimeUtils.getSETime(bean.getQueryTime());
			param.put("starttime" ,se.get("starttime"));
			param.put("endtime" ,se.get("endtime"));
			//区域
			param.put("province", bean.getProvince());
			param.put("district", bean.getDistrict());
			param.put("area", bean.getArea());
			//Kpi
			param.put("kpiId", bean.getKpiType());
			
			int flag = -1;
			areaKpiDao.areaAnalysis(param);
			flag = NumberUtils.toInt(param.get("flag").toString());
		}else{
			uuid = bean.getUuid();
		}
		return uuid;
	}
	
	public String exportKpi(HttpServletRequest request ,QueryBean bean ,User user) throws Exception{
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("uuid", bean.getUuid());
		//Kpi
		param.put("kpiId", bean.getKpiType());
		List<Map<String ,Object>>list = areaKpiDao.exportList(param);
		
		list = Shield(list ,user);
		String column = StringUtils.KpiToName(bean.getKpiType());
		List<Object> titleName = new ArrayList<Object>();
		List<String> columnName = new ArrayList<String>();
		List<Integer> type = new ArrayList<Integer>();
		String[] tn = {"市名","区县","第一名",column,"第二名",column,"第三名",column};
		String[] cn = {"cityName","countryName","telecoms1","kpiValue1","telecoms2","kpiValue2","telecoms3","kpiValue3"};
		Integer[] tp = {1,1,1,1,1,1,1,1};
		for(int i=0;i<cn.length;i++){
			titleName.add(tn[i]);
			columnName.add(cn[i]);
			type.add(tp[i]);
		}
		List<String> sheetName = new ArrayList<String>();
		sheetName.add("区域指标排名");
		List<String> sheetColumn  = new ArrayList<String>();
		sheetColumn.add("area");
		Map<String ,Object> tln = new HashMap<String ,Object>();
		tln.put("area", titleName);
		Map<String ,Object> col = new HashMap<String ,Object>();
		col.put("area", columnName);
		Map<String ,Object> types = new HashMap<String ,Object>();
		types.put("area", type);
		Map<String ,Object> lt = new HashMap<String ,Object>();
		lt.put("area", list);
		String fileName = ExportUtils.createExcel(request, sheetName,sheetColumn, tln, col ,types , lt);
		return fileName;
	}
	
	/**
	 * 区域权限数据控制
	 * @param list 
	 */
	public List<Map<String ,Object>> Shield(List<Map<String ,Object>> ls ,User user){
		List<Map<String ,Object>> list = new ArrayList<Map<String ,Object>>();
		List<String> operators = StringUtils.getOperator(user.getTeleAuth());
		if(ls.size()>0){
			for(Map<String ,Object> map:ls){
				for(int i=1;i<=3;i++){
					if(!haveAuthority(operators,(String)map.get("telecoms"+i))){//判断是否有对应区域权限
						map.put("telecoms"+i,"-");
						map.put("kpiValue"+i,"-");
					}
				}
				list.add(map);
			}
		}
		return list;
	}
	
	/**
	 * 判断该区域是否有权限
	 * @param operators
	 * @param name
	 * @return
	 */
	public boolean haveAuthority(List<String> operators, String name){
		for(String operator:operators){
			if(operator.equals(name)){
				return true;
			}
		}
		return false;
	}
}
