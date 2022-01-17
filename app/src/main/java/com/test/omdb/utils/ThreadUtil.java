package com.test.omdb.utils;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;

/**
 * Thread related utility functions.
 */
public final class ThreadUtil {

    private static volatile Handler handler;

    private ThreadUtil() {}

    private static Handler getHandler() {
        if (handler == null) {
            synchronized (ThreadUtil.class) {
                if (handler == null) {
                    handler = new Handler(Looper.getMainLooper());
                }
            }
        }
        return handler;
    }

    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static void runOnMain(final @NonNull Runnable runnable) {
        if (isMainThread()) runnable.run();
        else getHandler().post(runnable);
    }

    public static void assertMainThread() {
        if (!isMainThread()) {
            throw new AssertionError("Must run on main thread.");
        }
    }

}
