package com.huawei.holosens.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.huawei.holobasic.utils.ScreenUtils;
import com.huawei.holosens.R;

import androidx.annotation.Nullable;


public class PointsView extends View {


    private static final String TAG = "PointsView";

    public static final int POINT_RADIUS = ScreenUtils.dip2px(1); // 圆点半径
    public static final int POINTS_SPACE = ScreenUtils.dip2px(20); // 圆点间的间隔

    private int mHorizontalPadding; // 水平方向内边距

    private Paint mPaint;


    public PointsView(Context context) {
        super(context);
        init();
    }

    public PointsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PointsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int oneWidth = POINTS_SPACE + POINT_RADIUS * 2;
        int pointCountH = getWidth() / oneWidth;
        if (getWidth() - oneWidth * pointCountH < oneWidth) {
            pointCountH--;
        }
        mHorizontalPadding = (getWidth() - oneWidth * pointCountH + POINTS_SPACE) / 2;
        int pointCountV = getHeight() / oneWidth;
        for (int i = 0; i < pointCountV; i++) {
            float cy = POINTS_SPACE + POINT_RADIUS + i * oneWidth;
            for (int j = 0; j < pointCountH; j++) {
                float cx = mHorizontalPadding + POINT_RADIUS + j * oneWidth;
                canvas.drawCircle(cx, cy, POINT_RADIUS, mPaint);
            }
        }
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(getResources().getColor(R.color.points_color));
    }



}
