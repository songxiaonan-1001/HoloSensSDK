package com.huawei.holosens.live.play.player;

import android.graphics.Point;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.google.gson.Gson;
import com.huawei.holobase.Consts;
import com.huawei.holobasic.consts.MySharedPreferenceKey;
import com.huawei.holobasic.utils.MySharedPreference;
import com.huawei.holosens.R;
import com.huawei.holosens.bean.Channel;
import com.huawei.holosens.bean.Device;
import com.huawei.holosens.bean.LiveUrlBean;
import com.huawei.holosens.bean.ProtocolType;
import com.huawei.holosens.consts.JVOctConst;
import com.huawei.holosens.live.play.bean.Glass;
import com.huawei.holosens.live.play.event.MsgEvent;
import com.huawei.holosens.live.play.ui.JVMultiPlayActivity;
import com.huawei.holosens.live.play.ui.WindowFragment;
import com.huawei.holosens.live.play.util.ExtendSimpleTask;
import com.huawei.holosens.live.play.util.MyGestureDispatcher;
import com.huawei.holosens.live.play.util.SimpleTask;
import com.huawei.holosens.live.play.util.SimpleThreadPool;
import com.huawei.holosens.utils.JniUtil;
import com.huawei.holosens.utils.ToastUtils;
import com.huawei.net.retrofit.impl.AppImpl;
import com.huawei.net.retrofit.impl.ResponseListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * 云视通协议2.0
 *
 */
public class C2PlayHelper extends BasePlayHelper {

    private static final String TAG = "C2PlayHelper";

    // 断开视频连接超时时间
    private static final int DISCONNECT_TIMEOUT = 12 * 1000;

    // 播放状态
    private static final int IDLE = 0;
    private static final int STARTED = 1;
    private static final int RESUMED = 2;
    private static final int PAUSED = 3;
    private static final int STOPPED = 4;


    // 连接状态信息数组
    private String[] mConnectStateArray;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Surface mSurface;
    private OnStateChangeListener mStateChangeListener;
    private Glass mGlass;
    private Channel mChannel;
    private Device mDevice;
    private int mGlassNo;
    private int mPlayState = IDLE;
    private volatile boolean isExistSurface;
    private ExtendSimpleTask<Integer> mConnectTask;
    private SimpleTask mDisconnectTask, mDisconnectTimeoutTask;
    private volatile boolean isDisconnectTimeout;
    // 等待O帧任务(调用底层JNI方法resume时使用)
    private SimpleTask mResumeTask;
    private SimpleTask mPauseTask;
    private GestureDetector mGestureDetector;

    protected JVMultiPlayActivity mActivity;
    protected WindowFragment mWindow;

    public C2PlayHelper(SurfaceView surfaceView, Glass glass, WindowFragment window, OnStateChangeListener onStateChangeListener) {
        mWindow = window;
        mActivity = window.getPlayActivity();
        mGlass = glass;
        mGlassNo = glass.getNo();
        mChannel = glass.getChannel();
        mDevice = glass.getChannel().getParent();
        mConnectStateArray = surfaceView.getContext().getResources().getStringArray(R.array.c2_connect_state);
        initSurfaceViewAndListener(surfaceView, onStateChangeListener);
        mGestureDetector = new GestureDetector(new MyGestureListener());
        mGestureDetector.setOnDoubleTapListener(new MyDoubleTapListener());
    }

