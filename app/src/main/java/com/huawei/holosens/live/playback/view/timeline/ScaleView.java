package com.huawei.holosens.live.playback.view.timeline;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.TextureView;

import com.huawei.holosens.R;
import com.huawei.holosens.consts.JVEncodedConst;
import com.huawei.holosens.utils.ToastUtils;

import java.util.ArrayList;

public class ScaleView extends TextureView implements TextureView.SurfaceTextureListener, ScaleScroller.ScrollingListener {

    private static final String TAG = "ScaleViewExTextureView";

//    private static final int RECORD_0_ALL = 0;//0:所有
//    private static final int RECORD_1_TIME = 1;//1:定时
//    private static final int RECORD_2_MANUAL = 2;//2:手动
//    private static final int RECORD_3_MOTION = 3;//3:移动检测
//    private static final int RECORD_4_ALARM = 4;//4:报警
//    private static final int RECORD_5_INTELLIGENT = 5;//5:智能


    private int PAINT_BG = getResources().getColor(R.color.alarm_nofile);//控件背景色


    //    A：alarm报警录像；M：motion移动侦测；T:是定时N：normal手动录像 C:抽帧录像
    private final int PAINT_NO_FILE = getResources().getColor(R.color
            .alarm_nofile);//无文件颜色

    private final int PAINT_REC_NORMAL = getResources().getColor(R.color
            .alarm_kind_N);
    private final int PAINT_REC_TIME = getResources().getColor(R.color
            .alarm_kind_T);
    private final int PAINT_REC_MOTION = getResources().getColor(R.color
            .alarm_kind_M);
    private final int PAINT_REC_ALARM = getResources().getColor(R.color
            .alarm_kind_A);
    private final int PAINT_REC_ONE_MIN = getResources().getColor(R.color.alarm_kind_O);
    private final int PAINT_REC_CHFRAME = getResources().getColor(R.color
            .alarm_kind_C);
    private final int PAINT_REC_AREA_IN = getResources().getColor(R.color
            .alarm_kind_I);

    private int longLineHeight = 35;
    private int shortLineHeight = 20;

    //是否正在滚动
    private boolean isScrolling = false;

    //是否可以滚动
    private boolean canScroll = true;
    //不可滑动时，触摸提示
    private String disableScrollTip = getResources().getString(R.string.wait_add_channel);


    //是否正在缩放
    private boolean isZooming = false;

    //是否自动滚动
    private boolean isAutoScrolling = false;

    //是否手动调节滚动 2018.1.11
    private boolean isManuScroll = false;

    //file paint
    private Paint filePaint = new Paint();

    //缩放距离
    private float zoomDistance = 0;


    //滑动
    private ScaleScroller mScroller;

    //view矩形背景
    private RectF mBorderRectF = new RectF();

    //刻度分割块的数量 默认20
    private int allBlockNum = 20;

    //每小时几个block
    private final int blockEveryHour = 20;
    //view 的宽度
    private float mWidth;


    //刻度
    private Rect mTextRect = new Rect();
    //刻度数字字体大小
    private int mTextSize = sp2px(9);//文字字号
    //刻度字颜色
    private int mTextColor = getResources().getColor(R.color.popwindwow_text_content_color);
    //刻度文字paint
    private Paint textMarkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//此参数可以防止文字有锯齿，非常管用，2017.5.27


    //标尺线宽度
    private float mStrokeWidth = dip2px(0.5f);//线宽;
    //标尺线上下边界颜色
    private int mBorderColor = getResources().getColor(R.color.transparent);
    //标尺线上下边界paint
    private Paint mBorderPaint = new Paint();

    //标尺线刻度颜色
    private int mScaleVerticalColor = getResources().getColor(R.color.popwindwow_text_content_color);
    //标尺刻度线paint
    private Paint mScaleVerticalPaint = new Paint();


    //    //标尺中心线三角标
//    private Path topTri, bottomTri;
    //标尺中心线，三角标，颜色
    private int mCurrentMarkColor = getResources().getColor(R.color.text);
    //标尺中心刻度线paint
    private Paint mCurrentMarkPaint = new Paint();


