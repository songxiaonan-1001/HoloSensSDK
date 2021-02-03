package com.huawei.holosens.live.play.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.huawei.holosens.R;
import com.huawei.holosens.bean.Channel;
import com.huawei.holosens.bean.Device;
import com.huawei.holosens.consts.JVOctConst;
import com.huawei.holosens.live.play.bean.Glass;
import com.huawei.holosens.live.play.bean.PlayFunctionItem;
import com.huawei.holosens.live.play.event.MsgEvent;
import com.huawei.holosens.live.play.player.BasePlayHelper;
import com.huawei.holosens.live.play.util.MyGestureDispatcher;
import com.huawei.holosens.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 竖屏模式下播放区域底部功能栏
 *
 */
@SuppressWarnings("NullableProblems")
public class NavPortraitFragment extends SimpleFragment implements View.OnClickListener, View.OnTouchListener {

    private static final String TAG = "NavPortraitFragment";

    private static final String SPAN_COUNT = "spanCount";
    private static final String GLASS_NO = "selectedGlassNo";

    private View mRootView;
    private ImageButton mAudioBtn, mStreamTxt, mMultiScreenBtn, mFullScreen;

    private RecyclerView mRecyclerView;
    private BaseQuickAdapter<PlayFunctionItem, BaseViewHolder> mAdapter;
    @SuppressWarnings("unchecked")
    private ArrayList<PlayFunctionItem> mItems = new ArrayList();
    private View mCallLayout;
    //    private FrameLayout mLayoutCalling;
    private ImageView mIvCalling;
    private ImageView mBtnCall;
    private View mPtzLayout;

    private ImageView mPtzCenterImg, mPtzImg;

    // 分屏数量
    private int mSpanCount = 1;
    private int mShowSpanCount = 1;
    // 当前选中的玻璃
    private int mGlassNo;
    private Glass mGlass;
    private Channel mChannel;
    private Device mDevice;
    private BasePlayHelper mPlayHelper;

    private String[] mStreamTypeResArrayVer2;

    private boolean mIsEdit;//是否是编辑状态


