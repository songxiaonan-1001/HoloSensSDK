package com.huawei.holobase.bean;

/**
 * @ProjectName: HoloSensEnterprise
 * @Package: com.huawei.holobase.bean
 * @ClassName: SDKRecordBean
 * @Description: java类作用描述
 * @CreateDate: 2020-08-14 14:47
 * @Version: 1.0
 */
public class SDKRecordBean {

    /**
     * {
     *             "record_id":"9619",
     *             "record_name":"20200809235459.jav",
     *             "record_type":"NORMAL_RECORD",
     *             "record_size":220625117,
     *             "start_time":"2020-08-09 23:54:59",
     *             "end_time":"2020-08-10 00:25:00"
     *         }
     */

    /**
     * 录像类型 全量录像：ALL_RECORD 动检录像：MOTION_RECORD
     *     枚举值：
     *     PLAN_RECORD，MOTION_RECORD
     */


    private String start_time;
    private String end_time;
    private String record_id;
    private String record_name;
    private String record_size;
    private String record_type;

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

    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }

    public String getRecord_name() {
        return record_name;
    }

    public void setRecord_name(String record_name) {
        this.record_name = record_name;
    }

    public String getRecord_size() {
        return record_size;
    }

    public void setRecord_size(String record_size) {
        this.record_size = record_size;
    }

    public String getRecord_type() {
        return record_type;
    }

    public void setRecord_type(String record_type) {
        this.record_type = record_type;
    }

}
