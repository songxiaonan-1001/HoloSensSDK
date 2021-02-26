package com.huawei.holosens.utils;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.SurfaceHolder;

import com.huawei.holobasic.consts.AppConsts;
import com.huawei.holobasic.utils.DateUtil;
import com.huawei.holobasic.utils.FileUtil;
import com.huawei.holosens.base.BaseApplication;
import com.huawei.holosens.consts.JVSetParamConst;
import com.jovision.jvplay.Jni;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class JniUtil {
    private static final String TAG = "JniUtil";

    public static HashMap<Integer, Integer> windowPlayerIdMap = BaseApplication.getInstance().getPlayerIdWindowMap();//


    /**
     * 播放库初始化
     * @param obj
     * @return
     */
    public static int holosensPlayerInit(Object obj){
        int initRes;
        initRes = Jni.holosensPlayerInit(obj);
        Log.e(TAG,"function=holosensPlayerInit:initRes="+initRes);
        return initRes;
    }

    /**
     * 播放库释放
     * @return
     */
    public static void holosensPlayerRelease(){
        Jni.holosensPlayerRelease();
        Log.e(TAG,"function=holosensPlayerRelease");
    }

    /**
     * 连接方法
     * @param window
     * @param url
     * @return true:连接成功   false:连接失败
     */
    public static boolean connect(int window, String url) {
        int playerId = Jni.holosensPlayerConnect(url,0,0);
        Log.e(TAG, "function=connect:playerId=" + playerId + ", jvmp=" + url);
        if (playerId > 0) {
            BaseApplication.getInstance().playerIdWindowMap.put(playerId, window);
            windowPlayerIdMap.put(window,playerId);
            return true;
        }
        return false;
    }

    /**
     * 断开视频连接
     * @param window
     */
    public static void disConnect(int window){
        if (!windowPlayerIdMap.containsKey(window)) {
            return;
        }
        int playerId = windowPlayerIdMap.get(window);
        Jni.holosensPlayerDisconnect(playerId);
        windowPlayerIdMap.remove(window);
        Log.e(TAG, "function=holosensPlayerDisconnect:playerId=" + playerId);
    }

    /**
     * 显示视频
     * @param window
     * @param mSurfaceHolder
     */
    public static void show(int window, SurfaceHolder mSurfaceHolder){
        if (!windowPlayerIdMap.containsKey(window)) {
            return;
        }
        int playerId = windowPlayerIdMap.get(window);
        Jni.holosensPlayerShow(playerId, mSurfaceHolder.getSurface(), 0, 0, mSurfaceHolder.getSurfaceFrame().width(), mSurfaceHolder.getSurfaceFrame().height());
        Log.e(TAG, "function=holosensPlayerShow:playerId=" + playerId+";width="+mSurfaceHolder.getSurfaceFrame().width()+";height="+mSurfaceHolder.getSurfaceFrame().height());
    }

    /**
     * @brief 日志配置
     * @param log_path 日志存放路径
     * @param log_level 日志级别，0：无日志，1：有日志，2...其他暂未定义
     * @return 无
     */
    public static void holosensPlayerLogConfig(String log_path, int log_level){
        Jni.holosensPlayerLogConfig(log_path,log_level);
        Log.e(TAG,"function=holosensPlayerLogConfig:log_path="+log_path+";log_level="+log_level);
    }

    /**
     * @brief 建立octc连接
     * @param p2p_info p2p连接所需的信息，设备id，mts列表
     * @param window 通道标号，从0开始
     * @param stream_index 码流标号，从0开始
     * @return 成功返回连player_id
     */
    public static int holosensPlayerConnectByP2p(String p2p_info, int window, int stream_index, int channel){
        int playerId = Jni.holosensPlayerConnect(p2p_info,channel,stream_index);

        Log.e(TAG, "function=holosensPlayerConnectByP2p:playerId=" + playerId + ", p2p_info=" + p2p_info);
        if (playerId > 0) {
            BaseApplication.getInstance().playerIdWindowMap.put(playerId, window);
            windowPlayerIdMap.put(window,playerId);
        }
        Log.e(TAG,"function=holosensPlayerConnectByP2p:p2p_info="+p2p_info+";window="+window+";stream_index="+stream_index);
        return playerId;
    }

    /**
     * @brief 录像查询
     * @param p2p_info p2p连接所需的信息，设备id，mts列表
     * @param channel_index 通道标号，从0开始
     * @param begin_time rfc3999 格式的时间，如：2020-06-27T17:18:00.000+08:00
     * @param end_time rfc3999 格式的时间，如：2020-06-27T17:18:00.000+08:00
     * @return 成功返回连player_id
     */
    public static int holosensPlayerQueryRecordsByP2p(String p2p_info, int channel_index, int stream_index, String begin_time, String end_time){
        int result = Jni.holosensPlayerQueryRecords(p2p_info,channel_index,stream_index,begin_time,end_time);
        Log.e(TAG,"function=holosensPlayerQueryRecordsByP2p:p2p_info"+p2p_info+";channel_index="+channel_index+";begin_time="+begin_time+";end_time="+end_time+";result="+result);
        return result;
    }

    /**
     * @brief 录像查询
     * @param p2p_info p2p连接所需的信息，设备id，mts列表
     * @param channel_index 通道标号，从0开始
     * @param begin_time rfc3999 格式的时间，如：2020-06-27T17:18:00.000+08:00
     * @param end_time rfc3999 格式的时间，如：2020-06-27T17:18:00.000+08:00
     * @return 成功返回连player_id
     */
    public static int holosensPlayerQueryRecordDates(String p2p_info, int channel_index, int stream_index, String begin_time, String end_time){
        int result = Jni.holosensPlayerQueryRecordDates(p2p_info,channel_index,stream_index,begin_time,end_time);
        Log.e(TAG,"function=holosensPlayerQueryRecordDates:p2p_info"+p2p_info+";channel_index="+channel_index+";begin_time="+begin_time+";end_time="+end_time+";result="+result);
        return result;
    }

    /**
     * @brief 录像回放
     * @param p2p_info p2p连接所需的信息，设备id，mts列表
     * @param channel_index 通道标号，从0开始
     * @param begin_time rfc3999 格式的时间，如：2020-06-27T17:18:00.000+08:00
     * @return 播放器id
     */
    public static int holosensPlayerPlayRecordByP2p(int window, String p2p_info, int channel_index, int stream_index, String begin_time){
        int playerId = Jni.holosensPlayerPlayRecord(p2p_info,channel_index,stream_index,begin_time);
        Log.e(TAG,"function=holosensPlayerPlayRecordByP2p:p2p_info="+p2p_info+";channel_index="+channel_index+";begin_time="+begin_time+";playerId="+playerId);

        if (playerId > 0) {
            BaseApplication.getInstance().playerIdWindowMap.put(playerId, window);
            windowPlayerIdMap.put(window,playerId);
        }
        return playerId;
    }

    /**
     * @brief 打开声音
     * @param window 窗口，转换成播放器id
     * @return 无
     */
    public static void holosensPlayerOpenSound(int window){
        if (!windowPlayerIdMap.containsKey(window)) {
            return;
        }
        int playerId = windowPlayerIdMap.get(window);
        Jni.holosensPlayerMute(playerId,1);
        Log.e(TAG,"function=holosensPlayerOpenSound:playerId="+playerId+";window="+window);
    }

    /**
     * @brief 关闭声音
     * @param window 窗口，转换成播放器id
     * @return 无
     */
    public static void holosensPlayerCloseSound(int window){
        if (!windowPlayerIdMap.containsKey(window)) {
            return;
        }
        int playerId = windowPlayerIdMap.get(window);
        Jni.holosensPlayerMute(playerId,0);
        Log.e(TAG,"function=holosensPlayerCloseSound:playerId="+playerId+";window="+window);
    }

    /**
     * @brief 请求I帧，直播模式下使用
     * @param window 窗口，转换成播放器id
     * @return 0-成功, 失败-1
     */
    public static int holosensPlayerRequestIFrame(int window){
        if (!windowPlayerIdMap.containsKey(window)) {
            return -1;
        }
        int playerId = windowPlayerIdMap.get(window);
        int result = Jni.holosensPlayerRequestIFrame(playerId);
        Log.e(TAG,"function=holosensPlayerRequestIFrame:playerId="+playerId+";window="+window+";result="+result);
        return result;
    }

    /**
     * @brief 暂停
     * @param window 窗口，转换成播放器id
     * @return 无
     */
    public static void holosensPlayerPause(int window){
        if (!windowPlayerIdMap.containsKey(window)) {
            return;
        }
        int playerId = windowPlayerIdMap.get(window);
        Jni.holosensPlayerPause(playerId);
        Log.e(TAG,"function=holosensPlayerPause:playerId="+playerId+";window="+window);
    }

    /**
     * @brief 恢复
     * @param window 窗口，转换成播放器id
     * @return 无
     */
    public static void holosensPlayerResume(int window){
        if (!windowPlayerIdMap.containsKey(window)) {
            return;
        }
        int playerId = windowPlayerIdMap.get(window);
        Jni.holosensPlayerResume(playerId);
        Log.e(TAG,"function=holosensPlayerResume:playerId="+playerId+";window="+window);
    }

    /**
     * @brief 单帧
     * @param window 窗口，转换成播放器id
     * @return 无
     */
    public static void holosensPlayerStep(int window){
        if (!windowPlayerIdMap.containsKey(window)) {
            return;
        }
        int playerId = windowPlayerIdMap.get(window);
        Jni.holosensPlayerStep(playerId);
        Log.e(TAG,"function=holosensPlayerStep:playerId="+playerId+";window="+window);
    }

    /**
     * @brief 倍速
     * @param window 窗口，转换成播放器id
     * @param speed 取值（-5~0~5）播放速度为：2^speed
     * @return 无
     */
    public static void holosensPlayerSetSpeed(int window, int speed){
        if (!windowPlayerIdMap.containsKey(window)) {
            return;
        }
        int playerId = windowPlayerIdMap.get(window);
        Jni.holosensPlayerSetSpeed(playerId,speed);
        Log.e(TAG,"function=holosensPlayerSetSpeed:playerId="+playerId+";window="+window+";speed="+speed);
    }

    /**
     * @brief 跳转
     * @param window 窗口，转换成播放器id
     * @param time_pos rfc3999 格式的时间，如：2020-06-27T17:18:00.000+08:00
     * @return 无
     */
    public static void holosensPlayerSkip(int window, String time_pos){
        if (!windowPlayerIdMap.containsKey(window)) {
            return;
        }
        int playerId = windowPlayerIdMap.get(window);
        Jni.holosensPlayerSkip(playerId,time_pos);
        Log.e(TAG,"function=holosensPlayerSetSpeed:playerId="+playerId+";window="+window+";time_pos="+time_pos);
    }

    /**
     * 抓拍
     * @param window
     * @param devid
     * @return
     */
    public static String captureWithDev(int window, String devid) {
        String capturePath = AppConsts.CAPTURE_PATH + DateUtil.getCurrentDateSimple()
                + File.separator+devid.replace(File.separator, "")+ File.separator;
        String fileName = DateFormat.format(JVSetParamConst.FORMATTER_TIME,
                Calendar.getInstance(Locale.CHINA))
                + AppConsts.IMAGE_JPG_KIND;
        FileUtil.createDirectory(capturePath);

        if (!windowPlayerIdMap.containsKey(window)) {
            return "";
        }
        int playerId = windowPlayerIdMap.get(window);
        Jni.holosensPlayerSnapshot(playerId, 0, capturePath +fileName);
        return capturePath +fileName;
    }

    /**
     * 开始录像
     */
    public static String startRecordByDev(int window, String devName) {
        String videoPath = AppConsts.VIDEO_PATH + DateUtil.getCurrentDateSimple()
                + File.separator+devName.replace(File.separator, "")+ File.separator;

        String fileName = DateFormat.format(JVSetParamConst.FORMATTER_TIME,
                Calendar.getInstance(Locale.CHINA))
                + AppConsts.VIDEO_MP4_KIND;

        FileUtil.createDirectory(videoPath);

        if (!windowPlayerIdMap.containsKey(window)) {
            return "";
        }
        int playerId = windowPlayerIdMap.get(window);
        Jni.holosensPlayerRecordStart(playerId, 0, videoPath ,fileName);
        return videoPath +fileName;
    }

    /**
     * 结束录像
     * @param window
     * @return
     */
    public static void stopRecord(int window) {
        if (!windowPlayerIdMap.containsKey(window)) {
            return ;
        }
        int playerId = windowPlayerIdMap.get(window);
        Jni.holosensPlayerRecordStop(playerId);
    }
}
