package com.wireless.web.service.epinfoManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wireless.support.PageBean;
import com.wireless.web.dao.epinfoManager.EpinfoDao;
import com.wireless.web.dao.userManager.GroupDao;
import com.wireless.web.model.EPQueryBean;
import com.wireless.web.model.EpinfoBean;
import com.wireless.web.service.commManager.BaseService;
import com.wireless.web.utils.ExportUtils;
import com.wireless.web.utils.TimeUtils;
import com.wireless.web.utils.WebConstants;

@Service("epinfoService")
public class EpinfoService extends BaseService {

	private static final Logger logger = LoggerFactory
			.getLogger(EpinfoService.class);
	@Autowired
	public EpinfoDao dao;
	
	/**
	 * 分页查询
	 * @param params
	 * @return
	 */
	public PageBean queryPage(Map<String, Object> params){
		try {
			PageBean page = super.queryForList(EpinfoDao.class, params);
			return page;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * 父页面分页查询
	 * @param bean
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public PageBean queryInfo(EPQueryBean bean ,Integer pageIndex ,Integer pageSize){
		Map<String, Object> param = install(bean,pageIndex,pageSize);
		PageBean page = queryPage(param);
		return page;
	}
	/**
	 * 组装父页面查询条件
	 * @param bean
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public Map<String, Object> install(EPQueryBean bean ,Integer pageIndex ,Integer pageSize){
		Map<String, Object> param = new HashMap<String, Object>();
		//分页条件
		param.put("pageIndex", pageIndex==null?WebConstants.PAGE_DEFAULT_PAGEINDEX:pageIndex);
		param.put("pageSize",pageSize==null?WebConstants.PAGE_DEFAULT_PAGESIZE:pageSize);
		if(bean.getQueryTime()!=null &&!"".equals(bean.getQueryTime())){
			//时间条件
			Map<String ,String> time = TimeUtils.getSETime(bean.getQueryTime());
			param.put("starttime" ,time.get("starttime")+" 00:00:00");
			param.put("endtime" ,time.get("endtime")+" 23:59:59");
		}
		//区域条件
		String regionCode = null;
		if(!"-1".equals(bean.getArea())){
			regionCode = bean.getArea();
		}else{
			if(!"-1".equals(bean.getDistrict())){
				regionCode = bean.getDistrict().substring(0, 4)+"%";
			}else{
				regionCode = bean.getProvince().substring(0, 2)+"%";
			}
		}
		param.put("regionCode" ,regionCode);
		//运营商
		param.put("operator" ,bean.getOperator());
		//imei
		param.put("imei" ,bean.getImei()!=null?bean.getImei().trim()+"%":null);
		//终端型号
		param.put("model" ,bean.getModel());
		//采集方案
		param.put("colletion" ,bean.getColletion());
		//版本号
		param.put("vision" ,bean.getVision());
		return param;
	}
	public List<Map<String,Object>> queryCollection(){
		List<Map<String,Object>> list = dao.queryCollection();
		return list;
	}
	
	public List<Map<String,Object>> queryModel(){
		List<Map<String,Object>> list = dao.queryModel();
		return list;
	}
	
	public List<Map<String,Object>> queryVision(){
		List<Map<String,Object>> list = dao.queryVision();
		return list;
	}
	
	public List<String> getEpName(String name){
		return dao.getEpName(name);
	}

	public void save(EpinfoBean bean) throws Exception {
		Map<String ,Object> map = installChild(bean);
		if(bean.getId()!=null && !"".equals(bean.getId()) && bean.getId()!=-1){
			dao.updateInfo(map);
		}else{
			dao.insertInfo(map);
		}
	}
	
	public Map<String ,Object> installChild(EpinfoBean bean){
		Map<String ,Object> map = new HashMap<String ,Object>();
		map.put("id", bean.getId());
		map.put("imei", bean.getImei());
		map.put("term_model", bean.getModel());
		map.put("telecoms", bean.getOperator());
		map.put("soft_ver", bean.getVision());
		map.put("rule_name", bean.getColletion());
		map.put("province", bean.getProvince());
		map.put("district", bean.getDistrict());
		map.put("area", bean.getArea());
		map.put("region_code", bean.getRegionCode());
		map.put("is_del", 0);
		map.put("memo", bean.getMark());
		String time = TimeUtils.getTime(0);
		map.put("update_time", time);
		map.put("create_time", time);
		return map;
	}

	public Integer checkName(String imei) {
		return dao.checkName(imei);
	}

	/**
	 * 获取批量修改id
	 * @param ids
	 * @param bean
	 * @return
	 */
	public List<Integer> getUpdateList(String ids, EPQueryBean bean) {
		List<Integer> list = new ArrayList<Integer>();
		if("-1".equals(ids)){
			Map<String ,Object> param = install(bean,null,null);
			list = dao.getIds(param);
		}else if(ids.length()>0){
			String[] s = ids.split(",");
			for(String id:s){
				list.add(Integer.valueOf(id));
			}
		}
		return list;
	}

	/**
	 * 批量保存
	 * @param list
	 * @param value
	 * @param type  1  方案, 2  版本
	 * @throws Exception
	 */
	public void updateLarge(List<Integer> list, String value,String type) throws Exception {
		List<Map<String ,Object>> ll = new ArrayList<Map<String ,Object>>();
		Map<String ,Object> map = null;
		String time = TimeUtils.getTime(0);
		for(Integer id:list){
			map = new HashMap<String ,Object>();
			map.put("update_time", time);
			map.put("value", value);
			map.put("id", id);
			map.put("type", type);
			ll.add(map);
		}
		//批量插入新关系
		this.baseDao.batchInsert(EpinfoDao.class, ll, 200);
	}

	public Map<String, Object> queryChild(Integer id) {
		Map<String, Object> map = dao.queryChild(id);
		return map;
	}

	public String createExport(HttpServletRequest request ,EPQueryBean bean) throws Exception {
		Map<String, Object> param = install(bean,0,0);
		List<Map<String ,Object>> list = dao.queryAll(param);
		List<Object> titleName = new ArrayList<Object>();
		List<String> columnName = new ArrayList<String>();
		List<Integer> type = new ArrayList<Integer>();
		String[] tn = {"区域","运营商","终端型号","IMEI","采集方案","版本","录入时间","是否上报","备注"};
		String[] cn = {"area","telecoms","term_model","imei","rule_name","soft_ver","update_time","is_collect","memo"};
		Integer[] tp = {1,1,1,1,1,1,1,1,1};
		for(int i=0;i<cn.length;i++){
			titleName.add(tn[i]);
			columnName.add(cn[i]);
			type.add(tp[i]);
		}
		List<String> sheetName = new ArrayList<String>();
		sheetName.add("终端信息");
		List<String> sheetColumn  = new ArrayList<String>();
		sheetColumn.add("epinfo");
		Map<String ,Object> tln = new HashMap<String ,Object>();
		tln.put("epinfo", titleName);
		Map<String ,Object> col = new HashMap<String ,Object>();
		col.put("epinfo", columnName);
		Map<String ,Object> types = new HashMap<String ,Object>();
		types.put("epinfo", type);
		Map<String ,Object> lt = new HashMap<String ,Object>();
		lt.put("epinfo", list);
		String fileName = ExportUtils.createExcel(request, sheetName,sheetColumn, tln, col ,types , lt);
		return fileName;
	}
}
