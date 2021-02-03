package com.huawei.holosens.live.play.ui;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huawei.holosens.R;
import com.huawei.holosens.live.play.bean.Glass;
import com.huawei.holosens.live.play.event.MsgEvent;
import com.huawei.holosens.view.PlayViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * 播放Fragment
 *
 */
public class PlayFragment extends SimpleFragment {
    //
    private static final String SPAN_COUNT = "span_count";
    private static final String ON_WINDOW_NO = "on_window_no";
    private static final String SELECTED_GLASS_NO = "selected_glass_no";

    // 分屏数量
    private int mSpanCount = 1;
    // 指定的画面在哪扇窗户上面
    private int mOnWindowNo;
    // 当前选中的玻璃
    private int mSelectedGlassNo;
    protected PlayViewPager mViewPager;
    private List<List<Glass>> mWindowList;
    private WindowPagerAdapter mWindowPagerAdapter;
    // 用户当前可以看到的窗户号
    private int mCurrentSelectedWindowNo;//从0开始
    // 窗户的数量
    private int mWindowCount;
    private Bundle mBundleParam;
    private View mRootView;
    private ViewGroup.LayoutParams mRootViewLayoutParams;
    private boolean mInitSelect = true;

    private boolean mIsEdit = false;//是否是编辑状态

    public PlayFragment() {
    }

    /**
     * @param bundle 通道号/窗口所在的分组号/窗口等
     */
    public static PlayFragment newInstance(Bundle bundle) {
        PlayFragment fragment = new PlayFragment();
        Bundle args = new Bundle();
        args.putInt(SPAN_COUNT, bundle.getInt("spanCount"));
        args.putInt(ON_WINDOW_NO, bundle.getInt("onWindowNo"));
        args.putInt(SELECTED_GLASS_NO, bundle.getInt("selectedGlassNo"));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            mSpanCount = getArguments().getInt(SPAN_COUNT);
            mOnWindowNo = getArguments().getInt(ON_WINDOW_NO);
            mSelectedGlassNo = getArguments().getInt(SELECTED_GLASS_NO);
        }

