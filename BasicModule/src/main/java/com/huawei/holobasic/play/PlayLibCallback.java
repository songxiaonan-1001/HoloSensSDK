package com.huawei.holobasic.play;

import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * 服务端(播放库)回调
 */

public class PlayLibCallback {
    private WeakReference<IHandlerLikeNotify> mNotifyWeakReference;

    /**
     * 底层回调入口
     *
     * @param what
     * @param windowIndex 窗口索引
     * @param result      结果
     * @param obj
     * @Callback 底层所有的回调接口
     */
    public synchronized void onJniNotify(int what, int windowIndex, int result,
                                         Object obj) {
        if (null != getCurrentNotifyer()) {
            getCurrentNotifyer().onNotify(what, windowIndex, result, obj);
        } else {
            Log.e("TAG", "getCurrentNotifyer() is null!");
        }
    }

    /**
     * 修改当前显示的 Activity/Fragment 引用
     *
     * @param currentNotifyer
     */
    public void setCurrentNotifyer(IHandlerLikeNotify currentNotifyer) {
        mNotifyWeakReference = new WeakReference<>(currentNotifyer);
    }

    /**
     * 获取currentNotifyer
     *
     * @return
     */
    public IHandlerLikeNotify getCurrentNotifyer() {
        return mNotifyWeakReference.get();
    }

    // -------------------------------------
    // ## 其它↓
    // -------------------------------------

    private PlayLibCallback() {
    }

    public static PlayLibCallback getInstance() {
        return SingletonLoader.INSTANCE;
    }

    /**
     * 当前类(PlayLibCallback.java)的单例
     */
    private static class SingletonLoader {
        private static final PlayLibCallback INSTANCE = new PlayLibCallback();
    }
}