package com.huawei.holosens.live.play.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huawei.holobasic.utils.DateUtil;
import com.huawei.holobasic.utils.FileUtil;
import com.huawei.holosens.R;
import com.huawei.holosens.bean.Channel;
import com.huawei.holosens.consts.JVCloudConst;
import com.huawei.holosens.live.play.adapter.SelectAdapter;
import com.huawei.holosens.live.play.bean.Glass;
import com.huawei.holosens.live.play.player.IPlayHelper;
import com.huawei.holosens.live.play.ui.JVMultiPlayActivity;
import com.huawei.holosens.live.play.util.AlbumNotifyHelper;
import com.huawei.holosens.live.play.util.ToastUtilsB;
import com.huawei.holosens.utils.ToastUtils;
import com.huawei.holosens.view.ProgressBar;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import static com.huawei.holosens.consts.JVOctConst.STREAM_HD;
import static com.huawei.holosens.consts.JVOctConst.STREAM_SD;

/**
 * 多功能的播放控件
 *
 */
public class MultiFunctionBar extends RelativeLayout implements View.OnClickListener {
    private static final String TAG = "MultiFunctionBar";

    private LayoutInflater mInflater;
    private Context mContext;
    private Glass mGlass;
    private Channel mChannel;

    public int mConnectState = 0;// 字幕(连接状态)
    public View mContainer;
    public TextView mLinkState;// 连接文字
    public TextView tips;// 连接参数文字
    public ImageView mLoadingIcon;// 加载进度条
    public ImageView mRetryIcon;// 重试按钮
    private RelativeLayout mSelectListLayout;//码流切换布局
    private GridView mSelectListView;// 码流切换
    private View mLandDoubleCallTip;// 单双向对讲提示
    private ImageView mIvCalling;
    private View mRecordTimeTip;// 录像时间提示框
    private ImageView mRecordPointIcon;
    private TextView mRecordTimeTV;

    // 分屏选择
    public SelectAdapter mSelectAdapter;
    public String[] mStreamTxtArrayHor2;//流高超


    protected int mSelectedStream;// 当前选择的码流，1高清，2流畅

    public LinearLayout mTouchArea;
    private OnClickListener mClickListener;
    private boolean isLandScape;


