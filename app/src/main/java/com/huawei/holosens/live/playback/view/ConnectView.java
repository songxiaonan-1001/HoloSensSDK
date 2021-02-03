package com.huawei.holosens.live.playback.view;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huawei.holobasic.utils.DateUtil;
import com.huawei.holobasic.utils.FileUtil;
import com.huawei.holosens.R;
import com.huawei.holosens.consts.JVCloudConst;
import com.huawei.holosens.live.play.util.AlbumNotifyHelper;
import com.huawei.holosens.live.play.util.ToastUtilsB;
import com.huawei.holosens.view.ProgressBar;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;


public class ConnectView extends RelativeLayout {

    public static final int connecting = 0x20;// 连接中 ： 开始连接
    public static final int buffering1 = 0x21;// 缓冲中 ： connect change 回来，等待O帧
    public static final int buffering2 = 0x22;// 缓冲中... ： O帧过来了，等待I帧（I帧有可能解码失败）
    public static final int connected = 0x23;// 已连接 ： I帧过来了（分辨率很高时有可能I帧传的比较慢）
    public static final int connectFailed = 0x24;// 连接失败 : 连接失败
    public static final int disconnected = 0x25;// 断开连接 : 主动断开连接
    public static final int paused = 0x26;// 点击继续播放 ： 已暂停
    public static final int connectedNoData = 0x27;// 连接成功但是无数据

    public int height = -1;
    public int connectState = 0;// 连接状态

    public Animation hyperspaceJumpAnimation = null;//连接动画

    public TextView linkState;// 连接文字
    public TextView linkParam;// 连接参数文字
    public ImageView logoImg;// 小维logo
    public ImageView playImg;// 播放按钮

    private View mRecordTimeTip;// 录像时间提示框
    private ImageView mRecordPointIcon;
    private TextView mRecordTimeTV;

    public LinearLayout mTouchArea;

    private LayoutInflater inflater;
    private Context mContext;

