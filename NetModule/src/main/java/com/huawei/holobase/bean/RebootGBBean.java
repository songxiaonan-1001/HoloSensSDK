package com.huawei.holobase.bean;

/**
 * @ProjectName: HoloSens
 * @Package: com.huawei.base.bean
 * @ClassName: RebootBean
 * @Description: java类作用描述
 * @CreateDate: 2020-01-16 13:43
 * @Version: 1.0
 */
public class RebootGBBean {

    public RebootGBBean(int delay_time, String channel_id) {
        this.delay_time = delay_time;
        this.channel_id_gb = channel_id;
    }

    private int delay_time;

    public int getDelay_time() {
        return delay_time;
    }

    public void setDelay_time(int delay_time) {
        this.delay_time = delay_time;
    }

    private String channel_id_gb;

    public String getChannel_id_gb() {
        return channel_id_gb;
    }

    public void setChannel_id_gb(String channel_id_gb) {
        this.channel_id_gb = channel_id_gb;
    }
}
