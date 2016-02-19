package com.ekuaizhi.library.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.util.Xml;

import com.ekuaizhi.library.base.BaseApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 与硬件设备相关的一些实用方法
 */
public class DeviceUtil {
    static private String mDeviceUUID = null;
    static private String mDeviceUDID = null;

    /**
     * 获取设备操作系统主版本号
     */
    public static String getOSMainVersion() {
        int version = Build.VERSION.SDK_INT;

        switch (version) {
            case 1:
                return "1.0";
            case 2:
                return "1.1";
            case 3:
                return "1.5";
            case 4:
                return "1.6";
            case 5:
                return "2.0";
            case 6:
                return "2.0.1";
            case 7:
                return "2.1";
            case 8:
                return "2.2";
            case 9:
                return "2.3";
        }

        return Build.VERSION.RELEASE;
    }

    /**
     * 获取设备的 imei 号
     */
    public static String getImeiID() {
        String imeiID;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) BaseApp.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
            imeiID = telephonyManager.getDeviceId();
        } catch (Throwable e) {
            imeiID = "";
        }

        return imeiID == null ? "" : imeiID;
    }

    /**
     * 获取sim卡运营商信息
     */
    public static String getSubscriberId() {
        String IMSI;

        try {
            TelephonyManager telephonyManager = (TelephonyManager) BaseApp.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (null == telephonyManager) {
                return "";
            }
            IMSI = telephonyManager.getSubscriberId();
            if (null == IMSI || "".equals(IMSI)) {
                return "";
            }
        } catch (Throwable e) {
            IMSI = "";
        }

        return IMSI;
    }

    /**
     * 获取设备的 Android_ID 号
     */
    public static String getAndroidID() {
        String Android_ID;

        try {
            Android_ID = Secure.getString(BaseApp.getAppContext().getContentResolver(), Secure.ANDROID_ID);
        } catch (Throwable e) {
            Android_ID = "";
        }

        return Android_ID == null ? "" : Android_ID;
    }

    /**
     * 获取设备的UDID
     */
    public static synchronized String getUDID() {
        if (null == mDeviceUDID) {
            mDeviceUDID = getImeiID();

            if (mDeviceUDID.length() < 1) {
                mDeviceUDID = getAndroidID();
            }

            if (mDeviceUDID.length() < 1) {
                mDeviceUDID = getSubscriberId();
            }

			/* 针对模拟器特殊处理: 若 UDID 全为 0，则创建一个UUID */
            mDeviceUDID = mDeviceUDID.trim();
            if (mDeviceUDID.length() > 0) {
                Matcher matcher = Pattern.compile("^0+$").matcher(mDeviceUDID);
                if (matcher.find()) {
                    mDeviceUDID = "";
                }
            }

            if (mDeviceUDID.length() < 1) {
                mDeviceUDID = getLocalUUID();
            }
        }
        return mDeviceUDID;
    }

    /**
     * 本地生成一个UUID值，一旦生成会保存到应用对应的数据目录中
     *
     * @return String
     */
    public static synchronized String getLocalUUID() {
        String uuid;

        SharedPreferences sharedPreferences;
        try {
            sharedPreferences = BaseApp.getAppContext().getSharedPreferences("uuid", Context.MODE_PRIVATE);
        } catch (Throwable e) {
            sharedPreferences = null;
        }

        // 先从本地读取和校验老的UUID值
        if (null != sharedPreferences) {
            try {
                uuid = sharedPreferences.getString("uuid", "");
                if (!Pattern.matches("^AUTO_[0-9a-fA-F]{32}$", uuid)) {
                    uuid = null;
                } else {
                    String chk = sharedPreferences.getString("chk", "");
                    if (!chk.equalsIgnoreCase(Md5.md5(uuid))) {
                        uuid = null;
                    }
                }
            } catch (Throwable e) {
                uuid = null;
            }
        } else {
            uuid = null;
        }

        // 如果读取到到的UUID值为空，则需要生成一个新的UUID值
        if (null == uuid) {
            uuid = "AUTO_" + Md5.md5(UUID.randomUUID().toString() + "-" + UUID.randomUUID().toString() + "-" + UUID.randomUUID().toString() + "-" + UUID.randomUUID().toString());

            if (null != sharedPreferences) {
                String chk = Md5.md5(uuid);
                try {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("uuid", uuid);
                    editor.putString("chk", chk);
                    editor.commit();
                } catch (Throwable e) {
                }
            }
        }

        return uuid;
    }

    /**
     * 获取设备的UUID
     */
    public static synchronized String getUUID() {
        if (null == mDeviceUUID) {
            mDeviceUUID = Md5.md5(getUDID().getBytes());
        }
        return mDeviceUUID;
    }

    /**
     * 获取渠道信息
     */
    public static String getChannel(String fileName) {
        String channel;
        SharedPreferences sharedPreferences;
        try {
            sharedPreferences = BaseApp.getAppContext().getSharedPreferences("channel", Context.MODE_PRIVATE);
        } catch (Throwable e) {
            sharedPreferences = null;
        }
        if (null != sharedPreferences) {
            channel = sharedPreferences.getString("channel", "");
            if (channel.equals("")) {
                channel = getFromAssets(fileName);
                if (null != channel) {
                    try {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("channel", channel);
                        editor.commit();
                    } catch (Throwable e) {
                        //保存内存失败，暂不做任何处理
                    }
                }
            }
        } else {
            channel = null;
        }
        return channel;
    }

    /**
     * 读取assets文件
     *
     * @param fileName 文件名称
     * @return 返回读取的内容
     */
    public static String getFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(BaseApp.getAppContext().getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            return null;
        }
    }

    ////Environment.getExternalStoragePublicDirectory("txt").getPath() 路径
    public static synchronized String getGUID() {
        String guid;
        SharedPreferences sharedPreferences;
        try {
            sharedPreferences = BaseApp.getAppContext().getSharedPreferences("guid", Context.MODE_PRIVATE);
        } catch (Throwable e) {
            sharedPreferences = null;
        }
        if (null != sharedPreferences) {
            try {
                // 先从本地读取GUID
                guid = sharedPreferences.getString("guid", "");
                if (guid.equals("")) {
                    //未获取到数据，则从本地文件获取
                    guid = readFileData("guid.txt");
                    if (null != guid) {
                        try {
                            //获取到后保存本地
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("guid", guid);
                            editor.commit();
                        } catch (Throwable e) {
                            //保存内存失败，暂不做任何处理
                        }
                    }
                }
            } catch (Throwable e) {
                guid = null;
            }
        } else {
            guid = null;
        }
        return guid;
    }

    /**
     * 保存guid
     */
    public static void saveGuidToFile(String guid) {
        SharedPreferences sharedPreferences;
        try {
            sharedPreferences = BaseApp.getAppContext().getSharedPreferences("guid", Context.MODE_PRIVATE);
        } catch (Throwable tx) {
            sharedPreferences = null;
        }
        if (null != sharedPreferences) {
            try {
                //获取到后保存本地
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("guid", guid);
                editor.commit();

                writeFileData("guid.txt", guid);
            } catch (Throwable e) {
                //保存内存和文件失败，暂不做任何处理
            }
        }
    }

    /**
     * 一、私有文件夹下的文件存取（/data/data/包名/files）
     *
     * @param fileName
     * @param message
     */
    public static void writeFileData(String fileName, String message) {
        try {
            FileOutputStream fout = BaseApp.getAppContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            byte[] bytes = message.getBytes();
            fout.write(bytes);
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * //读文件在./data/data/包名/files/下面
     *
     * @param fileName
     * @return
     */
    public static String readFileData(String fileName) {
        String res;
        try {
            FileInputStream fin = BaseApp.getAppContext().openFileInput(fileName);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            res = buffer.toString();//  EncodingUtils.getString(buffer, "UTF-8");
            fin.close();
        } catch (Exception e) {
            res = null;
        }
        return res;
    }


    /**
     * The logical density of the display.
     */
    public static float getScreenScale() {
        return getScreenScale(BaseApp.getAppContext());
    }

    public static float getScreenScale(Context context) {
        try {
            return context.getResources().getDisplayMetrics().density;
        } catch (Throwable e) {
            return 1;
        }
    }

    /**
     * 像素值转字号值
     */
    public static int px2sp(float pxValue) {
        return px2sp(BaseApp.getAppContext(), pxValue);
    }

    public static int px2sp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 字号值转像素值
     */
    public static int sp2px(float spValue) {
        return sp2px(BaseApp.getAppContext(), spValue);
    }

    public static int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue * scale + 0.5f);
    }

    /**
     * 屏幕dip值转换为像素值
     *
     * @param dipValue 屏幕dip值
     * @return int 屏幕像素值
     */
    public static int dip2px(float dipValue) {
        return dip2px(BaseApp.getAppContext(), dipValue);
    }

    public static int dip2px(Context context, float dipValue) {
        return (int) (dipValue * getScreenScale(context) + 0.5f);
    }

    /**
     * 屏幕像素值转换为dip值
     *
     * @param pxValue 屏幕像素值
     * @return int 屏幕dip值
     */
    public static int px2dip(float pxValue) {
        return px2dip(BaseApp.getAppContext(), pxValue);
    }

    public static int px2dip(Context context, float pxValue) {
        return (int) (pxValue / getScreenDensity(context) + 0.5f);
    }

    /**
     * 获取屏幕宽度的像素值
     */
    public static int getScreenPixelsWidth() {
        return getScreenPixelsWidth(BaseApp.getAppContext());
    }

    public static int getScreenPixelsWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度的设备独立像素值 Density-independent pixel (dp)
     *
     * @return int
     */
    public static int getScreenDpHeight() {
        float density = BaseApp.getAppContext().getResources().getDisplayMetrics().density;
        int height = BaseApp.getAppContext().getResources().getDisplayMetrics().heightPixels;
        return (int) Math.ceil((float) height / density);
    }

    /**
     * 获取屏幕宽度的设备独立像素值 Density-independent pixel (dp)
     */
    public static int getScreenDpWidth() {
        return getScreenDpWidth(BaseApp.getAppContext());
    }

    public static int getScreenDpWidth(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        int width = context.getResources().getDisplayMetrics().widthPixels;
        return (int) Math.ceil((float) width / density);
    }

    /**
     * 获取屏幕高度的像素值
     */
    public static int getScreenPixelsHeight() {
        return getScreenPixelsHeight(BaseApp.getAppContext());
    }

    public static int getScreenPixelsHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }


    /**
     * 获取设备dip
     * <p>
     * 设备的独立像素，一个独立像素可能对应不同数量的实际像素值 这个值可能是浮点类型的
     *
     * @return float
     */
    public static float getScreenDensity() {
        return getScreenDensity(BaseApp.getAppContext());
    }

    public static float getScreenDensity(Context context) {
        try {
            return context.getResources().getDisplayMetrics().density;
        } catch (Throwable e) {
            return 1;
        }
    }

    /**
     * 获取设备的Dpi
     * <p>
     * 每英寸在屏幕上的点的数量
     *
     * @return int
     */
    public static int getScreenDpi() {
        try {
            return BaseApp.getAppContext().getResources().getDisplayMetrics().densityDpi;
        } catch (Throwable e) {
            return 160;
        }
    }

    public static String getSimOperatorName() {
        TelephonyManager telManager = (TelephonyManager) BaseApp.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = telManager.getSubscriberId();

        if (imsi != null) {
            if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
                //因为移动网络编号46000下的IMSI已经用完，所以虚拟了一个46002编号，134/159号段使用了此编号 //中国移动
                return "ChinaMobile";
            } else if (imsi.startsWith("46001")) {
                //中国联通
                return "ChinaUnicom";
            } else if (imsi.startsWith("46003")) {
                //中国电信
                return "ChinaTelecom";
            }
        }
        return "unKnown";
    }

    public static String getNetType() {
        String strNetworkType = "";

        NetworkInfo networkInfo = ((ConnectivityManager) BaseApp.getAppContext().
                getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();

                Log.e("cocos2d-x", "Network getSubtypeName : " + _strSubTypeName);
                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = "3G";
                        } else {
                            strNetworkType = _strSubTypeName;
                        }

                        break;
                }
                Log.e("cocos2d-x", "Network getSubtype : " + Integer.valueOf(networkType).toString());
            }
        }
        Log.e("cocos2d-x", "Network Type : " + strNetworkType);
        return strNetworkType;
    }

    // 得到本机ip地址
    public static String getHostIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        //if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet6Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            return "获取失败";
        }
        return "获取失败";
    }


    /**
     * 获取设备类型 区分手机和平板
     */
    public static String getDeviceType() {
        boolean isTablet = (BaseApp.getAppContext().getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
        if (isTablet) {
            return "pad";
        } else {
            return "phone";
        }
    }

    public static String getDeviceMac() {
        WifiManager wifi = (WifiManager) BaseApp.getAppContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    public static long ip2int(String ip) {
        String[] items = ip.split("\\.");
        return Long.valueOf(items[0]) << 24
                | Long.valueOf(items[1]) << 16
                | Long.valueOf(items[2]) << 8 | Long.valueOf(items[3]);
    }

    public static String int2ip(long ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

    /**
     * 获取手机内存
     */
    /**
     * 获取手机内存大小
     *
     * @return
     */
    public static String getTotalMemory() {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }

            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();

        } catch (IOException e) {
            return "";
        }
        return Formatter.formatFileSize(BaseApp.getAppContext(), initial_memory);// Byte转换为KB或者MB，内存大小规格化
    }

}
