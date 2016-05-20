package com.wireless.web.action.collectionManager;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wireless.support.PageBean;
import com.wireless.web.model.CollectionBean;
import com.wireless.web.model.User;
import com.wireless.web.service.CollectionManager.CollectionService;
import com.wireless.web.utils.StringUtils;

@Controller
@RequestMapping("/collection")
public class CollectionAction {

	private static final Logger logger = LoggerFactory.getLogger(CollectionAction.class);
	
	@Autowired
	CollectionService collectionService;
	
	@RequestMapping(value = "/collection")
	public ModelAndView CollectionQuery(String queryTime ,String collectionName ,Integer pageIndex ,
		Integer pageSize ,HttpServletResponse response ,HttpServletRequest request){
		ModelAndView mv = new ModelAndView("/collection/collection");
		collectionName = (collectionName!=null?collectionName.trim():null);
		/*if(queryTime ==null ||"".equals(queryTime)){
			queryTime = StringUtils.getTime(1,0);
		}*/
		PageBean page = collectionService.query(queryTime,collectionName,pageIndex,pageSize);
		mv.addObject("queryTime" , queryTime);
		mv.addObject("page" , page);
		mv.addObject("collectionName" , collectionName);
		return mv;
	}

	@RequestMapping(value = "/openModal")
	public ModelAndView openModal(Integer id){
		ModelAndView mv = new ModelAndView("/collection/child");
		if(id!=-1){
			try {
				CollectionBean bean = collectionService.queryCollection(id);
				mv.addObject("bean",bean);
			} catch (Exception e) {
				logger.error("query update infomation",e);
			}
		}
		return mv;
	}
	
	@RequestMapping(value = "/save")
	public @ResponseBody Map<String ,Object> save(CollectionBean bean){
		Map<String ,Object> msg = new HashMap<String ,Object>();
		Integer flag = -1;
		try {
			flag = collectionService.save(bean);
		} catch (Exception e) {
			logger.error("in save collection",e);
		}
		msg.put("msg", flag);
		return msg;
	}
	
	/**
	 * 删除
	 * @param bean
	 * @return
	 */
	@RequestMapping(value = "/delete")
	public @ResponseBody Integer delete(Integer id){
		Integer flag = collectionService.delete(id);
		return flag;
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
		Integer nid = collectionService.checkName(name);
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
}
