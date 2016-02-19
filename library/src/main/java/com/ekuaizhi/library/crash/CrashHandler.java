package com.ekuaizhi.library.crash;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.ekuaizhi.library.data.model.DataResult;
import com.ekuaizhi.library.http.UnifyHttpClient;
import com.ekuaizhi.library.manager.ActivityManager;
import com.ekuaizhi.library.util.AppUtil;
import com.ekuaizhi.library.util.CpuUtil;
import com.ekuaizhi.library.util.DeviceUtil;
import com.ekuaizhi.library.util.Md5;


/**
 * Created by livvym on 15-12-22.
 * 全局异常捕捉
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";

    // CrashHandler 实例
    private static CrashHandler INSTANCE = new CrashHandler();
    // 程序的 Context 对象
    private Context mContext;
    // 系统默认的 UncaughtException 处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    // 用来存储设备信息和异常信息
    private HashMap<String, String> mInfos = new HashMap<>();
    // 用于格式化日期,作为日志文件名的一部分
    private DateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    private String mCrashLogPath = "/sdcard/base/crashLog/";

    private boolean isUpload = false;

    protected String mCrashLogUrl = "http://192.168.1.97:8080/ekz-wkss/app/dt/crash";

    private HashMap<Object,Object> mCrashDeal = new HashMap<>();


    /** 保证只有一个 CrashHandler 实例 */
    private CrashHandler() {
    }

    /** 获取 CrashHandler 实例 ,单例模式 */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /** 设置保存错误log文件的目录 */
    public void setCrashLogPath(String path){
        if(path.equals("") || path.length() < 5){
            Log.e(TAG,"输出CrashLog的地址错误");
            return;
        }
        mCrashLogPath = path;
    }


    /**
     * 初始化
     * @param context 有效的上下文
     */
    public void init(Context context) {
        mContext = context;

        // 获取系统默认的 UncaughtException 处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        // 设置该 CrashHandler 为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);

    }

    /**
     * 当 UncaughtException 发生崩溃时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理，收集错误信息，发送错误报告等操作均在此完成
     *
     * @param ex 错误事件
     * @return true：如果处理了该异常信息；否则返回 false
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        if(mCrashDeal.containsKey(ex)){
            return false;
        }

        mCrashDeal.put(ex,ex.getCause());
        // 使用 Toast 来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "很抱歉，程序出现异常，即将退出。", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();

//         收集设备参数信息
        collectDeviceInfo(ex);
        if(isUpload){
            UploadCrashLogToServer();
        }
//        // 保存日志文件
//        saveCrashInfo2File(ex);
        return true;
    }


    /**
     * 收集设备参数信息
     */
    public void collectDeviceInfo(Throwable ex) {
        mInfos.put("ip",DeviceUtil.getHostIp());
        mInfos.put("deviceType",DeviceUtil.getDeviceType());
        mInfos.put("clientType","1");//1--android 2--ios
        mInfos.put("os",android.os.Build.VERSION.RELEASE);
        mInfos.put("sign", Md5.md5(AppUtil.appSignatures()));
        mInfos.put("path", ActivityManager.getActivityPath());
        mInfos.put("memory",DeviceUtil.getTotalMemory());
        mInfos.put("cpu", "max :" + CpuUtil.getMaxCpuFreq() + "min :" + CpuUtil.getMinCpuFreq() + "current:" + CpuUtil.getCurCpuFreq());
        mInfos.put("resolution",DeviceUtil.getScreenDpWidth() + "*" + DeviceUtil.getScreenDpHeight());
        mInfos.put("description",ex.getCause().toString());
        mInfos.put("detail",ex.toString());
    }

    public void isUploadCrashLogToServer(boolean isUpload){
        this.isUpload = isUpload;
    }

    private void UploadCrashLogToServer(){
        UnifyHttpClient.execute(mCrashLogUrl, mInfos, new UnifyHttpClient.UnifyHttpCallback() {
            @Override
            public void onResponse(DataResult response) {
            }
        });
    }

    public void setUploadCrashLogParams(HashMap<String,String> params){
        if(params.size() > 0){
            mInfos.putAll(params);
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex 错误事件
     * @return  返回文件名称,便于将文件传送到服务器
     */
    private String saveCrashInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : mInfos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();

        String result = writer.toString();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = mFormatter.format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".log";

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File dir = new File(mCrashLogPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(mCrashLogPath + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            Log.e(TAG,"an error occured while writing file...", e);
        }
        return null;
    }
}

