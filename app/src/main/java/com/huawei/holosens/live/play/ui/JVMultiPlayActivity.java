package com.huawei.holosens.live.play.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.huawei.holobase.bean.AccountInfoBean;
import com.huawei.holobase.bean.PlayBean;
import com.huawei.holobase.bean.PtzFocusBean;
import com.huawei.holobase.bean.PtzGBFocusBean;
import com.huawei.holobase.bean.PtzGBMoveBean;
import com.huawei.holobase.bean.PtzGBStopBean;
import com.huawei.holobase.bean.PtzMoveBean;
import com.huawei.holobase.bean.PtzStopBean;
import com.huawei.holobase.bean.PtzToken;
import com.huawei.holobase.bean.bean;
import com.huawei.holobase.view.TipDialog;
import com.huawei.holobasic.consts.AppConsts;
import com.huawei.holobasic.consts.MySharedPreferenceKey;
import com.huawei.holobasic.utils.ActivityManager;
import com.huawei.holobasic.utils.BitmapCache;
import com.huawei.holobasic.utils.MySharedPreference;
import com.huawei.holobasic.utils.ScreenUtils;
import com.huawei.holosens.DeviceSubListFragment;
import com.huawei.holosens.R;
import com.huawei.holosens.base.BaseActivity;
import com.huawei.holosens.bean.Channel;
import com.huawei.holosens.bean.Device;
import com.huawei.holosens.commons.BundleKey;
import com.huawei.holosens.commons.DevType;
import com.huawei.holosens.consts.JVCloudConst;
import com.huawei.holosens.consts.JVEncodedConst;
import com.huawei.holosens.consts.JVOctConst;
import com.huawei.holosens.live.play.adapter.MultiScreenAdapter;
import com.huawei.holosens.live.play.bean.Glass;
import com.huawei.holosens.live.play.bean.GlassType;
import com.huawei.holosens.live.play.event.MsgEvent;
import com.huawei.holosens.live.play.player.BasePlayHelper;
import com.huawei.holosens.live.play.util.NavigationBarTools;
import com.huawei.holosens.live.play.util.SimpleTask;
import com.huawei.holosens.live.play.util.SimpleThreadPool;
import com.huawei.holosens.live.play.util.StatesBarUtil;
import com.huawei.holosens.live.play.util.ToastUtilsB;
import com.huawei.holosens.live.play.util.VideoUtils;
import com.huawei.holosens.live.playback.ui.JVBaseRemoteLinePlayActivity;
import com.huawei.holosens.utils.AnimUtil;
import com.huawei.holosens.utils.CommonPermissionUtils;
import com.huawei.holosens.utils.ErrorUtil;
import com.huawei.holosens.utils.HeaderUtil;
import com.huawei.holosens.utils.JniUtil;
import com.huawei.holosens.utils.NetWorkUtil;
import com.huawei.holosens.utils.ToastUtils;
import com.huawei.holosens.view.SelectCallDialog;
import com.huawei.holosens.view.TopBarLayout;
import com.huawei.net.retrofit.impl.AppImpl;
import com.huawei.net.retrofit.request.BaseRequestParam;
import com.huawei.net.retrofit.request.ResponseCode;
import com.huawei.net.retrofit.request.ResponseData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import pub.devrel.easypermissions.EasyPermissions;
import rx.functions.Action1;

/**
 * 播放_多分屏
 * <p>
 * 1.当前多分屏支持的设备如下:TODO
 * 云视通1.0的IPC,流媒体协议的ipc和猫眼
 * 2.手机连接AP信号可以进入多分屏(但是不支持多分屏的操作)
 * 如果在普通Wifi下进入多分屏后切到AP信号,直接退出多分屏界面
 * 3.根据播放界面当前存在的设备数量来判断是否可以滑动。
 * a.如果账号下有效设备数为3(符合<=4的规则), 那播放界面当前存的设备数量就是3;
 * b.如果账号下有效设备数为5(符合>4的规则), 那播放界面当前存的设备数量就是1;
 */
public class JVMultiPlayActivity extends BaseActivity implements View.OnClickListener {


    private static final String TAG = "JVMultiPlayActivity";

    protected static final int REQUEST_RECORD_AUDIO = 0x001;

    /**
     * 多屏状态下，点击功能按钮，先切换到单屏，然后重新连接，连接成功之后自动开启对应功能
     */
    private static final String AUTO_FUNC_AUDIO = "auto_func_audio";
    private static final String AUTO_FUNC_STREAM = "auto_func_stream";
    private static final String AUTO_FUNC_SNAP = "auto_func_snap";
    private static final String AUTO_FUNC_VIDEO = "auto_func_video";
    private static final String AUTO_FUNC_CALL = "auto_func_call";
    private static final String AUTO_FUNC_PTZ = "auto_func_ptz";
    private static final String AUTO_FUNC_PLAYBACK_LOCAL = "auto_func_playback_local";
    private static final String AUTO_FUNC_PLAYBACK_CLOUD = "auto_func_playback_cloud";


    private String mAutoFunc;

    protected TopBarLayout mTopBarView;//顶部标题栏
    private FragmentManager mFragmentManager;
    private PlayFragment mPlayFragment;
    private NavPortraitFragment mNavPortraitFragment;
    private TextView mTvPage;
    private ImageView mBtnShare, mBtnSettings;
    /*横屏start*/
    private NavLandFragment mNavFragment;
    /*横屏end*/
    private DeviceSubListFragment mDeviceList2Fragment;
    private FrameLayout mLayoutMultiScreen;
    private GridView mSelectListView;

    private RelativeLayout mEditLayout;
    private CheckBox mCbAll;
    private TextView mTvSelect, mTvSelectNum, mTvDelete;

    private ImageView mIvGuide;
    private int mGuideNum = 1;

    // 一扇窗户上有几块玻璃(默认一块)
    private int mSpanCount = 1;//当前真实的分屏数量
    private int mLastSpanCount = 1;//上次的分屏数量，用来处理双击切回上次分屏
    private int mShowSpanCount = 1;//展示用的分屏数量
    // 通道号
    private int mChannelNo;
    // 指定的画面在哪扇窗户上面
    private int mOnWindowNo;
    // 指定的画面在哪块玻璃上面
    private int mSelectedGlassNo;
    // 设备号
    public String mDeviceNo;
    // 设备昵称
    private String mDeviceName;
    // 设备类型
    private int mDeviceType;
    private String mEnterpriseId;

    //
    private int mCurrentSelectedWindowNo = 1;
    // 分屏选择
    private MultiScreenAdapter mSelectAdapter;

    private boolean mNvrCalling;

    private boolean mIsEdit; // 是否是编辑状态
    private boolean jumpImageOrVideo; // 是否是查看图片或者视频，回来编辑状态保存


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Long lastDisTime = MySharedPreference.getLong(MySharedPreferenceKey.PlayKey.PLAY_LAST_DIS_TIME, 0);
        long timeSpan = System.currentTimeMillis() - lastDisTime;
        if (timeSpan > 0 && timeSpan < 800) {
            finish();
        }

        //点击防抖
        try {
            if (null != ActivityManager.getInstance().getActivityByClass(getClass())) {
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        //锁屏状态下，普通报警信息提示上点击查看不会弹出查看界面，会被手机锁屏拦截。
        //加上这几句话，为解决部分手机不兼容上述问题。
        Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_multi_play);
        mEnterpriseId = MySharedPreference.getString(MySharedPreferenceKey.LoginKey.CURRENT_ENTERPRISE);

        //初始化设置(获取通道数据;设置多屏选择)
        initSettings();
        //初始化列表
        initUi();
    }

    private void initSettings() {
        //--------参数处理 start--------
        Intent intent = getIntent();
        //获取传递过来的 包含设备信息的序列化数据
        String data = intent.getStringExtra(BundleKey.PLAY_BEAN_LIST);
        //对json数据判空处理
        if (!TextUtils.isEmpty(data)) {
            try {
                //new TypeToken<ArrayList<PlayBean>>(){}.getType() 即 Gson通过借助TypeToken获取泛型参数的类型的方法
                mPlayList = new Gson().fromJson(data, new TypeToken<ArrayList<PlayBean>>() {
                }.getType());
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                mPlayList = new ArrayList<>();
            }
        }
        //集合数据不为null
        if (mPlayList.size() > 0) {
            //获取真实分屏数量,默认为1(当前获取为1)
            mSpanCount = intent.getIntExtra(BundleKey.SPAN_COUNT, 1);
            mShowSpanCount = mSpanCount;
            if (mSpanCount > 1) {
                mLastSpanCount = mSpanCount;
            }

            //获取传递过来的 列表点击条目的下标
            mSelectedGlassNo = intent.getIntExtra(BundleKey.SELECT_NO, -1);
            //如果获取到有效下标(不为-1) 并且 下标小于集合长度(即点击的是有效通道)
            if (mSelectedGlassNo != -1 && mSelectedGlassNo < mPlayList.size()) {
                //根据下标获取设备信息

                //获取设备ID
                mDeviceNo = mPlayList.get(mSelectedGlassNo).getDeviceId();
                //获取通道ID
                mChannelNo = mPlayList.get(mSelectedGlassNo).getChannelId();
                //获取设备类型(IPC/NVR)
                mDeviceType = mPlayList.get(mSelectedGlassNo).getType();
                //获取通道名称
                mDeviceName = mPlayList.get(mSelectedGlassNo).getNickName();
            } else {
                //??? 理论上走不到这一步
                //取最后一个并且有效的通道显示
                mDeviceNo = mPlayList.get(mPlayList.size() - 1).getDeviceId();
                mChannelNo = mPlayList.get(mPlayList.size() - 1).getChannelId();
                mDeviceType = mPlayList.get(mPlayList.size() - 1).getType();
                mDeviceName = mPlayList.get(mPlayList.size() - 1).getNickName();
            }
            mDeviceName = TextUtils.isEmpty(mDeviceName) ? mDeviceNo : mDeviceName;
        }
        //--------参数处理 end--------

        //创建多屏选择的适配器
        mSelectAdapter = new MultiScreenAdapter(this);
        //创建selector数组
        int[] spanIds = new int[]{R.drawable.selector_multi_screen_select_1,
                R.drawable.selector_multi_screen_select_4,
                R.drawable.selector_multi_screen_select_9,
                R.drawable.selector_multi_screen_select_16};
        //设置适配器数据
        mSelectAdapter.setData(spanIds);

        initDatas();
    }

