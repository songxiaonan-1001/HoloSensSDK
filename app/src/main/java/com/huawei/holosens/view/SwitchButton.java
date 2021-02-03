package com.huawei.holosens.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * @ProjectName: HoloSens
 * @Package: com.huawei.holosens.view
 * @ClassName: SwitchButton
 * @Description: java类作用描述
 * @CreateDate: 2020-01-05 12:08
 * @Version: 1.0
 */
public class SwitchButton  extends View {

    private static final String TAG = SwitchButton.class.getSimpleName();
    private static final int mDefaultW = 41;
    private static final int mDefaultH = 20;
    private static final String mOpenColor = "#01b9d3";
    private static final String mCloseColor = "#FFFFFF";
    private static final int mAnimSpeed = 5;

    private int mWidth;
    private int mHeight;

    private final Paint mPaint = new Paint();
    private final Path mPath = new Path();
    private final Path mPathIn = new Path();
    private RectF mOvalRect = null;

    private int mStart = 0;
    private int mEnd = 0;
    private int mNowLocation = 0;
    private boolean isClose = true;
    private boolean isAnim = false;
    private MyHandler mHandler;
    private onChangClickListener mListener;
    private String mButtonColor = mCloseColor;

    public interface onChangClickListener {
        void onChange(View view, boolean isClose);
    }

    public SwitchButton(Context context) {
        this(context, null);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHandler = new MyHandler(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

//        int mode = MeasureSpec.getMode(widthMeasureSpec);
//        int width = MeasureSpec.getSize(widthMeasureSpec);
//
//        if (mode == MeasureSpec.EXACTLY) {
//            mWidth = width;
//            mHeight = width * 2;
//        } else {
//            mWidth = dip2px(mDefaultW);
//            mHeight = dip2px(mDefaultH);
//        }

        mWidth = dip2px(mDefaultW);
        mHeight = dip2px(mDefaultH);

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        RectF rectF = new RectF(0, 0, mHeight, mHeight);
        mPath.arcTo(rectF, 90, 180);
        rectF.left = mWidth - mHeight;
        rectF.right = mWidth;
        mPath.arcTo(rectF, 270, 180);
        mPath.close();

        int strokeWidth = dip2px(1);
        RectF rectFIn = new RectF(strokeWidth, strokeWidth, mHeight - strokeWidth, mHeight - strokeWidth);
        mPathIn.arcTo(rectFIn, 90, 180);
        rectFIn.left = mWidth - mHeight - strokeWidth;
        rectFIn.right = mWidth - strokeWidth;
        mPathIn.arcTo(rectFIn, 270, 180);
        mPathIn.close();

        mStart = mHeight / 2;
        mEnd = mWidth - mStart;
        int radius = (mHeight - strokeWidth * 4) / 2;

        int padding = strokeWidth * 2;
        int left = padding + mNowLocation;
        int right = left + radius * 2;
        int bottom = (mHeight - padding) < 0 ? 0 : (mHeight - padding);
        mOvalRect = new RectF(left, padding, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#DEDEDE"));
        canvas.drawPath(mPath, mPaint);
        mPaint.setColor(Color.parseColor("#FFFFFF"));
        canvas.drawPath(mPathIn, mPaint);
        mPaint.setColor(Color.parseColor(mButtonColor));
        canvas.translate(mNowLocation, 0);
        canvas.drawOval(mOvalRect, mPaint);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_UP:
                switchStatus(true);
                break;
        }
        return super.onTouchEvent(event);
    }

    private void switchStatus(boolean listener) {
        if (isAnim) {
            return;
        }
        isAnim = true;
        mHandler.sendEmptyMessage(0);
        if (listener) {
            if (mListener != null) {
                mListener.onChange(this, !isClose);
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private class MyHandler extends Handler {
        private WeakReference<Context> mContext;

        MyHandler(Context context) {
            mContext = new WeakReference<>(context);
        }

        @SuppressWarnings("ConstantConditions")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mContext.get() == null) {
                return;
            }
            if (isClose) {
                mNowLocation = mNowLocation + mAnimSpeed + mStart <= mEnd ? mNowLocation + mAnimSpeed : mEnd - mStart;
                if (mNowLocation + mStart < mEnd) {
                    invalidate();
                } else if (mNowLocation + mStart == mEnd) {
                    mButtonColor = mOpenColor;
                    mNowLocation = mEnd - mStart;
                    invalidate();
                    isClose = false;
                    isAnim = false;
                }
            } else {
                mNowLocation = mNowLocation - mAnimSpeed >= 0 ? mNowLocation - mAnimSpeed : 0;
                if (mNowLocation > 0) {
                    invalidate();
                } else if (mNowLocation == 0) {
                    mButtonColor = mCloseColor;
                    mNowLocation = 0;
                    invalidate();
                    isClose = true;
                    isAnim = false;
                }
            }
            if (isAnim) {
                mHandler.sendEmptyMessageDelayed(0, 10);
            }
        }
    }

    public void setOnChangeListener(onChangClickListener listener) {
        mListener = listener;
    }

    public void setChange(boolean change) {
        if (change == isClose) {
            switchStatus(false);
        }
    }

    public boolean status() {
        return isClose;
    }

    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
