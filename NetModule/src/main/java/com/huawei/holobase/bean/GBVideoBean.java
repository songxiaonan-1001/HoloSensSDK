package com.huawei.holobase.bean;

/**
 * 创建时间：2020-07-28
 * 创建人：dongdz
 * 功能描述：
 */
public class GBVideoBean {

    private String channel_id_gb;
    private String stream_id;
    private String start_time;
    private String end_time;
    private int page;
    private int page_size;

    public GBVideoBean(String channel_id, String stream_id, String start_time, String end_time, int page, int page_size) {
        this.channel_id_gb = channel_id;
        this.stream_id = stream_id;
        this.start_time = start_time;
        this.end_time = end_time;
        this.page = page;
        this.page_size = page_size;
    }

    public String getChannel_id_gb() {
        return channel_id_gb;
    }

    public void setChannel_id_gb(String channel_id_gb) {
        this.channel_id_gb = channel_id_gb;
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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }
}
