package com.huawei.holobase.bean;

/**
 * @ProjectName: HoloSens
 * @Package: com.huawei.base.bean
 * @ClassName: PtzMoveBean
 * @Description: java类作用描述
 * @CreateDate: 2020-01-14 14:25
 * @Version: 1.0
 */
public class PtzGBFocusBean {

    public PtzGBFocusBean(String channel_id, int focus_far, int iris_open) {
        this.channel_id_gb = channel_id;
        this.focus_far = focus_far;
        this.iris_open = iris_open;
    }

    private String channel_id_gb;
    private int focus_far;
    private int iris_open;

    public String getChannel_id_gb() {
        return channel_id_gb;
    }

    public void setChannel_id_gb(String channel_id_gb) {
        this.channel_id_gb = channel_id_gb;
    }

    public int getFocus_far() {
        return focus_far;
    }

    public void setFocus_far(int focus_far) {
        this.focus_far = focus_far;
    }

    public int getIris_open() {
        return iris_open;
    }

    public void setIris_open(int iris_open) {
        this.iris_open = iris_open;
    }
}
