package com.wireless.web.action.reportManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wireless.uitl.Constant;
import com.wireless.web.utils.ExportUtils;
import com.wireless.web.utils.StringUtils;
import com.wireless.web.utils.WebConstants;

@Controller
@RequestMapping("/export")
public class ExportAction {

	private static final Logger logger = LoggerFactory.getLogger(ExportAction.class);
	
	@RequestMapping(value = "/export")
	public void export(HttpServletResponse response ,HttpServletRequest request ,String fileName){
		ServletOutputStream out = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		String excelName = fileName+".xlsx";
		String url = request.getSession().getServletContext().getRealPath("/").replace("\\", "/");
		fileName = url.substring(0,url.length()-1)+WebConstants.APP_REPORT_TEMPLATE_EXPORT_PATH+fileName+".xlsx";
		try {
			if(fileName!=null && !"".equals(fileName)){
				File file = new File(fileName);
				byte[] buf = new byte[1024];
				int len = 0;
				response.reset(); // 非常重要
				response.addHeader("Content-Disposition", "attachment;filename=\""
						+ excelName + "\"");
				out = response.getOutputStream();
				bis = new BufferedInputStream(new FileInputStream(file));
				bos = new BufferedOutputStream(out);

				while (-1 != (len = bis.read(buf, 0, buf.length))) {
					bos.write(buf, 0, len);
				}
			}
		} catch (Exception e) {
			logger.error("export speed report", e);
		}finally{
			if (bis != null){
				try {
					bis.close();
				} catch (IOException e) {
				}
			}
			if (bos != null){
				try {
					bos.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	@RequestMapping(value = "/template")
	public void template(HttpServletResponse response ,HttpServletRequest request ,String fileName){
		ServletOutputStream out = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		String excelName = fileName+".xlsx";
		String url = request.getSession().getServletContext().getRealPath("/").replace("\\", "/");
		fileName = url.substring(0,url.length()-1)+WebConstants.APP_TEMPLATE_PATH+fileName+".xlsx";
		try {
			if(fileName!=null && !"".equals(fileName)){
				File file = new File(fileName);
				byte[] buf = new byte[1024];
				int len = 0;
				response.reset(); // 非常重要
				response.addHeader("Content-Disposition", "attachment;filename=\""
						+ excelName + "\"");
				out = response.getOutputStream();
				bis = new BufferedInputStream(new FileInputStream(file));
				bos = new BufferedOutputStream(out);

				while (-1 != (len = bis.read(buf, 0, buf.length))) {
					bos.write(buf, 0, len);
				}
			}
		} catch (Exception e) {
			logger.error("export speed report", e);
		}finally{
			if (bis != null){
				try {
					bis.close();
				} catch (IOException e) {
				}
			}
			if (bos != null){
				try {
					bos.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
