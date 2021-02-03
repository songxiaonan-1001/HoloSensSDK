package com.huawei.holosens.live.play.player;


import com.huawei.holosens.live.play.bean.Glass;

/**
 * 播放接口
 *
 */

public interface IPlayHelper {
    int prepare = 0X10;// (16)准备连接
    int connecting = 0x20;// (32)连接中 ： 开始连接
    int buffering1 = 0x21;// (33)缓冲中 ： connect change 回来，等待O帧
    int buffering2 = 0x22;// (34)缓冲中... ： O帧过来了，等待I帧（I帧有可能解码失败）
    int connected = 0x23;// (35)已连接 ： I帧过来了（分辨率很高时有可能I帧传的比较慢）
    int connectFailed = 0x24;// (36)连接失败 : 连接失败
    int disconnected = 0x25;// (37)断开连接 : 主动断开连接
    int paused = 0x26;// (38)点击继续播放 ：已暂停
    int area_error = 0x27;// (39)串货设备，文字提醒


    /**
     * 连接
     *
     * @param isPreConnect 是否为预连接
     */
    void connect(boolean isPreConnect);

    // 重新连接
    void resume();

    // 暂停
    void pause();

    // 停止
    void disconnect();

    // 销毁
    void destroy();

    // 调整视频大小
    void changeSize(Glass.Size glassSize);

    /**
     * 切换码流
     */
    void switchStream();
}