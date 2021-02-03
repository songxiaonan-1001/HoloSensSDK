package com.huawei.holosens.bean;

import android.util.Log;

import com.huawei.holosens.consts.JVEncodedConst;
import com.huawei.holosens.consts.JVOctConst;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 简单的通道集合类
 *
 * @author
 */
public class Channel {

    private final static String TAG = "Channel";
    /**
     * 通道昵称
     */
    public String channelName = "";
    /**
     * 窗口索引
     */
    private int index;// 0-35
    /**
     * 设备通道，从1 开始，与张帅服务器统一从1开始
     */
    private int channel;// 1-4
    /**
     * 通道类型，设备类型
     * @see com.huawei.holosens.commons.DevType
     */
    private int devType;
    private Device parent;

    /**
     * 新国标设备channel_id和其他不一样
     */
    private String channel_id;

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    private boolean isShared = false;//是否是被分享的通道

    private boolean isVoiceCall = false;// 是否正在对讲
    private boolean isLisening = false;// 是否正在监听
    private boolean isSendCMD = false;// 是否只发关键帧
    private boolean isRecording = false;//是否正在录像
    private boolean isRecordChangeStream = false;//切码流时是否正在录像
    private boolean isPtzLayoutShow = false; //是否打开了ptz界面
    private int recordTime;//录像时长

    private int audioType = 0;// 音频类型
    private int audioByte = 0;// 音频比特率8 16

    // [Neo] 语音对讲时编码类型
    private int audioEncType = 0;
    // [Neo] 语音对讲时编码帧大小
    private int audioBlock = 0;

    private boolean agreeTextData = false;// 是否同意文本聊天
    private boolean isOMX = false;// 是否硬解
    private int streamTag = JVOctConst.STREAM_SD;// 码流参数值 第一码流(6)MainStreamQos 1,2,3

    private int octStreamTag = -1;// 码流参数值 第一码流(6)MainStreamQos 1,2,3
    // 手机码流(5)MobileStreamQSos 1,2,3 融合代码后手机码流改为
    // MobileQuality 1,2,3
    private int width = 0;// 视频宽
    private int height = 0;// 视频高
    private boolean supportCall;// 是否支持通道对讲
    private boolean supportNvrCall;// 是否支持NVR对讲
    private boolean supportPtz;// 是否支持云台操作

    // 视频手势缩放用
    private int lastPortLeft;
    private int lastPortBottom;
    private int lastPortWidth;
    private int lastPortHeight;



    //O帧里面视频编码， 1：h264  2:h265
    private int videoEncType = 0;//video_encType


    /**
     * 设备好望和国标对应的channelid不一致，好望channel，国标：ipc_device_channel_id
     * @param device
     * @param index
     * @param channel
     * @param nick
     */
    public Channel(Device device, int index, int channel,// boolean isConnected,
                   String nick) {
        this.parent = device;
        this.index = index;
        this.channel = channel;
        this.channelName = nick;
    }

    public Channel() {
    }

    public int getLastPortLeft() {
        return lastPortLeft;
    }

    public void setLastPortLeft(int lastPortLeft) {
        this.lastPortLeft = lastPortLeft;
    }

    public int getLastPortBottom() {
        return lastPortBottom;
    }

    public void setLastPortBottom(int lastPortBottom) {
        this.lastPortBottom = lastPortBottom;
    }

    public int getLastPortWidth() {
        return lastPortWidth;
    }

    public void setLastPortWidth(int lastPortWidth) {
        this.lastPortWidth = lastPortWidth;
    }

    public int getLastPortHeight() {
        return lastPortHeight;
    }

    public void setLastPortHeight(int lastPortHeight) {
        this.lastPortHeight = lastPortHeight;
    }

    public boolean isAgreeTextData() {
        return agreeTextData;
    }

    public void setAgreeTextData(boolean agreeTextData) {
        this.agreeTextData = agreeTextData;
    }

    public boolean isOMX() {
        return isOMX;
    }

    public void setOMX(boolean isOMX) {
        this.isOMX = isOMX;
    }

    public int getStreamTag() {
        return streamTag;
    }

