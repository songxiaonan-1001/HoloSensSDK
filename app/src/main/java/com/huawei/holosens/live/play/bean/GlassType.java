    package com.huawei.holosens.live.play.bean;

/**
 * 玻璃类型
 *
 */

public class GlassType {

    /**
     * 协议
     */
    // 云视通1.0协议
    public static final int TYPE_PROTOCOL_CLOUDSEE_V1 = 0x1;
    // 云视通2.0协议
    public static final int TYPE_PROTOCOL_CLOUDSEE_V2 = 0x10;
    // 流媒体1.0协议
    public static final int TYPE_PROTOCOL_STREAM_V1 = 0x20;

    /**
     * 普通种类
     */
    // 玻璃材质_C1类玻璃(云视通1.0)_摄像机/猫眼
    public static final int TYPE_GLASS_CLOUDSEE_V1_IPC = TYPE_PROTOCOL_CLOUDSEE_V1 + 1;
    public static final int TYPE_GLASS_CLOUDSEE_V1_CAT = TYPE_PROTOCOL_CLOUDSEE_V1 + 2;
    //预留C1类...
    // 玻璃材质_C2类玻璃(云视通2.0)_摄像机/猫眼
    public static final int TYPE_GLASS_CLOUDSEE_V2_IPC = TYPE_PROTOCOL_CLOUDSEE_V2 + 1;
    public static final int TYPE_GLASS_CLOUDSEE_V2_CAT = TYPE_PROTOCOL_CLOUDSEE_V2 + 2;
    //预留C2类...
    // 玻璃材质_S类玻璃(流媒体)
    public static final int TYPE_GLASS_STREAM_V1_IPC = TYPE_PROTOCOL_STREAM_V1 + 1;
    public static final int TYPE_GLASS_STREAM_V1_CAT = TYPE_PROTOCOL_STREAM_V1 + 2;
    //预留R类...

    /**
     * 特殊种类
     */
    // 玻璃材质_白纸
    public static final int TYPE_EMPTY = -1;
    // 玻璃材质_"+"号
    public static final int TYPE_PLUS = -2;
}
