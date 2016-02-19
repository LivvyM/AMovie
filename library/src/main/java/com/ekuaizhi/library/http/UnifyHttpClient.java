package com.ekuaizhi.library.http;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ekuaizhi.library.base.BaseApp;
import com.ekuaizhi.library.data.encrypt.EncryptHandle;
import com.ekuaizhi.library.data.model.DataItem;
import com.ekuaizhi.library.data.model.DataResult;
import com.ekuaizhi.library.http.callback.Callback;
import com.ekuaizhi.library.log.LogLevel;
import com.ekuaizhi.library.manager.ActivityManager;
import com.ekuaizhi.library.manager.NetworkManager;
import com.ekuaizhi.library.manager.SplashManager;
import com.ekuaizhi.library.util.DeviceUtil;
import com.ekuaizhi.library.widget.list.DataListCellCenter;
import com.ekuaizhi.library.widget.network.DataViewAdapter;
import com.ekuaizhi.library.widget.repeater.DataCellOrganizer;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * 用于网络访问唯一对外使用的方式
 *
 * Created by livvy on 16-2-16.
 */
public class UnifyHttpClient {

    private static final int HTTP_STATUS_WITHOUT_PERMISSION = -4;//无权限访问

    public static LogLevel mLogLevel = LogLevel.FULL;  //网络访问的log是否打印
    private static HashMap<Activity, View> mNetworkHashMap = new HashMap<>();
    protected static DataViewAdapter adapter;
    protected static DataCellOrganizer mNetworkOrganizer;
    private static UIHandler mHandler = new UIHandler();

    private static UnifyHttpClient instance;

    private static OkHttpUtils mHttpUtils;

    public UnifyHttpClient() {
        mHttpUtils = new OkHttpUtils();
    }

    public static UnifyHttpClient getInstance() {
        if (null == instance) {
            instance = new UnifyHttpClient();
        }
        return instance;
    }

    public UnifyHttpClient setHttpLogLevel(LogLevel level) {
        mLogLevel = level;
        if (null == instance) {
            throw new NullPointerException("UnifyHttpClient is null,UnifyHttpClient must init");
        }
        return instance;
    }

    public UnifyHttpClient setConnectTimeout(long time) {
        mHttpUtils.getOkHttpClient().setConnectTimeout(time, TimeUnit.MILLISECONDS);
        if (null == instance) {
            throw new NullPointerException("UnifyHttpClient is null,UnifyHttpClient must init");
        }
        return instance;
    }

    /**
     * 进行网络访问的唯一方式。因为目前项目需求统一为post方式访问
     *
     * @param url      访问路径url
     * @param params   访问的参数
     * @param callback 异步方式（不输入的话则为同步方式）
     */
    public static DataResult execute(String url, HashMap<String, String> params, @Nullable UnifyHttpCallback callback) {
        if (null == mHttpUtils) {
            throw new NullPointerException("UnifyHttpClient is null,UnifyHttpClient must init");
        }
        //判断是否需要打印网络访问的参数
        if (mLogLevel == LogLevel.FULL) {
            StringBuilder str = new StringBuilder();
            for (HashMap.Entry<String, String> entry : params.entrySet()) {
                if (str.length() > 1) {
                    str.append("&");
                }
                str.append(entry.getKey());
                str.append("=");
                str.append(entry.getValue());
            }
            Log.i("httpRequest", "========================params==================================");
            Log.i("httpRequest", "访问的地址: " + url);
            Log.i("httpRequest", "访问的参数: " + str.toString());
            Log.i("httpRequest", "========================params==================================");
        }
        //todo 判断网络是否可用
        if (!NetworkManager.isNetworkAvailable()) {

            Message message = new Message();
            message.what = 1;
            DataItem item = new DataItem();
            item.setObject("item", params);
            item.setString("url", url);
            adapter = new DataViewAdapter(BaseApp.getAppContext(), item);
            mNetworkOrganizer = DataListCellCenter.networkOrganizer(adapter);
            mHandler.sendMessage(message);
        } else {
            Message message = new Message();
            message.what = 2;
            mHandler.sendMessage(message);
        }
        //同步方式
        if (null == callback) {
            return execute(url, params, true);
        } else {
            //异步方式
            execute(url, params, callback, true);
            return null;
        }
    }

    private static DataResult execute(String url, HashMap<String, String> params, boolean isCipher) {
        DataResult result;
        try {
            Response response;
            response = OkHttpUtils.post()
                    .url(url)
                    .params(getEncryptParams(params, isCipher)) //判断是否需要加密
                    .build()
                    .execute();
            result = processResponse(response);
        } catch (IOException e) {
            result = new DataResult();
        }
        return result;
    }

    private static void execute(String url, HashMap<String, String> params, UnifyHttpCallback callback, boolean isCipher) {
        OkHttpUtils.post()
                .url(url)
                .params(getEncryptParams(params,isCipher))//判断是否需要加密
                .build()
                .execute(callback);
    }

