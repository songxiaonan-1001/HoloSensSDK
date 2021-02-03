package com.huawei.holosens.live.play.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * doInBackground方法带返回值的SimpleTask
 *
 * @param <T>
 */
public abstract class ExtendSimpleTask<T> implements Runnable {

    private static final int STATE_NEW = 0x01;
    private static final int STATE_RUNNING = 0x02;
    private static final int STATE_FINISH = 0x04;
    private static final int STATE_CANCELLED = 0x08;

    private static final int MSG_TASK_DONE = 0x01;
    private static InternalHandler sHandler;
    private volatile T t;

    static {
        sHandler = new InternalHandler(Looper.getMainLooper());
    }

    private Thread mCurrentThread;
    private AtomicInteger mState = new AtomicInteger(STATE_NEW);

    public static void post(Runnable r) {
        sHandler.post(r);
    }

    public static void postDelay(Runnable r, long delayMillis) {
        sHandler.postDelayed(r, delayMillis);
    }

    public static void removeAllTask() {
        sHandler.removeCallbacksAndMessages(null);
    }

    public static void removeCallbacks(Runnable r) {
        sHandler.removeCallbacks(r);
    }

    /**
     * A worker will execute this method in a background thread
     */
    public abstract <T> T doInBackground();

    /**
     * will be called after doInBackground();
     */
    public abstract void onFinish(boolean canceled, T t);

    /**
     * When the Task is Cancelled.
     */
    protected void onCancel() {
    }

    /**
     * Restart the task, just set the state to {@link #STATE_NEW}
     */
    public void restart() {
        mState.set(STATE_NEW);
    }

    @Override
    public void run() {
        // switch state: STATE_NEW => STATE_RUNNING
        if (!mState.compareAndSet(STATE_NEW, STATE_RUNNING)) {
            return;
        }
        mCurrentThread = Thread.currentThread();
        t = doInBackground();
        sHandler.obtainMessage(MSG_TASK_DONE, this).sendToTarget();
    }

    /**
     * check whether this work is canceled.
     */
    public boolean isCancelled() {
        return mState.get() == STATE_CANCELLED;
    }

    /**
     * check whether this work has done
     *
     * @return
     */
    public boolean isDone() {
        return mState.get() == STATE_FINISH;
    }

    public void cancel() {
        if (mState.get() >= STATE_FINISH) {
            return;
        } else {
            // interrupt thread
            if (mState.get() == STATE_RUNNING && null != mCurrentThread) {
                try {
                    mCurrentThread.interrupt();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mState.set(STATE_CANCELLED);
            onCancel();
        }
    }

    private static class InternalHandler extends Handler {

        InternalHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            ExtendSimpleTask work = (ExtendSimpleTask) msg.obj;
            switch (msg.what) {
                case MSG_TASK_DONE:
                    boolean isCanceled = work.isCancelled();
                    work.mState.set(STATE_FINISH);
                    work.onFinish(isCanceled, work.t);
                    break;
                default:
                    break;
            }
        }
    }
}
