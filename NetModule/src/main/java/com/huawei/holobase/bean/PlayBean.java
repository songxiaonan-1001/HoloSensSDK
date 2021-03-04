package com.huawei.holobase.bean;

import com.huawei.holosens.bean.ProtocolType;

import java.util.List;

/**
 * @ProjectName: HoloSens
 * @Package: com.huawei.base.bean
 * @ClassName: PlayBean
 * @Description: 设备信息类
 * @CreateDate: 2020-01-11 11:51
 * @Version: 1.0
 */
public class PlayBean {

    /**
     * sdk使用
     * @param type 设备类型
     * @param deviceId 设备ID
     * @param channelID 通道ID
     * @param nickname 设备名称
     * @param protool 接入协议
     */
    public PlayBean(int type, String deviceId, String channelID, String nickname,String protool, int onlineStatus) {
        this.type = type;
        this.deviceId = deviceId;
        this.channelID = channelID;
        this.nickName = nickname;
        this.connect_type = protool.equals("HOLO")? ProtocolType.HOLO:ProtocolType.GB28181;
        this.onlineStatus = onlineStatus;
    }
    private String channelID;

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    private int type; // nvr ipc 通道
    private String deviceId;
    private int channelId;
    private String mts;
    private String token;
    private String nickName;
    private List<String> device_ability;
    private boolean isShared;//是否是被分享的设备

    /**
     * 设备连接协议：1：好望协议 2： GB28181
     */
    private int connect_type;

    public boolean isGB28181Device() {
        return connect_type == 2;
    }

    public void setConnect_type(int connect_type) {
        this.connect_type = connect_type;
    }

    public int getConnect_type() {
        return connect_type;
    }

    //国标ipc设备对应的通道ID
    private String ipc_device_channel_id;

    public String getIpc_device_channel_id() {
        return ipc_device_channel_id;
    }

    public void setIpc_device_channel_id(String ipc_device_channel_id) {
        this.ipc_device_channel_id = ipc_device_channel_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

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

    public String getMts() {
        return mts;
    }

    public void setMts(String mts) {
        this.mts = mts;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public List<String> getDevice_ability() {
        return device_ability;
    }

    public void setDevice_ability(List<String> device_ability) {
        this.device_ability = device_ability;
    }

    public boolean isShared() {
        return isShared;
    }

    public void setShared(boolean shared) {
        isShared = shared;
    }

    private int onlineStatus;        //设备在线状态

    public int getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(int onlineStatus) {
        this.onlineStatus = onlineStatus;
    }
}
