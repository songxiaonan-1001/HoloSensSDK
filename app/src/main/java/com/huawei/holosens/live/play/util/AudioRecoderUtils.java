package com.huawei.holosens.live.play.util;

import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;

import java.io.File;

/**
 * 录像工具
 * ps:由于开启对讲的时候,底层调用了录音工具,所以应用层不能再调用MediaRecorder的start()方法，
 * 否则会崩溃掉
 */

public class AudioRecoderUtils {

    private String filePath;
    private MediaRecorder mMediaRecorder;
    private final String TAG = "MediaRecord";
    public static final int MAX_LENGTH = 1000 * 60 * 10;// 最大录音时长1000*60*10;

    private OnAudioStatusUpdateListener audioStatusUpdateListener;

    public AudioRecoderUtils() {
        this.filePath = "/dev/null";
    }

    public AudioRecoderUtils(File file) {
        this.filePath = file.getAbsolutePath();
    }

    private long startTime;
    private long endTime;

    /**
     * 开始录音 使用amr格式
     * 录音文件
     *
     * @return
     */
    public void startRecord() {
        // 实例化MediaRecorder对象
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        }

        updateMicStatus();

        /*
           TODO ps:由于开启对讲的时候, 底层调用了录音工具, 所以应用层不能再调用MediaRecorder的start()方法, 否则会崩溃掉
         */

//        try {
//            mMediaRecorder.reset();
//            // setAudioSource/setVedioSource
//            // 设置麦克风
//            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//            // 设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样
//            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
//            /*
//               设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
//             ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
//             */
//            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//            // 准备
//            mMediaRecorder.setOutputFile(filePath);
//            mMediaRecorder.setMaxDuration(MAX_LENGTH);
//            mMediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
//                @Override
//                public void onError(MediaRecorder mr, int what, int extra) {
//                    // 发生错误，停止录制
//                    mMediaRecorder.stop();
//                    mMediaRecorder.release();
//                    mMediaRecorder = null;
//                }
//            });
//            mMediaRecorder.prepare();
//            // 开始
//            mMediaRecorder.start();
//            // AudioRecord audioRecord.
//            // 获取开始时间
//            startTime = System.currentTimeMillis();
//            updateMicStatus();
//            Log.i("ACTION_START", "startTime" + startTime);
//        } catch (IllegalStateException e) {
//            Log.i(TAG, "call startAmr(File mRecAudioFile) failed!" + e.getMessage());
//        } catch (IOException e) {
//            Log.i(TAG, "call startAmr(File mRecAudioFile) failed!" + e.getMessage());
//        } catch (Exception e) {
//        }
    }

    /**
     * 停止录音
     */
    public long stopRecord() {
        if (mMediaRecorder == null) {
            return 0L;
        }

        endTime = System.currentTimeMillis();
        Log.i("ACTION_END", "endTime" + endTime);
        /*
           TODO ps:由于开启对讲的时候, 底层调用了录音工具, 所以应用层不能再调用MediaRecorder的start()方法, 否则会崩溃掉
         */
//        mMediaRecorder.stop();
//        mMediaRecorder.reset();
//        mMediaRecorder.release();
//        mMediaRecorder = null;
        Log.i("ACTION_LENGTH", "Time" + (endTime - startTime));
        return endTime - startTime;
    }

    private final Handler mHandler = new Handler();

    private Runnable mUpdateMicStatusTimer = new Runnable() {
        @Override
        public void run() {
            updateMicStatus();
        }
    };

    /**
     * 更新话筒状态
     */
    private int BASE = 1;
    private int SPACE = 200;// 间隔取样时间

    public void setOnAudioStatusUpdateListener(OnAudioStatusUpdateListener audioStatusUpdateListener) {
        this.audioStatusUpdateListener = audioStatusUpdateListener;
    }

    private void updateMicStatus() {
        if (mMediaRecorder != null) {
//            double ratio = (double) mMediaRecorder.getMaxAmplitude() / BASE;
//            double db = 0;// 分贝
//            if (ratio > 1) {
//                db = 20 * Math.log10(ratio);
//                if (null != audioStatusUpdateListener) {
//                    audioStatusUpdateListener.onUpdate(db);
//                }
//            }

            // TODO 临时实现
            if (null != audioStatusUpdateListener) {
                audioStatusUpdateListener.onUpdate(0);
            }

            mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
        }
    }

    public interface OnAudioStatusUpdateListener {
        void onUpdate(double db);
    }
}