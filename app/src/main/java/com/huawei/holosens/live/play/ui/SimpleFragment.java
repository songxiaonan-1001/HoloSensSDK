package com.huawei.holosens.live.play.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;

import com.huawei.holobasic.utils.ScreenUtils;
import com.huawei.holosens.live.play.util.NavigationBarTools;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SimpleFragment extends Fragment {


    protected JVMultiPlayActivity mActivity;
    // 屏幕宽度、高度
    protected int mScreenWidth, mScreenHeight,mBarHeight = 0;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (JVMultiPlayActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getRealMetrics();
    }

    @Override
    public void onResume() {
        super.onResume();
        getRealMetrics();
    }

    /**
     * 获取屏幕的宽高
     * 因为虚拟按键原因
     */
    protected void getRealMetrics() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            DisplayMetrics dm = new DisplayMetrics();
//            mActivity.getWindowManager().getDefaultDisplay().getRealMetrics(dm);
//            mScreenWidth = dm.widthPixels;
//            mScreenHeight = dm.heightPixels;
//        } else {
//            Display display = mActivity.getWindowManager().getDefaultDisplay();
//            DisplayMetrics dm = new DisplayMetrics();
//            @SuppressWarnings("rawtypes")
//            Class c;
//            try {
//                c = Class.forName("android.view.Display");
//                @SuppressWarnings("unchecked")
//                Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
//                method.invoke(display, dm);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            mScreenWidth = dm.widthPixels;
//            mScreenHeight = dm.heightPixels;
//        }

        mScreenWidth = ScreenUtils.getScreenWidth();
        mScreenHeight = ScreenUtils.getScreenHeight();
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Rect outRect = new Rect();
            getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
            if (outRect.width() != 0) {
                mScreenHeight = outRect.width();
            }
        }

        if(NavigationBarTools.hasNavBar(mActivity)){
            mBarHeight = NavigationBarTools.getNavigationBarHeight(mActivity);
        }

    }

    /**
     * 显示/隐藏横屏时底部bar
     *
     * @param isVisible
     */
    protected void switchLandBottomBar(boolean isVisible) {

    }

    // ----------------------------------------------------------------
    // # 横竖屏切换
    // ----------------------------------------------------------------
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getRealMetrics();
    }

    // ----------------------------------------------------------------
    // # EventBus
    // ----------------------------------------------------------------
    /*
      此处不直接接收EventBus,功能区只在FunctionsFragment中接收EventBus.
     */
//    public void handleEventBus(MsgEvent event) {
//    }

    // ----------------------------------------------------------------
    // # 底层回调处理
    // ----------------------------------------------------------------

    /**
     * 分发处理底层回调, 只接收分发本窗户上的玻璃的回调信息
     *
     * @param what
     * @param glassNo 玻璃号
     * @param result
     * @param obj
     */

    public void handleNativeCallback(int what, int glassNo, int result, Object obj) {
    }
}
