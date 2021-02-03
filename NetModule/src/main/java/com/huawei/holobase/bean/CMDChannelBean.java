package com.huawei.holobase.bean;

/**
 * @ProjectName: HoloSens
 * @Package: com.huawei.base.bean
 * @ClassName: CMDChannelBean
 * @Description: java类作用描述
 * @CreateDate: 2020-01-16 14:20
 * @Version: 1.0
 */
public class CMDChannelBean {

    public CMDChannelBean(String channel_id) {
        this.channel_id = channel_id;
    }

    private String channel_id;

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }
}
