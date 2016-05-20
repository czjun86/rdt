package com.wireless.web.action.epinfoManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.wireless.support.PageBean;
import com.wireless.web.model.EPQueryBean;
import com.wireless.web.model.EpinfoBean;
import com.wireless.web.model.User;
import com.wireless.web.service.epinfoManager.EpinfoService;
import com.wireless.web.service.epinfoManager.ImportEpinfoService;
import com.wireless.web.service.reportManager.AreaService;
import com.wireless.web.utils.StringUtils;
import com.wireless.web.utils.WebConstants;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/epinfo")
public class EpinfoAction {

	private static final Logger logger = LoggerFactory.getLogger(EpinfoAction.class);
	
	@Autowired
	public EpinfoService service;
	@Autowired
	public ImportEpinfoService importService;
	@Autowired
	AreaService areaService;
	
	@RequestMapping(value = "/epinfo")
	public ModelAndView epinfoQuery(EPQueryBean bean ,Integer pageIndex ,Integer pageSize ,
			HttpServletResponse response ,HttpServletRequest request){
		ModelAndView mv = new ModelAndView("/epinfo/epinfo");
		//获取用户信息
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(WebConstants.USER);
		bean.setProvince(bean.getProvince()!=null?bean.getProvince():user.getProvince());
		bean.setDistrict(bean.getDistrict()!=null?bean.getDistrict():user.getDistrict());
		bean.setArea(bean.getArea()!=null?bean.getArea():user.getCounty());
		//查询对应区域信息
		Map<String ,Object> map = areaService.getAreaRoad(
				bean.getProvince(),
				bean.getDistrict(),
				bean.getArea(),
				-1,-1);
		String time = StringUtils.getTime(1,0);
		/*if(bean.getQueryTime()==null){
			bean.setQueryTime(time);
		}*/
		if(bean.getColletion()==null){
			bean.setColletion(-1);
		}
		if(bean.getVision()==null){
			bean.setVision(-1);
		}
		//查询
		PageBean page = service.queryInfo(bean,pageIndex,pageSize);
		
		mv.addObject("bean",bean);
		mv.addObject("page",page);
		mv.addObject("countrys" ,map.get("countrys"));
		mv.addObject("citys" ,map.get("citys"));
		mv.addObject("areas" ,map.get("areas"));
		mv.addObject("models" ,service.queryModel());
		mv.addObject("colletions" ,service.queryCollection());
		mv.addObject("visions" ,service.queryVision());
		return mv;
	}
	
	
	@RequestMapping(value = "/child")
	public ModelAndView child(EPQueryBean bean ,Integer pageIndex ,
			Integer pageSize,Integer id ,HttpServletResponse response ,HttpServletRequest request){
		ModelAndView mv = new ModelAndView("/epinfo/child");
		mv.addObject("bean",bean);
		mv.addObject("pageIndex",pageIndex);
		mv.addObject("pageSize",pageSize);
		
		if(id!=null&&!"".equals(id)){
			Map<String ,Object> rlt = service.queryChild(id);
			mv.addObject("epinfo",rlt);
			String areaid = (String) rlt.get("region_code");
			String area = "00".equals(areaid.substring(4, 6))?"-1":areaid;
			String district = "00".equals(areaid.substring(2, 4))?"-1":areaid.substring(0, 4)+"00";
			String province = areaid.substring(0, 2)+"0000";
			Map<String ,Object> map = areaService.getAreaRoad(province,district,area,-1,-1);
			mv.addObject("provinces" ,map.get("countrys"));
			mv.addObject("districts" ,map.get("citys"));
			mv.addObject("areas" ,map.get("areas"));
			mv.addObject("province" ,province);
			mv.addObject("district" ,district);
			mv.addObject("area" ,area);
		}else{
			//获取用户信息
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute(WebConstants.USER);
			Map<String ,Object> map = areaService.getAreaRoad(user.getProvince(),user.getDistrict(),user.getCounty(),-1,-1);
			mv.addObject("provinces" ,map.get("countrys"));
			mv.addObject("districts" ,map.get("citys"));
			mv.addObject("areas" ,map.get("areas"));
			mv.addObject("province" ,user.getProvince());
			mv.addObject("district" ,user.getDistrict());
			mv.addObject("area" ,user.getCounty());
		}
		mv.addObject("models" ,service.queryModel());
		mv.addObject("colletions" ,service.queryCollection());
		mv.addObject("visions" ,service.queryVision());
		return mv;
	}
	
