package org.remdev.android.common.utils;

import android.os.Looper;

/**
 * Created by satalin on 6/14/16.
 */
public class ThreadUtil {
    public static void checkAndThrowIfUIThread() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("Called from UI thread.");
        }
    }

    public static void checkAndThrowIfNonUIThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new RuntimeException("Called non from UI thread.");
        }
    }
}
