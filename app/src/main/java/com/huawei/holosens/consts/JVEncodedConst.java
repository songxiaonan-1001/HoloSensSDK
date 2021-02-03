package com.huawei.holosens.consts;


/**
 * 加密后的消息值
 */
public class JVEncodedConst {


    //视频连接方式，3:云视通2.0  1:流媒体（全转发）  0or-1:云视通 4、JVMP

    public static final int CONNECT_BY_JVMP = 4;
    public static final int CONNECT_BY_CLOUDSEE2 = 3;
    public static final int CONNECT_BY_ALL_TURN = 1;
    public static final int CONNECT_BY_CLOUDSEE = 0;


    public static final int  CALL_SDK_JVMP_PLAYBACK =  0x104;  //  时间戳回调

//    jmvp回调

    public static final int  CALL_JVMP_PLAYBACK_TIMESTAMP =  0x104;  //  时间戳回调


    private static final String REC_ALARM = "A";//65,报警录像 
    private static final String REC_CHFRAM = "C";//67,抽帧录像 
    private static final String REC_NOCONNECT = "D";//68,断开时录像 （没有客户端连接设备时，设备录像）
    private static final String REC_MOTION = "M";//77,移动侦测录像 
    private static final String REC_NORMAL = "N";//78,正常录像 
    private static final String REC_TIME = "T";//84，定时录像
    private static final String REC_IVP = "I";//105,智能视频分析录像


    //云视通1.0配置信息全，封装的map增加一个key用来存储原数据
    public static final String STR_CLOUDSEE_HWINFO_OBJ = "cloudsee_hwinfo_obj";
    //云视通1.0SD卡信息，封装的map增加一个key用来存储原数据
    public static final String STR_CLOUDSEE_SDINFO_OBJ = "cloudsee_sdinfo_obj";


    //实时视频what--协议共用的回调（原始回调）
    public static final int WHAT_CONNECT_CHANGE = 0xA1;//视频连接回调
    public static final int WHAT_NORMAL_DATA = 0xA2;//O帧回调 device_type：设备类型  is_jfh：是否带帧头（回放使用）
    public static final int WHAT_NEW_PICTURE = 0xA9;//I帧
    public static final int WHAT_CALL_DOWNLOAD = 0xA6;//回放文件下载
    public static final int WHAT_REMOTE_PLAY_DATA = 0xA7;//回放视频数据
    public static final int WHAT_CALL_STAT_REPORT = 0xAA;//数据统计,



    //回放视频what=0xA7，视频数据 arg2
    public final static int JVN_DATA_I = 0x01;// 视频I帧 收到视频帧，当前帧数进度+1
    public final static int JVN_DATA_B = 0x02;// 视频B帧 收到视频帧，当前帧数进度+1
    public final static int JVN_DATA_P = 0x03;// 视频P帧 收到视频帧，当前帧数进度+1
    public final static int JVN_DATA_A = 0x04;// 音频
    public final static int JVN_DATA_S = 0x05;// 帧尺寸
    public final static int JVN_DATA_OK = 0x06;// 视频帧收到确认
    public final static int JVN_DATA_DANDP = 0x07;// 下载或回放收到确认
    public final static int JVN_DATA_O = 0x08;// 其他自定义数据 视频O帧 total 文件总帧数
    public final static int JVN_DATA_SKIP = 0x09;// 视频S帧
    public static final int ARG2_REMOTE_PLAY_OVER = 0x32;// 回放结束
    public static final int ARG2_REMOTE_PLAY_ERROR = 0x39;// 回放出错
    public static final int ARG2_REMOTE_PLAY_TIMEOUT = 0x77;// 回放超时




    //自定义回调
    public static final int WHAT_CHAT = 0xFF1;//对讲回调
    public static final int WHAT_REMOTE_PRECISE_FILE_DATE_LIST = 0xFF2;//精准回放  回放有录像日期列表
    public static final int WHAT_REMOTE_PRECISE_FILE_LIST = 0xFF3;//精准回放  回放文件列表



