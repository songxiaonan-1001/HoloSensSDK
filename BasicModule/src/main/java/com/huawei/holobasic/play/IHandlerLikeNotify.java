package com.huawei.holobasic.play;

/**
 * 类似Handler的时间通知接口
 */
public interface IHandlerLikeNotify {
    /**
     * 消息通知
     *
     * @param what 分类
     * @param arg1 参数1
     * @param arg2 参数2
     * @param obj  附加对象
     */
    void onNotify(int what, int arg1, int arg2, Object obj);
}
