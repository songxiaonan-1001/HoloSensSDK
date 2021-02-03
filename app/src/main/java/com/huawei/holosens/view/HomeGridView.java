package com.huawei.holosens.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @ProjectName: HoloSens
 * @Package: com.huawei.holosens.view
 * @ClassName: HomeGridView
 * @Description: java类作用描述
 * @CreateDate: 2020-01-03 14:21
 * @Version: 1.0
 */
public class HomeGridView extends GridView {

    public HomeGridView(Context context) {
        super(context);
    }

    public HomeGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /*重点在这里重写onMeasure()*/
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


}
