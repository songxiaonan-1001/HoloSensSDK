package com.huawei.holosens.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.huawei.holosens.R;

import java.lang.reflect.Field;

/**
 * 一个特别的获取资源信息的类<br/>
 * 加载R文件里面的内容<br/>
 * 例:通过字符串"str_ok",获取strings.xml中相应的内容.
 */
public class ResourcesUnusualUtil {

    // R文件的对象
    private static Resources resources;

    // 初始化文件夹路径和R资源
    public static void register(Context context) {
        resources = context.getResources();
    }

    // 释放
    public static void unregister() {
        if (resources != null) {
            resources = null;
        }
    }

    /**
     * drawable文件夹下文件的id
     */
    public static int getDrawableID(String drawName) {
        return getResId(drawName, R.drawable.class);
    }

    // 获取到Drawable文件
    public static Drawable getDrawable(String drawName) {
        int drawId = getDrawableID(drawName);
        return resources.getDrawable(drawId);
    }

    /**
     * value文件夹
     */
    // 获取到dimen.xml文件里的元素的id
    public static int getDimenID(String dimenName) {
        return getResId(dimenName, R.dimen.class);
    }

    public static float getDimen(String dimenName) {
        return resources.getDimension(getDimenID(dimenName));
    }

    // 获取到color.xml文件里的元素的id
    public static int getColorID(String colorName) {
        return getResId(colorName, R.color.class);
    }

    // 获取到color.xml文件里的元素的id
    public static int getColor(String colorName) {
        return resources.getColor(getColorID(colorName));
    }

    // 获取到String.xml文件里的元素id
    public static int getStringID(String strName) {
        return getResId(strName, R.string.class);
    }

    // 获取到String.xml文件里的元素
    public static String getString(String strName) {
        int strId = getStringID(strName);
        return resources.getString(strId);
    }

    // 获取到String.xml文件里的元素
    public static String getString(String strName, String defaultStr) {
        int strId = getStringID(strName);
        try {
            return resources.getString(strId);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return defaultStr;
    }

    /**
     * 获取资源ID
     *
     * @param variableName 资源名称
     * @param c            类名
     * @return
     */
    private static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

}
