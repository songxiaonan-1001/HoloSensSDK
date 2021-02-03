package com.huawei.holosens.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;


/**
 * 可以屏蔽滑动的 ViewPager
 *
 */
public class PlayViewPager extends ViewPager {

    private static final String TAG = "PlayViewPager";

    private boolean disableSliding;
    private Context mContext;

    public PlayViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        disableSliding = false;
        mContext = context;
    }

    public PlayViewPager(Context context) {
        super(context);
        disableSliding = false;
        mContext = context;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    /**
     * 设置禁用滑动
     *
     * @param disableSliding
     */
    public void setDisableSliding(boolean disableSliding) {
        this.disableSliding = disableSliding;
    }

    public boolean isDisableSliding() {
        return disableSliding;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {

        if (disableSliding) return false;
        return super.onInterceptTouchEvent(arg0);
    }

}
