package com.huawei.holosens.view.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;
import com.huawei.holobasic.utils.ScreenUtils;
import com.huawei.holosens.R;

/**
 * @class name HoloMonthView
 * @date 创建时间：2020-01-18    11:00
 * @description: 日历月视图样式
 */
public class HoloPlaybackMonthView extends MonthView {

    private static final String TAG = "HoloPlaybackMonthView";

    private int mRadius;

    /**
     * 背景圆点
     */
    private Paint mPointPaint = new Paint();

    public HoloPlaybackMonthView(Context context) {
        super(context);
        //兼容硬件加速无效的代码
//        setLayerType(View.LAYER_TYPE_SOFTWARE,mSelectedPaint);
//        //4.0以上硬件加速会导致无效
//        mSelectedPaint.setMaskFilter(new BlurMaskFilter(25, BlurMaskFilter.Blur.SOLID));
        mCurMonthTextPaint.setFakeBoldText(false);
        mOtherMonthTextPaint.setFakeBoldText(false);
        mSchemeTextPaint.setFakeBoldText(false);
        mSelectTextPaint.setFakeBoldText(false);
        mCurDayTextPaint.setFakeBoldText(true);
        mCurDayLunarTextPaint.setFakeBoldText(false);

        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 5 * 2;
        mSchemePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onLoopStart(int x, int y) {

    }

    /**
     * 绘制选中的日子
     * @param canvas    canvas
     * @param calendar  日历日历calendar
     * @param x         日历Card x起点坐标
     * @param y         日历Card y起点坐标
     * @param hasScheme hasScheme 非标记的日期
     * @return 返回true 则会继续绘制onDrawScheme，因为这里背景色不是是互斥的，所以返回true，返回false，则点击scheme标记的日子，则不继续绘制onDrawScheme，自行选择即可
     */
    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        mSelectedPaint.setColor(getResources().getColor(R.color.main));
        mSelectedPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
        return false;
    }

    /**
     * 绘制标记的事件日子
     * @param canvas   canvas
     * @param calendar 日历calendar
     * @param x        日历Card x起点坐标
     * @param y        日历Card y起点坐标
     */
    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
//        Log.e(TAG, "onDrawScheme: " + calendar.toString() + ", " + calendar.isCurrentMonth() + ", " + calendar.isCurrentDay() );
        if (calendar.isCurrentMonth()) {
            boolean isSelected = isSelected(calendar);
            if (isSelected) {
                mPointPaint.setColor(Color.WHITE);
            } else {
                mPointPaint.setColor(getResources().getColor(R.color.main));
            }

            canvas.drawCircle(x + mItemWidth / 2, y + mItemHeight - ScreenUtils.dip2px(6), ScreenUtils.dip2px(2), mPointPaint);
        }
    }

    /**
     * 绘制文本
     * @param canvas     canvas
     * @param calendar   日历calendar
     * @param x          日历Card x起点坐标
     * @param y          日历Card y起点坐标
     * @param hasScheme  是否是标记的日期
     * @param isSelected 是否选中
     */
    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        float baselineY = mTextBaseLine + y;
        int cx = x + mItemWidth / 2;

//        Log.e(TAG, "onDrawText: " + calendar.toString() + ", " + calendar.isCurrentMonth() + ", " + calendar.isCurrentDay() );
        if (calendar.isCurrentMonth() && calendar.isCurrentDay() && !isSelected) {
            Log.e(TAG, "onDrawText1: ");
            Paint currentDayPaint = new Paint();
            currentDayPaint.setAntiAlias(true);
            currentDayPaint.setStyle(Paint.Style.FILL);
            currentDayPaint.setColor(getResources().getColor(R.color.today_bg));
            canvas.drawCircle(cx, y + mItemHeight / 2, mRadius, currentDayPaint);
            canvas.drawText(String.format("%02d", calendar.getDay()), cx, baselineY, mCurDayTextPaint);
        } else

        if (isSelected) {
//            Log.e(TAG, "onDrawText2: ");
            canvas.drawText(String.format("%02d", calendar.getDay()),
                    cx,
                    baselineY,
                    mSelectTextPaint);
        } else if (hasScheme) {
//            Log.e(TAG, "onDrawText3: ");
            canvas.drawText(String.valueOf(calendar.getScheme()),
                    cx,
                    baselineY,
                    calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);

        } else {
//            Log.e(TAG, "onDrawText4: ");
            canvas.drawText(String.format("%02d", calendar.getDay()), cx, baselineY,
                    calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
        }
    }
}
