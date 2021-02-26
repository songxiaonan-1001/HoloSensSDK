package com.huawei.holosens.live.playback.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haibin.calendarview.CalendarView;
import com.huawei.holobase.Consts;
import com.huawei.holobase.bean.EventMsg;
import com.huawei.holobase.bean.SDKCloudVodUrl;
import com.huawei.holobase.bean.SDKRecordBean;
import com.huawei.holobase.bean.SDKRecordListBean;
import com.huawei.holobasic.consts.AppConsts;
import com.huawei.holobasic.consts.MySharedPreferenceKey;
import com.huawei.holobasic.utils.BitmapCache;
import com.huawei.holobasic.utils.DateUtil;
import com.huawei.holobasic.utils.MySharedPreference;
import com.huawei.holobasic.utils.ScreenUtils;
import com.huawei.holosens.R;
import com.huawei.holosens.base.BaseActivity;
import com.huawei.holosens.base.BaseApplication;
import com.huawei.holosens.bean.Channel;
import com.huawei.holosens.bean.ChannelType;
import com.huawei.holosens.bean.Device;
import com.huawei.holosens.bean.ProtocolType;
import com.huawei.holosens.bean.SDKTimeStamp;
import com.huawei.holosens.commons.BundleKey;
import com.huawei.holosens.consts.JVCloudConst;
import com.huawei.holosens.consts.JVEncodedConst;
import com.huawei.holosens.consts.NativeCbConsts;
import com.huawei.holosens.consts.SelfConsts;
import com.huawei.holosens.live.play.event.MsgEvent;
import com.huawei.holosens.live.play.util.MyGestureDispatcher;
import com.huawei.holosens.live.play.util.NavigationBarTools;
import com.huawei.holosens.live.play.util.StatesBarUtil;
import com.huawei.holosens.live.play.util.ToastUtilsB;
import com.huawei.holosens.live.play.util.VideoUtils;
import com.huawei.holosens.live.playback.bean.RemoteRecord;
import com.huawei.holosens.live.playback.bean.RemoteRecordDate;
import com.huawei.holosens.live.playback.bean.RemoteRecordDates;
import com.huawei.holosens.live.playback.bean.sdk.hw.HwRecordDateResult;
import com.huawei.holosens.live.playback.bean.sdk.hw.HwRecordResult;
import com.huawei.holosens.live.playback.view.ConnectView;
import com.huawei.holosens.live.playback.view.timeline.ScaleView;
import com.huawei.holosens.middleware.vod.VodTs;
import com.huawei.holosens.utils.AppFrontBackHelper;
import com.huawei.holosens.utils.JniUtil;
import com.huawei.holosens.utils.ToastUtils;
import com.huawei.holosens.view.TopBarLayout;
import com.huawei.net.retrofit.impl.AppImpl;
import com.huawei.net.retrofit.impl.ResponseListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.view.View.VISIBLE;
import static android.widget.RelativeLayout.TRUE;
import static com.huawei.holosens.consts.JVEncodedConst.WHAT_REMOTE_PRECISE_FILE_DATE_LIST;
import static com.huawei.holosens.consts.JVEncodedConst.WHAT_REMOTE_PRECISE_FILE_LIST;

public class JVBaseRemoteLinePlayActivity extends BaseActivity {

    public final static int PLAY_BACK_TYPE_LOCAL = 1;
    public final static int PLAY_BACK_TYPE_CLOUD = 2;


    private static final String TAG = "JVBaseRemoteLinePlayAct";


    /**
     * 新手引导
     */
    private ImageView mIvGuide;

    /**
     * 添加
     */
    private LinearLayout mLayoutAdd;
    private FragmentManager mFragmentManager;
//    private DevsList2Fragment mDeviceListFragment;

    /**
     * 日历
     */
    private LinearLayout calendarLayout;//日历布局，防止后面布局响应事件的遮盖控件
    private TextView mTvMonth, mTvWeek, mTvYear;
    private CalendarView mCalendarView;

    private TopBarLayout mTopBarView;

    /**
     * 播放
     */
    private RelativeLayout playLayout;
    private SurfaceView playSurface;// 视频播放view
    private SurfaceHolder surfaceholder;
    private ConnectView connectView;

    /**
     * 横竖屏功能
     */
    private LinearLayout playVerBar;// 竖屏播放工具条
    private ImageButton audioVBtn, captureVBtn, recordVBtn, fullScreenVBtn;
    private RelativeLayout playHorBar;// 横屏播放工具条
    private ImageButton captureHBtn, audioHBtn, recordHBtn;
    private ImageView mBtnCollect, mBtnCollectLand;
    private ImageView mBtnSlow, pauseBtn, mBtnFast;
    private ImageView mBtnSlowLand, mBtnPlayLand, mBtnFastLand;

    /**
     * 时间轴
     */
    private FrameLayout scrollTimeLineLayout;//2018.1.11 高档时间轴布局
    private TextView selectTimeTV, mTimeLand;//2018.1.11 标尺滑动的时间
    private ScaleView scrollTimeLineView;//, scrollTimeLineViewH;//2018.1.11 高档时间轴


    /**
     * intent
     */
    private boolean connected = false;// 视频是否已经连接了
    private String deviceFullNo;//设备号
    private String nickName;
    private int channelIndex = 0;// 通道号，从1开始
    private int connectIndex = 0;// 窗口从0开始
    private boolean horizon = false;//true：播放进来是横屏
    private String mAlarmTime = "";//报警录像查看指定的时间
    private boolean mFromLive = false;//是否从实况跳转过来

    private Device connectDevice;
    private Channel connectChannel;// 连接的通道

    private int playSurfaceHeight = 0;// 视频比例固定后，视频高度也会固定

    private String currentDate = DateUtil.getCurrentDateSimple();// 当前日期

    //    private String currentDate = "2020-07-31";// 当前日期
    private int year = Calendar.getInstance().get(Calendar.YEAR);
    private int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
    private int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

    private HashMap<String, com.haibin.calendarview.Calendar> mHasRecordDateMap = new HashMap<>();
    private boolean mRequestDateEnd; // 请求报警日期是否结束

    private ArrayList<String[]> timeList = new ArrayList<>();//当天录像文件时间列表
    private ArrayList<RemoteRecord> videoList;
    private int currentPlayIndex;//当前播放文件的index
    private String scrollTime = "";
    private String rulerDefaultPos = "00:00:00";// 标尺默认位置
    private final int FLAG_SEEKING_PROGRESS = 1314;//进度调节完成FLAG
    private boolean seekingProcess = false;//正在调节进度，由于调节进度后，还会有老数据过来，所以当seek命令发送之后，等待两秒再处理I帧的时间戳

    //todo 8倍没给图先不让切换
    private int[] speedParamArrayOct = {-2, -1, 0, 1, 2/*, 8, 16*/};//修改此数组也要修改currentSpeedIndex
    private int currentSpeedIndex = 2;//当前速度index， 对应speedParamArrayOct中1的下标
    private int defaultSpeedIndex = 2;//最中间index
//    private String[] speed28181Array = {/*"1/16","1/8",*/"1/4", "1/2", "1", "2", "4"/*,"8","16"*/}; ////修改此数组也要修改currentSpeedIndex  0 1/16 1/8 1/4 1/2 1 2 4 8 16，传入0表示暂停

    private boolean newPictureFlag = false;//是否出图
    private boolean stopFilePlay = false;//停止文件播放
    private boolean needTipPlayEnd = true;//是否需要提示播放结束，切换日历时，不需要提示

    // 是否视频未连接成功就暂停了
    private boolean isPausedNotConnect = false;
    // 是否锁屏
    private boolean isScreenOff = false;
    // 是否正在监听
    private boolean isAudioListening = false;
    // 是否暂停
    private boolean isRemotePaused = false;

    private long mCreateTime;

    private int playBackType = PLAY_BACK_TYPE_LOCAL;
    /**
     * 设备的连接类型  1：好望 2：国标
     */
    private int connectType = ProtocolType.HOLO;

    /**
     * 国标设备的channelid
     */
    private String connectGbCHannelId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //设置视频播放不锁屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_remote_play);

        initSettings();
        initUi();

