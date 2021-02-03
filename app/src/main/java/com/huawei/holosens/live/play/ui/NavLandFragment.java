package com.huawei.holosens.live.play.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huawei.holosens.R;
import com.huawei.holosens.bean.Channel;
import com.huawei.holosens.bean.Device;
import com.huawei.holosens.live.play.bean.Glass;
import com.huawei.holosens.live.play.event.MsgEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 横屏模式下的顶部导航栏
 *
 */
public class NavLandFragment extends SimpleFragment implements View.OnTouchListener {


    /** 顶部按钮 */
    private View mRootView;
    private View mBackContainer;// 横屏模式的顶部返回布局
    private TextView mTitle;// 标题
    private ImageView mBtnShare, mBtnSettings, mBtnStream, mBtnCollect, mBtnAudio;

    /** 左侧按钮 */
    private LinearLayout mLeftLayout;
    private ImageView mBtnCall, mBtnPtz;
    private FrameLayout mPtzLayout;
    private ImageView mPtzCenterImg, mPtzImg;

    /** 右侧按钮 */
    private LinearLayout mRightLayout;
    private ImageView mBtnSnap, mBtnVideoRecord, mBtnPlayback;
    private ImageView mBtnPlayBackCloud;

    private Timer mTimer;
    private HideNavTimerTask mTimerTask;

    private int mCurrentSpanCount = 1;
    private Channel channel;


