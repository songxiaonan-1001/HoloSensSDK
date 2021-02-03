package com.huawei.holobase.bean;

import java.util.List;

public class SDKRecordListBean {

    /**
     * {
     *   "total" : 1,
     *   "device_id" : "D2102412469WLL3000011",
     *   "channel_id" : "CLL3000011",
     *   "records" : [ {
     *     "record_id" : "1000000",
     *     "start_time" : "2020-06-12 17:31:00",
     *     "end_time" : "2020-06-13 12:50:00",
     *     "record_name" : "录像1",
     *     "record_size" : 1024,
     *     "record_type" : "NORMAL_RECORD"
     *   } ]
     * }
     */

    private int total;
    private String device_id;
    private String channel_id;
    private List<SDKRecordBean> records;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
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

    public List<SDKRecordBean> getRecords() {
        return records;
    }

    public void setRecords(List<SDKRecordBean> records) {
        this.records = records;
    }
}
