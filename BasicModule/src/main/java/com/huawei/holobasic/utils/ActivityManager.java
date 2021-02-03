package com.huawei.holobasic.utils;

import android.app.Activity;
import android.os.Build;

import java.lang.ref.WeakReference;
import java.util.Stack;

/**
 * 自定义的Activity管理
 *
 */
public class ActivityManager {
    private static Stack<WeakReference<Activity>> mActivityStack;

    private ActivityManager() {
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }
    }

    /***
     * 栈中Activity的数
     *
     * @return Activity的数
     */
    public int getStackSize() {
        return mActivityStack.size();
    }

    /**
     * 移除某个Activity
     *
     * @param activity 要移除的Activity
     */
    public void pop(WeakReference<Activity> activity) {
        if (activity.get() != null) {
            mActivityStack.remove(activity);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (!activity.get().isFinishing()) {
                    activity.get().finish();
                }
            } else {
                if (!activity.get().isFinishing() && !activity.get()
                        .isDestroyed()) {
                    activity.get().finish();
                }
            }
        }
    }

    /**
     * 通过class获取某个Activity
     */
    public Activity getActivityByClass(Class<?> cls) {
        for (WeakReference<Activity> activity : mActivityStack) {
            if (null != activity.get() && activity.get().getClass().equals(cls)) {
                return activity.get();
            }
        }
        return null;
    }

    /**
     * 获得当前栈顶Activity
     *
     * @return 栈顶的Activity
     */
    public Activity currentActivity() {
        int len = getStackSize();
        if (len == 0) {
            return null;
        }
        return mActivityStack.lastElement().get();
    }

    /**
     * 把Activity推入栈中
     *
     * @param activity 要放入栈的activity
     */
    public void push(WeakReference<Activity> activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }
        mActivityStack.push(activity);
    }

    /**
     * 除了当前Activity,退出栈中其它所有Activity
     */
    public void popAllActivityExceptThis() {
        int len = getStackSize();
        if (len == 0) {
            return;
        }
        Activity current = currentActivity();
        for (int i = len - 1; i >= 0; i--) {
            Activity activity = mActivityStack.get(i).get();
            if (activity == null || (activity == current)) {
                continue;
            }
            pop(mActivityStack.get(i));
        }
    }

    // -----------------------------------------------

    /**
     * 当前类(ActivityManager.java)的单例
     */
    private static class SingletonLoader {
        private static final ActivityManager INSTANCE = new ActivityManager();
    }

    public static ActivityManager getInstance() {
        return SingletonLoader.INSTANCE;
    }
}
