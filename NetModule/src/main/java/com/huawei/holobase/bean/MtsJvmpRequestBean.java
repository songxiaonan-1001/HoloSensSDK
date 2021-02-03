package com.huawei.holobase.bean;

/**
 * @ProjectName: HoloSens
 * @Package: com.huawei.base.bean
 * @ClassName: MtsJvmpRequestBean
 * @Description: java类作用描述 请求mts jvmp 的request bean
 * @CreateDate: 2020-01-30 22:25
 * @Version: 1.0
 */
public class MtsJvmpRequestBean {

    public MtsJvmpRequestBean(String device_id, String channel_id, int stream_type) {
        this.device_id = device_id;
        this.channel_id = channel_id;
        this.stream_type = stream_type;
    }

    private String device_id;
    private String channel_id;
    private int stream_type;
    private String protocol;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public int getStream_type() {
        return stream_type;
    }

    public void setStream_type(int stream_type) {
        this.stream_type = stream_type;
    }
}
