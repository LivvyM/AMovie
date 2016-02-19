package com.ekuaizhi.library.http.builder;


import com.ekuaizhi.library.util.DeviceUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by livvy on 15-12-24.
 * 对参数进行处理的builder
 * get/post 方式访问都需要继承改builder对params进行统一的管理
 */
public abstract class KZHttpBuilder extends OkHttpRequestBuilder{


    /**
     * 对参数进行管理 加密等
     * @param key params 的key值
     * @param val params 的value值
     * @return 返回配置完成的builder
     */
    @Override
    public OkHttpRequestBuilder addParams(String key, String val) {
        return null;
    }


    /**
     * build完成。将url、params等值传递HttpRequest进行网络访问
     * 在这里可以统一对网络访问的url params等数据进行统一的管理
     * @return
     */
    public void setBuild(){
        //todo 可以在这里对传递的参数进行处理 e.j 加密等
//        if(params == null){
//            params = new HashMap<>();
//        }
//
    }

    /**
     * 统一设置网络访问的params 例如网络访问接口需要验证签名等操作需呀统一在网络访问的时候添加体格相同的params。
     * 这样的话，该操作可以在这里进行操作。
     * @param params
     * @return
     */
    @Override
    public OkHttpRequestBuilder params(Map<String, String> params) {
        return null;
    }
}
