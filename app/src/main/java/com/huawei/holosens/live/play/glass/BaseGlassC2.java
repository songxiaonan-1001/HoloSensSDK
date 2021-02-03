package com.huawei.holosens.live.play.glass;

import android.graphics.Point;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.huawei.holobasic.consts.AppConsts;
import com.huawei.holosens.R;
import com.huawei.holosens.bean.Channel;
import com.huawei.holosens.bean.Device;
import com.huawei.holosens.live.play.bean.Glass;
import com.huawei.holosens.live.play.event.MsgEvent;
import com.huawei.holosens.live.play.player.BasePlayHelper;
import com.huawei.holosens.live.play.player.C2PlayHelper;
import com.huawei.holosens.live.play.ui.JVMultiPlayActivity;
import com.huawei.holosens.live.play.ui.PlayFragment;
import com.huawei.holosens.live.play.ui.WindowFragment;
import com.huawei.holosens.live.play.util.MyGestureDispatcher;
import com.huawei.holosens.live.play.view.MultiFunctionBar;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 针对C2类型的玻璃的基类(云视通协议2.0)
 *
 */

public class BaseGlassC2 extends BasePlayGlass implements BasePlayHelper.OnStateChangeListener, View
        .OnClickListener {
    private static final String TAG = "BaseGlassC2";

    protected JVMultiPlayActivity mActivity;
    protected PlayFragment mPlayFragment;
    protected WindowFragment mWindow;
    protected ViewGroup mGlassContainer;
    protected SurfaceView mGlassSurfaceView;
    protected CheckBox mCheckBox;
    protected Glass mGlass;
    protected Channel mChannel;
    protected Device mDevice;
    protected int mSelectedGlassNo;
    protected C2PlayHelper mPlayerHelper;
    // 玻璃尺寸
    protected Glass.Size mGlassSize;
    // 连接状态信息数组
    protected String[] mConnectStateArray;
    // 覆盖在视频上面的多用途工具栏
    protected MultiFunctionBar multiFunctionBar;
    // 边框内填充
    private int mSelectedDeviceBorderPadding, mDeviceBorderPadding;


    /**
     * @param window          窗户
     * @param glassView       玻璃
     * @param glassSize       玻璃尺寸
     * @param selectedGlassNo 当前选中的玻璃号
     * @param visibleToUser   用户是否可以看到当前的玻璃
     */
    public BaseGlassC2(WindowFragment window, View glassView, Glass.Size glassSize, int
            selectedGlassNo, boolean visibleToUser, boolean isEdit) {
        super(glassView);
        mWindow = window;
        if (mWindow.getParentFragment() instanceof PlayFragment) {
            mPlayFragment = (PlayFragment) mWindow.getParentFragment();
        }
        mActivity = window.getPlayActivity();
        mSelectedGlassNo = selectedGlassNo;
        isVisibleToUser = visibleToUser;
        mGlassContainer = glassView.findViewById(R.id.flyt_glass);
        mGlassContainer.setOnClickListener(this);
        mGlassSurfaceView = glassView.findViewById(R.id.sv_glass);
        mCheckBox = glassView.findViewById(R.id.checkbox);
        mCheckBox.setOnClickListener(this);
        if (isEdit) {
            mCheckBox.setVisibility(View.VISIBLE);
            if (null != mGlass) {
                mCheckBox.setChecked(mGlass.isChecked());
            }
        } else {
            mCheckBox.setVisibility(View.GONE);
        }
        // 设置玻璃的尺寸
        mGlassSize = glassSize;
        mGlassContainer.getLayoutParams().width = glassSize.width;
        mGlassContainer.getLayoutParams().height = glassSize.height;
        mConnectStateArray = glassView.getContext().getResources().getStringArray(R.array.c2_connect_state);
        // 覆盖在视频上面的多用途工具栏
        multiFunctionBar = glassView.findViewById(R.id.caption);
        multiFunctionBar.changeViewSize(glassSize.width, glassSize.height, mActivity.isLandScape());
        // 调整字幕中文字大小
        multiFunctionBar.changeTextSize(mWindow.getGlassCount());
        multiFunctionBar.setListener(this);
        // 边框内填充
        mSelectedDeviceBorderPadding = mActivity.getResources().getDimensionPixelSize(R.dimen.selected_dev_divide_line);
        mDeviceBorderPadding = mActivity.getResources().getDimensionPixelSize(R.dimen.normal_dev_divide_line);
    }

    protected void setPlaySize() {
        if (mActivity.getSpanCount() == 1 && null != mChannel) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mGlassSurfaceView.getLayoutParams();
            if (mChannel.getWidth() == 0 || mChannel.getHeight() == 0) {
                layoutParams.width = mGlassSize.width;
                layoutParams.height = mGlassSize.height;
            } else {
                float playWidth, playHeight;
                //左右留边
                if ((float) mGlassSize.width / mGlassSize.height
                        > (float) mChannel.getWidth() / mChannel.getHeight()) {
                    playHeight = mGlassSize.height;
                    playWidth = (float) mGlassSize.height / mChannel.getHeight() * mChannel.getWidth();
                } else {//上下留边
                    playWidth = mGlassSize.width;
                    playHeight = (float) mGlassSize.width / mChannel.getWidth() * mChannel.getHeight();
                }
                layoutParams.width = (int) playWidth;
                layoutParams.height = (int) playHeight;
            }
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mGlassSurfaceView.setLayoutParams(layoutParams);
                    mChannel.setLastPortWidth(layoutParams.width);
                    mChannel.setLastPortHeight(layoutParams.height);
                }
            });
        }
    }

    /**
     * 将数据绑定到玻璃上
     *
     * @param glass
     */
    public void bindGlass(Glass glass) {
        mGlass = glass;
        mChannel = mGlass.getChannel();
        mDevice = mChannel.getParent();
        mPlayerHelper = (C2PlayHelper) mGlass.getPlayHelper();
        // 调试信息
        if (AppConsts.DEBUG_STATE) {
            StringBuffer sb = new StringBuffer();
            sb.append("C2 玻璃" + mGlass.getNo());
            sb.append("#");
            sb.append("通道" + mChannel.getChannel());
//            sb.append("#" + mDevice.getDeviceType());
//            sb.append("#" + mDevice.getFullNo());
            sb.append("#" + mDevice.getIp());
            sb.append("#" + mDevice.getPort());
//            sb.append("#" + mDevice.getUser());
//            sb.append("#" + mDevice.getPwd());
//            sb.append("#" + mChannel.getVideoEncType());
            // 设置连接状态
            multiFunctionBar.showTips(sb.toString());
        }
        // 设置玻璃
        multiFunctionBar.setGlass(mGlass);

        // 单屏时不标记边框颜色
        if (mWindow.getGlassCount() != 1) {
            // 判断是否为当前选中的玻璃
            if (mSelectedGlassNo == mGlass.getNo()) {
                mGlassContainer.setPadding(mSelectedDeviceBorderPadding, mSelectedDeviceBorderPadding,
                        mSelectedDeviceBorderPadding, mSelectedDeviceBorderPadding);
                mGlassContainer.setBackgroundResource(R.drawable.border_glass_pressed);
            } else {
                mGlassContainer.setPadding(mDeviceBorderPadding, mDeviceBorderPadding, mDeviceBorderPadding,
                        mDeviceBorderPadding);
                mGlassContainer.setBackgroundResource(R.drawable.border_glass_normal);
            }
        } else {
            mGlassContainer.setPadding(0, 0, 0, 0);
        }

        initPlayerHelper(mGlassSurfaceView);
    }

    /**
     * 初始化播放助手
     *
     * @param surfaceView 画面
     */
    private void initPlayerHelper(SurfaceView surfaceView) {
        if (mPlayerHelper == null) {
            mPlayerHelper = new C2PlayHelper(surfaceView, mGlass, mWindow, this);
        } else {
            mPlayerHelper.initSurfaceViewAndListener(surfaceView, this);
        }

        // 玻璃关联播放助手
        mGlass.setPlayHelper(mPlayerHelper);
    }

    @Override
    public void connect() {
        mPlayerHelper.connect(false);
    }

    @Override
    public void resume() {
        mPlayerHelper.resume();
    }

    @Override
    public void pause(boolean isGB28182) {
        if (isGB28182) {
            mPlayerHelper.disconnect();
        } else {
//            mPlayerHelper.pause();
            mPlayerHelper.disconnect();
        }

    }

    @Override
    public void disconnect() {
        mPlayerHelper.disconnect();
    }

    @Override
    public void refresh(int state, Object obj) {
        if (obj instanceof Integer) {// 资源ID
            multiFunctionBar.setCaption(state, (Integer) obj);
        } else {// 资源内容
            multiFunctionBar.setCaption(state, (String) obj);
        }
    }

    @Override
    public void changeBorderColor(int selectedGlassNo) {
        mSelectedGlassNo = selectedGlassNo;
        // 单屏时不标记边框颜色
        if (mWindow.getGlassCount() != 1) {
            // 判断是否为当前选中的玻璃
            if (mSelectedGlassNo == mGlass.getNo()) {
                mGlassContainer.setPadding(mSelectedDeviceBorderPadding, mSelectedDeviceBorderPadding,
                        mSelectedDeviceBorderPadding, mSelectedDeviceBorderPadding);
                mGlassContainer.setBackgroundResource(R.drawable.border_glass_pressed);
            } else {
                mGlassContainer.setPadding(mDeviceBorderPadding, mDeviceBorderPadding, mDeviceBorderPadding,
                        mDeviceBorderPadding);
                mGlassContainer.setBackgroundResource(R.drawable.border_glass_normal);
            }
        } else {
            if (mChannel.isRecording()) {
                mGlassContainer.setPadding(mSelectedDeviceBorderPadding, mSelectedDeviceBorderPadding,
                        mSelectedDeviceBorderPadding, mSelectedDeviceBorderPadding);
                mGlassContainer.setBackgroundResource(R.drawable.border_glass_record);
            } else {
                mGlassContainer.setPadding(0, 0, 0, 0);
            }
        }
    }

    @Override
    public void changeGlassSize(Glass.Size glassSize) {
        mGlassSize = glassSize;
        Log.e("FunctionUtil", "setPlaySize:0 " + mGlassSize.width + ", " + mGlassSize.height);
        mGlassContainer.getLayoutParams().width = glassSize.width;
        mGlassContainer.getLayoutParams().height = glassSize.height;
        setPlaySize();
        multiFunctionBar.changeViewSize(glassSize.width, glassSize.height, mActivity.isLandScape());
//        mPlayerHelper.changeSize(glassSize);//setPlaySize()中调用重复了
    }

    // --------------------------------------------------
    // # OnStateChangeListener监听回调
    // --------------------------------------------------
    @Override
    public void onSurfaceCreated() {
    }

    @Override
    public void onUpdate(int state, Object obj) {
        update(state, obj);
    }

    @Override
    public void onGesture(int glassNo, int gesture, int distance, Point vector, Point middle) {
        if (null != mPlayFragment && mActivity.isLandScape()) {
            //响应一个手势先禁用vp的滑动
            mPlayFragment.setDisableSliding(true);
        }
        MsgEvent event;
        JSONObject item;

        // 是否为原始尺寸
        boolean originSize = false;
        if (mPlayerHelper.isConnected()) {
            if (mChannel.getLastPortWidth() == mGlassSize.width) {
                originSize = true;
            }
        }
        int glassWidth = mGlassSurfaceView.getWidth();
        int glassHeight = mGlassSurfaceView.getHeight();
        int left = mChannel.getLastPortLeft();
        int bottom = mChannel.getLastPortBottom();
        int width = mChannel.getLastPortWidth();
        int height = mChannel.getLastPortHeight();

        boolean needRedraw = false;
        boolean leftRightDrag = false;

        switch (gesture) {
            case MyGestureDispatcher.CLICK_EVENT:// 单击
                if (mActivity.isLandScape()) {
                    /*
                     * 横屏时处理顶部和底部的工具栏显示/隐藏
                     * 顶部工具栏通过点击实现,底部的工具栏需要发送通知来实现
                     */
                    event = new MsgEvent();
                    event.setMsgTag(MsgEvent.MSG_EVENT_TOP_BOTTOM_NAV);
                    EventBus.getDefault().post(event);
                }

                if (mWindow.isEdit()) {
                    mCheckBox.setChecked(!mCheckBox.isChecked());
                    mGlass.setChecked(mCheckBox.isChecked());

                    event = new MsgEvent();
                    event.setMsgTag(MsgEvent.MSG_EVENT_GLASS_CHECK);
                    EventBus.getDefault().postSticky(event);
                    return;
                }

                /*
                  单屏时不处理单击选择事件
                  单屏滑屏的时候会处理选择事件
                 */
                if (mWindow.getGlassCount() == 1) {
                    return;
                }
                //点击的是已被选中的窗口，不处理
                if (mActivity.getSelectedGlassNo() == glassNo) {
                    return;
                }

                event = new MsgEvent();
                event.setMsgTag(MsgEvent.MSG_EVENT_GLASS_SELECT);
                item = new JSONObject();
                try {
                    item.put("glassNo", glassNo);
                    item.put("deviceNo", mGlass.getChannel().getParent().getSn());
                    item.put("channelNo", mGlass.getChannel().getChannel());
                    item.put("deviceType", mGlass.getChannel().getDevType());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                event.setAttachment(item.toString());
                EventBus.getDefault().post(event);
                break;
            case MyGestureDispatcher.DOUBLE_CLICK_EVENT:// 双击

                if (mPlayFragment.isDisableSliding()) {
                    break;
                }
                if (!mActivity.isLandScape()) {
                    event = new MsgEvent();
                    event.setMsgTag(MsgEvent.MSG_EVENT_CHANGE_WINDOW_DOUBLECLICK);
                    item = new JSONObject();
                    try {
                        item.put("glassNo", glassNo);
                        item.put("deviceNo", mGlass.getChannel().getParent().getSn());
                        item.put("channelNo", mGlass.getChannel().getChannel());
                        item.put("deviceType", mGlass.getChannel().getDevType());
                        if (mWindow.getGlassCount() > 1) {
                            // 多切一
                            item.put("multiScreen", false);
                        } else {
                            // 一切多
                            item.put("multiScreen", true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    event.setAttachment(item.toString());
                    EventBus.getDefault().post(event);
                }

                break;
            case MyGestureDispatcher.GESTURE_TO_LEFT:
            case MyGestureDispatcher.GESTURE_TO_RIGHT:

//                MyLog.e(TAG, "event------zuoyou");
                // 非横屏或者非单屏, 不支持左右滑动
                if (!mActivity.isLandScape() || mWindow.getGlassCount() != 1) {
                    return;
                }
                leftRightDrag = true;
                if (!originSize) {
                    left += vector.x;
                    bottom += vector.y;
                    needRedraw = true;
                }
                break;
            case MyGestureDispatcher.GESTURE_TO_UP:
            case MyGestureDispatcher.GESTURE_TO_DOWN:

//                MyLog.e(TAG, "event------shangxia");
                // 非横屏或者非单屏, 不支持上下滑动
                if (!mActivity.isLandScape() || mWindow.getGlassCount() != 1) {
                    return;
                }
                left += vector.x;
                bottom += vector.y;
                needRedraw = true;
                break;
            case MyGestureDispatcher.GESTURE_TO_BIGGER:
            case MyGestureDispatcher.GESTURE_TO_SMALLER:
                // 非横屏或者非单屏, 不支持放大缩小
                if (!mActivity.isLandScape() || mWindow.getGlassCount() != 1) {
                    return;
                }
                if (width > glassWidth || distance > 0) {
                    float xFactor = (float) vector.x / glassWidth;
                    float yFactor = (float) vector.y / glassHeight;
                    float factor = yFactor;

                    if (distance > 0) {
                        if (xFactor > yFactor) {
                            factor = xFactor;
                        }
                    } else {
                        if (xFactor < yFactor) {
                            factor = xFactor;
                        }
                    }

                    int xMiddle = middle.x - left;
                    int yMiddle = glassHeight - middle.y - bottom;

                    factor += 1;
                    left = middle.x - (int) (xMiddle * factor);
                    bottom = (glassHeight - middle.y) - (int) (yMiddle * factor);
                    width = (int) (width * factor);
                    height = (int) (height * factor);

                    if (width <= glassWidth || height < glassHeight) {
                        left = 0;
                        bottom = 0;
                        width = glassWidth;
                        height = glassHeight;
                    } else if (width > 4000 || height > 4000) {
                        width = mChannel.getLastPortWidth();
                        height = mChannel.getLastPortHeight();

                        if (width > height) {
                            factor = 4000.0f / width;
                            width = 4000;
                            height = (int) (height * factor);
                        } else {
                            factor = 4000.0f / height;
                            width = (int) (width * factor);
                            height = 4000;
                        }

                        left = middle.x - (int) (xMiddle * factor);
                        bottom = (glassHeight - middle.y) - (int) (yMiddle * factor);
                    }
                    needRedraw = true;
                }
                break;
            default:
                break;
        }

        if (needRedraw) {
            if (left + width < glassWidth) {
                left = glassWidth - width;
            } else if (left > 0) {
                left = 0;
            }

            if (bottom + height < glassHeight) {
                bottom = glassHeight - height;
            } else if (bottom > 0) {
                bottom = 0;
            }

            if (leftRightDrag) {
                if (left == 0 // 滑到了最左边，可以左滑了
                        || glassWidth - left == width) { // 滑到了最右边，可以右滑了
                    if (null != mPlayFragment) {
                        mPlayFragment.setDisableSliding(false);
                    }

                }
            }

            mChannel.setLastPortLeft(left);
            mChannel.setLastPortBottom(bottom);
            mChannel.setLastPortWidth(width);
            mChannel.setLastPortHeight(height);
        } else {
            if (null != mPlayFragment) {
                mPlayFragment.setDisableSliding(false);
            }
        }
    }

    // --------------------------------------------------
    // # OnClick事件
    // --------------------------------------------------

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lyt_result:// 点击播放结果区域
                if (mWindow.isEdit()) {
                    mCheckBox.setChecked(!mCheckBox.isChecked());
                    mGlass.setChecked(mCheckBox.isChecked());

                    MsgEvent event = new MsgEvent();
                    event.setMsgTag(MsgEvent.MSG_EVENT_GLASS_CHECK);
                    EventBus.getDefault().postSticky(event);
                    return;
                }
                //点击重连，将mts置空，重新获取并连接
                if (null != mChannel) {
                    mChannel.setMts(null);
                }
                mPlayerHelper.connect(false);
                //点击的是已被选中的窗口，不处理
                if (mActivity.getSelectedGlassNo() != mGlass.getNo()) {
                    MsgEvent event = new MsgEvent();
                    event.setMsgTag(MsgEvent.MSG_EVENT_GLASS_SELECT);
                    JSONObject item = new JSONObject();
                    try {
                        item.put("glassNo", mGlass.getNo());
                        item.put("deviceNo", mGlass.getChannel().getParent().getSn());
                        item.put("channelNo", mGlass.getChannel().getChannel());
                        item.put("deviceType", mGlass.getChannel().getDevType());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    event.setAttachment(item.toString());
                    EventBus.getDefault().post(event);
                }
                break;
            case R.id.flyt_glass:
                mCheckBox.setChecked(!mCheckBox.isChecked());
            case R.id.checkbox:
                mGlass.setChecked(mCheckBox.isChecked());

                MsgEvent event = new MsgEvent();
                event.setMsgTag(MsgEvent.MSG_EVENT_GLASS_CHECK);
                EventBus.getDefault().postSticky(event);
                break;
            default:
                break;
        }
    }

    // --------------------------------------------------
    // # 其它
    // --------------------------------------------------

    /**
     * 播放与功能两个区域交互
     */
    @Override
    public void doInteraction(String json) {
        try {
            JSONObject item = new JSONObject(json);
            String type = item.optString("type");
            switch (type) {
                case "dismissRecordingCalling":// 隐藏录像和对讲状态
                    mChannel.setRecording(false);
                    multiFunctionBar.stopRecord(mChannel.isRecording());
                    multiFunctionBar.dismissRecordingCalling();
                    Log.e("asdfdfsd", "Base2-dismissRecordingCalling");
                    break;
                case "switchStream":// 码流切换
                    multiFunctionBar.switchStreamWindow();
                    break;
                case "hiddenStream":// 隐藏码流布局
                    multiFunctionBar.hiddenStream();
                    break;
                case "switchCall":// 横屏下开始/停止双向对讲
                    if (mActivity.isLandScape()) {
                        if (item.optBoolean("startCall")) {
                            multiFunctionBar.startDoubleCall();
                        } else {
                            multiFunctionBar.resetDoubleState();
                        }
                    }
                    break;
                case "switchRecord":// 开始/停止录像
                    if (item.optBoolean("startRecord")) {// 开始录像
                        multiFunctionBar.startRecord(item.optString("fileName"));

                        mGlassContainer.setPadding(mSelectedDeviceBorderPadding, mSelectedDeviceBorderPadding,
                                mSelectedDeviceBorderPadding, mSelectedDeviceBorderPadding);
                        mGlassContainer.setBackgroundResource(R.drawable.border_glass_record);
                    } else {
                        multiFunctionBar.stopRecord(mChannel.isRecording());

                        mGlassContainer.setPadding(mDeviceBorderPadding, mDeviceBorderPadding, mDeviceBorderPadding,
                                mDeviceBorderPadding);
                        mGlassContainer.setBackgroundResource(R.drawable.border_glass_normal);
                    }
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setEdit(boolean isEdit) {
        if (null != mCheckBox) {
            if (isEdit) {
                mCheckBox.setVisibility(View.VISIBLE);
                if (null != mGlass) {
                    mCheckBox.setChecked(mGlass.isChecked());
                }
            } else {
                mCheckBox.setVisibility(View.GONE);
            }
        }
    }

    // --------------------------------------------------
    // # 底层回调
    // --------------------------------------------------

    /**
     * 处理底层回调
     *
     * @param what
     * @param glassNo 玻璃号
     * @param result
     * @param obj
     */
    @Override
    public void handleNativeCallback(int what, int glassNo, int result, Object obj) {
    }

}

