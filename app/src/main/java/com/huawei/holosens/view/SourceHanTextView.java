package com.huawei.holosens.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.huawei.holosens.R;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * @class name SourceHanTextView
 * @date 创建时间：2020-02-25    19:48
 * @description: 思源黑体
 */
public class SourceHanTextView extends AppCompatTextView {


    public static final int TYPEFACE_REGULAR = 0;
    public static final int TYPEFACE_MEDIUM = 1;
    public static final int TYPEFACE_BOLD = 2;


    private int mTypeface = TYPEFACE_REGULAR;

    public SourceHanTextView(Context context) {
        this(context, null);
    }

    public SourceHanTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SourceHanTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SourceHanTextView);
        mTypeface = typedArray.getInt(R.styleable.SourceHanTextView_typefaceSh, TYPEFACE_REGULAR);
        setTypeface(mTypeface);
    }

    public void setTypeface(int typefaceSh) {
        mTypeface = typefaceSh;
        try{
            /*
             *必须事先在assets底下创建一fonts文件夹，并放入要使用的字体文件(.ttf/.otf)
             *并提供相对路径给createFromAsset()来创建Typeface对象
             */
            Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/SourceHanSansCN-Regular.otf");
            if (mTypeface == 1) {
                typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/SourceHanSansCN-Medium.otf");
            } else if (mTypeface == 2) {
                typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/SourceHanSansCN-Bold.otf");
            }
            // 当使用外部字体却又发现字体没有变化的时候(以Droid Sans代替)，通常是因为这个字体android没有支持,而非你的程序发生了错误
            if(typeface != null)
                setTypeface(typeface);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
