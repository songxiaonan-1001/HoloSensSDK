package com.huawei.holosens.middleware.callback;

import android.util.Log;

import com.huawei.holobasic.play.IHandlerLikeNotify;
import com.huawei.holosens.consts.NativeCbConsts;

import static com.huawei.holosens.consts.JVEncodedConst.CCONNECTTYPE_CONNOK;
import static com.huawei.holosens.consts.JVEncodedConst.CCONNECTTYPE_DISOK;
import static com.huawei.holosens.consts.JVEncodedConst.WHAT_CONNECT_CHANGE;
import static com.huawei.holosens.consts.JVEncodedConst.WHAT_NEW_PICTURE;
import static com.huawei.holosens.consts.JVEncodedConst.WHAT_NORMAL_DATA;

/**
 * @ProjectName: HoloSensEnterprise
 * @Package: com.huawei.holosens.utils
 * @ClassName: NativeCbTs
 * @Description: java类作用描述      底层回调转发
 * 将holo播放库底层回调 转发为 云视通应用层识别的回调
 * @CreateDate: 2020-08-10 11:12
 * @Version: 1.0
 */
public class NativeCbTs {

    private static final String TAG = "JVMultiPlayActivity";


    public static void  transmit(IHandlerLikeNotify currentNotify, int what, int arg1, int arg2, Object obj) {

        switch (what) {
            case NativeCbConsts.EVENT_TYPE_HPET_PLAY: {//播放事件，含直播和回放
                switch (arg2) {
                    case NativeCbConsts.EVENT_STATE_HPS_NONE: {/*!< 无状态 */
                        break;
                    }
                    case NativeCbConsts.EVENT_STATE_HPS_CONNECTED: {/*!< 已连接 */
                        Log.e(TAG, "已连接 what");
                        currentNotify.onNotify(WHAT_CONNECT_CHANGE, arg1, CCONNECTTYPE_CONNOK, obj);
                        break;
                    }
                    case NativeCbConsts.EVENT_STATE_HPS_CONNECT_FAILED: {/*!< 连接失败 */
                        Log.e(TAG, "连接失败");
                        currentNotify.onNotify(WHAT_CONNECT_CHANGE, arg1, CCONNECTTYPE_DISOK, obj);
                        break;
                    }
                    case NativeCbConsts.EVENT_STATE_HPS_CONNECTION_LIMIT: {/*!< 连接限制（p2p连接时，设备连接达到上线了） */
                        Log.e(TAG, "连接限制（p2p连接时，设备连接达到上线了");
                        break;
                    }
                    case NativeCbConsts.EVENT_STATE_HPS_CONNECTION_BROKEN: {/*!< 连接中断（网络异常或服务中断） */
                        Log.e(TAG, "连接中断（网络异常或服务中断)");
                        currentNotify.onNotify(WHAT_CONNECT_CHANGE, arg1, CCONNECTTYPE_DISOK, obj);
                        break;
                    }
                    case NativeCbConsts.EVENT_STATE_HPS_VIDEO_LOADING: {/*!< 正在缓冲 */
                        Log.e(TAG, "正在缓冲");
                        currentNotify.onNotify(WHAT_NORMAL_DATA, arg1, arg2, obj);
                        break;
                    }
                    case NativeCbConsts.EVENT_STATE_HPS_VIDEO_DECODE_FAILED: {/*!< 解码失败 */
                        Log.e(TAG, "解码失败");
                        break;
                    }
                    case NativeCbConsts.EVENT_STATE_HPS_VIDEO_DECODE_SUCCESS: {/*!< 解码成功，收到解码成功后即可调用show接口来预览图像 */
                        Log.e(TAG, "解码成功，收到解码成功后即可调用show接口来预览图像");
                        currentNotify.onNotify(WHAT_NEW_PICTURE, arg1, arg2, obj);
                        break;
                    }

                    default:

                        break;
                }
                break;
            }
            default:
                currentNotify.onNotify(what, arg1, arg2, obj);
                break;
        }
    }
}