    /************************* 视频连接结果回调 -start **********************************/

    /* 云视通1.0  云视通2.0  流媒体协议  连接结果 */
    // 视频连接线程断开
    public static final int REAL_DISCONNECTED = -0x03;
    public static final int CCONNECTTYPE_UNCNOW = 0;     //
    public static final int CCONNECTTYPE_CONNOK = 1;     //连接成功
    public static final int CCONNECTTYPE_CONNOK_NO_LIVE = 101;   //只连接不返回实时流，连接成功
    public static final int CCONNECTTYPE_DISOK = 2;     //断开连接成功
    public static final int CCONNECTTYPE_DISOK2 = -3;     //断开连接成功
    public static final int CCONNECTTYPE_RECONN = 3;     //不必重复连接
    public static final int CCONNECTTYPE_CONNERR = 4;     //连接失败
    public static final int CCONNECTTYPE_NOCONN = 5;     //没连接
    public static final int CCONNECTTYPE_DISCONNE = 6;     //连接异常断开（全转发，云视通）
    public static final int CCONNECTTYPE_SSTOP = 7;     //服务停止，连接断开
    public static final int CCONNECTTYPE_DISF = 8;     //断开连接失败
    public static final int CCONNECTTYPE_NET_TIMEOUT = 9;     //连接超时(全转发协议)
    public static final int CCONNECTTYPE_PWDERR = 10;     //用户名密码错误(全转发协议)
    public static final int CCONNECTTYPE_UNKONW_CONNECT = 11;     //没有告诉全转发服务器设备号(全转发协议)
    public static final int CCONNECTTYPE_NO_ONLINE = 12;     //服务器通知手机:设备不在线(全转发协议)
    public static final int CCONNECTTYPE_OFFLINE = 13;     //服务器通知手机:设备掉线了(全转发协议)
    public static final int CCONNECTTYPE_WAKEUP_DEVICE_TIMEOUT = 14;     //唤醒设备超时(全转发协议)
    public static final int CCONNECTTYPE_DEVICE_REJECT_CONNECT = 15;     //设备拒绝连接(全转发协议)
    //oct
    public static final int CCONNECTTYPE_ERROR = 16;     // 未知错误
    public static final int CCONNECTTYPE_INVALID_PARAM = 17;     // 无效参数
    public static final int CCONNECTTYPE_INVALID_HANDLE = 18;     // 无效的句柄
    public static final int CCONNECTTYPE_USER_ABORT = 19;     // 用户中断请求
    public static final int CCONNECTTYPE_PARSE_ADDR_FAILED = 20;     // 地址解析失败
    public static final int CCONNECTTYPE_CREATE_SOCKET_FAILED = 21;     // 创建套接字失败
    public static final int CCONNECTTYPE_SYSTEM_CALL_FAILED = 22;     // 系统调用失败
    public static final int CCONNECTTYPE_CONNECT_TIMEOUT = 23;     // 连接超时
    public static final int CCONNECTTYPE_CONNECT_FAILED = 24;     // 连接失败
    public static final int CCONNECTTYPE_USER_VERIFY_FAILED = 25;     // 用户验证失败
    public static final int CCONNECTTYPE_CONNECTION_ABORT = 26;     // 连接异常断开
    public static final int CCONNECTTYPE_ALLOC_MEMORY_FAILED = 27;     // 内存分配失败
    public static final int CCONNECTTYPE_TIMEOUT = 28;     // 操作超时
    public static final int CCONNECTTYPE_ALLOC_CONTEXT_FAILED = 29;     // 分配上下文失败
    public static final int CCONNECTTYPE_START_THREAD_FAILED = 30;     // 启动工作线程失败
    public static final int CCONNECTTYPE_INIT_RES_FAILED = 31;    // 初始化资源失败
    public static final int CCONNECTTYPE_UNINIT_RES = 32;     // 未初始化的资源
    public static final int CCONNECTTYPE_SERIAL_DATA_FAILED = 33;     // 序列化数据失败
    public static final int CCONNECTTYPE_PARSE_DATA_FAILED = 34;     // 解析数据失败
    public static final int CCONNECTTYPE_SEND_DATA_FAILED = 35;     // 发送数据失败
    public static final int CCONNECTTYPE_RECV_DATA_FAILED = 36;     // 接收数据失败
    public static final int CCONNECTTYPE_BIND_ADDR_FAILED = 37;     // 绑定地址失败
    public static final int CCONNECTTYPE_BIND_SERVICE_FAILED = 38;     // 绑定网络传输服务失败
    public static final int CCONNECTTYPE_REPEAT_CALL = 39;     // 重复调用
    public static final int CCONNECTTYPE_INVALID_UOID = 40;     // 无效的设备证书
    public static final int CCONNECTTYPE_INVALID_EID = 41;     // 无效的设备识别码
    public static final int CCONNECTTYPE_DEV_NOT_ONLINE = 42;     // 设备未上线
    public static final int CCONNECTTYPE_SERVER_ERROR = 43;     // 服务器错误
    public static final int CCONNECTTYPE_PEER_NOT_READY = 44;     // 对端未就绪
    public static final int CCONNECTTYPE_PEER_HAVE_NO_RES = 45;     // 对端没有足够资源
    public static final int CCONNECTTYPE_SERVER_REJECT_DEV_ONLINE = 46;   // 服务器拒绝设备上线
    public static final int CCONNECTTYPE_SERVER_ABORT_ONLINE = 47;     // 服务器中断设备上线状态
    public static final int CCONNECTTYPE_OPEN_FILE_FAILED = 48;     // 打开文件失败
    public static final int CCONNECTTYPE_OVER_LIMIT = 49;     // 超过限制
    public static final int OCT_ERRORCODE_RELAY_CONN_DURATION_LIMITED = 50;     // 到达转发服务器单次连接时间限制
    public static final int OCT_ERRORCODE_NO_SUCH_STREAM = 51;     // 通道号或者码流号不对
    public static final int CCONNECTTYPE_NO_DEV_INFO = 52;     // 没有设置设备信息
    public static final int CCONNECTTYPE_DIFFERENT_ENCRYPT_TYPE = 53;     // 加密类型不匹配
    public static final int CCONNECTTYPE_CLOUD_TURN = 56;     // 云转发
    public static final int CCONNECTTYPE_CHANNEL_OFFLINE = 57;     // 通道不在线


