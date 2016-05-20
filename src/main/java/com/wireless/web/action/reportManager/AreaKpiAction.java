package com.wireless.web.action.reportManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.wireless.web.model.Area;
import com.wireless.web.model.QueryBean;
import com.wireless.web.model.User;
import com.wireless.web.service.reportManager.AreaKpiService;
import com.wireless.web.service.reportManager.AreaService;
import com.wireless.web.utils.StringUtils;
import com.wireless.web.utils.WebConstants;

@Controller
@RequestMapping("/areaKpiTop")
public class AreaKpiAction {

	private static final Logger logger = LoggerFactory
			.getLogger(AreaKpiAction.class);
	
	@Autowired
	AreaService areaService;
	@Autowired
	AreaKpiService areaKpiService;
	
	@RequestMapping(value = "/areaKpiTop")
	public ModelAndView roadCompare(HttpServletRequest request){
		ModelAndView mv = new ModelAndView("/reportManager/areaKpiTop");
		//获取用户信息
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(WebConstants.USER);
		//查询对应区域信息
		Map<String ,Object> map = areaService.getAreaRoadAuthority(user.getProvince(),user.getDistrict(),user.getCounty(),user);
		mv.addObject("countrys" ,map.get("countrys"));
		mv.addObject("citys" ,map.get("citys"));
		mv.addObject("areas" ,map.get("areas"));
		mv.addObject("queryTime",StringUtils.getTime(1,1));
		
		QueryBean bean = new QueryBean();
		bean.setProvince(user.getProvince());
		bean.setDistrict(user.getDistrict());
		bean.setArea(user.getCounty());
		bean.setQueryTime(StringUtils.getTime(1,1));
		bean.setKpiType(8);
		mv.addObject("queryBean",bean);
		
		mv.addObject("kpiName",StringUtils.KpiToName(bean.getKpiType()));
		
		PageBean page = new PageBean();
		page.setPageIndex(WebConstants.PAGE_DEFAULT_PAGEINDEX);
		page.setPageSize(WebConstants.PAGE_DEFAULT_PAGESIZE);
		page.setTotalPage(1);
		mv.addObject("page",page);
		return mv;
	}
	
	
	@RequestMapping(value = "/query")
	public ModelAndView queryKpi(HttpServletRequest request ,QueryBean bean){
		ModelAndView mv = new ModelAndView("/reportManager/areaKpiTop");
		//获取用户信息
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(WebConstants.USER);
		//查询分页数据
		PageBean page = new PageBean();
		try {
			page = areaKpiService.queryKpi(bean,user);
		} catch (Exception e1) {
			
			e1.printStackTrace();
			logger.error("区域指标TOP查询失败！", e1);
		}
		
		//查询对应区域信息
		Map<String ,Object> map = areaService.getAreaRoadAuthority(bean.getProvince(),bean.getDistrict(),bean.getArea(),user);
		mv.addObject("countrys" ,map.get("countrys"));
		mv.addObject("citys" ,map.get("citys"));
		mv.addObject("areas" ,map.get("areas"));
		mv.addObject("queryTime",StringUtils.getTime(1,1));
		mv.addObject("queryBean",bean);
		mv.addObject("kpiName",StringUtils.KpiToName(bean.getKpiType()));
		mv.addObject("page",page);
		mv.addObject("flag","1");
		return mv;
	}
	
	/**
	 * 
	 * @param request
	 * @param bean 导出
	 * @return
	 */
	@RequestMapping(value = "/export")
	public @ResponseBody Map<String ,String> export(HttpServletRequest request ,QueryBean bean){
		Map<String ,String> map = new HashMap<String ,String>();
		//获取用户信息
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(WebConstants.USER);
		//查询分页数据
		String fileName;
		try {
			fileName = areaKpiService.exportKpi(request,bean,user);
		} catch (Exception e) {
			fileName = "-1";
			logger.error("create excel for AreaKpiTop",e);
		}
		map.put("fileName", fileName);
		return map;
	}
}