    /**
     * 初始化SurfaceView
     *
     * @param surfaceView
     * @param onStateChangeListener
     */
    public void initSurfaceViewAndListener(SurfaceView surfaceView, OnStateChangeListener onStateChangeListener) {
        mSurfaceView = surfaceView;
        mSurfaceHolder = surfaceView.getHolder();
        mStateChangeListener = onStateChangeListener;
        // 设置回调
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.e(TAG, "Surface 销毁");
                isExistSurface = false;
                destroy();
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.e(TAG, "Surface 创建");
                mSurfaceHolder = holder;
                mSurface = holder.getSurface();
                mStateChangeListener.onSurfaceCreated();
                isExistSurface = true;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.e(TAG, "Surface 改变");
                mSurface = holder.getSurface();
                mChannel.setLastPortWidth(width);
                mChannel.setLastPortHeight(height);
                if (null != mSurfaceView) {
                    mChannel.setLastPortLeft(mSurfaceView.getLeft());
                } else {
                    mChannel.setLastPortLeft(0);
                }
                mChannel.setLastPortBottom(0);
            }
        });

        // 设置SurfaceView监听
        if (null != mSurfaceView) {
            mSurfaceView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mGestureDetector.onTouchEvent(event);
                    mDispatcher.motion(event, false);
                    return true;
                }
            });
        }
    }


    @Override
    public void connect(final boolean isPreConnect) {

        if (mPlayState == STARTED) {
            return;
        }
        mPlayState = STARTED;
        mGlass.setManualDisconnect(false);
        mStateChangeListener.onUpdate(prepare, 0);
        if(mChannel.getOnlineStatus() == 0){        //离线
            mStateChangeListener.onUpdate(connectFailed, mConnectStateArray[27]);
        }else {
            getPlayInfo();
        }
    }

    private void startConnect() {
        Log.e(TAG, "startConnect: " + mActivity.isFinishing());
        if (mActivity.isFinishing()) return;

        if (mConnectTask == null) {
            mConnectTask = new ExtendSimpleTask<Integer>() {
                @Override
                public Integer doInBackground() {
                    int result = -999;
                    try {
                        if (!Thread.interrupted()) {
                            /**
                             * 以下两种场合需要等待:
                             * 1.如果SurfaceView还没有创建, 等待创建完成
                             * 2.视频断开任务正在执行, 需要等待任务执行完成
                             */
                            boolean isDisconnecting = mDisconnectTask != null && !(mDisconnectTask.isDone() ||
                                    mDisconnectTask.isCancelled());
                            Log.e(TAG, "开始执行连接任务, isExistSurface:" + isExistSurface + ", " +
                                    "isDisconnecting:" + isDisconnecting);
                            while (!isExistSurface || isDisconnecting) {
                                SystemClock.sleep(50);
                                isDisconnecting = mDisconnectTask != null && !(mDisconnectTask.isDone() ||
                                        mDisconnectTask.isCancelled());
                            }

                            /*
                              TODO
                              现在的想法是：完全不去考虑上次是否断开的问题。应用层不需要关心这个，使用就去连接，用完就断开。
                              否则去连接视频很麻烦:
                              1.如果之前连接过，需要确认是否已经断开，否则连接不上视频；
                              2.如果之前从未连接过，直接连接。
                             */
                            setConnectState(connecting);
                            mStateChangeListener.onUpdate(connecting, 0);
                            Log.e(TAG, "connect#start" + mActivity.isFinishing() + ", " + isCancelled());

                            result = JniUtil.holosensPlayerConnectByP2p(mChannel.getJvmpUrl(), mGlass.getNo(), mChannel.getStreamTag()==JVOctConst.STREAM_SD?1:0, mChannel.getChannelType() == ProtocolType.HOLO?Integer.parseInt(mChannel.getChannel_id()):0);

                            Log.e(TAG, "connect#end" + mActivity.isFinishing() + ", " + isCancelled());
                            if (!mActivity.isFinishing() && !isCancelled()) {
                                Thread.sleep(1);
                            }
                        }
                    } catch (InterruptedException e) {
                        // TODO
                        e.printStackTrace();
                        Log.e(TAG, " e.printStackTrace:" + e.toString());
                    }
                    Log.e(TAG, "result:" + result);
                    return result;
                }

                @Override
                public void onFinish(boolean canceled, Integer result) {
                    if (canceled) {
                        return;
                    }

                    if (result < 0) {
                        int code = result + 15 + ((0 - result) * 2) - 16;
                        Log.e(TAG, "连接失败!, result=" + result + ", code=" + code);
                        mStateChangeListener.onUpdate(connectFailed, mConnectStateArray[code]);
                        mPlayState = IDLE;
                    } else {
                        Log.e(TAG, "等待连接回调!");
                    }
                }

                @Override
                protected void onCancel() {
                    Log.e(TAG, "连接被取消!");
                }
            };
        } else {
            if (mConnectTask.isCancelled() || mConnectTask.isDone()) {
                mConnectTask.restart();
            }
        }

        SimpleThreadPool.execute(mGlassNo, mConnectTask);
    }

    /**
     * 获取预览需要的信息
     */
    private void getPlayInfo() {
        if (mChannel.getChannelType() == ProtocolType.HOLO) {                   //好望设备
            ResponseListener listener = new ResponseListener() {
                @Override
                public void onSuccess(String result) {
                    Log.i(TAG+"11", "好望p2p:" + result);
                    if (result != null) {
                        mChannel.setMts(result);
                        mChannel.setJvmpUrl(result);
                    }
                    startConnect();
                }

                @Override
                public void onFailed(Throwable throwable) {
                    Log.i(TAG+"11", "好望p2p:onFailed");
                }
            };
            HashMap<String, Object> params = new HashMap<>();
            JSONArray channels = new JSONArray();
            JSONObject channel = new JSONObject();
            try {
                channel.put("device_id", mChannel.getParent().getSn());
                channel.put("channel_id", mChannel.getChannel_id());
                channel.put("stream_type", mChannel.getStreamTag()==JVOctConst.STREAM_SD?"SECONDARY_STREAM_1":"PRIMARY_STREAM");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            channels.put(channel);

            Log.i(TAG+"11", "channels:" + channels.toString());
            Log.i(TAG+"11", "channel:" + channel.toString());

            params.put("channels", channels);
            String url = Consts.HOLO_PLAYURL.replace("{user_id}", Consts.userId);
            AppImpl.getInstance(mActivity).getDataByPostMethod(url, MySharedPreference.getString(MySharedPreferenceKey.LoginKey.TOKEN), params, listener);

        } else {                        //国标的设备
            ResponseListener listener = new ResponseListener() {
                @Override
                public void onSuccess(String result) {
                    Log.i(TAG, "GBp2p:" + result);
                    if (result != null) {
                        LiveUrlBean liveUrl = new Gson().fromJson(result, LiveUrlBean.class);
                        if(liveUrl.getFailNum()== 0) {
                            try {
                                JSONObject object = new JSONObject(result);
                                JSONArray array = object.optJSONArray("channels");
                                JSONObject object1 = (JSONObject) array.get(0);
                                if (TextUtils.isEmpty(object1.optString("live_url"))) {
                                    ToastUtils.show(mActivity, "没有获取到播放地址");
                                    mStateChangeListener.onUpdate(connectFailed, mConnectStateArray[1]);
                                    return;
                                }
                                mChannel.setJvmpUrl(object1.optString("live_url"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            mChannel.setMts(result);
                            startConnect();
                        }else{
                            mStateChangeListener.onUpdate(connectFailed, mConnectStateArray[1]);
                            ToastUtils.show(mActivity, liveUrl.getChannels().get(0).getResult().getMsg());
                        }
                    }


                }

                @Override
                public void onFailed(Throwable throwable) {

                }
            };
            HashMap<String, Object> params = new HashMap<>();
            JSONArray channels = new JSONArray();
            JSONObject channel = new JSONObject();
            try {
                channel.put("device_id", mChannel.getParent().getSn());
                channel.put("channel_id", mChannel.getChannel_id());
                channel.put("stream_type", mChannel.getStreamTag()==JVOctConst.STREAM_SD?"SECONDARY_STREAM_1":"PRIMARY_STREAM");
                channel.put("live_protocol", "HOLO");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            channels.put(channel);
            params.put("channels", channels);
            String url = Consts.GB_PLAYURL.replace("{user_id}", Consts.userId);
            AppImpl.getInstance(mActivity).getDataByPostMethod(url, MySharedPreference.getString(MySharedPreferenceKey.LoginKey.TOKEN), params, listener);
        }


    }

    /**
     * 只要连接回调过来, 连接成功了, 就可以调用resume方法了
     */
    @Override
    public synchronized void resume() {

        if (mPlayState == STARTED) {
            return;
        }

        if (mPlayState == RESUMED) {
            return;
        }
        mPlayState = RESUMED;


        /*
           首先检查是否需要连接:
           a.查看当前连接状态
           b.检查是否有断开视频的任务正在执行
         */
        boolean isDisconnecting = mDisconnectTask != null && !(mDisconnectTask.isDone() ||
                mDisconnectTask.isCancelled());
        boolean needConnect = needConnect() || isDisconnecting;

        if (needConnect) {
            Log.e(TAG, "未连接, 去连接!");
            connect(false);
            return;
        }

        // 中断pause任务
        if (mPauseTask != null) {
            mPauseTask.cancel();
        }

        // 已经连接, 忽略操作
        if (isIFrameOk()) {
            return;
        }


        mStateChangeListener.onUpdate(buffering1, 0);
        if (mResumeTask == null) {
            mResumeTask = new SimpleTask() {
                @Override
                public void doInBackground() {
                    try {
                        Log.e(TAG, "resume-E");
                        if (!Thread.interrupted()) {
                            Log.e(TAG, "开始执行resume任务, 是否已经连接:" + isExecuteResume() + ", state:" +
                                    getConnectState());
                            while (!isExecuteResume()) {
                                SystemClock.sleep(50);
                            }

                            Log.e(TAG, "wait surface#start");
                            while (!isExistSurface) {
                                SystemClock.sleep(50);
                            }
                            Log.e(TAG, "wait surface#end");
                            /*
                              TODO BUG
                               a.从播放跳转到远程回放, 文件播放失败.
                               b.点击返回, 回到播放界面, 这个时候很大概率会连接不上视频.
                               对于远程回放文件播放失败,点击返回的处理:
                               Android:不去调用停止远程回放视频(stopRemoteFile)
                               iOS:调用停止远程回放视频,等回调回来以后再返回.
                               针对Android远程回放界面目前的这种处理,只能在重新回到播放界面的时候睡100毫秒(详细参考JVPlayActivity)
                               这个问题的原因需要底层同事调查, 现在感觉iOS的处理更加合理
                             */
                            SystemClock.sleep(100);
                            Log.e(TAG, "resume#start:" + mSurface);
                            Log.e(TAG, "resume-ing:mPlayState=" + mPlayState);

                            if (mPlayState == STOPPED) {

                                Log.e(TAG, "resume-exist:mPlayState=" + mPlayState);
                                return;
                            } else {
                                //恢复的时候要按照离开播放界面的码流打开对应的码流
//                                OctUtil.octOpenLiveStream(mGlassNo, mChannel.getChannel(), mChannel.getStreamTag(), 0);
//                                FunctionUtil.resumeSurface(mGlassNo, mSurface);
                                Log.e(TAG, "resume-X");
                                setConnectState(buffering2);
                                setPaused(false);
                                Log.e(TAG, "resume#end");

                                Thread.sleep(1);
                            }

                        }
                    } catch (InterruptedException e) {
                        // TODO
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFinish(boolean canceled) {
                }

                @Override
                protected void onCancel() {
                }
            };
        } else {
            if (mResumeTask.isCancelled() || mResumeTask.isDone()) {
                mResumeTask.restart();
            }
        }

        SimpleThreadPool.execute(mGlassNo, mResumeTask);
    }

    @Override
    public synchronized void pause() {
        if (null != mChannel) {
            mChannel.setStreamTag(JVOctConst.STREAM_SD);
        }
        if (mPlayState == PAUSED) {
            return;
        }
        mPlayState = PAUSED;

        // 中断resume任务
        if (mResumeTask != null) {
            mResumeTask.cancel();
        }

        // 未连接, 直接走断开
        if (!isConnected()) {
            disconnect();
            return;
        }

        if (mPauseTask == null) {
            mPauseTask = new SimpleTask() {
                @Override
                public void doInBackground() {
                    Log.e(TAG, "pause-E");
                    try {
                        if (!Thread.interrupted()) {
                            Log.e(TAG, "C1PlayHelper-pause");
                            Log.e(TAG, "pause#start");
                            Log.e(TAG, "pause-ing");
//                            OctUtil.octCloseLiveStream(mGlassNo);
//                            FunctionUtil.pauseSurface(mGlassNo);
                            Log.e(TAG, "pause-X");
                            setConnectState(paused);
                            setPaused(true);
                            Log.e(TAG, "pause#end");
                            stopAllFunction();
                            Thread.sleep(1);
                        }
                    } catch (InterruptedException e) {
                        // TODO
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFinish(boolean canceled) {
                }

                @Override
                protected void onCancel() {
                }
            };
        } else {
            if (mPauseTask.isCancelled() || mPauseTask.isDone()) {
                mPauseTask.restart();
            }
        }

        SimpleThreadPool.execute(mGlassNo, mPauseTask);
    }

    @Override
    public void disconnect() {
        if (null != mChannel) mChannel.setMts(null);
        if (mPlayState == STOPPED) {
            return;
        }
        mPlayState = STOPPED;

        // 中断连接任务
        if (mConnectTask != null) {
            mConnectTask.cancel();
        }

        // 只有处于连接中或者已连接才断开, 否则不执行断开连接操作
        if (!needDisconnect()) {
            Log.e(TAG, "不符合断开条件, state:" + getConnectState());
            return;
        }

        if (mDisconnectTask == null) {
            mDisconnectTask = new SimpleTask() {
                @Override
                public void doInBackground() {
                    try {
                        if (!Thread.interrupted()) {
                            stopAllFunction();
                            // 断开视频
                            Log.e(TAG, "disconnect#start");
                            mGlass.setManualDisconnect(true);
//                            boolean result = FunctionUtil.disconnect(mGlass.getNo());
                            JniUtil.disConnect(mGlass.getNo());
//                            Log.e(TAG, "disconnect#end, result:" + result);
                            setConnectState(disconnected);
//                            if (!result) {
//                                Log.e(TAG, "disconnect failed!");
//                            }

                            Thread.sleep(1);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFinish(boolean canceled) {
                    if (canceled) {
                        return;
                    }

                    reset();
                    closeDisconnectTimeout();
                }

                @Override
                protected void onCancel() {
                    reset();
                    if (isDisconnectTimeout) {
                        Log.e(TAG, "[超时] 断开视频连接超时");
                    } else {
                        closeDisconnectTimeout();
                    }
                }
            };
        } else {
            if (mDisconnectTask.isCancelled() || mDisconnectTask.isDone()) {
                mDisconnectTask.restart();
            }
        }

        SimpleThreadPool.execute(mGlassNo, mDisconnectTask);
        startDisconnectTimeout();
    }

    @Override
    public void switchStream(){
        disconnect();
        connect(false);
    }

    public void reset() {
        mPlayState = IDLE;
        // 重置状态
        setSendKeyFrame(false);
    }

    @Override
    public void destroy() {
        mSurfaceView = null;
        mSurfaceHolder = null;
    }

    @Override
    public void changeSize(Glass.Size glassSize) {
//        FunctionUtil.setViewPort(mGlass.getNo(), 0, 0, glassSize.width, glassSize.height);
    }

    /**
     * 只发关键帧
     */
    public void sendKeyFrameOnly() {
        setSendKeyFrame(true);
//        FunctionUtil.setOnlyIFrame(mGlassNo, JVEncodedConst.CONNECT_BY_CLOUDSEE2);
    }

    /**
     * 发全帧
     */
    public void sendFullFrame() {
        setSendKeyFrame(false);
//        FunctionUtil.setFullFrame(mGlassNo, JVEncodedConst.CONNECT_BY_CLOUDSEE2);
    }

    /**
     * 中断 断开视频连接任务
     */
    public void interruptDisconnectTask() {
        if (mDisconnectTask != null) {
            mDisconnectTask.cancel();
        }
    }

    /**
     * 开始断开连接超时计时
     */
    public void startDisconnectTimeout() {
        if (mDisconnectTimeoutTask == null) {
            mDisconnectTimeoutTask = new SimpleTask() {
                @Override
                public void doInBackground() {
                    try {
                        if (!Thread.interrupted()) {
                            Thread.sleep(1);
                        }
                    } catch (InterruptedException e) {
                        // TODO
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFinish(boolean canceled) {
                    if (canceled) {
                        return;
                    }
                    isDisconnectTimeout = true;
                    interruptDisconnectTask();
                }
            };
        } else {
            mDisconnectTimeoutTask.cancel();
            mDisconnectTimeoutTask.restart();
        }

        Log.e(TAG, "[开始] 断开视频超时计时");
        isDisconnectTimeout = false;
        SimpleTask.postDelay(mDisconnectTimeoutTask, DISCONNECT_TIMEOUT);
    }

    /**
     * 关闭断开连接超时计时
     */
    public void closeDisconnectTimeout() {
        Log.e(TAG, "[中断] 断开视频超时计时");
        SimpleTask.removeCallbacks(mDisconnectTimeoutTask);
        interruptDisconnectTask();
    }

    /**
     * 停止所有的功能
     */
    private void stopAllFunction() {
        if (null != mChannel) {
            // 断开所有功能
            Log.e(TAG, "stopAllFunc#start");// 断开所有功能
            // 如果正在对讲,等待对讲断开(最长等待2秒)
            int timeout = 0;
            while (null != mChannel && mChannel.isVoiceCall() && timeout < 1 * 500) {
                SystemClock.sleep(50);
                timeout += 50;
            }
            mChannel.setVoiceCall(false);
            mChannel.setLisening(false);
//        mChannel.setRecording(false);
            MsgEvent event = new MsgEvent();
            event.setMsgTag(MsgEvent.MSG_EVENT_INTERACTION);
            JSONObject item = new JSONObject();
            try {
                item.put("type", "switchAudio");
                item.put("audioOpen", false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            event.setAttachment(item.toString());
            EventBus.getDefault().post(event);
            Log.e(TAG, "stopAllFunc#end");
        }
    }

    // -----------------------------------------------------
    // 手势 start
    // -----------------------------------------------------

    MyGestureDispatcher mDispatcher = new MyGestureDispatcher(
            new MyGestureDispatcher.OnGestureListener() {
                @Override
                public void onGesture(int gesture, int distance, Point vector, Point middle) {
                    /*
                      不使用我们自己写的单击监测事件, 因为我们对于单击/双击区分的不好
                      这个监听类目前的主要作用就是横屏下监听手势的滑动、放大缩小
                     */
                    if (gesture == MyGestureDispatcher.CLICK_EVENT) {
                        return;
                    }
                    mStateChangeListener.onGesture(mGlassNo, gesture, distance, vector, middle);
                }
            });

    /**
     * 单击
     *
     * @param e
     */
    @Override
    protected void onSingleClick(MotionEvent e) {
        mStateChangeListener.onGesture(mGlassNo, MyGestureDispatcher.CLICK_EVENT, 0, null, null);
    }

    /**
     * 双击
     *
     * @param e
     */
    @Override
    protected void onDoubleClick(MotionEvent e) {
        mStateChangeListener.onGesture(mGlassNo, MyGestureDispatcher.DOUBLE_CLICK_EVENT, 0, null, null);
    }
    // -----------------------------------------------------
    // 手势 end
    // -----------------------------------------------------


}