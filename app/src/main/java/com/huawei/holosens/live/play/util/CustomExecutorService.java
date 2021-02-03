package com.huawei.holosens.live.play.util;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;

/**
 * 自定义线程池服务
 *
 */

public class CustomExecutorService extends ThreadPoolExecutor {
    private static final String TAG = "multi";
    private static final String CPU_NAME_REGEX = "cpu[0-9]+";
    private static final String CPU_LOCATION = "/sys/devices/system/cpu/";
    private static final int DEFAULT_THREAD_COUNT = 5;// 核心线程
    private static int maxThreadCount;// 最大线程 = cpu * 2 + 1
    private static int bestThreadCount;// 最优线程数
    private static int BLOCK_SIZE = 2;//阻塞队列大小
    private static long KEEP_ALIVE_TIME = 10;//空闲线程超时时间

    static {
        calculateBestThreadCount();
    }

    CustomExecutorService() {
        /*
            排队原则:
        　　 1. 如果运行的线程少于 corePoolSize，则 Executor 始终首选添加新的线程，而不进行排队。
        　　 2. 如果运行的线程等于或多于 corePoolSize，则 Executor 始终首选将请求加入队列，而不添加新的线程。
        　　 3. 如果无法将请求加入队列，则创建新的线程，除非创建此线程超出 maximumPoolSize，在这种情况下，任务将被拒绝。
            注:AbortPolicy -> 直接丢弃新任务，并抛出RejectedExecutionException通知调用者，任务被丢弃
         */
        super(bestThreadCount, maxThreadCount, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(BLOCK_SIZE), new MyThreadFactory(), new AbortPolicy());
    }

    /**
     * 根据网络调整线程池的线程数
     * ps:此方法提取自picasso项目
     *
     * @param info
     */
    void adjustThreadCount(NetworkInfo info) {
        if (info == null || !info.isConnectedOrConnecting()) {
            setCoreThreadCount(DEFAULT_THREAD_COUNT);
            return;
        }
        switch (info.getType()) {
            case ConnectivityManager.TYPE_WIFI:
            case ConnectivityManager.TYPE_WIMAX:
            case ConnectivityManager.TYPE_ETHERNET:
                setCoreThreadCount(bestThreadCount);
                break;
            case ConnectivityManager.TYPE_MOBILE:
                switch (info.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_LTE:  // 4G
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                        int count4G = (bestThreadCount - 1) > 0 ? (bestThreadCount - 1) : 1;
                        setCoreThreadCount(count4G);
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS: // 3G
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        int count3G = (bestThreadCount - 2) > 0 ? (bestThreadCount - 2) : 1;
                        setCoreThreadCount(count3G);
                        break;
                    case TelephonyManager.NETWORK_TYPE_GPRS: // 2G
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        setCoreThreadCount(1);
                        break;
                    default:
                        setCoreThreadCount(DEFAULT_THREAD_COUNT);
                }
                break;
            default:
                setCoreThreadCount(DEFAULT_THREAD_COUNT);
        }
    }

    /**
     * 设置线程数
     * ps:此方法提取自picasso项目
     *
     * @param threadCount 核心线程数量
     */
    private void setCoreThreadCount(int threadCount) {
        setCorePoolSize(threadCount);
    }

    /**
     * 计算最优的线程数
     * ps:此方法提取自glide项目
     *
     * @return
     */
    private static int calculateBestThreadCount() {
        if (bestThreadCount == 0) {
            // We override the current ThreadPolicy to allow disk reads.
            // This shouldn't actually do disk-IO and accesses a device file.
            // See: https://github.com/bumptech/glide/issues/1170
            StrictMode.ThreadPolicy originalPolicy = StrictMode.allowThreadDiskReads();
            File[] cpus = null;
            try {
                File cpuInfo = new File(CPU_LOCATION);
                final Pattern cpuNamePattern = Pattern.compile(CPU_NAME_REGEX);
                cpus = cpuInfo.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File file, String s) {
                        return cpuNamePattern.matcher(s).matches();
                    }
                });
            } catch (Throwable t) {
                if (Log.isLoggable(TAG, Log.ERROR)) {
                    Log.e(TAG, "Failed to calculate accurate cpu count", t);
                }
            } finally {
                StrictMode.setThreadPolicy(originalPolicy);
            }

            int cpuCount = cpus != null ? cpus.length : 0;
            int availableProcessors = Math.max(1, Runtime.getRuntime().availableProcessors());
            bestThreadCount =
                    Math.min(DEFAULT_THREAD_COUNT, Math.max(availableProcessors, cpuCount));
            // 最优线程数
            maxThreadCount = Math.max(availableProcessors, cpuCount) * 2 + 1;
            // TODO 针对自身情况,设置一个最大线程数
            maxThreadCount = 32;
        }
        return bestThreadCount;
    }


    // ---------------------------------------------------------
    // ---------------------------------------------------------
    static class MyThreadFactory implements ThreadFactory {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(@NonNull Runnable r) {
            String threadName = "JV_" + mCount.getAndIncrement();
            return new Thread(r, threadName);
        }
    }
}
