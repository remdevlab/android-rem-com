package org.remdev.android.common.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Alexandr.Salin on 1/28/16.
 */
public class CommonUtils {

    public static boolean isEmpty(Collection<?> collection) {
        boolean result = false;
        if (collection == null || collection.isEmpty()) {
            result = true;
        }
        return result;
    }

    public static <T> Class<T> getGenericClass(Collection<T> collection) {
        if (isEmpty(collection)) {
            throw new IllegalArgumentException("add() " + ((collection == null) ? "null" : "empty") + " collection");
        }
        return (Class<T>) collection.iterator().next().getClass();

    }

    public static <T> List<T> getNewArrayList() {
        return new ArrayList<>();
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                Log.e("CommonUtils", "Can't close resource", e);
            }
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    public static boolean isServiceRunning(Context ctx, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