    public void setStreamTag(int streamTag) {
        this.streamTag = streamTag;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    //国标ipc设备对应的通道ID
    private String ipc_device_channel_id;

    public String getIpc_device_channel_id() {
        return ipc_device_channel_id;
    }

    public void setIpc_device_channel_id(String ipc_device_channel_id) {
        this.ipc_device_channel_id = ipc_device_channel_id;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public JSONObject toJson() {
        JSONObject object = new JSONObject();

        try {
            object.put("index", index);
            object.put("channel", channel);
            object.put("channelName", channelName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    @Override
    public String toString() {
        return toJson().toString();
    }

    public JSONArray toJsonArray(ArrayList<Channel> channelList) {
        JSONArray channelArray = new JSONArray();

        try {
            if (null != channelList && 0 != channelList.size()) {
                int size = channelList.size();
                for (int i = 0; i < size; i++) {
                    channelArray.put(i, channelList.get(i).toJson());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return channelArray;
    }

    public String listToString(ArrayList<Channel> devList) {
        return toJsonArray(devList).toString();
    }

    public Device getParent() {
        return parent;
    }

    public void setParent(Device parent) {
        this.parent = parent;
    }

    // public boolean isConnecting() {
    // return isConnecting;
    // }
    //
    // public void setConnecting(boolean isConnecting) {
    // this.isConnecting = isConnecting;
    // }


    public boolean isShared() {
        return isShared;
    }

    public void setShared(boolean shared) {
        isShared = shared;
    }

    public boolean isVoiceCall() {
        return isVoiceCall;
    }

    public void setVoiceCall(boolean isVoiceCall) {
        Log.e(TAG, "setVoiceCall: 20180319 setVoiceCall : "+isVoiceCall);
        this.isVoiceCall = isVoiceCall;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public boolean isSendCMD() {
        return isSendCMD;
    }

    public void setSendCMD(boolean isSendCMD) {
        this.isSendCMD = isSendCMD;
    }

    public int getAudioType() {
        return audioType;
    }

    public void setAudioType(int audioType) {
        this.audioType = audioType;
    }

    public int getAudioByte() {
        return audioByte;
    }

    public void setAudioByte(int audioByte) {
        this.audioByte = audioByte;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getAudioEncType() {
        return audioEncType;
    }

    public void setAudioEncType(int audioEncType) {
        this.audioEncType = audioEncType;

        switch (audioEncType) {
            case JVEncodedConst.JAE_ENCODER_ALAW:
            case JVEncodedConst.JAE_ENCODER_ULAW:
                audioBlock = JVEncodedConst.ENC_G711_SIZE;
                break;

            case JVEncodedConst.JAE_ENCODER_SAMR:
                audioBlock = JVEncodedConst.ENC_AMR_SIZE;
                break;

            case JVEncodedConst.JAE_ENCODER_G729:
                audioBlock = JVEncodedConst.ENC_G729_SIZE;
                break;

            default:
                audioBlock = JVEncodedConst.ENC_PCM_SIZE;
                break;
        }
    }

    public boolean isSupportPtz() {
        return supportPtz;
    }

    public void setSupportPtz(boolean supportPtz) {
        this.supportPtz = supportPtz;
    }

    public int getAudioBlock() {
        return audioBlock;
    }

    public boolean isSupportCall() {
        return supportCall;
    }

    public void setSupportCall(boolean supportCall) {
        this.supportCall = supportCall;
    }

    public boolean isSupportNvrCall() {
        return supportNvrCall;
    }

    public void setSupportNvrCall(boolean supportNvrCall) {
        this.supportNvrCall = supportNvrCall;
    }

    public boolean isLisening() {
        return isLisening;
    }

    public void setLisening(boolean isLisening) {
        this.isLisening = isLisening;
    }

    public boolean isRecording() {
        return isRecording;
    }

    public void setRecording(boolean recording) {
        isRecording = recording;
    }

    public boolean isRecordChangeStream() {
        return isRecordChangeStream;
    }

    public void setRecordChangeStream(boolean recordChangeStream) {
        isRecordChangeStream = recordChangeStream;
    }

    public boolean isPtzLayoutShow() {
        return isPtzLayoutShow;
    }

    public void setPtzLayoutShow(boolean ptzLayoutShow) {
        isPtzLayoutShow = ptzLayoutShow;
    }

    public int getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(int recordTime) {
        this.recordTime = recordTime;
    }

    public int getOctStreamTag() {
        return octStreamTag;
    }

    public void setOctStreamTag(int octStreamTag) {
        this.octStreamTag = octStreamTag;
    }

    public int getVideoEncType() {
        return videoEncType;
    }

    public void setVideoEncType(int videoEncType) {
        this.videoEncType = videoEncType;
    }


    private String mts;
    private int channelType;                 //HOLO  GB28181
    private String token;

    public int getChannelType() {
        return channelType;
    }

    public void setChannelType(int channelType) {
        this.channelType = channelType;
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

    private String nickName;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getDevType() {
        return devType;
    }

    public void setDevType(int devType) {
        this.devType = devType;
    }

    private String jvmpUrl;
    private String workIngKey;

    public String getJvmpUrl() {
        return jvmpUrl;
    }

    public void setJvmpUrl(String jvmpUrl) {
        this.jvmpUrl = jvmpUrl;
    }

    public String getWorkIngKey() {
        return workIngKey;
    }

    public void setWorkIngKey(String workIngKey) {
        this.workIngKey = workIngKey;
    }

    private boolean isTurn;      //是否是云转发 0注码流，1次码流

    public boolean isTurn() {
        return isTurn;
    }

    public void setTurn(boolean turn) {
        isTurn = turn;
    }

    private int onlineStatus = 1;       //是否进度

    public int getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(int onlineStatus) {
        this.onlineStatus = onlineStatus;
    }
}
