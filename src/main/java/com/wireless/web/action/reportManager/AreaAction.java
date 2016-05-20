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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wireless.web.model.Area;
import com.wireless.web.model.Quotas;
import com.wireless.web.model.User;
import com.wireless.web.service.homePage.HomeService;
import com.wireless.web.service.reportManager.AreaService;
import com.wireless.web.shiro.OperatorToken;
import com.wireless.web.utils.StringUtils;
import com.wireless.web.utils.WebConstants;

@Controller
@RequestMapping("/area")
public class AreaAction {

	private static final Logger logger = LoggerFactory
			.getLogger(AreaAction.class);
	
	@Autowired
	AreaService areaService;
	@Autowired
	HomeService homeService;
	
	/**
	 * 打开区域列表
	 * @param areaids
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/arealist")
	public ModelAndView areaList(String areaids, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/area/arealist");
		mv.addObject("areaids", areaids);
		return mv;
	}
	
	
	/**
	 *区域跟区域联动
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/searchNextNoAuthority")
	public @ResponseBody Map<String ,Object> searchNextNoAuthority(HttpServletRequest request ,String id){
		Map<String ,Object> map = new HashMap<String ,Object>();;
		List<Area> citys = new ArrayList<Area>();
		citys = areaService.queryChildren(id);
		map.put("citys", citys);
		return map;
	}
	
	/**
	 *道路区域跟区域联动
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/searchNextNoAuthorityRoad")
	public @ResponseBody Map<String ,Object> searchNextNoAuthorityRoad(HttpServletRequest request ,String id,
			Integer roadType,Integer roadLevel,String roadArea){
		Map<String ,Object> map = new HashMap<String ,Object>();;
		List<Area> citys = new ArrayList<Area>();
		citys = areaService.queryChildren(id);
		List<Area> roads = new ArrayList<Area>();
		roads = areaService.getRoad(roadArea,roadType,roadLevel);
		map.put("citys", citys);
		map.put("roads", roads);
		return map;
	}
	
	
	/**
	 *区域跟区域联动
	 * @param request
	 * @param id
	 * @param type 2查2级，3查3级
	 * @return
	 */
	@RequestMapping(value = "/searchNext")
	public @ResponseBody Map<String ,Object> searchNext(HttpServletRequest request ,String id,Integer type){
		Map<String ,Object> map = new HashMap<String ,Object>();;
		List<Area> citys = new ArrayList<Area>();
		List<Area> areas = new ArrayList<Area>();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(WebConstants.USER);
		if(type == 2){
			if("-1".equals(user.getDistrict())){
				citys = areaService.queryChildren(id);
				if("-1".equals(user.getCounty())){
					areas = areaService.queryChildren(citys.get(0).getId());
				}else{
					areas = areaService.queryThisArea(user.getCounty());
				}
			}else{
				citys = areaService.queryThisArea(user.getDistrict());
				if("-1".equals(user.getCounty())){
					areas = areaService.queryChildren(citys.get(0).getId());
				}else{
					areas = areaService.queryThisArea(user.getCounty());
				}
			}
		}else if(type == 3){
			if("-1".equals(user.getCounty())){
				areas = areaService.queryChildren(id);
			}else{
				areas = areaService.queryThisArea(user.getCounty());
			}
		}
		map.put("citys", citys);
		map.put("areas", areas);
		return map;
	}
	/**
	 *道路区域跟区域联动
	 * @param request
	 * @param id
	 * @param type 2查2级，3查3级 ,4不查区域
	 * @return
	 */
	@RequestMapping(value = "/searchNextRoad")
	public @ResponseBody Map<String ,Object> searchNextRoad(HttpServletRequest request ,String id,Integer type ,
			Integer roadType,Integer roadLevel,String roadArea){
		Map<String ,Object> map = new HashMap<String ,Object>();
		if(type!=4){
			List<Area> citys = new ArrayList<Area>();
			List<Area> areas = new ArrayList<Area>();
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute(WebConstants.USER);
			if(type == 2){
				if("-1".equals(user.getDistrict())){
					citys = areaService.queryChildren(id);
					if("-1".equals(user.getCounty())){
						areas = areaService.queryChildren(citys.get(0).getId());
					}else{
						areas = areaService.queryThisArea(user.getCounty());
					}
				}else{
					citys = areaService.queryThisArea(user.getDistrict());
					if("-1".equals(user.getCounty())){
						areas = areaService.queryChildren(citys.get(0).getId());
					}else{
						areas = areaService.queryThisArea(user.getCounty());
					}
				}
			}else if(type == 3){
				if("-1".equals(user.getCounty())){
					areas = areaService.queryChildren(id);
				}else{
					areas = areaService.queryThisArea(user.getCounty());
				}
			}
			map.put("citys", citys);
			map.put("areas", areas);
		}
		List<Area> roads = new ArrayList<Area>();
		roads = areaService.getRoad(roadArea,roadType,roadLevel);
		map.put("roads", roads);
		return map;
	}

	/**
	 * 查询对应网络指标
	 * @param netType
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryNetType" , method = RequestMethod.POST)
	public @ResponseBody List<Quotas> queryNetType(String netType,HttpServletRequest request){
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(WebConstants.USER);
		List<Quotas> quotas = new ArrayList<Quotas>();
		try {
			quotas = areaService.getQuotas(StringUtils.getOperatorId(user.getOperator().toString()), Integer.valueOf(netType) ,1);
		} catch (NumberFormatException e) {
			logger.error("netType changer type in querynetType", e);
		}
		return quotas;
	}
	
	/**
	 *道路区域跟区域联动
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/searchRoad")
	public @ResponseBody Map<String ,Object> searchRoad(HttpServletRequest request ,
			String pid,String did,String aid,
			Integer roadType,Integer roadLevel,String roadArea){
		Map<String ,Object> map = new HashMap<String ,Object>();;
		List<Area> roads = new ArrayList<Area>();
		String area = "%";
		if(!"-1".equals(aid)){
			area=aid;
		}else{
			if(!"-1".equals(did)){
				area=did.substring(0,4)+"%";
			}else{
				area=pid.substring(0,2)+"%";
			}
		}
		roads = areaService.getRoad("%"+roadArea+"%",roadType,roadLevel,area);
		map.put("roads", roads);
		return map;
	}
}