    public static NavPortraitFragment newInstance(Bundle bundle) {
        NavPortraitFragment fragment = new NavPortraitFragment();
        Bundle args = new Bundle();
        args.putInt(SPAN_COUNT, bundle.getInt("spanCount"));
        args.putInt(GLASS_NO, bundle.getInt("selectedGlassNo"));
        args.putInt("showSpanCount", bundle.getInt("showSpanCount"));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            mSpanCount = getArguments().getInt(SPAN_COUNT);
            mShowSpanCount = getArguments().getInt("showSpanCount");
            mGlassNo = getArguments().getInt(GLASS_NO);
        }
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == mRootView) {
            mRootView = inflater.inflate(R.layout.fragment_play_nav_portrait, container, false);

            mAudioBtn = mRootView.findViewById(R.id.btn_portrait_audio);
            mStreamTxt = mRootView.findViewById(R.id.btn_portrait_stream);
            mMultiScreenBtn = mRootView.findViewById(R.id.btn_portrait_multi_screen);
            mFullScreen = mRootView.findViewById(R.id.btn_fullscreen);
            mAudioBtn.setOnClickListener(mActivity);
            mStreamTxt.setOnClickListener(mActivity);
            mMultiScreenBtn.setOnClickListener(mActivity);
            mFullScreen.setOnClickListener(mActivity);

            mStreamTypeResArrayVer2 = getResources().getStringArray(R.array.array_stream);

            mRecyclerView = mRootView.findViewById(R.id.rv_function);
            mCallLayout = mRootView.findViewById(R.id.layout_portrait_call);
//            mLayoutCalling = mRootView.findViewById(R.id.layout_calling);
            mIvCalling = mRootView.findViewById(R.id.iv_calling);
            AnimationDrawable bg = (AnimationDrawable) mIvCalling.getBackground();
            if (null != bg) {
                bg.start();
            }
            mBtnCall = mRootView.findViewById(R.id.btn_portrait_call);
            mPtzCenterImg = mRootView.findViewById(R.id.ptz_center_img);
            mPtzImg = mRootView.findViewById(R.id.ptz_img);

            mBtnCall.setOnClickListener(mActivity);
            mRootView.findViewById(R.id.btn_close_call).setOnClickListener(mActivity);
            mPtzLayout = mRootView.findViewById(R.id.layout_portrait_ptz);
            mRootView.findViewById(R.id.btn_close_ptz).setOnClickListener(mActivity);
            mPtzCenterImg.setOnTouchListener(this);

            PTZLongClickListener mListener = new PTZLongClickListener();
            mRootView.findViewById(R.id.bbd_img).setOnTouchListener(mListener);
            mRootView.findViewById(R.id.bbx_img).setOnTouchListener(mListener);
            mRootView.findViewById(R.id.bjd_img).setOnTouchListener(mListener);
            mRootView.findViewById(R.id.bjx_img).setOnTouchListener(mListener);
            mRootView.findViewById(R.id.gqd_img).setOnTouchListener(mListener);
            mRootView.findViewById(R.id.gqx_img).setOnTouchListener(mListener);

            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
            Integer[] mIcons = new Integer[]{R.drawable.selector_play_portrait_snap,
                    R.drawable.selector_play_portrait_record, R.drawable.selector_play_portrait_playback_local, R.drawable.selector_play_portrait_playback_cloud};
            for (int i = 0; i < mIcons.length; i++) {
                PlayFunctionItem item = new PlayFunctionItem();
                item.setResId(mIcons[i]);
                mItems.add(item);
            }
            mAdapter = new BaseQuickAdapter<PlayFunctionItem, BaseViewHolder>(R.layout.item_play_function, mItems) {
                @Override
                protected void convert(BaseViewHolder baseViewHolder, PlayFunctionItem item) {
                    baseViewHolder.setImageResource(R.id.iv_image, item.getResId());
                    baseViewHolder.getView(R.id.iv_image).setSelected(item.isSelected());
                }
            };
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    switch (position) {
                        case 0://抓拍
                            view.setId(R.id.btn_land_snap);
                            mActivity.onClick(view);
                            break;
                        case 1://录像
                            view.setId(R.id.btn_land_video);
                            mActivity.onClick(view);
                            break;
                        case 2://本地回放
                            view.setId(R.id.btn_land_playback);
                            mActivity.onClick(view);
                            break;
                        case 3: //cloud回放
                            view.setId(R.id.btn_land_playback_cloud);
                            mActivity.onClick(view);
                            break;
                    }
                }
            });

        }
        return mRootView;
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
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
                            mAudioBtn.setSelected(jsonInfo.optBoolean("audioOpen"));
                            break;
                        case "refreshStream":
                            String stream = jsonInfo.optString("currentStream");
                            if (!TextUtils.isEmpty(stream) && TextUtils.equals(stream, mStreamTypeResArrayVer2[1])) {
                                if (mIsEdit) {
                                    mStreamTxt.setImageResource(R.mipmap.ic_stream_hd_disable);
                                } else {
                                    mStreamTxt.setImageResource(R.mipmap.ic_stream_hd);
                                }
                            } else {
                                if (mIsEdit) {
                                    mStreamTxt.setImageResource(R.mipmap.ic_stream_sd_disable);
                                } else {
                                    mStreamTxt.setImageResource(R.mipmap.ic_stream_sd);
                                }
                            }
                            break;

                        case "switchCall":
                            displayCallLayout(jsonInfo.getBoolean("startCall"));