    public MultiFunctionBar(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public MultiFunctionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public MultiFunctionBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    private void init() {
        mInflater = LayoutInflater.from(mContext);
        View root = mInflater.inflate(R.layout.view_multi_bar, null);
        mContainer = root.findViewById(R.id.rlyt_container);
        mLinkState = (TextView) root.findViewById(R.id.link_state);
        tips = (TextView) root.findViewById(R.id.tv_tips);
        mLoadingIcon = (ImageView) root.findViewById(R.id.iv_loading);
        mRetryIcon = (ImageView) root.findViewById(R.id.iv_retry);
        mTouchArea = (LinearLayout) root.findViewById(R.id.lyt_result);


        mSelectListLayout = (RelativeLayout) root.findViewById(R.id.stream_ver_layout);
        mSelectListView = root.findViewById(R.id.lv_select_stream);
        mLandDoubleCallTip = root.findViewById(R.id.flyt_land_doublecall);
        mIvCalling = root.findViewById(R.id.iv_calling);
        mRecordTimeTip = root.findViewById(R.id.lyt_record);
        mRecordPointIcon = (ImageView) root.findViewById(R.id.iv_record);
        mRecordTimeTV = (TextView) root.findViewById(R.id.tv_record_time);
        // 进度加载图标
        mLoadingIcon.setImageDrawable(new ProgressBar(mContext, mLoadingIcon));

        mContainer.setOnClickListener(this);
//        mContainer.setEnabled(false);
        mContainer.setClickable(false);

        doStreamSelectSetting();

        if (getChildCount() > 0) {
            this.removeAllViews();
        }

        addView(root);
    }

    /**
     * 设置玻璃
     *
     * @param glass
     */
    public void setGlass(Glass glass) {
        if(null != glass){
            mGlass = glass;
            mChannel = glass.getChannel();
        }
    }

    private int subStreamNum = 2;

    /**
     * 改变View的大小
     *
     * @param width
     * @param height
     */
    public void changeViewSize(int width, int height, boolean isLandScape) {
        mContainer.getLayoutParams().width = width;
        mContainer.getLayoutParams().height = height;
        this.isLandScape = isLandScape;

        // 横竖屏切换, 隐藏码流UI, 更换录像UI
        modifyStreamUIByScreenSwitch(isLandScape);
        modifyRecordUIByScreenSwitch(isLandScape);
        modifyCallUIByScreenSwitch(isLandScape);
    }

    /**
     * 改变文字的大小
     *
     * @param glassCount 窗户上的玻璃数量
     */
    public void changeTextSize(int glassCount) {
        int textSize = mContext.getResources().getDimensionPixelSize(R.dimen.text_size_12);
        if (glassCount > 4) {
            textSize = mContext.getResources().getDimensionPixelSize(R.dimen.text_size_8);
        }

        mLinkState.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        tips.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    /**
     * 设置字幕(连接状态)
     *
     * @param connectState 连接状态
     * @param resId        连接状态文字资源ID
     */
    public void setCaption(int connectState, int resId) {
        if (resId <= 0) {
            setCaption(connectState, getResources().getString(R.string.disconnected));
        } else {
            setCaption(connectState, getResources().getString(resId));
        }
    }

    /**
     * 设置字幕(连接状态)
     *
     * @param connectState 连接状态
     * @param resContent   连接状态文字
     */
    public void setCaption(int connectState, String resContent) {
        mConnectState = connectState;
        this.setBackgroundColor(getResources().getColor(R.color.transparent));
        switch (connectState) {
            case IPlayHelper.area_error: {
                mLinkState.setVisibility(View.VISIBLE);
                mLinkState.setText(getResources().getString(R.string.connect_failed_error_area));
                mLinkState.setGravity(Gravity.CENTER_HORIZONTAL);
                mLoadingIcon.setVisibility(View.GONE);// 小维logo
                mRetryIcon.setVisibility(View.GONE);// 播放按钮
                mTouchArea.setClickable(false);
                break;
            }
            case IPlayHelper.prepare: {
                mLinkState.setVisibility(View.VISIBLE);
                mLinkState.setText(getResources().getString(R.string.prepare));
                mLoadingIcon.setVisibility(View.VISIBLE);
                mRetryIcon.setVisibility(View.GONE);// 播放按钮
                mTouchArea.setClickable(false);
                break;
            }
            case IPlayHelper.connecting: {
                mLinkState.setVisibility(View.VISIBLE);
                mLinkState.setText(getResources().getString(R.string.connectting));
                mLoadingIcon.setVisibility(View.VISIBLE);
                mRetryIcon.setVisibility(View.GONE);// 播放按钮
                mTouchArea.setClickable(false);
                break;
            }
            case IPlayHelper.buffering1: {
                mLinkState.setVisibility(View.VISIBLE);
                mLinkState.setText(getResources().getString(R.string.buffering1));
                mLoadingIcon.setVisibility(View.VISIBLE);
                mRetryIcon.setVisibility(View.GONE);// 播放按钮
                mTouchArea.setClickable(false);
                break;
            }
            case IPlayHelper.buffering2: {
                mLinkState.setVisibility(View.VISIBLE);
                mLinkState.setText(getResources().getString(R.string.buffering2));
                mLoadingIcon.setVisibility(View.VISIBLE);
                mRetryIcon.setVisibility(View.GONE);// 播放按钮
                mTouchArea.setClickable(false);
                break;
            }
            case IPlayHelper.connected: {//已连接
                mLinkState.setVisibility(View.GONE);
                mLoadingIcon.setVisibility(View.GONE);
                mRetryIcon.setVisibility(View.GONE);// 播放按钮
                mTouchArea.setClickable(false);
                break;
            }
            case IPlayHelper.connectFailed: {//连接失败
//                // 调试信息
//                if (AppConsts.DEBUG_STATE) {
                mLinkState.setVisibility(View.VISIBLE);
                mLinkState.setText(resContent);
//                } else {
//                    mLinkState.setVisibility(View.GONE);
//                }
                mLoadingIcon.setVisibility(View.GONE);
                mRetryIcon.setVisibility(View.VISIBLE);// 播放按钮
                mTouchArea.setClickable(true);


                break;
            }
            case IPlayHelper.disconnected: {//已断开
//                // 调试信息
//                if (AppConsts.DEBUG_STATE) {
                mLinkState.setVisibility(View.VISIBLE);
                mLinkState.setText(resContent);
//                } else {
//                    mLinkState.setVisibility(View.GONE);
//                }
                mLoadingIcon.setVisibility(View.GONE);
                mRetryIcon.setVisibility(View.VISIBLE);// 播放按钮
                mTouchArea.setClickable(true);

                break;
            }
            case IPlayHelper.paused: {//暂停
                mLinkState.setVisibility(View.VISIBLE);
                mLinkState.setText(getResources().getString(R.string.click_to_connect));
                mLoadingIcon.setVisibility(View.GONE);
                mRetryIcon.setVisibility(View.VISIBLE);// 播放按钮
                mTouchArea.setClickable(true);
                break;
            }
            default:
                break;
        }
    }

    /**
     * 显示连接参数
     *
     * @param tip
     */
    public void showTips(String tip) {
        tips.setVisibility(View.VISIBLE);// 连接文字
        tips.setText(tip);
    }


    /**
     * 显示连接参数
     *
     * @param tip
     */
    public void updateTips(String tip) {
        tips.setVisibility(View.VISIBLE);// 连接文字
        tips.setText(tip);
    }


    public void showTips(SpannableStringBuilder tip) {
        tips.setVisibility(View.VISIBLE);// 连接文字
        tips.setText(tip);
    }

    public void hiddenTips() {
        tips.setVisibility(GONE);
    }

    // ---------------------------------------------------
    // # 码流选择配置 Start
    // ---------------------------------------------------
    /*
      码流这块原来属于底部功能区域,但是现在的效果图需要显示在播放区域
      所以播放区域和底部功能区域都需要处理码流切换的操作。
      如何判断码流正在切换?播放区域使用下面的isStreamStartToChange方法;功能区域使用
      Glass中的setStreamStartToChange方法
     */
    private boolean streamStartToChange;

    public boolean isStreamStartToChange() {
        return streamStartToChange;
    }

    public void setStreamStartToChange(boolean streamStartToChange) {
        this.streamStartToChange = streamStartToChange;
    }

    /**
     * 隐藏录像状态恢复对讲状态
     */
    public void dismissRecordingCalling() {
        Log.e("asdfdfsd","Bar-dismissRecordingCalling");
        mRecordTimeTip.setVisibility(View.GONE);
        mLandDoubleCallTip.setVisibility(View.GONE);
        AnimationDrawable bg = (AnimationDrawable) mIvCalling.getBackground();
        if (null != bg) {
            bg.stop();
        }
    }

    /**
     * 分屏选择的显示/隐藏
     */
    public void switchStreamWindow() {

//        if (isLandScape) {
//
//        } else {
            if (mSelectListLayout.getVisibility() == View.VISIBLE) {
                mSelectListLayout.setVisibility(View.GONE);
                mContainer.setClickable(false);
            } else {
                mSelectListLayout.setVisibility(View.VISIBLE);
                mContainer.setClickable(true);

                mSelectedStream = mChannel.getStreamTag();
                mSelectAdapter.setSelected(subStreamNum - mSelectedStream);
                mSelectAdapter.notifyDataSetChanged();
            }
//        }
    }

    /**
     * 分屏隐藏
     */
    public void hiddenStream() {
//        if (isLandScape) {
//
//        } else {
            if (mSelectListLayout.getVisibility() == View.VISIBLE) {
                mSelectListLayout.setVisibility(View.GONE);
                mContainer.setClickable(false);
            }
//        }
    }

    /**
     * 码流切换处理
     */
    private void doStreamSelectSetting() {
        mStreamTxtArrayHor2 = getResources().getStringArray(R.array.array_stream);

        mSelectAdapter = new SelectAdapter(mContext);
        mSelectAdapter.setData(mStreamTxtArrayHor2);

        mSelectAdapter.setSelected(-1);
        mSelectListView.setAdapter(mSelectAdapter);
        mSelectListView.setNumColumns(1);
        mSelectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int index, long arg3) {
                //切换到相同码流，不处理
                if (mSelectedStream == subStreamNum - index) {
                    mSelectListLayout.setVisibility(View.GONE);
                    mContainer.setClickable(false);
                    return;
                }
                setStreamStartToChange(true);
                mGlass.setStreamStartToChange(true);
                showStreamTips(index);

                if (mChannel.isRecording()) {
                    mChannel.setRecordChangeStream(true);
                    if (mContext instanceof JVMultiPlayActivity) {
                        ((JVMultiPlayActivity) mContext).stopRecord();
                    }
                }

//                if (mGlass.getProtocol() == GlassType.TYPE_PROTOCOL_CLOUDSEE_V2) {

                    //切换到流畅
                    if (index == 0) {
                        mChannel.setStreamTag(STREAM_SD);

                    } else {//从流畅往其他码流切换
                        mChannel.setStreamTag(STREAM_HD);
                    }
                    mGlass.getPlayHelper().switchStream();
                    setStreamStartToChange(false);
                    mSelectedStream = subStreamNum - index;
                    mSelectAdapter.setSelected(index);
                    mSelectAdapter.notifyDataSetChanged();
                    mSelectListLayout.setVisibility(View.GONE);
                    mContainer.setClickable(false);

                    showStreamTips(index);
                    Log.e("mobileQuality", "change:mSelectedStream=" + mSelectedStream + ";");

                    return;
                }

//            }
        });

    }

    /**
     * 码流切换时, 播放窗口底部信息提示
     *
     * @param index 码流的索引
     */
    public void showStreamTips(int index) {
        //如果配置信息获取不全，index=-1，会导致空指针异常
        if (index < 0) {
            return;
        }

        Resources resources = mContext.getResources();
        if (!isStreamStartToChange()) {
            ToastUtils.show(getContext(), resources.getString(R.string.changestream_success));
        }

    }

    /**
     * 更新通道的码流
     */
    public void updateChannelStream() {
        mChannel.setStreamTag(mSelectedStream);
    }

    // ---------------------------------------------------
    // # 码流选择配置 End
    // ---------------------------------------------------
    // ---------------------------------------------------
    // # 单双向对讲 Start
    // ---------------------------------------------------

    public void startDoubleCall() {
        mLandDoubleCallTip.setVisibility(View.VISIBLE);
        AnimationDrawable bg = (AnimationDrawable) mIvCalling.getBackground();
        if (null != bg) {
            bg.start();
        }
    }

    public void resetDoubleState() {
        mLandDoubleCallTip.setVisibility(View.GONE);
        AnimationDrawable bg = (AnimationDrawable) mIvCalling.getBackground();
        if (null != bg) {
            bg.start();
        }
    }

    // ---------------------------------------------------
    // # 单双向对讲 End
    // ---------------------------------------------------

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
//        if (isLandScape) {
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
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //录像中切分屏，WindowFragment重建，收不到停止播放的event，也走不到本类的stopRecord方法，在此判断停止计时
                        if (mChannel.isRecording()) {
                            updateRecordCount(mRecordedTime);
                        } else {
                            mRecordedTime = 0;
                            if (null != mRecordingTimer) {
                                mRecordingTimer.cancel();
                                mRecordingTimer = null;
                            }
                            if (null != mRecordingTask) {
                                mRecordingTask.cancel();
                                mRecordingTask = null;
                            }
                            if (!"".equalsIgnoreCase(mRecordingFileName)) {
                                FileUtil.deleteDirOrFile(new File(mRecordingFileName));
                            }
                        }
                    }
                });
            }
        };

        mRecordingTimer = new Timer();
        mRecordingTimer.schedule(mRecordingTask, 0, 1000);
        mChannel.setRecording(true);
        mChannel.setRecordTime(mRecordedTime);
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
        mChannel.setRecording(false);
        mRecordTimeTip.setVisibility(View.GONE);


        int recordTime = (int) (System.currentTimeMillis() - mRecordStartTime);
        if (needTip) {
            Log.e("mRecordStartTime", "mRecordStartTime=" + mRecordStartTime + ";recordTimeStamp=" + recordTime + ";timeSecond=" + mRecordedTime);
            if (mRecordedTime * 1000 < JVCloudConst.RECORD_VIDEO_MIN_LENGTH) {
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
        } else {
            if (mRecordedTime * 1000 < JVCloudConst.RECORD_VIDEO_MIN_LENGTH) {
//                ToastUtils.show(mContext, R.string.record_short_failed);
                ToastUtilsB.show(R.string.record_short_failed);
                if (!"".equalsIgnoreCase(mRecordingFileName)) {
                    FileUtil.deleteDirOrFile(new File(mRecordingFileName));
                }
            }
        }
        mRecordedTime = 0;
        mChannel.setRecordTime(0);
    }


    /**
     * 更新录像时间
     *
     * @param time 单位秒
     */
    protected void updateRecordCount(int time) {
        // 红色圆点每秒显示隐藏
        if (time % 2 == 1) {
            mRecordPointIcon.setVisibility(View.VISIBLE);
        } else {
            mRecordPointIcon.setVisibility(View.INVISIBLE);
        }
        mRecordTimeTV.setText(DateUtil.getHoursAndSeconds(time));
        mChannel.setRecordTime(mRecordedTime);

        if (time < 5) {
//            ToastUtils.show(getContext(), getResources().getString(R.string.record_time_tip, 5-time));
            ToastUtilsB.show(getResources().getString(R.string.record_time_tip, 5-time));
        }
    }

    // ---------------------------------------------------
    // # 录像时间展示 End
    // ---------------------------------------------------

    // ---------------------------------------------------
    // # 播放区域一些横竖屏切换效果 Start
    // ---------------------------------------------------
    /*
      ps:像码流切换、录像、双向对讲这种操作是针对设备的，但是它们的横竖屏样式
      又不一样，所以横竖屏切换时，这块的样式也要做转换
     */

    /**
     * 横竖屏切换时, 隐藏码流窗口
     * TODO: 因为横屏时码流窗口和底部功能还有个交互显示隐藏的动作
     * 所以横竖屏切换时, 只是简单的隐藏码流窗口, 其实也挺合理, 因为码流窗口类似一个悬浮窗
     * 切换就隐藏了
     *
     * @param isLandScape
     */
    private void modifyStreamUIByScreenSwitch(boolean isLandScape) {
        if (isLandScape) {

//            // 竖切横, 如果竖屏时并没有显示码流窗口, 忽略此操作
//            if (mSelectListLayout.getVisibility() != View.VISIBLE) {
//                return;
//            }
//
//            // 隐藏竖屏UI
//            mSelectListLayout.setVisibility(View.GONE);

            mSelectListLayout.setBackgroundColor(getResources().getColor(R.color.bg_stream));
            mSelectListView.setBackgroundColor(Color.TRANSPARENT);
            mSelectListView.setNumColumns(1);
            LayoutParams layoutParams = (LayoutParams) mSelectListView.getLayoutParams();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            }
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        } else {

//            // 横切竖, 如果横屏时并没有显示码流窗口, 忽略此操作
//            if (mLandSelectGridView.getVisibility() != View.VISIBLE) {
//                return;
//            }
//
//            // 隐藏横屏UI
//            mLandSelectGridView.setVisibility(View.GONE);

            mSelectListLayout.setBackgroundColor(Color.TRANSPARENT);
            mSelectListView.setBackgroundColor(getResources().getColor(R.color.bg_stream));
            mSelectListView.setNumColumns(mSelectAdapter.getCount());
            LayoutParams layoutParams = (LayoutParams) mSelectListView.getLayoutParams();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                layoutParams.removeRule(RelativeLayout.CENTER_IN_PARENT);
            }
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, TRUE);
        }
    }

    /**
     * 横竖屏切换时, 录像样式改变
     *
     * @param isLandScape
     */
    private void modifyRecordUIByScreenSwitch(boolean isLandScape) {
        if (mRecordTimeTip.getVisibility() != View.VISIBLE) {
            return;
        }

        // 调整录像时间提示距离顶部的距离
//        int marginTop;
//        if (isLandScape) {
//            marginTop = getResources().getDimensionPixelSize(R.dimen.margin_40);
//        } else {
//            marginTop = getResources().getDimensionPixelSize(R.dimen.margin_24);
//        }
//        setMargins(mRecordTimeTip, 0, marginTop, 0, 0);
    }


    /**
     * 横竖屏切换时, 对讲样式改变
     *
     * @param isLandScape
     */
    private void modifyCallUIByScreenSwitch(boolean isLandScape) {
        if (isLandScape) {
            // 竖切横, 如果不支持对讲或者是单向对讲(需要长按,不存在横竖屏切换的问题), 忽略
            if (mChannel == null) {
                return;
            }

            if (!mChannel.isSupportCall() && !mChannel.isSupportNvrCall()) {
                return;
            }

            // 双向对讲, 并且正在对讲, 显示横屏UI
            if (mChannel.isVoiceCall()) {
                startDoubleCall();
            }
        } else {
            // 横切竖
            if (mLandDoubleCallTip.getVisibility() != View.VISIBLE) {
                return;
            }
            resetDoubleState();
        }
    }
    // ---------------------------------------------------
    // # 播放区域一些横竖屏切换效果 End
    // ---------------------------------------------------


    /**
     * 给CaptionView中的元素设置点击监听
     *
     * @param clickListener
     */
    public void setListener(OnClickListener clickListener) {
        mClickListener = clickListener;
        mTouchArea.setOnClickListener(mClickListener);
    }

    private final Handler mHandler = new Handler();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlyt_container: //拦截点击事件，隐藏码流切换布局 todo
                if (mSelectListLayout.getVisibility() == View.VISIBLE) {
                    mSelectListLayout.setVisibility(View.GONE);
                    mContainer.setClickable(false);
                }

//                mContainer.setEnabled(false);
                mContainer.setClickable(false);
                break;
            default:
                break;
        }
    }
}