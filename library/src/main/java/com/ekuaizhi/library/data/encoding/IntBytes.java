package com.ekuaizhi.library.data.encoding;

/**
 * int 数组和 byte 数组互转
 */
public class IntBytes {
	/**
	 * 把 byte 数组转换成 int 数组
     *
	 * @return int[]
	 */
	public static int[] toIntArray(byte[] data) {
		return toIntArray(data, false);
	}

	/**
	 * 把 int 数组转换成 byte 数组
     *
	 * @return byte[]
	 */
	public static byte[] toByteArray(int[] data) {
		return toByteArray(data, false);
	}

	/**
	 * 把 byte 数组转换成 int 数组
     *
	 * @param includeLength 第一个字节手否为长度信息
	 * @return int[]
	 */
	public static int[] toIntArray(byte[] data, boolean includeLength) {
		int n = (((data.length & 3) == 0) ? (data.length >>> 2) : ((data.length >>> 2) + 1));
		int[] result;

		if (includeLength) {
			result = new int[n + 1];
			result[n] = data.length;
		} else {
			result = new int[n];
		}
		n = data.length;
		for (int i = 0; i < n; i++) {
			result[i >>> 2] |= (0x000000ff & data[i]) << ((i & 3) << 3);
		}
		return result;
	}

	/**
	 * 把 int 数组转换成 byte 数组
     *
	 * @param includeLength 第一个字节手否为长度信息
	 * @return byte[]
	 */
	public static byte[] toByteArray(int[] data, boolean includeLength) {
		int n = data.length << 2;

		if (includeLength) {
			int m = data[data.length - 1];

			if (m > n) {
				return null;
			} else {
				n = m;
			}
		}

		byte[] result = new byte[n];

		for (int i = 0; i < n; i++) {
			result[i] = (byte) ((data[i >>> 2] >>> ((i & 3) << 3)) & 0xff);
		}

		return result;
	}
}
