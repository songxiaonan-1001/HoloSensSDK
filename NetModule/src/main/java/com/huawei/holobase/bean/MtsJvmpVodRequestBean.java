package com.huawei.holobase.bean;

public class MtsJvmpVodRequestBean {

    public MtsJvmpVodRequestBean(String device_id, int channel_id, String start_time,String end_time) {
        this.device_id = device_id;
        this.channel_id = channel_id;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    private String device_id;
    private int channel_id;
    private int stream_type;
    private String start_time;
    private String end_time;

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

    public int getStream_type() {
        return stream_type;
    }

    public void setStream_type(int stream_type) {
        this.stream_type = stream_type;
    }
}
