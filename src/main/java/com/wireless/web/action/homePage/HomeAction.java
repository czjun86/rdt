package com.wireless.web.action.homePage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.wireless.web.model.User;
import com.wireless.web.service.homePage.HomeService;
import com.wireless.web.utils.TimeUtils;
import com.wireless.web.utils.WebConstants;

@Controller
@RequestMapping("/home")
public class HomeAction {

	private static final Logger logger = LoggerFactory
			.getLogger(HomeAction.class);
	
	@Autowired
	HomeService homeService;
	
	/**
	 * 跳转首页并查询采样点和topn数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/homePage")
	public ModelAndView netWorkHome(HttpServletRequest request){
		ModelAndView mv = new ModelAndView("/homePage/homePage");
		try {
			//获取用户信息
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute(WebConstants.USER);
			
			Map<String ,Object> map = homeService.getPointInfo(user ,TimeUtils.getYesterday());
			
			mv.addObject("pointList",map.get("pointList"));
			mv.addObject("coverList",map.get("coverList"));
		} catch (Exception e) {
			logger.error("query info for point in HomeAction.netWorkHome",e);
		}
		return mv;
	}
	
	/**
	 * 查询覆盖率图和占比图数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getChartsInfo" ,method=RequestMethod.POST)
	public @ResponseBody Map<String ,Object> getChartsInfo(HttpServletRequest request){
		Map<String ,Object> map = new HashMap<String ,Object>();
		try {
			//获取用户信息
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute(WebConstants.USER);
			//默认时间为今天
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			String time = sdf.format(date);
			List<String> timeList = TimeUtils.getPrevSeven(time);
			
			Map<String ,Object> wirelessCover = homeService.getCoverRate(user,timeList,"wireless");//无线覆盖率查询
			Map<String ,Object> broadbandCover = homeService.getCoverRate(user,timeList,"broadband");//下行链路宽带查询
			Map<String ,Object> networkRate = homeService.getCoverRate(user,timeList,"networkRate");//网络掉线率
			Map<String ,Object> pingRate = homeService.getCoverRate(user,timeList,"pingRate");//业务掉线率
			Map<String ,Object> total = homeService.getQuotaProportion(TimeUtils.getYesterday(),user);//RSRP区间占比,SNR区间占比
			
			map.put("wirelessCover", wirelessCover);
			map.put("broadbandCover", broadbandCover);
			map.put("networkRate", networkRate);
			map.put("pingRate", pingRate);
			map.put("rsrpTotal", total.get("rsrpTotal"));
			map.put("snrTotal", total.get("snrTotal"));
		} catch (Exception e) {
			logger.error("query info for charts of cover,Accounting chart in HomeAction.getChartsInfo",e);
		}
		return map;
	}
	
	/**
	 * 占比图分页
	 * @param request
	 * @param time 查询时间
	 * @param pageType 翻页类型
	 * @param quota 查询指标
	 * @return
	 */
	@RequestMapping(value = "/queryChartsInfo" ,method=RequestMethod.POST)
	public @ResponseBody Map<String ,Object> queryChartsInfo(HttpServletRequest request ,String time 
			,String pageType,String quota){
		Map<String ,Object> map = new HashMap<String ,Object>();
		try {
			//获取用户信息
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute(WebConstants.USER);
			
			List<String> timeList = new ArrayList<String>();
			if("prev".equals(pageType)){
				timeList = TimeUtils.getPrevSeven(time);
			}else if("next".equals(pageType)){
				timeList = TimeUtils.getNextSeven(time);
			}
			
			Map<String ,Object> cover = homeService.getCoverRate(user,timeList,quota);
			map.put("cover", cover);
		} catch (Exception e) {
			logger.error("query info for Flip of charts in HomeAction.queryChartsInfo",e);
		}
		return map;
	}
}
