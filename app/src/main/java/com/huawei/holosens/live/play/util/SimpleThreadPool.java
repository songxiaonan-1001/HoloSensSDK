package com.huawei.holosens.live.play.util;

import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 线程池
 *
 */
public class SimpleThreadPool {
    protected static final String TAG = "multi";
    private static CustomExecutorService mThreadPool;

    static {
        mThreadPool = new CustomExecutorService();
    }

    /**
     * 根据不同的网络环境计算最优的线程泄核心线程数量
     *
     * @param info
     */
    public static void adjustThreadCount(NetworkInfo info) {
        mThreadPool.adjustThreadCount(info);
    }

    /**
     * 执行任务
     *
     * @param runnable
     */
    public static void execute(Runnable runnable) {
        try {
            mThreadPool.execute(runnable);
        } catch (RejectedExecutionException e) {
            Log.e(TAG, "线程池中的任务已满！");
            // 线程已满的情况, 通过开新线程的方式
            new Thread(runnable).start();
        } catch (Exception e) {
            Log.e(TAG, "线程池异常！");
        }
    }

    /**
     * 执行任务
     *
     * @param glassNo  玻璃号
     * @param runnable 任务
     */
    public static void execute(int glassNo, Runnable runnable) {
        try {
            mThreadPool.execute(runnable);
        } catch (RejectedExecutionException e) {
            Log.e(TAG, "[玻璃号:" + glassNo + "] 线程池中的任务已满！");
            // 线程已满的情况, 通过开新线程的方式
            new Thread(runnable).start();
        } catch (Exception e) {
            Log.e(TAG, "线程池异常！");
        }
    }

    /**
     * 从线程池中移除任务
     */
    public static void remove(Runnable runnable) {
        mThreadPool.remove(runnable);
    }

    public static void shutdownAndAwaitTermination() {
        mThreadPool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!mThreadPool.awaitTermination(1, TimeUnit.SECONDS)) {
                Log.e(TAG, "pool not close, try shutdownNow.");
                mThreadPool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!mThreadPool.awaitTermination(1, TimeUnit.SECONDS)) {
                    Log.e(TAG, "pool did not terminate.");
                }
            } else {
                Log.v(TAG, "pool has closed.");
            }
        } catch (InterruptedException ie) {
            Log.e(TAG, "pool InterruptedException.");
            // (Re-)Cancel if current thread also interrupted
            mThreadPool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 记录异常信息
     *
     * @param tag 日志标识
     * @param e   异常
     */
    public static void printError(String tag, Exception e) {
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            // 将出错的栈信息输出到printWriter中
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (pw != null) {
                pw.close();
            }
        }

        Log.e(tag, "printError, Throws Exception:" + sw.toString());
    }
}
