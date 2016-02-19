package com.ekuaizhi.library.util;

import java.io.File;
import java.io.FileInputStream;

public class Md5 {
	/**
	 * 获取字节数组的MD5报文(返回字节数组)
	 *
	 * @param source 需要获取报文的字节数组
	 * @return byte[] MD5报文字节数组
	 */
	public static byte[] md5Bytes(byte[] source) {
		byte[] result = null;

		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			md.update(source);
			byte[] tmp = md.digest();
			if(tmp.length == 16){
				result = tmp;
			}
		} catch (Throwable e) {
			result = null;
		}

		return result;
	}

	/**
	 * 获取字符串的MD5报文
	 *
	 * @param sourceStr 需要获取报文的字符串
	 * @return String MD5报文
	 */
	public static String md5(String sourceStr){
		if (null == sourceStr){
			return null;
		}
		return md5(sourceStr.getBytes());
	}

	/**
	 * 获取字节数组的MD5报文
	 *
	 * @param source 需要获取报文的字节数组
	 * @return String MD5报文
	 */
	public static String md5(byte[] source) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest();
			char str[] = new char[16 * 2];
			int k = 0;

			for (int i = 0; i < 16; i++) {
				byte byte0 = tmp[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}

			return new String(str);
		} catch (Throwable e) {
			return null;
		}
	}

	/**
	 * 获取指定路径文件的MD5报文
	 *
	 * @param filePath 需要读取的文件路径
	 * @return String MD5报文
	 */
	public static String md5_file(String filePath) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

		try {
			File tmpFile = new File(filePath);
			if (!tmpFile.isFile()) {
				return null;
			}

			FileInputStream fis = new FileInputStream(tmpFile);
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");

			byte[] buffer = new byte[1024];
			int read_len;
			while ((read_len = fis.read(buffer)) != -1) {
				md.update(buffer, 0, read_len);
			}
			fis.close();

			byte tmp[] = md.digest();
			char str[] = new char[16 * 2];
			int k = 0;

			for (int i = 0; i < 16; i++) {
				byte byte0 = tmp[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}

			return new String(str);
		} catch (Throwable e) {
			return null;
		}
	}
}
