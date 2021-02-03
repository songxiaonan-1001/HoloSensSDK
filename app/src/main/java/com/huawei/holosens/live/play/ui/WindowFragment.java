package com.huawei.holosens.live.play.ui;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huawei.holobase.bean.MtsJvmpRequestBean;
import com.huawei.holobasic.consts.MySharedPreferenceKey;
import com.huawei.holobasic.utils.MySharedPreference;
import com.huawei.holosens.R;
import com.huawei.holosens.bean.Channel;
import com.huawei.holosens.bean.Device;
import com.huawei.holosens.consts.JVEncodedConst;
import com.huawei.holosens.live.play.adapter.GlassAdapter;
import com.huawei.holosens.live.play.bean.Glass;
import com.huawei.holosens.live.play.event.MsgEvent;
import com.huawei.holosens.live.play.glass.BaseGlass;
import com.huawei.holosens.live.play.glass.BaseGlassC2;
import com.huawei.holosens.live.play.glass.BasePlayGlass;
import com.huawei.holosens.live.play.util.SimpleTask;
import com.huawei.holosens.live.play.util.SimpleThreadPool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * 窗户
 * ps：一扇窗户对应着X块玻璃, 每扇窗户上都有窗帘
 * 窗户上的窗帘默认是收起的(未打开状态)
 *
 */
public class WindowFragment extends SimpleFragment {

    private static final String TAG = "WindowFragment";

    // 参数
    private static final String WINDOW_NO = "window_no";
    private static final String SPAN_COUNT = "span_count";
    private static final String SELECTED_GLASS_NO = "selected_glass_no";

    // 分屏数(一扇窗户上有多少块玻璃)
    private int mSpanCount = 1;
    // 位置(哪扇窗户)
    private int mWindowNo;
    // 当前选中的玻璃号
    private int mSelectedGlassNo;

    private View mRootView;
    // 窗户尺寸、玻璃尺寸
    private ViewGroup.LayoutParams mWindowSize;
    private Glass.Size mGlassSize;

    // 列表
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private GlassAdapter mGlassAdapter;
    private int mColumnNo;

    // 当前窗户上的玻璃列表
    private List<Glass> mGlassList;
    private int mGlassCount;
    private boolean isWindowVisibleToUser;
    private boolean isResume, isCloseCurtain, isWindowRemoved;
    private boolean isRequestMts;
    private boolean isAbnormalDisconnect = false; // 是否在回放页面异常断开

    private boolean mIsEdit;//是否是编辑状态

    public WindowFragment() {
    }

    /**
     * @param bundle 通道号/窗口所在的分组号/窗口等
     */
    public static WindowFragment newInstance(Bundle bundle) {
        WindowFragment fragment = new WindowFragment();
        Bundle args = new Bundle();
        args.putInt(WINDOW_NO, bundle.getInt("windowNo"));
        args.putInt(SPAN_COUNT, bundle.getInt("spanCount"));
        args.putInt(SELECTED_GLASS_NO, bundle.getInt("selectedGlassNo"));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        /*
          窗户对用户可见/不可见需要isVisibleToUser和isResume两个变量配合使用
         */
        if (isVisibleToUser) {
            isWindowVisibleToUser = true;
            if (isResume) {
                handleWindowVisibleToUser();
            }
        } else {
            if (isWindowVisibleToUser) {
                isWindowVisibleToUser = false;
                handleWindowGoneToUser();
            }
        }
//        Log.e(TAG, "setUserVisibleHint: mWindowNo=" + mWindowNo );
//        Log.e(TAG, "setUserVisibleHint: isVisibleToUser=" + isVisibleToUser );
//        Log.e(TAG, "setUserVisibleHint: isWindowVisibleToUser=" + isWindowVisibleToUser );
//        Log.e(TAG, "setUserVisibleHint: isResume=" + isResume );

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            mWindowNo = getArguments().getInt(WINDOW_NO);
            mSpanCount = getArguments().getInt(SPAN_COUNT);
            mSelectedGlassNo = getArguments().getInt(SELECTED_GLASS_NO);
        }

