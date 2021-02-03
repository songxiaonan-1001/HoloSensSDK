package com.huawei.holosens.view.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;
import com.huawei.holosens.R;

/**
 * @class name HoloMonthView
 * @date 创建时间：2020-01-18    11:00
 * @description: 日历月视图样式
 */
public class HoloMonthView extends MonthView {

    private int mRadius;

    public HoloMonthView(Context context) {
        super(context);
        //兼容硬件加速无效的代码
//        setLayerType(View.LAYER_TYPE_SOFTWARE,mSelectedPaint);
//        //4.0以上硬件加速会导致无效
//        mSelectedPaint.setMaskFilter(new BlurMaskFilter(25, BlurMaskFilter.Blur.SOLID));
        mCurMonthTextPaint.setFakeBoldText(false);
        mOtherMonthTextPaint.setFakeBoldText(false);
        mSchemeTextPaint.setFakeBoldText(false);
        mSelectTextPaint.setFakeBoldText(false);
        mCurDayTextPaint.setFakeBoldText(false);
        mCurDayLunarTextPaint.setFakeBoldText(false);
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
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        mSchemePaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(cx, cy, mRadius, mSchemePaint);
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

        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    mSelectTextPaint);
        } else if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getScheme()),
                    cx,
                    baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);

        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
        }
    }
}
