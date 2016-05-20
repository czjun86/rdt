package com.wireless.uitl;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * <p>
 * Title:加密解密
 * </p>
 * 
 * @author tian.bo
 * @version 1.0
 */

public class SecretUtil {

	/**
	 * 进行MD5加密
	 * 
	 * @param source
	 *            原始字符串
	 * @return byte[] 
	 *            指定加密方式为md5后的byte[]
	 */

	private byte[] md5(String source) {
		byte[] returnByte = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			returnByte = md5.digest(source.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnByte;
	}

	/**
	 * 得到3-DES的密钥匙 根据接口规范，密钥匙为24个字节，md5加密出来的是16个字节，因此后面补8个字节的0
	 * 
	 * @param source
	 *            原始的source
	 * @return byte[] 
	 *            指定加密方式为md5后的byte[]
	 */

	private byte[] getEnKey(String source) {
		byte[] desKey = null;
		try {
			byte[] desKey1 = md5(source);
			desKey = new byte[24];
			int i = 0;
			while (i < desKey1.length && i < 24) {
				desKey[i] = desKey1[i];
				i++;
			}
			if (i < 24) {
				desKey[i] = 0;
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return desKey;
	}

	/**
	 * 3-DES加密
	 * 
	 * @param src 
	 *            要进行3-DES加密的byte[]
	 * @param enKey 
	 *            3-DES加密密钥
	 * @return byte[] 
	 *            3-DES加密后的byte[]
	 */

	public byte[] Encrypt(byte[] src, byte[] enKey) {
		byte[] encryptedData = null;
		try {
			DESedeKeySpec dks = new DESedeKeySpec(enKey);
			SecretKeyFactory keyFactory = SecretKeyFactory
					.getInstance("DESede");
			SecretKey key = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance("DESede");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			encryptedData = cipher.doFinal(src);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encryptedData;
	}

	/**
	 * 对字符串进行Base64编码
	 * 
	 * @param src 
	 *            要进行编码的字符
	 * @return String 
	 *            进行编码后的字符串
	 */
	@SuppressWarnings("restriction")
	public String getBase64Encode(byte[] src) {
		String requestValue = "";
		try {
			BASE64Encoder base64en = new BASE64Encoder();
			requestValue = base64en.encode(src);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return requestValue;
	}

	/**
	 * 去掉字符串的换行符号 base64编码3-DES的数据时，得到的字符串有换行符号 ，一定要去掉.
	 * 
	 * @param str
	 *            过滤字符串
	 * @return String   
	 *            去掉换行符后的字符串     
	 */
	private String filter(String str) {
		String output = null;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			int asc = str.charAt(i);
			if (asc != 10 && asc != 13)
				sb.append(str.subSequence(i, i + 1));
		}
		output = new String(sb);
		return output;
	}

	/**
	 * 对字符串进行URLDecoder.encode(strEncoding)编码
	 * 
	 * @param src
	 *             要进行编码的字符串
	 * @return String 
	 *             进行编码后的字符串
	 */
	public String getURLEncode(String src) {
		String requestValue = "";
		try {
			requestValue = URLEncoder.encode(src);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return requestValue;
	}

	/**
	 * 3-DES加密
	 * 
	 * @param src
	 *             要进行3-DES加密的字符串
	 * @param spkey
	 *             密钥
	 * @return String 
	 *             3-DES加密后的String
	 */
	public String get3DESEncrypt(String src, String spkey) {
		String requestValue = "";
		try {
			// 得到3-DES的密钥匙
			byte[] enKey = getEnKey(spkey);
			// 要进行3-DES加密的内容在进行/"UTF-16LE/"取字节
			byte[] src2 = src.getBytes("UTF-16LE");
			// 进行3-DES加密后的内容的字节
			byte[] encryptedData = Encrypt(src2, enKey);
			// 进行3-DES加密后的内容进行BASE64编码
			String base64String = getBase64Encode(encryptedData);
			// BASE64编码去除换行符后
			String base64Encrypt = filter(base64String);
			// 对BASE64编码中的HTML控制码进行转义的过程
			requestValue = getURLEncode(base64Encrypt);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return requestValue;
	}

	/**
	 * 对字符串进行URLDecoder.decode(strEncoding)解码
	 * 
	 * @param src
	 *            要进行解码的字符串
	 * @return String 
	 *            进行解码后的字符串
	 */

	public String getURLDecoderdecode(String src) {
		String requestValue = "";
		try {
			requestValue = URLDecoder.decode(src);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return requestValue;
	}

	/**
	 * 进行3-DES解密（密钥匙等同于加密的密钥匙）
	 * 
	 * @param debase64
	 *            要进行3-DES解密byte[]
	 * @param spKey
	 *            密钥
	 * @return String 
	 *            3-DES解密后的String
	 */
	public String deCrypt(byte[] debase64, String spKey) {
		String strDe = null;
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("DESede");
			byte[] key = getEnKey(spKey);
			DESedeKeySpec dks = new DESedeKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory
					.getInstance("DESede");
			SecretKey sKey = keyFactory.generateSecret(dks);
			cipher.init(Cipher.DECRYPT_MODE, sKey);
			byte ciphertext[] = cipher.doFinal(debase64);
			strDe = new String(ciphertext, "UTF-16LE");
		} catch (Exception ex) {
			strDe = "";
			ex.printStackTrace();
		}
		return strDe;
	}

	/**
	 * 3-DES解密
	 * 
	 * @param src
	 *            要进行3-DES解密的字符串
	 * @param spkey
	 *            密钥
	 * @return String 
	 *            3-DES加密后的String
	 */
	@SuppressWarnings("restriction")
	public String get3DESDecrypt(String src, String spkey) {
		String requestValue = "";
		try {
			// URLDecoder.decodeTML控制码进行转义的过程
			String URLValue = getURLDecoderdecode(src);
			// 进行3-DES加密后的内容进行BASE64编码
			BASE64Decoder base64Decode = new BASE64Decoder();
			byte[] base64DValue = base64Decode.decodeBuffer(URLValue);
			// 要进行3-DES加密的内容在进行/"UTF-16LE/"取字节
			requestValue = deCrypt(base64DValue, spkey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return requestValue;
	}

//	public static void main(String[] args) {
//		SecretUtil test = new SecretUtil();
//		String oldString = "cst2008";
//		String SPKEY = "CBA5100396D6EA4225E78BCEA53B7F6C";
//		System.out.println("密钥为:  " + SPKEY);
//		System.out.println("原始密码:  " + oldString);
//		String reValue = test.get3DESEncrypt(oldString, Key.KEY);
//		reValue = reValue.trim().intern();
//		System.out.println("进行3-DES加密后的密码 " + reValue);
//		String reValue2 = test.get3DESDecrypt(reValue, Key.KEY);
//		System.out.println("进行3-DES解密后的密码: " + reValue2);
//	}
}