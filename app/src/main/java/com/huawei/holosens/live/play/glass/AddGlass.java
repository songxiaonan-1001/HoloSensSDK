package com.huawei.holosens.live.play.glass;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.huawei.holosens.R;
import com.huawei.holosens.live.play.bean.Glass;
import com.huawei.holosens.live.play.ui.JVMultiPlayActivity;
import com.huawei.holosens.live.play.ui.WindowFragment;

/**
 * "+"号玻璃
 *
 */

public class AddGlass extends BaseGlass {

    protected JVMultiPlayActivity mActivity;
    private ViewGroup mPaperContainer;
    private ImageView mIvAdd;

    /**
     * @param glassView
     * @param glassSize
     */
    public AddGlass(WindowFragment window, View glassView, Glass.Size glassSize) {
        super(glassView);
        mActivity = window.getPlayActivity();
        mPaperContainer = glassView.findViewById(R.id.layout_glass_add_main);
        mIvAdd = glassView.findViewById(R.id.iv_add);
        // 设置白纸的尺寸
        mPaperContainer.getLayoutParams().width = glassSize.width;
        mPaperContainer.getLayoutParams().height = glassSize.height;
        mPaperContainer.setOnClickListener(mActivity);
        mIvAdd.setOnClickListener(mActivity);
        if (mActivity.isLandScape()) {
            mIvAdd.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
            mIvAdd.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
        } else {
            mIvAdd.getLayoutParams().width = glassSize.width / 5;
            mIvAdd.getLayoutParams().height = glassSize.width / 5;
        }
        if (mActivity.getSpanCount() == 1) {
            mPaperContainer.setBackgroundColor(mActivity.getResources().getColor(R.color.bg_play_add));
        } else {
            mPaperContainer.setBackgroundResource(R.drawable.border_glass_normal);
        }
    }

    @Override
    public void changeGlassSize(Glass.Size glassSize) {
        mPaperContainer.getLayoutParams().width = glassSize.width;
        mPaperContainer.getLayoutParams().height = glassSize.height;
        if (mActivity.isLandScape()) {
            mIvAdd.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
            mIvAdd.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
        } else {
            mIvAdd.getLayoutParams().width = glassSize.width / 5;
            mIvAdd.getLayoutParams().height = glassSize.width / 5;
        }
    }

}