package com.huawei.holosens.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.huawei.holobasic.consts.AppConsts;
import com.huawei.holobasic.play.IHandlerLikeNotify;
import com.huawei.holobasic.utils.FileUtil;
import com.huawei.holobasic.utils.MySharedPreference;
import com.huawei.holobasic.utils.ScreenUtils;
import com.huawei.holosens.consts.NativeCbConsts;
import com.huawei.holosens.live.play.event.MsgEvent;
import com.huawei.holosens.middleware.callback.NativeCbTs;
import com.huawei.holosens.utils.AppFrontBackHelper;
import com.huawei.holosens.utils.BackgroundHandler;
import com.huawei.holosens.utils.ErrorUtil;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;


public class BaseApplication extends Application {


    public static final long CLICK_DIS_TIME = 500;
    private static final String TAG = "BaseApplication";

    private AppFrontBackHelper mAppHelper;

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private static BaseApplication mAppInstance;

    public static BaseApplication getInstance() {
        return mAppInstance;
    }

    /**
     * 获取全局上下文
     */
    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAppInstance = this;
        context = getApplicationContext();
        initALL();
    }


    private void initALL() {
        MySharedPreference.init(this);
        ScreenUtils.init(this);
        ErrorUtil.getInstance().setmContext(this);
        initBugly();
        initMediaIgnore();


        mAppHelper = new AppFrontBackHelper();
        mAppHelper.register(this, new AppFrontBackHelper.OnAppStatusListener() {
            @Override
            public void onFront() {
                //应用切到前台处理
                //刷新消息未读数量
                MsgEvent event = new MsgEvent();
                event.setMsgTag(MsgEvent.MSG_EVENT_UPDATE_UNREAD_COUNT);
                JSONObject item = new JSONObject();
                try {
                    item.put("refresh", true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                event.setAttachment(item.toString());
                EventBus.getDefault().post(event);
            }

            @Override
            public void onBack() {
                //应用切到后台处理
            }
        });
    }


    private void initBugly() {
//        if(MySharedPreference.getBoolean(MySharedPreferenceKey.FIRST_SHOW_PERMISSION_DIALOG, true))
//            MySharedPreference.putBoolean(MySharedPreferenceKey.SAFE_SETTING,true);
//        if(MySharedPreference.getBoolean(MySharedPreferenceKey.SAFE_SETTING)) {
//            Log.e(TAG, "open bugly");
            CrashReport.initCrashReport(getApplicationContext(), "e7b09dc3d8", true);
//        }
//        AGConnectCrash.getInstance().enableCrashCollection(true);
    }

    /**
     * 初始化app整个缓存目录
     * 增加android .nomedia
     * 防止被相册扫描到
     */
    private void initMediaIgnore() {
        FileUtil.createDirectory(AppConsts.APP_PATH);
        File ignoreFile = new File(AppConsts.APP_PATH + ".nomedia");
        if (ignoreFile.exists() && ignoreFile.isFile()) {
            return;
        }

        try {
            ignoreFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --------------------------------------------------------
    // 接收底层回调设置 start
    // --------------------------------------------------------
    private static Handler mHandler = new Handler(BackgroundHandler.getLooper()) {
        @Override
        public void handleMessage(Message msg) {
            IHandlerLikeNotify nativeCallback = mAppInstance.getCurrentNotifier();
            if (null == nativeCallback) {
                Log.e(TAG, "null notifier");
                return;
            }

            nativeCallback.onNotify(msg.what, msg.arg1, msg.arg2, msg.obj);

        }
    };

    @Override
    public void onTerminate() {
        super.onTerminate();
        mAppHelper.unRegister(this);
    }

    private WeakReference<IHandlerLikeNotify> mNotifyWeakReference;

    /**
     * 获取currentNotifier
     *
     * @return
     */
    public IHandlerLikeNotify getCurrentNotifier() {
        return mNotifyWeakReference.get();
    }

    /**
     * 修改当前显示的 Activity/Fragment 引用
     *
     * @param currentNotifier
     */
    public void setCurrentNotifier(IHandlerLikeNotify currentNotifier) {
        Log.e(TAG, "setCurrentNotifier: " + currentNotifier.getClass().getName());
        mNotifyWeakReference = new WeakReference<>(currentNotifier);
    }
    // --------------------------------------------------------
    // 接收底层回调设置 end
    // --------------------------------------------------------


    /**
     * 新播放库
     */

    /**
     * 修改当前显示的 Activity 引用
     *
     * @param currentNotify
     */


    public HashMap<Integer, Integer> playerIdWindowMap = new HashMap<Integer, Integer>();//

    public HashMap<Integer, Integer> getPlayerIdWindowMap() {
        return playerIdWindowMap;
    }

    public void setPlayerIdWindowMap(HashMap<Integer, Integer> playerIdWindowMap) {
        this.playerIdWindowMap = playerIdWindowMap;
    }

    /**
     * (启用)底层所有的回调接口-2020.7.16
     *
     * @param player_id
     * @param event_type
     * @param event_state
     * @param json_data
     */
    void OnEvent(int player_id, int event_type, int event_state, String json_data){

        Log.e(TAG,"OnEvent-player_id="+player_id+";event_type="+event_type+";event_state="+event_state+";json_data="+json_data);

        switch (event_type){
            case NativeCbConsts.EVENT_TYPE_HPET_PLAY:
            case NativeCbConsts.EVENT_TYPE_HPET_PLAY_TIME_POS:{
                int window=-1;
                if(null != playerIdWindowMap && playerIdWindowMap.size()>0) {
                    try {
                        window = playerIdWindowMap.get(player_id);
                    } catch (Exception e) {
                        window = -1;
                        e.printStackTrace();
                    }
                    if(window == -1)
                        return;
                }
                if (null != getCurrentNotifier()) {
                    Log.e(TAG, "window =="+window);
                    NativeCbTs.transmit(getCurrentNotifier(),event_type, window, event_state, json_data);
                } else {
                    Log.e(TAG, "currentNotify is null!");
                }
            }
            break;

            default:
                getCurrentNotifier().onNotify(event_type, player_id, event_state, json_data);
                break;
        }

    }

}
