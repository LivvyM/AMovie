package com.ekuaizhi.library.data.encoding;

/**
 * 十六进制字符串和字节流互转
 */
public class HexBytes {
	/**
	 * 字节转换为十六进制字符串
     *
	 * @return String
	 */
	public static String byte2hex(byte[] data) {
		if (null == data) {
			return null;
		}

		StringBuilder sb = new StringBuilder();

        for(byte chr :data){
            sb.append(String.format("%02x", chr));
        }

		return sb.toString();
	}

	/**
	 * 十六进制字符串转换为字节
     *
	 * @return byte[]
	 */
	public static byte[] hexToBytes(String str) {
		if (str == null) {
			return null;
		} else if (str.length() < 2 || str.length() % 2 != 0) {
			return null;
		} else {
			int len = str.length() / 2;
			byte[] buffer = new byte[len];

			for (int i = 0; i < len; i++) {
				try {
					buffer[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
				} catch (NumberFormatException e) {
					return null;
				}
			}

			return buffer;
		}
	}
}
