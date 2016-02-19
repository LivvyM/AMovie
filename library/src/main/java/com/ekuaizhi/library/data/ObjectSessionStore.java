package com.ekuaizhi.library.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 本类提供静态方法，以供不同 Activity 之间传递对象
 */
public class ObjectSessionStore {
    // 存放对象键值对的哈希表
    private static Map<String, Object> session = null;

    /**
     * 放入一个对象
     *
     * @param key   对象的key
     * @param value 需要放入的对象
     */
    public static void setObject(String key, Object value) {
        if (null == key || null == value) {
            return;
        }

        if (null == session) {
            session = new HashMap<>();
        }

        session.put(key, value);
    }

    /**
     * 放入一个对象，以随机的以个不重复的值作为key
     *
     * @param value 需要放入的对象
     * @return key 对象的key
     */
    public static String insertObject(Object value) {
        if (null == value) {
            return "";
        }

        UUID uuid = UUID.randomUUID();
        String key = ObjectSessionStore.class.hashCode() + "$" + uuid.toString();

        setObject(key, value);

        return key;
    }

    /**
     * 获取一个对象
     *
     * @param key 对象的key
     * @return Object 对象
     */
    public static Object getObject(String key) {
        if (null == session || null == key || !session.containsKey(key)) {
            return null;
        }

        return session.get(key);
    }

    /**
     * 获取一个对象后，从哈希表中清空该对象
     *
     * @param key 对象的key
     * @return Object 对象
     */
    public static Object popObject(String key) {
        if (null == session || null == key || !session.containsKey(key)) {
            return null;
        }

        Object value = session.get(key);
        session.remove(key);

        return value;
    }

    /**
     * 移除一个对象
     *
     * @param key 对象的key
     */
    public static void removeObject(String key) {
        if (null == session || null == key || !session.containsKey(key)) {
            return;
        }

        session.remove(key);
    }

    /**
     * 清空所有对象
     */
    public static void clearAllObjects() {
        if (null == session) {
            return;
        }

        session.clear();
    }
}
