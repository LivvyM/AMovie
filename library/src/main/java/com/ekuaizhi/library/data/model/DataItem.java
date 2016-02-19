package com.ekuaizhi.library.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;


import com.ekuaizhi.library.data.encoding.UrlEncode;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * 单条数据容器
 * <p/>
 * 封装的目的是为了使用时读写数据节点更加方便，存储时兼容 Parcelable 接口更加高效
 * 目前键值对数据值的类型支持  DataItem/DataItemArray/String/Long/Integer/Double/Number/Boolean 8种
 * 此类支持的数据值的类型不可轻易改动，序列化时，分别对应 o/a/s/l/i/d/n/b 8种类型
 * 序列化和反序列化时，有版本概念，目前只做了第一版
 */
public final class DataItem implements Parcelable {
    private static final int CURRENT_PARCEL_VERSION = 1; // 当前序列化格式的版本号
    private final LinkedHashMap<String, Object> mNameValuePairs = new LinkedHashMap<>();

    public DataItem() {
    }

    public DataItem(JSONObject jsonObject) {
        appendJSONObject(jsonObject);
    }

    public DataItem(Parcel in) {
        fromParcel(in);
    }

    /**
     * 单条数据容器 从一个Parcel容器中反序列化
     */
    public final boolean fromParcel(Parcel in) {
        try {
            int parcelVersion = in.readInt();
            if (parcelVersion == 1) { // 目前只支持第一版
                fromParcelV1(in);
            } else {
                throw new Exception("DataItem.fromParcel(in): unkown parcel version: " + parcelVersion);
            }

            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    public final Set<String> keySet() {
        return mNameValuePairs.keySet();
    }

    /**
     * 判断是否为空
     */
    public boolean isEmpty() {
        return mNameValuePairs.size() < 1;
    }

    /**
     * 构造Uri参数字符串
     */
    public String toUriString() {
        StringBuilder str = new StringBuilder();

        for (String key : mNameValuePairs.keySet()) {
            if (str.length() > 1) {
                str.append("&");
            }

            str.append(UrlEncode.encode(key));
            str.append("=");
            str.append(UrlEncode.encode(getString(key)));
        }

        return str.toString();
    }

    /**
     * 构造Uri参数字节数组
     */
    public byte[] toUriBytes() {
        String uriString = toUriString();
        return uriString.getBytes();
    }

    /**
     * 为当前对象建立一份相同的副本
     */
    public DataItem makeCopy() {
        DataItem newCopy = new DataItem();

        for (String key : mNameValuePairs.keySet()) {
            Object newO = mNameValuePairs.get(key);

            if (newO instanceof DataItem) {
                newCopy.mNameValuePairs.put(key, ((DataItem) newO).makeCopy());
            } else if (newO instanceof DataItemArray) {
                newCopy.mNameValuePairs.put(key, ((DataItemArray) newO).makeCopy());
            } else {
                newCopy.mNameValuePairs.put(key, newO);
            }
        }

        return newCopy;
    }

    /**
     * 把某个键名对应的元素同步到另一个键上
     */
    public synchronized DataItem syncDataFromKey(String fromKey, String toKey) {
        if (TextUtils.isEmpty(fromKey) || TextUtils.isEmpty(toKey) || fromKey.equals(toKey)) {
            return this;
        }

        if (!mNameValuePairs.containsKey(fromKey)) {
            mNameValuePairs.remove(toKey);
        } else {
            mNameValuePairs.put(toKey, mNameValuePairs.get(fromKey));
        }

        return this;
    }

    /**
     * 删除一个元素并返回元素的值，元素不存在则返回null
     */
    public Object remove(String name) {
        if (null == name || !mNameValuePairs.containsKey(name)) {
            return null;
        }

        return mNameValuePairs.remove(name);
    }

    /**
     * 清除所有元素
     */
    public DataItem clear() {
        mNameValuePairs.clear();
        return this;
    }

    /**
     * 获取一条 double 类型数据
     */
    public double getDouble(String name) {
        if (null == name) {
            return 0.0d;
        }

        Object object = mNameValuePairs.get(name);
        if (object instanceof Double) {
            return (Double) object;
        } else if (object instanceof Boolean) {
            return ((Boolean) object) ? 1 : 0;
        } else if (object instanceof Number) {
            return ((Number) object).doubleValue();
        } else if (object instanceof String) {
            try {
                return Double.valueOf((String) object);
            } catch (NumberFormatException ignored) {
            }
        }

        return 0.0d;
    }

    /**
     * 设定一条 double 类型数据
     */
    public boolean setDouble(String name, double value) {
        if (null == name) {
            return false;
        }

        mNameValuePairs.put(name, value);

        return true;
    }

    /**
     * 获取一个对象类型的数据
     */
    public <T extends Object> T getObject(String name) {
        if (null == name) {
            return null;
        }

        Object object = mNameValuePairs.get(name);
        if (null != object) {
            try {
                return (T) object;
            } catch (Throwable e) {
                return null;
            }
        }

        return null;
    }

    /**
     * 设定一个对象类型的数据
     */
    public boolean setObject(String name, Object object) {
        if (null == name || null == object) {
            return false;
        }

        mNameValuePairs.put(name, object);

        return true;
    }

    /**
     * 获取一条长整型数据
     */
    public long getLong(String name) {
        if (null == name) {
            return 0;
        }

        Object object = mNameValuePairs.get(name);
        if (object instanceof Long) {
            return (Long) object;
        } else if (object instanceof Boolean) {
            return ((Boolean) object) ? 1 : 0;
        } else if (object instanceof Number) {
            return ((Number) object).longValue();
        } else if (object instanceof String) {
            try {
                return (long) Double.parseDouble((String) object);
            } catch (NumberFormatException ignored) {
            }
        }
        return 0;
    }

    /**
     * 设定一条长整型数据
     */
    public boolean setLong(String name, long value) {
        if (null == name) {
            return false;
        }

        mNameValuePairs.put(name, value);

        return true;
    }

    /**
     * 获取一条“数据列表容器”数据
     */
    public DataItemArray getDataItemArray(String name) {
        if (null == name) {
            return null;
        }

        Object object = mNameValuePairs.get(name);
        if (object instanceof DataItemArray) {
            return (DataItemArray) object;
        }

        return null;
    }

    /**
     * 设定一条“数据列表容器”数据
     */
    public boolean setDataItemArray(String name, DataItemArray value) {
        if (null == name || null == value) {
            return false;
        }

        mNameValuePairs.put(name, value);

        return true;
    }

    /**
     * 获取一条“单条数据容器”数据
     */
    public DataItem getDataItem(String name) {
        if (null == name) {
            return null;
        }

        Object object = mNameValuePairs.get(name);
        if (object instanceof DataItem) {
            return (DataItem) object;
        }

        return null;
    }

    /**
     * 设定一条“单条数据容器”数据
     */
    public boolean setDataItem(String name, DataItem value) {
        if (null == name || null == value) {
            return false;
        }

        mNameValuePairs.put(name, value);

        return true;
    }

    /**
     * 设定一个字符串键值对
     */
    public boolean setString(String name, String value) {
        if (null == name) {
            return false;
        }

        if (null == value){
            remove(name);
        } else {
            mNameValuePairs.put(name, value);
        }

        return true;
    }

    /**
     * 设定一个整型键值对
     */
    public boolean setInt(String name, int value) {
        if (null == name) {
            return false;
        }

        mNameValuePairs.put(name, value);

        return true;
    }

    /**
     * 设定布尔键值对
     */
    public boolean setBool(String name, boolean value) {
        if (null == name) {
            return false;
        }

        mNameValuePairs.put(name, value);

        return true;
    }

    /**
     * 获取整型值
     */
    public int getInt(String name) {
        if (null == name) {
            return 0;
        }

        if (!mNameValuePairs.containsKey(name)) {
            return 0;
        }

        Object object = mNameValuePairs.get(name);
        if (object instanceof Integer) {
            return (Integer) object;
        } else if (object instanceof Boolean) {
            return ((Boolean) object) ? 1 : 0;
        } else if (object instanceof Number) {
            return ((Number) object).intValue();
        } else if (object instanceof String) {
            try {
                return (int) Double.parseDouble((String) object);
            } catch (NumberFormatException ignored) {
            }
        }

        return 0;
    }

    /**
     * 判断是否包含key
     */
    public boolean containsKey(String key){
        if(key == null){
            return false;
        }
        return mNameValuePairs.containsKey(key);
    }

    /**
     * 获取布尔值
     */
    public boolean getBool(String name) {
        if (null == name) {
            return false;
        }

        if (!mNameValuePairs.containsKey(name)) {
            return false;
        }

        Object object = mNameValuePairs.get(name);
        if (object instanceof Boolean) {
            return (Boolean) object;
        } else if (object instanceof String) {
            String stringValue = (String) object;
            return !(TextUtils.isEmpty(stringValue) || "false".equalsIgnoreCase(stringValue) || "off".equalsIgnoreCase(stringValue) || "0".equalsIgnoreCase(stringValue));
        } else if (object instanceof Integer) {
            return ((Integer) object) != 0;
        } else if (object instanceof Number) {
            return ((Number) object).intValue() != 0;
        }

        return false;
    }

    /**
     * 判断当前节点是否存在一个键名
     */
    public boolean has(String name) {
        return !TextUtils.isEmpty(name) && mNameValuePairs.containsKey(name);
    }

    /**
     * 判断当前节点是否存在某个字符串键值对
     */
    public boolean matches(String name, String value) {
        return !TextUtils.isEmpty(name) && null != value && mNameValuePairs.containsKey(name) && value.equals(getString(name));
    }

    /**
     * 判断当前节点是否存在某个布尔键值对
     */
    public boolean matches(String name, boolean value) {
        if (TextUtils.isEmpty(name)) {
            return false;
        }

        if (!mNameValuePairs.containsKey(name)) {
            return false;
        }

        return value == getBool(name);
    }

    /**
     * 判断两个 DataItem 对象是否相等
     */
    @Override
    public boolean equals(Object o) {
        if (null == o) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (!(o instanceof DataItem)) {
            return false;
        }

        LinkedHashMap<String, Object> oNameValuePairs = ((DataItem) o).mNameValuePairs;

        if (oNameValuePairs.size() != mNameValuePairs.size()) {
            return false;
        }

        for (String key : mNameValuePairs.keySet()) {
            if (!oNameValuePairs.containsKey(key)) {
                return false;
            }

            Object oo = oNameValuePairs.get(key);
            Object om = mNameValuePairs.get(key);

            if (!oo.getClass().equals(om.getClass())) {
                return false;
            }

            if (!oo.equals(om)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 获取字符串值
     */
    public String getString(String name) {
        if (null == name) {
            return "";
        }

        if (!mNameValuePairs.containsKey(name)) {
            return "";
        }

        Object object = mNameValuePairs.get(name);

        if (object instanceof String) {
            return (String) object;
        } else if (object instanceof Boolean) {
            return ((Boolean) object) ? "1" : "0";
        } else if (object != null) {
            return String.valueOf(object);
        }

        return "";
    }

    /**
     * 获取记录总数
     */
    public final int size() {
        return mNameValuePairs.size();
    }

    /**
     * 从另一个 DataItem 追加数据到本对象
     */
    public DataItem append(DataItem item) {
        if (null != item) {
            for (String key : item.mNameValuePairs.keySet()) {
                mNameValuePairs.put(key, item.mNameValuePairs.get(key));
            }
        }

        return this;
    }

    /**
     * 调试时使用，输出Map中所有元素
     */
    public void Dump() {
        for (String key : mNameValuePairs.keySet()) {
            Log.v("Dump", "  [" + key + "] => " + mNameValuePairs.get(key).toString());
        }
    }

    /**
     * 转成字符串
     */
    @Override
    public String toString() {
        return toJSONObject().toString();
    }

    /**
     * 单条数据容器 反序列化第一版规则
     */
    private void fromParcelV1(Parcel in) throws Throwable {
        int itemCount = in.readInt();

        for (int i = 0; i < itemCount; i++) {
            String key = in.readString();
            byte keyType = in.readByte();
            switch (keyType) {
                case 'o':
                    DataItem item = new DataItem();
                    if (!item.fromParcel(in)) {
                        throw new Exception("DataItem.fromParcelV1(in): read DataItem type error!");
                    }
                    mNameValuePairs.put(key, item);
                    break;
                case 'a':
                    DataItemArray items = new DataItemArray();
                    if (!items.fromParcel(in)) {
                        throw new Exception("DataItem.fromParcelV1(in): read DataItemArray type error!");
                    }
                    mNameValuePairs.put(key, items);
                    break;
                case 's':
                    mNameValuePairs.put(key, in.readString());
                    break;
                case 'l':
                    mNameValuePairs.put(key, in.readLong());
                    break;
                case 'i':
                    mNameValuePairs.put(key, in.readInt());
                    break;
                case 'd':
                    mNameValuePairs.put(key, in.readDouble());
                    break;
                case 'n':
                    Number number = (Number) in.readSerializable();
                    mNameValuePairs.put(key, number);
                    break;
                case 'b':
                    Boolean bool = (Boolean) in.readSerializable();
                    mNameValuePairs.put(key, bool);
                    break;
                default:
                    throw new Exception("DataItem.fromParcelV1(in): unkown value type: " + keyType);
            }
        }
    }

    /**
     * 把对象数据转为字节数组
     *
     * @return byte[]
     */
    public byte[] toBytes() {
        byte[] data;

        try {
            Parcel out = Parcel.obtain();
            writeToParcel(out, 0);
            out.setDataPosition(0);

            data = out.marshall();
            out.recycle();
        } catch (Throwable e) {
            data = null;
        }

        return data;
    }

    /**
     * 把一个String类型的JSONObject转换为 DataItem 对象
     *
     * @param jsonString
     * @return DataItem
     */
    public static DataItem fromJSONString(String jsonString) {
        DataItem item = new DataItem();
        item.appendJSONString(jsonString);
        return item;
    }

    /**
     * 把字节数据转换为 DataItem 对象
     *
     * @param bytesData 字节数据
     * @return DataItem 返回对象
     */
    public static DataItem fromBytes(byte[] bytesData) {
        if (null == bytesData) {
            return new DataItem();
        }

        try {
            Parcel in = Parcel.obtain();

            in.unmarshall(bytesData, 0, bytesData.length);
            in.setDataPosition(0);

            return DataItem.CREATOR.createFromParcel(in);
        } catch (Throwable e) {
        }

        return new DataItem();
    }

    /**
     * 往单条数据容器中追加一个 String 类型的 JSONObject 对象所有的键值对
     */
    public final void appendJSONString(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            appendJSONObject(jsonObject);
        } catch (Throwable e) {
        }
    }

    /**
     * 往单条数据容器中追加一个 JSONObject 对象所有的键值对
     */
    public final void appendJSONObject(JSONObject jsonObject) {
        if (null != jsonObject) {
            for (Iterator iter = jsonObject.keys(); iter.hasNext(); ) {
                String key = (String) iter.next();
                Object jo = jsonObject.opt(key);

                if (jo instanceof JSONObject) {
                    mNameValuePairs.put(key, new DataItem((JSONObject) jo));
                } else if (jo instanceof JSONArray) {
                    mNameValuePairs.put(key, new DataItemArray((JSONArray) jo));
                } else if (jo instanceof String || jo instanceof Long || jo instanceof Integer || jo instanceof Double || jo instanceof Number || jo instanceof Boolean) {
                    mNameValuePairs.put(key, jo);
                }
            }
        }
    }


    /**
     * 把一个单条数据容器转换成一个 JSONObject 对象
     */
    public final JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();

        try {
            for (String key : mNameValuePairs.keySet()) {
                Object jo = mNameValuePairs.get(key);

                if (jo instanceof DataItem) {
                    jsonObject.put(key, ((DataItem) jo).toJSONObject());
                } else if (jo instanceof DataItemArray) {
                    jsonObject.put(key, ((DataItemArray) jo).toJSONArray());
                } else if (jo instanceof String || jo instanceof Long || jo instanceof Integer || jo instanceof Double || jo instanceof Number || jo instanceof Boolean) {
                    jsonObject.put(key, jo);
                }
            }
        } catch (Throwable e) {
        }

        return jsonObject;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // 序列化对象的格式版本号
        dest.writeInt(CURRENT_PARCEL_VERSION);

        // 写入序列化对象的键值对个数
        dest.writeInt(mNameValuePairs.size());

        // 写入键值对
        for (String key : mNameValuePairs.keySet()) {
            byte keyType;
            Object jo = mNameValuePairs.get(key);

            // 写入键值对的键名
            dest.writeString(key);

            if (jo instanceof DataItem) { // DataItem
                keyType = 'o';
                dest.writeByte(keyType);
                ((DataItem) jo).writeToParcel(dest, flags);
            } else if (jo instanceof DataItemArray) { // DataItemArray
                keyType = 'a';
                dest.writeByte(keyType);
                ((DataItemArray) jo).writeToParcel(dest, flags);
            } else if (jo instanceof String) { // String
                keyType = 's';
                dest.writeByte(keyType);
                dest.writeString((String) jo);
            } else if (jo instanceof Long) { // Long
                keyType = 'l';
                dest.writeByte(keyType);
                dest.writeLong((Long) jo);
            } else if (jo instanceof Integer) { // Integer
                keyType = 'i';
                dest.writeByte(keyType);
                dest.writeInt((Integer) jo);
            } else if (jo instanceof Double) { // Double
                keyType = 'd';
                dest.writeByte(keyType);
                dest.writeDouble((Double) jo);
            } else if (jo instanceof Number) { // Number
                keyType = 'n';
                dest.writeByte(keyType);
                dest.writeSerializable((Number) jo);
            } else if (jo instanceof Boolean) { // Boolean
                keyType = 'b';
                dest.writeByte(keyType);
                dest.writeSerializable((Boolean) jo);
            } else { // 其他类型做为空字符串占位处理
                keyType = 's';
                dest.writeByte(keyType);
                dest.writeString("");
            }
        }
    }

    /**
     * 对象反序列化构造容器
     */
    public static final Creator<DataItem> CREATOR = new Creator<DataItem>() {
        public DataItem createFromParcel(Parcel in) {
            return new DataItem(in);
        }

        public DataItem[] newArray(int size) {
            return new DataItem[size];
        }
    };
}