        mCreateTime = System.currentTimeMillis();
    }

    private void initSettings() {
        Intent intent = getIntent();
        if (null != intent) {
            playBackType = intent.getIntExtra(BundleKey.PLAYBACK_TYPE, PLAY_BACK_TYPE_LOCAL);
            connected = intent.getBooleanExtra(BundleKey.CONNECTED, false);
            deviceFullNo = intent.getStringExtra(BundleKey.DEVICE_ID);
            nickName = intent.getStringExtra(BundleKey.NICK_NAME);
            channelIndex = intent.getIntExtra(BundleKey.CHANNEL_ID, 0);
            connectIndex = intent.getIntExtra(BundleKey.WINDOW_INDEX, 10000);
            mAlarmTime = intent.getStringExtra(BundleKey.ALARM_TIME);
            connectType = intent.getIntExtra(BundleKey.CONNECT_TYPE, ProtocolType.HOLO);
            connectGbCHannelId = intent.getStringExtra(BundleKey.GB_CHANNEL_ID);
            initSpeedData();
            //传过来指定时间，计算要播放的时间
            if (!TextUtils.isEmpty(mAlarmTime)) {
                try {
                    Log.e(TAG, "initSettings:1 " + mAlarmTime);
                    String[] s = mAlarmTime.split("T");
                    String[] split = s[0].split("-");
                    year = Integer.parseInt(split[0]);
                    month = Integer.parseInt(split[1]);
                    day = Integer.parseInt(split[2]);
                    currentDate = s[0];

                    long timeStamp = DateUtil.string2Millis(mAlarmTime.replace("T", " "), "yyyy-MM-dd HH:mm:ss");
                    timeStamp -= 10 * 1000;
                    if (timeStamp < DateUtil.string2Millis(currentDate, "yyyy-MM-dd")) {
                        timeStamp = DateUtil.string2Millis(currentDate, "yyyy-MM-dd");
                    }
                    mAlarmTime = DateUtil.millis2String(timeStamp, "HH:mm:ss");
                    Log.e(TAG, "initSettings:2 " + mAlarmTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (connected) {
                horizon = intent.getBooleanExtra("horizon", false);
            }
        }

        connectDevice = new Device();
        connectDevice.setSn(deviceFullNo);
        connectDevice.setConnect_type(connectType);

        connectChannel = new Channel(connectDevice, connectIndex, channelIndex, "");
        connectChannel.setChannel_id(connectGbCHannelId);
        playSurfaceHeight = mScreenWidth / 16 * 9;// 视频比例固定后，视频高度也会固定

    }

    private void initUi() {
        //topbar
        mTopBarView = getTopBarView();
        mTopBarView.setTopBar(R.drawable.selector_back_icon, -1,
                R.string.history_video_playback, mOnClickListener);
        //引导图
        mIvGuide = findViewById(R.id.iv_guide);
        mIvGuide.setVisibility(View.GONE);
        findViewById(R.id.linearlayout_time).setOnClickListener(mOnClickListener);
        if (!MySharedPreference.getBoolean(MySharedPreferenceKey.GuideKey.GUIDE_PLAYBACK, false)) {
            StatesBarUtil.setFitsSystemWindows(this, true);
            mTopBarView.setVisibility(View.GONE);
            mIvGuide.setVisibility(View.VISIBLE);
            mIvGuide.setOnClickListener(mOnClickListener);
        }

        //添加，设备列表
        mLayoutAdd = findViewById(R.id.layout_add);
        findViewById(R.id.btn_add).setOnClickListener(mOnClickListener);
        mFragmentManager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putInt(BundleKey.DEVICELIST2FRAGMENT_PAGE_TYPE, 1);
        initCalendar();
        initPlay();
        initFunction();
        initTimeLine();

        setPlayViewWH();

        hideBottomUIMenu();
        addHideBottomUIMenuListener();

        connectView.setConnectState(ConnectView.connecting, 0);
        mFromLive = !TextUtils.isEmpty(deviceFullNo);
        if (TextUtils.isEmpty(deviceFullNo)) {
            mLayoutAdd.setVisibility(View.VISIBLE);
            playSurface.setVisibility(View.GONE);
            connectView.setVisibility(View.GONE);
        } else {
            connect();
        }
    }

    /**
     * 初始化控制类数据
     */
    private void initSpeedData() {
        boolean isJvmpType = (playBackType == PLAY_BACK_TYPE_LOCAL && connectType == 2) || playBackType == PLAY_BACK_TYPE_CLOUD;
        if (isJvmpType) {
            currentSpeedIndex = 2;
            defaultSpeedIndex = 2;
        } else {
            currentSpeedIndex = 2;
            defaultSpeedIndex = 2;
        }

    }

    private HashMap<String, RemoteRecordDates> localVideoDatas = new HashMap<>();


    /**
     * 本地录像设置日历的数据
     *
     * @param remoteRecordDates
     */
    private void setCalendarDatas(RemoteRecordDates remoteRecordDates) {
        if (remoteRecordDates != null && remoteRecordDates.getDate_list() != null) {
            List<RemoteRecordDate> list = remoteRecordDates.getDate_list();
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    RemoteRecordDate remoteRecordDate = list.get(i);
                    try {
                        com.haibin.calendarview.Calendar calendar = new com.haibin.calendarview.Calendar();
                        calendar.setYear(remoteRecordDate.getYear());
                        calendar.setMonth(remoteRecordDate.getMonth());
                        calendar.setDay(remoteRecordDate.getDay());
                        calendar.setScheme(String.format("%02d", remoteRecordDate.getDay()));
                        mHasRecordDateMap.put(calendar.toString(), calendar);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                mCalendarView.setSchemeDate(mHasRecordDateMap);
            }
        }
    }

    /**
     * 设置标题栏右边view状态
     */
    private void setTopBarRightView() {
        if (playBackType == PLAY_BACK_TYPE_LOCAL) {
            mTopBarView.setRightText(getString(R.string.play_back_cloud));
        } else {
            mTopBarView.setRightText(getString(R.string.play_back_local));
        }
        mTopBarView.setRightListener(mOnClickListener);
    }

    private void initCalendar() {
        calendarLayout = findViewById(R.id.calendar_layout);
        calendarLayout.setOnClickListener(mOnClickListener);
        mTvMonth = findViewById(R.id.tv_selected_month);
        mTvWeek = findViewById(R.id.tv_selected_week);
        mTvYear = findViewById(R.id.tv_selected_year);
        mTvMonth.setText(String.format("%02d", month));
        mTvYear.setText(String.valueOf(year));
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
        mTvWeek.setText("周" + getResources().getStringArray(R.array.array_calendar_week)[dayOfWeek]);
        mCalendarView = findViewById(R.id.calendar_view);
        if (!TextUtils.isEmpty(mAlarmTime)) {
            mCalendarView.scrollToCalendar(year, month, day);
        }
        mCalendarView.setOnMonthChangeListener(new CalendarView.OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month) {
                if (null == mHasRecordDateMap) {
                    mHasRecordDateMap = new HashMap<>();
                } else {
                    mHasRecordDateMap.clear();
                }
                if (connectType == 1 && playBackType == PLAY_BACK_TYPE_LOCAL) {
//                    //查询有录像的日期
//                    int res = PlayBackPreciseUtil.preciseCheckDate(connectIndex, channelIndex, year, month);
//                    //请求失败，请求结束；请求成功，在回调处判断
//                    mRequestDateEnd = res < 0;
                }
                mTvMonth.setText(String.format("%02d", month));
                mTvYear.setText(String.valueOf(year));

            }
        });
        mCalendarView.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
            @Override
            public void onCalendarOutOfRange(com.haibin.calendarview.Calendar calendar) {

            }

            @Override
            public void onCalendarSelect(com.haibin.calendarview.Calendar calendar, boolean isClick) {
                mTvMonth.setText(String.format("%02d", calendar.getMonth()));
                mTvYear.setText(String.valueOf(calendar.getYear()));
                java.util.Calendar selectCalendar = java.util.Calendar.getInstance();
                selectCalendar.setTimeInMillis(mCalendarView.getSelectedCalendar().getTimeInMillis());
                int dayOfWeek = selectCalendar.get(java.util.Calendar.DAY_OF_WEEK) - 1;
                mTvWeek.setText("周" + getResources().getStringArray(R.array.array_calendar_week)[dayOfWeek]);

            }
        });
    }

    private void initPlay() {
        playLayout = findViewById(R.id.play_layout);
        playSurface = findViewById(R.id.playsurface);
        playSurface.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dispatcher.motion(event, false);
                return true;// true：响应缩放
            }

        });
        surfaceholder = playSurface.getHolder();
        connectView = findViewById(R.id.connectview);
        connectView.mTouchArea.setOnClickListener(mOnClickListener);

    }

    private void initFunction() {
        playVerBar = findViewById(R.id.remote_vertical_bar_line);
        audioVBtn = findViewById(R.id.audio_vbtn);
        captureVBtn = findViewById(R.id.capture_vbtn);
        recordVBtn = findViewById(R.id.record_vbtn);
        mBtnCollect = findViewById(R.id.btn_portrait_collect);
        fullScreenVBtn = findViewById(R.id.fullscreen_vbtn);

        playHorBar = findViewById(R.id.remote_horizon_bar_line);
        mBtnCollectLand = findViewById(R.id.btn_land_collect);
        audioHBtn = findViewById(R.id.audio_hbtn);
        captureHBtn = findViewById(R.id.capture_hbtn);
        recordHBtn = findViewById(R.id.record_hbtn);

        mBtnSlow = findViewById(R.id.btn_slow);
        pauseBtn = findViewById(R.id.play_pause_img);
        mBtnFast = findViewById(R.id.btn_fast);
        mBtnSlowLand = findViewById(R.id.btn_land_slow);
        mBtnPlayLand = findViewById(R.id.btn_land_pause_play);
        mBtnFastLand = findViewById(R.id.btn_land_fast);


        audioVBtn.setOnClickListener(mOnClickListener);
        captureVBtn.setOnClickListener(mOnClickListener);
        recordVBtn.setOnClickListener(mOnClickListener);
        mBtnCollect.setOnClickListener(mOnClickListener);
        fullScreenVBtn.setOnClickListener(mOnClickListener);

        mBtnSlow.setOnClickListener(mOnClickListener);
        pauseBtn.setOnClickListener(mOnClickListener);
        mBtnFast.setOnClickListener(mOnClickListener);
        mBtnSlowLand.setOnClickListener(mOnClickListener);
        mBtnPlayLand.setOnClickListener(mOnClickListener);
        mBtnFastLand.setOnClickListener(mOnClickListener);

        findViewById(R.id.left_hbtn).setOnClickListener(mOnClickListener);
        mBtnCollectLand.setOnClickListener(mOnClickListener);
        audioHBtn.setOnClickListener(mOnClickListener);
        captureHBtn.setOnClickListener(mOnClickListener);
        recordHBtn.setOnClickListener(mOnClickListener);
    }

    private void initTimeLine() {
        scrollTimeLineLayout = findViewById(R.id.time_scale_view_layout);
        scrollTimeLineView = findViewById(R.id.scaleview);
        selectTimeTV = findViewById(R.id.select_time);
        selectTimeTV.setText(getSelectTime(rulerDefaultPos));
        mTimeLand = findViewById(R.id.select_time_land);
        scrollTimeLineView.setAllBlockNum(80);
        scrollTimeLineView.setCenterSecond(0);
        scrollTimeLineView.scrollToTime(rulerDefaultPos);
        scrollTimeLineView.setCanScroll(false);
        if (TextUtils.isEmpty(deviceFullNo)) {
            scrollTimeLineView.setDisableScrollTip(getString(R.string.wait_add_channel));
        } else {
            scrollTimeLineView.setDisableScrollTip(getString(R.string.loading_data));
        }
    }


    /**
     * 隐藏虚拟按键 2019.5.5
     */
    private void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT < 19) { // lower api
            View v = getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
        //设置状态栏文字颜色及图标为深色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }

    /**
     * 监听虚拟按钮,如果显示立即隐藏
     */
    private void addHideBottomUIMenuListener() {
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == View.VISIBLE) {
                    hideBottomUIMenu();
                }
            }
        });
    }

    /**
     * 连接方法
     */
    private void connect() {
        mLayoutAdd.setVisibility(View.GONE);
        playSurface.setVisibility(View.VISIBLE);
        connectView.setVisibility(View.VISIBLE);

        searchRecordList();

        surfaceholder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                surfaceholder = holder;

//                Log.e(TAG, "surfaceCreated: " + connectView.getConnectState()
//                        + ", " + isPausedNotConnect + ", " + isScreenOff);
//                if (connectView.isConnected()) {
//                    if (connectView.getConnectState() != ConnectView.connectedNoData) {
//                        connectView.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                isRemotePaused = true;
//                                pauseOrGoonPlay();
//                            }
//                        }, 500);
//                    }
//                } else {
//                    // 继续播放视频
////                    FunctionUtil.resumeSurface(connectIndex, surfaceholder.getSurface());
//                    if (isPausedNotConnect) {
//                        connect();
//                    } else {
////                        PlayBackUtil.enableRemotePlay(connectIndex, true);
//                    }
//                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

        });
    }

    /**
     * 设置播放区域的大小
     */
    private void setPlayViewWH() {
        try {
            if (null == connectChannel) {
                return;
            }
            connectChannel.setLastPortLeft(0);
            connectChannel.setLastPortBottom(0);
            if (isLandScape()) {
                //隐藏状态栏
                StatesBarUtil.setFitsSystemWindows(this, true);
                //隐藏导航栏
                if (NavigationBarTools.hasNavBar(this) && NavigationBarTools.isNavBarVisible(this)) {
                    NavigationBarTools.setNavBarVisibility(this, false);
                }

                //适配刘海屏
                Rect outRect = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
                int measureWidth = mScreenHeight;
                if (outRect.width() != 0) {
                    measureWidth = outRect.width();
                }
                Log.e(TAG, "setPlayViewWH: screen.height=" + mScreenHeight + ", rect.width=" + outRect.width());

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        measureWidth, mScreenWidth);
                playLayout.setLayoutParams(layoutParams);
                connectView.setLayoutParams(layoutParams);
                connectView.setRequestedOrientation(true);

                mTopBarView.setVisibility(View.GONE);

                if (!TextUtils.isEmpty(deviceFullNo)) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) findViewById(R.id.layout_playback_function).getLayoutParams();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        params.removeRule(RelativeLayout.BELOW);
                    } else {
                        params.addRule(RelativeLayout.BELOW, 0);
                    }
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, TRUE);
                    params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                    playVerBar.setVisibility(View.GONE);
                    scrollTimeLineLayout.setVisibility(View.GONE);
