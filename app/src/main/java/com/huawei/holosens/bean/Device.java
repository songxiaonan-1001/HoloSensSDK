package com.huawei.holosens.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 简单的设备集合类
 *
 * @author
 */
public class Device implements Serializable, Cloneable{

    private String sn;
    private String user;
    private String pwd;

    /**
     * 通道列表
     */
    private HashMap<String, Channel> channelMap;
    /**
     * 设备域名
     */
    private String doMain = "";
    /**
     * 分组 A
     */
    private String groupId;
    /**
     * 云视通号 361
     */
    private int no;
    /**
     * 小助手是否起作用
     */
    private boolean isHelperEnabled;
    /**
     * 云视通定义的设备类型
     */
    private int type;// 云视通端定义 如下常量及含义
    // public static final int DEVICE_TYPE_UNKOWN = -1;
    // public static final int DEVICE_TYPE_DVR = 0x01;
    // public static final int DEVICE_TYPE_950 = 0x02;
    // public static final int DEVICE_TYPE_951 = 0x03;
    // public static final int DEVICE_TYPE_IPC = 0x04;
    // public static final int DEVICE_TYPE_NVR = 0x05;

    private String ip;
    private int port;
    private boolean isOnline;
    private boolean hasSdCard;//弃用

    private int sdCardState;//0,无卡   1，有卡   2，卡异常
    /**
     * 0几版解码器
     */
    private boolean is05;
    /**
     * 是否带帧头
     */
    private boolean isJFH;
    /**
     * 是否启用TCP连接 2015-03-02
     */
    private int enableTcpConnect; // 是否为TCP连接 0. 不开启TCP连接 1.开启TCP连接

    /**
     * 设备服务器是否上线
     */
    private int serverOnLineState = 0;// 1,在线 0,离线
    /**
     * 设备是否带Wi-Fi
     */
    private int hasWifi = 0;// 1：带wifi 其他不带wifi

    private boolean isCard = false;// 是否是板卡

    /**
     * 设备云存储开关: 1,开 ,0关
     */
    private int cloudEnabled = -1; // 设备云存储开关: 1,开 ,0关

    /**
     * 设备是否支持云存储: 1,支持 ,0不支持
     */
    private int cloudStorage = -1;// 设备是否支持云存储: 1,支持 ,0不支持

    private int id;

    private boolean isDeviceAlarmEnable;//设备端是否开启了移动侦测开关
    private boolean isDeviceAlarmGot;//是否已经从设备端获取到了报警推送开关状态
    private boolean isDeviceAlarmGetting;//是否已经从设备端获取到了报警推送开关状态


    /**
     * 猫眼电量,-1-100,-1是未知,0-100是正常电量,家用设备默认是0
     */
    private int catBattery = -1; // 猫眼电量,-1-100,-1是未知,0-100是正常电量,家用设备默认是0

    private String devNetVersionName = "";//主控网络库版本名,无则是空字符串

    private int devNetVersion = -1;//主控网络库版本int值,无则是-1

    /**
     * 2016.11.03 视频连接方式：-1和0代表使用云视通连接，1代表猫眼使用流媒体链接
     */
    private int devConnWay = -1;

    private int ytSpeed;// 云台速度
    private int isCh;// 0-未窜货 1-窜货

    private int isFish;// 0-非鱼眼设备 1-鱼眼设备

    private int streamChangeSwitch = 0;//是否有码流切换权限
    private int playBackSwitch = 0;//是否有远程回放权限
    private int alarmSwitch = 0;//是否有查看报警消息权限

    private int timePoint = 0;//1:支持精准回放，0：不支持精准回放

    private String groupType;//设备所属组 2018/02/26,增加设备分组管理功能

    private boolean isSharingDev = false;//是否分享中的设备 2018.07.25

    // 是否支持3D定位
    private boolean isSupport3DLocate;

    // 是否支持声光报警
    private boolean isSupportAlarmSound;

    private int subStreamNum = 3;//码流个数

    // 门铃设备报警次数信息json字符串
    private String doorbellExtra;

