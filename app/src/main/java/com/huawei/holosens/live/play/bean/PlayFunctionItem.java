package com.huawei.holosens.live.play.bean;

public class PlayFunctionItem {

    //图片资源
    private int resId;
    //选中状态，用于改变功能按钮的状态
    private boolean isSelected;
    //禁用状态
    private boolean isDisabled;

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }
}