//                            mBtnCall.setSelected(jsonInfo.getBoolean("startCall"));
//                            if (mBtnCall.isSelected()) {
//                                mLayoutCalling.setVisibility(View.VISIBLE);
//                            } else {
//                                mLayoutCalling.setVisibility(View.GONE);
//                            }
                            break;
                        case "switchPtz":
                            displayPtzLayout(mPtzLayout.getVisibility() != View.VISIBLE);
                            break;
                        case "switchRecord":
                            mItems.get(1).setSelected(jsonInfo.getBoolean("startRecord"));
                            mAdapter.notifyItemChanged(1);
                            break;

                        case "favor":
                            mItems.get(4).setSelected(jsonInfo.optBoolean("favor"));
                            mAdapter.notifyItemChanged(4);
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    public void setEdit(boolean isEdit) {
        this.mIsEdit = isEdit;

        Bundle bundle = new Bundle();
        bundle.putInt(SPAN_COUNT, mSpanCount);
        bundle.putInt("showSpanCount", mShowSpanCount);
        bundle.putInt(GLASS_NO, mGlassNo);
        updateAfterSpan(bundle);
    }

    public boolean isEdit() {
        return mIsEdit;
    }

    /**
     * 分屏数量变化后, 进行信息更新
     *
     * @param bundle 更新的参数信息(一扇窗户上的玻璃数量、窗户号)
     */
    public void updateAfterSpan(Bundle bundle) {
        mSpanCount = bundle.getInt("spanCount");
        mShowSpanCount = bundle.getInt("showSpanCount");
        mGlassNo = bundle.getInt("selectedGlassNo");

        mGlass = mActivity.getGlassByNo(mGlassNo);
        if (null != mGlass) {
            mChannel = mGlass.getChannel();
        } else {
            mChannel = null;
        }
        if (null != mChannel) {
            mChannel.setStreamTag(JVOctConst.STREAM_SD);//默认连接的码流

            mDevice = mChannel.getParent();
        } else {
            mDevice = null;
        }

        mAudioBtn.setEnabled(!mIsEdit);
        mStreamTxt.setEnabled(!mIsEdit);
        if (mIsEdit) {
            mStreamTxt.setImageResource(R.mipmap.ic_stream_sd_disable);
        } else {
            mStreamTxt.setImageResource(R.mipmap.ic_stream_sd);
        }
        mMultiScreenBtn.setEnabled(!mIsEdit);
        if (mShowSpanCount == 4) {
            if (mIsEdit) {
                mMultiScreenBtn.setImageResource(R.mipmap.ic_multi_screen_4_disable);
            } else {
                mMultiScreenBtn.setImageResource(R.mipmap.ic_multi_screen_4);
            }
        } else if (mShowSpanCount == 9) {
            if (mIsEdit) {
                mMultiScreenBtn.setImageResource(R.mipmap.ic_multi_screen_9_disable);
            } else {
                mMultiScreenBtn.setImageResource(R.mipmap.ic_multi_screen_9);
            }
        } else if (mShowSpanCount == 16) {
            if (mIsEdit) {
                mMultiScreenBtn.setImageResource(R.mipmap.ic_multi_screen_16_disable);
            } else {
                mMultiScreenBtn.setImageResource(R.mipmap.ic_multi_screen_16);
            }
        } else {
            if (mIsEdit) {
                mMultiScreenBtn.setImageResource(R.mipmap.ic_multi_screen_1_disable);
            } else {
                mMultiScreenBtn.setImageResource(R.mipmap.ic_multi_screen_1);
            }
        }
        mFullScreen.setEnabled(!mIsEdit);
        if (mIsEdit) {
            mFullScreen.setImageResource(R.mipmap.ic_play_fullscreen_disable);
        } else {
            mFullScreen.setImageResource(R.mipmap.ic_play_fullscreen);
        }
        for (int i = 0; i < mItems.size(); i++) {
            if (i == 2) {
                if (null != mChannel) {
                    mItems.get(i).setDisabled(mIsEdit || !mChannel.isSupportCall() && !mChannel.isSupportNvrCall());
                } else {
                    mItems.get(i).setDisabled(mIsEdit);
                }
            } else if (i == 3) {
                if (null != mChannel) {
                    mItems.get(i).setDisabled(mIsEdit || !mChannel.isSupportPtz());
                } else {
                    mItems.get(i).setDisabled(mIsEdit);
                }
            } else if (i == 4) {
//                if (null != mChannel && null != mDevice) {
//                    String enterpriseId = MySharedPreference.getString(MySharedPreferenceKey.LoginKey.CURRENT_ENTERPRISE);
//                    final int position = i;
//                    if (mChannel.getParent().isGB28181Device()) {
//                        FavorUtil.getInstance().checkFavStatus(enterpriseId, mChannel.getParent().getSn(), mChannel.getIpc_device_channel_id(), new FavorUtil.FavListener() {
//                            @Override
//                            public void unfav() {
//                                mItems.get(position).setSelected(false);
//                                mAdapter.notifyDataSetChanged();
//                            }
//
//                            @Override
//                            public void fav() {
//                                mItems.get(position).setSelected(true);
//                                mAdapter.notifyDataSetChanged();
//                            }
//                        });
//
//                    } else {
//                        FavorUtil.getInstance().checkFavStatus(enterpriseId, mChannel.getParent().getSn(), mChannel.getChannel() + "", new FavorUtil.FavListener() {
//                            @Override
//                            public void unfav() {
//                                mItems.get(position).setSelected(false);
//                                mAdapter.notifyDataSetChanged();
//                            }
//
//                            @Override
//                            public void fav() {
//                                mItems.get(position).setSelected(true);
//                                mAdapter.notifyDataSetChanged();
//                            }
//                        });
//
//                    }
//                } else {
//                    mItems.get(i).setSelected(false);
//                }
//                mItems.get(i).setDisabled(mIsEdit);
            } else {
                mItems.get(i).setSelected(false);
                mItems.get(i).setDisabled(mIsEdit);
            }
        }
        mAdapter.notifyDataSetChanged();
        if (mCallLayout.getVisibility() == View.VISIBLE) {
            displayCallLayout(false);
        }
        if (mPtzLayout.getVisibility() == View.VISIBLE) {
            displayPtzLayout(false);
        }

    }

    /**
     * 视频是否连接成功(I帧过来, 出图)
     */
    private boolean isIFrameOk() {
        if (null == mGlass) {
            return false;
        }
        // 是否出图
        mPlayHelper = (BasePlayHelper) mGlass.getPlayHelper();

        if (null == mPlayHelper) {
            return false;
        }
        boolean isIFrameOk = mPlayHelper.isIFrameOk();
        if (!isIFrameOk) {
            ToastUtils.show(mActivity, R.string.wait_connect);
            return false;
        }
        return true;
    }

    private long CLICK_INTERVAL = 300;
    private long lastClickTime;
    private int lastClickId;

    @Override
    public void onClick(View v) {
        //防止重复点击
        if (v.getId() == lastClickId) {
            if (System.currentTimeMillis() - lastClickTime < CLICK_INTERVAL) {
                return;
            }
        }
        lastClickId = v.getId();
        lastClickTime = System.currentTimeMillis();

        switch (v.getId()) {

            case R.id.bbd_img:
            case R.id.bbx_img:
            case R.id.bjd_img:
            case R.id.bjx_img:
                break;
            default:
                break;
        }
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
        Log.d(TAG, "onTouch: xsss= " + x + "y=" + y);
        if (view.getId() == R.id.ptz_center_img) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_UP:
                    FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams) view.getLayoutParams();
                    params1.leftMargin = 0;
                    params1.topMargin = 0;
                    view.setLayoutParams(params1);
                    mPtzImg.setImageResource(R.mipmap.device_live_ptz_bg);
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
                    Log.e(TAG, "move xD = " + xDistance + ", yD = " + yDistance);
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

                    mPtzImg.setImageResource(R.mipmap.device_live_ptz_left);
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

                    mPtzImg.setImageResource(R.mipmap.device_live_ptz_right);
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

                    mPtzImg.setImageResource(R.mipmap.device_live_ptz_up);
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

                    mPtzImg.setImageResource(R.mipmap.device_live_ptz_down);
                }
            }
        }
        if (mLeft == 0 && mUp == 0 && mZoom == 0)
            return;
        mActivity.ptzMoveStart(mLeft, mUp, mZoom);
    }

    /**
     * 长按--云台事件
     */
    private class PTZLongClickListener implements View.OnTouchListener {

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.e(TAG, "onTouch: " + event.getAction() );
            int action = event.getAction();
            int direction = 0;
            boolean isDirection = false;
            int mLeft = 0;
            int mUp = 0;
            int mZoom = 0;
            boolean isFi = false;
            int mFi = 0;
            int mIris = 0;
            boolean autoFlag = false;
            int i = v.getId();
//            if(i == R.id.up_img){
//                showDirection(0, -185, false);
//                isDirection = true;
//            }
//            if(i == R.id.down_img){
//                showDirection(0, 185, false);
//                isDirection = true;
//            }
//            if(i == R.id.left_img){
//                showDirection(-185, 0, false);
//                isDirection = true;
//            }
//            if(i == R.id.right_img){
//                showDirection(185, 0, false);
//                isDirection = true;
//            }
//
//            if (i == R.id.auto_img) {
//                autoFlag = true;
//                direction = MyGestureDispatcher.GESTURE_TO_CENTER;
//                if (action == MotionEvent.ACTION_DOWN) {
//                }
//
//            } else
            if (i == R.id.bbd_img) {
                direction = MyGestureDispatcher.GESTURE_TO_CENTER;
                mZoom = mCurrentPTZSpeed;
            } else if (i == R.id.bbx_img) {
                direction = MyGestureDispatcher.GESTURE_TO_CENTER;
                mZoom = -mCurrentPTZSpeed;
            } else if (i == R.id.bjd_img) {
                direction = MyGestureDispatcher.GESTURE_TO_CENTER;
                isFi = true;
                mFi = mCurrentPTZSpeed;
            } else if (i == R.id.bjx_img) {
                direction = MyGestureDispatcher.GESTURE_TO_CENTER;
                isFi = true;
                mFi = -mCurrentPTZSpeed;
            } else if (i == R.id.gqd_img) {
                direction = MyGestureDispatcher.GESTURE_TO_CENTER;
                isFi = true;
                mIris = mCurrentPTZSpeed;
            } else if (i == R.id.gqx_img) {
                direction = MyGestureDispatcher.GESTURE_TO_CENTER;
                isFi = true;
                mIris = -mCurrentPTZSpeed;
            }
            try {
                boolean show = false;
                if (action == MotionEvent.ACTION_DOWN) {
                    show = true;
                    switch (direction) {
                        case MyGestureDispatcher.GESTURE_TO_CENTER: {

                            break;
                        }
                    }
                    if (!autoFlag) {
                        if (!isFi)
                            mActivity.ptzMoveStart(mLeft, mUp, mZoom);
                        else
                            mActivity.ptzFiStart(mFi, mIris);
                    }
                } else if (action == MotionEvent.ACTION_UP) {
                    show = false;
                    switch (direction) {
                        case MyGestureDispatcher.GESTURE_TO_CENTER: {
                            break;
                        }
                    }
                    if (!autoFlag) {
                        if (!isFi) {
                            mActivity.ptzMoveStop();
                            mPtzImg.setImageResource(R.mipmap.device_live_ptz_bg);
                        } else
                            mActivity.ptzFiStop();
                    }
                    if (isDirection) {
                        mActivity.ptzMoveStop();
                        mPtzImg.setImageResource(R.mipmap.device_live_ptz_bg);
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

    }


    /**
     * 切换语音布局，显示或者隐藏
     */
    private void displayCallLayout(boolean isVisible) {
        if (isVisible) {
            mCallLayout.setVisibility(View.VISIBLE);
            AnimationDrawable bg = (AnimationDrawable) mIvCalling.getBackground();
            if (null != bg) {
                bg.start();
            }
//            Animation rightInAnim = AnimationUtils.loadAnimation(getContext(), R.anim.player_function_right_in);
//            mCallLayout.startAnimation(rightInAnim);
//
//            Animation leftOutAnim = AnimationUtils.loadAnimation(getContext(), R.anim.player_function_left_out);
//            leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
                    mRecyclerView.setVisibility(View.GONE);
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//
//                }
//            });
//            mRecyclerView.startAnimation(leftOutAnim);

        } else {
            if (mPtzLayout.getVisibility() != View.VISIBLE) {
                mRecyclerView.setVisibility(View.VISIBLE);
            }
//            Animation leftInAnim = AnimationUtils.loadAnimation(getContext(), R.anim.player_function_left_in);
//            mRecyclerView.startAnimation(leftInAnim);
//
//            Animation rightOutAnim = AnimationUtils.loadAnimation(getContext(), R.anim.player_function_right_out);
//            rightOutAnim.setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
                    mCallLayout.setVisibility(View.GONE);
                    AnimationDrawable bg = (AnimationDrawable) mIvCalling.getBackground();
                    if (null != bg) {
                        bg.stop();
                    }
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//
//                }
//            });
//            mCallLayout.startAnimation(rightOutAnim);

        }
    }

    /**
     * 切换云台布局，显示或者隐藏
     */
    private void displayPtzLayout(boolean isVisible) {
        if(null != mChannel)
            mChannel.setPtzLayoutShow(isVisible);
        if (isVisible) {
            mPtzLayout.setVisibility(View.VISIBLE);
//            Animation rightInAnim = AnimationUtils.loadAnimation(getContext(), R.anim.player_function_right_in);
//            mPtzLayout.startAnimation(rightInAnim);
//
//            Animation leftOutAnim = AnimationUtils.loadAnimation(getContext(), R.anim.player_function_left_out);
//            leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
                    mRecyclerView.setVisibility(View.GONE);
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//
//                }
//            });
//            mRecyclerView.startAnimation(leftOutAnim);

        } else {
            if (mCallLayout.getVisibility() != View.VISIBLE) {
                mRecyclerView.setVisibility(View.VISIBLE);
            }
//            Animation leftInAnim = AnimationUtils.loadAnimation(getContext(), R.anim.player_function_left_in);
//            mRecyclerView.startAnimation(leftInAnim);
//
//            Animation rightOutAnim = AnimationUtils.loadAnimation(getContext(), R.anim.player_function_right_out);
//            rightOutAnim.setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
                    mPtzLayout.setVisibility(View.GONE);
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//
//                }
//            });
//            mPtzLayout.startAnimation(rightOutAnim);

            if (null != mChannel) {
//                BaseRequestParam param = new BaseRequestParam();
//                LinkedHashMap<String, Object> data = new LinkedHashMap<>();
//                data.put("device_id", mChannel.getParent().getSn());
//                data.put("channel_id", mChannel.getChannel());
//                param.putAll(data);
//                HashMap<String, Object> header = HeaderUtil.createHeader(MySharedPreference.getString(MySharedPreferenceKey.LoginKey.TOKEN));
//                param.putAllHeader(header);
//                AppImpl.getInstance(getActivity()).releasePtzControlToken(param).subscribe(new Action1<ResponseData<bean>>() {
//                    @Override
//                    public void call(ResponseData<bean> responseData) {
//                        if (responseData.getCode() == ResponseCode.SUCCESS) {
//                        } else if (ErrorUtil.CheckError(responseData.getCode())) {
//                            ToastUtils.show(mActivity, ErrorUtil.getInstance().getErrorValue(responseData.getCode()));
//                        }
//                    }
//                });
            }
        }
    }

}
