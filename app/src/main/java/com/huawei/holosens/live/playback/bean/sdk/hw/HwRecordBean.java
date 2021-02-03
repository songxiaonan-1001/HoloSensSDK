package com.huawei.holosens.live.playback.bean.sdk.hw;

/**
 * @ProjectName: HoloSensEnterprise
 * @Package: com.huawei.holosens.live.playback.bean.sdk.hw
 * @ClassName: HwRecordBean
 * @Description: java类作用描述
 * @CreateDate: 2020-08-11 14:03
 * @Version: 1.0
 */
public class HwRecordBean {

    /**
     * {
     *                 "begin_time":"2020-08-11 00:00:00",
     *                 "end_time":"2020-08-11 00:46:20",
     *                 "file_size":146309120,
     *                 "file_name":"_1_7_.rf",
     *                 "type":"normal"
     *             }
     */
    private String begin_time;
    private String end_time;
    private long   file_size;
    private String file_name;
    private String type;

    public String getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(String begin_time) {
        this.begin_time = begin_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public long getFile_size() {
        return file_size;
    }

    public void setFile_size(long file_size) {
        this.file_size = file_size;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
