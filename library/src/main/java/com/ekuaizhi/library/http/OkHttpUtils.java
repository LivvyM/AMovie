package com.ekuaizhi.library.http;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.ekuaizhi.library.base.BaseApp;
import com.ekuaizhi.library.http.builder.GetBuilder;
import com.ekuaizhi.library.http.builder.PostFileBuilder;
import com.ekuaizhi.library.http.builder.PostFormBuilder;
import com.ekuaizhi.library.http.builder.PostStringBuilder;
import com.ekuaizhi.library.http.callback.Callback;
import com.ekuaizhi.library.http.cookie.PersistentCookieStore;
import com.ekuaizhi.library.http.https.HttpsUtils;
import com.ekuaizhi.library.http.request.RequestCall;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.CookiePolicy;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Created by zhy on 15/8/17.
 *
 */
public class OkHttpUtils {
    public static final long DEFAULT_MILLISECONDS = 10000;
    private static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;

    public OkHttpUtils() {
        mOkHttpClient = new OkHttpClient();
        //cookie enabled
        if(BaseApp.getAppContext() != null){
            mOkHttpClient.setCookieHandler(new CookieManager(new PersistentCookieStore(BaseApp.getAppContext()), CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        }else{
            mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        }
        mDelivery = new Handler(Looper.getMainLooper());

        if (false) {
            mOkHttpClient.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        }
    }

    public static OkHttpUtils getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils();
                }
            }
        }
        return mInstance;
    }

    public Handler getDelivery() {
        return mDelivery;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }


    public static GetBuilder get() {
        return new GetBuilder();
    }

    public static PostStringBuilder postString() {
        return new PostStringBuilder();
    }

    public static PostFileBuilder postFile() {
        return new PostFileBuilder();
    }

    public static PostFormBuilder post() {
        return new PostFormBuilder();
    }


    public void execute(final RequestCall requestCall, Callback callback) {
        if (callback == null)
            callback = Callback.CALLBACK_DEFAULT;
        final Callback finalCallback = callback;

        requestCall.getCall().enqueue(new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                sendFailResultCallback(request, e, finalCallback);
            }

            @Override
            public void onResponse(final Response response) {
                if (response.code() >= 400 && response.code() <= 599) {
                    try {
                        sendFailResultCallback(requestCall.getRequest(), new RuntimeException(response.body().string()), finalCallback);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                try {
                    Object o = finalCallback.parseNetworkResponse(response);
                    sendSuccessResultCallback(o, finalCallback);
                } catch (IOException e) {
                    sendFailResultCallback(response.request(), e, finalCallback);
                }

            }
        });
    }


    public void sendFailResultCallback(final Request request, final Exception e, final Callback callback) {
        if (callback == null) return;

        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(request, e);
                callback.onAfter();
            }
        });
    }

    public void sendSuccessResultCallback(final Object object, final Callback callback) {
        if (callback == null) return;
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(object);
                callback.onAfter();
            }
        });
    }

    public void cancelTag(Object tag) {
        mOkHttpClient.cancel(tag);
    }


    public void setCertificates(InputStream... certificates) {
        HttpsUtils.setCertificates(getOkHttpClient(), certificates, null, null);
    }


}

