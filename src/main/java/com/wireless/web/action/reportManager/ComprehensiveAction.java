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
import com.wireless.web.model.QueryBean;
import com.wireless.web.model.User;
import com.wireless.web.service.reportManager.AreaService;
import com.wireless.web.service.reportManager.ComprehensiveService;
import com.wireless.web.utils.StringUtils;
import com.wireless.web.utils.WebConstants;

@Controller
@RequestMapping("/compreQuota")
public class ComprehensiveAction {
	
	private static final Logger logger = LoggerFactory
			.getLogger(ComprehensiveAction.class);
	
	@Autowired
	AreaService areaService;

	@Autowired
	ComprehensiveService service;
	/**
	 * 跳转进入页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/compreQuota")
	public ModelAndView compreQuota(HttpServletRequest request){
		ModelAndView mv = new ModelAndView("/reportManager/compreQuota");
		//获取用户信息
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(WebConstants.USER);
		//查询对应区域信息
		Map<String ,Object> map = areaService.getAreaRoadAuthority(user.getProvince(),user.getDistrict(),user.getCounty(),-1,-1,user);
		//获取自己的运营商权限
		List<Map<String ,Object>> ops = new ArrayList<Map<String ,Object>>();
		Map<String ,Object> op = new HashMap<String ,Object>();
		op.put("id", user.getTeleAuth());
		op.put("text", "全部");
		ops.add(op);
		List<Map<String ,Object>> operators = areaService.getMyOperator(user.getTeleAuth());
		ops.addAll(operators);
		PageBean page = new PageBean();
		page.setPageIndex(WebConstants.PAGE_DEFAULT_PAGEINDEX);
		page.setPageSize(WebConstants.PAGE_DEFAULT_PAGESIZE);
		page.setTotalPage(1);
		
		mv.addObject("countrys" ,map.get("countrys"));
		mv.addObject("citys" ,map.get("citys"));
		mv.addObject("areas" ,map.get("areas"));
		mv.addObject("roads" ,map.get("roads"));
		mv.addObject("operators" ,ops);
		mv .addObject("page" ,page);
		
		QueryBean bean = new QueryBean();
		bean.setProvince(user.getProvince());
		bean.setDistrict(user.getDistrict());
		bean.setArea(user.getCounty());
		bean.setQueryTime(StringUtils.getTime(1,1));
		bean.setTimegransel("1");
		bean.setOperator(user.getOperator());
		bean.setShowTime(1);
		mv.addObject("queryBean",bean);
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
		ModelAndView mv = new ModelAndView("/reportManager/compreQuota");
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(WebConstants.USER);
		//查询对应区域信息
		Map<String ,Object> map = areaService.getAreaRoadAuthority(bean.getProvince(),bean.getDistrict(),bean.getArea(),bean.getRoadType(),bean.getRoadLevel(),user);
		//获取自己的运营商权限
		List<Map<String ,Object>> ops = new ArrayList<Map<String ,Object>>();
		Map<String ,Object> op = new HashMap<String ,Object>();
		op.put("id", user.getTeleAuth());
		op.put("text", "全部");
		ops.add(op);
		List<Map<String ,Object>> operators = areaService.getMyOperator(user.getTeleAuth());
		ops.addAll(operators);
		mv.addObject("countrys" ,map.get("countrys"));
		mv.addObject("citys" ,map.get("citys"));
		mv.addObject("areas" ,map.get("areas"));
		mv.addObject("roads" ,map.get("roads"));
		mv.addObject("operators" ,ops);
		//传入参数调用存储过程
		String uuid = service.getParam(bean,user);
		//查询列名
		Map<String ,Object> titleName = service.queryUserInterval(user);
		titleName = service.changeWorld(titleName);
		//查询
		Map<String ,Object> params = new HashMap<String ,Object>();
		params.put("uuid", uuid);
		params.put("pageIndex", bean.getPageIndex()==null?WebConstants.PAGE_DEFAULT_PAGEINDEX:bean.getPageIndex());
		params.put("pageSize", bean.getPageSize()==null?WebConstants.PAGE_DEFAULT_PAGESIZE:bean.getPageSize());
		params.put("queryBean", bean);
		params.put("user", user);
		PageBean page = new PageBean();
		page = service.queryPage(params);
		
		mv.addObject("titleName",titleName);
		mv.addObject("queryTime",StringUtils.getTime(8,1));
		mv .addObject("page" ,page);
		bean.setUuid(uuid);
		mv.addObject("queryBean",bean);
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
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(WebConstants.USER);
		try {
			map = service.export(request,bean ,user);
		} catch (Exception e) {
			map.put("fileName", "-1");
			logger.error("create excel for compreQuota",e);
		}
		return map;
	}
}
