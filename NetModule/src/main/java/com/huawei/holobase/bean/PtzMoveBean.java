package com.huawei.holobase.bean;

/**
 * @ProjectName: HoloSens
 * @Package: com.huawei.base.bean
 * @ClassName: PtzMoveBean
 * @Description: java类作用描述
 * @CreateDate: 2020-01-14 14:25
 * @Version: 1.0
 */
public class PtzMoveBean {

    public PtzMoveBean(int channel_id, int pan_left, int tilt_up, int zoom_in) {
        this.channel_id = channel_id;
        this.pan_left = pan_left;
        this.tilt_up = tilt_up;
        this.zoom_in = zoom_in;
    }

    private int channel_id;
    private int pan_left;
    private int tilt_up;
    private int zoom_in;

    public int getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(int channel_id) {
        this.channel_id = channel_id;
    }

    public int getPan_left() {
        return pan_left;
    }

    public void setPan_left(int pan_left) {
        this.pan_left = pan_left;
    }

    public int getTilt_up() {
        return tilt_up;
    }

    public void setTilt_up(int tilt_up) {
        this.tilt_up = tilt_up;
    }

    public int getZoom_in() {
        return zoom_in;
    }

    public void setZoom_in(int zoom_in) {
        this.zoom_in = zoom_in;
    }
}
