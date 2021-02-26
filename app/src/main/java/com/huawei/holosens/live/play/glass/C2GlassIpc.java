package com.huawei.holosens.live.play.glass;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.huawei.holobasic.consts.AppConsts;
import com.huawei.holosens.bean.Channel;
import com.huawei.holosens.bean.Device;
import com.huawei.holosens.consts.JVEncodedConst;
import com.huawei.holosens.live.play.bean.Glass;
import com.huawei.holosens.live.play.player.IPlayHelper;
import com.huawei.holosens.live.play.ui.WindowFragment;
import com.huawei.holosens.utils.JniUtil;
import com.huawei.holosens.utils.ToastUtils;

import org.json.JSONObject;

/**
 * 云视通协议2.0_摄像机
 *
 * @author CSV
 */

public class C2GlassIpc extends BaseGlassC2 {

    private static final String TAG = "C2GlassIpc";

    /**
     * @param window          窗户
     * @param glassView       玻璃
     * @param glassSize       玻璃尺寸
     * @param selectedGlassNo 当前选中的玻璃号
     * @param visibleToUser   用户是否可以看到当前的玻璃
     * @param isEdit          是否编辑状态
     */
    public C2GlassIpc(WindowFragment window, View glassView, Glass.Size glassSize, int
            selectedGlassNo, boolean visibleToUser, boolean isEdit) {
        super(window, glassView, glassSize, selectedGlassNo, visibleToUser, isEdit);
    }

    // --------------------------------------------------
    // # 底层回调
    // --------------------------------------------------

    /**
     * 处理底层回调
     *
     * @param what
     * @param glassNo 玻璃号
     * @param result
     * @param obj
     */
    @Override
    public void handleNativeCallback(int what, int glassNo, int result, Object obj) {

        try {
            Channel channel = mGlass.getChannel();
            Device device = channel.getParent();

            switch (what) {
                case JVEncodedConst.WHAT_CONNECT_CHANGE:// 连接改变
                    Log.e(TAG, "CALL_CONNECT_CHANGE=0xA1:glassNo=" + glassNo + ";result=" + result + ";obj=" + (obj == null ? "null" : obj.toString()));

                    switch (result) {
                        //56 标记走到云转发，接下来会回调1连接成功
                        case JVEncodedConst.CCONNECTTYPE_CLOUD_TURN:
                            channel.setTurn(true);
                            break;
                        // 1 -- 连接成功
                        case JVEncodedConst.CCONNECTTYPE_CONNOK:
                            Log.e(TAG, "1:连接成功");
                            mPlayerHelper.setConnectState(IPlayHelper.buffering1);
                            update(IPlayHelper.buffering1, 0);

                            if (null != obj) {
                                try {
                                    JSONObject connectObj = new JSONObject(obj.toString());
                                    String msg = connectObj.optString("msg");
                                    if (!TextUtils.isEmpty(msg)){
                                        JSONObject msgObj = new JSONObject(msg);
                                        int subStreamNum = msgObj.optInt("sub_stream_num");
                                        if (subStreamNum <= 0) {
                                            subStreamNum = 3;
                                        }
//                                    subStreamNum = 3;

                                        device.setSubStreamNum(subStreamNum);

                                        channel.getParent().setSubStreamNum(subStreamNum);
                                        mGlass.setChannel(channel);
                                    }



                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            break;
                        // 2和-3 断开连接成功
                        case JVEncodedConst.REAL_DISCONNECTED:
                        case JVEncodedConst.CCONNECTTYPE_DISOK: {
                            Log.e(TAG, "2:断开连接成功");
                            if(!mGlass.isManualDisconnect())        //不是手动断开
                            {
                                update(IPlayHelper.connectFailed, mConnectStateArray[1]);
                            }
                            break;
                        }
                        //25.用户身份验证失败
                        case JVEncodedConst.CCONNECTTYPE_USER_VERIFY_FAILED: {

                            int code = result - 16;
                            mPlayerHelper.setConnectState(IPlayHelper.connectFailed);
                            update(IPlayHelper.connectFailed, mConnectStateArray[code]);
                            mPlayerHelper.disconnect();
                            break;
                        }
                        default:
                            int code = result - 16;
                            Log.e(TAG, mConnectStateArray[code]);
                            mPlayerHelper.setConnectState(IPlayHelper.connectFailed);
                            update(IPlayHelper.connectFailed, mConnectStateArray[code]);
                            mPlayerHelper.disconnect();
                            break;
                    }
                    break;
                case  JVEncodedConst.WHAT_NORMAL_DATA:// o帧

                    Log.e(TAG, "o帧");
                    mPlayerHelper.setConnectState(IPlayHelper.buffering2);
                    update(IPlayHelper.buffering2, 0);

                    Log.v(TAG, "898098778899-O-jdlfjsjd-Glass-CALL_NORMAL_DATA=0xA2;what=" + what + ";arg1=" + glassNo + ";arg2=" + result + ";streamTag=" + mChannel.getStreamTag()
                            + ";subStreamNum=" + mChannel.getParent().getSubStreamNum());

                    Log.v(TAG, "CALL_NORMAL_DATA=0xA2;what=" + what + ";arg1=" + glassNo + ";arg2=" + result + ";obj=" + (obj == null ? "null" : obj.toString()));

                    // 窗户上玻璃数量超过4, 只发关键帧;否则, 反之.
//                    if (mWindow.getGlassCount() > 4 && !mPlayerHelper.isSendKeyFrame()) {
//                        mPlayerHelper.sendKeyFrameOnly();
//                    } else if (mWindow.getGlassCount() <= 4 && mPlayerHelper.isSendKeyFrame()) {
//                        mPlayerHelper.sendFullFrame();
//                    }

                    JSONObject jObj = new JSONObject(obj.toString());
                    int type = jObj.optInt("device_type");
                    mDevice.setType(type);
                    mDevice.setJFH(jObj.getBoolean("is_jfh"));
                    mDevice.set05(jObj.getBoolean("is05"));
                    mChannel.setAudioType(jObj.getInt("audio_type"));
                    mChannel.setAudioByte(jObj.getInt("audio_bit"));
                    mChannel.setAudioEncType(jObj.getInt("audio_enc_type"));
                    mChannel.setVideoEncType(jObj.getInt("video_encType"));
                    mChannel.setWidth(jObj.getInt("width"));
                    mChannel.setHeight(jObj.getInt("height"));

                    if(AppConsts.DEBUG_STATE){
                        ToastUtils.show(mActivity, "video_encType="+jObj.getInt("video_encType"));
                    }

                    break;
                case JVEncodedConst.WHAT_NEW_PICTURE:// I帧
                    JniUtil.show(glassNo, mGlassSurfaceView.getHolder());
                    Log.v(TAG, "898098778899-I-CALL_NEW_PICTURE=0xA9;what=" + what + ";arg1=" + glassNo + ";arg2=" + result);
                    Log.e(TAG, "I帧");
                    /*if (mWindow.getGlassCount() > 4 && !mPlayerHelper.isSendKeyFrame()) {
                        mPlayerHelper.sendKeyFrameOnly();
                    } else if (mWindow.getGlassCount() <= 4 && mPlayerHelper.isSendKeyFrame()) {
                        mPlayerHelper.sendFullFrame();
                    }*/

                    mPlayerHelper.setConnectState(IPlayHelper.connected);
                    update(IPlayHelper.connected, 0);

                    setPlaySize();

                    break;

                default:
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}