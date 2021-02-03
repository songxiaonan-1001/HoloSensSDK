package com.huawei.holobase.bean;

/**
 * @ProjectName: HoloSens
 * @Package: com.huawei.base.bean
 * @ClassName: PtzStopBean
 * @Description: java类作用描述
 * @CreateDate: 2020-01-14 15:23
 * @Version: 1.0
 */
public class PtzGBStopBean {

    public PtzGBStopBean(String channel_id) {
        this.channel_id_gb = channel_id;
    }

    private String channel_id_gb;

    public String getChannel_id_gb() {
        return channel_id_gb;
    }

    public void setChannel_id_gb(String channel_id_gb) {
        this.channel_id_gb = channel_id_gb;
    }
}
