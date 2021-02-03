package com.huawei.holobasic.play;

/**
 * 类似Handler的时间通知接口
 */
public interface IHandlerLikeNotify {
    /**
     * 消息通知
     *
     * @param what
     * @param arg1
     * @param arg2
     * @param obj
     */
    void onNotify(int what, int arg1, int arg2, Object obj);
}
