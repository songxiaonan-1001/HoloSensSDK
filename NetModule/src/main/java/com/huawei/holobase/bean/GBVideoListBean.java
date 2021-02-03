package com.huawei.holobase.bean;

/**
 * 创建时间：2020-07-28
 * 创建人：dongdz
 * 功能描述：
 */
public class GBVideoListBean {

    private String channel_id;
    private String stream_id;
    private String start_time;
    private String end_time;

    public GBVideoListBean(String channel_id, String stream_id, String start_time, String end_time) {
        this.channel_id = channel_id;
        this.stream_id = stream_id;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getStream_id() {
        return stream_id;
    }

    public void setStream_id(String stream_id) {
        this.stream_id = stream_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}