    public ConnectView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public ConnectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public ConnectView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    public void setConnectState(int conState, int stringId) {
        Log.e("connectView", "setConnectState: conState = " + conState);
        try {
            connectState = conState;
            this.setBackgroundColor(getResources().getColor(R.color.transparent));
            playImg.setImageResource(R.mipmap.icon_play);
            switch (conState) {
                case connecting: {
                    linkState.setVisibility(View.VISIBLE);// 连接文字
                    linkState.setText(getResources().getString(R.string.connectting));
                    logoImg.setVisibility(View.VISIBLE);// 小维logo
                    playImg.setVisibility(View.GONE);// 播放按钮
                    mTouchArea.setClickable(false);
                    break;
                }
                case buffering1: {
                    linkState.setVisibility(View.VISIBLE);// 连接文字
                    linkState.setText(getResources().getString(R.string.buffering1));
                    logoImg.setVisibility(View.VISIBLE);// 小维logo
                    playImg.setVisibility(View.GONE);// 播放按钮
                    mTouchArea.setClickable(false);
                    break;
                }
                case buffering2: {
                    linkState.setVisibility(View.VISIBLE);// 连接文字
                    linkState.setText(getResources().getString(R.string.buffering2));
                    logoImg.setVisibility(View.VISIBLE);// 小维logo
                    playImg.setVisibility(View.GONE);// 播放按钮
                    mTouchArea.setClickable(false);
                    break;
                }
                case connected: {//已连接
                    linkState.setVisibility(View.GONE);// 连接文字
                    logoImg.setVisibility(View.GONE);// 小维logo
                    playImg.setVisibility(View.GONE);// 播放按钮
                    mTouchArea.setClickable(false);

                    break;
                }
                case connectedNoData: {//连接成功但是没有数据
                    if (TextUtils.isEmpty(getResources().getString(stringId))) {
                        linkState.setVisibility(View.GONE);// 连接文字
                    } else {
                        linkState.setVisibility(View.VISIBLE);// 连接文字
                        linkState.setText(getResources().getString(stringId));
                    }
                    logoImg.setVisibility(View.GONE);// 小维logo
                    playImg.setImageResource(R.mipmap.icon_retry);
                    playImg.setVisibility(View.VISIBLE);// 播放按钮
                    mTouchArea.setClickable(!TextUtils.isEmpty(getResources().getString(stringId)));
                    break;
                }
                case connectFailed: {//连接失败
                    linkState.setVisibility(View.VISIBLE);// 连接文字
                    linkState.setText(getResources().getString(stringId));
                    logoImg.setVisibility(View.GONE);// 小维logo
                    playImg.setImageResource(R.mipmap.icon_retry);
                    playImg.setVisibility(View.VISIBLE);// 播放按钮
                    mTouchArea.setClickable(true);
                    break;
                }
                case disconnected: {//已断开
                    linkState.setVisibility(View.VISIBLE);// 连接文字
                    if (stringId <= 0) {
                        linkState.setText(getResources().getString(R.string.disconnected));
                    } else {
                        linkState.setText(getResources().getString(stringId));
                    }

                    logoImg.setVisibility(View.GONE);// 小维logo
                    playImg.setVisibility(View.GONE);// 播放按钮
                    mTouchArea.setClickable(true);
                    break;
                }
                case paused: {//暂停
                    linkState.setVisibility(View.VISIBLE);// 连接文字
//                    linkState.setText(getResources().getString(R.string.click_to_connect));
                    logoImg.setVisibility(View.GONE);// 小维logo
                    playImg.setVisibility(View.VISIBLE);// 播放按钮
                    mTouchArea.setClickable(true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("connectView", "setConnectState: error = " + e.getMessage());
        }
    }

    /**
     * 显示连接参数
     *
     * @param linkParams
     */
    public void setConnectState(String linkParams) {
        linkParam.setVisibility(View.VISIBLE);// 连接文字
        linkParam.setText(linkParams);
    }

    private void init() {
        // 加载动画
        hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                mContext, R.anim.loading_animation);

        inflater = LayoutInflater.from(mContext);
        View root = inflater.inflate(R.layout.view_connect, null);

        linkState = (TextView) root.findViewById(R.id.link_state);
        linkParam = (TextView) root.findViewById(R.id.link_params);
        logoImg = (ImageView) root.findViewById(R.id.link_loading);
        playImg = (ImageView) root.findViewById(R.id.link_play);
        mTouchArea = (LinearLayout) root.findViewById(R.id.link_play_area);

        mRecordTimeTip = root.findViewById(R.id.lyt_record);
        mRecordPointIcon = root.findViewById(R.id.iv_record);
        mRecordTimeTV = root.findViewById(R.id.tv_record_time);

        logoImg.setImageDrawable(new ProgressBar(mContext, logoImg));

        if (getChildCount() > 0) {
            this.removeAllViews();
        }
        this.addView(root);
    }

    /**
     * 判断视频是否需要断开
     *
     * @return
     */
    public boolean needDisconnect() {
        boolean need = false;
        if (connecting == connectState
                || buffering1 == connectState
                || buffering2 == connectState
                || connected == connectState
                || paused == connectState) {// 已连接或者连接中 先断开视频
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
        if (connected == connectState || buffering2 == connectState || buffering1 == connectState || paused == connectState || connectedNoData == connectState) {// 已连接或者连接中 先断开视频
            connectRes = true;
        }

        return connectRes;
    }

    /**
     * 是否已暂停
     *
     * @return
     */
    public boolean isPaused() {
        boolean isPausedState = false;
        if (paused == connectState) {
            isPausedState = true;
        }

        return isPausedState;
    }


    /**
     * 是否已连接
     * Real
     *
     * @return
     */
    public boolean isRealConnected() {
        boolean connectRes = false;
        if (connected == connectState) {// 已连接或者连接中 先断开视频
            connectRes = true;
        }
        return connectRes;
    }


    /**
     * 是否已连接
     *
     * @return
     */
    public boolean isConnecting() {
        boolean connectRes = false;
        if (connecting == connectState
                || buffering1 == connectState
                || buffering2 == connectState) {// 已连接或者连接中 先断开视频
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
        return connectState;
    }

    public String getLinkState() {
        return linkState.getText().toString();
    }

    private void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof MarginLayoutParams) {
            MarginLayoutParams p = (MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    public void setRequestedOrientation(boolean isLand) {
        if (mRecordTimeTip.getVisibility() == VISIBLE) {
            // 调整录像时间提示距离顶部的距离
//            int marginTop;
//            if (isLand) {
//                marginTop = getResources().getDimensionPixelSize(R.dimen.margin_40);
//            } else {
//                marginTop = getResources().getDimensionPixelSize(R.dimen.margin_24);
//            }
//            setMargins(mRecordTimeTip, 0, marginTop, 0, 0);
        }
    }


    // ---------------------------------------------------
    // # 录像时间展示 Start
    // ---------------------------------------------------
    // 录像时间，单位s
    private int mRecordedTime = 0;
    // 录像timer
    private Timer mRecordingTimer = null;
    // 录像计时线程
    private TimerTask mRecordingTask = null;
    // 录像开始时间
    private long mRecordStartTime = 0;
    // 录像文件
    private String mRecordingFileName;

    /**
     * 开始录像
     *
     * @param fileName 录像文件名称
     */
    public void startRecord(String fileName) {
        //视频异常断开时，没有清空录像时间，为了防止此问题，在开启录像时清空录像时间
        mRecordedTime = 0;
        mRecordTimeTV.setText("00:00:00");

        if (null != mRecordingTimer) {
            mRecordingTimer.cancel();
            mRecordingTimer = null;
        }
        if (null != mRecordingTask) {
            mRecordingTask.cancel();
            mRecordingTask = null;
        }

        // 调整录像时间提示距离顶部的距离
//        int marginTop;
//        if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            marginTop = getResources().getDimensionPixelSize(R.dimen.margin_40);
//        } else {
//            marginTop = getResources().getDimensionPixelSize(R.dimen.margin_24);
//        }
//        setMargins(mRecordTimeTip, 0, marginTop, 0, 0);

        mRecordingFileName = fileName;
        mRecordStartTime = System.currentTimeMillis();
        Log.e("mRecordStartTime", "mRecordStartTime=" + mRecordStartTime);
        mRecordTimeTip.setVisibility(View.VISIBLE);
        mRecordingTask = new TimerTask() {
            @Override
            public void run() {
                mRecordedTime++;
                mRecordTimeTV.post(new Runnable() {
                    @Override
                    public void run() {
                        updateRecordCount(mRecordedTime);
                    }
                });
            }
        };

        mRecordingTimer = new Timer();
        mRecordingTimer.schedule(mRecordingTask, 0, 1000);

        mTouchArea.setBackgroundResource(R.drawable.border_glass_record);
    }

    /**
     * 停止录像
     */
    public void stopRecord(boolean needTip) {

        //TODO 录像时，分屏切换，不弹录像提示 2018.6.1

        mRecordTimeTV.setText(mRecordedTime + "");
        if (null != mRecordingTimer) {
            mRecordingTimer.cancel();
            mRecordingTimer = null;
        }
        if (null != mRecordingTask) {
            mRecordingTask.cancel();
            mRecordingTask = null;
        }
        mRecordTimeTip.setVisibility(View.GONE);


        if (needTip) {
            int recordTime = (int) (System.currentTimeMillis() - mRecordStartTime);
            Log.e("mRecordStartTime", "mRecordStartTime=" + mRecordStartTime + ";recordTime=" + recordTime);
            if (recordTime / 1000 < JVCloudConst.RECORD_VIDEO_MIN_LENGTH / 1000) {
//                ToastUtils.show(mContext, R.string.record_short_failed);
                ToastUtilsB.show(R.string.record_short_failed);
                if (!"".equalsIgnoreCase(mRecordingFileName)) {
                    FileUtil.deleteDirOrFile(new File(mRecordingFileName));
                }
            } else {
                AlbumNotifyHelper.insertVideoToMediaStore(mContext, mRecordingFileName, 0, recordTime);
                MediaScannerConnection.scanFile(mContext, new String[]{mRecordingFileName}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String s, Uri uri) {
                    }
                });

            }
        }
        mRecordedTime = 0;

        mTouchArea.setBackgroundResource(0);
    }

    /**
     * 是否正在录像
     */
    public boolean isRecording() {
        return mRecordTimeTip.getVisibility() == VISIBLE;
    }

    public int getRecordTime() {
        return mRecordedTime;
    }

    /**
     * 更新录像时间
     *
     * @param time 单位秒
     */
    private void updateRecordCount(int time) {
        if (mRecordTimeTip.getVisibility() != VISIBLE) return;
        // 红色圆点每秒显示隐藏
        if (time % 2 == 1) {
            mRecordPointIcon.setVisibility(View.VISIBLE);
        } else {
            mRecordPointIcon.setVisibility(View.INVISIBLE);
        }
        mRecordTimeTV.setText(DateUtil.getHoursAndSeconds(time));

        if (time < 5) {
//            ToastUtils.show(getContext(), getResources().getString(R.string.record_time_tip, 5-time));
            ToastUtilsB.show(getResources().getString(R.string.record_time_tip, 5-time));
        }
    }

    // ---------------------------------------------------
    // # 录像时间展示 End
    // ---------------------------------------------------


}
