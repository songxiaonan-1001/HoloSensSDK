package com.huawei.holosens.live.play.util;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import com.huawei.holosens.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息声音工具类
 */
public class MsgSoundUtil {

    private static final String TAG = "MsgSoundUtil";
    /**
     * 声音编号
     */
    public static final int MSG_SOUND_ALARM = 1;
    public static final int MSG_SOUND_CAT = 2;
    private Context mContext;
    private SoundPool sp;
    private Map<Integer, Integer> map;
    private int streamID;
    private boolean isPause;
    private boolean isLoadComplete;

    public MsgSoundUtil(Context context) {
        mContext = context;
        initSoundpool();
        initSounds();
    }

    /**
     * 初始化
     */
    private void initSoundpool() {
        // 参数依次为:最多几个资源/资源类型/srcQuality暂无意义 默认用0
        //当前系统的SDK版本大于等于21(Android 5.0)时
        if(Build.VERSION.SDK_INT > 21){
            SoundPool.Builder builder = new SoundPool.Builder();
            //传入音频数量
            builder.setMaxStreams(5);
            //AudioAttributes是一个封装音频各种属性的方法
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            //设置音频流的合适的属性
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_RING);//STREAM_MUSIC
            //加载一个AudioAttributes
            builder.setAudioAttributes(attrBuilder.build());
            sp = builder.build();
        }else{
            sp = new SoundPool(5,// 同时播放的音效
                    AudioManager.STREAM_RING, 5);
        }
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool arg0, int arg1, int status) {
                Log.v("MsgSoundUtil", "onLoadComplete");
                isLoadComplete = true;
            }
        });

    }

    private void initSounds() {
        map = new HashMap<Integer, Integer>();
        map.put(MSG_SOUND_ALARM, sp.load(mContext, R.raw.alarm, 0));
        map.put(MSG_SOUND_CAT, sp.load(mContext, R.raw.cat, 0));
    }

    /**
     * @param sound  文件
     * @param number 循环次数 0是不循环，-1是永远循环
     */
    public void play(int sound, int number) {
        AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);// 实例化
        float audioMaxVolum = am.getStreamMaxVolume(AudioManager.STREAM_RING);// 报警声同铃声的大小 音效最大值
        float audioCurrentVolum = am.getStreamVolume(AudioManager.STREAM_RING);

        Log.d(TAG,"audioCurrentVolum="+audioCurrentVolum+";audioMaxVolum="+audioMaxVolum);
        final float audioRatio = audioCurrentVolum / audioMaxVolum;
        Log.d(TAG,"audioRatio="+audioRatio + ", loadComplete=" + isLoadComplete);
        /**
         * 设置延时播放声音,如果立即播放.此时音频文件可能还没有加载完成<br/>
         * 这种情况会没有声音播放(问题在小米手机上必现).
         */
        try {
            if (!isLoadComplete) {
                Thread.sleep(1000);
                play(sound, number, audioRatio);
            }else {
                play(sound, number, audioRatio);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "start play");
    }

    private void play(int sound, int number, float audioRatio) {
        streamID = sp.play(map.get(sound),
                audioRatio,// 左声道音量
                audioRatio,// 右声道音量
                1, // 优先级
                number,// 循环播放次数(0是不循环，-1是永远循环)
            1f);// 回放速度，该值在0.5-2.0之间 1为正常速度
        Log.d(TAG, "play: res=" + streamID);
}

    /**
     * 暂停
     */
    public void pause() {
        sp.pause(streamID);
        isPause = true;
    }

    /**
     * 释放
     */
    public void release() {
        sp.stop(streamID);
        sp.release();
    }

    public void resume() {
        sp.resume(streamID);
        isPause = false;
    }

    public boolean isPause() {
        return isPause;
    }

}
