/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huawei.holosens.utils;

import android.os.HandlerThread;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackgroundHandler {

    // HandlerThread对象
    static HandlerThread sLooperThread;
    // 线程池ExecutorService对象
    static ExecutorService mThreadPool;

    static {
        /*
          a.应用层调用底层的方法,底层回调应用层的方法.
          b.底层回调应用层时会在一个线程中,在这个线程不能做耗时处理,所以应用层自己开个线程去处理
         */
        sLooperThread = new HandlerThread("BackgroundHandler", HandlerThread
                .MIN_PRIORITY);
        sLooperThread.start();
        mThreadPool = Executors.newCachedThreadPool();
    }

    private BackgroundHandler() {
    }

    public static void execute(Runnable runnable) {
        mThreadPool.execute(runnable);
    }

    public static Looper getLooper() {
        return sLooperThread.getLooper();
    }

    // ---------------------------------------------------
    // ## 扩展
    // ---------------------------------------------------
}
