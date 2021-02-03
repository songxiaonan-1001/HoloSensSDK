package com.huawei.holosens.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huawei.holosens.R;

import androidx.annotation.ColorInt;


/**
 * 统一的topBar布局部分
 *
 */
public class TopBarLayout extends RelativeLayout {

    private final Context mContext;
    /**中间标题布局*/
    private LinearLayout mContentRlyt;
    /**下拉按钮*/
    private ImageView mPulldown;
    /**标题*/
    private TextView title;
    /**左侧图标一*/
    private ImageView mLeftImage;
    /**左侧图标二*/
    private ImageView mLeftExtendImage;
    /**右侧图标一*/
    private ImageView mRightImage;
    /**右侧图标二*/
    private ImageView mRightExtendImage;
    /**右侧文字二*/
    private TextView mRightExtendTv;

    /**右侧布局、左侧布局*/
    private View mLeftFlyt, mRightFlyt;
    private FrameLayout rightBtn;
    /**topbar整个布局*/
    private RelativeLayout topBarContentLayout;
    /**左侧文字、右侧文字*/
    private TextView mRightText, mLeftText;

    public TopBarLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initView();
    }

    public TopBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }



    @SuppressLint("CutPasteId")
    private void initView() {
        View.inflate(getContext(), R.layout.topbar_layout, this);
        mContentRlyt = findViewById(R.id.content_lyt);
        mPulldown = findViewById(R.id.iv_pulldown);
        title = findViewById(R.id.center_title);
        mLeftFlyt = findViewById(R.id.left_btn);
        mRightFlyt = findViewById(R.id.right_btn);
        mLeftImage = findViewById(R.id.iv_left);
        mLeftExtendImage = findViewById(R.id.left_btn_extend);
        mRightImage = findViewById(R.id.iv_right);
        mRightExtendImage = findViewById(R.id.right_btn_extend);
        mRightExtendTv = findViewById(R.id.tv_right_extend);
        topBarContentLayout = findViewById(R.id.topbar_layout);
        mRightText = findViewById(R.id.tv_right);
        mLeftText = findViewById(R.id.tv_left);
        rightBtn = findViewById(R.id.right_btn);

        // 设置标题，实现跑马灯效果
        title.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        title.setSingleLine(true);
        title.setSelected(true);
        title.setFocusable(true);
        title.setFocusableInTouchMode(true);
    }

    /**
     * 设置TopBarView标题
     *
     * @param text
     */
    public void setTitle(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            title.setVisibility(View.GONE);
            return;
        }
        title.setText(text);
        title.setVisibility(View.VISIBLE);
    }

    /**
     * 获取TopBarView标题
     *
     */
    public String getTitle() {
        return title.getText().toString();
    }

    /**
     * 设置TopBarView标题
     *
     * @param resid
     */
    public void setTitle(int resid) {
        if (resid < 0) {
            title.setVisibility(View.GONE);
            return;
        }
        title.setText(resid);
        title.setVisibility(View.VISIBLE);
    }

    /**
     * 设置TopBarView标题颜色
     *
     * @param color
     */
    public void setTitleColor(@ColorInt int color) {
        title.setTextColor(color);
    }

    /**
     * 设置TopBarView 左侧按钮的图标
     *
     * @param resId
     */
    public void setLeftButtonRes(int resId) {
        if (resId < 0) {
            mLeftFlyt.setVisibility(View.INVISIBLE);
            return;
        }
        mLeftFlyt.setVisibility(View.VISIBLE);
        mLeftImage.setImageResource(resId);
        mLeftImage.setVisibility(View.VISIBLE);
        mLeftText.setVisibility(View.GONE);
    }


    public ImageView getmLeftImage(){

        return mLeftImage;
    }

    /**
     * 设置左侧的文字
     *
     * @param resId
     */
    public void setLeftTextRes(int resId) {
        if (resId < 0) {
            mLeftFlyt.setVisibility(View.INVISIBLE);
            return;
        }
        mLeftFlyt.setVisibility(View.VISIBLE);
        mLeftText.setText(resId);
        mLeftText.setVisibility(View.VISIBLE);
        mLeftImage.setVisibility(View.GONE);
    }

    /**
     * 设置TopBarView 右边按钮的图标
     *
     * @param resId
     */
    public void setRightButtonRes(int resId) {
        if (resId <= 0) {
            mRightFlyt.setVisibility(View.INVISIBLE);
            rightBtn.setVisibility(GONE);
            return;
        }
        mRightFlyt.setVisibility(View.VISIBLE);
        mRightImage.setImageResource(resId);
        mRightImage.setVisibility(View.VISIBLE);
        mRightText.setVisibility(View.GONE);
    }

    /**
     * 设置右侧的文字
     *
     * @param resId
     */
    public void setRightTextRes(int resId) {
        if (resId < 0) {
            mRightFlyt.setVisibility(View.INVISIBLE);

            return;
        }
        mRightFlyt.setVisibility(View.VISIBLE);
        mRightText.setText(resId);
        mRightText.setVisibility(View.VISIBLE);
        mRightImage.setVisibility(View.GONE);
    }
    /**
     * 设置右侧的文字
     *
     * @param resId
     */
    public void setRightTextResAndColor(int resId, int color) {
        if (resId < 0) {
            mRightFlyt.setVisibility(View.INVISIBLE);

            return;
        }
        mRightFlyt.setVisibility(View.VISIBLE);
        mRightText.setText(resId);
        mRightText.setTextColor(color);
        mRightText.setVisibility(View.VISIBLE);
        mRightImage.setVisibility(View.GONE);
    }
    /**
     * 设置右侧的文字
     *
     * @param text 文字描述
     */
    public void setRightText(String text) {
        if (text == null) {
            mRightFlyt.setVisibility(View.INVISIBLE);
            return;
        }
        mRightFlyt.setVisibility(View.VISIBLE);
        mRightText.setText(text);
        mRightText.setVisibility(View.VISIBLE);
        mRightImage.setVisibility(View.GONE);
    }

    public void setRightListener(OnClickListener listener){
        mRightFlyt.setOnClickListener(listener);
    }

    public ImageView getRightImg() {
        return mRightImage;
    }

    public TextView getmRightText() {
        return mRightText;
    }

    public ImageView getmRightExtendImage() {
        return mRightExtendImage;
    }

    public void setmRightExtendImage(ImageView mRightExtendImage) {
        this.mRightExtendImage = mRightExtendImage;
    }

    public TextView getmRightExtendTv() {
        return mRightExtendTv;
    }

    public void setmRightExtendTv(TextView mRightExtendTv) {
        this.mRightExtendTv = mRightExtendTv;
    }

    /**
     * 设置右侧的文字的大小
     *
     * @param size
     */
    public void setRightTextSize(float size) {
        mRightText.setTextSize(size);
    }

    public void setRightTextColor(int color) {
        mRightText.setTextColor(mContext.getResources().getColor(color));
    }

    /**
     * 设置右侧的扩展图标
     *
     * @param resId
     */
    public void setLeftExtendButtonRes(int resId) {
        if (resId <= 0) {
            mLeftExtendImage.setVisibility(View.GONE);
            return;
        }
        mLeftExtendImage.setImageResource(resId);
        mLeftExtendImage.setVisibility(View.VISIBLE);
    }

    /**
     * 设置右侧的扩展图标
     *
     * @param resId
     */
    public void setRightExtendButtonRes(int resId) {
        if (resId <= 0) {
            return;
        }
        mRightExtendImage.setImageResource(resId);
        mRightExtendImage.setVisibility(View.VISIBLE);
    }

    /**
     * 设置右侧的扩展图标
     *
     * @param resId
     */
    public void setRightExtendTvRes(String resId) {
        mRightExtendTv.setText(resId);
        mRightExtendTv.setVisibility(View.VISIBLE);
    }


    /**
     * 显示下拉图标
     *
     * @param visibility 是否显示下拉图标
     */
    public void showPulldownIcon(int visibility) {
        mPulldown.setVisibility(visibility);
    }

    /**
     * 设置下拉图标
     *
     * @param resId
     */
    public void setPulldownIconRes(int resId) {
        if (resId <= 0) {
            mPulldown.setVisibility(View.GONE);
            return;
        }
        mPulldown.setImageResource(resId);
        mPulldown.setVisibility(View.VISIBLE);
    }


    /**
     * 设置纯图片的按钮TopBarView
     *
     * @param leftResid  左侧图标资源ID
     * @param rightResid 右侧图标的资源ID
     * @param titleResid 中间标题的资源ID
     * @param listener   监听
     */
    public void setTopBar(int leftResid, int rightResid,
                          int titleResid, OnClickListener listener) {
        // 左侧
        setLeftButtonRes(leftResid);
        mLeftFlyt.setOnClickListener(listener);
        // 左侧扩展按钮
        mLeftExtendImage.setOnClickListener(listener);
        // 右侧
        setRightButtonRes(rightResid);
        mRightFlyt.setOnClickListener(listener);
        // 右侧扩展按钮
        mRightExtendImage.setOnClickListener(listener);
        mRightExtendTv.setOnClickListener(listener);
        // 标题
        setTitle(titleResid);
        mContentRlyt.setOnClickListener(listener);

    }

    /**
     * 设置纯图片的按钮TopBarView
     *
     * @param leftResid  左侧图标资源ID
     * @param rightResid 右侧图标的资源ID
     * @param titleRes   中间标题的资源ID
     * @param listener   监听
     */
    public void setTopBar(int leftResid, int rightResid,
                          String titleRes, OnClickListener listener) {
        // 左侧
        setLeftButtonRes(leftResid);
        mLeftFlyt.setOnClickListener(listener);
        // 左侧扩展按钮
        mLeftExtendImage.setOnClickListener(listener);
        // 右侧
        setRightButtonRes(rightResid);
        mRightFlyt.setOnClickListener(listener);
        // 右侧扩展按钮
        mRightExtendImage.setOnClickListener(listener);
        // 标题
        setTitle(titleRes);
        mContentRlyt.setOnClickListener(listener);

    }



    public void setTopBarBackgroundResource(int resid) {
        topBarContentLayout.setBackgroundResource(resid);
    }

    public void setBackgoundColor(int resid) {
        topBarContentLayout.setBackgroundColor(resid);
    }
}
