package com.hnf.guet.comhnfpatent.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2018/3/24.
 */

public class ThreadUtils {
    private static Handler sHandler = new Handler(Looper.getMainLooper());
    //单线程线程池
    private static Executor sExecutor = Executors.newFixedThreadPool(3);

    public static void runOnBackgroundThread(Runnable runnable) {
        sExecutor.execute(runnable);
    }

    /**
     * 在主线程执行runnable
     * @param runnable
     */
    public static void runOnMainThread(Runnable runnable) {
        sHandler.post(runnable);
    }
}