        mGlassList = new ArrayList<>();
        // 重新计算窗户的尺寸
        mColumnNo = (int) Math.sqrt(mSpanCount);
        mGlassSize = new Glass.Size();
        if (mActivity.isLandScape()) {
//            mGlassSize.width = mScreenWidth / mColumnNo;
            mGlassSize.height = mScreenWidth/ mColumnNo;



            Log.e("sdgadgdgadgd----1","mScreenWidth="+mScreenWidth+";mScreenHeight="+mScreenHeight+";mBarHeight="+mBarHeight);




            Rect outRect = new Rect();
            getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);

            Log.e("sdgadgdgadgd----2","outRect.width()="+outRect.width()+";outRect.height()="+outRect.height());

            if( outRect.width()==0){
                mGlassSize.width = mScreenHeight / mColumnNo;
            }else{
                mGlassSize.width = outRect.width() / mColumnNo;
            }



        } else {
            mGlassSize.width = mScreenWidth / mColumnNo;
            int windowHeight = mScreenWidth / 16 * 9;
            mGlassSize.height = windowHeight / mColumnNo;
        }


        Log.e("sdgadgdgadgd----3","mGlassSize.width="+mGlassSize.width+";mGlassSize.height="+mGlassSize.height);
        mGlassAdapter = new GlassAdapter(this, mGlassSize, mSelectedGlassNo);
        initWindow();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == mRootView) {
            mRootView = inflater.inflate(R.layout.fragment_window, container, false);
            mRecyclerView = mRootView.findViewById(R.id.window_recycler_view);
            // 设置窗户的尺寸(窗户的尺寸一旦固定后就不会改变)
            mWindowSize = mRecyclerView.getLayoutParams();
            if (mActivity.isLandScape()) {
                mWindowSize.width = mScreenWidth;
                mWindowSize.height = mScreenHeight;
            } else {
                mWindowSize.width = mScreenWidth;
                mWindowSize.height = mScreenWidth / 16 * 9;
            }
            // 设置窗户的布局
            mGridLayoutManager = new GridLayoutManager(mActivity, mColumnNo);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(mGridLayoutManager);
//            mGlassAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(mGlassAdapter);
        }
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        isResume = true;
        /*
          A.窗户对用户可见
            a.窗户上的窗帘关着, 打开窗帘
            b.窗户上的窗帘开着, 安装玻璃
          B.窗户对用户不可见
            a.窗户上未安装玻璃的话, 安装玻璃
         */
