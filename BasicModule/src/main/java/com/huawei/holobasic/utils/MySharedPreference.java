package com.huawei.holobasic.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 *         注:如果不关心返回 SharedPreferences 的返回值，edit 之后，用 apply()，不要用 commit():
 *         http://stackoverflow
 *         .com/questions/5960678/whats-the-difference-between-commit-and
 *         -apply-in-shared-preference
 */

public class MySharedPreference {
    private static Context mContext = null;
    private static SharedPreferences sharedPreferences = null;
    private static Editor editor = null;// 获取编辑器
    private static String name = "MIX";

    /**
     * 初始化
     *
     * @param con
     */
    public static void init(Context con) {
        try {
            if (mContext == null) {
                mContext = con;
                sharedPreferences = mContext.getSharedPreferences(name,
                        Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化
     *
     * @param con
     * @param fileName SharedPreference文件名称
     */
    public static void init(Context con, String fileName) {
        try {
            if (mContext == null) {
                mContext = con;
                sharedPreferences = mContext.getSharedPreferences(fileName,
                        Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Context getContext() {
        return mContext;
    }

    /**
     * 保存String
     *
     * @param key
     * @param value
     * @return
     */
    public static void putString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * 保存boolean
     *
     * @param key
     * @param value
     * @return
     */
    public static void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * 保存int
     *
     * @param key
     * @param value
     * @return
     */
    public static void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * 保存 long
     *
     * @param key
     * @param value
     * @return
     */
    public static void putLong(String key, long value) {
        editor.putLong(key, value);
        editor.apply();
    }

    /**
     * 读取String
     *
     * @param key
     * @return
     */
    public static String getString(String key) {
        if (null == sharedPreferences) {
            sharedPreferences = mContext.getSharedPreferences(name,
                    Context.MODE_PRIVATE);
        }
        return sharedPreferences.getString(key, "");
    }

    /**
     * 读取boolean
     *
     * @param key
     * @return
     */
    public static boolean getBoolean(String key) {
        if (null == sharedPreferences) {
            sharedPreferences = mContext.getSharedPreferences(name,
                    Context.MODE_PRIVATE);
        }
        return sharedPreferences.getBoolean(key, false);
    }

    /**
     * 读取boolean
     *
     * @param key
     * @return
     */
    public static boolean getBoolean(String key, boolean def) {
        if (null == sharedPreferences) {
            sharedPreferences = mContext.getSharedPreferences(name,
                    Context.MODE_PRIVATE);
        }
        return sharedPreferences.getBoolean(key, def);
    }

    /**
     * 读取int
     *
     * @param key
     * @return
     */
    public static int getInt(String key) {
        if (null == sharedPreferences) {
            sharedPreferences = mContext.getSharedPreferences(name,
                    Context.MODE_PRIVATE);
        }
        return sharedPreferences.getInt(key, 0);
    }

    /**
     * 读取int
     *
     * @param key
     * @return
     */
    public static int getInt(String key, int defaultValue) {
        if (null == sharedPreferences) {
            sharedPreferences = mContext.getSharedPreferences(name,
                    Context.MODE_PRIVATE);
        }
        return sharedPreferences.getInt(key, defaultValue);
    }

    /**
     * 读取 long
     *
     * @param key
     * @return
     */
    public static long getLong(String key, long defaultValue) {
        if (null == sharedPreferences) {
            sharedPreferences = mContext.getSharedPreferences(name,
                    Context.MODE_PRIVATE);
        }
        return sharedPreferences.getLong(key, defaultValue);
    }

    /**
     * 清空所有数据
     */

    public static void clearAll() {
        if (null != editor) {
            editor.clear();
            editor.apply();
        }
    }

    /**
     * 从SharedPreferences中移除某项数据
     */
    public static void remove(String key) {
        if (null != editor) {
            editor.remove(key);
            editor.apply();
        }
    }
}
