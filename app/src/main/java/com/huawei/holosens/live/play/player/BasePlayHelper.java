package com.huawei.holosens.live.play.player;

import android.graphics.Point;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.huawei.holosens.live.play.bean.Glass;


/**
 *
 */

public class BasePlayHelper implements IPlayHelper {
    // 字幕(连接状态)
    private volatile int mConnectState = 0;
    // 是否只发送关键帧
    private volatile boolean isSendKeyFrame;
    // 视频是否暂停
    private volatile boolean isPaused;
    // 是否有过O帧
    private volatile boolean hasOFrame;

    @Override
    public void connect(boolean isPreConnect) {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void changeSize(Glass.Size glassSize) {

    }

    @Override
    public void switchStream() {

    }

    /**
     * 判断视频是否需要断开
     *
     * @return
     */
    public boolean needDisconnect() {
        boolean need = false;
        int state = getConnectState();
        //没来过O帧但是已经有连接结果了，不需要断开
//        if (!hasOFrame && connectFailed == state) {
//            return false;
//        }
        /*
          a.正在连接
          b.connect change过来或者o帧、I帧过来
          c.暂停中
          d.连接失败
         */
        if (connecting == state
                || buffering1 == state
                || buffering2 == state
                || connected == state
                || paused == state
                || connectFailed == state) {
            need = true;
        }

        return need;
    }

    /**
     * 判断视频是否需要连接
     *
     * @return
     */
    public boolean needConnect() {
        boolean need = false;
        int state = getConnectState();
        /*
            a.默认
            b.连接失败
            c.断开连接
         */
        if (state == 0 || state == connectFailed || state == disconnected) {
            need = true;
        }

        return need;
    }

    /**
     * 是否已连接
     *
     * @return
     */
    public boolean isConnected() {
        boolean connectRes = false;
        int state = getConnectState();
        /*
            a.O帧过来
            b.I帧过来(connected)
            c.暂停中
         */
        if (connected == state
                || buffering2 == state
                || paused == state) {
            connectRes = true;
        }

        return connectRes;
    }

    /**
     * 是否可以执行resume方法
     *
     * @return
     */
    public boolean isExecuteResume() {
        boolean connectRes = false;
        int state = getConnectState();
        /*
            a.connect change 回来
            b.O帧过来
            c.暂停中
         */
        if (buffering1 == state
                || buffering2 == state
                || paused == state) {
            connectRes = true;
        }

        return connectRes;
    }

    /**
     * 是否出图(I帧)
     *
     * @return
     */
    public boolean isIFrameOk() {
        boolean connectRes = false;
        int state = getConnectState();
        if (connected == state) {
            connectRes = true;
        }
        return connectRes;
    }

    /**
     * 主动获取当前状态
     *
     * @return 当前视频连接状态
     */
    public int getConnectState() {
        return mConnectState;
    }

    public void setConnectState(int connectState) {
        mConnectState = connectState;
        if (mConnectState == buffering2) {
            hasOFrame = true;
        }
    }

    public boolean isSendKeyFrame() {
        return isSendKeyFrame;
    }

    public void setSendKeyFrame(boolean sendKeyFrame) {
        isSendKeyFrame = sendKeyFrame;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public interface OnStateChangeListener {
        /**
         * Surface创建
         */
        void onSurfaceCreated();

        /**
         * 更新内容
         *
         * @param code
         * @param obj
         */
        void onUpdate(int code, Object obj);

        /**
         * 手势方向事件
         *
         * @param glassNo  窗口号
         * @param gesture  方向
         * @param distance 距离
         * @param vector   向量
         * @param middle   中心点
         */
        void onGesture(int glassNo, int gesture, int distance,
                       Point vector, Point middle);
    }

    // -----------------------------------------------------
    // 手势 start
    // -----------------------------------------------------

    /**
     * 单击
     *
     * @param e
     */
    protected void onSingleClick(MotionEvent e) {
    }

    /**
     * 双击
     *
     * @param e
     */
    protected void onDoubleClick(MotionEvent e) {

    }

    /**
     * 基本的手势监听
     */
    class MyGestureListener implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }

    /**
     * 双击监听(可以有效的区分单双击)
     */
    class MyDoubleTapListener implements GestureDetector.OnDoubleTapListener {

        /*
        * 单击事件。用来判定该次点击是单纯的SingleTap而不是DoubleTap，如果连续点击两次就是DoubleTap手势，
        * 如果只点击一次，系统等待一段时间后没有收到第二次点击则判定该次点击为SingleTap而不是DoubleTap，
        * 然后触发SingleTapConfirmed事件。触发顺序是：OnDown->OnsingleTapUp->OnsingleTapConfirmed
        * 关于onSingleTapConfirmed和onSingleTapUp的一点区别：二者的区别是：onSingleTapUp，只要手抬起就会执行，
        * 而对于onSingleTapConfirmed来说，如果双击的话，则onSingleTapConfirmed不会执行。
        * */
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            onSingleClick(e);
            return false;
        }


        /*
        * 双击事件
        * */
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return false;
        }


        /*
        *双击间隔中发生的动作。指触发onDoubleTap以后，在双击之间发生的其它动作，包含down、up和move事件
        * */
        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }
    }
    // -----------------------------------------------------
    // 手势 end
    // -----------------------------------------------------

}