    //中心点数字（单位s）
    private int mCenterSecond;
    //最大数字（一天的s数）
    private int maxSecond = 86400;
    //最小数字
    private int minSecond = 0;
    //每一个刻度间相差数
    private int secondEveryScale = 720;
    //每秒多少像素
    private float pixEverySecond = 0;

    //远程回放文件list
    private ArrayList<String[]> timeList = null;

    //没有文件的时间列表list
    public ArrayList<int[]> noFileSecondsList = new ArrayList<int[]>();

    //时间列表list
    public ArrayList<int[]> secondsList = new ArrayList<int[]>();
    //时间段文件的类型list
    public ArrayList<String> fileKindList = new ArrayList<String>();

    //刻度那行的高度
    private int scaleHeight = 0;

    private NumberListener numberListener;

    public ScaleView(Context context) {
        super(context);
        init();
    }

    public ScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mScroller = new ScaleScroller(getContext(), this);
        setSurfaceTextureListener(this);
        initPaints();

    }


    /**
     * 初始化画笔
     */
    private void initPaints() {

        //标尺线上下边缘
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStyle(Paint.Style.FILL);
        mBorderPaint.setStrokeWidth(mStrokeWidth);
        mBorderPaint.setTextSize(mTextSize);


        //标尺线竖刻度
        mScaleVerticalPaint.setColor(mScaleVerticalColor);
        mScaleVerticalPaint.setStyle(Paint.Style.FILL);
        mScaleVerticalPaint.setStrokeWidth(mStrokeWidth);
        mScaleVerticalPaint.setTextSize(mTextSize);

        //标尺刻度
        textMarkPaint.setColor(mTextColor);
        textMarkPaint.setStyle(Paint.Style.FILL);
        textMarkPaint.setStrokeWidth(mStrokeWidth);
        textMarkPaint.setTextSize(mTextSize);


        //文件区域的paint
        filePaint.setStyle(Paint.Style.FILL);
        filePaint.setStrokeWidth(mStrokeWidth);


        //当前位置刻度线
        mCurrentMarkPaint.setColor(mCurrentMarkColor);
        mCurrentMarkPaint.setStyle(Paint.Style.FILL);
        mCurrentMarkPaint.setStrokeWidth(mStrokeWidth * 2);


    }

    //刷新视图
    public void refreshCanvas() {
        if (mBorderRectF.isEmpty()) {
            return;
        }

        Canvas canvas = lockCanvas();
        if (canvas != null) {
            //横屏圆角
//            if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                float radius = ScreenUtils.dip2px(12);
//                float[] rids = {radius, radius, radius, radius, 0.0f, 0.0f, 0.0f, 0.0f,};
//                Path path = new Path();
//                int w = this.getWidth();
//                int h = this.getHeight();
//                /*向路径中添加圆角矩形。radii数组定义圆角矩形的四个圆角的x,y半径。radii长度必须为8*/
//                path.addRoundRect(new RectF(0, 0, w, h), rids, Path.Direction.CW);
//                canvas.clipPath(path);
//            }

            canvas.drawColor(PAINT_BG);
            drawBorder(canvas);
            drawScaleMark(canvas);
            drawMarkPoint(canvas);
        }
        unlockCanvasAndPost(canvas);
    }

    /**
     * 设置背景色
     *
     * @param color
     */
    public void setBackGroundColor(int color) {
        this.PAINT_BG = getResources().getColor(color);
    }

    /**
     * 根据中间刻度，获得左右的刻度（秒），根据左右的刻度划出标尺线，文件区域
     *
     * @param canvas
     */
    private void drawScaleMark(Canvas canvas) {

//        //画之前，清空画布，否则会有拖尾现象
//        canvas.drawColor(0, PorterDuff.Mode.CLEAR);


        if (mCenterSecond > maxSecond)
            mCenterSecond = maxSecond;
        if (mCenterSecond < minSecond)
            mCenterSecond = minSecond;

        if (numberListener != null) {
            numberListener.onChanged(String2TimeUtil.secToTime(mCenterSecond), isAutoScrolling);
        }

        Log.e(TAG, "mCenterSecond=" + mCenterSecond);

        int leftHeadSecond = mCenterSecond - allBlockNum * secondEveryScale / 2;//单位秒
        int rightEndSecond = mCenterSecond + allBlockNum * secondEveryScale / 2;//单位秒


        //只画可现实区域的标尺
        int leftRemainderSecond = leftHeadSecond % secondEveryScale;//左侧余数
        int leftMargin = secondEveryScale - leftRemainderSecond;//差数

        Log.e(TAG, "allBlockNum=" + allBlockNum);


        //        int blockEveryHour = 5;
//        if (allBlockNum <= 5) {
//            blockEveryHour = 40;
//        } else if (allBlockNum > 5 && allBlockNum <= 10) {
//            blockEveryHour = 40;
//        } else if (allBlockNum > 10 && allBlockNum <= 20) {
//            blockEveryHour = 20;
//        } else if (allBlockNum > 20 && allBlockNum <= 30) {
//            blockEveryHour = 5;
//        } else if (allBlockNum > 30 && allBlockNum <= 40) {
//            blockEveryHour = 5;
//        }


//        for (int i = 0; i < allBlockNum; i++) {
//
//            //第一个position
//            int linePositionSecond = leftHeadSecond + i * secondEveryScale + leftMargin;
//            String leftText = String2TimeUtil.secToTime(linePositionSecond).substring(0, 5);//;String.valueOf(leftNum);
//            float linePosition = (i * secondEveryScale + leftMargin) * pixEverySecond;
//
//            if (linePositionSecond >= minSecond && linePositionSecond <= maxSecond) {
//
//
//                //间隔5刻度画文字信息
//                if (linePositionSecond % (5 * secondEveryScale) == 0) {//画小时
//                    if (linePositionSecond % 3600 == 0) {//小时画长线
//                        //上长线
//                        canvas.drawLine(linePosition, 0, linePosition, longLineHeight, mScaleVerticalPaint);
//                        //下长线
//                        canvas.drawLine(linePosition, canvas.getHeight() - scaleHeight - longLineHeight, linePosition, canvas.getHeight() - scaleHeight, mScaleVerticalPaint);
//                    } else {//其他时画短线
//                        //上长线
//                        canvas.drawLine(linePosition, 0, linePosition, shortLineHeight, mScaleVerticalPaint);
//                        //下长线
//                        canvas.drawLine(linePosition, canvas.getHeight() - scaleHeight - shortLineHeight, linePosition, canvas.getHeight() - scaleHeight, mScaleVerticalPaint);
//                    }
//
//                    if (allBlockNum > 50) {
//                        //太大了，只画小时
//                        if (linePositionSecond % 3600 == 0) {
//                            mScaleVerticalPaint.getTextBounds(leftText, 0, leftText.length(), mTextRect);
//                            canvas.drawText(leftText, linePosition - mTextRect.centerX(), canvas.getHeight() - 20, textMarkPaint);
//                        }
//                    } else {
//                        mScaleVerticalPaint.getTextBounds(leftText, 0, leftText.length(), mTextRect);
//                        canvas.drawText(leftText, linePosition - mTextRect.centerX(), canvas.getHeight() - 20, textMarkPaint);
//                    }
//
//                } else {
//                    if (allBlockNum < 100) {
//                        //上短线
//                        canvas.drawLine(linePosition, 0, linePosition, shortLineHeight, mScaleVerticalPaint);
//                        //下短线
//                        canvas.drawLine(linePosition, canvas.getHeight() - scaleHeight - shortLineHeight, linePosition, canvas.getHeight() - scaleHeight, mScaleVerticalPaint);
//                    }
//
//                }
//            }
//        }


        //查询完无文件，画无文件标尺
        if (null == secondsList || secondsList.size() <= 0) {
            Log.v(TAG, "secondsList.size()=0");

            //无文件时画无文件区域
            int startSecond = minSecond;
            int endSecond = maxSecond;

            int startToLeft = startSecond - leftHeadSecond;
            int widthSecond = endSecond - startSecond;

            float left = pixEverySecond * startToLeft;
            float width = pixEverySecond * widthSecond;

            filePaint.setColor(PAINT_NO_FILE);
            canvas.drawRect(left, 0, left + width, canvas.getHeight() - scaleHeight - mStrokeWidth, filePaint);
//            return;
        }


        //画有文件日期的无文件区域
        for (int i = 0; i < noFileSecondsList.size(); i++) {
            int[] timeArray = noFileSecondsList.get(i);

            int startSecond = timeArray[0];
            int endSecond = timeArray[1];

            int startToLeft = startSecond - leftHeadSecond;
            int widthSecond = endSecond - startSecond;

            float left = pixEverySecond * startToLeft;
            float width = pixEverySecond * widthSecond;


            boolean needDraw = false;
            if (leftHeadSecond >= startSecond && leftHeadSecond <= endSecond) {
                needDraw = true;
            } else if (rightEndSecond >= startSecond && rightEndSecond <= endSecond) {
                needDraw = true;
            } else if (startSecond >= leftHeadSecond && endSecond <= rightEndSecond) {
                needDraw = true;
            }

            if (needDraw) {
                filePaint.setColor(PAINT_NO_FILE);
                if (width > 0) {
                    canvas.drawRect(left, 0, left + width, canvas.getHeight() - scaleHeight, filePaint);
                }
            }
        }


        //画有文件日期的有文件区域
        for (int i = 0; i < secondsList.size(); i++) {
            int[] timeArray = secondsList.get(i);

            int startSecond = timeArray[0];
            int endSecond = timeArray[1];

            int startToLeft = startSecond - leftHeadSecond;
            int widthSecond = endSecond - startSecond;

//            MyLog.v("break-find-file-canvas.drawRect", "endSecond=" + endSecond + ";startSecond=" + startSecond + ";widthSecond=" + widthSecond);

            float left = pixEverySecond * startToLeft;
            float width = pixEverySecond * widthSecond;


            boolean needDraw = false;
            if (leftHeadSecond >= startSecond && leftHeadSecond <= endSecond) {
                needDraw = true;
                //找到最左边所在文件
//                MyLog.v("break-find-file", "找到最左边所在文件,index=" + i + ";startSecond=" + String2TimeUtil.secToTime(startSecond) + ";endSecond=" + String2TimeUtil.secToTime(endSecond));
            } else if (rightEndSecond >= startSecond && rightEndSecond <= endSecond) {
                needDraw = true;
                //找到最右边所在文件
//                MyLog.v("break-find-file", "找到最右边所在文件,index=" + i + ";startSecond=" + String2TimeUtil.secToTime(startSecond) + ";endSecond=" + String2TimeUtil.secToTime(endSecond));
            } else if (startSecond >= leftHeadSecond && endSecond <= rightEndSecond) {
                needDraw = true;
                //应该划线的文件
//                MyLog.e("break-find-file", "应该划线文件,index=" + i + ";startSecond=" + String2TimeUtil.secToTime(startSecond) + ";endSecond=" + String2TimeUtil.secToTime(endSecond));
//
//                MyLog.e("break-find-file-drawLine", "startToLeft=" + startToLeft
//                        + ";\nwidthSecond=" + widthSecond
//                        + ";\npixEverySecond=" + pixEverySecond
//                        + ";\nleft=" + left
//                        + ";\nwidth=" + width);
            }


//            //除了手动录像是蓝色的，其他的都是红色的
//            filePaint.setColor(fileAlarmColor4);

            if (needDraw) {
                int fileKind = Integer.parseInt(fileKindList.get(i));

                switch (fileKind) {
                    case JVEncodedConst.INT_REC_NORMAL:
                        filePaint.setColor(PAINT_REC_NORMAL);
                        break;
                    case JVEncodedConst.INT_REC_TIME:
                        filePaint.setColor(PAINT_REC_TIME);
                        break;
                    case JVEncodedConst.INT_REC_MOTION:
                        filePaint.setColor(PAINT_REC_MOTION);
                        break;
                    case JVEncodedConst.INT_REC_ALARM:
                        filePaint.setColor(PAINT_REC_ALARM);
                        break;
                    case JVEncodedConst.INT_REC_ONE_MIN:
                        filePaint.setColor(PAINT_REC_ONE_MIN);
                        break;
                    case JVEncodedConst.INT_REC_CHFRAME:
                        filePaint.setColor(PAINT_REC_CHFRAME);
                        break;
                    case JVEncodedConst.INT_REC_SMART:
                        filePaint.setColor(PAINT_REC_AREA_IN);
                        break;
                    default:
                        filePaint.setColor(PAINT_NO_FILE);
                        break;
                }


//                MyLog.v("break-find-file-canvas.drawRect", "left=" + left + ";right=" + (left + width) + ";width=" + width);

                if (width > 0) {
                    canvas.drawRect(left, longLineHeight + 60, left + width, canvas.getHeight() - scaleHeight - 1, filePaint);
                }

            }

        }

        for (int i = 0; i < allBlockNum; i++) {

            //第一个position
            int linePositionSecond = leftHeadSecond + i * secondEveryScale + leftMargin;
            String leftText = String2TimeUtil.secToTime(linePositionSecond).substring(0, 5);//;String.valueOf(leftNum);
            float linePosition = (i * secondEveryScale + leftMargin) * pixEverySecond;

            if (linePositionSecond >= minSecond && linePositionSecond <= maxSecond) {


                //间隔5刻度画文字信息
                if (linePositionSecond % (5 * secondEveryScale) == 0) {//画小时
                    if (linePositionSecond % 3600 == 0) {//小时画长线
                        //上长线
                        canvas.drawLine(linePosition, 0, linePosition, longLineHeight, mScaleVerticalPaint);
//                        //下长线
//                        canvas.drawLine(linePosition, canvas.getHeight() - scaleHeight - longLineHeight, linePosition, canvas.getHeight() - scaleHeight, mScaleVerticalPaint);
                    } else {//其他时画短线
                        //上长线
                        canvas.drawLine(linePosition, 0, linePosition, shortLineHeight, mScaleVerticalPaint);
//                        //下长线
//                        canvas.drawLine(linePosition, canvas.getHeight() - scaleHeight - shortLineHeight, linePosition, canvas.getHeight() - scaleHeight, mScaleVerticalPaint);
                    }

                    if (allBlockNum > 50) {
                        //太大了，只画小时
                        if (linePositionSecond % 3600 == 0) {
                            mScaleVerticalPaint.getTextBounds(leftText, 0, leftText.length(), mTextRect);
                            canvas.drawText(leftText, linePosition - mTextRect.centerX(), longLineHeight + 26, textMarkPaint);
                        }
                    } else {
                        mScaleVerticalPaint.getTextBounds(leftText, 0, leftText.length(), mTextRect);
                        canvas.drawText(leftText, linePosition - mTextRect.centerX(), longLineHeight + 26, textMarkPaint);
                    }

                } else {
                    if (allBlockNum < 100) {
                        //上短线
                        canvas.drawLine(linePosition, 0, linePosition, shortLineHeight, mScaleVerticalPaint);
//                        //下短线
//                        canvas.drawLine(linePosition, canvas.getHeight() - scaleHeight - shortLineHeight, linePosition, canvas.getHeight() - scaleHeight, mScaleVerticalPaint);
                    }

                }
            }
        }

    }

    private void drawBorder(Canvas canvas) {
        canvas.drawLine(mBorderRectF.left, mBorderRectF.top - mStrokeWidth / 2, mBorderRectF.right, mBorderRectF.top - mStrokeWidth / 2, mBorderPaint);
        canvas.drawLine(mBorderRectF.left, mBorderRectF.bottom + mStrokeWidth / 2 - scaleHeight, mBorderRectF.right, mBorderRectF.bottom + mStrokeWidth / 2 - scaleHeight, mBorderPaint);
    }


    private void drawMarkPoint(Canvas canvas) {
        int centerX = (int) mBorderRectF.centerX();
        canvas.drawLine(centerX - mStrokeWidth, 0, centerX - mStrokeWidth, canvas.getHeight() - scaleHeight - 1, mCurrentMarkPaint);

//        topTri = new Path();
//        topTri.moveTo(centerX - mStrokeWidth, 20);
//        topTri.lineTo(centerX - mStrokeWidth - 12, 0);
//        topTri.lineTo(centerX - mStrokeWidth + 12, 0);
//
//        bottomTri = new Path();
//        bottomTri.moveTo(centerX - mStrokeWidth, canvas.getHeight() - mStrokeWidth - scaleHeight - 20);
//        bottomTri.lineTo(centerX - mStrokeWidth - 12, canvas.getHeight() - mStrokeWidth - scaleHeight);
//        bottomTri.lineTo(centerX - mStrokeWidth + 12, canvas.getHeight() - mStrokeWidth - scaleHeight);
//
//        canvas.drawPath(topTri, mCurrentMarkPaint);//上三角
//        canvas.drawPath(bottomTri, mCurrentMarkPaint);//上三角

    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mBorderRectF.set(mStrokeWidth, mStrokeWidth,
                width - mStrokeWidth, height - mStrokeWidth);
        mWidth = mBorderRectF.width();
        pixEverySecond = mWidth / (secondEveryScale * allBlockNum);
        refreshCanvas();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        mBorderRectF.set(mStrokeWidth, mStrokeWidth,
                width - mStrokeWidth, height - mStrokeWidth);
        mWidth = mBorderRectF.width();
        pixEverySecond = mWidth / (secondEveryScale * allBlockNum);
        refreshCanvas();
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }


    private long toastTime = 0;
    @Override
    public void onScroll(int distance) {

        if (!canScroll) {
            if (System.currentTimeMillis() - toastTime > 1000) {
                ToastUtils.show(getContext(), disableScrollTip);
                toastTime = System.currentTimeMillis();
            }
            return;
        }
        Log.e(TAG, "onScroll isZooming=" + isZooming);
        if (isZooming) {
            return;
        }
        isScrolling = true;
        int scrollSecond;
        if (Math.abs(distance) <= 1) {
            scrollSecond = (int) ((distance * (secondEveryScale * allBlockNum)) / mWidth) / 13;
        } else {
            scrollSecond = (int) ((distance * (secondEveryScale * allBlockNum)) / mWidth);
        }

        Log.e(TAG, "distance=" + distance + ";scrollSecond=" + scrollSecond);
        mCenterSecond -= scrollSecond;
        Log.e(TAG, "mCenterSecond=" + mCenterSecond);


        refreshCanvas();
    }


    /**
     * 滚动到指定时间
     *
     * @param scrollToTime
     */
    public void scrollToTime(String scrollToTime) {
        mCenterSecond = String2TimeUtil.getSecondsByTimeStr(scrollToTime);
        refreshCanvas();
    }

    @Override
    public void onStarted() {
    }

    @Override
    public void onFinished() {
        isScrolling = false;
    }

    @Override
    public void onJustify() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP) {
            isZooming = false;
        }
        isManuScroll = true;

        switch (event.getPointerCount()) {
            case 1:
//                MyLog.e(TAG, "1 touch return=" + mScroller.onTouchEvent(event));

                boolean result = mScroller.onTouchEvent(event);

//                isManuScroll = false;
                return result;

            case 2:
                isZooming = true;
                zoomSeeker(event);
//                MyLog.e(TAG, "2 touch isZooming=" + isZooming);
                isManuScroll = false;
                break;
            default:
                break;
        }

        return false;

    }

    //获取是否手动滑动
    public boolean isManuScroll() {
        return isManuScroll;
    }

    //设置手动滑动状态
    public void setManuScroll(boolean state) {
        isManuScroll = state;
    }


    private void zoomSeeker(MotionEvent event) {

        Log.e(TAG, "mCenterSecond=" + mCenterSecond);

        if (event.getAction() == MotionEvent.ACTION_POINTER_1_UP ||
                event.getAction() == MotionEvent.ACTION_POINTER_2_UP) {
            zoomDistance = 0;
            return;
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {


            Log.e(TAG, "P0=" + event.getX(0) + "；P1=" + event.getX(1));


            if (zoomDistance != 0) {
                zoom(Math.abs((event.getX(0) - event.getX(1))) - zoomDistance);
            }
            zoomDistance = Math.abs((event.getX(0) - event.getX(1)));
        }


    }

    /**
     * 双触点时，缩放时间轴.单格分别表示为5分钟、10分钟、半小时、1小时
     */
    public void zoom(float zoom) {

        Log.e(TAG, "zoom=" + zoom);
        Log.e(TAG, "mCenterSecond=" + mCenterSecond);
        if (zoom > 0) {
            allBlockNum = allBlockNum - 2 < 20 ? 20 : allBlockNum - 2;
        } else {
            allBlockNum = allBlockNum + 2 > 160 ? 160 : allBlockNum + 2;
        }

        pixEverySecond = mWidth / (secondEveryScale * allBlockNum);

        refreshCanvas();
        Log.e(TAG, "zoom end isZooming=" + isZooming);
    }


    public interface NumberListener {
        public void onChanged(String mCurrentTime, boolean isAutoScrolling);
    }


    public void setNumberListener(NumberListener listener) {
        this.numberListener = listener;
    }

    /**
     * 获取是否正在滚动状态
     *
     * @return
     */
    public boolean isScrolling() {
        return isScrolling;
    }


    /**
     * 获取是否自动滚动状态
     *
     * @return
     */
    public boolean isAutoScrolling() {
        return isAutoScrolling;
    }

    /**
     * 设置自动滚动状态
     *
     * @param isAutoScrolling
     */
    public void setAutoScrolling(boolean isAutoScrolling) {
        this.isAutoScrolling = isAutoScrolling;
    }

    /**
     * 设置是否可以滚动
     *
     * @param canScroll
     */
    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }

    public void setDisableScrollTip(String disableScrollTip) {
        this.disableScrollTip = disableScrollTip;
    }

    public void setTextSize(int textSize) {
        this.mTextSize = textSize;
    }

    public void setMaxSecond(int maxSecond) {
        this.maxSecond = maxSecond;
    }

    public void setMinSecond(int minSecond) {
        this.minSecond = minSecond;
    }

    public void setAllBlockNum(int allBlockNum) {
        this.allBlockNum = allBlockNum;
        this.secondEveryScale = 3600 / blockEveryHour;

    }


    public void setCenterSecond(int centerSecond) {
        this.mCenterSecond = centerSecond;
    }

    /**
     * 给时间轴设置数据
     *
     * @param timeList
     */
    public void setTimeList(ArrayList<String[]> timeList) {
        this.timeList = timeList;


        noFileSecondsList.clear();
        secondsList.clear();
        fileKindList.clear();

        if (null == timeList || 0 == timeList.size()) {

        } else {

            int noFileStart = 0;
            for (int i = 0; i < timeList.size(); i++) {
                String[] arrayStr = timeList.get(i);

                int startSeconds = String2TimeUtil.getSecondsByTimeStr(arrayStr[0]);
                int endSeconds = String2TimeUtil.getSecondsByTimeStr(arrayStr[1]);


                if (endSeconds < startSeconds) {//最后一个文件跨天了
                    endSeconds = maxSecond;
                }

//                MyLog.e(TAG, "setTimeList--" + "startTime=" + arrayStr[0] + ";endTime=" + arrayStr[1] + "--------startSeconds=" + startSeconds + ";endSeconds=" + endSeconds);

                int[] arrayFileInt = {startSeconds, endSeconds};
                secondsList.add(arrayFileInt);
                fileKindList.add(arrayStr[2]);

                if (noFileStart < startSeconds) {
                    int[] arrayNoFileInt = {noFileStart, startSeconds};

//                    MyLog.e(TAG, "setTimeList--" + "noFileStart=" + noFileStart + ";startSeconds=" + startSeconds);
                    noFileSecondsList.add(arrayNoFileInt);
                }

                //最后一个文件
                if (i == (timeList.size() - 1)) {
                    if (endSeconds < maxSecond) {
                        int[] arrayNoFileInt1 = {endSeconds, maxSecond};
//                        MyLog.e(TAG, "setNoFileTimeListEnd--" + "endSeconds=" + endSeconds + ";maxSecond=" + maxSecond);
                        noFileSecondsList.add(arrayNoFileInt1);
                    }
                }

                noFileStart = endSeconds;
            }
        }
        refreshCanvas();
    }


    /**
     * 根据手机的分辨率从dp的单位转成px（像素），0.5是为了转换时不丢失精度
     *
     * @param dpValue
     * @return
     */
    public float dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        float pxValue = (dpValue * scale + 0.5f);
        Log.e("dip2px---", "dpValue=" + dpValue + ";pxValue=" + pxValue);
        return pxValue;
    }

    /**
     * 根据手机的分辨率从px（像素）的单位转成dp
     *
     * @param pxValue
     * @return
     */
    public float px2dip(float pxValue) {
        final float scale = getResources().getDisplayMetrics().density;
        float dpValue = (int) (pxValue / scale + 0.5f);
        Log.e("px2dip---", "pxValue=" + pxValue + ";dpValue=" + dpValue + ";scale=" + scale);

        return dpValue;
    }


    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    public int px2sp(float pxValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public int sp2px(float spValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}
