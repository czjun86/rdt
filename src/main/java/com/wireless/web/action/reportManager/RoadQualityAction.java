package com.wireless.web.action.reportManager;

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
import com.wireless.web.model.QueryBean;
import com.wireless.web.model.User;
import com.wireless.web.service.reportManager.AreaService;
import com.wireless.web.service.reportManager.RoadQualityService;
import com.wireless.web.utils.StringUtils;
import com.wireless.web.utils.TimeUtils;
import com.wireless.web.utils.WebConstants;

@Controller
@RequestMapping("/roadQuality")
public class RoadQualityAction {

	private static final Logger logger = LoggerFactory
			.getLogger(RoadQualityAction.class);
	
	@Autowired
	AreaService areaService;
	
	@Autowired
	RoadQualityService service;
	
	/**
	 * 跳转进入页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/roadQuality")
	public ModelAndView roadQuality(HttpServletRequest request){
		ModelAndView mv = new ModelAndView("/reportManager/roadQuality");
		//获取用户信息
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(WebConstants.USER);
		//查询对应区域信息
		Map<String ,Object> map = areaService.getAreaRoadAuthority(user.getProvince(),user.getDistrict(),user.getCounty(),user);
		//获取自己的运营商权限
		List<Map<String ,Object>> operators = areaService.getMyOperator(user.getTeleAuth());
		PageBean page = new PageBean();
		page.setPageIndex(WebConstants.PAGE_DEFAULT_PAGEINDEX);
		page.setPageSize(WebConstants.PAGE_DEFAULT_PAGESIZE);
		page.setTotalPage(1);
		
		mv.addObject("countrys" ,map.get("countrys"));
		mv.addObject("citys" ,map.get("citys"));
		mv.addObject("areas" ,map.get("areas"));
		mv.addObject("operators" ,operators);
		mv .addObject("page" ,page);
		
		QueryBean bean = new QueryBean();
		bean.setProvince(user.getProvince());
		bean.setDistrict(user.getDistrict());
		bean.setArea(user.getCounty());
		bean.setQueryTime(StringUtils.getTime(1,1));
		bean.setOperator(user.getOperator());
		bean.setCompare(2);
		bean.setKpiType(8);
		mv.addObject("queryBean",bean);
		mv.addObject("user",user);
		mv.addObject("myOperator",StringUtils.getOperatorName(user.getOperator()));
		mv.addObject("otherOperator","对比");
		mv.addObject("kpiName",StringUtils.KpiToName(bean.getKpiType()));
		Map<String ,String> mt =TimeUtils.getOneMoon(bean.getQueryTime());
		mv.addObject("starttime" ,mt.get("starttime"));
		mv.addObject("endtime" ,mt.get("endtime"));
		mv.addObject("hasTime" ,mt.get("hasTime"));
		return mv;
	}
	
	/**
	 * 页面查询数据
	 * @param request
	 * @param bean
	 * @return
	 */
	@RequestMapping(value = "/query")
	public ModelAndView query(HttpServletRequest request ,QueryBean bean){
		ModelAndView mv = new ModelAndView("/reportManager/roadQuality");
		//获取用户信息
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(WebConstants.USER);
		
		//查询对应区域信息
		Map<String ,Object> map = areaService.getAreaRoadAuthority(bean.getProvince(),bean.getDistrict(),bean.getArea(),user);
		//获取自己的运营商权限
		List<Map<String ,Object>> operators = areaService.getMyOperator(user.getTeleAuth());
		PageBean page = new PageBean();
		
		page = service.queryRoadQuality(user ,bean);
		mv.addObject("countrys" ,map.get("countrys"));
		mv.addObject("citys" ,map.get("citys"));
		mv.addObject("areas" ,map.get("areas"));
		mv.addObject("operators" ,operators);
		mv.addObject("page" ,page);
		mv.addObject("queryBean",bean);
		mv.addObject("myOperator",StringUtils.getOperatorName(user.getOperator()));
		mv.addObject("otherOperator",StringUtils.getOperatorName(bean.getOperator()));
		Map<String ,String> mt =TimeUtils.getOneMoon(bean.getQueryTime());
		mv.addObject("starttime" ,mt.get("starttime"));
		mv.addObject("endtime" ,mt.get("endtime"));
		mv.addObject("hasTime" ,mt.get("hasTime"));
		mv.addObject("kpiName",StringUtils.KpiToName(bean.getKpiType()));
		mv.addObject("flag","1");
		return mv;
	}
	

	/**
	 * 页面导出
	 * @param request
	 * @param bean
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/export")
	public @ResponseBody Map<String ,Object> export(HttpServletRequest request ,QueryBean bean) throws Exception{
		//获取用户信息
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(WebConstants.USER);
		
		Map<String ,Object> map = new HashMap<String ,Object>();
		String fileName = service.exportRoadQuality(request,user,bean);
		map.put("fileName", fileName);
		return map;
	}
	
}