//                    mTimeLand.setVisibility(View.VISIBLE);
                    findViewById(R.id.layout_playback_function).setBackgroundResource(R.color.transparent);
                    findViewById(R.id.layout_playback_speed).setVisibility(View.GONE);
                }
                scrollTimeLineView.getLayoutParams().height = ScreenUtils.dip2px(54);

                findViewById(R.id.left_hbtn).setVisibility(View.VISIBLE);
                playHorBar.setVisibility(View.GONE);//横屏按钮默认先隐藏
                mTimeLand.setVisibility(View.GONE);
                scrollTimeLineView.setVisibility(View.GONE);//横屏时间轴跟随按钮显隐

            } else {
                if (mIvGuide.getVisibility() != View.VISIBLE) {
                    //显示状态栏
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    //显示导航栏
                    NavigationBarTools.setNavBarVisibility(this, true);
                }

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        mScreenWidth, playSurfaceHeight);
                playLayout.setLayoutParams(layoutParams);
                connectView.setLayoutParams(layoutParams);
                connectView.setRequestedOrientation(false);

                if (changeScreen4Add) {
                    changeScreen4Add = false;
                    mTopBarView.setVisibility(View.GONE);
                } else {
                    if (mIvGuide.getVisibility() != View.VISIBLE) {
                        mTopBarView.setVisibility(View.VISIBLE);
                    }
                }

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) findViewById(R.id.layout_playback_function).getLayoutParams();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                } else {
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
                }
                params.addRule(RelativeLayout.BELOW, playLayout.getId());
                params.height = RelativeLayout.LayoutParams.MATCH_PARENT;
                playVerBar.setVisibility(View.VISIBLE);
                scrollTimeLineLayout.setVisibility(View.VISIBLE);
                mTimeLand.setVisibility(View.GONE);
                findViewById(R.id.layout_playback_function).setBackgroundResource(R.color.bg_play_function);
                findViewById(R.id.layout_playback_speed).setVisibility(View.VISIBLE);

                scrollTimeLineView.setVisibility(View.VISIBLE);
                scrollTimeLineView.getLayoutParams().height = ScreenUtils.dip2px(62);

                findViewById(R.id.left_hbtn).setVisibility(View.GONE);
                playHorBar.setVisibility(View.GONE);
                handler.removeMessages(SelfConsts.WHAT_PLAY_DISPLAY_NAV_BTN);

            }

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) playSurface.getLayoutParams();
            if (connectChannel.getWidth() == 0 || connectChannel.getHeight() == 0) {
                layoutParams.width = playLayout.getLayoutParams().width;
                layoutParams.height = playLayout.getLayoutParams().height;
            } else {
                float playWidth, playHeight;
                //左右留边
                if ((float) playLayout.getLayoutParams().width / playLayout.getLayoutParams().height
                        > (float) connectChannel.getWidth() / connectChannel.getHeight()) {
                    playHeight = playLayout.getLayoutParams().height;
                    playWidth = (float) playLayout.getLayoutParams().height / connectChannel.getHeight() * connectChannel.getWidth();
                } else {//上下留边
                    playWidth = playLayout.getLayoutParams().width;
                    playHeight = (float) playLayout.getLayoutParams().width / connectChannel.getWidth() * connectChannel.getHeight();
                }
                layoutParams.width = (int) playWidth;
                layoutParams.height = (int) playHeight;
            }
            playSurface.setLayoutParams(layoutParams);
            connectChannel.setLastPortWidth(layoutParams.width);
            connectChannel.setLastPortHeight(layoutParams.height);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        dismissSnapWindow();
        setPlayViewWH();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: " + isScreenOff + ", " + connectView.getConnectState());
        //vivo手机开屏之后surface不重新走surfacecreate，在此特殊处理
        if (isScreenOff) {
            isScreenOff = false;
            if (TextUtils.equals(Build.BRAND.toLowerCase(), "vivo")) {
                if (connectView.isConnected()) {
                    if (connectView.getConnectState() != ConnectView.connectedNoData) {
                        pauseOrGoonPlay();
                    }
                } else {
                    if (isPausedNotConnect) {
                        // 继续播放视频
//                        FunctionUtil.resumeSurface(connectIndex, surfaceholder.getSurface());
                        connect();
                    } else {
//                        PlayBackUtil.enableRemotePlay(connectIndex, true);
                    }
                }
            }
        }
        scrollTimeLineView.scrollToTime(scrollTime);
    }

    @Override
    protected void onPause() {
        if (isLandScape()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if (connectView.isConnected()) {
            Log.e(TAG, "onPause, paused=" + isRemotePaused);
            stopRemote2Func();
            if (isRemotePaused) {
                //手动暂停的不做任何处理
            } else {
                //视频播放中时home键，需要暂停视频
                pauseOrGoonPlay();
            }
        } else {
            if (!TextUtils.isEmpty(deviceFullNo) &&
                    !TextUtils.equals(connectView.getLinkState(), getString(R.string.play_over))) {
                isPausedNotConnect = true;

                if (connected) {
                    stopRemotePlay();
                } else if (!isFinishing()) {
//                    new Thread() {
//                        @Override
//                        public void run() {
//                            FunctionUtil.disconnect(connectIndex);
//                            connectIndex++;
//                        }
//                    }.start();
                    JniUtil.disConnect(connectIndex);
                    connectIndex++;
                }
            }
        }
        super.onPause();

    }

    @Override
    public void onBackPressed() {

        if (calendarLayout.getVisibility() == View.VISIBLE) {
            calendarLayout.setVisibility(View.GONE);
            return;
        }

        if (System.currentTimeMillis() - mCreateTime < 1000) {
            return;
        }

        if (isLandScape()) {
            if (isRemotePaused) {
                pauseOrGoonPlay();
            }
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {

            if (!TextUtils.isEmpty(deviceFullNo)) {
                stopRemote3Func();
//                if (connected) {
//                    stopRemotePlay();
//                } else {
                JniUtil.disConnect(connectIndex);
//                }
            }

            dismissLoading();
            finish();
        }
    }

    @Override
    public void onNotify(int what, int arg1, int arg2, Object obj) {
        handler.sendMessage(handler.obtainMessage(what, arg1, arg2, obj));
    }

    @Override
    public void onHandler(int what, int arg1, int arg2, Object obj) {
        if (obj != null) {
            Log.e(TAG, "onHandler:  what:" + what + ",arg1=" + arg1 + ", arg2=" + arg2 + ", obj=" + obj.toString());
        }
        switch (what) {
            case NativeCbConsts.EVENT_TYPE_HPET_RECORDS_QUERY://录像查询
                if (null != obj) {
                    HwRecordResult result = new Gson().fromJson(obj.toString(), HwRecordResult.class);
                    this.onNotify(WHAT_REMOTE_PRECISE_FILE_LIST, 0, 0, VodTs.transmitHwLocalRecord(result.getResult().getRecords(), currentDate));
                }
                break;
            case NativeCbConsts.EVENT_TYPE_HPET_RECORD_DATES_QUERY://录像日期列表查询
                if (null != obj) {
                    HwRecordDateResult result = new Gson().fromJson(obj.toString(), HwRecordDateResult.class);
                    if (null != result.getResult()) {
                        this.onNotify(WHAT_REMOTE_PRECISE_FILE_DATE_LIST, 0, 0, VodTs.transmitLocalRecordDate(result.getResult().getDates()));
                    }
                }
                break;
            case NativeCbConsts.EVENT_TYPE_HPET_PLAY_TIME_POS://播放时间点(回放)
                if (scrollTimeLineView.isManuScroll()) {
                    //手动滑动时不滚动时间轴
                    Log.e(TAG, "onHandler: what: rulerDefaultP不滚动");
                } else {
                    String time = new Gson().fromJson(obj.toString(), SDKTimeStamp.class).getResult().getTime_pos();
                    if (!isJvmpConn()) {
                        sdkLocalTimeStimpToStringTime(time);
                    } else {
                        sdkTimeStimpToStringTime(time);
                    }
                    Log.e(TAG, "onHandler:  what: rulerDefaultPos" + rulerDefaultPos);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            scrollTimeLineView.scrollToTime(rulerDefaultPos);
                        }
                    });
                }
                break;
//            //时间戳回调
//            case CALL_JVMP_PLAYBACK_TIMESTAMP: {
//                if (scrollTimeLineView.isManuScroll()) {
//                    //手动滑动时不滚动时间轴
//                    Log.e(TAG, "onHandler: what: rulerDefaultP不滚动");
//                } else {
//                    long time = new Gson().fromJson(obj.toString(), JvmpTimeStamp.class).getJvmp_timeStamp();
//                    timeStimpToStringTime(time);
//                    Log.e(TAG, "onHandler:  what: rulerDefaultPos" + rulerDefaultPos);
//                    scrollTimeLineView.scrollToTime(rulerDefaultPos);
//                }
//                break;
//            }
            //视频连接回调
            case JVEncodedConst.WHAT_CONNECT_CHANGE: {
                if (connectIndex == arg1) {
                    switch (arg2) {
                        // 1 -- 连接成功
                        case JVEncodedConst.CCONNECTTYPE_CONNOK:
                        case JVEncodedConst.CCONNECTTYPE_CONNOK_NO_LIVE: {
                            // 101 -- 连接成功(只连接不返回实时流)
                            if ((playBackType == PLAY_BACK_TYPE_LOCAL && connectType == 2) || playBackType == PLAY_BACK_TYPE_CLOUD) {
                                //如果回放类型为本地并且设备连接类型为国标协议  或者   回放类型为云端
                                Log.e("dongdz", "&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                                return;
                            }
                            //好望
                            if (!AppFrontBackHelper.isAppOnForeground(mActivity)) {
                                //如果应用不在前台,断开视频连接
//                                FunctionUtil.disconnect(connectIndex);
                                JniUtil.disConnect(connectIndex);
                                connectIndex++;
                                break;
                            }
                            connectView.setConnectState(ConnectView.buffering1, 0);
                            //启用远程回放，否则会播放实时视频数据
//                            PlayBackUtil.enableRemotePlay(connectIndex, true);

                            //1、跳转到默认播放时间点进行播放
                            if (!TextUtils.isEmpty(mAlarmTime)) {
                                playRemoteAtTime(mAlarmTime);
                                selectTimeTV.setText(mAlarmTime);
                                mTimeLand.setText(mAlarmTime);
                                mAlarmTime = "";
                            } else {
                                playRemoteAtTime(rulerDefaultPos);
                                selectTimeTV.setText(rulerDefaultPos);
                                mTimeLand.setText(rulerDefaultPos);
                            }
                            //2、同时查询有录像的日期用来绘制日历
//                            int res = PlayBackPreciseUtil.preciseCheckDate(connectIndex, channelIndex, year, month);
//                            //请求失败，请求结束；请求成功，在回调处判断
//                            mRequestDateEnd = res < 0;
                            //3、同时查询当天的录像文件用来绘制时间轴
//                            searchRemoteData(year, month, day);
                            break;
                        }
                        // 2 -- 断开连接成功
                        case JVEncodedConst.CCONNECTTYPE_DISOK:
                            // -3 -- 断开连接成功
                        case JVEncodedConst.CCONNECTTYPE_DISOK2:
                            // 8 -- 断开连接失败
                        case JVEncodedConst.CCONNECTTYPE_DISF: {
                            connectView.setConnectState(ConnectView.disconnected, 0);
                            break;
                        }
                        // 3 -- 不必要重复连接
                        case JVEncodedConst.CCONNECTTYPE_RECONN:
                            // 5 -- 没有连接
                        case JVEncodedConst.CCONNECTTYPE_NOCONN: {
                            connectView.setConnectState(ConnectView.disconnected, 0);
//                            FunctionUtil.disconnect(connectIndex);
                            JniUtil.disConnect(connectIndex);
                            connectIndex++;
                            connected = false;
                            EventMsg event = new EventMsg();
                            event.setMsgTag(MsgEvent.MSG_EVENT_ABNORMAL_DISCONNECT);
                            EventBus.getDefault().post(event);
                            break;
                        }
                        // 4 -- 连接失败
                        case JVEncodedConst.CCONNECTTYPE_CONNERR: {
                            connectView.setConnectState(ConnectView.connectFailed, R.string.connect_failed);
//                            FunctionUtil.disconnect(connectIndex);
                            JniUtil.disConnect(connectIndex);
                            connectIndex++;
                            connected = false;
                            EventMsg event = new EventMsg();
                            event.setMsgTag(MsgEvent.MSG_EVENT_ABNORMAL_DISCONNECT);
                            EventBus.getDefault().post(event);
                            break;
                        }
                        default: {
                            if (arg2 != JVEncodedConst.CCONNECTTYPE_CLOUD_TURN) {
                                stopRemote3Func();
//                                FunctionUtil.disconnect(connectIndex);
                                JniUtil.disConnect(connectIndex);
                                connectIndex++;
                                connectView.setConnectState(ConnectView.disconnected, R.string.abnormal_closed);
                                connected = false;
                                MsgEvent event = new MsgEvent();
                                event.setMsgTag(MsgEvent.MSG_EVENT_ABNORMAL_DISCONNECT);
                                EventBus.getDefault().post(event);
                            }
                            break;
                        }
                    }
                }
                break;
            }
            // O帧出图
            case JVEncodedConst.WHAT_NORMAL_DATA: {
                operDeviceO(obj);
                break;
            }
            //I帧出图
            case JVEncodedConst.WHAT_NEW_PICTURE: {// I帧
                JniUtil.show(connectIndex, surfaceholder);
                newPictureFlag = true;
                operDeviceI(obj);
                break;
            }
            //查询有录像日期的回调，用来绘制日历
            case WHAT_REMOTE_PRECISE_FILE_DATE_LIST: {
                mRequestDateEnd = true;
                if (null != obj) {
                    ArrayList<RemoteRecordDate> dateList = (ArrayList<RemoteRecordDate>) obj;
                    if (null == mHasRecordDateMap) {
                        mHasRecordDateMap = new HashMap<>();
                    } else {
                        mHasRecordDateMap.clear();
                    }
                    if (null != dateList && dateList.size() > 0) {//有数据

                        for (int i = 0; i < dateList.size(); i++) {
                            RemoteRecordDate remoteRecordDate = dateList.get(i);
                            try {
                                com.haibin.calendarview.Calendar calendar = new com.haibin.calendarview.Calendar();
                                calendar.setYear(remoteRecordDate.getYear());
                                calendar.setMonth(remoteRecordDate.getMonth());
                                calendar.setDay(remoteRecordDate.getDay());
                                calendar.setScheme(String.format("%02d", remoteRecordDate.getDay()));
                                mHasRecordDateMap.put(calendar.toString(), calendar);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        mCalendarView.setSchemeDate(mHasRecordDateMap);
                    }
                }
                break;
            }
            //查询某天录像文件的回调，用来绘制时间轴
            case WHAT_REMOTE_PRECISE_FILE_LIST: {
                if (null != obj) {
                    videoList = (ArrayList<RemoteRecord>) obj;
                    if (null != videoList && videoList.size() > 0) {
                        if (null != timeList) {
                            timeList.clear();
                        } else {
                            timeList = new ArrayList<>();
                        }
                        String nowDate = currentDate.replace("-", "");
                        for (int i = 0; i < videoList.size(); i++) {
                            RemoteRecord remoteRecord = videoList.get(i);
                            //移除非当前日期的文件
//                            if (!remoteRecord.getFilePath().contains(nowDate)) {
//                                videoList.remove(i);
//                                i--;
//                            } else {
                            String[] timeArray = {remoteRecord.getStartTime(), remoteRecord.getEndTime(), String.valueOf(remoteRecord.getRecordType())};
                            timeList.add(timeArray);
//                            }
                        }
                        if (null != timeList && 0 != timeList.size()) {
                            needTipPlayEnd = true;
                            scrollTimeLineView.setTimeList(timeList);
                            setNumberListener(true);
                        } else {
                            //提示无远程文件
                            connectView.setConnectState(ConnectView.connectedNoData, R.string.video_nodata_failed);
                            scrollTimeLineView.setDisableScrollTip(getString(R.string.wait_connect));
                            selectTimeTV.setText(getSelectTime(rulerDefaultPos));
                            mTimeLand.setText(rulerDefaultPos);
                        }
                    } else {
                        //提示无远程文件
                        connectView.setConnectState(ConnectView.connectedNoData, R.string.video_nodata_failed);
                        scrollTimeLineView.setDisableScrollTip(getString(R.string.wait_connect));
                        selectTimeTV.setText(getSelectTime(rulerDefaultPos));
                        mTimeLand.setText(rulerDefaultPos);
                    }
                } else {
                    //提示无远程文件
                    connectView.setConnectState(ConnectView.connectedNoData, R.string.video_nodata_failed);
                    scrollTimeLineView.setDisableScrollTip(getString(R.string.wait_connect));
                    selectTimeTV.setText(getSelectTime(rulerDefaultPos));
                    mTimeLand.setText(rulerDefaultPos);
                }
                break;
            }
            //远程回放数据,O,I,B,P
            case JVEncodedConst.WHAT_REMOTE_PLAY_DATA: {
                Log.e("dongdz", "**********************************arg2:" + arg2);
                switch (arg2) {
                    case JVEncodedConst.JVN_DATA_O: {
                        operDeviceO(obj);
                        break;
                    }
                    case JVEncodedConst.JVN_DATA_I: {
                        operDeviceI(obj);
                        break;
                    }
                    case JVEncodedConst.JVN_DATA_B: {
                        Log.e(TAG, getLocalClassName() + "--BFrame = " + obj.toString());
                        break;
                    }
                    case JVEncodedConst.JVN_DATA_P: {
                        Log.e(TAG, getLocalClassName() + "--PFrame = " + obj.toString());
                        break;
                    }
                    default:
                        break;
                }
                break;
            }

            case FLAG_SEEKING_PROGRESS: {//进度调节完成
                seekingProcess = false;
                Log.e(TAG, "P2-seekingProcess=" + seekingProcess);
                break;
            }

            case SelfConsts.WHAT_REMOTE_PLAY_AT_INDEX: {

                scrollTimeLineView.setManuScroll(false);
                Log.e(TAG, "ppppp2_setManuScroll=false");
                if (null == videoList || 0 == videoList.size()) {
                    break;
                }

                if (AppConsts.DEBUG_STATE) {
                    ToastUtils.show(JVBaseRemoteLinePlayActivity.this, "playAt=" + currentDate + " " + scrollTime + ";fileIndex=" + arg1);
                }

                //如果正在录像，停止录像
                if (connectView.isRecording()) {
                    record(true);
                }

                //视频暂停情况下，滑动时间轴，将视频暂停取消
                if (isRemotePaused) {
                    pauseOrGoonPlay();
                }

                if (connectView.isConnected()) {
//                    connectView.setConnectState(ConnectView.buffering1, 0);
                    Log.e(TAG, "buffering-----------2");

                    handler.removeMessages(FLAG_SEEKING_PROGRESS);
                    seekingProcess = true;
                    Log.e(TAG, "P1-seekingProcess=" + seekingProcess);

//                    if (playBackType == PLAY_BACK_TYPE_CLOUD) {
//                        PlayBackPreciseUtil.preciseSeekToTime(connectIndex, currentDate, scrollTime, JVEncodedConst.CONNECT_BY_JVMP);
//                    } else
//                        PlayBackPreciseUtil.preciseSeekToTime(connectIndex, currentDate, scrollTime, connectType == 1 ? JVEncodedConst.CONNECT_BY_CLOUDSEE2 : JVEncodedConst.CONNECT_BY_JVMP);
                    JniUtil.holosensPlayerSkip(connectIndex, currentDate + " " + scrollTime);

                    handler.sendEmptyMessageDelayed(FLAG_SEEKING_PROGRESS, 2 * 1000);
                } else {
                    playRemoteAtTime(scrollTime);
                }

                if (arg1 < 0) {
                    newPictureFlag = true;
                }
                break;
            }

            //5s隐藏横屏功能按钮
            case SelfConsts.WHAT_PLAY_DISPLAY_NAV_BTN: {
                if (isLandScape()) {
                    playHorBar.setVisibility(View.GONE);
                    mTimeLand.setVisibility(View.GONE);
                    scrollTimeLineView.setVisibility(View.GONE);//横屏时间轴跟随按钮显隐
                }
                break;
            }

            //隐藏抓拍弹窗
            case WHAT_DISMISS_POPUPWINDOW:
                dismissSnapWindow();
                break;

            //锁屏
            case SelfConsts.WHAT_ON_SCREEN_OFF:
                Log.e(TAG, "onHandler:WHAT_ON_SCREEN_OFF ");
                isScreenOff = true;
                break;
            //解锁屏幕
            case SelfConsts.WHAT_ON_SCREEN_ON:
                Log.e(TAG, "onHandler:WHAT_ON_SCREEN_ON ");
                break;
            default:
                break;
        }

    }

    /**
     * 处理设备的i帧
     *
     * @param obj
     */
    private void operDeviceI(Object obj) {
        connectView.setConnectState(ConnectView.connected, 0);
        boolean isJvmpType = (playBackType == PLAY_BACK_TYPE_LOCAL && connectType == 2) || playBackType == PLAY_BACK_TYPE_CLOUD;
        if (isJvmpType) {
            if (currentSpeedIndex == 2) {
                pauseBtn.setImageResource(R.mipmap.ic_play_speed_portrait_playing);
                mBtnPlayLand.setImageResource(R.mipmap.ic_play_speed_land_playing);
            }
        } else {
            if (currentSpeedIndex == 2) {
                pauseBtn.setImageResource(R.mipmap.ic_play_speed_portrait_playing);
                mBtnPlayLand.setImageResource(R.mipmap.ic_play_speed_land_playing);
            }
        }


        if (!newPictureFlag) {
//                            break;
        } else {
            scrollTimeLineView.setManuScroll(false);
            Log.e(TAG, "ppppp3_setManuScroll=false");
        }

        if (seekingProcess || isRemotePaused) {
            if (obj != null) {
                Log.v(TAG, "I frame,P3-seekingProcess=" + seekingProcess + ";time=" + obj.toString());

            }
            return;
        }

        if (null != obj) {
            String time = obj.toString();
            Log.e(TAG, "nTimestamp-newTime=" + time);
            if (scrollTimeLineView.isManuScroll()) {
                //手动滑动时不滚动时间轴
            } else {
                connectView.setConnectState(ConnectView.connected, 0);

                scrollTimeLineView.scrollToTime(time);
                Log.e(TAG, "I frame,P4-seekingProcess=" + seekingProcess + ";time=" + time);

                selectTimeTV.setText(getSelectTime(time));
                mTimeLand.setText(time);
            }
        }
    }

    /**
     * 处理设备的o帧逻辑
     *
     * @param obj
     */
    private void operDeviceO(Object obj) {
        //正在录像，停止录像
        if (connectView.isRecording()) {
            stopRecord(true);
        }
        connectView.setConnectState(ConnectView.connected, 0);

        try {
            JSONObject jObj = new JSONObject(obj.toString());
            if (null != jObj && null != connectChannel) {
                connectChannel.setWidth(jObj.getInt("width"));
                connectChannel.setHeight(jObj.getInt("height"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 设置视频的宽高
        setPlayViewWH();
    }

    private long lastClickTime = 0;

    //横屏点击添加会自动切换到竖屏，以此标记
    private boolean changeScreen4Add = false;
    View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            long currentClickTime = System.currentTimeMillis();
            if (currentClickTime - lastClickTime <= BaseApplication.CLICK_DIS_TIME) {
                return;
            }
            switch (view.getId()) {
                //新手引导页
                case R.id.iv_guide:
                    StatesBarUtil.setFitsSystemWindows(mActivity, false);
                    mTopBarView.setVisibility(View.VISIBLE);
                    mIvGuide.setVisibility(View.GONE);
                    MySharedPreference.putBoolean(MySharedPreferenceKey.GuideKey.GUIDE_PLAYBACK, true);
                    break;
                //添加设备
                case R.id.btn_add:
//                    if (isLandScape()) {
//                        changeScreen4Add = true;
//                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                    }
//                    mTopBarView.setVisibility(View.GONE);
//                    mFragmentManager.beginTransaction().show(mDeviceListFragment).commitAllowingStateLoss();
                    break;
                //返回
                case R.id.left_btn:
                case R.id.left_hbtn: {// 竖屏和横屏返回
                    onBackPressed();
                    break;
                }
                //日历
                case R.id.linearlayout_time: {
                    if (TextUtils.isEmpty(deviceFullNo)) {
                        ToastUtils.show(JVBaseRemoteLinePlayActivity.this, R.string.wait_add_channel);
                        return;
                    }
                    if (connectType == 1 && playBackType == PLAY_BACK_TYPE_LOCAL) {
//                        if (!mRequestDateEnd) {
//                            ToastUtils.show(JVBaseRemoteLinePlayActivity.this, R.string.loading_data);
//                            return;
//                        }
                        if (mRequestDateEnd && mHasRecordDateMap.size() == 0) {
//                            int res = PlayBackPreciseUtil.preciseCheckDate(connectIndex, channelIndex, year, month);
//                            //请求失败，请求结束；请求成功，在回调处判断
//                            mRequestDateEnd = res < 0;
                        }
                    }
                    if (View.VISIBLE == calendarLayout.getVisibility()) {
                        calendarLayout.setVisibility(View.GONE);
//                        mTopBarView.setPulldownIconRes(R.mipmap.icon_tree_down_normal);
//                        setTopBarRightView();
                    } else {
                        calendarLayout.setVisibility(View.VISIBLE);
                        mCalendarView.scrollToCalendar(year, month, day);
//                        mTopBarView.setPulldownIconRes(R.mipmap.icon_tree_up_pressed);
                        mTopBarView.setRightText(getResources().getString(R.string.sure));
                        mTopBarView.setRightListener(mOnClickListener);
                    }
                    break;
                }
                //日历确定
                case R.id.right_btn: {
                    //有录像切换 todo
                    String operStr = mTopBarView.getmRightText().getText().toString().trim();
                    //日历确定
                    if (!TextUtils.isEmpty(operStr) && operStr.equals(getString(R.string.sure))) {
                        if (true || mCalendarView.getSelectedCalendar().hasScheme()) {
                            calendarLayout.setVisibility(View.GONE);
//                            mTopBarView.setPulldownIconRes(R.mipmap.icon_tree_down_normal);
//                            setTopBarRightView();
                            mTopBarView.setRightText("");

                            year = mCalendarView.getSelectedCalendar().getYear();
                            month = mCalendarView.getSelectedCalendar().getMonth();
                            day = mCalendarView.getSelectedCalendar().getDay();

//                            mTopBarView.setTitle();
//                            currentDate = mTopBarView.getTitle();
                            String data = String.format(AppConsts.FORMATTER_REMOTE_DATE, year, month, day);
                            currentDate = data;
                            selectTimeTV.setText(getSelectTime(rulerDefaultPos));

                            calendarLayout.setVisibility(View.GONE);
//                            mTopBarView.setPulldownIconRes(R.mipmap.icon_tree_down_normal);
//                            setTopBarRightView();

                            needTipPlayEnd = false;

                            //倍速播放恢复默认
                            resetSpeed();

                            stopRemote3Func();
                            stopRemotePlay();
                            timeList.clear();
                            scrollTimeLineView.setTimeList(timeList);
                            JniUtil.disConnect(connectIndex);
                            connectIndex++;
                            connectView.setConnectState(ConnectView.connecting, 0);
                            searchRecordList();
//                            if (!isJvmpConn()) {
//                                //1、跳转到默认播放时间点进行播放
//                                playRemoteAtTime(rulerDefaultPos);
//                                selectTimeTV.setText(rulerDefaultPos);
//                                mTimeLand.setText(rulerDefaultPos);
//                                //2、同时查询要切换天的录像文件用来绘制新的时间轴
////                                searchRemoteData(year, month, day);
//                            } else {
//
//                                connectView.setConnectState(ConnectView.connecting, 0);
//                                searchRecordList();
//                            }

                        } else {
                            ToastUtils.show(JVBaseRemoteLinePlayActivity.this, R.string.video_nodata_failed);
                        }
                    } else {
                        //处理录像转换
                        if (TextUtils.isEmpty(deviceFullNo)) {
                            if (playBackType == PLAY_BACK_TYPE_CLOUD) {
                                playBackType = PLAY_BACK_TYPE_LOCAL;
                            } else {
                                playBackType = PLAY_BACK_TYPE_CLOUD;
                            }

                            setTopBarRightView();
                            return;
                        } else {
                            if (connectView.isConnecting()) {
                                ToastUtils.show(mActivity, getString(R.string.video_connect_tips));
                                return;
                            }
                            if (playBackType == PLAY_BACK_TYPE_CLOUD) {
                                playBackType = PLAY_BACK_TYPE_LOCAL;
                            } else {
                                playBackType = PLAY_BACK_TYPE_CLOUD;
                            }
                            setTopBarRightView();
                        }
                        resetSpeed();
                        stopRemote3Func();
//                        stopRemotePlay();
//                        Single.create(new Single.OnSubscribe<Object>() {
//                            @Override
//                            public void call(SingleSubscriber<? super Object> singleSubscriber) {
//                                FunctionUtil.disconnect(connectIndex);
//                            }
//                        }).observeOn(Schedulers.io());
                        new Thread() {
                            @Override
                            public void run() {
                                JniUtil.disConnect(connectIndex);
                                connectIndex++;
                            }
                        }.start();
                        connectView.setConnectState(ConnectView.connecting, 0);
                        loading(true);
                        connectView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
//                                getRecordDataListFromNet();
                                searchRecordList();

                            }
                        }, 3000);//延迟执行，等待断开成功

                    }

                    break;
                }
                //日历背景
                case R.id.calendar_layout:
                    calendarLayout.setVisibility(View.GONE);
//                    mTopBarView.setPulldownIconRes(R.mipmap.icon_tree_down_normal);
//                    setTopBarRightView();
                    break;

                // 音频
                case R.id.audio_hbtn:
                case R.id.audio_vbtn: {
                    if (isRemotePaused) {
                        ToastUtils.show(JVBaseRemoteLinePlayActivity.this, R.string.pause_wait_connect);
                        break;
                    }
                    if (allowThisFuc1()) {
                        if (currentSpeedIndex != defaultSpeedIndex) {
                            break;
                        }
                        audioRemote();
                    }

                    break;
                }
                // 抓拍
                case R.id.capture_hbtn:
                case R.id.capture_vbtn: {
                    if (isRemotePaused) {
                        ToastUtils.show(JVBaseRemoteLinePlayActivity.this, R.string.pause_wait_connect);
                        break;
                    }
                    if (allowThisFuc1()) {
                        capture();
                    }
                    break;
                }
                // 录像
                case R.id.record_hbtn:
                case R.id.record_vbtn: {
                    if (isRemotePaused) {
                        ToastUtils.show(JVBaseRemoteLinePlayActivity.this, R.string.pause_wait_connect);
                        break;
                    }
                    if (!TextUtils.isEmpty(deviceFullNo)) {
                        boolean isJvmpType = (playBackType == PLAY_BACK_TYPE_LOCAL && connectType == 2) || playBackType == PLAY_BACK_TYPE_CLOUD;
                        if (isJvmpType) {
                            if (currentSpeedIndex != defaultSpeedIndex) {
                                ToastUtils.show(mActivity, "录像需要正常倍速");
                                break;
                            }
                        } else {
                            if (currentSpeedIndex != defaultSpeedIndex) {
                                ToastUtils.show(mActivity, "录像需要正常倍速");
                                break;
                            }
                        }
                    }
                    if (allowThisFuc1()) {
                        record(true);
                    }
                    break;
                }

                //查看抓拍图片或者视频
                case R.id.layout_snap:
                case R.id.iv_snap_land:
//                    String path;
//                    if (null != mIvSnap.getTag()) {
//                        path = (String) mIvSnap.getTag();
//                    } else {
//                        path = (String) view.getTag();
//                    }
//                    if (TextUtils.isEmpty(path)) {
//                        return;
//                    }
//                    stopRemote2Func();
//                    if (path.endsWith(AppConsts.IMAGE_JPG_KIND)) {
//                        if (isLandScape()) {
//                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                        }
//                        Intent intent = new Intent(JVBaseRemoteLinePlayActivity.this, ImageActivity.class);
////                        ArrayList<String> urls = new ArrayList<>();
////                        urls.add(path);
//                        ArrayList<String> urls = sanCaptureDir(path);
//                        intent.putStringArrayListExtra("urls", urls);
//                        intent.putExtra("position", 0);
//                        startActivity(intent);
//                    } else if (path.endsWith(AppConsts.VIDEO_MP4_KIND)) {
//                        VideoUtils.tryPlayVideo(JVBaseRemoteLinePlayActivity.this, path);
//                    }
                    break;

                //收藏
                case R.id.btn_portrait_collect:
                case R.id.btn_land_collect:
                    break;
                // 全屏
                case R.id.fullscreen_vbtn: {
                    if (!TextUtils.isEmpty(deviceFullNo) && !connectView.isConnected()
                            && !TextUtils.equals(connectView.getLinkState(), getString(R.string.connect_failed))) {
                        ToastUtils.show(JVBaseRemoteLinePlayActivity.this, R.string.wait_connect);
                        return;
                    }
                    if (isRemotePaused) {
                        pauseOrGoonPlay();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    break;
                }

                //播放界面暂停按钮
                case R.id.btn_land_pause_play:
                case R.id.play_pause_img: {
                    if (currentSpeedIndex != defaultSpeedIndex) {
                        changeSpeed(defaultSpeedIndex);
                        break;
                    }
                    if (TextUtils.equals(connectView.getLinkState(), getString(R.string.play_over))) {
                        ToastUtils.show(JVBaseRemoteLinePlayActivity.this, R.string.wait_connect);
                        break;
                    }
                    if (stopFilePlay) {//文件停止播放了，需要调用播放文件
                        playRemoteAtTime(selectTimeTV.getText().toString());
                    } else {
                        if (allowThisFuc1()) {
                            stopRemote2Func();
                            pauseOrGoonPlay();

                            if (isRemotePaused) {
                                connectView.setVisibility(View.GONE);
                                pauseBtn.setImageResource(R.mipmap.ic_play_speed_portrait_paused);
                                mBtnPlayLand.setImageResource(R.mipmap.ic_play_speed_land_paused);
                            } else {
                                pauseBtn.setImageResource(R.mipmap.ic_play_speed_portrait_playing);
                                mBtnPlayLand.setImageResource(R.mipmap.ic_play_speed_land_playing);
                            }
                        }


                    }
                    break;
                }
                //变倍慢放
                case R.id.btn_slow:
                case R.id.btn_land_slow:
                    if (isRemotePaused) {
                        ToastUtils.show(JVBaseRemoteLinePlayActivity.this, R.string.pause_wait_connect);
                        break;
                    }
                    if (allowThisFuc()) {
                        if (currentSpeedIndex > 0) {
                            if (connectView.isRecording()) {// 正在录像
                                record(true);
                            }
                            if (isAudioListening) {//正在监听停止监听
                                audioRemote();
                            }
                            changeSpeed(currentSpeedIndex - 1);
                        }
                    }
                    break;
                //变倍快放
                case R.id.btn_fast:
                case R.id.btn_land_fast:
                    if (isRemotePaused) {
                        ToastUtils.show(JVBaseRemoteLinePlayActivity.this, R.string.pause_wait_connect);
                        break;
                    }
                    if (allowThisFuc()) {
                        boolean isAdd;
                        isAdd = currentSpeedIndex < (speedParamArrayOct.length - 1);

                        if (isAdd) {
                            if (connectView.isRecording()) {// 正在录像
                                record(true);
                            }
                            if (isAudioListening) {//正在监听停止监听
                                audioRemote();
                            }
                            changeSpeed(currentSpeedIndex + 1);
                        }
                    }
                    break;
            }
        }
    };

    /**
     * 手势缩放视频
     */
    private MyGestureDispatcher dispatcher = new MyGestureDispatcher(
            new MyGestureDispatcher.OnGestureListener() {

                @Override
                public void onGesture(int gesture, int distance, Point vector, Point middle) {
                    Log.e("MyGestureDispatcher", "onGesture: gesture = " + gesture + "; distance = " + distance + "; vector = " + vector + "; middle = " + middle);

                    if (null == connectChannel) {
                        return;
                    }
                    if (!isLandScape()) {
                        return;
                    }

                    int viewWidth = playSurface.getWidth();
                    int viewHeight = playSurface.getHeight();

                    int left = connectChannel.getLastPortLeft();
                    int bottom = connectChannel.getLastPortBottom();
                    int width = connectChannel.getLastPortWidth();
                    int height = connectChannel.getLastPortHeight();

                    boolean needRedraw = false;

                    switch (gesture) {
                        case MyGestureDispatcher.GESTURE_TO_LEFT:
                        case MyGestureDispatcher.GESTURE_TO_RIGHT:
                        case MyGestureDispatcher.GESTURE_TO_UP:
                        case MyGestureDispatcher.GESTURE_TO_DOWN:
                            left += vector.x;
                            bottom += vector.y;
                            needRedraw = true;
                            break;
                        case MyGestureDispatcher.GESTURE_TO_BIGGER:
                        case MyGestureDispatcher.GESTURE_TO_SMALLER:
                            Log.e("sdgadlgjsdlfghs", "BIGGER_SMALLER");
                            if (width > viewWidth || distance > 0) {
                                float xFactor = (float) vector.x / viewWidth;
                                float yFactor = (float) vector.y / viewHeight;
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
                                int yMiddle = viewHeight - middle.y - bottom;

                                factor += 1;
                                left = middle.x - (int) (xMiddle * factor);
                                bottom = (viewHeight - middle.y) - (int) (yMiddle * factor);
                                width = (int) (width * factor);
                                height = (int) (height * factor);

                                if (width <= viewWidth || height < viewHeight) {
                                    left = 0;
                                    bottom = 0;
                                    width = viewWidth;
                                    height = viewHeight;
                                } else if (width > 4000 || height > 4000) {
                                    width = connectChannel.getLastPortWidth();
                                    height = connectChannel.getLastPortHeight();

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
                                    bottom = (viewHeight - middle.y) - (int) (yMiddle * factor);
                                }

                                needRedraw = true;
                            }
                            break;

                        // 手势单击双击
                        case MyGestureDispatcher.CLICK_EVENT:
                            if (isLandScape()) {
                                if (View.VISIBLE == playHorBar.getVisibility()) {
                                    handler.removeMessages(SelfConsts.WHAT_PLAY_DISPLAY_NAV_BTN);
                                    playHorBar.setVisibility(View.GONE);
                                    mTimeLand.setVisibility(View.GONE);
                                    scrollTimeLineView.setVisibility(View.GONE);//横屏时间轴跟随按钮显隐
                                } else {
                                    playHorBar.setVisibility(View.VISIBLE);
                                    mTimeLand.setVisibility(View.VISIBLE);
                                    scrollTimeLineView.setVisibility(View.VISIBLE);//横屏时间轴跟随按钮显隐
                                    handler.sendEmptyMessageDelayed(SelfConsts.WHAT_PLAY_DISPLAY_NAV_BTN, 5000);
                                }
                            }
                            break;
//                        case MyGestureDispatcher.DOUBLE_CLICK_EVENT:
//                            break;
                        default:
                            break;
                    }

                    if (needRedraw) {
                        if (left + width < viewWidth) {
                            left = viewWidth - width;
                        } else if (left > 0) {
                            left = 0;
                        }

                        if (bottom + height < viewHeight) {
                            bottom = viewHeight - height;
                        } else if (bottom > 0) {
                            bottom = 0;
                        }

                        connectChannel.setLastPortLeft(left);
                        connectChannel.setLastPortBottom(bottom);
                        connectChannel.setLastPortWidth(width);
                        connectChannel.setLastPortHeight(height);
                    }
                }
            });


    /**
     * 根据时间点播放视频
     *
     * @param playTime 播放时间点
     */
    private void playRemoteAtTime(String playTime) {
        if (null == playTime || "".equalsIgnoreCase(playTime)) {
            return;
        }

        stopRemote3Func();

        connectView.setConnectState(ConnectView.connecting, 0);

        stopFilePlay = false;

        Log.e(TAG, "playRemoteAtTime,scrollToTime=" + playTime);
        scrollTimeLineView.scrollToTime(playTime);

        playHwRecord(currentDate + "T00:00:00.000+08:00");
    }

    /**
     * 调用功能前判断是否可调用(真正连接上)
     */
    private boolean allowThisFuc() {
        if (TextUtils.isEmpty(deviceFullNo)) {
//            ToastUtils.show(mActivity, R.string.wait_add_channel);
            ToastUtils.show(mActivity, R.string.wait_add_channel);
            return false;
        }
        if (connectView.isRealConnected()) {
            return true;
        } else {
//            if (TextUtils.equals(connectView.getLinkState(), getString(R.string.video_nodata_failed))) {
//                ToastUtils.show(JVBaseRemoteLinePlayActivity.this, R.string.wait_connect);
//            } else {
//                ToastUtils.show(JVBaseRemoteLinePlayActivity.this, R.string.wait_connect);
//            }
            ToastUtils.show(mActivity, R.string.wait_connect);
            return false;
        }
    }

    /**
     * 调用功能前判断是否可调用（缓冲，暂停，连接上都算连接上）
     */
    private boolean allowThisFuc1() {
        if (TextUtils.isEmpty(deviceFullNo)) {
//            ToastUtils.show(mActivity, R.string.wait_add_channel);
            ToastUtils.show(mActivity, R.string.wait_add_channel);
            return false;
        }
        if (connectView.isConnected() && connectView.getConnectState() != ConnectView.connectedNoData) {
            return true;
        } else {
//            ToastUtils.show(JVBaseRemoteLinePlayActivity.this, R.string.wait_connect);
            ToastUtils.show(mActivity, R.string.wait_connect);
            return false;
        }
    }

    /**
     * 暂停或者继续播放视频
     */
    private void pauseOrGoonPlay() {
        Log.e("play_remote", "pauseOrGoonPlay: " + isRemotePaused);
        connectView.setVisibility(VISIBLE);
        if (isRemotePaused) {
            pauseBtn.setImageResource(R.mipmap.ic_play_speed_portrait_playing);
            mBtnPlayLand.setImageResource(R.mipmap.ic_play_speed_land_playing);

            connectView.setConnectState(ConnectView.connected, 0);

            isRemotePaused = false;
            // 继续播放视频
//            FunctionUtil.resumeSurface(connectIndex, surfaceholder.getSurface());
            if (currentSpeedIndex != defaultSpeedIndex) {
                changeSpeed(defaultSpeedIndex);
            } else {

                JniUtil.holosensPlayerResume(connectIndex);

            }

        } else {
            if (connectView.getConnectState() != ConnectView.connectedNoData) {
                pauseBtn.setImageResource(R.mipmap.ic_play_speed_portrait_paused);
                mBtnPlayLand.setImageResource(R.mipmap.ic_play_speed_land_paused);

                connectView.setConnectState(ConnectView.paused, 0);
                connectView.setVisibility(View.GONE);

                isRemotePaused = true;
            }
            JniUtil.holosensPlayerPause(connectIndex);
        }
    }

    /**
     * 停止远程回放的3个功能
     */
    private void stopRemote3Func() {
        dismissSnapWindow();

        if (connectView.isRecording()) {// 正在录像
            record(false);
        }
        if (isAudioListening) {//正在监听停止监听
            audioRemote();
        }
        if (isRemotePaused) {//已经暂停的，取消暂停
            Log.e(TAG, "stopRemote3Func-pause Or goon");
            pauseOrGoonPlay();
        }
    }

    /**
     * 停止远程回放的2个功能
     */
    private void stopRemote2Func() {
        dismissSnapWindow();
        if (connectView.isRecording()) {// 正在录像
            record(false);
        }
        if (isAudioListening) {//正在监听停止监听
            audioRemote();
        }

    }

    /**
     * 停止远程回放
     */
    private void stopRemotePlay() {
        if (null != connectView && connectView.needDisconnect()) {// 已连接或者连接中 先断开视频
            stopFilePlay = true;
            JniUtil.disConnect(connectIndex);
            connectIndex++;
            connectView.setConnectState(ConnectView.disconnected, 0);
            connectView.setVisibility(View.GONE);

            pauseBtn.setImageResource(R.mipmap.ic_play_speed_portrait_paused);
            mBtnPlayLand.setImageResource(R.mipmap.ic_play_speed_land_paused);
        }
    }


    /**
     * 判断当前是否横屏
     */
    private boolean isLandScape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

//
//    /**
//     * 添加设备成功，重新连接
//     */
//    public void addDevice(List<PlayBean> data) {
//        if (null != data && data.size() > 0) {
//            mFragmentManager.beginTransaction().hide(mDeviceListFragment).commitAllowingStateLoss();
//            mTopBarView.setVisibility(View.VISIBLE);
//            mLayoutAdd.setVisibility(View.GONE);
//
//            deviceFullNo = data.get(0).getDeviceId();
//            channelIndex = data.get(0).getChannelId();
//            connectGbCHannelId = data.get(0).getIpc_device_channel_id();
//            connectChannel.setChannel(channelIndex);
//            connectChannel.setIpc_device_channel_id(data.get(0).getIpc_device_channel_id());
//            nickName = data.get(0).getNickName();
//            connectType = data.get(0).getConnect_type();
//            connectChannel.getParent().setConnect_type(connectType);
//            initSpeedData();
////            getRecordDataListFromNet();
//            JniUtil();
//        }
//    }


    /**
     * 音频监听
     */
    private void audioRemote() {
        if (allowThisFuc()) {
            if (isAudioListening) {
                JniUtil.holosensPlayerCloseSound(connectIndex);
                boolean res = true;// stop audio
                if (res) {
                    isAudioListening = false;
                    audioVBtn.setSelected(false);
                    audioHBtn.setSelected(false);
                }
            } else {
                JniUtil.holosensPlayerOpenSound(connectIndex);
                boolean res = true;
                if (res) {
                    isAudioListening = true;
                    audioVBtn.setSelected(true);
                    audioHBtn.setSelected(true);
                }
            }
        }
    }

    /**
     * 抓拍
     */
    private void capture() {
        if (hasSDCard(5, true) && allowThisFuc()) {
            String capturePath = JniUtil.captureWithDev(connectIndex, TextUtils.isEmpty(nickName) ? deviceFullNo + "-" + (channelIndex + 1) : nickName);

            if (null != capturePath && !"".equalsIgnoreCase(capturePath)) {

                showSnapWindow(capturePath);
                Log.e(TAG, getLocalClassName() + "success");
            } else {
                ToastUtils.show(JVBaseRemoteLinePlayActivity.this, R.string.capture_error);
            }
        }
    }

    private static final int WHAT_DISMISS_POPUPWINDOW = 0x2020 + 1;
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
            mSnapView.findViewById(R.id.layout_snap).setOnClickListener(mOnClickListener);

            mLandSnapView = View.inflate(this, R.layout.layout_snap_window_land, null);
            mIvLandSnap = mLandSnapView.findViewById(R.id.iv_snap_land);
            mIvLandSnap.setOnClickListener(mOnClickListener);
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

                View targetView = captureHBtn;

                mIvLandSnap.setTag(imgPath);
                mIvSnap.setTag(null);
                if (imgPath.endsWith(AppConsts.IMAGE_JPG_KIND)) {
                    mIvLandSnap.setImageBitmap(BitmapCache.loadImageBitmap(imgPath, -1));
                } else if (imgPath.endsWith(AppConsts.VIDEO_MP4_KIND)) {
                    mIvLandSnap.setImageBitmap(VideoUtils.getVideoThumbnail(imgPath));

                    targetView = recordHBtn;
                }
                int[] location = new int[2];
                targetView.getLocationOnScreen(location);

                int[] locationWindow = new int[2];
                playLayout.getLocationOnScreen(locationWindow);

                mPopupWindow.showAtLocation(targetView, Gravity.NO_GRAVITY,
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

                mPopupWindow.showAsDropDown(playLayout, 0, -mSnapView.getMeasuredHeight());
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

    // 录像文件名称
    private String recordingFileName;

    /**
     * 录像
     */
    private void record(boolean showRecordPop) {
        if (allowThisFuc()) {
            if (connectView.isRecording()) {// 正在录像

                if (connectView.getRecordTime() * 1000 >= JVCloudConst.RECORD_VIDEO_MIN_LENGTH) {
                    stopRecord(showRecordPop);

                    if (showRecordPop) {
                        ToastUtilsB.show("录像成功");
//                        File file = new File(recordingFileName);
//                        if (!TextUtils.isEmpty(recordingFileName) && file.exists()) {
//
//                            showSnapWindow(recordingFileName);
//                        }
                    } else {
                        ToastUtils.show(this, getResources().getString(R.string.video_path, recordingFileName));
                    }
                } else {
                    stopRecord(true);
                }
            } else {
                startRecord();
            }
        }
    }

    /**
     * 开始录像
     */
    private void startRecord() {
        recordingFileName = JniUtil.startRecordByDev(connectIndex, TextUtils.isEmpty(nickName) ? deviceFullNo + "-" + (channelIndex + 1) : nickName);
        if (connectView.getVisibility() == View.GONE) {
            connectView.setVisibility(View.VISIBLE);
        }
        connectView.startRecord(recordingFileName);

        recordVBtn.setSelected(true);
        recordHBtn.setSelected(true);
    }

    /**
     * 停止录像
     */
    private void stopRecord(boolean showRecordPop) {
        JniUtil.stopRecord(connectIndex);

        connectView.stopRecord(showRecordPop);

        recordVBtn.setSelected(false);
        recordHBtn.setSelected(false);
    }


    /**
     * 恢复默认速度
     */
    private void resetSpeed() {

        //倍速播放恢复默认
        initSpeedData();

        mBtnSlow.setImageResource(R.mipmap.ic_play_speed_portrait_slow);
        mBtnFast.setImageResource(R.mipmap.ic_play_speed_portrait_fast);
        mBtnSlowLand.setImageResource(R.mipmap.ic_play_speed_land_slow);
        mBtnFastLand.setImageResource(R.mipmap.ic_play_speed_land_fast);
        if (isRemotePaused) {
            pauseBtn.setImageResource(R.mipmap.ic_play_speed_portrait_paused);
            mBtnPlayLand.setImageResource(R.mipmap.ic_play_speed_land_paused);
        } else {
            pauseBtn.setImageResource(R.mipmap.ic_play_speed_portrait_playing);
            mBtnPlayLand.setImageResource(R.mipmap.ic_play_speed_land_playing);
        }
    }

    //列表点击事件
    private void changeSpeed(int speedIndex) {

        currentSpeedIndex = speedIndex;

        JniUtil.holosensPlayerSetSpeed(connectIndex, speedParamArrayOct[speedIndex]);

        mBtnSlow.setImageResource(R.mipmap.ic_play_speed_portrait_slow);
        mBtnFast.setImageResource(R.mipmap.ic_play_speed_portrait_fast);
        mBtnSlowLand.setImageResource(R.mipmap.ic_play_speed_land_slow);
        mBtnFastLand.setImageResource(R.mipmap.ic_play_speed_land_fast);
//        if (!isJvmpType) {//好望
        switch (currentSpeedIndex) {
            case 0:
                mBtnSlow.setImageResource(R.mipmap.ic_play_speed_portrait_1_4);
                mBtnSlowLand.setImageResource(R.mipmap.ic_play_speed_land_1_4);
                break;
            case 1:
                mBtnSlow.setImageResource(R.mipmap.ic_play_speed_portrait_1_2);
                mBtnSlowLand.setImageResource(R.mipmap.ic_play_speed_land_1_2);
                break;
            case 3:
                mBtnFast.setImageResource(R.mipmap.ic_play_speed_portrait_2);
                mBtnFastLand.setImageResource(R.mipmap.ic_play_speed_land_2);
                break;
            case 4:
                mBtnFast.setImageResource(R.mipmap.ic_play_speed_portrait_4);
                mBtnFastLand.setImageResource(R.mipmap.ic_play_speed_land_4);
                break;
        }

        if (currentSpeedIndex != defaultSpeedIndex) {
            pauseBtn.setImageResource(R.mipmap.ic_play_speed_portrait_reset);
            mBtnPlayLand.setImageResource(R.mipmap.ic_play_speed_land_reset);
        } else {
            if (isRemotePaused) {
                pauseBtn.setImageResource(R.mipmap.ic_play_speed_portrait_paused);
                mBtnPlayLand.setImageResource(R.mipmap.ic_play_speed_land_paused);
            } else {
                pauseBtn.setImageResource(R.mipmap.ic_play_speed_portrait_playing);
                mBtnPlayLand.setImageResource(R.mipmap.ic_play_speed_land_playing);
            }
        }
    }

    /**
     * 远程文件列表查询完成后设置Listener
     */
    private void setNumberListener(boolean enable) {
        scrollTimeLineView.setCanScroll(enable);
        if (enable) {
            scrollTimeLineView.setNumberListener(numberListener);
        } else {
            scrollTimeLineView.setNumberListener(null);
        }
    }

    ScaleView.NumberListener numberListener = new ScaleView.NumberListener() {
        @Override
        public void onChanged(String mCurrentNum, boolean isAutoScrolling) {
            Log.e(TAG, "setNumberListener, mCurrentNum=" + mCurrentNum + ",tvNumber=" + selectTimeTV.getText().toString() + ",isAutoScrolling=" + isAutoScrolling);
            if (!checkScrollAvalid(mCurrentNum)) {
                scrollTimeLineView.setManuScroll(false);
                return;
            }
            if (null == videoList || videoList.size() == 0) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        selectTimeTV.setText(getSelectTime(mCurrentNum));
                        mTimeLand.setText(mCurrentNum);
                    }
                });
                Log.e(TAG, "setNumberListener.return-1");
                return;
            }
            if (mCurrentNum.equalsIgnoreCase(selectTimeTV.getText().toString()) && connectView.isConnected()) {//
                Log.e(TAG, "setNumberListener.return-2");
                return;
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    selectTimeTV.setText(getSelectTime(mCurrentNum));
                    mTimeLand.setText(mCurrentNum);
                }
            });

            Log.e(TAG, "setNumberListener.manu_scroll=" + scrollTimeLineView.isManuScroll() + ", isScrolling=" + scrollTimeLineView.isScrolling());
