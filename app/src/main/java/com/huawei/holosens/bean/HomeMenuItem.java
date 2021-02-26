package com.huawei.holosens.bean;


import com.huawei.holosens.base.BaseActivity;

/**
 * @ProjectName: FanKong
 * @Package: com.jovision.fankong.bean
 * @ClassName: HomeMenuItem
 * @Description: java类作用描述
 * @CreateDate: 2019/10/10 2:53 PM
 * @Version: 1.0
 */
public class HomeMenuItem {
    private String title;
    private int resId;
    private int backgroundId;
    private Class<? extends BaseActivity> targetClass;

    public HomeMenuItem(String title, int resId, Class<? extends BaseActivity> targetClass){
        this.title = title;
        this.resId = resId;
        this.targetClass = targetClass;
    }

    public HomeMenuItem(String title, int resId, int backgroundId, Class<? extends BaseActivity> targetClass) {
        this.title = title;
        this.resId = resId;
        this.backgroundId = backgroundId;
        this.targetClass = targetClass;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public int getBackgroundId(

    ) {
        return backgroundId;
    }

    public void setBackgroundId(int backgroundId) {
        this.backgroundId = backgroundId;
    }

    public Class<? extends BaseActivity> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<? extends BaseActivity> targetClass) {
        this.targetClass = targetClass;
    }
}