    public static final int DEVICE_TYPE_UNKOWN = -1;
    public static final int DEVICE_TYPE_DVR = 0x01;
    public static final int DEVICE_TYPE_950 = 0x02;
    public static final int DEVICE_TYPE_951 = 0x03;
    public static final int DEVICE_TYPE_IPC = 0x04;
    public static final int DEVICE_TYPE_NVR = 0x05;
    public static final int JAE_ENCODER_SAMR = 0x00;
    public static final int JAE_ENCODER_ALAW = 0x01;
    public static final int JAE_ENCODER_ULAW = 0x02;
    public static final int JAE_ENCODER_G729 = 0x03;
    public static final int ENC_PCM_SIZE = 320;
    public static final int ENC_AMR_SIZE = 640;
    public static final int ENC_G711_SIZE = 640;
    public static final int ENC_G729_SIZE = 960;



    public static final int INT_REC_NORMAL = 78;//78,正常录像
    public static final int INT_REC_TIME = 84;//84，定时录像
    public static final int INT_REC_MOTION = 77;//77,移动侦测录像
    public static final int INT_REC_ALARM = 65;//65,报警录像
    public static final int INT_REC_STOP_ALARM = 68;//68,停止录像
    public static final int INT_REC_ONE_MIN = 79;//79,一分钟录像
    public static final int INT_REC_CHFRAME = 67;//67,抽帧录像
    public static final int INT_REC_SMART = 73;//73,区域检测报警
}
