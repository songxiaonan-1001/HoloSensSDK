package com.huawei.holobase.bean;

/**
 * @ProjectName: HoloSens
 * @Package: com.huawei.base.bean
 * @ClassName: PtzStopBean
 * @Description: java类作用描述
 * @CreateDate: 2020-01-14 15:23
 * @Version: 1.0
 */
public class PtzStopBean {

    public PtzStopBean(int channel_id) {
        this.channel_id = channel_id;
    }

    private int channel_id;

    public int getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(int channel_id) {
        this.channel_id = channel_id;
    }
}
