package com.wireless.web.action.versionManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.wireless.web.service.VersionManager.VersionService;
import com.wireless.web.utils.StringUtils;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/version")
public class VersionAction {

	private static final Logger logger = LoggerFactory.getLogger(VersionAction.class);
	
	@Autowired
	VersionService versionService;
	
	@RequestMapping(value = "/version")
	public ModelAndView VersionQuery(String queryTime ,String versionName ,
			Integer pageIndex ,Integer pageSize ,HttpServletResponse response ,
			HttpServletRequest request){
		ModelAndView mv = new ModelAndView("/versionManager/version");
		String time = StringUtils.getTime(1,0);
		/*if(queryTime==null){
			queryTime = time;
		}*/
		PageBean page = versionService.query(queryTime,versionName,pageIndex,pageSize);
		mv.addObject("queryTime",queryTime);
		mv.addObject("versionName",versionName);
		mv.addObject("page" , page);
		return mv;
	}
		
	@RequestMapping(value = "/openModal")
	public ModelAndView openModal(Integer id){
		ModelAndView mv = new ModelAndView("/versionManager/child");
		Map<String ,Object> bean = new HashMap<String ,Object>();
		/*if(id!=-1){
			try {
				bean = versionService.queryVersion(id);
			} catch (Exception e) {
				logger.error("query update infomation",e);
			}
		}*/
		mv.addObject("bean",bean);
		return mv;
	}
	
	@RequestMapping(value="/delete")
	public @ResponseBody Integer delete(Integer id){
		Integer msg = -1;
		try {
			msg = versionService.delete(id);
		} catch (Exception e) {
			logger.error("delete version" ,e);
		}
		return msg;
	}
	
	/**
	 * 检查用户名是否重复
	 * @param request
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/checkName")
	public @ResponseBody String checkName(Integer id ,String name ,HttpServletRequest request) {
		String re = "true";
		Integer nid = versionService.checkName(name);
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
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param file
	 * @throws IOException
	 */
	@RequestMapping(value="save", method = RequestMethod.POST)
	public  void save(HttpServletRequest request ,HttpServletResponse response
			,@RequestParam("file")MultipartFile file ,
			String fileName ,String soft_ver ,String desc) throws IOException{
		//生成文件存放于服务器的路径
		String filePath = request.getSession().getServletContext().getRealPath("/");
		filePath = filePath.replace("\\", "/");
		
		Map<String ,Object> map = new HashMap<String ,Object>();
		Integer msg = -1;
		try {
			if(file.getSize()<30*1024*1024)        
            {               
                msg = versionService.save(file,filePath ,fileName ,soft_ver ,desc ,request);
                map.put("msg",msg);
            }else{
            	map.put("msg",2);
            }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String json = JSONObject.fromObject(map).toString();
		StringUtils.sendJson(json,response);
	}
}
