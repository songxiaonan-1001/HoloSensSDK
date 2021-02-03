package com.huawei.holosens.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @ProjectName: HoloSens
 * @Package: com.huawei.holosens.view
 * @ClassName: MarqueTextView
 * @Description: java类作用描述
 * @CreateDate: 2020-02-04 22:37
 * @Version: 1.0
 */
@SuppressLint("AppCompatCustomView")
public class MarqueTextView extends TextView {

    public MarqueTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MarqueTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueTextView(Context context) {
        super(context);
    }

    @Override

    public boolean isFocused() {
        return true;
    }
}