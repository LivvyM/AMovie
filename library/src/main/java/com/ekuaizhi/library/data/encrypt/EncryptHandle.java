package com.ekuaizhi.library.data.encrypt;



import android.util.Base64;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EncryptHandle {
    /**
     * 加解密算法 0001版 xxtea
     */
    private static final String ENCRYPT_XXTEA_VERSION = "0001";

    private static final String ENCRYPT_HEAD = "ekzencrypt";
    private static final int LENGTH_ENCRYPT_HEAD = 10;
    private static final int LENGTH_ENCRYPT_VERSION = 4;
    private static final int LENGTH_ENCRYPT_TIMESTAMP = 4;
    private static final int LENGTH_ENCRYPT_BODY_LEN = 6;

    private static final int INDEX_ENCRYPT_VERSION = 10;
    private static final int INDEX_ENCRYPT_TIMESTAMP = 14;
    private static final int INDEX_ENCRYPT_BODY_LEN = 18;
    private static final int INDEX_ENCRYPT_BODY = 24;

    /**
     * 加密
     *
     * @param data       明文数据
     * @param saltSource salt 源数据
     * @param uuid       uuid
     * @return
     */
    public static String encrypt(String data, String saltSource, String uuid) {
        StringBuffer res = new StringBuffer(ENCRYPT_HEAD);
        SimpleDateFormat formatter = new SimpleDateFormat("HHmm");
        String timeNowHHmm = formatter.format(new Date(System.currentTimeMillis()));
        //加解密算法 0001版 xxtea
        String salt = saltSource.substring(3, 10);
        String key = uuid + salt + timeNowHHmm;

        String encryptBody = Base64.encodeToString(Xxtea.encrypt(data.getBytes(Charset.forName("UTF-8")), key.getBytes(Charset.forName("UTF-8"))), Base64.NO_WRAP);
        //return encrypt data
        res.append(ENCRYPT_XXTEA_VERSION).append(timeNowHHmm).append(String.format("%06d", encryptBody.length()))
                .append(encryptBody);
        return res.toString();
    }

    /**
     * 解密
     * @param data 解密的数据
     * @param saltSource   salt 源数据
     * @param uuid uuid
     * @return 明文数据
     */
    public static String decrypt(String data, String saltSource, String uuid) {
        String res = data;
        if (data == null || data.length() < 22 || !ENCRYPT_HEAD.equals(data.substring(0, LENGTH_ENCRYPT_HEAD))) {
//             res = data;
        } else {
            String encryptVersion = data.substring(INDEX_ENCRYPT_VERSION, INDEX_ENCRYPT_VERSION +
                    LENGTH_ENCRYPT_VERSION);
            String encryptTimestampHhmm = data.substring(INDEX_ENCRYPT_TIMESTAMP, INDEX_ENCRYPT_TIMESTAMP +
                    LENGTH_ENCRYPT_TIMESTAMP);
            int encryptBodyLen = Integer.valueOf(data.substring(INDEX_ENCRYPT_BODY_LEN, INDEX_ENCRYPT_BODY_LEN +
                    LENGTH_ENCRYPT_BODY_LEN));
            String encryptBody = data.substring(INDEX_ENCRYPT_BODY, encryptBodyLen + INDEX_ENCRYPT_BODY);

            //加解密算法 0001版 xxtea， key salt = saltSource[3,9]
            if (ENCRYPT_XXTEA_VERSION.equals(encryptVersion)) {
                String salt = saltSource.substring(3, 10);
                String key = uuid + salt + encryptTimestampHhmm;
                byte[] decryptByte = Xxtea.decrypt(Base64.decode(encryptBody,Base64.NO_WRAP), key.getBytes(Charset.forName("UTF-8")));
                if(null != decryptByte){
                    res = new String(decryptByte);
                }
            }
        }
        return res;
    }


    public static void main(String[] args)   {

        String data = "13816161264";
        String encrypt = encrypt(data,"1234567890asdfbqwerqwer123", "1234567890asdfbqwerqwer123");
        System.out.println("encrypt:=" + encrypt);
//
//        String decrypt = decrypt("ekzencrypt000110340124+0wzvHxpVoN0cyD1Gk+tEAqR3ZDWsSBSx/FQuJkucZWHVR9hvnHNDV+QDWhbfdLYAq70ACkggEP2h1tk+4sXT8iOMuq32t1nlpNPgI6JNWGgO+DbUSBy228eh20=",
//                "86574102046918874102041034", "86574102046918874102041034");

//        String decrypt = decrypt(encrypt,
//                "1234567890asdfbqwerqwer123", "1234567890asdfbqwerqwer123");
//        System.out.println("decrypt:=" + decrypt);


//        byte[] encrypt =Xxtea.encrypt(data.getBytes(), "1234567890abc45678901033".getBytes());
//        System.out.println("encrypt:=" + new String(encrypt));
//        System.out.println("decrypt:=" + new String(Xxtea.decrypt(encrypt, "1234567890abc45678901033".getBytes())));
    }

}