	/**
	 * 模糊搜索终端信号
	 * @param name
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getEpName")
	public @ResponseBody List<String> getEpName(String name ,HttpServletResponse response ,HttpServletRequest request){
		List<String> list = new ArrayList<String>();
		if(name!=null && !"".equals(name)){
			list = service.getEpName("%"+name+"%");
		}
		return list;
	}
	
	@RequestMapping(value = "/save")
	public @ResponseBody Integer save(EpinfoBean bean ,HttpServletResponse response ,HttpServletRequest request){
		Integer i = -1;
		try {
			service.save(bean);
			i = 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i;
	}
	
	/**
	 * 检查用户名是否重复
	 * @param request
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/checkName")
	public @ResponseBody String checkName(Integer id ,String imei ,HttpServletRequest request) {
		String re = "true";
		Integer nid = service.checkName(imei);
		if(nid==null){
			re="true";
		}else{
			if(id!= null && id == nid){
				re="true";
			}else{
				re="false";
			}
		}
		return re;
	}
	
	@RequestMapping(value = "/updateCollection")
	public @ResponseBody Integer updateCollection(String value ,String ids ,EPQueryBean bean ,HttpServletRequest request) {
		Integer msg = 0;
		try {
			List<Integer> list = service.getUpdateList(ids,bean);
			service.updateLarge(list,value,"collection");
			msg = 1;
		} catch (Exception e) {
			logger.error("Large batch processing",e);
		}
		return msg;
	}
	
	@RequestMapping(value = "/updateVivsion")
	public @ResponseBody Integer updateVivsion(String value ,String ids ,EPQueryBean bean ,HttpServletRequest request) {
		Integer msg = 0;
		try {
			List<Integer> list = service.getUpdateList(ids,bean);
			service.updateLarge(list,value ,"vision");
			msg = 1;
		} catch (Exception e) {
			logger.error("Large batch processing",e);
		}
		return msg;
	}
	
	@RequestMapping(value = "/createExport")
	public @ResponseBody String createExport(EPQueryBean bean ,HttpServletRequest request) {
		String fileName = "-1";
		try {
			fileName = service.createExport(request,bean);
		} catch (Exception e) {
			logger.error("Large batch processing",e);
		}
		return fileName;
	}
	
	@RequestMapping(value = "/importFile")
	public ModelAndView importFile(Integer id){
		ModelAndView mv = new ModelAndView("/epinfo/importFile");
		return mv;
	}
	
	/**
	 * 批量导入
	 * @param request
	 * @param response
	 * @return 0成功  1解析失败 2UUID有重复 ,3表示有错误excel要返回
	 * @throws IOException 
	 */
	/*@RequestMapping(value="saveExcel", method = RequestMethod.POST)
	public  ModelAndView saveExcel(HttpServletRequest request,@RequestParam("file")MultipartFile file) throws IOException{
		//生成文件存放于服务器的路径
		String filePath = request.getSession().getServletContext().getRealPath("/");
		filePath = filePath.replace("\\", "/");
		ModelAndView mv = new ModelAndView("/epinfo/lead");
		try {
			List<Map<String, Object>> list = importService.analytical(file,filePath);
			mv.addObject("error", list);
			if(list.size()>0){
				mv.addObject("msg", -1);
			}else{
				mv.addObject("msg", 0);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mv;
	}*/
	@RequestMapping(value="saveExcel", method = RequestMethod.POST)
	public  void saveExcel(HttpServletRequest request ,HttpServletResponse response
			,@RequestParam("file")MultipartFile file) throws IOException{
		//生成文件存放于服务器的路径
		String filePath = request.getSession().getServletContext().getRealPath("/");
		filePath = filePath.replace("\\", "/");
		Map<String ,Object> map = new HashMap<String ,Object>();
		try {
			List<Map<String, Object>> list = importService.analytical(file,filePath);
			if(list.size()>0){
				map.put("msg", -1);
				map.put("error", getModal(list));
			}else{
				map.put("msg", 0);
				map.put("error", null);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String json = JSONObject.fromObject(map).toString();
		StringUtils.sendJson(json,response);
	}
	
	public String getModal(List<Map<String, Object>> list){
		StringBuilder sb = new StringBuilder();
		/*sb.append("<div class='portlet box blue' style='width:545px;margin-left:37%;margin-top:50px;'>");
			sb.append("<div class='portlet-title'>");
				sb.append("<div class='caption'>错误信息</div>");
			sb.append("</div>");
			sb.append("<div class='portlet-body form'>");
				sb.append("<div class='form-body'>");*/
					for(Map<String, Object> info:list){
						sb.append(info.get("name")+" : "+info.get("info")+"<br/>");
					}
				/*sb.append("</div>");
				sb.append("<div class='form-actions modal-footer'>");
					sb.append("<button type='button' class='btn default' data-dismiss='modal'>关闭</button>");
				sb.append("</div>");
			sb.append("</div>");
		sb.append("</div>");*/
		return sb.toString();
	}
}
