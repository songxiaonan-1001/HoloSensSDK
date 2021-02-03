package com.huawei.holobase.bean;

/**
 * @ProjectName: HoloSens
 * @Package: com.huawei.holobase.bean
 * @ClassName: DelDevChannels
 * @Description: java类作用描述
 * @CreateDate: 2020-03-26 11:17
 * @Version: 1.0
 */
public class DelDevChannels {

    public DelDevChannels(String device_id, int channel_id) {
        this.device_id = device_id;
        this.channel_id = channel_id;
    }

    private String device_id;
    private int channel_id;

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
}
