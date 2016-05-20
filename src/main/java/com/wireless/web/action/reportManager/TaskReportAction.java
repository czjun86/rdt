package com.wireless.web.action.reportManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wireless.support.PageBean;
import com.wireless.uitl.HelpUtil;
import com.wireless.web.model.QueryBean;
import com.wireless.web.model.User;
import com.wireless.web.service.reportManager.AreaService;
import com.wireless.web.service.reportManager.ComprehensiveService;
import com.wireless.web.service.reportManager.TaskReportService;
import com.wireless.web.utils.ReplenishRoadPointsUtils;
import com.wireless.web.utils.StringUtils;
import com.wireless.web.utils.TimeUtils;
import com.wireless.web.utils.WebConstants;

@Controller
@RequestMapping("/taskReport")
public class TaskReportAction {
	
	private static final Logger logger = LoggerFactory
			.getLogger(TaskReportAction.class);
	
	@Autowired
	TaskReportService service;
	
	
	/**
	 * 跳转进入页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/taskReport")
	public ModelAndView compreQuota(HttpServletRequest request){
		ModelAndView mv = new ModelAndView("/reportManager/taskReport");
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(WebConstants.USER);
		Map<String ,Object> params = new HashMap<String ,Object>();
		params.put("pageIndex", WebConstants.PAGE_DEFAULT_PAGEINDEX);
		params.put("pageSize", WebConstants.PAGE_DEFAULT_PAGESIZE);
		params.put("userId", user.getUserid());
		
		PageBean page = new PageBean();
		page = service.queryPage(params);
		
		mv .addObject("page" ,page);
		
		
		return mv;
	}
	
	/**
	 * 
	 * @param request
	 * @param bean 查询条件
	 * @return
	 */
	@RequestMapping(value = "/query")
	public ModelAndView query(HttpServletRequest request ,QueryBean bean){
		ModelAndView mv = new ModelAndView("/reportManager/taskReport");
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(WebConstants.USER);
		
		//查询
		Map<String ,Object> params = new HashMap<String ,Object>();
		params.put("pageIndex", bean.getPageIndex()==null?WebConstants.PAGE_DEFAULT_PAGEINDEX:bean.getPageIndex());
		params.put("pageSize", bean.getPageSize()==null?WebConstants.PAGE_DEFAULT_PAGESIZE:bean.getPageSize());
		params.put("userId", user.getUserid());
		if(bean.getQueryTime()!=null&&!bean.getQueryTime().equals("")){
			Map<String ,String> se = TimeUtils.getSETime(bean.getQueryTime());
			params.put("starttime" ,se.get("starttime")+" 00:00:00");
			params.put("endtime" ,se.get("endtime")+" 23:59:59");
		}
		
		params.put("taskName", bean.getTaskName());
		PageBean page = new PageBean();
		page = service.queryPage(params);
		
		mv.addObject("queryTime",bean.getQueryTime());
		mv .addObject("page" ,page);
		mv.addObject("queryBean",bean);
		return mv;
	}
	
	/**
	 * spark任务提交
	 * @param request
	 * @param bean 导出
	 * @return
	 */
	@RequestMapping(value = "/submitTask")
	public @ResponseBody String submitTask(HttpServletRequest request ,QueryBean bean){
		String map = "scc";
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(WebConstants.USER);
		//设置条件
		Map<String, Object> param = getParam(bean,user);
		//调用jobserver提交任务
		String re = postToJob(param);
		//保存这条任务到任务表
		if(re.equals("scc")){
			map = service.saveTask(param);
		}else{
			map = re;
		}
		
		return map;
	}
	