    public static NavLandFragment newInstance(Bundle bundle) {
        NavLandFragment fragment = new NavLandFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == mRootView) {
            mRootView = inflater.inflate(R.layout.fragment_play_nav_land, container, false);
            mBackContainer = mRootView.findViewById(R.id.rlyt_back);
            mTitle = mRootView.findViewById(R.id.tv_title);
            mBtnShare = mRootView.findViewById(R.id.btn_land_share);
            mBtnSettings = mRootView.findViewById(R.id.btn_land_settings);
            mBtnStream = mRootView.findViewById(R.id.btn_land_stream);
            mBtnCollect = mRootView.findViewById(R.id.btn_land_collect);
            mBtnAudio = mRootView.findViewById(R.id.btn_land_audio);
            mRootView.findViewById(R.id.btn_back).setOnClickListener(mActivity);
            mBtnShare.setOnClickListener(mActivity);
            mBtnSettings.setOnClickListener(mActivity);
            mBtnStream.setOnClickListener(mActivity);
            mBtnCollect.setOnClickListener(mActivity);
            mBtnAudio.setOnClickListener(mActivity);
            setTitle(mActivity.getTopBarTitle());

            mLeftLayout = mRootView.findViewById(R.id.layout_play_left);
            mBtnCall = mRootView.findViewById(R.id.btn_land_call);
            mBtnPtz = mRootView.findViewById(R.id.btn_land_ptz);
            mPtzLayout = mRootView.findViewById(R.id.layout_land_ptz);
            mPtzImg = mRootView.findViewById(R.id.land_ptz_img);
            mPtzCenterImg = mRootView.findViewById(R.id.land_ptz_center_img);
            mBtnCall.setOnClickListener(mActivity);
            mBtnPtz.setOnClickListener(mActivity);
            mPtzCenterImg.setOnTouchListener(this);

            mRightLayout = mRootView.findViewById(R.id.layout_play_right);
            mBtnSnap = mRootView.findViewById(R.id.btn_land_snap);
            mBtnVideoRecord = mRootView.findViewById(R.id.btn_land_video);
            mBtnPlayback = mRootView.findViewById(R.id.btn_land_playback);
            mBtnPlayBackCloud = mRootView.findViewById(R.id.btn_land_playback_cloud);
            mBtnSnap.setOnClickListener(mActivity);
            mBtnVideoRecord.setOnClickListener(mActivity);
            mBtnPlayback.setOnClickListener(mActivity);
            mBtnPlayBackCloud.setOnClickListener(mActivity);

        }
        return mRootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        updateAfterSpan(getArguments());

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        cancelHideBarTimer();
        mTimer = null;
        mTimerTask = null;
    }

    /**
     * 云台滑动事件
     */
    private int xDelta;
    private int yDelta;
    private int maxLen = 85;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        final int x = (int) event.getRawX();
        final int y = (int) event.getRawY();
        if (view.getId() == R.id.land_ptz_center_img) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_UP:
                    FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams) view.getLayoutParams();
                    params1.leftMargin = 0;
                    params1.topMargin = 0;
                    view.setLayoutParams(params1);
                    mPtzImg.setImageResource(R.mipmap.device_live_hor_ptz_bg);
                    mActivity.ptzMoveStop();
                    mDirection = 0;
                    break;
                case MotionEvent.ACTION_DOWN:
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                    xDelta = x - params.leftMargin;
                    yDelta = y - params.topMargin;
                    break;
                case MotionEvent.ACTION_MOVE:
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                    int xDistance = x - xDelta;
                    int yDistance = y - yDelta;
                    if (xDistance > maxLen)
                        xDistance = maxLen;
                    if (xDistance < -maxLen)
                        xDistance = -maxLen;

                    if (yDistance > maxLen)
                        yDistance = maxLen;
                    if (yDistance < -maxLen)
                        yDistance = -maxLen;
                    layoutParams.leftMargin = xDistance;
                    layoutParams.topMargin = yDistance;
                    view.setLayoutParams(layoutParams);
                    //方向判断
                    showDirection(xDistance, yDistance);
                    break;
            }
            mPtzImg.invalidate();
        }

        return true;
    }

    int leftx = -50;
    int rightx = 50;
    int upy = -50;
    int downy = 50;
    int mDirection = 0;  //0 停止 1左 2右 3上 4下
    private int mCurrentPTZSpeed = 120;

    private void showDirection(int x, int y) {

        int mLeft = 0;
        int mUp = 0;
        int mZoom = 0;


        if (x < leftx) {        //左半侧区域
            if (Math.abs(x) >= Math.abs(y)) {  //x的绝对值 大于y
//                ToastUtil.show(this, "左");
                if (mDirection == 1)
                    return;
                else {
                    mDirection = 1;
                    mLeft = mCurrentPTZSpeed;

                    mPtzImg.setImageResource(R.mipmap.device_live_hor_ptz_left);
                }
            }
        }
        if (x > rightx) {       //又半侧区域
            if (Math.abs(x) >= Math.abs(y)) {  //x的绝对值 大于y
//                ToastUtil.show(this, "右");
                if (mDirection == 2)
                    return;
                else {
                    mLeft = -mCurrentPTZSpeed;
                    mDirection = 2;

                    mPtzImg.setImageResource(R.mipmap.device_live_hor_ptz_right);
                }
            }
        }
        if (y < upy) {          //上半侧区域
            if (Math.abs(y) > Math.abs(x)) {
//                ToastUtil.show(this, "上");
                if (mDirection == 3)
                    return;
                else {
                    mUp = mCurrentPTZSpeed;
                    mDirection = 3;

                    mPtzImg.setImageResource(R.mipmap.device_live_hor_ptz_up);
                }
            }
        }
        if (y > downy) {        //下半侧区域
            if (Math.abs(y) > Math.abs(x)) {
//                ToastUtil.show(this, "下");
                if (mDirection == 4)
                    return;
                else {
                    mUp = -mCurrentPTZSpeed;
                    mDirection = 4;

                    mPtzImg.setImageResource(R.mipmap.device_live_hor_ptz_down);
                }
            }
        }
        if (mLeft == 0 && mUp == 0 && mZoom == 0)
            return;
        mActivity.ptzMoveStart(mLeft, mUp, mZoom);
    }


    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        if (mTitle == null) {
            return;
        }
        mTitle.setText(title);
    }

    /**
     * 分屏数量变化后, 进行信息更新
     *
     * @param bundle 更新的参数信息(一扇窗户上的玻璃数量、窗户号)
     */
    public void updateAfterSpan(Bundle bundle) {
        mCurrentSpanCount = bundle.getInt("spanCount");
        int glassNo = bundle.getInt("selectedGlassNo");

        mBtnShare.setVisibility(View.GONE);
        mBtnSettings.setVisibility(View.GONE);
        mBtnStream.setImageResource(R.mipmap.ic_stream_land_sd);

        Glass glass = mActivity.getGlassByNo(glassNo);
        if (null != glass) {
            channel = glass.getChannel();
            if (null != channel) {
                mBtnCall.setEnabled(channel.isSupportCall() || channel.isSupportNvrCall());
                mBtnPtz.setEnabled(channel.isSupportPtz());
//                mBtnCall.setEnabled(true);
//                mBtnPtz.setEnabled(true);
                Device device = channel.getParent();
//                if (!channel.isShared()) {
//                    mBtnShare.setVisibility(View.VISIBLE);
//                }
            }
        }

        if (mPtzLayout.getVisibility() == View.VISIBLE) {
            displayPtzLayout(false);
        }

    }

    /**
     * 显示/隐藏状态栏
     * @param isShow 是否显示状态栏
     */
    public void switchStatusBar(boolean isShow) {
        Log.e("mBackContainer0000","isShow="+isShow);
        if (isShow) {
            mBackContainer.setVisibility(View.VISIBLE);

            mLeftLayout.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.player_function_left_in);
            animation.setFillAfter(true);
            mLeftLayout.startAnimation(animation);

            mRightLayout.setVisibility(View.VISIBLE);
            Animation animation2 = AnimationUtils.loadAnimation(getContext(), R.anim.player_function_right_in);
            animation2.setFillAfter(true);
            mRightLayout.startAnimation(animation2);

            startHideBarTimer();
        } else {
            cancelHideBarTimer();

            // 对讲开启状态下，暂定不隐藏操作按钮，云台应该有同样逻辑
//            if (mBtnCall.isSelected()) {
//                return;
//            }

            mBackContainer.setVisibility(View.GONE);

            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.player_function_left_out);
            animation.setFillAfter(true);
            mLeftLayout.startAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mLeftLayout.setVisibility(View.INVISIBLE);
                    mLeftLayout.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            Animation animation2 = AnimationUtils.loadAnimation(getContext(), R.anim.player_function_right_out);
            animation2.setFillAfter(true);
            mRightLayout.startAnimation(animation2);
            animation2.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mRightLayout.setVisibility(View.GONE);
                    mRightLayout.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    /**
     * 延时自动隐藏
     */
    private void startHideBarTimer() {
        cancelHideBarTimer();
        mTimer = new Timer();
        mTimerTask = new HideNavTimerTask();
        mTimer.schedule(mTimerTask, 5000);
    }

    /**
     * 取消延时自动隐藏
     */
    private void cancelHideBarTimer() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEventBus(MsgEvent event) {

        switch (event.getMsgTag()) {
            case MsgEvent.MSG_EVENT_INTERACTION:
                try {
                    JSONObject jsonInfo = new JSONObject(event.getAttachment());
                    String type = jsonInfo.optString("type");
                    switch (type) {
                        case "switchAudio":
                            mBtnAudio.setSelected(jsonInfo.optBoolean("audioOpen"));
                            break;
                        case "refreshStream":
                            String stream = jsonInfo.optString("currentStream");
                            String hd = getResources().getStringArray(R.array.array_stream)[1];
                            if (!TextUtils.isEmpty(stream) && TextUtils.equals(stream, hd)) {
                                mBtnStream.setImageResource(R.mipmap.ic_stream_land_hd);
                            } else {
                                mBtnStream.setImageResource(R.mipmap.ic_stream_land_sd);
                            }
                            break;
                        case "favor":
                            mBtnCollect.setSelected(jsonInfo.optBoolean("favor"));
                            break;
                        case "switchCall":
                            mBtnCall.setSelected(jsonInfo.getBoolean("startCall"));
                            if (mBtnCall.isSelected()) {
//                                mLayoutCalling.setVisibility(View.VISIBLE);
                            } else {
//                                mLayoutCalling.setVisibility(View.GONE);
                                startHideBarTimer();
                            }
                            break;
                        case "switchRecord":
                            mBtnVideoRecord.setSelected(jsonInfo.getBoolean("startRecord"));
                            break;
                        case "switchPtz":
                            displayPtzLayout(mPtzLayout.getVisibility() != View.VISIBLE);
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    /**
     * 切换云台布局，显示或者隐藏
     */
    private void displayPtzLayout(boolean isVisible) {
        if(null != channel)
            channel.setPtzLayoutShow(isVisible);
        if (isVisible) {
            mPtzLayout.setVisibility(View.VISIBLE);

        } else {
            mPtzLayout.setVisibility(View.GONE);

        }
        mBtnPtz.setSelected(isVisible);
    }

    public class HideNavTimerTask extends TimerTask {
        @Override
        public void run() {
            mRootView.post(new Runnable() {
                @Override
                public void run() {
                    mActivity.hiddenBar();
                }
            });
        }
    }

}
