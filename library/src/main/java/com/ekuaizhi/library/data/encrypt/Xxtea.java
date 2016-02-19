package com.ekuaizhi.library.data.encrypt;




import com.ekuaizhi.library.data.encoding.HexBytes;
import com.ekuaizhi.library.data.encoding.IntBytes;

import java.nio.charset.Charset;

/**
 * xxtea 加密解密算法
 */
public class Xxtea {
    /**
     * 给定key，加密数据 (hex)
     */
    public static String hexEncrypt(byte[] data, String key) {
        if (null == data || data.length == 0) {
            return null;
        }

        try {
            int[] intData = IntBytes.toIntArray(data, true);
            int[] intKey = IntBytes.toIntArray(key.getBytes(Charset.forName("UTF-8")), false);
            int[] intResult = encrypt(intData, intKey);
            byte[] byteResult = IntBytes.toByteArray(intResult, false);

            return HexBytes.byte2hex(byteResult);
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * 给定key，解密数据 (hex)
     */
    public static byte[] hexDecrypt(String data, String key) {
        byte[] byteData = HexBytes.hexToBytes(data);

        if (null == byteData || byteData.length == 0) {
            return null;
        }

        try {
            int[] intData = IntBytes.toIntArray(byteData, false);
            int[] intKey = IntBytes.toIntArray(key.getBytes(Charset.forName("UTF-8")), false);
            int[] intResult = decrypt(intData, intKey);

            return IntBytes.toByteArray(intResult, true);
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * 给定key，加密数据 (String)
     */
    public static String encrypt(String data, String key) {
        try {
            return new String(encrypt(data.getBytes(Charset.forName("UTF-8")), key.getBytes(Charset.forName("UTF-8"))));
        } catch (Throwable e) {
            return "";
        }
    }

    /**
     * 给定key，解密数据 (String)
     */
    public static String decrypt(String data, String key) {
        try {
            return new String(decrypt(data.getBytes(Charset.forName("UTF-8")), key.getBytes(Charset.forName("UTF-8"))));
        } catch (Throwable e) {
            return "";
        }
    }

    /**
     * 给定key，加密数据 (byte[])
     */
    public static byte[] encrypt(byte[] data, byte[] key) {
        if (null == data || data.length == 0) {
            return null;
        }

        try {
            int[] intData = IntBytes.toIntArray(data, true);
            int[] intKey = IntBytes.toIntArray(key, false);
            int[] intResult = encrypt(intData, intKey);

            return IntBytes.toByteArray(intResult, false);
        } catch (Throwable e) {
            return null;
        }

    }

    /**
     * 给定key，解密数据 (byte[])
     */
    public static byte[] decrypt(byte[] data, byte[] key) {
        if (null == data || data.length == 0) {
            return null;
        }

        try {
            int[] intData = IntBytes.toIntArray(data, false);
            int[] intKey = IntBytes.toIntArray(key, false);
            int[] intResult = decrypt(intData, intKey);
            return IntBytes.toByteArray(intResult, true);
        } catch (Throwable e) {
            return null;
        }

    }

    /**
     * 给定key，加密数据 (int[])
     */
    private static int[] encrypt(int[] v, int[] k) {
        int n = v.length - 1;

        if (n < 1) {
            return v;
        }
        if (k.length < 4) {
            int[] key = new int[4];

            System.arraycopy(k, 0, key, 0, k.length);
            k = key;
        }
        int z = v[n], y, delta = 0x9E3779B9, sum = 0, e;
        int p, q = 6 + 52 / (n + 1);

        while (q-- > 0) {
            sum = sum + delta;
            e = sum >>> 2 & 3;
            for (p = 0; p < n; p++) {
                y = v[p + 1];
                z = v[p] += (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
            }
            y = v[0];
            z = v[n] += (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
        }

        return v;
    }

    /**
     * 给定key，解密数据 (int[])
     */
    private static int[] decrypt(int[] v, int[] k) {
        int n = v.length - 1;

        if (n < 1) {
            return v;
        }
        if (k.length < 4) {
            int[] key = new int[4];

            System.arraycopy(k, 0, key, 0, k.length);
            k = key;
        }
        int z, y = v[0], delta = 0x9E3779B9, sum, e;
        int p, q = 6 + 52 / (n + 1);

        sum = q * delta;
        while (sum != 0) {
            e = sum >>> 2 & 3;
            for (p = n; p > 0; p--) {
                z = v[p - 1];
                y = v[p] -= (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
            }
            z = v[n];
            y = v[0] -= (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
            sum = sum - delta;
        }
        return v;
    }
}
