package com.huawei.holosens.live.play.glass;

import android.view.View;

import com.huawei.holosens.live.play.bean.Glass;

import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseGlass extends RecyclerView.ViewHolder {

    public BaseGlass(View itemView) {
        super(itemView);
    }

    // 当前玻璃所在的窗户是否可以被用户看到
    protected volatile boolean isVisibleToUser;

    public void setVisibleToUser(boolean visibleToUser) {
        isVisibleToUser = visibleToUser;
    }

    /**
     * 改变玻璃尺寸
     *
     * @param glassSize
     */
    public abstract void changeGlassSize(Glass.Size glassSize);

}
