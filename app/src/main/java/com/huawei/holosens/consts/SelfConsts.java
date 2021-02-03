package com.huawei.holosens.consts;

public class SelfConsts {
    public static final int BASE_ID = 0x1000;


    /**
     * 远程回放消息
     **/
    public static final int WHAT_REMOTE_DOWNLOAD_FILE = BASE_ID + 0x003;// 远程回放点击下载按钮

    public static final int WHAT_REMOTE_PLAY_AT_INDEX = BASE_ID + 0x00D;// 视频播放3s钟隐藏抓拍布局

    /**
     * 视频播放相关的消息
     */
    // 远程回放5s隐藏操作按钮
    public static final int WHAT_PLAY_DISPLAY_NAV_BTN = BASE_ID + 0x105;

    /**
     * 监听到锁屏和解锁
     **/
    public static final int WHAT_ON_SCREEN_OFF = BASE_ID + 0x210;
    public static final int WHAT_ON_SCREEN_ON = BASE_ID + 0x211;



}
