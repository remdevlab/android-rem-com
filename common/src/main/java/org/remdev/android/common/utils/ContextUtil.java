package org.remdev.android.common.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by satalin on 8/10/17.
 */

public class ContextUtil {
    public static Context getAppContext() {
        Context result = null;
        try {
            result = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null, (Object[]) null);
            if (result == null) {
                result = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null, (Object[]) null);
            }
        } catch (Exception ignore) {
        }
        return result;
    }
}
