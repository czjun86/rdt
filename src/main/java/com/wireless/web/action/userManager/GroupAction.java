package com.wireless.web.action.userManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wireless.support.PageBean;
import com.wireless.web.model.DialogList;
import com.wireless.web.service.userManager.GroupService;

@Controller
@RequestMapping("/group")
public class GroupAction {
	@Autowired
	private GroupService groupService;
	
	private static Logger logger = LoggerFactory.getLogger(GroupAction.class);
	
	@RequestMapping(value = "/groupManager")
	public ModelAndView groupManager(HttpServletRequest request, Integer pageIndex, Integer pageSize) {
		ModelAndView mv = new ModelAndView("/groupManager/groupManager");	
		Map<String, Object> params = new HashMap<String, Object>();				
		params.put("pageSize", pageSize);
		params.put("pageIndex", pageIndex);
		PageBean page = groupService.queryPage(params);		
		mv.addObject("page", page);
		return mv;
	}
	
	/**
	 * 进入用户组新增页面
	 * @param request
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/addNewGroup")
	public ModelAndView addGroup(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/groupManager/addGroup");
		return mv;
	}
	
	/**
	 * 判断是否重名
	 */
	@RequestMapping(value = "/checkGroupName")
	public @ResponseBody String checkGroupName(String groupName){
		String re = "true";
		int i = groupService.checkGroupName(groupName);		
		if(i>0){
			re = "false";
		}
		return re;
	}
	
	/**
	 * 保存新增
	 * @param groupName
	 * @return
	 */
	@RequestMapping(value = "/saveGroup")
	public @ResponseBody Integer saveGroup(String groupName){
		Map<String ,Object> param = new HashMap<String ,Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		String time = sdf.format(new Date());
		param.put("rolename", groupName);
		param.put("modifyDate", time);
		param.put("createDate", time);
		param.put("isActive", 1);
		try {
			groupService.saveGroup(param);
			return 1;
		} catch (Exception e) {
			logger.error("save new group",e);
			return 0;
		}
	}
	
	/**
	 * 逻辑删除
	 * @param groupId
	 * @return
	 */
	@RequestMapping(value = "/deleteGroup")
	public @ResponseBody Integer deleteGroup(Integer groupid){
		Map<String ,Object> param = new HashMap<String ,Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		String time = sdf.format(new Date());
		param.put("roleid", groupid);
		param.put("modifyDate", time);
		param.put("isActive", 0);
		try {
			groupService.deleteGroup(param);
			return 1;
		} catch (Exception e) {
			logger.error("save new group",e);
			return 0;
		}
	}
	@RequestMapping(value = "/hasUser")
	public @ResponseBody Integer hasUser(String groupid){
		Integer msg = groupService.hasUser(groupid);
		return msg;
	}
	
	@RequestMapping(value = "/saveGroupIds")
	public @ResponseBody Integer saveGroupIds(Integer roleid ,String groupids){
		Integer msg = -1;
		try {
			List<String> list = new ArrayList<String>();
			if(groupids!=null && groupids.length()>0){
				String[] str = groupids.split(",");
				list = Arrays.asList(str);
			}
			groupService.saveGroupIds(String.valueOf(roleid),list);
			msg = 1;
		} catch (Exception e) {
			logger.error("save menu for role" ,e);
		}
		
		return msg;
	}
	
	@RequestMapping(value = "/grouplist")
	public ModelAndView areaList(String groupid, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/groupManager/grouplist");
		mv.addObject("groupid", groupid);
		return mv;
	}
	
	
	@RequestMapping(value = "/getarea")
	public @ResponseBody
	List<DialogList> getarea(Integer groupid, HttpServletRequest request) {
		List<DialogList> list = new ArrayList<DialogList>();
		//所有菜单
		List<Map<String ,Object>> groups = groupService.queryGroups();
		//已有菜单
		List<Map<String ,Object>> myGroups = groupService.queryMyGroups(groupid);
		DialogList dl1 = null;//一级目录
		DialogList dl2 = null;//二级目录
		List<DialogList> dl2List = null;//二级目录集合
		for(Map<String ,Object> parent:groups){
			if((Integer)parent.get("parentid") == 0){//一级目录
				//当前目录信息
				dl1 = new DialogList();
				dl2List = new ArrayList<DialogList>();
				
				Integer id1 = (Integer) parent.get("rightid");
				dl1.setId(id1);
				dl1.setText((String) parent.get("rightname"));
				dl1.setState("open");
				for(Map<String ,Object> myGroup:myGroups){
					Integer i = Integer.valueOf((String) myGroup.get("rightid"));
					if(id1 == i){
						dl1.setChecked(true);
						break;
					}
				}
				//查找子集
				for(Map<String ,Object> child:groups){
					Integer myParent = (Integer)child.get("parentid");
					if(myParent == id1){
						dl2 = new DialogList();
						Integer id2 = (Integer) child.get("rightid");
						dl2.setId(id2);
						dl2.setText((String) child.get("rightname"));
						dl2.setState("open");
						for(Map<String ,Object> myGroup:myGroups){
							Integer i = Integer.valueOf((String) myGroup.get("rightid"));
							if(id2 == i){
								dl2.setChecked(true);
								break;
							}
						}
						dl2List.add(dl2);
					}
				}
				dl1.setChildren(dl2List);
				list.add(dl1);
			}else{
				continue;
			}
		}
		return list;
	}
}