	private Map<String ,Object> getParam(QueryBean bean ,User user){
		Map<String ,Object> param = new HashMap<String ,Object>();
		String uuid = StringUtils.getUuid();//生成任务编号
		param.put("uuid", uuid);
		param.put("taskName", bean.getTaskName());
		//用户信息
		param.put("userId", user.getUserid());
		param.put("operator", user.getOperator());
		//时间
		param.put("timegransel", bean.getTimegransel());
		Map<String ,String> se = TimeUtils.getSETime(bean.getQueryTime());
		param.put("starttime" ,se.get("starttime"));
		param.put("endtime" ,se.get("endtime"));
		//运营商
		param.put("telecoms", bean.getOperator());
		//imei
		param.put("imei", bean.getImei()!=null&&!"".equals(bean.getImei())?bean.getImei():"-1");
		//区域
		param.put("province", bean.getProvince());
		param.put("district", bean.getDistrict());
		param.put("area", bean.getArea());
		//道路
		param.put("roadType", bean.getRoadType()!=null?bean.getRoadType():-1);
		param.put("roadLevel", bean.getRoadLevel()!=null?bean.getRoadLevel():-1);
		param.put("roadId", bean.getRoadId()!=null?bean.getRoadId().equals("")?"-1":bean.getRoadId():"-1");
		//大于12Mbps占比
		param.put("threshold", Integer.valueOf(bean.getThreshold().intValue()));
		//维度
		param.put("showTime", bean.getShowTime()!=null?bean.getShowTime():0);
		param.put("showArea", bean.getShowArea()!=null?bean.getShowArea():0);
		param.put("showRoad", bean.getShowRoad()!=null?bean.getShowRoad():0);
		param.put("showOperator", bean.getShowOperator()!=null?bean.getShowOperator():0);
		param.put("showInterval", bean.getShowInterval()!=null?bean.getShowInterval():0);
		
		//仅用于展示的查询条件
		param.put("queryTime_ps", bean.getQueryTime());
		param.put("roadName_ps", bean.getRoadName());
		param.put("showRsrp_ps", bean.getRsrp()!=null?bean.getRsrp():0);
		param.put("showRsrq_ps", bean.getRsrq()!=null?bean.getRsrq():0);
		param.put("showSnr_ps", bean.getSnr()!=null?bean.getSnr():0);
		param.put("showBroadband_ps", bean.getBroadband()!=null?bean.getBroadband():0);
		param.put("showDelay_ps", bean.getDelay()!=null?bean.getDelay():0);
		param.put("showLose_ps", bean.getLose()!=null?bean.getLose():0);
		param.put("province_ps", bean.getProvinceName());
		param.put("district_ps", bean.getDistrictName());
		param.put("area_ps", bean.getAreaName());
		param.put("operator_ps", bean.getOperatorName());//运营商的查询条件
		
		return param;
	}
	
	private String postToJob(Map<String ,Object> param){
		String re = "scc";
		BufferedReader br = null;
		HttpURLConnection connection = null;
		PrintWriter printWriter = null;
		// 组织请求参数  
		StringBuffer ps = new StringBuffer(); 
        Iterator it = param.entrySet().iterator();  
        while (it.hasNext()) {
            Map.Entry element = (Map.Entry) it.next();
            if(!element.getKey().toString().endsWith("_ps")){
            	ps.append(element.getKey());  
                ps.append("=");
                ps.append(element.getValue());  
                ps.append("&");
            }
        }  
        if (ps.length() > 0) {  
        	ps.deleteCharAt(ps.length() - 1);  
        }
		try {
			//读取jobServer配置
			String path = "config/taskReport.properties";
			Properties poper = HelpUtil.readProperties(path);
			StringBuffer sf = new StringBuffer("http://");
			sf.append(poper.getProperty("josServerAddress"));
			sf.append(":");
			sf.append(poper.getProperty("josServerPort"));
			sf.append("/");
			sf.append(poper.getProperty("josServerName"));
			URL url = new URL(sf.toString());
			connection = (HttpURLConnection) url.openConnection();// 打开连接
			connection.setRequestMethod("POST");
			// Post 请求不能使用缓存
	        connection.setUseCaches(false);
			connection.setDoOutput(true);  
			connection.setDoInput(true);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			connection.setRequestProperty("Connection", "keep-alive");
			//发送Post请求
			connection.connect();
			// 获取URLConnection对象对应的输出流  
            printWriter = new PrintWriter(connection.getOutputStream());
			// 发送请求参数  
            printWriter.write(ps.toString());  
            // flush输出流的缓冲  
            printWriter.flush();  
         // 根据ResponseCode判断连接是否成功  
            int responseCode = connection.getResponseCode();  
            if (responseCode != 200) {  
                re ="连接失败!";
            }
            
            // 获取输入流
            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder sl = new StringBuilder();
            while ((line = br.readLine()) != null) {// 循环读取流
            	sl.append(line);
            }
            
//            re = sl.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(printWriter!=null){
				printWriter.close();
			}
			if(connection!=null){
				connection.disconnect();
			}
			
		}
		
		return re;
	}
	
}
