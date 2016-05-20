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
import com.wireless.web.dao.reportManager.RoadQualityDao;
import com.wireless.web.model.QueryBean;
import com.wireless.web.model.User;
import com.wireless.web.service.commManager.BaseService;
import com.wireless.web.utils.ExportUtils;
import com.wireless.web.utils.StringUtils;
import com.wireless.web.utils.TimeUtils;
import com.wireless.web.utils.WebConstants;

@Service("roadQualityService")
public class RoadQualityService extends BaseService  {

	private static final Logger logger = LoggerFactory
			.getLogger(RoadQualityService.class);
	
	@Autowired
	RoadQualityDao roadQualityDao;
	
	/**
	 * 分页查询
	 * @param params
	 * @return
	 */
	public PageBean queryPage(Map<String, Object> params){
		try {
			PageBean page = super.queryForList(RoadQualityDao.class, params);
			return page;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * 分页查询
	 * @param user
	 * @param bean
	 * @return
	 */
	public PageBean queryRoadQuality(User user , QueryBean bean){
		Map<String, Object> param = new HashMap<String, Object>();
		
		String uuid = getParam(user,bean);
		bean.setUuid(uuid);//赋值查询UUID
		param.put("uuid", uuid);
		param.put("compare", bean.getCompare());
		//阀值
		param.put("threshold", bean.getThreshold());
		//分页
		param.put("pageIndex", bean.getPageIndex()==null?WebConstants.PAGE_DEFAULT_PAGEINDEX:bean.getPageIndex());
		param.put("pageSize", bean.getPageSize()==null?WebConstants.PAGE_DEFAULT_PAGESIZE:bean.getPageSize());
		PageBean page= queryPage(param);
		return page;
	}
	
	/**
	 * 导出
	 * @param user
	 * @param bean
	 * @return
	 * @throws Exception 
	 */
	public String exportRoadQuality(HttpServletRequest request ,User user , QueryBean bean) throws Exception{
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("uuid", bean.getUuid());
		param.put("compare", bean.getCompare());
		//阀值
		param.put("threshold", bean.getThreshold());
		List<Map<String ,Object>> list = roadQualityDao.exportList(param);
		
		String kpiName = StringUtils.KpiToName(bean.getKpiType());
		List<Object> titleName = new ArrayList<Object>();
		List<String> columnName = new ArrayList<String>();
		List<Integer> type = new ArrayList<Integer>();
		
		String [] title = {"市名","区县","道路名称",StringUtils.getOperatorName(user.getOperator())+" - "+kpiName,StringUtils.getOperatorName(bean.getOperator())+" - "+kpiName,"对比指标差值"};
		String [] column = {"cityName","countryName","roadName","selfValue","compValue","diffValue"};
		Integer [] tp = {1,1,1,2,2,2};
		//通用列名
		for(int i=0;i<title.length;i++){
			titleName.add(title[i]);
			columnName.add(column[i]);
			type.add(tp[i]);
		}
		if(bean.getKpiType()!=1){
			titleName.add(StringUtils.KpiToName(1));
			columnName.add("lteRsrp");
			type.add(2);
		}
		if(bean.getKpiType()!=8){
			titleName.add(StringUtils.KpiToName(8));
			columnName.add("wirelessCoverage");
			type.add(2);
		}
		if(bean.getKpiType()!=2){
			titleName.add(StringUtils.KpiToName(2));
			columnName.add("lteSnr");
			type.add(2);
		}
		if(bean.getKpiType()!=4){
			titleName.add(StringUtils.KpiToName(4));
			columnName.add("bwLink");
			type.add(2);
		}
		if (bean.getKpiType() != 9) {
			titleName.add(StringUtils.KpiToName(9));
			columnName.add("networkRate");
			type.add(1);
		}
		if (bean.getKpiType() != 10) {
			titleName.add(StringUtils.KpiToName(10));
			columnName.add("pingRate");
			type.add(1);
		}
		titleName.add(StringUtils.KpiToName(11));
		columnName.add("lteToWcdma");
		type.add(1);
		
		List<String> sheetName = new ArrayList<String>();
		sheetName.add("道路覆盖质量对比分析");
		List<String> sheetColumn  = new ArrayList<String>();
		sheetColumn.add("roadQuality");
		
		Map<String ,Object> tln = new HashMap<String ,Object>();
		tln.put("roadQuality", titleName);
		Map<String ,Object> col = new HashMap<String ,Object>();
		col.put("roadQuality", columnName);
		Map<String ,Object> types = new HashMap<String ,Object>();
		types.put("roadQuality", type);
		Map<String ,Object> lt = new HashMap<String ,Object>();
		lt.put("roadQuality", list);
		String fileName = ExportUtils.createExcel(request, sheetName,sheetColumn, tln, col ,types , lt);
		return fileName;
	}
	
	/**
	 * 组装查询条件
	 * @param user
	 * @param bean
	 * @return
	 */
	public String getParam(User user , QueryBean bean){
		String uuid = null;
		if(bean.getUuid() == null || "".equals(bean.getUuid().trim())){
			uuid = StringUtils.getUuid();
			Map<String ,Object> param = new HashMap<String ,Object>();
			param.put("uuid", uuid);
			//用户信息
			param.put("operator", user.getOperator());
			//时间
			Map<String ,String> se = TimeUtils.getSETime(bean.getQueryTime());
			param.put("starttime" ,se.get("starttime"));
			param.put("endtime" ,se.get("endtime"));
			//区域
			param.put("province", bean.getProvince());
			param.put("district", bean.getDistrict());
			param.put("area", bean.getArea());
			//道路
			param.put("roadType", bean.getRoadType());
			param.put("roadLevel", bean.getRoadLevel());
			//对比运营商
			param.put("telecoms", bean.getOperator());
			//阀值
			param.put("compValue", bean.getThreshold());
			//对比方式
			param.put("compare", bean.getCompare());
			//kpi
			param.put("kpiId", bean.getKpiType());
			
			int flag = -1;
			roadQualityDao.roadAnalysis(param);
			flag = NumberUtils.toInt(param.get("flag").toString());
		}else{
			uuid = bean.getUuid();
		}
		return uuid;
	}
}