//        Log.e(TAG, "onResume: mWindowNo=" + mWindowNo );
//        Log.e(TAG, "onResume: isWindowVisibleToUser=" + isWindowVisibleToUser );
//        Log.e(TAG, "onResume: SelectedWindowIndex=" + mActivity.getSelectedWindowIndex() );
//        Log.e(TAG, "onResume: isCloseCurtain=" + isCloseCurtain );
        if (isWindowVisibleToUser) {
            if (isCloseCurtain) {
                if (isAbnormalDisconnect) {
                    isAbnormalDisconnect = false;
                    installGlass();
                } else {
                    openTheCurtain();
                }
            } else {
                if (isRequestMts) {
                    return;
                }

                isRequestMts = true;
                installGlass();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isResume = false;
        /*
          a.首先窗户需要对用户可见
          b.窗户上的窗帘未关
          符合以上两个条件, 关窗帘
         */
        if (isWindowVisibleToUser) {
            if (!isCloseCurtain) {
                closeTheCurtain();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        removeWindow();
    }

    /**
     * 默认的初始化窗户
     */
    private void initWindow() {
        if (0 != mActivity.getWindowList().size()
                && mWindowNo < mActivity.getWindowList().size()){
            mGlassList = mActivity.getWindowList().get(mWindowNo);
            mGlassCount = mGlassList.size();
            mGlassAdapter.updateGlassList(mGlassList);
        }

    }

    public void setEdit(boolean isEdit) {
        this.mIsEdit = isEdit;

        if (null != mGlassAdapter) {
            mGlassAdapter.setEdit(mIsEdit);
        }

        // 修改编辑状态
        for (int i = 0; i < mGlassCount; i++) {
            RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForLayoutPosition(i);
            if (holder != null) {
                if (holder instanceof BasePlayGlass) {
                    BasePlayGlass baseViewHolder = (BasePlayGlass) holder;
                    baseViewHolder.setEdit(isEdit);
                }
            }
        }
    }

    public boolean isEdit() {
        return mIsEdit;
    }

    public JVMultiPlayActivity getPlayActivity() {
        return mActivity;
    }

    /**
     * 获取窗户号码
     *
     * @return
     */
    public int getWindowNo() {
        return mWindowNo;
    }

    /**
     * 获取当前窗户上的玻璃数量
     *
     * @return
     */
    public int getGlassCount() {
        return mGlassCount;
    }

    /**
     * 获取玻璃列表
     *
     * @return
     */
    public List<Glass> getGlassList() {
        return mGlassList;
    }

    /**
     * 窗户对用户可见的处理
     * ps:只有滑屏切换窗户的时候才会执行此方法
     */
    private void handleWindowVisibleToUser() {
        // 退出播放界面
        if (mActivity.isBackPressed()) {
            return;
        }
        Log.e(TAG, "窗户[" + mWindowNo + "] 可见");
        mGlassAdapter.setVisibleToUser(true);

        installGlass();
        return;

    }

    private void startGlassVisible() {
        // 对用户可以看到的窗户进行处理
        for (int i = 0; i < mGlassCount; i++) {
            // 将回调传递到不同类型的玻璃上面
            RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForLayoutPosition(i);
            if (holder != null) {
                if (holder instanceof BasePlayGlass) {
                    BasePlayGlass baseViewHolder = (BasePlayGlass) holder;
                    // 改变border颜色
                    baseViewHolder.changeBorderColor(mSelectedGlassNo);
                    // 更新具体holder中的可见标识
                    baseViewHolder.setVisibleToUser(true);
                    baseViewHolder.connect();
                }
            }
        }
    }

    /**
     * 窗户对用户不可见的处理
     * ps:只有滑屏切换窗户的时候才会执行此方法
     */
    private void handleWindowGoneToUser() {
        // 退出播放界面
        if (mActivity.isBackPressed()) {
            return;
        }
        // 窗户被销毁
        if (isWindowRemoved) {
            return;
        }

        Log.e(TAG, "窗户[" + mWindowNo + "] 不可见");
        mGlassAdapter.setVisibleToUser(false);
        // 对用户看不见的窗户进行处理
        for (int i = 0; i < mGlassCount; i++) {
            // 将回调传递到不同类型的玻璃上面
            RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForLayoutPosition(i);
            if (holder != null) {
                if (holder instanceof BasePlayGlass) {
                    BasePlayGlass baseViewHolder = (BasePlayGlass) holder;
                    // 更新具体holder中的可见标识
                    baseViewHolder.setVisibleToUser(false);
                    baseViewHolder.disconnect();
                }
            }
        }
    }

    /**
     * 安装玻璃
     */
    private void installGlass() {
        if (mActivity.isBackPressed()) {
            return;
        }

        // 窗户被销毁
        if (isWindowRemoved) {
            return;
        }

        startInstallGlass();
//        List<MtsJvmpRequestBean> hwlist = new ArrayList<>();
//        List<MtsJvmpRequestBean> gblist = new ArrayList<>();
//        for (int i = 0; i < mGlassList.size(); i++) {
//            //+号玻璃没有channel
//            if (null != mGlassList.get(i).getChannel()) {
//                if (mGlassList.get(i).getChannel().getParent().isGB28181Device()) {
//                    MtsJvmpRequestBean bean = new MtsJvmpRequestBean(mGlassList.get(i).getChannel().getParent().getSn(), mGlassList.get(i).getChannel().getIpc_device_channel_id() + "", 1);
//                    bean.setProtocol("jvmp");
////                    bean.setDevice_id("34020000001320000009");// 09054910001320000003
////                    bean.setChannel_id("34020000001310000001"); // 09054910001310000001
//                    gblist.add(bean);
//                } else {
//                    MtsJvmpRequestBean bean = new MtsJvmpRequestBean(mGlassList.get(i).getChannel().getParent().getSn(), mGlassList.get(i).getChannel().getChannel() + "", 1);
//                    hwlist.add(bean);
//                }
//            }
//        }
//        if (hwlist.size() == 0 && gblist.size() == 0) {
//            startInstallGlass();
//            return;
//        }
//        if (hwlist != null && hwlist.size() > 0) {
//            getHwStreamSuccess = false;
//            getHwLiveStream(hwlist);
//        } else {
//            getHwStreamSuccess = true;
//        }
//        if (gblist != null && gblist.size() > 0) {
//            getGbStreamSuccess = false;
//            getGbLiveStream(gblist);
//        } else {
//            getGbStreamSuccess = true;
//        }
    }

    private boolean getHwStreamSuccess;
    private boolean getGbStreamSuccess;

    /**
     * 批量获取国标设备直播流
     *
     * @param list
     */
    private void getGbLiveStream(List<MtsJvmpRequestBean> list) {
//        BaseRequestParam param = new BaseRequestParam();
//        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
//        data.put("enterprise_id", MySharedPreference.getString(MySharedPreferenceKey.LoginKey.CURRENT_ENTERPRISE));
//        data.put("channels", com.alibaba.fastjson.JSONObject.toJSON(list));
//        param.putAll(data);
//        HashMap<String, Object> header = HeaderUtil.createHeader(MySharedPreference.getString(MySharedPreferenceKey.LoginKey.TOKEN));
//        param.putAllHeader(header);
//
//        AppImpl.getInstance(mActivity).connectDeviceJvmp(param, true).subscribe(new Action1<ResponseData<MtsJvmpListBean>>() {
//            @Override
//            public void call(ResponseData<MtsJvmpListBean> responseData) {
//                if (responseData.getCode() == ResponseCode.SUCCESS) {
////                    list.get(0).setToken(responseData.getData().getResult().get(0).getToken());
////                    list.get(0).setMts(new Gson().toJson(new PlayMtsBean(responseData.getData().getResult().get(0).getMts_list())));
//                    if (responseData.getData().getDevices() != null) {
//                        for (MtsBean mtsBean : responseData.getData().getDevices()) {
//                            for (Glass glass : mGlassList) {
//                                if (null != glass.getChannel() &&
//                                        TextUtils.equals(mtsBean.getDevice_id(), glass.getChannel().getParent().getSn())) {
//                                    glass.getChannel().setToken(mtsBean.getToken());
//                                    glass.getChannel().setMts(new Gson().toJson(new PlayMtsBean(mtsBean.getMts_list())));
//                                }
//                            }
//                        }
//                    }
//
//                    if (responseData.getData().getChannels() != null) {
//                        for (JvmpBean bean : responseData.getData().getChannels()) {
//                            for (Glass glass : mGlassList) {
//                                boolean isMatch = false;
//                                if (glass != null && glass.getChannel() != null && glass.getChannel().getParent() != null && glass.getChannel().getParent().isGB28181Device()) {
//                                    isMatch = TextUtils.equals(bean.getChannel_id(), glass.getChannel().getIpc_device_channel_id());
//                                } else {
//                                    try {
//                                        int hwChannelId = Integer.parseInt(bean.getChannel_id());
//                                        isMatch = hwChannelId == glass.getChannel().getChannel();
//                                    } catch (Exception ex) {
//
//                                    }
//                                }
//                                if (null != glass.getChannel() &&
//                                        TextUtils.equals(bean.getDevice_id(), glass.getChannel().getParent().getSn()) && isMatch) {
////                                    &&
////                                    (bean.getChannel_id() == glass.getChannel().getChannel())
//                                    String jvmp_url = "";
//                                    if (!TextUtils.isEmpty(bean.getJvmp_url())) {
//                                        jvmp_url = bean.getJvmp_url();
//                                    } else if (!TextUtils.isEmpty(bean.getUrl())) {
//                                        jvmp_url = bean.getUrl();
//                                    }
//                                    glass.getChannel().setJvmpUrl(jvmp_url);
//                                    glass.getChannel().setWorkIngKey(bean.getWorking_key());
//                                    if (TextUtils.isEmpty(jvmp_url)) {
//                                        ToastUtils.show(mActivity, "没有获取到播放地址");
//                                    }
//                                }
//                            }
//                        }
//
//                    }
//                } else if (ErrorUtil.CheckError(responseData.getCode())) {
//                    ToastUtils.show(mActivity, ErrorUtil.getInstance().getErrorValue(responseData.getCode()));
//                }
//                getGbStreamSuccess = true;
//                if (getGbStreamSuccess && getHwStreamSuccess) {
//                    startInstallGlass();
//                }
//            }
//        });
        startInstallGlass();
    }

    /**
     * 批量获取好望设备直播流
     *
     * @param list
     */
    private void getHwLiveStream(List<MtsJvmpRequestBean> list) {
//        BaseRequestParam param = new BaseRequestParam();
//        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
//        data.put("enterprise_id", MySharedPreference.getString(MySharedPreferenceKey.LoginKey.CURRENT_ENTERPRISE));
//        data.put("channels", com.alibaba.fastjson.JSONObject.toJSON(list));
//        param.putAll(data);
//        HashMap<String, Object> header = HeaderUtil.createHeader(MySharedPreference.getString(MySharedPreferenceKey.LoginKey.TOKEN));
//        param.putAllHeader(header);
//
//        AppImpl.getInstance(mActivity).connectDeviceJvmp(param, false).subscribe(new Action1<ResponseData<MtsJvmpListBean>>() {
//            @Override
//            public void call(ResponseData<MtsJvmpListBean> responseData) {
//                if (responseData.getCode() == ResponseCode.SUCCESS) {
////                    list.get(0).setToken(responseData.getData().getResult().get(0).getToken());
////                    list.get(0).setMts(new Gson().toJson(new PlayMtsBean(responseData.getData().getResult().get(0).getMts_list())));
//                    if (responseData.getData().getDevices() != null) {
//                        for (MtsBean mtsBean : responseData.getData().getDevices()) {
//                            for (Glass glass : mGlassList) {
//                                if (null != glass.getChannel() &&
//                                        TextUtils.equals(mtsBean.getDevice_id(), glass.getChannel().getParent().getSn())) {
//                                    glass.getChannel().setToken(mtsBean.getToken());
//                                    glass.getChannel().setMts(new Gson().toJson(new PlayMtsBean(mtsBean.getMts_list())));
//                                }
//                            }
//                        }
//                    }
//
//                    if (responseData.getData().getChannels() != null) {
//                        for (JvmpBean bean : responseData.getData().getChannels()) {
//                            for (Glass glass : mGlassList) {
////                                &&
////                                (bean.getChannel_id() == glass.getChannel().getChannel())
//                                boolean isMatch = false;
//                                if (glass.getChannel() != null) {
//                                    if (glass.getChannel().getParent().isGB28181Device()) {
//                                        isMatch = TextUtils.equals(bean.getChannel_id(), glass.getChannel().getIpc_device_channel_id());
//                                    } else {
//                                        try {
//                                            int hwChannelId = Integer.parseInt(bean.getChannel_id());
//                                            isMatch = hwChannelId == glass.getChannel().getChannel();
//                                        } catch (Exception ex) {
//
//                                        }
//                                    }
//                                }
//                                if (null != glass.getChannel() &&
//                                        TextUtils.equals(bean.getDevice_id(), glass.getChannel().getParent().getSn()) && isMatch) {
//                                    glass.getChannel().setJvmpUrl(bean.getJvmp_url());
//                                    glass.getChannel().setWorkIngKey(bean.getWorking_key());
//                                    if (TextUtils.isEmpty(bean.getJvmp_url())) {
//                                        ToastUtils.show(mActivity, "没有获取到播放地址");
//                                    }
//                                }
//                            }
//                        }
//
//                    }
//                } else if (ErrorUtil.CheckError(responseData.getCode())) {
//                    ToastUtils.show(mActivity, ErrorUtil.getInstance().getErrorValue(responseData.getCode()));
//                }
//                getHwStreamSuccess = true;
//                if (getGbStreamSuccess && getHwStreamSuccess) {
//                    startInstallGlass();
//                }
//            }
//        });
        startInstallGlass();
    }

    private void startInstallGlass() {

        Log.e(TAG, "窗户[" + mWindowNo + "] 安裝玻璃, 窗户是否可见:" + isWindowVisibleToUser);
        SimpleTask installTask = new SimpleTask() {
            @Override
            public void doInBackground() {
                MySharedPreference.putLong(MySharedPreferenceKey.PlayKey.PLAY_LAST_CONNECT_TIME, System.currentTimeMillis());

                for (int i = 0; i < mGlassCount; i++) {
                    Glass glass = mGlassList.get(i);
                    if (glass.isInstalled()) {
                        Log.e(TAG, "玻璃[" + i + "] 已被安装");
                    }
                    RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForLayoutPosition(i);
                    int timeout = 0;
                    while (holder == null && timeout < 1000) {
                        SystemClock.sleep(50);
                        holder = mRecyclerView.findViewHolderForLayoutPosition(i);
                        timeout += 50;
                    }

                    if (holder != null) {
                        if (holder instanceof BaseGlassC2) {
                            BaseGlassC2 baseViewHolder = (BaseGlassC2) holder;
                            baseViewHolder.bindGlass(glass);
                            baseViewHolder.connect();
                            // 记录玻璃已经被安装
                            glass.setInstalled(true);
                            Log.e(TAG, "玻璃[" + i + "] 安裝成功");
                        }
                    } else {
                        Log.e(TAG, "玻璃[" + i + "] 安裝失败");
                    }
                }
            }

            @Override
            public void onFinish(boolean canceled) {
            }
        };

        SimpleThreadPool.execute(installTask);
    }

    /**
     * 关窗帘
     * ps:
     * a.窗帘关上以后, 窗户上的玻璃对用户是不可见的(但是窗户是可见的)
     * b.关窗帘,进行pause处理
     */
    private void closeTheCurtain() {
        if (mActivity.isBackPressed()) {
            return;
        }
        Log.e(TAG, "窗户[" + mWindowNo + "] 关窗帘");
        isCloseCurtain = true;
        for (int i = 0; i < mGlassCount; i++) {
            if (i < mGlassList.size()) {
                Glass glass = mGlassList.get(i);
                if (glass != null) {
                    Channel channel = glass.getChannel();
                    if (channel != null) {
                        Device device = channel.getParent();
                        RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForLayoutPosition(i);
                        if (holder != null) {
                            if (holder instanceof BasePlayGlass) {
                                BasePlayGlass baseViewHolder = (BasePlayGlass) holder;
                                baseViewHolder.pause(device.isGB28181Device());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 开窗帘
     * ps:
     * a.窗帘打开以后, 窗户上的玻璃对用户是可见的
     * b.开窗帘,进行resume处理
     */
    private void openTheCurtain() {
        if (mActivity.isBackPressed()) {
            return;
        }
        Log.e(TAG, "窗户[" + mWindowNo + "] 开窗帘");
        isCloseCurtain = false;
        for (int i = 0; i < mGlassCount; i++) {
            RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForLayoutPosition(i);
            if (holder != null) {
                if (holder instanceof BasePlayGlass) {
                    BasePlayGlass baseViewHolder = (BasePlayGlass) holder;
                    baseViewHolder.resume();
                }
            }
        }
    }

    /**
     * 拆除玻璃, 拆除窗户
     */
    private void removeWindow() {
        if (mActivity.isBackPressed()) {
            return;
        }
        Log.e(TAG, "窗户[" + mWindowNo + "] 拆除");
        isWindowRemoved = true;
        for (int i = 0; i < mGlassCount; i++) {
            Glass glass = mGlassList.get(i);
            RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForLayoutPosition(i);
            if (holder != null) {
                if (holder instanceof BasePlayGlass) {
                    BasePlayGlass baseViewHolder = (BasePlayGlass) holder;
                    baseViewHolder.disconnect();
                    // 记录拆除玻璃
                    glass.setInstalled(false);
                }
            }
        }
    }

    // ----------------------------------------------------------------
    // # 分发处理底层回调 start
    // ----------------------------------------------------------------

    /**
     * 分发处理底层回调, 只接收分发本窗户上的玻璃的回调信息
     *
     * @param what
     * @param glassNo 玻璃号
     * @param result
     * @param obj
     */
    public void dispatchNativeCallback(final int what, final int glassNo, final int result, final Object obj) {


        // 检查窗户是否被拆除
        if (mGlassList == null || mRecyclerView == null) {
            return;
        }

        /*
          检查是否处理统计回调
          窗户上有多块玻璃的时候不处理统计回调
         */
        boolean isNotHandleReport = what == JVEncodedConst.WHAT_CALL_STAT_REPORT && mSpanCount != 1;
        if (isNotHandleReport) {
            return;
        }

        /*
           检查本窗户上是否有 [glassNo] 这块玻璃, 没有不处理
           但是统计回调中的glassNo值为0, 所以非统计回调, 并且玻璃不属于当前窗户的才return
         */
        int glassIndex = -1;
        if (what == JVEncodedConst.WHAT_CALL_STAT_REPORT) {
            glassIndex = 0;
        } else {
            glassIndex = contains(mGlassList, glassNo);
            if (glassIndex == -1) {
                return;
            }
        }

        // 将回调传递到不同类型的玻璃上面
        int finalGlassIndex = glassIndex;
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                final RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForLayoutPosition(finalGlassIndex);
                if (holder != null) {
                    if (holder instanceof BasePlayGlass) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                BasePlayGlass baseViewHolder = (BasePlayGlass) holder;
                                baseViewHolder.handleNativeCallback(what, glassNo, result, obj);
                            }
                        });

                    }
                }
            }
        });
    }

    /**
     * 判断数组中是否包含某元素
     *
     * @param glassList
     * @param glassNo
     * @return 返回元素在数组中的索引
     */
    public static int contains(List<Glass> glassList, int glassNo) {
        int position = -1;
        int length = glassList.size();
        for (int i = 0; i < length; i++) {
            Glass item = glassList.get(i);
            if (item.getNo() == glassNo) {
                position = i;
                return position;
            }
        }
        return position;
    }
    // ----------------------------------------------------------------
    // # 分发处理底层回调 end
    // ----------------------------------------------------------------

    // ----------------------------------------------------------------
    // # 横竖屏切换 start
    // ----------------------------------------------------------------
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 设置窗户的尺寸(窗户的尺寸一旦固定后就不会改变)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mWindowSize.width = mScreenHeight;
            mWindowSize.height = mScreenWidth;

            //刘海屏 横屏适配
            Rect outRect = new Rect();
            getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
            if( outRect.width()==0){
                mGlassSize.width = mScreenHeight / mColumnNo;
            }else{
                mGlassSize.width = outRect.width() / mColumnNo;
            }
            mGlassSize.height = mScreenWidth / mColumnNo;
        } else {
            int windowHeight = mScreenWidth / 16 * 9;
            mWindowSize.width = mScreenWidth;
            mWindowSize.height = windowHeight;
            mGlassSize.width = mScreenWidth / mColumnNo;
            mGlassSize.height = windowHeight / mColumnNo;
        }

        // 修改窗户上玻璃的尺寸
        for (int i = 0; i < mGlassCount; i++) {
            RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForLayoutPosition(i);
            if (holder != null) {
                if (holder instanceof BaseGlass) {
                    BaseGlass baseViewHolder = (BaseGlass) holder;
                    baseViewHolder.changeGlassSize(mGlassSize);
                }
            }
        }
    }
    // ----------------------------------------------------------------
    // # 横竖屏切换 end
    // ----------------------------------------------------------------

    // ----------------------------------------------------------------
    // # EventBus start
    // ----------------------------------------------------------------

    /**
     * 设备列表操作通知
     */
    //todo
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEventBus(MsgEvent event) {
        switch (event.getMsgTag()) {
            case MsgEvent.MSG_EVENT_GLASS_SELECT:// 改变玻璃的选择
            case MsgEvent.MSG_EVENT_CHANGE_WINDOW_DOUBLECLICK:// 双击改变窗户上的玻璃数量
            case MsgEvent.MSG_EVENT_GLASS_CHANGE:// 左右滑动切换玻璃
                try {
                    JSONObject item = new JSONObject(event.getAttachment());
                    mSelectedGlassNo = item.getInt("glassNo");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // 改变border颜色
                for (int i = 0; i < mGlassCount; i++) {
                    RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForLayoutPosition(i);
                    if (holder != null) {
                        if (holder instanceof BasePlayGlass) {
                            BasePlayGlass baseViewHolder = (BasePlayGlass) holder;
                            baseViewHolder.changeBorderColor(mSelectedGlassNo);
                        }
                    }
                }
                break;
            case MsgEvent.MSG_EVENT_INTERACTION:// 功能区和播放区的交互
                /*
                  功能区和播放区交互时, 窗户肯定对用户可见
                 */
                if (isWindowVisibleToUser) {
                    try {
                        RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForLayoutPosition(0);
                        if (holder == null) {
                            return;
                        }
                        if (!(holder instanceof BasePlayGlass)) {
                            return;
                        }
                        BasePlayGlass baseViewHolder = (BasePlayGlass) holder;
                        // 码流切换、单向对讲等操作
                        baseViewHolder.doInteraction(event.getAttachment());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case MsgEvent.MSG_EVENT_ABNORMAL_DISCONNECT:
                isAbnormalDisconnect = true;
                break;
            default:
                break;
        }
    }
    // ----------------------------------------------------------------
    // # EventBus end
    // ----------------------------------------------------------------
}
