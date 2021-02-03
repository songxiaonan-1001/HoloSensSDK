package com.huawei.holosens.consts;

public class NativeCbConsts {

    /* 接口调用错误码 */
    public static final int HOLOSENS_ERRORCODE_NOERROR = 0;    /*!< 无错误 */
    public static final int HOLOSENS_ERRORCODE_FAILED = -1;    /*!< 调用失败 */
    public static final int HOLOSENS_ERRORCODE_TIMEOUT = -2;    /*!< 调用超时 */


    /* 好望客户端连接错误码 */
    public static final int HCE_CONNECTED = 0;         /*!< 已连接                                      */
    public static final int HCE_CONNECT_FAILED = 1;           /*!< 连接失败                                    */
    public static final int HCE_CONNECTION_LIMIT = 2;         /*!< 连接限制（p2p连接时，设备连接达到上线了）   */
    public static final int HCE_CONNECTION_BROKEN = 3;        /*!< 连接中断（网络异常或服务中断）              */
    public static final int HCE_DISCONNECTED = 4;          /*!< 正常断开（主动调用断开）                    */
    public static final int HCE_MAX = 5;


    /* 视频以及解码状态 */
    public static final int HVE_NONE = 0;            /*!< 无数据 */
    public static final int HVE_LOADING = 1;           /*!< 正在缓冲 */
    public static final int HVE_DECODE_FAILED = 2;         /*!< 解码失败 */
    public static final int HVE_DECODE_SUCCESS = 3;         /*!< 解码成功，收到解码成功后即可调用show接口来预览图像 */
    public static final int HVE_MAX = 0;




    //回调event_type定义
//    public static final int EVENT_TYPE_HPET_PLAY = 0;						/*!< 播放事件，含直播和回放，事件状态详见： holosens_play_state_e，无json数据*/
//    public static final int EVENT_TYPE_HPET_RECORD_DATES_QUERY = 1;			/*!< 录像日期列表查询，详细暂未定义 */
//    public static final int EVENT_TYPE_HPET_RECORDS_QUERY = 2;					/*!< 录像查询，详细暂未定义 */
//    public static final int EVENT_TYPE_HPET_PLAYBACK = 3;					/*!< 回放事件 */

    //回调event_type定义
    public static final int EVENT_TYPE_HPET_PLAY = 0;						/*!< 播放事件，含直播和回放，事件状态详见： holosens_play_state_e，无json数据*/
    public static final int EVENT_TYPE_HPET_RECORD_DATES_QUERY=1;			/*!< 录像日期列表查询，详见json定义，method：player_record_dates_query */
    public static final int EVENT_TYPE_HPET_RECORDS_QUERY=2;					/*!< 录像查询，详见json定义，method：player_records_query */
    public static final int EVENT_TYPE_HPET_PLAY_TIME_POS=3;					/*!< 播放时间点（回放），详见json定义，method：player_play_time_pos */
    public static final int EVENT_TYPE_HPET_PLAY_FILE_POS=4;					/*!< 播放文件位置（回放），详见json定义，method：player_play_file_pos */
    public static final int EVENT_TYPE_HPET_RECORD=5;						/*!< 录像事件 */
    public static final int EVENT_TYPE_HPET_INTERCOM=6;						/*!< 语音对讲事件 */


    //回调event_state定义
    public static final int EVENT_STATE_HPS_NONE = 0;						/*!< 无状态 */
    public static final int EVENT_STATE_HPS_CONNECTED = 1;						/*!< 已连接 */
    public static final int EVENT_STATE_HPS_CONNECT_FAILED = 2;					/*!< 连接失败 */
    public static final int EVENT_STATE_HPS_CONNECTION_LIMIT = 3;				/*!< 连接限制（p2p连接时，设备连接达到上线了） */
    public static final int EVENT_STATE_HPS_CONNECTION_BROKEN = 4;				/*!< 连接中断（网络异常或服务中断） */
    public static final int EVENT_STATE_HPS_VIDEO_LOADING = 5;					/*!< 正在缓冲 */
    public static final int EVENT_STATE_HPS_VIDEO_DECODE_FAILED = 6;			/*!< 解码失败 */
    public static final int EVENT_STATE_HPS_VIDEO_DECODE_SUCCESS = 7;			/*!< 解码成功，收到解码成功后即可调用show接口来预览图像 */

    //回调event_state定义 录像状态对应EVENT_TYPE_HPET_RECORD
    public static final int EVENT_STATE_HRS_NONE = 0;						/*!< 无状态 */
    public static final int EVENT_STATE_HRS_START_RECORD=1;					/*!< 开始录像 */
    public static final int EVENT_STATE_HRS_CREATE_PACKAGE_OK=2;				/*!< 创建录像文件成功，收到json数据，详见：player_record */
    public static final int EVENT_STATE_HRS_CREATE_PACKAGE_FAILED=3;			/*!< 创建录像文件失败 */
    public static final int EVENT_STATE_HRS_CLOSE_PACKAGE_OK=4;				/*!< 关闭录像文件成功，收到json数据，详见：player_record */
    public static final int EVENT_STATE_HRS_CLOSE_PACKAGE_FAILED=5;			/*!< 关闭录像文件失败 */
    public static final int EVENT_STATE_HRS_WRITE_FAILED=6;					/*!< 写入失败 */
    public static final int EVENT_STATE_HRS_STORE_THE_WARNING=7;				/*!< 存储空间不足*/
    public static final int EVENT_STATE_HRS_STOP_RECORD=8;					/*!< 关闭录像 */

}
