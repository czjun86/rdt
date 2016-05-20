/**     
* @文件名称: AppUpdAction.java  
* @类路径: com.born.cms.action  
* @描述: TODO  
* @作者：tc  
* @时间：2015-3-4 下午2:28:37  
* @版本：V1.0     
*/
package com.wireless.web.action.visionManager;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wireless.web.utils.MD5FileUtil;

/**  
 * @类功能说明：  
 * @类修改者：  
 * @修改日期：  
 * @修改说明：  
 * @公司名称：  
 * @作者：tc  
 * @创建时间：2015-3-4 下午2:28:37  
 * @版本：V1.0  
 */
@Controller
@RequestMapping("/cms")
public class AppUpdAction{
//public class AppUpdAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	
	private String avs;
	
	@RequestMapping(value = "cms_updapp")
	public void updapp(HttpServletRequest request,HttpServletResponse response){
		String json ="";
        try {
        	//获取APP版本号
        	SAXReader   reader = new SAXReader();
    		Document document = reader.read(new File(this.getClass().getClassLoader().getResource("").getPath()+"/system.xml"));
    		//获取根节点
    		Element root = document.getRootElement();
    		Element as = root.element("cmsappvsn");
    		String re="";
    		if(as.getText()!=null &&  as.getText().length()>0){
    			re = as.getText();
    		}
        	
    		String appVersion=avs;//最新app版本号
    		String appPath="";//最新版本下载地址
    		String appMd5="";//文件MD5值(终端用于判断下载的文件是否完整)
    		
    		//判断终端app版本是否为最新版本
    		if(!re.equals(avs)){//版本不一致，为终端提供最新版本下载地址
    			//获取服务器部署路径
//        		String basePath =this.getClass().getResource("/").getPath(); //根路径
//        		String basePath2 =basePath.substring(0, basePath.length()-16); //页面根路径
//        		//一些特殊情况需要加工路径
//        		if(basePath2.startsWith("/")){
//        			basePath2=basePath2.substring(1);
//        		}else if(basePath2.startsWith("file:/")){
//        			basePath2=basePath2.substring(6);
//        		}else if(basePath2.startsWith("jar:file:/")){ 
//        			basePath2=basePath2.substring(10);
//        		}
        		//获取请求路径
        		//ActionContext ct= ActionContext.getContext(); 
        		//HttpServletRequest request = (HttpServletRequest)ct.get(ServletActionContext.HTTP_REQUEST);
        		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
        		appPath=basePath+"web/appfile/"+re+".zip";
    			appMd5=re;
    			appVersion=re;
    		}
        	
    		//组合结果
    		json = "{\"avs\":\""+appVersion+"\",\"dh\":\""+appPath+"\",\"dmd\":\""+appMd5+"\"}";
    		//打印到页面
    		//MD5FileUtil.printStringToPage(json,ActionContext.getContext());
    		MD5FileUtil.printStringToPage(json,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getAvs() {
		return avs;
	}

	public void setAvs(String avs) {
		this.avs = avs;
	}
	
}