    /**
     * 初始化数据
     * <= 4 个设备, 分屏时设备全展示;
     * > 4 个设备, 分屏时显示当前设备和+号.
     */
    private void initDatas() {
        try {
        /*
          1.计算初始用到的玻璃
         */
            //有效玻璃数量
            int allValidGlassCount = mPlayList.size();
            for (int i = 0; i < allValidGlassCount; i++) {
                //创建玻璃类
                Glass glass = new Glass(GlassType.TYPE_GLASS_CLOUDSEE_V2_IPC);
                //创建通道类
                Channel channel = new Channel();
                //设置通道id
                channel.setChannel(mPlayList.get(i).getChannelId());
                //设置通道名称
                channel.setChannel_id(mPlayList.get(i).getChannelID());
                //设置通道(设备)类型
                channel.setDevType(mPlayList.get(i).getType());
                //设置通道连接协议(holo/GB28181)
                channel.setChannelType(mPlayList.get(i).getConnect_type());
                //设置是否支持通道对讲
                channel.setSupportCall(null != mPlayList.get(i).getDevice_ability() && mPlayList.get(i).getDevice_ability().contains("talk"));
                //设置是否支持nvr对讲
                channel.setSupportNvrCall(channel.getDevType() == DevType.NVR);
                //设置是否支持云台操作
                channel.setSupportPtz(null != mPlayList.get(i).getDevice_ability() && mPlayList.get(i).getDevice_ability().contains("ptz"));
                //设置是否是被分享的通道
                channel.setShared(mPlayList.get(i).isShared());
                //设置通道昵称
                channel.setNickName(mPlayList.get(i).getNickName());
                //设置国标IPC设备对应的通道ID
                channel.setIpc_device_channel_id(mPlayList.get(i).getIpc_device_channel_id());
                //设置设备在线状态
                channel.setOnlineStatus(mPlayList.get(i).getOnlineStatus());
                //创建设备集合类
                Device device = new Device();
                //设置设备连接协议
                device.setConnect_type(mPlayList.get(i).getConnect_type());
                //设置设备ID(产品序列号)
                device.setSn(mPlayList.get(i).getDeviceId());
                //给通道设置所属设备
                channel.setParent(device);
                //玻璃设置对应的通道
                glass.setChannel(channel);
                //添加玻璃对象到玻璃列表中
                mAddGlassList.add(glass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        initWindow(false);
    }


    private void initUi() {
        //顶部标题栏
        mTopBarView = findViewById(R.id.topbar);
        //设置顶部标题栏
        mTopBarView.setTopBar(R.drawable.selector_back_icon, -1, R.string.live_title, this);
        //设置右侧文字(编辑)
        mTopBarView.setRightTextRes(R.string.edit);
        //设置标题栏背景色
        mTopBarView.setTopBarBackgroundResource(R.color.white);

        //当前页面(如:1/2)
        mTvPage = findViewById(R.id.tv_page);

        //分享按钮(功能未实现)
        mBtnShare = findViewById(R.id.btn_portrait_share);
        mBtnShare.setOnClickListener(this);
        //设置按钮(功能未实现)
        mBtnSettings = findViewById(R.id.btn_portrait_settings);
        mBtnSettings.setOnClickListener(this);

        //刷新数据
        refreshData();

        if (mSpanCount == 1 && null != mChannel && !mChannel.isShared()) {
            //当前分屏数为1;通道不为null;通道未分享
            mBtnShare.setVisibility(View.VISIBLE);
        }
        if (null == mChannel || mSpanCount != 1) {
            //通道为null 或者 当前分屏数量不为1
            mBtnSettings.setVisibility(View.GONE);
        } else {
            mBtnSettings.setVisibility(View.VISIBLE);
        }

        mFragmentManager = getSupportFragmentManager();
        mLayoutMultiScreen = findViewById(R.id.layout_multi_screen);
        mLayoutMultiScreen.setOnClickListener(this);
        mSelectListView = findViewById(R.id.lv_select);
        doPageSelectSetting();
        // 播放窗口
        mFragmentManager.beginTransaction()
                .add(R.id.play_fragment_container, createWindowsFragment())
                .commitAllowingStateLoss();
        // 竖屏下播放区域底部功能栏
        mFragmentManager.beginTransaction()
                .add(R.id.nav_portrait_fragment_container, createNavPortraitFragment())
                .commitAllowingStateLoss();

        // 横屏下状态栏(竖屏下隐藏)
        mFragmentManager.beginTransaction()
                .add(R.id.nav_fragment_container, createNavFragment())
                .commitAllowingStateLoss();
        // 隐藏顶部状态栏
        mFragmentManager.beginTransaction().hide(mNavFragment).commitAllowingStateLoss();

        // 设备列表
        mFragmentManager.beginTransaction()
                .add(R.id.device_list_fragment_container, createDeviceListFragment())
                .commitAllowingStateLoss();
        mFragmentManager.beginTransaction().hide(mDeviceList2Fragment).commitAllowingStateLoss();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hideBottomUIMenu();
        }
        addHideBottomUIMenuListener();

        mEditLayout = findViewById(R.id.bottom);
        mCbAll = findViewById(R.id.cb_all);
        mTvSelect = findViewById(R.id.select);
        mTvSelectNum = findViewById(R.id.selected_num);
        mTvDelete = findViewById(R.id.delete);
        mCbAll.setOnClickListener(this);
        mTvDelete.setOnClickListener(this);

        mIvGuide = findViewById(R.id.iv_guide);
        mIvGuide.setVisibility(View.GONE);
        if (mPlayList.size() == 0) {
            if (!MySharedPreference.getBoolean(MySharedPreferenceKey.GuideKey.GUIDE_PLAY, false)) {
                StatesBarUtil.setFitsSystemWindows(this, true);
                mIvGuide.setVisibility(View.VISIBLE);
                mIvGuide.setOnClickListener(this);
            }
        } else {
            if (!MySharedPreference.getBoolean(MySharedPreferenceKey.PlayKey.PLAY_WITHOUT_WIFI)) {
                if (NetWorkUtil.IsNetWorkEnable() && !NetWorkUtil.isWifiAvailable()) {
                    ToastUtils.show(this, R.string.play_without_wifi);
                    MySharedPreference.putBoolean(MySharedPreferenceKey.PlayKey.PLAY_WITHOUT_WIFI, true);
                }
            }
        }

    }


    @Override
    protected int getTitleLayout() {
        /*
          不使用自动追加的标题栏,我们在当前xml布局中追加.
          这主要是因为索引页的UI设计决定的
         */
        return -1;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mLayoutMultiScreen.getVisibility() == View.VISIBLE) {
            mLayoutMultiScreen.setVisibility(View.GONE);
        }

        stopAllFunc();

        // 隐藏码流窗口
        hiddenStreamWindow();

        if (isLandScape()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        if (jumpImageOrVideo) {
            jumpImageOrVideo = false;
        } else {
            if (mIsEdit) {
                mIsEdit = false;
                mTopBarView.setLeftButtonRes(R.drawable.selector_back_icon);
                mTopBarView.setRightTextRes(R.string.edit);
                AnimUtil.hiddenToBottom(mEditLayout);

                if (null != mPlayFragment) {
                    mPlayFragment.setEdit(mIsEdit);
                }
                if (null != mNavPortraitFragment) {
                    mNavPortraitFragment.setEdit(mIsEdit);
                }
                mBtnShare.setEnabled(!mIsEdit);
                mBtnSettings.setEnabled(!mIsEdit);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        if (mDeviceList2Fragment.isVisible()) {
            mFragmentManager.beginTransaction().hide(mDeviceList2Fragment).commitAllowingStateLoss();
            return;
        }
        exitMultiPlay(false);
    }

    private long CLICK_INTERVAL = 400;
    private long lastClickTime;
    private int lastClickId;


    public void hideFragment() {
        if (mDeviceList2Fragment.isVisible()) {
            mFragmentManager.beginTransaction().hide(mDeviceList2Fragment).commitAllowingStateLoss();
            return;
        }
    }

    @Override
    /**
     * 点击事件
     */
    public void onClick(View v) {
        //防止重复点击
        if (v.getId() == lastClickId) {
            if (System.currentTimeMillis() - lastClickTime < CLICK_INTERVAL) {
                lastClickTime = System.currentTimeMillis();
                return;
            }
        }
        lastClickId = v.getId();
        lastClickTime = System.currentTimeMillis();

        switch (v.getId()) {
            case R.id.iv_guide://新手引导页
                mGuideNum++;
                if (mGuideNum == 2) {
                    mFragmentManager.beginTransaction().show(mDeviceList2Fragment).commitAllowingStateLoss();
                    mIvGuide.setImageResource(R.mipmap.ic_play_guide_2);
                } else if (mGuideNum == 3) {
                    mFragmentManager.beginTransaction().hide(mDeviceList2Fragment).commitAllowingStateLoss();
                    mIvGuide.setImageResource(R.mipmap.ic_play_guide_3);
                } else if (mGuideNum == 4) {
                    mIvGuide.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    mIvGuide.setImageResource(R.mipmap.ic_play_guide_end);
                } else {
                    StatesBarUtil.setFitsSystemWindows(this, false);
                    mIvGuide.setVisibility(View.GONE);
                    MySharedPreference.putBoolean(MySharedPreferenceKey.GuideKey.GUIDE_PLAY, true);
                }
                break;
            case R.id.left_btn://返回
            case R.id.btn_back:
                if (mIsEdit) {
                    //判断是否为编辑状态
                    mIsEdit = false;
                    mTopBarView.setLeftButtonRes(R.drawable.selector_back_icon);
                    mTopBarView.setRightTextRes(R.string.edit);
                    AnimUtil.hiddenToBottom(mEditLayout);

                    if (null != mPlayFragment) {
                        mPlayFragment.setEdit(mIsEdit);
                    }

                    if (null != mNavPortraitFragment) {
                        mNavPortraitFragment.setEdit(mIsEdit);
                    }

                    //取消禁用分享按钮
                    mBtnShare.setEnabled(!mIsEdit);
                    //取消禁用设置按钮
                    mBtnSettings.setEnabled(!mIsEdit);
                    break;
                }
                exitMultiPlay(false);
                break;
            case R.id.layout_glass_add_main:
            case R.id.iv_add://添加设备
                if (mIsEdit) {
                    break;
                }
                if (v.getId() == R.id.layout_glass_add_main) {
                    if (mSpanCount == 1) {
                        break;
                    }
                }
                if (mLayoutMultiScreen.getVisibility() == View.VISIBLE) {
                    mLayoutMultiScreen.setVisibility(View.GONE);
                }
                // 隐藏码流窗口
                hiddenStreamWindow();

                stopAllFunc();

                if (isLandScape()) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                mDeviceList2Fragment.update();
                mFragmentManager.beginTransaction().show(mDeviceList2Fragment).commitAllowingStateLoss();
                break;

            case R.id.right_btn://编辑
                if (mDeviceList2Fragment.isVisible()) {
                    return;
                }
                if (mSpanCount == 1 && mCurrentSelectedWindowNo == mWindowList.size()) {
                    if (mPlayList.size() == 0) {
                        ToastUtils.show(mActivity, R.string.wait_add_channel);
                    }
                    break;
                }
                refreshData();
                if (null == mGlass) {
//                    ToastUtils.show(mActivity, R.string.wait_add_channel);
                    break;
                }
                if (!mIsEdit) {
                    //默认全不选中
                    for (Glass glass : mAddGlassList) {
                        glass.setChecked(false);
                    }
                    mCbAll.setChecked(false);
                    mTvSelect.setVisibility(View.GONE);
                    mTvSelectNum.setText(R.string.check_all);
                    mTvDelete.setText(R.string.delete_all);

                    mIsEdit = true;
                    mTopBarView.setLeftButtonRes(R.mipmap.ic_playfunc_close_default);
                    mTopBarView.setRightText("");
                    AnimUtil.showFromBotton(mEditLayout);

                    // 停止录像
                    refreshData();
                    if (null != mChannel && mChannel.isRecording()) {
                        switchRecord(true);
                    }
                    stopAllFunc();

                    if (null != mPlayFragment) {
                        mPlayFragment.setEdit(mIsEdit);
                    }
                    if (null != mNavPortraitFragment) {
                        mNavPortraitFragment.setEdit(mIsEdit);
                    }
                    mBtnShare.setEnabled(!mIsEdit);
                    mBtnSettings.setEnabled(!mIsEdit);
                }
                break;
            case R.id.cb_all:
                if (mCbAll.isChecked()) {
                    int checkedCount = 0;
                    for (Glass glass : getWindowList().get(mOnWindowNo)) {
                        if (glass.getType() != GlassType.TYPE_PLUS) {
                            glass.setChecked(true);
                            checkedCount++;
                        }
                    }
                    //再触发一下达到刷新目的
                    mPlayFragment.setEdit(true);
                    mTvSelect.setVisibility(View.VISIBLE);
                    mTvSelectNum.setText(String.valueOf(checkedCount));
                    mTvDelete.setText(R.string.delete);
                } else {
                    for (Glass glass : getWindowList().get(mOnWindowNo)) {
                        if (glass.getType() != GlassType.TYPE_PLUS) {
                            glass.setChecked(false);
                        }
                    }
                    //再触发一下达到刷新目的
                    mPlayFragment.setEdit(true);
                    mTvSelect.setVisibility(View.GONE);
                    mTvSelectNum.setText(R.string.check_all);
                    mTvDelete.setText(R.string.delete_all);
                }

                break;
            case R.id.delete:
                deleteDevice();
                break;


            //分屏选择布局点击事件
            case R.id.layout_multi_screen:
                if (mLayoutMultiScreen.getVisibility() == View.VISIBLE) {
                    mLayoutMultiScreen.setVisibility(View.GONE);
                }
                break;
            //多分屏
            case R.id.btn_portrait_multi_screen:
                switchPageSpanWindow();
                break;

            //全屏
            case R.id.btn_fullscreen:
                if (!isLandScape()) {
                    if (mSpanCount != 1) {
                        // 多 -> 1，多切一
                        mSpanCount = 1;
                        changeWindow(false);
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                break;

            //切码流
            case R.id.btn_portrait_stream:
            case R.id.btn_land_stream:
//                ToastUtilsB.show("设备暂不支持切换码流");
                if (mDevice == null) {
                    ToastUtils.show(mActivity, getString(R.string.wait_add_channel));
                    return;
                }

//                if (!isIFrameOk(AUTO_FUNC_STREAM)) {
//                    return;
//                }
                refreshData();
                if (null != mChannel && mChannel.isVoiceCall()) {
                    switchCall();
                }
                switchStream();
                break;

            //抓拍
            case R.id.btn_land_snap:
                // 隐藏码流窗口
                hiddenStreamWindow();
                if (!isIFrameOk(AUTO_FUNC_SNAP)) {
                    return;
                }
                snap();
                break;

            //录像
            case R.id.btn_land_video:
                // 隐藏码流窗口
                hiddenStreamWindow();
                if (!isIFrameOk(AUTO_FUNC_VIDEO)) {
                    return;
                }
                switchRecord(true);
                break;

            //音频监听
            case R.id.btn_portrait_audio:
            case R.id.btn_land_audio:
                // 隐藏码流窗口
                hiddenStreamWindow();
                if (!isIFrameOk(AUTO_FUNC_AUDIO)) {
                    return;
                }
                switchAudio();
                break;

            //对讲
            case R.id.btn_portrait_call:
            case R.id.btn_close_call:
            case R.id.btn_land_call:
                if (mChannel.getParent().isGB28181Device()) {
                    ToastUtils.show(mActivity, "设备暂不支持对讲");
                    return;
                }

                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                //至少有一个通话存在，或者振铃中，不可打开对讲
                if (TelephonyManager.CALL_STATE_OFFHOOK == telephonyManager.getCallState()
                        || TelephonyManager.CALL_STATE_RINGING == telephonyManager.getCallState()) {
                    ToastUtils.show(mActivity, R.string.on_call);
                    return;
                }

                // 隐藏码流窗口
                hiddenStreamWindow();
                if (!isIFrameOk(AUTO_FUNC_CALL)) {
                    return;
                }

                refreshData();
//                if (null != mChannel && !mChannel.isSupportCall() && !mChannel.isSupportNvrCall()) {
//                    ToastUtils.show(mActivity, R.string.not_support_function);
//                    return;
//                }

                String[] perms = {Manifest.permission.RECORD_AUDIO};
                if (EasyPermissions.hasPermissions(this, perms)) {
                    switchCall();

                } else {
                    String permissionNames = CommonPermissionUtils.getInstance().getNameByPermissionArray(this, perms);
                    // Ask for one permission
                    EasyPermissions.requestPermissions(this, getString(R.string.permission_request, permissionNames),
                            REQUEST_RECORD_AUDIO, perms);
                }
                break;

            //云台
            case R.id.btn_land_ptz:
            case R.id.btn_close_ptz:
                // 隐藏码流窗口
                hiddenStreamWindow();
                if (!isIFrameOk(AUTO_FUNC_PTZ)) {
                    return;
                }
                refreshData();
//                if (null != mChannel && !mChannel.isSupportPtz()) {
//                    ToastUtils.show(mActivity, R.string.not_support_function);
//                    return;
//                }
                if (!mChannel.isPtzLayoutShow())
                    getPTZControlToken();
                else
                    releasePTZControlToken();
                break;

            //远程回放
            case R.id.btn_land_playback:
                // 隐藏码流窗口
                skipToPlayBackPage(false);
                break;
            case R.id.btn_land_playback_cloud:
                skipToPlayBackPage(true);
                break;


            //查看抓拍图片或者视频
            case R.id.layout_snap:
            case R.id.iv_snap_land:
//                String path;
//                if (null != mIvSnap.getTag()) {
//                    path = (String) mIvSnap.getTag();
//                } else {
//                    path = (String) v.getTag();
//                }
//                if (TextUtils.isEmpty(path)) return;
//                dismissSnapWindow();
//                jumpImageOrVideo = true;
//                if (path.endsWith(AppConsts.IMAGE_JPG_KIND)) {
//                    if (isLandScape()) {
//                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                    }
//                    Intent intent = new Intent(this, ImageActivity.class);
//                    ArrayList<String> urls = sanCaptureDir(path);
//                    intent.putStringArrayListExtra("urls", urls);
//                    intent.putExtra("position", 0);
//                    startActivity(intent);
//                } else if (path.endsWith(AppConsts.VIDEO_MP4_KIND)) {
//                    VideoUtils.tryPlayVideo(this, path);
//                    if (TextUtils.equals(Build.MODEL, "PDCM00")) {
//                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                    }
//                }
                break;
            default:
                break;
        }
    }

    /**
     * 跳转到对应的回放页面
     */
    private void skipToPlayBackPage(boolean isCloud) {
        hiddenStreamWindow();
//        if (!isIFrameOk(AUTO_FUNC_PLAYBACK_CLOUD) || !isIFrameOk(AUTO_FUNC_PLAYBACK_LOCAL)) {
//            return;
//        }
        stopAllFunc();
        Intent remoteIntent = new Intent();
        remoteIntent.setClass(mActivity, JVBaseRemoteLinePlayActivity.class);
        remoteIntent.putExtra(BundleKey.DEVICE_ID, mDeviceNo);
        remoteIntent.putExtra(BundleKey.CHANNEL_ID, mChannelNo);
        remoteIntent.putExtra(BundleKey.CONNECTED, false);
        remoteIntent.putExtra(BundleKey.NICK_NAME, mDeviceName);
        remoteIntent.putExtra(BundleKey.CONNECT_TYPE, mDevice.getConnect_type());
        remoteIntent.putExtra(BundleKey.GB_CHANNEL_ID, mChannel.getChannel_id());
        if (isCloud) {
            remoteIntent.putExtra(BundleKey.PLAYBACK_TYPE, JVBaseRemoteLinePlayActivity.PLAY_BACK_TYPE_CLOUD);
        } else {
            remoteIntent.putExtra(BundleKey.PLAYBACK_TYPE, JVBaseRemoteLinePlayActivity.PLAY_BACK_TYPE_LOCAL);
        }
        remoteIntent.putExtra("horizon", false);
        mActivity.startActivity(remoteIntent);
    }


    /**
     * 检查通道有没有已经被添加
     *
     * @return
     */
    private boolean checkChannelExist(PlayBean bean) {
        for (int i = 0; i < mPlayList.size(); i++) {
            if (bean.isGB28181Device() == mPlayList.get(i).isGB28181Device()) {
                if (bean.isGB28181Device()) {
                    if (bean.getDeviceId().equals(mPlayList.get(i).getDeviceId()) && bean.getIpc_device_channel_id().equals(mPlayList.get(i).getIpc_device_channel_id())) {
                        return true;
                    }
                } else {
                    if (bean.getDeviceId().equals(mPlayList.get(i).getDeviceId()) && bean.getChannelId() == mPlayList.get(i).getChannelId()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == REQUEST_RECORD_AUDIO) {
//            switchCall();
        }
    }


    /**
     * 隐藏虚拟按键，并且全屏
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT < 19) { // lower api
            View v = getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
        //设置状态栏文字颜色及图标为深色
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

    }


    /**
     * 监听虚拟按钮,如果显示立即隐藏
     */
    private void addHideBottomUIMenuListener() {
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == View.VISIBLE) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        hideBottomUIMenu();
                    }
                }
            }
        });
    }

    /**
     * 播放窗口模块
     */
    private Fragment createWindowsFragment() {
        if (mPlayFragment == null) {
            mPlayFragment = PlayFragment.newInstance(getBundle());
        }
        return mPlayFragment;
    }

    /**
     * 创建播放区域底部功能栏
     */
    private Fragment createNavPortraitFragment() {
        if (mNavPortraitFragment == null) {
            mNavPortraitFragment = NavPortraitFragment.newInstance(getBundle());
        }
        return mNavPortraitFragment;
    }

    /**
     * 创建项部导航栏
     */
    private Fragment createNavFragment() {
        if (mNavFragment == null) {
            mNavFragment = NavLandFragment.newInstance(getBundle());
        }
        return mNavFragment;
    }

    /**
     * 创建设备列表
     */
    private Fragment createDeviceListFragment() {
//        if (mDeviceListFragment == null) {
//            mDeviceListFragment = DevsListFragment.newInstance();
//        }
//        return mDeviceListFragment;

        if (mDeviceList2Fragment == null) {
            mDeviceList2Fragment = new DeviceSubListFragment();
        }
        return mDeviceList2Fragment;
    }

    /**
     * 设置分屏数量
     *
     * @param spanCount
     */
    public void setSpanCount(int spanCount) {
        mSpanCount = spanCount;
    }

    /**
     * 获取分屏数量
     *
     * @return
     */
    public int getSpanCount() {
        return mSpanCount;
    }

    public int getSelectedGlassNo() {
        return mSelectedGlassNo;
    }

    /**
     * 获取bundle信息
     *
     * @return
     */
    private Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt("spanCount", mSpanCount);
        bundle.putInt("showSpanCount", mShowSpanCount);
        bundle.putInt("onWindowNo", mOnWindowNo);
        bundle.putInt("selectedGlassNo", mSelectedGlassNo);
        return bundle;
    }


    /**
     * 更改标题栏
     *
     * @param title
     */
    @SuppressLint("SetTextI18n")
    private void setTitle(String title) {

        if (getWindowList().size() > 1 || mSpanCount > 1) {
            mTvPage.setVisibility(View.VISIBLE);
            mTvPage.setText(mCurrentSelectedWindowNo + "/" + getWindowList().size());
        } else {
            mTvPage.setVisibility(View.GONE);
        }
    }

    /**
     * 更改标题栏
     *
     * @param glass
     */
    private void setTitle(Glass glass) {
        if (glass == null) {
            if (getWindowList().size() > 1) {
                mTvPage.setVisibility(View.VISIBLE);
                mTvPage.setText(mCurrentSelectedWindowNo + "/" + getWindowList().size());
            } else {
                mTvPage.setVisibility(View.GONE);
            }
            return;
        }
        Device device = glass.getChannel().getParent();
        String title = device.getSn();
        setTitle(title);
    }

    /**
     * 获取标题
     */
    public String getTopBarTitle() {
        return mTopBarView.getTitle();
    }

    /**
     * 设置当前选中的窗户号
     *
     * @param windowNo 当前窗户号
     */
    public void setSelectedWindowNo(int windowNo) {
        mCurrentSelectedWindowNo = windowNo + 1;
        setTitle(getGlassByNo(mSelectedGlassNo));
    }

    /**
     * 获取当前的窗户下标，从0开始
     */
    public int getSelectedWindowIndex() {
        return mOnWindowNo;
    }

    /**
     * 获取窗户列表
     *
     * @return
     */
    public List<List<Glass>> getWindowList() {
        return mWindowList;
    }

    /**
     * 通过玻璃号码获得玻璃详情
     *
     * @param glassNo
     * @return
     */
    public Glass getGlassByNo(int glassNo) {
        if (null == mAddGlassList || mAddGlassList.size() == 0) {
            return null;
        } else {
            for (Glass glass : mAddGlassList) {
                if (glass.getNo() == glassNo) {
                    return glass;
                }
            }
            return null;
        }
    }

    /**
     * 通过下标获得玻璃详情
     *
     * @param glassIndex
     * @return
     */
    public Glass getGlassByIndex(int glassIndex) {
        if (null == mAddGlassList || glassIndex < 0 || glassIndex >= mAddGlassList.size()) {
            return null;
        } else {
            return mAddGlassList.get(glassIndex);
        }
    }

    /**
     * 获取用户当前添加的全部玻璃
     *
     * @return
     */
    public List<Glass> getAddGlassList() {
        return mAddGlassList;
    }


    /**
     * 播放页面目前已经添加玻璃总数
     *
     * @return
     */
    public int getMultiGlassCount() {
        return getAddGlassList() != null ? getAddGlassList().size() : 0;
    }


    // ---------------------------------------------------
    // # 分屏选择配置 Start
    // ---------------------------------------------------

    /**
     * 分屏选择的显示/隐藏
     */
    private void switchPageSpanWindow() {
        if (mLayoutMultiScreen.getVisibility() == View.VISIBLE) {
            mLayoutMultiScreen.setVisibility(View.GONE);
        } else {
            mLayoutMultiScreen.setVisibility(View.VISIBLE);
            //根据分屏数量获取gridview列表的选中下标
            int selected = getIndexBySpanCount();
            //设置对应下标的选中状态
            mSelectAdapter.setSelected(selected);
            //刷新数据
            mSelectAdapter.notifyDataSetChanged();

            // 隐藏码流窗口
            hiddenStreamWindow();
        }
    }

    /**
     * 页面选择设置
     */
    private void doPageSelectSetting() {
        mSelectListView.setAdapter(mSelectAdapter);
        mSelectAdapter.setSelected(0);
        mSelectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int index, long arg3) {
                //获取分屏数量：1/4/9/16
                int spanCount = getSpanCountByIndex(index);
                if (mShowSpanCount == spanCount && mSpanCount == spanCount && mLastSpanCount == spanCount) {
                    //如果 分屏数量 = 展示用的分屏数量 = 当前用的分屏数量 = 上次用的分屏数量;退出
                    return;
                }
                //更新当前分屏数量
                mSpanCount = spanCount;
                //更新当前分屏数量
                mLastSpanCount = spanCount;
                //更新展示用的分屏数量
                mShowSpanCount = spanCount;
                //切换窗口
                changeWindow(false);
                //隐藏分屏列表
                mLayoutMultiScreen.setVisibility(View.GONE);

            }
        });
    }

    /**
     * 通过下标获取分屏数量
     *
     * @param index 下标
     * @return
     */
    private int getSpanCountByIndex(int index) {
        int spanCount = 1;
        switch (index) {
            case 1:
                spanCount = 4;
                break;
            case 2:
                spanCount = 9;
                break;
            case 3:
                spanCount = 16;
                break;
            default:
                break;
        }

        return spanCount;
    }

    /**
     * 根据分屏数量获取下标
     *
     * @return
     */
    private int getIndexBySpanCount() {
        int index = 1;
        switch (mShowSpanCount) {
            case 1:
                index = 0;
                break;
            case 4:
                index = 1;
                break;
            case 9:
                index = 2;
                break;
            case 16:
                index = 3;
                break;
            default:
                break;
        }

        return index;
    }

    // ---------------------------------------------------
    // # 分屏选择配置 End
    // ---------------------------------------------------
    // ---------------------------------------------------
    // # 窗户/玻璃计算 Start
    // ---------------------------------------------------
    // 窗户列表，包括可以连接的设备和特殊窗户如+号
    private List<List<Glass>> mWindowList = new ArrayList<>();
    // 多分屏中当前添加的玻璃列表，每一个代表可以连接一个通道
    private List<Glass> mAddGlassList = new ArrayList<>();
    // deviceid和channelid确定一个用来连接的通道
    private List<PlayBean> mPlayList = new ArrayList<>();

    //获取通道列表
    public List<PlayBean> getPlayList() {
        return mPlayList;
    }


    /**
     * 初始化窗户
     *
     * @param delete
     */
    private void initWindow(boolean delete) {
        if (mWindowList != null) {
            //清空窗户列表
            mWindowList.clear();
        }

        /*
          计算需要补全的+号玻璃个数
          如果一扇窗户需要四块玻璃，现在只有三块玻璃，那剩下的一块用+号玻璃填充。
          如果一扇窗户需要四块玻璃，现在刚好有四块，那么增加一扇窗户(第二页)，用四块+号玻璃填充。
        */
        //当前添加的通道数量
        int currentGlassCount = mAddGlassList.size();
        //加号数量
        int addCount;
        if (currentGlassCount == 0) {
            //如果当前添加的通道数量为0;
            //加号数量 = 当前真实分屏数量
            addCount = mSpanCount;
        } else {
            if (currentGlassCount % mSpanCount == 0) {
                //如果当前添加的通道数量 取余运算 当前分屏数量(1/4/9/16),值为0
                //那么: 加号数量 = 当前真实分屏数量(即第二页全是加号,数量为分屏数量)
                //例:通道数:4; 分屏数:4; 加号数:4;
                addCount = mSpanCount;
            } else {
                //如果当前添加的通道数量 取余运算 当前分屏数量(1/4/9/16),值不为0
                //那么: 加号数量 = 当前真实分屏数量 - 余数(即不够一屏的通道数量)
                //例: 通道数:5; 分屏数:4; 加号数:4-(5%4)=3;
                addCount = mSpanCount - currentGlassCount % mSpanCount;
            }
        }
        //玻璃列表
        List<Glass> glassList = null;
        //遍历每一块玻璃(包括加号玻璃),
        for (int i = 0; i < currentGlassCount + addCount; i++) {
            if (i % mSpanCount == 0) {
                //如果当前玻璃为窗户中的最后一块
                //那么添加玻璃列表到窗户列表中
                glassList = new ArrayList<>();
                mWindowList.add(glassList);
            }
            if (i < currentGlassCount) {
                //如果当前玻璃上存在通道
                // 获取窗户号(从0开始)
                int windowNo = mWindowList.size() - 1;
                //获取下标对应的玻璃对象
                Glass glass = mAddGlassList.get(i);
                if (delete) {
                    if (glass.getNo() == GlassType.TYPE_EMPTY) {
                        //设置玻璃号
                        glass.setNo(i);
                    }
                } else {
                    //设置玻璃号
                    glass.setNo(i);
                }
                //设置窗口号
                glass.setWindowNo(windowNo);
                //添加玻璃
                glassList.add(glass);


                //获取玻璃对应的通道
                Channel channel = glass.getChannel();
                //获取通道对应的设备
                Device device = channel.getParent();
                // 检查当前的玻璃是否是用户选中的窗户上的那块玻璃
                // 获取设备序列号和设备号比较 同时 获取设备通道号和通道比较
                boolean isSelectedGlass = device.getSn().equals(mDeviceNo) && (mChannelNo == channel.getChannel());
                if (isSelectedGlass) {
                    //如果是选中的玻璃,获取选中玻璃号
                    mSelectedGlassNo = glass.getNo();
                    //设置画面所在窗户
                    mOnWindowNo = windowNo;
                }
            } else {
                //如果当前玻璃上不存在通道(即 加号玻璃 "+")
                Glass empty = new Glass(GlassType.TYPE_PLUS);
                empty.setNo(GlassType.TYPE_PLUS);
                glassList.add(empty);
            }
        }

        String account = "";
        AccountInfoBean mInfo = new Gson().fromJson(MySharedPreference.getString(MySharedPreferenceKey.Account.AccountInfo), AccountInfoBean.class);
        if (mInfo != null && null != mInfo.getAccount()) {
            account = mInfo.getAccount();
        }
        MySharedPreference.putString(MySharedPreferenceKey.PlayKey.PLAY_LIST + account, new Gson().toJson(mPlayList));
        MySharedPreference.putInt(MySharedPreferenceKey.PlayKey.PLAY_SPAN_COUNT + account, mSpanCount);
        MySharedPreference.putInt(MySharedPreferenceKey.PlayKey.PLAY_SELECT_NO + account, mSelectedGlassNo);

    }

    /**
     * 改变窗户
     */
    public void changeWindow(boolean delete) {

        //切换窗口需要停止录像
        refreshData();
        if (null != mChannel && mChannel.isRecording()) {
            switchRecord(true);
        }
        // 停止对讲
        if (null != mChannel && mChannel.isVoiceCall()) {
            stopDoubleCall(false);
            mChannel.setVoiceCall(false);//不等回调，直接置为false
        }
        //初始化窗户
        initWindow(delete);
        //改变窗户上的玻璃数量后进行的更新各种信息
        updateInfos();

    }

    /**
     * 在播放界面中通过"+"添加的设备后的操作
     */
    public void updateAddDeviceList(List<PlayBean> addList) {
        if (null != addList && addList.size() > 0) {
            try {
                List<PlayBean> availableAddList = new ArrayList<>();
                for (int i = 0; i < addList.size(); i++) {
                    PlayBean bean = addList.get(i);
                    if (bean == null) {
                        continue;
                    }
//                    if (checkChannelExist(bean)) {
//                        continue;
//                    }
                    availableAddList.add(bean);
                    mPlayList.add(bean);
                    Glass glass = new Glass(GlassType.TYPE_GLASS_CLOUDSEE_V2_IPC);
                    Channel channel = new Channel();
                    channel.setChannel(bean.getChannelId());
                    channel.setIpc_device_channel_id(bean.getIpc_device_channel_id());
                    channel.setChannel_id(bean.getChannelID());
                    channel.setChannelType(bean.getConnect_type());
                    channel.setDevType(bean.getType());
                    channel.setSupportCall(null != bean.getDevice_ability() && bean.getDevice_ability().contains("talk"));
                    channel.setSupportNvrCall(channel.getDevType() == DevType.NVR);
                    channel.setSupportPtz(null != bean.getDevice_ability() && bean.getDevice_ability().contains("ptz"));
                    channel.setShared(bean.isShared());
                    Device device = new Device();
                    device.setSn(bean.getDeviceId());
                    device.setConnect_type(bean.getConnect_type());
                    channel.setParent(device);
                    channel.setNickName(bean.getNickName());
                    glass.setChannel(channel);
                    mAddGlassList.add(glass);
                }
                if (availableAddList.size() > 0) {
                    //每次添加都默认选中最后一个可利用的通道
                    mDeviceNo = availableAddList.get(availableAddList.size() - 1).getDeviceId();
                    mChannelNo = availableAddList.get(availableAddList.size() - 1).getChannelId();
                    mDeviceType = availableAddList.get(availableAddList.size() - 1).getType();
                    mDeviceName = availableAddList.get(availableAddList.size() - 1).getNickName();
                }
                changeWindow(false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mFragmentManager.beginTransaction().hide(mDeviceList2Fragment).commitAllowingStateLoss();

    }

    //删除设备
    private void deleteDevice() {
        final TipDialog dialog = new TipDialog(this);
        dialog.setTitle(getString(R.string.delete_channel_tip))
                .setPositive(getString(R.string.sure))
                .setNegtive(getString(R.string.cancel))
                .setSingle(false).setOnClickBottomListener(new TipDialog.OnClickBottomListener() {
            @Override
            public void onPositiveClick() {
                dialog.dismiss();

                if (!mCbAll.isChecked()) {//当前页面全部删除
                    for (Glass glass : getWindowList().get(mOnWindowNo)) {
                        glass.setChecked(true);
                    }
                }

//                boolean deleteSelectNo = false;
                for (int i = mAddGlassList.size() - 1; i >= 0; i--) {
                    //因为要重建，所以全断开
                    BasePlayHelper playHelper = (BasePlayHelper) mAddGlassList.get(i).getPlayHelper();
                    if (null != playHelper) {
                        // 调用断开方法
                        playHelper.disconnect();
                    }
                    if (mAddGlassList.get(i).isChecked()) {
//                        if (mAddGlassList.get(i).getNo() == mSelectedGlassNo) {
//                            deleteSelectNo = true;
//                        }

                        mAddGlassList.remove(i);
                        mPlayList.remove(i);
                    }
                }
                //更新设备列表
//                if (deleteSelectNo) {
                try {
                    int position = mOnWindowNo * mSpanCount;
                    if (position >= mAddGlassList.size()) {
                        position = 0;
                    }
                    if (position < mAddGlassList.size()) {
                        Glass glass = mAddGlassList.get(position);
                        if (null != glass && glass.getNo() != GlassType.TYPE_PLUS) {
                            mDeviceNo = glass.getChannel().getParent().getSn();
                            mChannelNo = glass.getChannel().getChannel();
                            mDeviceType = glass.getChannel().getDevType();
                            mDeviceName = glass.getChannel().getNickName();
                            mDeviceName = TextUtils.isEmpty(mDeviceName) ? mDeviceNo : mDeviceName;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
//                }

                if (null != mDeviceList2Fragment) {
//                    mDeviceList2Fragment.updateDevs();
                }

                mIsEdit = false;
                mTopBarView.setLeftButtonRes(R.drawable.selector_back_icon);
                mTopBarView.setRightTextRes(R.string.edit);
                AnimUtil.hiddenToBottom(mEditLayout);
                if (null != mPlayFragment) {
                    mPlayFragment.setEdit(mIsEdit);
                }
                if (null != mNavPortraitFragment) {
                    mNavPortraitFragment.setEdit(mIsEdit);
                }
                mBtnShare.setEnabled(!mIsEdit);
                mBtnSettings.setEnabled(!mIsEdit);

                changeWindow(true);
            }

            @Override
            public void onNegtiveClick() {
                dialog.dismiss();
            }
        }).show();
    }


    /**
     * 改变窗户上的玻璃数量后进行的更新各种信息(changeWindow的后续操作)
     */
    private void updateInfos() {
        setTitle(getGlassByNo(mSelectedGlassNo));
        Bundle bundle = new Bundle();
        bundle.putInt("spanCount", mSpanCount);
        bundle.putInt("showSpanCount", mShowSpanCount);
        bundle.putInt("onWindowNo", mOnWindowNo);
        bundle.putInt("selectedGlassNo", mSelectedGlassNo);
        mPlayFragment.updateAfterSpan(bundle);
        mNavFragment.updateAfterSpan(bundle);
        mNavPortraitFragment.updateAfterSpan(bundle);
        refreshData();
        if (mSpanCount == 1 && null != mChannel && !mChannel.isShared() && !isLandScape()) {
//            mBtnShare.setVisibility(View.VISIBLE);
        } else {
            mBtnShare.setVisibility(View.GONE);
        }
        if (null == mChannel || mSpanCount != 1 || isLandScape()) {
            mBtnSettings.setVisibility(View.GONE);
        } else {
//            mBtnSettings.setVisibility(View.VISIBLE);
        }

        if (!MySharedPreference.getBoolean(MySharedPreferenceKey.PlayKey.PLAY_WITHOUT_WIFI)) {
            if (NetWorkUtil.IsNetWorkEnable() && !NetWorkUtil.isWifiAvailable()) {
                ToastUtils.show(this, R.string.play_without_wifi);
                MySharedPreference.putBoolean(MySharedPreferenceKey.PlayKey.PLAY_WITHOUT_WIFI, true);
            }
        }
    }

    // ---------------------------------------------------------
    // #退出播放 start
    // ---------------------------------------------------------
    private SimpleTask mTimeoutTask, mDisconnectTask;
    private boolean isBackPressed;

    //是否退出播放页面
    public boolean isBackPressed() {
        return isBackPressed;
    }

    /**
     * 退出播放页面
     * @param exitDirectly true:直接退出  false:需要先切换竖屏
     */
    private void exitMultiPlay(boolean exitDirectly) {
        Log.e("lockScreen", "exitMultiPlay");
        /*
          横屏下,按返回键的处理与横屏导航栏返回按钮操作一致
         */
        if (isLandScape() && !exitDirectly) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return;
        }

        isBackPressed = true;

        if (TextUtils.isEmpty(mDeviceNo)) {
            finish();
            return;
        }
        long lastConnectTime = MySharedPreference.getLong(MySharedPreferenceKey.PlayKey.PLAY_LAST_CONNECT_TIME, 0);

        if (System.currentTimeMillis() - lastConnectTime < 500) {
            return;
        }

//        loading(false);
        stopAllFunc();

        // 超时计算任务
        mTimeoutTask = new SimpleTask() {
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

                if (mDisconnectTask != null) {
                    mDisconnectTask.cancel();
                    mDisconnectTask = null;
                }
//                dismissLoading();
                Log.e("lockScreen", "mDisconnectTask.cancel");
                MySharedPreference.putLong(MySharedPreferenceKey.PlayKey.PLAY_LAST_DIS_TIME, System.currentTimeMillis());
                finish();
            }
        };


        final List<Fragment> windowList = new ArrayList<>();
        windowList.addAll(mPlayFragment.getWindowList());

        // 断开连接任务
        mDisconnectTask = new SimpleTask() {
            @Override
            public void doInBackground() {
                try {
                    if (!Thread.interrupted()) {
                        // 敲碎所有的玻璃
                        int windowCount = windowList.size();
                        for (int i = 0; i < windowCount; i++) {
                            WindowFragment window = (WindowFragment) windowList.get(i);
                            if (null == window) {
                                continue;
                            }
                            List<Glass> glassList = window.getGlassList();
                            int glassCount = glassList.size();
                            for (int j = 0; j < glassCount; j++) {
                                Glass glass = glassList.get(j);
                                BasePlayHelper playHelper = (BasePlayHelper) glass.getPlayHelper();
                                if (playHelper == null) {
                                    continue;
                                }
                                // 调用断开方法
                                playHelper.disconnect();
                            }
                        }
                    }

                    Thread.sleep(1);
                } catch (Exception e) {
                    // TODO
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish(boolean canceled) {
                if (canceled) {
                    return;
                }
                //start 不置为初始值会导致界面播放区域有白屏 2018.06.13
                mSpanCount = 1;
                mLastSpanCount = 1;
                mShowSpanCount = 1;
                //end

//                dismissLoading();
                SimpleTask.removeCallbacks(mTimeoutTask);
                if (mTimeoutTask != null) {
                    mTimeoutTask.cancel();
                    mTimeoutTask = null;
                }
                Log.e("lockScreen", "mTimeoutTask.cancel");
                MySharedPreference.putLong(MySharedPreferenceKey.PlayKey.PLAY_LAST_DIS_TIME, System.currentTimeMillis());
                finish();
            }
        };

        SimpleThreadPool.execute(mDisconnectTask);
        SimpleTask.postDelay(mTimeoutTask, 15 * 1000);
    }

    // ---------------------------------------------------------
    // #退出播放 end
    // ---------------------------------------------------------

    // ----------------------------------------------------------------
    // # EventBus start
    // ----------------------------------------------------------------

    /**
     * 设备列表操作通知
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEventBus(MsgEvent event) {
        switch (event.getMsgTag()) {
            case MsgEvent.MSG_EVENT_CHANGE_WINDOW_DOUBLECLICK:// 双击改变窗户上的玻璃数量
                Log.e("changewindow-doubleClic", "k-1,mLastSpanCount=" + mLastSpanCount + ";mSpanCount=" + mSpanCount);
                /*
                  单屏双击, 弹出分屏选择Dialog
                  多屏上单击某块玻璃, 直接切到对应的单屏
                 */
                try {

                    JSONObject item = new JSONObject(event.getAttachment());
                    mSelectedGlassNo = item.getInt("glassNo");
                    mDeviceNo = item.getString("deviceNo");
                    mChannelNo = item.getInt("channelNo");
                    mDeviceType = item.getInt("deviceType");
                    if (null != getGlassByNo(mSelectedGlassNo)) {
                        mDeviceName = getGlassByNo(mSelectedGlassNo).getChannel().getNickName();
                        if (TextUtils.isEmpty(mDeviceName)) {
                            mDeviceName = mDeviceNo;
                        }
                    } else {
                        mDeviceName = mDeviceNo;
                    }
                    // 是否从单屏切到多屏，一切多
                    boolean isMultiScreen = item.getBoolean("multiScreen");
                    if (isMultiScreen) {
                        /*
                          1 -> 多:
                          a.如果之前没有切过多屏,弹出分屏选择Dialog
                          b.如果之前切过多屏, 直接切到上次用户选择的分屏
                         */
                        if (mLastSpanCount == 1) {
//                            switchPageSpanWindow();
                        } else {
                            mSpanCount = mLastSpanCount;
                            mShowSpanCount = mSpanCount;
                            changeWindow(false);
                        }

                        // 横屏的场合,需要将播放区域上方的底部功能区移除
                        if (isLandScape()) {
                            hiddenBar();
                        }
                    } else {

                        // 多 -> 1，多切一
                        mShowSpanCount = mSpanCount;
                        mSpanCount = 1;
                        changeWindow(false);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case MsgEvent.MSG_EVENT_GLASS_SELECT:// 改变玻璃的选择
            case MsgEvent.MSG_EVENT_GLASS_CHANGE:// 左右滑动切换玻璃
                try {
                    // 停止录像
                    refreshData();
                    if (null != mChannel && mChannel.isRecording()) {
                        switchRecord(true);
                    }
                    stopAllFunc();
                    // 隐藏码流窗口
                    hiddenStreamWindow();

                    JSONObject item = new JSONObject(event.getAttachment());
                    if (item.optBoolean("changeToAdd")) {
                        //滑动到了+号玻璃
                        mChannel = null;
                        mSelectedGlassNo = GlassType.TYPE_PLUS;
                        Bundle bundle = new Bundle();
                        bundle.putInt("spanCount", mSpanCount);
                        bundle.putInt("showSpanCount", mShowSpanCount);
                        bundle.putInt("selectedGlassNo", GlassType.TYPE_PLUS);
                        mNavFragment.updateAfterSpan(bundle);
                        mNavPortraitFragment.updateAfterSpan(bundle);
                        mBtnShare.setVisibility(View.GONE);
                        mBtnSettings.setVisibility(View.GONE);
                        return;
                    }
//                    if (mSelectedGlassNo == item.getInt("glassNo")) {
//                        return;
//                    }
                    mSelectedGlassNo = item.getInt("glassNo");
                    mDeviceNo = item.getString("deviceNo");
                    mChannelNo = item.getInt("channelNo");
                    mDeviceType = item.getInt("deviceType");
                    Glass glass = getGlassByNo(mSelectedGlassNo);
                    mOnWindowNo = glass.getWindowNo();
                    setTitle(glass);

                    Bundle bundle = new Bundle();
                    bundle.putInt("spanCount", mSpanCount);
                    bundle.putInt("showSpanCount", mShowSpanCount);
                    bundle.putInt("onWindowNo", mOnWindowNo);
                    bundle.putInt("selectedGlassNo", mSelectedGlassNo);
                    mNavFragment.updateAfterSpan(bundle);
                    mNavPortraitFragment.updateAfterSpan(bundle);
                    refreshData();
                    if (mSpanCount == 1 && null != mChannel && !mChannel.isShared() && !isLandScape()) {
//                        mBtnShare.setVisibility(View.VISIBLE);
                    } else {
                        mBtnShare.setVisibility(View.GONE);
                    }
                    if (null == mChannel || mSpanCount != 1 || isLandScape()) {
                        mBtnSettings.setVisibility(View.GONE);
                    } else {
//                        mBtnSettings.setVisibility(View.VISIBLE);
                    }
                    String account = "";
                    AccountInfoBean mInfo = new Gson().fromJson(MySharedPreference.getString(MySharedPreferenceKey.Account.AccountInfo), AccountInfoBean.class);
                    if (mInfo != null && null != mInfo.getAccount()) {
                        account = mInfo.getAccount();
                    }
                    MySharedPreference.putInt(MySharedPreferenceKey.PlayKey.PLAY_SELECT_NO + account, mSelectedGlassNo);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case MsgEvent.MSG_EVENT_TOP_BOTTOM_NAV:// 横屏模式下显示/隐藏顶部和底部状态栏
                /*
                  TODO 暂时屏蔽掉横屏下单击切换底部状态栏的功能
                  最早规划的是单击显示隐藏顶部和底部状态栏,但是如果正在单向对讲的时候,这个时候会有问题。
                  问题是工具栏3s自动消失逻辑和单向对讲有冲突，但是3s自动消失逻辑已去掉，解注此逻辑2018.5.31
                 */
                if (isLandScape() && mSpanCount == 1) {
                    switchBar();
                }
                break;
            case MsgEvent.MSG_EVENT_EXIT_PLAY:// 退出播放
                exitMultiPlay(true);
                break;
            case MsgEvent.MSG_EVENT_GLASS_CHECK:// 玻璃选中
                int checkedCount = 0;
                for (int i = 0; i < mAddGlassList.size(); i++) {
                    if (mAddGlassList.get(i).isChecked()) {
                        checkedCount++;
                    }
                }
                if (checkedCount == 0) {
                    mCbAll.setChecked(false);
                    mTvSelect.setVisibility(View.GONE);
                    mTvSelectNum.setText(R.string.check_all);
                    mTvDelete.setText(R.string.delete_all);
                } else {
                    mCbAll.setChecked(true);
                    mTvSelect.setVisibility(View.VISIBLE);
                    mTvSelectNum.setText(String.valueOf(checkedCount));
                    mTvDelete.setText(R.string.delete);
                }
                break;
            default:
                break;
        }
    }

    // ----------------------------------------------------------------
    // # EventBus end
    // ----------------------------------------------------------------

    // ----------------------------------------------------------------
    // # 服务器回调 start
    // ----------------------------------------------------------------
    @Override
    public void onHandler(int what, int glassNo, int result, Object obj) {

        /*
         * 视频连接，先收到连接回调，然后收到一个O帧，然后收到1个I帧，屏幕出图
         * 切换码流，先断开连接然后重新连接，走设备连接流程，一般在收到O帧之后操作切换成功
         */
        switch (what) {
            //视频连接回调
            case JVEncodedConst.WHAT_CONNECT_CHANGE:
                //todo 触发功能按钮多分屏切换到单屏时，若连接失败，是否取消后续功能按钮的响应
                if (result != JVEncodedConst.CCONNECTTYPE_CONNOK && result != JVEncodedConst.CCONNECTTYPE_CLOUD_TURN) {
                    stopAllFunc();
                    // 隐藏码流窗口
                    hiddenStreamWindow();

                    //连接断开之后重连默认连接标清
                    MsgEvent streamEvent = new MsgEvent();
                    streamEvent.setMsgTag(MsgEvent.MSG_EVENT_INTERACTION);
                    JSONObject streamObj = new JSONObject();
                    try {
                        streamObj.put("type", "refreshStream");
                        streamObj.put("currentStream", "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    streamEvent.setAttachment(streamObj.toString());
                    EventBus.getDefault().post(streamEvent);
                }
                break;
            //收到O帧
            case JVEncodedConst.WHAT_NORMAL_DATA:
                refreshData();
                if (null == mChannel) {
                    return;
                }
                String[] streamArray = getResources().getStringArray(R.array.array_stream);
                String currentStream = mChannel.getStreamTag() == JVOctConst.STREAM_HD ? streamArray[1] : streamArray[0];

                MsgEvent streamEvent = new MsgEvent();
                streamEvent.setMsgTag(MsgEvent.MSG_EVENT_INTERACTION);
                JSONObject streamObj = new JSONObject();
                try {
                    streamObj.put("type", "refreshStream");
                    streamObj.put("currentStream", currentStream);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                streamEvent.setAttachment(streamObj.toString());
                EventBus.getDefault().post(streamEvent);

                // 码流切换完成. 如果正在录像需要停止录像,2秒以后重新录
                if (mChannel.isRecordChangeStream()) {
                    mChannel.setRecordChangeStream(false);
                    handler.sendEmptyMessageDelayed(WHAT_RECORD_VIDEO, 2 * 1000);
                }
                break;
            //收到I帧，出图
            case JVEncodedConst.WHAT_NEW_PICTURE:
                Log.e(TAG, "onHandler: IIIIII, spanCount=" + mSpanCount);
                if (mSpanCount == 1 && !TextUtils.isEmpty(mAutoFunc)) {
                    switch (mAutoFunc) {
                        case AUTO_FUNC_AUDIO:
                            //打开关闭音频监听
                            switchAudio();
                            break;
                        case AUTO_FUNC_STREAM:
                            //切换码流
                            switchStream();
                            break;
                        case AUTO_FUNC_SNAP:

                            mTopBarView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    snap();
                                }
                            }, 300);
                            break;
                        case AUTO_FUNC_VIDEO:
                            switchRecord(true);
                            break;
                        case AUTO_FUNC_CALL:
                            String[] perms = {Manifest.permission.RECORD_AUDIO};
                            if (EasyPermissions.hasPermissions(this, perms)) {
                                switchCall();

                            } else {
                                String permissionNames = CommonPermissionUtils.getInstance().getNameByPermissionArray(this, perms);
                                // Ask for one permission
                                EasyPermissions.requestPermissions(this, getString(R.string.permission_request, permissionNames),
                                        REQUEST_RECORD_AUDIO, perms);
                            }
                            break;
                        case AUTO_FUNC_PTZ:
                            refreshData();
                            if (!mChannel.isPtzLayoutShow()) {
                                getPTZControlToken();
                            } else {
                                releasePTZControlToken();
                            }
                            break;
                        case AUTO_FUNC_PLAYBACK_LOCAL:
                            skipToPlayBackPage(false);
                            break;
                        case AUTO_FUNC_PLAYBACK_CLOUD:
                            skipToPlayBackPage(true);
                            break;
                    }
                    mAutoFunc = "";
                }
                break;
            //对讲回调
            case JVEncodedConst.WHAT_CHAT: {
                break;
            }


            //隐藏抓拍弹窗
            case WHAT_DISMISS_POPUPWINDOW:
                dismissSnapWindow();
                break;
            //切换码流之后，之前的录像重新录制
            case WHAT_RECORD_VIDEO:
                ToastUtilsB.show(R.string.video_change);
                switchRecord(false);
                break;
        }

    }


    @Override
    public void onNotify(int what, int glassNo, int result, Object obj) {
        Log.e(TAG, "onNotify: " + what + ", " + glassNo + ", " + result);
        handler.sendMessage(handler.obtainMessage(what, glassNo, result, obj));
        // 播放Fragment
        if (mPlayFragment != null) {
            mPlayFragment.dispatchNativeCallback(what, glassNo, result, obj);
        }
    }
    // ----------------------------------------------------------------
    // # 服务器回调 end
    // ----------------------------------------------------------------

    // ----------------------------------------------------------------
    // # 横竖屏切换 start
    // ----------------------------------------------------------------
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        dismissSnapWindow();

        // 隐藏码流窗口
        hiddenStreamWindow();

        // 设置窗户的尺寸(窗户的尺寸一旦固定后就不会改变)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            // 隐藏分屏
            mLayoutMultiScreen.setVisibility(View.GONE);
            setFullScreen();

            // 显示顶部Fragment，隐藏竖屏功能按钮
            mFragmentManager.beginTransaction()
                    .show(mNavFragment)
                    .hide(mNavPortraitFragment)
                    .commitAllowingStateLoss();

            // 转到横屏默认先隐藏状态栏
            if (null != mChannel && mChannel.isVoiceCall()) {
                showBar();
            } else {
                hiddenBar();
            }

            mBtnShare.setVisibility(View.GONE);
            mBtnSettings.setVisibility(View.GONE);

        } else {

            cancelFullScreen();

            // 隐藏顶部Fragment，显示竖屏功能按钮
            mFragmentManager.beginTransaction()
                    .hide(mNavFragment)
                    .show(mNavPortraitFragment)
                    .commitAllowingStateLoss();

            // 转到竖屏隐藏状态栏
            hiddenBar();

            refreshData();
            if (mSpanCount == 1 && null != mChannel && !mChannel.isShared() && !isLandScape()) {
//                mBtnShare.setVisibility(View.VISIBLE);
            } else {
                mBtnShare.setVisibility(View.GONE);
            }
            if (null == mChannel || mSpanCount != 1 || isLandScape()) {
                mBtnSettings.setVisibility(View.GONE);
            } else {
//                mBtnSettings.setVisibility(View.VISIBLE);
            }

        }

    }

    public boolean isLandScape() {
        Configuration mConfiguration = this.getResources().getConfiguration();
        return mConfiguration.orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * 隐藏码流窗口
     * ps:分屏是针对所有设备的, 码流选择是针对单个设备的;其它两个选择窗口同时存在
     * 不冲突; 根据要求要做成互斥的。如果码流窗口显示, 切横屏要隐藏掉,因为横竖屏的码流
     * 选择UI不一样
     */
    private void hiddenStreamWindow() {
        MsgEvent event = new MsgEvent();
        event.setMsgTag(MsgEvent.MSG_EVENT_INTERACTION);
        JSONObject item = new JSONObject();
        try {
            item.put("type", "hiddenStream");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        event.setAttachment(item.toString());
        EventBus.getDefault().post(event);
    }

    /**
     * 设置全屏
     */
    public void setFullScreen() {
//        getWindow().setFlags(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        StatesBarUtil.setFitsSystemWindows(this, true);
        mTopBarView.setVisibility(View.GONE);

        //隐藏导航栏
        if (NavigationBarTools.hasNavBar(this) && NavigationBarTools.isNavBarVisible(this)) {
            NavigationBarTools.setNavBarVisibility(this, false);
        }
    }

    /**
     * 取消全屏
     */
    public void cancelFullScreen() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mTopBarView.setVisibility(View.VISIBLE);

        //显示导航栏
        NavigationBarTools.setNavBarVisibility(this, true);
    }

    /**
     * 显示/隐藏横屏场合的顶部和底部工具栏
     * ps:顶部的返回按钮会一直存在
     */
    private boolean isBarVisibleToUser;

    public void switchBar() {

        if (isBarVisibleToUser) {
            hiddenBar();
        } else {
            showBar();
        }
    }

    public void hiddenBar() {
        isBarVisibleToUser = false;
        mNavFragment.switchStatusBar(false);
    }

    public void showBar() {
        isBarVisibleToUser = true;
        if (mSpanCount == 1 && mAddGlassList.size() > 0) {
            mNavFragment.switchStatusBar(true);
        }
    }


    // ----------------------------------------------------------------
    // # 横竖屏切换 end
    // ----------------------------------------------------------------


    /**
     * 获取视频连接状态
     *
     * @return
     */
    public int getConnectState() {
        int state = -1;
        for (int i = 0; i < mAddGlassList.size(); i++) {
            BasePlayHelper playHelper = (BasePlayHelper) mAddGlassList.get(i).getPlayHelper();
            if (playHelper != null) {
                return playHelper.getConnectState();
            }
        }

        return state;
    }


    //****************************************************//


    public static final int WHAT_DISMISS_POPUPWINDOW = 0x2020 + 1;
    public static final int WHAT_RECORD_VIDEO = 0x2020 + 2;
    private PopupWindow mPopupWindow;
    private View mSnapView;
    private ImageView mIvSnap;
    private ImageView mIvSnapType;
    private View mLandSnapView;
    private ImageView mIvLandSnap;

    private void initPopupWindow() {
        if (null == mPopupWindow) {
            mPopupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            mPopupWindow.setTouchable(true);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable());

            mSnapView = View.inflate(this, R.layout.layout_snap_window, null);
            mIvSnap = mSnapView.findViewById(R.id.iv_snap);
            mIvSnapType = mSnapView.findViewById(R.id.iv_type);
            mSnapView.findViewById(R.id.layout_snap).setOnClickListener(this);

            mLandSnapView = View.inflate(this, R.layout.layout_snap_window_land, null);
            mIvLandSnap = mLandSnapView.findViewById(R.id.iv_snap_land);
            mIvLandSnap.setOnClickListener(JVMultiPlayActivity.this);
        }
    }


    private Glass mGlass;
    private Channel mChannel;
    private Device mDevice;

    /**
     * 更新数据
     */
    private void refreshData() {
        mGlass = getGlassByNo(mSelectedGlassNo);
        if (null != mGlass) {
            mChannel = mGlass.getChannel();
        } else {
            mChannel = null;
        }
        if (null != mChannel) {
            mDevice = mChannel.getParent();
        } else {
            mDevice = null;
        }
    }

    /**
     * 视频是否连接成功(I帧过来, 出图)
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean isIFrameOk(String autoFunc) {
        if (mLayoutMultiScreen.getVisibility() == View.VISIBLE) {
            mLayoutMultiScreen.setVisibility(View.GONE);
        }

        //过滤单屏的+号，多屏选不中+号(点击便到设备列表了)
        //mCurrentSelectedWindowNo用于显示页数的，在此判断准确，onWindowNo遇到+号没有更新
        if (mSpanCount == 1 && mCurrentSelectedWindowNo == mWindowList.size()) {
            ToastUtils.show(mActivity, R.string.wait_add_channel);
            return false;
        }

        refreshData();
        if (null == mGlass) {
            ToastUtils.show(mActivity, R.string.wait_add_channel);
            return false;
        }
        // 是否出图
        BasePlayHelper playHelper = (BasePlayHelper) mGlass.getPlayHelper();

        if (null == playHelper) {
//            ToastUtils.show(mActivity, R.string.wait_connect);
            ToastUtilsB.show(R.string.wait_connect);
            return false;
        }
        boolean isIFrameOk = playHelper.isIFrameOk();
        if (!isIFrameOk) {
//            ToastUtils.show(mActivity, R.string.wait_connect);
            ToastUtilsB.show(R.string.wait_connect);
            return false;
        }
        if (!TextUtils.isEmpty(autoFunc) && mSpanCount != 1) {
            mShowSpanCount = mSpanCount;
            mSpanCount = 1;
            changeWindow(false);
//            ToastUtils.show(mActivity, R.string.wait_change_window_connect);
            mAutoFunc = autoFunc;
            return false;
        }

        return true;
    }

    /**
     * 停止所有功能
     */
    protected void stopAllFunc() {
        if (null != mChannel) {

            Log.e(TAG, "stopAllFunc:isListening=" + mChannel.isLisening() + ";isRecording=" + mChannel.isRecording());
            // 重置文本聊天
            mChannel.setAgreeTextData(false);
            // 停止监听
            if (mChannel.isLisening()) {
                stopAudio();
            }
            // 停止录像
            if (mChannel.isRecording()) {
                stopRecord();
            }
            // 停止对讲
            if (null != mChannel && mChannel.isVoiceCall()) {
                stopDoubleCall(false);
                mChannel.setVoiceCall(false);//不等回调，直接置为false
            }
            // 停止云台
            if (mChannel.isPtzLayoutShow()) {
                releasePTZControlToken();
            }
        }

    }

    /**
     * 切换码流
     */
    private void switchStream() {
        /*
          这个地方涉及到功能区和播放区的交互,所以使用EventBus来传递信息到WindowFragment中
         */
        MsgEvent event = new MsgEvent();
        event.setMsgTag(MsgEvent.MSG_EVENT_INTERACTION);
        JSONObject item = new JSONObject();
        try {
            item.put("type", "switchStream");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        event.setAttachment(item.toString());
        EventBus.getDefault().post(event);


        if (mChannel.isRecording()) {
            mChannel.setRecordChangeStream(true);
            stopRecord();
        }

        int targetStream = mChannel.getStreamTag() == JVOctConst.STREAM_SD ? JVOctConst.STREAM_HD : JVOctConst.STREAM_SD;

        mChannel.setStreamTag(targetStream);
    }

    /**
     * 抓拍
     */
    private void snap() {
        boolean hasSDCard = hasSDCard(5, true);
        if (hasSDCard) {
            String capturePath = JniUtil.captureWithDev(mSelectedGlassNo, mChannel.getNickName());

            if (!TextUtils.isEmpty(capturePath)) {
                try {
                    AssetManager assetMgr = this.getAssets();
                    // 打开指定音乐文件
                    AssetFileDescriptor afd = assetMgr.openFd(AppConsts.CAPTURE_SOUND_FILE);
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.reset();

                    // 使用MediaPlayer加载指定的声音文件。
                    mediaPlayer.setDataSource(afd.getFileDescriptor(),
                            afd.getStartOffset(), afd.getLength());
                    // 准备声音
                    mediaPlayer.prepare();
                    // 播放
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                showSnapWindow(capturePath);
            } else {
                ToastUtils.show(mActivity, R.string.capture_error);
            }
        }
    }

    /**
     * 显示抓拍的图片
     *
     * @param imgPath
     */
    private void showSnapWindow(String imgPath) {
        initPopupWindow();
        if (!TextUtils.isEmpty(imgPath)) {

            if (isLandScape()) {
                mPopupWindow.setContentView(mLandSnapView);
                mLandSnapView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

                View targetView = mNavFragment.getView().findViewById(R.id.btn_land_snap);

                mIvLandSnap.setTag(imgPath);
                mIvSnap.setTag(null);
                if (imgPath.endsWith(AppConsts.IMAGE_JPG_KIND)) {
                    mIvLandSnap.setImageBitmap(BitmapCache.loadImageBitmap(imgPath, -1));
                } else if (imgPath.endsWith(AppConsts.VIDEO_MP4_KIND)) {
                    mIvLandSnap.setImageBitmap(VideoUtils.getVideoThumbnail(imgPath));

                    targetView = mNavFragment.getView().findViewById(R.id.btn_land_video);
                }
                int[] location = new int[2];
                targetView.getLocationOnScreen(location);

                int[] locationWindow = new int[2];
                mPlayFragment.getView().getLocationOnScreen(locationWindow);

                mPopupWindow.showAtLocation(mNavFragment.getView(), Gravity.NO_GRAVITY,
                        location[0] - mLandSnapView.getMeasuredWidth() - locationWindow[0] - ScreenUtils.dip2px(10),
                        location[1] + (targetView.getHeight() - mLandSnapView.getMeasuredHeight()) / 2);
                mPopupWindow.update(mLandSnapView.getMeasuredWidth(), mLandSnapView.getMeasuredHeight());
            } else {
                mPopupWindow.setContentView(mSnapView);
                mSnapView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

                mIvSnap.setTag(imgPath);
                mIvSnapType.setVisibility(View.GONE);
                if (imgPath.endsWith(AppConsts.IMAGE_JPG_KIND)) {
                    mIvSnap.setImageBitmap(BitmapCache.loadImageBitmap(imgPath, -1));
                } else if (imgPath.endsWith(AppConsts.VIDEO_MP4_KIND)) {
                    mIvSnap.setImageBitmap(VideoUtils.getVideoThumbnail(imgPath));
                    mIvSnapType.setVisibility(View.VISIBLE);
                }

                mPopupWindow.showAsDropDown(mPlayFragment.getView(), 0, -mSnapView.getMeasuredHeight());
//                int[] location = new int[2];
//                mNavPortraitFragment.getView().getLocationOnScreen(location);
//                mPopupWindow.showAtLocation(mNavPortraitFragment.getView(), Gravity.NO_GRAVITY,
//                        0, location[1] - mSnapView.getMeasuredHeight());
                mPopupWindow.update(ScreenUtils.getScreenWidth(), mSnapView.getMeasuredHeight());
            }

            //4s后自动隐藏弹窗
            handler.removeMessages(WHAT_DISMISS_POPUPWINDOW);
            handler.sendEmptyMessageDelayed(WHAT_DISMISS_POPUPWINDOW, 4 * 1000);
        }
    }

    /**
     * 隐藏抓拍弹窗
     */
    private void dismissSnapWindow() {
        handler.removeMessages(WHAT_DISMISS_POPUPWINDOW);

        if (null != mPopupWindow && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    /**
     * 打开/关闭录像
     *
     * @param showRecordPop 录制成功之后是否显示弹窗，因为切码流时，录像会自动停止然后重新录制
     */
    private void switchRecord(boolean showRecordPop) {
        refreshData();
        if (mChannel.isRecording()) {// 正在录像

            if (mChannel.getRecordTime() * 1000 >= JVCloudConst.RECORD_VIDEO_MIN_LENGTH) {
                stopRecord();
                if (showRecordPop) {
                    ToastUtilsB.show("录像成功");
//                    File file = new File(recordingFileName);
//                    if (!TextUtils.isEmpty(recordingFileName) && file.exists()) {
//
//                        showSnapWindow(recordingFileName);
//                    }
                } else {
                    ToastUtils.show(mActivity, getResources().getString(R.string.video_path, recordingFileName));
                }
            } else {
                stopRecord();
//                ToastUtils.show(mActivity, R.string.record_short_failed);
                ToastUtilsB.show(R.string.record_short_failed);
            }
        } else {
            startRecord();
        }
    }

    // 录像文件名称
    private String recordingFileName;

    /**
     * 开始录像
     */
    private void startRecord() {
        Log.e(TAG, "video: mChannel.isRecording()=" + mChannel.isRecording());
        mChannel.setRecording(true);

        recordingFileName = JniUtil.startRecordByDev(mSelectedGlassNo, mChannel.getNickName());

        /*
          这个地方涉及到功能区和播放区的交互,所以使用EventBus来传递信息到WindowFragment中
         */
        MsgEvent event = new MsgEvent();
        event.setMsgTag(MsgEvent.MSG_EVENT_INTERACTION);
        JSONObject item = new JSONObject();
        try {
            item.put("type", "switchRecord");
            item.put("startRecord", true);
            item.put("fileName", recordingFileName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        event.setAttachment(item.toString());
        EventBus.getDefault().post(event);
    }

    /**
     * 停止录像
     */
    public void stopRecord() {
        Log.e(TAG, "stopRecord: " + mSelectedGlassNo);
        refreshData();
        if (null != mChannel && mChannel.isRecording()) {
            JniUtil.stopRecord(mSelectedGlassNo);
            mChannel.setRecording(false);
            mChannel.setRecordTime(0);
        }

        /*
          这个地方涉及到功能区和播放区的交互,所以使用EventBus来传递信息到WindowFragment中
         */
        MsgEvent event = new MsgEvent();
        event.setMsgTag(MsgEvent.MSG_EVENT_INTERACTION);
        JSONObject item = new JSONObject();
        try {
            item.put("type", "switchRecord");
            item.put("startRecord", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        event.setAttachment(item.toString());
        EventBus.getDefault().post(event);

    }


    private boolean mUserOpenAudio;//用户主动开启音频监听

    /**
     * 打开/关闭音频监听
     */
    private void switchAudio() {
        refreshData();

        if (mChannel == null) {
            return;
        }
        // 双向对讲的时候会打开监听,所以正在对讲时不处理
        if (mChannel.isVoiceCall()) {
            return;
        }
        if (mChannel.isLisening()) {
            stopAudio();
        } else {
            mUserOpenAudio = true;
            startAudio();
        }
    }

    /**
     * 打开音频监听
     */
    private void startAudio() {
        JniUtil.holosensPlayerOpenSound(mSelectedGlassNo);
        boolean startRes = true;
        if (startRes) {
            mChannel.setLisening(true);
            // 记录用户主动操作的监听状态
            MsgEvent event = new MsgEvent();
            event.setMsgTag(MsgEvent.MSG_EVENT_INTERACTION);
            JSONObject item = new JSONObject();
            try {
                item.put("type", "switchAudio");
                item.put("audioOpen", true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            event.setAttachment(item.toString());
            EventBus.getDefault().post(event);
        }
    }

    /**
     * 关闭音频监听
     */
    private void stopAudio() {
        JniUtil.holosensPlayerCloseSound(mSelectedGlassNo);
        boolean stopRes = true;
        if (stopRes) {
            mChannel.setLisening(false);
            mUserOpenAudio = false;
            // 记录用户主动操作的监听状态
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
        }
    }

    private SelectCallDialog mSelectCallDialog;

    private void showSelectCallDialog(boolean supportNvrCall) {
        if (null == mSelectCallDialog) {
            mSelectCallDialog = new SelectCallDialog(this);
            mSelectCallDialog.setOnClickBottomListener(new SelectCallDialog.OnClickBottomListener() {
                @Override
                public void onCallNvrClick() {
                    mNvrCalling = true;
                    startDoubleCall(true);
                    mSelectCallDialog.dismiss();
                }

                @Override
                public void onCallChannelClick() {
                    startDoubleCall(false);
                    mSelectCallDialog.dismiss();
                }
            });
        }
        mSelectCallDialog.setSupport(supportNvrCall, mChannel.isSupportCall());
        mSelectCallDialog.show();
    }

    /**
     * 打开/关闭对讲
     */
    private void switchCall() {
        refreshData();

        if (null == mChannel) {
            return;
        }

        if (mChannel.isVoiceCall()) {
            // 关闭对讲
            stopDoubleCall(true);
        } else {
            // 打开对讲
            if (mDeviceType == DevType.NVR) {
//                BaseRequestParam param = new BaseRequestParam();
//                LinkedHashMap<String, Object> data = new LinkedHashMap<>();
//                data.put("device_id", mDeviceNo);
//                param.putAll(data);
//                HashMap<String, Object> header = HeaderUtil.createHeader(MySharedPreference.getString(MySharedPreferenceKey.LoginKey.TOKEN));
//                param.putAllHeader(header);
//
//                AppImpl.getInstance(mActivity).getDevInfo(param).subscribe(new Action1<ResponseData<DevInfo>>() {
//                    @Override
//                    public void call(ResponseData<DevInfo> responseData) {
//                        if (responseData.getCode() == ResponseCode.SUCCESS) {
//                            showSelectCallDialog(null != responseData.getData() && responseData.getData().getAbility().contains("talk"));
//                        } else if (ErrorUtil.CheckError(responseData.getCode())) {
//                            ToastUtils.show(mActivity, ErrorUtil.getInstance().getErrorValue(responseData.getCode()));
//                            showSelectCallDialog(false);
//                        }
//                    }
//                });
            } else {
                startDoubleCall(false);
            }
        }
    }

    /**
     * 开启双向对讲
     */
    private void startDoubleCall(boolean callNvr) {

////        mActivity.createDialog("", false);
//        // 发送请求打开对讲命令
//        if (callNvr) {//和NVR对讲，通道号直接传0
//            // 关闭音频数据传输
//            if (mChannel.isLisening()) {
//                stopAudio();
//            }
//            Jni.setTalkBackDataSource(mSelectedGlassNo, true);
//            Jni.playAudio(mSelectedGlassNo, true);
//            FunctionUtil.startCall(mSelectedGlassNo, -1, false, mChannel.getParent().isGB28181Device() ? JVEncodedConst.CONNECT_BY_JVMP : JVEncodedConst.CONNECT_BY_CLOUDSEE2);
//        } else {
//            // 打开音频数据传输
//            if (!mChannel.isLisening()) {
//                mUserOpenAudio = false;
//                startAudio();
//            }
//            // 只针对打开对讲，nvr传从1开始的通道号，ipc或者球机传0，由于接口返回通道号从0开始，方法里面通道号处理了+1，所以此处调用ipc对讲需要传-1
//            if (mDeviceType == DevType.NVR) {
//                FunctionUtil.startCall(mSelectedGlassNo, mChannel.getChannel(), false, mChannel.getParent().isGB28181Device() ? JVEncodedConst.CONNECT_BY_JVMP : JVEncodedConst.CONNECT_BY_CLOUDSEE2);
//            } else {
//                //修改为，ipc或者球机可以传通道号为1，和NVR走同样的逻辑
//                FunctionUtil.startCall(mSelectedGlassNo, -1 + 1, false, mChannel.getParent().isGB28181Device() ? JVEncodedConst.CONNECT_BY_JVMP : JVEncodedConst.CONNECT_BY_CLOUDSEE2);
//            }
//        }
//
//        FunctionUtil.resetAecDenoise(mSelectedGlassNo, true, false);

        //等待开启对讲回调
    }

    /**
     * 关闭双向对讲：单屏下的对讲关闭显示dialog，分屏切换时，dialog不显示
     *
     * @param showDialog true:显示dialog  false:不显示dialog
     */
    private void stopDoubleCall(boolean showDialog) {
//        if (showDialog) {
////            mActivity.createDialog("", false);
//        }
//
//        Jni.setTalkBackDataSource(mSelectedGlassNo, false);
//        if (mNvrCalling) {
//            mNvrCalling = false;
//            Jni.playAudio(mSelectedGlassNo, true);
//        }
//        FunctionUtil.stopCall(mSelectedGlassNo, JVEncodedConst.CONNECT_BY_CLOUDSEE2);
//        FunctionUtil.stopRecordSendAudio(mSelectedGlassNo);
//        // 停止音频数据传输，若在用户开启对讲之前主动打开了音频监听，那么不关闭音频监听
//        if (!mUserOpenAudio) {
//            stopAudio();
//        }
//
//        MsgEvent event = new MsgEvent();
//        event.setMsgTag(MsgEvent.MSG_EVENT_INTERACTION);
//        JSONObject item = new JSONObject();
//        try {
//            item.put("type", "switchCall");
//            item.put("startCall", false);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        event.setAttachment(item.toString());
//        EventBus.getDefault().post(event);
    }


    /**
     * 显示或者隐藏ptz界面
     */
    private void PtzLayout() {

        MsgEvent event = new MsgEvent();
        event.setMsgTag(MsgEvent.MSG_EVENT_INTERACTION);
        JSONObject item = new JSONObject();
        try {
            item.put("type", "switchPtz");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        event.setAttachment(item.toString());
        EventBus.getDefault().post(event);
    }

    /**
     * 获取是否能开启云台的权限
     * 如果能，打开云台界面
     */
    private void getPTZControlToken() {
        BaseRequestParam param = new BaseRequestParam();
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("enterprise_id", mEnterpriseId);
        data.put("device_id", mChannel.getParent().getSn());
        Device device = mChannel.getParent();
        if (device.isGB28181Device()) {
            data.put("channel_id", mChannel.getIpc_device_channel_id());
        } else {
            data.put("channel_id", mChannel.getChannel());
        }
        param.putAll(data);
        HashMap<String, Object> header = HeaderUtil.createHeader(MySharedPreference.getString(MySharedPreferenceKey.LoginKey.TOKEN));
        param.putAllHeader(header);
        AppImpl.getInstance(this).getPtzControlToken(param).subscribe(new Action1<ResponseData<PtzToken>>() {
            @Override
            public void call(ResponseData<PtzToken> responseData) {
                if (responseData.getCode() == ResponseCode.SUCCESS) {
                    PtzToken ptzToken = responseData.getData();
                    if (ptzToken != null && !TextUtils.isEmpty(ptzToken.getControl_token())) {
                        PtzLayout();
                        MySharedPreference.putString(MySharedPreferenceKey.PTZ_CONTROL_TOKEN, ptzToken.getControl_token());
                    } else {
                        ToastUtils.show(mActivity, getString(R.string.pan_tilt_control_failed));
                    }

                } else if (ErrorUtil.CheckError(responseData.getCode())) {
                    ToastUtils.show(mActivity, ErrorUtil.getInstance().getErrorValue(responseData.getCode()));
                }
            }
        });
    }

    /**
     * 释放设备控制权
     */
    private void releasePTZControlToken() {
        PtzLayout();
        BaseRequestParam param = new BaseRequestParam();
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("enterprise_id", mEnterpriseId);
        data.put("device_id", mChannel.getParent().getSn());
        Device device = mChannel.getParent();
        if (device.isGB28181Device()) {
            data.put("channel_id", mChannel.getIpc_device_channel_id());
        } else {
            data.put("channel_id", mChannel.getChannel());
        }
        param.putAll(data);
        HashMap<String, Object> header = HeaderUtil.createHeader(MySharedPreference.getString(MySharedPreferenceKey.LoginKey.TOKEN));
        param.putAllHeader(header);
        AppImpl.getInstance(this).releasePtzControlToken(param).subscribe(new Action1<ResponseData<bean>>() {
            @Override
            public void call(ResponseData<bean> responseData) {
                if (responseData.getCode() == ResponseCode.SUCCESS) {
                }
//                else if(ErrorUtil.CheckError(responseData.getCode())){
//                    ToastUtils.show(mActivity, ErrorUtil.getInstance().getErrorValue(responseData.getCode()));
//                }
            }
        });
    }

    /**
     * 云台控制
     *
     * @param left
     * @param up
     * @param zoom
     */
    public void ptzMoveStart(int left, int up, int zoom) {

        refreshData();
        Device device = mChannel.getParent();
        String channelId;
        if (device.isGB28181Device()) {
            channelId = mChannel.getIpc_device_channel_id();
        } else {
            channelId = mChannel.getChannel() + "";
        }
        BaseRequestParam param = new BaseRequestParam();
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("method", "ptz_move_start");
        data.put("comments", "start move ptz");
        if (device.isGB28181Device()) {
            PtzGBMoveBean bean = new PtzGBMoveBean(channelId, left, up, zoom);
            data.put("param", com.alibaba.fastjson.JSONObject.toJSON(bean));
        } else {
            PtzMoveBean bean = new PtzMoveBean(Integer.parseInt(channelId), left, up, zoom);
            data.put("param", com.alibaba.fastjson.JSONObject.toJSON(bean));
        }
        param.putAll(data);
        HashMap<String, Object> header = HeaderUtil.createHeader(MySharedPreference.getString(MySharedPreferenceKey.LoginKey.TOKEN));
        param.putAllHeader(header);
        String controlToken = MySharedPreference.getString(MySharedPreferenceKey.PTZ_CONTROL_TOKEN);
        AppImpl.getInstance(mActivity).ptzDevSendCmd(param, mEnterpriseId, mChannel.getParent().getSn(),
                channelId, controlToken).subscribe(new Action1<ResponseData<Object>>() {
            @Override
            public void call(ResponseData<Object> responseData) {
                if (responseData.getCode() == ResponseCode.SUCCESS) {
//                    showCheckInfo((UpdateCheckBean)(responseData.getData().getResult()));
                } else if (ErrorUtil.CheckError(responseData.getCode())) {
                    if (ErrorUtil.getInstance().isControlTokenValid(responseData.getCode())) {
                        PtzLayout();
                    }
                    ToastUtils.show(mActivity, ErrorUtil.getInstance().getErrorValue(responseData.getCode()));
                }
            }
        });
    }

    /**
     * 云台停止
     */
    public void ptzMoveStop() {

        refreshData();
        Device device = mChannel.getParent();
        String channelId;
        if (device.isGB28181Device()) {
            channelId = mChannel.getIpc_device_channel_id();
        } else {
            channelId = mChannel.getChannel() + "";
        }
        BaseRequestParam param = new BaseRequestParam();
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("method", "ptz_move_stop");
        data.put("comments", "stop move ptz");
        if (device.isGB28181Device()) {
            PtzGBStopBean bean = new PtzGBStopBean(channelId);
            data.put("param", com.alibaba.fastjson.JSONObject.toJSON(bean));
        } else {
            PtzStopBean bean = new PtzStopBean(Integer.parseInt(channelId));
            data.put("param", com.alibaba.fastjson.JSONObject.toJSON(bean));
        }
        param.putAll(data);
        HashMap<String, Object> header = HeaderUtil.createHeader(MySharedPreference.getString(MySharedPreferenceKey.LoginKey.TOKEN));
        param.putAllHeader(header);
        String controlToken = MySharedPreference.getString(MySharedPreferenceKey.PTZ_CONTROL_TOKEN);
        AppImpl.getInstance(mActivity).ptzDevSendCmd(param, mEnterpriseId, mChannel.getParent().getSn(),
                channelId, controlToken).subscribe(new Action1<ResponseData<Object>>() {
            @Override
            public void call(ResponseData<Object> responseData) {
                if (responseData.getCode() == ResponseCode.SUCCESS) {
                } else if (ErrorUtil.CheckError(responseData.getCode())) {
                    if (ErrorUtil.getInstance().isControlTokenValid(responseData.getCode())) {
                        PtzLayout();
                    }
                    ToastUtils.show(mActivity, ErrorUtil.getInstance().getErrorValue(responseData.getCode()));
                }
            }
        });
    }

    /**
     * 聚焦光圈启动
     *
     * @param focus_far
     * @param iris_open
     */
    public void ptzFiStart(int focus_far, int iris_open) {

        refreshData();
        Device device = mChannel.getParent();
        String channelId;
        if (device.isGB28181Device()) {
            channelId = mChannel.getIpc_device_channel_id();
        } else {
            channelId = mChannel.getChannel() + "";
        }
        BaseRequestParam param = new BaseRequestParam();
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("method", "ptz_fi_start");
        data.put("comments", "fi speed ranges from -254 to 254");
        if (device.isGB28181Device()) {
            PtzGBFocusBean bean = new PtzGBFocusBean(channelId, focus_far, iris_open);
            data.put("param", com.alibaba.fastjson.JSONObject.toJSON(bean));
        } else {
            PtzFocusBean bean = new PtzFocusBean(Integer.parseInt(channelId), focus_far, iris_open);
            data.put("param", com.alibaba.fastjson.JSONObject.toJSON(bean));
        }
        param.putAll(data);
        HashMap<String, Object> header = HeaderUtil.createHeader(MySharedPreference.getString(MySharedPreferenceKey.LoginKey.TOKEN));
        param.putAllHeader(header);
        String controlToken = MySharedPreference.getString(MySharedPreferenceKey.PTZ_CONTROL_TOKEN);
        AppImpl.getInstance(mActivity).ptzDevSendCmd(param, mEnterpriseId, mChannel.getParent().getSn(), channelId, controlToken).subscribe(new Action1<ResponseData<Object>>() {
            @Override
            public void call(ResponseData<Object> responseData) {
                if (responseData.getCode() == ResponseCode.SUCCESS) {
                } else if (ErrorUtil.CheckError(responseData.getCode())) {
                    if (ErrorUtil.getInstance().isControlTokenValid(responseData.getCode())) {
                        PtzLayout();
                    }
                    ToastUtils.show(mActivity, ErrorUtil.getInstance().getErrorValue(responseData.getCode()));
                }
            }
        });
    }

    /**
     * 停止聚焦和光圈
     */
    public void ptzFiStop() {

        refreshData();
        Device device = mChannel.getParent();
        String channelId;
        if (device.isGB28181Device()) {
            channelId = mChannel.getIpc_device_channel_id();
        } else {
            channelId = mChannel.getChannel() + "";
        }
        BaseRequestParam param = new BaseRequestParam();
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        data.put("method", "ptz_fi_stop");
        data.put("comments", "stop focus and iris");
        if (device.isGB28181Device()) {
            PtzGBStopBean bean = new PtzGBStopBean(channelId);
            data.put("param", com.alibaba.fastjson.JSONObject.toJSON(bean));
        } else {
            PtzStopBean bean = new PtzStopBean(Integer.parseInt(channelId));
            data.put("param", com.alibaba.fastjson.JSONObject.toJSON(bean));
        }
        param.putAll(data);
        HashMap<String, Object> header = HeaderUtil.createHeader(MySharedPreference.getString(MySharedPreferenceKey.LoginKey.TOKEN));
        param.putAllHeader(header);
        String controlToken = MySharedPreference.getString(MySharedPreferenceKey.PTZ_CONTROL_TOKEN);
        AppImpl.getInstance(mActivity).ptzDevSendCmd(param, mEnterpriseId, mChannel.getParent().getSn(), channelId, controlToken).subscribe(new Action1<ResponseData<Object>>() {
            @Override
            public void call(ResponseData<Object> responseData) {
                if (responseData.getCode() == ResponseCode.SUCCESS) {
                } else if (ErrorUtil.CheckError(responseData.getCode())) {
                    if (ErrorUtil.getInstance().isControlTokenValid(responseData.getCode())) {
                        PtzLayout();
                    }
                    ToastUtils.show(mActivity, ErrorUtil.getInstance().getErrorValue(responseData.getCode()));
                }
            }
        });
    }

}