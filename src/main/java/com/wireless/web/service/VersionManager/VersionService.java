package com.wireless.web.service.VersionManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.wireless.support.PageBean;
import com.wireless.web.dao.VersionManager.VersionDao;
import com.wireless.web.service.commManager.BaseService;
import com.wireless.web.utils.MD5FileUtil_tanc;
import com.wireless.web.utils.TimeUtils;
import com.wireless.web.utils.WebConstants;

@Controller
@RequestMapping("/versionService")
public class VersionService extends BaseService {

	private static final Logger logger = LoggerFactory
			.getLogger(VersionService.class);
	
	@Autowired
	VersionDao dao;
	
	/**
	 * 分页查询
	 * @param params
	 * @return
	 */
	public PageBean queryPage(Map<String, Object> params){
		try {
			PageBean page = super.queryForList(VersionDao.class, params);
			return page;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * 查询条件组装
	 */
	public PageBean query(String queryTime ,String versionName ,Integer pageIndex ,
			Integer pageSize){
		Map<String ,Object> param = new HashMap<String ,Object>();
		param.put("pageIndex", pageIndex==null?WebConstants.PAGE_DEFAULT_PAGEINDEX:pageIndex);
		param.put("pageSize", pageSize==null?WebConstants.PAGE_DEFAULT_PAGESIZE:pageSize);
		if(queryTime!=null && !"".equals(queryTime)){
			Map<String ,String> se = TimeUtils.getSETime(queryTime);
			param.put("starttime" ,se.get("starttime")+" 00:00:00");
			param.put("endtime" ,se.get("endtime")+" 23:59:59");
		}
		param.put("name", versionName!=null&&!"".equals(versionName)?"%"+versionName+"%":null);
		PageBean page = queryPage(param);
		return page;
	}

	public Integer delete(Integer id) throws Exception {
		Integer msg = -1;
		int i = dao.hasUsed(id);
		if(i>0){
			msg = 2; 
		}else{
			dao.delete(id);
			msg = 1;
		}
		return msg;
	}

	/**
	 * 判断用户是否重复
	 * @param name
	 * @return
	 */
	public Integer checkName(String name) {
		return dao.checkName(name);
	}

	public Map<String, Object> queryVersion(Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = dao.queryVersion(id);
		return map;
	}

	public Integer save(MultipartFile file, String filePath,
			String fileName, String soft_ver, String desc ,HttpServletRequest request) {
		Integer flag = -1;
		try {
			Map<String,Object> map = getUrl(request,filePath);
			String url = (String) map.get("url");
			String path = getPath(soft_ver,fileName ,url);
			String realPath = getPath(soft_ver,fileName ,(String)map.get("path"));
			//文件保存到服务器
			saveFileBody(file,path);
			File fl = new File(path);
	        String md5 = MD5FileUtil_tanc.getFileMD5String(fl); 
			//文件信息入库
			versionFileInfo(soft_ver,fileName,desc,realPath,md5);
			flag = 0;
		} catch (Exception e) {
			flag = 1;
			logger.error("get address of version for save VersionManager",e);
		}
		return flag;
	}
	
	/**
	 * 文件本体保存
	 * @param file
	 * @param soft_ver
	 * @throws IOException
	 */
	private void saveFileBody(MultipartFile file, String path) throws IOException {
		InputStream inp = null;
		inp = file.getInputStream();
		FileOutputStream out = new FileOutputStream(path);
			byte[] b = new byte[1024];
			while((inp.read(b)) != -1){
				out.write(b);
			}
		out.flush(); 
		inp.close();
		out.close();
	}
	
	/**
	 * 保存版本信息
	 * @param soft_ver
	 * @param url
	 * @param desc
	 */
	private void versionFileInfo(String soft_ver ,String fileName , String desc ,String path ,String md5) {
		Map<String ,Object> map = new HashMap<String ,Object>();
		String time = TimeUtils.getTime(0);
		map.put("soft_ver", soft_ver);
		map.put("desc", desc);
		map.put("flie_md5", md5);
		map.put("path", path);
		map.put("is_del", 0);
		map.put("create_time", time);
		dao.saveInfo(map);
		
	}
	
	/**
	 * 拼接全路径及名字
	 */
	private String getPath(String soft_ver,String fileName , String url){
		//如果文件夹不存在则创建    
		File file = new File(url);
		if(!file.exists()&&!file.isDirectory())  {
			file.mkdirs(); 
		}
		String[] str = fileName.split("\\.");
		StringBuilder builder = new StringBuilder();
		builder.append(url);
		builder.append(soft_ver);
		if(str.length>0){
			builder.append(".");
			builder.append(str[str.length-1]);
		}
		return builder.toString();
	}
	/**
	 * 获取版本文件管理地址
	 */
	public Map<String ,Object> getUrl(HttpServletRequest request ,String filePath) throws Exception{
		Properties prop = new Properties();   
		//String path = filePath+WebConstants.VERMANAGER_CONF_ADDRESS;
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(WebConstants.VERMANAGER_CONF_ADDRESS);
	    //InputStream in = Object.class.getResourceAsStream(path);
	    prop.load(in);   
        String url = prop.getProperty("url").trim();
        String ip = prop.getProperty("ip").trim();
        String port = prop.getProperty("port").trim();
        String webName = prop.getProperty("webName").trim();
        Map<String ,Object> map =new HashMap<String ,Object>();
        String realPath = request.getSession().getServletContext().getRealPath("/");
        map.put("url", realPath.replaceAll("\\\\" ,"/")+url);
        map.put("path", ip+":"+port+"/"+webName+"/"+url);
        return map;
	}
	
}