//            if (scrollTimeLineView.isManuScroll()) {
//                getPlayIndex(mCurrentNum);
//            }


            String playTime = currentDate + " " + mCurrentNum;

            scrollTime = mCurrentNum;
            handler.removeMessages(SelfConsts.WHAT_REMOTE_PLAY_AT_INDEX);

            //手动滑动，才调用播放
            if (scrollTimeLineView.isManuScroll()) {
                newPictureFlag = false;
                Log.e(TAG, "setNumberListener:newPictureFlag=" + newPictureFlag);
                handler.sendMessageDelayed(handler.obtainMessage(SelfConsts.WHAT_REMOTE_PLAY_AT_INDEX, currentPlayIndex, 0, playTime), 100);
            }

        }
    };

    /**
     * @return
     */
    private boolean checkScrollAvalid(String time) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");//设置日期格式
        for (String[] times : timeList) {
            String begin = times[0];
            String end = times[1];
            try {
                Date beginD = df.parse(begin);
                Date endD = df.parse(end);
                Date selectT = df.parse(time);
                if (selectT.getTime() >= beginD.getTime() && selectT.getTime() <= endD.getTime()) {
                    return true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 拼接时间标签上的
     *
     * @param currentTime
     * @return
     */
    private String getSelectTime(String currentTime) {
        return currentDate + " " + currentTime;
    }

    private void getPlayIndex(String playTime) {
        String[] timeArray = playTime.split(":");
        currentPlayIndex = getVideoIndex(Integer.parseInt(timeArray[0]), Integer.parseInt(timeArray[1]), Integer.parseInt(timeArray[2]));
    }

    /**
     * 根据时间确定videoList的index
     *
     * @param hour
     * @param minute
     * @return
     */
    private int getVideoIndex(int hour, int minute, int seconds) {
        int index = -1;
        Log.e(TAG, getLocalClassName() + "--查找时间=" + hour + ":" + minute + ":" + seconds);
        if (null != timeList && 0 != timeList.size()) {
            for (int i = 0; i < timeList.size(); i++) {
                if (null != timeList.get(i)) {
                    String[] timeArray = timeList.get(i);

                    String[] dateStrArrayStart = timeArray[0].split(":");
                    int startHour = Integer.parseInt(dateStrArrayStart[0]);
                    int startMinute = Integer.parseInt(dateStrArrayStart[1]);
                    int startSeconds = Integer.parseInt(dateStrArrayStart[2]);


                    String[] dateStrArrayEnd = timeArray[1].split(":");
                    int endHour = Integer.parseInt(dateStrArrayEnd[0]);
                    int endMinute = Integer.parseInt(dateStrArrayEnd[1]);
                    int endSeconds = Integer.parseInt(dateStrArrayEnd[2]);

//                    Log.e(TAG, getLocalClassName() + "--列表Index=" + i + ";时间是:" + timeArray[0] + "到" + timeArray[1]);

                    if (hour == startHour && hour == endHour) {//找到小时,开始和结束的小时等于查找的小时
                        if (minute >= startMinute && minute < endMinute) {//查找的分钟大于等于开始的分钟,小于结束的分钟
                            index = i;
//                            Log.e(TAG, getLocalClassName() + "--列表Index=" + i + ";时间是:" + timeArray[0] + "到" + timeArray[1] + "找到了相等的分");
                            break;
                        } else if (minute == endMinute) {//查找的分钟等于结束的分钟
                            if (seconds < endSeconds) {//查找的秒数小于结束的秒数
                                index = i;
//                                Log.e(TAG, getLocalClassName() + "--列表Index=" + i + ";时间是:" + timeArray[0] + "到" + timeArray[1] + "找到了秒");
                                break;
                            }
                        }
                    } else if (hour == endHour && hour > startHour) {//找到小时,结束的小时等于查找的小时
                        if (minute < endMinute) {//分钟在结束之前
                            index = i;
//                            Log.e(TAG, getLocalClassName() + "-- 列表Index=" + i + ";时间是:" + timeArray[0] + "到" + timeArray[1] + "找到了结束的分");
                            break;
                        }
                    } else if (hour == startHour && hour < endHour) {//找到小时,开始的小时等于查找的小时
                        if (minute > startMinute) {//分钟在开始之后
                            index = i;
//                            Log.e(TAG, getLocalClassName() + "--列表Index=" + i + ";时间是:" + timeArray[0] + "到" + timeArray[1] + "找到了开始的分");
                            break;
                        } else if (minute == endMinute) {//分钟等于开始的分钟
                            if (seconds > startSeconds) {//秒在开始之后
                                index = i;
//                                Log.e(TAG, getLocalClassName() + "--列表Index=" + i + ";时间是:" + timeArray[0] + "到" + timeArray[1] + "找到了开始的秒");
                                break;
                            }
                        }
                    }

                }
            }
        }
        //Log.e(TAG, getLocalClassName() + "--最终index=" + index);
        return index;
    }

    /**
     * 获取录像文件列表
     */
    private void searchRecordList() {
        loading(true);
        //切换设备，切换录像方式，重连 移除所有的分页信息
        clearAllRecordsInfo();

        if (playBackType == PLAY_BACK_TYPE_LOCAL) {
            if (connectType == ProtocolType.GB28181) {
                BG28181RecordList(false);       //国标 前端录像
            } else {
                HWRecordList();                   //好望设备 前端录像
            }

        } else {
            BG28181RecordList(true);            //云端录像
        }

    }

    /**
     * 华为好望设备
     * 获取本地录像文件
     */
    private void HWRecordList() {
        rulerDefaultPos = "00:00:00";
        selectTimeTV.setText(rulerDefaultPos);
        mTimeLand.setText(rulerDefaultPos);

        dismissLoading();
        playHwRecord(currentDate + "T00:00:00.000+08:00");        //播放
        searchHoloRecord();                                         //获取好望录像文件列表
        searchHoloRecordDate();
    }


    /**
     * 分页查询录像文件
     */
    int page = 0;
    int pageSize = 1000;
    int currentNum = 0;
    private ArrayList<RemoteRecord> recordList = new ArrayList<>();

    //重新搜索的时候信息都清空
    private void clearAllRecordsInfo() {
        page = 0;
        currentNum = 0;
        recordList.clear();
    }

    /**
     * jvmp时间戳转换
     *
     * @param time
     * @return
     */
    private String timeStimpToStringTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat _df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        SimpleDateFormat df2 = new SimpleDateFormat("HH:mm:ss");//设置日期格式
        try {
            Date beginTime = sdf.parse("1970-01-01 08:00:00");
            long mTime = beginTime.getTime() + time;

            Date date = new Date();
            date.setTime(time);

            rulerDefaultPos = df2.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rulerDefaultPos;
    }

    /**
     * jvmp时间戳转换
     *
     * @param time
     * @return
     */
    private String sdkTimeStimpToStringTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat _df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        SimpleDateFormat df2 = new SimpleDateFormat("HH:mm:ss");//设置日期格式
        try {
            Date date = sdf.parse(time);
            Date selectD = _df.parse(currentDate);
            if (date.getTime() < selectD.getTime())
                return "00:00:00";

            rulerDefaultPos = df2.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rulerDefaultPos;
    }

    /**
     * jvmp时间戳转换
     *
     * @param time
     * @return
     */
    private String sdkLocalTimeStimpToStringTime(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        SimpleDateFormat df2 = new SimpleDateFormat("HH:mm:ss");//设置日期格式
        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = simpleDateFormat.parse(time);
            Date selectD = simpleDateFormat3.parse(currentDate);
            if (date.getTime() < selectD.getTime())
                return "00:00:00";

            rulerDefaultPos = df2.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rulerDefaultPos;
    }


    /**
     * 是否是jvmp连接
     * 好望本地前端录像 走p2p
     * 其他的都走jvmp
     *
     * @return
     */
    private boolean isJvmpConn() {
        if (connectType == ProtocolType.HOLO && playBackType == PLAY_BACK_TYPE_LOCAL) {
            return false;
        }
        return true;
    }


    //todo 以下是sdk录像回放相关功能

    /**
     * 查询国标GB28181录像列表
     */
    private void BG28181RecordList(boolean cloud) {
        connectView.setVisibility(VISIBLE);
        connectView.setConnectState(ConnectView.connecting, 0);

        ResponseListener listener = new ResponseListener() {
            @Override
            public void onSuccess(String result) {
                dismissLoading();
                Log.i(TAG, "BG28181RecordList:" + result);
                SDKRecordListBean bean = new Gson().fromJson(result, SDKRecordListBean.class);
                manageGb28181Record(bean, cloud);
            }

            @Override
            public void onFailed(Throwable throwable) {
                dismissLoading();
                Log.i(TAG, "BG28181RecordList onFailed");
            }
        };

        String end_time = currentDate + " 23:59:59";
        String start_time = currentDate + " 00:00:00";
//        int limit = 1000;
//        int offset = 0;
        String type;
        if (cloud) {
            type = "NORMAL_RECORD";
        } else {
            type = "SECONDARY_STREAM_1";
        }

        String url;
        if (cloud) {
            url = Consts.GET_CLOUD_RECORDS;
        } else {
            url = Consts.GET_LOCAL_RECORDS;
        }
        url = url.replace("{user_id}", Consts.userId)
                .replace("{device_id}", connectChannel.getParent().getSn())
                .replace("{channel_id}", connectChannel.getChannel_id());
        AppImpl.getInstance(mActivity).getGB28181RecordList(url, MySharedPreference.getString(MySharedPreferenceKey.LoginKey.TOKEN), start_time, end_time, pageSize, page, type, cloud, listener);
    }

    /**
     * GB28181录像转化为videoList里面的RemoteRecord
     *
     * @param bean
     */
    private void manageGb28181Record(SDKRecordListBean bean, boolean cloud) {
        if (bean == null || bean.getRecords() == null || bean.getRecords().size() <= 0) {
            dismissLoading();
            connectView.setVisibility(VISIBLE);
            connectView.setConnectState(ConnectView.connectedNoData, R.string.video_nodata_failed);
            return;
        }
        if (page == 0 && bean.getRecords() != null && bean.getRecords().size() > 0) {
            //播放第一个录像文件
            String m = String.valueOf(month);
            if (month < 10) {
                m = "0" + month;
            }
            String d = String.valueOf(day);
            if (day < 10) {
                d = "0" + day;
            }
            getGb28181Url(String.format("%d-%s-%s 00:00:00", year, m, d), String.format("%d-%s-%s 23:59:59", year, m, d), ChannelType.HOLO, cloud);

        }
        for (SDKRecordBean b : bean.getRecords()) {
            RemoteRecord remoteRecord = new RemoteRecord();
            remoteRecord.setRecordType(78);
            remoteRecord.setFileName(b.getRecord_name());
            remoteRecord.setFilePath(b.getRecord_name());
            long startTime = DateUtil.toTimeStamp(b.getStart_time(), "yy-MM-dd HH:mm:ss");
            long endTime = DateUtil.toTimeStamp(b.getEnd_time(), "yy-MM-dd HH:mm:ss");
            remoteRecord.setFileDuration((int) (endTime - startTime));
            remoteRecord.setStartTime(DateUtil.formatCloudRecordStartTime(b.getStart_time(), currentDate));
            remoteRecord.setEndTime(DateUtil.formatCloudRecordStartTime(b.getEnd_time(), currentDate));

            recordList.add(remoteRecord);
        }
        currentNum = currentNum + recordList.size();
        if (currentNum < bean.getTotal()) {
            page++;
            BG28181RecordList(false);
            return;
        }

        this.onNotify(WHAT_REMOTE_PRECISE_FILE_LIST, 0, 0, recordList);
    }

    /**
     * GB28181录像文件的播放url
     *
     * @param startTime
     * @param endTime
     */
    private void getGb28181Url(String startTime, String endTime, String protocol, boolean cloud) {

        ResponseListener listener = new ResponseListener() {
            @Override
            public void onSuccess(String result) {
                dismissLoading();
                Log.i(TAG, "getGb28181Url:" + result);
                SDKCloudVodUrl vodBean = new Gson().fromJson(result, SDKCloudVodUrl.class);
                playVod(vodBean.getPlayback_url());
            }

            @Override
            public void onFailed(Throwable throwable) {
                dismissLoading();
                Log.i(TAG, "getGb28181Url: onFailed");
            }
        };
        String url;
        if (cloud) {
            url = Consts.GET_CLOUD_VOD_URL;
        } else {
            url = Consts.GET_LOCAL_VOD_URL;
        }
        url = url.replace("{user_id}", Consts.userId);
        url = url.replace("{device_id}", connectChannel.getParent().getSn());
        url = url.replace("{channel_id}", connectChannel.getChannel_id());
        AppImpl.getInstance(mActivity).getGb2881VodUrl(url, MySharedPreference.getString(MySharedPreferenceKey.LoginKey.TOKEN), startTime, endTime, protocol, listener);
    }

    /**
     * 播放vod
     *
     * @param url
     */
    private void playVod(String url) {
        if (TextUtils.isEmpty(url)) {
            connectView.setVisibility(VISIBLE);
            connectView.setConnectState(ConnectView.connectedNoData, R.string.video_nodata_failed);
            return;
        }
        connectChannel.setToken("");
        connectChannel.setMts("");
        connectChannel.setJvmpUrl(url);
        connectChannel.setWorkIngKey("");
        Single.create(new Single.OnSubscribe<Integer>() {
            @Override
            public void call(SingleSubscriber<? super Integer> singleSubscriber) {
                int res = JniUtil.holosensPlayerConnectByP2p(url, connectIndex, 0, channelIndex);
                singleSubscriber.onSuccess(res);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer connectResult) {
                        mRequestDateEnd = true;

                    }
                });
    }


    //todo sdk 好望本地录像

    /**
     * 好望设备查询本地录像列表
     */
    private void searchHoloRecord() {
        timeList.clear();
        scrollTimeLineView.scrollToTime(rulerDefaultPos);
        scrollTimeLineView.setTimeList(timeList);
        setNumberListener(false);

        ResponseListener listener = new ResponseListener() {
            @Override
            public void onSuccess(String result) {
                dismissLoading();
                Log.i(TAG, "好望p2p:" + result);
                if (result != null) {
                    connectChannel.setMts(result);
                    connectChannel.setJvmpUrl(result);
                }

                JniUtil.holosensPlayerQueryRecordsByP2p(connectChannel.getJvmpUrl(), Integer.parseInt(connectGbCHannelId), 1, currentDate + "T00:00:00.000+08:00", currentDate + "T23:59:00.000+08:00");
            }

            @Override
            public void onFailed(Throwable throwable) {
                dismissLoading();
            }
        };
        HashMap<String, Object> params = new HashMap<>();
        JSONArray channels = new JSONArray();
        JSONObject channel = new JSONObject();
        try {
            channel.put("device_id", connectChannel.getParent().getSn());
            channel.put("channel_id", connectChannel.getChannel_id());
            channel.put("stream_type", "SECONDARY_STREAM_1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        channels.put(channel);

        params.put("channels", channels);
        String url = Consts.HOLO_PLAYURL.replace("{user_id}", Consts.userId);
        AppImpl.getInstance(mActivity).getDataByPostMethod(url, MySharedPreference.getString(MySharedPreferenceKey.LoginKey.TOKEN), params, listener);
    }

    /**
     * 好望设备查询本地录像列表
     */
    private void searchHoloRecordDate() {
        timeList.clear();
        scrollTimeLineView.scrollToTime(rulerDefaultPos);
        scrollTimeLineView.setTimeList(timeList);
        setNumberListener(false);

        ResponseListener listener = new ResponseListener() {
            @Override
            public void onSuccess(String result) {
                dismissLoading();
                Log.i(TAG, "好望p2p:" + result);
                if (result != null) {
                    connectChannel.setMts(result);
                    connectChannel.setJvmpUrl(result);
                }

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                JniUtil.holosensPlayerQueryRecordDates(connectChannel.getJvmpUrl(), Integer.parseInt(connectGbCHannelId), 1, simpleDateFormat.format(DateUtil.getFirstDayOfMonth(year, month - 1)), simpleDateFormat.format(DateUtil.getLastDayOfMonth(year, month - 1)));
            }

            @Override
            public void onFailed(Throwable throwable) {
                dismissLoading();
            }
        };
        HashMap<String, Object> params = new HashMap<>();
        JSONArray channels = new JSONArray();
        JSONObject channel = new JSONObject();
        try {
            channel.put("device_id", connectChannel.getParent().getSn());
            channel.put("channel_id", connectChannel.getChannel_id());
            channel.put("stream_type", "SECONDARY_STREAM_1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        channels.put(channel);

        params.put("channels", channels);
        String url = Consts.HOLO_PLAYURL.replace("{user_id}", Consts.userId);
        AppImpl.getInstance(mActivity).getDataByPostMethod(url, MySharedPreference.getString(MySharedPreferenceKey.LoginKey.TOKEN), params, listener);
    }


    /**
     * 播放好望录像
     * 2020-08-11T00:00:00.000+08:00
     */
    private void playHwRecord(String time) {
        ResponseListener listener = new ResponseListener() {
            @Override
            public void onSuccess(String result) {
                dismissLoading();
                Log.i(TAG, "好望p2p:" + result);
                if (result != null) {
                    connectChannel.setMts(result);
                    connectChannel.setJvmpUrl(result);
                }
                JniUtil.holosensPlayerPlayRecordByP2p(connectIndex, connectChannel.getJvmpUrl(), Integer.parseInt(connectGbCHannelId), 1, time);
            }

            @Override
            public void onFailed(Throwable throwable) {
                dismissLoading();
            }
        };
        HashMap<String, Object> params = new HashMap<>();
        JSONArray channels = new JSONArray();
        JSONObject channel = new JSONObject();
        try {
            channel.put("device_id", connectChannel.getParent().getSn());
            channel.put("channel_id", connectChannel.getChannel_id());
            channel.put("stream_type", "SECONDARY_STREAM_1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        channels.put(channel);

        Log.i(TAG, "channels:" + channels.toString());
        Log.i(TAG, "channel:" + channel.toString());

        params.put("channels", channels);
        String url = Consts.HOLO_PLAYURL.replace("{user_id}", Consts.userId);
        AppImpl.getInstance(mActivity).getDataByPostMethod(url, MySharedPreference.getString(MySharedPreferenceKey.LoginKey.TOKEN), params, listener);
    }
}