    private static DataResult processResponse(Response response) throws IOException {
        if (null == response) {
            throw new NullPointerException("Response is null");
        }
        DataResult result;
        switch (response.code()) {
            case 200://正常数据展示
                try {
                    //对返回的数据进行解密
                    String resultStrEncrypt = response.body().string();
                    String resultStr = EncryptHandle.decrypt(resultStrEncrypt, DeviceUtil.getUUID(), DeviceUtil.getUUID());
                    //是否打印返回的数据
                    if (mLogLevel == LogLevel.FULL) {
                        Log.i("===请求路径:===", response.request().urlString());
                        Log.i("===请求返回:===", resultStr);
                        Log.i("===请求code:===", response.code() + "");
                    }
                    JSONObject jsonObject = new JSONObject(resultStr);
                    result = new DataResult(jsonObject);
                    //todo 无权限访问
                    if (result.status == HTTP_STATUS_WITHOUT_PERMISSION) {
//                        //重新连接
                        String mSplashAction = SplashManager.getSplashAction();
                        if (!mSplashAction.equals("")) {
                            result.hasError = true;
                            result.message = "呀，好像被拒绝了哦,点击重新加载试试 >_<";

                            Intent intent = new Intent(mSplashAction);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            BaseApp.getAppContext().startActivity(intent);
                        } else {
                            throw new NullPointerException("SplashManager is null,SplashManager must init");
                        }
                    }
                } catch (Throwable e) {
                    result = new DataResult();
                    result.hasError = true;
                    result.localError = true;
                    result.message = "囧，解析数据失败 >_<";
                }
                break;
            case 302://Cookie失效,需要重新链接--暂未使用。目前使用判断的方式的status为-4的情况下
                result = new DataResult();
                //重新连接
                String mSplashAction = SplashManager.getSplashAction();
                if (!mSplashAction.equals("")) {
                    Intent intent = new Intent(mSplashAction);
                    BaseApp.getAppContext().startActivity(intent);
                }
                result.hasError = true;
                result.localError = true;
                result.message = "呀，好像被拒绝了哦,点击重新加载试试 >_<";
                break;
            case 404:
                result = new DataResult();
                break;
            case 500:
                result = new DataResult();
                break;
            default://非正常数据
                result = new DataResult();
                result.hasError = true;
                result.localError = true;
                result.message = "囧，获取数据出现问题了 >_<";
                break;
        }
        return result;
    }


    /**
     * 对参数进行加密处理
     * 遍历获取业务需要传递的参数，拼接成url，然后统一进行加密处理。最后存放在key:body的map中。
     *
     * @param params 包含通用的参数的params
     */
    private static HashMap<String, String> getEncryptParams(HashMap<String, String> params, boolean isCipher) {
        if (!isCipher) {
            return params;
        }
        //因为遍历过程中进行remove操作会导致ConcurrentModificationException异常。
        //所以将通用的参数保存在新的map中，方便使用
        HashMap<String, String> mEncryptParams = new HashMap<>();
        //用于存放拼接加密的参数url
        StringBuilder str = new StringBuilder();
        //遍历params
        for (HashMap.Entry<String, String> entry : params.entrySet()) {
            //过滤通用的参数,然后将业务参数进行拼接
            if (!entry.getKey().contains("partner") && (!entry.getKey().contains("uuid"))
                    && (!entry.getKey().contains("guid")) && (!entry.getKey().contains("ver"))
                    && (!entry.getKey().contains("productname"))) {

                if (str.length() > 1) {
                    str.append("&");
                }
                str.append(entry.getKey());
                str.append("=");
                str.append(entry.getValue());
            } else {
                mEncryptParams.put(entry.getKey(), entry.getValue());
            }
        }
        //加密后放入body中。
        if (str.length() > 1) {
            mEncryptParams.put("eyBody", EncryptHandle.encrypt(str.toString(), DeviceUtil.getUUID(), DeviceUtil.getUUID()));
        }
        return mEncryptParams;
    }

    static class UIHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    View view;
//                    NoNetworkView view = null;
                    if (mNetworkHashMap.containsKey(ActivityManager.getCurrentActivity())) {
                        view = mNetworkHashMap.get(ActivityManager.getCurrentActivity());
                        view.setVisibility(View.VISIBLE);
                    } else {
                        view = mNetworkOrganizer.getCellView(ActivityManager.getCurrentActivity().getCurrentFocus(), 0);
                        FrameLayout.LayoutParams mLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);
                        mLayoutParams.setMargins(0,110,0,0);
                        ActivityManager.getCurrentActivity().addContentView(view, mLayoutParams);
                        mNetworkHashMap.put(ActivityManager.getCurrentActivity(), view);
                    }
                    break;
                case 2:
                    if (mNetworkHashMap.containsKey(ActivityManager.getCurrentActivity())) {
                        view = mNetworkHashMap.get(ActivityManager.getCurrentActivity());
                        view.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    }

    public static abstract class UnifyHttpCallback extends Callback<DataResult> {
        @Override
        public DataResult parseNetworkResponse(Response response) throws IOException {
            DataResult result;
            result = processResponse(response);
            return result;
        }

        @Override
        public void onError(Request request, Exception e) {
            onErrors(request, e);
        }

        @Override
        public DataResult onErrors(Request request, Exception e) {
            if (mLogLevel == LogLevel.FULL) {
                Log.i("===HttpError===", "url:" + request.url().toString());
                Log.i("===HttpError===", "body:" + request.body().toString());
            }
            DataResult result = new DataResult();
            result.hasError = true;
            result.status = 10001;
            result.message = "呀，网络访问失败了啊 >_<";
            return result;
        }
    }

}
