package com.wireless.uitl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class StringUtil {

	/**
	 * 判断对象是否为空
	 */
	public static boolean isEmpty(Object obj) {
		if (null == obj) {
			return true;
		} else {
			return StringUtils.isEmpty(String.valueOf(obj));
		}
	}

	public static boolean isEmpty(HttpServletRequest request, String key) {
		if (null != request.getParameter(key)) {
			return isEmpty(request.getParameter(key));
		} else {
			return true;
		}
	}
	
	public static int isZero(Integer obj){
		if(null == obj){
			return 0;
		}
		return obj;
	}

	/**
	 * 转换名称
	 * 
	 * @param filed
	 * @return
	 */
	public static String convertName(String filed) {
		String attrName = "";
		if (filed.indexOf("_") > 0) {
			String[] nameArray = filed.split("_");
			int j = 0;
			for (String str : nameArray) {
				if (j == 0) {
					attrName = str.toLowerCase();
				} else {
					attrName += StringUtils.capitalize(str.toLowerCase());
				}
				j++;
			}
		} else {
			attrName = filed.toLowerCase();
		}
		if ("".equals(attrName)) {
			return filed;
		} else {
			return attrName;
		}
	}

	/**
	 * 判断是否为数字
	 */
	public static boolean isNumber(Object str) {
		if (!isEmpty(str)) {
			if (str.toString().matches("\\d*")) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 生成MD5编码
	 */
	public static String encodeMd5(String str) {
		StringBuffer buf = new StringBuffer();
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(str.getBytes());
			byte[] bytes = md5.digest();
			for (int i = 0; i < bytes.length; ++i) {
				String s = Integer.toHexString(bytes[i] & 0xFF);
				if (s.length() == 1) {
					buf.append("0");
				}
				buf.append(s);
			}
		} catch (Exception localException) {
		}

		return buf.toString();
	}
	
	/**
	 * 输入流转换为字符串
	 * 
	 * @param is
	 * @return
	 */
	public static String converts(InputStream is) {
		StringBuilder sb = new StringBuilder();
		String readline = "";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			while (br.ready()) {
				readline = br.readLine();
				sb.append(readline);
			}
			br.close();
		} catch (IOException ie) {
			System.out.println("converts failed.");
		}
		return sb.toString();
	}

	/**
	 * 字符是否为空
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(String value) {
		if (null == value || "".equals(value)) {
			return true;
		}
		return false;
	}

	/**
	 * 截取字符
	 * 
	 * @param content
	 * @param begin
	 * @param end
	 * @return
	 */
	public static String subContent(String content, String begin, String end) {
		if (content.contains(begin) && content.contains(end)) {
			int start = content.indexOf(begin) + begin.length();
			content = content.substring(start);
			int endIndex = content.indexOf(end);
			return content.substring(0, endIndex);
		}

		return content;
	}
	
	public static String subContent(String content, String begin) {
		if (content.contains(begin) ) {
			int start = content.indexOf(begin) + begin.length();
			return content.substring(start);
		}

		return content;
	}
	
	/**
	 * 截取字符
	 * 
	 * @param content
	 * @param begin
	 * @param end
	 * @return
	 */
	public static String subContentEmpty(String content, String begin, String end) {
		if (content.contains(begin) && content.contains(end)) {
			int start = content.indexOf(begin) + begin.length();
			content = content.substring(start);
			int endIndex = content.indexOf(end);
			return content.substring(0, endIndex);
		}else{
			return null;
		}
	}
}
