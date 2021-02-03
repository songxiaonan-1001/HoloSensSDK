package com.huawei.holosens.live.play.bean;


import com.huawei.holosens.bean.Channel;
import com.huawei.holosens.live.play.player.IPlayHelper;

/**
 * 玻璃实体类
 *
 */

public class Glass {
    /*
      玻璃的制造工艺:
      云视通1.0、云视通2.0、流媒体协议
     */
    private int mProtocol;
    /*
      玻璃的具体类型:
      云视通1.0(IPC|猫眼)、云视通2.0、流媒体协议(IPC|猫眼)
     */
    private int mType;
    /*
      玻璃所在的分组名称:
      主要用于播放界面点击"+"号添加设备的Dialog
     */
    private String mGroupName;
    // 玻璃号
    private int mNo = GlassType.TYPE_EMPTY;
    // 窗户号(每块玻璃都会有所属的窗户)
    private int mWindowNo;
    // 一块玻璃对应一个通道
    private Channel mChannel;
    // 播放视频控制助手
    private IPlayHelper mPlayHelper;
    // 玻璃是否被安装
    private boolean isInstalled;
    // 玻璃是否被选中
    private boolean isChecked;
    // 玻璃是否正在被擦洗(码流切换)
    private boolean streamStartToChange;
    // ----- AP模式 Start -----
    // 是否为AP模式(主要针对AP设备,默认不是AP设备)
    private boolean isApMode;
    private boolean isLocal;
    // ----- AP模式 End ------

    // ----- 是否是手动断开，手动断开不显示连接断开 Start -----
    private boolean isManualDisconnect;
    // ----- 是否是手动断开，手动断开不显示连接断开 End ------

    // 玻璃尺寸
    public static class Size {
        public int width;
        public int height;
    }

    public Glass(int type) {
        mType = type;
        doSetProtocol(type);
    }

    public int getProtocol() {
        return mProtocol;
    }

    public void setProtocol(int protocol) {
        mProtocol = protocol;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
        doSetProtocol(type);
    }

    public String getGroupName() {
        return mGroupName;
    }

    public void setGroupName(String groupName) {
        mGroupName = groupName;
    }

    public int getNo() {
        return mNo;
    }

    public void setNo(int no) {
        mNo = no;
    }

    public int getWindowNo() {
        return mWindowNo;
    }

    public void setWindowNo(int windowNo) {
        mWindowNo = windowNo;
    }

    public Channel getChannel() {
        return mChannel;
    }

    public void setChannel(Channel channel) {
        mChannel = channel;
    }

    public IPlayHelper getPlayHelper() {
        return mPlayHelper;
    }

    public void setPlayHelper(IPlayHelper playHelper) {
        mPlayHelper = playHelper;
    }

    public boolean isInstalled() {
        return isInstalled;
    }

    public void setInstalled(boolean installed) {
        isInstalled = installed;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isManualDisconnect() {
        return isManualDisconnect;
    }

    public void setManualDisconnect(boolean manualDisconnect) {
        isManualDisconnect = manualDisconnect;
    }

    public boolean isStreamStartToChange() {
        return streamStartToChange;
    }

    public void setStreamStartToChange(boolean streamStartToChange) {
        this.streamStartToChange = streamStartToChange;
    }

    public boolean isApMode() {
        return isApMode;
    }

    public void setApMode(boolean apMode) {
        isApMode = apMode;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    /**
     * 设置玻璃使用的工艺
     *
     * @param type
     */
    private void doSetProtocol(int type) {
        // 设置玻璃使用的工艺
        int protocol = 0;
        switch (type) {
            case GlassType.TYPE_GLASS_CLOUDSEE_V1_CAT:
            case GlassType.TYPE_GLASS_CLOUDSEE_V1_IPC:
                protocol = GlassType.TYPE_PROTOCOL_CLOUDSEE_V1;
                break;
            case GlassType.TYPE_GLASS_CLOUDSEE_V2_CAT:
            case GlassType.TYPE_GLASS_CLOUDSEE_V2_IPC:
                protocol = GlassType.TYPE_PROTOCOL_CLOUDSEE_V2;
                break;
            case GlassType.TYPE_GLASS_STREAM_V1_CAT:
            case GlassType.TYPE_GLASS_STREAM_V1_IPC:
                protocol = GlassType.TYPE_PROTOCOL_STREAM_V1;
                break;
            case GlassType.TYPE_EMPTY:
                protocol = -1;
                break;
            case GlassType.TYPE_PLUS:
                protocol = -2;
                break;
            default:
                break;
        }

        setProtocol(protocol);
    }
}