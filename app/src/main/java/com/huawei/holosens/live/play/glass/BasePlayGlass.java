package com.huawei.holosens.live.play.glass;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

/**
 * 玻璃的基类
 *
 */

public abstract class BasePlayGlass extends BaseGlass {

    public BasePlayGlass(View itemView) {
        super(itemView);
    }


    // ------------------------------------------------
    // #
    // ------------------------------------------------

    /**
     * 连接视频
     *
     */
    public abstract void connect();

    /**
     * 显示
     */
    public abstract void resume();

    /**
     * 暂停
     */
    public abstract void pause(boolean isGB28182);

    /**
     * 销毁
     */
    public abstract void disconnect();

    /**
     * 刷新
     *
     * @param state 状态
     * @param obj   内容
     */
    public abstract void refresh(int state, Object obj);

    /**
     * 改变border颜色
     *
     * @param selectedGlassNo 选中的玻璃号
     */
    public abstract void changeBorderColor(int selectedGlassNo);

    /**
     * 底部功能区和播放区的交互工作(码流切换,单向对讲等)
     */
    public abstract void doInteraction(String json);

    public abstract void setEdit(boolean isEdit);


    /**
     * 处理底层回调
     *
     * @param what
     * @param glassNo
     * @param result
     * @param obj
     */
    public abstract void handleNativeCallback(int what, int glassNo, int result, Object obj);

    // ------------------------------------------------
    // # 更新内容
    // ------------------------------------------------

    /**
     * 更新
     *
     * @param state 状态
     * @param obj
     */
    public void update(int state, Object obj) {
        Message message = sHandler.obtainMessage(REFRESH_CONTENT);
        message.arg1 = state;
        message.obj = obj;
        sHandler.sendMessage(message);
    }

    // ------------------------------------------------
    // # Handler
    // ------------------------------------------------
    private final int REFRESH_CONTENT = 1;
    private InternalHandler sHandler = new InternalHandler(Looper.getMainLooper());

    private class InternalHandler extends Handler {
        InternalHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_CONTENT:
                    refresh(msg.arg1, msg.obj);
                    break;
                default:
                    break;
            }
        }
    }

}