package com.huawei.holosens.bean;

/**
 * @ProjectName: HoloSens
 * @Package: com.huawei.holosens.utils
 * @ClassName: LocalDev
 * @Description: java类作用描述
 * @CreateDate: 2020-01-14 15:39
 * @Version: 1.0
 */
public class LocalDev {

    public LocalDev() {
    }

    public LocalDev(String deviceId, int channelId, String nickName) {
        this.deviceId = deviceId;
        this.channelId = channelId;
        this.nickName = nickName;
    }

    private String deviceId;
    private int channelId;
    private String nickName;
    private long date;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
