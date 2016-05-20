package com.wireless.web.service.CollectionManager;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wireless.support.PageBean;
import com.wireless.web.dao.CollectionManager.CollectionDao;
import com.wireless.web.model.CollectionBean;
import com.wireless.web.service.commManager.BaseService;
import com.wireless.web.utils.TimeUtils;
import com.wireless.web.utils.WebConstants;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("collectionService")
public class CollectionService extends BaseService {
	
	private static final Logger logger = LoggerFactory
			.getLogger(CollectionService.class);
	@Autowired
	public CollectionDao dao;

	/**
	 * 分页查询
	 * @param params
	 * @return
	 */
	public PageBean queryPage(Map<String, Object> params){
		try {
			PageBean page = super.queryForList(CollectionDao.class, params);
			return page;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * 查询条件组装
	 */
	public PageBean query(String queryTime ,String collectionName ,Integer pageIndex ,
			Integer pageSize){
		Map<String ,Object> param = new HashMap<String ,Object>();
		param.put("pageIndex", pageIndex==null?WebConstants.PAGE_DEFAULT_PAGEINDEX:pageIndex);
		param.put("pageSize", pageSize==null?WebConstants.PAGE_DEFAULT_PAGESIZE:pageSize);
		if(queryTime!=null && !"".equals(queryTime)){
			Map<String ,String> se = TimeUtils.getSETime(queryTime);
			param.put("starttime" ,se.get("starttime")+" 00:00:00");
			param.put("endtime" ,se.get("endtime")+" 23:59:59");
		}
		param.put("name", collectionName!=null&&!"".equals(collectionName)?"%"+collectionName+"%":null);
		PageBean page = queryPage(param);
		return page;
	}
	
	/**
	 * 存储方案
	 * @param bean
	 * @return
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	public Integer save(CollectionBean bean) throws Exception{
		Map<String ,Object> param = new HashMap<String ,Object>();
		String time = TimeUtils.getTime(0);
		if(bean.getId()!=null &&!"".equals(bean.getId()) && bean.getId()!=-1){//修改
			param.put("id", bean.getId());
			//param.put("url", bean.getUrl());
			param.put("name", bean.getName());
			param.put("desc", bean.getMark());
			param.put("content", this.getContent(bean).toString());
			param.put("is_collect", bean.getIsCollect());//是否上报数据
			param.put("is_complex", 0);//是否复合方案   0: 否 1：是  ，默认 0
			param.put("is_del", 0);//是否删除   0: 否 1：是  ，默认 0
			param.put("update_time", time);
			dao.update(param);
		}else{//新增
			//param.put("url", bean.getUrl());
			param.put("name", bean.getName());
			param.put("desc", bean.getMark());
			param.put("content", this.getContent(bean).toString());
			param.put("is_collect", bean.getIsCollect());//是否上报数据
			param.put("is_complex", 0);//是否复合方案   0: 否 1：是  ，默认 0
			param.put("is_del", 0);//是否删除   0: 否 1：是  ，默认 0
			param.put("update_time", time);
			param.put("create_time", time);
			dao.insert(param);
		}
		return 1;
	}
	
	private JSONObject getContent(CollectionBean bean) throws Exception{
		JSONObject titleJson = new JSONObject();
		JSONArray array = new JSONArray();
		titleJson.put("rn", bean.getName());
		titleJson.put("tst", bean.getIsCollect());
		titleJson.put("fdl", bean.getFdl());
		
		JSONObject otherJson = new JSONObject();
		otherJson.put("td", bean.getUrl());
		otherJson.put("tp", bean.getTcpport());
		otherJson.put("up", bean.getUdpport());
		otherJson.put("pn", bean.getPacknum());
		otherJson.put("ps", bean.getPacksize());
		
		Class clazz = bean.getClass();
		JSONObject json = null;
		for(int i=1;i<=5;i++){
			Method val1 = null;
			Method val2 = null;
			Method val3 = null;
			Method val4 = null;
			val1 = clazz.getDeclaredMethod("getStart"+i);
			val2 = clazz.getDeclaredMethod("getEnd"+i);
			val3 = clazz.getDeclaredMethod("getWireless"+i);
			val4 = clazz.getDeclaredMethod("getApp"+i);
			val2.invoke(bean);
			if(val1.invoke(bean)!=null&&!"".equals(val1.invoke(bean))&&val2.invoke(bean)!=null&&!"".equals(val2.invoke(bean))){
				json = new JSONObject();
				json.put("ts", val1.invoke(bean));
				json.put("te", val2.invoke(bean));
				json.put("wat", val3.invoke(bean)==null||"".equals(val3.invoke(bean))?"0":"1");
				json.put("wrc", val3.invoke(bean));
				json.put("aat", val4.invoke(bean)==null||"".equals(val4.invoke(bean))?"0":"1");
				json.put("arc", val4.invoke(bean));
				array.add(json);
			}
		}
		otherJson.put("tr", array);
		JSONArray ay = new JSONArray();
		ay.add(otherJson);
		titleJson.put("ct", ay);
		return titleJson;
	}

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public Integer delete(Integer id) {
		Integer flag = -1;
		try {
			Integer i = dao.isUsed(id);
			if(i == 0){
				Map<String ,Object> param = new HashMap<String ,Object>();
				String time = TimeUtils.getTime(0);
				param.put("id", id);
				param.put("is_del", 1);//是否删除   0: 否 1：是  ，默认 0
				param.put("update_time", time);
				dao.delete(param);
				flag = 1;
			}else{
				flag = 2;
			}
		} catch (Exception e) {
			logger.error("delete collection" ,e);
		}
		return flag;
	}

	/**
	 * 查询修改的方案信息
	 * @param id
	 * @return
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	public CollectionBean queryCollection(Integer id) throws Exception {
		CollectionBean bean = new CollectionBean();
		Map<String ,Object> map = dao.queryCollection(id);
		bean.setId(id);
		bean.setIsCollect((Integer) map.get("is_collect"));
		//bean.setUrl(map.get("url").toString());
		bean.setName(map.get("name").toString());
		bean.setMark(map.get("desc")!=null?map.get("desc").toString():"");
		
		JSONObject titlejson = JSONObject.fromObject(map.get("content"));
		bean.setFdl((Integer) titlejson.get("fdl"));
		JSONArray ay = (JSONArray) titlejson.get("ct");
		JSONObject json = (JSONObject)(ay.get(0));
		bean.setTcpport((Integer) json.get("tp"));
		bean.setUdpport((Integer) json.get("up"));
		bean.setPacknum((Integer) json.get("pn"));
		bean.setPacksize((Integer) json.get("ps"));
		bean.setUrl(json.get("td").toString());
		JSONArray array = (JSONArray) json.get("tr");
		String[] name = {"setStart","setEnd","setWireless","setApp"};
		String[] words = {"ts","te","wrc","arc"};
		for(int i=0;i<array.size();i++){
			JSONObject js = (JSONObject) array.get(i);
			Class clazz = bean.getClass();
			
			for(int j=0;j<name.length;j++){
				Method val = null;
				val = clazz.getDeclaredMethod(name[j]+(i+1) ,Integer.class);
				val.invoke(bean,(Integer) js.get(words[j]));
			}
		}
		return bean;
	}

	/**
	 * 判断用户是否重复
	 * @param name
	 * @return
	 */
	public Integer checkName(String name) {
		return dao.checkName(name);
	}
}
