package com.livvy.amovie.test;


import com.ekuaizhi.library.data.model.DataItem;
import com.ekuaizhi.library.data.model.DataResult;

import org.json.JSONObject;

/**
 * 虚拟提供 Demo 所需数据
 */
public class DemoData {
    /**
     * 模拟 本地直接设置数据 操作
     * @return DataResult
     */
    public static DataResult loadFromMemory(String itemTitle){
        DataResult result = new DataResult();

        // 模拟设置本地数据
        for(int i=0;i<100;i++){
            DataItem item = new DataItem();
            item.setString("title", itemTitle + ": " + (i+1));
            result.addItem(item);
        }

        return result;
    }

    /**
     * 模拟从网络加载数据
     * @return DataResult
     */
    public static DataResult loadDataFromNetwork(int pageAt, int pageSize){
        // 数据加载器的 fetchData 方法是在新线程中实现的，所以这里就用sleep来模拟阻塞式网络加载数据时的网络请求等待时间
        try {
            Thread.sleep(2000);
        } catch (Throwable e) {
        }

        DataResult result = new DataResult();
        DataItem item;
        int startPos = (pageAt - 1) * pageSize;

        // 模拟服务器返回记录总数
        result.totalcount = 200;

        // 模拟构造网络加载的数据
        for (int i = 0; i < 10; i++) {
            int jobnum = startPos + i + 1;

            item = new DataItem();
            item.setString("job_name", "职位名称" + jobnum);
            item.setString("co_name", "公司名称" + jobnum);
            item.setString("pub_date", "2015-12-13");
            result.addItem(item);
        }

        return result;
    }

    /**
     * 模拟 数据解析出错 操作
     * 这里模拟的是服务器返回数据异常的情况，网络未连接等情况也是一样的，放到公共类一面一并处理好，调用的时候一行代码设置数据源即可
     * @return DataResult
     */
    public static DataResult loadDataError(){
        String errorJsonString = "<html><title>10086免费热点登陆后才能使用....</title></html>";
        DataResult result = new DataResult();

        // 数据加载器的 fetchData 方法是在新线程中实现的，所以这里就用sleep来模拟阻塞式网络加载数据时的网络请求等待时间
        try {
            Thread.sleep(2000);
        } catch (Throwable e) {
        }

        // 下面是模拟网络解析数据的过程
        try {
            // 从字符串解析JSON数据，当然在网络请求过程中 errorJsonString 是从服务器拿到的字符串，这里为了简单省略了从服务器获取字符串过程
            // 特别要注意的是，如果真实环境中需要从服务器拿数据，必须使用 listView.setDataLoader 方法，否则在主线程发起网络请求会闪退
            // listView.setDataLoader方法是会开辟新线程执行的
            JSONObject jsonObject = new JSONObject(errorJsonString);

            // 如果上一句没有抛出异常，那说明解析Json数据成功
            result.appendJSONObject(jsonObject);
        } catch (Throwable e){
            result.hasError = true;
            result.localError = true;
            result.message = "囧，解析数据失败 >_<";
        }

        return result;
    }

    /**
     * 模拟 数据加载为空 操作
     * @return DataResult
     */
    public static DataResult loadDataEmpty(){
        DataResult result = new DataResult();

        // 数据加载器的 fetchData 方法是在新线程中实现的，所以这里就用sleep来模拟阻塞式网络加载数据时的网络请求等待时间
        try {
            Thread.sleep(2000);
        } catch (Throwable e) {

        }

        // 数据为空时，result 结果集是正常的，只是 result.items 里面没东西，这时候全凭服务器返回的 message 节点或status节点来设置界面显示
        result.message = "囧，你没有申请过职位 >_<";

        return result;
    }
}
