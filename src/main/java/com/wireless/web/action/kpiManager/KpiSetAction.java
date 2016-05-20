package com.wireless.web.action.kpiManager;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wireless.web.model.KpiBean;
import com.wireless.web.model.User;
import com.wireless.web.service.kpiManager.KpiSetService;
import com.wireless.web.utils.StringUtils;
import com.wireless.web.utils.WebConstants;

@Controller
@RequestMapping("/kpiSet")
public class KpiSetAction {

private static final Logger logger = LoggerFactory.getLogger(KpiSetAction.class);

	@Autowired
	KpiSetService kpiSetService;
	
	@RequestMapping(value = "/kpiSet")
	public ModelAndView kpiSet(HttpServletResponse response ,HttpServletRequest request){
		ModelAndView mv = new ModelAndView("/kpiSet/kpiSet");
		//获取用户信息
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(WebConstants.USER);
		Map<String ,Object> param = new HashMap<String ,Object>();
		param.put("id", user.getUserid());
		param.put("operator", user.getOperator());
		param.put("kpiType", 1);//默认展示RSRP
		Map<String ,Object> map = kpiSetService.getKpi(param);
		mv.addObject("kpiBean",map);
		mv.addObject("unit",StringUtils.KpiU(1));
		return mv;
	}
	
	@RequestMapping(value = "/query")
	public ModelAndView query(HttpServletResponse response ,HttpServletRequest request,Integer kpiType){
		ModelAndView mv = new ModelAndView("/kpiSet/kpiSet");
		//获取用户信息
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(WebConstants.USER);
		Map<String ,Object> param = new HashMap<String ,Object>();
		param.put("id", user.getUserid());
		param.put("operator", user.getOperator());
		param.put("kpiType", kpiType);
		
		Map<String ,Object> map = kpiSetService.getKpi(param);
		mv.addObject("kpiBean",map);
		mv.addObject("unit",StringUtils.KpiU(kpiType));
		return mv;
	}
	
	@RequestMapping(value = "/update")
	public @ResponseBody Map<String ,Object> query(HttpServletResponse response ,HttpServletRequest request,KpiBean kpiBean){
		//获取用户信息
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(WebConstants.USER);
		Map<String ,Object> pm = new HashMap<String ,Object>();
		pm.put("id", user.getUserid());
		pm.put("operator", user.getOperator());
		pm.put("kpiType", kpiBean.getKpiType());
		Map<String ,Object> map = new HashMap<String ,Object>();
		map = kpiSetService.saveKpi(kpiBean,pm);
		return map;
	}
}