        mCurrentSelectedWindowNo = mOnWindowNo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == mRootView) {
            mRootView = inflater.inflate(R.layout.fragment_play, container, false);
            mRootViewLayoutParams = mRootView.getLayoutParams();
            mRootViewLayoutParams.height = mScreenWidth / 16 * 9;
            mViewPager = mRootView.findViewById(R.id.windows_view_pager);
            mWindowList = mActivity.getWindowList();
            mWindowCount = mWindowList.size();
            mWindowPagerAdapter = new WindowPagerAdapter(getChildFragmentManager());
            mViewPager.setAdapter(mWindowPagerAdapter);
            mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    // 右上角通过切不同多分屏时，页码更换，更新选中的窗户号
                    mCurrentSelectedWindowNo = position;
                    mActivity.setSelectedWindowNo(position);
                }

                /**
                 * viewpager：切换窗户，刷新播放区域（单屏+多屏）
                 *
                 * @param position
                 */
                @Override
                public void onPageSelected(int position) {

                    mViewPager.setDisableSliding(true);
                    mViewPager.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mViewPager.setDisableSliding(mIsEdit);
                        }
                    }, 1500);

                    // 更新选中的窗户号
                    mCurrentSelectedWindowNo = position;
                    mActivity.setSelectedWindowNo(position);

                    /*
                      单屏时(一扇窗户上只有一块玻璃),窗户切换时,及时更新当前选中的玻璃号
                     */
                    MsgEvent event = new MsgEvent();
                    JSONObject item = new JSONObject();
                    event.setMsgTag(MsgEvent.MSG_EVENT_GLASS_CHANGE);

                    //滑屏时，默认选中左上角第一个视频
                    int needSelectGlass;
                    if (mSpanCount == 1) {
                        needSelectGlass = position;
                    } else {
                        needSelectGlass = mCurrentSelectedWindowNo * mSpanCount;

                        //处理初始化时默认选中窗口逻辑
                        if (mSelectedGlassNo > needSelectGlass) {
                            if (mInitSelect) {
                                needSelectGlass = mSelectedGlassNo;
                                mInitSelect = false;
                            }
                        } else {
                            mInitSelect = false;
                        }
                    }

                    try {

                        //分屏左上角第一个是+不是视频，不处理，会导致滑屏崩溃
                        if (needSelectGlass >= mActivity.getAddGlassList().size()) {
                            item.put("changeToAdd", true);
                            item.put("glassNo", needSelectGlass);
                            event.setAttachment(item.toString());
                            EventBus.getDefault().post(event);
                            return;
                        }

                        Glass glass = mActivity.getGlassByIndex(needSelectGlass);
                        item.put("glassNo", needSelectGlass);
                        item.put("deviceNo", glass.getChannel().getParent().getSn());
                        item.put("channelNo", glass.getChannel().getChannel());
                        item.put("deviceType", glass.getChannel().getDevType());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    event.setAttachment(item.toString());
                    EventBus.getDefault().post(event);


                    // 滑屏之后，隐藏之前显示的选择码流布局
                    MsgEvent hiddenStreamEvent = new MsgEvent();
                    hiddenStreamEvent.setMsgTag(MsgEvent.MSG_EVENT_INTERACTION);
                    JSONObject object = new JSONObject();
                    try {
                        object.put("type", "hiddenStream");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    hiddenStreamEvent.setAttachment(object.toString());
                    EventBus.getDefault().post(hiddenStreamEvent);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });

            mViewPager.setCurrentItem(mCurrentSelectedWindowNo);
        }

        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void setEdit(boolean isEdit) {
        this.mIsEdit = isEdit;

        if (null != mViewPager) {
            mViewPager.setDisableSliding(isEdit);
        }

        List<Fragment> windowList = getWindowList();
        if (null == windowList) {
            return;
        }
        int windowCount = windowList.size();
        for (int i = 0; i < windowCount; i++) {
            WindowFragment window = (WindowFragment) windowList.get(i);
            if (null == window) {
                continue;
            }

            window.setEdit(mIsEdit);
        }
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
        mBundleParam = null;
        int spanCount = bundle.getInt("spanCount");
        mOnWindowNo = bundle.getInt("onWindowNo");
        mSelectedGlassNo = bundle.getInt("selectedGlassNo");
        mCurrentSelectedWindowNo = mOnWindowNo;
        int windowCount = mActivity.getWindowList().size();
        /*
          窗户数量变化或者分屏数量变化，都重新加载窗户
          // TODO
         */
        if (windowCount != mWindowCount || spanCount != mSpanCount) {
            mWindowCount = windowCount;
            mSpanCount = spanCount;
            mWindowList = mActivity.getWindowList();
            // 下面这句设置Adapter不添加的话,CustomFragmentStatePagerAdapter中的instantiateItem和destroyItem会产生奇怪的数据打印,完全不符合我们的预期
            mViewPager.setAdapter(mWindowPagerAdapter);
            mViewPager.setCurrentItem(mCurrentSelectedWindowNo);
            mWindowPagerAdapter.notifyDataSetChanged();
        } else {

            /*
              TODO 窗户数量变化和分屏数量都没有变化, 不重新加载(防止现有的设备连接断开)
              TODO 现在else中采用和if中相同的逻辑, 后期如果必须要进行调整, 可以在这里继续进行修改。
             */
            mWindowCount = windowCount;
            mSpanCount = spanCount;
            mWindowList = mActivity.getWindowList();
            // 下面这句设置Adapter不添加的话,CustomFragmentStatePagerAdapter中的instantiateItem和destroyItem会产生奇怪的数据打印,完全不符合我们的预期
            mViewPager.setAdapter(mWindowPagerAdapter);
            mViewPager.setCurrentItem(mCurrentSelectedWindowNo);
            mWindowPagerAdapter.notifyDataSetChanged();
        }
    }

    public Bundle getBundleParam() {
        if (mBundleParam == null) {
            mBundleParam = new Bundle();
            mBundleParam.putInt("spanCount", mSpanCount);
            mBundleParam.putInt("selectedGlassNo", mSelectedGlassNo);
        }
        return mBundleParam;
    }

    /**
     * 获取窗户列表
     */
    public List<Fragment> getWindowList() {
        return mWindowPagerAdapter == null ? null : mWindowPagerAdapter.getWindows();
    }

    /**
     * 禁用/启用滑动，响应缩放手势
     */
    public void setDisableSliding(boolean disableSliding) {
        if (!mIsEdit) {
            mViewPager.setDisableSliding(disableSliding);
        }
    }

    public boolean isDisableSliding() {
        return mViewPager.isDisableSliding();
    }

    /**
     * FunctionPagerAdapter
     */
    class WindowPagerAdapter extends FragmentStatePagerAdapter {
        private FragmentManager mFragmentManager;

        private WindowPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragmentManager = fm;
        }

        /**
         * @return
         */
        public List<Fragment> getWindows() {
            return mFragmentManager.getFragments();
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = getBundleParam();
            bundle.putInt("windowNo", position);
            return WindowFragment.newInstance(bundle);
        }

        @Override
        public int getCount() {
            return mWindowList.size();
        }

        // This is called when notifyDataSetChanged() is called
        @Override
        public int getItemPosition(Object object) {
            // refresh all fragments when data set changed
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }
    }

    /**
     * 分发处理底层回调
     *
     * @param what
     * @param glassNo
     * @param result
     * @param obj
     */
    public void dispatchNativeCallback(int what, int glassNo, int result, Object obj) {
        /*
          getWindows()方法不能获取到被删除的Fragment, 所以视频断开的回调无法传递到Fragment中.
          经过和底层同事沟通,可以通过disconnect方法的返回值来判断是否断开
         */
        // 转发到WindowFragment中(转到具体的处理信息的窗户上面)
        List<Fragment> windowList = getWindowList();
        if (null == windowList) {
            return;
        }
        int windowCount = windowList.size();
        for (int i = 0; i < windowCount; i++) {
            WindowFragment window = (WindowFragment) windowList.get(i);
            if (null == window) {
                continue;
            }

            window.dispatchNativeCallback(what, glassNo, result, obj);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            刘海屏 横屏适配
            Rect outRect = new Rect();
            getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
            if( outRect.width()==0){
                mRootViewLayoutParams.width = mScreenHeight;
            }else{
                mRootViewLayoutParams.width = outRect.width();
            }
            mRootViewLayoutParams.height = mScreenWidth;
        } else {
            mRootViewLayoutParams.width = mScreenWidth;
            mRootViewLayoutParams.height = mScreenWidth / 16 * 9;

            setDisableSliding(false);
        }
    }

    // ----------------------------------------------------------------
    // # EventBus start
    // ----------------------------------------------------------------
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEventBus(MsgEvent event) {
        switch (event.getMsgTag()) {
            case MsgEvent.MSG_EVENT_GLASS_SELECT:// 单击选择
            case MsgEvent.MSG_EVENT_CHANGE_WINDOW_DOUBLECLICK:// 双击改变窗户上的玻璃数量
            case MsgEvent.MSG_EVENT_GLASS_CHANGE:// 左右滑动切换玻璃
                try {
                    JSONObject item = new JSONObject(event.getAttachment());
                    mSelectedGlassNo = item.getInt("glassNo");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
    // ----------------------------------------------------------------
    // # EventBus end
    // ----------------------------------------------------------------
}