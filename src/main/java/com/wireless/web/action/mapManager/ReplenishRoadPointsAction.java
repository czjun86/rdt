package com.wireless.web.action.mapManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

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
import com.wireless.web.model.ReplenishRoadPoionts;
import com.wireless.web.model.User;
import com.wireless.web.model.VoBean;
import com.wireless.web.service.mapManager.ReplenishRoadPointsService;
import com.wireless.web.service.reportManager.AreaService;
import com.wireless.web.utils.ReplenishRoadPointsUtils;
import com.wireless.web.utils.WebConstants;


@Controller
@RequestMapping("/replenishrps")
public class ReplenishRoadPointsAction {
private static final Logger logger = LoggerFactory.getLogger(ReplenishRoadPointsAction.class);
	
	@Autowired
	private ReplenishRoadPointsService service;
	@Autowired
	private AreaService areaService;
	/**
	 * 页面初始化,根据用户ID查询出对应的一级行政划
	 * @param request
	 * @return
	 */
	
	@RequestMapping(value = "/replenishrps")
	public ModelAndView mapList(HttpServletRequest request,VoBean vo) {
	
		//获取用户信息
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(WebConstants.USER);
		
		ModelAndView mv = new ModelAndView("/mapManager/replenishRoadPointsMap");
		VoBean vv=new VoBean();
		Map<String ,Object> map=new HashMap<String ,Object>();
		vv.setUserId(user.getUserid());
		
		vv.setProvince(user.getProvince());
		vv.setDistrict(user.getDistrict());
		vv.setArea(user.getCounty());
		map = areaService.getAreaRoadAuthority(user.getProvince(),user.getDistrict(),user.getCounty(),-1,-1,user);
		List<Area> roads = (List<Area>) map.get("roads");
	    vv.setRoadId(null);
		
		Area area=service.getAreaNameByUserId(user.getUserid()+"");
		mv.addObject("oneregionname", area.getText());
		mv.addObject("countrys" ,map.get("countrys"));
		mv.addObject("citys" ,map.get("citys"));
		mv.addObject("areas" ,map.get("areas"));
		mv.addObject("roads" ,map.get("roads"));
		mv.addObject("user", user);
		mv.addObject("vo", vv);
		return mv;
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
		Map<String, String> pm = new HashMap<String, String>();
		VoBean vv=JSON.parseObject(vo, VoBean.class);
		
		//解析区域
		String regCode = "";
		String pro = vv.getProvince();//一级区域
		String dis = vv.getDistrict();//二级区域
		String area = vv.getArea();//三级区域
		if(dis.equals("-1")){
			regCode = pro.substring(0, 2);
		}else if(area.equals("-1")){
			regCode = dis.substring(0, 4);
		}else{
			regCode = area;
		}
		
		pm.put("regCode", regCode);
		pm.put("roadId", vv.getRoadId());
		pm.put("roadLevel", vv.getRoadLevel().toString());
		pm.put("roadType", vv.getRoadType().toString());
		List<ReplenishRoadPoionts> points=service.getPoint(pm);
		map.put("list", points);
		return map;
	}

	
	/**
	 * 道路点标记补充入库
	 * @return
	 */
	@RequestMapping(value = "/saveMarks", method = RequestMethod.POST)
	public @ResponseBody int saveMarks(HttpServletRequest request,String list,String roadId,String roadName) {
		
		int re = 1;// 1 表示成功，0  表示失败
		
		String [] ls = ReplenishRoadPointsUtils.getStringArray4Json(list);
		
		re = service.saveMarks(ls,roadId,roadName);
		return re;
	}
	
	/**
	 * 道路点标删除
	 * @return
	 */
	@RequestMapping(value = "/removePoint", method = RequestMethod.POST)
	public @ResponseBody int removePoint(HttpServletRequest request,String roadId,String uuid) {
		int re = 1;// 1 表示成功，0  表示失败
		re = service.removePoint(roadId,uuid);
		return re;
	}
	
	/**
	 * 道路点标记提示信息
	 * @return
	 */
	@RequestMapping(value = "/qryApi", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> qryApi(HttpServletRequest request,String lng,String lat) {
		Map<String, String> re = ReplenishRoadPointsUtils.regeo(lng, lat, "baidu");
		return re;
	}
	
}
