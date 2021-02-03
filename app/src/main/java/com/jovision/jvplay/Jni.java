package com.jovision.jvplay;

public class Jni {
    static {
        System.loadLibrary("jvplay");
    }

    /**
     * @brief 初始化
     * @param object 事件回调对象，需实现void OnEvent(int player_id, int event_type, int event_state, String json_data);
     * @return 错误码
     */
    public static native int holosensPlayerInit( Object object );
    /**
     * @brief 释放资源
     * @return 无
     */
    public static native void holosensPlayerRelease();

    /**
     * @brief 日志配置（暂未实现）
     * @param log_path 日志存放路径
     * @param log_level 日志级别，0：无日志，1：有日志，2...其他暂未定义
     * @return 无
     */
    public static native void holosensPlayerLogConfig(String log_path, int log_level);

    /**
     * @brief 建立octc连接
     * @param p2p_info p2p连接所需的信息，设备id，mts列表
     * @param channel_index 通道标号，从0开始
     * @param stream_index 码流标号，从0开始
     * @return 成功返回连player_id
     */
    public static native int holosensPlayerConnect(String p2p_info, int channel_index, int stream_index);

    /**
     * @brief 录像日期查询，查询存在录像的日期列表
     * @param p2p_info p2p连接所需的信息，设备id，mts列表
     * @param channel_index 通道标号，从0开始
     * @param stream_index 码流标号，从0开始
     * @return 成功返回连player_id
     */
    public static native int holosensPlayerQueryRecordDates(String p2p_info, int channel_index, int stream_index, String begin_time, String end_time);

    /**
     * @brief 录像查询
     * @param p2p_info p2p连接所需的信息，设备id，mts列表
     * @param channel_index 通道标号，从0开始
     * @param stream_index 码流标号，从0开始
     * @param begin_time rfc3999 格式的时间，如：2020-06-27T17:18:00.000+08:00
     * @param end_time rfc3999 格式的时间，如：2020-06-27T17:18:00.000+08:00
     * @return 成功返回连player_id
     */
    public static native int holosensPlayerQueryRecords(String p2p_info, int channel_index, int stream_index, String  begin_time, String  end_time);
    /**
     * @brief 录像回放
     * @param p2p_info p2p连接所需的信息，设备id，mts列表
     * @param channel_index 通道标号，从0开始
     * @param stream_index 码流标号，从0开始
     * @param begin_time rfc3999 格式的时间，如：2020-06-27T17:18:00.000+08:00
     * @return 播放器id
     */
    public static native int holosensPlayerPlayRecord(String p2p_info, int channel_index, int stream_index, String begin_time);

    /**
     * @brief 断开连接，异步操作，无回调。所有返回的播放id都应调用此接口进行销毁
     * @param player_id 播放器id
     * @return 无
     */
    public static native void holosensPlayerDisconnect(int player_id);
    /**
     * @brief 显示画面，可重复调用（设置显示区域），目前第二次以及后续的调用windows_id无效
     * @param player_id 播放器id
     * @param surface 窗口句柄（安卓平台传入surface，IOS平台传入view）
     * @param left 屏幕坐标，显示位置最左侧（手机平台还不确定，调试阶段可暂传入0）
     * @param top 屏幕坐标，显示位置最顶侧（手机平台还不确定，调试阶段可暂传入0）
     * @param right 屏幕坐标，显示位置最右侧（手机平台还不确定，调试阶段可暂传入view的宽）
     * @param bottom 屏幕坐标，显示位置最底侧（手机平台还不确定，调试阶段可暂传入view的高）
     * @return 无
     */
    public static native void holosensPlayerShow(int player_id, Object surface, int left, int top, int right, int bottom);

    /**
     * @brief 隐藏画面
     * @param player_id 播放器id
     * @return 无
     */
    public static native void holosensPlayerHide(int player_id);

    /**
     * @brief 是否静音
     * @param player_id 播放器id
     * @param enable 0 静音， 1 播放
     * @return 无
     */
    public static native void holosensPlayerMute(int player_id, int enable);
    /**
     * @brief 请求I帧，直播模式下使用
     * @param player_id 播放器id
     * @return 0-成功, 失败-1
     */
    public static native int holosensPlayerRequestIFrame(int player_id);
    /**
     * @brief 暂停
     * @param player_id 播放器id
     * @return 无
     */
    public static native void holosensPlayerPause(int player_id);
    /**
     * @brief 恢复
     * @param player_id 播放器id
     * @return 无
     */
    public static native void holosensPlayerResume(int player_id);
    /**
     * @brief 单帧
     * @param player_id 播放器id
     * @return 无
     */
    public static native void holosensPlayerStep(int player_id);
    /**
     * @brief 倍速
     * @param player_id 播放器id
     * @param speed 取值（-5~0~5）播放速度为：2^speed
     * @return 无
     */
    public static native void holosensPlayerSetSpeed(int player_id, int speed);
    /**
     * @brief 跳转
     * @param player_id 播放器id
     * @param time_pos rfc3999 格式的时间，如：2020-06-27T17:18:00.000+08:00
     * @return 无
     */
    public static native void holosensPlayerSkip(int player_id, String  time_pos);

    /**
     * @brief 抓拍画面，适用于直播和回放
     * @param player_id 播放器id
     * @param format 0：bmp，其他暂不支持
     * @param fullname 抓图文件名（全路径）
     * @return 无
     */
    public static native void holosensPlayerSnapshot(int player_id, int format, String fullname);

    /**
     * @brief 开始录像
     * @param player_id 播放器id
     * @param format 0：mp4，其他暂不支持
     * @param save_path 存储路径
     * @param name_prefix 录像文件名前缀，录像名为：prefix_开始时间_结束时间.mp4
     * @return 无
     */
    public static native void holosensPlayerRecordStart(int player_id, int format, String save_path, String name_prefix);

    /**
     * @brief 停止录像
     * @param player_id 播放器id
     * @return 无
     */
    public static native void holosensPlayerRecordStop(int player_id);

    /**
     * @brief 开启语音对讲
     * @return 播放器id，用holosens_player_disconnect即可关闭对讲
     */
    public static native int holosensPlayerIntercomOn(String info);


}
