package com.ekuaizhi.library.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据列表容器
 * <p/>
 * 封装的目的是为了使用时读写数据节点更加方便，存储时兼容 Parcelable 接口更加高效
 * 目前键值对数据值的类型支持  DataItem/DataItemArray/String/Long/Integer/Double/Number/Boolean 8种
 * 此类支持的数据值的类型不可轻易改动，序列化时，分别对应 o/a/s/l/i/d/n/b 8种类型
 * 序列化和反序列化时，有版本概念，目前只做了第一版
 */
public final class DataItemArray implements Parcelable {
    private static final int CURRENT_PARCEL_VERSION = 1; // 当前序列化格式的版本号
    protected final List<Object> mItems = new ArrayList<>();

    public DataItemArray() {
    }

    public DataItemArray(JSONArray jsonArray) {
        appendJSONArray(jsonArray);
    }

    public DataItemArray(Parcel in) {
        fromParcel(in);
    }

    /**
     * 数据列表容器 从一个Parcel容器中反序列化
     */
    public final boolean fromParcel(Parcel in) {
        try {
            int parcelVersion = in.readInt();
            if (parcelVersion == 1) { // 目前只支持第一版
                fromParcelV1(in);
            } else {
                throw new Exception("DataItemArray.fromParcel(in): unkown parcel version: " + parcelVersion);
            }

            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * 删除数组的最后一个对象
     */
    public void removeLastItem() {
        if (mItems.size() < 1) {
            return;
        }
        mItems.remove(size() - 1);
    }

    /**
     * 删除一个对象
     */
    public void remove(Object obj) {
        if (null == obj) {
            return;
        }

        if (mItems.contains(obj)) {
            mItems.remove(obj);
        }
    }

    /**
     * 删除一个指定位置的对象
     */
    public void remove(int position) {
        if (position < 0 || position >= size()) {
            return;
        }

        mItems.remove(position);
    }

    /**
     * 删除所有与指定的 DataItem 对象相同的对象
     */
    public void removeItemsEquals(DataItem item) {
        if (null == item) {
            return;
        }

        for (int i = mItems.size() - 1; i >= 0; i--) {
            Object obj = mItems.get(i);

            if (!(obj instanceof DataItem)) {
                continue;
            }

            if (!obj.equals(item)) {
                continue;
            }

            mItems.remove(obj);
        }
    }

    /**
     * 返回第一个匹配指定键值对的 DataItem 对象，找不到则返回null
     */
    public DataItem firstItemMatches(String key, String value) {
        if (null == key ) {
            return null;
        }

        for (int i = 0; i < mItems.size(); i++) {
            Object obj = mItems.get(i);

            if (!(obj instanceof DataItem)) {
                continue;
            }

            if (!((DataItem) obj).matches(key, value)) {
                continue;
            }

            return (DataItem) obj;
        }

        return null;
    }

    /**
     * 删除所有能匹配指定键值对的 DataItem 对象
     */
    public void removeItemsMatches(String key, String value) {
        if (null == key || null == key) {
            return;
        }

        for (int i = mItems.size() - 1; i >= 0; i--) {
            Object obj = mItems.get(i);

            if (!(obj instanceof DataItem)) {
                continue;
            }

            if (!((DataItem) obj).matches(key, value)) {
                continue;
            }

            mItems.remove(obj);
        }
    }

    /**
     * 清除所有数据
     */
    public DataItemArray clear() {
        mItems.clear();
        return this;
    }

    public <T extends Object>List<T> getItems(){
        List<T> itemList = new ArrayList<>();
        for(Object object : mItems){
            itemList.add((T)object);
        }
        return itemList;
    }

    /**
     * 为当前对象建立一份相同的副本
     */
    public DataItemArray makeCopy() {
        DataItemArray newCopy = new DataItemArray();

        for (Object newO : mItems) {
            if (newO instanceof DataItem) {
                newCopy.mItems.add(((DataItem) newO).makeCopy());
            } else if (newO instanceof DataItemArray) {
                newCopy.mItems.add(((DataItemArray) newO).makeCopy());
            } else {
                newCopy.mItems.add(newO);
            }
        }

        return newCopy;
    }

    /**
     * 判断两个 DataItemArray 对象是否相等
     */
    @Override
    public boolean equals(Object o) {
        if (null == o) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (!(o instanceof DataItemArray)) {
            return false;
        }

        List<Object> oItems = ((DataItemArray) o).mItems;

        if (oItems.size() != mItems.size()) {
            return false;
        }

        for (int i = 0; i < mItems.size(); i++) {
            Object oo = oItems.get(i);
            Object mo = mItems.get(i);

            if (!oo.getClass().equals(mo.getClass())) {
                return false;
            }

            if (!oo.equals(mo)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 获取指定位置的对象，类型可能有多重
     * 遇到不存在的index则返回null
     */
    public Object get(int index) {
        if (index < 0 || index >= mItems.size()) {
            return null;
        }

        return mItems.get(index);
    }

    /**
     * 获取指定位置的类型为 DataItem 的 item
     * 如果 index 对应 item 不存在，则返回 null
     * 如果 index 对应 item 类型不是 DataItem 则返回一个空的 DataItem
     */
    public DataItem getItem(int index) {
        Object item = get(index);

        if (null == item) {
            return null;
        }

        if (!(item instanceof DataItem)) {
            return new DataItem();
        }

        return (DataItem) item;
    }

    /**
     * 把items数组中某个键名对应的元素全部同步到另一个键上
     */
    public synchronized DataItemArray syncItemsDataFromKey(String fromKey, String toKey) {
        if (TextUtils.isEmpty(fromKey) || TextUtils.isEmpty(toKey) || fromKey.equals(toKey)) {
            return this;
        }

        for (int i = 0; i < size(); i++) {
            DataItem item = getItem(i);
            item.syncDataFromKey(fromKey, toKey);
        }

        return this;
    }

    /**
     * 获取指定位置的类型为 String 的 item
     * 如果 index 对应 item 不存在或类型不对，则返回 ""
     */
    public String getString(int index) {
        Object item = get(index);

        if (null == item) {
            return "";
        }

        if (item instanceof String) {
            return (String) item;
        } else if (item != null) {
            return String.valueOf(item);
        }

        return "";
    }

    /**
     * 获取指定位置的类型为 Integer 的 item
     * 如果 index 对应 item 不存在或类型不对，则返回 0
     */
    public int getInt(int index) {
        Object item = get(index);

        if (null == item) {
            return 0;
        }

        if (item instanceof Integer) {
            return (Integer) item;
        } else if (item instanceof Number) {
            return ((Number) item).intValue();
        } else if (item instanceof String) {
            try {
                return (int) Double.parseDouble((String) item);
            } catch (NumberFormatException ignored) {
            }
        }

        return 0;
    }

    /**
     * 获取记录总数
     */
    public final int size() {
        return mItems.size();
    }

    /**
     * 往队列最末尾追加数据
     */
    public boolean add(Object item) {
        return add(item, -1, null);
    }

    /**
     * 往指定位置追加数据，位置超出范围则追加到队列最末尾
     */
    public boolean add(Object item, int position) {
        return add(item, position, null);
    }

    /**
     * 追加不重复主键的数据
     */
    public boolean add(Object item, int position, String uniqueKey) {
        if (item == null) {
            return false;
        }

        if (item instanceof DataItem && !TextUtils.isEmpty(uniqueKey)) {
            String checkValue = ((DataItem) item).getString(uniqueKey);

            for (Object obj : mItems) {
                if (obj instanceof DataItem) {
                    String tempValue = ((DataItem) obj).getString(uniqueKey);
                    if (tempValue.equals(checkValue)) {
                        return true;
                    }
                }
            }
        }

        if (item instanceof DataItemArray || item instanceof DataItem || item instanceof String || item instanceof Long || item instanceof Integer || item instanceof Double || item instanceof Number || item instanceof Boolean) {
            if (position < 0 || position >= mItems.size()) {
                return mItems.add(item);
            } else {
                mItems.add(position, item);
                return true;
            }
        }

        return false;
    }

    /**
     * 从另一个 DataItemArray 追加数据到本对象
     */
    public DataItemArray append(DataItemArray items) {
        if (null != items) {
            for (Object item : items.mItems) {
                mItems.add(item);
            }
        }

        return this;
    }

    /**
     * 调试时使用，输出Map中所有元素
     */
    public void Dump() {
        for (int i = 0; i < mItems.size(); i++) {
            Log.v("Dump", "  items[" + i + "] => " + mItems.get(i).toString());
//            Log.v("Dump", "  items[" + i + "] => " + mItems.get(i).toString());
        }
    }

    /**
     * 转成字符串
     */
    @Override
    public String toString() {
        return toJSONArray().toString();
    }

    /**
     * 数据列表容器 反序列化第一版规则
     */
    private void fromParcelV1(Parcel in) throws Throwable {
        int itemCount = in.readInt();

        for (int i = 0; i < itemCount; i++) {
            byte keyType = in.readByte();
            switch (keyType) {
                case 'o':
                    DataItem item = new DataItem();
                    if (!item.fromParcel(in)) {
                        throw new Exception("DataItemArray.fromParcelV1(in): read DataItem type error!");
                    }
                    mItems.add(item);
                    break;
                case 'a':
                    DataItemArray items = new DataItemArray();
                    if (!items.fromParcel(in)) {
                        throw new Exception("DataItemArray.fromParcelV1(in): read DataItemArray type error!");
                    }
                    mItems.add(items);
                    break;
                case 's':
                    mItems.add(in.readString());
                    break;
                case 'l':
                    mItems.add(in.readLong());
                    break;
                case 'i':
                    mItems.add(in.readInt());
                    break;
                case 'd':
                    mItems.add(in.readDouble());
                    break;
                case 'n':
                    Number number = (Number) in.readSerializable();
                    mItems.add(number);
                    break;
                case 'b':
                    Boolean bool = (Boolean) in.readSerializable();
                    mItems.add(bool);
                    break;
                default:
                    throw new Exception("DataItemArray.fromParcelV1(in): unkown value type: " + keyType);
            }
        }
    }

    /**
     * 追加一个 JSONArray 数组中所有元素到数据列表容器
     */
    public final void appendJSONArray(JSONArray jsonArray) {
        if (null != jsonArray) {
            for (int i = 0; i < jsonArray.length(); i++) {
                Object jo = jsonArray.opt(i);

                if (jo instanceof JSONObject) {
                    mItems.add(new DataItem((JSONObject) jo));
                } else if (jo instanceof JSONArray) {
                    mItems.add(new DataItemArray((JSONArray) jo));
                } else if (jo instanceof String || jo instanceof Long || jo instanceof Integer || jo instanceof Double || jo instanceof Number || jo instanceof Boolean) {
                    mItems.add(jo);
                }
            }
        }
    }

    /**
     * 把一个数据列表容器转换为一个 JSONArray 数组
     */
    public final JSONArray toJSONArray() {
        JSONArray jsonArray = new JSONArray();

        try {
            for (int i = 0; i < mItems.size(); i++) {
                Object jo = mItems.get(i);

                if (jo instanceof DataItem) {
                    jsonArray.put(((DataItem) jo).toJSONObject());
                } else if (jo instanceof DataItemArray) {
                    jsonArray.put(((DataItemArray) jo).toJSONArray());
                } else if (jo instanceof String || jo instanceof Long || jo instanceof Integer || jo instanceof Double || jo instanceof Number || jo instanceof Boolean) {
                    jsonArray.put(jo);
                }
            }
        } catch (Throwable e) {
        }

        return jsonArray;
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
        dest.writeInt(mItems.size());

        // 写入元素
        for (Object jo : mItems) {
            byte keyType;

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
    public static final Creator<DataItemArray> CREATOR = new Creator<DataItemArray>() {
        public DataItemArray createFromParcel(Parcel in) {
            return new DataItemArray(in);
        }

        public DataItemArray[] newArray(int size) {
            return new DataItemArray[size];
        }
    };
}
