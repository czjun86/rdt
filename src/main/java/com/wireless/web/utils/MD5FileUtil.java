/**     
* @文件名称: MD5FileUtil.java  
* @类路径: com.born.cms.util  
* @描述: TODO  
* @作者：tc  
* @时间：2015-3-4 下午1:53:58  
* @版本：V1.0     
*/
package com.wireless.web.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletResponse;

/**  
 * @类功能说明：  
 * @类修改者：  
 * @修改日期：  
 * @修改说明：  
 * @公司名称：  
 * @作者：tc  
 * @创建时间：2015-3-4 下午1:53:58  
 * @版本：V1.0  
 */
public class MD5FileUtil {
    protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',  
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };  
    protected static MessageDigest messagedigest = null;  
    static {  
        try {  
            messagedigest = MessageDigest.getInstance("MD5");  
        } catch (NoSuchAlgorithmException e) { 
        	System.out.println("MD5FileUtil messagedigest初始化失败");
        	e.printStackTrace();
        }  
    }  
  
    public static String getFileMD5String(File file) throws IOException {  
        FileInputStream in = new FileInputStream(file);  
        FileChannel ch = in.getChannel();  
        MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0,  
                file.length());  
        messagedigest.update(byteBuffer);  
        return bufferToHex(messagedigest.digest());  
    }  
  
    public static String getMD5String(String s) {  
        return getMD5String(s.getBytes());  
    }  
 
    public static String getMD5String(byte[] bytes) {  
        messagedigest.update(bytes);  
        return bufferToHex(messagedigest.digest());  
    }  
 
    private static String bufferToHex(byte bytes[]) {  
        return bufferToHex(bytes, 0, bytes.length);  
    }  
  
    private static String bufferToHex(byte bytes[], int m, int n) {  
        StringBuffer stringbuffer = new StringBuffer(2 * n);  
        int k = m + n;  
        for (int l = m; l < k; l++) {  
            appendHexPair(bytes[l], stringbuffer);  
        }  
        return stringbuffer.toString();  
    }  
  
    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {  
        char c0 = hexDigits[(bt & 0xf0) >> 4];  
        char c1 = hexDigits[bt & 0xf];  
        stringbuffer.append(c0);  
        stringbuffer.append(c1);  
    }  
  
    public static boolean checkPassword(String password, String md5PwdStr) {  
        String s = getMD5String(password);  
        return s.equals(md5PwdStr);  
    }  
    
    
    /**
	 * 打印字符串到页面
	 */
	//public static void printStringToPage(String mag,ActionContext ctx){
    public static void printStringToPage(String mag,HttpServletResponse response){
		//HttpServletResponse response = (HttpServletResponse) ctx.get(ServletActionContext.HTTP_RESPONSE);
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(mag);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(out!=null){
				out.close();
			}
		}
	}
    
  
    public static void main(String[] args) throws IOException {  
        long begin = System.currentTimeMillis();  
  
        File big = new File("F:/Vnlg.zip");  
        String md5 = getFileMD5String(big);  
  
        long end = System.currentTimeMillis();  
        System.out.println("md5:" + md5);  
        System.out.println("time:" + ((end - begin) / 1000) + "s");  
 
    }
}
