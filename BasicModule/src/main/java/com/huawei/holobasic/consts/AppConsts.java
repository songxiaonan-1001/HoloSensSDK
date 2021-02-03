package com.huawei.holobasic.consts;


import android.os.Environment;

import com.huawei.holobasic.utils.AppUtils;

import java.io.File;


public class AppConsts {

    /**==================APP用到额路径*/
    /**
     *
     */
    public static String SD_CARD_PATH = AppUtils.getApp().getExternalCacheDir().getPath() + File.separator;

    // 软件名
    public static final String APP_NAME = "HoloSensEnterprise";

    // 应用路径
    public static final String APP_PATH = SD_CARD_PATH + APP_NAME + File
            .separator;
    // 普通日志路径
    public static final String LOG_PATH = APP_PATH + "log" + File.separator;

    public static final String CAPTURE_PATH = APP_PATH + "capture" + File
            .separator;

    public static final String VIDEO_PATH = APP_PATH + "video" + File.separator;
    public static final String AD_PATH = APP_PATH + ".ad" + File.separator;
    public static final String WELCOME_IMG_PATH = APP_PATH + "welcome" + File
            .separator;

    // 抓拍声音 文件存放位置 capture sound fileName
    public static final String CAPTURE_SOUND_FILE = "capture" + File.separator + "capture.wav";

    public static final String IMAGE_JPG_KIND = ".jpg";// 图片类型
    public static final String VIDEO_MP4_KIND = ".mp4";// 视频类型
    /*************/
    // 是否是debug模式
    public static final boolean DEBUG_STATE = false;

    public static final String FORMATTER_REMOTE_DATE = "%04d-%02d-%02d";


    /**
     * 图片移动到相册专用目录
     * 外部目录，非系统目录
     * 允许相册访问到
     */
    public static final String SD_CARD_ALBUM_PATH = Environment
            .getExternalStorageDirectory().getPath() + File.separator;

}
