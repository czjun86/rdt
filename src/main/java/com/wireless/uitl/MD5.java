package com.wireless.uitl;

import org.apache.commons.lang.StringUtils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	private static final String algorithm = "MD5";
	private static final char hexDigits[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * encrypt the source
	 * 
	 * @param source
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	public static String encrypt(String source) throws NoSuchAlgorithmException {
		if (StringUtils.isEmpty(source)) {
			return "";
		}

		MessageDigest md5 = MessageDigest.getInstance(algorithm);
		byte buffer[] = source.getBytes();
		md5.update(buffer);
		byte bDigest[] = md5.digest();
		char str[] = new char[16 * 2];
		int k = 0;
		for (int i = 0; i < 16; i++) {
			byte byte0 = bDigest[i];
			str[k++] = hexDigits[byte0 >>> 4 & 0xf];
			str[k++] = hexDigits[byte0 & 0xf];
		}
		return new String(str);
	}

}
