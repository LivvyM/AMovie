package com.ekuaizhi.library.data.encoding;

import android.text.TextUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * URLEncode 编码解码
 */
public class UrlEncode {
	/**
	 * URLEncode 编码函数
	 *
	 * @return String
	 */
	public static String encode(String data){
		if (TextUtils.isEmpty(data)) {
			return "";
		}

		try {
			return URLEncoder.encode(data, "UTF-8");
		} catch (Throwable e) {
			return "";
		}
	}

	/**
	 * URLDecode 编码函数
	 *
	 * @return String
	 */
	public static String decode(String data){
		if (TextUtils.isEmpty(data)) {
			return "";
		}

		try {
			return URLDecoder.decode(data, "UTF-8");
		} catch (Throwable e) {
			return "";
		}
	}
}
