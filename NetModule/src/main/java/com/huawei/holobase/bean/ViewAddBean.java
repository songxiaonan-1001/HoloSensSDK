package com.huawei.holobase.bean;

/**
 * @ProjectName: HoloSens
 * @Package: com.huawei.base.bean
 * @ClassName: ViewAddBean
 * @Description: java类作用描述
 * @CreateDate: 2020-01-15 20:33
 * @Version: 1.0
 */
public class ViewAddBean {
    public ViewAddBean(String device_id, int channel_id, int position) {
        this.device_id = device_id;
        this.channel_id = channel_id;
        this.position = position;
    }

    private String device_id;
    private int channel_id;
    private int position;

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public int getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(int channel_id) {
        this.channel_id = channel_id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
