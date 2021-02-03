package com.huawei.holosens.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huawei.holosens.R;

import androidx.annotation.StringRes;


/**
 * @class name OptionItemView
 * @date 创建时间：2019-05-31    09:21
 * @description: 简单封装的item layout，一般用于设置选项
 * 左边图标
 * 中间   顶部标题，右侧红点
 *       底部副标题，隐藏时item高56dp，显示时item高68dp
 * 右边   内容
 *       图标 隐藏(默认)、右箭头、单选框、开关
 * 底部分割线 默认高度0dp隐藏，可以设置
 */
public class OptionItemView extends RelativeLayout {


    public static final int GONE = 0;
    public static final int ARROW = 1;
    public static final int CHECK = 2;
    public static final int ON_OFF = 3;

    private ImageView mIvLeft, mIvRight;
    private TextView mTvTitle, mTvSubtitle, mTvContent;
    private ImageView mIvTitlePoint;
    private TextView mTvNewVersion;
    private View mIvBottomLine;

    private int rightImageStyle = GONE;
    private boolean isChecked; // 选中状态，开关状态

    private int titleStyle = 0;//标题默认不加粗

    public OptionItemView(Context context) {
        super(context);
        initView(context, null);
    }

    public OptionItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public OptionItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }


    private void initView(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_option_item, this, true);

        Drawable background = getBackground();
        if (background != null) {
            if (background instanceof ColorDrawable) {
                view.findViewById(R.id.rl_item_root).setBackgroundColor(((ColorDrawable) background).getColor());
            } else {
                view.findViewById(R.id.rl_item_root).setBackgroundDrawable(background);
            }
        }

        mIvLeft = view.findViewById(R.id.image_left);
        mIvRight = view.findViewById(R.id.image_right);
        mIvBottomLine = view.findViewById(R.id.bottom_line);
        mTvTitle = view.findViewById(R.id.tv_title);
        mTvSubtitle = view.findViewById(R.id.tv_subtitle);
        mTvContent = view.findViewById(R.id.tv_content);
        mIvTitlePoint = view.findViewById(R.id.iv_title_point);
        mTvNewVersion = view.findViewById(R.id.tv_new_version);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.OptionItemView);
        int resourceId = typedArray.getResourceId(R.styleable.OptionItemView_leftImage, -1);
        if (resourceId == -1) {
            mIvLeft.setVisibility(View.GONE);
        } else {
            mIvLeft.setVisibility(View.VISIBLE);
            mIvLeft.setImageResource(resourceId);
        }
        mTvTitle.setText(typedArray.getText(R.styleable.OptionItemView_titleText));
        mTvTitle.setTextColor(typedArray.getColor(R.styleable.OptionItemView_titleTextColor, getResources().getColor(R.color.text)));

        setSubtitle(typedArray.getText(R.styleable.OptionItemView_subtitleText));
        mTvContent.setText(typedArray.getText(R.styleable.OptionItemView_content));
        if (typedArray.getBoolean(R.styleable.OptionItemView_showTitlePoint, false)) {
            mIvTitlePoint.setVisibility(VISIBLE);
        } else {
            mIvTitlePoint.setVisibility(View.GONE);
        }
        isChecked = typedArray.getBoolean(R.styleable.OptionItemView_checked, false);
        rightImageStyle = typedArray.getInt(R.styleable.OptionItemView_rightImageStyle, GONE);
        titleStyle = typedArray.getInt(R.styleable.OptionItemView_titleStyle, 0);
        setRightImage();
        setTitleStyle();
        setBottomLineHeight(typedArray.getDimension(R.styleable.OptionItemView_bottomLineHeight, 0f));
        typedArray.recycle();
    }

    private void setTitleStyle() {
        switch (titleStyle) {
            case 0:
                mTvTitle.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;
            case 1:
                mTvTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                break;
        }
    }

    private void setRightImage() {
        switch (rightImageStyle) {
            case GONE:
                mIvRight.setVisibility(View.GONE);
                break;
            case ARROW:
                mIvRight.setVisibility(View.VISIBLE);
                mIvRight.setImageResource(R.drawable.selector_right_arrow);
                break;
            case CHECK:
                mIvRight.setVisibility(View.VISIBLE);
                if (isChecked) {
                    mIvRight.setImageResource(R.mipmap.ic_checkbox_checked);
                } else {
                    mIvRight.setImageResource(R.mipmap.ic_checkbox_normal);
                }
                break;
            case ON_OFF:
                mIvRight.setVisibility(View.VISIBLE);
                if (isChecked) {
                    mIvRight.setImageResource(R.mipmap.ic_switch_on);
                } else {
                    mIvRight.setImageResource(R.mipmap.ic_switch_off);
                }
                break;
        }
    }


    /**
     * 设置标题
     * */
    public void setTitle(@StringRes int resid) {
        mTvTitle.setText(getContext().getResources().getText(resid));
        setTitleStyle();
    }

    /**
     * 设置标题
     * */
    public void setTitle(CharSequence title) {
        mTvTitle.setText(title);
        setTitleStyle();
    }

    /**
     * 获取标题
     * */
    public String getTitle() {
        return mTvTitle.getText().toString();
    }

    /**
     * 显隐标题右侧红点
     * */
    public void showTitlePoint(boolean isShow) {
        if (isShow) {
            mIvTitlePoint.setVisibility(VISIBLE);
        } else {
            mIvTitlePoint.setVisibility(View.GONE);
        }
    }

    /**
     * 设置副标题
     * */
    public void setSubtitle(@StringRes int resid) {
        mTvSubtitle.setText(getContext().getResources().getText(resid));
        if (TextUtils.isEmpty(getSubtitle())) {
            mTvSubtitle.setVisibility(View.GONE);
        } else {
            mTvSubtitle.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置副标题
     * */
    public void setSubtitle(CharSequence title) {
        mTvSubtitle.setText(title);
        if (TextUtils.isEmpty(getSubtitle())) {
            mTvSubtitle.setVisibility(View.GONE);
        } else {
            mTvSubtitle.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取副标题
     * */
    public String getSubtitle() {
        return mTvSubtitle.getText().toString();
    }

    /**
     * 设置右边内容
     * */
    public void setContent(@StringRes int resid) {
        mTvContent.setText(getContext().getResources().getText(resid));
    }

    /**
     * 设置右边内容
     * */
    public void setContent(CharSequence content) {
        mTvContent.setText(content);
    }

    /**
     * 获取右边内容
     * */
    public String getContent() {
        return mTvContent.getText().toString();
    }

    /**
     * 设置右边图片
     * */
    public void setRightImageStyle(int rightImageStyle) {
        this.rightImageStyle = rightImageStyle;
        setRightImage();
    }

    /**
     * 返回右边图片
     * */
    public ImageView getRightImage() {
        return mIvRight;
    }

    /**
     * 设置选中状态
     * 单选框选中或者开关选中
     * */
    public void setChecked(boolean checked) {
        isChecked = checked;
        setRightImage();
    }

    /**
     * 选中状态
     * */
    public boolean isChecked() {
        return isChecked;
    }

    /**
     * 设置底部分割线的高度，<=0是隐藏
     * */
    public void setBottomLineHeight(float dpHeight) {
        if (dpHeight <= 0) {
            mIvBottomLine.setVisibility(View.GONE);
            return;
        }
        if (mIvBottomLine.getVisibility() != VISIBLE) {
            mIvBottomLine.setVisibility(VISIBLE);
        }
        LayoutParams layoutParams = (LayoutParams) mIvBottomLine.getLayoutParams();
        layoutParams.height = (int) dpHeight;
    }

    /**
     * 设置新版本提示显示
     */
    public void displayNewVersion(boolean visible) {
        if (visible) {
            mTvNewVersion.setVisibility(VISIBLE);
        } else {
            mTvNewVersion.setVisibility(View.GONE);
        }
    }

}