    /**
     * 设备连接协议：1：好望协议 2： GB28181
     */
    private int connect_type;

    public boolean isGB28181Device() {
        return connect_type == 2;
    }

    public int getConnect_type() {
        return connect_type;
    }

    public void setConnect_type(int connect_type) {
        this.connect_type = connect_type;
    }



    public Device() {
        this.user = "";
        this.pwd = "";
    }

    /**
     * 创建指定通道个数的起始索引的设备
     *
     * @param ip
     * @param port
     * @param groupId
     * @param no
     * @param user
     * @param pwd
     * @param isHomeProduct
     * @param channelCount
     * @param startWindowIndex
     */
    public Device(String ip, int port, String groupId, int no, String user,
                  String pwd, boolean isHomeProduct, int channelCount,
                  int startWindowIndex, String devName) {
        this.ip = ip;
        this.port = port;
        this.groupId = groupId;

        this.no = no;
        this.user = user;
        this.pwd = pwd;

        isHelperEnabled = false;

        channelMap = new HashMap<String, Channel>(channelCount);
        Channel channel;

        for (int i = 0; i < channelCount; i++) {
            channel = new Channel(this, i, i, "");
            channelMap.put(channel.channelName, channel);
        }

    }

    public static JSONArray toJsonArray(ArrayList<Device> devList) {
        JSONArray devArray = new JSONArray();

        try {
            if (null != devList && 0 != devList.size()) {
                int size = devList.size();
                for (int i = 0; i < size; i++) {
                    devArray.put(i, devList.get(i).toJson());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return devArray;
    }

    public static String listToString(ArrayList<Device> devList) {
        return toJsonArray(devList).toString();
    }

    public ArrayList<Channel> getChannelList() {
        if (null == channelMap) return null;
        return new ArrayList<Channel>(channelMap.values());
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String gid) {
        this.groupId = gid;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }



    // public void setChannelList(ArrayList<Channel> channelList) {
    // this.channelList = channelList;
    // }

    public boolean isHelperEnabled() {
        return isHelperEnabled;
    }

    public void setHelperEnabled(boolean isHelperEnabled) {
        this.isHelperEnabled = isHelperEnabled;
    }

    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();

        try {
            object.put("ip", ip);
            object.put("port", port);
            object.put("doMain", doMain);
            object.put("gid", groupId);
            object.put("no", no);
            object.put("isOnline", isOnline);
            // object.put("isHomeProduct", isHomeProduct);
            object.put("is05", is05);
            object.put("enableTcpConnect", enableTcpConnect);
            object.put("hasWifi", hasWifi);
            object.put("serverOnLineState", serverOnLineState);
            object.put("cloudEnabled", cloudEnabled);// 云存储
            try {
                ArrayList<Channel> list = getChannelList();
                if (list!=null&&list.size()>0){
                    int size = list.size();
                    for (int i = 0; i < size; i++) {
                        array.put(i, list.get(i).toJson());
                    }
                    object.put("channelList", array);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    @Override
    public String toString() {
        return toJson().toString();
    }

    public boolean is05() {
        return is05;
    }

    public void set05(boolean is05) {
        this.is05 = is05;
    }

    public int getHasWifi() {
        return hasWifi;
    }

    public void setHasWifi(int hasWifi) {
        this.hasWifi = hasWifi;
    }

    public String getDoMain() {
        return doMain;
    }

    public void setDoMain(String doMain) {
        this.doMain = doMain;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isCard() {
        return isCard;
    }

    public void setCard(boolean isCard) {
        this.isCard = isCard;
    }




    public boolean isJFH() {
        return isJFH;
    }

    public void setJFH(boolean isJFH) {
        this.isJFH = isJFH;
    }

    public int getEnableTcpConnect() {
        return enableTcpConnect;
    }

    public void setEnableTcpConnect(int enableTcpConnect) {
        this.enableTcpConnect = enableTcpConnect;
    }

    public int getCloudEnabled() {
        return this.cloudEnabled;
    }

    public void setCloudEnabled(int enabled) {
        this.cloudEnabled = enabled;
    }


    public boolean isIs05() {
        return is05;
    }

    public void setIs05(boolean is05) {
        this.is05 = is05;
    }

    public int getServerOnLineState() {
        return serverOnLineState;
    }

    public void setServerOnLineState(int serverOnLineState) {
        this.serverOnLineState = serverOnLineState;
    }

    public int getYtSpeed() {
        return ytSpeed;
    }

    public void setYtSpeed(int ytSpeed) {
        this.ytSpeed = ytSpeed;
    }


    public boolean isOnline() {
        return isOnline;
    }
    public void setOnline(boolean status) {
        isOnline = status;
    }

    public boolean hasSdCard() {
        return hasSdCard;
    }

    public void setSdCard(boolean flag) {
        hasSdCard = flag;
    }


    public int getCloudStorage() {
        return cloudStorage;
    }

    public void setCloudStorage(int cloudStorage) {
        this.cloudStorage = cloudStorage;
    }

    public int getCatBattery() {
        return catBattery;
    }

    public void setCatBattery(int catBattery) {
        this.catBattery = catBattery;
    }

    public int getIsCh() {
        return isCh;
    }

    public void setIsCh(int isCh) {
        this.isCh = isCh;
    }

    public int getAlarmSwitch() {
        return alarmSwitch;
    }

    public void setAlarmSwitch(int alarmSwitch) {
        this.alarmSwitch = alarmSwitch;
    }

    public int getTimePoint() {
        return timePoint;
    }

    public void setTimePoint(int timePoint) {
        this.timePoint = timePoint;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public boolean isSupport3DLocate() {
        return isSupport3DLocate;
    }

    public void setSupport3DLocate(boolean support3DLocate) {
        isSupport3DLocate = support3DLocate;
    }


    public boolean isDeviceAlarmEnable() {
        return isDeviceAlarmEnable;
    }

    public void setDeviceAlarmEnable(boolean deviceAlarmEnable) {
        isDeviceAlarmEnable = deviceAlarmEnable;
    }

    public boolean isDeviceAlarmGot() {
        return isDeviceAlarmGot;
    }

    public void setDeviceAlarmGot(boolean deviceAlarmGot) {
        isDeviceAlarmGot = deviceAlarmGot;
    }

    public boolean isSupportAlarmSound() {
        return isSupportAlarmSound;
    }

    public void setSupportAlarmSound(boolean supportAlarmSound) {
        isSupportAlarmSound = supportAlarmSound;
    }

    public boolean isDeviceAlarmGetting() {
        return isDeviceAlarmGetting;
    }

    public void setDeviceAlarmGetting(boolean deviceAlarmGetting) {
        isDeviceAlarmGetting = deviceAlarmGetting;
    }

    public int getStreamChangeSwitch() {
        return streamChangeSwitch;
    }

    public void setStreamChangeSwitch(int streamChangeSwitch) {
        this.streamChangeSwitch = streamChangeSwitch;
    }

    public int getPlayBackSwitch() {
        return playBackSwitch;
    }

    public void setPlayBackSwitch(int playBackSwitch) {
        this.playBackSwitch = playBackSwitch;
    }

    public boolean isSharingDev() {
        return isSharingDev;
    }

    public void setSharingDev(boolean sharingDev) {
        isSharingDev = sharingDev;
    }

    public int getIsFish() {
        return isFish;
    }

    public void setIsFish(int isFish) {
        this.isFish = isFish;
    }

    public int getSubStreamNum() {
        return subStreamNum;
    }

    public void setSubStreamNum(int subStreamNum) {
        this.subStreamNum = subStreamNum;
    }

    public int getSdCardState() {
        return sdCardState;
    }

    public void setSdCardState(int sdCardState) {
        this.sdCardState = sdCardState;
    }

    public String getDoorbellExtra() {
        return doorbellExtra;
    }

    public void setDoorbellExtra(String doorbellExtra) {
        this.doorbellExtra = doorbellExtra;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getDevNetVersion() {
        return devNetVersion;
    }

    public void setDevNetVersion(int devNetVersion) {
        this.devNetVersion = devNetVersion;
    }
}