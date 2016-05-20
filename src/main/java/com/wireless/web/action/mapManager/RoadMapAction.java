package com.wireless.web.action.mapManager;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.wireless.web.model.Area;
import com.wireless.web.model.KpiSettings;
import com.wireless.web.model.Points;
import com.wireless.web.model.Pot;
import com.wireless.web.model.QueryBean;
import com.wireless.web.model.RoadPoints;
import com.wireless.web.model.User;
import com.wireless.web.model.VoBean;
import com.wireless.web.service.mapManager.MapService;
import com.wireless.web.service.mapManager.RoadMapService;
import com.wireless.web.service.reportManager.AreaService;
import com.wireless.web.utils.StringUtils;
import com.wireless.web.utils.WebConstants;


@Controller
@RequestMapping("/roadMap")
public class RoadMapAction {
private static final Logger logger = LoggerFactory.getLogger(RoadMapAction.class);
	
	@Autowired
	private RoadMapService roadMapService;
//	@Autowired
//	private TrendService trendService;
	@Autowired
	private AreaService areaService;
	/**
	 * 页面初始化,根据用户ID查询出对应的一级行政划
	 * @param request
	 * @return
	 */
	
	@RequestMapping(value = "/roadMap")
	public ModelAndView mapList(HttpServletRequest request,VoBean vo) {
	
		//获取用户信息
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(WebConstants.USER);
		ModelAndView mv = new ModelAndView("/mapManager/roadMap");
		VoBean vv=new VoBean();
		Map<String ,Object> map=new HashMap<String ,Object>();
		if(vo!=null&&vo.getProvince()!=null){
			vv=vo;
			mv.addObject("tiaozhuan", "1");
			vv.setUserId(user.getUserid());
			if(vv.getRoadId()!=null&&!"-1".equals(vv.getRoadId())){
				vv.setRoadName(areaService.getRoadName(vv.getRoadId()));
			}
			map = areaService.getAreaRoadAuthority(vv.getProvince(),vv.getDistrict(),vv.getArea(),vo.getRoadType(),vo.getRoadLevel(),user);
			mv.addObject("queryTime",vv.getStarttime()+"-"+vv.getEndtime());
		}else{
			vv.setUserId(user.getUserid());
			vv.setKpiType(1);
			vv.setOperator(Integer.parseInt(user.getOperator()));
			vv.setKpiInterv("7,1,2,3,4,5,6");
			vv.setProvince(user.getProvince());
			vv.setDistrict(user.getDistrict());
			vv.setArea(user.getCounty());
			vv.setWarn("-1");
			mv.addObject("queryTime",StringUtils.getTime(2,1));
			map = areaService.getAreaRoadAuthority(user.getProvince(),user.getDistrict(),user.getCounty(),-1,-1,user);
			vv.setRoadId("-1");
		}
		//获取自己的运营商权限
		List<Map<String ,Object>> operators = areaService.getMyOperator(user.getTeleAuth());
		mv.addObject("operators" ,operators);
		//查询对应区域信息
		List<KpiSettings> kpis=roadMapService.getKpi(vv,"0");
		List<KpiSettings> kpisWarn=new ArrayList<KpiSettings>();
		if("9".equals(vv.getWarn())||"10".equals(vv.getWarn())){
			 kpisWarn=roadMapService.getKpi(vv,"1");
		}
		Area area=roadMapService.getAreaNameByUserId(user.getUserid()+"");
		mv.addObject("kpisWarn", kpisWarn.size()>0?kpisWarn.get(0):null);
		mv.addObject("oneregionname", area.getText());
		mv.addObject("countrys" ,map.get("countrys"));
		mv.addObject("citys" ,map.get("citys"));
		mv.addObject("areas" ,map.get("areas"));
		mv.addObject("roads" ,map.get("roads"));
		mv.addObject("kpis", kpis.get(0));
		mv.addObject("user", user);
		mv.addObject("vo", vv);
		return mv;
	}
	
	/**
	 * 查询区间值

	 * @return
	 */
	@RequestMapping(value = "/mapkpi", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> mapkpi(HttpServletRequest request,String vo) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(WebConstants.USER);
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> mm = new HashMap<String, Object>();
		VoBean vv=JSON.parseObject(vo, VoBean.class);
		List<KpiSettings> mrlist = new ArrayList<KpiSettings>();
		vv.setUserId(user.getUserid());
		mm.put("vo", vv);
		mrlist=roadMapService.getKpi(vv,"0");
		map.put("kpis", mrlist.get(0));
		return map;
	}
	
	/**
	 * 道路集合或采样点查询
	 * @return
	 */
	@RequestMapping(value = "/mapPoint", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> mapPoint(HttpServletRequest request,String vo) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(WebConstants.USER);
		Map<String, Object> map = new HashMap<String, Object>();
		VoBean vv=JSON.parseObject(vo, VoBean.class);
		List<RoadPoints> points=roadMapService.getPoint(vv);
		map.put("list", points);	
		
		return map;
	}
	
	/**
	 * 道路集合或采样点查询
	 * @return
	 */
	@RequestMapping(value = "/mapPot", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> mapPot(HttpServletRequest request,String vo) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(WebConstants.USER);
		Map<String, Object> map = new HashMap<String, Object>();
		VoBean vv=JSON.parseObject(vo, VoBean.class);
		if(vv.getUuid()==null||vv.getUuid().equals("")){
			vv.setUuid(StringUtils.getUuid());
		}
	
		if(vv.getLevel()==null){
			vv.setLevel(0);
			map.put("uuid", vv.getUuid());
		}
		vv.setUserId(user.getUserid());
		vv.setStarttime(vv.getStarttime().replaceAll("\\.", "-"));
		vv.setEndtime(vv.getEndtime().replaceAll("\\.", "-"));
		Map<String, Object> list = new HashMap<String, Object>();
		list =roadMapService.getPot(vv);
		map.put("list", list.get("points"));	
		map.put("warnList", list.get("warnPoints"));	
		return map;
	}
	/**
	 * 点或框选集合详细信息查询
	 * @return
	 */
	@RequestMapping(value = "/pointInfo", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> pointInfo(HttpServletRequest request,String vo) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> mm = new HashMap<String, Object>();
		VoBean vv=JSON.parseObject(vo, VoBean.class);
		mm.put("vo", vv);
		map=roadMapService.getPointAvg(mm);
		return map;
	}
	/**
	 * 
	 * @param request
	 * @param bean 导出
	 * @return
	 */
	@RequestMapping(value = "/excel")
	public @ResponseBody Map<String ,String> excel(HttpServletRequest request ,String vo){
		VoBean vv=JSON.parseObject(vo, VoBean.class);
		Map<String ,String> map = new HashMap<String ,String>();
		String fileName;
		try {
			fileName = roadMapService.exportPoint(request,vv);
		} catch (Exception e) {
			fileName = "-1";
			logger.error("create excel for AreaKpiTop",e);
		}
		map.put("fileName", fileName);
		return map;
	}
}
