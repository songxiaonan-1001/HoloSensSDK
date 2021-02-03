package com.huawei.holosens.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;


/**
 * @class name AlarmViewPager
 * @date 创建时间：2020-02-27    14:09
 * @description: 可以屏蔽左滑查看下一个或者右滑查看上一个的ViewPager
 */
public class AlarmViewPager extends ViewPager {

    private static final String TAG = "AlarmViewPager";

    private Context mContext;
    private boolean disableLeftScroll, disableRightScroll;
    private float mLastX;


    public AlarmViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public AlarmViewPager(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.e(TAG, "dispatchTouchEvent: " + ev.getX() );
        if (!disableLeftScroll && !disableRightScroll) {
            return super.dispatchTouchEvent(ev);
        } else {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mLastX = ev.getX();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float distance = ev.getX() - mLastX;
                    if (disableLeftScroll && distance < 0) {
                        return true;
                    }
                    if (disableRightScroll && distance > 0) {
                        return true;
                    }
                    mLastX = ev.getX();
                    break;
                case MotionEvent.ACTION_UP:
                    mLastX = 0;
                    break;
            }
            return super.dispatchTouchEvent(ev);
        }
    }

    /**
     * 设置禁用左滑
     */
    public void setDisableLeftScroll(boolean disableLeftScroll) {
        this.disableLeftScroll = disableLeftScroll;
    }

    /**
     * 设置禁用右滑
     */
    public void setDisableRightScroll(boolean disableRightScroll) {
        this.disableRightScroll = disableRightScroll;
    }

    /**
     * 设置禁用左滑和右滑
     */
    public void setDisableScroll(boolean disableLeftScroll, boolean disableRightScroll) {
        this.disableLeftScroll = disableLeftScroll;
        this.disableRightScroll = disableRightScroll;
    }

}
